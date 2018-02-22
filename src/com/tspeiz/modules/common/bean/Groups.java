package com.tspeiz.modules.common.bean;

import java.util.List;

import com.tspeiz.modules.common.bean.weixin.ReSourceBean;

public class Groups {
	private String key;
	private List<ReSourceBean> beans;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<ReSourceBean> getBeans() {
		return beans;
	}
	public void setBeans(List<ReSourceBean> beans) {
		this.beans = beans;
	}
}
