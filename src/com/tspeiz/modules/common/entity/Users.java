package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户表（患者1 医生3 专家2---usertype）
 * 
 * @author heyongb
 * 
 */
@Entity
@Table(name = "Users")
public class Users implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String loginName;
	private String displayName;
	private String mobileNumber;
	private String passwordHashed;
	private String salt;
	private Long registerTime;
	private String reigsterIp;
	private Long lastLoginTime;
	private String lastLoginIp;
	private String profilePicture;
	private Integer status;
	private Integer gender;
	private String province;
	private String city;
	private String country;
	private String discriminator;
	private Double balance;
	private String origin;
	private String cardName;
	private String cardNo;
	private String cardBank;
	private String idNumber;
	private Integer userType;
	private Long lastUpdateTime;
	
	private String recommend;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "LoginName", length = 64)
	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name = "DisplayName", length = 64)
	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name = "MobileNumber", length = 32)
	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Column(name = "PasswordHashed", length = 64)
	public String getPasswordHashed() {
		return this.passwordHashed;
	}

	public void setPasswordHashed(String passwordHashed) {
		this.passwordHashed = passwordHashed;
	}

	@Column(name = "Salt", length = 6)
	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Column(name = "RegisterTime", nullable = false)
	public long getRegisterTime() {
		return this.registerTime;
	}

	public void setRegisterTime(long registerTime) {
		this.registerTime = registerTime;
	}

	@Column(name = "ReigsterIp", length = 16)
	public String getReigsterIp() {
		return this.reigsterIp;
	}

	public void setReigsterIp(String reigsterIp) {
		this.reigsterIp = reigsterIp;
	}

	@Column(name = "LastLoginTime", nullable = false)
	public long getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Column(name = "LastLoginIp", length = 16)
	public String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	@Column(name = "ProfilePicture", length = 256)
	public String getProfilePicture() {
		return this.profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	@Column(name = "Status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "Gender", nullable = false)
	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	@Column(name = "Province", length = 32)
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "City", length = 32)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "Country", length = 32)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "Discriminator")
	public String getDiscriminator() {
		return this.discriminator;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	@Column(name = "Balance", precision = 10, scale = 4)
	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Column(name = "Origin", length = 50)
	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Column(name = "CardName", length = 50)
	public String getCardName() {
		return this.cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	@Column(name = "CardNo", length = 50)
	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	@Column(name = "CardBank", length = 50)
	public String getCardBank() {
		return this.cardBank;
	}

	public void setCardBank(String cardBank) {
		this.cardBank = cardBank;
	}

	@Column(name = "IdNumber", length = 50)
	public String getIdNumber() {
		return this.idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	@Column(name = "UserType")
	public Integer getUserType() {
		return this.userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	@Column(name = "LastUpdateTime")
	public Long getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Column(name="Recommend")
	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	
	
}