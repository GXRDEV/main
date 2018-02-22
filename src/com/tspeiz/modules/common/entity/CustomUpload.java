package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 本地上传图片记录
 */
@Entity
@Table(name = "CustomUpload")
public class CustomUpload implements Serializable {
	private static final long serialVersionUID = -7535002472016832375L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "OrderId")
	private Integer orderId;

	@Column(name = "FileName", length = 32)
	private String fileName;

	@Column(name = "UrlPath", length = 128)
	private String urlPath;

	@Column(name = "CreateTime", length = 20)
	private String createTime;
	
	public CustomUpload(){}
	
	public CustomUpload(Integer orderId,String fileName,String urlPath,String createTime){
		this.orderId=orderId;
		this.fileName=fileName;
		this.urlPath=urlPath;
		this.createTime=createTime;
	}

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

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
