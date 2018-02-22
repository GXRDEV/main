package com.tspeiz.modules.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class MyMultipartResolver extends CommonsMultipartResolver {
	public boolean isMultipart(HttpServletRequest request) {
		if (request.getRequestURI().contains("/uploadLocalFile")
				|| request.getRequestURI().contains("uploadPic")
				|| request.getRequestURI().contains("/imageupload")
				|| request.getRequestURI().contains("/uploadFileNew")) {
			return false;
		} else {
			return super.isMultipart(request);
		}
	}
}
