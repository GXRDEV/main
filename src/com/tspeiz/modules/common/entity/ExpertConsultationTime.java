package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "expert_consultation_time")
public class ExpertConsultationTime implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2933106351855151307L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "ExpertId",length=11)
	private Integer expertId;
	
	@Column(name = "WeekDay",length=11)
	private Integer weekDay;
	
	@Column(name = "StartTime",length=8)
	private String startTime;
	
	@Column(name = "EndTime",length=8)
	private String endTime;
	
	
	@Column(name = "Remark",length=256)
	private String remark;
	
	@Column(name = "Status",length=1)
	private Integer status; //0 关闭，1开启
	
	@Column(name = "Cost", columnDefinition = "double(8,2) default '0.00'")
	private Float cost; //0 关闭，1开启
	
	@Transient
	private String timeSlices; //时间片段
	
	@Transient
	private String flag;//1--可用，0不可用
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(Integer weekDay) {
		this.weekDay = weekDay;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getExpertId() {
		return expertId;
	}

	public void setExpertId(Integer expertId) {
		this.expertId = expertId;
	}

	public String getTimeSlices() {
		return timeSlices;
	}

	public void setTimeSlices(String timeSlices) {
		this.timeSlices = timeSlices;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}
}
