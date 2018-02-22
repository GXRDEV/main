package com.tspeiz.modules.common.entity.weixin;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="Orders")
public class Orders implements Serializable{
	
	private static final long serialVersionUID = 5061802326736141944L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "ConversationId")
	private Integer  conversationId;
	
	@Column(name = "Amount")
	private BigDecimal amount;
	
	@Column(name = "Status")
	private Integer status;
	
	@Column(name = "PayMode")
	private Integer payMode;
	
	@Column(name = "CreateTime")
	private Long createTime;
	
	@Column(name = "PayTime")
	private Long payTime;
	
	@Column(name = "TalkingNumber")
	private Integer talkingNumber;
	
	@Column(name = "FlowNumber")
	private String flowNumber;
	
	@Column(name = "OrderType")
	private Integer orderType;
	
	@Column(name = "PresentTime")
	private Long presentTime;
	
	@Column(name = "FromId")
	private Integer fromId;
	
	@Column(name = "ToId")
	private Integer toId;
	
	@Column(name = "ParentId")
	private Integer parentId;
	
	@Column(name = "PlaceTime")
	private Long placeTime;
	
	@Column(name = "CompleteTime")
	private Long completeTime;
	
	@Column(name = "batchNo",length=50)
	private String batchNo;
	
	@Column(name = "WxPrepayXml",length=300)
	private String wxPrepayXml;
	
	@Column(name = "IsComplete")
	private Boolean complete;
	
	@Column(name = "CompletTime")
	private Long completTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getConversationId() {
		return conversationId;
	}

	public void setConversationId(Integer conversationId) {
		this.conversationId = conversationId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getPayTime() {
		return payTime;
	}

	public void setPayTime(Long payTime) {
		this.payTime = payTime;
	}

	public Integer getTalkingNumber() {
		return talkingNumber;
	}

	public void setTalkingNumber(Integer talkingNumber) {
		this.talkingNumber = talkingNumber;
	}

	public String getFlowNumber() {
		return flowNumber;
	}

	public void setFlowNumber(String flowNumber) {
		this.flowNumber = flowNumber;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Long getPresentTime() {
		return presentTime;
	}

	public void setPresentTime(Long presentTime) {
		this.presentTime = presentTime;
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Long getPlaceTime() {
		return placeTime;
	}

	public void setPlaceTime(Long placeTime) {
		this.placeTime = placeTime;
	}

	public Long getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Long completeTime) {
		this.completeTime = completeTime;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getWxPrepayXml() {
		return wxPrepayXml;
	}

	public void setWxPrepayXml(String wxPrepayXml) {
		this.wxPrepayXml = wxPrepayXml;
	}

	public Boolean getComplete() {
		return complete;
	}

	public void setComplete(Boolean complete) {
		this.complete = complete;
	}

	public Long getCompletTime() {
		return completTime;
	}

	public void setCompletTime(Long completTime) {
		this.completTime = completTime;
	}
}
