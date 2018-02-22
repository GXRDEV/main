package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CooHospitalDetails")
public class CooHospitalDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "DisplayName",length=50)
	private String displayName;
	
	@Column(name = "HospitalLevel",length=50)
	private String hospitalLevel;
	
	@Column(name = "[Desc]",length=2000)
	private String des;
	
	@Column(name = "Lat")
	private BigDecimal lat;
	
	@Column(name = "lng")
	private BigDecimal lng;
	
	@Column(name = "Picture",length=255)
	private String picture;
	
	@Column(name = "FieldUserId")
	private Integer fieldUserId;
	
	@Column(name = "QRCodeUri",length=255)
	private String qrCodeUri;
	
	@Column(name = "PictureBig",length=255)
	private String pictureBig;
	
	@Column(name = "ShortName",length=50)
	private String shortName;
	
	@Column(name = "AddNumAmount")
	private BigDecimal addNumAmount;
	
	@Column(name = "Introduction",length=2000)
	private String introduction;
	
	@Column(name = "News",length=2000)
	private String news;
	
	@Column(name = "MoreDetails",length=50)
	private String moreDetails;
	
	@Column(name = "Area",length=50)
	private String area;
	
	@Column(name = "Province",length=50)
	private String province;
	
	@Column(name = "City",length=50)
	private String city;
	
	@Column(name = "BodyCode",length=50)
	private String bodyCode;
	
	@Column(name = "GoodDeptment",length=80)
	private String goodDeptment;
	
	@Column(name = "IsHot")
	private Integer hot;
	
	@Column(name = "BannerUrl",length=255)
	private String bannerUrl;
	
	@Column(name = "IndexBannerImg",length=255)
	private String indexBannerImg;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getHospitalLevel() {
		return hospitalLevel;
	}

	public void setHospitalLevel(String hospitalLevel) {
		this.hospitalLevel = hospitalLevel;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getFieldUserId() {
		return fieldUserId;
	}

	public void setFieldUserId(Integer fieldUserId) {
		this.fieldUserId = fieldUserId;
	}

	public String getQrCodeUri() {
		return qrCodeUri;
	}

	public void setQrCodeUri(String qrCodeUri) {
		this.qrCodeUri = qrCodeUri;
	}

	public String getPictureBig() {
		return pictureBig;
	}

	public void setPictureBig(String pictureBig) {
		this.pictureBig = pictureBig;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public BigDecimal getAddNumAmount() {
		return addNumAmount;
	}

	public void setAddNumAmount(BigDecimal addNumAmount) {
		this.addNumAmount = addNumAmount;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
	}

	public String getMoreDetails() {
		return moreDetails;
	}

	public void setMoreDetails(String moreDetails) {
		this.moreDetails = moreDetails;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBodyCode() {
		return bodyCode;
	}

	public void setBodyCode(String bodyCode) {
		this.bodyCode = bodyCode;
	}

	public String getGoodDeptment() {
		return goodDeptment;
	}

	public void setGoodDeptment(String goodDeptment) {
		this.goodDeptment = goodDeptment;
	}

	public Integer getHot() {
		return hot;
	}

	public void setHot(Integer hot) {
		this.hot = hot;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getIndexBannerImg() {
		return indexBannerImg;
	}

	public void setIndexBannerImg(String indexBannerImg) {
		this.indexBannerImg = indexBannerImg;
	}
}
