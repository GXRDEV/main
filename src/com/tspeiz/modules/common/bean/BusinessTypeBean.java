package com.tspeiz.modules.common.bean;

public class BusinessTypeBean {
	String keyName;
	Integer keyCode;
	
	public BusinessTypeBean(){
		
	}
	public BusinessTypeBean(String keyName,Integer keyCode){
		this.keyName=keyName;
		this.keyCode=keyCode;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public Integer getKeyCode() {
		return keyCode;
	}
	public void setKeyCode(Integer keyCode) {
		this.keyCode = keyCode;
	}
	
}
