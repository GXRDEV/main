package com.tspeiz.modules.home;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tspeiz.modules.pacs.bean.InstanceInfo;
import com.tspeiz.modules.pacs.bean.InstanceRelate;
import com.tspeiz.modules.pacs.bean.SeriesInfo;
import com.tspeiz.modules.pacs.bean.StudyInfo;
import com.tspeiz.modules.pacs.common.service.IDcmService;
import com.tspeiz.modules.pacs.util.ImageOrientationUtil;
import com.tspeiz.modules.util.common.StringRetUtil;
@Controller
@RequestMapping("dcm")
public class DcmAdminController {
	private Logger log = Logger.getLogger(DcmAdminController.class);
	@Resource
	private IDcmService dcmService;

	@RequestMapping(value = "/studyinfo", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> studyinfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String patientId = request.getParameter("patientID");
		String studyId =request.getParameter("studyUID");
		StudyInfo sinfo = dcmService.queryStudyinfoByCondition(patientId,
				studyId);
		//sinfo.setPat_Name(sinfo.getPat_Name());
		System.out.println("----time---"+sinfo.getStudyDate());
		map.put("sinfo", sinfo);
		return map;
	}

	// 序列
	@RequestMapping(value = "/seriesinfos", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> seriesInfos(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String patientId = request.getParameter("patientID");
		String studyId =request.getParameter("studyUID");
		List<SeriesInfo> series = dcmService.querySeriesInfosByCondition(
				patientId, studyId);
		map.put("series", series);
		log.info("========series====="+JSONArray.fromObject(series).toString());
		return map;
	}

	// 获取序列中的instances
	@RequestMapping(value = "/gaininstances", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gaininstances(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String patientId = request.getParameter("patientID");
		String studyId =request.getParameter("studyUID");
		String seriesid = request.getParameter("seriesUID");
		List<InstanceInfo> instances = dcmService
				.queryInstanceInfosByCondition(patientId, studyId, seriesid);
		parseinstances(instances, studyId, seriesid);
		map.put("instances", instances);
		log.info("=====instances========"+JSONArray.fromObject(instances).toString());
		return map;
	}

	private void parseinstances(List<InstanceInfo> instances, String studyid,
			String seriesid) throws Exception {
		String ip_port=StringRetUtil.gainPacsIPAndPort();
		for (InstanceInfo info : instances) {
			InstanceRelate ir = dcmService.queryInstanceRelateByInstPk(info
					.getPk());
			if (ir == null) {
				String wadoURL = "http://"+ip_port+"/wado"
						+ "?requestType=WADO&contentType=application/dicom&studyUID="
						+ studyid + "&seriesUID=" + seriesid;
				InputStream is = null;
				DicomInputStream dis = null;
				String objectUID = info.getSopUID();
				String UrlTmp = null;
				try {
					UrlTmp = wadoURL + "&objectUID=" + objectUID
							+ "&transferSyntax=1.2.840.10008.1.2.1";
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
					/*if ((refImageSeq != null) && (refImageSeq.hasItems())) {
						DicomObject dcmObj1 = refImageSeq.getDicomObject();
						refSOPInsUID = dcmObj1.get(528725);
						referSopInsUid = refSOPInsUID != null ? new String(
								refSOPInsUID.getBytes()) : "";
					}*/

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
						imgOrient= ImageOrientationUtil.getOrientation(imgOrient);
	
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
				} catch (Exception e) {
					if(is!=null){
						is.close();
					}
					if(dis!=null){
						dis.close();
					}
					log.error("Error while reading DICOM attributes " + e.getMessage().toString());
					e.printStackTrace();
				}
			}else{
				info.setWindowCenter(ir.getWindowCenter());
				info.setWindowWidth(ir.getWindowWidth());
				info.setNativeRows(ir.getNativeRows());
				info.setNativeColumns(ir.getNativeColumns());
				info.setImageOrientation(ir.getImageOrientation());
				info.setSliceLocation(ir.getSliceLocation());
				info.setSliceThickness(ir.getSliceThickness());
				info.setFrameOfReferenceUID(ir.getFrameOfReferenceUID());
				info.setImagePositionPatient(ir.getImagePositionPatient());
				info.setImageOrientPatient(ir.getImageOrientPatient());
				info.setPixelSpacing(ir.getPixelSpacing());
				info.setRefSOPInsUID(ir.getRefSOPInsUID());
				info.setImageType(ir.getImageType());
				info.setNumberOfFrames(ir.getNumberOfFrames());
			}
		}
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
