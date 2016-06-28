package jcore.jsonrpc.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jcore.jsonrpc.tools.GetAllSupersProperty;
import jcore.jsonrpc.tools.Tools;

/***
 * 对象转换为json的串形式
 * @author 夏天
 *
 */
public class ObjectToJSON implements Serializable{
	private static final long serialVersionUID = 764043683337733449L;
	private Object o = null;
	private JSONRPCBridge brige = null;
	
	/***
	 * 将对象obj转换为
	 * @param obj   将要转换的对象
	 * @param szCurRegPath 被转换对象的注册名路径
	 * @param brige  桥接对象
	 */
	public ObjectToJSON(Object obj, JSONRPCBridge brige)
	{
		this.o = obj;
		this.brige = brige;
	}
	
	/***
	 * 将字符串中需要转义的字符进行转义，例如：",\r\n\t\f\b中文字符等
	 * @param string
	 * @return
	 */
	private static String quote(String string, boolean bJh) {
	        if (null == string || 0 == string.length()) 
	            return "\"\"";

	        char         c;
	        int          i;
	        int          len = string.length();
	        StringBuffer sb = new StringBuffer(len + 4);
	        String       t;

	        sb.append('"');
	        for (i = 0; i < len; i += 1) {
	            c = string.charAt(i);
	            switch (c) {
	            case '\\':
	            case '"':
	            case '/':
	                sb.append('\\');
	                sb.append(c);
	                break;
	            case '\b':
	                sb.append("\\b");
	                break;
	            case '\t':
	                sb.append("\\t");
	                break;
	            case '\n':
	                sb.append("\\n");
	                break;
	            case '\f':
	                sb.append("\\f");
	                break;
	            case '\r':
	                sb.append("\\r");
	                break;
	            default:
	                if (c < ' ' || c >= 128) {
	                	if(bJh)
	                		sb.append(c);
	                	else
	                	{
		                    t = "000" + Integer.toHexString(c);
		                    sb.append("\\u" + t.substring(t.length() - 4));
	                	}
	                } else {
	                    sb.append(c);
	                }
	            }
	        }
	        sb.append('"');
	        return sb.toString();
	}
	// 不输出编号的标志
	private boolean bJh = false;
	
