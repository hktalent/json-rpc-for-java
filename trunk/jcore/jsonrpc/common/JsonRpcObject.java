package jcore.jsonrpc.common;
import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jcore.jsonrpc.common.face.IJsonRpcObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***
 * JSON-RPC 服务对象
 * @author 夏天
 *
 */
public abstract class JsonRpcObject implements IJsonRpcObject,Serializable
{
	private transient HttpServletRequest request = null;
	public transient Log log = LogFactory.getLog(JsonRpcObject.class);
   
	public JsonRpcObject(){}

	
	/**
	 * 获取所有的界面中所有输入对象的值，
	 * @return
	 */
	public Map getAllInputParms()
	{
		return new MyMap((Map)getRequest().getAttribute("allPms"));
	}
	
	
	public JsonRpcObject setRequest(HttpServletRequest r)
	{
		this.request = r;
		return this;
	}

	/***
	 * 获取Request对象
	 * @return
	 */
	public HttpServletRequest getRequest()
	{
		return this.request;
	}
	
	public String errMsg = "";
	
	 /***
	  * 获取异常、错误消息使用
	  * @return
	  */
	public String getErrMsg()
	{
		String s = errMsg;
		errMsg = "";
		return s;
	}
	
	/***
	 * 设置异常错误消息
	 */
	public void setErrMsg(String s)
	{
		errMsg = s;
	}

	/* 释放资源
	 */
	public void release(){
	}

}
