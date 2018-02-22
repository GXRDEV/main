package com.tspeiz.modules.common.bean.dto;

import java.math.BigDecimal;
import java.util.Date;
/*
 * 私人医生辅助表
 * 
 */
public class BusinessD2pPrivateOrderDto {
	
	private Integer id;
	private Integer source;
	private Integer docServicePackageId;
	private Integer doctorId;
	private Integer userId;
	private Integer status;
	private Integer PayStatus;
	private String docName;
	private String userName;
	private String createTimes;
	private String uuid;
	private BigDecimal money;
	private String startDate;
	private String endDate;
	private String packageName;
	private String duty;
	private String hosName;
	private String depName;
	private Integer sex;
	private String remark;
    private String relieveTime;//解约时间
    private String totalServerTime;//服务时间
    private BigDecimal serverMoney;//服务费用
    private BigDecimal returnMoney;//返还费用

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public Integer getDocServicePackageId() {
		return docServicePackageId;
	}
	public void setDocServicePackageId(Integer docServicePackageId) {
		this.docServicePackageId = docServicePackageId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getPayStatus() {
		return PayStatus;
	}
	public void setPayStatus(Integer payStatus) {
		PayStatus = payStatus;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCreateTimes() {
		return createTimes;
	}
	public void setCreateTimes(String createTimes) {
		this.createTimes = createTimes;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getHosName() {
		return hosName;
	}
	public void setHosName(String hosName) {
		this.hosName = hosName;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRelieveTime() {
		return relieveTime;
	}
	public void setRelieveTime(String relieveTime) {
		this.relieveTime = relieveTime;
	}
	public String getTotalServerTime() {
		return totalServerTime;
	}
	public void setTotalServerTime(String totalServerTime) {
		this.totalServerTime = totalServerTime;
	}
	public BigDecimal getServerMoney() {
		return serverMoney;
	}
	public void setServerMoney(BigDecimal serverMoney) {
		this.serverMoney = serverMoney;
	}
	public BigDecimal getReturnMoney() {
		return returnMoney;
	}
	public void setReturnMoney(BigDecimal returnMoney) {
		this.returnMoney = returnMoney;
	}

}
