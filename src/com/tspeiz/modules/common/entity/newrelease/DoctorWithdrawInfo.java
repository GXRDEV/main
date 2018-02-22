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

@Entity
@Table(name="doctor_withdraw_info")
public class DoctorWithdrawInfo implements Serializable{

	private static final long serialVersionUID = 6087747487080043287L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "UUID")
	private String UUID;
	
	@Column(name="DoctorId")
	private Integer doctorId;
	
	@Column(name="CreateTime")
	private Timestamp createTime;
	
	@Column(name="Money")
	private BigDecimal money;
	
	@Column(name="CardNo")
	private String cardNo;
	
	@Column(name="IssuingBank")
	private String issuingBank;
	
	@Column(name="Cardholder")
	private String cardholder;
	
	@Column(name="Telephone")
	private String telephone;
	
	@Column(name="AuditTime")
	private Timestamp auditTime;
	
	@Column(name="Auditor")
	private String auditor;
	
	@Column(name="OutTradeNo")
	private String outTradeNo;
	
	@Column(name="TaxationMoney")
	private BigDecimal taxationMoney;//扣税金额
	
	@Column(name="ActualMoney")
	private BigDecimal actualMoney;//实际所得
	
	@Column(name="Status")
	private Integer status;//1待审核，2审核通过，3审核未通过
	
	@Column(name="IDCardNo")
	private String idCardNo;//身份证号
	
	
	@Transient
	private String docName;
	
	@Transient
	private String hosName;
	
	@Transient
	private String depName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getIssuingBank() {
		return issuingBank;
	}

	public void setIssuingBank(String issuingBank) {
		this.issuingBank = issuingBank;
	}

	public String getCardholder() {
		return cardholder;
	}

	public void setCardholder(String cardholder) {
		this.cardholder = cardholder;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Timestamp getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public BigDecimal getTaxationMoney() {
		return taxationMoney;
	}

	public void setTaxationMoney(BigDecimal taxationMoney) {
		this.taxationMoney = taxationMoney;
	}

	public BigDecimal getActualMoney() {
		return actualMoney;
	}

	public void setActualMoney(BigDecimal actualMoney) {
		this.actualMoney = actualMoney;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}
	
}
