package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 医生评价中的标签信息
 * 
 * @author liqi
 * 
 */
@Entity
@Table(name = "appraisement_impression_doctor")
public class AppraisementImpressionDoctor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 42369673697678722L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "AppraisementId")
	private Integer appraisementId;

	@Column(name = "ImpressionCode")
	private Integer impressionCode;

	@Column(name = "DoctorId")
	private Integer doctorId;
	
	@Transient
	private String tagName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAppraisementId() {
		return appraisementId;
	}

	public void setAppraisementId(Integer appraisementId) {
		this.appraisementId = appraisementId;
	}

	public Integer getImpressionCode() {
		return impressionCode;
	}

	public void setImpressionId(Integer impressionCode) {
		this.impressionCode = impressionCode;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public void setImpressionCode(Integer impressionCode) {
		this.impressionCode = impressionCode;
	}

}
