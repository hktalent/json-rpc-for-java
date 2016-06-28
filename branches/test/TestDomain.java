package test;

import java.io.Serializable;

public class TestDomain implements Serializable{
	private static final long serialVersionUID = 1L;
	private String myName = "夏天";
	private String sex = "男";
	private int age = 10000;
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public String toXml()
	{
		return new StringBuffer()
		.append("<name>").append(getMyName()).append("</name>")
		.append("<sex>").append(getSex()).append("</sex>")
		.append("<age>").append(getAge()).append("</age>")
		.toString();
	}
	public String getMyName() {
		return myName;
	}
	public void setMyName(String myName) {
		this.myName = myName;
	}
	
	/***
	 * 制造异常让框架自动抓取
	 * @throws Exception
	 */
	public void makeException() throws Exception
	{
		throw new Exception("让框架自动捕获错误消息");
	}

}
