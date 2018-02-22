package com.tspeiz.modules.common.entity.newrelease;

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
 * 图文问诊订单
 * @author heyongb
 *
 */
@Entity
@Table(name="business_tuwen_order")
public class BusinessTuwenOrder implements Serializable{
	private static final long serialVersionUID = 5373933720985098087L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="UUID")
	private String uuid; 
	
	@Column(name = "CaseId")
	private Integer caseId;
	
	@Column(name = "DoctorId")
	private Integer doctorId;
	
	@Column(name = "UserId")
	private Integer userId;
	
	@Column(name = "CreateTime")
	private Timestamp createTime;
	
	@Column(name = "ClosedTime")
	private Timestamp closedTime;
	
	@Column(name = "CloserType")
	private Integer closerType;//关闭人员类型
	
	@Column(name = "CloserId")
	private Integer closerId;//关闭人员Id
	
	@Column(name = "DocFirstAnswerTime")
	private Timestamp docFirstAnswerTime;//专家首次回答时间
	
	@Column(name = "DocLastAnswerTime")
	private Timestamp docLastAnswerTime;//专家最后回复时间
	
	@Column(name = "PatLastAnswerTime")
	private Timestamp patLastAnswerTime;//患者最后回复时间
	
	@Column(name = "DocSendMsgCount")
	private Integer docSendMsgCount;//医生发送消息数
	
	@Column(name = "PatSendMsgCount")
	private Integer patRecvMsgCount;//患者发送消息数
	
	@Column(name = "DocUnreadMsgNum")
	private Integer docUnreadMsgNum;//医生未读数
	
	@Column(name = "PatUnreadMsgNum")
	private Integer patUnreadMsgNum;//患者未读数
	
	@Column(name = "Source")
	private Integer source;//订单来源
	
	@Column(name = "OpenId")
	private String openId;
	
	@Column(name = "Status")
	private Integer status;//订单状态
	
	@Column(name = "PayStatus")
	private Integer payStatus;//支付状态
	
	@Column(name = "DelFlag")
	private Integer delFlag;//删除标记
	
	@Transient
	private String timeStr;
	
	@Transient
	private String desc;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getPatRecvMsgCount() {
		return patRecvMsgCount;
	}

	public void setPatRecvMsgCount(Integer patRecvMsgCount) {
		this.patRecvMsgCount = patRecvMsgCount;
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

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
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

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}	
}
