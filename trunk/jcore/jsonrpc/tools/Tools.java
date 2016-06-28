package jcore.jsonrpc.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jcore.jsonrpc.common.JSONObject;

public class Tools {
	public static boolean bDebug =  false;
	private static boolean bGetClassName = false;
	private static Class[] className = null;
	
	/**
	 * 包处理
	 * @param packageName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Class[] getClasses(String packageName)
			throws Exception {
		if(bGetClassName && null != className)return className;
		
		ClassLoader classLoader = Tools.class.getClassLoader();// Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = "/" + packageName.replace('.', '/');
		Enumeration resources = classLoader.getResources(path);
		List dirs = new ArrayList();
		while (resources.hasMoreElements()) {
			URL resource = (URL) resources.nextElement();
			if(bDebug)System.out.println(resource.getFile());
			dirs.add(new File(URLDecoder.decode(resource.getFile(), "UTF-8")));
		}
		ArrayList classes = new ArrayList();
		for (int j = 0; j < dirs.size(); j++) {
			File directory = (File) dirs.get(j);
			classes.addAll(findClasses(directory, packageName));
		}
		Class []cs = new Class[classes.size()];
		for(int i = 0; i < cs.length; i++)
			cs[i] = (Class)classes.get(i);
		bGetClassName = true;
		return cs;
	}
	
	/**
	 * 包搜索
	 * @param directory
	 * @param packageName
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static List findClasses(File directory, String packageName) throws Exception {
        List classes = new ArrayList();
        if (!directory.exists()) {
        	if(bDebug)System.out.println("目录不存在：" + directory.getAbsoluteFile());
            return classes;
        }
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++)
        {
        	File file  = files[i];
            if (file.isDirectory())
            {
                if(-1 == file.getName().indexOf("."))
                {
                	if(bDebug)System.out.println(file.getName());
                	classes.addAll(findClasses(file, packageName + "." + URLDecoder.decode(file.getName(), "UTF-8")));
                }
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + URLDecoder.decode(file.getName(), "UTF-8").substring(0, file.getName().length() - 6)));
                if(bDebug)System.out.println("搞定一个");
            }
        }
        return classes;
    }
	
	/**
	 * 传入包名，获取该包下的所有类名，
	 * @param szPkg     "/jcore/jsonrpc/rpcobj"
	 * @param szFltPkg  过滤的包名，也就是包含这个包的才返回
	 * @return
	 */
//	public static List getClassName(String szPkg, String szFltPkg)
//	{
//		if(bGetClassName)return className;
//		// String szFltPkg
//		List lst = new ArrayList();
//		String s = URLDecoder.decode(szPkg);
//		File f = new File(s);
//		File[] fs = f.listFiles();
//		int n = s.indexOf(".jar");
//		if (null == fs && -1 < n)
//		{
//			// war部署的时候
////			if(-1 == n)n = s.indexOf(".war");
//			try
//			{
//				s = s.substring(0, n + 4);
//				int nTomCat = s.lastIndexOf("webapps");
//				if(-1 < nTomCat)
//				{
//					s = "../" + s.substring(nTomCat).replaceAll("\\\\", "/");
//				}
//				else if(bDebug)System.out.println("no webapps (" + s + ")");
//			   JarFile jarFile = new JarFile(s);
//		       Enumeration myenum = jarFile.entries();
//		       int k = 0; 
//		       while (myenum.hasMoreElements()) {
//		    	   JarEntry entry = (JarEntry)myenum.nextElement();
//		    	   String szClassName =  entry.getName();
//		    	   k = szClassName.lastIndexOf(".class");
//		    	   // System.out.println(szClassName);
//		    	   if(-1 < k && -1 < szClassName.indexOf(szFltPkg))
//		    	   {
//		    			   szClassName = szClassName.substring(0, k).replaceAll("[/]", ".");
//		    			   if(-1 < szClassName.indexOf(szFltPkg))
//		    				   lst.add(szClassName);
//		    			    // System.out.println(szClassName);
//		    	   }
//		       }
//			} catch (Exception e) {
//				System.out.print(s);
//				e.printStackTrace();
//			}
////			else if(bDebug)System.out.print("not find .jar: " + s);
//		}
//		else if(null != fs)// war部署的时候
//		{
////			if(bDebug)System.out.println((null == fs) + "[" + fs.length + "]");
//			for (int i = 0; i < fs.length; i++) {
//				if (fs[i].isDirectory())
//					getClassName(fs[i].getAbsolutePath(), szFltPkg);
//				else {
//					String s1 = fs[i].getAbsolutePath();
//					if (-1 < s1.indexOf(".svn"))
//						continue;
//					s1 = s1.substring(s1.indexOf("jcore"));
//					if (-1 < s1.indexOf("\\"))
//						s1 = s1.substring(0, s1.indexOf("."));
//					s1 = s1.replaceAll("\\\\", ".");
//					s1 = s1.replaceAll("/", ".");
//					String pknm = "jcore.jsonrpc.rpcobj";
//					if(s1.endsWith(".class"))
//						s1 = s1.substring(0, s1.length() - 6);
//					if (s1.startsWith(pknm))
//						lst.add(s1);
////						try {
////							
////							JsonRpcRegister.registerObject(request, s1
////									.substring(pknm.length() + 1), Class
////									.forName(s1));
////						} catch (Exception e) {
////							e.printStackTrace();
////						}
//				}
//			}
//		}
//		bGetClassName = true;
//		return className = lst;
//	}
	
