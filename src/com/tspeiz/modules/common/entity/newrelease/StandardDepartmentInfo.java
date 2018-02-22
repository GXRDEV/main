package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="standard_department_info")
public class StandardDepartmentInfo implements Serializable{
	private static final long serialVersionUID = -7271695137600539226L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="DisplayName",length=64)
	private String displayName;
	
	@Column(name="BigDepId")
	private Integer bigDepId;
	
	@Column(name="Rank")
	private Integer rank;
	
	@Column(name="Remark")
	private String remark;

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

	public Integer getBigDepId() {
		return bigDepId;
	}

	public void setBigDepId(Integer bigDepId) {
		this.bigDepId = bigDepId;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
