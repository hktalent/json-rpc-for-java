package test;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jcore.jsonrpc.common.JsonRpcObject;

public class TestObject extends JsonRpcObject implements Serializable{
	private static final long serialVersionUID = 1L;

	private List myList = new ArrayList();
	private Map map = new HashMap();
	// 测试入口参数为Map
	private Map inputMap = null;
	
	/***
	 * 综合参数的测试
	 * @param szKey1
	 * @param ln01
	 * @param inMap
	 * @return
	 */
	public Map setInputMap(String szKey1, Long ln01,
			Map inMap, // Map的测试 
			String szKey2, TestDomain domain, // JavaBean对象的测试
			String szKey3, Date oDate ,// 日期对象的处理，日期传入以整数方式传入
			String szKey4, Boolean bIn, // Boolean的入口参数测试
			String szKey5, Character ch // Character类型的测试
			)
	{
		inputMap = inMap;
		inputMap.put(szKey1, ln01);
		inputMap.put(szKey2, domain);
		inputMap.put(szKey3, oDate);
		inputMap.put(szKey4, bIn);
		inputMap.put(szKey5, ch);
		HttpServletRequest request = this.getRequest();
		return inputMap;
	}
	
	public TestObject()
	{
		myList.add("good");
		myList.add(new TestDomain());
		// map中也可以放入复合对象
		map.put("first", "第一条值");
		map.put("p2", new Date());
		map.put("domain", myList.get(1));
	}
	
	/***
	 * 返回Map对象
	 * @return
	 */
	public Map getMap()
	{
		return map;
	}
	
	/***
	 * 获取一个普通对象
	 * @return
	 */
	public Object getStr()
	{
		return myList.get(0);
	}
	
	/***
	 * 获取一个复合对象
	 * @return
	 */
	public Object getMyObj()
	{
		return myList.get(1);
	}
	
	/***
	 * 获取List对象
	 * @return
	 */
	public List getList()
	{
		return myList;
	}
}