	/***************************************************************************
	 * 通过路径获取File对象
	 * 
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	public static File getResourceAsFile(String resource) throws IOException {
		return new File(getResourceURL(resource).getFile());
	}

	public static Reader getResourceAsReader(String resource)
			throws IOException {
		return new InputStreamReader(getResourceAsStream(resource));
	}
	 public static InputStream getResourceAsStream(String resource) throws IOException {
		    InputStream in = null;
		    ClassLoader loader = Tools.class.getClassLoader();
		    if (loader != null) in = loader.getResourceAsStream(resource);
		    if (in == null) in = ClassLoader.getSystemResourceAsStream(resource);
		    if (in == null) throw new IOException("Could not find resource " + resource);
		    return in;
		  }


	/***************************************************************************
	 * 通过路径获取URL对象
	 * 
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	public static URL getResourceURL(String resource) throws IOException {
		URL url = null;
		ClassLoader loader = Tools.class.getClassLoader();
		if (loader != null)
			url = loader.getResource(resource);
		if (url == null)
			url = ClassLoader.getSystemResource(resource);
		if (url == null)
			url = Tools.class.getResource(resource);

		if (url == null)
			throw new IOException("Could not find resource " + resource);
		return url;
	}
	
	 private static final char c[] = { '<', '>', '&', '\"'};
	 private static final String expansion[] = {"&lt;", "&gt;", "&amp;", "&quot;"};
	 /**
	  * 将串中的 <, >, &, " 编码为html的表示方式
	  * @param s
	  * @return
	  */
	public static String HTMLEncode(String s) {
	      StringBuffer st = new StringBuffer();
	      for (int i = 0; i < s.length(); i++) {
	          boolean copy = true;
	          char ch = s.charAt(i);
	          for (int j = 0; j < c.length ; j++) {
	            if (c[j]==ch) {
	                st.append(expansion[j]);
	                copy = false;
	                break;
	            }
	          }
	          if (copy) st.append(ch);
	      }
	      return st.toString();
	    }
	
	/**
	 * html方式的解码
	 * @param s
	 * @return
	 */
	public static String HTMLDecode(String s) {
		  if(null == s || 0 == (s = s.trim()).length())return s;
	      return s.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
	    }

	/***************************************************************************
	 * 解码html方式编码的中文汉字 ，例如将： "&#24322;&#24120;" 解码为 "异常"
	 * 符合的汉字正则表达式范围是：[\u4E00-\u9FA5]
	 * 
	 * @param szStr
	 * @return
	 */
	public static String decodeUnicodeHtm(String szStr) {
		if(null == szStr || 0 == szStr.trim().length())return szStr;
		Pattern p = Pattern.compile("&#(\\d+);", Pattern.MULTILINE);
		Matcher m = null;
		try
		{
			if(null != szStr && szStr.replaceAll("%[0-9A-Fa-f]+", "").length() != szStr.length())
				szStr = java.net.URLDecoder.decode(szStr, "UTF-8");
	    } catch (Exception e) {}
	    if(null != szStr && szStr.replaceAll("&[A-Za-z]+;", "").length() != szStr.length())
	    	szStr = HTMLDecode(szStr);
		try {
			m = p.matcher(szStr);
		} catch (Exception e) {
			return szStr;
		}
		StringBuffer buf = new StringBuffer();
		if(null != m)
		while (m.find())
			m.appendReplacement(buf, (char) Integer.valueOf(m.group(1))
					.intValue()
					+ "");
		m.appendTail(buf);
		return buf.toString();
	}
	
	/** 解码js中escape方法加码的串
	 * %7B%22xtDesTable%22%3A%5B%5B%222011%u5E7407%u670825
	 * @param szStr
	 * @return
	 */
	public static String decodeJsEscape(String szStr) {
		if(null == szStr || 0 == szStr.trim().length())return szStr;
		Pattern p = Pattern.compile("%(u?[0-9a-fA-F]+)", Pattern.MULTILINE);
		Matcher m = null;
		try {
			m = p.matcher(szStr);
		} catch (Exception e) {
			e.printStackTrace();
			return szStr;
		}
		StringBuffer buf = new StringBuffer();
		int lnRst = 0;
		String sk;
		if(null != m)
		while (m.find())
		{
			sk = m.group(1);
			if(m.group(1).startsWith("u"))
				sk = m.group(1).substring(1);
			lnRst = Integer.parseInt(sk, 16);
//			System.out.println(sk + ": " + lnRst);
			m.appendReplacement(buf, (char) lnRst + "");
		}
		m.appendTail(buf);
		return buf.toString();
	}
	
	/*****************************************************************************
	 * 编码码字符串为html方式编码的中文汉字，例如将： "异常" 编码为 "&#24322;&#24120;"
	 * 符合的汉字正则表达式范围是：[\u4E00-\u9FA5]
	 * 
	 * @param szStr
	 * @return
	 */
	public static String encodeUnicodeHtm(String szStr) {
		if (null == szStr || 0 == szStr.trim().length())
			return szStr;
		Pattern p = Pattern.compile("[\u4E00-\u9FA5]", Pattern.MULTILINE);
		Matcher m = p.matcher(szStr);
		StringBuffer buf = new StringBuffer();
		while (m.find())
			m.appendReplacement(buf, "&#" + (int) m.group(0).toCharArray()[0] + ";");
		m.appendTail(buf);
		return buf.toString();
	}
	
	/**
	 * 将s中的汉字转换为\u4E00-\u9FA5这样的形式
	 * @param s
	 * @return
	 */
	public static String encodeUnicode2Js(String s)
	{
		StringBuffer buf = new StringBuffer();
		for(int i = 0, j = s.length(); i < j; i++)
		{
			int n = (int)s.charAt(i);
			// if(0x4E00 <= n && n <= 0x9FA5)
			if(255 < n || n <= 0)
			   buf.append("\\u" + Integer.toHexString(n));
			else buf.append((char)n);
		}
		return buf.toString();
	}
	
