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
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.entity.ExpertConsultationSchedule;
import com.tspeiz.modules.common.entity.MedicalRecords;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessOrderInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenZhenInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenzhenTel;
import com.tspeiz.modules.common.entity.newrelease.CaseImages;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.Dictionary;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.UserAccountInfo;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;
import com.tspeiz.modules.common.entity.newrelease.UserFeedBackInfo;
import com.tspeiz.modules.common.entity.newrelease.UserWeixinRelative;
import com.tspeiz.modules.common.entity.weixin.DoctorEstimateInfo;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.common.thread.TokenThread;
import com.tspeiz.modules.util.CheckNumUtil;
import com.tspeiz.modules.util.DataCatchUtil;
import com.tspeiz.modules.util.DateUtil;
import com.tspeiz.modules.util.EmojiFilterUtil;
import com.tspeiz.modules.util.IdcardUtils;
import com.tspeiz.modules.util.LocationUtil;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.PythonVisitUtil;
import com.tspeiz.modules.util.common.GeneUserBillRecordUtil;
import com.tspeiz.modules.util.common.StringRetUtil;
import com.tspeiz.modules.util.common.TwAndTelCommonPayUtil;
import com.tspeiz.modules.util.config.ReaderConfigUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.oss.OSSConfigure;
import com.tspeiz.modules.util.oss.OSSManageUtil;
import com.tspeiz.modules.util.weixin.CommonUtil;
import com.tspeiz.modules.util.weixin.SignUtil;
import com.tspeiz.modules.util.weixin.WeixinUtil;

/**
 * 改版 --20160908
 * 
 * @author heyongb
 * 
 */
