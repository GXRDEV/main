package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 远程会诊申请表
 * @author heyongb
 *
 */
@Entity
@Table(name="remote_consultation_application")
public class RemoteConsultationApp implements Serializable{
	private static final long serialVersionUID = -6949049575941874166L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="UUID")
	private String uuid;
	
	@Column(name="AppConName")
	private String appConName;//申请人姓名
	
	@Column(name="AppConTel")
	private String appConTel;//申请人联系电话
	
	@Column(name="ConArea")
	private String conArea;//会诊地区
	
	@Column(name="PatientName")
	private String patientName;//患者姓名
	
	@Column(name="Age")
	private Integer age;//患者年龄
	
	@Column(name="Sex")
	private Integer sex;//患者性别
	
	@Column(name="LocalDoctorId")
	private Integer localDoctorId;//当地医师id
	
	@Column(name="ConMoney")
	private BigDecimal conMoney;//会诊费用
	
	@Column(name="RequireConDate")
	private String requireConDate;//会诊要求时间
	
	@Column(name="PatientDesc")
	private String patientDesc;//患者病情描述
	
	@Column(name="AskProblem")
	private String askProblem;//咨询目的
	
	@Column(name="NormalImages")
	private String normalImages;//病例图片
	
	@Column(name="Remark")
	private String remark;//备注
	
	@Column(name="TriagePerson")
	private String triagePerson;//分诊负责人
	
	@Column(name="TriagePersonTel")
	private String triagePersonTel;//分诊负责人电话
	
	@Column(name="ChargePersonName")
	private String chargePersonName;//具体负责人

	@Column(name="ChargePersonTel")
	private String chargePersonTel;//负责人联系电话
	
	@Column(name="ConExpertId")
	private Integer conExpertId;//确认会诊专家
	
	@Column(name="ConSureTime")
	private String conSureTime;//会诊时间
	
	@Column(name="AppCreateTime")
	private String appCreateTime;//申请创建时间
	
	@Column(name="OrderId")
	private Integer orderId;
	
	/**
	 * 状态
	 * 1：填写完申请信息，带分诊
	 * 2：分诊完成，待确认会诊
	 * 3：确认会诊信息完成
	 * 4：已生成订单
	 */
	@Column(name="Status")
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getConArea() {
		return conArea;
	}

	public void setConArea(String conArea) {
		this.conArea = conArea;
	}


	public String getAppCreateTime() {
		return appCreateTime;
	}

	public void setAppCreateTime(String appCreateTime) {
		this.appCreateTime = appCreateTime;
	}
	
	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
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

	public String getAppConName() {
		return appConName;
	}

	public void setAppConName(String appConName) {
		this.appConName = appConName;
	}

	public String getAppConTel() {
		return appConTel;
	}

	public void setAppConTel(String appConTel) {
		this.appConTel = appConTel;
	}

	public BigDecimal getConMoney() {
		return conMoney;
	}

	public void setConMoney(BigDecimal conMoney) {
		this.conMoney = conMoney;
	}

	public String getRequireConDate() {
		return requireConDate;
	}

	public void setRequireConDate(String requireConDate) {
		this.requireConDate = requireConDate;
	}

	public String getPatientDesc() {
		return patientDesc;
	}

	public void setPatientDesc(String patientDesc) {
		this.patientDesc = patientDesc;
	}

	public String getConSureTime() {
		return conSureTime;
	}

	public void setConSureTime(String conSureTime) {
		this.conSureTime = conSureTime;
	}

	public String getChargePersonName() {
		return chargePersonName;
	}

	public void setChargePersonName(String chargePersonName) {
		this.chargePersonName = chargePersonName;
	}

	public String getChargePersonTel() {
		return chargePersonTel;
	}

	public void setChargePersonTel(String chargePersonTel) {
		this.chargePersonTel = chargePersonTel;
	}

	public String getNormalImages() {
		return normalImages;
	}

	public void setNormalImages(String normalImages) {
		this.normalImages = normalImages;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTriagePerson() {
		return triagePerson;
	}

	public void setTriagePerson(String triagePerson) {
		this.triagePerson = triagePerson;
	}

	public Integer getConExpertId() {
		return conExpertId;
	}

	public void setConExpertId(Integer conExpertId) {
		this.conExpertId = conExpertId;
	}

	public Integer getLocalDoctorId() {
		return localDoctorId;
	}

	public void setLocalDoctorId(Integer localDoctorId) {
		this.localDoctorId = localDoctorId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getAskProblem() {
		return askProblem;
	}

	public void setAskProblem(String askProblem) {
		this.askProblem = askProblem;
	}

	public String getTriagePersonTel() {
		return triagePersonTel;
	}

	public void setTriagePersonTel(String triagePersonTel) {
		this.triagePersonTel = triagePersonTel;
	}
	
	
}
