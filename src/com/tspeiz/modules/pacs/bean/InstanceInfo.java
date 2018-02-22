package com.tspeiz.modules.pacs.bean;

import java.math.BigInteger;

public class InstanceInfo {
	private BigInteger pk;
	private String imageOrientation;
    private String  imageOrientPatient;
    private String SopClassUID;
    private String InstanceNo;
    private String  imagePositionPatient;
    private String pixelSpacing;
    private String frameOfReferenceUID;
    private String sliceLocation;
    private String refSOPInsUID;
    private String SopUID;
    private String windowCenter;
    private String sliceThickness;
    private Integer nativeColumns;
    private Integer nativeRows;
    private String numberOfFrames;
    private String windowWidth;
    private String imageType;
	public String getImageOrientation() {
		return imageOrientation;
	}
	public void setImageOrientation(String imageOrientation) {
		this.imageOrientation = imageOrientation;
	}
	public String getImageOrientPatient() {
		return imageOrientPatient;
	}
	public void setImageOrientPatient(String imageOrientPatient) {
		this.imageOrientPatient = imageOrientPatient;
	}
	public String getSopClassUID() {
		return SopClassUID;
	}
	public void setSopClassUID(String sopClassUID) {
		SopClassUID = sopClassUID;
	}
	public String getInstanceNo() {
		return InstanceNo;
	}
	public void setInstanceNo(String instanceNo) {
		InstanceNo = instanceNo;
	}
	public String getImagePositionPatient() {
		return imagePositionPatient;
	}
	public void setImagePositionPatient(String imagePositionPatient) {
		this.imagePositionPatient = imagePositionPatient;
	}
	public String getPixelSpacing() {
		return pixelSpacing;
	}
	public void setPixelSpacing(String pixelSpacing) {
		this.pixelSpacing = pixelSpacing;
	}
	public String getFrameOfReferenceUID() {
		return frameOfReferenceUID;
	}
	public void setFrameOfReferenceUID(String frameOfReferenceUID) {
		this.frameOfReferenceUID = frameOfReferenceUID;
	}
	public String getSliceLocation() {
		return sliceLocation;
	}
	public void setSliceLocation(String sliceLocation) {
		this.sliceLocation = sliceLocation;
	}
	public String getRefSOPInsUID() {
		return refSOPInsUID;
	}
	public void setRefSOPInsUID(String refSOPInsUID) {
		this.refSOPInsUID = refSOPInsUID;
	}
	public String getSopUID() {
		return SopUID;
	}
	public void setSopUID(String sopUID) {
		SopUID = sopUID;
	}
	public String getWindowCenter() {
		return windowCenter;
	}
	public void setWindowCenter(String windowCenter) {
		this.windowCenter = windowCenter;
	}
	public String getSliceThickness() {
		return sliceThickness;
	}
	public void setSliceThickness(String sliceThickness) {
		this.sliceThickness = sliceThickness;
	}
	public Integer getNativeColumns() {
		return nativeColumns;
	}
	public void setNativeColumns(Integer nativeColumns) {
		this.nativeColumns = nativeColumns;
	}
	public Integer getNativeRows() {
		return nativeRows;
	}
	public void setNativeRows(Integer nativeRows) {
		this.nativeRows = nativeRows;
	}
	public String getNumberOfFrames() {
		return numberOfFrames;
	}
	public void setNumberOfFrames(String numberOfFrames) {
		this.numberOfFrames = numberOfFrames;
	}
	public String getWindowWidth() {
		return windowWidth;
	}
	public void setWindowWidth(String windowWidth) {
		this.windowWidth = windowWidth;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public BigInteger getPk() {
		return pk;
	}
	public void setPk(BigInteger pk) {
		this.pk = pk;
	}
}
