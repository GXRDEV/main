package com.tspeiz.modules.common.entity.newrelease;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "baidu_audio_token")
public class BaiduAudioToken implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5968740124468061864L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "AppId")
	private String appId;

	@Column(name = "AppKey")
	private String appKey;

	@Column(name = "AppSecretKey")
	private String appSecretKey;
	
	@Column(name="AccessToken")
	private String accessToken;
	
	@Column(name="NewGenerateDate")
	private String newGenerateDate;//最新生成token时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getAppSecretKey() {
		return appSecretKey;
	}

	public void setAppSecretKey(String appSecretKey) {
		this.appSecretKey = appSecretKey;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getNewGenerateDate() {
		return newGenerateDate;
	}

	public void setNewGenerateDate(String newGenerateDate) {
		this.newGenerateDate = newGenerateDate;
	}
}
