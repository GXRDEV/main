


package com.tspeiz.modules.common.entity.newrelease;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 消息实体类
 * @author heyongb
 *
 */
@Entity
@Table(name="business_message_info")
public class BusinessMessageBean implements Serializable{
	private static final long serialVersionUID = -1787037139690751222L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="OrderUuid")
	private String orderUuid;
	
	@Column(name = "OrderId")
	private Integer orderId;
	
	@Column(name = "OrderType")
	private Integer orderType;
	
	@Column(name = "MsgType")
	private String msgType;
	
	@Column(name = "MsgContent")
	private String msgContent;
	
	@Column(name = "FileUrl")
	private String fileUrl;

	@Column(name = "RecvId")
	private Integer recvId;
	
	@Column(name = "RecvType")
	private Integer recvType;
	
	@Column(name = "SendId")
	private Integer sendId;
	
	@Column(name = "SendType")
	private Integer sendType;
	
	@Column(name = "SendTime")
	private Timestamp sendTime;
	
	@Column(name = "RecvTime")
	private Timestamp recvTime;
	
	@Column(name = "Status")
	private Integer status;
	
	@Column(name="ChannelType")
	private String channelType;//会话类型
	
	@Transient
	private String msgTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
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

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Integer getRecvId() {
		return recvId;
	}

	public void setRecvId(Integer recvId) {
		this.recvId = recvId;
	}

	public Integer getRecvType() {
		return recvType;
	}

	public void setRecvType(Integer recvType) {
		this.recvType = recvType;
	}

	public Integer getSendId() {
		return sendId;
	}

	public void setSendId(Integer sendId) {
		this.sendId = sendId;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public Timestamp getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(Timestamp recvTime) {
		this.recvTime = recvTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}

	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
}
