package com.tspeiz.modules.dicom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tspeiz.modules.util.PropertiesUtil;

public class DcmImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(DcmImage.class);
	private static String DCM_IP="";
	private static String DCM_WADO_PORT="";
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DCM_IP=PropertiesUtil.getString("DCM_IP");
		DCM_WADO_PORT=PropertiesUtil.getString("DCM_WADO_PORT");
		String imageURL = "";
		String study = request.getParameter("study");
		String series = request.getParameter("series");
		String object = request.getParameter("object");
		String contentType = request.getParameter("contentType");
		imageURL = "http://"+DCM_IP+":"+DCM_WADO_PORT+"/wado?requestType=WADO&studyUID=" + study + "&seriesUID="
				+ series + "&objectUID=" + object;
		if (contentType != null) {
			imageURL = imageURL + "&contentType=" + contentType;
			response.setContentType(contentType);
		}
		InputStream resultInStream = null;
		OutputStream resultOutStream = response.getOutputStream();
		try {
			URL imageUrl = new URL(imageURL);
			resultInStream = imageUrl.openStream();
			byte[] buffer = new byte[4096];
			int bytes_read;
			while ((bytes_read = resultInStream.read(buffer)) != -1) {
				resultOutStream.write(buffer, 0, bytes_read);
			}
			resultOutStream.flush();
			resultOutStream.close();
			resultInStream.close();
		} catch (Exception e) {
			log.error(e);
		}
	}
}
