package com.tspeiz.modules.dicom;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dcm4che2.data.DicomObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;

import com.tspeiz.modules.common.bean.dcm.model.QueryModel;
import com.tspeiz.modules.pacs.bean.InstanceInfo;
import com.tspeiz.modules.pacs.bean.SeriesInfo;
import com.tspeiz.modules.pacs.common.service.IDcmService;
import com.tspeiz.modules.util.SpringUtil;

public class SeriesQuery extends HttpServlet {
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
	String objid;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			this.param = new QueryModel();
			this.param.setPatientId(request.getParameter("PatientID"));
			this.param.setStudyId(request.getParameter("StudyID"));

			out.print(getDataSeries(this.param));
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private JSONArray getDataSeries(QueryModel param) {
		List<SeriesInfo> seriesList = null;
		JSONArray list = new JSONArray();
		seriesList = dcmService.querySeriesInfosByCondition(
				param.getPatientId(), param.getstudyId());
		for (int i = 0; i < seriesList.size(); i++) {
			SeriesInfo serInfo = seriesList.get(i);
			JSONObject Objectjson = new JSONObject();
			Objectjson.put("seriesUID", serInfo.getSeriesUID());
			Objectjson.put("seriesNumber", serInfo.getSeriesNumber());
			Objectjson.put("modality", serInfo.getModality());
			Objectjson.put("seriesDesc", serInfo.getSeriesDesc());
			Objectjson.put("bodyPart", serInfo.getBodyPart());
			Objectjson.put("totalInstances", serInfo.getTotalInstances());
			Objectjson.put("patientId", param.getPatientId());
			Objectjson.put("studyUID", param.getstudyId());
			Objectjson.put(
					"url",
					getDataInstance(param.getPatientId(), param.getstudyId(),
							serInfo.getSeriesUID()));
			list.put(Objectjson);
		}
		return list;
	}

	JSONArray getDataInstance(String patId, String studyId, String seriesID) {
		JSONArray list = new JSONArray();
		List<InstanceInfo> instances = null;
		try {
			instances = dcmService.queryInstanceInfosByCondition(patId,
					studyId, seriesID);
			for (int i = 0; i < instances.size(); i++) {
				InstanceInfo instance = (InstanceInfo) instances.get(i);
				int index = 0;
				String indexnum =instance.getInstanceNo();
				if (indexnum == null) {
					list.put("imagestream?studyUID=" + studyId
							+ "&seriesUID=" + seriesID + "&objectUID="
							+ instance.getSopUID());
				} else {
					index = Integer.parseInt(indexnum);
					list.put(index, "imagestream?studyUID=" + studyId
							+ "&seriesUID=" + seriesID + "&objectUID="
							+ instance.getSopUID());
				}
			}
			for (int i = 0; i < list.length(); i++) {
				if (list.get(i).equals(null)) {
					list.remove(i);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
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
}
