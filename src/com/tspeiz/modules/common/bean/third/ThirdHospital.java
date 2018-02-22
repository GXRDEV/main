package com.tspeiz.modules.common.bean.third;

import java.io.Serializable;

public class ThirdHospital implements Serializable{
	private String hospitalName;
	
	private String hospitalId;

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}
}
