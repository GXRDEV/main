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
 * 订单信息表
 * 
 * @author liqi
 * 
 */
@Entity
@Table(name = "business_order_all")
public class BusinessOrderInfo implements Serializable {

	private static final long serialVersionUID = -5674256049448848631L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "WenZhen_Id")
	private Integer wenZhen_Id;

	@Column(name = "Amount")
	private BigDecimal amount;

	@Column(name = "OrderType")
	private Integer orderType;

	@Column(name = "PayStatus")
	private Integer payStatus;

	@Column(name = "FromId")
	private Integer fromId;

	@Column(name = "ToId")
	private Integer toId;

	@Column(name = "CreateTime")
	private Date createTime;

	@Column(name = "PayMode")
	private Integer payMode;

	@Column(name = "PayTime")
	private Date payTime;

	@Column(name = "FlowNumber", length = 100)
	private String flowNumber;

	@Column(name = "RefundType")
	private Integer refundType;

	@Column(name = "RefundTime")
	private Date refundTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWenZhen_Id() {
		return wenZhen_Id;
	}

	public void setWenZhen_Id(Integer wenZhen_Id) {
		this.wenZhen_Id = wenZhen_Id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getFromId() {
		return fromId;
	}

	public void setFromId(Integer fromId) {
		this.fromId = fromId;
	}

	public Integer getToId() {
		return toId;
	}

	public void setToId(Integer toId) {
		this.toId = toId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getFlowNumber() {
		return flowNumber;
	}

	public void setFlowNumber(String flowNumber) {
		this.flowNumber = flowNumber;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}
}
