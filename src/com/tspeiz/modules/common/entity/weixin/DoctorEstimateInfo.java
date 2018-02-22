package com.tspeiz.modules.common.entity.weixin;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

/**
 * 评价
 * @author kx
 *
 */
@Entity
@Table(name="doctor_estimate_info")
public class DoctorEstimateInfo implements Serializable{
	private static final long serialVersionUID = -7827858226770848658L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="DoctorId")
	private Integer doctorId;//专家Id
	
	@Column(name="OrderId")
	private Integer orderId;//订单id
	
	@Column(name="AttendanceLevel")
	private Integer attendanceLevel;//陪护人员的评价等级
	
	@Column(name="DoctorLevel")
	private Integer doctorLevel;//专家评价等级
	
	@Type(type = "text")
	@Column(name="DoctorDesc")
	private String doctorDesc;//对专家的评价
	
	@Column(name="CreateTime",length=20)
	private String createTime;//评价时间
	
	@Column(name="Source")
	private Integer source;//1图文，2电话，3.远程

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getAttendanceLevel() {
		return attendanceLevel;
	}

	public void setAttendanceLevel(Integer attendanceLevel) {
		this.attendanceLevel = attendanceLevel;
	}

	public Integer getDoctorLevel() {
		return doctorLevel;
	}

	public void setDoctorLevel(Integer doctorLevel) {
		this.doctorLevel = doctorLevel;
	}

	public String getDoctorDesc() {
		return doctorDesc;
	}

	public void setDoctorDesc(String doctorDesc) {
		this.doctorDesc = doctorDesc;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}
}
