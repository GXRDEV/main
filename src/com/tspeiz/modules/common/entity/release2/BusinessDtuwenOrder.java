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
@Table(name="business_d_tuwen_order")
public class BusinessDtuwenOrder implements Serializable{
	private static final long serialVersionUID = 4039192491506702207L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="UUID")
	private String uuid;

	@Column(name="CaseId")
	private Integer caseId;
	
	@Column(name="CaseUuid")
	private String caseUuid;

	@Column(name="DoctorId")
	private Integer doctorId;
	
	@Column(name="ExpertId")
	private Integer expertId;
	
	@Column(name="ExpertType")
	private Integer expertType;

	@Column(name="UserId")
	private Integer userId;

	@Column(name="SubUserUuid")
	private String subUserUuid;

	@Column(name="CreateTime")
	private Timestamp createTime;
	
	@Column(name="ClosedTime")
	private Timestamp closedTime;

	@Column(name="ExpertFirstAnswerTime")
	private Timestamp expertFirstAnswerTime;

	@Column(name="CloserType")
	private Integer closerType;

	@Column(name="CloserId")
	private Integer closerId;

	@Column(name="ExpertLastAnswerTime")
	private Timestamp expertLastAnswerTime;

	@Column(name="DocLastAnswerTime")
	private Timestamp docLastAnswerTime;

	@Column(name="ExpertSendMsgCount")
	private Integer expertSendMsgCount;

	@Column(name="PatSendMsgCount")
	private Integer patSendMsgCount;

	@Column(name="DocSendMsgCount")
	private Integer docSendMsgCount;

	@Column(name="ExpertUnreadMsgNum")
	private Integer expertUnreadMsgNum;
	
	@Column(name="docUnreadMsgNum")
	private Integer docUnreadMsgNum;

	@Column(name="Source")
	private Integer source;

	@Column(name="Status")
	private Integer status;

	@Column(name="PayStatus")
	private Integer payStatus;
	
	@Column(name="IsAppraisal")
	private Integer isAppraisal;

	@Column(name="DelFlag")
	private Integer delFlag;
	
	@Column(name="ReceiveTime")
	private Timestamp receiveTime;
	
	@Column(name="OpenId")
	private String openId;
	
	@Column(name="RefusalReason")
	private String refusalReason;
	
	@Column(name="ConsultationOpinionUuid")
	private String consultationOpinionUuid;
	@Column(name="SignatureErWeiMa")
	private String signatureErWeiMa;
	
	@Column(name="ConsultationRequestUuid")
	private String consultationRequestUuid;
	
	@Column(name="TeamUuid")
	private String teamUuid;

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

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Timestamp getClosedTime() {
		return closedTime;
	}

	public void setClosedTime(Timestamp closedTime) {
		this.closedTime = closedTime;
	}

	public String getCaseUuid() {
		return caseUuid;
	}

	public void setCaseUuid(String caseUuid) {
		this.caseUuid = caseUuid;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getExpertId() {
		return expertId;
	}

	public void setExpertId(Integer expertId) {
		this.expertId = expertId;
	}

	public Integer getExpertType() {
		return expertType;
	}

	public void setExpertType(Integer expertType) {
		this.expertType = expertType;
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getExpertFirstAnswerTime() {
		return expertFirstAnswerTime;
	}

	public void setExpertFirstAnswerTime(Timestamp expertFirstAnswerTime) {
		this.expertFirstAnswerTime = expertFirstAnswerTime;
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

	public Timestamp getExpertLastAnswerTime() {
		return expertLastAnswerTime;
	}

	public void setExpertLastAnswerTime(Timestamp expertLastAnswerTime) {
		this.expertLastAnswerTime = expertLastAnswerTime;
	}

	public Timestamp getDocLastAnswerTime() {
		return docLastAnswerTime;
	}

	public void setDocLastAnswerTime(Timestamp docLastAnswerTime) {
		this.docLastAnswerTime = docLastAnswerTime;
	}

	public Integer getExpertSendMsgCount() {
		return expertSendMsgCount;
	}

	public void setExpertSendMsgCount(Integer expertSendMsgCount) {
		this.expertSendMsgCount = expertSendMsgCount;
	}

	public Integer getPatSendMsgCount() {
		return patSendMsgCount;
	}

	public void setPatSendMsgCount(Integer patSendMsgCount) {
		this.patSendMsgCount = patSendMsgCount;
	}

	public Integer getDocSendMsgCount() {
		return docSendMsgCount;
	}

	public void setDocSendMsgCount(Integer docSendMsgCount) {
		this.docSendMsgCount = docSendMsgCount;
	}

	public Integer getExpertUnreadMsgNum() {
		return expertUnreadMsgNum;
	}

	public void setExpertUnreadMsgNum(Integer expertUnreadMsgNum) {
		this.expertUnreadMsgNum = expertUnreadMsgNum;
	}

	public Integer getDocUnreadMsgNum() {
		return docUnreadMsgNum;
	}

	public void setDocUnreadMsgNum(Integer docUnreadMsgNum) {
		this.docUnreadMsgNum = docUnreadMsgNum;
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

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getIsAppraisal() {
		return isAppraisal;
	}

	public void setIsAppraisal(Integer isAppraisal) {
		this.isAppraisal = isAppraisal;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public String getConsultationOpinionUuid() {
		return consultationOpinionUuid;
	}

	public void setConsultationOpinionUuid(String consultationOpinionUuid) {
		this.consultationOpinionUuid = consultationOpinionUuid;
	}

	public String getSignatureErWeiMa() {
		return signatureErWeiMa;
	}

	public void setSignatureErWeiMa(String signatureErWeiMa) {
		this.signatureErWeiMa = signatureErWeiMa;
	}

	public String getConsultationRequestUuid() {
		return consultationRequestUuid;
	}

	public void setConsultationRequestUuid(String consultationRequestUuid) {
		this.consultationRequestUuid = consultationRequestUuid;
	}

	public String getTeamUuid() {
		return teamUuid;
	}

	public void setTeamUuid(String teamUuid) {
		this.teamUuid = teamUuid;
	}

	
}
