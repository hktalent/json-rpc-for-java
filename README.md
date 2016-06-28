# json-rpc-for-java
&lt;100line js code, &lt;10 java file, nice json rpc for java；仅仅不到100行的javascript代码和不到10个java文件实现的超级轻量级的通过 javaScript快速调用java对象并返回任意对象的轻量级框架，并且支持级联调用，也就是说不需要额外 的JavaScript编程，就可以通过javascript调用被注册的java对象并返回java对象，如果被返回的对象 还有方法，这个在javascript中返回的java对象的变量，你还可以继续调用它的方法.....这就是这个轻 量级json-rpc-for-java的神奇之处。

JavaScript中远程级联调用(RPC)java对象中的方法并返回结果
强荐友人新RPC框架

来自java eye/it eye的1.0详细报道

``` 特点是什么 1、JS2Java RPC：能够在javascript的web浏览器环境中指定java类，获取该类实例的属性， 调用该实例的方法

2、支持复杂js对象作为入参：能够传递复杂的JavaScript Object对象作为调用参数， 在java中得到对应的Map对象

3、自动压缩、解压传递的数据：调用过程中传递的数据自动进行压缩传输，后台自动解压， 然后转换为Map给开发人员；因此从一定层面上保护、加密了传输的数据

4、JS回调函数中能够接收java返回的复杂对象：java中可以返回复杂的对象， 比如Map、List，在javascript中对应为Object和Array

5、友好逐行数据输出并回调：java中可以将List逐行输出到浏览器中， 框架每接收到一条数据，能够自动回调给定的javascript方法， 从而实现数据流的逐行输出； 该接口继承与“com.ibatis.sqlmap.client.event.RowHandler” 因此很容易将ibatis的数据流逐行输出给浏览器，从而实现非常友好的数据加载方式

6、支持回调全异步（观察者模式）：所有的过程调用都采用异步方式， 可以指定回调函数，用来接收返回的对象，并进行其他处理

7、日志回调：L异步过程可以设置日志回调函数，让你掌控全过程

```
相关链接

继（本开源作者的书）《JavaScript高级应用与实践》之后推出的开源代码——json-rpc-for-java light AJAX framework：

微博 作者csdn博客 | 作者新浪600多万次点击博客 | 作者网站

