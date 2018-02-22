package com.tspeiz.modules.common.entity.weixin;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Cases")
public class Cases implements Serializable {

	private static final long serialVersionUID = -4877969080946428578L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "CaseName", length = 50)
	private String caseName;

	@Column(name = "[Desc]", length = 1024)
	private String desc;
	
	@Column(name = "FirstVisit")
	private Integer firstVisit;
	
	@Column(name = "Hospital", length = 50)
	private String hospital;
	
	@Column(name = "PatientId")
	private Integer patientId;
	
	@Column(name="PatientName",length=50)
	private String patientName;
	
	@Column(name = "Age")
	private Integer age;
	
	@Column(name = "Sex")
	private Integer sex;
	
	@Column(name = "Area")
	private String area;
	
	@Column(name = "Phone")
	private String phone;
	
	@Column(name = "IDNumber")
	private String idNumber;
	
	@Column(name = "Keywords")
	private String keywords;
	
	@Column(name = "FamilyHistory")
	private String familyHistory;
	
	@Column(name = "AskProblem")
	private String askProblem;
	
	@Column(name = "CanQuote")
	private Integer canQuote;
	
	@Column(name = "CreateTime")
	private Long createTime;

	@Column(name = "Id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getFirstVisit() {
		return firstVisit;
	}

	public void setFirstVisit(Integer firstVisit) {
		this.firstVisit = firstVisit;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}

	public String getAskProblem() {
		return askProblem;
	}

	public void setAskProblem(String askProblem) {
		this.askProblem = askProblem;
	}

	public Integer getCanQuote() {
		return canQuote;
	}

	public void setCanQuote(Integer canQuote) {
		this.canQuote = canQuote;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
}
