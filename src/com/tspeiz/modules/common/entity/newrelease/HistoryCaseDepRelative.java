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
@Table(name="live_departments_relative")
public class HistoryCaseDepRelative implements Serializable{
	private static final long serialVersionUID = 5945106757832846964L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="Id",unique = true,nullable = false)
	private Integer id;
	
	@Column(name="LiveUuid")
	private String historyCaseUuid;
	
	@Column(name="StandardDepId")
	private Integer standardDepId;
	
	@Column(name="CreateTime")
	private Timestamp createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHistoryCaseUuid() {
		return historyCaseUuid;
	}

	public void setHistoryCaseUuid(String historyCaseUuid) {
		this.historyCaseUuid = historyCaseUuid;
	}

	public Integer getStandardDepId() {
		return standardDepId;
	}

	public void setStandardDepId(Integer standardDepId) {
		this.standardDepId = standardDepId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