@Controller
@RequestMapping("twzjh")
public class MainController_20160908 {
	private Logger log = Logger.getLogger(MainController_20160908.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat code_time = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat _sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private SimpleDateFormat dir_time = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat _format = new SimpleDateFormat("yyyy-MM-dd");
	@Resource
	private IWeixinService weixinService;
	@Resource
	private ICommonService commonService;
	@Resource
	private IWenzhenService wenzhenService;

	/**
	 * 远程门诊进入主界面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		openid = gainOpenIdByConditions(request, openid);
		map.put("openid", openid);
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		map.put("ltype", 4);// 远程
		return new ModelAndView("zjh/main", map);
	}

	/**
	 * 远程门诊-定位
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gainCityByLocation", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainCityByLocation(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String latitude = request.getParameter("latitude");
		String longitude = request.getParameter("longitude");
		String city = "";
		String distcode = "";
		if (StringUtils.isNotBlank(latitude)
				&& StringUtils.isNotBlank(longitude)) {
			city = LocationUtil
					.gainLocationString(Double.parseDouble(latitude),
							Double.parseDouble(longitude));
			HospitalDetailInfo cd = weixinService
					.queryCooHospitalInfoByCity(city);
			if (cd != null)
				distcode = cd.getDistCode();
		}
		map.put("distcode", distcode);
		map.put("city", city);
		return map;
	}

	/**
	 * 获取开通的城市
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gainopencitys", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainopencitys(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ReSourceBean> beans = weixinService.queryOpenCitys();
		map.put("beans", beans);
		return map;
	}

	/**
	 * 获取开通城市的医院
	 */
	@RequestMapping(value = "/gainhosbycity", method = RequestMethod.GET)
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
	 * 获取当地医院科室
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gaindepartsbyhos", method = RequestMethod.GET)
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
	 * 远程门诊-获取当地科室对应的专家数据
	 * 
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
	 * 远程门诊-选择专家，进入选择预约时间界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/remoteDocDetail", method = RequestMethod.GET)
	public ModelAndView remoteDocDetail(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer sid = Integer.parseInt(request.getParameter("sid"));// 专家id
		String depid = request.getParameter("depid");
		String openid = request.getParameter("openid");
		map.put("openid", openid);
		map.put("depid", depid);
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(sid);
		map.put("special", special);
		map.put("ltype", 4);
		return new ModelAndView("zjh/3.0/remote/remote_doc_detail", map);
	}

	/**
	 * 获取所选专家的出诊时间数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gainSpecialTimes", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainSpecialTimes(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer sid = Integer.parseInt(request.getParameter("sid"));// 专家id
		String sdate = request.getParameter("sdate");// 选择的日期
		List<ExpertConsultationSchedule> times = weixinService
				.queryExpertConTimeSchedulsByConditions(sid, sdate);
		map.put("times", times);
		return map;
	}

	/**
	 * 远程门诊--选择时间后下一步进入确认信息界面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/suretoconfirm")
	public ModelAndView suretoconfirm(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer ltype = Integer.parseInt(request.getParameter("ltype"));// 类型
		String openid = request.getParameter("openid");
		map.put("openid", openid);
		String redirectUrl = "";
		switch (ltype) {
		case 1:// 图文
		case 2:// 电话
			Integer docid = Integer.parseInt(request.getParameter("docid"));
			map.put("docid", docid);
			redirectUrl = "/suretoconfirm?docid=" + docid + "&ltype=" + ltype
					+ "&openid=" + openid;
		case 4:
			String splevel = request.getParameter("levelid");// 子项目远程会诊中的选择了专家级别
			if (StringUtils.isNotBlank(splevel)) {
				// 远程会诊
				String hospitalId = request.getParameter("hosid");
				String depid = request.getParameter("depid");
				String timeid = request.getParameter("timeid");
				map.put("hosid", hospitalId);
				map.put("depid", depid);
				map.put("splevel", splevel);
				map.put("timeid", timeid);
				map.put("openid", openid);
				redirectUrl = "/suretoconfirm?openid=" + openid + "&hosid="
						+ hospitalId + "&depid=" + depid + "&splevel="
						+ splevel + "&timeid=" + timeid + "&ltype=4";
			} else {
				// 远程门诊
				Integer sid = Integer.parseInt(request.getParameter("sid"));// 专家id
				Integer departId = Integer.parseInt(request
						.getParameter("departId"));// 当地医院科室id
				Integer stimeid = Integer.parseInt(request
						.getParameter("stimeid"));// 选择的时间id
				map.put("sid", sid);
				map.put("departId", departId);
				map.put("stimeid", stimeid);
				redirectUrl = "/suretoconfirm?openid=" + openid + "&departId="
						+ departId + "&stimeid=" + stimeid + "&sid=" + sid
						+ "&ltype=4";
			}
			break;
		}
		UserWeixinRelative ur = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		if (ur == null) {
			// 未绑定公号-进入绑定界面
			map.put("vurl", redirectUrl);
			return new ModelAndView("zjh/3.0/bind_tel", map);
		}
		List<UserContactInfo> conInfos = weixinService
				.queryUserContactInfosByUserId(ur.getUserId());
		map.put("conInfos", conInfos);
		map.put("ltype", ltype);
		return new ModelAndView("zjh/3.0/user/user_case", map);
	}

	/**
	 * 进入下一步支付界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/nexttopay", method = RequestMethod.POST)
	public ModelAndView nexttopay(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		map.put("openid", openid);
		Integer ltype = Integer.parseInt(request.getParameter("ltype"));
		String contactId = request.getParameter("contactId");// 联系人id
		map.put("contactId", contactId);
		switch (ltype) {
		case 1:// 图文
		case 2:// 电话
			map.putAll(showtwandtelinfo(request));
			break;
		case 4:
			// 远程
			map.putAll(showremoteinfo(request));
			break;
		}
		map.put("caseName", request.getParameter("caseName"));
		map.put("description", request.getParameter("description"));
		map.put("caseImages", request.getParameter("caseImages"));
		return new ModelAndView("zjh/3.0/topay", map);
	}

	/**
	 * 显示远程订单信息
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, Object> showremoteinfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String levelid = request.getParameter("levelid");
		if (StringUtils.isNotBlank(levelid)) {
			String timeid = request.getParameter("timeid");
			String hospitalId = request.getParameter("hosid");
			String depid = request.getParameter("depid");
			// 远程会诊
			HospitalDetailInfo hospital = weixinService
					.queryHospitalDetailInfoById(Integer.parseInt(hospitalId));
			HospitalDepartmentInfo depart = weixinService
					.queryHospitalDepartmentInfoById(Integer.parseInt(depid));
			map.put("hospital", hospital);
			map.put("depart", depart);
			Dictionary level = weixinService.queryDictionaryById(Integer
					.parseInt(levelid));
			Dictionary time = weixinService.queryDictionaryById(Integer
					.parseInt(timeid));
			map.put("level", level);
			map.put("time", time);
			map.put("hosid", hospital.getId());
			map.put("depid", depid);
			map.put("levelid", levelid);
			map.put("money", level.getDisplayValue());

		} else {
			// 远程门诊
			Integer sid = Integer.parseInt(request.getParameter("sid"));// 专家id
			Integer departId = Integer.parseInt(request
					.getParameter("departId"));// 当地医院科室id
			Integer stimeid = Integer.parseInt(request.getParameter("stimeid"));// 选择的时间id
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
			map.put("special", special);
			map.put("sid", sid);
			map.put("depid", departId);
			map.put("hosid", hospital.getId());
			map.put("money", special.getVedioAmount());
		}
		return map;
	}

	private Map<String, Object> showtwandtelinfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer docid = Integer.parseInt(request.getParameter("docid"));// 选择的专家
		Integer ltype = Integer.parseInt(request.getParameter("ltype"));
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(docid);
		map.put("special", special);
		if (ltype.equals(1)) {
			map.put("money", special.getAskAmount());
		} else if (ltype.equals(2)) {
			map.put("money", special.getTelAmount());
		}
		map.put("docid", docid);
		return map;
	}

	/**
	 * 确认支付--远程订单，在线订单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/suretopay", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> suretopay(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer ltype = Integer.parseInt(request.getParameter("ltype"));
		switch (ltype) {
		case 1:// 图文
		case 2:// 电话
			map.putAll(payonline(request, response));
			break;
		case 4:
			map.putAll(payremote(request, response));
			break;
		}
		return map;
	}

	/**
	 * 远程订单--远程门诊-远程问诊
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> payremote(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String levelid = request.getParameter("levelid");
		RemoteConsultation rc = null;
		Float money = null;
		String body = "";
		if (StringUtils.isNotBlank(levelid)) {
			// 远程会诊
			Dictionary level = weixinService.queryDictionaryById(Integer
					.parseInt(levelid));
			money = Float.valueOf(level.getDisplayValue());
			body = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/remotesubbody");
		} else {
			// 远程门诊
			Integer sid = Integer.parseInt(request.getParameter("sid"));// 专家id
			MobileSpecial special = commonService
					.queryMobileSpecialByUserIdAndUserType(sid);
			money = special.getVedioAmount().floatValue();
			body = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/remotebody");
		}
		rc = generateRemoteOrder(request, money);
		Map<String, Object> retMap = WeixinUtil.weipay(request, response,
				money, PropertiesUtil.getString("APPID"),
				PropertiesUtil.getString("APPSECRET"),
				PropertiesUtil.getString("PARTNER"),
				PropertiesUtil.getString("PARTNERKEY"), body,
				request.getParameter("openid"),
				PropertiesUtil.getString("DOMAINURL") + "wzjh/remotenotify",
				null, null, null);
		rc.setOutTradeNo(retMap.get("out_trade_no").toString());
		weixinService.updateRemoteConsultation(rc);
		// 新建门诊病历
		String caseName = request.getParameter("caseName");
		String desc = request.getParameter("description");
		MedicalRecords record = new MedicalRecords();
		record.setCaseName(caseName);
		record.setMainSuit(desc);
		record.setOrderId(rc.getId());
		commonService.saveMedicalRecords(record);
		return retMap;
	}

	private Map<String, Object> payonline(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer ltype = Integer.parseInt(request.getParameter("ltype"));
		String openid = request.getParameter("openid");
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		CaseInfo caseinfo = createCaseInfo(request, uwr.getUserId());// 生成病例
		processCaseImages(request, caseinfo.getId());// 处理图片
		Map<String, Object> retmap = createTwOrTelOrder(request,
				uwr.getUserId(), caseinfo.getId(), response, ltype, openid);
		map.putAll(retmap);
		return map;
	}

	/**
	 * 生成远程订单
	 * 
	 * @param request
	 * @param money
	 * @return
	 */
	private RemoteConsultation generateRemoteOrder(HttpServletRequest request,
			Float money) {
		RemoteConsultation rc = new RemoteConsultation();
		String levelid = request.getParameter("levelid");
		String openid = request.getParameter("openid");
		if (StringUtils.isNotBlank(levelid)) {
			// 远程会诊
			String timeid = request.getParameter("timeid");
			Dictionary level = weixinService.queryDictionaryById(Integer
					.parseInt(levelid));
			Dictionary time = weixinService.queryDictionaryById(Integer
					.parseInt(timeid));
			rc.setConsultationDate(time.getDisplayName());
			rc.setExLevel(level.getDisplayName());
			rc.setRemoteSub(1);// 远程会诊标识
		} else {
			// 远程门诊
			String stimeid = request.getParameter("stimeid");
			if (StringUtils.isNotBlank(stimeid)) {
				rc.setSchedulerDateId(Integer.parseInt(stimeid));
				ExpertConsultationSchedule ecs = weixinService
						.queryExpertConScheduleById(Integer.parseInt(stimeid));
				rc.setConsultationDate(ecs.getScheduleDate());
				rc.setConsultationTime(ecs.getStartTime());
			}
			rc.setExpertId(Integer.parseInt(request.getParameter("sid")));
		}
		rc.setLocalHospitalId(Integer.parseInt(request.getParameter("hosid")));
		rc.setLocalDepartId(Integer.parseInt(request.getParameter("depid")));
		rc.setCreateTime(new Date());
		rc.setRefreshTime(new Date());
		rc.setConsultationDur(15);
		Integer contactId = Integer.parseInt(request.getParameter("contactId"));
		UserContactInfo ucon = weixinService
				.queryUserContactorInfoById(contactId);
		rc.setPatientName(ucon.getContactName());
		rc.setTelephone(ucon.getTelphone());
		rc.setIdCard(ucon.getIdCard());
		rc.setSex(ucon.getSex());
		rc.setAge(ucon.getAge());
		rc.setAmount(new BigDecimal(money));
		rc.setPayMode(2);
		rc.setOpenId(openid);
		rc.setOutTradeNo("");
		rc.setStatus(0);
		rc.setLocalPlusNo("123");
		rc.setSource(1);
		rc.setPayStatus(0);
		String imgstr = request.getParameter("caseImages");
		rc.setNormalImages(imgstr);
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		rc.setUserId(uwr.getUserId());
		Integer oid = weixinService.saveRemoteConsultation(rc);
		rc.setId(oid);
		return rc;
	}

	private CaseInfo createCaseInfo(HttpServletRequest request, Integer userid) {
		String caseName = request.getParameter("caseName");
		String desc = request.getParameter("description");
		Integer contactId = Integer.parseInt(request.getParameter("contactId"));
		UserContactInfo user = weixinService
				.queryUserContactorInfoById(contactId);
		// 新建病例
		CaseInfo info = new CaseInfo();
		TwAndTelCommonPayUtil.caseInfoData(info, request, userid, caseName,
				user.getContactName(), user.getIdCard(), desc,
				user.getTelphone());
		Integer cid = wenzhenService.saveCaseInfo(info);
		info.setId(cid);
		return info;
	}

	/**
	 * 处理病例图片
	 * 
	 * @param request
	 * @param cmap
	 */
	private void processCaseImages(HttpServletRequest request, Integer caseid) {
		String imgstr = request.getParameter("caseImages");
		if (StringUtils.isNotBlank(imgstr)) {
			String[] strs = imgstr.split(",");
			if (strs != null && strs.length > 0) {
				for (String _str : strs) {
					CaseImages ci = wenzhenService.queryCaseImagesById(Integer
							.parseInt(_str));
					if (ci.getCaseId() != null) {
						if (!ci.getCaseId().equals(caseid)) {
							CaseImages _ci = new CaseImages();
							_ci.setCaseId(caseid);
							_ci.setUrl(ci.getUrl());
							wenzhenService.saveCaseImages(_ci);
						}
					} else {
						ci.setCaseId(caseid);
						wenzhenService.updateCaseImages(ci);
					}
				}
			}
		}
	}

	/**
	 * 创建图文或电话订单
	 * 
	 * @param request
	 * @param uid
	 * @param caseid
	 * @param response
	 * @param ltype
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> createTwOrTelOrder(HttpServletRequest request,
			Integer uid, Integer caseid, HttpServletResponse response,
			Integer ltype, String openid) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ltype", ltype);
		Integer docid = Integer.parseInt(request.getParameter("docid"));// 专家id
		DoctorDetailInfo doc = commonService.queryDoctorDetailInfoById(docid);
		boolean pay = false;
		Integer oid = null;
		String desc = "";
		BigDecimal money = null;
		/*if (ltype.equals(1)) {
			// 图文问诊
			if (doc.getAskAmount() != null
					&& doc.getAskAmount().compareTo(BigDecimal.ZERO) > 0) {
				pay = true;
			}
			// 生成订单
			oid = generateTwOrder(request, uid, caseid, docid, pay);
			desc = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/twbody");
			money = doc.getAskAmount();
		} else {
			// 电话问诊
			if (doc.getTelAmount() != null
					&& doc.getTelAmount().compareTo(BigDecimal.ZERO) > 0) {
				pay = true;
			}
			// 生成订单
			oid = generateTelOrder(request, uid, docid, caseid, pay);
			desc = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/telbody");
			money = doc.getTelAmount();
		}*/
		if (pay) {
			// 支付
			String ret = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/orderPrefix_zaixian");
			// 创建支付order
			BusinessOrderInfo payinfo = new BusinessOrderInfo();
			TwAndTelCommonPayUtil.payOrderInfo(payinfo, oid, money, ltype, 4,
					uid, docid, "");
			Integer boid = wenzhenService.saveBusinessOrderInfo(payinfo);
			payinfo.setId(boid);
			/*Map<String, Object> retMap = WeixinUtil.weipay(request, response,
					doc.getAskAmount().floatValue(),
					PropertiesUtil.getString("APPID"),
					PropertiesUtil.getString("APPSECRET"),
					PropertiesUtil.getString("PARTNER"),
					PropertiesUtil.getString("PARTNERKEY"), desc, openid,
					PropertiesUtil.getString("DOMAINURL")
							+ "kangxin/wenzhennotify", null, boid, ret);
			payinfo.setFlowNumber(retMap.get("out_trade_no").toString());
			wenzhenService.updateBusinessOrderInfo(payinfo);
			map.putAll(retMap);*/
		}
		map.put("payStatus", pay);
		return map;
	}

