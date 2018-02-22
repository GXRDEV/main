package com.tspeiz.modules.common.service;

import javax.servlet.http.HttpServletRequest;

public interface ICaseService {
	public Integer saveOrUpdateCase(HttpServletRequest request,Integer userId,Integer usertype) throws Exception;
}
