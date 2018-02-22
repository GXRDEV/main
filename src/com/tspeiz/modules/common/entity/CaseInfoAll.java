package com.tspeiz.modules.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="user_case_all")
public class CaseInfoAll implements Serializable{

	private static final long serialVersionUID = -3018428356482391416L;
	@Id
	@Column(name="Id")
	private Integer id;
	
	@Column(name="User_Id")
	private Integer userId;
	
	@Column(name="CaseName")
	private String caseName;
	
	@Column(name="ContactName")
	private String contactName;
	
	@Column(name="IdNumber")
	private String idNumber;
	
	@Column(name="Sex")
	private Integer sex;
	
	@Column(name="Age")
	private Integer age;
	
	@Column(name="Keywords")
	private String keywords;
	
	@Column(name="AskProblem")
	private String askProblem;
	
	@Column(name="Description")
	private String description;
	
	@Column(name="FamilyHistory")
	private String familyHistory;
	
	@Column(name="Telephone")
	private String telephone;
	
	@Column(name="CreateTime")
	private Timestamp createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
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

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getAskProblem() {
		return askProblem;
	}

	public void setAskProblem(String askProblem) {
		this.askProblem = askProblem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
	
}
