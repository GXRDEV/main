package com.tspeiz.modules.util.getui;

public class GetuiConfigure {

	private String appId;//节点 设定接收的应用
	private String appKey; //用于鉴定身份是否合法
	private String masterSecret;//第三方客户端个推集成鉴权码，用于验证第三方合法性。在客户端集成SDK时需要提供
	
	public GetuiConfigure(){
		
	}
	
	public GetuiConfigure(String appId,String appKey,String masterSecret ){
		this.appId = appId;
		this.appKey = appKey;
		this.masterSecret = masterSecret;
	}
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getMasterSecret() {
		return masterSecret;
	}
	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}

}
