package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name="hospital_detail_info")
public class HospitalDetailInfo implements Serializable{
	private static final long serialVersionUID = 339133126780972119L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "DisplayName",length=64)
	private String displayName;
	
	@Column(name = "ShortName",length=64)
	private String shortName;
	
	@Column(name = "HospitalLevel",length=64)
	private Integer hospitalLevel;
	
	@Column(name="DistCode",length=6)
	private String distCode;
	
	@Column(name = "Location",length=64)
	private String location;
	
	@Column(name = "BigPicture",length=64)
	private String bigPicture;
	
	@Column(name = "Remark",length=64)
	private String remark;
	
	@Column(name = "IsCooHospital",length=64)
	private Integer isCooHospital;
	
	@Column(name = "Keywords",length=64)
	private String keywords;
	
	@Column(name = "Status",length=64)
	private Integer status;
	
	@Column(name = "Lat")
	private BigDecimal lat;
	
	@Column(name = "Lng")
	private BigDecimal lng;
	
	@Column(name = "AuditStatus",length=1)
	private String auditStatus;
	
	@Column(name="HosProperty")
	private Integer hosProperty;
	
	//医院类型
	@Column(name="HosType")
	private Integer hosType;
	
	//医院官网
	@Column(name="HosWebSite",length=128)
	private String hosWebSite;
	
	//日门诊量
	@Column(name="DailyOutpatientNumber")
	private Integer dailyOutpatientNumber;
	
	//住院床位数量
	@Column(name="DailyAppointNumber")
	private Integer dailyAppointNumber;
	
	//上级转诊医院
	@Type(type="text")
	@Column(name="ExternalAppointmentApproach")
	private String externalAppointmentApproach;
	
	//医院简介
	@Type(type="text")
	@Column(name="HosProfile")
	private String hosProfile;
	
	//特色门诊
	@Type(type="text")
	@Column(name="CharacteristicClinic")
	private String characteristicClinic;
	
	//联系人姓名
	@Column(name="ContactorName",length=16)
	private String contactorName;
	
	//联系人电话
	@Column(name="ContactorTelphone",length=20)
	private String contactorTelphone;
	
	//联系人职务
	@Column(name="ContactorDuty",length=32)
	private String contactorDuty;
	
	//联系人邮箱
	@Column(name="ContactorEmail",length=32)
	private String contactorEmail;
	
	//授权文件
	@Column(name="AuthorizeFile",length=128)
	private String authorizeFile;
	
	//对接模式---1简单对接，2深度对接
	@Column(name="DockingMode",length=1)
	private String dockingMode;
	
	@Column(name="AccountCreate",length=1)
	private String accountCreate;//是否已创建账号
	
	@Column(name="Rank")
	private Integer rank;
	
	@Column(name="HospitalLogo",length=128)
	private String hospitalLogo;
	
	@Type(type="text")
	@Column(name="HospitalIntroduction")
	private String hospitalIntroduction;
	
	@Column(name="CreateTime")
	private Timestamp createTime;
	
	@Transient
	private Integer relativeId;//运营医院id
	@Transient
	private String city;
	
	@Transient
	private String hosLevel;
	@Transient
	private String distName;
	@Transient
	private Integer expertNum=0;
	@Transient
	private Integer depNum=0;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getHospitalLevel() {
		return hospitalLevel;
	}

	public void setHospitalLevel(Integer hospitalLevel) {
		this.hospitalLevel = hospitalLevel;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBigPicture() {
		return bigPicture;
	}

	public void setBigPicture(String bigPicture) {
		this.bigPicture = bigPicture;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsCooHospital() {
		return isCooHospital;
	}

	public void setIsCooHospital(Integer isCooHospital) {
		this.isCooHospital = isCooHospital;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistCode() {
		return distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public String getHosLevel() {
		return hosLevel;
	}

	public void setHosLevel(String hosLevel) {
		this.hosLevel = hosLevel;
	}

	public String getDistName() {
		return distName;
	}

	public void setDistName(String distName) {
		this.distName = distName;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getHosType() {
		return hosType;
	}

	public void setHosType(Integer hosType) {
		this.hosType = hosType;
	}

	public String getHosWebSite() {
		return hosWebSite;
	}

	public void setHosWebSite(String hosWebSite) {
		this.hosWebSite = hosWebSite;
	}

	public Integer getDailyOutpatientNumber() {
		return dailyOutpatientNumber;
	}

	public void setDailyOutpatientNumber(Integer dailyOutpatientNumber) {
		this.dailyOutpatientNumber = dailyOutpatientNumber;
	}

	public Integer getDailyAppointNumber() {
		return dailyAppointNumber;
	}

	public void setDailyAppointNumber(Integer dailyAppointNumber) {
		this.dailyAppointNumber = dailyAppointNumber;
	}

	public String getExternalAppointmentApproach() {
		return externalAppointmentApproach;
	}

	public void setExternalAppointmentApproach(String externalAppointmentApproach) {
		this.externalAppointmentApproach = externalAppointmentApproach;
	}

	public String getHosProfile() {
		return hosProfile;
	}

	public void setHosProfile(String hosProfile) {
		this.hosProfile = hosProfile;
	}

	public String getCharacteristicClinic() {
		return characteristicClinic;
	}

	public void setCharacteristicClinic(String characteristicClinic) {
		this.characteristicClinic = characteristicClinic;
	}

	public String getContactorName() {
		return contactorName;
	}

	public void setContactorName(String contactorName) {
		this.contactorName = contactorName;
	}

	public String getContactorTelphone() {
		return contactorTelphone;
	}

	public void setContactorTelphone(String contactorTelphone) {
		this.contactorTelphone = contactorTelphone;
	}

	public String getContactorDuty() {
		return contactorDuty;
	}

	public void setContactorDuty(String contactorDuty) {
		this.contactorDuty = contactorDuty;
	}

	public String getContactorEmail() {
		return contactorEmail;
	}

	public void setContactorEmail(String contactorEmail) {
		this.contactorEmail = contactorEmail;
	}

	public String getAuthorizeFile() {
		return authorizeFile;
	}

	public void setAuthorizeFile(String authorizeFile) {
		this.authorizeFile = authorizeFile;
	}

	public Integer getHosProperty() {
		return hosProperty;
	}

	public void setHosProperty(Integer hosProperty) {
		this.hosProperty = hosProperty;
	}

	public String getDockingMode() {
		return dockingMode;
	}

	public void setDockingMode(String dockingMode) {
		this.dockingMode = dockingMode;
	}

	public Integer getExpertNum() {
		return expertNum;
	}

	public void setExpertNum(Integer expertNum) {
		this.expertNum = expertNum;
	}

	public Integer getDepNum() {
		return depNum;
	}

	public void setDepNum(Integer depNum) {
		this.depNum = depNum;
	}

	public String getAccountCreate() {
		return accountCreate;
	}

	public void setAccountCreate(String accountCreate) {
		this.accountCreate = accountCreate;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getHospitalLogo() {
		return hospitalLogo;
	}

	public void setHospitalLogo(String hospitalLogo) {
		this.hospitalLogo = hospitalLogo;
	}

	public String getHospitalIntroduction() {
		return hospitalIntroduction;
	}

	public void setHospitalIntroduction(String hospitalIntroduction) {
		this.hospitalIntroduction = hospitalIntroduction;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getRelativeId() {
		return relativeId;
	}

	public void setRelativeId(Integer relativeId) {
		this.relativeId = relativeId;
	}
}
