package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="custom_file_storage")
public class CustomFileStorage implements Serializable{
	private static final long serialVersionUID = 6628807677929274187L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="OrderId")
	private Integer orderId;
	
	@Column(name="FileName",length=32)
	private String fileName;
	
	@Column(name="FileUrl",length=256)
	private String fileUrl;
	
	@Column(name="FileType",length=20)
	private String fileType;
	
	@Column(name="Status",length=1)
	private Integer status;
	
	@Column(name="CreateTime")
	private Timestamp createTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		//if(turn!=null&&turn>0){
			//fileUrl=fileUrl+"?x-oss-process=image/rotate,90";
		//}
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
