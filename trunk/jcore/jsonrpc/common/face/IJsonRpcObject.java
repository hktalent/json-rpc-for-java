package jcore.jsonrpc.common.face;

import javax.servlet.http.HttpServletRequest;

import jcore.jsonrpc.common.JsonRpcObject;

public interface IJsonRpcObject extends IResultObject{

	/***
	 * 当服务对象在请求使用的时候，由框架注入request对象
	 * @param r
	 * @return
	 */
	public JsonRpcObject setRequest(HttpServletRequest r);
	
	/***
	 * 获取request对象，通过它可以获取到session对象
	 * @return
	 */
	public HttpServletRequest getRequest();
	
	/***
	 * 释放资源
	 */
	public void release();
	
	/***
	  * 获取异常、错误消息使用
	  * @return
	  */
	public String getErrMsg();
	
	/***
	 * 设置异常错误消息
	 */
	public void setErrMsg(String s);
}
