/*
 * 版权所有归《JavaScript高级应用与实践》(电子工业出版社.博文视点)的作者夏天所有
 * V1.3 支持任何语言编码
 * */
function JsonRpcClient(url) {
	url || (url = ("undefined" == typeof contextPath ? "." : contextPath) + "/JRPC");
	if (this == window)
		return JsonRpcClient._cache || (JsonRpcClient._cache = new JsonRpcClient(url));
	this["url"] = url;
	var _this = this, obj = {}, bind = function (f, o) {
		return function () {
			return f.apply(o, arguments);
		};
	}, _A = function (p) {
		var r = [], i = 0, j = p.length;
		for (; i < j; i++)
			r.push(p[i]);
		return r;
	}, AJAX = function (o) {
		if (window == this)
			return new AJAX(o);
		var _this = this, lct = document.location, fncbk = function(){if (_this.xml && 4 == _this.xml.readyState) {
			(200 == _this.xml.status || 404 == _this.xml.status || 500 == _this.xml.status) && o.clbkFun && o.clbkFun(_this.xml.responseText.replace(/&#(\d+);/gm, function()
			{
			   return String.fromCharCode(arguments[1]);
			})), _this.xml && (delete _this.xml.onreadystatechange, delete _this.xml);
	    }};
	    if(-1 == o.url.indexOf("http:"))o.url = [lct.protocol, "//", lct.host].join("") + o.url;
		if (this.xml = window.ActiveXObject ? new ActiveXObject("Microsoft.XMLHTTP") : new XMLHttpRequest()) {
			o.bAsync && (this.xml.onreadystatechange = function () {
				fncbk();
			});
			this.xml.open("POST", o.url + (-1 < o.url.indexOf("?") ? '&' : '?')
			 + "xui="+ new Date().getTime()
			// + ("undefined" != typeof g_szJsessionid ? "&jsessionid="+ g_szJsessionid : "")
			 , o.bAsync, "", "");
			this.xml.setRequestHeader("XUIAJAX",1);
			this.xml.setRequestHeader("CMHS","JsonRpc");
			this.xml.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			this.xml.send(o.data && o.data.replace(/[\u4E00-\u9FA5]/gm, function()
			{
			   return "&#" + arguments[0].charCodeAt(0) + ";";
			}) || "");
			if(!o.bAsync)
			  fncbk();
		}
	};this.AJAX = AJAX;
	AJAX({url:url, bAsync:false, clbkFun:function () {
		try {
			eval("obj = " + arguments[0]);
		}catch (e) {}
	}});
	obj = obj.result;
	/* 获取界面所有输入对象并进行传递到后台2010-4-18 */
	var fnGetAllIpt = function(){
	    var tps, i = 0,j, b, aIps = _A(document.getElementsByTagName("input")).concat(_A(document.getElementsByTagName("select"))).concat(_A(document.getElementsByTagName("textarea"))), oAs = {};
	    for(; i < aIps.length; i++)
	    {
	       if(!aIps[i].name)continue;
	       tps = aIps[i].type;
	       if("select-multiple" == tps)
	       {
	          var vs = [];
	          b = aIps[i].getElementsByTagName("option");
	          for(j = 0; j < b.length; j++)if(b[j].selected)vs.push(encodeURIComponent(b[j].value));
	          oAs[aIps[i].name] = vs.join(",");
	       }
	       else if((b = "checkbox" == tps || "radio" == tps) && aIps[i].checked || !b)
	       {
	          if(aIps[i].name)oAs[aIps[i].name] = encodeURIComponent(aIps[i].value); 
	       }
	    }aIps = null;
	    return oAs;
	},fnTmp = function (oTmp) {
					if ("number" == (szTp = typeof oTmp)) {
						return isFinite(oTmp) ? oTmp : 0;
					} else {
						if ("boolean" == szTp || null == oTmp) {
							return oTmp;
						} else {
							k = {"\r":"", "\n":"\\n", "\t":"\\t", "\b":"\\b", "\f":"\\f", "\"":"\\\""};
							return "'" + encodeURIComponent(oTmp || "").toString().replace(/([\r\n\t\b\f"])/gm, function (a, b) {
								return "\\" + k[b];
							}) + "'";
						}
					}
				}, o2json = function (oTmp1) {
				var k, aTmp = [];
				if ("object" == typeof oTmp1 && oTmp1) {
					for (k in oTmp1) {
						oTmp1[k] && aTmp.push("'" + k + "':" + fnTmp(oTmp1[k]));
					}
					return "\"{" + aTmp.join(",").replace(/([\r\n\t\b\f"])/gm, "\\$1") + "}\"";
				} else {
					return fnTmp(oTmp1);
				}
			},fnRpcCall = function () {
		var params = _A(arguments), cbk = params[0], bAsync = "function" == typeof (cbk || ""), oRst = {};
		bAsync && params.shift();
		AJAX({url:this.url, bAsync:bAsync, data:"{\"allPms\":" + o2json(fnGetAllIpt()) + ",\"method\":\"" + this.methodName + "\",\"_id_\":\"" + this["_id_"] + "\",\"params\":" + (function (arg) {
			var b = [], szTp;
			for (var i = 0; i < arg.length; i++)
			    b.push(o2json(arg[i]));
			return "[" + b.join(",") + "]";
		})(params) + "}", clbkFun:function () {
			try {
				eval("var oTmp = " + arguments[0]);
				if (null != oTmp && "object" == typeof oTmp) {
					if (Array == (oTmp["constructor"] || "")) {
						oRst = [];
						for (var i = 0; i < oTmp.length; i++)
							if ("object" == typeof oTmp[i])
								_this.fnMakeObj(oTmp[i], oRst[i] = {});
							else oRst[i] = oTmp[i];
					} else _this.fnMakeObj(oTmp, oRst);
				} else oRst = oTmp;
				bAsync && cbk.apply(oRst, [oRst]);
			}catch (e){}
		}});
		return oRst;
	};
	this.fnMakeObj = function (o, oRstObj) {
		var oT = oRstObj;
		o._name_ && (oT = (oRstObj[o._name_] = {}));
		for (var k in o) {
			if ("methods" == k) {
				for (var i = o[k].length - 1; i >= 0; i--)
					oT[o[k][i]] = bind(fnRpcCall, {url:_this.url, methodName:o[k][i], "_id_":o["_id_"]});
				delete o[k];
			} else {
				if (o[k] && "object" == o[k]["constructor"])
					o[k]["_name_"] = k, _this.fnMakeObj(o[k], oT);
				else oT[k] = o[k];
			}
		}
	};
	if(obj)for (var i = 0; i < obj.length; i++)this.fnMakeObj(obj[i], _this);
	this.cacheObj = [];this.LoadJsObj = function(s)
	{
	  if("undefined" == typeof _this._LoadJsObj)return this;
	  eval("var o = window." + s + ";");
	  if(o)return o;
	  try{o = _this.cacheObj[s] || (_this.cacheObj[s] = eval("1," + _this._LoadJsObj.getJsObj(s).getResult()))}catch(e){};
	  if(o)
	  {
	     if(o.init) o = o.init();
	     else
	     {
	       if(Base)
	        for(var k in Base)
	          if(!o[k])o[k] = Base[k];
	     }
	     _this.cacheObj[s] = o;
	     eval("window." + s + "=o;");
	  }
	  return o
	};
	/*
	 * 获取一个未注册的java对象，该对象需要继承, jcore.jsonrpc.common.JsonRpcObject
	 * 或实现接口jcore.jsonrpc.common.face.IJsonRpcObject,并有默认的构造函数
	 */
	this.getRpcObj = function(s)
	{
	   return _this._LoadJsObj.getRpcObj(s);
	};
}
var rpc = JsonRpcClient()
    /*,XUI = function()
    {
        var o = "undefined" == typeof Base && rpc.LoadJsObj("Base") || Base, a = o.A(arguments).concat([o]), k, i, p = a[0];
        for(i = 1; i < a.length; i++)
           for(k in a[i])if(!p[k])p[k] = a[i][k];
        return p;
    }*/;