package com.tspeiz.modules.common.bean.third;

import java.io.Serializable;

public class ThirdDepartment implements Serializable{
	private String departmentId;
	
	private String departmentName;

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
