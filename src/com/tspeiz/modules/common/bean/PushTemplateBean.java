package com.tspeiz.modules.common.bean;

import java.util.Date;

/**
 * 推送内容模版
 * 
 * @author liqi
 * 
 */
public class PushTemplateBean {

	// push唯一标识
	private String pushKey;

	// 推送含义
	private Integer pushCode;

	private Date createTime;

	// 业务主键
	private String businessKey;

	// 业务类别
	private Integer businessType;

	// 推送显示内容
	private String content;

	private Integer isRead;
	
	//业务扩展字段
	private String BusinessExtend;
	
	private String createTimeExtend;

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
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public String getBusinessExtend() {
		return BusinessExtend;
	}

	public void setBusinessExtend(String businessExtend) {
		BusinessExtend = businessExtend;
	}

	public String getCreateTimeExtend() {
		return createTimeExtend;
	}

	public void setCreateTimeExtend(String createTimeExtend) {
		this.createTimeExtend = createTimeExtend;
	}
	
	
}
