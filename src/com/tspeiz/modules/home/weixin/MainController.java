package com.tspeiz.modules.home.weixin;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
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

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.entity.ExpertConsultationSchedule;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.newrelease.AppraisementDoctorInfo;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDictionary;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDoctor;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
import com.tspeiz.modules.common.entity.newrelease.BusinessPayInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessTelOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenZhenInfo;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.Dictionary;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.UserAccountInfo;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;
import com.tspeiz.modules.common.entity.newrelease.UserFeedBackInfo;
import com.tspeiz.modules.common.entity.newrelease.UserWeixinRelative;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.common.thread.TokenThread;
import com.tspeiz.modules.manage.CommonManager;
import com.tspeiz.modules.manage.PayCallBackManager;
import com.tspeiz.modules.manage.PayOrderManager;
import com.tspeiz.modules.manage.WenzhenManager;
import com.tspeiz.modules.manage.WxMainManager;
import com.tspeiz.modules.util.CheckNumUtil;
import com.tspeiz.modules.util.DataCatchUtil;
import com.tspeiz.modules.util.DateUtil;
import com.tspeiz.modules.util.EmojiFilterUtil;
import com.tspeiz.modules.util.IdcardUtils;
import com.tspeiz.modules.util.LocationUtil;
import com.tspeiz.modules.util.PasswordUtil;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.common.StringRetUtil;
import com.tspeiz.modules.util.config.ReaderConfigUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.log.RecordLogUtil;
import com.tspeiz.modules.util.oss.OSSConfigure;
import com.tspeiz.modules.util.oss.OSSManageUtil;
import com.tspeiz.modules.util.weixin.CommonUtil;
import com.tspeiz.modules.util.weixin.SignUtil;
import com.tspeiz.modules.util.weixin.WeixinUtil;
/**
 * 佰医汇微信公众号主控制器
 * @author heyongb
 *
 */
@Controller
@RequestMapping("wzjh")
public class MainController {
	private Logger log = Logger.getLogger(MainController.class);
	private Logger payLog=Logger.getLogger("payMongoDB");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat code_time = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat _sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private SimpleDateFormat dir_time = new SimpleDateFormat("yyyyMMdd");
	@Resource
	private IWeixinService weixinService;
	@Resource
	private ICommonService commonService;
	@Resource
	private IWenzhenService wenzhenService;
	@Autowired
	private WxMainManager wxMainManager;
	@Autowired
	private WenzhenManager wenzhenManager;
	@Autowired
	private PayOrderManager payOrderManager;
	@Autowired
	private PayCallBackManager payCallBackManager;
	@Autowired
	private CommonManager commonManager;
	/**
	 * 主入口
	 * @param request
	 * @return zjh/main.jsp
	 * @throws Exception
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		openid = gainOpenIdByConditions(request, openid);
		map.put("openid", openid);
		// 合作机构
		List<HospitalDetailInfo> coohos = weixinService.queryCoohospitalInfos();
		map.put("coohos", coohos);
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		return new ModelAndView("zjh/main", map);
	}

	/**
	 * 获取专家出诊时间
	 * @param request
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * 改版获取专家出诊时间
	 * @param request
	 * @return
	 */
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

	/**
	 *  通知-远程订单支付回调通知
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/remotenotify", method = RequestMethod.POST)
	public void remotenotify(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
		payLog.info("{'ret':'"+result+"','desc':'远程订单回调返回值','time':'"+sdf.format(new Date())+"'}");
		// 修改订单状态---已支付
		payCallBackManager.updateRemoteNotifyOrder(result);
		
		response.getWriter().write(setXML("SUCCESS", ""));
	}

	private String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}

	private  String gainOpenIdByConditions(HttpServletRequest request,
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
	
	/**
	 *判定验证码并绑定公号
	 * @param request
	 * @return
	 */
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
					UserAccountInfo u = weixinService
							.queryUserAccountInfoByMobilePhone(tel);
					Integer uid = null;
					if (u == null) {
						u = new UserAccountInfo();
						u.setPasswordHashed(PasswordUtil.MD5Salt("123456"));
						u.setSalt("cvYl8U");
						u.setLoginName(tel);
						u.setMobileNumber(tel);
						u.setRegisterTime(new Date());
						u.setOrigin("weixin");
						u.setStatus("1");
						uid = weixinService.saveUserAccountInfo(u);
					} else {
						uid = u.getId();
					}
					UserWeixinRelative ue = weixinService
							.queryUserWeiRelativeByOpenId(openid);
					if (ue == null) {
						String disname = jo.containsKey("nickname") ? EmojiFilterUtil
								.filterEmoji(jo.getString("nickname")) : "匿名用户";
						String sex = jo.containsKey("sex") ? jo
								.getString("sex") : "0";
						ue = new UserWeixinRelative();
						ue.setAppId(PropertiesUtil.getString("APPID"));
						ue.setUserId(uid);
						ue.setSex(Integer.parseInt(sex));
						ue.setDisplayName(disname);
						ue.setOpenId(openid);
						ue.setHeadImageUrl(jo.containsKey("headimgurl") ? jo
								.getString("headimgurl") : "");
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
		WeiAccessToken wat=weixinService.queryWeiAccessTokenById(PropertiesUtil.getString("APPID"));
		System.out.println("====ticket===" + wat);
		String signature = SignUtil.getSignature(wat.getJsapiTicket(),
				nonceStr, timestamp, homeUrl.toString());
		map.put("appid", PropertiesUtil.getString("APPID"));
		map.put("timestamp", timestamp);
		map.put("nonceStr", nonceStr);
		map.put("signature", signature);
		return map;
	}

	/**
	 * 关于我们-菜单项
	 * @param request
	 * @return zjh/about_us.jsp
	 */
	@RequestMapping(value = "/aboutus", method = RequestMethod.GET)
	public ModelAndView aboutus(HttpServletRequest request) {
		return new ModelAndView("zjh/about_us");
	}

	/**
	 * 常见问题-菜单项
	 * @param request
	 * @return zjh/qas.jsp
	 */
	@RequestMapping(value = "/qas", method = RequestMethod.GET)
	public ModelAndView fqas(HttpServletRequest request) {
		return new ModelAndView("zjh/qas");
	}

	/**
	 * 用户反馈-菜单项
	 * @param request
	 * @return zjh/feedbacks.jsp
	 * @throws Exception
	 */
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

	/**
	 * 用户反馈保存
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveFeedback", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveFeedback(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserFeedBackInfo info = new UserFeedBackInfo();
		info.setContent(request.getParameter("content"));
		info.setOpenid(request.getParameter("openid"));
		info.setSource(1);
		info.setUserType(1);
		info.setCreateTime(new Date());
		weixinService.saveUserFeedbackInfo(info);
		return map;
	}

	/**
	 * 下载app-菜单项
	 * @param request
	 * @return zjh/getapps.jsp
	 */
	@RequestMapping(value = "/gainApps", method = RequestMethod.GET)
	public ModelAndView gainApps(HttpServletRequest request) {
		return new ModelAndView("zjh/getapps");
	}

	/**
	 * 取消订单
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
	@ResponseBody
	Map<String, Object> canCelOrder(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		RemoteConsultation rc = weixinService.queryRemoteConsultationById(oid);
		rc.setRefreshTime(new Date());
		rc.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey());// 用户取消
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

	private static String changeY2F(String amount) {
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

	/**
	 * 二次或三次就诊标记-弃用
	 * @param request
	 * @return
	 */
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