	private Integer generateTwOrder(HttpServletRequest request, Integer uid,
			Integer caseid, Integer docid, Boolean pay) {
		BusinessWenZhenInfo info = new BusinessWenZhenInfo();
		info.setOpenId(request.getParameter("openid"));
		/*TwAndTelCommonPayUtil.twOrderInfo(info, caseid, docid, uid, 4, 0,
				"佰医汇微信公众号", pay);*/
		return wenzhenService.saveBusinessWenZhenTwInfo(info);
	}

	private Integer generateTelOrder(HttpServletRequest request, Integer uid,
			Integer docid, Integer caseid, Boolean pay) {
		BusinessWenzhenTel telinfo = new BusinessWenzhenTel();
		telinfo.setOpenId(request.getParameter("openid"));
	/*	TwAndTelCommonPayUtil.telOrderInfo(telinfo, uid, docid, caseid, pay,
				"佰医汇微信公众号", 0);*/
		return wenzhenService.saveBusinessWenZhenTel(telinfo);
	}

	/**
	 * 购买远程订单通知
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */

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

	/**
	 * 支付回调通知 -远程
	 * 
	 * @param result
	 */
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
				rc.setPayStatus(1);
				rc.setRefreshTime(new Date());
				weixinService.updateRemoteConsultation(rc);
				GeneUserBillRecordUtil.geneBill(rc.getId(), rc.getUserId(),rc.getExpertId()!=null?rc.getExpertId():null,
						rc.getAmount(), "消费", "远程门诊", "");
				// 更新时间为已选
				ExpertConsultationSchedule sch = weixinService
						.queryExpertConScheduleById(rc.getSchedulerDateId());
				sch.setHasAppoint("1");
				weixinService.updatExpertConScheduleDate(sch);
				// 对接his系统进行挂号
				// toplusInHis(rc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 支付完成时进行六安挂号
	 * 
	 * @param rc
	 * @throws Exception
	 */
	private void toplusInHis(RemoteConsultation rc) throws Exception {
		Integer local_depid = rc.getLocalDepartId();
		HospitalDepartmentInfo depart = weixinService
				.queryHospitalDepartmentInfoById(local_depid);
		if (depart.getLocalDepId() != null) {
			log.info("=====六安挂号开始====");
			Map<String, Object> pinfo = new HashMap<String, Object>();
			pinfo.put("name", rc.getPatientName());
			pinfo.put("id_card", rc.getIdCard());
			pinfo.put("telephone", rc.getTelephone());
			String pstr = PythonVisitUtil.generateJSONObject(pinfo).toString();
			System.out.println(pstr);
			pinfo.clear();
			pinfo.put("dept_id", depart.getLocalDepId());
			pinfo.put("type_name", "普通挂号");
			pinfo.put("register_fee", 2);
			pinfo.put("register_type", gainType(rc));
			pinfo.put("register_time", gaintime(rc));
			String rinfo = PythonVisitUtil.generateJSONObject(pinfo).toString();
			pinfo.clear();
			pinfo.put("patient_info", pstr);
			pinfo.put("register_info", rinfo);
			String data = PythonVisitUtil.generateJSONObject(pinfo).toString();
			log.info("=====六安挂号data:" + data);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
			nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
			nameValuePairs.add(new BasicNameValuePair("data", data));
			Map<String, Object> map = PythonVisitUtil
					.register_info(nameValuePairs);
			rc.setRetId(map.get("eid").toString());
			rc.setRetMessage(map.get("message").toString());
			weixinService.updateRemoteConsultation(rc);
			// 发送短信通知
			if (map.get("eid").toString().equalsIgnoreCase("0")) {
				sendsms(rc);
			}
			log.info("=====六安挂号结束====");
		}
	}

