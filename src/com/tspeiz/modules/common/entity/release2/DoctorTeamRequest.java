package com.tspeiz.modules.common.entity.release2;

import java.io.Serializable;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 医生团队请求
 *
 */
@Entity
@Table(name="doctor_team_request")
public class DoctorTeamRequest implements Serializable{
	private static final long serialVersionUID = 8026738275275678190L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "UUID")
	private String uuid;

	@Column(name = "TeamUuid")
	private String teamUuid;

	@Column(name = "DoctorId")
	private Integer doctorId;
	
	@Column(name = "DoctorType")
	private Integer doctorType;

	@Column(name = "Role")
	private Integer role;

	@Column(name = "CreateTime")
	private Timestamp createTime;

	@Column(name = "Message")
	private String message;

	@Column(name = "RequestType")
	private Integer requestType;

	@Column(name = "InviterId")
	private Integer inviterId;

	@Column(name = "InviterType")
	private Integer inviterType;

	@Column(name = "AuditTime")
	private Timestamp auditTime;

	@Column(name = "Status")
	private Integer status;
	
	@Column(name="AuditorId")
	private Integer auditorId;
	
	@Column(name="AuditorType")
	private Integer auditorType;

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

	public String getTeamUuid() {
		return teamUuid;
	}

	public void setTeamUuid(String teamUuid) {
		this.teamUuid = teamUuid;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getDoctorType() {
		return doctorType;
	}

	public void setDoctorType(Integer doctorType) {
		this.doctorType = doctorType;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public Integer getInviterId() {
		return inviterId;
	}

	public void setInviterId(Integer inviterId) {
		this.inviterId = inviterId;
	}

	public Integer getInviterType() {
		return inviterType;
	}

	public void setInviterType(Integer inviterType) {
		this.inviterType = inviterType;
	}

	public Timestamp getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

	public Integer getAuditorType() {
		return auditorType;
	}

	public void setAuditorType(Integer auditorType) {
		this.auditorType = auditorType;
	}
	
}
