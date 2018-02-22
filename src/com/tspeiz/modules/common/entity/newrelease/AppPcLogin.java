package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * app-pc-登陆
 * @author heyongb
 *
 */
@Entity
@Table(name="app_scan_info")
public class AppPcLogin implements Serializable{
	private static final long serialVersionUID = -2181589281637320675L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="KeyId")
	private String keyId;//二维码传递 uuid
	
	@Column(name="UserId")
	private Integer userId;//app登陆用户id
	
	@Column(name="UserType")
	private Integer userType;//app登陆用户类型
	
	@Column(name="OrderUuid")
	private String orderUuid;//订单uid  如果需要直接进入到订单界面-必填
	
	@Column(name="OrderType")
	private Integer orderType;//订单类型
	
	@Column(name="ScanType")
	private Integer scanType;//1 -完善病例   2-专家系统 3-医生系统
	
	@Column(name="CreateTime")
	private Timestamp createTime;//时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public Integer getScanType() {
		return scanType;
	}

	public void setScanType(Integer scanType) {
		this.scanType = scanType;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
