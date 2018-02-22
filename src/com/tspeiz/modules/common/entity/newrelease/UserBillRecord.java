package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户消费单
 * 
 * @author heyongb
 * 
 */
@Entity
@Table(name = "user_bill_record")
public class UserBillRecord implements Serializable {
	private static final long serialVersionUID = 8745913488492176300L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "UserId")
	private Integer userId;
	
	@Column(name="DoctorId")
	private Integer doctorId;

	@Column(name = "OpTime")
	private Date opTime;

	@Column(name = "OpAction")
	private String opAction;
	
	@Column(name = "Content")
	private String content;
	
	@Column(name = "Money")
	private BigDecimal money;
	
	@Column(name = "Remark")
	private String remark;
	
	@Column(name = "OrderId")
	private Integer orderId;
	
	@Column(name="BusinessId")
	private String businessId;
	
	@Column(name="BusinessType")
	private Integer businessType;
	
	@Column(name="Type")
	private Integer type;

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

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getOpAction() {
		return opAction;
	}

	public void setOpAction(String opAction) {
		this.opAction = opAction;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
