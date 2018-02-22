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
@Entity
@Table(name="business_d2p_fastask_order")
public class BusinessD2pFastaskOrder implements Serializable{
	private static final long serialVersionUID = 8046175431741057829L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name="UUID")
	private String uuid;

	@Column(name="CaseId")
	private Integer caseId;//病例id

	@Column(name="DoctorId")
	private Integer doctorId;//医生id

	@Column(name="UserId")
	private Integer userId;//用户id
	
	@Column(name="SubUserUuid")
	private String subUserUuid;//家庭成员uuid
	
	@Column(name="DepId")
	private Integer depId;//标准科室id

	@Column(name="CreateTime")
	private Timestamp createTime;//下单时间

	@Column(name="ClosedTime")
	private Timestamp closedTime;//关闭时间

	@Column(name="CloserType")
	private Integer closerType;//关闭人员类型

	@Column(name="CloserId")
	private Integer closerId;//关闭人员id

	@Column(name="DocFirstAnswerTime")
	private Timestamp docFirstAnswerTime;//医生首次回答时间

	@Column(name="DocLastAnswerTime")
	private Timestamp docLastAnswerTime;//医生最后一次回答时间

	@Column(name="PatLastAnswerTime")
	private Timestamp patLastAnswerTime;//患者最后回答时间

	@Column(name="DocSendMsgCount")
	private Integer docSendMsgCount;//医生发送消息数

	@Column(name="PatSendMsgCount")
	private Integer patSendMsgCount;//患者发送消息数

	@Column(name="DocUnreadMsgNum")
	private Integer docUnreadMsgNum;//医生未读消息数

	@Column(name="PatUnreadMsgNum")
	private Integer patUnreadMsgNum;//患者未读消息数

	@Column(name="Source")
	private Integer source;//来源

	@Column(name="Status")
	private Integer status;//订单状态

	@Column(name="PayStatus")
	private Integer payStatus;//支付状态
	
	@Column(name="DelFlag")
	private Integer delFlag;//删除标志
	
	@Column(name="Remark")
	private String remark;//备注

	@Column(name="ReceiveTime")
	private Timestamp receiveTime;//接诊时间
	
	@Column(name="OpenId")
	private String openId;
	
	@Column(name="DistCode")
	private String distCode;
	
	@Column(name="RefusalReason")
	private String refusalReason;

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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
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

	public String getDistCode() {
		return distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}
}
