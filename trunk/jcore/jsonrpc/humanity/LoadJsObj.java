package jcore.jsonrpc.humanity;

import java.io.InputStream;
import java.io.Serializable;

import jcore.jsonrpc.common.Content;
import jcore.jsonrpc.common.JsonRpcObject;
import jcore.jsonrpc.common.ResultObject;
import jcore.jsonrpc.common.face.IJsonRpcObject;
import jcore.jsonrpc.tools.Tools;

/***
 * 获取JS对象
 * @author 夏天
 *
 */
public class LoadJsObj extends JsonRpcObject implements IJsonRpcObject, Serializable{
	private static final long serialVersionUID = -1988214985561562945L;
	
	/***
	 * 获取一个未注册的java对象，该对象需要继承
	 * jcore.jsonrpc.common.JsonRpcObject
	 * 或实现接口jcore.jsonrpc.common.face.IJsonRpcObject
	 * 并有默认的构造函数
	 * @param szClassPathName
	 * @return
	 */
	public Object getRpcObj(String szClassPathName)
	{
		try
		{
			return (Class.forName(szClassPathName).newInstance());
		}catch(Exception e)
		{
			e.printStackTrace();
			log.debug(e);
		}
		return null;
	}
	
	/***
	 * 通过js对象名获取对象，区分大小写
	 * @param szName
	 * @return
	 * @throws Exception
	 */
	public ResultObject getJsObj(String szName) 
	{
		ResultObject oRst = new ResultObject();
		InputStream f = null;
		try
		{
			String szCharset = "UTF-8"; // UTF-8  GBK
			f = Tools.getResourceAsStream("jcore/jsonrpc/humanity/" + szName + ".js");
			if(null != f)
			{
				StringBuffer buf = new StringBuffer();
				byte []b = new byte[1024];
				int j = 0;
				boolean bFirst = true;
				while(1024 == (j = f.read(b, 0, 1024)))
				{
					if(bFirst)
					{
						bFirst = false;
						// UTF-8 文件头三个字节是：0xefbbbf
						if(-17 == b[0] && -69 == b[1] && -65 == b[2])
							b[0] = b[1] = b[2] = 0x20;
					}
					buf.append(new String(b, szCharset));
				}
				if(0 < j)
				{
					byte []b1 = new byte[j];
					System.arraycopy(b, 0, b1, 0, j);
					buf.append(new String(b1, szCharset));
				}
				// buf.toString().trim();//
			    String s = buf.toString().trim();// Content.JS(buf.toString().trim());// Content.JS(buf.toString().trim()).replaceAll("\\/\\*[^\\*]+\\*\\/", "");
				// String s = buf.toString().trim();
//				s = Content.JS(s);
//				s = s.replaceFirst("^\\\\ufeff", "");
			    
				s = s.replaceAll("\\t", "");
				s = s.replaceAll("\\s*=\\s*", "=");
				s = s.replaceAll("\\s*,\\s*", ",");
				s = s.replaceAll("\\/\\*[^\\r\\n\\*\\/]*\\*\\/", "");
				s = s.replaceAll("\\/\\/[^\\r\\n]\\n", "\\n");
				oRst.setResult(s);
			}
			else oRst.setErrMsg("指定的对象不存在，请确认大小写是否正确。");
		}catch(Exception e)
		{
			e.printStackTrace();
			log.debug(e);
			oRst.setErrMsg(e.getMessage());
		}
		finally
		{
			if(null != f)
				try{f.close();}catch(Exception e)
				{
					oRst.setErrMsg(e.getMessage());
				}
			f = null;
		}
		return oRst;
	}
}
