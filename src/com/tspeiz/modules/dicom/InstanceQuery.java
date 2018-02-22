package com.tspeiz.modules.dicom;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;

import com.tspeiz.modules.common.bean.dcm.model.QueryModel;
import com.tspeiz.modules.pacs.bean.InstanceInfo;
import com.tspeiz.modules.pacs.bean.InstanceRelate;
import com.tspeiz.modules.pacs.common.service.IDcmService;
import com.tspeiz.modules.pacs.util.ImageOrientationUtil;
import com.tspeiz.modules.util.SpringUtil;
import com.tspeiz.modules.util.common.StringRetUtil;

public class InstanceQuery extends HttpServlet {
	private static IDcmService dcmService;
	static {
		ApplicationContext ac = SpringUtil.getApplicationContext();
		dcmService = (IDcmService) ac.getBean("dcmService");
	}
	ArrayList<DicomObject> lists;
	QueryModel param;
	String ae;
	String host;
	String port;
	String wado;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String patID = request.getParameter("patientId");
		String studyUID = request.getParameter("studyUID");
		String seriesUID = request.getParameter("seriesUID");
		out.print(getDataInstance(patID, studyUID, seriesUID));
	}

	ArrayList<String> argumentConstInstance(String patId, String studyId,
			String seriesID) {
		ArrayList armnts = new ArrayList();
		armnts.add(this.ae + "@" + this.host + ":" + this.port);
		armnts.add("-I");
		armnts.add("-q00100020=" + patId);
		armnts.add("-q0020000D=" + studyId);
		armnts.add("-q0020000E=" + seriesID);
		return armnts;
	}

	JSONArray getDataInstance(String patId, String studyId, String seriesID) {
		JSONArray list = new JSONArray();
		String ip_port=StringRetUtil.gainPacsIPAndPort();
		try {
			List<InstanceInfo> instances = dcmService
					.queryInstanceInfosByCondition(patId, studyId, seriesID);
			for (int i = 0; i < instances.size(); i++) {
				JSONObject jobject = new JSONObject();
				InstanceInfo info = instances.get(i);
				String UrlTmp = "http://"+ip_port+"/wado?requestType=WADO&contentType=application/dicom&studyUID="
						+ studyId
						+ "&seriesUID="
						+ seriesID
						+ "&objectUID="
						+ info.getSopUID()
						+ "&transferSyntax=1.2.840.10008.1.2.1";
				list.put(getImageInfo(info, UrlTmp, jobject));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	JSONObject getImageInfo(InstanceInfo info, String UrlTmp, JSONObject jobject)throws Exception {
		InputStream is = null;
		DicomInputStream dis = null;
		URL url = new URL(UrlTmp);
		is = url.openStream();
		dis = new DicomInputStream(is);
		DicomObject dcmObj = dis.readDicomObject();
		DicomElement wcDcmElement = dcmObj.get(2625616);
		DicomElement wwDcmElement = dcmObj.get(2625617);
		DicomElement rowDcmElement = dcmObj.get(2621456);
		DicomElement colDcmElement = dcmObj.get(2621457);
		DicomElement imgOrientation = dcmObj.get(2097207);
		DicomElement imgPosition = dcmObj.get(2097202);
		DicomElement sliceThick = dcmObj.get(1572944);
		DicomElement frameOfRefUID = dcmObj.get(2097234);
		DicomElement pixelSpacingEle = dcmObj.get(2621488);
		DicomElement totalFramesEle = dcmObj.get(2621448);
		DicomElement imageType = dcmObj.get(524296);
		String image_type = "";
		if (imageType != null) {
			image_type = new String(imageType.getBytes());
			String[] imageTypes = image_type.split("\\\\");
			if (imageTypes.length >= 3) {
				image_type = imageTypes[2];
			}
		}

		DicomElement refImageSeq = dcmObj.get(528704);
		DicomElement refSOPInsUID = null;
		String referSopInsUid = "";
		if ((refImageSeq != null) && (refImageSeq.hasItems())) {
			DicomObject dcmObj1 = refImageSeq.getDicomObject();
			refSOPInsUID = dcmObj1.get(528725);
			referSopInsUid = refSOPInsUID != null ? new String(
					refSOPInsUID.getBytes()) : "";
		}

		String windowCenter = wcDcmElement != null ? new String(
				wcDcmElement.getBytes()) : "";
		String windowWidth = wwDcmElement != null ? new String(
				wwDcmElement.getBytes()) : "";
		int nativeRows = rowDcmElement != null ? rowDcmElement
				.getInt(false) : 0;
		int nativeColumns = colDcmElement != null ? colDcmElement
				.getInt(false) : 0;
		String imgOrient = imgOrientation != null ? new String(
				imgOrientation.getBytes()) : "";
		String sliceThickness = sliceThick != null ? new String(
				sliceThick.getBytes()) : "";
		String forUID = frameOfRefUID != null ? new String(
				frameOfRefUID.getBytes()) : "";
		if (imgOrient.length() > 0)
			imgOrient = ImageOrientationUtil.getOrientation(imgOrient);

		String sliceLoc = "";
		String imagePosition = "";
		if (imgPosition != null) {
			imagePosition = new String(imgPosition.getBytes());
			sliceLoc = imagePosition.substring(imagePosition
					.lastIndexOf("\\") + 1);
		}

		String pixelSpacing = pixelSpacingEle != null ? new String(
				pixelSpacingEle.getBytes()) : "";
		String totalFrames = totalFramesEle != null ? new String(
				totalFramesEle.getBytes()) : "";
		jobject.put("windowCenter",
				windowCenter.replaceAll("\\\\", "|"));
		jobject.put("windowWidth", windowWidth.replaceAll("\\\\", "|"));
		jobject.put("nativeRows", nativeRows);
		jobject.put("nativeColumns", nativeColumns);
		jobject.put("numberofframes", totalFrames);
		jobject.put("sliceLocation", sliceLoc);
		jobject.put("sliceThickness", sliceThickness);
		jobject.put("frameOfReferenceUID", forUID.replaceAll("", ""));
		jobject.put("imagePositionPatient", imagePosition);
		jobject.put("imageOrientPatient", imgOrient);
		jobject.put("pixelSpacing", pixelSpacing);
		jobject.put("refSOPInsUID", referSopInsUid.replaceAll("", ""));
		jobject.put("imageType", image_type);
		/*info.setWindowCenter(windowCenter.replaceAll("\\\\", "|"));
		info.setWindowWidth(windowWidth.replaceAll("\\\\", "|"));
		info.setNativeRows(nativeRows);
		info.setNativeColumns(nativeColumns);
		info.setImageOrientation(imgOrient);
		info.setSliceLocation(sliceLoc);
		info.setSliceThickness(sliceThickness);
		info.setFrameOfReferenceUID(forUID.replaceAll("", ""));
		info.setImagePositionPatient(imagePosition);
		info.setImageOrientPatient(imgOrient);
		info.setPixelSpacing(pixelSpacing);
		info.setRefSOPInsUID(referSopInsUid.replaceAll("", ""));
		info.setImageType(image_type);
		info.setNumberOfFrames(totalFrames);
		saveInstanceRelate(info);*/
		dis.close();
		is.close();
		/*try {
			InstanceRelate ir = dcmService.queryInstanceRelateByInstPk(info
					.getPk());
			if (ir == null) {
				InputStream is = null;
				DicomInputStream dis = null;
				URL url = new URL(UrlTmp);
				is = url.openStream();
				dis = new DicomInputStream(is);
				DicomObject dcmObj = dis.readDicomObject();
				DicomElement wcDcmElement = dcmObj.get(2625616);
				DicomElement wwDcmElement = dcmObj.get(2625617);
				DicomElement rowDcmElement = dcmObj.get(2621456);
				DicomElement colDcmElement = dcmObj.get(2621457);
				DicomElement imgOrientation = dcmObj.get(2097207);
				DicomElement imgPosition = dcmObj.get(2097202);
				DicomElement sliceThick = dcmObj.get(1572944);
				DicomElement frameOfRefUID = dcmObj.get(2097234);
				DicomElement pixelSpacingEle = dcmObj.get(2621488);
				DicomElement totalFramesEle = dcmObj.get(2621448);
				DicomElement imageType = dcmObj.get(524296);
				String image_type = "";
				if (imageType != null) {
					image_type = new String(imageType.getBytes());
					String[] imageTypes = image_type.split("\\\\");
					if (imageTypes.length >= 3) {
						image_type = imageTypes[2];
					}
				}

				DicomElement refImageSeq = dcmObj.get(528704);
				DicomElement refSOPInsUID = null;
				String referSopInsUid = "";
				if ((refImageSeq != null) && (refImageSeq.hasItems())) {
					DicomObject dcmObj1 = refImageSeq.getDicomObject();
					refSOPInsUID = dcmObj1.get(528725);
					referSopInsUid = refSOPInsUID != null ? new String(
							refSOPInsUID.getBytes()) : "";
				}

				String windowCenter = wcDcmElement != null ? new String(
						wcDcmElement.getBytes()) : "";
				String windowWidth = wwDcmElement != null ? new String(
						wwDcmElement.getBytes()) : "";
				int nativeRows = rowDcmElement != null ? rowDcmElement
						.getInt(false) : 0;
				int nativeColumns = colDcmElement != null ? colDcmElement
						.getInt(false) : 0;
				String imgOrient = imgOrientation != null ? new String(
						imgOrientation.getBytes()) : "";
				String sliceThickness = sliceThick != null ? new String(
						sliceThick.getBytes()) : "";
				String forUID = frameOfRefUID != null ? new String(
						frameOfRefUID.getBytes()) : "";
				if (imgOrient.length() > 0)
					imgOrient = ImageOrientationUtil.getOrientation(imgOrient);

				String sliceLoc = "";
				String imagePosition = "";
				if (imgPosition != null) {
					imagePosition = new String(imgPosition.getBytes());
					sliceLoc = imagePosition.substring(imagePosition
							.lastIndexOf("\\") + 1);
				}

				String pixelSpacing = pixelSpacingEle != null ? new String(
						pixelSpacingEle.getBytes()) : "";
				String totalFrames = totalFramesEle != null ? new String(
						totalFramesEle.getBytes()) : "";
				jobject.put("windowCenter",
						windowCenter.replaceAll("\\\\", "|"));
				jobject.put("windowWidth", windowWidth.replaceAll("\\\\", "|"));
				jobject.put("nativeRows", nativeRows);
				jobject.put("nativeColumns", nativeColumns);
				jobject.put("numberofframes", totalFrames);
				jobject.put("sliceLocation", sliceLoc);
				jobject.put("sliceThickness", sliceThickness);
				jobject.put("frameOfReferenceUID", forUID.replaceAll("", ""));
				jobject.put("imagePositionPatient", imagePosition);
				jobject.put("imageOrientPatient", imgOrient);
				jobject.put("pixelSpacing", pixelSpacing);
				jobject.put("refSOPInsUID", referSopInsUid.replaceAll("", ""));
				jobject.put("imageType", image_type);
				info.setWindowCenter(windowCenter.replaceAll("\\\\", "|"));
				info.setWindowWidth(windowWidth.replaceAll("\\\\", "|"));
				info.setNativeRows(nativeRows);
				info.setNativeColumns(nativeColumns);
				info.setImageOrientation(imgOrient);
				info.setSliceLocation(sliceLoc);
				info.setSliceThickness(sliceThickness);
				info.setFrameOfReferenceUID(forUID.replaceAll("", ""));
				info.setImagePositionPatient(imagePosition);
				info.setImageOrientPatient(imgOrient);
				info.setPixelSpacing(pixelSpacing);
				info.setRefSOPInsUID(referSopInsUid.replaceAll("", ""));
				info.setImageType(image_type);
				info.setNumberOfFrames(totalFrames);
				saveInstanceRelate(info);
				dis.close();
				is.close();
			}else{
				jobject.put("windowCenter",ir.getWindowCenter());
				jobject.put("windowWidth", ir.getWindowWidth());
				jobject.put("nativeRows", ir.getNativeRows());
				jobject.put("nativeColumns", ir.getNativeColumns());
				jobject.put("numberofframes", ir.getNumberOfFrames());
				jobject.put("sliceLocation", ir.getSliceLocation());
				jobject.put("sliceThickness", ir.getSliceThickness());
				jobject.put("frameOfReferenceUID", ir.getFrameOfReferenceUID());
				jobject.put("imagePositionPatient", ir.getImagePositionPatient());
				jobject.put("imageOrientPatient", ir.getImageOrientation());
				jobject.put("pixelSpacing", ir.getPixelSpacing());
				jobject.put("refSOPInsUID", ir.getRefSOPInsUID());
				jobject.put("imageType",ir.getImageType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return jobject;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	public String getServletInfo() {
		return "Short description";
	}
	
	private void saveInstanceRelate(InstanceInfo info){
		InstanceRelate relate=new InstanceRelate();
		relate.setInstFk(info.getPk());
		relate.setFrameOfReferenceUID(info.getFrameOfReferenceUID());
		relate.setImageOrientation(info.getImageOrientation());
		relate.setImageOrientPatient(info.getImageOrientPatient());
		relate.setImagePositionPatient(info.getImagePositionPatient());
		relate.setImageType(info.getImageType());
		relate.setNativeColumns(info.getNativeColumns());
		relate.setNativeRows(info.getNativeRows());
		relate.setNumberOfFrames(info.getNumberOfFrames());
		relate.setPixelSpacing(info.getPixelSpacing());
		relate.setRefSOPInsUID(info.getRefSOPInsUID());
		relate.setSliceLocation(info.getSliceLocation());
		relate.setSliceThickness(info.getSliceThickness());
		relate.setWindowCenter(info.getWindowCenter());
		relate.setWindowWidth(info.getWindowWidth());
		dcmService.saveInstanceRelate(relate);
	}
}