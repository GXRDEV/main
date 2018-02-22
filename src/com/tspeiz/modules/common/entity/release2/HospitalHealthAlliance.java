package com.tspeiz.modules.common.entity.release2;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 医联体主表
 * @author heyongb
 *
 */
@Entity
@Table(name="hospital_health_alliance")
public class HospitalHealthAlliance implements Serializable{
	private static final long serialVersionUID = -4034707629777564436L;
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

	@Column(name = "YltName")
	private String yltName;

	@Column(name = "Profile")
	private String profile;

	@Column(name = "Status")
	private Integer status;

	@Column(name = "IconUrl")
	private String iconUrl;
	
	@Column(name="HospitalId")
	private Integer hospitalId;
	
	@Column(name="HospitalLevel")
	private Integer hospitalLevel;
	
	@Column(name="Speciality")
	private String speciality;
	
	@Transient
	private String applicant;
	@Transient
	private String auditor;
	@Transient
	private String position;
	@Transient
	private String hosName;
	@Transient
	private String hosLevel;
	@Transient
	private String distName;
	
	@Transient
	private String mainHosName;

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

	public Integer getApplicantType() {
		return applicantType;
	}

	public void setApplicantType(Integer applicantType) {
		this.applicantType = applicantType;
	}

	public Timestamp getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

	public Integer getAuditorType() {
		return auditorType;
	}

	public void setAuditorType(Integer auditorType) {
		this.auditorType = auditorType;
	}

	public String getYltName() {
		return yltName;
	}

	public void setYltName(String yltName) {
		this.yltName = yltName;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public Integer getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(Integer applicantId) {
		this.applicantId = applicantId;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Integer getHospitalLevel() {
		return hospitalLevel;
	}

	public void setHospitalLevel(Integer hospitalLevel) {
		this.hospitalLevel = hospitalLevel;
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

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getMainHosName() {
		return mainHosName;
	}

	public void setMainHosName(String mainHosName) {
		this.mainHosName = mainHosName;
	}
}
