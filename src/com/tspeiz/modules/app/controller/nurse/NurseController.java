package com.tspeiz.modules.app.controller.nurse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tspeiz.modules.app.base.BaseController;

@Controller
@RequestMapping("/nurse")
public class NurseController extends BaseController {

	/*@Resource(name = "appNurseService")
	private IAppNurseService appNurseService;

	private SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat codesdf = new SimpleDateFormat("yyyyMMddHHmmss");

	*//**
	 * 帐号是否存在
	 * 
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/isexist", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<Boolean> isUserExist(
			@RequestParam("registerName") String registerName,
			HttpServletRequest request, HttpServletResponse response) {
		ReturnValueNet<Boolean> returnValueNet = new ReturnValueNet<Boolean>();

		try {

			// 用户名或秘密为空
			if (StringUtil.isBlank(registerName)) {

				returnValueNet.setCode(AppReturnCodeEnum.CODE_USER_NOT_EXIST
						.getCode());
				returnValueNet.setDesc(AppReturnCodeEnum.CODE_USER_NOT_EXIST
						.getDesc());
				return returnValueNet;
			}

			request.setCharacterEncoding("UTF-8");
			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());

			if (appNurseService.isNurseAccountExist(registerName)) {

				returnValueNet.setResult(true);
				returnValueNet.setDesc("此帐号已被注册使用");
			} else {

				returnValueNet.setResult(false);
			}

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}

	}

	*//**
	 * 获取验证码，需要把SessionId发给客户端
	 * 
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/getverifycode", method = RequestMethod.POST)
	@ResponseBody
	public ReturnValueNet<Integer> getVerifyCode(
			@RequestBody final Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response) {
		ReturnValueNet<Integer> returnValueNet = new ReturnValueNet<Integer>();
		try {
			request.setCharacterEncoding("UTF-8");
			HttpSession session = request.getSession();

			Cookie cookie = new Cookie("JSESSIONID", session.getId());
			response.addCookie(cookie);

			String telephone = map.get("telephone").toString();

			String verifyCode = CheckNumUtil.randomChars(4);
			session.setAttribute("verifyCode", verifyCode);
			session.setAttribute("verifyCodeTime", codesdf.format(new Date()));

			System.out.println("SessionId:" + session.getId()
					+ ",getVerifyCode:" + verifyCode);

			String content = "验证码" + verifyCode
					+ ".您正在使用天使陪诊医护端,需要进行验证.(请勿向任何人提供您收到的短信验证码)【天使陪诊】";

			String ret = HttpSendSmsUtil.sendSmsInteface(telephone, content);
			int retCode = Integer.parseInt(ret);
			if (retCode == 100) {
				returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			} else {
				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_SEND_VERIFYCODE_ERROR
								.getCode());
				returnValueNet.setDesc(CheckNumUtil.getErrString(retCode));
			}

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());

			return returnValueNet;
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public ReturnValueNet<NurseInfo> register(
			@RequestBody Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		ReturnValueNet<NurseInfo> returnValueNet = new ReturnValueNet<NurseInfo>();

		try {
			request.setCharacterEncoding("UTF-8");
			HttpSession session = request.getSession();

			String registerName = map.get("registerName").toString();
			String password = map.get("password").toString();
			String verifyCode = map.get("verifyCode").toString();
			String version = map.get("version").toString();// 版本号
			Object localVerifyCode = session.getAttribute("verifyCode");

			System.out.print("======NurseController:register registerName:"
					+ registerName + ",password:" + password);

			// 用户名或秘密为空
			if (StringUtil.isBlank(registerName)
					|| StringUtil.isBlank(password)) {

				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_ACCOUNT_OR_PASSWORD_EMPTY
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_ACCOUNT_OR_PASSWORD_EMPTY
								.getDesc());
				return returnValueNet;
			}
			// 验证码实效
			if (localVerifyCode == null || localVerifyCode.toString().isEmpty()) {

				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_VERIFYCODE_NOT_EXIST
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_VERIFYCODE_NOT_EXIST
								.getDesc());

				return returnValueNet;

				// 验证码不匹配
			} else if (!verifyCode.equalsIgnoreCase(localVerifyCode.toString())) {

				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_VERIFYCODE_NOT_MATCH
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_VERIFYCODE_NOT_MATCH
								.getDesc());

				return returnValueNet;
			}

			if (checkVersionOutdated(version)) {

				returnValueNet.setCode(AppReturnCodeEnum.CODE_VERSION_OUTDATED
						.getCode());
				returnValueNet.setDesc(AppReturnCodeEnum.CODE_VERSION_OUTDATED
						.getDesc());

				return returnValueNet;
			}

			String localVerifyCodeTime = (String) session
					.getAttribute("verifyCodeTime");
			String now = codesdf.format(new Date());
			long second = DateUtil.calculartorDateSecond(localVerifyCodeTime,
					now);
			// 验证码已过期
			if (second > 60) {
				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_VERIFYCODE_EXPIRED
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_VERIFYCODE_EXPIRED
								.getDesc());

				return returnValueNet;
			}

			boolean isExist = appNurseService.isNurseAccountExist(registerName);

			// 帐号信息已经存在
			if (!isExist) {

				NurseInfo nurseInfo = new NurseInfo();
				nurseInfo.setRegisterName(registerName);
				nurseInfo.setTelephone(registerName);
				nurseInfo.setPassword(password);
				nurseInfo.setBalance(0.0f);
				nurseInfo.setIsCheck(0);
				Date curDate = new Date();
				nurseInfo.setRegisterTime(formatter.format(curDate));

				appNurseService.saveNurseInfo(nurseInfo);

				returnValueNet.setResult(nurseInfo);
				returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());

				session.setAttribute(CUR_NURSE_ID, nurseInfo.getId());
				session.setMaxInactiveInterval(-1);// 永久有效

				// 注册成功后取消验证码。
				session.removeAttribute("verifyCode");
				session.removeAttribute("verifyCodeTime");
			} else {

				returnValueNet.setCode(AppReturnCodeEnum.CODE_USER_REGISTERED
						.getCode());
				returnValueNet.setDesc(AppReturnCodeEnum.CODE_USER_REGISTERED
						.getDesc());

				// 注册成功后取消验证码。
				session.removeAttribute("verifyCode");
				session.removeAttribute("verifyCodeTime");
			}

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());

			return returnValueNet;
		}
	}

	*//**
	 * 
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ReturnValueNet<NurseInfo> login(
			@RequestBody final Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response) {
		ReturnValueNet<NurseInfo> returnValueNet = new ReturnValueNet<NurseInfo>();
		try {

			JSONObject json = JSONObject.fromObject(map);

			System.out.print("=====login:" + json.toString());

			request.setCharacterEncoding("UTF-8");
			HttpSession session = request.getSession();

			Cookie cookie = new Cookie("JSESSIONID", session.getId());
			response.addCookie(cookie);

			String registerName = map.get("registerName").toString();
			String password = map.get("password").toString();
			String version = map.get("version").toString();// 版本号

			// 用户名或秘密不能为空
			if (StringUtil.isBlank(registerName)
					|| StringUtil.isBlank(password)) {

				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_ACCOUNT_OR_PASSWORD_EMPTY
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_ACCOUNT_OR_PASSWORD_EMPTY
								.getDesc());

				return returnValueNet;
			}

			if (checkVersionOutdated(version)) {

				returnValueNet.setCode(AppReturnCodeEnum.CODE_VERSION_OUTDATED
						.getCode());
				returnValueNet.setDesc(AppReturnCodeEnum.CODE_VERSION_OUTDATED
						.getDesc());

				return returnValueNet;
			}

			NurseInfo nurseInfo = appNurseService
					.getAllNurseInfoByRegisterName(registerName);

			System.out.print(JSONObject.fromObject(nurseInfo).toString());

			if (nurseInfo != null
					&& password.equalsIgnoreCase(nurseInfo.getPassword())) {

				// 把注册信息保存到session中去
				session.setAttribute(CUR_NURSE_ID, nurseInfo.getId());
				session.setMaxInactiveInterval(-1);// 永久有效

				returnValueNet.setResult(nurseInfo);
				returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());

			} else {
				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_ACCOUNT_OR_PASSWORD_ERROR
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_ACCOUNT_OR_PASSWORD_ERROR
								.getDesc());
			}
			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());

			return returnValueNet;
		}
	}

	*//**
	 * 找回密码
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/retrievepassword", method = RequestMethod.POST)
	@ResponseBody
	public ReturnValueNet<Integer> retrivevPasswordNew(
			@RequestBody final Map<String, String> map,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ReturnValueNet<Integer> returnValueNet = new ReturnValueNet<Integer>();
		try {

			request.setCharacterEncoding("UTF-8");
			HttpSession session = request.getSession();

			String telephone = map.get("telephone").toString();
			String password = map.get("newPassword").toString();
			String verifyCode = map.get("verifyCode").toString();

			Object localVerifyCode = session.getAttribute("verifyCode");

			// 用户名或秘密为空
			if (StringUtil.isBlank(password)) {

				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_ACCOUNT_PASSWORD_EMPTY
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_ACCOUNT_PASSWORD_EMPTY
								.getDesc());
				return returnValueNet;
			}

			// 用户名或秘密为空
			if (StringUtil.isBlank(telephone)) {

				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_ACCOUNT_TELEPHONE_EMPTY
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_ACCOUNT_TELEPHONE_EMPTY
								.getDesc());
				return returnValueNet;
			}
			// 验证码实效
			if (localVerifyCode == null || localVerifyCode.toString().isEmpty()) {

				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_VERIFYCODE_NOT_EXIST
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_VERIFYCODE_NOT_EXIST
								.getDesc());

				return returnValueNet;

				// 验证码不匹配
			} else if (!verifyCode.equalsIgnoreCase(localVerifyCode.toString())) {

				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_VERIFYCODE_NOT_MATCH
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_VERIFYCODE_NOT_MATCH
								.getDesc());

				return returnValueNet;
			}

			String localVerifyCodeTime = (String) session
					.getAttribute("verifyCodeTime");
			String now = codesdf.format(new Date());
			long second = DateUtil.calculartorDateSecond(localVerifyCodeTime,
					now);
			// 验证码已过期
			if (second > 60) {
				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_VERIFYCODE_EXPIRED
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_VERIFYCODE_EXPIRED
								.getDesc());

				return returnValueNet;
			}

			NurseInfo nurseInfo = appNurseService
					.getNurseInfoByTelephone(telephone);
			if (nurseInfo != null) {

				nurseInfo.setPassword(password);
				appNurseService.updateNurseInfo(nurseInfo);

				// session.setAttribute(CUR_NURSE_ID, nurseInfo.getId());
				// session.setMaxInactiveInterval(-1);// 永久有效

				returnValueNet.setResult(nurseInfo.getId());
				returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());

				// 注册成功后取消验证码。
				session.removeAttribute("verifyCode");
				session.removeAttribute("verifyCodeTime");
				session.invalidate();

			}

			else {
				returnValueNet.setCode(AppReturnCodeEnum.CODE_USER_NOT_EXIST
						.getCode());
				returnValueNet.setDesc(AppReturnCodeEnum.CODE_USER_NOT_EXIST
						.getDesc());

				// 注册成功后取消验证码。
				session.removeAttribute("verifyCode");
				session.removeAttribute("verifyCodeTime");
			}
			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());

			return returnValueNet;
		}
	}

	@RequestMapping(value = "/modifypassword", method = RequestMethod.POST)
	@ResponseBody
	public ReturnValueNet<Integer> modifyPassword(
			@RequestBody final Map<String, String> map,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ReturnValueNet<Integer> returnValueNet = new ReturnValueNet<Integer>();
		try {

			String registerName = map.get("registerName");
			String oldPassword = map.get("oldPassword");
			String newPassword = map.get("newPassword");

			// 用户名或秘密为空
			if (StringUtil.isBlank(registerName)
					|| StringUtil.isBlank(oldPassword)
					|| StringUtil.isBlank(newPassword)) {

				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_ACCOUNT_OR_PASSWORD_EMPTY
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_ACCOUNT_OR_PASSWORD_EMPTY
								.getDesc());
				return returnValueNet;
			}

			NurseInfo nurseInfo = appNurseService
					.getNurseInfoByRegisterName(registerName);
			if (nurseInfo != null) {

				if (oldPassword.equalsIgnoreCase(nurseInfo.getPassword())) {

					nurseInfo.setPassword(newPassword);
					appNurseService.updateNurseInfo(nurseInfo);

				} else {

					returnValueNet
							.setCode(AppReturnCodeEnum.CODE_ACCOUNT_OR_PASSWORD_ERROR
									.getCode());
					returnValueNet
							.setDesc(AppReturnCodeEnum.CODE_ACCOUNT_OR_PASSWORD_ERROR
									.getDesc());
				}
			} else {

				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_ACCOUNT_OR_PASSWORD_ERROR
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_ACCOUNT_OR_PASSWORD_ERROR
								.getDesc());
			}

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());

			return returnValueNet;
		}
	}

	*//**
	 * 用户注销
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/logout", method = RequestMethod.DELETE)
	@ResponseBody
	public ReturnValueNet<Integer> logout(HttpServletRequest request,
			@RequestParam("id") Integer id) {

		ReturnValueNet<Integer> returnValueNet = new ReturnValueNet<Integer>();

		try {
			request.setCharacterEncoding("UTF-8");
			HttpSession session = request.getSession();

			if (session.getAttribute(CUR_NURSE_ID) != null) {
				session.removeAttribute(CUR_NURSE_ID);
			}
			// session失效
			session.invalidate();

			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception

			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());

			return returnValueNet;
		}
	}

	@RequestMapping(value = "/getAccountBalance", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<Float> getAccountBalance(
			@RequestParam("id") Integer id, HttpServletRequest request) {

		ReturnValueNet<Float> returnValueNet = new ReturnValueNet<Float>();

		try {
			request.setCharacterEncoding("UTF-8");
			NurseInfo nurseInfo = appNurseService.getNurseInfoById(id);

			if (nurseInfo != null) {

				returnValueNet.setResult(nurseInfo.getBalance());
				returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			} else {

				returnValueNet.setCode(AppReturnCodeEnum.CODE_USER_NOT_EXIST
						.getCode());
				returnValueNet.setDesc(AppReturnCodeEnum.CODE_USER_NOT_EXIST
						.getDesc());
			}

			// }

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}

	}

	@RequestMapping(value = "/nurseCheckStatus", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<Integer> getNurseCheckStatus(
			@RequestParam("id") Integer id, HttpServletRequest request) {

		ReturnValueNet<Integer> returnValueNet = new ReturnValueNet<Integer>();

		try {
			request.setCharacterEncoding("UTF-8");
			NurseInfo nurseInfo = appNurseService.getNurseInfoById(id);

			if (nurseInfo != null) {

				int nCheckStatus = nurseInfo.getIsCheck() != null ? nurseInfo
						.getIsCheck() : 0;

				returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
				returnValueNet.setResult(nCheckStatus);

			} else {

				returnValueNet.setCode(AppReturnCodeEnum.CODE_USER_NOT_EXIST
						.getCode());
				returnValueNet.setDesc(AppReturnCodeEnum.CODE_USER_NOT_EXIST
						.getDesc());
			}

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}

	}

	// //////////////////////////////////////////////////////////////////////////////
	private boolean checkVersionOutdated(String version) {

		if (LOWEST_NURSE_VERSION.compareToIgnoreCase(version) > 0) {
			return true;
		}

		return false;
	}*/

}
