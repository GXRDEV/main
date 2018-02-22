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
@Table(name = "expert_consultation_schedule")
public class ExpertConsultationSchedule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7274188988171309751L;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "ExpertId",length=11,nullable = false)
	private Integer expertId;
	
	@Column(name = "ScheduleDate",length=10,nullable = false)
	private String scheduleDate;
	
	@Column(name = "StartTime",length=5,nullable = false)
	private String startTime;
	
	@Column(name = "Duration",length=11,nullable = false)
	private Integer duration;
	
	@Column(name = "Status",length=1,nullable = false)
	private Integer status; //0 关闭，1开启
	
	@Column(name = "Cost", columnDefinition = "double(8,2) default '0.00'")
	private Float cost;
	
	@Column(name="HasAppoint")
	private String hasAppoint="0";//默认0未预约，1已预约
	
	@Transient
	private String weekDesc;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getExpertId() {
		return expertId;
	}

	public void setExpertId(Integer expertId) {
		this.expertId = expertId;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public String getHasAppoint() {
		return hasAppoint;
	}

	public void setHasAppoint(String hasAppoint) {
		this.hasAppoint = hasAppoint;
	}

	public String getWeekDesc() {
		return weekDesc;
	}

	public void setWeekDesc(String weekDesc) {
		this.weekDesc = weekDesc;
	} 

	
}
