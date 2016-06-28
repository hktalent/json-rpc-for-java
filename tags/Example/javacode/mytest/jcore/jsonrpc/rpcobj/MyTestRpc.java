package jcore.jsonrpc.rpcobj; // 必须是jcore.jsonrpc.rpcobj包下才可以免注册

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jcore.jsonrpc.common.JsonRpcObject;
import jcore.jsonrpc.testobj.MyBean;
import jcore.jsonrpc.testobj.MyTestObj;

public class MyTestRpc extends JsonRpcObject {
	/**
	 * 返回普通的类型
	 * 直接使用类名MyTestRpc就可以调用，如下： 调用：rpc.MyTestRpc.getTestMsg()
	 * 
	 * @return
	 */
	public String getTestMsg() {
		// 获取界面中所有的输入对象值
		Map m = this.getAllInputParms();
		StringBuffer buf = new StringBuffer("噢，成功了！\n获取来自界面输入对象的值：\n\n");
		Iterator i = m.entrySet().iterator();
		while(i.hasNext())
		{
			Map.Entry en = (Map.Entry)i.next();
			buf.append(en.getKey()).append(" = ").append(en.getValue()).append("\n");
		}
		return buf.toString();
	}
	
	/**
	 * 返回List
	 * @return
	 */
	public List testGetList()
	{
		List lst = new ArrayList();
		lst.add(new String[]{"good1", "测试2", "测试4"});
		lst.add(new Object[]{new Integer(234), new Boolean(true), new java.math.BigDecimal(3444.454345)});
		return lst;
	}
	
	/**
	 * 返回复合对象
	 * @return
	 */
	public Object getMyObj()
	{
		return new MyTestObj();
	}
	
	public Object setMyBean(MyBean o)
	{
		Map m = new HashMap();
		m.put("name", o.getName());
		m.put("age", new Integer(o.getAge()));
		return m;
	}
	
	public Object setMyMap(Map o)
	{
		Map m = new HashMap();
		m.put("name", o.get("name"));
		m.put("age", o.get("age"));
		return m;
	}
}