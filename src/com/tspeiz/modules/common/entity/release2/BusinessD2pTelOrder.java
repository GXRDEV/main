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

/**
 * 患者2医生 电话订单
 * @author heyongb
 *
 */
@Entity
@Table(name="business_d2p_tel_order")
public class BusinessD2pTelOrder implements Serializable{

	private static final long serialVersionUID = -2577169640880120217L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="UUID")
	private String uuid;
	
	@Column(name="CreateTime")
	private Timestamp createTime;
	
	@Column(name="CaseId")
	private Integer caseId;
	
	@Column(name="DoctorId")
	private Integer doctorId;
	
	@Column(name="UserId")
	private Integer userId;
	
	@Column(name="SubUserUuid")
	private String subUserUuid;
	
	
	@Column(name="OrderTime")
	private String orderTime;
	
	@Column(name="OrderDur")
	private Integer orderDur;
	
	@Column(name="TalkTime")
	private Timestamp talkTime;
	
	@Column(name="TalkDur")
	private Integer talkDur;
	
	@Column(name="ClosedTime")
	private Timestamp closedTime;
	
	@Column(name="CloserType")
	private Integer closerType;
	
	@Column(name="CloserId")
	private Integer closerId;
	
	@Column(name="Source")
	private Integer source;
	
	@Column(name="Status")
	private Integer status;
	
	@Column(name="PayStatus")
	private Integer payStatus;
	
	@Column(name="DelFlag")
	private Integer delFlag;
	
	@Column(name="Remark")
	private String remark;
	
	@Column(name="ReceiveTime")
	private Timestamp receiveTime;
	
	@Column(name="OpenId")
	private String openId;
	
	@Column(name="AnswerTelephone")
	private String answerTelephone;
	
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

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getOrderDur() {
		return orderDur;
	}

	public void setOrderDur(Integer orderDur) {
		this.orderDur = orderDur;
	}

	public Timestamp getTalkTime() {
		return talkTime;
	}

	public void setTalkTime(Timestamp talkTime) {
		this.talkTime = talkTime;
	}

	public Integer getTalkDur() {
		return talkDur;
	}

	public void setTalkDur(Integer talkDur) {
		this.talkDur = talkDur;
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

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAnswerTelephone() {
		return answerTelephone;
	}

	public void setAnswerTelephone(String answerTelephone) {
		this.answerTelephone = answerTelephone;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}
}
