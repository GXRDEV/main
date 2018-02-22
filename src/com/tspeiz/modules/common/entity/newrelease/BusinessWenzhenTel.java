package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 电话问诊
 * 
 * @author liqi
 * 
 */
@Entity
@Table(name = "business_wenzhen_tel")
public class BusinessWenzhenTel implements Serializable {

	private static final long serialVersionUID = -6102335219457132361L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "UserId")
	private Integer userId;

	@Column(name = "DoctorId")
	private Integer doctorId;

	@Column(name = "OrderId")
	private Integer orderId;

	@Column(name = "CaseId")
	private Integer caseId;

	@Column(name = "Status")
	private Integer status;

	@Column(name = "CreateTime")
	private Date createTime;

	@Column(name = "PayTelTime")
	private Integer payTelTime;

	@Column(name = "LastUpdateTime")
	private Date lastUpdateTime;

	@Column(name = "Origin", length = 50)
	private String origin;
	
	@Column(name="PayStatus",length=1)
	private Integer payStatus;
	
	@Column(name="UserType")
	private Integer userType;
	
	@Column(name="CloserId")
	private Integer closerId;
	
	@Column(name="OpenId")
	private String openId;
	
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPayTelTime() {
		return payTelTime;
	}

	public void setPayTelTime(Integer payTelTime) {
		this.payTelTime = payTelTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getUtype() {
		return userType;
	}

	public void setUtype(Integer userType) {
		this.userType = userType;
	}

	public Integer getCloserId() {
		return closerId;
	}

	public void setCloserId(Integer closerId) {
		this.closerId = closerId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
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
	
	
}
