package com.tspeiz.modules.common.handler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.tspeiz.modules.common.bean.dcm.SeriesInfo;
import com.tspeiz.modules.common.service.IDcmService;
import com.tspeiz.modules.util.SpringUtil;

public class SeriesDetailsHandler extends SimpleTagSupport {
	private static Logger log = Logger.getLogger(SeriesDetailsHandler.class);
	private String patientId;
	private String study;
	private String dcmURL;
	private static IDcmService dcmService;
	static {
		ApplicationContext ac = SpringUtil.getApplicationContext();
		dcmService = (IDcmService) ac.getBean("dcmService");
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
		List<SeriesInfo> seriesList=null;
		try {
			seriesList=dcmService.querySeriesInfosByCondition(patientId, this.study);
			for (int i = 0; i < seriesList.size(); i++) {
				SeriesInfo serInfo=seriesList.get(i);
				getJspContext().setAttribute("seriesId",serInfo.getSeriesUID());
				getJspContext().setAttribute("seriesNumber",
						serInfo.getSeriesNumber());
				getJspContext().setAttribute("seriesDesc",
						serInfo.getSeriesDesc());
				getJspContext().setAttribute("modality", serInfo.getModality());
				getJspContext().setAttribute("numberOfImages",
						serInfo.getTotalInstances());
				getJspContext().setAttribute("bodyPart",
						serInfo.getBodyPart());
				getJspContext().setAttribute("firstSeries",
						Boolean.valueOf(i == 0));
				getJspBody().invoke(null);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}