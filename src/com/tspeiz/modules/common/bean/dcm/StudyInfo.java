package com.tspeiz.modules.common.bean.dcm;

import java.sql.Timestamp;

public class StudyInfo {
	private String accNumber;//--ok
	
	private String modality;//--ok
	
	private String refPhysician;//
	
	private Integer totalSeries;//ok
	
	private String pat_gender;//ok
	
	private String pat_Birthdate;//ok
	
	private String bgColor;
	
	private String pat_Name;//ok
	
	private String studyDesc;//ok
	
	private Timestamp studyDate;//ok
	
	private String pat_ID;//ok
	
	private Integer totalIns;
	
	private String studyUID;

	public String getAccNumber() {
		return accNumber;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public String getRefPhysician() {
		return refPhysician;
	}

	public void setRefPhysician(String refPhysician) {
		this.refPhysician = refPhysician;
	}

	public Integer getTotalSeries() {
		return totalSeries;
	}

	public void setTotalSeries(Integer totalSeries) {
		this.totalSeries = totalSeries;
	}

	public String getPat_gender() {
		return pat_gender;
	}

	public void setPat_gender(String pat_gender) {
		this.pat_gender = pat_gender;
	}

	public String getPat_Birthdate() {
		return pat_Birthdate;
	}

	public void setPat_Birthdate(String pat_Birthdate) {
		this.pat_Birthdate = pat_Birthdate;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getPat_Name() {
		return pat_Name;
	}

	public void setPat_Name(String pat_Name) {
		this.pat_Name = pat_Name;
	}

	public String getStudyDesc() {
		return studyDesc;
	}

	public void setStudyDesc(String studyDesc) {
		this.studyDesc = studyDesc;
	}

	public Timestamp getStudyDate() {
		return studyDate;
	}

	public void setStudyDate(Timestamp studyDate) {
		this.studyDate = studyDate;
	}

	public String getPat_ID() {
		return pat_ID;
	}

	public void setPat_ID(String pat_ID) {
		this.pat_ID = pat_ID;
	}

	public Integer getTotalIns() {
		return totalIns;
	}

	public void setTotalIns(Integer totalIns) {
		this.totalIns = totalIns;
	}

	public String getStudyUID() {
		return studyUID;
	}

	public void setStudyUID(String studyUID) {
		this.studyUID = studyUID;
	}
}
