package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="FaceDiagnoses")
public class FaceDiagnoses implements Serializable{

	private static final long serialVersionUID = -7263951525037082404L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "PatientId")
	private Integer patientId;
	
	@Column(name = "SpecialistId")
	private Integer specialistId;
	
	@Column(name = "OrderId")
	private Integer orderId;
	
	@Column(name = "CaseId")
	private Integer caseId;
	
	@Column(name = "Date")
	private Long date;
	
	@Column(name = "Time",length=10)
	private String time;
	
	@Column(name = "Status")
	private Integer status;
	
	@Column(name = "Code",length=50)
	private String code;
	
	@Column(name = "IsSpecialistUpdated")
	private Integer specialistUpdated;
	
	@Column(name = "IsPatientUpdated")
	private Integer patientUpdated;
	
	@Column(name = "WorkLocation")
	private String workLocation;
	
	@Column(name = "CancelId")
	private Integer cancelId;
	
	@Column(name = "CooSpecialistId")
	private Integer cooSpecialistId;
	
	@Column(name = "AutoSendSMSCount")
	private Integer autoSendSmsCount;
	
	@Column(name="CreateTime")
	private Long createTime;
	
	@Column(name="AgreeTime")
	private Long agreeTime;
	
	@Column(name="Remark",length=50)
	private String remark;

	@Column(name="Origin",length=32)
	private String origin;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getSpecialistId() {
		return specialistId;
	}

	public void setSpecialistId(Integer specialistId) {
		this.specialistId = specialistId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSpecialistUpdated() {
		return specialistUpdated;
	}

	public void setSpecialistUpdated(Integer specialistUpdated) {
		this.specialistUpdated = specialistUpdated;
	}

	public Integer getPatientUpdated() {
		return patientUpdated;
	}

	public void setPatientUpdated(Integer patientUpdated) {
		this.patientUpdated = patientUpdated;
	}

	public String getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	public Integer getCancelId() {
		return cancelId;
	}

	public void setCancelId(Integer cancelId) {
		this.cancelId = cancelId;
	}

	public Integer getCooSpecialistId() {
		return cooSpecialistId;
	}

	public void setCooSpecialistId(Integer cooSpecialistId) {
		this.cooSpecialistId = cooSpecialistId;
	}

	public Integer getAutoSendSmsCount() {
		return autoSendSmsCount;
	}

	public void setAutoSendSmsCount(Integer autoSendSmsCount) {
		this.autoSendSmsCount = autoSendSmsCount;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}


	public Long getAgreeTime() {
		return agreeTime;
	}

	public void setAgreeTime(Long agreeTime) {
		this.agreeTime = agreeTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
}
