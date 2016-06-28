package jcore.jsonrpc.common;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jcore.jsonrpc.common.face.IJsonRpcObject;
import jcore.jsonrpc.tools.Tools;

/***
 * 
 * @author 夏天
 *
 */
public class JSONRPCBridge implements Serializable{
	private static final long serialVersionUID = 1L;
	// 为了集群中使用内存复制使用
	private transient HttpSession session = null;
	
	public transient static String ObjIdMapName = "_ObjIdMapName_";
	// 注册中的对象
	private Map globalMap = Collections.synchronizedMap(new HashMap());
	
	// 缓存顶级的被注册对象的JSON格式
	private Map cache = Collections.synchronizedMap(new HashMap());
	// 顶级对象链路
	private Map links = Collections.synchronizedMap(new HashMap());
	// 顶级被注册的名字
	private Map topNms = Collections.synchronizedMap(new HashMap());
	
	/***
	 * 返回所有注册全局的对象为JSON的字符串格式
	 * @return
	 */
	public String getRegObjsToString()
	{
		StringBuffer buf = new StringBuffer("{\"result\":[");
		int n = 0;
		Iterator oIt = topNms.entrySet().iterator();
		while(oIt.hasNext())
		{
			if(0 < n)
				buf.append(",");
			Map.Entry oKey = (Map.Entry)oIt.next();
			
			// 为了提高性能，采用cache
			String szTmp  = (String)cache.get(oKey.getKey());
			if(null == szTmp)
			{
				// 只对顶级注册的进行获取, oKey.getValue()只是全局中的key
				szTmp = new ObjectToJSON(getObject(oKey.getValue().toString()), this).toJSON(oKey.getKey().toString());
				// szTmp = szTmp.replaceFirst("\\{", "{name:\"" + oKey.getKey() + "\",");
				cache.put(oKey.getKey(), szTmp);
				// 再次调用，以便集群环境下能正常工作
				if(null != session)
					session.setAttribute(Content.RegSessionJSONRPCName, this);
			}
			n++;
			buf.append(szTmp);
		}
		buf.append("]}");
		return buf.toString();
	}
	
	/***
	 * 构造函数，构造后一定要setSession
	 */
	public JSONRPCBridge(){}
	
	/***
	 * 构造的时候可以带上session对象
	 * @param session
	 */
	public JSONRPCBridge(HttpSession session)
	{
		this.session = session;
	}
	
	/***
	 * 注册父亲对象链路
	 * @param nSelfHashCode
	 * @param nParentHashCode
	 * @return
	 */
	public JSONRPCBridge registerParentObject(int nSelfHashCode, int nParentHashCode)
	{
		String szKeyName = nSelfHashCode + "";
		if(null == links.get(szKeyName))
		{
			// 注册
			links.put(szKeyName, nParentHashCode + "");
		}
		// 再次调用，以便集群环境下能正常工作
		if(null != session)
			session.setAttribute(Content.RegSessionJSONRPCName, this);
		return this;
	}
	
	/***
	 * 移除对象对应的顶级对象的注册信息
	 * @param nSelfHashCode
	 */
	public void removeParentRegInfo(int nSelfHashCode)
	{
		String szKeyName = nSelfHashCode + "";
		links.remove(szKeyName);
		
		// 移除子对象
		Iterator oIt = links.entrySet().iterator();
		while(oIt.hasNext())
		{
			Map.Entry oKey = (Map.Entry)oIt.next();
			String szChildId = (String)oKey.getValue();
			if(szKeyName.equals(szChildId))
			{
				links.remove(szChildId);
				removeObject(Integer.parseInt((String)oKey.getKey()));
			}
		}

		
		// 再次调用，以便集群环境下能正常工作
		if(null != session)
			session.setAttribute(Content.RegSessionJSONRPCName, this);
	}
	
	/***
	 * 获取对象的顶级对象
	 * @param nSelfHashCode
	 * @return
	 */
	public Object getParentObject(int nSelfHashCode)
	{
		String szKeyName = nSelfHashCode + "";
		int nRst = 0;
		while(0 == nRst)
		{
			Object o = links.get(szKeyName);
			if(null == o)
				break;
			nRst = Integer.parseInt((String)o);
		}
		if(0 < nRst)
			return this.getObject(nRst + "");
		return null;
	}
	
	/***
	 * 注册对象
	 * @param nHashCodeName 利用hashcode注册对象，防止同一实例注册多次
	 * @param o
	 * @return this
	 */
	public JSONRPCBridge registerObject(int nHashCodeName, Object o)
	{
		String szKeyName = nHashCodeName + "";
		if(null == globalMap.get(szKeyName))
		{
			// 注册
			globalMap.put(szKeyName, o);
		}
		// 再次调用，以便集群环境下能正常工作
		if(null != session)
			session.setAttribute(Content.RegSessionJSONRPCName, this);
		return this;
	}
	
	public JSONRPCBridge registerObject(String szKeyName, Object o)
	{
		if(null != szKeyName && 0 < szKeyName.trim().length())
		{
			if(null == topNms.get(szKeyName))
				topNms.put(szKeyName, o.hashCode() + "");
			return registerObject(o.hashCode(), o);
		}
		return this;
	}
	
	/***
	 * 移除注册的对象
	 * @param nHashCodeName 利用hashcode注册对象，防止同一实例注册多次
	 * @return this
	 */
	public JSONRPCBridge removeObject(int nHashCodeName)
	{
		String szKeyName = nHashCodeName + "";
		// 移除
		globalMap.remove(szKeyName);
		// 再次调用，以便集群环境下能正常工作
		if(null != session)
			session.setAttribute(Content.RegSessionJSONRPCName, this);
		return this;
	}
	
