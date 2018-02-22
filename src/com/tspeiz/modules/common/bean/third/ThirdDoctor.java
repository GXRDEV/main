package com.tspeiz.modules.common.bean.third;

import java.io.Serializable;

public class ThirdDoctor implements Serializable{
	
	private String doctorId;
	
	private String doctorName;

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
}
