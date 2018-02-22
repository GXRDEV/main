package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 图文问诊信息表
 * 
 * @author liqi
 * 
 */
@Entity
@Table(name = "business_wenzhen_tw")
public class BusinessWenZhenInfo implements Serializable {

	private static final long serialVersionUID = 6056971472452275605L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "Cases_Id")
	private Integer cases_Id;

	@Column(name = "DoctorId")
	private Integer doctorId;

	@Column(name = "UserId")
	private Integer userId;

	@Column(name = "AskStatus")
	private Integer askStatus;

	@Column(name = "AskType")
	private Integer askType;

	@Column(name = "StartTime")
	private Date startTime;

	@Column(name = "ClosedTime")
	private Date closedTime;

	@Column(name = "UserType")
	private Integer userType;

	@Column(name = "CloserId")
	private Integer closerId;

	@Column(name = "SpecialistFirstAnswerTime")
	private Date specialistFirstAnswerTime;

	@Column(name = "SpecialistLastAnswerTime")
	private Date specialistLastAnswerTime;

	@Column(name = "PatientLastAnswerTime")
	private Date patientLastAnswerTime;

	@Column(name = "DocMessageCount")
	private Integer docMessageCount;

	@Column(name = "PatMessageCount")
	private Integer patMessageCount;

	@Column(name = "Origin", length = 50)
	private String origin;

	@Column(name = "DocTalkingNumber")
	private Integer docTalkingNumber;
	
	@Column(name="PatTalkingNumber")
	private Integer patTalkingNumber;

	@Column(name = "IsCooHospital")
	private Integer isCooHospital;

	@Column(name = "IsSeen")
	private Integer isSeen;
	
	@Column(name="PayStatus",length=1)
	private Integer payStatus;//支付状态-------  4等待付款,1-已付款 0-免费
	
	@Column(name="OpenId")
	private String openId;
	
	@Transient
	private String timeStr;
	
	@Transient
	private String desc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCases_Id() {
		return cases_Id;
	}

	public void setCases_Id(Integer cases_Id) {
		this.cases_Id = cases_Id;
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

	public Integer getAskStatus() {
		return askStatus;
	}

	public void setAskStatus(Integer askStatus) {
		this.askStatus = askStatus;
	}

	public Integer getAskType() {
		return askType;
	}

	public void setAskType(Integer askType) {
		this.askType = askType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getClosedTime() {
		return closedTime;
	}

	public void setClosedTime(Date closedTime) {
		this.closedTime = closedTime;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getCloserId() {
		return closerId;
	}

	public void setCloserId(Integer closerId) {
		this.closerId = closerId;
	}

	public Date getSpecialistFirstAnswerTime() {
		return specialistFirstAnswerTime;
	}

	public void setSpecialistFirstAnswerTime(Date specialistFirstAnswerTime) {
		this.specialistFirstAnswerTime = specialistFirstAnswerTime;
	}

	public Date getSpecialistLastAnswerTime() {
		return specialistLastAnswerTime;
	}

	public void setSpecialistLastAnswerTime(Date specialistLastAnswerTime) {
		this.specialistLastAnswerTime = specialistLastAnswerTime;
	}

	public Date getPatientLastAnswerTime() {
		return patientLastAnswerTime;
	}

	public void setPatientLastAnswerTime(Date patientLastAnswerTime) {
		this.patientLastAnswerTime = patientLastAnswerTime;
	}

	public Integer getDocMessageCount() {
		return docMessageCount;
	}

	public void setDocMessageCount(Integer docMessageCount) {
		this.docMessageCount = docMessageCount;
	}

	public Integer getPatMessageCount() {
		return patMessageCount;
	}

	public void setPatMessageCount(Integer patMessageCount) {
		this.patMessageCount = patMessageCount;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Integer getDocTalkingNumber() {
		return docTalkingNumber;
	}

	public void setDocTalkingNumber(Integer docTalkingNumber) {
		this.docTalkingNumber = docTalkingNumber;
	}

	public Integer getIsCooHospital() {
		return isCooHospital;
	}

	public void setIsCooHospital(Integer isCooHospital) {
		this.isCooHospital = isCooHospital;
	}

	public Integer getIsSeen() {
		return isSeen;
	}

	public void setIsSeen(Integer isSeen) {
		this.isSeen = isSeen;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getPatTalkingNumber() {
		return patTalkingNumber;
	}

	public void setPatTalkingNumber(Integer patTalkingNumber) {
		this.patTalkingNumber = patTalkingNumber;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
