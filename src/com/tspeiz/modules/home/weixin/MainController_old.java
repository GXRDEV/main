package com.tspeiz.modules.home.weixin;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tspeiz.modules.common.bean.weixin.DepartHelper;
import com.tspeiz.modules.common.bean.weixin.DepartString;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.entity.BigDepartment;
import com.tspeiz.modules.common.entity.CooHospitalDepartments;
import com.tspeiz.modules.common.entity.CooHospitalDetails;
import com.tspeiz.modules.common.entity.CustomUpload;
import com.tspeiz.modules.common.entity.Departments;
import com.tspeiz.modules.common.entity.ExpertConsultationSchedule;
import com.tspeiz.modules.common.entity.FaceDiagnoses;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.SpecialistAppoint;
import com.tspeiz.modules.common.entity.Users;
import com.tspeiz.modules.common.entity.VedioRelative;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.UserAccountInfo;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;
import com.tspeiz.modules.common.entity.newrelease.UserWeixinRelative;
import com.tspeiz.modules.common.entity.weixin.Cases;
import com.tspeiz.modules.common.entity.weixin.CasesImage;
import com.tspeiz.modules.common.entity.weixin.ContactInfo;
import com.tspeiz.modules.common.entity.weixin.GraphicsInfo;
import com.tspeiz.modules.common.entity.weixin.Orders;
import com.tspeiz.modules.common.entity.weixin.UserExternals;
import com.tspeiz.modules.common.entity.weixin.UserFeedback;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.common.thread.TokenThread;
import com.tspeiz.modules.util.CheckNumUtil;
import com.tspeiz.modules.util.DateUtil;
import com.tspeiz.modules.util.EmojiFilterUtil;
import com.tspeiz.modules.util.IdcardUtils;
import com.tspeiz.modules.util.LocationUtil;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.PythonVisitUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.oss.OSSConfigure;
import com.tspeiz.modules.util.oss.OSSManageUtil;
import com.tspeiz.modules.util.weixin.CommonUtil;
import com.tspeiz.modules.util.weixin.SignUtil;
import com.tspeiz.modules.util.weixin.WeixinUtil;

public class MainController_old {
	private Logger log = Logger.getLogger(MainController_old.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat code_time = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat _sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private SimpleDateFormat dir_time = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat _format=new SimpleDateFormat("yyyy-MM-dd");
	@Resource
	private IWeixinService weixinService;
	@Resource
	private ICommonService commonService;

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		openid = gainOpenIdByConditions(request, openid);
		map.put("openid", openid);
		// 合作机构
		List<HospitalDetailInfo> coohos=weixinService.queryCoohospitalInfos();
		map.put("coohos", coohos);
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		return new ModelAndView("zjh/main", map);
	}

