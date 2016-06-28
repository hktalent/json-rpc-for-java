package jcore.jsonrpc.common;

import java.util.LinkedHashMap;
import java.util.Map;

import jcore.jsonrpc.tools.Tools;

public class MyMap extends LinkedHashMap {

	public MyMap(Map m)
	{
		super.putAll(m);
	}
	
	public Object get(Object key) 
	{
		Object o = super.get(key);
		if(o instanceof String)
			o = Tools.decodeUnicodeHtm((String)o);
		return o;
	}
}
