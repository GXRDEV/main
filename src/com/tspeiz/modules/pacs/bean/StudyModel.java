package com.tspeiz.modules.pacs.bean;

public class StudyModel {
	private String patientID;
	private String patientName;
	private String patientGender;
	private String patientBirthDate;
	private String physicianName;
	private String studyDate;
	private String studyTime;
	private String studyDescription;
	private String modalitiesInStudy;
	private String studyRelatedInstances;
	private String accessionNumber;
	private String studyInstanceUID;
	private String studyRelatedSeries;

	public StudyModel() {
	}

	public String getAccessionNumber() {
		return this.accessionNumber;
	}

	public String getModalitiesInStudy() {
		return this.modalitiesInStudy;
	}

	public String getPatientBirthDate() {
		return this.patientBirthDate;
	}

	public String getPatientGender() {
		return this.patientGender;
	}

	public String getPatientID() {
		return this.patientID;
	}

	public String getPatientName() {
		return this.patientName;
	}

	public String getPhysicianName() {
		return this.physicianName;
	}

	public String getStudyDate() {
		return this.studyDate;
	}

	public String getStudyDescription() {
		return this.studyDescription;
	}

	public String getStudyInstanceUID() {
		return this.studyInstanceUID;
	}

	public String getStudyRelatedInstances() {
		return this.studyRelatedInstances;
	}

	public String getStudyRelatedSeries() {
		return this.studyRelatedSeries;
	}

	public String getStudyTime() {
		return this.studyTime;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		StudyModel other = (StudyModel) obj;

		return this.studyInstanceUID == null ? other.studyInstanceUID == null
				: this.studyInstanceUID.equals(other.studyInstanceUID);
	}
}