package com.tspeiz.modules.home;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.tspeiz.modules.util.oss.OSSConfigure;
import com.tspeiz.modules.util.oss.OSSManageUtil;

public class UeditorImageController implements Controller {
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		System.out.println("===============开始上传==============");
		Map<String, Object> ret_map = OSSManageUtil.uploadPhoto("ueditor/",
				new OSSConfigure(), request);
		JSONObject obj = new JSONObject();
		if (ret_map.containsKey("urlpath")) {
			obj.put("state", "SUCCESS");
			obj.put("url", ret_map.get("urlpath").toString());
			obj.put("title", ret_map.get("filename").toString());
			obj.put("original", ret_map.get("filename").toString());
		} else {

		}
		System.out.println("=================上传结束=============");
		response.getWriter().print(obj.toString());
		return null;
	}
}
