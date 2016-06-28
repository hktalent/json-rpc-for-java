/**
 * Language.java
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

import java.util.Arrays;
import java.util.List;

/**
 * Defines language information for the Google Translate API.
ar|en        阿拉伯文到英语
ko|en        朝鲜语到英语
de|fr        德语到法语
de|en        德语到英语
ru|en        俄语到英语
fr|de        法语到德语
fr|en        法语到英语
nl|en        荷兰语到英语
pt|en        葡萄牙语到英语
ja|en        日语到英语
es|en        西班牙语到英语
el|en        希腊语到英语
it|en        意大利语到英语
en|ar        英语到阿拉伯文
en|ko        英语到朝鲜语
en|de        英语到德语
en|ru        英语到俄语
en|fr        英语到法语
en|nl        英语到荷兰语
en|pt        英语到葡萄牙语
en|ja        英语到日语
en|es        英语到西班牙语
en|el        英语到希腊语
en|it        英语到意大利语
en|zh-TW     英语到中文(繁体)
en|zh-CN     英语到中文(简体)
zh|en        中文到英语
zh-TW|zh-CN  中文(繁体到简体)
zh-CN|zh-TW  中文(简体到繁体)
 * @author Richard Midwinter
 */

public final class Language {
	/***
	 * 阿拉伯文
	 */
	public static final String ARABIC = "ar";
	/***
	 * 中文
	 */
	public static final String CHINESE = "zh";
	/***
	 * 中文(简体)
	 */
	public static final String CHINESE_SIMPLIFIED = "zh-CN";
	/***
	 * 中文(繁体)
	 */
	public static final String CHINESE_TRADITIONAL = "zh-TW";
	/***
	 * 荷兰语
	 */
	public static final String DUTCH = "nl";
	/***
	 * 英语
	 */
	public static final String ENGLISH = "en";
	/***
	 * 法语
	 */
	public static final String FRENCH = "fr";
	/***
	 * 德语
	 */
	public static final String GERMAN = "de";
	/***
	 * 希腊语
	 */
	public static final String GREEK = "el";
	/***
	 * 意大利语
	 */
	public static final String ITALIAN = "it";
	/***
	 * 日语
	 */
	public static final String JAPANESE = "ja";
	/***
	 * 朝鲜语
	 */
	public static final String KOREAN = "ko";
	/***
	 * 葡萄牙语
	 */
	public static final String PORTUGESE = "pt";
	/***
	 * 俄语
	 */
	public static final String RUSSIAN = "ru";
	/***
	 * 西班牙语
	 */
	public static final String SPANISH = "es";
	
	private static final List validLanguages = Arrays.asList(new String[] {
			ARABIC,
			CHINESE,
			CHINESE_SIMPLIFIED,
			CHINESE_TRADITIONAL,
			ENGLISH,
			FRENCH,
			GERMAN,
			ITALIAN,
			JAPANESE,
			KOREAN,
			PORTUGESE,
			RUSSIAN,
			SPANISH
			});
	
	private static final List validLanguagePairs = Arrays.asList(new String[] {
			ARABIC +'|' +ENGLISH,
			CHINESE +'|' +ENGLISH,
			CHINESE_SIMPLIFIED +'|' +CHINESE_TRADITIONAL,
			CHINESE_TRADITIONAL +'|' +CHINESE_SIMPLIFIED,
			DUTCH +'|' +ENGLISH,
			ENGLISH +'|' +ARABIC,
			ENGLISH +'|' +CHINESE,
			ENGLISH +'|' +CHINESE_SIMPLIFIED,
			ENGLISH +'|' +CHINESE_TRADITIONAL,
			ENGLISH +'|' +DUTCH,
			ENGLISH +'|' +FRENCH,
			ENGLISH +'|' +GERMAN,
			ENGLISH +'|' +GREEK,
			ENGLISH +'|' +ITALIAN,
			ENGLISH +'|' +JAPANESE,
			ENGLISH +'|' +KOREAN,
			ENGLISH +'|' +PORTUGESE,
			ENGLISH +'|' +RUSSIAN,
			ENGLISH +'|' +SPANISH,
			FRENCH +'|' +ENGLISH,
			FRENCH +'|' +GERMAN,
			GERMAN +'|' +ENGLISH,
			GERMAN +'|' +FRENCH,
			GREEK +'|' +ENGLISH,
			ITALIAN +'|' +ENGLISH,
			JAPANESE +'|' +ENGLISH,
			KOREAN +'|' +ENGLISH,
			PORTUGESE +'|' +ENGLISH,
			RUSSIAN +'|' +ENGLISH,
			SPANISH +'|' +ENGLISH
	});
	
	/**
	 * Checks a given language is available to use with Google Translate.
	 * 
	 * @param language The language code to check for.
	 * @return true if this language is available to use with Google Translate, false otherwise.
	 */
	protected static boolean isValidLanguage(String language) {
		return validLanguages.contains(language);
	}
	
	/**
	 * Checks the languages to translate to and from match with a supported Google Translate pairing.
	 * 
	 * @param from The language code to translate from.
	 * @param to The language code to translate to.
	 * @return true if the language pairing is supported, false otherwise.
	 */
	protected static boolean isValidLanguagePair(String from, String to) {
		return validLanguagePairs.contains(from +'|' +to);
	}
}