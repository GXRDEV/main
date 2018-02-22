package com.tspeiz.modules.common.entity.release2;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="third_order_info")
public class ThirdOrderInfo implements Serializable{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id ;
	
	@Column(name="Uuid")
	private String uuid;
	
	@Column(name="OrderUuid")
	private String orderUuid;//视频会诊订单uuid
	
	@Column(name="PatientName")
	private String patientName;
	
	@Column(name="PatientAge")
	private Integer patientAge;
	
	@Column(name="PatientSex")
	private Integer patientSex;
	
	@Column(name="PatientPhone")
	private String patientPhone;
	
	@Column(name="IdCard")
	private String idCard;
	
	@Column(name="ConsultationTypeId")
	private Integer consultationTypeId;
	
	@Column(name="AppHosName")
	private String appHosName;
	
	@Column(name="AppDepName")
	private String appDepName;
	
	@Column(name="AppDocName")
	private String appDocName;
	
	@Column(name="AppDocTelphone")
	private String appDocTelphone;

	@Column(name="DoctorId")
	private String doctorId;
	
	@Column(name="DoctorName")
	private String doctorName;
	
	@Column(name="DepartmentId")
	private String departmentId;
	
	@Column(name="DepartmentName")
	private String departmentName;
	
	@Column(name="HospitalId")
	private String hospitalId;
	
	@Column(name="HospitalName")
	private String hospitalName;
	
	@Column(name="Emergency")
	private Integer emergency;
	
	@Column(name="ConsultationId")
	private String consultationId;//会诊申请返回的会诊id
	
	@Column(name="Status")
	private String status;//会诊状态
	
	
	@Column(name="CreateTime")
	private Timestamp createTime; //创建时间
	
	@Column(name="UpdateTime")
	private Timestamp updateTime;//更新时间

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

	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public String getConsultationId() {
		return consultationId;
	}

	public void setConsultationId(String consultationId) {
		this.consultationId = consultationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(Integer patientAge) {
		this.patientAge = patientAge;
	}

	public Integer getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(Integer patientSex) {
		this.patientSex = patientSex;
	}

	public Integer getConsultationTypeId() {
		return consultationTypeId;
	}

	public void setConsultationTypeId(Integer consultationTypeId) {
		this.consultationTypeId = consultationTypeId;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Integer getEmergency() {
		return emergency;
	}

	public void setEmergency(Integer emergency) {
		this.emergency = emergency;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPatientPhone() {
		return patientPhone;
	}

	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
	}

	public String getAppHosName() {
		return appHosName;
	}

	public void setAppHosName(String appHosName) {
		this.appHosName = appHosName;
	}

	public String getAppDepName() {
		return appDepName;
	}

	public void setAppDepName(String appDepName) {
		this.appDepName = appDepName;
	}

	public String getAppDocName() {
		return appDocName;
	}

	public void setAppDocName(String appDocName) {
		this.appDocName = appDocName;
	}

	public String getAppDocTelphone() {
		return appDocTelphone;
	}

	public void setAppDocTelphone(String appDocTelphone) {
		this.appDocTelphone = appDocTelphone;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
}
