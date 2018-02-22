package com.tspeiz.modules.common.bean.dcm;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="instance_relate")
public class InstanceRelate implements Serializable{
	private static final long serialVersionUID = -4862587762428615558L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "pk", unique = true, nullable = false)
	private Integer pk;
	
	@Column(name="inst_fk")
	private BigInteger instFk;
	
	@Column(name = "ImageOrientation",length=256)
	private String imageOrientation;
	
	@Column(name = "ImageOrientPatient",length=256)
    private String  imageOrientPatient;
	
	@Column(name = "ImagePositionPatient",length=256)
    private String  imagePositionPatient;
	
	@Column(name = "PixelSpacing",length=128)
    private String pixelSpacing;
	
	@Column(name = "frameOfReferenceUID",length=128)
    private String frameOfReferenceUID;
	
	@Column(name = "SliceLocation",length=128)
    private String sliceLocation;
	
	@Column(name = "RefSOPInsUID",length=128)
    private String refSOPInsUID;
	
	@Column(name = "WindowCenter",length=64)
    private String windowCenter;
	
	@Column(name = "SliceThickness",length=64)
    private String sliceThickness;
	
	@Column(name = "NativeColumns")
    private Integer nativeColumns;
	
	@Column(name = "NativeRows")
    private Integer nativeRows;
	
	@Column(name = "NumberOfFrames",length=32)
    private String numberOfFrames;
	
	@Column(name = "WindowWidth",length=16)
    private String windowWidth;
	
	@Column(name = "ImageType",length=16)
    private String imageType;
	public Integer getPk() {
		return pk;
	}
	public void setPk(Integer pk) {
		this.pk = pk;
	}
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
	public BigInteger getInstFk() {
		return instFk;
	}
	public void setInstFk(BigInteger instFk) {
		this.instFk = instFk;
	}
	
	
}
