package com.tspeiz.modules.common.entity.weixin;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Patients")
public class Patients implements Serializable{
	private static final long serialVersionUID = -5085393181666830767L;
	@Id
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "RealName",length=32)
	private String realName;
	
	@Column(name = "Department",length=32)
	private String department;
	
	@Column(name = "JobTitle",length=32)
	private String jobTitle;
	
	@Column(name = "NickName",length=32)
	private String nickName;
	
	@Column(name = "OriginId")
	private Integer originId;
	
	@Column(name = "Tel",length=32)
	private String tel;
	
	@Column(name = "QQ",length=32)
	private String qq;
	
	@Column(name = "Age")
	private Integer age=0;
	
	@Column(name = "Region",length=64)
	private String region;
	
	@Column(name = "Remark",length=1024)
	private String remark;
	
	@Column(name = "Compliance",length=256)
	private String compliance;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getOriginId() {
		return originId;
	}

	public void setOriginId(Integer originId) {
		this.originId = originId;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCompliance() {
		return compliance;
	}

	public void setCompliance(String compliance) {
		this.compliance = compliance;
	}
}
