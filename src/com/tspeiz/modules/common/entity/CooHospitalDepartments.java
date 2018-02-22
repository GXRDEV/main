package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="CooHospitalDepartments")
public class CooHospitalDepartments implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6044850537326456326L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "CooHospitalId",length=11)
	private Integer cooHospitalId;
	
	@Column(name = "DisplayName",length=52)
	private String displayName;
	
	@Column(name = "status",length=1)
	private Integer status;//0为开通，1未开通
	
	@Column(name = "IsGood",length=1)
	private Integer isGood;
	
	@Column(name = "describe",length=255)
	private String describe;
	
	@Column(name = "DepartmentMoney", columnDefinition = "double(10,2) default '0.00'")
	private BigDecimal departmentMoney;
	
	@Column(name = "LiuAnDepartmentId",length=11)
	private Integer liuAnDepartmentId; //有用吗 ？ 应该是没有用的====本地科室对应ID
	
	@Transient
	private String hosName;
	
	@Transient
	private String location;
	
	@Transient 
	private String contactTelephone;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCooHospitalId() {
		return cooHospitalId;
	}

	public void setCooHospitalId(Integer cooHospitalId) {
		this.cooHospitalId = cooHospitalId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsGood() {
		return isGood;
	}

	public void setIsGood(Integer isGood) {
		this.isGood = isGood;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public BigDecimal getDepartmentMoney() {
		return departmentMoney;
	}

	public void setDepartmentMoney(BigDecimal departmentMoney) {
		this.departmentMoney = departmentMoney;
	}

	public Integer getLiuAnDepartmentId() {
		return liuAnDepartmentId;
	}

	public void setLiuAnDepartmentId(Integer liuAnDepartmentId) {
		this.liuAnDepartmentId = liuAnDepartmentId;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContactTelephone() {
		return contactTelephone;
	}

	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
	}
}
