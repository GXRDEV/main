package com.tspeiz.modules.common.bean;

import java.sql.Timestamp;

import javax.persistence.Column;

public class D2pOrderBean {
	
	private String type;
	
	private Integer id;
	private String uuid;
	
	private Integer caseId;
	private String caseName;
	private String idCard;
	private String presentIll;
	
	private Timestamp createTime;
	
	private Timestamp receiveTime;
	
	private Integer payStatus;
	
	private Integer status;
	
	private String docName;
	
	private String profession;
	
	private String hosName;
	
	private Integer doctorId;
	
	private Integer expertId;
	
	private String expertName;
	
	private String expertHos;
	
	private String expertDep;
	
	
	private String depName;
	
	private String userName;
	
	private Integer sex;
	
	private Integer age;
	
	private String telphone;
	
	private String orderDate;
	
	private String orderTime;
	
	private Integer orderDur;
	
	private Timestamp talkTime;
	
	private Integer talkDur;
	
	private Timestamp closedTime;
	
	private Integer closerType;
	
	private Integer closerId;
	
	private Integer source;
	
	private String remark;
	
	private Timestamp docFirstAnswerTime;

	private Timestamp docLastAnswerTime;

	private Timestamp patLastAnswerTime;

	private Integer docSendMsgCount;

	private Integer patSendMsgCount;

	private Integer docUnreadMsgNum;

	private Integer patUnreadMsgNum;

	private String askProblem;
	
	private String timeStr;
	
	private Integer chatStatus;
	
	private String teamName;
	
	private String tdocName;
	private String tprofession;
	private String thosName;
	private String tdepName;
	
	private Integer consultationType;
	private String caseDesc;
	private String answerTelephone;
	
	private Integer refundStatus;
	
	private String refundTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getOrderDur() {
		return orderDur;
	}

	public void setOrderDur(Integer orderDur) {
		this.orderDur = orderDur;
	}

	public Timestamp getTalkTime() {
		return talkTime;
	}

	public void setTalkTime(Timestamp talkTime) {
		this.talkTime = talkTime;
	}

	public Integer getTalkDur() {
		return talkDur;
	}

	public void setTalkDur(Integer talkDur) {
		this.talkDur = talkDur;
	}

	public Timestamp getClosedTime() {
		return closedTime;
	}

	public void setClosedTime(Timestamp closedTime) {
		this.closedTime = closedTime;
	}

	public Integer getCloserType() {
		return closerType;
	}

	public void setCloserType(Integer closerType) {
		this.closerType = closerType;
	}

	public Integer getCloserId() {
		return closerId;
	}

	public void setCloserId(Integer closerId) {
		this.closerId = closerId;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getDocFirstAnswerTime() {
		return docFirstAnswerTime;
	}

	public void setDocFirstAnswerTime(Timestamp docFirstAnswerTime) {
		this.docFirstAnswerTime = docFirstAnswerTime;
	}

	public Timestamp getDocLastAnswerTime() {
		return docLastAnswerTime;
	}

	public void setDocLastAnswerTime(Timestamp docLastAnswerTime) {
		this.docLastAnswerTime = docLastAnswerTime;
	}

	public Timestamp getPatLastAnswerTime() {
		return patLastAnswerTime;
	}

	public void setPatLastAnswerTime(Timestamp patLastAnswerTime) {
		this.patLastAnswerTime = patLastAnswerTime;
	}

	public Integer getDocSendMsgCount() {
		return docSendMsgCount;
	}

	public void setDocSendMsgCount(Integer docSendMsgCount) {
		this.docSendMsgCount = docSendMsgCount;
	}

	public Integer getPatSendMsgCount() {
		return patSendMsgCount;
	}

	public void setPatSendMsgCount(Integer patSendMsgCount) {
		this.patSendMsgCount = patSendMsgCount;
	}

	public Integer getDocUnreadMsgNum() {
		return docUnreadMsgNum;
	}

	public void setDocUnreadMsgNum(Integer docUnreadMsgNum) {
		this.docUnreadMsgNum = docUnreadMsgNum;
	}

	public Integer getPatUnreadMsgNum() {
		return patUnreadMsgNum;
	}

	public void setPatUnreadMsgNum(Integer patUnreadMsgNum) {
		this.patUnreadMsgNum = patUnreadMsgNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getAskProblem() {
		return askProblem;
	}

	public void setAskProblem(String askProblem) {
		this.askProblem = askProblem;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public Integer getExpertId() {
		return expertId;
	}

	public void setExpertId(Integer expertId) {
		this.expertId = expertId;
	}

	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	public String getExpertHos() {
		return expertHos;
	}

	public void setExpertHos(String expertHos) {
		this.expertHos = expertHos;
	}

	public String getExpertDep() {
		return expertDep;
	}

	public void setExpertDep(String expertDep) {
		this.expertDep = expertDep;
	}

	public Integer getChatStatus() {
		return chatStatus;
	}

	public void setChatStatus(Integer chatStatus) {
		this.chatStatus = chatStatus;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Integer getConsultationType() {
		return consultationType;
	}

	public void setConsultationType(Integer consultationType) {
		this.consultationType = consultationType;
	}

	public String getTdocName() {
		return tdocName;
	}

	public void setTdocName(String tdocName) {
		this.tdocName = tdocName;
	}

	public String getTprofession() {
		return tprofession;
	}

	public void setTprofession(String tprofession) {
		this.tprofession = tprofession;
	}

	public String getThosName() {
		return thosName;
	}

	public void setThosName(String thosName) {
		this.thosName = thosName;
	}

	public String getTdepName() {
		return tdepName;
	}

	public void setTdepName(String tdepName) {
		this.tdepName = tdepName;
	}

	public String getCaseDesc() {
		return caseDesc;
	}

	public void setCaseDesc(String caseDesc) {
		this.caseDesc = caseDesc;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPresentIll() {
		return presentIll;
	}

	public void setPresentIll(String presentIll) {
		this.presentIll = presentIll;
	}

	public String getAnswerTelephone() {
		return answerTelephone;
	}

	public void setAnswerTelephone(String answerTelephone) {
		this.answerTelephone = answerTelephone;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}	
}
