package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Departments")
public class Departments implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="DisplayName",length=64,nullable=false)
	private String displayName;
	
	@Column(name="ShortName",length=8,nullable=false)
	private String shortName;
	
	@Column(name="Rank")
	private Integer rank;
	
	@Column(name="Icon",length=128)
	private String icon;
	
	@Column(name="Remark",length=128)
	private String remark;
	
	@Column(name="IsHot")
	private Integer hot;
	
	@Column(name="BigDepartmentId",length=11)
	private Integer bigDepartmentId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getHot() {
		return hot;
	}

	public void setHot(Integer hot) {
		this.hot = hot;
	}

	public Integer getBigDepartmentId() {
		return bigDepartmentId;
	}

	public void setBigDepartmentId(Integer bigDepartmentId) {
		this.bigDepartmentId = bigDepartmentId;
	}
}
