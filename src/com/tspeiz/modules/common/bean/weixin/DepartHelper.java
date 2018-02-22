package com.tspeiz.modules.common.bean.weixin;

import java.util.List;

import com.tspeiz.modules.common.entity.Departments;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;

public class DepartHelper {
	private String name;
	private List<Departments> beans;
	private List<StandardDepartmentInfo> stands;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Departments> getBeans() {
		return beans;
	}
	public void setBeans(List<Departments> beans) {
		this.beans = beans;
	}
	public List<StandardDepartmentInfo> getStands() {
		return stands;
	}
	public void setStands(List<StandardDepartmentInfo> stands) {
		this.stands = stands;
	}
}
