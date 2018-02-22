package com.tspeiz.modules.common.bean.dto;
/**
 * 医生专家咨询 输出类
 * @author heyongb
 *
 */
public class D2DTuwenDto {
	private String uuid;
	private Integer orderId;//订单id
	private Integer expertId;//专家id
	private String userName;//患者姓名
	private Integer sex;//患者性别
	private Integer age;//患者年龄
	private String telephone;//联系电话
	private String exDocName;//专家姓名
	private String exHosName;//专家医院
	private String exDepName;//专家科室
	private String localDocName;
	private String localHosName;
	private String localDepName;
	private String createTime;//下单时间
	private String disCreateTime;//距离下单时间
	private Integer payStatus;//支付状态  1：已支付  4：未支付
	private String receiveTime;//接诊时间
	private String disReceiveTime;//距离接诊时间
	private Integer unReadMsgCount;//未读消息数
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getExpertId() {
		return expertId;
	}
	public void setExpertId(Integer expertId) {
		this.expertId = expertId;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDisCreateTime() {
		return disCreateTime;
	}
	public void setDisCreateTime(String disCreateTime) {
		this.disCreateTime = disCreateTime;
	}
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getDisReceiveTime() {
		return disReceiveTime;
	}
	public void setDisReceiveTime(String disReceiveTime) {
		this.disReceiveTime = disReceiveTime;
	}
	public Integer getUnReadMsgCount() {
		return unReadMsgCount;
	}
	public void setUnReadMsgCount(Integer unReadMsgCount) {
		this.unReadMsgCount = unReadMsgCount;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
}
