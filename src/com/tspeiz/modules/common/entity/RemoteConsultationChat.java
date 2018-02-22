package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RemoteConsultationChat")
public class RemoteConsultationChat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3784531229206444393L;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "FromId", nullable = false)
	private Integer fromId;

	@Column(name = "ToId", nullable = false)
	private Integer toId;

	@Column(name = "SendTime", nullable = false)
	private Date sendTime;

	@Column(name = "RecvTime", nullable = false)
	private Date recvTime;
	
	@Column(name = "MsgType", length = 1,nullable = false,columnDefinition="INT default 0")
	private Integer msgType;//0:文本1：图片

	@Column(name = "Status", length = 1,nullable = false,columnDefinition="INT default 0")
	private Integer status; //0 未读 1：已读
	
	@Column(name = "MsgContent", length = 1024)
	private String msgContent;
	
	@Column(name = "ImageUrl", length = 256)
	private String imageUrl;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFromId() {
		return fromId;
	}

	public void setFromId(Integer fromId) {
		this.fromId = fromId;
	}

	public Integer getToId() {
		return toId;
	}

	public void setToId(Integer toId) {
		this.toId = toId;
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

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
