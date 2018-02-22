package com.tspeiz.modules.common.entity.release2;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 患者就诊记录
 * @author heyongb
 *
 */
@Entity
@Table(name="user_medical_record")
public class UserMedicalRecord implements Serializable{

	private static final long serialVersionUID = 1048274211815536753L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="UUID")
	private String uuid;

	@Column(name="CreateTime")
	private Timestamp createTime;
	
	@Column(name="CaseId")
	private Integer caseId;

	@Column(name="UserId")
	private Integer userId;

	@Column(name="SubUserUuid")
	private String subUserUuid;

	@Column(name="VisitDate")
	private Date visitDate;

	@Column(name="HosId")
	private Integer hosId;

	@Column(name="HosName")
	private String hosName;

	@Column(name="DepId")
	private Integer depId;

	@Column(name="DepName")
	private String depName;

	@Column(name="DoctorId")
	private Integer doctorId;

	@Column(name="DoctorName")
	private String doctorName;

	@Column(name="FirstVisit")
	private Integer firstVisit;//0：初诊，1：复诊

	@Column(name="DelFlag")
	private Integer delFlag;
	
	@Transient
	private String diseaseDes;

	@Transient
	private String diseaseName;

	@Transient
	private String images;


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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSubUserUuid() {
		return subUserUuid;
	}

	public void setSubUserUuid(String subUserUuid) {
		this.subUserUuid = subUserUuid;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public Integer getHosId() {
		return hosId;
	}

	public void setHosId(Integer hosId) {
		this.hosId = hosId;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getFirstVisit() {
		return firstVisit;
	}

	public void setFirstVisit(Integer firstVisit) {
		this.firstVisit = firstVisit;
	}

	public String getDiseaseDes() {
		return diseaseDes;
	}

	public void setDiseaseDes(String diseaseDes) {
		this.diseaseDes = diseaseDes;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
}
