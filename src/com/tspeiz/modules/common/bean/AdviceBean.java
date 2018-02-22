package com.tspeiz.modules.common.bean;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * 最近咨询 辅助类
 * @author heyongb
 *
 */
public class AdviceBean implements Serializable{
	private static final long serialVersionUID = 2199897874933626155L;

	private Integer orderId;
	
	private String orderType;
	
	private String headImage;
	
	private String docName;
	
	private String hosName;
	
	private String depName;
	
	private String duty;
	
	private String msgContent;
	
	private Timestamp msgTime;
	
	private String timeStr;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getHeadImage() {
		return headImage;
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

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public Timestamp getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(Timestamp msgTime) {
		this.msgTime = msgTime;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	
	
}
