package com.tspeiz.modules.common.handler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.tspeiz.modules.common.bean.dcm.StudyInfo;
import com.tspeiz.modules.common.bean.dcm.oviyam.PatientInfo;
import com.tspeiz.modules.common.bean.dcm.oviyam.StudyModel;
import com.tspeiz.modules.common.service.IDcmService;
import com.tspeiz.modules.util.SpringUtil;

public class StudyInfoHandler extends SimpleTagSupport {
	private static Logger log = Logger.getLogger(StudyInfoHandler.class);
	private String patientId;
	private String study;
	private String dcmURL;
	private SimpleDateFormat parseDateFmt;
	private SimpleDateFormat readableDateFmt;
	private static IDcmService dcmService;
	static {
		ApplicationContext ac = SpringUtil.getApplicationContext();
		dcmService = (IDcmService) ac.getBean("dcmService");
	}

	public StudyInfoHandler() {
		this.parseDateFmt = new SimpleDateFormat("yyyyMMdd");
		this.readableDateFmt = new SimpleDateFormat("dd-MM-yyyy");
	}

	public void setPatientId(String patientId) {
		if (patientId == null)
			this.patientId = "";
		else
			this.patientId = patientId;
	}

	public void setStudy(String study) {
		this.study = study;
	}

	public void setDcmURL(String dcmURL) {
		this.dcmURL = dcmURL;
	}

	public void doTag() throws JspException, IOException {
		log.info("==============进入studyinfohandler====" + this.patientId);
		List<StudyInfo> studyList = null;
		try {
			studyList = dcmService.queryStudysByCondition(patientId);
		} catch (Exception e) {
			log.error(
					"Unable to create instance of SeriesInfo and access its callFindWithQuery()",
					e);
			return;
		}
		try {
			for (int i = 0; i < studyList.size(); i++) {
				StudyInfo study = studyList.get(i);
				getJspContext().setAttribute("studyId", study.getStudyUID());
				getJspContext().setAttribute("studyDate",
						study.getStudyDate().toString());
				getJspContext().setAttribute("studyDesc", study.getStudyDesc());
				getJspContext().setAttribute("modalityInStudy",
						study.getModality());
				getJspContext().setAttribute("numberOfImages",
						study.getTotalIns());
				getJspContext().setAttribute("numberOfSeries",
						study.getTotalSeries());
				getJspContext().setAttribute("selected",
						Boolean.valueOf(i == 0));
				getJspBody().invoke(null);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}