	/**
	 * 将s中的汉字转换为\u4E00-\u9FA5这样的形式
	 * @param s
	 * @return
	 */
	public static String decodeUnicode4Js(String szStr)
	{
		if (null == szStr || 0 == szStr.trim().length())
			return szStr;
		Pattern p = Pattern.compile("\\\\u([0-9A-Fa-f])", Pattern.MULTILINE);
		Matcher m = p.matcher(szStr);
		StringBuffer buf = new StringBuffer();
		while (m.find())
		{
			m.appendReplacement(buf, "" + (char)Integer.parseInt( m.group(0), 16));
		}
		m.appendTail(buf);
		return buf.toString();
	}

	/***************************************************************************
	 * 判断对象o是否是为不需要注册的"简单"对象
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isSimpleType(Object o) {
		if (null == o)
			return false;
		String szType = o.getClass().getName();
		Pattern pa = Pattern.compile("^class \\[[ZBCISJFDL]");
		Matcher m = pa.matcher(szType);

		if (-1 < ",java.lang.String,java.util.Date,java.sql.Timestamp,java.lang.Boolean,java.lang.Character,java.lang.Short,java.lang.Integer,java.lang.Long,java.lang.Float,java.lang.Double,boolean,char,byte,short,int,long,float,double,"
				.indexOf("," + szType + ",")
				|| m.find()) {
			// 清除对象使用关联关系，便于内存的有效利用
			m = null;
			pa = null;
			szType = null;
			return true;
		}
		// 清除对象使用关联关系，便于内存的有效利用
		m = null;
		pa = null;
		szType = null;
		return false;
	}

	/***************************************************************************
	 * 判断对象o实现的所有接口中是否有szInterface 2008-08-07 修正多继承中判断接口的功能， 以及修正接口继承后的判断功能
	 * package test;
	 * 
	 * public interface ITest extends Serializable public interface ITest1
	 * extends ITest public class Test1 implements ITest1 public class Test2
	 * extends Test1 public class Test3 extends Test2
	 * 
	 * isInterface(Test3.class, "java.io.Serializable") = true
	 * isInterface(Test3.class, "test.ITest") = true isInterface(Test3.class,
	 * "test.ITest1") = true
	 * 
	 * @param c
	 * @param szInterface
	 * @return
	 */
	public static boolean isInterface(Class c, String szInterface) {
		if (c.getName().equals(szInterface))
			return true;
		Class[] face = c.getInterfaces();
		for (int i = 0, j = face.length; i < j; i++) {
			if (face[i].getName().equals(szInterface)) {
				return true;
			} else {
				Class[] face1 = face[i].getInterfaces();
				for (int x = 0; x < face1.length; x++) {
					if (face1[x].getName().equals(szInterface)) {
						return true;
					} else if (isInterface(face1[x], szInterface)) {
						return true;
					}
				}
			}
		}
		if (null != c.getSuperclass()) {
			return isInterface(c.getSuperclass(), szInterface);
		}
		return false;
	}

	/***************************************************************************
	 * 从c中获取到指定名字的方法对象，参数类型必须符合clsParm的描述，并尝试从super中进行搜索
	 * 
	 * @param c
	 * @param clsParm
	 * @param szName
	 * @return
	 */
	public static Method getSpecifyNameMethod(Class c, Class[] clsParm,
			String szName) {
		try {
			return c.getDeclaredMethod(szName, clsParm);
		} catch (Exception e) {
		}
		if (!c.getSuperclass().getName().equals("java.lang.Object"))
			return getSpecifyNameMethod(c.getSuperclass(), clsParm, szName);
		return null;
	}

	/***************************************************************************
	 * 在对象继承链路中寻找指定的field对象
	 * 
	 * @param c
	 * @param szName
	 * @return
	 */
	public static Field getSpecifyNameField(Class c, String szName) {
		try {
			Field[] f = c.getDeclaredFields();
			for (int i = 0; i < f.length; i++) {
				if (f[i].getName().equals(szName)) {
					f[i].setAccessible(true);
					return f[i];
				}
			}
		} catch (Exception e) {
		}
		if (!c.getSuperclass().getName().equals("java.lang.Object"))
			return getSpecifyNameField(c.getSuperclass(), szName);
		return null;
	}

