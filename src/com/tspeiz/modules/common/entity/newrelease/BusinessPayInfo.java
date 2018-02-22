package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;

/**
 * 订单支付实体
 * @author heyongb
 *
 */
@Entity
@Table(name="business_pay_info")
public class BusinessPayInfo implements Serializable{
	private static final long serialVersionUID = -2801483889048089829L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "OrderType")
	private Integer orderType;
	
	@Column(name = "OrderId")
	private Integer orderId;
	
	@Column(name = "CreateTime")
	private Timestamp createTime;
	
	@Column(name = "TotalMoney")
	private BigDecimal totalMoney;
	
	@Column(name = "PayMode")
	private Integer payMode;
	
	@Column(name = "OnlinePay")
	private BigDecimal onlinePay;
	
	@Column(name = "AccountPay")
	private BigDecimal accountPay;
	
	@Column(name = "PointsPay")
	private BigDecimal pointsPay;
	
	@Column(name = "CouponPay")
	private BigDecimal couponPay;
	
	@Column(name = "OutTradeNo")
	private String outTradeNo;
	
	@Column(name = "OutTradeResult")
	private String outTradeResult;
	
	@Column(name = "PayStatus")
	private Integer payStatus;
	
	@Column(name = "PayTime")
	private Timestamp payTime;
	
	@Column(name = "RefundTime")
	private Timestamp refundTime;
	
	@Column(name = "CouponId")
	private Integer couponId;
	
	@Column(name="RefundStatus")
	private Integer refundStatus;
	
	@Column(name="OutRefundResult")
	private String outRefundResult;
	
	@Column(name="TradeType")
	private String tradeType;
	
	@Column(name="FromType")
	private Integer fromTye;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public BigDecimal getOnlinePay() {
		return onlinePay;
	}

	public void setOnlinePay(BigDecimal onlinePay) {
		this.onlinePay = onlinePay;
	}

	public BigDecimal getAccountPay() {
		return accountPay;
	}

	public void setAccountPay(BigDecimal accountPay) {
		this.accountPay = accountPay;
	}

	public BigDecimal getPointsPay() {
		return pointsPay;
	}

	public void setPointsPay(BigDecimal pointsPay) {
		this.pointsPay = pointsPay;
	}

	public BigDecimal getCouponPay() {
		return couponPay;
	}

	public void setCouponPay(BigDecimal couponPay) {
		this.couponPay = couponPay;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOutTradeResult() {
		return outTradeResult;
	}

	public void setOutTradeResult(String outTradeResult) {
		this.outTradeResult = outTradeResult;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Timestamp getPayTime() {
		return payTime;
	}

	public void setPayTime(Timestamp payTime) {
		this.payTime = payTime;
	}

	public Timestamp getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Timestamp refundTime) {
		this.refundTime = refundTime;
	}

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getOutRefundResult() {
		return outRefundResult;
	}

	public void setOutRefundResult(String outRefundResult) {
		this.outRefundResult = outRefundResult;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getFromTye() {
		return fromTye;
	}

	public void setFromTye(Integer fromTye) {
		this.fromTye = fromTye;
	}
}
