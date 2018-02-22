package com.tspeiz.modules.common.bean.weixin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.sun.star.bridge.oleautomation.Decimal;

public class MobileSpecial implements Serializable{
	private Integer specialId;// 专家id

	private String specialName;// 专家姓名
	
	private Integer userType;

	private String specialTitle;// 职称

	private String specialty;// 擅长

	private Integer hosId;// 医院Id

	private String hosName;// 医院

	private Integer depId;// 科室id

	private String depName;// 科室名称

	private String listSpecialPicture;//头像

	private String detailsProfilePicture;

	private Integer openAsk;//
	private Integer openAddNum;
	private Integer openTel;
	private Integer openEmergency;
	private Integer openVedio;

	private Integer addNumCount;

	private Integer rank;

	private String regisStr;// 出诊时间段

	private String duty;
	
	private Integer dutyId;

	private String position;

	private String telphone;
	private String mobileTelphone;//联系电话

	private String profession;

	private String profile;

	private String relatedPics;

	private String hosLevel;

	private Timestamp registerTime;// 注册时间
	private String registerTimes;// 注册时间

	private Integer recommond;// 是否推荐
	
	private String distCode;
	
	private Integer openVideo;//视频问诊
	
	private BigDecimal vedioAmount;
	private BigDecimal askAmount;//图文问诊价格
	private BigDecimal telAmount;//电话问诊价格
	
	private BigDecimal adviceAmount;
	
	private Integer openAdvice;
	
	private Integer famousDoctor;//名医介绍显示
	private String outTime;
	
	private String erweimaUrl;
	
	private String badgeUrl;//执照
	
	
	private Integer status;
	
	private Integer areaOptimal;//是否推优
	
	private String distName;
	
	
	private String idCardNo;

	private String serviceName; //服务

	private String shengName;
	private String shiName;
	
	private Integer isOpenvedio;//是否开通远程会诊
	private Integer isOpentuwen;//是否开通图文会诊
	
	private String invitCode;//邀请码
	
	private Integer isTest;
	
	private Integer sex;

    private BigDecimal warmMoney;

	public MobileSpecial() {
		super();
	}

	public MobileSpecial(Integer specialId, String specialName, Integer userType, String specialTitle, String specialty,
			Integer hosId, String hosName, Integer depId, String depName, String listSpecialPicture,
			String detailsProfilePicture, Integer openAsk, Integer openAddNum, Integer openTel, Integer openEmergency,
			Integer openVedio, Integer addNumCount, Integer rank, String regisStr, String duty, Integer dutyId,
			String position, String telphone, String mobileTelphone, String profession, String profile,
			String relatedPics, String hosLevel, Timestamp registerTime, String registerTimes, Integer recommond,
			String distCode, Integer openVideo, BigDecimal vedioAmount, BigDecimal askAmount, BigDecimal telAmount,
			BigDecimal adviceAmount, Integer openAdvice, Integer famousDoctor, String outTime, String erweimaUrl,
			String badgeUrl, Integer status, Integer areaOptimal, String distName, String idCardNo, String serviceName,
			String shengName, String shiName, Integer isOpenvedio, Integer isOpentuwen, String invitCode,
			Integer isTest, Integer sex, BigDecimal warmMoney) {
		super();
		this.specialId = specialId;
		this.specialName = specialName;
		this.userType = userType;
		this.specialTitle = specialTitle;
		this.specialty = specialty;
		this.hosId = hosId;
		this.hosName = hosName;
		this.depId = depId;
		this.depName = depName;
		this.listSpecialPicture = listSpecialPicture;
		this.detailsProfilePicture = detailsProfilePicture;
		this.openAsk = openAsk;
		this.openAddNum = openAddNum;
		this.openTel = openTel;
		this.openEmergency = openEmergency;
		this.openVedio = openVedio;
		this.addNumCount = addNumCount;
		this.rank = rank;
		this.regisStr = regisStr;
		this.duty = duty;
		this.dutyId = dutyId;
		this.position = position;
		this.telphone = telphone;
		this.mobileTelphone = mobileTelphone;
		this.profession = profession;
		this.profile = profile;
		this.relatedPics = relatedPics;
		this.hosLevel = hosLevel;
		this.registerTime = registerTime;
		this.registerTimes = registerTimes;
		this.recommond = recommond;
		this.distCode = distCode;
		this.openVideo = openVideo;
		this.vedioAmount = vedioAmount;
		this.askAmount = askAmount;
		this.telAmount = telAmount;
		this.adviceAmount = adviceAmount;
		this.openAdvice = openAdvice;
		this.famousDoctor = famousDoctor;
		this.outTime = outTime;
		this.erweimaUrl = erweimaUrl;
		this.badgeUrl = badgeUrl;
		this.status = status;
		this.areaOptimal = areaOptimal;
		this.distName = distName;
		this.idCardNo = idCardNo;
		this.serviceName = serviceName;
		this.shengName = shengName;
		this.shiName = shiName;
		this.isOpenvedio = isOpenvedio;
		this.isOpentuwen = isOpentuwen;
		this.invitCode = invitCode;
		this.isTest = isTest;
		this.sex = sex;
		this.warmMoney = warmMoney;
	}

	public Integer getSpecialId() {
		return specialId;
	}

	public void setSpecialId(Integer specialId) {
		this.specialId = specialId;
	}

	public String getSpecialName() {
		return specialName;
	}

	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getSpecialTitle() {
		return specialTitle;
	}

