package com.tspeiz.modules.dicom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tspeiz.modules.util.common.StringRetUtil;

public class ImageServlet extends HttpServlet {

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		 
		try {

			String imageURL = "";
			String study = request.getParameter("studyUID");
			String series = request.getParameter("seriesUID");
			String object = request.getParameter("objectUID");
			String type = request.getParameter("type");
			String framenumber = request.getParameter("framedata");
			String ip_port=StringRetUtil.gainPacsIPAndPort();
			if (framenumber == null) {
				framenumber = "Empty";
			}
			if ((type != null) && (!type.equalsIgnoreCase("")))
				imageURL = "http://"+ip_port+"/wado?requestType=WADO&studyUID="
						+ study
						+ "&seriesUID="
						+ series
						+ "&objectUID="
						+ object
						+ "&transferSyntax=1.2.840.100008.1.2&contentType=application/dicom";
			else if (!framenumber.equals("Empty"))
				imageURL = "http://"+ip_port+"/wado?requestType=WADO&studyUID="
						+ study
						+ "&seriesUID="
						+ series
						+ "&objectUID="
						+ object + "&frameNumber=" + framenumber;
			else {
				imageURL = "http://"+ip_port+"/wado?requestType=WADO&studyUID="
						+ study
						+ "&seriesUID="
						+ series
						+ "&objectUID="
						+ object + "&rows=500&coloumns=500";
			}
			InputStream resultInStream = null;
			OutputStream resultOutStream = response.getOutputStream();
			try {
				URL imgURL = new URL(imageURL);

				if ((object != null) && (object.length() > 0)) {
					resultInStream = imgURL.openStream();

					byte[] buffer = new byte[4096];
					int bytes_read;
					while ((bytes_read = resultInStream.read(buffer)) != -1) {
						resultOutStream.write(buffer, 0, bytes_read);
					}

					resultOutStream.flush();
				}
				resultOutStream.flush();
				resultOutStream.close();
				resultInStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
		}
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