package jcore.jsonrpc.common.face;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ISecureCheck extends Serializable {

	/***
	 * 异步安全检查接口
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean secureCheck(HttpServletRequest request, HttpServletResponse response);
}
