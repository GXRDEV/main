package com.tspeiz.modules.common.bean.dto;

import java.math.BigInteger;

/**
 * 
 * 医生信息辅助类
 * @author kangxin
 *
 */
public class DocAboutDatas {
	
	private Integer doctorId;
	private String docName;
	private String hosName;
	private String depName;
	private String tel;//联系电话
	private BigInteger attentCount;//关注患者量
	private BigInteger reportCount;//报道患者量
	private BigInteger telCount;//图文问诊量
	private BigInteger tuwenCount;//电话问诊量
	private BigInteger dtuwenCount;//图文会诊量
	private BigInteger vedioCount;//视频会诊量
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
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public BigInteger getAttentCount() {
		return attentCount;
	}
	public void setAttentCount(BigInteger attentCount) {
		this.attentCount = attentCount;
	}
	public BigInteger getReportCount() {
		return reportCount;
	}
	public void setReportCount(BigInteger reportCount) {
		this.reportCount = reportCount;
	}
	public BigInteger getTelCount() {
		return telCount;
	}
	public void setTelCount(BigInteger telCount) {
		this.telCount = telCount;
	}
	public BigInteger getTuwenCount() {
		return tuwenCount;
	}
	public void setTuwenCount(BigInteger tuwenCount) {
		this.tuwenCount = tuwenCount;
	}
	public BigInteger getDtuwenCount() {
		return dtuwenCount;
	}
	public void setDtuwenCount(BigInteger dtuwenCount) {
		this.dtuwenCount = dtuwenCount;
	}
	public BigInteger getVedioCount() {
		return vedioCount;
	}
	public void setVedioCount(BigInteger vedioCount) {
		this.vedioCount = vedioCount;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
}
