package com.tspeiz.modules.common.entity.newrelease;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_detail_info")
public class UserDetailInfo implements Serializable{
	private static final long serialVersionUID = -7195786174462515370L;
	@Id
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="DisplayName")
	private String displayName;
	
	@Column(name="Sex")
	private Integer sex;
	
	@Column(name="BirthDay")
	private String birthDay;
	
	@Column(name="DistCode")
	private String distCode;
	
	@Column(name="Blood")
	private String blood;
	
	@Column(name="HeadImageUrl")
	private String headImageUrl;
	
	@Column(name="Telephone")
	private String telephone;
	
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

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getDistCode() {
		return distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	public String getBlood() {
		return blood;
	}

	public void setBlood(String blood) {
		this.blood = blood;
	}

	public String getHeadImageUrl() {
		return headImageUrl;
	}

	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}	
}
