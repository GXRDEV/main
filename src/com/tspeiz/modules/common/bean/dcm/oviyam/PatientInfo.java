package com.tspeiz.modules.common.bean.dcm.oviyam;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class PatientInfo {
	private static Logger log = Logger.getLogger(PatientInfo.class);

	public ArrayList<StudyModel> studyList = new ArrayList();

	public PatientInfo() {
		this.studyList.clear();
	}

	public ArrayList<StudyModel> getStudyList() {
		return this.studyList;
	}
}
