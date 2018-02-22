package com.tspeiz.modules.common.entity.release2;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 用户暖意表
 * @author heyongb
 *
 */
@Entity
@Table(name="user_warmth_info")
public class UserWarmthInfo implements Serializable{
	private static final long serialVersionUID = -2379613545988921374L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="CreateTime")
	private Timestamp createTime;
	
	@Column(name="UserId")
	private Integer userId;

	@Column(name="DoctorId")
	private Integer doctorId;

	@Column(name="OrderUuid")
	private String orderUuid;

	@Column(name="OrderType")
	private Integer orderType;

	@Column(name="Content")
	private String content;

	@Column(name="WarmthMoney")
	private BigDecimal warmthMoney;

	@Column(name="WarmthName")
	private String warmthName;

	@Column(name="Remark")
	private String remark;
	
	@Column(name="Status")
	private Integer status;
	
	@Transient
	private String timeStr;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
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

	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BigDecimal getWarmthMoney() {
		return warmthMoney;
	}

	public void setWarmthMoney(BigDecimal warmthMoney) {
		this.warmthMoney = warmthMoney;
	}

	public String getWarmthName() {
		return warmthName;
	}

	public void setWarmthName(String warmthName) {
		this.warmthName = warmthName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
}
