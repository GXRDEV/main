package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 用户登录设备信息
 * 
 * @author liqi
 * 
 */
@Entity
@Table(name = "user_devices_record")
public class UserDevicesRecord implements Serializable {

	private static final long serialVersionUID = 7518602254106429472L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer Id;

	@Column(name = "UserId")
	private Integer UserId;

	@Column(name = "UserType")
	private Integer UserType;

	@Column(name = "Platform", length = 16)
	private String Platform;

	@Column(name = "Model", length = 32)
	private String Model;

	@Column(name = "Factory", length = 32)
	private String Factory;

	@Column(name = "ScreenSize", length = 32)
	private String ScreenSize;

	@Column(name = "Denstiy", length = 10)
	private String Denstiy;

	@Column(name = "IMEI", length = 64)
	private String IMEI;

	@Column(name = "Mac", length = 64)
	private String Mac;

	@Column(name = "ClientId", length = 64)
	private String ClientId;

	@Column(name = "PushId", length = 64)
	private String PushId;

	@Column(name = "LastLoginTime")
	private Date LastLoginTime;
	
	@Column(name="LoginVersion")
	private String loginVersion;
	
	@Transient
	private String loginName;
	
	@Transient
	private String docName;

	@Transient
	private BigInteger loginNum;
	
	@Transient
	private String hosName;
	@Transient
	private String depName;
	@Transient
	private String lastTimes;
	@Transient
	private BigInteger counts;
	@Transient
	private BigInteger todaycounts;
	
	
	public String getLastTimes() {
		return lastTimes;
	}

	public void setLastTimes(String lastTimes) {
		this.lastTimes = lastTimes;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public Integer getUserId() {
		return UserId;
	}

	public void setUserId(Integer userId) {
		UserId = userId;
	}

	public Integer getUserType() {
		return UserType;
	}

	public void setUserType(Integer userType) {
		UserType = userType;
	}

	public String getPlatform() {
		return Platform;
	}

	public void setPlatform(String platform) {
		Platform = platform;
	}

	public String getModel() {
		return Model;
	}

	public void setModel(String model) {
		Model = model;
	}

	public String getFactory() {
		return Factory;
	}

	public void setFactory(String factory) {
		Factory = factory;
	}

	public String getScreenSize() {
		return ScreenSize;
	}

	public void setScreenSize(String screenSize) {
		ScreenSize = screenSize;
	}

	public String getDenstiy() {
		return Denstiy;
	}

	public void setDenstiy(String denstiy) {
		Denstiy = denstiy;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public String getMac() {
		return Mac;
	}

	public void setMac(String mac) {
		Mac = mac;
	}

	public String getClientId() {
		return ClientId;
	}

	public void setClientId(String clientId) {
		ClientId = clientId;
	}

	public String getPushId() {
		return PushId;
	}

	public void setPushId(String pushId) {
		PushId = pushId;
	}

	public Date getLastLoginTime() {
		return LastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		LastLoginTime = lastLoginTime;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getLoginVersion() {
		return loginVersion;
	}

	public void setLoginVersion(String loginVersion) {
		this.loginVersion = loginVersion;
	}

	public BigInteger getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(BigInteger loginNum) {
		this.loginNum = loginNum;
	}

	public BigInteger getCounts() {
		return counts;
	}

	public void setCounts(BigInteger counts) {
		this.counts = counts;
	}

	public BigInteger getTodaycounts() {
		return todaycounts;
	}

	public void setTodaycounts(BigInteger todaycounts) {
		this.todaycounts = todaycounts;
	}
	
}
