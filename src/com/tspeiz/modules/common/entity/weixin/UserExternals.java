package com.tspeiz.modules.common.entity.weixin;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户关注
 * 
 * @author heyongb
 * 
 */
@Entity
@Table(name = "UserExternals")
public class UserExternals implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "UserId", nullable = false)
	private Integer userId;

	@Column(name = "Type", nullable = false)
	private Integer type;

	@Column(name = "ExternalId", nullable = false, length = 64)
	private String externalId;

	@Column(name = "DisplayName", length = 64)
	private String displayName;

	@Column(name = "EventKey", length = 64)
	private String eventKey;

	@Column(name = "Ticket", length = 64)
	private String ticket;

	@Column(name = "Gender", length = 64)
	private Integer gender;// 性别

	@Column(name = "Province", length = 64)
	private String province;

	@Column(name = "City", length = 64)
	private String city;

	@Column(name = "Country", length = 64)
	private String country;

	@Column(name = "ProfilePictureUrl", length = 64)
	private String profilePictureUrl;

	@Column(name = "UnionId", length = 64)
	private String unionId;

	@Column(name = "QRCodeSceneId", length = 64)
	private Integer qrCodeSceneId;

	@Column(name = "Status")
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}

	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public Integer getQrCodeSceneId() {
		return qrCodeSceneId;
	}

	public void setQrCodeSceneId(Integer qrCodeSceneId) {
		this.qrCodeSceneId = qrCodeSceneId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