	/***************************************************************************
	 * 将对象oValue向convert2TypeName类型进行转换
	 * 
	 * @param convert2TypeName
	 * @param oValue
	 * @return
	 */
	public static Object convertObject(Class convert2TypeName, Object oValue) {
		String szNm = convert2TypeName.getName();
		// 如果类型不匹配，就进行一系列转换
		// 将整数向日期进行转换
		if (null != oValue && !szNm.equals(oValue.getClass().getName())
				&& null != oValue) {
			String s = oValue.toString().trim();// , szTmp01 = s.replaceAll("[^\\d\\.\\-]", "");
			// 支持的参数类型、复合类型对象传入的处理
			// 防止无效的值在强制转换中发生异常
			try {
				if (szNm.equals("java.util.Date"))
					return new Date(Long.parseLong(s));
				else if (szNm.equals("java.math.BigDecimal"))
					return new BigDecimal(s);
				else if (szNm.equals("boolean")
						|| szNm.equals("java.lang.Boolean"))
					return Boolean.valueOf(s);
				else if (szNm.equals("char")
						|| szNm.equals("java.lang.Character"))
					return new Character(s.charAt(0));
				else if (szNm.equals("float") || szNm.equals("java.lang.Float"))
					return new Float(s);
				else if (szNm.equals("java.lang.Short"))
					return new Short(s);
				else if (szNm.equals("int") || szNm.equals("java.lang.Integer"))
					return new Integer(s);
				else if (szNm.equals("long") || szNm.equals("java.lang.Long"))
					return new Long(s);
				else if (szNm.equals("double")
						|| szNm.equals("java.lang.Double"))
					return new Double(s);
				else if (szNm.equals("java.lang.String")) {
					return s;
				} else {
					// 复合对象的处理
					if (s.startsWith("{") && s.endsWith("}")) {
						boolean bMap = isInterface(convert2TypeName,
								"java.util.Map");
						// 入口参数的符合对象类型必须是能够实例化的对象
						Object oRst = bMap ? new HashMap() : convert2TypeName
								.newInstance();
						Map map = new JSONObject(s).getHashMap();
						if (bMap)
							return map;
						else {
							// 迭代传入的参数
							Iterator it = map.entrySet().iterator();
							map = null;
							while (it.hasNext()) {
								Map.Entry entry = (Map.Entry) it.next();
								String szKey = (String) entry.getKey();
								if (null != szKey) {
									Field field = getSpecifyNameField(
											convert2TypeName, szKey);
									if (null != field) {
										field.setAccessible(true);
										field.set(oRst, entry.getValue());
									}
									// 寻找set方法
									else {
										Method mehod = getSpecifyNameMethod(
												convert2TypeName,
												new Class[] { java.lang.Object.class },
												"set"
														+ szKey.substring(0, 1)
																.toUpperCase()
														+ szKey.substring(1));
										// 注入内容
										if (null != mehod)
											try {
												mehod.invoke(oRst,
														new Object[] { entry
																.getValue() });
											} catch (Exception e) {
											}
									}
									field = null;
								}
								entry = null;
								szKey = null;
							}
							return oRst;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			return oValue;
		return null;
	}
	
	/**
     * 汉字串转首字母拼音缩写
     * @param str  //要转换的汉字字符串
     * @return String  //拼音缩写
     */
    public static String getPYString(String str)
    {
            String tempStr = "";
            for(int i=0; i<str.length(); i++) {
                    char c = str.charAt(i);
                    if((int)c >= 33 && (int)c <=126) {//字母和符号原样保留
                            tempStr += String.valueOf(c);
                    }
                    else {//累加拼音声母
                            tempStr += getPYChar( String.valueOf(c) );
                    }
            }
            return tempStr;
    }

    /**
     * 取单个字符的拼音声母
     * @param c  //要转换的单个汉字
     * @return String 拼音声母
     */
    public static String getPYChar(String c)
    {
    	    if(null == c || 0 == c.trim().length())return c;
            byte[] array = new byte[2];
            array = String.valueOf(c).getBytes();
            if(2 > array.length)return c;
            int i = (short)(array[0] - '\0' + 256) * 256 + ((short)(array[1] - '\0' + 256));
            if ( i < 0xB0A1) return "*";
            if ( i < 0xB0C5) return "a";
            if ( i < 0xB2C1) return "b";
            if ( i < 0xB4EE) return "c";
            if ( i < 0xB6EA) return "d";
            if ( i < 0xB7A2) return "e";
            if ( i < 0xB8C1) return "f";
            if ( i < 0xB9FE) return "g";
            if ( i < 0xBBF7) return "h";
            if ( i < 0xBFA6) return "j";
            if ( i < 0xC0AC) return "k";
            if ( i < 0xC2E8) return "l";
            if ( i < 0xC4C3) return "m";
            if ( i < 0xC5B6) return "n";
            if ( i < 0xC5BE) return "o";
            if ( i < 0xC6DA) return "p";
            if ( i < 0xC8BB) return "q";
            if ( i < 0xC8F6) return "r";
            if ( i < 0xCBFA) return "s";
            if ( i < 0xCDDA) return "t";
            if ( i < 0xCEF4) return "w";
            if ( i < 0xD1B9) return "x";
            if ( i < 0xD4D1) return "y";
            if ( i < 0xD7FA) return "z";
            return "*";
    }
    
    
//    /**
//     * Unicode和UTF-8之间的转换关系表
//UCS-4编码	UTF-8字节流
//U+00000000 – U+0000007F	  0xxxxxxx
//U+00000080 – U+000007FF	  110xxxxx 10xxxxxx
//U+00000800 – U+0000FFFF	  1110xxxx 10xxxxxx 10xxxxxx
//U+00010000 – U+001FFFFF	  11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
//U+00200000 – U+03FFFFFF	  111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
//U+04000000 – U+7FFFFFFF	  1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
//     * 判断给定的数据是否为UTF-8编码
//     * 0zzzzzzz(00-7F)
//     * 110yyyyy(C2-DF) 10zzzzzz(80-BF)
//     * 1110xxxx(E0-EF) 10yyyyyy 10zzzzzz
//     * 11110www(F0-F4) 10xxxxxx 10yyyyyy 10zzzzzz
//对于UTF-8编码中的任意字节B，如果B的第一位为0，则B为ASCII码，并且B独立的表示一个字符;
//如果B的第一位为1，第二位为0，则B为一个非ASCII字符（该字符由多个字节表示）中的一个字节，并且不为字符的第一个字节编码;
//如果B的前两位为1，第三位为0，则B为一个非ASCII字符（该字符由多个字节表示）中的第一个字节，并且该字符由两个字节表示;
//如果B的前三位为1，第四位为0，则B为一个非ASCII字符（该字符由多个字节表示）中的第一个字节，并且该字符由三个字节表示;
//如果B的前四位为1，第五位为0，则B为一个非ASCII字符（该字符由多个字节表示）中的第一个字节，并且该字符由四个字节表示;
//因此，对UTF-8编码中的任意字节，根据第一位，可判断是否为ASCII字符;根据前二位，可判断该字节是否为一个字符编码的第一个字节; 
//根据前四位（如果前两位均为1），可确定该字节为字符编码的第一个字节，并且可判断对应的字符由几个字节表示;根据前五位（如果前四位为1），
//可判断编码是否有错误或数据传输过程中是否有错误。
//     * @param b
//     * @return
//     */
//    public static boolean isUTF8(byte []b)
//	{
//		try
//		{
//			if(3 < b.length && 0xEF == b[0] && 0xBB == b[1] && 0xBF == b[2])
//				return true;
//			for(int i = 0; i < b.length; i++)
//			{
//				String s = Integer.toString(b[i] & 0xff, 2);
//				// 0xxxxxxx
//				if(8 > s.length())continue;
//				/*  注意UTF-8的最前面n个1，表示整个UTF-8串是由n个字节构成的
//  U-00000080   -   U-000007FF:   110xxxxx   10xxxxxx   
//  U-00000800   -   U-0000FFFF:   1110xxxx   10xxxxxx   10xxxxxx   
//  U-00010000   -   U-001FFFFF:    11110xxx   10xxxxxx   10xxxxxx   10xxxxxx   
//  U-00200000   -   U-03FFFFFF:   111110xx   10xxxxxx   10xxxxxx   10xxxxxx   10xxxxxx   
//  U-04000000   -   U-7FFFFFFF:   1111110x   10xxxxxx   10xxxxxx   10xxxxxx   10xxxxxx   10xxxxxx   
//				 */
//				String []a = s.split("");
//				int x = 0;
//				for(; x < a.length; x++)
//				{
//					if(0 == a[x].length() || 0 == x)continue;
//					if(!"1".equals(a[x]))break;
//					if(x > 5)return false; // 没有超过6字节编码的utf-8
//					int nPos = i + x;
//					if(nPos >= b.length)return false;// 超过最大的长度了，就不是utf-8编码了
//					// 下一字节前面是10开头
//					String s1 = Integer.toString(b[nPos] & 0xff, 2);
//					if(8 == s1.length() && s1.startsWith("10"))continue;
//					System.out.println("oh, No: " + s + " == " + i + ", " + nPos  + " -- " + s1 + " ::: " + (char)b[i] + " ::: " + (char)b[nPos] );
//					return false;
//				}
//				i = i + x;
////				 if(-27 == b[i])
////				System.out.println("E5:" + b[i] + ", " + Integer.toHexString((byte)b[i]));
//			}
//			return true;
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		return false;
//	}
//    
//   
//    /**
//     * 判断给定的数据是否为UTF-8编码
//     * @param s
//     * @return
//     */
//	public static boolean isUTF8(String s)
//	{
//		if(null == s || 0 == s.trim().length())return true;
//		try
//		{
//			return isUTF8(s.getBytes("UTF-8"));
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		return false;
//	}
//	
//	
//	
//	
//	/**
//	 * 判断给定的数据是否为GBK编码
//	 * @param s
//	 * @return
//	 */
//	public static boolean isGBK(String s)
//	{
//		if(null == s || 0 == s.trim().length())return true;
//		try
//		{
//			return isGBK(s.getBytes("GBK"));
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		return false;
//	}
//	
//	/**
//	 * 判断是否为GB2312
//	 * @param b
//	 * @return
//	 */
//	public static boolean isGB2312(byte []b)
//	{
//		for(int i = 0; i < b.length; i++)
//		{
//			int b1 = (int)b[i];
//			if((i + 1) < b.length)
//			{
//				int b2 = (int)b[i + 1];
//				// 级汉字从16区起始，汉字区的“高位字节”的范围是0xB0-0xF7，“低位字节”的范围是0xA1-0xFE，
//				// 占用的码位是72*94=6768。其中有5个空位是D7FA-D7FE
////				if( 0xB0 <= b1 && 0xF7 >= b1 && 
////				    0xA1 <= b2 && 0xFE >= b2 &&
////				   !(0xD7 == b1 && 0xFA <= b2 && 0xFE >= b2))
////					continue;
//				if(b1>=176 && b1<=247 && b2>=160 && b2<=254)
//					continue;
//				// “高位字节”使用了0xA1-0xF7（把01-87区的区号加上0xA0），
//				// “低位字节”使用了0xA1-0xFE（把01-94加上0xA0）
//			}
//			if(0 <= b1 && 127 >= b1)continue;
//			return false;
//		}
//		return true;
//	}
//	
//	/**
//	 * 判断数据是不是CJK字符集
//	 * 其中从0x4E00到 0x9FA5 的连续区域包含了 20902 个来自
//	 * 中国（包括台湾）、日本、韩国的汉字，称为 CJK (Chinese Japanese Korean) 汉字
//	 * 汉字：613 - 32448(10进制)
//	 * @param b
//	 * @return
//	 */
//	public static boolean isCJK(byte []b)
//	{
//		try
//		{
//		   Object [][]a = {           
//				     {new Integer(Integer.parseInt("0020",16)), new Integer(Integer.parseInt("007E",16)), "基本拉丁语"},
////					 {new Integer(Integer.parseInt("00A0",16)), new Integer(Integer.parseInt("00FF",16)), "拉丁语1"},
////					 {new Integer(Integer.parseInt("0101",16)), new Integer(Integer.parseInt("0178",16)), "拉丁语扩充A"},
////					 {new Integer(Integer.parseInt("0192",16)), new Integer(Integer.parseInt("01F9",16)), "拉丁语扩充B"},
//					 {new Integer(Integer.parseInt("0251",16)), new Integer(Integer.parseInt("0261",16)), "国际音标扩充"},
//					 {new Integer(Integer.parseInt("02C6",16)), new Integer(Integer.parseInt("02DC",16)), "进格的修饰字符"},
////					 {new Integer(Integer.parseInt("0391",16)), new Integer(Integer.parseInt("03C9",16)), "基本希腊语"},
////					 {new Integer(Integer.parseInt("0401",16)), new Integer(Integer.parseInt("0451",16)), "西里尔文"},
//					 {new Integer(Integer.parseInt("2010",16)), new Integer(Integer.parseInt("203B",16)), "广义标点"},
//					 {new Integer(Integer.parseInt("20AC",16)), new Integer(Integer.parseInt("20AC",16)), "货币符号"},
//					 {new Integer(Integer.parseInt("2103",16)), new Integer(Integer.parseInt("2122",16)), "类似字母的符号（摄氏度、千分位等）"},
//					 {new Integer(Integer.parseInt("2160",16)), new Integer(Integer.parseInt("2179",16)), "数字形式（Ⅳ）"},
//					 {new Integer(Integer.parseInt("2190",16)), new Integer(Integer.parseInt("2199",16)), "箭头符号"},
//					 {new Integer(Integer.parseInt("2208",16)), new Integer(Integer.parseInt("22BF",16)), "数学运算符号"},
//					 {new Integer(Integer.parseInt("2312",16)), new Integer(Integer.parseInt("2312",16)), "零杂技术用符号"},
//					 {new Integer(Integer.parseInt("2460",16)), new Integer(Integer.parseInt("249B",16)), "带括号的字母、数字"},
//					 {new Integer(Integer.parseInt("2500",16)), new Integer(Integer.parseInt("2573",16)), "制表符号"},
//					 {new Integer(Integer.parseInt("2581",16)), new Integer(Integer.parseInt("2595",16)), "方块元素"},
//					 {new Integer(Integer.parseInt("25A0",16)), new Integer(Integer.parseInt("25E5",16)), "几何图形符号"},
//					 {new Integer(Integer.parseInt("2605",16)), new Integer(Integer.parseInt("2642",16)), "零杂丁贝符号（示意符号等）"},
//					 {new Integer(Integer.parseInt("2E81",16)), new Integer(Integer.parseInt("2ECA",16)), "CJK偏旁部首增补"},
//					 {new Integer(Integer.parseInt("2FF0",16)), new Integer(Integer.parseInt("2FFB",16)), "象形文字"},
//					 {new Integer(Integer.parseInt("3000",16)), new Integer(Integer.parseInt("3029",16)), "CJK符号和标点（全角）"},
////					 {new Integer(Integer.parseInt("3041",16)), new Integer(Integer.parseInt("309E",16)), "平假名"},
////					 {new Integer(Integer.parseInt("30A1",16)), new Integer(Integer.parseInt("30FE",16)), "片假名"},
//					 {new Integer(Integer.parseInt("3105",16)), new Integer(Integer.parseInt("3129",16)), "注音"},
//					 {new Integer(Integer.parseInt("3220",16)), new Integer(Integer.parseInt("32A3",16)), "带括号的CJK字母和月份"},
//					 {new Integer(Integer.parseInt("338E",16)), new Integer(Integer.parseInt("33D5",16)), "CJK兼容字符"},
//					 {new Integer(Integer.parseInt("3447",16)), new Integer(Integer.parseInt("9FA5",16)), "CJK统一汉字"},
//					 {new Integer(Integer.parseInt("F92C",16)), new Integer(Integer.parseInt("FA29",16)), "CJK兼容汉字"},
//					 {new Integer(Integer.parseInt("FE30",16)), new Integer(Integer.parseInt("FE4F",16)), "CJK兼容形式"},
//					 {new Integer(Integer.parseInt("FE50",16)), new Integer(Integer.parseInt("FE6B",16)), "小写变体"},
//					 {new Integer(Integer.parseInt("FF01",16)), new Integer(Integer.parseInt("FFE5",16)), "半形及全形字符"}
//					 };
//			String s = new String(b, "GBK");
//			for(int i = 0; i < s.length(); i++)
//			{
//				char c = s.charAt(i);
//				
//				// 控制字符
//				if('\b' == c || '\r' == c || '\n' == c || '\t' == c|| '\f' == c )continue;
//				boolean bOk = false;
//				for(int j = 0; j < a.length; j++)
//				{
//					int nStart = ((Integer)a[j][0]).intValue();
//					int nEnd = ((Integer)a[j][1]).intValue();
//					if(nStart <= c && c <= nEnd)
//					{
//						bOk = true;
//						break;
//					}
//				}
//				if(bOk)continue;
//				System.out.println(c + " 0x" + Integer.toHexString(c));
//				return false;
//			}
//			return true;
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		return false;
//	}
//	
//	/**
//	 * 判断给定的数据是否为GBK编码
//	 * CJK 统一汉字"(C 指中国.J 指日本.K 指朝鲜
//	 * (GBK).英文名称 Chinese Internal Code Specification
//	 * Universal Multilpe-Octet Coded Character Set(简称 UCS).通用多八位编码字符集:广用多八位元编码字元集
//	 * 0020 - 007E    基本拉丁语
//	 * 00A0 - 00FF   拉丁语1
//	 * 0101  -  0178   拉丁语扩充A
//	 * 0192 -  01F9    拉丁语扩充B
//	 * 0251 -  0261    国际音标扩充
//	 * 02C6 -  02DC  进格的修饰字符
//	 * 0391 -  03C9   基本希腊语
//	 * 0401 -  0451   西里尔文
//	 * 2010 - 203B  广义标点
//	 * 20AC - 20AC  货币符号
//	 * 2103 - 2122 类似字母的符号（摄氏度、千分位等）
//	 * 2160 - 2179 数字形式（Ⅳ）
//	 * 2190 - 2199 箭头符号
//	 * 2208 - 22BF 数学运算符号
//	 * 2312 - 2312 零杂技术用符号
//	 * 2460 - 249B 带括号的字母、数字
//	 * 2500 -  2573 制表符号
//	 * 2581 -  2595 方块元素
//	 * 25A0 -  25E5 几何图形符号
//	 * 2605 - 2642 零杂丁贝符号（示意符号等）
//	 * 2E81 -  2ECA  CJK偏旁部首增补
//	 * 2FF0 - 2FFB 象形文字
//	 * 3000 -3029  CJK符号和标点（全角）
//	 * 3041 - 309E 平假名
//	 * 30A1 - 30FE 片假名
//	 * 3105 -  3129 注音
//	 * 3220 - 32A3 带括号的CJK字母和月份
//	 * 338E - 33D5 CJK兼容字符
//	 * 3447 - 9FA5  CJK统一汉字
//	 * F92C -  FA29 CJK兼容汉字
//	 * FE30 - FE4F  CJK兼容形式
//	 * FE50 - FE6B 小写变体
//	 * FF01 - FFE5 半形及全形字符
//	 * 
//	 * GBK 规范收录了 ISO 10646.1 中的全部 CJK 汉字和符号.并有所补充.具体包括: 
//1. GB 2312 中的全部汉字.非汉字符号. 
//2. GB 13000.1 中的其他 CJK 汉字.以上合计 20902 个 GB 化汉字. 
//3. <简化字总表>中未收入 GB 13000.1 的 52 个汉字. 
//4. <康熙字典>及<辞海>中未收入 GB 13000.1 的 28 个部首及重要构件. 
//5. 13 个汉字结构符. 
//6. BIG-5 中未被 GB 2312 收入.但存在于 GB 13000.1 中的 139 个图形符号. 
//7. GB 12345 增补的 6 个拼音符号. 
//8. 汉字[○". 
//9. GB 12345 增补的 19 个竖排标点符号(GB 12345 较 GB 2312 增补竖排标点符号 29 个.其中 10 个未被 GB 13000.1 收入.
//故 GBK 亦不收). 
//10. 从 GB 13000.1 的 CJK 兼容区挑选出的 21 个汉字. 
//11. GB 13000.1 收入的 31 个 IBM OS/2 专用符号. 
//
//GBK 亦采用双字节表示.总体编码范围为 8140-FEFE.首字节在 81-FE 之间.尾字节在 40-FE 之间.剔除 xx7F 一条线
//.总计 23940 个码位.共收入 21886 个汉字和图形符号.其中汉字(包括部首和构件)21003 个.图形符号 883 个
//	 * @param b
//	 * @return
//	 */
//	public static boolean isGBK(byte []b)
//	{
//		if(null == b || 0 == b.length)return true;
//		try
//		{
//			for(int i = 0; i < b.length; i++)
//			{
//				int b1 = (int)(b[i] & 0x00ff);
//				// 总体上说第一字节的范围是81–FE（也就是不含80和FF），第二字节的一部分领域在40–FE，其他领域在80–FE
//				if((i + 1) < b.length)
//				{
//					int b2 = (int)(b[i + 1] & 0x00ff);
//					// 字数717: A1–A9	A1–FE
//					if(0xA1 <= b1 && 0xA9 >= b1 && 0xA1 <= b2 && 0xFE >= b2)continue;
//					// 字数 6,763: B0–F7	A1–FE
//					if(0xB0 <= b1 && 0xF7 >= b1 && 0xA1 <= b2 && 0xFE >= b2)continue;
//					//6,080: 81–A0	 40–FE (7F除外)
//					if(0x81 <= b1 && 0xA0 >= b1 && 0x40 <= b2 && 0xFE >= b2 && 0x7F != b2)continue;
//					// 8,160: AA–FE	40–A0 (7F除外)
//					if(0xAA <= b1 && 0xFE >= b1 && 0x40 <= b2 && 0xA0 >= b2 && 0x7F != b2)continue;
//					// 166: A8–A9	40–A0 (7F除外)
//					if(0xA8 <= b1 && 0xA9 >= b1 && 0x40 <= b2 && 0xA0 >= b2 && 0x7F != b2)continue;
//					// 564: AA–AF	A1–FE
//					if(0xAA <= b1 && 0xAF >= b1 && 0xA1 <= b2 && 0xFE >= b2)continue;
//					// 658: F8–FE	A1–FE
//					if((0xF8 <= b1) && (0xFE >= b1) && (0xA1 <= b2) &&(0xFE >= b2))continue;
//					// 672: A1–A7	40–A0 (7F除外)
//					if(0xA1 <= b1 && 0xA7 >= b1 && 0x40 <= b2 && 0xA0 >= b2 && 0x7F != b2)continue;
//				}
//				// 00–7F范围内是一位，和ASCII保持一致，此范围内严格上说有96个文字和32个控制符号,128个
//				if(0 <= b1 && 0x7F >= b1)continue;
//				 return false;
//			}
//			return true;
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		return false;
//	}
	
	/**
	 * 判断是否GBK
	 * @param b
	 * @return
	 */
	public static boolean isGBK_Good(byte[] b) {
		try {
			String s = new String(b, "GBK");
			// 0 ~ 255字符
			s = s.replaceAll("[\\u0000-\\u00ff]", "");
			// 0x3447; i < 0x9fa5 常用汉字范围
			s = s.replaceAll("[\\u3447-\\u9fa5]", "");
			// 不常用的汉字区域
//			s = s.replaceAll("[\\uF92C-\\uFA29]", "");
			// 全角标点符号:　、。〃々〆〇〈〉《》「」『』【】〒〓〔〕〖〗〝〞〡〢〣〤〥〦〧〨〩
			s = s.replaceAll("[\\u3000-\\u3029]", "");
			// 0xFE50; i <= 0xFE6B: ﹐﹑﹒﹔﹕﹖﹗﹙﹚﹛﹜﹝﹞﹟﹠﹡﹢﹣﹤﹥﹦﹨﹩﹪﹫
			s = s.replaceAll("[\\uFE50-\\uFE6B]", "");
			// 0xFF01; i <= 0xFFE5: ！＂＃＄％＆＇（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［＼］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～￠￡￢￣￤￥
			s = s.replaceAll("[\\uFF01-\\uFFE5]", "");			
			return 0 == s.length();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
//	
//	public static Class[] getClassesFromFileJarFile(String pckgname, String baseDirPath) throws ClassNotFoundException
//	{
//		ArrayList classes = new ArrayList();
//		String path = pckgname.replace('.', '/') + "/";
//		File mF = new File(baseDirPath);
//		String[] files = mF.list();
//		ArrayList jars = new ArrayList();
//		for (int i = 0; i < files.length; i++)
//			if (files[i].endsWith(".jar")) jars.add(files[i]);
//		
//		for (int i = 0; i < jars.size(); i++)
//		{
//			try
//			{
//				JarFile currentFile = new JarFile(jars.get(i).toString());
//				for (Enumeration e = currentFile.entries(); e.hasMoreElements(); ) 
//				{
//					JarEntry current = (JarEntry) e.nextElement();
//					if(current.getName().length() > path.length() && current.getName().substring(0, path.length()).equals(path) && current.getName().endsWith(".class"))
//						classes.add(Class.forName(current.getName().replaceAll("/", ".").replaceAll(".class", "")));
//				}
//			}
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//		}
//		Class[] classesA = new Class[classes.size()];
//		classes.toArray(classesA);
//		return classesA;
//	}
//	
	public static void main(String args[])
	{
		try
		{
//			Class []c = Tools.getClassesFromFileJarFile("jcore.jsonrpc.rpcobj", "/");
	//		Package[] pkg = Tools.class.getClassLoader().getSystemClassLoader()..getPackage()..getPackages();
	//		Class.forName("").getClasses();
			System.out.println(Tools.decodeJsEscape("%7B%22xtDesTable%22%3A%5B%5B%222011%u5E7407%u670825%u65E5%u661F%u671F%u4E00%22%2C%22%u4E0A%u5348%22%2C%229%22%2C%2200%22%2C%2212%22%2C%2200%22%2C%22%u5C81%u7684%u8001%u7EA2%u519B%u4E2A%5C%22sdf%5C%22d%5C%22dd%27dd%5C%22%27dd%27%5C%22d%5C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u665A%u4E0A%22%2C%2212%22%2C%2200%22%2C%2213%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E2D%u5348%22%2C%2213%22%2C%2200%22%2C%2215%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E0B%u5348%22%2C%2215%22%2C%2200%22%2C%2223%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%222011%u5E7407%u670826%u65E5%u661F%u671F%u4E8C%22%2C%22%u4E0A%u5348%22%2C%229%22%2C%2200%22%2C%2212%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u665A%u4E0A%22%2C%2212%22%2C%2200%22%2C%2213%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E2D%u5348%22%2C%2213%22%2C%2200%22%2C%2215%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E0B%u5348%22%2C%2215%22%2C%2200%22%2C%2223%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%222011%u5E7407%u670827%u65E5%u661F%u671F%u4E09%22%2C%22%u4E0A%u5348%22%2C%229%22%2C%2200%22%2C%2212%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u665A%u4E0A%22%2C%2212%22%2C%2200%22%2C%2213%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E2D%u5348%22%2C%2213%22%2C%2200%22%2C%2215%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E0B%u5348%22%2C%2215%22%2C%2200%22%2C%2223%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%222011%u5E7407%u670828%u65E5%u661F%u671F%u56DB%22%2C%22%u4E0A%u5348%22%2C%229%22%2C%2200%22%2C%2212%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u665A%u4E0A%22%2C%2212%22%2C%2200%22%2C%2213%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E2D%u5348%22%2C%2213%22%2C%2200%22%2C%2215%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E0B%u5348%22%2C%2215%22%2C%2200%22%2C%2223%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%222011%u5E7407%u670829%u65E5%u661F%u671F%u4E94%22%2C%22%u4E0A%u5348%22%2C%229%22%2C%2200%22%2C%2212%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u665A%u4E0A%22%2C%2212%22%2C%2200%22%2C%2213%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E2D%u5348%22%2C%2213%22%2C%2200%22%2C%2215%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E0B%u5348%22%2C%2215%22%2C%2200%22%2C%2223%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%222011%u5E7407%u670830%u65E5%u661F%u671F%u516D%22%2C%22%u4E0A%u5348%22%2C%229%22%2C%2200%22%2C%2212%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u665A%u4E0A%22%2C%2212%22%2C%2200%22%2C%2213%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E2D%u5348%22%2C%2213%22%2C%2200%22%2C%2215%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E0B%u5348%22%2C%2215%22%2C%2200%22%2C%2223%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%222011%u5E7407%u670831%u65E5%u661F%u671F%u65E5%22%2C%22%u4E0A%u5348%22%2C%229%22%2C%2200%22%2C%2212%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u665A%u4E0A%22%2C%2212%22%2C%2200%22%2C%2213%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E2D%u5348%22%2C%2213%22%2C%2200%22%2C%2215%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%2C%5B%22%u4E0B%u5348%22%2C%2215%22%2C%2200%22%2C%2223%22%2C%2200%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%5D%2C%20%22mxtDes%22%3A%5B%5B%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%2C%22%22%5D%5D%7D"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
