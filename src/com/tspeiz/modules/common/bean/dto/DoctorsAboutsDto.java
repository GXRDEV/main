package com.tspeiz.modules.common.bean.dto;

import java.math.BigInteger;

public class DoctorsAboutsDto {
	private Integer id;
	private String docName;
	private String hosName;
	private String depName;
	private BigInteger counts;
	private BigInteger todayCounts;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getHosName() {
		return hosName;
	}
	public void setHosName(String hosName) {
		this.hosName = hosName;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public BigInteger getCounts() {
		return counts;
	}
	public void setCounts(BigInteger counts) {
		this.counts = counts;
	}
	public BigInteger getTodayCounts() {
		return todayCounts;
	}
	public void setTodayCounts(BigInteger todayCounts) {
		this.todayCounts = todayCounts;
	}
	
	
		
}
