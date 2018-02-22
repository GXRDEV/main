package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 医生的评价信息
 * 
 * @author liqi
 * 
 */
@Entity
@Table(name = "appraisement_doctor_info")
public class AppraisementDoctorInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8396555203599436653L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "OrderUuid")
	private String orderUuid;

	@Column(name = "OrderType")
	private Integer orderType;

	@Column(name = "DoctorId")
	private Integer doctorId;

	@Column(name = "UserId")
	private Integer patientId;
	
	@Column(name="SubUserUuid")
	private String subUserUuid;

	@Column(name = "Grade")
	private Integer grade;

	@Column(name = "Content", length = 255)
	private String content;

	@Column(name = "CreateTime")
	private Date createTime;

	@Column(name = "IsPassed", length = 1)
	private Integer isPassed;
	
	@Transient
	private List<AppraisementImpressionDoctor> tags;
	@Transient
	private String userAccount;
	
	@Transient
	private String timeStr;
	
	@Transient
	private Integer impressionCode;
	
	@Transient
	private Integer grades;
	
	@Transient
	private Integer codes;
	
	@Transient
	private String contents;
	
	

	public Integer getImpressionCode() {
		return impressionCode;
	}

	public void setImpressionCode(Integer impressionCode) {
		this.impressionCode = impressionCode;
	}

	public Integer getGrades() {
		return grades;
	}

	public void setGrades(Integer grades) {
		this.grades = grades;
	}

	public Integer getCodes() {
		return codes;
	}

	public void setCodes(Integer codes) {
		this.codes = codes;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getIsPassed() {
		return isPassed;
	}

	public void setIsPassed(Integer isPassed) {
		this.isPassed = isPassed;
	}

	public List<AppraisementImpressionDoctor> getTags() {
		return tags;
	}

	public void setTags(List<AppraisementImpressionDoctor> tags) {
		this.tags = tags;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getSubUserUuid() {
		return subUserUuid;
	}

	public void setSubUserUuid(String subUserUuid) {
		this.subUserUuid = subUserUuid;
	}
}
