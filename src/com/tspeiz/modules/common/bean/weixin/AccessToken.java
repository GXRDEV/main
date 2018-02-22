package com.tspeiz.modules.common.bean.weixin;
/**
 * 
 * @author heyongb
 *
 */
public class AccessToken {
	/**
	 * 唯一令牌
	 */
	private String token;
	/**
	 * 生效时间
	 */
	private int expiresIn;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
}
