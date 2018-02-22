package com.tspeiz.modules.common.bean.dto;
/*
 * 医生统计输出类
 * 
 */

import java.math.BigInteger;

public class DoctorAboutCount {
	private String docName;
	private String hosName;
	private String depName;
	private BigInteger counts;//报道总人数
	private BigInteger refuseCounts;//拒绝报道人数
	private BigInteger receiveCounts;//接受报道人数
	private BigInteger waitCount;//待审核
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
	public BigInteger getRefuseCounts() {
		return refuseCounts;
	}
	public void setRefuseCounts(BigInteger refuseCounts) {
		this.refuseCounts = refuseCounts;
	}
	public BigInteger getReceiveCounts() {
		return receiveCounts;
	}
	public void setReceiveCounts(BigInteger receiveCounts) {
		this.receiveCounts = receiveCounts;
	}
	public BigInteger getWaitCount() {
		return waitCount;
	}
	public void setWaitCount(BigInteger waitCount) {
		this.waitCount = waitCount;
	}
	
}
