package com.tspeiz.modules.common.entity.weixin;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


/*
 * 图文会诊订单
 * @heyongb
 */
@Entity
@Table(name="GraphicsInfo")
public class GraphicsInfo implements Serializable{

	private static final long serialVersionUID = 1915074296977953237L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="ExpertId")
	private Integer expertId;
	
	@Column(name="OpenId",length=64)
	private String openId;
	
	@Column(name="UserName",length=16)
	private String userName;
	
	@Column(name="Sex",length=1)
	private String sex;
	
	@Column(name="Age")
	private Integer age;
	
	@Column(name="Telphone",length=20)
	private String telphone;
	
	@Type(type="text")
	@Column(name="SickInfo")
	private String sickInfo;//病情描述
	
	@Column(name="Status")
	private Integer status=0;//0未支付，1,已支付
	
	@Column(name="OutTradeNo",length=64)
	private String outTradeNo;//微信支付交易号
	
	@Column(name="PayMoney", columnDefinition = "float(8,2) default '0.00'")
	private Float payMoney;//支付金额
	
	@Column(name="RelaImages",length=128)
	private String relaImages;
	
	@Column(name="CreateTime",length=20)
	private String createTime;//提交时间
	
	@Column(name="RefreshTime",length=20)
	private String refreshTime;//更新时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public Integer getExpertId() {
		return expertId;
	}

	public void setExpertId(Integer expertId) {
		this.expertId = expertId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
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

	public String getSickInfo() {
		return sickInfo;
	}

	public void setSickInfo(String sickInfo) {
		this.sickInfo = sickInfo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public Float getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Float payMoney) {
		this.payMoney = payMoney;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(String refreshTime) {
		this.refreshTime = refreshTime;
	}

	public String getRelaImages() {
		return relaImages;
	}

	public void setRelaImages(String relaImages) {
		this.relaImages = relaImages;
	}
	
}
