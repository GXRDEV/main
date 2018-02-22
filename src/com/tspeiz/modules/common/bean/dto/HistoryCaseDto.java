package com.tspeiz.modules.common.bean.dto;

public class HistoryCaseDto {
	private String hisCaseUuid;
	private String title;
	private String createTime;
	private String depName;
	
	public String getHisCaseUuid() {
		return hisCaseUuid;
	}
	public void setHisCaseUuid(String hisCaseUuid) {
		this.hisCaseUuid = hisCaseUuid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	
}
