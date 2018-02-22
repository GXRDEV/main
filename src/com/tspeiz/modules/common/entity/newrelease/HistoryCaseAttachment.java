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
@Table(name="live_data_relative")
public class HistoryCaseAttachment implements Serializable{
	private static final long serialVersionUID = -832246521782955155L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="Id",unique = true,nullable = false)
	private Integer id;
	
	@Column(name="UUID")
	private String uuid;
	
	@Column(name="CreateTime")
	private Timestamp createTime;
	
	@Column(name="LiveUuid")
	private String historyCaseUuid;
	
	@Column(name="AttentmentsId")
	private String attentmentsId;
	
	@Column(name="Title")
	private String title;

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

	public String getHistoryCaseUuid() {
		return historyCaseUuid;
	}

	public void setHistoryCaseUuid(String historyCaseUuid) {
		this.historyCaseUuid = historyCaseUuid;
	}

	public String getAttentmentsId() {
		return attentmentsId;
	}

	public void setAttentmentsId(String attentmentsId) {
		this.attentmentsId = attentmentsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
