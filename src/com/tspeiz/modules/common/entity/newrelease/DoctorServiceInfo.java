package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 专家服务信息
 * @author heyongb
 *
 */
@Entity
@Table(name="doctor_service_info")
public class DoctorServiceInfo implements Serializable{
	private static final long serialVersionUID = -306308239788507969L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="Id")
	private Integer id;
	
	@Column(name="DoctorId")
	private Integer doctorId;
	
	@Column(name="TeamUuid")
	private String teamUuid;
	
	@Column(name="ServiceId")
	private Integer serviceId;
	
	@Column(name="PackageId")
	private Integer packageId;
	
	@Column(name="IsOpen")
	private Integer isOpen;
	
	@Column(name="Amount")
	private BigDecimal amount;
	
	@Column(name="Description")
	private String description;
	
	@Column(name="IsMultiPackage")
	private Integer isMultiPackage;
	
	@Column(name="ServicePhone")
	private String servicePhone;
	
	@Column(name="ShowPrice")
	private BigDecimal showPrice;//计算的显示价格
	
	@Transient
	private String serviceName;
	@Transient
	private BigDecimal doctorDivided;//医生分成
	@Transient
	private BigDecimal expertDivided;//服务分成
	@Transient
	private BigDecimal platformDivided;//平台分成

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public BigDecimal getExpertDivided() {
		return expertDivided;
	}

	public void setExpertDivided(BigDecimal expertDivided) {
		this.expertDivided = expertDivided;
	}

	public BigDecimal getDoctorDivided() {
		return doctorDivided;
	}

	public void setDoctorDivided(BigDecimal doctorDivided) {
		this.doctorDivided = doctorDivided;
	}

	public BigDecimal getPlatformDivided() {
		return platformDivided;
	}

	public void setPlatformDivided(BigDecimal platformDivided) {
		this.platformDivided = platformDivided;
	}

	public BigDecimal getShowPrice() {
		return showPrice;
	}

	public void setShowPrice(BigDecimal showPrice) {
		this.showPrice = showPrice;
	}

	public String getTeamUuid() {
		return teamUuid;
	}

	public void setTeamUuid(String teamUuid) {
		this.teamUuid = teamUuid;
	}

	public Integer getIsMultiPackage() {
		return isMultiPackage;
	}

	public void setIsMultiPackage(Integer isMultiPackage) {
		this.isMultiPackage = isMultiPackage;
	}

	public String getServicePhone() {
		return servicePhone;
	}

	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}
	
	
}
