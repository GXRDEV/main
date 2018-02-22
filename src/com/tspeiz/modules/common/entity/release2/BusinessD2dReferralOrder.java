package com.tspeiz.modules.common.entity.release2;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 转诊订单
 * @author heyongb
 *
 */
@Entity
@Table(name="business_d2d_referral_order")
public class BusinessD2dReferralOrder implements Serializable{
	private static final long serialVersionUID = -7938993050044021142L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "UUID")
	private String uuid;

	@Column(name = "CreateTime")
	private Timestamp createTime;

	@Column(name = "CaseId")
	private Integer caseId;
	
	@Column(name="CaseUuid")
	private String caseUuid;

	@Column(name = "DoctorId")
	private Integer doctorId;

	@Column(name = "UserId")
	private Integer userId;

	@Column(name = "SubUserUuid")
	private String subUserUuid;

	@Column(name = "ReferralHosId")
	private Integer referralHosId;

	@Column(name = "ReferralDepId")
	private Integer referralDepId;

	@Column(name = "ReferralDocId")
	private Integer referralDocId;

	@Column(name = "ReferralDate")
	private String referralDate;

	@Column(name = "ReferralType")
	private Integer referralType;

	@Column(name = "ClosedTime")
	private Timestamp closedTime;

	@Column(name = "CloserType")
	private Integer closerType;

	@Column(name = "CloserId")
	private Integer closerId;

	@Column(name = "Source")
	private Integer source;

	@Column(name = "Status")
	private Integer status;

	@Column(name = "ReceiveTime")
	private Timestamp receiveTime;

	@Column(name = "OpenId")
	private String openid;

	@Column(name = "IsAppraisal")
	private Integer isAppraisal;

	@Column(name = "Remark")
	private String remark;

	@Column(name = "DelFlag")
	private Integer delFlag=0;

	@Column(name="RefusalReason")
	private String refusalReason;
	
	@Column(name = "LocalHospitalId")
	private Integer localHospitalId;
	
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSubUserUuid() {
		return subUserUuid;
	}

	public void setSubUserUuid(String subUserUuid) {
		this.subUserUuid = subUserUuid;
	}

	public Integer getReferralHosId() {
		return referralHosId;
	}

	public void setReferralHosId(Integer referralHosId) {
		this.referralHosId = referralHosId;
	}

	public Integer getReferralDepId() {
		return referralDepId;
	}

	public void setReferralDepId(Integer referralDepId) {
		this.referralDepId = referralDepId;
	}

	public Integer getReferralDocId() {
		return referralDocId;
	}

	public void setReferralDocId(Integer referralDocId) {
		this.referralDocId = referralDocId;
	}

	public String getReferralDate() {
		return referralDate;
	}

	public void setReferralDate(String referralDate) {
		this.referralDate = referralDate;
	}

	public Integer getReferralType() {
		return referralType;
	}

	public void setReferralType(Integer referralType) {
		this.referralType = referralType;
	}

	public Timestamp getClosedTime() {
		return closedTime;
	}

	public void setClosedTime(Timestamp closedTime) {
		this.closedTime = closedTime;
	}

	public Integer getCloserType() {
		return closerType;
	}

	public void setCloserType(Integer closerType) {
		this.closerType = closerType;
	}

	public Integer getCloserId() {
		return closerId;
	}

	public void setCloserId(Integer closerId) {
		this.closerId = closerId;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getIsAppraisal() {
		return isAppraisal;
	}

	public void setIsAppraisal(Integer isAppraisal) {
		this.isAppraisal = isAppraisal;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public String getCaseUuid() {
		return caseUuid;
	}

	public void setCaseUuid(String caseUuid) {
		this.caseUuid = caseUuid;
	}

	public Integer getLocalHospitalId() {
		return localHospitalId;
	}

	public void setLocalHospitalId(Integer localHospitalId) {
		this.localHospitalId = localHospitalId;
	}
	
}
