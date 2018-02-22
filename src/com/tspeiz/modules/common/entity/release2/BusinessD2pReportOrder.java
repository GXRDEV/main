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
 * 患者报道
 * @author heyongb
 *
 */
@Entity
@Table(name="business_d2p_report_order")
public class BusinessD2pReportOrder implements Serializable{
	private static final long serialVersionUID = 5400808715844001718L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="UUID")
	private String uuid;

	@Column(name="CaseId")
	private Integer caseId;//病例id

	@Column(name="DoctorId")
	private Integer doctorId;//医生id

	@Column(name="UserId")
	private Integer userId;//用户id
	
	@Column(name="SubUserUuid")
	private String subUserUuid;//家庭成员uuid

	@Column(name="CreateTime")
	private Timestamp createTime;//下单时间

	@Column(name="Source")
	private Integer source;//来源

	@Column(name="Status")
	private Integer status;//订单状态
	
	@Column(name="DelFlag")
	private Integer delFlag;//删除标志
	
	@Column(name="Remark")
	private String remark;//备注

	@Column(name="ReceiveTime")
	private Timestamp receiveTime;//同意时间
	
	@Column(name="OpenId")
	private String openId;
	
	@Column(name="IscheckALl")
	private Integer isCheckAll;
	
	@Transient
	private String headImage;
	
	@Transient
	private String docName;
	
	@Transient
	private String duty;
	
	@Transient
	private String hosName;
	
	@Transient
	private String depName;

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

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public Integer getIsCheckAll() {
		return isCheckAll;
	}

	public void setIsCheckAll(Integer isCheckAll) {
		this.isCheckAll = isCheckAll;
	}
}
