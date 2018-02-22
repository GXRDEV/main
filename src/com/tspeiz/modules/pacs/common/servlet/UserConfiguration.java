package com.tspeiz.modules.pacs.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserConfiguration extends HttpServlet {
	/*private static Logger log = Logger.getLogger(UserConfiguration.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = null;
		try {
			String settings = request.getParameter("settings");
			String actionToDo = request.getParameter("todo");
			String settingsValue = request.getParameter("settingsValue");

			String userName = request.getUserPrincipal().getName();

			if (actionToDo.equalsIgnoreCase("ROLE")) {
				String role = "";
				if ((request.isUserInRole("WebAdmin"))
						|| (request.isUserInRole("admin")))
					role = "Admin";
				else {
					role = "Other";
				}
				response.setContentType("text/html");
				out = response.getWriter();
				out.print(role);
				out.close();
			} else {
				UserHandler uh = new UserHandler();
				String str = null;
				out = response.getWriter();
				User user = uh.findUserByName(userName);

				if (user == null) {
					user = new User();
					user.setUserName(userName);
					Button btn = new Button();
					btn.setLabel("Today CT");
					btn.setDateCrit("t");
					btn.setModality("CT");
					btn.setAutoRefresh("0");
					uh.addNewUser(btn, userName);
				}

				if (user != null)
					if (actionToDo.equalsIgnoreCase("READ")) {
						if (settings.equals("theme")) {
							str = user.getTheme();
						} else if (settings.equals("sessTimeout")) {
							str = user.getSessTimeout();
						} else if (settings.equals("userName")) {
							str = user.getUserName();
							String sessTimeout = user.getSessTimeout();
							if (sessTimeout != null) {
								HttpSession session = request.getSession(false);
								session.setMaxInactiveInterval(Integer
										.parseInt(sessTimeout));
							}
						} else if (settings.equals("viewerSlider")) {
							str = user.getViewerSlider();
						} else if (settings.equals("roles")) {
							if ((request.isUserInRole("WebAdmin"))
									|| (request.isUserInRole("admin")))
								str = "Admin";
							else
								str = "Other";
						} else if (settings.equals("buttons")) {
							QueryParamHandler qph = new QueryParamHandler();
							List butList = qph.getAllButtons(userName);
							JSONArray jsonArray = new JSONArray(butList);
							str = jsonArray.toString();
						}
						out.print(str);
					} else if (actionToDo.equalsIgnoreCase("UPDATE")) {
						if (settings.equals("theme"))
							user.setTheme(settingsValue);
						else if (settings.equals("sessTimeout"))
							user.setSessTimeout(settingsValue);
						else if (settings.equals("viewerSlider")) {
							user.setViewerSlider(settingsValue);
						}
						uh.updateUser(user);
						out.println("Success");
					}
			}
		} catch (Exception ex) {
			log.error("Exception occured in User Configuration servlet", ex);
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}*/
}
