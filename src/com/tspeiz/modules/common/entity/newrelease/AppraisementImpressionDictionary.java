package com.tspeiz.modules.common.entity.newrelease;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 评价标签信息字典表
 * 
 * @author liqi
 * 
 */
@Entity
@Table(name = "appraisement_impression_dictionary")
public class AppraisementImpressionDictionary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3663896851491930309L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "Code", length = 11)
	private Integer code;

	@Column(name = "Content", length = 255)
	private String content;

	@Column(name = "Type", length = 1)
	private Integer type;
	
	@Transient
	private BigInteger totalNumber;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigInteger getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(BigInteger totalNumber) {
		this.totalNumber = totalNumber;
	}
}
