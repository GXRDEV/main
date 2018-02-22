package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统服务默认定价
 * @author heyongb
 *
 */
@Entity
@Table(name="system_service_default")
public class SystemServiceDefault implements Serializable{
	private static final long serialVersionUID = 7781189849413593713L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="ServiceId")
	private Integer serviceId;
	
	@Column(name="DutyId")
	private Integer dutyId;
	
	@Column(name="DefaultMoney")
	private BigDecimal defaultMoney;

	@Column(name="MaxMoney")
	private BigDecimal maxMoney;

	@Column(name="MinMoney")
	private BigDecimal minMoney;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getDutyId() {
		return dutyId;
	}

	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
	}

	public BigDecimal getDefaultMoney() {
		return defaultMoney;
	}

	public void setDefaultMoney(BigDecimal defaultMoney) {
		this.defaultMoney = defaultMoney;
	}

	public BigDecimal getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(BigDecimal maxMoney) {
		this.maxMoney = maxMoney;
	}

	public BigDecimal getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(BigDecimal minMoney) {
		this.minMoney = minMoney;
	}
}
