package jcore.jsonrpc.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllSupersProperty {

	/**
	 * 去除不要的属性，去除重复的属性
	 * @param fRst
	 * @return
	 */
	public static Field [] cleanField(Field []fRst)
	{
		List lst = new ArrayList();
		Map m = new HashMap();
		if(0 < fRst.length)
		{
			for(int i = fRst.length; --i >= 0;)
			{
				String s = fRst[i].getName();
				if( s.equals("log") || 
					 s.equals("serialVersionUID") || 
					 s.equals("request") || 
					 -1 < s.indexOf("class$"))
					continue;
				if(null == m.get(s))
				{
					lst.add(fRst[i]);
					m.put(s, "1");
				}
			}
		}
		Field []mt = new Field[lst.size()];
		System.arraycopy(lst.toArray(), 0, mt, 0, mt.length);
		lst = null;
		return mt;
	}
	
	public static Field[] getDeclaredFields(Class c)
	{
		Field []fRst = c.getDeclaredFields();
		c = c.getSuperclass();
		while(!(c.getName().equals(Object.class.getName()) || c.getName().equals(Class.class.getName())))
		{
			Field []fRst1 = c.getDeclaredFields();
			Field []fRst2 = new Field[fRst.length + fRst1.length];
			System.arraycopy(fRst, 0, fRst2, 0, fRst.length);
			System.arraycopy(fRst1, 0, fRst2, fRst.length, fRst1.length);
			fRst = fRst2;
			fRst1 = null;
			c = c.getSuperclass();
		}
		return fRst;
	}
	
	public static Field[] getFields(Class c)
	{
		Field []fRst = c.getFields();
		Field []fRst1 = getDeclaredFields(c);
		
		Field []fRst2 = new Field[fRst.length + fRst1.length];
		System.arraycopy(fRst, 0, fRst2, 0, fRst.length);
		System.arraycopy(fRst1, 0, fRst2, fRst.length, fRst1.length);
		fRst = fRst2;
		c = c.getSuperclass();
		while(!(c.getName().equals(Object.class.getName()) || c.getName().equals(Class.class.getName())))
		{
			fRst1 = c.getFields();
			fRst2 = new Field[fRst.length + fRst1.length];
			System.arraycopy(fRst, 0, fRst2, 0, fRst.length);
			System.arraycopy(fRst1, 0, fRst2, fRst.length, fRst1.length);
			fRst = fRst2;
			fRst1 = null;
			c = c.getSuperclass();
		}
		
		return cleanField(fRst);
	}
	
	/**
	 * 去除不要的方法，去除重复的方法名
	 * @param fRst
	 * @return
	 */
	public static Method [] cleanMethod(Method []fRst)
	{
		List lst = new ArrayList();
		Map m = new HashMap();
		if(0 < fRst.length)
		{
			for(int i = fRst.length; --i >= 0;)
			{
				String s = fRst[i].getName();
				
				if( -1 < "getUs,getSimpleUidService,getBaseSystemService,getDao,getUser,getService(getRequest)(setRequest)()(notifyAll)|(getClass)|(wait)|(wait)|(equals)|(notify)|(main)|(hashCode)|(toString)".indexOf("(" + s + ")"))
					continue;
				if(null == m.get(s))
				{
					lst.add(fRst[i]);
					m.put(s, "1");
				}
			}
		}
		Method []mt = new Method[lst.size()];
		System.arraycopy(lst.toArray(), 0, mt, 0, mt.length);
		lst = null;
		return mt;
	}
    public static Method [] getMethods(Class c)
    {
    	Method []fRst = c.getMethods();
		c = c.getSuperclass();
		while(!(c.getName().equals(Object.class.getName()) || c.getName().equals(Class.class.getName())))
		{
			Method []fRst1 = c.getMethods();
			Method []fRst2 = new Method[fRst.length + fRst1.length];
			System.arraycopy(fRst, 0, fRst2, 0, fRst.length);
			System.arraycopy(fRst1, 0, fRst2, fRst.length, fRst1.length);
			fRst = fRst2;
			fRst1 = null;
			c = c.getSuperclass();
		}
		return cleanMethod(fRst);
    }
	
}
