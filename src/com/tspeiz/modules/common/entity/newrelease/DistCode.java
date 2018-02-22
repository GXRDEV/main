package com.tspeiz.modules.common.entity.newrelease;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dict_district_info")
public class DistCode implements Serializable {
	private static final long serialVersionUID = -6232998884119099309L;
	@Id
	@Column(name = "DistCode", unique = true, nullable = false)
	private String distCode;

	@Column(name = "DistName", length = 50)
	private String distName;

	public String getDistCode() {
		return distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	public String getDistName() {
		return distName;
	}

	public void setDistName(String distName) {
		this.distName = distName;
	}
}