	/***
	 * 移除对象
	 * @param szKeyName
	 * @param o
	 * @return
	 */
	public JSONRPCBridge removeObject(String szKeyName, Object o)
	{
		if(null != szKeyName && 0 < szKeyName.trim().length())
		{
			topNms.remove(szKeyName);
			return removeObject(o.hashCode());
		}
		return this;
	}	
	
	/***
	 * 执行JSON-RPC请求的方法，并返回JSON格式的结果
	 * @param szParm
	 * @return
	 */
	public String ExecObjectMethod(HttpServletRequest request, String szParm)
	{
		try {
			if(null != this.session)
			   this.session.setAttribute(ObjIdMapName, new HashMap());
			String szPOld = szParm;
			 szParm = Tools.decodeUnicodeHtm(szParm);
			JSONObject oJson = null;
			try
			{
				oJson = new JSONObject(szParm);
			}catch(Exception e9){
				oJson = new JSONObject(szPOld);
			}
			// 所有界面中的输入对象参数
			try
			{
				request.setAttribute("allPms",  new JSONObject(oJson.getString("allPms")).getHashMap());
			}catch(Exception e10){
				// e10.printStackTrace();
			}
			String szName = oJson.getString("_id_"), 
			       szMeshod = oJson.getString("method");
			JSONArray oParams = (JSONArray)oJson.get("params");
			// 获取代理的对象
			Object o = getObject(szName);
			
			if(null != o)
			{
				int nParentHashCode = o.hashCode();
				// 获取对象的顶级对象
				Object oParent = this.getParentObject(nParentHashCode);
				if(null != oParent)
					nParentHashCode = oParent.hashCode();
				else oParent = o;
				
				// 如果是要求释放对象内存资源
				if("release".equals(szMeshod))
				{
					// 移除对象注册信息
					removeObject(oParent.hashCode());
					Iterator oIt = topNms.entrySet().iterator();
					while(oIt.hasNext())
					{
						Map.Entry oKey = (Map.Entry)oIt.next();
						if(szName.equals(oKey.getValue()))
						{
							topNms.remove(oKey.getKey());
							break;
						}
					}
					// 移除顶级对象注册信息
					removeParentRegInfo(oParent.hashCode());
					return "true";
				}
				
				Class c = o.getClass();
				// 获取对象的方法列表
				Method []m = c.getMethods();
				
				// 注入 reqeust 对象 start
				IJsonRpcObject json = null;
				if(Tools.isInterface(o.getClass(), IJsonRpcObject.class.getName()))
				{
					json =(IJsonRpcObject)o;
					json.setRequest(request);
				}
				// 注入 reqeust 对象 end
				
				// 这里不能采用getSpecifyNameMethod获取方法的原因是，因为参数可能有复合对象
				// 构造参数
				Object []aParam = null;
				// 函数参数类型
				Class []oTyps = null;
				Method mExec = null;
				for(int i = 0; i < m.length; i++)
				{
					// 函数名匹配，参数个数也必须同时匹配，才进行执行
					if(szMeshod.equals(m[i].getName()) && oParams.length() == m[i].getParameterTypes().length)
					{
						try {
							boolean bCnt = false;
							// 构造参数
							aParam = new Object[oParams.length()];
							// 函数参数类型
							oTyps = m[i].getParameterTypes();
							// 构造参数对象
							for(int j = 0; j < aParam.length; j++)
							{
								// 如果类型不匹配，就进行一系列转换
								// 将整数向日期进行转换
								// 数字类型进行松散匹配
								if(-1 == oParams.get(j).getClass().getName().toLowerCase().indexOf(m[i].getParameterTypes()[j].getName().toLowerCase()))
									bCnt = true;
								aParam[j] = Tools.convertObject(oTyps[j], aParam[j] = oParams.get(j));
							}
							mExec = m[i];
							if(bCnt)continue;
							oTyps = null;
							oParams = null;
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
				}
				// 方法寻找完之后再执行调用
				if(null != mExec)
				{
					Object oRst = null;
					try
					{
						// 循环做解码处理
						for(int i = 0; i < aParam.length; i++)
						{
							if(aParam[i] instanceof String)
								aParam[i] = Tools.decodeUnicodeHtm((String)aParam[i]);
						}
						oRst = mExec.invoke(o, aParam);
					} catch (Exception e) 
					{
						if(null != json)
						{
							String szErrMsg = e.getMessage();
							if(null == szErrMsg && null != e.getCause())
								szErrMsg = e.getCause().getMessage();
							json.setErrMsg(szErrMsg);
							szErrMsg = null;
						}
					}
					aParam = null;
					if(null != oRst)
					{
						// 不是简单类型就注册他
						if(!Tools.isSimpleType(oRst))
						{
							// 设置顶级对象
							registerObject(oRst.hashCode(), oRst).registerParentObject(oRst.hashCode(), nParentHashCode);
						}
						String szOut = new ObjectToJSON(oRst, this).toJSON(null);
						return szOut;
					}
					return "null";
				}
			}
		} catch (ParseException e) {e.printStackTrace();
		}
		return "false";
	}
	
	/***
	 * 根据注册路径获取注册对象，如果找不到就返回null
	 * @param szKeyName
	 * @return
	 */
	public Object getObject(String szKeyName)
	{
		return globalMap.get(szKeyName);
	}
	
	/***
	 * 获取被注册的对象
	 * @param szKeyName
	 * @return
	 */
	public Object getRegObject(String szKeyName)
	{
		return globalMap.get(topNms.get(szKeyName));
	}
	
	/***
	 * 设置session对象
	 * @param session
	 * @return
	 */
	public JSONRPCBridge setSession(HttpSession session) {
		this.session = session;
		return this;
	}

	public HttpSession getSession() {
		return session;
	}

}