	public void setSpecialTitle(String specialTitle) {
		this.specialTitle = specialTitle;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public Integer getHosId() {
		return hosId;
	}

	public void setHosId(Integer hosId) {
		this.hosId = hosId;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public Integer getDepId() {
		return depId;
	}

	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getListSpecialPicture() {
		return listSpecialPicture;
	}

	public void setListSpecialPicture(String listSpecialPicture) {
		this.listSpecialPicture = listSpecialPicture;
	}

	public String getDetailsProfilePicture() {
		return detailsProfilePicture;
	}

	public void setDetailsProfilePicture(String detailsProfilePicture) {
		this.detailsProfilePicture = detailsProfilePicture;
	}

	public Integer getOpenAsk() {
		return openAsk;
	}

	public void setOpenAsk(Integer openAsk) {
		this.openAsk = openAsk;
	}

	public Integer getOpenAddNum() {
		return openAddNum;
	}

	public void setOpenAddNum(Integer openAddNum) {
		this.openAddNum = openAddNum;
	}

	public Integer getOpenTel() {
		return openTel;
	}

	public void setOpenTel(Integer openTel) {
		this.openTel = openTel;
	}

	public Integer getOpenEmergency() {
		return openEmergency;
	}

	public void setOpenEmergency(Integer openEmergency) {
		this.openEmergency = openEmergency;
	}

	public Integer getOpenVedio() {
		return openVedio;
	}

	public void setOpenVedio(Integer openVedio) {
		this.openVedio = openVedio;
	}

	public Integer getAddNumCount() {
		return addNumCount;
	}

	public void setAddNumCount(Integer addNumCount) {
		this.addNumCount = addNumCount;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getRegisStr() {
		return regisStr;
	}

	public void setRegisStr(String regisStr) {
		this.regisStr = regisStr;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public Integer getDutyId() {
		return dutyId;
	}

	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getMobileTelphone() {
		return mobileTelphone;
	}

	public void setMobileTelphone(String mobileTelphone) {
		this.mobileTelphone = mobileTelphone;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getRelatedPics() {
		return relatedPics;
	}

	public void setRelatedPics(String relatedPics) {
		this.relatedPics = relatedPics;
	}

	public String getHosLevel() {
		return hosLevel;
	}

	public void setHosLevel(String hosLevel) {
		this.hosLevel = hosLevel;
	}

	public Timestamp getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	public String getRegisterTimes() {
		return registerTimes;
	}

	public void setRegisterTimes(String registerTimes) {
		this.registerTimes = registerTimes;
	}

	public Integer getRecommond() {
		return recommond;
	}

	public void setRecommond(Integer recommond) {
		this.recommond = recommond;
	}

	public String getDistCode() {
		return distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	public Integer getOpenVideo() {
		return openVideo;
	}

	public void setOpenVideo(Integer openVideo) {
		this.openVideo = openVideo;
	}

	public BigDecimal getVedioAmount() {
		return vedioAmount;
	}

	public void setVedioAmount(BigDecimal vedioAmount) {
		this.vedioAmount = vedioAmount;
	}

	public BigDecimal getAskAmount() {
		return askAmount;
	}

	public void setAskAmount(BigDecimal askAmount) {
		this.askAmount = askAmount;
	}

	public BigDecimal getTelAmount() {
		return telAmount;
	}

	public void setTelAmount(BigDecimal telAmount) {
		this.telAmount = telAmount;
	}

	public BigDecimal getAdviceAmount() {
		return adviceAmount;
	}

	public void setAdviceAmount(BigDecimal adviceAmount) {
		this.adviceAmount = adviceAmount;
	}

	public Integer getOpenAdvice() {
		return openAdvice;
	}

	public void setOpenAdvice(Integer openAdvice) {
		this.openAdvice = openAdvice;
	}

	public Integer getFamousDoctor() {
		return famousDoctor;
	}

	public void setFamousDoctor(Integer famousDoctor) {
		this.famousDoctor = famousDoctor;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getErweimaUrl() {
		return erweimaUrl;
	}

	public void setErweimaUrl(String erweimaUrl) {
		this.erweimaUrl = erweimaUrl;
	}

	public String getBadgeUrl() {
		return badgeUrl;
	}

	public void setBadgeUrl(String badgeUrl) {
		this.badgeUrl = badgeUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAreaOptimal() {
		return areaOptimal;
	}

	public void setAreaOptimal(Integer areaOptimal) {
		this.areaOptimal = areaOptimal;
	}

	public String getDistName() {
		return distName;
	}

	public void setDistName(String distName) {
		this.distName = distName;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getShengName() {
		return shengName;
	}

	public void setShengName(String shengName) {
		this.shengName = shengName;
	}

	public String getShiName() {
		return shiName;
	}

	public void setShiName(String shiName) {
		this.shiName = shiName;
	}

	public Integer getIsOpenvedio() {
		return isOpenvedio;
	}

	public void setIsOpenvedio(Integer isOpenvedio) {
		this.isOpenvedio = isOpenvedio;
	}

	public Integer getIsOpentuwen() {
		return isOpentuwen;
	}

	public void setIsOpentuwen(Integer isOpentuwen) {
		this.isOpentuwen = isOpentuwen;
	}

	public String getInvitCode() {
		return invitCode;
	}

	public void setInvitCode(String invitCode) {
		this.invitCode = invitCode;
	}

	public Integer getIsTest() {
		return isTest;
	}

	public void setIsTest(Integer isTest) {
		this.isTest = isTest;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public BigDecimal getWarmMoney() {
		return warmMoney;
	}

	public void setWarmMoney(BigDecimal warmMoney) {
		this.warmMoney = warmMoney;
	}

	
}