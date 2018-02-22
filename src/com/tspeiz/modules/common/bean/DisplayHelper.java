package com.tspeiz.modules.common.bean;

import java.util.List;

import com.tspeiz.modules.common.bean.weixin.ReSourceBean;

public class DisplayHelper {
	private String recordId;
	private String firstUrl;
	
	private List<ReSourceBean> beans;

	public List<ReSourceBean> getBeans() {
		return beans;
	}

	public void setBeans(List<ReSourceBean> beans) {
		this.beans = beans;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getFirstUrl() {
		return firstUrl;
	}

	public void setFirstUrl(String firstUrl) {
		this.firstUrl = firstUrl;
	}
}
