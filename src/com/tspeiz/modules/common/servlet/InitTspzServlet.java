package com.tspeiz.modules.common.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.tspeiz.modules.common.thread.TokenThread;
import com.tspeiz.modules.util.PropertiesUtil;

/**
 * 永久获取accessAction
 * 
 * @author heyongb
 * 
 */
public class InitTspzServlet extends HttpServlet {
	private static final long serialVersionUID = 6778177010613784833L;

	public void init() throws ServletException {
		TokenThread.appid = PropertiesUtil.getString("APPID");
		TokenThread.appsecret = PropertiesUtil.getString("APPSECRET");
		// 未配置appid、appsecret时给出提示
		if ("".equals(TokenThread.appid) || "".equals(TokenThread.appsecret)) {
			System.out.println("===appid和appsecret为空===");
		} else {
			// 启动定时获取access_token的线程
			new Thread(new TokenThread()).start();
		}
	}
}
