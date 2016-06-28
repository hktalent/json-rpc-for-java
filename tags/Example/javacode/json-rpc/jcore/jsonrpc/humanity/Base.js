{
  bIE: false,
  nVer: 0,
  replaceHtml: function(el, html) {
	var oldEl = typeof el === "string" ? document.getElementById(el) : el;
	if(this.isIE)return Base.clearChldNd(oldEl).innerHTML = html, oldEl;
	var newEl = oldEl.cloneNode(false);
	Base.clearChldNd(newEl).innerHTML = html;
	oldEl.parentNode.replaceChild(newEl, oldEl);
	return newEl;
},/* 获取top中的菜单数据 */
getMenuData:function()
{
   return window.topMenu || (window.topMenu = top.frames[0].topMenu);
},/* 防止内存泄漏 */
clearChldNd:function(o)
{
    var i, t, k;
    for(i = o.childNodes.length; --i >= 0;)
    {
        t = o.childNodes(i);
        /* 清除属性 */
        if(t.clearAttributes)t.clearAttributes();
        if(t.childNodes && 0 < t.childNodes.length)Base.clearChldNd(t);
        o.removeChild(t);
    }
    return o;
}
,/* 根据输入对象,获取输入对象布局的div对象 */
getInputDiv:function(o)
{
   var _t = Base;
   if(o)return $(_t.p(_t.p(_t.getObj(o)[0], "DIV", 10), "DIV", 3));
   return o;
},/* 将容器中的对象滚动到可见区域 c:容器对象或id, a要求可见对象或id，b深度递归处理，n限定深度*/
fnSciv:function(c, a, b, n)
{
		if("string" == typeof c)
		c = $("#" + c)[0], a = $("#" + a)[0];
		if(0 == (c.scrollTop = a.offsetTop))
		   c.scrollTop = $(a).position().top;
		c.scrollTop = c.scrollTop;
		if(!c.scrollTop)return;
		if((b || n && 0 < --n) && "BODY" != c.nodeName)
		  this.fnSciv(c.parentNode, c, b);
},/* 弹出消息提示 */
PopMsgWin:function(o)
{
   if(!o)return false;
   alert(o);
},/* 异步更新指定property或者id的对象，包括：输入对象、panel、grid */
AjaxUpdateUi: function(szProperty, szReqCode, szUrl, szData, szDesId, isAsync,callBackFn)
{
   mkClctDt();   
   var form, reqCode, a = arguments, cbk = a[a.length - 1], bHvCbk = "function" == typeof cbk;
   if(bHvCbk)
   {
	   callBackFn = cbk;
	   if(2 == a.length)szReqCode = null;
	   else if(3 == a.length)szUrl = null;
	   else if(4 == a.length)szData = null;
	   else if(5 == a.length)szDesId = null;
	   else if(6 == a.length)isAsync = false;
   } 
   if (!szUrl && (form = $("form:first")[0])
            && form.action && "undefind" != typeof 
              (reqCode = $("input[name=reqCode]")[0]).value){
     szUrl = form.action + "?" + "reqCode=" + (szReqCode || reqCode.value);
   } else {
     var separator = szUrl &&  -1 < szUrl.indexOf("?") ? "&" : "?";
     szReqCode && (szUrl = contextPath + szUrl + separator + "reqCode=" + szReqCode) || (szUrl = contextPath + szUrl);  
   }
   szData || (szData = ":input:not(:checkbox[@checked=false])");
   var _t = this;
   if(szReqCode)Base.setValue("reqCode", szReqCode);
   $(document).ready(function()
   {
     var obj = szProperty && _t.getObj(szProperty) || $(document), szId;
     obj.attr('id', szId = obj.attr('id') || szProperty);
     ("undefined" == typeof isAsync)&&(isAsync = !szDesId);
     if(!szReqCode && obj.attr('reqCode'))Base.setValue("reqCode", szReqCode = obj.attr('reqCode'));
     _t.updateUi({url:szUrl,bAsync: isAsync,postData:[_t.getAllInput(szData)],data:[[szDesId || szId,1,""]],fn:function(s){
        var o = null, szStyle = "";  
        if(0 < obj.length)
          o = "INPUT" == obj[0].nodeName ? $(_t.p(obj[0],"DIV")).parent("div") : obj;
        var script = "", n = s.indexOf("<script");
        if(-1 < n) 
        {
           script = s.substr(n);
           script = script.substr(0, script.lastIndexOf("</" + "script>"));
           script = script.replace(/^\s*<script[^>]*>\s*<!--\/\/--><!\[CDATA\[\/\/><!--/, "");
           script = script.replace(/\/\/--><!\]\]>\s*$/, "");
           s = s.substr(0, n);
        }
         if(!szDesId)
        {
          if(-1 < (n = s.indexOf(">")))
          {
               szStyle = s.substr(0, n);
               if(-1 < (n = szStyle.indexOf("style=\"")))
               {
                 szStyle = szStyle.substr(n + 7);
                 szStyle = szStyle.substr(0, szStyle.indexOf('"'));
               }else szStyle = ""; 
          }
          s = s.replace(/^\s*<div[^>]*>/mi, "");
          s = s.substr(0, s.lastIndexOf("</div>"));
        }
        try{
          if(script){if(false == isAsync)eval(script);else setTimeout(function(){eval(script)}, 777)};
        }catch(e){
          alert("\u5f02\u6b65\u8c03\u7528\u9519\u8bef:\u6267\u884c\u8fd4\u56de\u7684\u811a\u672c\u51fa\u9519" + ",\u9519\u8bef\u6d88\u606f\u662f:" + e.message);
        }        
        if ("undefined" == typeof Base.PopMsgWin.obj || 3 != Base.PopMsgWin.obj.type ){
        if(o && "#document" != o.attr("nodeName") && s){Base.clearChldNd(o[0]).innerHTML = s;if(szStyle)o.attr('style', szStyle);}
		if($.isFunction(callBackFn))callBackFn();
        }
    }});
   }); 
   if(false == isAsync){return (Base.PopMsgWin.obj||{}).type;}
},
/*控制是否异步执行的简便方法， id:需要异步刷新的id，isAsync:是否异步标志,true为异步,false为同步*/
AjaxSyn : function(id, isAsync){
  return this.AjaxUpdateUi(id, null, null, null, null, isAsync);
},
AjaxTab: function(tabId, szReqCode, url, data, destId,szCallBackFn){
   mkClctDt();
   var _t = this, separator = url &&  -1 < url.indexOf("?") ? "&" : "?";
   (szReqCode && (url = contextPath + url + separator + "reqCode=" + szReqCode)) || (url = contextPath + url);
   data || (data = ":input:not(:checkbox[@checked=false])");
   if(szReqCode)Base.setValue("reqCode", szReqCode);
   $(document).ready(function(){
     var szId = tabId + "_body";
     _t.updateUi({url:url,bAsync: !destId,postData:[_t.getAllInput(data)],data:[[destId || szId,1,""]],fn:function(s){
        var o =  _t.getDom(szId);
        var script = "", n = s.indexOf("<script");
        if(-1 < n)
        {
           script = s.substr(n);
           script = script.substr(0, script.lastIndexOf("</" + "script>"));
           script = script.replace(/^\s*<script[^>]*>\s*<!--\/\/--><!\[CDATA\[\/\/><!--/, "");
           script = script.replace(/\/\/--><!\]\]>\s*$/, "");
        }
        if(!destId)
        {
          s = s.replace(/^\s*<div[^>]*>/mi, "");
          s = s.substr(0, s.lastIndexOf("</div>"));
        }
        if(s && -1 < s.indexOf("<div"))Base.clearChldNd(o).innerHTML = s;
        if($.isFunction(szCallBackFn))szCallBackFn();
        try{script && eval(script)}catch(e){alert("异步调用错误:执行返回的脚本出错" + ",错误消息是:" + e.message);}
     }});
   });
},
fsubmit:function(n, oWin, bNLd)
{
    if(false != bNLd)this.XuiLoading();
    mkClctDt();
 	$(":input[type=button]").each(function(){
    	$(this).attr("disabled",true);
    	$(Base.p(this, "TABLE")).addClass("z-btn-dsb");
    });
    (oWin || window).document.forms[n || 0].submit();
},getObj: function(s)
{
   var o = document.getElementsByName(s);
   if(o && 0 < o.length)return $(Base.A(o));
   o =  document.getElementsByName("dto(" + s + ")");
   if(o && 0 < o.length)return $(Base.A(o));
   o = $("#" + s);
   if(0 < o.length)return o;
   return s;
}, /* 隐藏指定名字或id的对象 */
hideObj:function(szNameOrId)
{
  var o = $("#" + szNameOrId);
  if(0 < o.length)
     o.hide();
  else
  {
     o = $(":input[@name=" + szNameOrId + "]");
     if(0 == o.length)o = $(":input[@name=dto(" + szNameOrId + ")]");
     if(0 < o.length)
     {
	     o = Base.p(o[0], "DIV");o = Base.p(o, "DIV");
	     o = $(o);
	     if(0 < o.length)o.hide();
     }
  }
},  /* 显示指定名字或id的对象 */
showObj:function(szNameOrId)
{
  var o = $("#" + szNameOrId);
  if(0 < o.length)
     o.show();
  else
  {
     o = $(":input[@name=" + szNameOrId + "]");
     if(0 == o.length)o = $(":input[@name=dto(" + szNameOrId + ")]");
      if(0 < o.length)
      {
	     o = Base.p(o[0], "DIV");o = Base.p(o, "DIV");
	     o = $(o);
	     if(0 < o.length)o.show();
     }
  }
}
,/* 传递Xpath szData指定的数据，并更新flash区域的查询 */
doUpdateCollection:function(szCollectionId, szData, szReqCode)
{
  var o, _t = this,s, argF = arguments[arguments.length - 1];
  $(document).ready(function(){
     setTimeout(function(){
     o = szCollectionId.swf();
     mkClctDt();
      if($.isFunction(argF))
      {
         window[s = szCollectionId + "DataChgCbk"] = argF;
   	     szCollectionId.setDataCmpltCbk(s);
   	  }
      if(o && o.doUpdateCollection){o.doUpdateCollection(_t.getAllInput(szData),szReqCode || null);}
     else window[szCollectionId+"S_cache"]=function(){_t.doUpdateCollection(szCollectionId,szData,szReqCode);}
     },777);
   });
},getAllInput:function(s)
{
   var a = [], _t = Base,o = $(s || ":input:not(:checkbox[@checked=false])"), ecd = _t.decodeStr;
   if(0 < o.size())
   o.each(function(){
      if ("checkbox" == this.type && false == this.checked)return true; //排除没有勾选的checkbox
      if ("radio" == this.type && false == this.checked)return true;    //排除没有选择的radiobox
      if(this.name)
      	s = encodeURIComponent(ecd($(this).val())), a.push(this.name + "=" + s);
   });
   else{
      var p = s.split("&"), u;
      for(var i = 0; i < p.length; i++)
      {
        u = p[i].split("=");
        if(u[0])
        s = encodeURIComponent(ecd(u[1])),a.push(u[0] + "=" + s);
      }
   }
   return a.join("&");
},
/* Ajax转转统一控制 */
XuiLoading:function(o)
{
   /*if("undefined" != typeof Base.AjaxObj)
   (Base.myMask = new Ext.LoadMask(Base.AjaxObj, {msg:"Please wait..."})).show();*/
},
  init: function()
  {
      if("undefined" != typeof Base)return this;
      Array.prototype.each = function(f){var t = this, i = 0;for(;i < t.length; i++)f.apply(t[i], [t[i]]);return this};
       /* 计时 */
     $(window).unload(function(){
        top.g_nPgCntTm || (top.g_nPgCntTm = 0);
        if(top.g_nPgCntTm < new Date().getTime())
        top.g_nPgCntTm = new Date().getTime()
        setTimeout(function(){var o = getCcObj();
        if(o && "function" == typeof o.getCodeList)o.getCodeList()},13);
     }).load(function(){
        var  n = new Date().getTime();
        "undefined" == typeof top.g_nPgCntTm && (top.g_nPgCntTm = n);
        "undefined" == typeof g_nJspTmCnt && (g_nJspTmCnt = 0);
        top.status = "\u9875\u9762\u52a0\u8f7d\u7528\u65f6: " +  g_nJspTmCnt + "/" + ((n - top.g_nPgCntTm) / 1000) + "\u79d2";
     });
     
      if(-1 == String(window.alert).indexOf("g_fcsfld"))
      {
      window.alt = window.alert;
      window.cfm = window.confirm;
      window.alert = function(o)
      {setTimeout(function(){
          if("undefined" != typeof g_fcsfld && g_fcsfld)g_fcsfld.setFocus(),g_fcsfld = null;
          if(!("object" == typeof o && null != o && o.hasOwnProperty('message')))return window.alt(o);
          var fnTmp = window.alt;
          if(0 < o.type)
          {
              if(!(o.okScript || o.okUrl || o.errScript || o.errUrl))return window.alt(o.message);
              if(window.cfm(o.message))
              {
                  if(o.okScript){
                    if("function" == typeof o.okScript)o.okScript();
                    else eval(o.okScript);
                  }
                  if(o.okUrl)location.href = contextPath + o.okUrl;
              }
              else 
              {
                 if(o.errScript)
                 {
                    if("function" == typeof o.errScript)o.errScript();
                    else eval(o.errScript);
                 }
                 if(o.errUrl)location.href = contextPath + o.errUrl;
              }
          }
          else
          {
              alt(o.message);
              if(o.okScript){
                    if("function" == typeof o.okScript)o.okScript();
                    else eval(o.okScript);
               }
              if(o.okUrl)location.href = contextPath + o.okUrl;
          }
          }, 13);
      };
       window.confirm = function(s, fn, fn1)
      {
         if(1 == arguments.length)return window.cfm(s);
         var o = {type:1, message:String(s)};
         if(fn)o.okScript = fn;if(fn1)o.errScript = fn1;
         return Base.PopMsgWin(o);
      };window.opn = window.open;
      window.open = function(s, t,p)
      {
          var i,a = (p||"").toLowerCase().replace(/\s/g, '').split(","), b, g = "=", u = s.split(/\?/);
          for(i = 0; i < a.length; i++)
          {
             b = a[i].split(g);
             if(2 == b.length)
             {
                switch(b[0])
                {
                   case "left":b[0] = "dialogLeft";break;
                   case "top":b[0] = "dialogTop";break;
                   case "width":b[0] = "dialogWidth";break;
                   case "height":b[0] = "dialogHeight";break;
                }
                if(!isNaN(Number(b[1])))b[1] = Number(b[1]) + "px"; 
                a[i] = b.join(g);
             }
          }
          a.push("center=1");a.push("help=0");a.push("resizable=1");a.push("scroll=1");a.push("status=0");
          p = a.join(";").replace(/=/g, ":");
          if(1 == u.length)u[0] += "?";
          if(-1 == s.indexOf("jsessionid"))u[u.length - 1] += "&jsessionid=" + g_szJsessionid;
          s = u.join("?");
          return window.showModalDialog(s, window, p);
      };
      }
      
      if(window.dialogArguments)window.opener = window.dialogArguments;
        $(function(){
         /* 键盘事件的统一入口处理 */
         $($(document)[0]).keydown(window.nxtfcs = function(e, oI) {
            var k, c;
            if(e)k = e.which || e.charCode || e.keyCode, c = e.metaKey || e.ctrlKey;
            else if(window.event) k = window.event.keyCode || 0, c = window.event.ctrlKey || 0;
            if(c && 192 == k)
              top.frames[1].document.getElementById('menuId').focus();
             
            /* 回车进入下一输入焦点*/
            if(oI || (document.activeElement && 13 == k))
            {
               var oCur = oI || document.activeElement, szNdNm = oCur.nodeName, a = $(":input"), i = 0, bStart = false;
                if("BUTTON" == szNdNm)
                   ;// $(oCur).click();
               else if("INPUT" == szNdNm || "SELECT" == szNdNm)
               {
                  for(i = 0; i <  a.length; i++)
                   {
                      if(bStart)
                      {
                          if(0 == $(a[i]).height() || "hidden" == $(a[i]).attr("type") || $(a[i]).attr("readOnly") || $(a[i]).attr("disabled"))continue;
	                      $(a[i]).focus();
	                      break;
                      }
                      if(a[i] == oCur)bStart = true;
                   }
                   return false;
               }
            }
            return true;
         });});
         
        $(window).resize(window.xuiResize = function(){window.xuiResize.start()});
        window.xuiResize.start = function(){},window.xuiResize.a = [];       
        var ua = navigator.userAgent.toLowerCase(), _t = this;
        _t.isStrict = document.compatMode == "CSS1Compat",
	    _t.isOpera = ua.indexOf("opera") > -1,
	    _t.isSafari = (/webkit|khtml/).test(ua),
	    _t.chrome = (/chrome/).test(ua),
	    _t.isSafari3 = _t.isSafari && ua.indexOf('webkit/5') != -1,
	    _t.isOmniweb = -1 < ua.indexOf("omniweb"),
	    _t.bIE = _t.isIE = (!_t.isOpera && ua.indexOf("msie") > -1 && !_t.isOmniweb),
	    _t.isIE7 = !_t.isOpera && ua.indexOf("msie 7") > -1,
	    _t.isIE6 = !_t.isOpera && ua.indexOf("msie 6") > -1,
	    _t.isGecko = !_t.isSafari && ua.indexOf("gecko") > -1,
	    _t.isGecko3 = !_t.isSafari && ua.indexOf("rv:1.9") > -1,
	    _t.isBorderBox = _t.isIE && !_t.isStrict,
	    _t.isWindows = (ua.indexOf("windows") != -1 || ua.indexOf("win32") != -1),
	    _t.isMac = (ua.indexOf("macintosh") != -1 || ua.indexOf("mac os x") != -1),
	    _t.isAir = (ua.indexOf("adobeair") != -1),
	    _t.isLinux = (ua.indexOf("linux") != -1),
	    _t.isSecure = window.location.href.toLowerCase().indexOf("https") === 0; 
	    _t.isW3C = !!document.getElementById;
        _t.isIE5 = _t.isW3C && _t.isIE;
        _t.isNS6 = _t.isW3C && "Netscape" == navigator.appName;
        window.getAllInput = _t.getAllInput;
        window.mkClctDt = function(){
               $("#_Xui_SelectDiv").hide();
               var a = window.mkClct || [], i,o;
               for(i = a.length; 0 <= --i;)
                   if((o = a[i].swf()) && "function" == typeof o.mkSubmit)o.mkSubmit();
           };
        jQuery.fn.extend({
           getValue:(_t.getValue = function(s){
             var s1, oI, szTp;
             if(s)
             {
                 s1 = (oI = $(_t.getObj(s))).val();
                 if("checkbox" == (szTp = $(oI[0]).attr("type")) && !oI.attr("checked"))
                     s1 = null;
                 if("radio" == szTp)
                 {
                     s1 = null;
                     oI.each(function(){
                         if(this.checked)s1 = this.value;
                     });
                 }
             }
             else s1=this.val();
             if("undefined" == (s1 || typeof s1))s1 = "";
             return s1;
           }),
           setValue:(_t.setValue = function(s, s2){
              if(!s)return this;
              if(null == s2 || "undefined" == typeof s2 || "null" == s2 || "undefined" == s2)s2 = "";
              s2 = String(s2);
              s2=("undefined" == typeof s2 ? "" : s2.replace(/"/gm,"&#34;").replace(/>/gm,"&gt;").replace(/</gm,"&lt;"));
              window.bBoBq = true;
              if(2 == arguments.length)
              {
                 s2 || (s2 = "");
                 if("string" != typeof s2 && s2['time'])
                 {
                     var o = s2, fnT = function(n){return 10 > n? "0" + n: n};o.month++;
                     s2 = [o.fullYear, fnT(o.month), fnT(o.date)].join("-");
                     if(o.seconds || o.hours || o.minutes)
                     s2 += " " + [fnT(o.hours), fnT(o.minutes), fnT(o.seconds)].join(":");
                     fnT = null;
                 }
                 var oIpt = $(_t.getObj(s)), szNm = $(oIpt[0]).attr("nodeName");
                 if("TEXTAREA" != szNm)
                 {
	                 if(0 == oIpt.length || "INPUT" != szNm)
	                 {
	                   var oFom = $("form");
	                   if(0 < oFom.length)oFom = oFom[0];
	                   else oFom = $("body")[0];
	                   Base.insertHtml(oFom, "beforeend", "<input type='hidden' value=\"" + s2 + "\" name=\"" + s + "\"  id=\"" + s + "\">");
	                  }
	                 else{
	                     if("hidden" == oIpt.attr("type"))
	                     {
	                         oIpt.val(s2);
		                     oIpt = Base.getInputDiv(oIpt)[0];
		                     oIpt = $(Base.A(oIpt.getElementsByTagName("INPUT")));
		                     if(2 == oIpt.length && -1 < String($(oIpt[0]).attr("onkeydown")).indexOf("Select"))
		                        $(oIpt[0]).val(Select.getDescByValue(s2, oIpt[0]));
	                     }
	                     else if("checkbox" == oIpt.attr("type"))oIpt.attr("checked", true),(oIpt.attr("name") + "__").setValue(s2);
	                     else if("radio" == $(oIpt[0]).attr("type"))
	                         oIpt.each(function(){
	                             if(this.value == s2)this.checked = true,(this.name + "__").setValue(s2);
	                         });
	                     else oIpt.val(s2);
	                 }
                 }else oIpt.val(s2);
              }
              else this.val(s || "");
              window.bBoBq = false;
           }),
           setFocus:(_t.setFocus = function(s){
              var o = this,szTitle,arg = arguments;
              $(document).ready(function(){
              if(s)o = $(_t.getObj(s));
              if(0 == o.length)o = ("#" + s);
              if("hidden" == o.attr("type"))
              {
                 if("INPUT" == o.prev().attr("nodeName"))
                      o = o.prev();/* 对下拉树的支持 */
              }
              else if("DIV" == o.attr("nodeName"))
                  o = o.find(":input:first");
              window.g_fcsfld = o.attr("name") || o.attr("id");
              o.focus();
              if(szTitle = o.attr("title") || arg[1])
              o.bt(szTitle.replace(/\n/gm,"<br>"), {positions: ['left', 'right', 'bottom','top'] }).btOn();
              });
           }),
           setReadOnly:(_t.setReadOnly = function(s, b){
              var o = this, p;
              if(s)
              {
                 o = $("#" + s);
                 if(0 == o.size())
                 {
                         $(":input").each(function(){
                         if(this.name == s || this.name == "dto(" + s + ")")
                         {
                            o = $(this);
                            // if("hidden" == o.attr("type"))o = o.prev();
                            p = Base.getInputDiv(o);
                            o = $(p.find(":input")[0]);
                            if("undefined" == typeof b || true == b)
                                o.attr("readonly", "readonly"), p.addClass("readOnly");
                            else o.removeAttr("readonly"),p.removeClass("readOnly");
                         }
                        });
                    return this;
                 }
              }
              if("undefined" == typeof b || true == b)
                   o.attr("readonly", "readonly").addClass("readOnly");
              else o.removeAttr("readonly").removeClass("readOnly");
           }),
          enabledButton:(_t.enabledButton = function(s){
 			 var o = this;
             if(s)o = $("#" + s);
             o.btn().enable();
              
           }),
           disabledButton:(_t.disabledButton = function(s){
              var o = this;
              if(s)o = $("#" + s);
              o.btn().disable();
           }),
           addRedStar:(_t.addRedStar = function(s)
           {
             var o = this;
             if(s)
             {
                var i,a = s.split(/[,;\|\s]/);
                for(i = 0; i < a.length; i++)
                {
                  o = _t.getInputDiv(a[i]);
                  if(0 == o.find("b").length)
                  _t.insertHtml(o.find("nobr")[0], "AfterBegin", "<b class=\"redStar\">*</b>");
                  o.find("input:first").attr("isRequired", "true");
                }
             }
             else o.each(function()
             {
                var o1 = _t.getInputDiv(this);
                if(0 == o1.find("b").length)
                _t.insertHtml(o1.find("nobr")[0], "AfterBegin", "<b class=\"redStar\">*</b>");
                o1.find("input:first").attr("isRequired", "true");
             });
           }),
           delRedStar:(_t.delRedStar = function(s)
           {
             var o = this;
             if(s)
             {
                var i,a = s.split(/[,;\|\s]/);
                for(i = 0; i < a.length; i++)
                {
                  if(0 >=$(Base.getObj(a[i])).length)continue;
                  o = Base.getInputDiv(a[i]);// _t.getObj(a[i]).parent().parent();
                  o.find("b").remove();
                  o.find("input:first").removeAttr("isRequired");
                }
             }
             else o.each(function()
             {
                var o1 = Base.getInputDiv(this);// $(this).parent("div").parent("div");
                o1.find("b").remove();
                o1.find("input:first").removeAttr("isRequired");
             });
           }),
           validateForm: (_t.validateForm = function(s)
           {
             var o = this, bR = true;
             if(s)
             {
                var i,a = s.split(/[;,\s\|]/), oCur;
                for(i = 0; i < a.length; i++)
                {
                   oCur = $(_t.getObj(a[i]));
                   if("true" == oCur.attr("isRequired") && !oCur.val().trim() || -1 < (oCur.attr("class") || '').indexOf("x-form-invalid"))
                   {
                      if("hidden" == oCur.attr("type"))oCur = oCur.prev();
                      window.g_fcsfld = oCur.attr("name") || oCur.attr("id");
                      alert($(_t.p(oCur[0], "DIV")).parent("div").find("nobr").text().replace(/^\s*\**/, "") + " 不能为空");
                      return false;
                   }
                }
             }
             else
             {
               if("undefined" == typeof o.length)o = $(":input:not(:checkbox[@checked=false])");
               if(o.length)
               try{
                 o.each(function()
                 {
                   var oCur = $(this);
                   if("true" == oCur.attr("isRequired") && !oCur.val().trim())
                   {
                      if("hidden" == oCur.attr("type"))oCur = oCur.prev();
                      g_fcsfld = oCur.attr("name") || oCur.attr("id");
                      alert($(_t.p(oCur[0], "DIV")).parent("div").find("nobr").text().replace(/^\s*\**/, "") + "  不能为空");
                      bR = false;
                      throw "stop";
                   }
                 });
               }catch(e){}
             }
             return bR;
           })
        });
        window.getBrowserObjects = function()
       {
      var tempArr = [];
      for (var name in navigator)
      {
        var value = navigator[name];
        switch (typeof(value))
        {
            case "string":
            case "boolean":
                tempArr.push("navigator." + name + "=" + escape(value));
                break;
        }
       }
       for (var name in screen)
       {
         var value = screen[name];
         switch (typeof(value))
         {
            case "number":
                tempArr.push("screen." + name + "=" + escape(value));
                break;
          }
        }        
        return tempArr.join("&");
      };
      if(_t.bIE)
      { 
       _t.nVer = parseFloat(/MSIE\s*(\d(\.\d)?);/g.exec(navigator.userAgent)[1]) ||  0;
       if(7 > _t.nVer)
         try{document.execCommand("BackgroundImageCache", false, true)}catch(e){}
      }
      _t.trim = String.prototype.trim = function(s){return (s||this.toString()).replace(/(^\s*)|(\s*$)/gm, "")};
      String.prototype.swf = function(){
         var id = this + "S";
         return -1 != navigator.appName.indexOf("Microsoft") ? window[id] || document.getElementById(id): document[id];
      };
      Array.prototype.indexOf = function(f){
        for(var i = 0; i < this.length; i++)
         if(this[i] == f)return i;
        return -1;
      };
      $(document).ready(function(){
         $(window).error(function(){return false});
      });
      Function.prototype.bind = function(o)
	  {
	     var _t = this, a = Base.A(arguments);a.shift();
	     return function(e)
	     {
	        _t.apply(o || _t, Base.A(arguments).concat(a));
	     }
	  };
	  
	  $.fn.insertNode = function(where, node){
	    return this.each(function(){
	      if (this.insertAdjacentElement){
	        this.insertAdjacentElement(where, node);
	      } else {
	        switch(where){
	          case "beforeBegin":
	            this.parentNode.insertBefore(node,this); 
	            break;
	          case "afterBegin" :  
	            this.insertBefore(node,this.firstChild); 
	            break;
	          case "beforeEnd":
	            this.appendChild(node);
	            break;   
	          case "afterEnd":
	            if(this.nextSibling){
	              this.parentNode.insertBefore(node,this.nextSibling);
	            } else {
	              this.parentNode.appendChild(node);
	            }  
	            break;   
	        }
	      }
	    });
	  };
      return this;
  },binds: function(a)
  {
     for(var i = 0; i < a.length; i++)
       this[a[i]] = this[a[i]].bind(this);
  },
  /* 一些初始化动作 */
  bUnload: 1,
  a:[],nDatetime:24 * 60 * 60 * 1000,
  /* 获取对象o的父亲节点 ，例如 Base.p(o, 'TR') */
  p:function(o,szTagName, n)
  {
    var i = 0;
    while(o && i++ < (n || 500))
    {
      if(o = o.parentNode)
      {
        if("BODY" == o.nodeName)return null;
        if(o.nodeName === szTagName)return o;
      }else break;
    }
    return null;
  }, /* 将a转换为有效的Array */
  A:function(a)
  {
   if(0 == arguments.length)
     a = arguments.callee.caller.arguments;
    var i = 0, b = [];
    for(; i < a.length; i++)
       b.push(a[i]);
    return b;
  }, /* 获取id为s的对象 */
  getDom:function(s)
  {
     if(!s || !document)return null;
     var o = ("string" == typeof s ? document.getElementById(s) : s), k;
     /* for(k in this)o[k] = this[k]; */
     
    return o;
  },
  getByTagName: function(s,o)
  {
     return (o || document).getElementsByTagName(s)
  },
  getByName: function(s, o)
  {
     return (o || document).getElementsByName(s)
  }, /* 触发事件，例如: Base.fireEvent(o, 'click') */
  fireEvent:function(szElement,szEvent)
  {
    if(document.all)
       this.getDom(szElement).fireEvent('on' + szEvent);
    else{
      var evt = document.createEvent('HTMLEvents');
      evt.initEvent(szEvent,true,true);
      this.getDom(szElement).dispatchEvent(evt);
    }
  }, /* 将对象o绑定给fn函数 */
  bind:function(fn, o)
  {
     var _t = this, a = _t.A(arguments);a.shift();a.shift();
     return function(e)
     {
        return fn.apply(o || _t, _t.A(arguments).concat(a));
     }
  }, /* unLoad窗口无效时卸载事件绑定 */
  unLoad:function(o, t, f)
  {
    var _this = Base || this, b = _this.a, i;
    if(b)
    {
	    i = b.length - 1;
	    if(_this.bIE)for(; i > -1; i--)b[i][0].detachEvent(b[i][1], b[i][2]);
	    else for(; i > -1; i--)b[i][0].removeEventListener(b[i][1], b[i][2], false);
	    delete b, delete _this.a;
    }
  }, /* 卸载事件,例如：Base.detachEvent(o, 'click', fn) */
  detachEvent:function(o, type, fn)
  {
    o = o || document.body;
    o.detachEvent ? o.detachEvent("on" + type, fn) : o.removeEventListener(type, fn, false);
  }, /* 绑定事件,例如：Base.addEvent(o, 'click', fn) */
  addEvent:function()
  {
    var o = arguments[0], t = arguments[1], f = arguments[2], _this = this, fn = function(){
      _this.bIE && o.attachEvent('on' + t, f) || o.addEventListener(t, f, false);
      o != window && _this.a.push([o, t, f]);
      _this.bUnload && (_this.bUnload = 0, _this.addEvent(window, "unload", _this.unLoad));
    };
    'load' != t && window.setTimeout(fn, 13) || fn();
    return this;
  }, /* 获取名字为k的cookie,例如：Base.getCookie('myVar') */
  getCookie:function(k)
  {
    var a = (document.cookie || '').split(";");
    for (var i = 0; i < a.length; i++)
    {
       var b = a[i].split("=");
       if(k == b[0].replace(/(^\s*)|(\s*$)/g, ''))
         return unescape(b[1]);
    }
    return "";
  }, /* 设置名字为k的cookie,例如：Base.setCookie(k,'myVar') */
  setCookie: function(k, v)
  {
    var d = new Date(), s = k + "=" + escape(v) + ";expires=";
    d.setTime(d.getTime() + 365 * this.nDatetime);
    if(!v)s += "Fri, 31 Dec 1999 23:59:59 GMT;";
    else s += d.toGMTString();
    document.cookie = s;
    return this;
  }, /* 清楚保留的o滚动条信息,例如：Base.clearScroll(o) */
  clearScroll:function(o)
  {
    var k = this.getDom(o).id;
    delete top.__aScroll[k];
    this.setCookie(k, null);
  }, /* 设置对象o自动保存滚动条信息,例如：Base.autoSaveScroll(o) */
  autoSaveScroll: function(o)
  {
    top.__aScroll || (top.__aScroll = []);
    o = this.getDom(o);
    var t = this, k = o.id, s = t.getCookie(k) || top.__aScroll[k];
    s && t.addEvent(window, 'load', function(e){o.scrollTop = s,t.setCookie(k, null),delete top.__aScroll[k]});
    t.addEvent(o, 'scroll', function(e)
    {
      e = t.FromEventObj(e);
      window.setTimeout(function(){
        t.setCookie(k, top.__aScroll[k] = e.scrollTop);
      }, 13);
    });
    return this;
  },decodeStr: function(s)
  {/* \u4E00-\u9FA5 */
        return (s || '').replace(/[^\0-\255]/gm, function()
        {
          return "&#" + arguments[0].charCodeAt(0) + ";";
        })
  }, /* 异步刷新区域的封装，还没有实现完整 */
  updateUi:function(o)
  {
    mkClctDt();
    var s = [], s1 = [""], o1, _t = this, s2;
    if(!o.data)return alert("updateUi调用参数不正确，没有指定参数data");
    /* post数据，格式为["aac001", "#myTab:input", "divId1"] */
    o.postData && o.postData.each(function()
    {
       o1 = $(this.toString());
       if(o1[0] && !o1[0].nodeName)o1 = $("#"+ this + " :input:not(:checkbox[@checked=false])");
       if(o1[0] && !o1[0].nodeName)o1 = $(":input[@name=" + this + "]");
       if(o1 && 0 < o1.length)
       {
          o1.each(function()
          {
             if(this.name && (s2 = $(this).val()))
               s1.push(this.name + "=" + escape(_t.decodeStr(s2)));
          });
       }
       else s1.push(this);
    });
    /* data为请求刷新的对象，格式为[id,1或true表示过滤后面的字段,需要过滤的字段] */
    o.data.each(function(){s.push(this.join(","))});
    if("undefined" != typeof Base && Base.XuiLoading)Base.XuiLoading();//show
    o.url = o.url || document.location.href;
    if("undefined" != typeof g_szJsessionid)
    {
        if(-1 == o.url.indexOf("?"))o.url += "?jsessionid=" + g_szJsessionid;
        else o.url += "&jsessionid=" + g_szJsessionid;
    }/* 防止缓存 */
    if(-1 == o.url.indexOf("?"))o.url += "?tm=" + new Date().getTime();
    else o.url += "&tm=" + new Date().getTime();
    $.ajax({
    	cache:false,
    	async:o.bAsync && !!o.fn,
    	beforeSend:function(xml){
        xml.setRequestHeader("XUIAJAX",1);
			  xml.setRequestHeader("CMHS","JsonRpc");
      },
    	data: "__ajaxParam_=" + s.join('|') + s1.join("&"),
    	url: o.url || document.location.href,
    	type:"post",
    	dataType:"html",
    	complete:function(obj){
    		if("undefined" != typeof Base && Base.myMask)Base.myMask.hide();//hidden
    		o.fn(obj.responseText);
    	}
    	}); 
    /*  JsonRpcClient().AJAX({ data: "__ajaxParam_=" + s.join('|') + s1.join("&"),  url: o.url || document.location.href,  bAsync: o.bAsync && !!o.fn,  clbkFun: function(){ try {  o.fn && o.fn(arguments[0]); }catch(e){} }});*/
  }, /* 创建图层 */
  createDiv:function()
  {
     var o = null, b = !!arguments[0] || false,
         p = arguments[0], k;
      p && !p["id"] && (p["id"] = "_Xui_SelectDiv");
     if(p && p["id"] && (o = this.getDom(p["id"])))return o;
     o = document.createElement("div");
     if(b)
     {
       p["className"] || (p["className"] = "x-combo-list");
       for(k in p)o[k] = p[k];
     }
     document.body.appendChild(o);
     return o;
  },/* 显示阴影图层 */
  showShadow:function(o)
  {
      var old = o;
      var w = parseFloat(this.getStyle(o, "width")) + 10, h = parseFloat(this.getStyle(o, "height") || 1) + 7,
          oTmp = this.getDom("xuiSelectShdow") || {}, obj = oTmp.style,
         left = parseFloat(this.getStyle(o, "left")) - 4, top = parseFloat(this.getStyle(o, "top")) - 2 , 
         zIndex = (this.getStyle(o, "zIndex") || 11000) - 1;
     if(!obj || !h || !w || 12 > h)return this;
     o = o.style;
     $(oTmp).css({width:w + "px", height: h + "px", top: top + "px", left: left + "px", zIndex: zIndex, position: "absolute"});
     if(!(obj = this.getDom("xuislctsd4")))return this;
     if(12 < w)
     obj.style.width = this.getDom("xuislctsd3").style.width =
     this.getDom("xuislctsd1").style.width = (w - 12) + "px";
     obj = this.getDom("xuislctsd2");
     obj.style.height = (h - 12) + "px";
     o = obj.getElementsByTagName("div");
     for(w = 0; w < o.length; w++)o[w].style.height = obj.style.height;
     oTmp.style.display = old.style.display = "block";
  },hiddenShadow:function(o)
  {
    var oTmp;
    if(oTmp = this.getDom("xuiSelectShdow"))oTmp.style.display='none';
    o.style.display = 'none';
    return this;
  },
  regTimer:function(fn, n)
  {
    var _t = this,nTime = window.setInterval(function()
    {
      if(fn(_t))window.clearInterval(nTime);
    }, n || 13);
    return nTime;
  },clearTimer:function(n){n && window.clearInterval(n)},
   addInvalid: function(o)
   {
      this.addClass("x-form-invalid", o);
   },delInvalid: function(o)
   {
      this.delClass("x-form-invalid", o);
   },
   /* 给o增加class为s */
   addClass: function(s, o)
   {
      $(o).addClass(s);
      return this;
   }, /* 去除o中s的class */
   delClass: function(s, o)
   {
      $(o).removeClass(s);
      return this;
   },
  FromEventObj: function(e){return (e = e || window.event).target || e.srcElement},
  /* 事件返回false */
  preventDefault:function(e)
  {
      e = e || window.event || {};
      return e.preventDefault ? e.preventDefault() : (e.returnValue = false);
  }, /* 停止事件往上层传递 */
  stopPropagation:function(e)
  {
     e = e || window.event || {};
     return e.stopPropagation ? e.stopPropagation() : (e.cancelBubble = true);
  }, /* 在对象el中插入html代码 */
  insertHtml:function(el, where, html){
  where = where.toLowerCase();if(!el)return this;
  if(el.insertAdjacentHTML){
      switch(where){
          case "beforebegin":
              el.insertAdjacentHTML('BeforeBegin', html);
              return el.previousSibling;
          case "afterbegin":
              el.insertAdjacentHTML('AfterBegin', html);
              return el.firstChild;
          case "beforeend":
              el.insertAdjacentHTML('BeforeEnd', html);
              return el.lastChild;
          case "afterend":
              el.insertAdjacentHTML('AfterEnd', html);
              return el.nextSibling;
      }
  }
  var range = el.ownerDocument.createRange(), frag;
  switch(where){
       case "beforebegin":
          range.setStartBefore(el);
          frag = range.createContextualFragment(html);
          el.parentNode.insertBefore(frag, el);
          return el.previousSibling;
       case "afterbegin":
          if(el.firstChild){
              range.setStartBefore(el.firstChild);
              frag = range.createContextualFragment(html);
              el.insertBefore(frag, el.firstChild);
              return el.firstChild;
          }else{
              Base.clearChldNd(el).innerHTML = html;
              return el.firstChild;
          }
      case "beforeend":
          if(el.lastChild){
              range.setStartAfter(el.lastChild);
              frag = range.createContextualFragment(html);
              el.appendChild(frag);
              return el.lastChild;
          }else{
              Base.clearChldNd(el).innerHTML = html;
              return el.lastChild;
          }
      case "afterend":
          range.setStartAfter(el);
          frag = range.createContextualFragment(html);
          el.parentNode.insertBefore(frag, el.nextSibling);
          return el.nextSibling;
      }
  },/* 操作输入对象o上的选择、光标位置，e为事件对象，没有时为null */
  /* FireFox下n2等于光标位置 */
  fnMvIstPoint: function(o, n1, n2, e)
  {
    try{
     e = e || window.event || null;
     o = o || e.target || e.srcElement || null;
     var bErr = false;
     if("undefined" != typeof document.selection)
     {
      try{
        /* To get cursor position, get empty selection range*/
        var oSel = document.selection.createRange();
        /* Move selection start to 0 position */
        oSel.moveStart ('character', -o.value.length);
        oSel.moveEnd("character", -o.value.length);
        /* Move selection start and end to desired position */
        oSel.moveStart('character', n1);
        oSel.moveEnd('character', n2 || 0);
        r.select();
        }catch(e){bErr=true;}
     }
     if(bErr && o.createTextRange)
     {
  	    var r = o.createTextRange();
  	    /* r.moveStart('character', -o.value.length), r.moveEnd('character', -o.value.length); */
  	    r.moveStart('character', n1);
  	    /* r.moveEnd('character', n2 || 0); */
  	    r.collapse(true);
  	    r.select();
     }else
     {
         o.startSelection = n1 - 1;
         o.selectionEnd = n2 || n1 || 0;
         o.focus();
     }
    }catch(e){}
  }, /* 判断n是否为闰年 */
     isLeapYear:function(n)
     {
        return(0 == n % 400 || (0 == n % 4 && 0 != n % 100))
     },/* 获取对象o，或者今天是星期几，返回0是星期天，或者getWeek(2009,12,30) */
    getWeek:function(o)
    {
       if(3 == arguments.length)arguments[1]--,o = new Date(arguments[0], arguments[1], arguments[2]);
       return o.getDay();     
    }, /* 保证fn只能在一个线程里执行 */
     RunOne: function(fn, o)
     {
        var _t = this;
        new function(){
	        if(this._RunOne)return o || _t;
	        this._RunOne = true;
	        fn.call(o || _t);
	        this._RunOne = false;
        }
     },getStyle : function(){
         var view = document.defaultView, propCache = {}, 
            camelRe = /(-[a-z])/gi,
            camelFn = function(m, a){ return a.charAt(1).toUpperCase(); };  
        return view && view.getComputedStyle ?
            function(el, prop){
                var v, cs, camel;
                if(prop == 'float'){
                    prop = "cssFloat";
                }
                if(v = el.style[prop]){
                    return v;
                }
                if(cs = view.getComputedStyle(el, "")){
                    if(!(camel = propCache[prop])){
                        camel = propCache[prop] = prop.replace(camelRe, camelFn);
                    }
                    return cs[camel];
                }
                return null;
            } :
            function(el, prop){
                var v, cs, camel,
                    camelRe = /(-[a-z])/gi,
                    camelFn = function(m, a){ return a.charAt(1).toUpperCase(); }; 
                if(prop == 'opacity'){
                    if(typeof el.style.filter == 'string'){
                        var m = el.style.filter.match(/alpha\(opacity=(.*)\)/i);
                        if(m){
                            var fv = parseFloat(m[1]);
                            if(!isNaN(fv)){
                                return fv ? fv / 100 : 0;
                            }
                        }
                    }
                    return 1;
                }else if(prop == 'float'){
                    prop = "styleFloat";
                }
                if(!(camel = propCache[prop])){
                    camel = propCache[prop] = prop.replace(camelRe, camelFn);
                }
                if(v = el.style[camel]){
                    return v;
                }
                if(cs = el.currentStyle){
                    return cs[camel];
                }
                return null;
            };
    }(),
    getScroll : function(d){
        var doc = document;
        if(d == doc || d == doc.body){
            var l, t;
            if(this.isIE && this.isStrict){
                l = doc.documentElement.scrollLeft || (doc.body.scrollLeft || 0);
                t = doc.documentElement.scrollTop || (doc.body.scrollTop || 0);
            }else{
                l = window.pageXOffset || (doc.body.scrollLeft || 0);
                t = window.pageYOffset || (doc.body.scrollTop || 0);
            }
            return {left: l, top: t};
        }else{
            return {left: d.scrollLeft, top: d.scrollTop};
        }
    },
     getViewWidth : function(full) {
            return full ? this.getDocumentWidth() : this.getViewportWidth();
        },

        getViewHeight : function(full) {
            return full ? this.getDocumentHeight() : this.getViewportHeight();
        },

        getDocumentHeight: function() {
            var scrollHeight = (this.compatMode != "CSS1Compat") ? document.body.scrollHeight : document.documentElement.scrollHeight;
            return Math.max(scrollHeight, this.getViewportHeight());
        },

        getDocumentWidth: function() {
            var scrollWidth = (this.compatMode != "CSS1Compat") ? document.body.scrollWidth : document.documentElement.scrollWidth;
            return Math.max(scrollWidth, this.getViewportWidth());
        },

        getViewportHeight: function(){
            if(this.isIE){
                return this.isStrict ? document.documentElement.clientHeight :
                         document.body.clientHeight;
            }else{
                return self.innerHeight;
            }
        },

        getViewportWidth: function() {
            if(this.isIE){
                return this.isStrict ? document.documentElement.clientWidth :
                         document.body.clientWidth;
            }else{
                return self.innerWidth;
            }
        },
    getOffset: function(o){
    /* offsetLeft, offsetTop */
    var a = [o.offsetLeft, o.offsetTop, o.offsetWidth,o.offsetHeight, 0, 0], r, parent, n;
    if(o.getBoundingClientRect)
    {
       r = o.getBoundingClientRect();
       var scrollTop = Math.max(document.documentElement.scrollTop, document.body.scrollTop),
           scrollLeft = Math.max(document.documentElement.scrollLeft, document.body.scrollLeft);
       a[0] = parseInt(r.left + scrollLeft);
       a[1] = parseInt(r.bottom + scrollTop);
    }
    else if(document.getBoxObjectFor)
	{
	  r = document.getBoxObjectFor(o); 
	  var s = this.getStyle(o, "borderLeftWidth"),
	      borderLeft = s ? parseInt(s) : 0, 
	      borderTop = (s = this.getStyle(o, "borderTopWidth")) ? parseInt(s) : 0; 
	  a[0] = r.x - borderLeft, a[1] = r.y - borderTop;
	}
    else /* safari & opera */
    {
        a[0] = a[1] = a[4] = a[5] = 0;
        a[1] += o.clientHeight;
        parent = o;
        if(o != parent.offsetParent)
        {
	        while(parent && document.body != parent)
	        {
	          a[0] += (parent.offsetLeft || 0);
	          a[1] += (parent.offsetTop || 0);
	          a[4] += (parent.scrollLeft || 0);
	          a[5] += (parent.scrollTop || 0);
	          if(!this.isNS6)
	          {
	             if(n = parseInt(parent.currentStyle.borderLeftWidth, 10))a[0] += n;
	             if(n = parseInt(parent.currentStyle.borderTopWidth, 10))a[1] += n;
              }
	          parent = parent.offsetParent
	        }
        }
     }
     return a;
    }, isCSS1Compat: (document.compatMode == "CSS1Compat"),
    /*collection的链接标签,根据url打开一个新的窗口*/
	openWin: function(o, a){
	  var p = o["param"], dto = o["dto"], target = o["target"] || null,
	  url = '_self' != target ? o["url"] + p + "&xui_pop_win=true" : o["url"] + p, 
	  width = o["width"] || 800, height = o["height"] || 600,
	  option = "height=" + height + ",width=" + width + ",top=" + parseInt((screen.height - height)/2 * 0.75) + ",left=" + parseInt((screen.width - width)/2) + ",status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	  if ("undefined" != typeof dto){
	    var _this = this;
	    $(dto).each(function(){
	      var value = _this.getObj(this).val();
	      if(null != value && "" != value){
	        url += "&" + this + "=" + escape(_this.decodeStr(value));
	      }
	    });
	  }
	  window.open(url, target, option);
	  return false;
	},
	/*按钮的弹出窗口. url:指定的路径, width:宽度, height:高度, param:参数*/
	/*param:参数需要是数组,例如["aac001", "aac002"]*/
	openBtnWin : function(url, param, width, height){
	   var p = "", _this = this, o = null;
	  width || (width = 950);
	   height || (height = 760);
	   if(param)
	   $(param).each(function(){
	     var os = _this.getObj(this), value = $(os[os.length - 1]).val();
	     if(null != value){
	        p += "&" + this + "=" + escape(_this.decodeStr(value));
	      }
	   });
	   o = {"url":url, "param":p, "width":width, "height":height};
	   this.openWin(o, null);
	},
    showDiv: function(o, oDiv, w, h, left)
	{
	if(this.bIE)
	{
	  var nS = document.body.scrollHeight;
	  obj = o.getBoundingClientRect();
	  oDiv.style.left = (obj.left + document.documentElement.scrollLeft) + "px";
	  oDiv.style.top  = (obj.bottom + document.documentElement.scrollTop)+ "px";
	  oDiv.style.width = (w || (obj.right - obj.left)) + "px";
	  if(h)$(oDiv).height(h);
	  oDiv.style.position = "absolute";
	  oDiv.style.zIndex = 10000;
	  oDiv.style.display = 'block';
	  if(document.body.scrollHeight > nS)oDiv.style.top = (obj.bottom - $(o).height() - $(oDiv).height()) + "px";
	}else
	{
	  var oR = this.getOffset(o), style = oDiv.style, k, 
	      hs = [ document.documentElement.scrollHeight, document.documentElement.clientHeight,
	             document.documentElement.scrollWidth, document.documentElement.clientWidth],
 	      p = { left: (left || (oR[0] - (this.bIE ? 2 : 0))) + "px", 
              top: (oR[1] - (this.bIE ? 5 : 2)) + "px", 
              position: "absolute",
              width: parseInt(w || o.clientWidth || oR[2]) + "px"};

      if(h)p["height"] = parseInt(h, 10) + 'px'; 
      for(k in p)style[k] = p[k];
      style["display"] = "block";
      /* 修正显示定位 */
      style["height"] = $(oDiv).height() + "px";
      if(4 == arguments.length)
      oDiv.style.width = Math.max(parseInt($(o).width(), 10), parseInt(oDiv.style.width, 10)) + "px";
      hs[4] = parseInt(oDiv.style.top, 10);
      hs[5] = parseInt(oDiv.style.height, 10);
      hs[6] = parseInt(oDiv.style.left, 10);
      hs[7] = parseInt(oDiv.style.width, 10);
      if(hs[4] + hs[5] > hs[1] + document.documentElement.scrollTop + 40)oDiv.style.top = (hs[4] - hs[5] - $(o).height()) + "px";
      if(hs[6] + hs[7] > hs[3] + document.documentElement.scrollLeft)oDiv.style.left = (hs[6] - hs[7] - $(o).width()) + "px";
      }
      this.showShadow(oDiv);
	}	
}