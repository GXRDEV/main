package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 远程会诊 运营人员信息
 * @author heyongb
 *
 */
@Entity
@Table(name="business_operative_info")
public class BusinessOperativeInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "OrderId")
	private Integer orderId;
	
	@Column(name = "OpName")
	private String opName;
	
	@Column(name = "OpTel")
	private String opTel;

	@Column(name = "OpMoney")
	private String opMoney;
	
	@Column(name="CreateTime")
	private String createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String getOpTel() {
		return opTel;
	}

	public void setOpTel(String opTel) {
		this.opTel = opTel;
	}

	public String getOpMoney() {
		return opMoney;
	}

	public void setOpMoney(String opMoney) {
		this.opMoney = opMoney;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
