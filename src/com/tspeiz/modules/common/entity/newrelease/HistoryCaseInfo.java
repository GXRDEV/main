package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="live_order_info")
public class HistoryCaseInfo implements Serializable{
	private static final long serialVersionUID = -9051486784753727503L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="Id",unique = true,nullable = false)
	private Integer id;
	
	@Column(name="UUID")
	private String uuid;
	
	@Column(name="Type")
	private Integer type;
	
	@Column(name="CreateTime")
	private Timestamp createTime;
	
	@Column(name="EndTime")
	private Timestamp endTime;
	
	@Column(name="Title")
	private String title;
	
	@Column(name="MainSuit")
	private String mainSuit;
	
	@Column(name="TreatAdvice")
	private String treatAdvice;
	
	@Column(name="BackImageUrl")
	private String backImageUrl;
	
	@Column(name="CommentNum")
	private Integer commentNum;
	
	@Column(name="Status")
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMainSuit() {
		return mainSuit;
	}

	public void setMainSuit(String mainSuit) {
		this.mainSuit = mainSuit;
	}

	public String getTreatAdvice() {
		return treatAdvice;
	}

	public void setTreatAdvice(String treatAdvice) {
		this.treatAdvice = treatAdvice;
	}

	public String getBackImageUrl() {
		return backImageUrl;
	}

	public void setBackImageUrl(String backImageUrl) {
		this.backImageUrl = backImageUrl;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
