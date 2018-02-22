package com.tspeiz.modules.common.bean.weixin;

public class OrderBean {
	private Integer id;
	private String timeStart;
	private String hosName;
	private String expertName;
	private String userName;
	private String age;
	private String sex;
	private String sick;
	private String status;

	public OrderBean() {
	}

	public OrderBean(Integer id, String timeStart, String hosName,
			String expertName, String userName, String age, String sex,
			String sick, String status) {
		super();
		this.id = id;
		this.timeStart = timeStart;
		this.hosName = hosName;
		this.expertName = expertName;
		this.userName = userName;
		this.age = age;
		this.sex = sex;
		this.sick = sick;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSick() {
		return sick;
	}

	public void setSick(String sick) {
		this.sick = sick;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
