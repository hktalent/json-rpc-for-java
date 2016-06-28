<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.net.URLDecoder"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>My JSP 'index.jsp' starting page</title>
		<meta content="text/html; charset=UTF-8" http-equiv="content-type">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
	</head>

	<body>
		<script type="text/javascript">
   var contextPath = "<%=path%>";// 关键的地方
   </script>
		<script type="text/javascript" charset="UTF-8" src="JsonRpcClient.js"></script>
		<script type="text/javascript">
var i,a, o, k, myrpc = rpc.MyTestRpc;		
   
   function fnTest()
   {
      alert(myrpc.getTestMsg());
      a = myrpc.testGetList();
      // getMyObj返回的复合对象，再调用getList则是级联调用
      a = a.concat(myrpc.getMyObj().getList());
      // 由于getSelf返回的是自己，因此可以无限的调用下去
      alert("级联调用：" + myrpc.getMyObj().getSelf().getSelf().getSelf().getList()[2]["key2"]);
      
      for(i = 0; i < a.length; i++)
      {
         o = a[i];
         for(k in o)
         {
            alert(k + " = " + o[k]);
         }
      }
   }
   
   </script>普通输入对象：
		<input name="mytest1" value="第一个值">
		<input name="mytest2" value="第'er'颗值">
		<input name="mytest3" value='第"three value"'>
		<br>
		my checkbox: <input type="checkbox" name="mycheckbox" value="checkboxValue">
		my radio 001: <input type="radio" name="myradio" value="myradio001">
		my radio 002 : <input type="radio" name="myradio" value="myradio002">
		<br>当选下拉：
		<select name="mySelect1">
			<optgroup label="第一个组">
				<option value="夏天">
					夏天
				<option value="summer">
					summer
				<option value="الصيف">
					الصيف
			</optgroup>
			<optgroup label="第two个组">
				<option value="Sumar">
					Sumar
				<option value="تابستان">
					تابستان
				<option value="Tag-init">
					Tag-init
			</optgroup>
		</select><br>多选下拉：
		<select name="mySelect2" multiple="multiple" size="10" style="width:50%">
			<optgroup label="第一个组">
			<option value="夏天">
				夏天
			<option value="summer">
				summer
			<option value="الصيف" selected>
				الصيف
				</optgroup>
				<optgroup label="第two个组">
					<option value="Sumar" selected>
						Sumar
					<option value="تابستان">
						تابستان
					<option value="Tag-init" selected>
						Tag-init
				</optgroup>
		</select><br>
大文本输入：
		<textarea name="mytextarea" style="width:50%;height:80px">
   做做更多精彩的内容，请访问：
   http://jxpath.3322.org:82/my3dbarcode/my3DBarcode.html
   </textarea>
<br><br>
		<button onclick=fnTest()>
			普通调用、级联调用、获取界面输入对象测试
		</button>
<br><br>
<button onclick="alert(myrpc.setMyBean({name:'summer', age:10000}).name)">
传入JavaBean: myrpc.setMyBean({name:'summer', age:10000})
</button>
<br><br>
<button onclick="alert(myrpc.setMyMap({name:'summer', age:10000}).age)">
传入Map: myrpc.setMyMap({name:'summer', age:10000})
</button>
</body>
</html>