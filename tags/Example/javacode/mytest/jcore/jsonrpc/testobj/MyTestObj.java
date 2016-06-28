package jcore.jsonrpc.testobj;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 返回复合测试对象
 * @author 夏天 QQ：11602011
 *
 */
public class MyTestObj {
	
	public MyTestObj(){}

	/**
	 * 测试级联调用使用
	 * @return
	 */
	public Object getSelf()
	{
		return this;
	}
	
	/**
	 * 测试级联调用使用，并返回符合对象
	 * @return
	 */
	public Object getList()
	{
		List lst = new ArrayList();
		lst.add(new String[]{"MyTestObj 1", "MyTestObj 2", "MyTestObj 3"});
		lst.add(new Object[]{new Integer(999), new Boolean(false), new java.math.BigDecimal(11602011.15)});
		Map m = new LinkedHashMap();
		m.put("哥哥是", "summer");
		m.put("key1", "哥在觅寻中....");
		m.put("key2", "哥会'回'来的！");
		lst.add(m);
		return lst;
	}
}
