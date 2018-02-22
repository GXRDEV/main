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
 * 电话问诊订单
 * @author heyongb
 *
 */
@Entity
@Table(name="business_tel_order")
public class BusinessTelOrder implements Serializable{
	private static final long serialVersionUID = 4815453091543473999L;
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
	
	@Column(name = "OrderDate")
	private String orderDate;//预约日期
	
	@Column(name = "OrderTime")
	private String orderTime;//预约时间
	
	@Column(name = "OrderDur")
	private Integer orderDur;//预约时长
	
	@Column(name = "TalkTime")
	private Timestamp talkTime;//通话时间
	
	@Column(name = "TalkDur")
	private Integer talkDur;//通话时长
	
	@Column(name = "ClosedTime")
	private Timestamp closedTime;//关闭时间
	
	@Column(name = "CloserType")
	private Integer closerType;//关闭人员类型
	
	@Column(name = "CloserId")
	private Integer closerId;//关闭人员Id
	
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

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
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
