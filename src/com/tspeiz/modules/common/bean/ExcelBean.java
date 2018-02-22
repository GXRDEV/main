package com.tspeiz.modules.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * excel导出辅助类
 * 
 * @author kangxin
 *
 */
public class ExcelBean implements Serializable{
	
	 private String consultationDate;
	 
	 private Integer payStatus;
	 
	 private BigDecimal money;
	 
	 private Integer status;

	 private String localHosName;
	 
	 private String localDepName;
	 
	 private String localDocName;
	 
	 private String hosName;
	 
	 private String depName;
	 
	 private String expertName;
	 
	 private String patientName;
	 
	 private Integer  sex;
	 
	 private Integer age;

	public String getConsultationDate() {
		return consultationDate;
	}

	public void setConsultationDate(String consultationDate) {
		this.consultationDate = consultationDate;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLocalHosName() {
		return localHosName;
	}

	public void setLocalHosName(String localHosName) {
		this.localHosName = localHosName;
	}

	public String getLocalDepName() {
		return localDepName;
	}

	public void setLocalDepName(String localDepName) {
		this.localDepName = localDepName;
	}

	public String getLocalDocName() {
		return localDocName;
	}

	public void setLocalDocName(String localDocName) {
		this.localDocName = localDocName;
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

	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	
	 
	 
}
