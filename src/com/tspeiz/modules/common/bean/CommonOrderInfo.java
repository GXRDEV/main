package com.tspeiz.modules.common.bean;

public class CommonOrderInfo {

	private String orderUuid;// 订单uuid
	private Integer caseId;// 病例id
	private Integer doctorId;// 医生id
	private Integer expertId;//专家id
	private Integer payStatus;//支付状态
	private Integer source;//订单来源
	private String  consultationResult;//复核结果
	private Integer status;//订单状态
	private Integer referralType;//转诊类型
	private String referralDate;//转诊日期
	// 会诊申请
	private String teamUuid;// 团队uuid

	// 电话问诊
	private String answerTelephone;// 接听电话
	private String answerTime;// 接听时间
	
	
	

	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getAnswerTelephone() {
		return answerTelephone;
	}

	public void setAnswerTelephone(String answerTelephone) {
		this.answerTelephone = answerTelephone;
	}

	public String getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getTeamUuid() {
		return teamUuid;
	}

	public void setTeamUuid(String teamUuid) {
		this.teamUuid = teamUuid;
	}

	public Integer getExpertId() {
		return expertId;
	}

	public void setExpertId(Integer expertId) {
		this.expertId = expertId;
	}

	public String getConsultationResult() {
		return consultationResult;
	}

	public void setConsultationResult(String consultationResult) {
		this.consultationResult = consultationResult;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getReferralType() {
		return referralType;
	}

	public void setReferralType(Integer referralType) {
		this.referralType = referralType;
	}

	public String getReferralDate() {
		return referralDate;
	}

	public void setReferralDate(String referralDate) {
		this.referralDate = referralDate;
	}


}
