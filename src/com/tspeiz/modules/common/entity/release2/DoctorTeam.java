package com.tspeiz.modules.common.entity.release2;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 医生团队
 * @author heyongb
 *
 */
@Entity
@Table(name="doctor_team")
public class DoctorTeam implements Serializable{

	private static final long serialVersionUID = 8026738275275678190L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "UUID")
	private String uuid;

	@Column(name = "ApplicationTime")
	private Timestamp applicationTime;

	@Column(name = "ApplicantId")
	private Integer applicantId;
	
	@Column(name = "ApplicantType")
	private Integer applicantType;

	@Column(name = "AuditTime")
	private Timestamp auditTime;

	@Column(name = "AuditorId")
	private Integer auditorId;

	@Column(name = "AuditorType")
	private Integer auditorType;

	@Column(name = "TeamName")
	private String teamName;

	@Column(name = "Profile")
	private String profile;

	@Column(name = "Speciality")
	private String speciality;

	@Column(name = "Keywords")
	private String keywords;

	@Column(name = "Status")
	private Integer status;
	
	@Column(name="AreaOptimal")
	private Integer areaOptimal;
	
	@Column(name="LogoUrl")
	private String logoUrl;
	
	@Column(name="ErweimaUrl")
	private String erweimaUrl;
	
	@Column(name="RefusalReason")
	private String refusalReason;
	
	@Column(name="IsTest")
	private Integer isTest;
	
	@Column(name="DeFlag")
	private Integer deFlag;
	
	@Transient
	private String docName;
	
	@Transient
	private String hosName;
	
	@Transient
	private String depName;
	@Transient
	private String docDuty;
	@Transient
	private String docProfession;
	
	@Transient
	private String applicant;
	@Transient
	private String hosLevel;
	@Transient
	private BigInteger memNum;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Timestamp getApplicationTime() {
		return applicationTime;
	}

	public void setApplicationTime(Timestamp applicationTime) {
		this.applicationTime = applicationTime;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public Timestamp getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getAuditorType() {
		return auditorType;
	}

	public void setAuditorType(Integer auditorType) {
		this.auditorType = auditorType;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
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

	public Integer getApplicantType() {
		return applicantType;
	}

	public void setApplicantType(Integer applicantType) {
		this.applicantType = applicantType;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
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

	public Integer getAreaOptimal() {
		return areaOptimal;
	}

	public void setAreaOptimal(Integer areaOptimal) {
		this.areaOptimal = areaOptimal;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public Integer getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(Integer applicantId) {
		this.applicantId = applicantId;
	}

	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

	public String getDocDuty() {
		return docDuty;
	}

	public void setDocDuty(String docDuty) {
		this.docDuty = docDuty;
	}

	public String getDocProfession() {
		return docProfession;
	}

	public void setDocProfession(String docProfession) {
		this.docProfession = docProfession;
	}

	public String getHosLevel() {
		return hosLevel;
	}

	public void setHosLevel(String hosLevel) {
		this.hosLevel = hosLevel;
	}

	public BigInteger getMemNum() {
		return memNum;
	}

	public void setMemNum(BigInteger memNum) {
		this.memNum = memNum;
	}

	public String getErweimaUrl() {
		return erweimaUrl;
	}

	public void setErweimaUrl(String erweimaUrl) {
		this.erweimaUrl = erweimaUrl;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public Integer getIsTest() {
		return isTest;
	}

	public void setIsTest(Integer isTest) {
		this.isTest = isTest;
	}

	public Integer getDeFlag() {
		return deFlag;
	}

	public void setDeFlag(Integer deFlag) {
		this.deFlag = deFlag;
	}
	
}
