package com.tspeiz.modules.common.bean.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DoctorIncomeDto {
	
	private Integer docid;
	private BigDecimal tw;
	private BigDecimal fasts;
	private BigDecimal tel;
	private BigDecimal warm;
	private BigDecimal invite;
	private BigDecimal qiandao;
	private BigDecimal todaymoney;
	private BigDecimal summoney;
	private String docName;
	private String hosName;
	private String depName;
	public Integer getDocid() {
		return docid;
	}
	public void setDocid(Integer docid) {
		this.docid = docid;
	}
	
	public BigDecimal getTw() {
		return tw;
	}
	public void setTw(BigDecimal tw) {
		this.tw = tw;
	}
	public BigDecimal getFasts() {
		return fasts;
	}
	public void setFasts(BigDecimal fasts) {
		this.fasts = fasts;
	}
	public BigDecimal getTel() {
		return tel;
	}
	public void setTel(BigDecimal tel) {
		this.tel = tel;
	}
	public BigDecimal getWarm() {
		return warm;
	}
	public void setWarm(BigDecimal warm) {
		this.warm = warm;
	}
	public BigDecimal getInvite() {
		return invite;
	}
	public void setInvite(BigDecimal invite) {
		this.invite = invite;
	}
	public BigDecimal getTodaymoney() {
		return todaymoney;
	}
	public void setTodaymoney(BigDecimal todaymoney) {
		this.todaymoney = todaymoney;
	}
	public BigDecimal getSummoney() {
		return summoney;
	}
	public void setSummoney(BigDecimal summoney) {
		this.summoney = summoney;
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
	public BigDecimal getQiandao() {
		return qiandao;
	}
	public void setQiandao(BigDecimal qiandao) {
		this.qiandao = qiandao;
	}
	
}
