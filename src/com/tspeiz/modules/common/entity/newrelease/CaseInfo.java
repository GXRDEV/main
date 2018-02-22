package com.tspeiz.modules.common.entity.newrelease;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;

/**
 * 病历信息表
 * 
 * @author liqi
 * 
 */
@Entity
@Table(name = "user_case_info")
public class CaseInfo implements Serializable {

	private static final long serialVersionUID = -5101450492008284746L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="Uuid")
	private String uuid;

	@Column(name = "UserId")
	private Integer userId;
	
	@Column(name="SubUserUuid")
	private String subUserUuid;
	
	@Column(name = "CreateTime")
	private Date createTime;//创建时间
	
	@Column(name = "ContactName", length = 50)
	private String contactName;//联系人姓名

	@Column(name = "IdCard", length = 18)
	private String idNumber;//身份证号

	@Column(name = "Sex")
	private Integer sex;//性别

	@Column(name = "BirthDay")
	private Date birthDay;//出生日期
	
	@Column(name = "Age")
	private Integer age;//年龄
	
	@Column(name = "Telephone", length = 50)
	private String telephone;//联系电话
	
	@Column(name = "CaseName", length = 50)
	private String caseName;//病例名称（疾病名称）

	@Column(name = "Keywords", length = 100)
	private String keywords;//关键字
	
	@Column(name = "DiseaseDes")
	private String description;//病情描述,放弃字段，改用现病史
	
	
	@Type(type = "text")
	@Column(name="MainSuit")
	private String mainSuit;//主诉
	
	@Type(type = "text")
	@Column(name = "PastHistory")
	private String historyIll;//既往史
	
	
	@Type(type = "text")
	@Column(name= "PresentHistory")
	private String presentIll;//现病史/病情描述

	@Type(type = "text")
	@Column(name = "Examined")
	private String examined;//体检
	
	@Type(type = "text")
	@Column(name = "AssistantResult")
	private String assistantResult;//辅检
	
	@Type(type = "text")
	@Column(name = "InitialDiagnosis")
	private String  initialDiagnosis;//诊断
	
	@Type(type = "text")
	@Column(name = "TreatAdvice")
	private String  treatAdvice;//治疗意见，放弃，无效字段
	
	@Type(type = "text")
	@Column(name = "FamilyHistory")
	private String  familyHistory;//家族史，放弃，无效字段
	
	@Column(name="NormalImages")
	private String normalImages; //其他病例图片
	
	@Column(name="UserType")
	private Integer userType;
	
	@Column(name="CheckReportImages")
	private String checkReportImages;  //检查报告
	
	@Column(name="RadiographFilmImages")
	private String radiographFilmImags; //印象胶片
	
	@Column(name="AskProblem")
	private String askProblem; //咨询问题，新增
	
	@Transient
	private String timeStr;
	
	@Transient
    private List<CustomFileStorage> caseImages;

	@Transient
	private List<UserCaseAttachment> attachments;//附件集合

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
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

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMainSuit() {
		return mainSuit;
	}

	public void setMainSuit(String mainSuit) {
		this.mainSuit = mainSuit;
	}

	public String getHistoryIll() {
		return historyIll;
	}

	public void setHistoryIll(String historyIll) {
		this.historyIll = historyIll;
	}

	public String getPresentIll() {
		return presentIll;
	}

	public void setPresentIll(String presentIll) {
		this.presentIll = presentIll;
	}

	public String getExamined() {
		return examined;
	}

	public void setExamined(String examined) {
		this.examined = examined;
	}

	public String getAssistantResult() {
		return assistantResult;
	}

	public void setAssistantResult(String assistantResult) {
		this.assistantResult = assistantResult;
	}

	public String getInitialDiagnosis() {
		return initialDiagnosis;
	}

	public void setInitialDiagnosis(String initialDiagnosis) {
		this.initialDiagnosis = initialDiagnosis;
	}

	public String getTreatAdvice() {
		return treatAdvice;
	}

	public void setTreatAdvice(String treatAdvice) {
		this.treatAdvice = treatAdvice;
	}

	public String getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public List<UserCaseAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UserCaseAttachment> attachments) {
		this.attachments = attachments;
	}

	public String getNormalImages() {
		return normalImages;
	}

	public void setNormalImages(String normalImages) {
		this.normalImages = normalImages;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getCheckReportImages() {
		return checkReportImages;
	}

	public void setCheckReportImages(String checkReportImages) {
		this.checkReportImages = checkReportImages;
	}

	public String getRadiographFilmImags() {
		return radiographFilmImags;
	}

	public void setRadiographFilmImags(String radiographFilmImags) {
		this.radiographFilmImags = radiographFilmImags;
	}

	public String getAskProblem() {
		return askProblem;
	}

	public void setAskProblem(String askProblem) {
		this.askProblem = askProblem;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSubUserUuid() {
		return subUserUuid;
	}

	public void setSubUserUuid(String subUserUuid) {
		this.subUserUuid = subUserUuid;
	}

	public List<CustomFileStorage> getCaseImages() {
		return caseImages;
	}

	public void setCaseImages(List<CustomFileStorage> caseImages) {
		this.caseImages = caseImages;
	}
	
}
