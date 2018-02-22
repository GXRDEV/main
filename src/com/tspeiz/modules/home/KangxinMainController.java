package com.tspeiz.modules.home;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.ExpertConsultationSchedule;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
import com.tspeiz.modules.common.entity.newrelease.BusinessPayInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessTelOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseImages;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorForum;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.UserAccountInfo;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;
import com.tspeiz.modules.common.entity.newrelease.UserFeedBackInfo;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAlliance;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IKangxinService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.manage.CommonManager;
import com.tspeiz.modules.manage.KangxinManager;
import com.tspeiz.modules.manage.PayCallBackManager;
import com.tspeiz.modules.manage.PayOrderManager;
import com.tspeiz.modules.util.CheckNumUtil;
import com.tspeiz.modules.util.DateUtil;
import com.tspeiz.modules.util.MatrixToImageWriter;
import com.tspeiz.modules.util.PasswordUtil;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.config.ReaderConfigUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.log.RecordLogUtil;
import com.tspeiz.modules.util.oss.OSSConfigure;
import com.tspeiz.modules.util.oss.OSSManageUtil;
import com.tspeiz.modules.util.weixin.WeixinUtil;

/**
 * 官网
 * 
 * @author heyongb
 * 
 */
@Controller
@RequestMapping("kangxin")
public class KangxinMainController {
	private SimpleDateFormat code_time = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat dir_time = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat year_sdf=new SimpleDateFormat("yyyy");
	private static SimpleDateFormat month_sdf=new SimpleDateFormat("MM");
	private static SimpleDateFormat day_sdf=new SimpleDateFormat("dd");
	private Logger log = Logger.getLogger(KangxinMainController.class);
	private Logger payLog=Logger.getLogger("payMongoDB");
	@Resource
	private IKangxinService kangxinService;
	@Resource
	private IWeixinService weixinService;
	@Resource
	private ICommonService commonService;
	@Resource
	private IWenzhenService wenzhenService;
	@Autowired
	private PayOrderManager payOrderManager;
	@Autowired
	private PayCallBackManager payCallBackManage;
	@Autowired
	private CommonManager commonManager;
	@Autowired
	private KangxinManager kangxinManager;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) {

		return new ModelAndView("website/index");
	}

	/**
	 * 获取医院数据
	 */
	@RequestMapping(value = "/gainHospitals", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainHospitals(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		Integer _pageNo = 1;
		Integer _pageSize = 4;
		if (StringUtils.isNotBlank(pageNo)) {
			_pageNo = Integer.parseInt(pageNo);
		}
		if (StringUtils.isNotBlank(pageSize)) {
			_pageSize = Integer.parseInt(pageSize);
		}
		List<HospitalDetailInfo> hospitals = kangxinService
				.queryHospitalsByPage(_pageNo, _pageSize);
		for (HospitalDetailInfo hos : hospitals) {
			// 计算医院专家数量
			Map<String, Integer> nummap = kangxinService
					.queryExpertAndDepNumberByHos(hos.getId());
			hos.setExpertNum(nummap.get("expertnum"));
			hos.setDepNum(nummap.get("depnum"));
		}
		map.put("hospitals", hospitals);
		return map;
	}

	/*
	 * 获取名医对对碰信息
	 */
	@RequestMapping(value = "/gaindoctrforums", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gaindoctrforums(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		String sort = request.getParameter("sorttype");
		if (!StringUtils.isNotBlank(sort))
			sort = "desc";
		Integer _pageNo = 1;
		Integer _pageSize = 3;
		if (StringUtils.isNotBlank(pageNo))
			_pageNo = Integer.parseInt(pageNo);
		if (StringUtils.isNotBlank(pageSize))
			_pageSize = Integer.parseInt(pageSize);
		List<DoctorForum> docforums = kangxinService
				.queryDoctorForumsByConditions(_pageNo, _pageSize, sort);
		map.put("docforums", docforums);
		return map;
	}

	/**
	 * page 获取名医对对碰信息
	 */
	@RequestMapping(value = "/gaindoctrforumspage", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gaindoctrforumspage(@ModelAttribute Pager pager,
			HttpServletRequest request) {
		if (pager == null)
			pager = new Pager();
		Map<String, String> querymap = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();
		String sort = request.getParameter("sorttype");
		if (!StringUtils.isNotBlank(sort))
			sort = "desc";
		querymap.put("sort", sort);
		pager.setQueryBuilder(querymap);
		pager = kangxinService.queryDoctorForumsByConditions(pager);
		map.put("pager", pager);
		return map;
	}

	/**
	 * 获取合作医院
	 */
	@RequestMapping(value = "/gaincoohospitals")
	public @ResponseBody
	Map<String, Object> gainhospitals(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		/*List<HospitalDetailInfo> hospitals = weixinService
				.queryCoohospitalInfos();*/
		String procost = request.getParameter("procost");
		List<HospitalDetailInfo> hospitals = weixinService.queryHospitalDetailsByDsitcode(procost,2);
		map.put("hospitals", hospitals);
		return map;
	}

	/**
	 * 根据合作医院获取开通的科室
	 */

	@RequestMapping(value = "/gainhosdeparts")
	public @ResponseBody
	Map<String, Object> gainhosdeparts(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer hosid = Integer.parseInt(request.getParameter("hosid"));
		List<HospitalDepartmentInfo> localDeparts = weixinService
				.queryCoohospitalDepartmentsByCooHos(hosid);
		map.put("departs", localDeparts);
		return map;
	}

	// 远程门诊 相关专家列表
	@RequestMapping(value = "/gainspecialspage")
	public @ResponseBody
	Map<String, Object> gainspecialspage(@ModelAttribute Pager pager,
			HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (pager == null)
			pager = new Pager();
		String hosid = request.getParameter("hosid");
		String depid = request.getParameter("depid");
		Map<String, String> querymap = new HashMap<String, String>();
		if (StringUtils.isNotBlank(hosid)) {
			querymap.put("hosid", hosid);
		}
		if (StringUtils.isNotBlank(depid)) {
			querymap.put("depid", depid);
		}
		querymap.put("stype", "remote");
		querymap.put("openVedio", "true");
		pager.setQueryBuilder(querymap);
		pager = kangxinService.searchspecialsByPager_remote(pager);
		retMap.put("pager", pager);
		return retMap;
	}

	// 获取专家详情
	@RequestMapping(value = "/gainspedetail")
	public @ResponseBody
	Map<String, Object> gainspedetail(HttpServletRequest request) {
		Integer docid = Integer.parseInt(request.getParameter("docid"));
		Map<String, Object> map = new HashMap<String, Object>();
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(docid);
		special.setVedioAmount(commonManager.gainMoneyByOrder(4, special.getSpecialId()));
		special.setAskAmount(commonManager.gainMoneyByOrder(1, special.getSpecialId()));
		special.setTelAmount(commonManager.gainMoneyByOrder(2, special.getSpecialId()));
		map.put("special", special);
		
		return map;
	}

	@RequestMapping(value = "/userlogin", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> userlogin(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String tel = request.getParameter("tel");
		String password = request.getParameter("password");
		UserAccountInfo user = weixinService
				.queryUserAccountInfoByMobilePhone(tel);
		if (user == null) {
			map.put("status", "error");
			map.put("message", "手机号未注册");
		} else {
			if (StringUtils.isNotBlank(user.getPasswordHashed())) {
				if (user.getPasswordHashed().equalsIgnoreCase(
						PasswordUtil.MD5Salt(password, user.getSalt()))) {
					map.put("status", "success");
					map.put("user", user);
				} else {
					map.put("status", "error");
					map.put("message", "密码错误");
				}
			} else {
				map.put("status", "nopass");
				map.put("message", "此手机号已经绑定了微信公众号，需要设置密码功能才能登陆");
			}
		}
		return map;
	}

	// 判断用户是否存在
	@RequestMapping(value = "/telluser", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> telluser(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String tel = request.getParameter("tel");
		UserAccountInfo user = weixinService
				.queryUserAccountInfoByMobilePhone(tel);
		if (user == null) {
			map.put("status", "success");
		} else {
			map.put("status", "error");
			map.put("message", "用户已存在");
		}
		return map;
	}

	@RequestMapping(value = "/gainVeryCode", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainVeryCode(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String telphone = request.getParameter("tel");
		String type = request.getParameter("ctype");// 1--注册 2--忘记密码
		String code = CheckNumUtil.randomChars(4);
		String content = "";
		if (type.equalsIgnoreCase("1")) {
			content = "感谢注册佰医汇，您的短信验证码是" + code + "，如非本人注册请忽略该短信【佰医汇】";
		} else {
			content = "您的短信验证码是" + code + "【佰医汇】";
		}
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

	// 用户注册
	@RequestMapping(value = "/userregister", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> userregister(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String code = request.getParameter("code");
		String tel = request.getParameter("tel");
		String password = request.getParameter("password");
		String ctype = request.getParameter("ctype");
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
				map.put("message", "验证码失效，请重新获取");
			} else {
				if ((tel + "" + code).equalsIgnoreCase(bindCode)) {
					if (ctype.equalsIgnoreCase("1")) {
						// 注册
						map.put("status", "success");
						UserAccountInfo u = weixinService
								.queryUserAccountInfoByMobilePhone(tel);
						if (u == null) {
							u = new UserAccountInfo();
							u.setLoginName(tel);
							u.setMobileNumber(tel);
							String salt = CheckNumUtil.randomChars_new(6);
							u.setSalt(salt);
							u.setPasswordHashed(PasswordUtil.MD5Salt(password,
									salt));
							u.setRegisterTime(new Date());
							u.setOrigin("官网");
							u.setStatus("1");
							Integer uid = weixinService.saveUserAccountInfo(u);
							map.put("uid", uid);
						} else {
							map.put("status", "error");
							map.put("message", "用户已存在");
						}
					} else if (ctype.equalsIgnoreCase("2")) {
						UserAccountInfo u = weixinService
								.queryUserAccountInfoByMobilePhone(tel);
						if (u == null) {
							map.put("status", "error");
							map.put("message", "用户不存在");
						} else {
							map.put("status", "success");
							String salt = CheckNumUtil.randomChars_new(6);
							u.setPasswordHashed(PasswordUtil.MD5Salt(password,
									salt));
							u.setSalt(salt);
							weixinService.updateUserAccountInfo(u);
						}
					}
				} else {
					map.put("status", "error");
					map.put("message", "验证码输入错误");
				}
			}
		} else {
			map.put("status", "error");
			map.put("message", "请输入验证码");
		}
		return map;
	}

	// 显示支付二维码
	@RequestMapping("/showqr")
	@ResponseBody
	public void getQRCode(String code_url, HttpServletResponse response) {
		encodeQrcode(code_url, response);
	}

	@SuppressWarnings("unchecked")
	public static void encodeQrcode(String content, HttpServletResponse response) {
		if (StringUtils.isBlank(content))
			return;
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 设置字符集编码类型
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = multiFormatWriter.encode(content,
					BarcodeFormat.QR_CODE, 300, 300, hints);
			BufferedImage image = MatrixToImageWriter
					.toBufferedImage(bitMatrix);
			// 输出二维码图片流
			try {
				ImageIO.write(image, "png", response.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (WriterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// 获取联系人
	@RequestMapping(value = "/gaincontactors/{uid}", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gaincontactors(@PathVariable Integer uid,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<UserContactInfo> contactors = weixinService
				.queryUserContactInfosByUserId(uid);
		map.put("contactors", contactors);
		return map;
	}

	// 新增联系人
	@RequestMapping(value = "/savenewcontactor", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> savenewcontactor(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		String username = request.getParameter("username");
		String tel = request.getParameter("tel");
		String idcard = request.getParameter("idcard");
		UserContactInfo con = weixinService.queryUserContactorByCondition(uid,
				username, tel);
		if (con == null) {
			map.put("status", "success");
			con = new UserContactInfo();
			con.setUserId(uid);
			con.setContactName(username);
			con.setCreateTime(new Timestamp(new Date().getTime()));
			con.setTelphone(tel);
			con.setStatus(1);
			con.setIdCard(idcard);
			Integer contactorid = weixinService.saveUserContactInfo(con);
			map.put("contactorid", contactorid);
		} else {
			map.put("status", "error");
			map.put("message", "联系人已存在");
		}
		return map;
	}

	// =============专家团队
	// 根据地方获取医院
	@RequestMapping(value = "/gainHospitalsByAddress", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainHospitalsByAddress(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String distcode = request.getParameter("distcode");
		List<HospitalDetailInfo> hospitals = weixinService
				.queryHospitalDetailsByDsitcode(distcode,1);
		map.put("hospitals", hospitals);
		return map;
	}

	// 获取所有标准科室
	@RequestMapping(value = "/gainStandDeps", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainStandDeps(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<StandardDepartmentInfo> sdeps = weixinService
				.queryStandardDepartments();
		map.put("sdeps", sdeps);
		return map;
	}

	// 获取专家团队数据---可做首页专家数据
	@RequestMapping(value = "/gainspecialdatas", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainspecialdatas(@ModelAttribute Pager pager,
			HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (pager == null)
			pager = new Pager();
		Map<String, String> querymap = new HashMap<String, String>();
		String distcode = request.getParameter("distcode");
		String hosid = request.getParameter("hosid");
		String sdepid = request.getParameter("sdepid");
		String duty = request.getParameter("duty");
		String stype = request.getParameter("stype");
		String keyword = request.getParameter("keyword");
		if (StringUtils.isNotBlank(keyword)) {
			querymap.put("queryseach", keyword);
		}
		if (StringUtils.isNotBlank(distcode)) {
			querymap.put("distcode", distcode);
		}
		if (StringUtils.isNotBlank(hosid)) {
			querymap.put("hosid", hosid);
		}
		if (StringUtils.isNotBlank(sdepid)) {
			querymap.put("sdepid", sdepid);
		}
		if (StringUtils.isNotBlank(duty)) {
			querymap.put("duty", duty);
		}
		if (!StringUtils.isNotBlank(stype)) {
			querymap.put("stype", "steam");// 类型专家团队
		} else {
			querymap.put("stype", stype);
		}
		querymap.put("openVedio", "false");
		querymap.put("recommond", "false");
		pager.setQueryBuilder(querymap);
		pager = kangxinService.searchspecialsByPager(pager);
		retMap.put("pager", pager);
		return retMap;
	}

	// 进入确认订单界面
	@RequestMapping(value = "/confirmorderandpay")
	public ModelAndView confirmorderandpay(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer expertId = Integer.parseInt(request.getParameter("expertId"));
		String localHosId = request.getParameter("localHosId");
		String localDepartId = request.getParameter("localDepartId");
		Integer stimeid = Integer.parseInt(request.getParameter("stimeid"));
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		ExpertConsultationSchedule sch = weixinService
				.queryExpertConScheduleById(stimeid);
		String week = gainWeek(DateUtil.dayForWeek(sch.getScheduleDate()));
		sch.setWeekDesc(week);
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(expertId);
		special.setVedioAmount(commonManager.gainMoneyByOrder(4, special.getSpecialId()));
		List<UserContactInfo> contactors = weixinService
				.queryUserContactInfosByUserId(uid);
		map.put("special", special);
		map.put("localHosId", localHosId);
		map.put("localDepartId", localDepartId);
		map.put("apptime", sch);
		map.put("contactors", contactors);
		return new ModelAndView("website/confirm_pay", map);
	}

	// 监听支付状态
	@RequestMapping(value = "/listenpaystatus", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> listenpaystatus(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String tradeNo = request.getParameter("tradeno");
		BusinessPayInfo payinfo=commonService.queryBusinessPayInfoByTradeNo(tradeNo);
		if (payinfo.getPayStatus() != null && payinfo.getPayStatus().equals(1)) {
			
			//远程门诊
			if(payinfo.getOrderType().equals(4)){
			// 支付成功		
				BusinessVedioOrder rc=commonService.queryBusinessVedioOrderById(payinfo.getOrderId());
				
				CaseInfo caseinfo=commonService.queryCaseInfoById(rc.getCaseId());
				MobileSpecial special = commonService
						.queryMobileSpecialByUserIdAndUserType(rc.getExpertId());
				HospitalDetailInfo hos = weixinService
						.queryHospitalDetailInfoById(rc.getLocalHospitalId());
				map.put("personName", caseinfo.getContactName());
				map.put("docName", special.getSpecialName());
				map.put("hosName", special.getHosName());
				map.put("mtime",
						rc.getConsultationDate() + " " + rc.getConsultationTime());
				map.put("cost", payinfo.getTotalMoney());
				map.put("hosAdd", hos.getDisplayName());
				map.put("status", "success");
				map.put("oid", rc.getId());
			} else if (payinfo.getOrderType().equals(5)){

				SpecialAdviceOrder rc=commonService.querySpecialAdviceOrderById(payinfo.getOrderId());
				CaseInfo caseinfo=commonService.queryCaseInfoById(rc.getCaseId());
				MobileSpecial special = commonService
						.queryMobileSpecialByUserIdAndUserType(rc.getExpertId());
				map.put("personName", caseinfo.getContactName());
				map.put("docName", special.getSpecialName());
				map.put("hosName", special.getHosName());
				map.put("cost", payinfo.getTotalMoney());
				map.put("status", "success");
				map.put("oid", rc.getId());
			} else {
				//未处理
			}
		} 
		else {
			map.put("status", "error");
		}
		return map;
	}
	
	
	// 提交反馈
	@RequestMapping(value = "/savefeedback", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> savefeedback(@ModelAttribute UserFeedBackInfo feedinfo,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		feedinfo.setCreateTime(new Date());
		feedinfo.setSource(4);
		feedinfo.setUserType(1);
		weixinService.saveUserFeedbackInfo(feedinfo);
		return map;
	}

	private String gainWeek(Integer d) {
		String str = "";
		switch (d) {
		case 0:
			str = "周日";
			break;
		case 1:
			str = "周一";
			break;
		case 2:
			str = "周二";
			break;
		case 3:
			str = "周三";
			break;
		case 4:
			str = "周四";
			break;
		case 5:
			str = "周五";
			break;
		case 6:
			str = "周六";
			break;
		default:
			break;
		}
		return str;
	}

	// =======================================================图文问诊=================================
	// 判断该用户是否还有未结束的图文订单
	@RequestMapping(value = "/existnoclosed", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> existnoclosed(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		Boolean b = wenzhenService.queryExistNoClosedTwOrder(uid);
		map.put("existornot", b);
		return map;
	}

	// 获取历史病例
	@RequestMapping(value = "/gainhiscases", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainhiscases(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		List<CaseInfo> cases = wenzhenService.queryHisCaseInfosByUId(uid);
		for (CaseInfo _case : cases) {
			// 查询病例图片
			List<CustomFileStorage> images = wenzhenService
					.queryCustomFilesByCaseIds(_case.getNormalImages());
			_case.setCaseImages(images);
		}
		map.put("cases", cases);
		return map;
	}

	// 上传病例图片
	@RequestMapping(value = "/uploadCaseFile", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> uploadCaseFile(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String rootDir=year_sdf.format(new Date())+"/"+month_sdf.format(new Date())+"/"+day_sdf.format(new Date());
		Map<String, Object> ret_map = OSSManageUtil.uploadFile_in(rootDir,
				new OSSConfigure(), request);
		CaseImages ci = new CaseImages();
		ci.setUrl((String) ret_map.get("urlpath"));
		Integer cuid = wenzhenService.saveCaseImages(ci);
		map.put("upid", cuid);
		map.put("urlpath", ret_map.get("urlpath"));
		return map;
	}

	// 提交订单
	@RequestMapping(value = "/subtworder", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> subtworder(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		// 病例信息
		Map<String, Object> cmap = createOrGainCase(request, uid);
		// 图文订单
		Map<String, Object> retmap = createTwOrder(request, uid,
				Integer.parseInt(cmap.get("caseid").toString()), response);
		map.putAll(retmap);
		return map;
	}

	// 图文订单
	private Map<String, Object> createTwOrder(HttpServletRequest request,
			Integer uid, Integer caseid, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer docid = Integer.parseInt(request.getParameter("docid"));// 专家id
		DoctorDetailInfo doc = commonService.queryDoctorDetailInfoById(docid);
		doc.setAskAmount(commonManager.gainMoneyByOrder(1, doc.getId()));
		boolean pay = false;
		if (doc.getAskAmount() != null
				&& doc.getAskAmount().compareTo(BigDecimal.ZERO) > 0) {
			// 专家设置了价格
			// 判断一周之内该用户有没有下过下专家的单
			pay = true;
		}
		// 生成订单
		Integer oid = generateTwOrder(request, uid, caseid, docid, pay);
		if (pay) {
			// 支付
			UUID uuid = UUID.randomUUID();
			String product_id = uuid.toString().replace("-", "");
			String ret = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/orderPrefix_zaixian");
			String twbody=ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/twbody");
			log.info("===tbody==="+twbody);
			Map<String, Object> retMap = WeixinUtil.weipay_pc(request,
					response, doc.getAskAmount().floatValue(), PropertiesUtil
							.getString("APPID"), PropertiesUtil
							.getString("APPSECRET"), PropertiesUtil
							.getString("PARTNER"), PropertiesUtil
							.getString("PARTNERKEY"),
							twbody, product_id,
					PropertiesUtil.getString("DOMAINURL")
							+ "kangxin/wenzhennotify", null, null, ret);
			Integer pid=payOrderManager.savePayInfo(1, oid, retMap.get("out_trade_no").toString(), doc.getAskAmount().floatValue(), 2, doc.getAskAmount().floatValue(), 0.0f, 0.0f, 0.0f, null);
			map.put("code_url", retMap.get("code_url"));
			map.put("out_trade_no", retMap.get("out_trade_no"));
			try {
				RecordLogUtil.insert("1", "图文问诊", oid+"",pid+"",
						(String) retMap.get("paramxml"),
						(String) retMap.get("prepayxml"), "",retMap.get("out_trade_no").toString());
			} catch (Exception e) {

			}
		}
		map.put("payStatus", pay);
		map.put("oid", oid);
		return map;
	}

	private Integer generateTwOrder(HttpServletRequest request, Integer uid,
			Integer caseid, Integer docid, Boolean pay) {
		BusinessTuwenOrder info=new BusinessTuwenOrder();
		info.setCaseId(caseid);
		info.setDoctorId(docid);
		info.setUserId(uid);
		info.setCreateTime(new Timestamp(System.currentTimeMillis()));
		info.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());;
		info.setDelFlag(0);
		info.setSource(4);
		if (pay) {
			info.setPayStatus(4);
		} else {
			info.setPayStatus(0);
		}
		info.setUuid(commonManager.generateUUID(1));
		return wenzhenService.saveBusinessTuwenOrder(info);
	}


	// 处理病例
	private Map<String, Object> createOrGainCase(HttpServletRequest request,
			Integer uid) {
		Map<String, Object> map = new HashMap<String, Object>();
		String caseName = request.getParameter("caseName");
		String cname = request.getParameter("contactName");
		String idcard = request.getParameter("idcard");
		String desc = request.getParameter("description");
		String tel = request.getParameter("telephone");
		// 新建病例
		CaseInfo info = new CaseInfo();
		String age = request.getParameter("age");
		if (StringUtils.isNotBlank(age)) {
			info.setAge(Integer.parseInt(age));
		}
		String sex = request.getParameter("sex");
		if (StringUtils.isNotBlank(sex)) {
			info.setSex(Integer.parseInt(sex));
		}
		info.setUuid(UUID.randomUUID().toString().replace("-", ""));
		info.setUserId(uid);
		info.setCaseName(caseName);
		info.setContactName(cname);
		info.setIdNumber(idcard);
		//info.setDescription(desc);
		info.setPresentIll(desc);
		info.setTelephone(tel);
		info.setCreateTime(new Date());
		info.setNormalImages(request.getParameter("caseImages"));
		info.setAskProblem(request.getParameter("askProblem"));
		Integer cid = wenzhenService.saveCaseInfo(info);
		map.put("caseid", cid);
		return map;
	}

	// 图文支付回调
	@RequestMapping(value = "/wenzhennotify", method = RequestMethod.POST)
	public void wenzhennotify(HttpServletRequest request,
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
		log.info("===购买在线订单ret=====" + result);
		payLog.info("{'ret':'"+result+"','desc':'在线问诊订单回调返回值','time':'"+sdf.format(new Date())+"'}");
		// 修改订单状态---已支付
		payCallBackManage.updateWenzhenNotifyOrder(result);
		response.getWriter().write(setXML("SUCCESS", ""));
	}

	// 监听图文支付状态
	@RequestMapping(value = "/listenpaystatus_tw", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> listenpaystatus_tw(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String tradeNo = request.getParameter("tradeno");
		BusinessPayInfo rc=commonService.queryBusinessPayInfoByTradeNo(tradeNo);
		if (rc.getPayStatus() != null && rc.getPayStatus().equals(1)) {
			map.put("status", "success");
			map.put("oid", rc.getId());
		} else {
			map.put("status", "error");
		}
		return map;
	}

	

	private String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}

	// //////////////////////////////////个人中心/////////////////////////////
	/**
	 * 获取图文订单列表--含分页及更多。。
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gaingraphics", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gaingraphics(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer uid = Integer.parseInt(request.getParameter("uid"));// 用户id
		String pageNo = request.getParameter("pageNo");
		Integer _pageNo = 1;
		Integer _pageSize = 10;
		String pageSize = request.getParameter("pageSize");
		if (StringUtils.isNotBlank(pageSize)) {
			_pageSize = Integer.parseInt(pageSize);
		}
		if (StringUtils.isNotBlank(pageNo)) {
			_pageNo = Integer.parseInt(pageNo);
		}
		// 查询问诊列表
		List<WenzhenBean> orders = wenzhenService.queryBusinessWenzhensByUId(
				uid, _pageNo, _pageSize);
		for (WenzhenBean _bean : orders) {
			_bean.setSubTime(sdf.format(_bean.getCreateTime()));
		}
		map.put("orders", orders);
		return map;
	}

	/**
	 * 获取图文消息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gaingraphicmessages", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gaingraphicmessages(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		BusinessTuwenOrder twinfo=wenzhenService.queryBusinessTuwenInfoById(oid);
		map.put("twinfo", twinfo);
		// 获取消息
		List<BusinessMessageBean> messages=wenzhenService.queryBusinessMessageBeansByCon(oid,twinfo.getUuid(), 1);
		map.put("oid", oid);
		map.put("messages", messages);
		twinfo.setPatUnreadMsgNum(0);
		wenzhenService.updateBusinessTuwenOrder(twinfo);
		return map;
	}

	/**
	 * 追加消息提问
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/appendmessage", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> appendmessage(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 问诊id
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		String msgContent = request.getParameter("msgContent");
		BusinessTuwenOrder order=wenzhenService.queryBusinessTuwenInfoById(oid);
		BusinessMessageBean message=new BusinessMessageBean();
		message.setOrderId(order.getId());
		message.setOrderType(1);
		message.setMsgType("text");
		message.setMsgContent(msgContent);
		message.setRecvId(order.getDoctorId());
		message.setRecvType(2);
		message.setSendId(uid);
		message.setSendTime(new Timestamp(System.currentTimeMillis()));
		message.setSendType(1);
		message.setStatus(1);
		wenzhenService.saveBusinessMessageBean(message);
		
		order.setPatLastAnswerTime(new Timestamp(System.currentTimeMillis()));
		order.setPatRecvMsgCount(order.getPatRecvMsgCount()==null?1:(order.getPatRecvMsgCount()+1));
		order.setDocUnreadMsgNum(order.getDocUnreadMsgNum()==null?1:(order.getDocUnreadMsgNum()+1));
		wenzhenService.updateBusinessTuwenOrder(order);
		
		MobileSpecial special=commonService.queryMobileSpecialByUserIdAndUserType(order.getDoctorId());
		HttpSendSmsUtil.sendSmsInteface("13521231353,15001299884", "患者向专家"+special.getSpecialName()+"发起了一条提问。请及时回复！【佰医汇】");
		return map;
	}

	// ===========================================电话问诊============================

	@RequestMapping(value = "/subtelorder", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> subtelorder(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		// 病例信息
		Map<String, Object> cmap = createOrGainCase(request, uid);
		// 电话问诊订单
		Map<String, Object> retmap = createtelOrder(request, uid,
				Integer.parseInt(cmap.get("caseid").toString()), response);
		map.putAll(retmap);
		return map;
	}

	public Map<String, Object> createtelOrder(HttpServletRequest request,
			Integer uid, Integer caseid, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer docid = Integer.parseInt(request.getParameter("docid"));
		DoctorDetailInfo doc = commonService.queryDoctorDetailInfoById(docid);
		doc.setTelAmount(commonManager.gainMoneyByOrder(2, doc.getId()));
		boolean pay = false;
		if (doc.getTelAmount() != null
				&& doc.getTelAmount().compareTo(BigDecimal.ZERO) > 0) {
			pay = true;
		}
		// 生成订单
		Integer oid = generateTelOrder(request, uid, docid, caseid, pay);
		if (pay) {
			// 支付
			UUID uuid = UUID.randomUUID();
			String product_id = uuid.toString().replace("-", "");
			String ret = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/orderPrefix_zaixian");
			Map<String, Object> retMap = WeixinUtil.weipay_pc(request,
					response, doc.getTelAmount().floatValue(), PropertiesUtil
							.getString("APPID"), PropertiesUtil
							.getString("APPSECRET"), PropertiesUtil
							.getString("PARTNER"), PropertiesUtil
							.getString("PARTNERKEY"),
					ReaderConfigUtil.gainConfigVal(request, "basic.xml",
							"root/telbody"), product_id,
					PropertiesUtil.getString("DOMAINURL")
							+ "kangxin/wenzhennotify", null, null, ret);
			Integer pid=payOrderManager.savePayInfo(2, oid, retMap.get("out_trade_no").toString(), doc.getTelAmount().floatValue(), 2, doc.getTelAmount().floatValue(), 0.0f, 0.0f, 0.0f, null);
			map.put("code_url", retMap.get("code_url"));
			map.put("out_trade_no", retMap.get("out_trade_no"));
			
			try {
				RecordLogUtil.insert("1", "电话问诊", oid+"",pid+"",
						(String) retMap.get("paramxml"),
						(String) retMap.get("prepayxml"), "",retMap.get("out_trade_no").toString());
			} catch (Exception e) {

			}
		}
		map.put("payStatus", pay);
		map.put("oid", oid);
		return map;
	}

	private Integer generateTelOrder(HttpServletRequest request, Integer uid,
			Integer docid, Integer caseid, Boolean pay) {
		BusinessTelOrder telinfo=new BusinessTelOrder();
		telinfo.setCaseId(caseid);
		telinfo.setDoctorId(docid);
		telinfo.setUserId(uid);
		telinfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		telinfo.setSource(4);
		telinfo.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());// 等待确认
		if (pay) {
			telinfo.setPayStatus(4);
		} else {
			telinfo.setPayStatus(0);
		}
		telinfo.setUuid(commonManager.generateUUID(2));
		
		return wenzhenService.saveBusinessTelOrder(telinfo);
	}

	// --------------------患者待支付点击继续支付-------------
	@RequestMapping(value = "/continuepay", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> continuepay(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		Integer otype = Integer.parseInt(request.getParameter("otype"));// 订单类型
																		// 图文订单-1
																		// ，电话订单-2
		BigDecimal money = null;
		String desc = "";
		Integer uid = null;
		Integer docid = null;
		if (otype.equals(1)) {
			BusinessTuwenOrder info=wenzhenService.queryBusinessTuwenInfoById(oid);
			DoctorDetailInfo doc = commonService.queryDoctorDetailInfoById(info
					.getDoctorId());
			doc.setAskAmount(commonManager.gainMoneyByOrder(1, doc.getId()));
			money = doc.getAskAmount();
			desc = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/twbody");
			uid = info.getUserId();
			docid = info.getDoctorId();
		} else if (otype.equals(2)) {
			BusinessTelOrder telinfo=wenzhenService.queryBusinessTelOrderById(oid);
			DoctorDetailInfo doc = commonService
					.queryDoctorDetailInfoById(telinfo.getDoctorId());
			doc.setTelAmount(commonManager.gainMoneyByOrder(2, doc.getId()));
			money = doc.getTelAmount();
			desc = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/telbody");
			uid = telinfo.getUserId();
			docid = telinfo.getDoctorId();
		}
		UUID uuid = UUID.randomUUID();
		String product_id = uuid.toString().replace("-", "");
		Map<String, Object> retMap = WeixinUtil
				.weipay_pc(request, response, money.floatValue(),
						PropertiesUtil.getString("APPID"), PropertiesUtil
								.getString("APPSECRET"), PropertiesUtil
								.getString("PARTNER"), PropertiesUtil
								.getString("PARTNERKEY"), desc, product_id,
						PropertiesUtil.getString("DOMAINURL")
								+ "kangxin/wenzhennotify", null, oid,
						ReaderConfigUtil.gainConfigVal(request, "basic.xml",
								"root/orderPrefix_zaixian"));
		map.put("code_url", retMap.get("code_url"));
		map.put("out_trade_no", retMap.get("out_trade_no"));
		Integer pid=payOrderManager.savePayInfo(otype, oid, retMap.get("out_trade_no").toString(), money.floatValue(), 2, money.floatValue(), 0.0f, 0.0f, 0.0f, null);
		map.put("money", money);
		try {
			RecordLogUtil.insert(otype+"", desc, oid+"",pid+"",
					(String) retMap.get("paramxml"),
					(String) retMap.get("prepayxml"), "",retMap.get("out_trade_no").toString());
		} catch (Exception e) {

		}
		return map;
	}

	// ----------------电话订单
	@RequestMapping(value = "/gaintelorders", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gaintelorders(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		List<WenzhenBean> orders = wenzhenService
				.queryBusinessWenZhenTelUid(uid);
		for (WenzhenBean _bean : orders) {
			_bean.setSubTime(sdf.format(_bean.getCreateTime()));
		}
		map.put("orders", orders);
		return map;
	}

	@RequestMapping(value = "/gainnewforum", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainnewforum(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorForum forum = commonService.querynewforum();
		map.put("forum", forum);
		return map;
	}
	
	//===========================================医联体模块=================================
	/**
	 * 获取医联体区域
	 * 访问：http://localhost:8080/kangxin/gainAllianceArea
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainAllianceArea")
	@ResponseBody
	public Map<String,Object> gainAllianceArea(HttpServletRequest request){
		return kangxinManager.gainAllianceArea();
	}
	/**
	 * 根据区域获取医联体
	 * 访问:http://localhost:8080/kangxin/gainAllianceByArea
	 * 参数：distCode:区域编码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainAllianceByArea")
	@ResponseBody
	public Map<String,Object> gainAllianceByArea(HttpServletRequest request){
		String distCode=request.getParameter("distCode");
		return kangxinManager.gainAllianceByArea(distCode);
	}
	/**
	 * 获取医联体详情
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainAllianceDetailInfo")
	@ResponseBody
	public Map<String,Object> gainAllianceDetailInfo(HttpServletRequest request){
		Integer allianceId=Integer.parseInt(request.getParameter("allianceId"));
		Map<String,Object> map=new HashMap<String,Object>();
		HospitalHealthAlliance alliance=kangxinManager.gainAllianceDetailInfo(allianceId);
		map.put("alliance", alliance);
		return map;
	}
	/**
	 * 加载医联体医生数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/loadAllianceDoctors")
	@ResponseBody
	public Map<String,Object> loadAllianceDoctors(@ModelAttribute Pager pager,HttpServletRequest request){
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (pager == null)
			pager = new Pager();
		String allianceId=request.getParameter("allianceId");
		Map<String, String> querymap = new HashMap<String, String>();
		if (StringUtils.isNotBlank(allianceId)) {
			querymap.put("allianceId", allianceId);
		}
		pager.setQueryBuilder(querymap);
		pager = commonService.queryAllianceDoctorsPager(pager);
		retMap.put("pager", pager);
		return retMap;
	}
}
