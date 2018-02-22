package com.tspeiz.modules.common.entity.release2;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="doctor_consultation_opinion")
public class DoctorConsultationOpinion implements Serializable{
	private static final long serialVersionUID = -7308430023375404509L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="UUID")
	private String uuid;
	
	@Column(name="OrderUuid")
	private String orderUuid;
	
	@Column(name="OrderType")
	private Integer orderType;
	
	@Column(name="Diagnosis")
	private String diagnosis;
	
	@Column(name="TreatPlan")
	private String treatPlan;
	
	@Column(name="Attentions")
	private String attentions;
	
	@Column(name="Signature")
	private String signature;
	
	@Column(name="PhotoReport")
	private String photoReport;
	
	@Column(name="RefreshTime")
	private Timestamp refreshTime;
	
	@Column(name="CreateTime")
	private Timestamp createTime;

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

	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getTreatPlan() {
		return treatPlan;
	}

	public void setTreatPlan(String treatPlan) {
		this.treatPlan = treatPlan;
	}

	public String getAttentions() {
		return attentions;
	}

	public void setAttentions(String attentions) {
		this.attentions = attentions;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPhotoReport() {
		return photoReport;
	}

	public void setPhotoReport(String photoReport) {
		this.photoReport = photoReport;
	}

	public Timestamp getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(Timestamp refreshTime) {
		this.refreshTime = refreshTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
