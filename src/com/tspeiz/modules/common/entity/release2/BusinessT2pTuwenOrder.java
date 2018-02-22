package com.tspeiz.modules.common.entity.release2;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="business_t2p_tuwen_order")
public class BusinessT2pTuwenOrder implements Serializable{
	private static final long serialVersionUID = -4473752446386207474L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "UUID")
	private String uuid;

	@Column(name = "CaseId")
	private Integer caseId;

	@Column(name = "UserId")
	private Integer userId;

	@Column(name = "SubUserUuid")
	private String subUserUuid;

	@Column(name = "CreateTime")
	private Timestamp createTime;

	@Column(name = "CloseTime")
	private Timestamp closeTime;

	@Column(name = "CloserType")
	private Integer closerType;

	@Column(name = "CloserId")
	private Integer closerId;

	@Column(name = "DoctorId")
	private Integer doctorId;

	@Column(name = "TeamUuid")
	private String teamUuid;

	@Column(name = "DocFirstAnswerTime")
	private Timestamp docFirstAnswerTime;

	@Column(name = "DocLastAnswerTime")
	private Timestamp docLastAnswerTime;

	@Column(name = "PatLastAnswerTime")
	private Timestamp patLastAnswerTime;

	@Column(name = "DocSendMsgCount")
	private Integer docSendMsgCount;

	@Column(name = "PatSendMsgCount")
	private Integer patSendMsgCount;

	@Column(name = "DocUnreadMsgNum")
	private Integer docUnreadMsgNum;

	@Column(name = "PatUnreadMsgNum")
	private Integer patUnreadMsgNum;

	@Column(name = "NeedDispatch")
	private Integer needDispatch;

	@Column(name = "DispatchTime")
	private Timestamp dispatchTime;

	@Column(name = "DispatchNum")
	private Integer dispatchNum;

	@Column(name = "Source")
	private Integer source;

	@Column(name = "Status")
	private Integer status;

	@Column(name = "PayStatus")
	private Integer payStatus;

	@Column(name = "DelFlag")
	private Integer delFlag;

	@Column(name = "ReceiveTime")
	private Timestamp receiveTime;

	@Column(name = "OpenId")
	private String openId;

	@Column(name = "IsAppraisal")
	private Integer isAppraisal;

	@Column(name = "RefusalReason")
	private String refusalReason;
	
	@Column(name = "Remark")
	private String remark;

	@Column(name = "ConsultationResult")
	private String consultationResult;
	
	@Column(name="Telephone")
	private String telephone;
	
	@Transient
	private String timeStr;
	/**
	 * 用户信息
	 */
	@Transient
	private String userName;
	@Transient
	private Integer sex;
	@Transient
	private Integer age;
	
	@Transient
	private String teamName;
	@Transient
	private String tdocName;
	@Transient
	private String tdocHos;
	@Transient
	private String tdocDep;
	
	@Transient
	private String docName;
	@Transient
	private String docHos;
	@Transient
	private String docDep;
	
	@Transient
	private Integer refundStatus;
	
	@Transient
	private String refundTime;
	
	

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

	public Timestamp getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Timestamp closeTime) {
		this.closeTime = closeTime;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getTeamUuid() {
		return teamUuid;
	}

	public void setTeamUuid(String teamUuid) {
		this.teamUuid = teamUuid;
	}

	public Timestamp getDocFirstAnswerTime() {
		return docFirstAnswerTime;
	}

	public void setDocFirstAnswerTime(Timestamp docFirstAnswerTime) {
		this.docFirstAnswerTime = docFirstAnswerTime;
	}

	public Timestamp getDocLastAnswerTime() {
		return docLastAnswerTime;
	}

	public void setDocLastAnswerTime(Timestamp docLastAnswerTime) {
		this.docLastAnswerTime = docLastAnswerTime;
	}

	public Timestamp getPatLastAnswerTime() {
		return patLastAnswerTime;
	}

	public void setPatLastAnswerTime(Timestamp patLastAnswerTime) {
		this.patLastAnswerTime = patLastAnswerTime;
	}

	public Integer getDocSendMsgCount() {
		return docSendMsgCount;
	}

	public void setDocSendMsgCount(Integer docSendMsgCount) {
		this.docSendMsgCount = docSendMsgCount;
	}

	public Integer getPatSendMsgCount() {
		return patSendMsgCount;
	}

	public void setPatSendMsgCount(Integer patSendMsgCount) {
		this.patSendMsgCount = patSendMsgCount;
	}

	public Integer getDocUnreadMsgNum() {
		return docUnreadMsgNum;
	}

	public void setDocUnreadMsgNum(Integer docUnreadMsgNum) {
		this.docUnreadMsgNum = docUnreadMsgNum;
	}

	public Integer getPatUnreadMsgNum() {
		return patUnreadMsgNum;
	}

	public void setPatUnreadMsgNum(Integer patUnreadMsgNum) {
		this.patUnreadMsgNum = patUnreadMsgNum;
	}

	public Integer getNeedDispatch() {
		return needDispatch;
	}

	public void setNeedDispatch(Integer needDispatch) {
		this.needDispatch = needDispatch;
	}

	public Timestamp getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(Timestamp dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public Integer getDispatchNum() {
		return dispatchNum;
	}

	public void setDispatchNum(Integer dispatchNum) {
		this.dispatchNum = dispatchNum;
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

	public Integer getIsAppraisal() {
		return isAppraisal;
	}

	public void setIsAppraisal(Integer isAppraisal) {
		this.isAppraisal = isAppraisal;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public String getConsultationResult() {
		return consultationResult;
	}

	public void setConsultationResult(String consultationResult) {
		this.consultationResult = consultationResult;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTdocName() {
		return tdocName;
	}

	public void setTdocName(String tdocName) {
		this.tdocName = tdocName;
	}

	public String getTdocHos() {
		return tdocHos;
	}

	public void setTdocHos(String tdocHos) {
		this.tdocHos = tdocHos;
	}

	public String getTdocDep() {
		return tdocDep;
	}

	public void setTdocDep(String tdocDep) {
		this.tdocDep = tdocDep;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocHos() {
		return docHos;
	}

	public void setDocHos(String docHos) {
		this.docHos = docHos;
	}

	public String getDocDep() {
		return docDep;
	}

	public void setDocDep(String docDep) {
		this.docDep = docDep;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}
}
