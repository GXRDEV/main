package com.tspeiz.modules.common.entity.newrelease;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "business_d_tuwen_order")
public class SpecialAdviceOrder implements Serializable {
	private static final long serialVersionUID = -3866877272361594821L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Integer id;

	@Column(name = "UUID")
	private String uuid;

	@Column(name = "CaseId")
	private Integer caseId;

	@Column(name="CaseUuid")
	private String caseUuid;
	
	@Column(name = "UserId")
	private Integer userId;

	@Column(name = "DoctorId")
	private Integer doctorId;

	@Column(name = "ExpertId")
	private Integer expertId;

	@Column(name = "ExpertType")
	private Integer expertType;

	@Column(name = "CreateTime")
	private Timestamp createTime;

	@Column(name = "ClosedTime")
	private Timestamp closedTime;

	@Column(name = "CloserType")
	private Integer closerType;

	@Column(name = "CloserId")
	private Integer closerId;

	@Column(name = "DocLastAnswerTime")
	private Timestamp docLastAnswerTime;

	@Column(name = "ExpertLastAnswerTime")
	private Timestamp expertLastAnswerTime;

	@Column(name = "DocSendMsgCount")
	private Integer docSendMsgCount;

	@Column(name = "ExpertSendMsgCount")
	private Integer expertSendMsgCount;

	@Column(name = "DocUnreadMsgNum")
	private Integer docUnreadMsgNum = 0;

	@Column(name = "ExpertUnreadMsgNum")
	private Integer expertUnreadMsgNum = 0;

	@Column(name = "Source")
	private Integer source;

	@Column(name = "OpenId")
	private String openid;

	@Column(name = "Status")
	private Integer status;

	@Column(name = "PayStatus")
	private Integer payStatus;

	@Column(name = "DelFlag")
	private Integer delFlag = 0;

	@Column(name = "ReceiveTime")
	private Timestamp receiveTime;
	@Column(name = "ConsultationOpinionUuid")
	private String consultationOpinionUuid;

	@Column(name = "SignatureErWeiMa")
	private String signatureErWeiMa;

	@Column(name = "RefusalReason")
	private String refusalReason;

	@Column(name = "SubUserUuid")
	private String subUserUuid;

	@Column(name = "LocalHospitalId")
	private Integer localHospitalId;
	
	
	@Transient
	private String userName;
	@Transient
	private String idCard;
	@Transient
	private Integer sex;
	@Transient
	private Integer age;
	@Transient
	private String telephone;
	@Transient
	private String expertName;
	@Transient
	private String hosName;
	@Transient
	private String depName;
	@Transient
	private String exProfession;
	@Transient
	private String headImageUrl;
	@Transient
	private String diseaseDes;
	@Transient
	private String localDocName;
	@Transient
	private String localHosName;
	@Transient
	private String localDepName;
	@Transient
	private String localProfession;
	@Transient
	private Integer refundStatus;
	@Transient
	private String refundTime;
	@Transient
	private String createTimes;
	@Transient
	private BigDecimal money;
	@Transient
	private String consultationDate;
	
	public String getConsultationDate() {
		return consultationDate;
	}

	public void setConsultationDate(String consultationDate) {
		this.consultationDate = consultationDate;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public String getCreateTimes() {
		return createTimes;
	}

	public void setCreateTimes(String createTimes) {
		this.createTimes = createTimes;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public Integer getExpertId() {
		return expertId;
	}

	public void setExpertId(Integer expertId) {
		this.expertId = expertId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
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

	public Timestamp getDocLastAnswerTime() {
		return docLastAnswerTime;
	}

	public void setDocLastAnswerTime(Timestamp docLastAnswerTime) {
		this.docLastAnswerTime = docLastAnswerTime;
	}

	public Timestamp getExpertLastAnswerTime() {
		return expertLastAnswerTime;
	}

	public void setExpertLastAnswerTime(Timestamp expertLastAnswerTime) {
		this.expertLastAnswerTime = expertLastAnswerTime;
	}

	public Integer getDocSendMsgCount() {
		return docSendMsgCount;
	}

	public void setDocSendMsgCount(Integer docSendMsgCount) {
		this.docSendMsgCount = docSendMsgCount;
	}

	public Integer getExpertSendMsgCount() {
		return expertSendMsgCount;
	}

	public void setExpertSendMsgCount(Integer expertSendMsgCount) {
		this.expertSendMsgCount = expertSendMsgCount;
	}

	public Integer getDocUnreadMsgNum() {
		return docUnreadMsgNum;
	}

	public void setDocUnreadMsgNum(Integer docUnreadMsgNum) {
		this.docUnreadMsgNum = docUnreadMsgNum;
	}

	public Integer getExpertUnreadMsgNum() {
		return expertUnreadMsgNum;
	}

	public void setExpertUnreadMsgNum(Integer expertUnreadMsgNum) {
		this.expertUnreadMsgNum = expertUnreadMsgNum;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
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

	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
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

	public String getHeadImageUrl() {
		return headImageUrl;
	}

	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}

	public String getDiseaseDes() {
		return diseaseDes;
	}

	public void setDiseaseDes(String diseaseDes) {
		this.diseaseDes = diseaseDes;
	}

	public String getLocalDocName() {
		return localDocName;
	}

	public void setLocalDocName(String localDocName) {
		this.localDocName = localDocName;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getExProfession() {
		return exProfession;
	}

	public void setExProfession(String exProfession) {
		this.exProfession = exProfession;
	}

	public String getLocalProfession() {
		return localProfession;
	}

	public void setLocalProfession(String localProfession) {
		this.localProfession = localProfession;
	}

	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Integer getExpertType() {
		return expertType;
	}

	public void setExpertType(Integer expertType) {
		this.expertType = expertType;
	}

	public String getConsultationOpinionUuid() {
		return consultationOpinionUuid;
	}

	public void setConsultationOpinionUuid(String consultationOpinionUuid) {
		this.consultationOpinionUuid = consultationOpinionUuid;
	}

	public String getSignatureErWeiMa() {
		return signatureErWeiMa;
	}

	public void setSignatureErWeiMa(String signatureErWeiMa) {
		this.signatureErWeiMa = signatureErWeiMa;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSubUserUuid() {
		return subUserUuid;
	}

	public void setSubUserUuid(String subUserUuid) {
		this.subUserUuid = subUserUuid;
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

	public String getCaseUuid() {
		return caseUuid;
	}

	public void setCaseUuid(String caseUuid) {
		this.caseUuid = caseUuid;
	}

	public Integer getLocalHospitalId() {
		return localHospitalId;
	}

	public void setLocalHospitalId(Integer localHospitalId) {
		this.localHospitalId = localHospitalId;
	}

}
