package com.tspeiz.modules.common.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


/**
 * 门诊病历
 * @author kx
 *
 */
@Entity
@Table(name="medical_record_info")
public class MedicalRecords implements Serializable{
	private static final long serialVersionUID = -2039915282120446838L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="OrderId")
	private Integer orderId;
	
	@Column(name="CaseName",length=32)
	private String caseName;
	
	@Type(type = "text")
	@Column(name="MainSuit")
	private String mainSuit;//主诉
	
	@Type(type = "text")
	@Column(name= "PresentIll")
	private String presentIll;//现病史
	
	@Type(type = "text")
	@Column(name = "HistoryIll")
	private String historyIll;//既往史
	
	@Type(type = "text")
	@Column(name = "Examined")
	private String examined;//体检
	
	@Type(type = "text")
	@Column(name = "AssistantResult")
	private String assistantResult;//辅助检查结果
	
	@Type(type = "text")
	@Column(name = "InitialDiagnosis")
	private String  initialDiagnosis;//初步诊断
	
	@Type(type = "text")
	@Column(name = "TreatAdvice")
	private String  treatAdvice;//治疗意见

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMainSuit() {
		return mainSuit;
	}

	public void setMainSuit(String mainSuit) {
		this.mainSuit = mainSuit;
	}

	public String getPresentIll() {
		return presentIll;
	}

	public void setPresentIll(String presentIll) {
		this.presentIll = presentIll;
	}

	public String getHistoryIll() {
		return historyIll;
	}

	public void setHistoryIll(String historyIll) {
		this.historyIll = historyIll;
	}

	public String getExamined() {
		return examined;
	}

	public void setExamined(String examined) {
		this.examined = examined;
	}

	public String getAssistantResult() {
		return assistantResult;
	}

	public void setAssistantResult(String assistantResult) {
		this.assistantResult = assistantResult;
	}

	public String getInitialDiagnosis() {
		return initialDiagnosis;
	}

	public void setInitialDiagnosis(String initialDiagnosis) {
		this.initialDiagnosis = initialDiagnosis;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getTreatAdvice() {
		return treatAdvice;
	}

	public void setTreatAdvice(String treatAdvice) {
		this.treatAdvice = treatAdvice;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
}
