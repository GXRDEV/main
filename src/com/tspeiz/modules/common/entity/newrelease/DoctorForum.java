package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * 名医对对碰
 * @author heyongb
 *
 */
@Entity
@Table(name="doctor_forum")
public class DoctorForum implements Serializable{
	private static final long serialVersionUID = -8281661232862011705L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="Title",length=64)
	private String title;//标题
	
	@Column(name="DepName",length=32)
	private String depName;//科室名称
	
	@Column(name="Duty",length=16)
	private String duty;//职务--主任
	
	@Column(name="DocName",length=16)
	private String docName;//医生姓名
	
	@Column(name="BackImag",length=128)
	private String backImag;
	
	@Column(name="AccessUrl",length=128)
	private String accessUrl;//连接地址
	
	@Column(name="PeriodNum")
	private Integer periodNum;
	
	@Column(name="CreateTime")
	private Timestamp createTime;
	
	@Column(name="WeiBoUrl",length=1024)
	private String weiBoUrl;//微博嵌套地址
	
	@Type(type="text")
	@Column(name="GuestIntro")
	private String guestIntro;
	
	@Column(name="CurrWatch")
	private String currWatch;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getPeriodNum() {
		return periodNum;
	}

	public void setPeriodNum(Integer periodNum) {
		this.periodNum = periodNum;
	}

	public String getBackImag() {
		return backImag;
	}

	public void setBackImag(String backImag) {
		this.backImag = backImag;
	}

	public String getWeiBoUrl() {
		return weiBoUrl;
	}

	public void setWeiBoUrl(String weiBoUrl) {
		this.weiBoUrl = weiBoUrl;
	}

	public String getGuestIntro() {
		return guestIntro;
	}

	public void setGuestIntro(String guestIntro) {
		this.guestIntro = guestIntro;
	}

	public String getCurrWatch() {
		return currWatch;
	}

	public void setCurrWatch(String currWatch) {
		this.currWatch = currWatch;
	}
}
