package com.tspeiz.modules.common.bean;

import java.util.List;

public class PacsHelper {
	private String parentPacs;
	private String seriesNumber;
	private String httpUrl;
	private String studyId;
	private String seriesId;
	private String instanceId;
	private List<PacsHelper> subPics;
	public String getParentPacs() {
		return parentPacs;
	}
	public void setParentPacs(String parentPacs) {
		this.parentPacs = parentPacs;
	}
	public String getSeriesNumber() {
		return seriesNumber;
	}
	public void setSeriesNumber(String seriesNumber) {
		this.seriesNumber = seriesNumber;
	}
	public String getHttpUrl() {
		return httpUrl;
	}
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	public List<PacsHelper> getSubPics() {
		return subPics;
	}
	public void setSubPics(List<PacsHelper> subPics) {
		this.subPics = subPics;
	}
	public String getStudyId() {
		return studyId;
	}
	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}
	public String getSeriesId() {
		return seriesId;
	}
	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
}
