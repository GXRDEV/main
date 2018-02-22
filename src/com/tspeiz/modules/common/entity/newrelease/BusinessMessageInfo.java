package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 图文问诊消息记录
 * 
 * @author liqi
 * 
 */
@Entity
@Table(name = "business_message_tw")
public class BusinessMessageInfo implements Serializable {
	private static final long serialVersionUID = 5248437369330075814L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "WenZhen_Id")
	private Integer wenZhen_Id;

	@Column(name = "FromId")
	private Integer fromId;

	@Column(name = "FromUserType")
	private Integer fromUserType;

	@Column(name = "ToId")
	private Integer toId;

	@Column(name = "ToUserType")
	private Integer toUserType;

	@Column(name = "SendTime")
	private Date sendTime;

	@Column(name = "RecvTime")
	private Date recvTime;

	@Column(name = "MsgType")
	private String msgType;

	@Column(name = "MsgContent")
	private String msgContent;

	@Column(name = "Path")
	private String path;

	@Column(name = "SendStatus")
	private Integer sendStatus;
	
	@Transient
	private String msgTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWenZhen_Id() {
		return wenZhen_Id;
	}

	public void setWenZhen_Id(Integer wenZhen_Id) {
		this.wenZhen_Id = wenZhen_Id;
	}

	public Integer getFromId() {
		return fromId;
	}

	public void setFromId(Integer fromId) {
		this.fromId = fromId;
	}

	public Integer getFromUserType() {
		return fromUserType;
	}

	public void setFromUserType(Integer fromUserType) {
		this.fromUserType = fromUserType;
	}

	public Integer getToId() {
		return toId;
	}

	public void setToId(Integer toId) {
		this.toId = toId;
	}

	public Integer getToUserType() {
		return toUserType;
	}

	public void setToUserType(Integer toUserType) {
		this.toUserType = toUserType;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}
}
