package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;

@Entity
@Table(name="hospital_department_info")
public class HospitalDepartmentInfo implements Serializable{
	private static final long serialVersionUID = 985994956323525224L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "DisplayName",length=64)
	private String displayName;
	
	@Column(name = "LocalDepId")
	private Integer localDepId;
	
	@Column(name = "Keywords",length=10)
	private String keywords;
	
	@Column(name = "Status")
	private Integer status;//做远程门诊状态
	
	@Column(name="AppointStatus")
	private Integer AppointStatus;//微信公号中的预约挂号科室
	
	@Column(name = "HospitalId")
	private Integer hospitalId;
	
	@Type(type="text")
	@Column(name="Introduction")
	private String introduction;
	
	@Type(type="text")
	@Column(name="IntroductionTxt")
	private String introductionTxt;
	
	@Column(name="DepIcon",length=128)
	private String depIcon;//科室图标
	
	@Transient
	private String hosName;

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

	public Integer getLocalDepId() {
		return localDepId;
	}

	public void setLocalDepId(Integer localDepId) {
		this.localDepId = localDepId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Integer getAppointStatus() {
		return AppointStatus;
	}

	public void setAppointStatus(Integer appointStatus) {
		AppointStatus = appointStatus;
	}

	public String getIntroductionTxt() {
		return introductionTxt;
	}

	public void setIntroductionTxt(String introductionTxt) {
		this.introductionTxt = introductionTxt;
	}

	public String getDepIcon() {
		return depIcon;
	}

	public void setDepIcon(String depIcon) {
		this.depIcon = depIcon;
	}
}
