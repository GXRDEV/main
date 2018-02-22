package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="system_push_info")
public class SystemPushInfo implements Serializable{
	private static final long serialVersionUID = 5327946125278934519L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="PushKey")
	private String pushKey;
	
	@Column(name="PushCode")
	private Integer pushCode;
	
	@Column(name="CreateTime")
	private Date CreateTime;
	
	@Column(name="BusinessKey")
	private String businessKey;
	
	@Column(name="BusinessType")
	private Integer businessType;
	
	@Column(name="UserId")
	private Integer userId;
	
	@Column(name="UserType")
	private Integer userType;
	
	@Column(name="Content")
	private String content;
	
	@Column(name="PushResponse")
	private String pushResponse;
	
	@Column(name="IsRead")
	private Integer isRead=0;
	
	@Column(name="ReadTime")
	private Date readTime;
	
	@Column(name="BusinessExtend")//业务扩展字段
	private String businessExtend;
	
	@Column(name="CreateTimeExtend") 
	private String createTimeExtend;
	
	@Transient
	private String timeStr;//距当前时间
	@Transient
	private String desc;//描述
	
    @Transient
    private Integer badge;//应用icon上显示的数字

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPushKey() {
		return pushKey;
	}

	public void setPushKey(String pushKey) {
		this.pushKey = pushKey;
	}

	public Integer getPushCode() {
		return pushCode;
	}

	public void setPushCode(Integer pushCode) {
		this.pushCode = pushCode;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPushResponse() {
		return pushResponse;
	}

	public void setPushResponse(String pushResponse) {
		this.pushResponse = pushResponse;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
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

	public String getBusinessExtend() {
		return businessExtend;
	}

	public void setBusinessExtend(String businessExtend) {
		this.businessExtend = businessExtend;
	}

	public String getCreateTimeExtend() {
		return createTimeExtend;
	}

	public void setCreateTimeExtend(String createTimeExtend) {
		this.createTimeExtend = createTimeExtend;
	}

	public Integer getBadge() {
		return badge;
	}

	public void setBadge(Integer badge) {
		this.badge = badge;
	}
	
}