	/**
	 * 主列表界面
	 * @param request
	 * @return
	 *//*
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
	}*/

	
	/**
	 * 远程门诊-进入我要预约
	 * @param request
	 * @return zjh/2.0/appoint.jsp
	 */
	@RequestMapping(value = "/iwantappoint", method = RequestMethod.GET)
	public ModelAndView iwantappoint(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 合作医院id
		Integer cooHosid = Integer.parseInt(request.getParameter("cooHosId"));
		HospitalDetailInfo hos = weixinService
				.queryHospitalDetailInfoById(cooHosid);
		List<HospitalDepartmentInfo> localDeparts = weixinService
				.queryCoohospitalDepartmentsByCooHos(cooHosid);
		List<MobileSpecial> specials = null;
		String openid = request.getParameter("openid");
		String orderid = request.getParameter("orderid");
		Integer depid = null;
		Integer expertId = null;
		if (StringUtils.isNotBlank(orderid)) {
			// 二次或三次下单
			RemoteConsultation order = weixinService
					.queryRemoteConsultationById(Integer.parseInt(orderid));
			depid = order.getLocalDepartId();
			expertId = order.getExpertId();
		}
		map.put("hos", hos);
		map.put("depid", depid);
		map.put("expertId", expertId);
		map.put("localDeparts", localDeparts);
		map.put("specials", specials);
		map.put("openid", openid);
		return new ModelAndView("zjh/2.0/appoint", map);
	}

	/**
	 * 根据所选科室查询相关推荐专家
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gainRecommondSpecials", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainRecommondSpecials(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 科室id
		Integer departId = Integer.parseInt(request.getParameter("departId"));
		List<MobileSpecial> specials = weixinService
				.queryMobileSpecialsByLocalDepartId_new(departId,4);
		map.put("specials", specials);
		return map;
	}

	/**
	 * 加载专家的就诊日期
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gainSpecialTimes", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainSpecialTimes(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer sid = Integer.parseInt(request.getParameter("sid"));// 专家id
		String sdate = request.getParameter("sdate");
		List<ExpertConsultationSchedule> times = weixinService
				.queryExpertConTimeSchedulsByConditions(sid, sdate);
		map.put("times", times);
		return map;
	}

	/**
	 * 确认进入，补充信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/suretoconfirm", method = RequestMethod.POST)
	public ModelAndView suretoconfirm(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer sid = Integer.parseInt(request.getParameter("sid"));// 专家id
		Integer departId = Integer.parseInt(request.getParameter("departId"));// 当地医院科室id
		Integer stimeid = Integer.parseInt(request.getParameter("stimeid"));// 选择的时间id
		String openid = request.getParameter("openid");
		String orderid = request.getParameter("orderid");// 多次下单的订单id
		// 获取预约时间
		ExpertConsultationSchedule timesch = weixinService
				.queryExpertConScheduleById(stimeid);
		map.put("timesch", timesch);
		// 就诊医院
		HospitalDepartmentInfo depart = weixinService
				.queryHospitalDepartmentInfoById(departId);
		HospitalDetailInfo hospital = weixinService
				.queryHospitalDetailInfoById(depart.getHospitalId());
		map.put("depart", depart);
		map.put("hospital", hospital);
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(sid);
		special.setVedioAmount(commonManager.gainMoneyByOrder(4,special.getSpecialId()));
		map.put("special", special);
		UserWeixinRelative ur = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		if (StringUtils.isNotBlank(orderid)) {
			// 二次或三次下单
			RemoteConsultation rc = weixinService
					.queryRemoteConsultationById(Integer.parseInt(orderid));
			ExpertConsultationSchedule ecs = weixinService
					.queryExpertConScheduleById(stimeid);
			String condate = ecs.getScheduleDate();
			String contime = ecs.getStartTime();
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
		if (ur != null) {
			UserAccountInfo u = weixinService.queryUserAccountInfoById(ur
					.getUserId());
			// Users u = weixinService.queryUsersById(ue.getUserId());
			if (u != null && StringUtils.isNotBlank(u.getMobileNumber())) {
				map.put("f_tel", u.getMobileNumber());

			}
			// 常用联系人
			List<UserContactInfo> conInfos = weixinService
					.queryUserContactInfosByOpenId(openid);
			// List<UserContactInfo>
			// conInfos=weixinService.queryUserContactInfosByUserId(ur.getUserId());
			map.put("conInfos", conInfos);
			redirect = "zjh/2.0/confirm";
		} else {
			redirect = "zjh/bind_tel";
		}
		// String redirect = "zjh/2.0/confirm";
		map.put("sid", sid);
		map.put("departId", departId);
		map.put("stimeid", stimeid);
		map.put("openid", openid);
		return new ModelAndView(redirect, map);
	}

	/**
	 * 提交订单，微信支付
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/surepay", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> surepay(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		Float money = Float.parseFloat(request.getParameter("pmoney"));
		System.out.println(request.getParameter("username"));
		Map<String,Object> retMap=wxMainManager.processRemoteOrder(request, response,1, uwr.getUserId(), money);
		map.put("appid", retMap.get("appid"));
		map.put("timeStamp", retMap.get("timeStamp"));
		map.put("nonceStr", retMap.get("nonceStr"));
		map.put("package", retMap.get("package"));
		map.put("sign", retMap.get("sign"));
		return map;
	}


	/**
	 * 我的病例
	 * @param request
	 * @return zjh/2.0/mycases.jsp
	 */
	@RequestMapping(value = "/mycases", method = RequestMethod.GET)
	public ModelAndView mycases(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		openid = gainOpenIdByConditions(request, openid);
		map.put("openid", openid);
		// 获取首页五条病例信息
		List<RemoteConsultation> cases = weixinService
				.queryRemoteConsultationOrdersByConditions(openid, 1, 5,
						"1,2,3,4,5,10");// 进行中，已完成
		map.put("cases", cases);
		return new ModelAndView("zjh/2.0/mycases", map);
	}

	/**
	 * 获取更多病例
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/morecases", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> morecases(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
		List<RemoteConsultation> cases = weixinService
				.queryRemoteConsultationOrdersByConditions(openid, pageNo, 5,
						"1,2,3,4,5,10");// 进行中，已完成
		map.put("cases", cases);
		return map;
	}

	/**
	 * 进入病例详情
	 * @param request
	 * @return zjh/2.0/casedetail.jsp
	 */
	@RequestMapping(value = "/casedetail", method = RequestMethod.GET)
	public ModelAndView casedetail(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		RemoteConsultation ocase = weixinService
				.queryRemoteConsultationById(oid);
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(ocase.getExpertId());
		String conDate = "";
		if (StringUtils.isNotBlank(ocase.getThirdConsultationDate())) {
			conDate = ocase.getThirdConsultationDate() + " "
					+ ocase.getThirdConsultationTime();
		} else if (StringUtils.isNotBlank(ocase.getSecondConsultationDate())) {
			conDate = ocase.getSecondConsultationDate() + " "
					+ ocase.getSecondConsultationTime();
		} else {
			conDate = ocase.getConsultationDate() + " "
					+ ocase.getConsultationTime();
		}
		ocase.setConDate(conDate);
		// 获取录制的视频信息
		// List<VedioRelative> vedios = weixinService.queryVediosByOrderId(oid);
		List<CustomFileStorage> vedios = weixinService
				.queryCustomFileStorageVedios(oid);
		map.put("vedios", vedios);
		map.put("special", special);
		map.put("ocase", ocase);
		map.put("oid", oid);
		return new ModelAndView("zjh/2.0/casedetail", map);
	}

