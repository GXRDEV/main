package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="hos_appoint_order")
public class HosAppointOrder implements Serializable{
	private static final long serialVersionUID = 87433502227600406L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="DocId")
	private Integer docId;
	
	@Column(name="DepId")
	private Integer depId;
	
	@Column(name="PatientName",length=16)
	private String patientName;
	
	@Column(name="Telphone",length=20)
	private String telphone;
	
	@Column(name="IdCard",length=32)
	private String idCard;
	
	@Column(name="RegTimeId")
	private Integer regTimeId;
	
	@Column(name="AppointDate",length=20)
	private String appointDate;
	
	@Column(name="Amount")
	private BigDecimal amount;
	
	@Column(name="OpenId",length=64)
	private String openId;
	
	@Column(name="TradeNo",length=64)
	private String tradeNo;
	
	@Column(name="Status")
	private Integer status=0;
	
	@Column(name="RetStatus")
	private String retStatus;
	
	@Column(name="RegMessage",length=32)
	private String regMessage;
	
	@Column(name="CreateTime",length=20)
	private String createTime;
	
	@Column(name="RefreshTime",length=20)
	private String refreshTime;
	
	@Transient
	private String hosName;
	
	@Transient
	private String depName;
	
	@Transient
	private String timeType;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public Integer getRegTimeId() {
		return regTimeId;
	}

	public void setRegTimeId(Integer regTimeId) {
		this.regTimeId = regTimeId;
	}

	public String getAppointDate() {
		return appointDate;
	}

	public void setAppointDate(String appointDate) {
		this.appointDate = appointDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRetStatus() {
		return retStatus;
	}

	public void setRetStatus(String retStatus) {
		this.retStatus = retStatus;
	}

	public String getRegMessage() {
		return regMessage;
	}

	public void setRegMessage(String regMessage) {
		this.regMessage = regMessage;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(String refreshTime) {
		this.refreshTime = refreshTime;
	}

	public Integer getDepId() {
		return depId;
	}

	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
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

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
}
