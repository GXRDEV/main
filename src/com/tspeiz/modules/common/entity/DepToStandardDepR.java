package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DepToStandardDepR")
public class DepToStandardDepR implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3847625554265776880L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "DepartmentId",length=11)
	private Integer departmentId;
	
	@Column(name = "StandardDepId",length=11)
	private Integer standardDepId;
	
	@Column(name = "DepartmengType",length=1)
	private Integer departmengType; //1:专家医院  2:合作医院

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getStandardDepId() {
		return standardDepId;
	}

	public void setStandardDepId(Integer standardDepId) {
		this.standardDepId = standardDepId;
	}

	public Integer getDepartmengType() {
		return departmengType;
	}

	public void setDepartmengType(Integer departmengType) {
		this.departmengType = departmengType;
	}

}
