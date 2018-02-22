package com.tspeiz.modules.common.bean;

import java.math.BigInteger;
import java.util.Date;


public class WenzhenBean {
	private String type;
	
	private Integer id;
	
	private Integer userId;

	private String contactName;//患者
	
	private Integer doctorId;
	
	private Integer sex;//性别
	
	private Integer age;//年龄

	private String telephone;//联系电话
	

	private String docName;//问诊专家

	private String hospital;//专家医院
	
	private String depart;//专家科室
	
	private String docTel;//专家电话
	
	private Integer askStatus;//问诊状态
	
	private Date createTime;//提交时间
	
	private Integer caseId;//病例Id
	
	private Integer payStatus;//支付状态  0-免费  1-已支付 4-待支付
	
	private Integer docMessageCount=0;
	
	private Integer patMessageCount=0;
	
	private String subTime;//提交时间
	
	private String origin;//来源
	
	private String desc;
	
	private Integer paStatus;
	
	private String timeStr;
	
	private Integer source;
	

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getContactName() {
		return contactName;
	}


	public void setContactName(String contactName) {
		this.contactName = contactName;
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


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public String getDocName() {
		return docName;
	}


	public void setDocName(String docName) {
		this.docName = docName;
	}


	public Integer getAskStatus() {
		return askStatus;
	}


	public void setAskStatus(Integer askStatus) {
		this.askStatus = askStatus;
	}


	public String getHospital() {
		return hospital;
	}


	public void setHospital(String hospital) {
		this.hospital = hospital;
	}


	public String getDepart() {
		return depart;
	}


	public void setDepart(String depart) {
		this.depart = depart;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Integer getCaseId() {
		return caseId;
	}


	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}


	public Integer getPayStatus() {
		return payStatus;
	}


	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
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


	public String getSubTime() {
		return subTime;
	}


	public void setSubTime(String subTime) {
		this.subTime = subTime;
	}


	public String getDocTel() {
		return docTel;
	}


	public void setDocTel(String docTel) {
		this.docTel = docTel;
	}


	public String getOrigin() {
		return origin;
	}


	public void setOrigin(String origin) {
		this.origin = origin;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public Integer getDoctorId() {
		return doctorId;
	}


	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}


	public Integer getPaStatus() {
		return paStatus;
	}


	public void setPaStatus(Integer paStatus) {
		this.paStatus = paStatus;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getTimeStr() {
		return timeStr;
	}


	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}


	public Integer getSource() {
		return source;
	}


	public void setSource(Integer source) {
		this.source = source;
	}
	
	
}
