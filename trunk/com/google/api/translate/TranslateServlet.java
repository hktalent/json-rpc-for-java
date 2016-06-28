package com.google.api.translate;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TranslateServlet extends HttpServlet{
	
	// 代理信息
	private String szProxyServer = null;
	private String szPort = null;
	private String Charset = "UTF-8";
	
	private static final long serialVersionUID = 1L;
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain;charset=" + Charset);
		OutputStream out = response.getOutputStream();
		try {
			String szOut = Translate.translate(request.getParameter("data"), request.getParameter("from"), request.getParameter("to"));
			// System.out.println(szOut);
			out.write(szOut.getBytes(Charset));
			szOut = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
	public void destroy() {
		if(null == szProxyServer || 0 == szProxyServer.trim().length())
			System.getProperties().put("proxySet", "false");
		Charset = szProxyServer = szPort = null;
		super.destroy();
	}
	
	/***
	 * 初始化配置并设置代理
	 */
	public void init(ServletConfig config) throws ServletException
	{
		szProxyServer = config.getInitParameter("ProxyServer");
		szPort = config.getInitParameter("Port");
		if(null != config.getInitParameter("Charset"))
		  Charset = config.getInitParameter("Charset");
		if(null != szProxyServer && 0 < szProxyServer.trim().length())
		{
			String szUser = config.getInitParameter("user"), szPassword = config.getInitParameter("password");
			int nType = Translate.proxyHttpType;
			if(null != config.getInitParameter("type"))
				nType = Integer.parseInt(config.getInitParameter("type"));
			Translate.setProxy(szProxyServer, szPort, nType, szUser, szPassword);
		}
		super.init(config);
	}
}
