<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

jcore.jsonrpc.common.JsonRpcRegister.registerObject(request, "myjsonrpc", test.TestObject.class);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>测试JSON-RPC for java</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
<script type="text/javascript" src="./resource/javascript/JsonRpcClient.js"></script>
  </head>
  <body><h1 style="text-align:center;">JSON RPC For JAVA AJAX 轻量级级联调用框架示例</h1>
<script type="text/javascript">
var json = JsonRpcClient("JRPC"), myjsonrpc = json.myjsonrpc;
var oTestDomain = myjsonrpc.getMyObj(); // myjsonrpc.myList[1]
</script>
<pre style="width:100%;height:350px;overflow:auto" id="myTextArea">
// TestDomain.java
package test;

import java.io.Serializable;

public class TestDomain implements Serializable{
	private static final long serialVersionUID = 1L;
	private String myName = "夏天";
	private String sex = "男";
	private int age = 10000;
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public String toXml()
	{
		return new StringBuffer()
		.append("<name>").append(getMyName()).append("</name>")
		.append("<sex>").append(getSex()).append("</sex>")
		.append("<age>").append(getAge()).append("</age>")
		.toString();
	}
	public String getMyName() {
		return myName;
	}
	public void setMyName(String myName) {
		this.myName = myName;
	}
	public void makeException() throws Exception
	{
		throw new Exception("让框架自动捕获错误消息");
	}

}

// TestObject.java
package test;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

</pre>
<style type="text/css">
p{cursor:pointer}
</style>
<p onclick="alert(oTestDomain.getMyName())">调用TestDomain.java中的getMyName,代码：alert(oTestDomain.getMyName())</p>
<p onclick="myjsonrpc.getStr(function(s){alert(s)});">调用TestObject.java中的getStr,代码：myjsonrpc.getStr(function(s){alert(s)});</p>
<p onclick="oTestDomain.setAge(999999)">调用TestDomain.java中的setAge,代码：oTestDomain.setAge(999999)</p>
<p onclick="oTestDomain.setMyName('风情主人 QQ: 11602011')">调用TestDomain.java中的setMyName,代码：oTestDomain.setMyName('风情主人 QQ: 11602011')</p>
<p onclick="alert(oTestDomain.toXml())">调用TestDomain.java中的toXml,代码：alert(oTestDomain.toXml())</p>
<p onclick="oTestDomain.makeException(),alert(myjsonrpc.getErrMsg())">测试自动捕获错误消息，调用TestDomain.java中的makeException,代码：oTestDomain.makeException(),alert(myjsonrpc.getErrMsg())</p>
<p onclick="alert(myjsonrpc.getMap()['p2'])">获取Map对象，代码：alert(myjsonrpc.getMap()['p2'])</p>
<p onclick="alert(myjsonrpc.getList()[1].toXml())">获取List对象，代码：alert(myjsonrpc.getList()[1].toXml())</p>
<b>符合参数传入的测试</b><br/>
<script type="text/javascript">
function testFh()
{
  try{
var oMap = myjsonrpc.setInputMap(
'myKey1', 9999,
{mapKey1:'Map中的第一个值', mapkey2:4444},
'myKey2', {sex:'未知', age:11111},
'myKey3', "" + new Date().getTime(),
'myKey4', true,
'myKey5', 'X'); 

alert( [oMap['myKey2'].sex, 
        oMap['mapKey1'], 
        oMap['myKey3'], 
        oMap['myKey4'], 
        oMap['myKey5']].join('\n'));
}catch(e){alert(e.message)}
}

var o = json.LoadJsObj("Base");
// 自动保存滚动条的位置
o.autoSaveScroll("myTextArea");
</script>
<pre>
<p onclick="testFh()">
var oMap = myjsonrpc.setInputMap(
'myKey1', 9999,
{mapKey1:'Map中的第一个值', mapkey2:4444},
'myKey2', {sex:'未知', age:11111},
'myKey3', "" + new Date().getTime(),
'myKey4', true,
'myKey5', 'X'); 

alert( [oMap['myKey2'].sex, 
        oMap['myKey1']['mapKey1'], 
        oMap['myKey5'], 
        oMap['myKey4']].join('\n'));
</p></pre>

<p onclick="myjsonrpc.release()">释放注册的对象，释放后上面的调用将发生错误,代码：myjsonrpc.release()</p>

  </body>
</html>