作者的经典博客(通过代理才可以访问，链接中已经包含代理http://jsonrpc.blogspot.com)

作者强力推荐淘宝店

三维条码名片生成器(科技含量身份的象征)
json-rpc常见问题

1、为什么json-rpc总是无法正确运行？

答：请检查你的所有Filter代码，其中不能有类似request.getParameter的代码，或者你根据头信息判断后再获取，例如来自json-rpc的请求，会有附加的头信息：CMHS 因为，本json-rpc会有request.getInputStream这样的代码，在这一的代码之前不能调用request.getParameter
支持的浏览器

IE6、IE7、IE8、FireFox(×)、Opera(×)、Safari(×)、Google Chrome(×)等等

目前应用验证环境：tomcat系列、jboss系列、weblogic系列等均有实际项目应用

hp unix、linux、windowsXP, win 2008,win7
已知使用本开源的部分项目
某软件公司数百个项目中使用

1、异步调用支持传入复合对象作为异步方法的参数，例如： rpc.XRpc.myFunc({aac002:345,kad:'good'}); Java中可以为Map或者JavaBean对象

2、增加防止对象成员变量引用自身导致的死循环递归堆栈溢出

3、增加直接访问父类属性、方法的功能

4、支持级联调用：返回java对象，继续调用该对象的方法

5、只要继承于jcore.jsonrpc.common.JsonRpcObject的json-rpc服务类，可以直接调用getAllInputParms()获得界面中所有输入对象的值

6、只要包名为jcore.jsonrpc.rpcobj可以不用注册【免注册】就可以直接调用，见后面示例
工程svn下载地址

http://json-rpc-for-java.googlecode.com/svn/trunk/'>http://json-rpc-for-java.googlecode.com/svn/trunk/
示例工程下载地址

测试环境：MyEclipse、Jre1.4、tomcat 5.0
如果你要测试，可以采用相应的环境，不一定要那么高版本的环境

svn:http://json-rpc-for-java.googlecode.com/svn/tags/Example'>Example

rar:http://json-rpc-for-java.googlecode.com/files/JsonRpcForJava3.2_sample.rar'>3.2example
最新当前版本下载地址

http://json-rpc-for-java.googlecode.com/svn/trunk/JSON-RPC.jar'>JSON-RPC.jar(65.1 KB) |
http://json-rpc-for-java.googlecode.com/svn/trunk/JsonRpcClient.js'>JsonRpcClient.js(6.34 KB) |

http://json-rpc-for-java.googlecode.com/svn/tags/Example/lib/commons-logging-api.jar'>支持包：commons-logging.jar(51.6 KB) |
http://json-rpc-for-java.googlecode.com/svn/tags/Example/lib/commons-logging.jar'>支持包：commons-logging-api.jar(21.8 KB) |

https://json-rpc-for-java.googlecode.com/svn/trunk/JsonRpcClient_min.js'>JsonRpcClient_min.js(3.66 KB) |
http://json-rpc-for-java.googlecode.com/svn/trunk/JSON-RPC'>for Java使用说明.doc JSON-RPC for Java使用说明.doc |
http://json-rpc-for-java.googlecode.com/svn/trunk/JSON-RPC'>for Java使用说明.pdf JSON-RPC for Java使用说明.pdf
概述

json-rpc-for-java，是仅仅不到100行的javascript代码和不到10个java文件实现的超级轻量级的通过
javaScript快速调用java对象并返回任意对象的轻量级框架，并且支持级联调用，也就是说不需要额外
的JavaScript编程，就可以通过javascript调用被注册的java对象并返回java对象，如果被返回的对象
还有方法，这个在javascript中返回的java对象的变量，你还可以继续调用它的方法.....这就是这个轻
量级json-rpc-for-java的神奇之处。
当返回的是java对象List的时候，在javascript中体现为ArrayList，如果是Map，着体现为js中的
Object，其他的和java中的调用基本相同。
特殊的地方是，通过JS调用java对象方法的时候只能传入简单类型的参数，比
如：String,int,float,long等。

注意事项

如果你的java服务对象返回的是Object、Bean、Map或者自定义对象，不能有属性_name、id，这两个属性被本框架内部使用
json rpc for java的使用

请从http://code.google.com/p/json-rpc-for-java/downloads/list
下载


http://www.china-pub.com/39669'>http://images.china-pub.com/ebook35001-40000/39669/zcover.jpg' />
调用未注册和配置的类方法

1、 首先，被调用的类需要继承jcore.jsonrpc.common.JsonRpcObject或实现接口jcore.jsonrpc.common.face.IjsonRpcObject，并有默认的构造函数；
例如：




package test.rpc;


import jcore.jsonrpc.common.JsonRpcObject;


public class MyTestRpc extends JsonRpcObject {


    /**


     * 调用：rpc.getRpcObj('test.rpc.MyTestRpc').getTestMsg()


     * @return


     */


    public String getTestMsg()


    {


        return "噢，成功了！";


    }


}


2、 JSP的JavaScript中调用的方式，例如：alert(rpc.getRpcObj('test.rpc.MyTestRpc').getTestMsg());

或者
1、 首先，被调用的类需要继承jcore.jsonrpc.common.JsonRpcObject或实现接口jcore.jsonrpc.common.face.IjsonRpcObject，并有默认的构造函数；
例如：

package jcore.jsonrpc.rpcobj; // 必须是jcore.jsonrpc.rpcobj包下才可以免注册


import jcore.jsonrpc.common.JsonRpcObject;


public class MyTestRpc extends JsonRpcObject {


    /**      直接使用类名MyTestRpc就可以调用，如下：


     * 调用：rpc.MyTestRpc.getTestMsg()


     * @return


     */


    public String getTestMsg()


    {


        return "噢，成功了！";


    }


}


2、 JSP的JavaScript中调用的方式，例如：alert(rpc.MyTestRpc.getTestMsg());
推荐音乐
【水月洞天】片头曲曲名：绝世

词曲：唐健

演唱：张克帆

世间种种的诱惑　不惊不扰我清梦

    山高路远不绝我　追踪你绝美的笑容
    登高一呼时才懂　始终在为你心痛
    俯首对花影摇动　都是东风在捉弄

    世间种种的迷惑　都是因你而猜错
    水光月光又交融　描述这朗朗的夜空
    生死到头的相从　似狂花落叶般从容
    当一切泯灭如梦　就在远山被绝世尘封

    啊...
    水光月光又交融　描述这朗朗的夜空
    生死到头的相从　似狂花落叶般从容

    啊...
    不扰我清梦　泯灭如梦
    都是东风在捉弄　像落叶般从容

Related Links

Following the "JavaScript and Practice of advanced applications," after the introduction of open-source code - json-rpc-for-java light AJAX framework:

Csdn blog authors | Author Sina more than 600 million hits blog | author site

Browser support

IE6, IE7, IE8, FireFox ?(×), Opera (×), Safari (×), Google Chrome (×), etc.

Asynchronous call to support the introduction of asynchronous composite object as a method parameter

Download svn project

http://json-rpc-for-java.googlecode.com/svn/trunk/'>http://json-rpc-for-java.googlecode.com/svn/trunk/

Download sample project

Test environment: MyEclipse?, Jre1.4, tomcat 5.0 If you want to test the environment can be used, need not be so high version of the environment 2.8.1example

Download the latest version of the current

JSON-RPC.jar | JsonRpcClient.js

Overview

json-rpc-for-java, is only less than 100 lines of javascript code and less than 10 java files to achieve the adoption of the super-lightweight java objects javaScript quick call and return to the lightweight arbitrary object framework, and support level joint call, which means no additional JavaScript? programming, they can be registered through the javascript to call the java object and return java object, the object is returned if there are ways to return to this in the javascript in the java object variables, You can also continue to call it ..... This is the way to the lightweight json-rpc-for-java's magic. When they returned to the List when the java object, embodied in the javascript for the ArrayList?, If it is Map, reflected in the js in the Object, and other java call in basically the same. Special place is that by JS call java object can only be imported when the parameters of simple types, such as: String, int, float, long, etc..

Notes

If you return to the java client is the Object, Bean, Map or custom objects, can not have attributes name, id, these two attributes for internal use by the framework

json rpc for java use

Please download http://code.google.com/p/json-rpc-for-java/downloads/list'>http://code.google.com/p/json-rpc-for-java/downloads/list

Call not the registration and configuration of the class method

1, first of all, is called the class needs to inherit jcore.jsonrpc.common.JsonRpcObject? Or the realization of interface jcore.jsonrpc.common.face.IjsonRpcObject?, And the default constructor; such as:

package test.rpc;
import jcore.jsonrpc.common.JsonRpcObject;
public class MyTestRpc extends JsonRpcObject (

    /

        Call: rpc.getRpcObj ( 'test.rpc.MyTestRpc'). GetTestMsg ()
        @ Return
        /

    public String getTestMsg ()
    (
    return "Oh, a success!";

)
)
2, JSP's JavaScript? Way call, for example: alert (rpc.getRpcObj ( 'test.rpc.MyTestRpc?'). GetTestMsg ());
