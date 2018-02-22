package com.tspeiz.modules.common.bean.dto;
/**
 * 视频会诊 输出类
 * @author heyongb
 *
 */
public class VedioOrderDto {
	private Integer vedioId;//视频会诊id
	private String userName;//患者姓名
	private Integer sex;//患者性别
	private Integer age;//患者年龄
	private String telephone;//联系电话
	private String createTime;//下单时间
	private String consultationDate;//会诊日期
	private String consultationTime;//会诊时间
	private Integer payStatus;//支付状态
	private String localDocName;//发起医生姓名
	private String localHosName;//发起医生医院
	private String localDepName;//发起医生科室
	private String exDocName;//接受医生姓名
	private String exHosName;//接受医生医院
	private String exDepName;//接受医生科室
	private Integer status;//订单状态
	private String disCreateTime;//距离下单时间
	private String disBeginTime;//
	public Integer getVedioId() {
		return vedioId;
	}
	public void setVedioId(Integer vedioId) {
		this.vedioId = vedioId;
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
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
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
	public String getExDocName() {
		return exDocName;
	}
	public void setExDocName(String exDocName) {
		this.exDocName = exDocName;
	}
	public String getExHosName() {
		return exHosName;
	}
	public void setExHosName(String exHosName) {
		this.exHosName = exHosName;
	}
	public String getExDepName() {
		return exDepName;
	}
	public void setExDepName(String exDepName) {
		this.exDepName = exDepName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDisCreateTime() {
		return disCreateTime;
	}
	public void setDisCreateTime(String disCreateTime) {
		this.disCreateTime = disCreateTime;
	}
	public String getDisBeginTime() {
		return disBeginTime;
	}
	public void setDisBeginTime(String disBeginTime) {
		this.disBeginTime = disBeginTime;
	}
	
	
}
