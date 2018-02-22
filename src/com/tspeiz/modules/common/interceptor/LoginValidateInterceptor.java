package com.tspeiz.modules.common.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tspeiz.modules.common.entity.Users;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;

public class LoginValidateInterceptor implements HandlerInterceptor {

	// private String loginUrl = "/tspeiz/login.jsp";
	private String loginUrl = "/index.jsp";
	private List<String> uncheckUrls;

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		String requestUrl = request.getRequestURI();
		System.out.println("==请求地址=="+requestUrl);
		if (requestUrl.contains("/expert/")
				|| requestUrl.contains("/docadmin/")
				|| requestUrl.contains("/nuradmin/")
				|| requestUrl.contains("/hospital/")
				|| requestUrl.contains("/doctor/index")
				|| requestUrl.contains("/doctor/gainVeryCode")
				|| requestUrl.contains("/system/changedtstatus")
				|| requestUrl.contains("/system/changeHosHealthStatus")
				|| requestUrl.contains("/system/savehoshealth")
				|| requestUrl.contains("/system/createGroup")
				|| requestUrl.contains("/system/disSolveGroup")
				|| requestUrl.contains("/system/disSolveDocTeamGroup")
				|| requestUrl.contains("/xinyi")) {
			if(requestUrl.contains("/xinyi")) return true;
			if (!uncheckUrls.contains(requestUrl)) {
				boolean flag = true;
				HttpSession session = request.getSession(true);
				DoctorRegisterInfo user = (DoctorRegisterInfo) session
						.getAttribute("user");
				if (user == null) {
					flag = false;
					response.sendRedirect(loginUrl);
				}
				if (!flag) {
					return false;
				}
			}
		}
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
	}

	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
	}

	public List<String> getUncheckUrls() {
		return uncheckUrls;
	}

	public void setUncheckUrls(List<String> uncheckUrls) {
		this.uncheckUrls = uncheckUrls;
	}

}
