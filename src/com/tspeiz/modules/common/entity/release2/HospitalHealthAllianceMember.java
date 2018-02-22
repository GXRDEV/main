package com.tspeiz.modules.common.entity.release2;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 医联体成员
 * @author heyongb
 *
 */
@Entity
@Table(name="hospital_health_alliance_member")
public class HospitalHealthAllianceMember implements Serializable{
	private static final long serialVersionUID = -704877257958223971L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "CreateTime")
	private Timestamp createTime;

	@Column(name = "AllianceUuid")
	private String allianceUuid;

	@Column(name = "HospitalId")
	private Integer hospitalId;

	@Column(name = "HospitalLevel")
	private Integer hospitalLevel;

	@Column(name = "ParentHosId")
	private Integer parentHosId;

	@Column(name = "Level")
	private Integer level;

	@Column(name = "Role")
	private Integer role;

	@Column(name = "Status")
	private Integer status=1;
	
	@Transient
	private String hosName;
	
	@Transient
	private String area;
	
	@Transient
	private String levelDesc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getAllianceUuid() {
		return allianceUuid;
	}

	public void setAllianceUuid(String allianceUuid) {
		this.allianceUuid = allianceUuid;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Integer getHospitalLevel() {
		return hospitalLevel;
	}

	public void setHospitalLevel(Integer hospitalLevel) {
		this.hospitalLevel = hospitalLevel;
	}

	public Integer getParentHosId() {
		return parentHosId;
	}

	public void setParentHosId(Integer parentHosId) {
		this.parentHosId = parentHosId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getLevelDesc() {
		return levelDesc;
	}

	public void setLevelDesc(String levelDesc) {
		this.levelDesc = levelDesc;
	}
}
