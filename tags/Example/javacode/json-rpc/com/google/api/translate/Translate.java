/**
 * Translate.java
 *
 * Copyright (C) 2007,  Richard Midwinter
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.google.api.translate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Makes the Google Translate API available to Java applications.
 * 
 * @author Richard Midwinter
 * @author Emeric Vernat
 * @author Juan B Cabral,chinese xiatianQQ11602011
 */
public class Translate {

	private static final String ENCODING = "UTF-8";
	private static final String INTERMEDIATE_LANGUAGE = Language.ENGLISH;
	private static final String URL_STRING = "http://translate.google.com/translate_t?langpair=";
	private static final String TEXT_VAR = "&text=";
	private static final int RATE_DELAY = 10; // 以前的值：2000，我得更小了
	
	public static final int proxyHttpType = 1;
	public static final int proxySocketType = 2;
	public static final int proxyFtpType = 3;

	private static boolean rateControl = true;
	private static long lastQueryTime = 0L;

	/**
	 * Are we throttling queries to prevent being blocked? Defaults to true.
	 * 
	 * @return Returns true if throttling query frequency.
	 */
	public static boolean isUsingRateControl() {
		return rateControl;
	}

	/**
	 * Allows turning off rate control
	 * 
	 * @param rateControl
	 *            Turns on rate control if true, off if false.
	 */
	public static void setUsingRateControl(boolean rateControl) {
		Translate.rateControl = rateControl;
	}

	/**
	 * Reads an InputStream and returns its contents as a String. Also effects
	 * rate control.
	 * 
	 * @param inputStream
	 *            The InputStream to read from.
	 * @return The contents of the InputStream as a String.
	 * @throws Exception
	 */
	private static String toString(InputStream inputStream) throws Exception {
		StringBuffer outputBuilder = new StringBuffer();
		try {
			while (rateControl
					&& (lastQueryTime + RATE_DELAY > System.currentTimeMillis())) {
				try {
					Thread.sleep(lastQueryTime + RATE_DELAY
							- System.currentTimeMillis());
				} catch (InterruptedException e) {
				}
			}
			String string;
			if (inputStream != null) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, ENCODING));
				while (null != (string = reader.readLine())) {
					outputBuilder.append(string).append('\n');
				}
			}
			if (rateControl)
				lastQueryTime = System.currentTimeMillis();
		} catch (Exception ex) {
			throw new Exception(
					"[google-api-translate-java] Error reading translation stream.",
					ex);
		}
		return outputBuilder.toString();
	}

	/**
	 * Translates text from a given language to another given language using
	 * Google Translate http://www.google.com/translate_t?hl=zh-CN
	 * 
	 * @param text
	 *            The String to translate.
	 * @param from
	 *            The language code to translate from.
	 * @param to
	 *            The language code to translate to.
	 * @return The translated String.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String translate(String text, String from, String to)
			throws Exception {
		if (Language.CHINESE.equals(from)
				&& Language.CHINESE_TRADITIONAL.equals(to))
			from = Language.CHINESE_SIMPLIFIED;
		if (Language.isValidLanguagePair(from, to)) {
			return retrieveTranslation(text, from, to);
		} else {
			return retrieveTranslation(retrieveTranslation(text, from,
					INTERMEDIATE_LANGUAGE), INTERMEDIATE_LANGUAGE, to);
		}
	}

	/***
	 * 设置代理
	 * @param szProxyServer  代理服务器ip
	 * @param szPort         代理服务器的端口
	 * @param nType          代理服务器的类型：Translate.proxyHttpType,Translate.proxyFtpType,Translate.proxySocketType
	 * @param szUser         代理服务器的用户名，如果不需要验证就用null
	 * @param szPassword     代理服务器的密码，如果不需要验证就用null
	 */
	public static void setProxy(String szProxyServer, String szPort, int nType, String szUser, String szPassword) {
		switch(nType)
		{
			case proxySocketType:
				System.getProperties().setProperty("socksProxySet", "true");
				System.getProperties().setProperty("socksProxyHost", szProxyServer);
				System.getProperties().setProperty("socksProxyPort", szPort);
				break;
			case proxyFtpType:
				System.setProperty("ftpProxyPort", szPort);
			    System.setProperty("ftpProxyHost", szProxyServer);
			    System.setProperty("ftpProxySet", "true");
			        
				System.getProperties().put( "ftpProxySet", "true");
				System.getProperties().put( "ftpProxyHost", szProxyServer);
				System.getProperties().put( "ftpProxyPort", szPort);
				break;
			case proxyHttpType:
			default:
				System.setProperty("http.proxyType", "4");
			    System.setProperty("http.proxyPort", szPort);
	            System.setProperty("http.proxyHost", szProxyServer);
	            System.setProperty("http.proxySet", "true");
	        
				System.getProperties().put("proxySet", "true");
				System.getProperties().put("proxyHost", szProxyServer);
				System.getProperties().put("proxyPort", szPort);
				break;
		}
		if(null != szUser && 0 < szUser.trim().length())
			Authenticator.setDefault(new Auth(szUser, szPassword));
	}
	
	/**
	 * Forms an HTTP request and parses the response for a translation.
	 * 
	 * @param text
	 *            The String to translate.
	 * @param from
	 *            The language code to translate from.
	 * @param to
	 *            The language code to translate to.
	 * @return The translated String.
	 * @throws Exception
	 */
	private static String retrieveTranslation(String text, String from,
			String to) {// throws Exception
		try {
			if (null == text || 0 == text.length())
				return "";

			StringBuffer url = new StringBuffer();
			url.append(URL_STRING).append(from).append('|').append(to);

			HttpURLConnection uc = (HttpURLConnection) new URL(url.toString())
					.openConnection();
			try {

				uc.setRequestProperty("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
				// 用post的方式能翻译大于1024字节的内容
				uc.setDoOutput(true);
				OutputStream out = uc.getOutputStream();
				out
						.write(("langpair=" + from + "|" + to + TEXT_VAR + URLEncoder
								.encode(text, ENCODING)).getBytes());
				out.flush();
				out.close();
				String page = toString(uc.getInputStream());

				int resultBox = page.indexOf("<div id=result_box dir=");
				if (resultBox < 0)
					throw new Error("No translation result returned.");

				String start = page.substring(resultBox);
				return start.substring(start.indexOf('>') + 1, start
						.indexOf("</div>"));
			} finally { // http://java.sun.com/j2se/1.5.0/docs/guide/net/http-keepalive.html
				try {
					uc.getInputStream().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (null != uc.getErrorStream())
					uc.getErrorStream().close();
				uc = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
}