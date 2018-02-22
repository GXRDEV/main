package com.tspeiz.modules.common.bean.dto;

import java.io.Serializable;
import java.math.BigInteger;

public class DocFollowDto implements Serializable{
	private static final long serialVersionUID = 2135052321111716813L;
	private String orderType;
	private String orderUuid;
	private Integer docId;
	private Integer subUserId;
	private String docName;
	private String docHos;
	private String docDep;
	private String userName;
	private String msgTime;
	private String msgContent;
	private BigInteger msgCount;
	
	private Integer sendType;
	
	private String fileUrl;
	private String msgType;
	
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDocHos() {
		return docHos;
	}
	public void setDocHos(String docHos) {
		this.docHos = docHos;
	}
	public String getDocDep() {
		return docDep;
	}
	public void setDocDep(String docDep) {
		this.docDep = docDep;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderUuid() {
		return orderUuid;
	}
	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}
	public BigInteger getMsgCount() {
		return msgCount;
	}
	public void setMsgCount(BigInteger msgCount) {
		this.msgCount = msgCount;
	}
	public Integer getDocId() {
		return docId;
	}
	public void setDocId(Integer docId) {
		this.docId = docId;
	}
	public Integer getSubUserId() {
		return subUserId;
	}
	public void setSubUserId(Integer subUserId) {
		this.subUserId = subUserId;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	
	
}
