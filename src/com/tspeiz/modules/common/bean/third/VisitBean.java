package com.tspeiz.modules.common.bean.third;

public class VisitBean {
	private String userId;
	
	private String password;
	
	private String tokenValid;//是否需要token验证 true需要，false不需要
	
	private String visitUrl;
	
	private String tokenUrl;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVisitUrl() {
		return visitUrl;
	}

	public void setVisitUrl(String visitUrl) {
		this.visitUrl = visitUrl;
	}

	public String getTokenUrl() {
		return tokenUrl;
	}

	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}

	public String getTokenValid() {
		return tokenValid;
	}

	public void setTokenValid(String tokenValid) {
		this.tokenValid = tokenValid;
	}
}
