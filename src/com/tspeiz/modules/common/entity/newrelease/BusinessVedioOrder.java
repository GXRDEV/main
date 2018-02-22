package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 远程订单实体
 * @author heyongb
 *
 */
@Entity
@Table(name="business_vedio_order")
public class BusinessVedioOrder implements Serializable{
	private static final long serialVersionUID = -7600801836740663953L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="UUID")
	private String uuid; 
	
	@Column(name ="CaseId")
	private Integer caseId;
	
	@Column(name="CaseUuid")
	private String caseUuid;
	
	@Column(name ="UserId")
	private Integer userId;
	
	@Column(name ="ExpertId")
	private Integer expertId;
	
	@Column(name="ExpertType")
	private Integer expertType;
	
	@Column(name="ExpertHospitalId")
	private Integer expertHospitalId;
	
	@Column(name="ExpertDepId")
	private Integer expertDepId;
	
	@Column(name ="CreateTime")
	private Timestamp createTime;
	
	@Column(name ="LocalHospitalId")
	private Integer LocalHospitalId;
	
	@Column(name ="LocalDepartId")
	private Integer localDepartId;
	
	@Column(name ="LocalDoctorId")
	private Integer localDoctorId;
	
	@Column(name ="ConsultationDate")
	private String consultationDate;
	
	@Column(name ="ConsultationTime")
	private String consultationTime;
	
	@Column(name ="ConsultationDur",nullable=false,columnDefinition="INT default 20")
	private Integer consultationDur;
	
	@Column(name ="VedioSubType")
	private Integer vedioSubType;
	
	@Column(name ="OrderMode")
	private Integer orderMode;
	
	@Column(name ="LocalPlusNo")
	private String localPlusNo;
	
	@Column(name ="LocalPlusRet")
	private String localPlusRet;
	
	@Column(name ="VedioTime")
	private Timestamp vedioTime;
	
	@Column(name ="VedioDur")
	private BigInteger vedioDur;
	
	@Column(name ="Source")
	private Integer source;
	
	@Column(name ="ConsultationResult")
	private String consultationResult;
	
	@Column(name ="ClosedTime")
	private Timestamp closedTime;
	
	@Column(name ="CloserType")
	private Integer closerType;
	
	@Column(name ="CloserId")
	private Integer closerId;
	
	@Column(name ="OpenId")
	private String openId;
	
	@Column(name ="Status")
	private Integer status;
	
	@Column(name ="OrderNum")
	private Integer orderNum;
	
	@Column(name ="PreOrderId")
	private Integer preOrderId;
	
	@Column(name ="ProgressTag")
	private Integer progressTag;
	
	@Column(name ="PayStatus")
	private Integer payStatus;
	
	@Column(name="ExLevel",length=32)
	private String exLevel;//专家级别
	
	@Column(name="HelpOrder")
	private Integer helpOrder;
	
	@Column(name="SchedulerDateId")
	private Integer schedulerDateId;//选择的时间id
	
	@Column(name="HelpOrderSelExpert")
	private Integer helpOrderSelExpert;
	
	@Column(name="NurseId")
	private Integer nurseId;
	
	@Column(name ="DelFlag")
	private Integer delFlag;
	
	@Column(name="TreatPlan")
	private String treatPlan;//治疗方案
	
	@Column(name="Attentions")
	private String attentions;//注意事项
	
	@Column(name="ReplyTime")
	private Timestamp replyTime;
	
	@Column(name="Remark")
	private String remark;
	@Column(name="ConsultationOpinionUuid")
	private String consultationOpinionUuid;
	
	@Column(name="SignatureErWeiMa")
	private String signatureErWeiMa;
	
	@Column(name="RefusalReason")
	private String refusalReason;
	
	@Column(name="SubUserUuid")
	private String subUserUuid;
	
	@Column(name="AttachmentIds")
	private String attachmentIds;//辅助附件集--中日系统
	
	@Transient
	private Integer refundStatus;
	@Transient
	private String refundTime;
	@Transient
	private BigDecimal money;//费用
	
	/*@Column(name="AppendResult")
	private String appendResult;//追加报告
*/	
	@Transient
	private String timeStr;
	
	@Transient
	private String conDate;
	
	@Transient
	private String desc;
	
	@Transient
	private String hosName;

	@Transient
	private String depName;
	@Transient
	private String exProfession;
	
	@Transient
	private String expertName;
	
	@Transient
	private String localHosName;
	
	@Transient
	private String localDepName;
	
	@Transient
	private String localDocName;
	@Transient
	private String localProfession;
	
	@Transient
	private String dockingMode;
	
	@Transient
	private String patientName;
	
	@Transient
	private Integer sex;
	
	@Transient
	private String telephone;
	
	@Transient
	private Integer age;
	
	@Transient
	private String sortTime;
	@Transient
	private String idCard;
	
	@Transient
	private String expertHeadImage;
	
	@Transient
	private String localDocImage;
	
	@Transient
	private String caseName;
	@Transient
	private String diseaseDes;
	@Transient
	private String mainSuit;
	
	@Transient
	private String appTime;
	
	
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

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public Integer getLocalHospitalId() {
		return LocalHospitalId;
	}

	public void setLocalHospitalId(Integer localHospitalId) {
		LocalHospitalId = localHospitalId;
	}

	public Integer getLocalDepartId() {
		return localDepartId;
	}

	public void setLocalDepartId(Integer localDepartId) {
		this.localDepartId = localDepartId;
	}

	public Integer getLocalDoctorId() {
		return localDoctorId;
	}

	public void setLocalDoctorId(Integer localDoctorId) {
		this.localDoctorId = localDoctorId;
	}

	public String getConsultationDate() {
		return consultationDate;
	}

	public void setConsultationDate(String consultationDate) {
		this.consultationDate = consultationDate;
	}

	public String getConsultationTime() {
		return consultationTime;
	}

	public void setConsultationTime(String consultationTime) {
		this.consultationTime = consultationTime;
	}

	public Integer getConsultationDur() {
		return consultationDur;
	}

	public void setConsultationDur(Integer consultationDur) {
		this.consultationDur = consultationDur;
	}

	public Integer getVedioSubType() {
		return vedioSubType;
	}

	public void setVedioSubType(Integer vedioSubType) {
		this.vedioSubType = vedioSubType;
	}

	public Integer getOrderMode() {
		return orderMode;
	}

	public void setOrderMode(Integer orderMode) {
		this.orderMode = orderMode;
	}

	public String getLocalPlusNo() {
		return localPlusNo;
	}

	public void setLocalPlusNo(String localPlusNo) {
		this.localPlusNo = localPlusNo;
	}

	public String getLocalPlusRet() {
		return localPlusRet;
	}

	public void setLocalPlusRet(String localPlusRet) {
		this.localPlusRet = localPlusRet;
	}

	public Timestamp getVedioTime() {
		return vedioTime;
	}

	public void setVedioTime(Timestamp vedioTime) {
		this.vedioTime = vedioTime;
	}

	public BigInteger getVedioDur() {
		return vedioDur;
	}

	public void setVedioDur(BigInteger vedioDur) {
		this.vedioDur = vedioDur;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getConsultationResult() {
		return consultationResult;
	}

	public void setConsultationResult(String consultationResult) {
		this.consultationResult = consultationResult;
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

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getPreOrderId() {
		return preOrderId;
	}

	public void setPreOrderId(Integer preOrderId) {
		this.preOrderId = preOrderId;
	}

	public Integer getProgressTag() {
		return progressTag;
	}

	public void setProgressTag(Integer progressTag) {
		this.progressTag = progressTag;
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

	public String getExLevel() {
		return exLevel;
	}

	public void setExLevel(String exLevel) {
		this.exLevel = exLevel;
	}

	public Integer getSchedulerDateId() {
		return schedulerDateId;
	}

	public void setSchedulerDateId(Integer schedulerDateId) {
		this.schedulerDateId = schedulerDateId;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public String getConDate() {
		return conDate;
	}

	public void setConDate(String conDate) {
		this.conDate = conDate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public String getDockingMode() {
		return dockingMode;
	}

	public void setDockingMode(String dockingMode) {
		this.dockingMode = dockingMode;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSortTime() {
		return sortTime;
	}

	public void setSortTime(String sortTime) {
		this.sortTime = sortTime;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Integer getHelpOrder() {
		return helpOrder;
	}

	public void setHelpOrder(Integer helpOrder) {
		this.helpOrder = helpOrder;
	}

	public Integer getHelpOrderSelExpert() {
		return helpOrderSelExpert;
	}

	public void setHelpOrderSelExpert(Integer helpOrderSelExpert) {
		this.helpOrderSelExpert = helpOrderSelExpert;
	}

	public Integer getNurseId() {
		return nurseId;
	}

	public void setNurseId(Integer nurseId) {
		this.nurseId = nurseId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getExpertHeadImage() {
		return expertHeadImage;
	}

	public void setExpertHeadImage(String expertHeadImage) {
		this.expertHeadImage = expertHeadImage;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getDiseaseDes() {
		return diseaseDes;
	}

	public void setDiseaseDes(String diseaseDes) {
		this.diseaseDes = diseaseDes;
	}

	public String getMainSuit() {
		return mainSuit;
	}

	public void setMainSuit(String mainSuit) {
		this.mainSuit = mainSuit;
	}

	public String getLocalDocImage() {
		return localDocImage;
	}

	public void setLocalDocImage(String localDocImage) {
		this.localDocImage = localDocImage;
	}

	public String getTreatPlan() {
		return treatPlan;
	}

	public void setTreatPlan(String treatPlan) {
		this.treatPlan = treatPlan;
	}

	public String getAttentions() {
		return attentions;
	}

	public void setAttentions(String attentions) {
		this.attentions = attentions;
	}

	public Timestamp getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Timestamp replyTime) {
		this.replyTime = replyTime;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getExpertType() {
		return expertType;
	}

	public void setExpertType(Integer expertType) {
		this.expertType = expertType;
	}

	public Integer getExpertHospitalId() {
		return expertHospitalId;
	}

	public void setExpertHospitalId(Integer expertHospitalId) {
		this.expertHospitalId = expertHospitalId;
	}

	public Integer getExpertDepId() {
		return expertDepId;
	}

	public void setExpertDepId(Integer expertDepId) {
		this.expertDepId = expertDepId;
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
	/*public String getAppendResult() {
		return appendResult;
	}

	public void setAppendResult(String appendResult) {
		this.appendResult = appendResult;
	}*/

	public String getCaseUuid() {
		return caseUuid;
	}

	public void setCaseUuid(String caseUuid) {
		this.caseUuid = caseUuid;
	}

	public String getAttachmentIds() {
		return attachmentIds;
	}

	public void setAttachmentIds(String attachmentIds) {
		this.attachmentIds = attachmentIds;
	}

	public String getAppTime() {
		return appTime;
	}

	public void setAppTime(String appTime) {
		this.appTime = appTime;
	}
	
	
}
