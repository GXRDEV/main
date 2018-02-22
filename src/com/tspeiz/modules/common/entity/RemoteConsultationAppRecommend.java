package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 远程会诊申请单推荐专家
 * @author heyongb
 *
 */
@Entity
@Table(name="remote_consultation_app_recommend")
public class RemoteConsultationAppRecommend implements Serializable{
	private static final long serialVersionUID = 757033595310679424L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "UUID")
	private String uuid;//会诊申请单uuid
	
	@Column(name="HospitalId")
	private Integer hospitalId;//专家医院id
	
	@Column(name = "HosName")
	private String hosName;//专家医院名称
	
	@Column(name="DepId")
	private Integer depId;//专家科室id
	
	@Column(name = "DepName")
	private String depName;//专家科室名称
	
	@Column(name = "Duty")
	private String duty;
	
	@Column(name="DoctorId")
	private Integer doctorId;//专家id
	
	@Column(name = "DocName")
	private String docName;//专家名称
	
	@Column(name="HeadImage")
	private String headImage;//专家头像

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

	public Integer getDepId() {
		return depId;
	}

	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	
}
