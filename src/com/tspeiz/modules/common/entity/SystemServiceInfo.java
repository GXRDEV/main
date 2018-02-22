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
@Table(name="system_service_info")
public class SystemServiceInfo implements Serializable{

	private static final long serialVersionUID = 8591890665442859040L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="ServiceName")
	private String serviceName;
	
	@Column(name="UserType")
	private Integer userType;
	
	@Column(name="ServicePrice")
	private BigDecimal servicePrice;
	
	@Column(name="Description")
	private String description;
	
	@Column(name="Rank")
	private Integer rank;
	
	@Column(name="MultiplePackage")
	private Integer multiplePackage;
	
	@Column(name="Status")
	private Integer status;
	
	@Column(name="DoctorDivided")
	private BigDecimal doctorDivided;
	
	@Column(name="ExpertDivided")
	private BigDecimal expertDivided;
	
	@Column(name="PlatformDivided")
	private BigDecimal platformDivided;
	
	@Column(name="Remark")
	private String remark;
	
	@Column(name="GroupType")
	private Integer groupType;
	
	@Column(name="ImageUrl")
	private String imageUrl;
	
	@Column(name="ImageUrlUnused")
	private String imageUrlUnused;
	
	@Column(name="ServiceNote")
	private String serviceNote;
	
	@Column(name="ServiceDescUrl")
	private String serviceDescUrl;
	
	@Column(name="PriceParameter")
	private String priceParameter;
	
	@Transient
	private Integer isOpen;
	@Transient
	private Integer doctorServiceId;//医生服务id
	@Transient
	private BigDecimal money;//服务价格
	@Transient
	private BigDecimal defaultMoney;//指导价格
	@Transient
	private String packageName;//包名
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public Integer getDoctorServiceId() {
		return doctorServiceId;
	}

	public void setDoctorServiceId(Integer doctorServiceId) {
		this.doctorServiceId = doctorServiceId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getDefaultMoney() {
		return defaultMoney;
	}

	public void setDefaultMoney(BigDecimal defaultMoney) {
		this.defaultMoney = defaultMoney;
	}

	public String getServiceNote() {
		return serviceNote;
	}

	public void setServiceNote(String serviceNote) {
		this.serviceNote = serviceNote;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public BigDecimal getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(BigDecimal servicePrice) {
		this.servicePrice = servicePrice;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getMultiplePackage() {
		return multiplePackage;
	}

	public void setMultiplePackage(Integer multiplePackage) {
		this.multiplePackage = multiplePackage;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getGroupType() {
		return groupType;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageUrlUnused() {
		return imageUrlUnused;
	}

	public void setImageUrlUnused(String imageUrlUnused) {
		this.imageUrlUnused = imageUrlUnused;
	}

	public String getServiceDescUrl() {
		return serviceDescUrl;
	}

	public void setServiceDescUrl(String serviceDescUrl) {
		this.serviceDescUrl = serviceDescUrl;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPriceParameter() {
		return priceParameter;
	}

	public void setPricingParameter(String priceParameter) {
		this.priceParameter = priceParameter;
	}
	
}