	/**
	 * 发送短信
	 * 
	 * @param rc
	 */
	private void sendsms(RemoteConsultation rc) {
		// 挂号成功
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(rc.getExpertId());
		String time = "";
		if (StringUtils.isNotBlank(rc.getThirdConsultationDate())) {
			time = rc.getThirdConsultationDate() + " "
					+ rc.getThirdConsultationTime();
		} else if (StringUtils.isNotBlank(rc.getSecondConsultationDate())) {
			time = rc.getSecondConsultationDate() + " "
					+ rc.getThirdConsultationTime();
		} else {
			time = rc.getConsultationDate() + " " + rc.getConsultationTime();
		}
		// =================患者短信============================
		String patient_content = "尊敬的用户，恭喜您预约成功，会诊时间：" + time
				+ "。请您准备好病历资料，提前一小时到您预约医院等候。详情请登录“佰医汇”微信公众号查看我的订单。【佰医汇】";
		HttpSendSmsUtil.sendSmsInteface(rc.getTelephone(), patient_content);
		// ================客服短信================================
		StringBuilder kefu_content = new StringBuilder();
		kefu_content.append("已有患者预约成功。患者详细信息如下：\n");
		kefu_content.append("姓名：" + rc.getPatientName() + "\n");
		if (rc.getSex() != null) {
			if (rc.getSex().equals(1))
				kefu_content.append("性别：男\n");
			if (rc.getSex().equals(2))
				kefu_content.append("性别：女\n");
		}
		kefu_content.append("手机号码：" + rc.getTelephone() + "\n");
		if (StringUtils.isNotBlank(rc.getIdCard())) {
			kefu_content.append("身份证：" + rc.getIdCard() + "\n");
		}
		kefu_content.append("预约信息如下：\n");
		kefu_content.append("类型：专家会诊\n");

		kefu_content.append("预约时间：" + time + "\n");
		kefu_content.append("专家：" + special.getSpecialName() + "("
				+ special.getHosName() + " " + special.getDepName() + ")\n");
		kefu_content.append("请提前联系患者，辅助患者完成此次服务。【佰医汇】");
		HttpSendSmsUtil.sendSmsInteface("15001299884", kefu_content.toString());
		// ===============专家短信=================================
		/*
		 * String expert_content="";
		 * HttpSendSmsUtil.sendSmsInteface(special.getTelphone(),
		 * expert_content);
		 */
	}

