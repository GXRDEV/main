package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SpecialistAppoints")
public class SpecialistAppoint implements Serializable{
	
	private static final long serialVersionUID = 8740803611947464357L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "SpecialistId")
	private Integer specialistId;
	
	@Column(name = "AppointDate")
	private Long appointDate;
	
	@Column(name = "CanMorning")
	private Integer canMorning;
	
	@Column(name = "CanAfternoon")
	private Integer canAfternoon;
	
	@Column(name = "CanEvening")
	private Integer canEvening;
	
	@Column(name = "AddCount")
	private Integer addCount;
	
	@Column(name = "WorkLocation")
	private String workLocation;
	
	@Column(name = "NumberType")
	private Integer numberType;
	
	@Column(name = "Amount")
	private BigDecimal amount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSpecialistId() {
		return specialistId;
	}

	public void setSpecialistId(Integer specialistId) {
		this.specialistId = specialistId;
	}

	public Long getAppointDate() {
		return appointDate;
	}

	public void setAppointDate(Long appointDate) {
		this.appointDate = appointDate;
	}

	public Integer getCanMorning() {
		return canMorning;
	}

	public void setCanMorning(Integer canMorning) {
		this.canMorning = canMorning;
	}

	public Integer getCanAfternoon() {
		return canAfternoon;
	}

	public void setCanAfternoon(Integer canAfternoon) {
		this.canAfternoon = canAfternoon;
	}

	public Integer getCanEvening() {
		return canEvening;
	}

	public void setCanEvening(Integer canEvening) {
		this.canEvening = canEvening;
	}

	public Integer getAddCount() {
		return addCount;
	}

	public void setAddCount(Integer addCount) {
		this.addCount = addCount;
	}

	public String getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	public Integer getNumberType() {
		return numberType;
	}

	public void setNumberType(Integer numberType) {
		this.numberType = numberType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
