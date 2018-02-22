package com.tspeiz.modules.common.handler;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.tspeiz.modules.common.bean.dcm.InstanceInfo;
import com.tspeiz.modules.common.service.IDcmService;
import com.tspeiz.modules.util.SpringUtil;

public class ImageHandler extends SimpleTagSupport {
	private static Logger log = Logger.getLogger(ImageHandler.class);
	private String patientId;
	private String study;
	private String series;
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

	public void setSeries(String series) {
		this.series = series;
	}

	public void setDcmURL(String dcmURL) {
		this.dcmURL = dcmURL;
	}

	public void doTag() throws JspException, IOException {
		List<InstanceInfo> instances = null;
		try {
			instances = dcmService.queryInstanceInfosByCondition(patientId,
					study, series);
			for (int instanceCount = 0; instanceCount < instances.size(); instanceCount++) {
				InstanceInfo instance = (InstanceInfo) instances
						.get(instanceCount);

				getJspContext().setAttribute("imageId", instance.getSopUID());

				getJspContext().setAttribute("instanceNumber",
						Integer.valueOf(instanceCount + 1));

				getJspContext().setAttribute("sopClassUID",
						instance.getSopClassUID());
				getJspContext().setAttribute("numberOfFrames",
						instance.getNumberOfFrames());
				getJspContext().setAttribute(
						"multiframe",
						!instance.getNumberOfFrames().equals("0") ? "yes"
								: "no");
				getJspContext().setAttribute("thumbSize","");
				getJspBody().invoke(null);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.info("=====imagehandler====");
	}
}