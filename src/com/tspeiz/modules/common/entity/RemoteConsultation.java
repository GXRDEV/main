package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import com.sun.star.bridge.oleautomation.Decimal;

@Entity
@Table(name = "remote_consultation_order")
public class RemoteConsultation implements Serializable{
	private static final long serialVersionUID = 2833873944613949353L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "ExpertId")
	private Integer expertId;

	@Column(name = "LocalHospitalId", nullable = false)
	private Integer localHospitalId;

	@Column(name = "LocalDepartId", nullable = false)
	private Integer localDepartId;

	@Column(name = "LocalDoctorId")
	private Integer localDoctorId;

	@Column(name = "CreateTime")
	private Date createTime;

	@Column(name = "RefreshTime")
	private Date refreshTime;//一次下单
	
	@Column(name="SecondRefreshTime")
	private Date secondRefreshTime;//二次下单更新时间
	
	@Column(name="ThirdRefreshTime")
	private Date thirdRefreshTime;//三次下单更新时间

	@Column(name = "ConsultationTime", length = 5)
	private String consultationTime;
	
	@Column(name = "SecondConsultationTime", length = 5)
	private String secondConsultationTime;//二次下单选择时间
	
	@Column(name = "ThirdConsultationTime", length = 5)
	private String thirdConsultationTime;//三次下单选择时间

	@Column(name = "ConsultationDate", length = 10)
	private String consultationDate;
	
	@Column(name = "SecondConsultationDate", length = 10)
	private String secondConsultationDate;
	
	@Column(name = "ThirdConsultationDate", length = 10)
	private String thirdConsultationDate;

	@Column(name = "ConsultationDur", length = 11)
	private Integer consultationDur;

	@Column(name = "PatientName", length = 32)
	private String patientName;

	@Column(name = "Telephone", length = 11)
	private String telephone;

	@Column(name = "IdCard", length = 11)
	private String idCard;

	@Column(name = "Sex")
	private Integer sex;

	@Column(name = "Amount")
	private BigDecimal amount;

	@Column(name = "PayMode")
	private Integer payMode;

	@Column(name = "OpenId", length = 64)
	private String openId;

	@Column(name = "OutTradeNo", length = 64)
	private String outTradeNo;

	@Column(name = "Status")
	private Integer status;

	@Column(name = "LocalPlusNo", length = 64)
	private String localPlusNo;

	@Type(type="text")
	@Column(name = "NormalImages", length = 512)
	private String normalImages;

	@Column(name = "DicomImages", length = 512)
	private String dicomImages;

	@Type(type = "text")
	@Column(name = "DetailInfo")
	private String detailInfo;

	@Column(name = "ChatPackId")
	private Integer chatPackId;

	@Column(name = "ProgressTag", length = 1)
	private Integer progressTag; // 进度标签  默认编辑病例    2===发送病例到专家   3===有事离开  4===已就绪，待链接   5--结束

	@Column(name = "ConsultationResult", length = 1024)
	private String consultationResult; // 会诊结果
	
	@Column(name="LocalAddress",length=128)
	private String localAddress;//患者地址
	
	@Column(name="VedioUID",length=128)
	private String vedioUID;
	
	@Column(name="HasUser",length=2)
	private String hasUser="0";//1有一人在连接，0无人在连接,2有两人在连接
	
	@Column(name="VedioTime")
	private Long vedioTime;
	
	@Column(name="RetId")
	private String retId;
	
	@Column(name="RetMessage")
	private String retMessage;
	
	@Column(name="ScheduleDateId")
	private Integer schedulerDateId;
	
	@Column(name="NurseId")
	private Integer nurseId;
	
	@Column(name="ExpertReaded")
	private Integer expertReaded;//专家是否查看
	
	@Column(name="HelpOrder",length=1)
	private String helpOrder;//是否医生的辅助下单
	
	@Column(name="HelpOrderSelExpert")
	private String helpOrderSelExpert;//辅助下单医生是否选择了专家   1---表示选择了
	
	@Column(name="Source")
	private Integer source;//1--微信公众号  2-佰医汇官网  3-护士 4-医生
	
	@Column(name="UserId")
	private Integer userId;
	
	@Column(name="PayStatus")
	private Integer payStatus=0;//0--未支付  1--已支付
	
	@Column(name="DeleFlag")
	private Integer deleFlag=0;//删除标记 0，1表示已删除
	
	@Column(name="RemoteSub",length=1)
	private Integer remoteSub=0;//0-远程门诊，1-远程问诊
	
	@Column(name="Age")
	private Integer age;//年龄
	
	@Column(name="ExLevel",length=32)
	private String exLevel;//专家级别
	
	@Transient
	private String sortTime;
	
	@Transient
	private String conDate;

	@Transient
	private String expertName;//专家姓名
	@Transient
	private String hosName;// 专家医院
	
	@Transient
	private String depName;// 专家科室
	
	@Transient
	private String localHosName;//当地医院
	
	@Transient
	private String localDepName;//当地科室
	
	@Transient
	private String localDocName;//当地医生
	
	@Transient
	private String hosAddress;//当地医院详细地址
	
	@Transient
	private BigDecimal lat;
	
	@Transient 
	private BigDecimal lng;
	
	@Transient
	private String expertTitle;//头衔--职称。。
	
	@Transient
	private String duty;
	
	@Transient
	private String pos;
	@Transient
	private String profession;
	@Transient
	private String dockingMode;//对接模式
	
	@Transient
	private BigInteger testId;
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

	public Integer getExpertId() {
		return expertId;
	}

	public void setExpertId(Integer expertId) {
		this.expertId = expertId;
	}

	public Integer getLocalHospitalId() {
		return localHospitalId;
	}

	public void setLocalHospitalId(Integer localHospitalId) {
		this.localHospitalId = localHospitalId;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(Date refreshTime) {
		this.refreshTime = refreshTime;
	}

	public String getConsultationTime() {
		return consultationTime;
	}

	public void setConsultationTime(String consultationTime) {
		this.consultationTime = consultationTime;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLocalPlusNo() {
		return localPlusNo;
	}

	public void setLocalPlusNo(String localPlusNo) {
		this.localPlusNo = localPlusNo;
	}

	public String getNormalImages() {
		return normalImages;
	}

	public void setNormalImages(String normalImages) {
		this.normalImages = normalImages;
	}

	public String getDicomImages() {
		return dicomImages;
	}

	public void setDicomImages(String dicomImages) {
		this.dicomImages = dicomImages;
	}

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public Integer getChatPackId() {
		return chatPackId;
	}

	public void setChatPackId(Integer chatPackId) {
		this.chatPackId = chatPackId;
	}

	public Integer getConsultationDur() {
		return consultationDur;
	}

	public void setConsultationDur(Integer consultationDur) {
		this.consultationDur = consultationDur;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public String getConsultationDate() {
		return consultationDate;
	}

	public void setConsultationDate(String consultationDate) {
		this.consultationDate = consultationDate;
	}

	public Integer getProgressTag() {
		return progressTag;
	}

	public void setProgressTag(Integer progressTag) {
		this.progressTag = progressTag;
	}

	public String getConsultationResult() {
		return consultationResult;
	}

	public void setConsultationResult(String consultationResult) {
		this.consultationResult = consultationResult;
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

	public Date getSecondRefreshTime() {
		return secondRefreshTime;
	}

	public void setSecondRefreshTime(Date secondRefreshTime) {
		this.secondRefreshTime = secondRefreshTime;
	}

	public Date getThirdRefreshTime() {
		return thirdRefreshTime;
	}

	public void setThirdRefreshTime(Date thirdRefreshTime) {
		this.thirdRefreshTime = thirdRefreshTime;
	}

	public String getSecondConsultationTime() {
		return secondConsultationTime;
	}

	public void setSecondConsultationTime(String secondConsultationTime) {
		this.secondConsultationTime = secondConsultationTime;
	}

	public String getThirdConsultationTime() {
		return thirdConsultationTime;
	}

	public void setThirdConsultationTime(String thirdConsultationTime) {
		this.thirdConsultationTime = thirdConsultationTime;
	}

	public String getSecondConsultationDate() {
		return secondConsultationDate;
	}

	public void setSecondConsultationDate(String secondConsultationDate) {
		this.secondConsultationDate = secondConsultationDate;
	}

	public String getThirdConsultationDate() {
		return thirdConsultationDate;
	}

	public void setThirdConsultationDate(String thirdConsultationDate) {
		this.thirdConsultationDate = thirdConsultationDate;
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	public String getVedioUID() {
		return vedioUID;
	}

	public void setVedioUID(String vedioUID) {
		this.vedioUID = vedioUID;
	}

	public String getSortTime() {
		return sortTime;
	}

	public void setSortTime(String sortTime) {
		this.sortTime = sortTime;
	}

	public String getConDate() {
		return conDate;
	}

	public void setConDate(String conDate) {
		this.conDate = conDate;
	}

	public String getHasUser() {
		return hasUser;
	}

	public void setHasUser(String hasUser) {
		this.hasUser = hasUser;
	}

	public Long getVedioTime() {
		return vedioTime;
	}

	public void setVedioTime(Long vedioTime) {
		this.vedioTime = vedioTime;
	}

	public String getRetId() {
		return retId;
	}

	public void setRetId(String retId) {
		this.retId = retId;
	}

	public String getRetMessage() {
		return retMessage;
	}

	public void setRetMessage(String retMessage) {
		this.retMessage = retMessage;
	}

	public Integer getSchedulerDateId() {
		return schedulerDateId;
	}

	public void setSchedulerDateId(Integer schedulerDateId) {
		this.schedulerDateId = schedulerDateId;
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

	public String getHosAddress() {
		return hosAddress;
	}

	public void setHosAddress(String hosAddress) {
		this.hosAddress = hosAddress;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public String getExpertTitle() {
		return expertTitle;
	}

	public void setExpertTitle(String expertTitle) {
		this.expertTitle = expertTitle;
	}

	public Integer getNurseId() {
		return nurseId;
	}

	public void setNurseId(Integer nurseId) {
		this.nurseId = nurseId;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public Integer getExpertReaded() {
		return expertReaded;
	}

	public void setExpertReaded(Integer expertReaded) {
		this.expertReaded = expertReaded;
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

	public String getHelpOrder() {
		return helpOrder;
	}

	public void setHelpOrder(String helpOrder) {
		this.helpOrder = helpOrder;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getHelpOrderSelExpert() {
		return helpOrderSelExpert;
	}

	public void setHelpOrderSelExpert(String helpOrderSelExpert) {
		this.helpOrderSelExpert = helpOrderSelExpert;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getDeleFlag() {
		return deleFlag;
	}

	public void setDeleFlag(Integer deleFlag) {
		this.deleFlag = deleFlag;
	}

	public Integer getRemoteSub() {
		return remoteSub;
	}

	public void setRemoteSub(Integer remoteSub) {
		this.remoteSub = remoteSub;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
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

	public String getExLevel() {
		return exLevel;
	}

	public void setExLevel(String exLevel) {
		this.exLevel = exLevel;
	}
}
