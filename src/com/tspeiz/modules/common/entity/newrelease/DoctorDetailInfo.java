package com.tspeiz.modules.common.entity.newrelease;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;


@Entity
@Table(name="doctor_detail_info")
public class DoctorDetailInfo implements Serializable{
	private static final long serialVersionUID = 497913629396138037L;
	@Id
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="DisplayName",length=32)
	private String displayName;
	
	@Column(name="HeadImageUrl",length=256)
	private String headImageUrl;
	
	@Column(name="DistCode",length=6)
	private String distCode;
	
	@Column(name="RefreshTime")
	private Timestamp refreshTime;
	
	@Column(name="DutyId")
	private Integer dutyId;
	
	@Column(name="Duty",length=32)
	private String duty;
	
	
	@Column(name="Position",length=64)
	private String position;
	
	
	@Column(name="Status")
	private Integer status;
	
	@Column(name="DepId")
	private Integer depId;
	
	@Column(name="HospitalId")
	private Integer hospitalId;
	
	@Column(name="Recommend")
	private Integer recommend;
	
	@Column(name="Speciality",length=1024)
	private String speciality;
	
	@Type(type="text")
	@Column(name="Profile")
	private String profile;
	
	@Column(name="Profession",length=20)
	private String profession;
	
	@Column(name="RelatedPics",length=256)
	private String relatedPics;
	
	
	@Column(name="FamousDoctor")
	private Integer famousDoctor;
	
	
	@Column(name="ErweimaUrl")
	private String erweimaUrl;//二维码名片地址
	@Column(name="RegHospitalName")
	private String regHospitalName;
	@Column(name="RegDepartmentName")
	private String regDepartmentName;
	@Column(name="BadgeUrl")
	private String badgeUrl;
	
	@Column(name="IDCardNo")
	private String idCardNo;
	
	@Column(name="AreaOptimal")
	private Integer areaOptimal;//是否推优
	
	@Column(name="Sex")
	private Integer sex;//是否推优
	
	@Column(name="InvitCode")
	private String invitCode;//运营人员邀请码
	
	

	@Transient
	private BigDecimal askAmount;
	@Transient
	private BigDecimal telAmount;
	
	@Transient
	private String telNumber;
	

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

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

	public String getHeadImageUrl() {
		return headImageUrl;
	}

	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}

	public String getDistCode() {
		return distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	public Timestamp getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(Timestamp refreshTime) {
		this.refreshTime = refreshTime;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDepId() {
		return depId;
	}

	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getRelatedPics() {
		return relatedPics;
	}

	public void setRelatedPics(String relatedPics) {
		this.relatedPics = relatedPics;
	}


	public Integer getFamousDoctor() {
		return famousDoctor;
	}

	public void setFamousDoctor(Integer famousDoctor) {
		this.famousDoctor = famousDoctor;
	}


	public String getErweimaUrl() {
		return erweimaUrl;
	}

	public void setErweimaUrl(String erweimaUrl) {
		this.erweimaUrl = erweimaUrl;
	}

	public String getRegHospitalName() {
		return regHospitalName;
	}

	public void setRegHospitalName(String regHospitalName) {
		this.regHospitalName = regHospitalName;
	}

	public String getRegDepartmentName() {
		return regDepartmentName;
	}

	public void setRegDepartmentName(String regDepartmentName) {
		this.regDepartmentName = regDepartmentName;
	}

	public String getBadgeUrl() {
		return badgeUrl;
	}

	public void setBadgeUrl(String badgeUrl) {
		this.badgeUrl = badgeUrl;
	}

	public Integer getDutyId() {
		return dutyId;
	}

	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
	}

	public Integer getAreaOptimal() {
		return areaOptimal;
	}

	public void setAreaOptimal(Integer areaOptimal) {
		this.areaOptimal = areaOptimal;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public BigDecimal getAskAmount() {
		return askAmount;
	}

	public void setAskAmount(BigDecimal askAmount) {
		this.askAmount = askAmount;
	}

	public BigDecimal getTelAmount() {
		return telAmount;
	}

	public void setTelAmount(BigDecimal telAmount) {
		this.telAmount = telAmount;
	}

	public String getInvitCode() {
		return invitCode;
	}

	public void setInvitCode(String invitCode) {
		this.invitCode = invitCode;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
}
