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
@Table(name="doctor_bill_info")
public class DoctorBillInfo implements Serializable{
	private static final long serialVersionUID = 2782009094881726026L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="UUID")
	private String uuid;
	
	@Column(name="DoctorId")
	private Integer doctorId;
	
	@Column(name="CreateTime")
	private Timestamp createTime;
	
	@Column(name="Source")
	private String source;
	
	@Column(name="Type")
	private Integer type;//1：收入；2：支出
	
	@Column(name="ActualMoney")
	private BigDecimal actualMoney;
	
	@Column(name="OriginalMoney")
	private BigDecimal originalMoney;
	
	@Column(name="TaxationMoney")
	private BigDecimal taxationMoney;
	
	@Column(name="SubsidyMoney")
	private BigDecimal subsidyMoney;
	
	@Column(name="CurAccount")
	private BigDecimal curAccount;
	
	@Column(name="PreAccount")
	private BigDecimal preAccount;
	
	@Column(name="Remark")
	private String remark;
	
	@Column(name="BusinessId")
	private String businessId;
	
	@Column(name="BusinessType")
	private Integer businessType;
	
	@Column(name="TeamUuid")
	private String teamUuid;
	
	@Column(name="Extend")
	private String extend;
	
	@Transient
	private String docName;
	
	@Transient
	private String hosName;
	
	@Transient
	private String depName;
	@Transient
	private String docTel;

	
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getActualMoney() {
		return actualMoney;
	}

	public void setActualMoney(BigDecimal actualMoney) {
		this.actualMoney = actualMoney;
	}

	public BigDecimal getOriginalMoney() {
		return originalMoney;
	}

	public void setOriginalMoney(BigDecimal originalMoney) {
		this.originalMoney = originalMoney;
	}

	public BigDecimal getTaxationMoney() {
		return taxationMoney;
	}

	public void setTaxationMoney(BigDecimal taxationMoney) {
		this.taxationMoney = taxationMoney;
	}

	public BigDecimal getSubsidyMoney() {
		return subsidyMoney;
	}

	public void setSubsidyMoney(BigDecimal subsidyMoney) {
		this.subsidyMoney = subsidyMoney;
	}

	public BigDecimal getCurAccount() {
		return curAccount;
	}

	public void setCurAccount(BigDecimal curAccount) {
		this.curAccount = curAccount;
	}

	public BigDecimal getPreAccount() {
		return preAccount;
	}

	public void setPreAccount(BigDecimal preAccount) {
		this.preAccount = preAccount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
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

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public String getTeamUuid() {
		return teamUuid;
	}

	public void setTeamUuid(String teamUuid) {
		this.teamUuid = teamUuid;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public String getDocTel() {
		return docTel;
	}

	public void setDocTel(String docTel) {
		this.docTel = docTel;
	}
	
	
}