	// 获取所在城市
	@RequestMapping(value = "/gainCityByLocation", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainCityByLocation(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String latitude = request.getParameter("latitude");
		String longitude = request.getParameter("longitude");
		String city = "";
		Integer chid = null;
		if (StringUtils.isNotBlank(latitude)
				&& StringUtils.isNotBlank(longitude)) {
			city = LocationUtil
					.gainLocationString(Double.parseDouble(latitude),
							Double.parseDouble(longitude));
			CooHospitalDetails cd = weixinService
					.queryCooHospitalDetailsByCity(city);
			if (cd != null)
				chid = cd.getId();
		}
		map.put("chid", chid);
		map.put("city", city);
		return map;
	}

	// 我选科室
	@RequestMapping(value = "/seldeparts", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> seldeparts(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Departments> departs = weixinService.queryDepartments();
		map.put("departs", departs);
		return map;
	}

	// 进入专家列表(可做成菜单的方法和选择科室后进入的方法)
	@RequestMapping(value = "/speciallist", method = RequestMethod.GET)
	public ModelAndView speciallist(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer _depid = null;
		String openid = request.getParameter("openid");
		String bigDepId = request.getParameter("bigDepId");
		String depid = request.getParameter("depid");
		String scontent = request.getParameter("scontent");// 检索
		String scity = request.getParameter("scity");// 城市过滤
		String spro = request.getParameter("spro");// 职称过滤
		String cooHosId = request.getParameter("cooHosId");
		map.put("cooHosId", cooHosId);
		map.put("scontent", scontent);
		map.put("scity", scity);
		map.put("spro", spro);
		if (StringUtils.isNotBlank(depid))
			_depid = Integer.parseInt(depid);
		List<MobileSpecial> specials = weixinService
				.queryMobileSpecialsByConditions(_depid, scontent, scity, spro,
						1, 10, null);
		map.put("specials", specials);
		map.put("bigDepId", bigDepId);
		map.put("depid", depid);
		map.put("openid", openid);
		return new ModelAndView("zjh/special_list", map);
	}

	@RequestMapping(value = "/gainBigDepartments", produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	String gainBigDepartments(HttpServletRequest request) {
		List<BigDepartment> bigDeps = weixinService.queryBigDepartments();
		List<DepartHelper> helps = new ArrayList<DepartHelper>();
		for (BigDepartment _big : bigDeps) {
			List<Departments> departs = weixinService
					.queryDepartmentByBigDep(_big.getId());
			DepartHelper helper = new DepartHelper();
			helper.setName(_big.getDisplayName());
			helper.setBeans(departs);
			helps.add(helper);
		}
		return JSONArray.fromObject(helps).toString();
	}

	// 根据大科室 获取相应的科室
	@RequestMapping(value = "/gainDepartsByBigDep", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainDepartsByBigDep(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String bigDepId = request.getParameter("bigDepId");
		List<Departments> departs = null;
		if (StringUtils.isNotBlank(bigDepId)) {
			departs = weixinService.queryDepartmentByBigDep(Integer
					.parseInt(bigDepId));
		}
		map.put("departs", departs);
		log.info(JSONArray.fromObject(departs).toString());
		return map;
	}

	// 下滑获取更多专家列表
	@RequestMapping(value = "/morespecials", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> morespecials(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer _depid = null;
		String depid = request.getParameter("depid");
		String scontent = request.getParameter("scontent");
		String scity = request.getParameter("scity");
		String spro = request.getParameter("spro");
		String type = request.getParameter("stype");
		if (StringUtils.isNotBlank(depid))
			_depid = Integer.parseInt(depid);
		Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
		List<MobileSpecial> specials = weixinService
				.queryMobileSpecialsByConditions(_depid, scontent, scity, spro,
						pageNo, 10, type);
		map.put("specials", specials);
		return map;
	}

	@RequestMapping(value = "/specialdetail", method = RequestMethod.GET)
	public ModelAndView specialdetail(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String orderId = request.getParameter("orderId");// 二次或三次下单时会传送
		Integer specialid = Integer.parseInt(request.getParameter("sid"));
		Integer cooHosId = Integer.parseInt(request.getParameter("cooHosId"));// 合作医院id
		map.put("orderId", orderId);
		map.put("cooHosId", cooHosId);
		String openid = request.getParameter("openid");
		map.put("openid", openid);
		MobileSpecial special = weixinService.queryMobileSpecialById(specialid);
		map.put("special", special);
		StringBuilder sb = new StringBuilder();
		// 获取科室
		List<DepartString> list = weixinService.queryDepartStrings(special
				.getSpecialId());
		for (DepartString _ds : list) {
			sb.append(_ds.getDepName() + " ");
		}
		special.setDepName(sb.toString());
		map.put("sid", specialid);
		List<CooHospitalDepartments> hosDeps = weixinService
				.queryCooHospitalDepartmentsByHospitalAndExpert(specialid,
						cooHosId);
		map.put("hosDeps", hosDeps);
		List<MobileSpecial> localSpecials = weixinService
				.queryMobileSpecialsByCurLocationAndExpert(specialid, cooHosId);
		map.put("localSpecials", localSpecials);
		return new ModelAndView("zjh/special_detail", map);
	}

	@RequestMapping(value = "/gaintimes", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gaintimes(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String time = request.getParameter("timedate");// 日期
		Integer week = DateUtil.dayForWeek(time);
		Integer sid = Integer.parseInt(request.getParameter("sid"));
		List<ReSourceBean> times = commonService
				.queryExpertConsultationTimesByExpertId(sid, time, week);
		map.put("times", times);
		return map;
	}

	// 改版获取时间
	@RequestMapping(value = "/gaintimesnew", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gaintimesnew(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sdate = request.getParameter("sdate");
		Integer sid = Integer.parseInt(request.getParameter("sid"));
		List<ExpertConsultationSchedule> times = weixinService
				.queryExpertConTimeSchedulsByConditions(sid, sdate);
		map.put("times", times);
		return map;
	}

	// 点击提交确认订单
	@RequestMapping(value = "/sureorder", method = RequestMethod.POST)
	public ModelAndView sureorder(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		Integer expertId = Integer.parseInt(request.getParameter("expertId"));// 专家id
		String condate = request.getParameter("condate");// 会诊日期
		String contime = request.getParameter("contime");// 会诊时间
		Integer localHosId = Integer.parseInt(request
				.getParameter("localHosId"));// 当地医院
		Integer localDepartId = Integer.parseInt(request
				.getParameter("localDepartId"));// 当地科室
		Integer cooHosId = Integer.parseInt(request.getParameter("cooHosId"));// 合作医院
		String cmoney = request.getParameter("cmoney");
		UserExternals ue = weixinService.queryUserExternalByExternalId(openid);
		String orderId = request.getParameter("orderId");
		if (StringUtils.isNotBlank(orderId)) {
			// 二次或三次下单
			RemoteConsultation rc = weixinService
					.queryRemoteConsultationById(Integer.parseInt(orderId));
			if (rc.getStatus().equals(2)) {
				rc.setSecondRefreshTime(new Date());
				rc.setSecondConsultationDate(condate);
				rc.setSecondConsultationTime(contime);
				rc.setStatus(4);// 第二次下单
			} else if (rc.getStatus().equals(3)) {
				rc.setThirdRefreshTime(new Date());
				rc.setThirdConsultationDate(condate);
				rc.setThirdConsultationTime(contime);
				rc.setStatus(5);
			}
			rc.setProgressTag(null);
			weixinService.updateRemoteConsultation(rc);
			List<RemoteConsultation> orders = weixinService
					.queryRemoteConsultationsByConditions(openid, 1, 5,
							"1,2,3,4,5");// 进行中订单 4--二次下的单，5==三次下的单
			map.put("openid", openid);
			map.put("orders", orders);
			map.put("flag", "processing");
			return new ModelAndView("zjh/myorders", map);
		}
		String redirect = "";
		if (ue != null) {
			Users u = weixinService.queryUsersById(ue.getUserId());
			if (u != null && StringUtils.isNotBlank(u.getMobileNumber())) {
				CooHospitalDetails cooHos = weixinService
						.queryCooHosPitalDetailsById(cooHosId);
				MobileSpecial expert = weixinService
						.queryMobileSpecialById(expertId);// 专家
				CooHospitalDepartments coodep = weixinService
						.queryCooHospitalDepartmentsById(localDepartId);
				map.put("cooHos", cooHos);
				map.put("expert", expert);
				map.put("coodep", coodep);
				map.put("f_tel", u.getMobileNumber());
				// 常用联系人
				List<ContactInfo> conInfos = weixinService
						.queryContactInfosByOpenId(openid);
				map.put("conInfos", conInfos);
				redirect = "zjh/sure_order";
			}
		} else {
			redirect = "zjh/bind_tel";
		}
		map.put("openid", openid);
		map.put("expertId", expertId);
		map.put("condate", condate);
		map.put("contime", contime);
		map.put("localHosId", localHosId);
		map.put("localDepartId", localDepartId);
		map.put("cooHosId", cooHosId);
		map.put("cmoney", cmoney);
		return new ModelAndView(redirect, map);
	}

	

	// 通知
	// 购买电话预约订单通知
	@RequestMapping(value = "/remotenotify", method = RequestMethod.POST)
	public void remotenotify(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("=====远程会诊订单=====zhifutongzhi:");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		System.out.println("~~~~~~~~~~~~~~~~付款成功~~~~~~~~~");
		outSteam.close();
		inStream.close();
		String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
		log.info("===购买远程会诊订单订单ret=====" + result);
		// 修改订单状态---已支付
		updateRemoteNotifyOrder(result);
		response.getWriter().write(setXML("SUCCESS", ""));
	}

	private void updateRemoteNotifyOrder(String result) {
		try {
			Document doc = DocumentHelper.parseText(result);
			Element rootElt = doc.getRootElement(); // 获取根节点
			String tradeNo = rootElt.elementText("out_trade_no");
			RemoteConsultation rc = weixinService
					.queryRemoteConsultationByTradeNo(tradeNo);
			// 更新服务订单状态
			if (rc.getStatus() != null && rc.getStatus().equals(0)) {
				rc.setStatus(1);
				rc.setRefreshTime(new Date());
				weixinService.updateRemoteConsultation(rc);
				//更新时间为已选
				ExpertConsultationSchedule sch=weixinService.queryExpertConScheduleById(rc.getSchedulerDateId());
				sch.setHasAppoint("1");
				weixinService.updatExpertConScheduleDate(sch);
				// 对接his系统进行挂号
				toplusInHis(rc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/testHis",method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> testHis(HttpServletRequest request) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		RemoteConsultation rc=weixinService.queryRemoteConsultationById(49);
		toplusInHis(rc);
		return map;
	}

	// 支付完成时进行六安挂号
	private void toplusInHis(RemoteConsultation rc) throws Exception {
		Integer local_depid = rc.getLocalDepartId();
		CooHospitalDepartments depart = weixinService
				.queryCooHospitalDepartmentsById(local_depid);
		log.info("=====六安挂号开始====");
		Map<String, Object> pinfo = new HashMap<String, Object>();
		pinfo.put("name", rc.getPatientName());
		pinfo.put("id_card", rc.getIdCard());
		pinfo.put("telephone", rc.getTelephone());
		String pstr = PythonVisitUtil.generateJSONObject(pinfo).toString();
		System.out.println(pstr);
		pinfo.clear();
		pinfo.put("dept_id", depart.getLiuAnDepartmentId());
		pinfo.put("type_name", "普通挂号");
		pinfo.put("register_fee", 2);
		pinfo.put("register_type",gainType(rc));
		pinfo.put("register_time",gaintime(rc));
		String rinfo=PythonVisitUtil.generateJSONObject(pinfo).toString();
		pinfo.clear();
		pinfo.put("patient_info", pstr);
		pinfo.put("register_info", rinfo);
		String data = PythonVisitUtil.generateJSONObject(pinfo).toString();
		log.info("=====六安挂号data:" + data);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", data));
		Map<String,Object> map=PythonVisitUtil.register_info(nameValuePairs);
		rc.setRetId(map.get("eid").toString());
		rc.setRetMessage(map.get("message").toString());
		weixinService.updateRemoteConsultation(rc);
		log.info("=====六安挂号结束====");
	}
	
	private Integer gainType(RemoteConsultation rc){
		Integer type=3;
		String time="";
		if (StringUtils.isNotBlank(rc.getThirdConsultationDate())) {
			time = rc.getThirdConsultationDate().trim();
		} else if (StringUtils.isNotBlank(rc
				.getSecondConsultationDate())) {
			time = rc.getSecondConsultationDate().trim();
		} else {
			time = rc.getConsultationDate().trim();
		}
		if(time.equalsIgnoreCase(_format.format(new Date()))){
			type=0;
		}
		return type;
	}
	private String gaintime(RemoteConsultation rc){
		String time="";
		if (StringUtils.isNotBlank(rc.getThirdConsultationDate())) {
			time = rc.getThirdConsultationDate().trim() + " "
					+ rc.getThirdConsultationTime().trim()+":00";
		} else if (StringUtils.isNotBlank(rc
				.getSecondConsultationDate())) {
			time = rc.getSecondConsultationDate().trim() + " "
					+ rc.getSecondConsultationTime().trim()+":00";
		} else {
			time = rc.getConsultationDate().trim() + " "
					+ rc.getConsultationTime().trim()+":00";
		}
		return time;
	}

	@RequestMapping(value = "/gainVeryCode", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainVeryCode(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String telphone = request.getParameter("telphone");
		String code = CheckNumUtil.randomChars(4);
		String content = " 验证码为" + code + "，佰医汇提醒您，您正在绑定微信公众平台，请勿泄露给第三人！【佰医汇】";
		log.info("code===" + code + " content===" + content);
		String ret = HttpSendSmsUtil.sendSmsInteface(telphone, content);
		if (ret.equalsIgnoreCase("100")) {
			map.put("status", "success");
			request.getSession().setAttribute("bindCode", telphone + "" + code);
			request.getSession().setAttribute("verifyCodeTime",
					code_time.format(new Date()));
		} else {
			map.put("status", "error");
		}
		return map;
	}

	@RequestMapping(value = "/tellCode", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> tellCode(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String code = request.getParameter("code");
		String tel = request.getParameter("tel");
		String openid = request.getParameter("openid");
		String bindCode = (String) request.getSession()
				.getAttribute("bindCode");
		String localVerifyCodeTime = (String) request.getSession()
				.getAttribute("verifyCodeTime");
		if (StringUtils.isNotBlank(bindCode)
				&& StringUtils.isNotBlank(localVerifyCodeTime)) {
			String now = code_time.format(new Date());
			long second = DateUtil.calculartorDateSecond(localVerifyCodeTime,
					now);
			if (second > 300) {
				// 超过五分钟,失效
				map.put("status", "error");
			} else {
				if ((tel + "" + code).equalsIgnoreCase(bindCode)) {
					JSONObject jo = CommonUtil.GetPersonInfo(weixinService
							.queryWeiAccessTokenById(PropertiesUtil.getString("APPID")).getAccessToken(),
							openid);
					map.put("status", "success");
					UserAccountInfo u=weixinService.queryUserAccountInfoByMobilePhone(tel);
					Integer uid = null;
					if (u == null) {
						u = new UserAccountInfo();
						u.setLoginName(tel);
						u.setMobileNumber(tel);
						u.setRegisterTime(new Date());
						u.setOrigin("weixin");
						u.setStatus("1");
						uid=weixinService.saveUserAccountInfo(u);
					} else {
						uid = u.getId();
					}
					
					/*UserExternals ue = weixinService
							.queryUserExternalByExternalId(openid);*/
					UserWeixinRelative ue=weixinService.queryUserWeiRelativeByOpenId(openid);
					if (ue == null) {
						String disname =jo.containsKey("nickname")? EmojiFilterUtil.filterEmoji(jo
								.getString("nickname")):"匿名用户";
						String sex=jo.containsKey("sex")?jo.getString("sex"):"0";
						ue = new UserWeixinRelative();
						ue.setUserId(uid);
						//ue.setSex(sex);
						ue.setDisplayName(disname);
						ue.setOpenId(openid);

						ue.setHeadImageUrl(jo.containsKey("headimgurl")?jo.getString("headimgurl"):"");
						weixinService.saveUserWeixinRelative(ue);
					}
				} else {
					map.put("status", "error");
				}
			}
		} else {
			map.put("status", "error");
		}
		return map;
	}

	private String parseSmsRet(String result) throws Exception {
		log.info("短信返回===" + result);
		Document doc = DocumentHelper.parseText(result);
		Element rootElt = doc.getRootElement();
		String sta = rootElt.elementText("State");
		return sta;
	}

	private String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}

	private String gainOpenIdByConditions(HttpServletRequest request,
			String openid) {
		if (!StringUtils.isNotBlank(openid)) {
			String code = request.getParameter("code");
			if (StringUtils.isNotBlank(code)) {
				// 根据code获取openid
				JSONObject jsonObject = gainJsonObject(code);
				if (jsonObject != null) {
					if (jsonObject.containsKey("openid")) {
						openid = jsonObject.getString("openid");
						request.getSession().setAttribute("cuopenid", openid);
					} else {
						openid = (String) request.getSession().getAttribute(
								"cuopenid");
					}
				}
			}
		}
		return openid;
	}

	private JSONObject gainJsonObject(String code) {
		String appid = PropertiesUtil.getString("APPID");
		String appsecret = PropertiesUtil.getString("APPSECRET");
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ appid + "&secret=" + appsecret + "&code=" + code
				+ "&grant_type=authorization_code";
		JSONObject jsonObject = CommonUtil.httpRequest(url, "GET", null);
		return jsonObject;
	}

	private Map<String, Object> signMap(StringBuffer homeUrl, String queryString)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(queryString)) {
			homeUrl.append("?").append(queryString);
		}
		System.out.println(homeUrl.toString());
		long timestamp = System.currentTimeMillis() / 1000;
		System.out.println("===timestamp==" + timestamp);
		String nonceStr = UUID.randomUUID().toString();
		System.out.println("===nonceStr==" + nonceStr);
		System.out.println("====ticket===" + TokenThread.jsapi_ticket);
		String signature = SignUtil.getSignature(TokenThread.jsapi_ticket,
				nonceStr, timestamp, homeUrl.toString());
		map.put("appid", PropertiesUtil.getString("APPID"));
		map.put("timestamp", timestamp);
		map.put("nonceStr", nonceStr);
		map.put("signature", signature);
		return map;
	}

	// ============================关于我们
	@RequestMapping(value = "/aboutus", method = RequestMethod.GET)
	public ModelAndView aboutus(HttpServletRequest request) {
		return new ModelAndView("zjh/about_us");
	}

	// ============================常见问题
	@RequestMapping(value = "/qas", method = RequestMethod.GET)
	public ModelAndView fqas(HttpServletRequest request) {
		return new ModelAndView("zjh/qas");
	}

	// ============================用户反馈
	@RequestMapping(value = "/feedbacks", method = RequestMethod.GET)
	public ModelAndView feedbacks(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = gainOpenIdByConditions(request, "");
		map.put("openid", openid);
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		return new ModelAndView("zjh/feedbacks", map);
	}

	// =============================用户反馈入库
	@RequestMapping(value = "/saveFeedback", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveFeedback(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserFeedback fb = new UserFeedback();
		String content = request.getParameter("content");
		String openid = request.getParameter("openid");
		fb.setContent(content);
		fb.setOpenid(openid);
		fb.setSource(1);
		fb.setUserType(1);
		fb.setCreateTime(new Date());
		weixinService.saveUserFeedback(fb);
		return map;
	}

	// ============================下载app
	@RequestMapping(value = "/gainApps", method = RequestMethod.GET)
	public ModelAndView gainApps(HttpServletRequest request) {
		return new ModelAndView("zjh/getapps");
	}

	// ============================我的订单
	@RequestMapping(value = "/myorders", method = RequestMethod.GET)
	public ModelAndView myorders(HttpServletRequest request)throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		if (!StringUtils.isNotBlank(openid)) {
			// 菜单进入
			openid = gainOpenIdByConditions(request, "");
		}
		// 查询订单
		List<RemoteConsultation> orders = weixinService
				.queryRemoteConsultationsByConditions(openid, 1, 5, "1,2,3,4,5");// 进行中订单// 4--二次下的单，5==三次下的单
		map.put("openid", openid);
		map.put("orders", orders);
		map.put("flag", "processing");
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		return new ModelAndView("zjh/myorders", map);
	}

	// ============================已完成订单
	@RequestMapping(value = "/mycompletedorders", method = RequestMethod.GET)
	public ModelAndView mycompletedorders(HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		// 查询订单
		List<RemoteConsultation> orders = weixinService
				.queryRemoteConsultationsByConditions(openid, 1, 1, "10");// 已完成订单
		map.put("openid", openid);
		map.put("orders", orders);
		map.put("flag", "complete");
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		return new ModelAndView("zjh/mycompleted_orders", map);
	}

	// ============================已取消订单
	@RequestMapping(value = "/mycancelorders", method = RequestMethod.GET)
	public ModelAndView mycancelorders(HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		// 查询订单
		List<RemoteConsultation> orders = weixinService
				.queryRemoteConsultationsByConditions(openid, 1, 5, "20,21");// 已取消订单
		map.put("openid", openid);
		map.put("orders", orders);
		map.put("flag", "cancel");
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		return new ModelAndView("zjh/mycancel_orders", map);
	}

	// =============================更多订单
	@RequestMapping(value = "/moreorders", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> moreorders(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
		String flag = request.getParameter("flag");
		List<RemoteConsultation> orders = weixinService
				.queryRemoteConsultationsByConditions(openid, pageNo, 5,
						retInclude(flag));
		map.put("orders", orders);
		return map;
	}

	@RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
	@ResponseBody
	Map<String, Object> canCelOrder(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		RemoteConsultation rc = weixinService.queryRemoteConsultationById(oid);
		rc.setRefreshTime(new Date());
		rc.setStatus(20);// 用户取消
		weixinService.updateRemoteConsultation(rc);
		if (StringUtils.isNotBlank(rc.getOpenId())
				&& StringUtils.isNotBlank(rc.getOutTradeNo())) {
			String finalmoney = rc.getAmount() + "";
			finalmoney = changeY2F(finalmoney);
			String out_refund_no = "YW" + _sdf.format(new Date());
			WeixinUtil.refund(PropertiesUtil.getString("APPID"),
					PropertiesUtil.getString("PARTNER"), rc.getOutTradeNo(),
					out_refund_no, finalmoney + "", finalmoney + "",
					PropertiesUtil.getString("PARTNER"),
					PropertiesUtil.getString("cert_path"));
		}
		return map;
	}

	public static String changeY2F(String amount) {
		String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥//
		// 或者$的金额
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(
					".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(
					".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(
					".", "") + "00");
		}
		return amLong.toString();
	}

	// ==============================二次或三次就诊标记
	@RequestMapping(value = "/consulagain", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> consulagin(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		RemoteConsultation rc = weixinService.queryRemoteConsultationById(oid);
		rc.setRefreshTime(new Date());
		rc.setStatus(rc.getStatus() + 1);
		weixinService.updateRemoteConsultation(rc);
		return map;
	}

	private String retInclude(String flag) {
		String stats = "";
		if (flag.equalsIgnoreCase("processing")) {
			// 进行中订单
			stats = "1,2,3,4,5";
		} else if (flag.equalsIgnoreCase("complete")) {
			// 已完成订单
			stats = "10";
		} else if (flag.equalsIgnoreCase("cancel")) {
			// 已取消订单
			stats = "20,21";
		}
		return stats;
	}

	// 图文会诊
	@RequestMapping(value = "/graphicCon", method = RequestMethod.GET)
	public ModelAndView graphicCon(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		map.put("openid", openid);
		return new ModelAndView("zjh/graphic_con", map);
	}

	// 上传图片
	@RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> uploadPic(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String _dir = _sdf.format(new Date());
		String dirpath = dir_time.format(new Date()) + "/" + _dir;
		Map<String, Object> ret_map = OSSManageUtil.uploadFile_in(dirpath,
				new OSSConfigure(), request);
		CustomUpload cu = new CustomUpload(null,
				(String) ret_map.get("filename"),
				(String) ret_map.get("urlpath"), sdf.format(new Date()));
		Integer cuid = commonService.saveCustomUpload(cu);
		map.put("upid", cuid);
		return map;
	}

	// 点击支付
	@RequestMapping(value = "/payGraphics", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> payGraphics(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		Float money = 0.1f;
		String out_trade_no = "";
		Map<String, Object> retMap /* WeixinUtil.weipay(request, response,
				money, PropertiesUtil.getString("APPID"),
				PropertiesUtil.getString("APPSECRET"),
				PropertiesUtil.getString("PARTNER"),
				PropertiesUtil.getString("PARTNERKEY"), "图文问诊",
				openid,// "oLAmZt5uvYKF9OTZVPC4pXQhUxHc",
				PropertiesUtil.getString("DOMAINURL") + "wzjh/graphicsnotify",
				null);*/=null;
		out_trade_no = (String) retMap.get("out_trade_no");
		generateGraphicsOrder(request, out_trade_no, money);

		map.put("out_trade_no", out_trade_no);
		map.put("appid", retMap.get("appid"));
		map.put("timeStamp", retMap.get("timeStamp"));
		map.put("nonceStr", retMap.get("nonceStr"));
		map.put("package", retMap.get("package"));
		map.put("sign", retMap.get("sign"));
		return map;
	}

	private void generateGraphicsOrder(HttpServletRequest request,
			String outTradeNo, Float money) {
		GraphicsInfo ginfo = new GraphicsInfo();
		ginfo.setOpenId(request.getParameter("openid"));
		ginfo.setUserName(request.getParameter("userName"));
		ginfo.setSex(request.getParameter("sex"));
		if (StringUtils.isNotBlank(request.getParameter("age"))) {
			ginfo.setAge(Integer.parseInt(request.getParameter("age")));
		}
		ginfo.setTelphone(request.getParameter("telphone"));
		ginfo.setOutTradeNo(outTradeNo);
		;
		ginfo.setPayMoney(money);
		ginfo.setSickInfo(request.getParameter("sickInfo"));
		ginfo.setRelaImages(request.getParameter("pics"));
		ginfo.setCreateTime(sdf.format(new Date()));
		ginfo.setRefreshTime(sdf.format(new Date()));
		ginfo.setStatus(0);
		weixinService.saveGraphicsInfo(ginfo);
	}

	// 通知
	// 购买电话预约订单通知
	@RequestMapping(value = "/graphicsnotify", method = RequestMethod.POST)
	public void graphicsnotify(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("=====图文问诊订单=====zhifutongzhi:");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		System.out.println("~~~~~~~~~~~~~~~~付款成功~~~~~~~~~");
		outSteam.close();
		inStream.close();
		String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
		log.info("===购买图文问诊ret=====" + result);
		// 修改订单状态---已支付
		updateGraphicsNotifyOrder(result);
		response.getWriter().write(setXML("SUCCESS", ""));
	}

	private void updateGraphicsNotifyOrder(String result) {
		try {
			Document doc = DocumentHelper.parseText(result);
			Element rootElt = doc.getRootElement(); // 获取根节点
			String tradeNo = rootElt.elementText("out_trade_no");
			GraphicsInfo ginfo = weixinService
					.queryGraphicsInfoByTradeNo(tradeNo);
			// 更新服务订单状态
			if (ginfo.getStatus() != null && ginfo.getStatus().equals(0)) {
				ginfo.setStatus(1);
				ginfo.setRefreshTime(sdf.format(new Date()));
				weixinService.updateGraphicsInfo(ginfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 加号就诊(null 专家会诊，2--开通加号)
	@RequestMapping(value = "/faceplus", method = RequestMethod.GET)
	public ModelAndView faceplus(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer _depid = null;
		String openid = request.getParameter("openid");
		String depid = request.getParameter("depid");
		String scontent = request.getParameter("scontent");// 检索
		String scity = request.getParameter("scity");// 城市过滤
		String spro = request.getParameter("spro");// 职称过滤
		map.put("scontent", scontent);
		map.put("scity", scity);
		map.put("spro", spro);
		if (StringUtils.isNotBlank(depid))
			_depid = Integer.parseInt(depid);
		List<MobileSpecial> specials = weixinService
				.queryMobileSpecialsByConditions(_depid, scontent, scity, spro,
						1, 10, "2");
		map.put("specials", specials);
		map.put("openid", openid);
		return new ModelAndView("zjh/face_plus", map);
	}

	// 加号--专家详情
	@RequestMapping(value = "/plusexpertdetail", method = RequestMethod.GET)
	public ModelAndView plusexpertdetail(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		Integer specialid = Integer.parseInt(request.getParameter("sid"));
		MobileSpecial special = weixinService.queryMobileSpecialById(specialid);
		map.put("special", special);
		map.put("openid", openid);
		map.put("sid", specialid);
		return new ModelAndView("zjh/face_plus_detail", map);
	}

	@RequestMapping(value = "/gainplustime", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainplustime(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer sid = Integer.parseInt(request.getParameter("sid"));
		String dtime = request.getParameter("dtime");
		MobileSpecial special = weixinService.queryMobileSpecialById(sid);
		Integer count = weixinService.queryCountByPlused(sid, dtime);
		if (special.getAddNumCount() != null) {
			if (count < special.getAddNumCount()) {
				List<SpecialistAppoint> sas = weixinService
						.querySpecialistAppointsBySid(sid, dtime);
				map.put("sas", sas);
			} else {
				map.put("status", "enough");
			}
		}
		return map;
	}

	// 选择日期之后进入信息填写
	@RequestMapping(value = "/basicconfirm")
	public ModelAndView basicconfirm(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String appiontId = request.getParameter("appiontId");// 加号时间id
		String openid = request.getParameter("openid");
		String sid = request.getParameter("sid");
		map.put("sid", sid);
		UserExternals ue = weixinService.queryUserExternalByExternalId(openid);
		String redirect = "";
		map.put("appointId", appiontId);// 预约时间id
		map.put("openid", openid);
		if (ue != null) {
			List<ContactInfo> conInfos = weixinService
					.queryContactInfosByOpenId(openid);
			map.put("conInfos", conInfos);
			List<Cases> cases = weixinService.queryCasesListByUserId(ue
					.getUserId());
			map.put("cases", cases);
			redirect = "zjh/basic_confirm";
		} else {
			map.put("stype", "plus");
			redirect = "zjh/bind_tel";
		}
		return new ModelAndView(redirect, map);
	}

	// 确认加号--生成预支付订单
	@RequestMapping(value = "/sureplus", method = RequestMethod.POST)
	public ModelAndView sureplus(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 预生成订单
		String openid = request.getParameter("openid");
		Integer appointId = Integer.parseInt(request.getParameter("appointId"));
		Integer sid = Integer.parseInt(request.getParameter("sid"));
		MobileSpecial special = weixinService.queryMobileSpecialById(sid);
		StringBuilder sb = new StringBuilder();
		// 获取科室
		List<DepartString> list = weixinService.queryDepartStrings(special
				.getSpecialId());
		for (DepartString _ds : list) {
			sb.append(_ds.getDepName() + " ");
		}
		special.setDepName(sb.toString());
		map.put("special", special);
		SpecialistAppoint sa = weixinService
				.querySpecialistAppointById(appointId);
		Integer faceid = generateFaceDiagnOrder(request, appointId);
		map.put("openid", openid);
		map.put("orderid", faceid);
		map.put("appointId", appointId);
		map.put("sa", sa);
		return new ModelAndView("zjh/topay_plus", map);
	}

	// 异步获取历史病历
	@RequestMapping(value = "/hiscases", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> hiscases(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		UserExternals ue = weixinService.queryUserExternalByExternalId(openid);
		List<Cases> cases = weixinService
				.queryCasesListByUserId(ue.getUserId());
		map.put("cases", cases);
		return map;
	}

	@RequestMapping(value = "/loadsinglecase", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> loadsinglecase(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer caseid = Integer.parseInt(request.getParameter("caseid"));
		Cases cases = weixinService.queryCasesById(caseid);
		map.put("cases", cases);
		List<CasesImage> images = weixinService.queryCaseImagesByCaseId(caseid);
		for (CasesImage _img : images) {
			CustomUpload cu = weixinService.queryCustomUploadByUrl(_img
					.getUrl());
			_img.setId(cu.getId());
		}
		map.put("images", images);
		return map;
	}

	private Integer generateFaceDiagnOrder(HttpServletRequest request,
			Integer appiontId) {
		String openid = request.getParameter("openid");
		SpecialistAppoint sa = weixinService
				.querySpecialistAppointById(appiontId);
		Integer sid = Integer.parseInt(request.getParameter("sid"));
		UUID uuid = UUID.randomUUID();
		String out_trade_no = uuid.toString().replace("-", "");
		UserExternals ue = weixinService.queryUserExternalByExternalId(openid);
		// 新增cases
		Cases ca = new Cases();
		ca.setKeywords(request.getParameter("keywords"));// 病情关键词
		ca.setAge(null);
		ca.setSex(null);
		ca.setDesc(request.getParameter("desc"));// 病情描述
		ca.setPatientName(request.getParameter("username"));// 用户名
		ca.setPatientId(ue.getUserId());
		ca.setPhone(request.getParameter("telphone"));// 电话
		ca.setIdNumber(request.getParameter("idcard"));// 身份证
		ca.setFamilyHistory(request.getParameter("familyHistory"));// 家族史
		ca.setAskProblem(request.getParameter("askProblem"));// 想问的问题
		Integer caseid = weixinService.saveCases(ca);
		// 存储case imags
		String pics = request.getParameter("pics");
		if (StringUtils.isNotBlank(pics)) {
			String[] _pics = pics.split(",");
			for (String _pic : _pics) {
				CustomUpload cu = commonService.queryCustomUpload(Integer
						.parseInt(_pic));
				CasesImage ci = new CasesImage();
				ci.setCaseId(caseid);
				ci.setUrl(cu.getUrlPath());
				weixinService.saveCaseImage(ci);
			}
		}
		// 存储 orders
		Orders ord = new Orders();
		ord.setAmount(sa.getAmount());
		ord.setStatus(0);
		ord.setCreateTime(gainlongtime());
		Integer orid = weixinService.saveOrders(ord);
		// 新增主表数据 FaceDiagnoses
		FaceDiagnoses fd = new FaceDiagnoses();
		fd.setPatientId(ue.getUserId());
		fd.setSpecialistId(sid);
		fd.setCode(out_trade_no);
		fd.setStatus(0);
		fd.setCreateTime(gainlongtime());
		fd.setOrigin("weixin");
		fd.setOrderId(orid);
		fd.setCaseId(caseid);
		Integer fid = weixinService.saveFaceDiagnoses(fd);
		return fid;
	}

	@RequestMapping(value = "/surepayplus", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> surepayplus(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		Integer fid = Integer.parseInt(request.getParameter("orderid"));
		FaceDiagnoses fd = weixinService.queryFaceDiagnosesById(fid);
		Orders ord = weixinService.queryOrdersById(fd.getOrderId());
		if (fd.getStatus().equals(0)) {
			Map<String, Object> retMap = /*WeixinUtil.weipay(request, response,
					ord.getAmount().floatValue(),
					PropertiesUtil.getString("APPID"),
					PropertiesUtil.getString("APPSECRET"),
					PropertiesUtil.getString("PARTNER"),
					PropertiesUtil.getString("PARTNERKEY"), "加号面诊",
					openid,// "oLAmZt5uvYKF9OTZVPC4pXQhUxHc",
					PropertiesUtil.getString("DOMAINURL") + "wzjh/plusnotify",
					fd.getCode())*/null;
			map.put("appid", retMap.get("appid"));
			map.put("timeStamp", retMap.get("timeStamp"));
			map.put("nonceStr", retMap.get("nonceStr"));
			map.put("package", retMap.get("package"));
			map.put("sign", retMap.get("sign"));
			map.put("status", "success");
		} else {
			map.put("status", "error");
			map.put("message", "已支付过");
		}
		return map;
	}

	@RequestMapping(value = "/plusnotify", method = RequestMethod.POST)
	public void plusnotify(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("=====加号面诊订单=====zhifutongzhi:");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		System.out.println("~~~~~~~~~~~~~~~~付款成功~~~~~~~~~");
		outSteam.close();
		inStream.close();
		String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
		log.info("===购买加号面诊ret=====" + result);
		// 修改订单状态---已支付
		updatePlusNotifyOrder(result);
		response.getWriter().write(setXML("SUCCESS", ""));
	}

	private void updatePlusNotifyOrder(String result) {
		try {
			Document doc = DocumentHelper.parseText(result);
			Element rootElt = doc.getRootElement(); // 获取根节点
			String tradeNo = rootElt.elementText("out_trade_no");
			FaceDiagnoses fd = weixinService
					.queryFaceDiagnosesByTradeNo(tradeNo);
			Orders ord = weixinService.queryOrdersById(fd.getOrderId());
			// 更新服务订单状态
			if (fd.getStatus() != null && fd.getStatus().equals(0)) {
				fd.setStatus(1);
				weixinService.updateFaceDiagnoses(fd);

				ord.setStatus(1);
				weixinService.updateOrders(ord);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Long gainlongtime() {
		String time = System.currentTimeMillis() + "";
		time = time.substring(0, time.length() - 3);
		return Long.valueOf(time);
	}

	// *********************************************便民服务模块**************************************
	// 1主列表界面
	@RequestMapping(value = "/convenienceServices", method = RequestMethod.GET)
	public ModelAndView convenienceServices(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		map.put("openid", openid);
		return new ModelAndView("zjh/services/main", map);
	}

	// 2预约挂号
	@RequestMapping(value = "convenienceAppoint", method = RequestMethod.GET)
	public ModelAndView convenienceAppoint(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		map.put("openid", openid);
		return new ModelAndView("zjh/services/appoint", map);
	}

	// 3查看报告单
	@RequestMapping(value = "/checkrecords", method = RequestMethod.GET)
	public ModelAndView checkrecords(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		map.put("openid", openid);
		return new ModelAndView("zjh/services/records", map);
	}

	// ************************************************微信公众号改版++++++++++++++++++++++++++++++++++++++
	// 进入我要预约
	@RequestMapping(value = "/iwantappoint", method = RequestMethod.GET)
	public ModelAndView iwantappoint(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 合作医院id
		Integer cooHosid = Integer.parseInt(request.getParameter("cooHosId"));
		HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(cooHosid);
		List<HospitalDepartmentInfo> localDeparts=weixinService.queryCoohospitalDepartmentsByCooHos(cooHosid);
		List<MobileSpecial> specials=null;
		String openid = request.getParameter("openid");
		String orderid=request.getParameter("orderid");
		Integer depid=null;
		Integer expertId=null;
		if(StringUtils.isNotBlank(orderid)){
			//二次或三次下单
			RemoteConsultation order=weixinService.queryRemoteConsultationById(Integer.parseInt(orderid));
			depid=order.getLocalDepartId();
			expertId=order.getExpertId();
			//specials = weixinService
					//.queryMobileSpecialsByLocalDepartId(order.getLocalDepartId());
		}else{
			//specials = weixinService
					//	.queryMobileSpecialsByLocalDepartId(localDeparts.get(0).getId());
		}
		map.put("hos", hos);
		map.put("depid", depid);
		map.put("expertId", expertId);
		map.put("localDeparts", localDeparts);
		map.put("specials", specials);
		map.put("openid", openid);
		return new ModelAndView("zjh/2.0/appoint", map);
	}

	// 根据所选科室查询相关推荐专家
	@RequestMapping(value = "/gainRecommondSpecials", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainRecommondSpecials(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 科室id
		Integer departId = Integer.parseInt(request.getParameter("departId"));
		List<MobileSpecial> specials = weixinService
				.queryMobileSpecialsByLocalDepartId_new(departId,4);
		map.put("specials", specials);
		System.out.println("===科室Id==="+departId);
		return map;
	}

	// 加载专家的就诊日期
	@RequestMapping(value = "/gainSpecialTimes", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainSpecialTimes(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer sid = Integer.parseInt(request.getParameter("sid"));// 专家id
		String sdate = request.getParameter("sdate");
		System.out.println(sdate);
		List<ExpertConsultationSchedule> times = weixinService
				.queryExpertConTimeSchedulsByConditions(sid, sdate);
		map.put("times", times);
		System.out.println(JSONArray.fromObject(times).toString());
		return map;
	}

	// 确认进入，补充信息
	@RequestMapping(value = "/suretoconfirm", method = RequestMethod.POST)
	public ModelAndView suretoconfirm(HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		Integer sid = Integer.parseInt(request.getParameter("sid"));// 专家id
		Integer departId = Integer.parseInt(request.getParameter("departId"));// 当地医院科室id
		Integer stimeid = Integer.parseInt(request.getParameter("stimeid"));// 选择的时间id
		String openid = request.getParameter("openid");
		String orderid=request.getParameter("orderid");//多次下单的订单id
		// 获取预约时间
		ExpertConsultationSchedule timesch = weixinService
				.queryExpertConScheduleById(stimeid);
		map.put("timesch", timesch);
		// 就诊医院
		CooHospitalDepartments depart = weixinService
				.queryCooHospitalDepartmentsById(departId);
		CooHospitalDetails hospital = weixinService
				.queryCooHosPitalDetailsById(depart.getCooHospitalId());
		map.put("depart", depart);
		map.put("hospital", hospital);
		MobileSpecial special = weixinService.queryMobileSpecialById(sid);
		map.put("special", special);
		UserExternals ue = weixinService.queryUserExternalByExternalId(openid);
		if (StringUtils.isNotBlank(orderid)) {
			// 二次或三次下单
			RemoteConsultation rc = weixinService
					.queryRemoteConsultationById(Integer.parseInt(orderid));
			ExpertConsultationSchedule ecs=weixinService.queryExpertConScheduleById(stimeid);
			String condate=ecs.getScheduleDate();
			String contime=ecs.getStartTime();
			if (rc.getStatus().equals(2)) {
				rc.setSecondRefreshTime(new Date());
				rc.setSecondConsultationDate(condate);
				rc.setSecondConsultationTime(contime);
				rc.setStatus(4);// 第二次下单
			} else if (rc.getStatus().equals(3)) {
				rc.setThirdRefreshTime(new Date());
				rc.setThirdConsultationDate(condate);
				rc.setThirdConsultationTime(contime);
				rc.setStatus(5);
			}
			rc.setProgressTag(null);
			weixinService.updateRemoteConsultation(rc);
			List<RemoteConsultation> orders = weixinService
					.queryRemoteConsultationsByConditions(openid, 1, 5,
							"1,2,3,4,5");// 进行中订单 4--二次下的单，5==三次下的单
			map.put("openid", openid);
			map.put("orders", orders);
			map.put("flag", "processing");
			Map<String, Object> ret = signMap(request.getRequestURL(),
					request.getQueryString());
			map.put("appid", ret.get("appid"));
			map.put("timestamp", ret.get("timestamp"));
			map.put("nonceStr", ret.get("nonceStr"));
			map.put("signature", ret.get("signature"));
			return new ModelAndView("zjh/myorders", map);
		}
		String redirect = "";
		if (ue != null) {
			Users u = weixinService.queryUsersById(ue.getUserId());
			if (u != null && StringUtils.isNotBlank(u.getMobileNumber())) {
				map.put("f_tel", u.getMobileNumber());
				// 常用联系人
				/*List<ContactInfo> conInfos = weixinService
						.queryContactInfosByOpenId(openid);*/
				List<UserContactInfo> conInfos=weixinService.queryUserContactInfosByOpenId(openid);
				map.put("conInfos", conInfos);
				redirect = "zjh/2.0/confirm";
			}else{
				List<ContactInfo> conInfos = weixinService
						.queryContactInfosByOpenId(openid);
				map.put("conInfos", conInfos);
				redirect = "zjh/2.0/confirm";
			}
		} else {
			redirect = "zjh/bind_tel";
		}
		
		map.put("sid",sid);
		map.put("departId",departId);
		map.put("stimeid",stimeid);
		map.put("openid",openid);
		return new ModelAndView(redirect, map);
	}

	// 提交订单，微信支付
	@RequestMapping(value = "/surepay", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> surepay(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		Float money = 0.1f;
		String out_trade_no = "";
		Map<String, Object> retMap = /*WeixinUtil.weipay(request, response,
				money, PropertiesUtil.getString("APPID"),
				PropertiesUtil.getString("APPSECRET"),
				PropertiesUtil.getString("PARTNER"),
				PropertiesUtil.getString("PARTNERKEY"), "远程会诊",
				openid,// "oLAmZt5uvYKF9OTZVPC4pXQhUxHc",
				PropertiesUtil.getString("DOMAINURL") + "wzjh/remotenotify",
				null)*/null;
		out_trade_no = (String) retMap.get("out_trade_no");
		generateRemoteOrder(request, out_trade_no, money);
		map.put("out_trade_no", out_trade_no);
		map.put("appid", retMap.get("appid"));
		map.put("timeStamp", retMap.get("timeStamp"));
		map.put("nonceStr", retMap.get("nonceStr"));
		map.put("package", retMap.get("package"));
		map.put("sign", retMap.get("sign"));
		return map;
	}

	//预先生成订单
	private void generateRemoteOrder(HttpServletRequest request,
			String outTradeNo, Float money) {
		RemoteConsultation rc = new RemoteConsultation();
		String stimeid = request.getParameter("stimeid");
		if(StringUtils.isNotBlank(stimeid)){
			rc.setSchedulerDateId(Integer.parseInt(stimeid));
			ExpertConsultationSchedule ecs=weixinService.queryExpertConScheduleById(Integer.parseInt(stimeid));
			rc.setConsultationDate(ecs.getScheduleDate());
			rc.setConsultationTime(ecs.getStartTime());
		}
		String condate=request.getParameter("condate");
		String contime=request.getParameter("contime");
		if(StringUtils.isNotBlank(condate)&&StringUtils.isNotBlank(contime)){
			rc.setConsultationDate(condate);
			rc.setConsultationTime(contime);
		}
		String openid = request.getParameter("openid");
		rc.setExpertId(Integer.parseInt(request.getParameter("expertId")));
		rc.setLocalHospitalId(Integer.parseInt(request
				.getParameter("localHosId")));
		rc.setLocalDepartId(Integer.parseInt(request
				.getParameter("localDepartId")));
		rc.setCreateTime(new Date());
		rc.setRefreshTime(new Date());
		
		rc.setConsultationDur(15);
		String username = request.getParameter("username");
		String telphone = request.getParameter("telphone");
		rc.setPatientName(username);
		rc.setTelephone(telphone);
		String idcard = request.getParameter("idcard");
		rc.setIdCard(idcard);
		String ssex = IdcardUtils.getGenderByIdCard(idcard);
		rc.setSex(ssex.equalsIgnoreCase("M") ? 1
				: (ssex.equalsIgnoreCase("F") ? 2 : 0));
		//rc.setAmount(0.1f);// ===================================待获取
		rc.setPayMode(1);// ======================================1 微信支付
		rc.setOpenId(openid);
		rc.setOutTradeNo(outTradeNo);
		rc.setStatus(0);
		rc.setLocalPlusNo("123");
		weixinService.saveRemoteConsultation(rc);
		boolean exist = weixinService.isExistUserContactInfo(openid, username,
				idcard, telphone);
		if (!exist) {
			UserContactInfo ci=new UserContactInfo();
			ci.setIdCard(idcard);
			ci.setOpenId(openid);
			ci.setTelphone(telphone);
			ci.setContactName(username);
			weixinService.saveUserContactInfo(ci);
		}
	}
	
	//我的病例
	@RequestMapping(value="/mycases",method=RequestMethod.GET)
	public ModelAndView mycases(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		openid = gainOpenIdByConditions(request, openid);
		map.put("openid", openid);
		//获取首页五条病例信息
		List<RemoteConsultation> cases=weixinService.queryRemoteConsultationsByConditions(openid, 1, 5, "1,2,3,4,5,10");//进行中，已完成
		map.put("cases", cases);
		return new ModelAndView("zjh/2.0/mycases", map);
	}
	
	//获取更多病例
	@RequestMapping(value="/morecases",method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> morecases(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		String openid=request.getParameter("openid");
		Integer pageNo=Integer.parseInt(request.getParameter("pageNo"));
		List<RemoteConsultation> cases=weixinService.queryRemoteConsultationsByConditions(openid, pageNo, 5, "1,2,3,4,5,10");//进行中，已完成
		map.put("cases", cases);	
		return map;
	}
	
	//进入病例详情
	@RequestMapping(value="/casedetail",method=RequestMethod.GET)
	public ModelAndView casedetail(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		RemoteConsultation ocase=weixinService.queryRemoteConsultationById(oid);
		MobileSpecial special =weixinService.queryMobileSpecialById(ocase.getExpertId());
		String conDate="";
		if(StringUtils.isNotBlank(ocase.getThirdConsultationDate())){
			conDate=ocase.getThirdConsultationDate()+" "+ocase.getThirdConsultationTime();
		}else if(StringUtils.isNotBlank(ocase.getSecondConsultationDate())){
			conDate=ocase.getSecondConsultationDate()+" "+ocase.getSecondConsultationTime();
		}else{
			conDate=ocase.getConsultationDate()+" "+ocase.getConsultationTime();
		}
		ocase.setConDate(conDate);
		//获取录制的视频信息
		List<VedioRelative> vedios=weixinService.queryVediosByOrderId(oid);
		map.put("vedios", vedios);
		map.put("special", special);
		map.put("ocase", ocase);
		map.put("oid", oid);
		return new ModelAndView("zjh/2.0/casedetail",map);
	}
	
	//在医生端同步的检验与pacs资料以及专家的结果需要在 公众号上患者能看到
	@RequestMapping(value="/gainOrderLisPacs",method=RequestMethod.GET)
	public ModelAndView gainOrderLisPacs(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer oid=Integer.parseInt(request.getParameter("orderid"));
		RemoteConsultation order=weixinService.queryRemoteConsultationById(oid);
		//专家结果
		
		map.put("order", order);
		return new ModelAndView("zjh/2.0/lis_pacs",map);
	}
	
	
}
