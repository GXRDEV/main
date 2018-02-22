package com.tspeiz.modules.pacs.common.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tspeiz.modules.util.common.StringRetUtil;

public class DcmStreamServlet extends HttpServlet {
	private static final long serialVersionUID = -772903801100672322L;
	private static Logger log = Logger.getLogger(DcmStreamServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String wadoUrl = request.getParameter("wadourl");
		String modifiedWadoUrl = wadoUrl.replaceAll("_", "&");
		//modifiedWadoUrl=modifiedWadoUrl.replace("undefined", "1.2.392.200036.9116.2.5.1.48.1221422019.1465108723.461968");
		String imageURL = modifiedWadoUrl
				.concat("&transferSyntax=1.2.840.100008.1.2&contentType=application/dicom");
		String ip_port=StringRetUtil.gainPacsIPAndPort();
		imageURL="http://"+ip_port+imageURL;
		InputStream resultInStream = null;
		OutputStream out = response.getOutputStream();
		try {
			URL imageUrl = new URL(imageURL);
			resultInStream = imageUrl.openStream();
			byte[] buffer = new byte[4096];
			int bytes_read;
			while ((bytes_read = resultInStream.read(buffer)) != -1) {
				out.write(buffer, 0, bytes_read);
			}
			out.flush();
		} catch (Exception e) {
			log.error("Error while streaming DICOM image ", e);
		} finally {
			out.close();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}