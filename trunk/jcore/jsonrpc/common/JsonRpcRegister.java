package jcore.jsonrpc.common;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/***
 * 注册JsonRpc对象
 * @author just
 *
 */
public class JsonRpcRegister{
	
	/***
	 * 通过request来注册对象
	 * @param request
	 * @param szKeyName
	 * @param o
	 */
	public static void registerObject(HttpServletRequest request, String szKeyName, Class o)
	{
		HttpSession session = request.getSession(false);
		if(null == session)
			session = request.getSession(true);
		JSONRPCBridge brg = (JSONRPCBridge)session.getAttribute(Content.RegSessionJSONRPCName);
		// 如果是第一次就注册对象
		if(null == brg)
			 session.setAttribute(Content.RegSessionJSONRPCName, brg = new JSONRPCBridge().setSession(session));
		try
		{
			// 防止对象o多次实例化和注册
			if(null == brg.getObject(szKeyName))
				brg.registerObject(szKeyName, o.newInstance());
		}catch(Exception e)
		{}
	}
}