	public ObjectToJSON setBJh(boolean b)
	{
		bJh = b;
		return this;
	}
	public String toJSON()
	{
		bJh = true;
		return toJSON(null);
	}
	/***************************************************************************
	 * 返回对象的JSON格式
	 */
	public String toJSON(String szObjName)
	{
		StringBuffer buf = new StringBuffer();
		// 简单类型模式
		String szSimpleTypeReg = "^(boolean|char|byte|short|int|long|float|double)$";
		String szSimpleArrTypeReg = "^class \\[([A-Z])";
		Pattern p = Pattern.compile(szSimpleTypeReg);
		Pattern pa = Pattern.compile(szSimpleArrTypeReg); 
		int nPos = 0;
		try
		{
			Class c = o.getClass();
			String szClassName = c.getName();
			// 数组的处理
			Pattern pSz = Pattern.compile("^\\[L.+$");
			
			// 特殊对象的处理
			if(szClassName.equals("java.lang.String"))
				return buf.append(quote(o.toString(), bJh)).toString();
			else if(szClassName.equals("java.lang.Object"))
				return buf.append(quote(o.toString(), bJh)).toString();
			else if(szClassName.equals("java.util.Date") || szClassName.equals("java.sql.Date") || szClassName.equals("java.sql.Timestamp"))
			{
				Timestamp tstp = null;
				if(o instanceof java.util.Date)
				   tstp = new Timestamp(((java.util.Date)o).getTime());
				else if(o instanceof java.sql.Date)
					   tstp = new Timestamp(((java.sql.Date)o).getTime());
				// 直接使用Timestamp的toString
				buf.append("'").append(tstp.toString()).append("'");
//				int m = oDate.getMonth() + 1;
//				// 来看需要返回JavaScript Date类型，就将下面的注释打开
//				// return buf.append("new Date(").append(((Date)o).getTime()).append(")").toString();
//				buf.append("'").append(oDate.getYear() + 1900)
//				.append("-").append(9 < m ? "" + m : "0" + m)
//				// 邓详静 2009-12-12 22:54:34 有问题 oDate.getDay() 应该是oDate.getDate() 
//				.append("-").append(9 < oDate.getDate() ? "" + oDate.getDate(): "0" + oDate.getDate());
//				if(0 < oDate.getHours() && 0 < oDate.getMinutes())
//					buf.append(" ").append(9 < oDate.getHours() ? "" + oDate.getHours(): "0" + oDate.getHours())
//				.append(":").append(9 < oDate.getMinutes() ? "" + oDate.getMinutes(): "0" + oDate.getMinutes())
//				.append(":").append(9 < oDate.getSeconds() ? "" + oDate.getSeconds(): "0" + oDate.getSeconds())
//				.append(".").append("000");
//				buf.append("'");
				return buf.toString();
			}
			// 简单类型的对象封装类
			else if(szClassName.equals("java.lang.Boolean"))
				return buf.append(((Boolean)o).booleanValue()).toString();
			else if(szClassName.equals("java.lang.Character"))
				return buf.append("'").append(((Character)o).charValue()).append("'").toString();
			else if(szClassName.equals("java.lang.Short"))
				return buf.append(((Short)o).shortValue()).toString();
			else if(szClassName.equals("java.lang.Integer"))
				return buf.append(((Integer)o).intValue()).toString();
			else if(szClassName.equals("java.lang.Long"))
				return buf.append(((Long)o).longValue()).toString();
			else if(szClassName.equals("java.lang.Float"))
				return buf.append(((Float)o).floatValue()).toString();
			else if(szClassName.equals("java.lang.Double"))
				return buf.append(((Double)o).doubleValue()).toString();
			else if(szClassName.equals("java.math.BigDecimal"))
				return buf.append(((BigDecimal)o).doubleValue()).toString();
			// 接口是Map
			else if(Tools.isInterface(o.getClass(), "java.util.Map"))
			{
				Iterator mapIt = ((Map)o).entrySet().iterator();
				int i = 0;
				while(mapIt.hasNext())
				{
					Map.Entry entry = (Map.Entry)mapIt.next();
					if(null != entry.getValue())
					{
						if(0 < i)
							buf.append(",");
						buf.append("\"").append(entry.getKey()).append("\":").append(new ObjectToJSON(entry.getValue(), brige).setBJh(bJh).toJSON(null));
						i++;
					}
				}
				if(null != szObjName)
				{
					if(1 < buf.length())buf.append(",");
				    buf.append("_name_:\"").append(szObjName).append("\"");
				}
				if(!bJh)
				{
				if(1 < buf.length())buf.append(",");
				buf.append("_id_:").append(this.o.hashCode());
				}
				return "{" + buf.append("}").toString();
			}
			// 接口是List
			else if(Tools.isInterface(o.getClass(), "java.util.List"))
			{
				List lst = (List)o;
				for(int i = 0, x = 0, j = lst.size(); i < j; i++)
				{
					if(null != lst.get(i))
					{
						if(0 < x)
							buf.append(",");
						buf.append(new ObjectToJSON(lst.get(i), brige).setBJh(bJh).toJSON(null));
						x++;
					}
				}
				
				return "[" + buf.append("]").toString();
			}
			// 数组的处理
			else if(pSz.matcher(szClassName).find())
			{
				Object []tmp09 = (Object [])this.o;
    	    	if(0 < tmp09.length)
    	    	{
    	    		if(null != tmp09[0])
    	    	    buf.append(new ObjectToJSON(tmp09[0], brige).setBJh(bJh).toJSON(null));
	    	    	for(int j = 1; j < tmp09.length; j++)
	    	    	{
	    	    		if(null != tmp09[j])
	    	    			buf.append(",").append(new ObjectToJSON(tmp09[j], brige).setBJh(bJh).toJSON(null));
	    	    	}
    	    	}
    	    	return "[" + buf.append("]").toString();
			}
			
			// 递归对象的犯错处理
			if(null != this.brige && null != this.brige.getSession())
			{
				Map mTmp = (Map)this.brige.getSession().getAttribute(JSONRPCBridge.ObjIdMapName);
				// 已经处理过的对象，防止递归对象的处理
				if(null != mTmp)
				{
					if(null != mTmp.get(this.o.hashCode() + ""))
						return "null";
					else mTmp.put(this.o.hashCode() + "", "1");
				}
			}
			
			// 如果是其它复合对象，就对其反射并生成其方法信息、属性信息
			if(null != brige)
				brige.registerObject(this.o.hashCode(), this.o);
			// 成员方法的处理
			Method []oMs = GetAllSupersProperty.getMethods(c);// c.getMethods();
			// 可能在应用中需要过滤，不将这些方法输出：
			// "main","getClass","wait","wait","wait","equals","toString","notify","notifyAll"
			if(!bJh && 0 < oMs.length)
			{
				buf.append("\"methods\":[");
				String szFlt = "(notifyAll)|(getClass)|(wait)|(wait)|(equals)|(notify)|(main)|(hashCode)|(toString)";
				Map mMTmp = new HashMap();
				for(int i = 0, k = 0; i < oMs.length; i++)
				{
					String szName = oMs[i].getName();
					if(0 < szName.replaceAll(szFlt, "").length())
					{
						// 同名的方法名只输出一次
						if(null != mMTmp.get(szName))
							continue;
						if(0 < k)
							buf.append(",");
					    buf.append("\"").append(szName).append("\"");
					    mMTmp.put(szName, (k++) + "");
					}
				}
				// 就近解除对象的使用引用关系，让虚拟机尽快回收对象使用的内存
				mMTmp = null;
				buf.append("]");
				nPos++;
			}
			
			// 成员变量的处理
			Field []f = GetAllSupersProperty.getFields(c);// c.getDeclaredFields(); // c.getFields();
			if(0 < f.length)
			{
				String szFlter = "(serialVersionUID)";
				for(int i = 0; i < f.length; i++)
				{
					f[i].setAccessible(true);
					// 如果不是public的就继续下一轮的处理
//					if (!Modifier.isPublic(f[i].getModifiers()))
//		                continue;
					// 属性名
					if("request".equals(f[i].getName()) || 0 == f[i].getName().replaceAll(szFlter, "").length())
						continue;
					// 如果不是第一次
					if(0 < nPos)buf.append(",");
				    buf.append("\"").append(f[i].getName()).append("\":");
				    // 类型
				    String szType = f[i].getType().toString();
				    // 值
				    Object oValue = f[i].get(o);
				    
				    // 如果为null
				    if(null == oValue)
				    	buf.append("null");
				    else
				    {
					    // 如果是数组
					    Matcher m = pa.matcher(szType);
					    if(m.find()) 
					    {
					    	buf.append("[");
					    	// L是java对象数组，而不是简单类型的数组
					    	switch(m.group(1).charAt(0))
					    	{
					    	    case 'Z': // boolean []
					    	    	boolean []tmp01 = (boolean [])oValue;
					    	    	if(0 < tmp01.length)
					    	    	{
						    	    	buf.append(tmp01[0]);
						    	    	for(int j = 1; j < tmp01.length; j++)
						    	    		buf.append(",").append(tmp01[j]);
					    	    	}
					    	    	break;
					    	    case 'B': // byte []
					    	    	byte []tmp02 = (byte [])oValue;
					    	    	if(0 < tmp02.length)
					    	    	{
						    	    	buf.append(tmp02[0]);
						    	    	for(int j = 1; j < tmp02.length; j++)
						    	    		buf.append(",").append(tmp02[j]);
					    	    	}
					    	    	break;
					    	    case 'C': // char []
					    	    	char []tmp03 = (char [])oValue;
					    	    	if(0 < tmp03.length)
					    	    	{
						    	    	buf.append("'").append(tmp03[0]).append("'");
						    	    	for(int j = 1; j < tmp03.length; j++)
						    	    		buf.append(",'").append(tmp03[j]).append("'");
					    	    	}
					    	    	break;
					    	    case 'I': // int []
					    	    	int []tmp04 = (int [])oValue;
					    	    	if(0 < tmp04.length)
					    	    	{
						    	    	buf.append(tmp04[0]);
						    	    	for(int j = 1; j < tmp04.length; j++)
						    	    		buf.append(",").append(tmp04[j]);
					    	    	}
					    	    	break;
					    	    case 'S': // short []
					    	    	short []tmp05 = (short [])oValue;
					    	    	if(0 < tmp05.length)
					    	    	{
						    	    	buf.append(tmp05[0]);
						    	    	for(int j = 1; j < tmp05.length; j++)
						    	    		buf.append(",").append(tmp05[j]);
					    	    	}
					    	    	break;
					    	    case 'J': // long []
					    	    	long []tmp06 = (long [])oValue;
					    	    	if(0 < tmp06.length)
					    	    	{
						    	    	buf.append(tmp06[0]);
						    	    	for(int j = 1; j < tmp06.length; j++)
						    	    		buf.append(",").append(tmp06[j]);
					    	    	}
					    	    	break;
					    	    case 'F': // float []
					    	    	float []tmp07 = (float [])oValue;
					    	    	if(0 < tmp07.length)
					    	    	{
						    	    	buf.append(tmp07[0]);
						    	    	for(int j = 1; j < tmp07.length; j++)
						    	    		buf.append(",").append(tmp07[j]);
					    	    	}
					    	    	break;
					    	    case 'D': // double []
					    	    	double []tmp08 = (double [])oValue;
					    	    	if(0 < tmp08.length)
					    	    	{
						    	    	buf.append(tmp08[0]);
						    	    	for(int j = 1; j < tmp08.length; j++)
						    	    		buf.append(",").append(tmp08[j]);
					    	    	}
					    	    	break;
					    	    case 'L': // 其他对象数组
					    	    	Object []tmp09 = (Object [])oValue;
					    	    	if(0 < tmp09.length)
					    	    	{
					    	    		if(null != tmp09[0])
					    	    		buf.append(new ObjectToJSON(tmp09[0], brige).setBJh(bJh).toJSON(null));
						    	    	for(int j = 1; j < tmp09.length; j++)
						    	    	{
						    	    		if(null != tmp09[j])
						    	    		buf.append(",").append(new ObjectToJSON(tmp09[j], brige).setBJh(bJh).toJSON(null));
						    	    	}
					    	    	}
					    	    	break;
					    	}
					    	buf.append("]");
					    }
					    else
					    {
						    // 如果是简单类型
						    Matcher m1 = p.matcher(szType);
						    // m.reset();
						    if(m1.find())
						    {
						    	// 获取到简单类型变量的值
						    	buf.append(oValue);
						    }
						    // 对象类型
						    else
						    {
						    	// 防止使用者不知情的情况下注册一个非实例化的class文件导致堆栈溢出
						    	if("sun.reflect.ReflectionFactory".equals(oValue.getClass().getName()))
						    		buf.append("null");
						    	else
						    		buf.append(new ObjectToJSON(oValue, brige).setBJh(bJh).toJSON(null));
						    }
					    }
				    }
				    nPos++;
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		if(null != szObjName)
		{
			if(1 < buf.length())buf.append(",");
		    buf.append("_name_:\"").append(szObjName).append("\"");
		}
		if(!bJh)
		{
		if(1 < buf.length())buf.append(",");
		buf.append("\"_id_\":\"").append(this.o.hashCode()).append("\"");
		}
		return "{" + buf.append("}").toString();
	}

}
