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

@Entity
@Table(name="platform_health_consultation")
public class PlatformHealthConsultation implements Serializable{
	private static final long serialVersionUID = 2383045766526610541L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "CreateTime")
	private Timestamp createTime;

	@Column(name = "BackImage")
	private String backImage;
	
	@Column(name = "TagType")
	private Integer TagType;

	@Column(name = "Title")
	private String title;

	@Column(name = "Summary")
	private String summary;
	
	@Column(name = "DetailType")
	private Integer detailType;
	
	@Column(name = "Status")
	private Integer status;
	
	@Column(name = "TextLink")
	private String textLink;
	
	@Column(name = "HtmlContent")
	private String htmlContent;
	
	@Column(name = "IssuerId")
	private String issuerId;
	
	@Column(name = "IssuerType")
	private String issuerType;
	
	@Column(name = "ReadNum")
	private String readNum;
	@Column(name = "LikeNum")
	private String likeNum;
	
	@Transient
	private String typeName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getBackImage() {
		return backImage;
	}

	public void setBackImage(String backImage) {
		this.backImage = backImage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTagType() {
		return TagType;
	}

	public void setTagType(Integer tagType) {
		TagType = tagType;
	}

	public Integer getDetailType() {
		return detailType;
	}

	public void setDetailType(Integer detailType) {
		this.detailType = detailType;
	}

	public String getTextLink() {
		return textLink;
	}

	public void setTextLink(String textLink) {
		this.textLink = textLink;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(String issuerId) {
		this.issuerId = issuerId;
	}

	public String getIssuerType() {
		return issuerType;
	}

	public void setIssuerType(String issuerType) {
		this.issuerType = issuerType;
	}

	public String getReadNum() {
		return readNum;
	}

	public void setReadNum(String readNum) {
		this.readNum = readNum;
	}

	public String getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(String likeNum) {
		this.likeNum = likeNum;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
}
