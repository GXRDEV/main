package com.tspeiz.modules.common.Exception;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class MyExceptionResolver implements HandlerExceptionResolver{
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger log=Logger.getLogger("MongoDB2");
	public ModelAndView resolveException(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception ex) {
		// TODO Auto-generated method stub
		log.info("{'time':'"+format.format(new Date())+"','exception': '"+JSONArray.fromObject(ex.getStackTrace()).toString()+"'}");
		ex.printStackTrace();
		return new ModelAndView("error");
	}
}
