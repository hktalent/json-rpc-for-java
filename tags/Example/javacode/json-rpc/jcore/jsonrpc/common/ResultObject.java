package jcore.jsonrpc.common;

import java.io.Serializable;

import jcore.jsonrpc.common.face.IResultObject;

public class ResultObject implements IResultObject,Serializable {
	private static final long serialVersionUID = -7059298543537434669L;
	private String errMsg;
	public transient Object result = null;
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

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