	/**
	 * 在医生端同步的检验与pacs资料以及专家的结果需要在 公众号上患者能看到
	 * @param request
	 * @return zjh/2.0/lis_pacs.jsp
	 */
	@RequestMapping(value = "/gainOrderLisPacs", method = RequestMethod.GET)
	public ModelAndView gainOrderLisPacs(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("orderid"));
		BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(oid);
		// 专家结果
		map.put("order", order);
		return new ModelAndView("zjh/2.0/lis_pacs", map);
	}

	/**
	 * 进入评价界面
	 * @param request
	 * @return zjh/3.0/evaluate.jsp
	 */
	@RequestMapping(value = "/toestimate", method = RequestMethod.GET)
	public ModelAndView toevaluate(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		String openid = request.getParameter("openid");
		String ltype = request.getParameter("ltype");
		// 获取标签
		List<AppraisementImpressionDictionary> dtags = wenzhenService
				.queryAppraisementImpressions();
		map.put("openid", openid);
		map.put("oid", oid);
		map.put("ltype", ltype);
		map.put("dtags", dtags);
		Integer docid = null;
		switch (ltype) {
		case "1":
			BusinessTuwenOrder tworder=wenzhenService.queryBusinessTuwenInfoById(oid);
			docid = tworder.getDoctorId();
			break;
		case "2":
			BusinessTelOrder telorder=wenzhenService.queryBusinessTelOrderById(oid);
			docid = telorder.getDoctorId();
			break;
		case "4":
			BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(oid);
			docid = order.getExpertId();
			break;
		}
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(docid);
		map.put("special", special);
		return new ModelAndView("zjh/3.0/evaluate", map);
	}

	/**
	 * 保存评价
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveestimate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveevalua(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 保存评价
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		Integer ltype = Integer.parseInt(request.getParameter("ltype"));// 类型
																		// 1图文，2电话
																		// 4远程
		String content = request.getParameter("content");// 评价内容
		Integer grade = Integer.parseInt(request.getParameter("grade"));// 星级
																		// 至少一颗心
		String openid = request.getParameter("openid");
		String tagids[] = request.getParameterValues("tagids");// 标签
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		Integer docid = null;
		String orderUuid="";
		switch (ltype) {
		case 1:
			BusinessTuwenOrder tworder=wenzhenService.queryBusinessTuwenInfoById(oid);
			docid = tworder.getDoctorId();
			orderUuid=tworder.getUuid();
			break;
		case 2:
			BusinessTelOrder telorder=wenzhenService.queryBusinessTelOrderById(oid);
			docid = telorder.getDoctorId();
			orderUuid=telorder.getUuid();
			break;
		case 4:
			BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(oid);
			docid = order.getExpertId();
			orderUuid=order.getUuid();
			break;
		}
		processInfoSave(uwr.getUserId(), orderUuid, ltype, docid, grade, content,
				tagids);
		return map;
	}

	private void processInfoSave(Integer uid, String orderUuid, Integer ltype,
			Integer docid, Integer grade, String content, String[] _tagids) {
		AppraisementDoctorInfo appraisementDoctorInfo = new AppraisementDoctorInfo();
		appraisementDoctorInfo.setContent(content);
		appraisementDoctorInfo.setCreateTime(new Date());
		appraisementDoctorInfo.setDoctorId(docid);
		appraisementDoctorInfo.setGrade(grade);
		appraisementDoctorInfo.setPatientId(uid);
		appraisementDoctorInfo.setOrderUuid(orderUuid);;
		appraisementDoctorInfo.setOrderType(ltype);
		appraisementDoctorInfo.setIsPassed(grade.equals(5) ? 1 : 0);
		Integer appraisementId = wenzhenService
				.saveAppraisementDoctorInfo(appraisementDoctorInfo);
		if (_tagids != null && _tagids.length > 0) {
			for (String _tagid : _tagids) {
				AppraisementImpressionDoctor appraisementImpressionDoctor = new AppraisementImpressionDoctor();
				appraisementImpressionDoctor.setAppraisementId(appraisementId);
				appraisementImpressionDoctor.setDoctorId(docid);
				appraisementImpressionDoctor.setImpressionId(Integer
						.parseInt(_tagid));
				wenzhenService
						.saveAppraisementImpressionDoctor(appraisementImpressionDoctor);
			}
		}
	}
	/**
	 * 二维码支付
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/surepay_pc", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> surepay_pc(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Float money = Float.parseFloat(request.getParameter("pmoney"));
		UUID uuid = UUID.randomUUID();
		String oid = request.getParameter("oid");
		BusinessVedioOrder rc = null;
		if (StringUtils.isNotBlank(oid)) {
			rc = generateRemoteOrder_pc2(Integer.parseInt(oid), request, "",
					money);
		} else {
			rc = generateRemoteOrder_pc(request, "", money);
		}
		String product_id = uuid.toString().replace("-", "");
		String ret = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
				"root/orderPrefix_remote");
		Map<String, Object> retMap = WeixinUtil.weipay_pc(request, response,
				money, PropertiesUtil.getString("APPID"), PropertiesUtil
						.getString("APPSECRET"), PropertiesUtil
						.getString("PARTNER"), PropertiesUtil
						.getString("PARTNERKEY"),
				ReaderConfigUtil.gainConfigVal(request, "basic.xml",
						"root/remotebody"), product_id,
				PropertiesUtil.getString("DOMAINURL") + "wzjh/remotenotify",
				null, rc.getId(), ret);
		Integer pid=payOrderManager.savePayInfo(4, rc.getId(), retMap.get("out_trade_no").toString(), money, 2, money, 0.0f, 0.0f, 0.0f,null);
		log.info("======二维码url===" + retMap.get("code_url"));
		map.put("code_url", retMap.get("code_url"));
		map.put("out_trade_no", retMap.get("out_trade_no"));
		try {
			RecordLogUtil.insert("4", "远程门诊", rc.getId()+"",pid+"",
					(String) retMap.get("paramxml"),
					(String) retMap.get("prepayxml"), "",(String)retMap.get("out_trade_no"));
		} catch (Exception e) {

		}
		return map;
	}

	/**
	 * 已有订单情况下的二维码支付订单更新
	 * @param oid
	 * @param request
	 * @param outTradeNo
	 * @param money
	 * @return
	 * @throws Exception
	 */
	private BusinessVedioOrder generateRemoteOrder_pc2(Integer oid,
			HttpServletRequest request, String outTradeNo, Float money)
			throws Exception {
		BusinessVedioOrder rc = commonService.queryBusinessVedioOrderById(oid);
		String stimeid = request.getParameter("stimeid");
		if (StringUtils.isNotBlank(stimeid)) {
			rc.setSchedulerDateId(Integer.parseInt(stimeid));
			ExpertConsultationSchedule ecs = weixinService
					.queryExpertConScheduleById(Integer.parseInt(stimeid));
			rc.setConsultationDate(ecs.getScheduleDate());
			rc.setConsultationTime(ecs.getStartTime());
		}
		rc.setExpertId(Integer.parseInt(request.getParameter("expertId")));
		rc.setConsultationDur(OrderStatusEnum.VEDIO_DURATION.getKey());
		rc.setHelpOrderSelExpert(1);
		rc.setUserId(StringUtils.isNotBlank(request.getParameter("uid")) ? Integer
				.parseInt(request.getParameter("uid")) : null);
		commonService.updateBusinessVedioOrder(rc);
		
		// 更新时间为已选---支付成功后更新
		/*ExpertConsultationSchedule sch = weixinService
				.queryExpertConScheduleById(rc.getSchedulerDateId());
		sch.setHasAppoint("1");
		weixinService.updatExpertConScheduleDate(sch);*/
		// 辅助下单更新病例
		updateCaseinfo(oid, request);
		pacsInfoStorage(oid, request);
		lisInfoStorage(oid, request);
		return rc;
	}

	/**
	 * 官网中二维码支付订单
	 * @param request
	 * @param outTradeNo
	 * @param money
	 * @return
	 * @throws Exception
	 */
	private BusinessVedioOrder generateRemoteOrder_pc(
			HttpServletRequest request, String outTradeNo, Float money)
			throws Exception {
		//处理病例
		Integer contid = Integer.parseInt(request.getParameter("contactorid"));
		UserContactInfo uinfo = weixinService
				.queryUserContactorInfoById(contid);
		String username = uinfo.getContactName();
		String telphone = uinfo.getTelphone();
		CaseInfo caseinfo=new CaseInfo();
		caseinfo.setUuid(UUID.randomUUID().toString().replace("-", ""));
		caseinfo.setUserId(StringUtils.isNotBlank(request.getParameter("uid")) ? Integer
				.parseInt(request.getParameter("uid")) : null);
		caseinfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		caseinfo.setContactName(username);
		String idcard = uinfo.getIdCard();
		caseinfo.setIdNumber(uinfo.getIdCard());
		if (StringUtils.isNotBlank(idcard)) {
			String ssex = IdcardUtils.getGenderByIdCard(idcard);
			caseinfo.setSex(ssex.equalsIgnoreCase("M") ? 1 : (ssex
					.equalsIgnoreCase("F") ? 0 : 0));
			caseinfo.setAge(IdcardUtils.getAgeByIdCard(idcard));
		} else {
			caseinfo.setSex(1);
		}
		caseinfo.setTelephone(telphone);
		caseinfo.setAskProblem(request.getParameter("askProblem"));
		Integer caseid=wenzhenService.saveCaseInfo(caseinfo);
		
		BusinessVedioOrder rc=new BusinessVedioOrder();
		rc.setCaseId(caseid);
		rc.setCaseUuid(caseinfo.getUuid());
		String stimeid = request.getParameter("stimeid");
		if (StringUtils.isNotBlank(stimeid)) {
			rc.setSchedulerDateId(Integer.parseInt(stimeid));
			ExpertConsultationSchedule ecs = weixinService
					.queryExpertConScheduleById(Integer.parseInt(stimeid));
			rc.setConsultationDate(ecs.getScheduleDate());
			rc.setConsultationTime(ecs.getStartTime());
		}
		rc.setExpertId(Integer.parseInt(request.getParameter("expertId")));
		rc.setCreateTime(new Timestamp(System.currentTimeMillis()));
		rc.setConsultationDur(OrderStatusEnum.VEDIO_DURATION.getKey());
		rc.setLocalHospitalId(Integer.parseInt(request
				.getParameter("localHosId")));
		rc.setLocalDepartId(Integer.parseInt(request
				.getParameter("localDepartId")));
		rc.setSource(2);// 官网下的
		rc.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
		rc.setLocalPlusNo("123");
		rc.setPayStatus(4);
		rc.setDelFlag(0);
		rc.setUserId(caseinfo.getUserId());
		rc.setUuid(commonManager.generateUUID(4));
		rc.setConsultationDur(20);
		Integer oid =weixinService.saveBusinessVedioOrder(rc);
		//记录消息
		commonManager.saveBusinessMessageInfo_sys(oid, 4, "text", "创建订单",null,null);
		rc.setId(oid);
		return rc;
	}

	private void updateCaseinfo(Integer oid, HttpServletRequest request) {
		BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(oid);
		CaseInfo caseinfo=commonService.queryCaseInfoById(order.getCaseId());
		caseinfo.setNormalImages(request.getParameter("normalImages"));
		caseinfo.setMainSuit(request.getParameter("mainSuit"));
		caseinfo.setPresentIll(request.getParameter("presentIll"));
		caseinfo.setAssistantResult(request.getParameter("assistantResult"));
		caseinfo.setExamined(request.getParameter("examined"));
		caseinfo.setHistoryIll(request.getParameter("historyIll"));
		caseinfo.setInitialDiagnosis(request.getParameter("initialDiagnosis"));
		caseinfo.setTreatAdvice(request.getParameter("treatAdvice"));
		caseinfo.setFamilyHistory(request.getParameter("familyHistory"));
		caseinfo.setCheckReportImages(request.getParameter("checkReportImages"));
		caseinfo.setRadiographFilmImags(request.getParameter("radiographFilmImags"));
		caseinfo.setAskProblem(request.getParameter("askProblem"));
		commonService.updateCaseInfo(caseinfo);
	}

	// pacs信息入库
	private void pacsInfoStorage(Integer oid, HttpServletRequest request)
			throws Exception {
		String pacs_ids = request.getParameter("pacs_ids");
		// 保存时数据更新
		DataCatchUtil.clearedPacsData(pacs_ids, oid+"");
	}

	// lis信息整理
	private void lisInfoStorage(Integer oid, HttpServletRequest request)
			throws Exception {
		String lis_ids = request.getParameter("lis_ids");
		// 保存时数据更新
		DataCatchUtil.clearedLisData(lis_ids, oid+"");
	}

	/**
	 * 进入充值界面
	 * 
	 * @param request
	 * @return
	 */
	/*
	 * @RequestMapping(value = "/toCharge", method = RequestMethod.GET) public
	 * ModelAndView toCharge(HttpServletRequest request) { Map<String, Object>
	 * map = new HashMap<String, Object>(); String openid =
	 * request.getParameter("openid"); openid = gainOpenIdByConditions(request,
	 * openid); map.put("openid", openid); UserWeixinRelative ur = weixinService
	 * .queryUserWeiRelativeByOpenId(openid); String redirectUrl="";
	 * if(ur==null){ map.put("flag", "toCharge"); redirectUrl="zjh/bind_tel";
	 * }else{ redirectUrl="zjh/2.0/charge"; } return new
	 * ModelAndView(redirectUrl, map); }
	 * 
	 * @RequestMapping(value="/chargewx",method=RequestMethod.POST) public
	 * 
	 * @ResponseBody Map<String,Object> chargewx(HttpServletRequest
	 * request,HttpServletResponse response){ Map<String,Object> map=new
	 * HashMap<String,Object>(); String tel = request.getParameter("telphone");
	 * UserAccountInfo u=weixinService.queryUserAccountInfoByMobilePhone(tel);
	 * if(u==null){ map.put("status", "error"); map.put("message",
	 * "该电话没有绑定公号，不能进行充值!"); }else{ Float money
	 * =Float.parseFloat(request.getParameter("pmoney")); String out_trade_no =
	 * ""; Map<String, Object> retMap = WeixinUtil.weipay(request, response,
	 * money, PropertiesUtil.getString("APPID"),
	 * PropertiesUtil.getString("APPSECRET"),
	 * PropertiesUtil.getString("PARTNER"),
	 * PropertiesUtil.getString("PARTNERKEY"), "远程会诊", openid,//
	 * "oLAmZt5uvYKF9OTZVPC4pXQhUxHc", PropertiesUtil.getString("DOMAINURL") +
	 * "wzjh/remotenotify", null); out_trade_no = (String)
	 * retMap.get("out_trade_no"); generateRemoteOrder(request, out_trade_no,
	 * money); map.put("out_trade_no", out_trade_no); map.put("appid",
	 * retMap.get("appid")); map.put("timeStamp", retMap.get("timeStamp"));
	 * map.put("nonceStr", retMap.get("nonceStr")); map.put("package",
	 * retMap.get("package")); map.put("sign", retMap.get("sign")); } return
	 * map; }
	 */

	/**
	 * 选择了专家进入提交病例界面--如果没绑定进入绑定界面
	 * 
	 * @param request
	 * @param docid
	 * @param ltype
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "/intocasesub")
	public ModelAndView intocasesub(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		Integer ltype = Integer.parseInt(request.getParameter("ltype"));
		Integer docid = Integer.parseInt(request.getParameter("docid"));
		map.put("openid", openid);
		map.put("ltype", ltype);
		map.put("docid", docid);
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		String redirectUrl = "";
		if (uwr == null) {
			map.put("vurl", "/intocasesub?docid=" + docid + "&ltype=" + ltype
					+ "&openid=" + openid);
			redirectUrl = "zjh/3.0/bind_tel";
		} else {
			/*List<CaseInfo> cases = wenzhenService.queryHisCaseInfosByUId(uwr
					.getUserId());
			for (CaseInfo _case : cases) {
				_case.setCaseImages(wenzhenService
						.queryCaseImagesByCaseId(_case.getId()));
			}
			map.put("cases", cases);*/
			MobileSpecial special = commonService
					.queryMobileSpecialByUserIdAndUserType(docid);
			if (ltype.equals(1)) {
				special.setAskAmount(commonManager.gainMoneyByOrder(1,special.getSpecialId()));
				map.put("money", special.getAskAmount());
			} else if (ltype.equals(2)) {
				special.setAskAmount(commonManager.gainMoneyByOrder(2,special.getSpecialId()));
				map.put("money", special.getTelAmount());
			}
			redirectUrl = "zjh/3.0/online/case_sub";
		}
		return new ModelAndView(redirectUrl, map);
	}

	/**
	 * 提交图文或者电话订单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/subonlineorder", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> subtworder(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		Integer ltype = Integer.parseInt(request.getParameter("ltype"));
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		Map<String,Object> retmap=wenzhenManager.processTwAndTelOrder(request,response,uwr.getUserId(),ltype);
		map.putAll(retmap);
		return map;
	}

	

	/**
	 * 我的订单
	 * @param request
	 * @return zjh/3.0/myorders.jsp
	 * @throws Exception
	 */
	@RequestMapping(value = "/myorders", method = RequestMethod.GET)
	public ModelAndView myorders(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		if (!StringUtils.isNotBlank(openid)) {
			// 菜单进入
			openid = gainOpenIdByConditions(request, "");
		}
		String flag = request.getParameter("flag");
		if (!StringUtils.isNotBlank(flag))
			flag = "processing";
		map.put("flag", flag);
		map.put("openid", openid);
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		String redirectUrl = "zjh/3.0/myorders";
		return new ModelAndView(redirectUrl, map);
	}

	/**
	 * 更多订单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gainorders", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainorders(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		Integer _pageSize = 5;
		Integer _pageNo = 1;
		if (StringUtils.isNotBlank(pageNo)) {
			_pageNo = Integer.parseInt(pageNo);
		}
		if (StringUtils.isNotBlank(pageSize)) {
			_pageSize = Integer.parseInt(pageSize);
		}
		String flag = request.getParameter("flag");
		List<WenzhenBean> orders = null;
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		if (uwr != null) {
			orders = weixinService.queryWenzhenOrdersByConditions(
					uwr.getUserId(), _pageNo, _pageSize, flag);
			processOrders(orders, flag);
			map.put("orders", orders);
		} else {
			orders = new ArrayList<WenzhenBean>();
		}
		return map;
	}

	private void processOrders(List<WenzhenBean> orders, String flag) {
		for (WenzhenBean _bean : orders) {
			_bean.setTimeStr(sdf.format(_bean.getCreateTime()));
			if (_bean.getType().equalsIgnoreCase("1")
					|| _bean.getType().equalsIgnoreCase("2")) {
				// 图文或电话
				if (flag.equalsIgnoreCase("processing")) {
					Integer _ps = _bean.getPaStatus().intValue();
					if (_ps.equals(4)) {
						_bean.setDesc("待付款");
					} else if (_ps.equals(1)) {
						_bean.setDesc("进行中");
					}
				} else if (flag.equalsIgnoreCase("complete")) {
					_bean.setDesc("已完成");
				} else if (flag.equalsIgnoreCase("cancel")) {
					_bean.setDesc("已取消");
				}
			} else if (_bean.getType().equalsIgnoreCase("4")) {
				// 远程门诊
				if (flag.equalsIgnoreCase("processing")) {
					Integer _ps=_bean.getPaStatus().intValue();
					if(_ps.equals(4)){
						_bean.setDesc("待付款");
					}else{
						_bean.setDesc(StringRetUtil.gainStringByStats(
								_bean.getAskStatus(), null));
					}
				} else if (flag.equalsIgnoreCase("complete")) {
					_bean.setDesc("已完成");
				} else if (flag.equalsIgnoreCase("cancel")) {
					_bean.setDesc("已取消");
				}
			}
		}
	}

	/**
	 * 继续支付
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/continuepay", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> continuepay(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		Integer type = Integer.parseInt(request.getParameter("type"));// 1图文，2电话
																		// 4远程门诊
		Integer docid = null;
		String desc = "";
		String notify_url = "";
		String openid = request.getParameter("openid");
		MobileSpecial special = null;
		Float money=0.0f;
		switch (type) {
		case 1://图文
			BusinessTuwenOrder tworder=wenzhenService.queryBusinessTuwenInfoById(oid);
			docid = tworder.getDoctorId();
			special=commonService
			.queryMobileSpecialByUserIdAndUserType(docid);
			desc = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/twbody");
			notify_url = "kangxin/wenzhennotify";
			money=commonManager.gainMoneyByOrder(1,special.getSpecialId()).floatValue();
			break;
		case 2://电话
			BusinessTelOrder telorder=wenzhenService.queryBusinessTelOrderById(oid);
			docid = telorder.getDoctorId();
			special=commonService
					.queryMobileSpecialByUserIdAndUserType(docid);
			desc = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/telbody");
			notify_url = "kangxin/wenzhennotify";
			money=commonManager.gainMoneyByOrder(2,special.getSpecialId()).floatValue();
			break;
		case 4://远程
			BusinessVedioOrder order=wenzhenService.queryBusinessVedioOrderById(oid);
			docid=order.getExpertId();
			special=commonService
					.queryMobileSpecialByUserIdAndUserType(docid);
			desc = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/remotebody");
			notify_url="wzjh/remotenotify";
			List<BusinessPayInfo> pays=wenzhenService.queryBusinesPayInfosByOId(oid,4);
			if(pays!=null&&pays.size()>0){
				money=pays.get(pays.size()-1).getTotalMoney().floatValue();
			}else{
				if(StringUtils.isNotBlank(order.getExLevel())){
					//如果是级别
					Dictionary dic=commonService.queryDictionaryByCon(order.getExLevel(),25);
					money=Float.valueOf(dic.getDisplayValue());
				}else{
					money=commonManager.gainMoneyByOrder(4,special.getSpecialId()).floatValue();
				}
			}
			break;
		}
		Map<String, Object> retMap = WeixinUtil.weipay(request, response,
				money, PropertiesUtil.getString("APPID"),
				PropertiesUtil.getString("APPSECRET"),
				PropertiesUtil.getString("PARTNER"),
				PropertiesUtil.getString("PARTNERKEY"), desc, openid,
				PropertiesUtil.getString("DOMAINURL") + notify_url, "", null,
				null);
		//生成支付订单
		Integer pid=payOrderManager.savePayInfo(type, oid, retMap.get("out_trade_no").toString(), money, 2, money, 0.0f, 0.0f, 0.0f,null);
		try {
			RecordLogUtil.insert(type.toString(), desc, oid+"", pid+"",
					(String) retMap.get("paramxml"),
					(String) retMap.get("prepayxml"), "",retMap.get("out_trade_no").toString());
		} catch (Exception e) {

		}
		map.putAll(retMap);
		return map;
	}

	/**
	 * 文本消息发送
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/appendmessage_txt", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> appendmessage_txt(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 问诊id
		String openid = request.getParameter("openid");
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		String msgContent = request.getParameter("msgContent");
		BusinessTuwenOrder order=wenzhenService.queryBusinessTuwenInfoById(oid);
		BusinessMessageBean message=new BusinessMessageBean();
		message.setOrderId(oid);
		message.setOrderType(1);
		message.setMsgType("text");
		message.setMsgContent(msgContent);
		message.setSendId(uwr.getUserId());
		message.setSendType(1);
		message.setSendTime(new Timestamp(System.currentTimeMillis()));
		message.setRecvId(order.getDoctorId());
		message.setRecvType(2);
		message.setStatus(1);
		wenzhenService.saveBusinessMessageBean(message);
		//患者最后回复时间
		order.setPatLastAnswerTime(new Timestamp(System.currentTimeMillis()));
		//患者发送消息数
		order.setPatRecvMsgCount(order.getPatRecvMsgCount()==null?1:(order.getPatRecvMsgCount()+1));
		//医生未读数
		order.setDocUnreadMsgNum(order.getDocUnreadMsgNum()==null?1:(order.getDocUnreadMsgNum()+1));
		wenzhenService.updateBusinessTuwenOrder(order);
		message.setMsgTime(sdf.format(message.getSendTime()));
		map.put("message", message);
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(order.getDoctorId());
		HttpSendSmsUtil.sendSmsInteface("13521231353,15001299884", "患者向专家"
				+ special.getSpecialName() + "发起了一条提问。请及时回复！【佰医汇】");
		return map;
	}

	/**
	 * 图片消息发送
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/appendmessage_image", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> appendmessage_image(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 问诊id
		BusinessWenZhenInfo order = wenzhenService
				.queryBusinessWenZhenInfoById(oid);
		String openid = request.getParameter("openid");
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		String dirpath = dir_time.format(new Date()) + "/";
		Map<String, Object> ret_map = OSSManageUtil.uploadFile_in(dirpath,
				new OSSConfigure(), request);
                                                                                                                                                                                                    
		BusinessMessageBean message=new BusinessMessageBean();
		message.setOrderId(oid);
		message.setOrderType(1);
		message.setMsgType("image/jpg");
		message.setFileUrl(ret_map.get("urlpath").toString());
		message.setSendId(uwr.getUserId());
		message.setSendType(1);
		message.setSendTime(new Timestamp(System.currentTimeMillis()));
		message.setRecvId(order.getDoctorId());                           
		message.setRecvType(2);
		message.setStatus(1);
		wenzhenService.saveBusinessMessageBean(message);
		map.put("urlpath", ret_map.get("urlpath"));                           
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(order.getDoctorId());
		HttpSendSmsUtil.sendSmsInteface("13521231353,15001299884", "患者向专家"
				+ special.getSpecialName() + "发起了一条提问。请及时回复！【佰医汇】");
		return map;          
	}

	/**
	 * 远程会诊 远程门诊子项目
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/remconsulsub", method = RequestMethod.GET)
	public ModelAndView remconsulsub(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		if (!StringUtils.isNotBlank(openid)) {
			// 菜单进入
			openid = gainOpenIdByConditions(request, "");
		}
		map.put("openid", openid);
		List<Dictionary> splevels = weixinService
				.queryDictionarysByParentId(25);// 获取专家级别及价钱
		map.put("splevels", splevels);
		List<Dictionary> seltimes = weixinService
				.queryDictionarysByParentId(29);// 获取就诊时间
		map.put("seltimes", seltimes);
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		return new ModelAndView("zjh/3.0/remote/remote_sub_main", map);
	}

	
	/**
	 * 进入预约确认界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/iwantsub")
	public ModelAndView iwantsub(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		String hospitalId = request.getParameter("hosid");
		String depid = request.getParameter("depid");
		String timeid = request.getParameter("timeid");
		String splevel = request.getParameter("levelid");//级别id   -1为自定义
		String op_name=request.getParameter("opname");//运营人员姓名
		String op_money=request.getParameter("opmoney");//自定义金额  必填
		String op_tel=request.getParameter("optel");//运营人员联系电话
		map.put("opname", op_name);
		map.put("op_money", op_money);
		map.put("optel", op_tel);
		map.put("hosid", hospitalId);
		map.put("depid", depid);
		map.put("splevel", splevel);
		map.put("timeid", timeid);
		map.put("openid", openid);
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		if (uwr == null) {
			// 未绑定
			map.put("vurl", "/iwantsub?openid=" + openid + "&hosid="
					+ hospitalId + "&depid=" + depid + "&levelid=" + splevel
					+ "&timeid=" + timeid+"&opname="+op_name+"&opmoney="+op_money+"&optel="+op_tel);
			return new ModelAndView("zjh/3.0/bind_tel", map);
		}
		HospitalDetailInfo hospital = weixinService
				.queryHospitalDetailInfoById(Integer.parseInt(hospitalId));
		HospitalDepartmentInfo depart = weixinService
				.queryHospitalDepartmentInfoById(Integer.parseInt(depid));
		map.put("hospital", hospital);
		map.put("depart", depart);
		BigDecimal money=null;
		
		if(splevel.equalsIgnoreCase("-1")){
			map.put("leveldesc", "自定义级别");
			money=new BigDecimal(op_money);
		}else{
			Dictionary level = weixinService.queryDictionaryById(Integer
					.parseInt(splevel));
			money=new BigDecimal(level.getDisplayValue());
			map.put("leveldesc", level.getDisplayName());
		}
		
		map.put("money", money);
		Dictionary time = weixinService.queryDictionaryById(Integer
				.parseInt(timeid));

		map.put("time", time);
		// 常用联系人
		List<UserContactInfo> contacts = weixinService
				.queryUserContactInfosByOpenId(openid);
		map.put("contacts", contacts);
		return new ModelAndView("zjh/3.0/remote/confirm", map);
	}

	/**
	 * 提交支付远程会诊
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/subremote", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> subremote(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		String levelid = request.getParameter("levelid");
		BigDecimal money=null;
		if(!levelid.equalsIgnoreCase("-1")){
			
			Dictionary level = weixinService.queryDictionaryById(Integer
					.parseInt(levelid));
			money=new BigDecimal(level.getDisplayValue());
		}else{
			//自定义级别
			money=new BigDecimal(request.getParameter("opmoney"));
		}
		Map<String,Object> retmap=wxMainManager.processRemoteOrder(request, response, 2, uwr.getUserId(),money.floatValue());
		map.putAll(retmap);
		return map;
	}

	/**
	 * 我的订单详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/myorderdetail", method = RequestMethod.GET)
	public ModelAndView myorderdetail(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		String oid = request.getParameter("oid");
		String uuid=request.getParameter("uuid");
		String flag = request.getParameter("flag");
		String type = request.getParameter("type");
		map.put("oid", oid);
		map.put("flag", flag);
		map.put("type", type);
		map.put("openid", openid);
		// 判断是否已评价
		AppraisementDoctorInfo apprase = wenzhenService
				.queryAppraisementDoctorInfoByConditions(uuid,
						Integer.parseInt(type));
		if (apprase != null) {
			map.put("iseval", "yes");
		} else {
			map.put("iseval", "no");
		}
		return new ModelAndView("zjh/3.0/myorder_detail", map);
	}

	/**
	 * 获取详情数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gainorderinfo", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainorderinfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		Integer type = Integer.parseInt(request.getParameter("type"));
		String flag = request.getParameter("flag");
		Integer caseid = null;
		Integer docid = null;
		List<BusinessPayInfo> payinfos = null;
		BigDecimal money = null;
		switch (type) {
		case 1:
			// 图文
			// 查询支付订单
			payinfos = wenzhenService.queryBusinesPayInfosByOId(oid, 1);
			BusinessTuwenOrder twinfo=wenzhenService.queryBusinessTuwenInfoById(oid);
			twinfo.setTimeStr(sdf.format(twinfo.getCreateTime()));
			twinfo.setPatUnreadMsgNum(0);
			wenzhenService.updateBusinessTuwenOrder(twinfo);
			processOrders(twinfo, payinfos, flag);
			map.put("twinfo", twinfo);
			caseid = twinfo.getCaseId();
			docid = twinfo.getDoctorId();
			List<BusinessMessageBean> messages =wenzhenService.queryBusinessMessageBeansByCon(oid,twinfo.getUuid(),1);
			for (BusinessMessageBean _bean : messages) {
				_bean.setMsgTime(sdf.format(_bean.getSendTime()));
			}
			map.put("messages", messages);
			if(payinfos!=null&&payinfos.size()>0){
				money=payinfos.get(0).getTotalMoney();
			}else{
				money=commonManager.gainMoneyByOrder(1,docid);
			}
			break;
		case 2:
			// 电话
			BusinessTelOrder telinfo=wenzhenService.queryBusinessTelOrderById(oid);
			payinfos = wenzhenService.queryBusinesPayInfosByOId(oid, 2);
			telinfo.setTimeStr(sdf.format(telinfo.getCreateTime()));
			processOrders(telinfo, payinfos, flag);
			map.put("telinfo", telinfo);
			caseid = telinfo.getCaseId();
			docid = telinfo.getDoctorId();
			if(payinfos!=null&&payinfos.size()>0){
				money=payinfos.get(0).getTotalMoney();
			}else{
				money=commonManager.gainMoneyByOrder(2,docid);
			}
			break;
		case 4:
			// 远程门诊
			BusinessVedioOrder reminfo=wenzhenService.queryBusinessVedioOrderById(oid);
			payinfos = wenzhenService.queryBusinesPayInfosByOId(oid, 4);
			reminfo.setTimeStr(sdf.format(reminfo.getCreateTime()));
			String time = gaintime_new(reminfo);
			time = time.equalsIgnoreCase("null null") ? "未设置" : time;
			reminfo.setConDate(time);
			map.put("reminfo", reminfo);
			if(payinfos!=null&&payinfos.size()>0){
				money = payinfos.get(payinfos.size()-1).getTotalMoney();
			}else{
				money=commonManager.gainMoneyByOrder(4, reminfo.getExpertId());
			}
			processOrders(reminfo, flag);
			caseid=reminfo.getCaseId();
			break;
		}
		if (docid != null) {
			MobileSpecial special = commonService
					.queryMobileSpecialByUserIdAndUserType(docid);
			map.put("special", special);
		}
		if (caseid != null) {
			CaseInfo caseinfo = wenzhenService.queryCaseInfoById(caseid);
			caseinfo.setTimeStr(sdf.format(caseinfo.getCreateTime()));
			map.put("caseinfo", caseinfo);
			// 病例图片
			List<CustomFileStorage> caseimages = wenzhenService
					.queryCustomFilesByCaseIds(caseinfo.getNormalImages());
			map.put("caseimages", caseimages);
		}
		map.put("money", money);
		return map;
	}

	private String gaintime_new(BusinessVedioOrder rc) {
		String time = "";
		switch (rc.getStatus()) {
		case 0:
		case 1:

		case 2:
			if (StringUtils.isNotBlank(rc.getConsultationDate())) {
				time += rc.getConsultationDate();
			}
			if (StringUtils.isNotBlank(rc.getConsultationTime())) {
				time += " " + rc.getConsultationTime();
			}
			break;
		case 3:
		case 4:
		case 5:
		case 10:
				time = rc.getConsultationDate() + " "
						+ rc.getConsultationTime();
			break;
		case 20:
		case 21:
			time = rc.getConsultationDate() + " " + rc.getConsultationTime();
			break;
		}
		return time;
	}

	private void processOrders(BusinessTuwenOrder twinfo,
			List<BusinessPayInfo> payinfos, String flag) {
		if (flag.equalsIgnoreCase("processing")) {
			boolean pay=true;
			for (BusinessPayInfo _payinfo : payinfos) {
				Integer _ps = _payinfo.getPayStatus().intValue();
				if (_ps.equals(4)) {
					pay=false;
					break;
				}
			}
			if(pay){
				twinfo.setDesc("进行中");
			}else{
				twinfo.setDesc("待付款");
			}
		} else if (flag.equalsIgnoreCase("complete")) {
			twinfo.setDesc("已完成");
		} else if (flag.equalsIgnoreCase("cancel")) {
			twinfo.setDesc("已取消");
		}
	}

	private void processOrders(BusinessTelOrder telinfo,
			List<BusinessPayInfo> payinfos, String flag) {
		// 图文或电话
		if (flag.equalsIgnoreCase("processing")) {
			boolean pay=true;
			for (BusinessPayInfo _payinfo : payinfos) {
				Integer _ps = _payinfo.getPayStatus().intValue();
				if (_ps.equals(4)) {
					pay=false;
					break;
				}
			}
			if(pay){
				telinfo.setDesc("进行中");
			}else{
				telinfo.setDesc("待付款");
			}
		} else if (flag.equalsIgnoreCase("complete")) {
			telinfo.setDesc("已完成");
		} else if (flag.equalsIgnoreCase("cancel")) {
			telinfo.setDesc("已取消");
		}
	}

	private void processOrders(BusinessVedioOrder reminfo, String flag) {
		reminfo.setTimeStr(sdf.format(reminfo.getCreateTime()));
		// 远程门诊
		if (flag.equalsIgnoreCase("processing")) {
			reminfo.setDesc(StringRetUtil.gainStringByStats(
					reminfo.getStatus(), null));
		} else if (flag.equalsIgnoreCase("complete")) {
			reminfo.setDesc("已完成");
		} else if (flag.equalsIgnoreCase("cancel")) {
			reminfo.setDesc("已取消");
		}
	}

	/**
	 * 在线问诊---合并图文及电话问诊 ltype--1图文问诊，2电话问诊
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/onlineask/{ltype}", method = RequestMethod.GET)
	public ModelAndView onlineask(HttpServletRequest request,
			@PathVariable Integer ltype) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		openid = gainOpenIdByConditions(request, openid);
		map.put("openid", openid);
		map.put("ltype", ltype);
		List<StandardDepartmentInfo> departs = weixinService
				.queryStandardDepartments();
		map.put("departs", departs);
		return new ModelAndView("zjh/3.0/online/start_page", map);
	}

	/**
	 * 点击标准科室进入的专家列表界面
	 */
	@RequestMapping(value = "/depintodocs", method = RequestMethod.GET)
	public ModelAndView depintodocs(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchContent", request.getParameter("searchContent"));
		map.put("depid", request.getParameter("depid"));
		map.put("ltype", request.getParameter("ltype"));
		map.put("openid", request.getParameter("openid"));
		return new ModelAndView("zjh/3.0/online/doc_list", map);
	}

	/**
	 * 获取开通了图文问诊或电话问诊的专家
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gainexpertsopenonline", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainexpertsopenonline(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer ltype = Integer.parseInt(request.getParameter("ltype"));
		String depid = request.getParameter("depid");
		String searchContent = request.getParameter("searchContent");
		System.out.println("===检索内容=="+searchContent);
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		Integer _pageNo = 1;
		Integer _pageSize = 10;
		if (StringUtils.isNotBlank(pageNo)) {
			_pageNo = Integer.parseInt(pageNo);
		}
		if (StringUtils.isNotBlank(pageSize)) {
			_pageSize = Integer.parseInt(pageSize);
		}
		Map<String, Object> querymap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(depid)) {
			querymap.put("depid", depid);
		}
		if (StringUtils.isNotBlank(searchContent)) {
			querymap.put("searchContent", searchContent);
		}
		querymap.put("ltype", ltype);
		List<MobileSpecial> specials = weixinService
				.queryMobileSpecialsByConditionsPro(querymap, _pageNo,
						_pageSize);
		map.put("specials", specials);
		return map;
	}

	@RequestMapping(value = "/intodocdetail", method = RequestMethod.GET)
	public ModelAndView intodocdetail(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer docid = Integer.parseInt(request.getParameter("docid"));
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(docid);
		special.setAskAmount(commonManager.gainMoneyByOrder(1, special.getSpecialId()));
		special.setTelAmount(commonManager.gainMoneyByOrder(2, special.getSpecialId()));
		map.put("special", special);
		map.put("openid", request.getParameter("openid"));
		map.put("ltype", request.getParameter("ltype"));
		String relatedPics = special.getRelatedPics();
		List<CustomFileStorage> images = null;
		if (StringUtils.isNotBlank(relatedPics)) {
			images = weixinService.queryCustomFileStorageImages(relatedPics);
		}
		map.put("images", images);
		return new ModelAndView("zjh/3.0/online/doc_detail", map);
	}
	
	/**
	 * 新版 远程项目订单入口
	 * 访问：http://localhost:8080/wzjh/newremote
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/newremote", method = RequestMethod.GET)
	public ModelAndView newremote(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		if (!StringUtils.isNotBlank(openid)) {
			// 菜单进入
			openid = gainOpenIdByConditions(request, "");
		}
		map.put("openid", openid);
		map.put("docid", request.getParameter("docid"));
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		return new ModelAndView("zjh/4.0/index", map);
	}
	/**
	 * 根据经纬度获取所在城市
	 * 访问：http://localhost:8080/wzjh/gainCityByLocation
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gainCityByLocation")
	public @ResponseBody
	Map<String, Object> gainCityByLocation(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String latitude = request.getParameter("latitude");
		String longitude = request.getParameter("longitude");
		String city = "";
		String distcode="";
		if (StringUtils.isNotBlank(latitude)
				&& StringUtils.isNotBlank(longitude)) {
			city = LocationUtil
					.gainLocationString(Double.parseDouble(latitude),
							Double.parseDouble(longitude));
			HospitalDetailInfo cd = weixinService
					.queryCooHospitalInfoByCity(city);
			if (cd != null){
				distcode=cd.getDistCode();
			}
		}
		map.put("city", city);//城市名称
		map.put("distcode", distcode);//城市distcode
		return map;
	}

	/**
	 * 加载已开通服务的城市
	 * 访问：http://localhost:8080/wzjh/gainopencitys
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gainopencitys")
	public @ResponseBody
	Map<String, Object> gainopencitys(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ReSourceBean> beans = weixinService.queryOpenCitys();
		map.put("beans", beans);
		return map;
	}

	/**
	 * 加载开通城市的医院
	 * 访问：http://localhost:8080/wzjh/gainhosbycity
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gainhosbycity")
	public @ResponseBody
	Map<String, Object> gainhosbycity(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String dictcode = request.getParameter("dictcode");
		List<HospitalDetailInfo> hospitals = weixinService
				.queryHospitalDetailsByDsitcode(dictcode, 2);
		map.put("hospitals", hospitals);
		return map;
	}

	/**
	 * 根据医院加载医院科室
	 * 访问：http://localhost:8080/wzjh/gaindepartsbyhos
	 * 参数：hosid
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gaindepartsbyhos")
	public @ResponseBody
	Map<String, Object> gaindepartsbyhos(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer hosid = Integer.parseInt(request.getParameter("hosid"));
		List<HospitalDepartmentInfo> localDeparts = weixinService
				.queryCoohospitalDepartmentsByCooHos(hosid);
		map.put("departs", localDeparts);
		return map;
	}

	/**
	 * 加载专家级别及价钱
	 * 访问：http://localhost:8080/wzjh/loadlevels
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/loadlevels")
	@ResponseBody
	public Map<String,Object> loadlevels(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		List<Dictionary> splevels = weixinService
				.queryDictionarysByParentId(25);// 获取专家级别及价钱
		map.put("splevels", splevels);
		return map;
	}
	/**
	 * 加载期望日期
	 * 访问：http://localhost:8080/wzjh/loadwishtime
	 * 
	 * @return
	 */
	@RequestMapping(value="/loadwishtime")
	@ResponseBody
	public Map<String,Object> loadwishtime(){
		Map<String,Object> map=new HashMap<String,Object>();
		List<Dictionary> seltimes = weixinService
				.queryDictionarysByParentId(29);// 获取就诊时间
		map.put("times", seltimes);
		return map;
	}
	
	/**
	 * 加载专家库中数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/loadexperts")
	@ResponseBody
	public Map<String,Object> loadexperts(HttpServletRequest request){
		System.out.println("===进入加载==");
		Map<String,Object> map=new HashMap<String,Object>();
		Integer depid=Integer.parseInt(request.getParameter("depid"));//医院科室id
		//String standdepid=request.getParameter("standdepid");//标准科室id 过滤
		//String hosid=request.getParameter("hosid");//医院id 过滤
		String zc=request.getParameter("zc");//职称
		String pageSize=request.getParameter("pageSize");//每页数据量
		String pageNo=request.getParameter("pageNo");//页码 从1开始
		String keywords=request.getParameter("keywords");//检索关键字
		Integer _pageSize=10;
		Integer _pageNo=1;
		if(StringUtils.isNotBlank(pageNo))_pageNo=Integer.parseInt(pageNo);
		if(StringUtils.isNotBlank(pageSize))_pageSize=Integer.parseInt(pageSize);
		List<MobileSpecial> specials=commonManager.loadExperts_wx(_pageNo,_pageSize,depid,"","",zc,keywords);
		map.put("specials", specials);
		return map;
	}
	/**
	 * 判定是否绑定
	 * 访问：http://localhost:8080/wzjh/tellbind
	 */
	@RequestMapping(value="/tellbind")
	@ResponseBody
	public Map<String,Object> tellbind(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String openid=request.getParameter("openid");
		if(isbind(openid)){
			map.put("isbind", true);
		}else{
			map.put("isbind", false);
		}
		return map;
	}
	private boolean isbind(String openid){
		UserWeixinRelative uwr=weixinService.queryUserWeiRelativeByOpenId(openid);
		if(uwr==null){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 填写完病例，提交支付
	 * 访问：http://localhost:8080/wzjh/subremotecase
	 * 返回：支付所需参数
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/subremotecase")
	@ResponseBody
	public Map<String,Object> subremotecase(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		map.putAll(wxMainManager.processWxRemoteOrder(request,response));
		return map;
	}
	
	///////////////////////////////////////个人中心///////////////////////////////////////////////////
	/**
	 * 进入个人中心--菜单进入
	 * 访问：http://localhost:8080/wzjh/mycenter
	 * @param request
	 * @return
	 */
	/*@RequestMapping(value="/mycenter")
	public ModelAndView mycenter(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		openid = gainOpenIdByConditions(request, openid);
		map.put("openid", openid);
		String url="";
		if(isbind(openid)){
			UserWeixinRelative uwr=weixinService.queryUserWeiRelativeByOpenId(openid);
			dataSet(uwr,openid,1);
			UserAccountInfo user=weixinService.queryUserAccountInfoById(uwr.getUserId());
			map.put("user", user);
			map.put("uwr",uwr);
			url="zjh/4.0/mycenter";
		}else{
			UserWeixinRelative uwr=new UserWeixinRelative();
			dataSet(uwr,openid,2);
			map.put("uwr",uwr);
			url="zjh/4.0/bind";
		}
		return new ModelAndView(url, map);
	}
	*/
	/**
	 * 获取用户信息
	 * 访问：http://localhost:8080/wzjh/gainpersoninfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainpersoninfo")
	@ResponseBody
	public Map<String,Object> gainpersoninfo(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String openid=request.getParameter("openid");
		openid = gainOpenIdByConditions(request,openid);
		map.putAll(wxMainManager.gainpersoninfo(openid));
		return map;
	}

	/**
	 * 修改基本信息
	 * 访问：http://localhost:8080/wzjh/modifyinfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/modifyinfo")
	public @ResponseBody Map<String,Object> modifyinfo(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String openid=request.getParameter("openid");
		wxMainManager.modifyinfo(openid, request);
		return map;
	}
	
	/**
	 * 获取验证码
	 * 访问：http://localhost:8080/wzjh/gainVeryCode
	 * 参数：telphone
	 * 如果是修改密码还需要参数：ltype:"pass",openid
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gainVeryCode", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainVeryCode(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String telphone = request.getParameter("telphone");
		String code = CheckNumUtil.randomChars(4);
		String ltype=request.getParameter("ltype");
		String openid=request.getParameter("openid");
		map.putAll(wxMainManager.gainVeryCode(ltype, openid, code, telphone, request));
		return map;
	}
	
	/**
	 *判定验证码并绑定公号  新
	 *访问：http://localhost:8080/wzjh/tellCodeNew
	 *参数：code，tel
	 *返回：status:"success",reg:"true"--注册过，"false"--未注册过
	 *      status:"error":验证失败
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/tellCodeNew", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> tellCodeNew(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String code = request.getParameter("code");
		String tel = request.getParameter("tel");
		map.putAll(wxMainManager.tellCodeNew(request, tel, code));
		return map;
	}
	
	/**
	 * 确认绑定
	 * 访问：http://localhost:8080/wzjh/suretobind
	 *参数：openid,tel,password(如果是未注册的，必填)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/suretobind")
	@ResponseBody
	public Map<String,Object> suretobind(HttpServletRequest request) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		String openid=request.getParameter("openid");
		String tel=request.getParameter("tel");
		String password=request.getParameter("password");
		wxMainManager.suretobind(request,tel, password, openid);
		return map;
	}
}
