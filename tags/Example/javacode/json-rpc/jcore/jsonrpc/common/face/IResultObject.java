package jcore.jsonrpc.common.face;

public interface IResultObject {

	/***
	 * 获取错误、异常消息
	 * @return
	 */
	public String getErrMsg();
	
	/***
	 * 设置异常错误消息，当发生异常的时候框架自动抓取异常消息并通过它注入
	 * @param s
	 */
	public void setErrMsg(String s);
}
