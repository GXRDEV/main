package com.tspeiz.modules.common.entity.release2;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;

@Entity
@Table(name = "user_case_attachment")
public class UserCaseAttachment implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="Uuid")
	private String uuid;

	@Column(name = "CreateTime")
	private Timestamp createTime;
	
	@Column(name="CaseUuid")
	private String caseUuid;
	
	@Column(name = "Type")
	private Integer type;

	@Column(name = "ReportTime")
	private Timestamp reportTime;

	@Column(name = "AttachmentIds")
	private String attachmentIds;

    @Column(name="Remark")
    private String remark;
    
    @Transient
    private List<CustomFileStorage> files;
    
    @Transient
    private String reportTimes;
    
    @Transient
    private Integer filescount;
    
    
    public Integer getFilescount() {
		return filescount;
	}

	public void setFilescount(Integer filescount) {
		this.filescount = filescount;
	}

	public String getReportTimes() {
        return reportTimes;
    }

    public void setReportTimes(String reportTimes) {
        this.reportTimes = reportTimes;
    }

	public List<CustomFileStorage> getFiles() {
		return files;
	}

	public void setFiles(List<CustomFileStorage> files) {
		this.files = files;
	}

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

	public String getCaseUuid() {
		return caseUuid;
	}

	public void setCaseUuid(String caseUuid) {
		this.caseUuid = caseUuid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Timestamp getReportTime() {
		return reportTime;
	}
	public void setReportTime(Timestamp reportTime) {
		this.reportTime = reportTime;
	}

	public String getAttachmentIds() {
		return attachmentIds;
	}

	public void setAttachmentIds(String attachmentIds) {
		this.attachmentIds = attachmentIds;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


    
}