	private Integer gainType(RemoteConsultation rc) {
		Integer type = 3;
		String time = "";
		if (StringUtils.isNotBlank(rc.getThirdConsultationDate())) {
			time = rc.getThirdConsultationDate().trim();
		} else if (StringUtils.isNotBlank(rc.getSecondConsultationDate())) {
			time = rc.getSecondConsultationDate().trim();
		} else {
			time = rc.getConsultationDate().trim();
		}
		if (time.equalsIgnoreCase(_format.format(new Date()))) {
			type = 0;
		}
		return type;
	}

	private String gaintime(RemoteConsultation rc) {
		String time = "";
		if (StringUtils.isNotBlank(rc.getThirdConsultationDate())) {
			time = rc.getThirdConsultationDate().trim() + " "
					+ rc.getThirdConsultationTime().trim() + ":00";
		} else if (StringUtils.isNotBlank(rc.getSecondConsultationDate())) {
			time = rc.getSecondConsultationDate().trim() + " "
					+ rc.getSecondConsultationTime().trim() + ":00";
		} else {
			time = rc.getConsultationDate().trim() + " "
					+ rc.getConsultationTime().trim() + ":00";
		}
		return time;
	}

	private String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
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
		map.put("ltype", 4);
		return new ModelAndView("zjh/3.0/remote/remote_sub_main", map);
	}

	// =======================在线问诊==========================
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
		Integer depid = Integer.parseInt(request.getParameter("depid"));
		Integer ltype = Integer.parseInt(request.getParameter("ltype"));
		map.put("depid", depid);
		map.put("ltype", ltype);
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
					UserAccountInfo u = weixinService
							.queryUserAccountInfoByMobilePhone(tel);
					Integer uid = null;
					if (u == null) {
						u = new UserAccountInfo();
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
						//ue.setSex(sex);
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
		UserFeedBackInfo info = new UserFeedBackInfo();
		info.setContent(request.getParameter("content"));
		info.setOpenid(request.getParameter("openid"));
		info.setSource(1);
		info.setUserType(1);
		info.setCreateTime(new Date());
		weixinService.saveUserFeedbackInfo(info);
		return map;
	}

	// ============================下载app
	@RequestMapping(value = "/gainApps", method = RequestMethod.GET)
	public ModelAndView gainApps(HttpServletRequest request) {
		return new ModelAndView("zjh/getapps");
	}

	// 我的病例
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

	// 获取更多病例
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

	// 进入病例详情
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

	// 在医生端同步的检验与pacs资料以及专家的结果需要在 公众号上患者能看到
	@RequestMapping(value = "/gainOrderLisPacs", method = RequestMethod.GET)
	public ModelAndView gainOrderLisPacs(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("orderid"));
		RemoteConsultation order = weixinService
				.queryRemoteConsultationById(oid);
		// 专家结果
		map.put("order", order);
		return new ModelAndView("zjh/2.0/lis_pacs", map);
	}

	// 进入评价界面
	@RequestMapping(value = "/toestimate", method = RequestMethod.GET)
	public ModelAndView toevaluate(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String oid = request.getParameter("oid");
		String openid = request.getParameter("openid");
		map.put("openid", openid);
		map.put("oid", oid);
		return new ModelAndView("zjh/2.0/evaluate", map);
	}

	@RequestMapping(value = "/saveestimate", method = RequestMethod.POST)
	public Map<String, Object> saveevalua(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 保存评价
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		String dlevel = request.getParameter("doctorlevel");// 专家等级
		String attlevel = request.getParameter("attlevel");// 陪护等级
		String docDesc = request.getParameter("docDesc");// 专家评价
		RemoteConsultation order = weixinService
				.queryRemoteConsultationById(oid);
		DoctorEstimateInfo estimate = new DoctorEstimateInfo();
		estimate.setCreateTime(sdf.format(new Date()));
		estimate.setOrderId(oid);
		estimate.setDoctorId(order.getExpertId());
		if (StringUtils.isNotBlank(dlevel)) {
			estimate.setDoctorLevel(Integer.parseInt(dlevel));
		}
		if (StringUtils.isNotBlank(attlevel)) {
			estimate.setAttendanceLevel(Integer.parseInt(attlevel));
		}
		estimate.setDoctorDesc(docDesc);
		weixinService.saveDoctorEstimateInfo(estimate);
		return map;
	}

	@RequestMapping(value = "/surepay_pc", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> surepay_pc(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Float money = Float.parseFloat(request.getParameter("pmoney"));
		UUID uuid = UUID.randomUUID();

		String oid = request.getParameter("oid");
		RemoteConsultation rc = null;
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
		rc.setOutTradeNo(retMap.get("out_trade_no").toString());
		weixinService.updateRemoteConsultation(rc);
		log.info("======二维码url===" + retMap.get("code_url"));
		map.put("code_url", retMap.get("code_url"));
		map.put("out_trade_no", retMap.get("out_trade_no"));
		return map;
	}

	private RemoteConsultation generateRemoteOrder_pc2(Integer oid,
			HttpServletRequest request, String outTradeNo, Float money)
			throws Exception {
		RemoteConsultation rc = weixinService.queryRemoteConsultationById(oid);
		String stimeid = request.getParameter("stimeid");
		if (StringUtils.isNotBlank(stimeid)) {
			rc.setSchedulerDateId(Integer.parseInt(stimeid));
			ExpertConsultationSchedule ecs = weixinService
					.queryExpertConScheduleById(Integer.parseInt(stimeid));
			rc.setConsultationDate(ecs.getScheduleDate());
			rc.setConsultationTime(ecs.getStartTime());
		}
		rc.setExpertId(Integer.parseInt(request.getParameter("expertId")));
		rc.setConsultationDur(15);
		rc.setOutTradeNo(outTradeNo);
		rc.setHelpOrderSelExpert("1");
		rc.setUserId(Integer.parseInt(request.getParameter("uid")));
		weixinService.updateRemoteConsultation(rc);
		// 更新时间为已选
		ExpertConsultationSchedule sch = weixinService
				.queryExpertConScheduleById(rc.getSchedulerDateId());
		sch.setHasAppoint("1");
		weixinService.updatExpertConScheduleDate(sch);
		// 辅助下单创建门诊病历
		createMedicalRecord(oid, request);
		pacsInfoStorage(oid, request);
		lisInfoStorage(oid, request);
		localPicsStorage(oid, request);
		return rc;
	}

	private RemoteConsultation generateRemoteOrder_pc(
			HttpServletRequest request, String outTradeNo, Float money)
			throws Exception {
		RemoteConsultation rc = new RemoteConsultation();
		String stimeid = request.getParameter("stimeid");
		if (StringUtils.isNotBlank(stimeid)) {
			rc.setSchedulerDateId(Integer.parseInt(stimeid));
			ExpertConsultationSchedule ecs = weixinService
					.queryExpertConScheduleById(Integer.parseInt(stimeid));
			rc.setConsultationDate(ecs.getScheduleDate());
			rc.setConsultationTime(ecs.getStartTime());
		}
		rc.setExpertId(Integer.parseInt(request.getParameter("expertId")));
		rc.setCreateTime(new Date());
		rc.setRefreshTime(new Date());
		rc.setConsultationDur(15);
		rc.setLocalHospitalId(Integer.parseInt(request
				.getParameter("localHosId")));
		rc.setLocalDepartId(Integer.parseInt(request
				.getParameter("localDepartId")));
		Integer contid = Integer.parseInt(request.getParameter("contactorid"));
		UserContactInfo uinfo = weixinService
				.queryUserContactorInfoById(contid);
		String username = uinfo.getContactName();
		String telphone = uinfo.getTelphone();
		rc.setPatientName(username);
		rc.setTelephone(telphone);
		String idcard = uinfo.getIdCard();
		rc.setIdCard(idcard);
		if (StringUtils.isNotBlank(idcard)) {
			String ssex = IdcardUtils.getGenderByIdCard(idcard);
			rc.setSex(ssex.equalsIgnoreCase("M") ? 1 : (ssex
					.equalsIgnoreCase("F") ? 2 : 0));
		} else {
			rc.setSex(0);
		}
		rc.setSource(2);// 官网下的
		rc.setAmount(new BigDecimal(money));// ===================================待获取
		rc.setPayMode(2);// ======================================1 微信支付
		rc.setOutTradeNo(outTradeNo);
		rc.setStatus(0);
		rc.setLocalPlusNo("123");
		rc.setPayStatus(0);
		rc.setUserId(Integer.parseInt(request.getParameter("uid")));
		Integer oid = weixinService.saveRemoteConsultation(rc);
		rc.setId(oid);
		return rc;
	}

	private void createMedicalRecord(Integer oid, HttpServletRequest request) {
		MedicalRecords mr = new MedicalRecords();
		mr.setOrderId(oid);
		mr.setMainSuit(request.getParameter("mainSuit"));
		mr.setPresentIll(request.getParameter("presentIll"));
		mr.setAssistantResult(request.getParameter("assistantResult"));
		mr.setExamined(request.getParameter("examined"));
		mr.setHistoryIll(request.getParameter("historyIll"));
		mr.setInitialDiagnosis(request.getParameter("initialDiagnosis"));
		mr.setTreatAdvice(request.getParameter("treatAdvice"));
		commonService.saveMedicalRecords(mr);
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

	// 本地图片
	private void localPicsStorage(Integer oid, HttpServletRequest request) {
		RemoteConsultation order = weixinService
				.queryRemoteConsultationById(oid);
		order.setNormalImages(request.getParameter("picsIds"));
		weixinService.updateRemoteConsultation(order);
	}

	/**
	 * ============================我的订单==========================
	 * 
	 * @param request
	 * @return
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
		/*
		 * if (flag.equalsIgnoreCase("processing")) { redirectUrl = ""; } else
		 * if (flag.equalsIgnoreCase("complete")) { redirectUrl =
		 * "zjh/mycompleted_orders"; } else if (flag.equalsIgnoreCase("cancel"))
		 * { redirectUrl = "zjh/mycancel_orders"; }
		 */
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
					_bean.setDesc(StringRetUtil.gainStringByStats(
							_bean.getAskStatus(), null));
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
		switch (type) {
		case 1:
			BusinessWenZhenInfo tworder = wenzhenService
					.queryBusinessWenZhenInfoById(oid);
			docid = tworder.getDoctorId();
			desc = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/twbody");
			notify_url = "kangxin/wenzhennotify";
			break;
		case 2:
			BusinessWenzhenTel telorder = wenzhenService
					.queryBusinessWenZhenTelById(oid);
			docid = telorder.getDoctorId();
			desc = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/telbody");
			notify_url = "kangxin/wenzhennotify";
			break;
		case 4:
			desc = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/remotebody");
			break;
		}
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(docid);
		Float money = (type.equals(1) ? special.getAskAmount() : (type
				.equals(2) ? special.getTelAmount() : special.getVedioAmount()))
				.floatValue();
		Map<String, Object> retMap = WeixinUtil.weipay(request, response,
				money, PropertiesUtil.getString("APPID"),
				PropertiesUtil.getString("APPSECRET"),
				PropertiesUtil.getString("PARTNER"),
				PropertiesUtil.getString("PARTNERKEY"), desc, openid,
				PropertiesUtil.getString("DOMAINURL") + notify_url, "", null,
				null);
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
		BusinessWenZhenInfo order = wenzhenService
				.queryBusinessWenZhenInfoById(oid);
		Date time = new Date();
		BusinessMessageInfo message = new BusinessMessageInfo();
		message.setWenZhen_Id(oid);
		message.setFromId(uwr.getUserId());
		message.setFromUserType(1);
		message.setToId(order.getDoctorId());
		message.setToUserType(2);
		message.setSendTime(time);
		message.setMsgContent(msgContent);
		message.setSendStatus(1);
		wenzhenService.saveBusinessMessageInfo(message);
		// 专家收到的消息数+1
		Integer num = order.getDocMessageCount();
		if (num == null) {
			num = 1;
		} else {
			num = num + 1;
		}
		order.setDocMessageCount(num);
		Integer pnum = order.getPatTalkingNumber();
		if (pnum == null) {
			pnum = 1;
		} else {
			pnum = pnum + 1;
		}
		order.setPatTalkingNumber(pnum);
		wenzhenService.updateBusinessWenzhenInfo(order);
		message.setMsgTime(sdf.format(message.getSendTime()));
		map.put("message", message);
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

		BusinessMessageInfo message = new BusinessMessageInfo();
		message.setWenZhen_Id(oid);
		message.setFromId(uwr.getUserId());
		message.setFromUserType(1);
		message.setToId(order.getDoctorId());
		message.setToUserType(2);
		message.setSendTime(new Date());
		message.setPath(ret_map.get("urlpath").toString());
		message.setSendStatus(1);
		message.setMsgType("image/jpg");
		wenzhenService.saveBusinessMessageInfo(message);
		map.put("urlpath", ret_map.get("urlpath"));
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
		String flag = request.getParameter("flag");
		String type = request.getParameter("type");
		map.put("oid", oid);
		map.put("flag", flag);
		map.put("type", type);
		map.put("openid", openid);
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
		BusinessOrderInfo payinfo = null;
		BigDecimal money = null;
		switch (type) {
		case 1:
			// 图文
			// 查询支付订单
			payinfo = wenzhenService.queryBusinessOrderInfoByOId(oid, 1);
			BusinessWenZhenInfo twinfo = wenzhenService
					.queryBusinessWenZhenInfoById(oid);
			twinfo.setTimeStr(sdf.format(twinfo.getStartTime()));
			processOrders(twinfo, payinfo, flag);
			map.put("twinfo", twinfo);
			caseid = twinfo.getCases_Id();
			docid = twinfo.getDoctorId();
			List<BusinessMessageInfo> messages = wenzhenService
					.queryBusinessMessagesByTwId(oid);
			for (BusinessMessageInfo _bean : messages) {
				_bean.setMsgTime(sdf.format(_bean.getSendTime()));
			}
			map.put("messages", messages);
			money = payinfo.getAmount();
			break;
		case 2:
			// 电话
			BusinessWenzhenTel telinfo = wenzhenService
					.queryBusinessWenZhenTelById(oid);
			payinfo = wenzhenService.queryBusinessOrderInfoByOId(oid, 2);
			telinfo.setTimeStr(sdf.format(telinfo.getCreateTime()));
			processOrders(telinfo, payinfo, flag);
			map.put("telinfo", telinfo);
			caseid = telinfo.getCaseId();
			docid = telinfo.getDoctorId();
			money = payinfo.getAmount();
			break;
		case 4:
			// 远程门诊
			RemoteConsultation reminfo = weixinService
					.queryRemoteConsulationsById_detail(oid);
			reminfo.setTimeStr(sdf.format(reminfo.getCreateTime()));
			String time = gaintime_new(reminfo);
			time = time.equalsIgnoreCase("null null") ? "未设置" : time;
			reminfo.setConDate(time);
			map.put("reminfo", reminfo);
			money = reminfo.getAmount();
			processOrders(reminfo, flag);
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
			List<CaseImages> caseimages = wenzhenService
					.queryCaseImagesByCaseId(caseid);
			map.put("caseimages", caseimages);
		}
		map.put("money", money);
		return map;
	}

	private String gaintime_new(RemoteConsultation rc) {
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
			if (StringUtils.isNotBlank(rc.getThirdConsultationDate())) {
				time = rc.getThirdConsultationDate() + " "
						+ rc.getThirdConsultationTime();
			} else if (StringUtils.isNotBlank(rc.getSecondConsultationDate())) {
				time = rc.getSecondConsultationDate() + " "
						+ rc.getSecondConsultationTime();
			} else {
				time = rc.getConsultationDate() + " "
						+ rc.getConsultationTime();
			}
			break;
		case 20:
		case 21:
			time = rc.getConsultationDate() + " " + rc.getConsultationTime();
			break;
		}
		return time;
	}

	private void processOrders(BusinessWenZhenInfo twinfo,
			BusinessOrderInfo payinfo, String flag) {
		if (flag.equalsIgnoreCase("processing")) {
			Integer _ps = payinfo.getPayStatus().intValue();
			if (_ps.equals(4)) {
				twinfo.setDesc("待付款");
			} else if (_ps.equals(1)) {
				twinfo.setDesc("进行中");
			}
		} else if (flag.equalsIgnoreCase("complete")) {
			twinfo.setDesc("已完成");
		} else if (flag.equalsIgnoreCase("cancel")) {
			twinfo.setDesc("已取消");
		}
	}

	private void processOrders(BusinessWenzhenTel telinfo,
			BusinessOrderInfo payinfo, String flag) {
		// 图文或电话
		if (flag.equalsIgnoreCase("processing")) {
			Integer _ps = telinfo.getPayStatus().intValue();
			if (_ps.equals(4)) {
				telinfo.setDesc("待付款");
			} else if (_ps.equals(1)) {
				telinfo.setDesc("进行中");
			}
		} else if (flag.equalsIgnoreCase("complete")) {
			telinfo.setDesc("已完成");
		} else if (flag.equalsIgnoreCase("cancel")) {
			telinfo.setDesc("已取消");
		}
	}

	private void processOrders(RemoteConsultation reminfo, String flag) {
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

	// /////////////////////////////新增患者///////////////////////////////////////////////////////
	@RequestMapping(value = "/intoadduser", method = RequestMethod.GET)
	public ModelAndView intoadduser(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String ltype = request.getParameter("ltype");
		// 远程门诊
		map.put("sid", request.getParameter("sid"));
		map.put("departId", request.getParameter("departId"));
		map.put("stimeid", request.getParameter("stimeid"));
		map.put("openid", request.getParameter("openid"));

		// 远程会诊
		map.put("depid", request.getParameter("depid"));
		map.put("timeid", request.getParameter("timeid"));
		map.put("levelid", request.getParameter("levelid"));
		map.put("hosid", request.getParameter("hosid"));

		// 在线问诊
		map.put("docid", request.getParameter("docid"));

		map.put("ltype", ltype);
		return new ModelAndView("zjh/3.0/user/user_add", map);
	}

	/**
	 * 新增患者，后台直接重定向到填写病历界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveuser", method = RequestMethod.POST)
	public String saveuser(HttpServletRequest request) {
		String openid = request.getParameter("openid");
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		String contactName = request.getParameter("contactName");
		String idcard = request.getParameter("idcard");
		String sex = request.getParameter("sex");
		String age = request.getParameter("age");
		String telphone = request.getParameter("telphone");
		String ismaster = request.getParameter("ismaster");// 是否默认用户
		UserContactInfo user = new UserContactInfo();
		user.setAge(StringUtils.isNotBlank(age) ? Integer.parseInt(age) : null);
		user.setContactName(contactName);
		user.setCreateTime(new Timestamp(System.currentTimeMillis()));
		user.setIdCard(idcard);
		user.setOpenId(openid);
		user.setSex(StringUtils.isNotBlank(sex) ? Integer.parseInt(sex) : null);
		user.setStatus(1);
		user.setUserId(uwr.getUserId());
		user.setTelphone(telphone);
		user.setIsMasterContact((StringUtils.isNotBlank(ismaster) && ismaster
				.equalsIgnoreCase("1")) ? Integer.parseInt(ismaster) : 0);
		weixinService.saveUserContactInfo(user);
		if (user.getIsMasterContact() != null
				&& user.getIsMasterContact().equals(1)) {
			// 修改其他的用户为非主账户
		}
		String redirectUrl="";
		Integer ltype = Integer.parseInt(request.getParameter("ltype"));
		switch (ltype) {
		case 1:
		case 2:
			String docid=request.getParameter("docid");
			redirectUrl = "wzjh/suretoconfirm?docid=" + docid + "&ltype=" + ltype
			+ "&openid=" + openid;
			break;
		case 4:
			String splevel = request.getParameter("levelid");
			if (StringUtils.isNotBlank(splevel)) {
				String hosid=request.getParameter("hosid");
				String depid=request.getParameter("depid");
				String timeid=request.getParameter("timeid");
				redirectUrl = "/suretoconfirm?openid=" + openid + "&hosid="
						+ hosid + "&depid=" + depid + "&splevel="
						+ splevel + "&timeid=" + timeid + "&ltype=4";
			}else{
				String depid=request.getParameter("departId");
				String stimeid=request.getParameter("stimeid");
				String sid=request.getParameter("sid");
				redirectUrl = "/suretoconfirm?openid=" + openid + "&departId="
						+ depid + "&stimeid=" + stimeid + "&sid=" + sid
						+ "&ltype=4";
			}
			break;
		}
		return "redirect:/"+redirectUrl;
	}
}
