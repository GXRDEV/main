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
 * 申请加入医联体
 * @author heyongb
 *
 */
@Entity
@Table(name="ylt_application_request")
public class YltApplicationRequest implements Serializable{
	private static final long serialVersionUID = 6350084275458302086L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "AllianceUuid")
	private String allianceUuid;
	
	@Column(name = "ApplicationTime")
	private Timestamp applicationTime;
	
	@Column(name = "ApplicantId")
	private Integer applicantId;
	
	@Column(name = "ApplicantType")
	private Integer applicantType;
	
	@Column(name = "ParentHosId")
	private Integer parentHosId;
	
	@Column(name = "HospitalId")
	private Integer hospitalId;
	
	@Column(name = "HospitalLevel")
	private Integer hospitalLevel;
	
	@Column(name = "AuditTime")
	private Timestamp auditTime;
	
	@Column(name = "AuditorId")
	private Integer auditorId;
	
	@Column(name = "AuditorType")
	private Integer auditorType;
	
	@Column(name = "Status")
	private Integer status;
	
	@Transient
	private String yltName;
	
	@Transient
	private String position;
	
	@Transient
	private String coreHosName;//核心医院
	
	@Transient
	private String coreHosLevel;//核心医院级别
	
	
	@Transient
	private String hosName;
	
	@Transient
	private String hosLevel;
	
	@Transient
	private String otherHosName;
	
	@Transient
	private String otherHosLevel;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAllianceUuid() {
		return allianceUuid;
	}

	public void setAllianceUuid(String allianceUuid) {
		this.allianceUuid = allianceUuid;
	}

	public Timestamp getApplicationTime() {
		return applicationTime;
	}

	public void setApplicationTime(Timestamp applicationTime) {
		this.applicationTime = applicationTime;
	}

	public Integer getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(Integer applicantId) {
		this.applicantId = applicantId;
	}

	public Integer getApplicantType() {
		return applicantType;
	}

	public void setApplicantType(Integer applicantType) {
		this.applicantType = applicantType;
	}

	public Integer getParentHosId() {
		return parentHosId;
	}

	public void setParentHosId(Integer parentHosId) {
		this.parentHosId = parentHosId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getYltName() {
		return yltName;
	}

	public void setYltName(String yltName) {
		this.yltName = yltName;
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

	public String getHosLevel() {
		return hosLevel;
	}

	public void setHosLevel(String hosLevel) {
		this.hosLevel = hosLevel;
	}

	public String getCoreHosName() {
		return coreHosName;
	}

	public void setCoreHosName(String coreHosName) {
		this.coreHosName = coreHosName;
	}

	public String getCoreHosLevel() {
		return coreHosLevel;
	}

	public void setCoreHosLevel(String coreHosLevel) {
		this.coreHosLevel = coreHosLevel;
	}

	public String getOtherHosName() {
		return otherHosName;
	}

	public void setOtherHosName(String otherHosName) {
		this.otherHosName = otherHosName;
	}

	public String getOtherHosLevel() {
		return otherHosLevel;
	}

	public void setOtherHosLevel(String otherHosLevel) {
		this.otherHosLevel = otherHosLevel;
	}
	
	
}
