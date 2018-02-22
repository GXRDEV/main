package com.tspeiz.modules.common.bean.dto;
/**
 * 转诊订单 输出类
 * @author heyongb
 *
 */
public class ReferOrderDto {
	private Integer referId;//转诊订单id
	private String createTime;//下单时间
	private String disCreateTime;//距离下单时间 
	private String receiveTime;//接收时间
	private Integer status;//订单状态
	private String referDate;//转诊日期
	private String disReferDate;//距离转诊日期时间
	private Integer referType;//转诊类型  0：门诊  1：住院
	private String docName;//医生姓名
	private String hosName;//医生医院
	private String depName;//医生科室
	private String referDocName;//转诊医生姓名
	private String referHosName;//转诊医生医院
	private String referDepName;//转诊医生科室
	private String userName;//患者姓名
	private Integer sex;//患者性别
	private Integer age;//患者年龄
	private String telphone;//联系电话
	private String caseDesc;//病情描述
	public Integer getReferId() {
		return referId;
	}
	public void setReferId(Integer referId) {
		this.referId = referId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getReferDate() {
		return referDate;
	}
	public void setReferDate(String referDate) {
		this.referDate = referDate;
	}
	public Integer getReferType() {
		return referType;
	}
	public void setReferType(Integer referType) {
		this.referType = referType;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
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
	public String getReferDocName() {
		return referDocName;
	}
	public void setReferDocName(String referDocName) {
		this.referDocName = referDocName;
	}
	public String getReferHosName() {
		return referHosName;
	}
	public void setReferHosName(String referHosName) {
		this.referHosName = referHosName;
	}
	public String getReferDepName() {
		return referDepName;
	}
	public void setReferDepName(String referDepName) {
		this.referDepName = referDepName;
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
	public String getCaseDesc() {
		return caseDesc;
	}
	public void setCaseDesc(String caseDesc) {
		this.caseDesc = caseDesc;
	}
	public String getDisCreateTime() {
		return disCreateTime;
	}
	public void setDisCreateTime(String disCreateTime) {
		this.disCreateTime = disCreateTime;
	}
	public String getDisReferDate() {
		return disReferDate;
	}
	public void setDisReferDate(String disReferDate) {
		this.disReferDate = disReferDate;
	}
	
	
}
