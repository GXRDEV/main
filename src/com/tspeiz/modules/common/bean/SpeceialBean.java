package com.tspeiz.modules.common.bean;

import java.math.BigDecimal;

import com.sun.star.bridge.oleautomation.Decimal;

public class SpeceialBean {
	
	//专家姓名
	private String specialName;
	//联系电话
	private String telphone;
	//医院名称
	private String hosName;
	//科室名称
	private String	depName;
	//职位
	private String  duty;
	//职称
	private String specialTitle;
	//图文会诊
	private BigDecimal adviceAmount;
	//视频会诊
	private BigDecimal vedioAmount;
	
	public String getSpecialName() {
		return specialName;
	}
	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
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
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getSpecialTitle() {
		return specialTitle;
	}
	public void setSpecialTitle(String specialTitle) {
		this.specialTitle = specialTitle;
	}
	public BigDecimal getAdviceAmount() {
		return adviceAmount;
	}
	public void setAdviceAmount(BigDecimal adviceAmount) {
		this.adviceAmount = adviceAmount;
	}
	public BigDecimal getVedioAmount() {
		return vedioAmount;
	}
	public void setVedioAmount(BigDecimal vedioAmount) {
		this.vedioAmount = vedioAmount;
	}
	
}
