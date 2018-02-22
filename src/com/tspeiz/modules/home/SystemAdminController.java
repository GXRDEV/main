package com.tspeiz.modules.home;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tspeiz.modules.common.entity.newrelease.*;
import com.tspeiz.modules.util.*;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.tspeiz.modules.common.bean.D2pOrderBean;
import com.tspeiz.modules.common.bean.ExcelBean;
import com.tspeiz.modules.common.bean.JSONParam;
import com.tspeiz.modules.common.bean.LoginBean;
import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.PushCodeEnum;
import com.tspeiz.modules.common.bean.SpeceialBean;
import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.bean.dto.BusinessD2pPrivateOrderDto;
import com.tspeiz.modules.common.bean.dto.BusinessT2pVipOrderDto;
import com.tspeiz.modules.common.bean.dto.DocFollowDto;
import com.tspeiz.modules.common.bean.dto.DocRegisterDto;
import com.tspeiz.modules.common.bean.dto.DoctorAboutCount;
import com.tspeiz.modules.common.bean.dto.DoctorIncomeDto;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.entity.BusinessOperativeInfo;
import com.tspeiz.modules.common.entity.ExpertConsultationSchedule;
import com.tspeiz.modules.common.entity.SystemServiceInfo;
import com.tspeiz.modules.common.entity.SystemServicePackage;
import com.tspeiz.modules.common.entity.coupon.MoneyExchange;
import com.tspeiz.modules.common.entity.release2.DoctorSceneEwm;
import com.tspeiz.modules.common.entity.release2.DoctorTeam;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAlliance;
import com.tspeiz.modules.common.entity.release2.OperatorInvitCode;
import com.tspeiz.modules.common.entity.release2.PlatformHealthConsultation;
import com.tspeiz.modules.common.entity.release2.PlatformHealthType;
import com.tspeiz.modules.common.entity.release2.ShufflingFigureConfig;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;
import com.tspeiz.modules.common.service.IApiGetuiPushService;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.common.thread.TokenThread;
import com.tspeiz.modules.manage.CommonManager;
import com.tspeiz.modules.manage.D2pManager;
import com.tspeiz.modules.manage.SystemAdminManager;
import com.tspeiz.modules.util.common.GenerageShortUrl;
import com.tspeiz.modules.util.common.StringRetUtil;
import com.tspeiz.modules.util.config.ReaderConfigUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.oss.OSSManageUtil;
import com.tspeiz.modules.util.poi.ExcelUtil;
import com.tspeiz.modules.util.poi.ExcelUtilToService;
import com.tspeiz.modules.util.weixin.CommonUtil;
import com.tspeiz.modules.util.weixin.SignUtil;

@Controller
@RequestMapping("system")
public class SystemAdminController {
	private Logger log = Logger.getLogger(SystemAdminController.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Resource
	private ICommonService commonService;
	@Resource
	private IWeixinService weixinService;
	@Resource
	private IWenzhenService wenzhenService;
	@Autowired
	private CommonManager commonManager;
	@Autowired
	private SystemAdminManager systemAdminManager;
	@Resource
	private IApiGetuiPushService apiGetuiPushService;
	@Autowired
	private D2pManager d2pManager;

	/**
	 * 进入统计界面
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/main")
	public ModelAndView index(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询合作医院
		List<HospitalDetailInfo> hospitals = weixinService
				.queryCoohospitalInfos();
		map.put("hospitals", hospitals);
		List<ReSourceBean> hosbeans = (List<ReSourceBean>) commonService
				.queryNewAddHospital(null).get("modata");
		map.put("hoscount", hosbeans.get(0).getCount());
		List<ReSourceBean> expertbeans = (List<ReSourceBean>) commonService
				.queryNewExpertsAdd(null).get("modata");
		map.put("excount", expertbeans.get(0).getCount());
		List<ReSourceBean> ordersAddCon = (List<ReSourceBean>) commonService
				.queryOrdersAddCon(null, null).get("modata");
		map.put("orcount", ordersAddCon.get(0).getCount());
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		return new ModelAndView("admin/main", map);
	}

	/**
	 * 新入驻的医院月增量统计
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/newhoscal", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> newhoscal(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(systemAdminManager.newhoscal());
		return map;
	}

	/**
	 * 新入驻的专家月增量统计
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/newexpertcal", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> newexpertcal(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(systemAdminManager.newexpertcal());
		return map;
	}

	/**
	 * 订单统计（包括 整体订单数，月增数）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ordercal", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ordercal(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hosid = request.getParameter("hosid");
		map.putAll(systemAdminManager.ordercal(hosid));
		return map;
	}

	/**
	 * 会诊订单医院分布
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderhoscal", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> orderhoscal(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		map.putAll(systemAdminManager.orderhoscal(startDate, endDate));
		return map;
	}

	/**
	 * 会诊订单专家分布
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderexcal", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> orderexcal(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hosid = request.getParameter("hosid");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		map.putAll(systemAdminManager.orderexcal(hosid, startDate, endDate));
		return map;
	}

	/**
	 * 会诊订单科室（科室，订单数，百分比）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderdepcal", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> orderdepcal(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hosid = request.getParameter("hosid");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		map.putAll(systemAdminManager.orderdepcal(hosid, startDate, endDate));
		return map;
	}

	// 所有医院管理
	@RequestMapping(value = "/hospitals", method = RequestMethod.GET)
	public ModelAndView hospitals(HttpServletRequest request) {
		return new ModelAndView("admin/hospital_list");
	}

	// 获取医院数据
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaindhospitals", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindhospitals(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String currorhis = paramMap.get("currorhis");
		StringBuilder stringJson = null;
		Integer type = null;
		if (StringUtils.isNotBlank(currorhis)
				&& currorhis.equalsIgnoreCase("coohos")) {
			type = 1;
		}
		Map<String, Object> retmap = weixinService.queryHospitalsBySystem(
				searchContent, start, length, type);
		Integer renum = (Integer) retmap.get("num");
		List<HospitalDetailInfo> hospitals = (List<HospitalDetailInfo>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		HospitalDetailInfo hos = null;
		for (int i = 0; i < hospitals.size(); i++) {
			hos = hospitals.get(i);
			stringJson.append("[");
			stringJson.append("\"" + hos.getId() + "\",");
			stringJson.append("\"" + hos.getDisplayName() + "\",");
			stringJson.append("\"" + hos.getShortName() + "\",");
			stringJson.append("\"" + hos.getHosLevel() + "\",");
			stringJson.append("\"" + hos.getDistName() + "\",");
			String str = (hos.getIsCooHospital() == null ? "否" : "是");
			if (hos.getIsCooHospital() != null
					&& hos.getIsCooHospital().equals(1)) {
				String _st = (StringUtils.isNotBlank(hos.getAuditStatus()) ? (hos
						.getAuditStatus().equalsIgnoreCase("1") ? "审核通过"
						: "审核不通过") : "待审核");
				String mode = StringUtils.isNotBlank(hos.getDockingMode()) ? ("-" + ((hos
						.getDockingMode().equalsIgnoreCase("1")) ? "简单对接"
						: "深度对接")) : "";
				str += "(" + _st + ")" + mode;
			}
			stringJson.append("\"" + str + "\",");
			stringJson.append("\""
					+ (hos.getStatus().equals(1) ? "已上线" : "已下线") + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (hospitals.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		log.info("====json==" + stringJson.toString());
		return stringJson.toString();
	}

	// 进入医院操作界面
	@RequestMapping(value = "/intohos", method = RequestMethod.GET)
	public ModelAndView intohos(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hosid = request.getParameter("hosid");
		if (StringUtils.isNotBlank(hosid)) {
			HospitalDetailInfo special = weixinService
					.queryHospitalDetailInfoById(Integer.parseInt(hosid));
			map.put("special", special);
			String relatedPics = special.getBigPicture();
			List<CustomFileStorage> images = null;
			if (StringUtils.isNotBlank(relatedPics)) {
				images = weixinService
						.queryCustomFileStorageImages(relatedPics);
			}
			map.put("images", images);
			String authorizeFile = special.getAuthorizeFile();
			List<CustomFileStorage> authorizeFiles = null;
			if (StringUtils.isNotBlank(authorizeFile)) {
				authorizeFiles = weixinService
						.queryCustomFileStorageImages(authorizeFile);
			}
			map.put("authorizeFiles", authorizeFiles);
		}
		return new ModelAndView("admin/hospital_op", map);
	}

	@RequestMapping(value = "/saveorupdatehos", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveorupdatehos(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hosid = request.getParameter("hosid");
		HospitalDetailInfo hos = null;
		if (StringUtils.isNotBlank(hosid)) {
			// 编辑
			hos = weixinService.queryHospitalDetailInfoById(Integer
					.parseInt(hosid));
			hospitalData(hos, request);
			weixinService.updateHospitalDetailInfo(hos);
		} else {
			// 新增
			hos = new HospitalDetailInfo();
			hos.setCreateTime(new Timestamp(System.currentTimeMillis()));
			hos.setStatus(0);
			hospitalData(hos, request);
			Integer id = weixinService.saveHospitalDetailInfo(hos);
			hos.setId(id);
			// 新增合作医院需要创建管理员账号
			if (StringUtils.isNotBlank(hos.getAuditStatus())
					&& hos.getAuditStatus().equalsIgnoreCase("1")) {
				map.putAll(createadminaccount(hos));
				map.put("hosid", id);
			} else {
				// 发送短信
				sendhossms(hos);
			}
		}
		return map;
	}

	private void sendhossms(HospitalDetailInfo hos) {
		if (StringUtils.isNotBlank(hos.getContactorTelphone())
				&& hos.getIsCooHospital() != null
				&& hos.getIsCooHospital().equals(1)) {
			// 向客户发送短信
			String khcontent = "您已向\"佰医汇\"提交入驻申请，请耐心等待审核，如2个工作日内仍未审核通过，请您联系\"佰医汇\"客服人员，电话400-890-5111。【佰医汇】";
			HttpSendSmsUtil.sendSmsInteface(hos.getContactorTelphone(),
					khcontent);
			// 向客服发送短信
			// 六安世立医院何勇滨通过
			String kfcontent = hos.getDisplayName() + hos.getContactorName()
					+ "通过\"佰医汇\"平台提交了入驻申请，请及时查看及审核。【佰医汇】";
			HttpSendSmsUtil.sendSmsInteface("15001299884", kfcontent);
		}
	}

	// 医院上下线
	@RequestMapping(value = "/onandoff", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> onandoff(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer hosid = Integer.parseInt(request.getParameter("hosid"));
		HospitalDetailInfo detail = weixinService
				.queryHospitalDetailInfoById(hosid);
		if (detail.getStatus().equals(0)) {
			detail.setStatus(1);
		} else {
			detail.setStatus(0);
		}
		weixinService.updateHospitalDetailInfo(detail);
		return map;
	}

	@RequestMapping(value = "/showdetail/{hosid}", method = RequestMethod.GET)
	public ModelAndView showdetail(@PathVariable Integer hosid,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		HospitalDetailInfo special = weixinService
				.queryHospitalDetailInfoById(hosid);
		map.put("special", special);
		String relatedPics = special.getBigPicture();
		List<CustomFileStorage> images = null;
		if (StringUtils.isNotBlank(relatedPics)) {
			images = weixinService.queryCustomFileStorageImages(relatedPics);
		}
		map.put("images", images);
		String authorizeFile = special.getAuthorizeFile();
		List<CustomFileStorage> authorizeFiles = null;
		if (StringUtils.isNotBlank(authorizeFile)) {
			authorizeFiles = weixinService
					.queryCustomFileStorageImages(authorizeFile);
		}
		map.put("authorizeFiles", authorizeFiles);
		return new ModelAndView("admin/hospital_de", map);
	}

	// 审核通过
	@RequestMapping(value = "/auditthrough", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> auditthrough(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer hosid = Integer.parseInt(request.getParameter("hosid"));
		String auditSta = request.getParameter("auditSta");
		HospitalDetailInfo info = weixinService
				.queryHospitalDetailInfoById(hosid);
		info.setAuditStatus(auditSta);// 1--审核通过，2--审
		weixinService.updateHospitalDetailInfo(info);
		// 审核通过需要创建院方管理账号并发送短信
		if (StringUtils.isNotBlank(info.getAuditStatus())
				&& info.getAuditStatus().equalsIgnoreCase("1")) {
			info.setStatus(1);
			weixinService.updateHospitalDetailInfo(info);
			if (StringUtils.isNotBlank(info.getAccountCreate())
					&& info.getAccountCreate().equalsIgnoreCase("1")) {
				// 已创建过账号
				DoctorRegisterInfo reg = commonService
						.queryDoctorRegisterInfoByHosAndType(hosid, 5);
				map.put("accountid", reg.getId());
				map.put("loginName", reg.getLoginName());
			} else {
				map.putAll(createadminaccount(info));
			}
		}
		return map;
	}

	// 确认账号
	@RequestMapping(value = "sureconfirm", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> sureconfirm(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer accountid = Integer.parseInt(request.getParameter("accountid"));
		String loginName = request.getParameter("loginName");
		Integer hosid = Integer.parseInt(request.getParameter("hosid"));
		HospitalDetailInfo hos = weixinService
				.queryHospitalDetailInfoById(hosid);
		DoctorRegisterInfo reg = commonService
				.queryDoctorRegisterInfoByLoginName(loginName);
		boolean b = true;
		String number = CheckNumUtil.randomChars_new(6);
		if (reg == null) {
			reg = commonService.queryDoctorRegisterInfoById(accountid);
			reg.setLoginName(loginName);
			reg.setPassword(PasswordUtil.MD5Salt(number));
			reg.setSalt("cvYl8U");
			commonService.updateDoctorRegisterInfo(reg);
			map.put("status", "success");
		} else {
			if (reg.getId().equals(accountid)) {
				reg.setLoginName(loginName);
				reg.setPassword(PasswordUtil.MD5Salt(number));
				reg.setSalt("cvYl8U");
				commonService.updateDoctorRegisterInfo(reg);
				map.put("status", "success");
			} else {
				b = false;
				map.put("status", "error");
			}
		}
		if (b) {
			String content = "恭喜您，您提交的医院信息经过审核，满足我们入驻资格审查，审批通过，请登录\"佰医汇\"平台"+PropertiesUtil.getString("DOMAINURL")+"使用我们提供的服务，用户名为"
					+ reg.getLoginName()
					+ "，初始密码为"
					+ number
					+ "，请尽快登录平台修改初始密码。【佰医汇】";
			HttpSendSmsUtil
					.sendSmsInteface(hos.getContactorTelphone(), content);
		}
		return map;
	}

	@RequestMapping(value = "/auditthroughs", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> auditthroughs(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hosids = request.getParameter("hosids");
		if (StringUtils.isNotBlank(hosids)) {
			String[] _hosids = hosids.split(",");
			if (_hosids != null && _hosids.length > 0) {
				for (String hos : _hosids) {
					Integer hosid = Integer.parseInt(hos);
					HospitalDetailInfo info = weixinService
							.queryHospitalDetailInfoById(hosid);
					info.setAuditStatus("1");
					weixinService.updateHospitalDetailInfo(info);
				}
			}
		}
		return map;
	}

	private Map<String, Object> createadminaccount(HospitalDetailInfo hos) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(hos.getContactorName())
				&& StringUtils.isNotBlank(hos.getContactorTelphone())) {
			String account = ChineseToPinyinUtil.getPingYin(hos
					.getDisplayName());
			DoctorRegisterInfo reg = null;
			String number = "";
			do {
				number = CheckNumUtil.randomChars_new(6);
				reg = commonService.queryDoctorRegisterInfo(account,
						PasswordUtil.MD5Salt(number));
				if (reg == null) {
					reg = new DoctorRegisterInfo();
				} else {
					reg = null;
				}
			} while (reg == null);
			reg.setLoginName(account);
			reg.setPassword(PasswordUtil.MD5Salt(number));
			reg.setSalt("cvYl8U");
			reg.setMobileNumber(hos.getContactorTelphone());
			reg.setRegisterTime(new Timestamp(new Date().getTime()));
			reg.setLastLoginTime(new Timestamp(new Date().getTime()));
			reg.setUserType(5);
			reg.setStatus(1);
			Integer _id = commonService.saveDoctorRegisterInfo(reg);
			DoctorDetailInfo doc = new DoctorDetailInfo();
			doc.setId(_id);
			doc.setRefreshTime(new Timestamp(new Date().getTime()));
			doc.setDisplayName(hos.getDisplayName());
			doc.setDistCode(hos.getDistCode());
			doc.setStatus(1);
			doc.setHospitalId(hos.getId());
			Integer docid = commonService.saveDoctorDetailInfo(doc);
			map.put("accountid", docid);
			map.put("loginName", account);
			hos.setAccountCreate("1");
			weixinService.updateHospitalDetailInfo(hos);
		}
		return map;
	}

	// 发送短信

	private void hospitalData(HospitalDetailInfo hos, HttpServletRequest request) {
		hos.setDisplayName(request.getParameter("displayName"));
		hos.setShortName(request.getParameter("shortName"));
		hos.setHospitalLevel(StringUtils.isNotBlank(request
				.getParameter("hospitalLevel")) ? Integer.parseInt(request
				.getParameter("hospitalLevel")) : null);
		hos.setDistCode(request.getParameter("distCode"));
		hos.setLocation(request.getParameter("location"));
		hos.setBigPicture(request.getParameter("bigPicture"));
		hos.setIsCooHospital(StringUtils.isNotBlank(request
				.getParameter("coohos")) ? Integer.parseInt(request
				.getParameter("coohos")) : null);
		hos.setKeywords(request.getParameter("keywords"));
		hos.setRemark(request.getParameter("remark"));
		hos.setLat(StringUtils.isNotBlank(request.getParameter("lat")) ? (new BigDecimal(
				request.getParameter("lat"))) : null);
		hos.setLng(StringUtils.isNotBlank(request.getParameter("lng")) ? (new BigDecimal(
				request.getParameter("lng"))) : null);
		hos.setHosProperty(StringUtils.isNotBlank(request
				.getParameter("hosProperty")) ? Integer.parseInt(request
				.getParameter("hosProperty")) : null);
		hos.setHosType(StringUtils.isNotBlank(request.getParameter("hosType")) ? Integer
				.parseInt(request.getParameter("hosType")) : null);
		hos.setHosWebSite(request.getParameter("hosWebSite"));
		hos.setDailyOutpatientNumber(StringUtils.isNotBlank(request
				.getParameter("dailyOutpatientNumber")) ? Integer
				.parseInt(request.getParameter("dailyOutpatientNumber")) : null);
		hos.setDailyAppointNumber(StringUtils.isNotBlank(request
				.getParameter("dailyAppointNumber")) ? Integer.parseInt(request
				.getParameter("dailyAppointNumber")) : null);
		hos.setExternalAppointmentApproach(request
				.getParameter("externalAppointmentApproach"));
		hos.setHosProfile(request.getParameter("hosProfile"));
		hos.setCharacteristicClinic(request
				.getParameter("characteristicClinic"));
		hos.setContactorName(request.getParameter("contactorName"));
		hos.setContactorTelphone(request.getParameter("contactorTelphone"));
		hos.setContactorDuty(request.getParameter("contactorDuty"));
		hos.setContactorEmail(request.getParameter("contactorEmail"));
		hos.setAuthorizeFile(request.getParameter("authorizeFile"));
		hos.setAuditStatus(request.getParameter("auditStatus"));
		hos.setDockingMode(request.getParameter("dockingMode"));// 对接方式
																// 1--简单对接，2--深度对接
	}

	// 所有科室管理
	@RequestMapping(value = "/departs", method = RequestMethod.GET)
	public ModelAndView departs(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取医院
		List<HospitalDetailInfo> hoses = weixinService
				.queryHospitalDetailsALL();
		map.put("hoses", hoses);
		// 获取科室
		List<HospitalDepartmentInfo> departs = weixinService
				.queryHospitalDeparts();
		map.put("departs", departs);
		// 获取标准科室
		List<StandardDepartmentInfo> stands = weixinService
				.queryStandardDepartments();
		map.put("stands", stands);
		return new ModelAndView("admin/depart_list", map);
	}

	// 医院科室设置
	@RequestMapping(value = "/departs/{hosid}", method = RequestMethod.GET)
	public ModelAndView departs(@PathVariable Integer hosid,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		/*
		 * // 获取医院 List<HospitalDetailInfo> hoses = weixinService
		 * .queryHospitalDetailsALL(); map.put("hoses", hoses);
		 */
		// 获取科室
		List<HospitalDepartmentInfo> departs = weixinService
				.queryHospitalDeparts();
		map.put("departs", departs);
		// 获取标准科室
		List<StandardDepartmentInfo> stands = weixinService
				.queryStandardDepartments();
		map.put("stands", stands);
		map.put("hosid", hosid);
		return new ModelAndView("admin/depart_list", map);
	}

	private Map<String, String> convertToMap(JSONParam[] params) {
		Map<String, String> map = new HashMap<String, String>();
		if (params != null && params.length > 0) {
			for (JSONParam jsonParam : params) {
				map.put(jsonParam.getName(), jsonParam.getValue());
			}
		}
		return map;
	}

	// 获取科室数据
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaindeparts", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindoctors(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer hosid = Integer.parseInt(paramMap.get("hosid"));
		Integer type = null;
		String currorhis = paramMap.get("currorhis");
		if (StringUtils.isNotBlank(currorhis)
				&& currorhis.equalsIgnoreCase("ondep")) {
			type = 1;
		} else {
			type = 0;
		}
		StringBuilder stringJson = null;
		Map<String, Object> retmap = weixinService
				.queryHospitalDepartmentssBySystem(searchContent, start,
						length, hosid, type);
		Integer renum = (Integer) retmap.get("num");
		List<HospitalDepartmentInfo> departs = (List<HospitalDepartmentInfo>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		HospitalDepartmentInfo dep = null;
		for (int i = 0; i < departs.size(); i++) {
			dep = departs.get(i);
			stringJson.append("[");
			stringJson.append("\"" + dep.getHospitalId() + "\",");
			stringJson.append("\"" + dep.getId() + "\",");
			stringJson.append("\"" + dep.getHosName() + "\",");
			stringJson.append("\"" + dep.getDisplayName() + "\",");
			stringJson.append("\""
					+ (dep.getLocalDepId() == null ? "" : dep.getLocalDepId())
					+ "\",");
			// 标准科室
			HospitalDetailInfo hos = weixinService
					.queryHospitalDetailInfoById(dep.getHospitalId());
			type = hos.getIsCooHospital() == null ? 2 : 1;
			String standardstr = weixinService.queryStandardDepartsByDepId(
					dep.getId(), type);
			stringJson.append("\"" + standardstr + "\",");
			stringJson.append("\""
					+ (dep.getStatus().equals(1) ? "已上线" : "已下线") + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (departs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		log.info("====json==" + stringJson.toString());
		return stringJson.toString();
	}

	@RequestMapping(value = "/gaindepartsbyhos", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gaindepartsbyhos(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer hosid = Integer.parseInt(request.getParameter("hosid"));
		List<HospitalDepartmentInfo> departs = weixinService
				.queryHospitalDepartmentssByHosId(hosid);
		map.put("departs", departs);
		return map;
	}

	// 获取科室对应的标准科室id集合
	@RequestMapping(value = "/gainstandsbydep", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainstandsbydep(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer depid = Integer.parseInt(request.getParameter("depid"));
		HospitalDepartmentInfo dep = weixinService
				.queryHospitalDepartmentInfoById(depid);
		HospitalDetailInfo hos = weixinService.queryHospitalDetailInfoById(dep
				.getHospitalId());
		Integer type = hos.getIsCooHospital() == null ? 2 : 1;
		List<Integer> sids = weixinService.queryStandsBydep(depid, type);
		map.put("sids", sids);
		return map;
	}

	// 科室与标准科室配置
	@RequestMapping(value = "/deptostand", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> deptostand(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer depid = Integer.parseInt(request.getParameter("depid"));
		HospitalDepartmentInfo dep = weixinService
				.queryHospitalDepartmentInfoById(depid);
		HospitalDetailInfo hos = weixinService.queryHospitalDetailInfoById(dep
				.getHospitalId());
		Integer type = hos.getIsCooHospital() == null ? 2 : 1;
		String sdepids = request.getParameter("sdepids");
		List<Integer> sids = weixinService.queryStandsBydep(depid, type);
		String dids = gainDeleteIds(sids, sdepids);
		DepStandardR dsr = null;
		if (StringUtils.isNotBlank(sdepids)) {
			String[] _sids = sdepids.split(",");
			if (_sids != null && _sids.length > 0) {
				for (String _sid : _sids) {
					dsr = weixinService.queryDepStandardRByConditions(
							Integer.parseInt(_sid), depid, type);
					if (dsr == null) {
						dsr = new DepStandardR();
						dsr.setDepartmentType(type);
						dsr.setDepId(depid);
						dsr.setStandardDepId(Integer.parseInt(_sid));
						weixinService.saveDepStandardR(dsr);
					}
				}
			}
			if (StringUtils.isNotBlank(dids)) {
				weixinService.deleteStandConfigByCondition(dids, depid, type);
			}
		} else {
			// 去除所有的配置
			weixinService.deleteStandConfig(depid, type);
		}
		return map;
	}

	private String gainDeleteIds(List<Integer> sids, String sdepids) {
		StringBuilder sb = new StringBuilder();
		List<Integer> _ss = new ArrayList<Integer>();
		if (sids != null && sids.size() > 0 && StringUtils.isNotBlank(sdepids)) {
			String[] _sdids = sdepids.split(",");
			if (_sdids != null && _sdids.length > 0) {
				for (String _sid : _sdids) {
					if (StringUtils.isNotBlank(_sid))
						_ss.add(Integer.parseInt(_sid));
				}
			}
			for (Integer osid : sids) {
				boolean b = false;
				for (Integer _osid : _ss) {
					if (osid.equals(_osid))
						b = true;
				}
				if (!b) {
					sb.append(osid + ",");
				}
			}
			if (StringUtils.isNotBlank(sb.toString())) {
				sb = sb.deleteCharAt(sb.length() - 1);
			}
		}
		return sb.toString();
	}

	// 新增与修改科室
	@RequestMapping(value = "/saveorupdatedepart", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveorupdatedepart(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String depid = request.getParameter("depid");
		String displayName = request.getParameter("displayName");
		String localDepartId = request.getParameter("localDepartId");
		Integer hosid = Integer.parseInt(request.getParameter("hosid"));
		HospitalDepartmentInfo depart = null;
		if (StringUtils.isNotBlank(depid)) {
			// 编辑科室
			depart = weixinService.queryHospitalDepartmentInfoById(Integer
					.parseInt(depid));
			depart.setDisplayName(displayName);
			depart.setHospitalId(hosid);
			depart.setLocalDepId(StringUtils.isNotBlank(localDepartId) ? Integer
					.parseInt(localDepartId) : null);
			weixinService.updateHospitalDepartmentInfo(depart);
		} else {
			// 新增科室
			depart = new HospitalDepartmentInfo();
			depart.setDisplayName(displayName);
			depart.setHospitalId(hosid);
			depart.setLocalDepId(StringUtils.isNotBlank(localDepartId) ? Integer
					.parseInt(localDepartId) : null);
			depart.setStatus(1);
			weixinService.saveHospitalDepartmentInfo(depart);
		}
		return map;
	}

	// 科室上下线
	@RequestMapping(value = "/onandoffdep", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> onandoffdep(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer depid = Integer.parseInt(request.getParameter("depid"));
		HospitalDepartmentInfo dep = weixinService
				.queryHospitalDepartmentInfoById(depid);
		dep.setStatus((dep.getStatus().equals(1)) ? 0 : 1);
		weixinService.updateHospitalDepartmentInfo(dep);
		return map;
	}

	// 专家管理
	@RequestMapping(value = "/experts", method = RequestMethod.GET)
	public ModelAndView experts(HttpServletRequest request) {
		return new ModelAndView("admin/expert_list");
	}

	// 获取专家数据
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gainexperts", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainexperts(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		StringBuilder stringJson = null;
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> retmap = weixinService.queryExpertsBySystem(
				searchContent, start, length,ostatus);

		Integer renum = (Integer) retmap.get("num");
		List<MobileSpecial> specials = (List<MobileSpecial>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		MobileSpecial special = null;

		for (int i = 0; i < specials.size(); i++) {
			special = specials.get(i);
			stringJson.append("[");
			stringJson.append("\"" + special.getSpecialId() + "\",");
			stringJson.append("\"" + special.getStatus() + "\",");
			stringJson.append("\"" + special.getSpecialName() + "\",");
			stringJson.append("\"" + special.getTelphone() + "\",");
			stringJson.append("\"" + special.getHosName() + "\",");
			stringJson.append("\"" + special.getDepName() + "\",");
			stringJson.append("\"" + special.getDuty() + "\",");
			stringJson.append("\"" + special.getProfession() + "\",");
			StringBuilder sb = new StringBuilder();
			stringJson.append("\"" + sb.toString() + "\",");
			stringJson.append("\""
					+ (special.getRecommond() != null ? "推荐" : "") + "\",");
			stringJson.append("\"" + (special.getAreaOptimal()!=null?(special.getAreaOptimal().equals(1)?"是":"否"):"否") + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");

		}
		if (specials.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		log.info("====json==" + stringJson.toString());
		return stringJson.toString();
	}

	@RequestMapping(value = "/gainopenservice", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> gainopenservice(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer did = Integer.parseInt(request.getParameter("eid"));
		List<DoctorServiceInfo> services = commonService
				.queryOpenDoctorServiceInfo(did,"1,2,4,5");
		StringBuilder sb = new StringBuilder();
		for (DoctorServiceInfo service : services) {
			if (service.getServiceId().equals(4)) {
				sb.append("特需远程门诊,");
			}
			if (service.getServiceId().equals(1)) {
				sb.append("图文咨询,");
			}
			if (service.getServiceId().equals(2)) {
				sb.append("电话问诊,");
			}
			if (service.getServiceId().equals(5)) {
				sb.append("专家咨询,");
			}
		}

		if (sb.length() > 0)
			sb = sb.deleteCharAt(sb.length() - 1);
		map.put("service", sb.toString());
		return map;
	}
	
	//专家服务表导出
		@RequestMapping(value = "/reportExpertService", produces = "text/plain;charset=UTF-8")
		@ResponseBody
		public  void getServiceList(HttpServletResponse response,HttpServletRequest request) throws Exception{
			String hosid = request.getParameter("hosid");
			String depid = request.getParameter("depid");
			//Integer hosid = Integer.valueOf(request.getParameter("hosid"));
			//Integer depid = Integer.valueOf(request.getParameter("depid"));
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			Integer isOpenvedio = ( request.getParameter("isOpenvedio") != null && !"".equals(request.getParameter("isOpenvedio")))?Integer.parseInt(request.getParameter("isOpenvedio")):0;
			Integer isOpentuwen = ( request.getParameter("isOpentuwen") != null && !"".equals(request.getParameter("isOpentuwen")))?Integer.parseInt(request.getParameter("isOpentuwen")):0;
			String title = "专家开通服务信息";
			List<MobileSpecial> service =commonService.getServiceList(hosid,startDate,endDate,isOpenvedio,isOpentuwen,depid);
			JSONArray ja = new JSONArray();
			for (MobileSpecial spe : service) {
				SpeceialBean speceialBean = new SpeceialBean();
				speceialBean.setSpecialName(spe.getSpecialName());
				speceialBean.setTelphone(spe.getTelphone());
				speceialBean.setDepName(spe.getDepName());
				speceialBean.setAdviceAmount(spe.getAdviceAmount());
				speceialBean.setDuty(spe.getDuty());
				speceialBean.setHosName(spe.getHosName());
				speceialBean.setVedioAmount(spe.getVedioAmount());
				speceialBean.setSpecialTitle(spe.getSpecialTitle());
				ja.add(speceialBean);
			}
				HashMap<String, String> headMap = new LinkedHashMap<>();
				headMap.put("specialName", "专家姓名");
				headMap.put("telphone", "联系电话");
				headMap.put("hosName", "医院名称");
				headMap.put("depName", "科室名称");
				headMap.put("duty", "职位");
				headMap.put("specialTitle", "职称");
				headMap.put("adviceAmount", "图文会诊");
				headMap.put("vedioAmount", "视频会诊");
			
				ExcelUtilToService.downloadExcelFile(title, headMap, ja, response);
			}
	

	// 专家详情信息
	@RequestMapping(value = "/intoexpert", method = RequestMethod.GET)
	public ModelAndView intoexpert(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sdid = request.getParameter("did");
		if (StringUtils.isNotBlank(sdid)) {
			Integer did = Integer.parseInt(sdid);
			MobileSpecial special = commonService
					.queryMobileSpecialByUserIdAndUserType(did);
			openserverset(special);
			String relatedPics = special.getRelatedPics();
			List<CustomFileStorage> images = null;
			if (StringUtils.isNotBlank(relatedPics)) {
				images = weixinService
						.queryCustomFileStorageImages(relatedPics);
			}
			map.put("images", images);
			map.put("special", special);
		}
		return new ModelAndView("admin/expert_op", map);
	}

	private void openserverset(MobileSpecial special) {
		DoctorServiceInfo vedio = commonService
				.queryDoctorServiceInfoByOrderType(4, special.getSpecialId());
		DoctorServiceInfo ask = commonService
				.queryDoctorServiceInfoByOrderType(1, special.getSpecialId());
		DoctorServiceInfo tel = commonService
				.queryDoctorServiceInfoByOrderType(2, special.getSpecialId());
		DoctorServiceInfo advice = commonService
				.queryDoctorServiceInfoByOrderType(5, special.getSpecialId());
		if (vedio != null) {
			special.setOpenVedio(vedio.getIsOpen() == null ? 0 : vedio
					.getIsOpen());
			special.setVedioAmount(vedio.getAmount());
		} else {
			special.setOpenVedio(null);
			special.setVedioAmount(null);
		}
		if (ask != null) {
			special.setOpenAsk(ask.getIsOpen() == null ? 0 : ask.getIsOpen());
			special.setAskAmount(ask.getAmount());
		} else {
			special.setOpenAsk(null);
			special.setAskAmount(null);
		}
		if (tel != null) {
			special.setOpenTel(tel.getIsOpen() == null ? 0 : tel.getIsOpen());
			special.setTelAmount(tel.getAmount());
		} else {
			special.setOpenTel(null);
			special.setTelAmount(null);
		}
		if (advice != null) {
			special.setOpenAdvice(advice.getIsOpen() == null ? 0 : advice
					.getIsOpen());
			special.setAdviceAmount(advice.getAmount());
		} else {
			special.setOpenAdvice(null);
			special.setAdviceAmount(null);
		}
	}

	// 新增专家或修改专家信息
	@RequestMapping(value = "/saveorupdateexpert", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveorupdateexpert(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String expertid = request.getParameter("expertid");
		DoctorRegisterInfo reg = null;
		DoctorDetailInfo detail = null;
		if (StringUtils.isNotBlank(expertid)) {
			// 编辑专家
			reg = commonService.queryDoctorRegisterInfoById(Integer
					.parseInt(expertid));
			reg.setLoginName(request.getParameter("telphone"));
			reg.setMobileNumber(request.getParameter("telphone"));
			commonService.updateDoctorRegisterInfo(reg);
			detail = commonService.queryDoctorDetailInfoById(Integer
					.parseInt(expertid));
			expertdata(detail, request);
			commonService.updateDoctorDetailInfo(detail);
		} else {
			// 新增专家
			reg = new DoctorRegisterInfo();
			reg.setLoginName(request.getParameter("telphone"));
			reg.setMobileNumber(request.getParameter("telphone"));
			reg.setRegisterTime(new Timestamp(new Date().getTime()));
			reg.setLastLoginTime(new Timestamp(new Date().getTime()));
			reg.setUserType(2);
			reg.setStatus(1);
			reg.setSalt("cvYl8U");
			reg.setPassword(PasswordUtil.MD5Salt("123456"));
			Integer _id = commonService.saveDoctorRegisterInfo(reg);
			detail = new DoctorDetailInfo();
			detail.setId(_id);
			expertdata(detail, request);
			commonService.saveDoctorDetailInfo(detail);
			// 自动生成专家二维码名片
			String url=PropertiesUtil.getString("DOMAINURL") + "module/patient.html#/docinfo/"+ _id;//进入主界面
			String erweimaUrl = OSSManageUtil.genErweima(
					ChineseToPinyinUtil.getPingYin(detail.getDisplayName()),
					url);
			detail.setErweimaUrl(erweimaUrl);
			commonService.updateDoctorDetailInfo(detail);
			sendExpertSms(reg.getLoginName(),"123456");
			map.put("defpass", "123456");
		}
		return map;
	}
	
	private void sendExpertSms(String telphone,String password){
		String content="恭喜您成功注册为佰医汇平台用户，登录账号："+telphone+"，密码："+password+"，为保证您的账户安全，请及时修改初始密码。如有疑问请拨打客服热线400-890-5111。点击http://dwz.cn/5TXbka  ，免费下载佰医汇客户端。【佰医汇】";
		HttpSendSmsUtil.sendSmsInteface(telphone, content);
	}
	

	private void expertdata(DoctorDetailInfo doc, HttpServletRequest request) {
		doc.setRefreshTime(new Timestamp(new Date().getTime()));
		doc.setDisplayName(request.getParameter("displayName"));
		doc.setSex(Integer.valueOf(request.getParameter("sex")));
		doc.setHeadImageUrl(request.getParameter("headImageUrl"));
		doc.setDistCode(request.getParameter("distcode"));
		doc.setDuty(request.getParameter("duty"));
		doc.setPosition(request.getParameter("position"));
		doc.setIdCardNo(request.getParameter("idCardNo"));
		doc.setStatus(1);
		doc.setHospitalId(Integer.parseInt(request.getParameter("hosid")));
		doc.setDepId(Integer.parseInt(request.getParameter("depid")));
		if (StringUtils.isNotBlank(request.getParameter("recommond"))) {
			doc.setRecommend(Integer.parseInt(request.getParameter("recommond")));
		} else {
			doc.setRecommend(null);
		}
		processopenserver(request, doc.getId());
		doc.setSpeciality(request.getParameter("speciality"));
		doc.setProfile(request.getParameter("profile"));
		doc.setProfession(request.getParameter("profession"));
		doc.setRelatedPics(request.getParameter("relatedPics"));
		dutyIdSet(doc);
	}
	
	private void dutyIdSet(DoctorDetailInfo doc){
		if(StringUtils.isNotBlank(doc.getDuty())){
			if(doc.getDuty().equalsIgnoreCase("主任医师")){
				doc.setDutyId(4);
			}else if(doc.getDuty().equalsIgnoreCase("主治医师")){
				doc.setDutyId(6);
			}else if(doc.getDuty().equalsIgnoreCase("副主任医师")){
				doc.setDutyId(5);
			}else if(doc.getDuty().equalsIgnoreCase("住院医师")){
				doc.setDutyId(44);
			}
		}
	}
	
	private void processopenserver(HttpServletRequest request, Integer docid) {
		DoctorServiceInfo vedio = commonService
				.queryDoctorServiceInfoByOrderType(4, docid);
		DoctorServiceInfo ask = commonService
				.queryDoctorServiceInfoByOrderType(1, docid);
		DoctorServiceInfo tel = commonService
				.queryDoctorServiceInfoByOrderType(2, docid);
		DoctorServiceInfo advice = commonService
				.queryDoctorServiceInfoByOrderType(5, docid);
		String openVedio = request.getParameter("openVedio");
		String openAsk = request.getParameter("openAsk");
		String openTel = request.getParameter("openTel");
		String openAdvice = request.getParameter("openAdvice");
		BigDecimal vedioAmount = null;
		BigDecimal askAmount = null;
		BigDecimal telAmount = null;
		BigDecimal adviceAmount = null;
		if (StringUtils.isNotBlank(request.getParameter("vedioAmount"))) {
			vedioAmount = new BigDecimal(request.getParameter("vedioAmount"));
		}
		if (StringUtils.isNotBlank(request.getParameter("askAmount"))) {
			askAmount = new BigDecimal(request.getParameter("askAmount"));
		}
		if (StringUtils.isNotBlank(request.getParameter("telAmount"))) {
			telAmount = new BigDecimal(request.getParameter("telAmount"));
		}
		if (StringUtils.isNotBlank(request.getParameter("adviceAmount"))) {
			adviceAmount = new BigDecimal(request.getParameter("adviceAmount"));
		}
		if (StringUtils.isNotBlank(openVedio)
				&& openVedio.equalsIgnoreCase("1")) {
			if (vedio != null) {
				vedio.setIsOpen(1);
				vedio.setAmount(vedioAmount);
				vedio.setShowPrice(vedioAmount);
				commonService.updateDoctorServiceInfo(vedio);
			} else {
				SystemServiceInfo ssi = commonService.querySystemServiceById(4);
				vedio = new DoctorServiceInfo();
				vedio.setDoctorId(docid);
				vedio.setServiceId(4);
				vedio.setPackageId(3);
				vedio.setIsOpen(1);
				vedio.setAmount(vedioAmount);
				vedio.setShowPrice(commonManager.processD2DVedioMoney(vedioAmount, BigDecimal.valueOf(Double.valueOf(ssi.getPriceParameter()))));
				commonService.saveDoctorServiceInfo(vedio);
			}
		} else {
			if (vedio != null) {
				vedio.setIsOpen(0);
				vedio.setAmount(vedioAmount);
				vedio.setShowPrice(vedioAmount);
				commonService.updateDoctorServiceInfo(vedio);
			}
		}

		if (StringUtils.isNotBlank(openAsk) && openAsk.equalsIgnoreCase("1")) {
			if (ask != null) {
				ask.setIsOpen(1);
				ask.setAmount(askAmount);
				ask.setShowPrice(askAmount);
				commonService.updateDoctorServiceInfo(ask);
			} else {
				ask = new DoctorServiceInfo();
				ask.setDoctorId(docid);
				ask.setServiceId(1);
				ask.setPackageId(1);
				ask.setIsOpen(1);
				ask.setAmount(askAmount);
				ask.setShowPrice(askAmount);
				commonService.saveDoctorServiceInfo(ask);
			}
		} else {
			if (ask != null) {
				ask.setIsOpen(0);
				ask.setAmount(askAmount);
				ask.setAmount(askAmount);
				commonService.updateDoctorServiceInfo(ask);
			}
		}

		if (StringUtils.isNotBlank(openTel) && openTel.equalsIgnoreCase("1")) {
			if (tel != null) {
				tel.setIsOpen(1);
				tel.setAmount(telAmount);
				tel.setShowPrice(telAmount);
				commonService.updateDoctorServiceInfo(tel);
			} else {
				tel = new DoctorServiceInfo();
				tel.setDoctorId(docid);
				tel.setServiceId(2);
				tel.setPackageId(2);
				tel.setIsOpen(1);
				tel.setAmount(telAmount);
				tel.setShowPrice(telAmount);
				commonService.saveDoctorServiceInfo(tel);
			}
		} else {
			if (tel != null) {
				tel.setIsOpen(0);
				tel.setAmount(telAmount);
				tel.setShowPrice(telAmount);
				commonService.updateDoctorServiceInfo(tel);
			}
		}

		if (StringUtils.isNotBlank(openAdvice)
				&& openAdvice.equalsIgnoreCase("1")) {
			if (advice != null) {
				advice.setIsOpen(1);
				advice.setAmount(adviceAmount);
				advice.setShowPrice(commonManager.processD2DAskMoney(adviceAmount));
				commonService.updateDoctorServiceInfo(advice);
			} else {
				advice = new DoctorServiceInfo();
				advice.setDoctorId(docid);
				advice.setServiceId(5);
				advice.setPackageId(5);
				advice.setIsOpen(1);
				advice.setAmount(adviceAmount);
				advice.setShowPrice(commonManager.processD2DAskMoney(adviceAmount));
				commonService.saveDoctorServiceInfo(advice);
			}
		} else {
			if (advice != null) {
				advice.setIsOpen(0);
				advice.setAmount(adviceAmount);
				advice.setShowPrice(adviceAmount);
				commonService.updateDoctorServiceInfo(advice);
			}
		}
	}

	/**
	 * 医生名片
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/docform/{docid}", method = RequestMethod.GET)
	public ModelAndView docform(@PathVariable Integer docid,
			HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorDetailInfo detail = commonService
				.queryDoctorDetailInfoById(docid);
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(docid);
		String relatedPics = special.getRelatedPics();
		List<CustomFileStorage> images = null;
		if (StringUtils.isNotBlank(relatedPics)) {
			images = weixinService.queryCustomFileStorageImages(relatedPics);
		}
		map.put("images", images);
		if (!StringUtils.isNotBlank(detail.getErweimaUrl())) {
			// 没有二维码，需要生成该专家的二维码图片
			String url=PropertiesUtil.getString("DOMAINURL") + "module/patient.html#/docinfo/"+ docid;//进入主界面
			String erweimaUrl = OSSManageUtil.genErweima(
					ChineseToPinyinUtil.getPingYin(detail.getDisplayName()),
					url);
			special.setErweimaUrl(erweimaUrl);
			detail.setErweimaUrl(erweimaUrl);
			commonService.updateDoctorDetailInfo(detail);
		}
		processServer(special);
		map.put("special", special);
		return new ModelAndView("admin/docform", map);
	}
	private void processServer(MobileSpecial special){
		DoctorServiceInfo vedio=commonService.queryDoctorServiceInfoByCon(special.getSpecialId(), 4, null);
		DoctorServiceInfo tuwen=commonService.queryDoctorServiceInfoByCon(special.getSpecialId(), 1, null);
		DoctorServiceInfo tel=commonService.queryDoctorServiceInfoByCon(special.getSpecialId(), 2, null);
		DoctorServiceInfo advice=commonService.queryDoctorServiceInfoByCon(special.getSpecialId(), 5, null);
		if(vedio!=null){
			SystemServiceInfo ssi = commonService.querySystemServiceById(4);
			special.setOpenVedio(vedio.getIsOpen());
			special.setVedioAmount(vedio.getShowPrice() != null?vedio.getShowPrice():
				commonManager.processD2DVedioMoney(vedio.getAmount(), BigDecimal.valueOf(Double.valueOf(ssi.getPriceParameter()))));
		}
		if(tuwen!=null){
			special.setOpenAsk(tuwen.getIsOpen());
			special.setAskAmount(tuwen.getAmount());
		}
		if(tel!=null){
			special.setOpenTel(tel.getIsOpen());
			special.setTelAmount(tel.getAmount());
		}
		if(advice!=null){
			special.setOpenAdvice(advice.getIsOpen());
			special.setAdviceAmount(advice.getShowPrice() != null?advice.getShowPrice():commonManager.processD2DAskMoney(advice.getAmount()));
		}
	}

	// 获取省市
	@RequestMapping(value = "/gainproorcitys", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainprovinces(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String stype = request.getParameter("stype");// 1===province,2===city,3===区县
		String procost = request.getParameter("procost");
		List<DistCode> codes = commonService.queryDistCodesByConditions(stype,
				procost);
		map.put("codes", codes);
		return map;
	}

	// 根据省获取医院
	@RequestMapping(value = "/gainhosbyprovin", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainhosbycity(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String procost = request.getParameter("procost");
		String type = request.getParameter("type");
		List<HospitalDetailInfo> hospitals = commonService
				.queryHospitalsByPro(procost,type);
		map.put("hospitals", hospitals);
		return map;
	}

	// ==================================================================================医院入驻===============================
	@RequestMapping(value = "/hosenter", method = RequestMethod.GET)
	public ModelAndView hosenter(HttpServletRequest request) {
		return new ModelAndView("admin/hos_enter");
	}

	@RequestMapping(value = "/gainDictorys", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainDictorys(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer pid = Integer.parseInt(request.getParameter("pid"));
		List<Dictionary> dics = weixinService.queryDictionarysByParentId(pid);
		map.put("dics", dics);
		return map;
	}

	// 远程订单管理
	@RequestMapping(value = "/helporders", method = RequestMethod.GET)
	public ModelAndView helporders(HttpServletRequest request) {
		return new ModelAndView("admin/help_orders");
	}

	private String gaintime(BusinessVedioOrder rc) {
		String time = "";
		if (StringUtils.isNotBlank(rc.getConsultationDate())) {
			time += rc.getConsultationDate();
		}
		if (StringUtils.isNotBlank(rc.getConsultationTime())) {
			time += " " + rc.getConsultationTime();
		}
		return time;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gainhelporders", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainhelporders(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);

		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer type=Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> retmap = null;
		retmap = weixinService.queryhelporders(searchContent, start,
				length, type);
		StringBuilder stringJson = null;

		Integer renum = (Integer) retmap.get("num");
		List<BusinessVedioOrder> orders = (List<BusinessVedioOrder>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		BusinessVedioOrder order = null;
		for (int i = 0; i < orders.size(); i++) {
			order = orders.get(i);
			stringJson.append("[");
			stringJson.append("\"" + order.getId() + "\",");
			stringJson.append("\"" + type + "\",");
			String time = gaintime(order);
			stringJson.append("\""
					+ (time.equalsIgnoreCase("null null") ? "未设置" : time)
					+ "\",");
			long cd = 0;
			if (StringUtils.isNotBlank(time) && time.length() > 10) {
				cd = DateUtil.calculateSecond(time + ":00",
						_sdf.format(new Date()));
			}
			if (cd > 0 && cd >= 1800) {
				stringJson.append("\"" + "1" + "\",");// 超时了
			} else {
				stringJson.append("\"" + "2" + "\",");// 没超时
			}
			stringJson.append("\""
					+ StringRetUtil.gainStringByStats(order.getStatus(),
							order.getProgressTag(), 2) + "\",");
			stringJson.append("\"" + order.getPatientName() + "\",");
			String sex = order.getSex() != null ? (order.getSex().equals(1) ? "男"
					: "女")
					: "未知";
			stringJson.append("\"" + sex + "\",");
			stringJson.append("\"" + order.getTelephone() + "\",");
			stringJson.append("\"" + order.getLocalHosName() + "\",");
			stringJson.append("\"" + order.getLocalDepName() + "\",");
			stringJson.append("\"" + order.getLocalDocName() + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(order.getHosName()) ? order
							.getHosName() : "未配置") + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(order.getDepName()) ? order
							.getDepName() : "未配置") + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(order.getExpertName()) ? order
							.getExpertName() : "未配置") + "\",");
			stringJson.append("\""
					+ ((order.getPayStatus() != null) ? (order.getPayStatus()
							.equals(1) ? "已支付" : "未支付") : "未支付") + "\",");
			stringJson
					.append("\"" + _sdf.format(order.getCreateTime()) + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(order.getExLevel()) ? order
							.getExLevel() : "") + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		log.info("====json==" + stringJson.toString());
		return stringJson.toString();
	}

	private String gainTransactionId(String result) throws Exception {
		String transaction_id = "";
		if (StringUtils.isNotBlank(result)) {
			Document doc = DocumentHelper.parseText(result);
			Element rootElt = doc.getRootElement(); // 获取根节点
			transaction_id = rootElt.elementText("transaction_id");
		}
		return transaction_id;
	}

	private boolean isXML(String value) {
		try {
			if (StringUtils.isNotBlank(value)) {
				DocumentHelper.parseText(value);
			} else {
				return false;
			}
		} catch (DocumentException e) {
			return false;
		}
		return true;
	}

	// 查看详情
	@RequestMapping(value = "/orderdetail/{oid}", method = RequestMethod.GET)
	public ModelAndView orderdetail(@PathVariable Integer oid,
			HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessVedioOrder order = wenzhenService
				.queryBusinessVedioOrderById(oid);
		CaseInfo caseinfo = wenzhenService.queryCaseInfoById(order.getCaseId());
		map.put("order", order);
		map.put("caseinfo", caseinfo);
		//病例图片
		map.put("caseimages", getAttahments(caseinfo));
		List<CustomFileStorage> normals = new ArrayList<CustomFileStorage>();
		// 支付信息
		List<BusinessPayInfo> payinfos = wenzhenService
				.queryBusinesPayInfosByOId(oid, 4);
		map.putAll(payinfo(payinfos));
		String normalImages = caseinfo.getNormalImages();
		CustomFileStorage cu = null;
		if (StringUtils.isNotBlank(normalImages)) {
			String[] _images = normalImages.split(",");
			if (_images != null && _images.length > 0) {
				for (String _image : _images) {
					cu = commonService.queryCustomFileStorage(Integer
							.parseInt(_image));
					if (cu != null) {
						String filename = cu.getFileUrl();
						cu.setFileType(filename.substring(filename
								.lastIndexOf(".") + 1));
						normals.add(cu);
					}
				}
			}
		}
		map.put("normals", normals);
		// 如果是自定义级别需要查询运营人员
		BusinessOperativeInfo op = weixinService
				.queryBusinessOperativeInfoByOid(oid);
		map.put("opinfo", op);
		//判断是否需要退款
		if((order.getStatus().equals(30)||order.getStatus().equals(50))&&order.getPayStatus()!=null&&order.getPayStatus().equals(1)){
			BigDecimal money=(BigDecimal)map.get("money");
			if(money!=null&&money.compareTo(BigDecimal.ZERO)>0){
				Object obj=map.get("refundStatus");
				if(obj!=null&&Integer.valueOf(obj.toString()).equals(1)){
					//退款成功，不需要显示退款
				}else{
					//待退款或退款失败
					map.put("needRefund", "true");
				}
			}
		}		
		
		return new ModelAndView("admin/order_detail", map);
	}

	/**
	 * 加载可以分配的专家 访问：http://localhost:8080/system/loadexperts 参数：oid订单id
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loadexperts")
	@ResponseBody
	public Map<String, Object> loadexperts(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 订单id
		String keywords = request.getParameter("keywords");// 检索关键字
		List<MobileSpecial> specials = null;
		if (StringUtils.isNotBlank(keywords)) {
			// 符合条件的所有专家
			specials = weixinService
					.queryMobileSpecialsByLocalDepartId_new(keywords);
		} else {
			// 该医生科室相应的专家
			BusinessVedioOrder order = commonService
					.queryBusinessVedioOrderById(oid);
			specials = weixinService.queryMobileSpecialsByLocalDepartId_new(
					order.getLocalDepartId(), 4);
		}
		map.put("specials", specials);
		return map;
	}

	@RequestMapping(value = "/delorder", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> delorder(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(oid);
		order.setDelFlag(1);
		commonService.updateBusinessVedioOrder(order);
		return map;
	}
	/**
	 * 恢复订单
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/restart")
	@ResponseBody
	public Map<String,Object> restart(HttpServletRequest request){
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(oid);
		order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey());
		order.setVedioDur(null);
		commonService.updateBusinessVedioOrder(order);
		return null;
	}
	@RequestMapping(value = "/gainspecials", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainspecials(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(oid);
		List<MobileSpecial> specials = weixinService
				.queryMobileSpecialsByLocalDepartId_new(
						order.getLocalDepartId(), 4);
		map.put("specials", specials);
		return map;
	}

	// 分配专家
	@RequestMapping(value = "/distexpert", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> distexpert(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		Integer expertid = Integer.parseInt(request.getParameter("expertId"));// 专家id
		String stimeid = request.getParameter("stimeid");// 选择的时间
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(oid);
		order.setExpertId(expertid);
		boolean b = false;
		if (StringUtils.isNotBlank(stimeid)) {
			order.setSchedulerDateId(Integer.parseInt(stimeid));
			ExpertConsultationSchedule ecs = weixinService
					.queryExpertConScheduleById(Integer.parseInt(stimeid));
			order.setConsultationDate(ecs.getScheduleDate());
			order.setConsultationTime(ecs.getStartTime());
			order.setConsultationDur(OrderStatusEnum.VEDIO_DURATION.getKey());
			b = true;
		}
		commonService.updateBusinessVedioOrder(order);
		// 更新消费记录
		/*
		 * UserBillRecord
		 * bill=wenzhenService.queryUserBillByCondition("特需远程门诊",oid);
		 * if(bill!=null){ bill.setDoctorId(expertid);
		 * wenzhenService.updateUserBillRecord(bill); }
		 */
		if (b) {
			// 更新时间为已选
			ExpertConsultationSchedule sch = weixinService
					.queryExpertConScheduleById(order.getSchedulerDateId());
			sch.setHasAppoint("1");
			weixinService.updatExpertConScheduleDate(sch);
		}
		// 记录消息
		CaseInfo ca = wenzhenService.queryCaseInfoById(order.getCaseId());
		commonManager.generateSystemPushInfo(21, order.getUuid(), 4,
				order.getLocalDoctorId(), 3,null, "来自患者" + ca.getContactName()
						+ "的会诊订单，已被分派专家");
		// 维护群组
		commonManager.joinGroup(order.getUuid(), 4);
		return map;
	}

	// 图文问诊订单管理
	@RequestMapping(value = "/tuwenmanage", method = RequestMethod.GET)
	public ModelAndView tuwenmanage(HttpServletRequest request) {
		return new ModelAndView("admin/tuwen_list");
	}

	// 获取图文问诊订单数据
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaintuwendatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaintuwendatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer _type=Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> retmap = wenzhenService
				.queryTuwenOrdersByConditions(searchContent, start, length,
						_type);
		StringBuilder stringJson = null;
		Integer renum = (Integer) retmap.get("num");
		List<WenzhenBean> orders = (List<WenzhenBean>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		WenzhenBean order = null;
		for (int i = 0; i < orders.size(); i++) {
			order = orders.get(i);
			stringJson.append("[");
			stringJson.append("\"" + order.getId() + "\",");
			stringJson.append("\""
					+ ((order.getPatMessageCount() == null ? 0 : order
							.getPatMessageCount()) + "-" + (order
							.getDocMessageCount() == null ? 0 : order
							.getDocMessageCount())) + "\",");
			stringJson
					.append("\"" + _sdf.format(order.getCreateTime()) + "\",");
			stringJson.append("\"" + order.getContactName() + "\",");
			stringJson
					.append("\""
							+ ((order.getSex() != null) ? (order.getSex()
									.equals(1) ? "男" : "女") : "未知") + "\",");
			stringJson.append("\""
					+ (order.getAge() == null ? "未知" : order.getAge()) + "\",");
			stringJson.append("\"" + order.getDocName() + "\",");
			stringJson.append("\"" + order.getHospital() + "\",");
			stringJson.append("\"" + order.getDepart() + "\",");
			String stat = StringRetUtil.gainstatusdesc(order.getAskStatus());
			stringJson.append("\"" + stat + "\",");
			stringJson.append("\""
					+ gainPayStatus(order.getId(), order.getPayStatus(), 1)
					+ "\",");
			stringJson.append("\"" + gainSource(order.getSource()) + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	private String gainSource(Integer source) {
		String _s = "";
		switch (source) {
		case 1:
			_s = "IOS";
			break;
		case 2:
			_s = "Android";
			break;
		case 3:
			_s = "微信公号";
			break;
		case 4:
			_s = "官网";
			break;
		case 6:
			_s="专家远程申请单";
			break;
		}
		return _s;
	}

	private String gainPayStatus(Integer oid, Integer payStatus, Integer otype) {

		String status = "";
		if (payStatus != null) {
			switch (payStatus) {
			case 0:
				status = "免费";
				break;
			case 1:
				status = "已支付";
				break;
			case 4:
				status = "待支付";
				break;
			}
		} else {
			List<BusinessPayInfo> oinfos = wenzhenService
					.queryBusinesPayInfosByOId(oid, otype);
			if (oinfos != null && oinfos.size() > 0) {
				boolean b = true;
				for (BusinessPayInfo _oinfo : oinfos) {
					if (_oinfo.getPayStatus().equals(4)) {
						b = false;
					}
				}
				if (!b) {
					status = "待支付";
				} else {
					status = "已支付";
				}
			} else {
				status = "免费";
			}
		}
		return status;
	}

	// 查看图文详情
	@RequestMapping(value = "/twdetail/{oid}", method = RequestMethod.GET)
	public ModelAndView twdetail(@PathVariable Integer oid,
			HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessTuwenOrder tw = wenzhenService.queryBusinessTuwenInfoById(oid);
		// 聊天消息
		List<BusinessMessageBean> messages = wenzhenService
				.queryBusinessMessageBeansByCon(oid,tw.getUuid(),1);
		// 病例信息
		CaseInfo cinfo = wenzhenService.queryCaseInfoById(tw.getCaseId());
		// 病例图片
		map.put("caseimages", getAttahments(cinfo));
		map.put("twinfo", tw);
		map.put("messages", messages);
		map.put("cinfo", cinfo);
		//病例图片
		map.put("caseimages", getAttahments(cinfo));
		String transactionId = "";
		String outTradeNo = "";
		BigDecimal money = null;
		List<BusinessPayInfo> payinfos = wenzhenService
				.queryBusinesPayInfosByOId(oid, 1);
		if (payinfos != null && payinfos.size() > 0) {
			if (payinfos.size() == 1) {
				outTradeNo = payinfos.get(0).getOutTradeNo();
				money = payinfos.get(0).getTotalMoney();
				transactionId = gainTransactionId(payinfos.get(0)
						.getOutTradeResult());
			} else {
				outTradeNo = payinfos.get(payinfos.size() - 1).getOutTradeNo();
				money = payinfos.get(payinfos.size() - 1).getTotalMoney();
				transactionId = gainTransactionId(payinfos.get(
						payinfos.size() - 1).getOutTradeResult());
			}
		}
		map.put("transactionId", transactionId);
		map.put("outTradeNo", outTradeNo);
		map.put("money", money);
		return new ModelAndView("admin/tuwen_detail", map);
	}

	// ============================电话订单管理----------------------------
	@RequestMapping(value = "/telorders", method = RequestMethod.GET)
	public ModelAndView telorders(HttpServletRequest request) {
		return new ModelAndView("admin/tel_orders");
	}

	// 获取电话订单数据
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaintelorders", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaintelorders(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		String docid = paramMap.get("docid");
		Map<String, Object> retmap = wenzhenService.querytelorders(
				searchContent, start, length, docid,ostatus);
		StringBuilder stringJson = null;
		Integer renum = (Integer) retmap.get("num");
		List<WenzhenBean> orders = (List<WenzhenBean>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		WenzhenBean order = null;
		for (int i = 0; i < orders.size(); i++) {
			order = orders.get(i);
			stringJson.append("[");
			stringJson.append("\"" + order.getId() + "\",");
			stringJson
					.append("\"" + _sdf.format(order.getCreateTime()) + "\",");
			stringJson.append("\"" + order.getContactName() + "\",");
			stringJson
					.append("\""
							+ ((order.getSex() != null) ? (order.getSex()
									.equals(1) ? "男" : "女") : "未知") + "\",");
			stringJson.append("\""
					+ (order.getAge() == null ? "未知" : order.getAge()) + "\",");
			stringJson.append("\"" + order.getTelephone() + "\",");
			stringJson.append("\"" + order.getDocName() + "\",");
			stringJson.append("\"" + order.getHospital() + "\",");
			stringJson.append("\"" + order.getDepart() + "\",");
			stringJson.append("\"" + order.getDocTel() + "\",");
			String stat = StringRetUtil.gainstatusdesc(order.getAskStatus());
			stringJson.append("\"" + stat + "\",");
			stringJson.append("\""
					+ gainPayStatus(order.getId(), order.getPayStatus(), 2)
					+ "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		log.info("====json==" + stringJson.toString());
		return stringJson.toString();
	}

	// 查看电话订单详情
	@RequestMapping(value = "/teldetail/{oid}", method = RequestMethod.GET)
	public ModelAndView teldetail(@PathVariable Integer oid,
			HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessTelOrder telinfo = wenzhenService
				.queryBusinessTelOrderById(oid);
		// 病例信息
		CaseInfo cinfo = wenzhenService.queryCaseInfoById(telinfo.getCaseId());
		// 病例图片
		map.put("caseimages", getAttahments(cinfo));
		map.put("telinfo", telinfo);
		map.put("cinfo", cinfo);
		String transactionId = "";
		String outTradeNo = "";
		BigDecimal money = null;
		List<BusinessPayInfo> payinfos = wenzhenService
				.queryBusinesPayInfosByOId(oid, 2);
		if (payinfos != null && payinfos.size() > 0) {
			if (payinfos.size() == 1) {
				outTradeNo = payinfos.get(0).getOutTradeNo();
				money = payinfos.get(0).getTotalMoney();
				transactionId = gainTransactionId(payinfos.get(0)
						.getOutTradeResult());
			} else {
				outTradeNo = payinfos.get(payinfos.size() - 1).getOutTradeNo();
				money = payinfos.get(payinfos.size() - 1).getTotalMoney();
				transactionId = gainTransactionId(payinfos.get(
						payinfos.size() - 1).getOutTradeResult());
			}
		}
		map.put("transactionId", transactionId);
		map.put("outTradeNo", outTradeNo);
		map.put("money", money);
		return new ModelAndView("admin/tel_detail", map);
	}

	/**
	 * 进入充值码界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/chargeCodes", method = RequestMethod.GET)
	public ModelAndView chargeCodes(HttpServletRequest request) {

		return new ModelAndView("admin/charge_codes");
	}

	// 获取电话订单数据
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gainchargecodes", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainchargecodes(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Map<String, Object> retmap = commonService.queryMoneyExchangeCodes(
				searchContent, start, length, 1);
		StringBuilder stringJson = null;
		Integer renum = (Integer) retmap.get("num");
		List<MoneyExchange> orders = (List<MoneyExchange>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		MoneyExchange order = null;
		for (int i = 0; i < orders.size(); i++) {
			order = orders.get(i);
			stringJson.append("[");
			stringJson.append("\"" + order.getId() + "\",");
			stringJson.append("\"" + order.getCharCode() + "\",");
			stringJson.append("\"" + order.getMoney() + "\",");
			stringJson.append("\"" + order.getDeadLine() + "\",");
			stringJson.append("\""
					+ (order.getUseOrNot().equals(0) ? "未使用" : "已使用") + "\",");
			stringJson.append("\""
					+ (order.getStatus().equals(0) ? "失效码" : "有效码") + "\",");
			stringJson.append("\"" + order.getCreateTime() + "\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		log.info("====json==" + stringJson.toString());
		return stringJson.toString();
	}

	@RequestMapping(value = "/generatecodes", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> generatecodes(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String money = request.getParameter("money");
		Integer num = Integer.parseInt(request.getParameter("num"));
		String code = "";
		for (int i = 0; i < num; i++) {
			MoneyExchange me;
			do {
				code = CheckNumUtil.randomChars_new(6);
				me = commonService.queryMoneyExchangeByConditions(code);
				if (me == null) {
					me = new MoneyExchange();
				} else {
					me = null;
				}
			} while (me == null);
			me.setCharCode(code);
			me.setCreateTime(_sdf.format(new Date()));
			me.setDeadLine("");
			me.setMoney(new BigDecimal(money));
			me.setMoneyType(1);
			me.setUseOrNot(0);
			me.setStatus(1);
			commonService.saveMoneyExchange(me);
		}
		return map;
	}

	// 生成分享链接
	@RequestMapping(value = "/generateUrl", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> generateUrl(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String ltype = request.getParameter("ltype");
		String oid = request.getParameter("oid");
		ShortUrlRelate surl = commonService.queryShortUrlRelate(ltype, oid);
		String code = "";
		if (surl == null) {
			code = GenerageShortUrl.shortUrl(ltype + "/" + oid);
			surl = new ShortUrlRelate();
			surl.setOrderId(Integer.parseInt(oid));
			surl.setOrderType(Integer.parseInt(ltype));
			surl.setShortCode(code);
			commonService.saveShortUrlRelate(surl);
		} else {
			code = surl.getShortCode();
		}
		map.put("ucode", code);
		return map;
	}

	/**
	 * 医生审核
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/docaudit")
	public ModelAndView docaudit(HttpServletRequest request) {
		return new ModelAndView("admin/doc_audit");
	}

	/**
	 * 加载需要审核的医生
	 * 
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loaddocauditdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object loaddocauditdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
		return systemAdminManager.queryNeedAuditDocs(sEcho, ostatus,searchContent,
				start, length);
	}

	/**
	 * 医生详情 访问：http://localhost:8080/system/docauditdetail/{docid}
	 * 
	 * @param request
	 * @param docid
	 * @return
	 */
	@RequestMapping(value = "/auditdocdetail/{docid}")
	public ModelAndView auditdocdetail(HttpServletRequest request,
			@PathVariable Integer docid) {
		Map<String, Object> map = new HashMap<String, Object>();
		MobileSpecial doc = commonService.queryAuditDocDetailById(docid);
		map.put("special", doc);
		map.put("docid", docid);
		List<Dictionary> dics=weixinService.queryDictionarysByParentId(2);
		map.put("dics", dics);
		return new ModelAndView("admin/doc_detail", map);
	}

	/**
	 * 医生详情 访问：http://localhost:8080/system/docdetail/{docid}
	 * 
	 * @param request
	 * @param docid
	 * @return
	 */
	@RequestMapping(value = "/docdetail/{docid}")
	public ModelAndView docdetail(HttpServletRequest request,
			@PathVariable Integer docid) {
		Map<String, Object> map = new HashMap<String, Object>();
		MobileSpecial doc = commonService
				.queryMobileSpecialByUserIdAndUserType(docid);
		map.put("special", doc);
		System.out.println(JSONObject.fromObject(doc).toString());
		return new ModelAndView("admin/doc_detail", map);
	}

	private String processErweima(Integer docid) {
		
		DoctorSceneEwm dse=commonService.queryDoctorSceneEwmByDoctorId(docid);
		if(dse != null) {
			if(!StringUtils.isNotBlank(dse.getRealUrl())){
				//更新二维码
				WeiAccessToken wat=weixinService
		                .queryWeiAccessTokenById(PropertiesUtil.getString("APPID"));
				String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + wat.getAccessToken();
				String json = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" +"docinfo_"+docid + "\"}}}";
				log.info("json:"+json);
				JSONObject obj =  CommonUtil.httpRequest(url, "POST", json);
				String ticket = obj.getString("ticket");
				dse.setRealUrl(obj.getString("url"));
				dse.setErweimaUrl("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket);
				commonService.updateDoctorSceneErweima(dse);
			}else {
				return dse.getErweimaUrl();
			}
		}else {
			WeiAccessToken wat=weixinService
	                .queryWeiAccessTokenById(PropertiesUtil.getString("APPID"));
			String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + wat.getAccessToken();
			String json = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" +"docinfo_"+docid + "\"}}}";
			log.info("json:"+json);
			JSONObject obj =  CommonUtil.httpRequest(url, "POST", json);
			String ticket = obj.getString("ticket");
			dse=new DoctorSceneEwm();
			dse.setDoctorId(docid);
			dse.setRealUrl(obj.getString("url"));
			dse.setErweimaUrl("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket);
			commonService.saveDoctorSceneErweima(dse);
			return dse.getErweimaUrl();
		}
		return null;
	}
	/**
	 * 提交审核 访问：http://localhost:8080/system/auditdoc
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/auditdoc")
	@ResponseBody
	public Map<String, Object> auditdoc(HttpServletRequest request) throws Exception{
		Integer docid = Integer.parseInt(request.getParameter("docid"));// 医生id
		Integer auditvalue = Integer.parseInt(request
				.getParameter("auditvalue"));// 审核值 审核通过：1 审核不通过：-3
		String refusalReason = request.getParameter("refusalReason");//审核不通过理由
		// 如果医院是手动输入,新建医院
		DoctorRegisterInfo reg = commonService
				.queryDoctorRegisterInfoById(docid);
		DoctorDetailInfo detail = commonService
				.queryDoctorDetailInfoById(docid);
		if(reg.getStatus() != null && !reg.getStatus().equals(1)) {
			String msg="";
			// 审核通过
			if (auditvalue.equals(1)) {
				Integer hosid = Integer.parseInt(request.getParameter("hosid"));
				Integer depid = Integer.parseInt(request.getParameter("depid"));
				HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(hosid);
				HospitalDepartmentInfo dep=weixinService.queryHospitalDepartmentInfoById(depid);
				detail.setRegHospitalName(hos.getDisplayName());
				detail.setRegDepartmentName(dep.getDisplayName());
				detail.setHospitalId(hosid);
				detail.setDepId(depid);
				String erweimaUrl = processErweima(docid);
				detail.setErweimaUrl(erweimaUrl);
				commonService.updateDoctorDetailInfo(detail);
				//动态控制是否开通默认服务
				if("true".equals(ReaderConfigUtil.gainConfigVal(request, "basic.xml", "root/service"))){
					//判断是否开通图文问诊服务
					DoctorServiceInfo docser=commonService.queryDoctorServiceInfoByOrderType(6,docid);
					if(docser == null){
						//默认开通（主任医师 价格为30 其余20）
						systemAdminManager.saveDocorServiceInfo(detail);
					}
				}
				msg="您好！您的医生端账户（{0}）已审核通过，赶快登陆佰医汇去体验吧。【佰医汇】";
			}else{
				//msg="您好！您的医生端账户（{0}）审核未通过，请登陆佰医汇修改或完善您的验证信息。【佰医汇】";
				msg = "很抱歉，因为您注册信息有误或上传图像不清晰，导致审核未通过，请及时登录账号修改或完善。【佰医汇】";
			}
			reg.setRefusalReason(refusalReason);
			reg.setStatus(auditvalue);
			commonService.updateDoctorRegisterInfo(reg);
			// app消息推送
			if (auditvalue.equals(1) || auditvalue.equals(-3)) {

				Integer pushCode = (auditvalue == 1) ? 2 : 3;
				String content = (auditvalue == 1) ? "您的账户已审核通过." : "您的账户审核未通过.";

				commonManager.generateSystemPushInfo(pushCode, null, null, docid,
						3,null, content);
			}
			if(StringUtils.isNotBlank(reg.getMobileNumber())){
				String tel=reg.getMobileNumber();
				String _tel=tel.substring(0, 3)+"***"+tel.substring(tel.length()-3);
				HttpSendSmsUtil.sendSmsInteface(reg.getMobileNumber(), MessageFormat.format(msg, _tel));
			}
			//判断是否二次审核
			DoctorBillInfo bills=commonService.querydoctorbill(docid);
			Boolean flag=bills==null;
			if(flag){
				//判断是否发审核奖励金
				if("true".equals(ReaderConfigUtil.gainConfigVal(request, "basic.xml", "root/bountyStatus"))){
					// 被邀请者发钱
					if(reg.getStatus().equals(1)){
		                if(StringUtils.isNotBlank(ReaderConfigUtil.gainConfigVal(request, "basic.xml", "root/bountyMoney"))){
		                    BigDecimal originalMoney = new BigDecimal(ReaderConfigUtil.gainConfigVal(request, "basic.xml", "root/bountyMoney"));//账单金额
		                    BigDecimal actualMoney = originalMoney;//实际收入金额
		                    BigDecimal preAccount=(BigDecimal) (reg.getBalance() == null ? new BigDecimal(0.00):reg.getBalance());//收支前余额 
		                    BigDecimal curAccount;//当前账户余额 
		                    curAccount = preAccount.add(actualMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
		                    systemAdminManager.saveDoctorBillInfo(reg,actualMoney,originalMoney,curAccount,preAccount);
		                    reg.setBalance(preAccount.add(originalMoney));
		                    commonService.updateDoctorRegisterInfo(reg);
		                    //app发奖励金推送
		                    if(reg.getStatus().equals(1)){
		                        String content = "您已经注册成功，平台赠送您"+originalMoney+"元作为奖励金，请在[我的收入]中查看。";
		                        commonManager.generateSystemPushInfo(211, null, null, docid,
		                                3,null, content);    
		                    }
		                    //短信发送
		                    if(StringUtils.isNotBlank(reg.getMobileNumber())){
		                        String msgs="您好！您已经注册成功，平台赠送您"+originalMoney+"元作为奖励金，请登陆佰医汇医生版点击【我的】，在【我的收入】中查看。【佰医汇】";
		                        HttpSendSmsUtil.sendSmsInteface(reg.getMobileNumber(),msgs);
		                    }

		                }
		            }
					//判断邀请码是否为真
					DoctorRegisterInfo invitCode = commonService.queryDoctorRegisterInfoById(docid);
					if(invitCode.getInvitationCode()!=null){
						OperatorInvitCode  invitDoc=commonService.queryDocIdByinvitCode(invitCode.getInvitationCode());
						if(invitDoc.getDoctorId()!=null){
							// 邀请人发钱
							DoctorRegisterInfo invotdocid=commonService.queryDoctorRegisterInfoById(invitDoc.getDoctorId());
							if(StringUtils.isNotBlank(ReaderConfigUtil.gainConfigVal(request, "basic.xml", "root/bountyMoney"))){
			                    BigDecimal originalMoney = new BigDecimal(ReaderConfigUtil.gainConfigVal(request, "basic.xml", "root/bountyMoney"));//账单金额
			                    BigDecimal actualMoney = originalMoney;//实际收入金额
			                    BigDecimal preAccount=(BigDecimal) (invotdocid.getBalance() == null ? new BigDecimal(0.00):invotdocid.getBalance());//收支前余额 
			                    BigDecimal curAccount;//当前账户余额 
			                    curAccount = preAccount.add(actualMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
			                    systemAdminManager.saveDoctorBillInfo(invotdocid,actualMoney,originalMoney,curAccount,preAccount);
			                    invotdocid.setBalance(preAccount.add(originalMoney));
			                    commonService.updateDoctorRegisterInfo(invotdocid);
			                    //app发奖励金推送
			                    if(reg.getStatus().equals(1)){
			                        String content = "您邀请的好友已注册成功，平台赠送您"+originalMoney+"元作为奖励金，请在[我的收入]中查看。";
			                        commonManager.generateSystemPushInfo(211, null, null, invotdocid.getId(),
			                                3,null, content);    
			                    }
			                    //短信发送
			                    if(StringUtils.isNotBlank(invotdocid.getMobileNumber())){
			                        String msgs="您好！您邀请的好友已注册成功，平台赠送您"+originalMoney+"元作为奖励金，请登陆佰医汇医生版点击【我的】，在【我的收入】中查看。【佰医汇】";
			                        HttpSendSmsUtil.sendSmsInteface(invotdocid.getMobileNumber(),msgs);
			                    }
			                }
						}
					}
				}
				
			}
		}
		return new HashMap<String, Object>();
	}
	
	/**
	 * 进入专家咨询订单管理
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/exadvices")
	public ModelAndView exadvices(HttpServletRequest request) {

		return new ModelAndView("admin/expert_advices");
	}

	/**
	 * 获取专家咨询订单数据
	 * 
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gainexadvices", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainexadvices(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.gainexadvices(paramMap);
	}

	/**
	 * 企业后台显示专家咨询订单详情
	 * 
	 * @param request
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "/showadvicedetail/{oid}")
	public ModelAndView showadvicedetail(HttpServletRequest request,
			@PathVariable Integer oid) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		SpecialAdviceOrder order = commonService
				.querySpecialAdviceOrderById(oid);
		CaseInfo caseinfo = commonService.queryCaseInfoById(order.getCaseId());
		map.put("order", order);
		map.put("caseinfo", caseinfo);
		//病例图片
		map.put("caseimages", getAttahments(caseinfo));
		// 聊天消息
		List<BusinessMessageBean> msgs = wenzhenService
				.queryBusinessMessageBeansByCon(oid,order.getUuid(), 5);
		for (BusinessMessageBean _msg : msgs) {
			_msg.setMsgTime(_sdf.format(_msg.getSendTime()));
		}
		map.put("msgs", msgs);
		if (order.getExpertId() != null) {
			MobileSpecial sp = commonService
					.queryMobileSpecialByUserIdAndUserType(order.getExpertId());
			map.put("expert", sp);
		}
		List<BusinessPayInfo> payinfos = wenzhenService
				.queryBusinesPayInfosByOId(oid, 5);
		map.putAll(payinfo(payinfos));
		//判断是否需要退款
		if((order.getStatus().equals(30)||order.getStatus().equals(50))&&order.getPayStatus()!=null&&order.getPayStatus().equals(1)){
			BigDecimal money=(BigDecimal)map.get("money");
			if(money!=null&&money.compareTo(BigDecimal.ZERO)>0){
				Object obj=map.get("refundStatus");
				if(obj!=null&&Integer.valueOf(obj.toString()).equals(1)){
					//退款成功，不需要显示退款
				}else{
					//待退款或退款失败
					map.put("needRefund", "true");
				}
			}
		}	
		return new ModelAndView("admin/advice_detail", map);
	}

	/**
	 * 专家咨询订单删除
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delzjzx")
	@ResponseBody
	public Map<String, Object> delzjzx(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		SpecialAdviceOrder order = commonService
				.querySpecialAdviceOrderById(oid);
		order.setDelFlag(1);// 删除
		commonService.updateSpecialAdviceOrder(order);
		return map;
	}

	private Map<String, Object> payinfo(List<BusinessPayInfo> payinfos)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String outTradeNo = "";
		BigDecimal money = null;
		String transactionId = "";
		Integer refundStatus=null;
		String refundTime="";
		String refundResult="";
		if (payinfos != null && payinfos.size() > 0) {
			BusinessPayInfo payInfo=payinfos.get(payinfos.size() - 1);
			outTradeNo = payInfo.getOutTradeNo();
			money = payInfo.getTotalMoney();
			if (isXML(payInfo.getOutTradeResult())) {
				transactionId = gainTransactionId(payInfo.getOutTradeResult());
			} else {
				transactionId = payInfo.getOutTradeResult();
			}
			refundStatus=payInfo.getRefundStatus();
			refundTime=payInfo.getRefundTime()!=null?_sdf.format(payInfo.getRefundTime()):"";
			refundResult=payInfo.getOutRefundResult();
			if(StringUtils.isNotBlank(refundResult)){
				refundResult=gainResultFromMsg(refundResult);
			}
		}
		map.put("transactionId", transactionId);
		map.put("outTradeNo", outTradeNo);
		map.put("money", money);
		map.put("refundStatus", refundStatus);
		map.put("refundTime", refundTime);
		map.put("refundResult", refundResult);
		return map;
	}
	
	private String gainResultFromMsg(String result) throws Exception {
		String return_msg = "";
		if (StringUtils.isNotBlank(result)) {
			if(isXML(result)){
				Document doc = DocumentHelper.parseText(result);
				Element rootElt = doc.getRootElement(); // 获取根节点
				return_msg = rootElt.elementText("return_msg");
			}else {
				return_msg=result;
			}
			
		}
		return return_msg;
	}

	/**
	 * 进入当地医生管理界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/localdocs")
	public ModelAndView localdocs(HttpServletRequest request) {
		return new ModelAndView("admin/local_docs");
	}
	/**
	 * 进入医生订单统计管理界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/docsorderlist")
	public ModelAndView docsorderlist(HttpServletRequest request) {
		return new ModelAndView("admin/docs_about");
	}
	
	/**
	 * 获取医生订单 关注的数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/aboutdocsdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object aboutdocsdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.aboutdocsdatas(paramMap);	
	}
	// 获取当地医生
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gainlocaldosc", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainlocaldosc(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = weixinService.querylocaldocs(searchContent, start, length,ostatus);
		Integer renum = (Integer) retmap.get("num");
		List<MobileSpecial> rcs = (List<MobileSpecial>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		MobileSpecial rc = null;
		for (int i = 0; i < rcs.size(); i++) {
			rc = rcs.get(i);
			stringJson.append("[");
			stringJson.append("\"" + rc.getSpecialId() + "\",");
			stringJson.append("\"" + rc.getStatus() + "\",");
			stringJson.append("\"" + rc.getSpecialName() + "\",");
			stringJson.append("\"" + rc.getTelphone() + "\",");	
			stringJson.append("\"" + rc.getHosName() + "\",");
			stringJson.append("\"" + rc.getDepName() + "\",");
			stringJson.append("\"" + rc.getDuty()+ "\",");
			stringJson.append("\"" + (rc.getAreaOptimal()!=null?(rc.getAreaOptimal().equals(1)?"是":"否"):"否")+ "\",");
			stringJson.append("\"" + _sdf.format(rc.getRegisterTime()) + "\",");
			stringJson.append("\"" + ((rc.getIsTest()!=null &&rc.getIsTest().equals(1))?"是":"否")+ "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		log.info("====json==" + stringJson.toString());
		return stringJson.toString();
	}

	@RequestMapping(value="/testFlag")
	@ResponseBody
	public Map<String,Object> testFlag(HttpServletRequest request) {
		Integer doctorId = Integer.parseInt(request.getParameter("did"));
		DoctorRegisterInfo reg=commonService.queryDoctorRegisterInfoById(doctorId);
		reg.setIsTest(1);
		commonService.updateDoctorRegisterInfo(reg);
		return null;
	}
	@RequestMapping(value="/doctorwithdraws")
	public ModelAndView doctorwithdraws(HttpServletRequest request){
		return new ModelAndView("admin/doctor_withdraws");
	}
	
	/**
	 * 进入医生编辑界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/docEdit")
	public ModelAndView docEdit(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sdid = request.getParameter("did");
		if (StringUtils.isNotBlank(sdid)) {
			Integer did = Integer.parseInt(sdid);
			MobileSpecial special = commonService
					.queryMobileSpecialByUserIdAndUserType(did);
			if(special.getHosId()!=null && !StringUtils.isNotBlank(special.getDistCode())) {
				HospitalDetailInfo hos = weixinService.queryHospitalDetailInfoById(special.getHosId());
				special.setDistCode(hos.getDistCode());
			}
			openserverdoc(special);
			String relatedPics = special.getRelatedPics();
			List<CustomFileStorage> images = null;
			if (StringUtils.isNotBlank(relatedPics)) {
				images = weixinService
						.queryCustomFileStorageImages(relatedPics);
			}
			map.put("images", images);
			map.put("doc", special);
		}
		return new ModelAndView("admin/doc_edit",map);
	}
	
	//医生开通服务
	private void openserverdoc(MobileSpecial special) {
		DoctorServiceInfo vedio = commonService
				.queryDoctorServiceInfoByOrderType(4, special.getSpecialId());
		DoctorServiceInfo ask = commonService
				.queryDoctorServiceInfoByOrderType(6, special.getSpecialId());
		DoctorServiceInfo tel = commonService
				.queryDoctorServiceInfoByOrderType(7, special.getSpecialId());
		if (vedio != null) {
			special.setOpenVedio(vedio.getIsOpen() == null ? 0 : vedio
					.getIsOpen());
			special.setVedioAmount(vedio.getAmount());
		} else {
			special.setOpenVedio(null);
			special.setVedioAmount(null);
		}
		if (ask != null) {
			special.setOpenAsk(ask.getIsOpen() == null ? 0 : ask.getIsOpen());
			special.setAskAmount(ask.getAmount());
		} else {
			special.setOpenAsk(null);
			special.setAskAmount(null);
		}
		if (tel != null) {
			special.setOpenTel(tel.getIsOpen() == null ? 0 : tel.getIsOpen());
			special.setTelAmount(tel.getAmount());
		} else {
			special.setOpenTel(null);
			special.setTelAmount(null);
		}
	}
	@RequestMapping(value="/saveOrUpdateDoc") 
	@ResponseBody
	public void saveOrUpdateDoc(HttpServletRequest request) {
		String docid = request.getParameter("docid");
		DoctorRegisterInfo reg = null;
		DoctorDetailInfo detail = null;
		if (StringUtils.isNotBlank(docid)) {
			// 编辑专家
			reg = commonService.queryDoctorRegisterInfoById(Integer
					.parseInt(docid));
			reg.setLoginName(request.getParameter("telphone"));
			reg.setMobileNumber(request.getParameter("telphone"));
			commonService.updateDoctorRegisterInfo(reg);
			detail = commonService.queryDoctorDetailInfoById(Integer
					.parseInt(docid));
			docdata(detail, request);
			commonService.updateDoctorDetailInfo(detail);
		} 
	}
	private void docdata(DoctorDetailInfo doc, HttpServletRequest request) {
		doc.setRefreshTime(new Timestamp(new Date().getTime()));
		doc.setDisplayName(request.getParameter("displayName"));
		doc.setSex(Integer.valueOf(request.getParameter("sex")));
		doc.setHeadImageUrl(request.getParameter("headImageUrl"));
		doc.setDistCode(request.getParameter("distcode"));
		doc.setDuty(request.getParameter("duty"));
		doc.setPosition(request.getParameter("position"));
		doc.setIdCardNo(request.getParameter("idCardNo"));
		doc.setHospitalId(Integer.parseInt(request.getParameter("hosid")));
		doc.setDepId(Integer.parseInt(request.getParameter("depid")));
		if (StringUtils.isNotBlank(request.getParameter("recommond"))) {
			doc.setRecommend(Integer.parseInt(request.getParameter("recommond")));
		} else {
			doc.setRecommend(null);
		}
		//processopenserver(request, doc.getId());  企业后台暂不管理服务开通与否
		processopenserverdoc(request, doc.getId());
		doc.setSpeciality(request.getParameter("speciality"));
		doc.setProfile(request.getParameter("profile"));
		doc.setProfession(request.getParameter("profession"));
		doc.setRelatedPics(request.getParameter("relatedPics"));
		dutyIdSet(doc);
	}
	private void processopenserverdoc(HttpServletRequest request, Integer docid) {
		DoctorServiceInfo vedio = commonService
				.queryDoctorServiceInfoByOrderType(4, docid);
		DoctorServiceInfo ask = commonService
				.queryDoctorServiceInfoByOrderType(6, docid);
		DoctorServiceInfo tel = commonService
				.queryDoctorServiceInfoByOrderType(7, docid);
		String openVedio = request.getParameter("openVedio");
		String openAsk = request.getParameter("openAsk");
		String openTel = request.getParameter("openTel");
		BigDecimal vedioAmount = null;
		BigDecimal askAmount = null;
		BigDecimal telAmount = null;
		if (StringUtils.isNotBlank(request.getParameter("vedioAmount"))) {
			vedioAmount = new BigDecimal(request.getParameter("vedioAmount"));
		}
		if (StringUtils.isNotBlank(request.getParameter("askAmount"))) {
			askAmount = new BigDecimal(request.getParameter("askAmount"));
		}
		if (StringUtils.isNotBlank(request.getParameter("telAmount"))) {
			telAmount = new BigDecimal(request.getParameter("telAmount"));
		}
		if (StringUtils.isNotBlank(openVedio)
				&& openVedio.equalsIgnoreCase("1")) {
			if (vedio != null) {
				vedio.setIsOpen(1);
				vedio.setAmount(vedioAmount);
				vedio.setShowPrice(vedioAmount);
				commonService.updateDoctorServiceInfo(vedio);
			} else {
				SystemServiceInfo ssi = commonService.querySystemServiceById(4);
				vedio = new DoctorServiceInfo();
				vedio.setDoctorId(docid);
				vedio.setServiceId(4);
				vedio.setPackageId(3);
				vedio.setIsOpen(1);
				vedio.setAmount(vedioAmount);
				vedio.setShowPrice(commonManager.processD2DVedioMoney(vedioAmount, BigDecimal.valueOf(Double.valueOf(ssi.getPriceParameter()))));
				commonService.saveDoctorServiceInfo(vedio);
			}
		} else {
			if (vedio != null) {
				vedio.setIsOpen(0);
				vedio.setAmount(vedioAmount);
				vedio.setShowPrice(vedioAmount);
				commonService.updateDoctorServiceInfo(vedio);
			}
		}
		
		if (StringUtils.isNotBlank(openAsk) && openAsk.equalsIgnoreCase("1")) {
			if (ask != null) {
				ask.setIsOpen(1);
				ask.setAmount(askAmount);
				ask.setShowPrice(askAmount);
				commonService.updateDoctorServiceInfo(ask);
			} else {
				ask = new DoctorServiceInfo();
				ask.setDoctorId(docid);
				ask.setServiceId(6);
				ask.setPackageId(6);
				ask.setIsOpen(1);
				ask.setAmount(askAmount);
				ask.setShowPrice(askAmount);
				commonService.saveDoctorServiceInfo(ask);
			}
		} else {
			if (ask != null) {
				ask.setIsOpen(0);
				ask.setAmount(askAmount);
				ask.setAmount(askAmount);
				commonService.updateDoctorServiceInfo(ask);
			}
		}
		
		if (StringUtils.isNotBlank(openTel) && openTel.equalsIgnoreCase("1")) {
			if (tel != null) {
				tel.setIsOpen(1);
				tel.setAmount(telAmount);
				tel.setShowPrice(telAmount);
				commonService.updateDoctorServiceInfo(tel);
			} else {
				tel = new DoctorServiceInfo();
				tel.setDoctorId(docid);
				tel.setServiceId(7);
				tel.setPackageId(7);
				tel.setIsOpen(1);
				tel.setAmount(telAmount);
				tel.setShowPrice(telAmount);
				commonService.saveDoctorServiceInfo(tel);
			}
		} else {
			if (tel != null) {
				tel.setIsOpen(0);
				tel.setAmount(telAmount);
				tel.setShowPrice(telAmount);
				commonService.updateDoctorServiceInfo(tel);
			}
		}
		
	}
	/**
	 *获取医生详情
	 *@param params
	 * @param request
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="/doctorDtailList")
	public ModelAndView getDoctorDetailList(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer docId =Integer.parseInt(request.getParameter("docId"));
		MobileSpecial doctors=commonService.getDoctorDetailList(docId);
		map.put("doctors", doctors);
		return new ModelAndView("admin/doctor_detail",map);
	}
	
	/**
	 * 获取提现申请数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaindoctorwithdrawdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindoctorwithdrawdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return commonManager.querydoctorwithdrawdatas(paramMap);	
	}
	
	/**
	 * 进入提现详情
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/docwithdrawdetail/{id}")
	public ModelAndView docwithdrawdetail(HttpServletRequest request,@PathVariable Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorWithdrawInfo info=commonService.queryDoctorWithdrawInfoById(id);
		map.put("info", info);
		return new ModelAndView("admin/doctor_withdraw_detail",map);
	}

	/**
	 * 审核提现
	 * id：提现id
	 * sval:审核状态
	 * auditor:审核人
	 * outTradeNo:审核人打款的交易单号
	 * 访问：http://localhost:8080/system/auditdraw
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/auditdraw")
	public @ResponseBody Map<String,Object> auditdraw(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer id=Integer.parseInt(request.getParameter("id"));
		DoctorWithdrawInfo info=commonService.queryDoctorWithdrawInfoByIdAll(id);
		//判断是否是二次申请提现
		if(info.getStatus().equals(1)){
		DoctorRegisterInfo doc=commonService.queryDoctorRegisterInfoById(info.getDoctorId());
		Integer sval=Integer.parseInt(request.getParameter("sval"));//2--审核通过  3--审核未通过
		String auditor=request.getParameter("auditor");//审核人
		String outTradeNo=request.getParameter("outTradeNo");//交易单号  
		info.setAuditor(auditor);
		info.setStatus(sval);
		info.setOutTradeNo(outTradeNo);
		info.setAuditTime(new Timestamp(System.currentTimeMillis()));
		commonService.updateDoctorWithdrawInfo(info);
        //提现审核通过
        if(info.getStatus().equals(2)){
        //同意生成流水
        	DoctorBillInfo doctorBillInfo = new DoctorBillInfo();
            doctorBillInfo.setUuid(UUIDUtil.getUUID());
            doctorBillInfo.setDoctorId(info.getDoctorId());
            doctorBillInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            doctorBillInfo.setSource("提现");
            doctorBillInfo.setType(2); // 2：支出
            doctorBillInfo.setActualMoney(info.getActualMoney()); // 实际收入金额/实际支出金额(收入时=账单金额+补贴-税费；支出时=到账金额+税费）
            doctorBillInfo.setOriginalMoney(info.getMoney()); //提现金额
            doctorBillInfo.setTaxationMoney(info.getTaxationMoney());
            doctorBillInfo.setCurAccount(doc.getBalance().subtract(info.getMoney())); // 当前余额
            doctorBillInfo.setPreAccount(doc.getBalance()); // 账户余额
            doctorBillInfo.setBusinessId(info.getUUID());
            doctorBillInfo.setBusinessType(-1);// 提现
        //保存流水
        commonService.addDoctorBillInfo(doctorBillInfo);
        // 修改账户金额
        doc.setBalance(doctorBillInfo.getCurAccount());
        commonService.updateDoctorRegisterInfo(doc);
        String content = "您好！您的提现申请已通过审核，提现金额将在1-3工作日内到账，具体金额与到账时间请以实际为准。【佰医汇】";
        HttpSendSmsUtil.sendSmsInteface(doc.getMobileNumber(),content);
        }else if(info.getStatus().equals(3)){
        	//审核不通过短信通知
            String content = "您好！您的提现申请审核未通过，给您带来的不便尽情谅解。【佰医汇】";
            HttpSendSmsUtil.sendSmsInteface(doc.getMobileNumber(),content);
        }
		 	}
		  return map;
	}
	
	/**
	 * 进入医生专家账单界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/doctorbills")
	public ModelAndView doctorbills(HttpServletRequest request){
		return new ModelAndView("admin/doctor_bills");
	}
	
	/**
	 * 获取医生专家账单数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaindocbilldatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindocbilldatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return commonManager.querydocbilldatas(paramMap);	
	}
	
	/**
	 * 进入专家登陆列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/exlogincal")
	public ModelAndView exlogincal(HttpServletRequest request){
		return new ModelAndView("admin/ex_logincal");
	}
	
	/**
	 * 获取专家登陆数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gainexlogindata", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainexlogindata(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return commonManager.queryexlogindata(paramMap);	
	}
	
	/**
	 * 患者-医生 电话订单界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/d2ptels")
	public ModelAndView d2ptels(HttpServletRequest request){
		return new ModelAndView("admin/d2p_tel_orders");
	}
	/**
	 * 获取患者-医生 电话订单数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/d2pteldatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object d2pteldatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return d2pManager.queryd2pteldatas(paramMap);	
	}
	//专家登陆统计导出
		@RequestMapping(value = "/reportlogin", produces = "text/plain;charset=UTF-8")
		@ResponseBody
		public  void getLoginList(HttpServletResponse response,HttpServletRequest request) throws Exception{
			    String docName = request.getParameter("docName"); //按姓名
			    String hosid = request.getParameter("hosid");//按医院
			    String depid = request.getParameter("depid");//按科室查询
			    String lastTimes = request.getParameter("lastTimes");//按月查询的时间
			    String startDate = request.getParameter("startDate");//开始时间
				String endDate = request.getParameter("endDate");//结束时间
				String title = "专家登录统计";
				List<UserDevicesRecord> logins =commonService.getLoginList(docName,hosid,depid,lastTimes,startDate,endDate);
				JSONArray ja = new JSONArray();
				for (UserDevicesRecord log : logins) {
					LoginBean login = new LoginBean();
					login.setLoginName(log.getLoginName());
					login.setDocName(log.getDocName());
					login.setDepName(log.getDepName());
					login.setHosName(log.getHosName());
					login.setLastLoginTime(log.getLastTimes());
					login.setLoginVersion(log.getLoginVersion());
					login.setModel(log.getModel());
					login.setPlatform(log.getPlatform());
					login.setImei(log.getIMEI());
					ja.add(login);
				}
				HashMap<String, String> headMap = new LinkedHashMap<>();
				headMap.put("loginName", "专家账号");
				headMap.put("docName", "专家姓名");
				headMap.put("hosName", "医院名称");
				headMap.put("depName", "科室名称");
				headMap.put("platform", "平台");
				headMap.put("model", "登陆设备");
				headMap.put("lastLoginTime", "登陆时间");
				headMap.put("loginVersion", "登录版本号");
				headMap.put("imei", "设备识别码");
				ExcelUtilToService.downloadExcelFile(title, headMap, ja, response);
			}
	
	/**
	 * 患者-医生 图文咨询订单
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/d2ptuwens")
	public ModelAndView d2ptuwens(HttpServletRequest request){
		return new ModelAndView("admin/d2p_tuwen_orders");
	}
	
	/**
	 * 获取患者-医生 图文咨询订单数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/d2ptuwendatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object d2ptuwendatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return d2pManager.queryd2ptuwendatas(paramMap);	
	}
	
	/**
	 * 患者-医生 快速问诊订单
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/d2pfastasks")
	public ModelAndView d2pfastasks(HttpServletRequest request){
		return new ModelAndView("admin/d2p_fastask_orders");
	}
	/**
	 * 获取患者-医生 快速问诊订单数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/d2pfastaskdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object d2pfastaskdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return d2pManager.queryd2pfastaskdatas(paramMap);	
	}
	
	/**
	 * 进入订单详情页（电话--7，图文--6，快速问诊--9）
	 * @param request
	 * @param oid
	 * @param otype
	 * @return
	 */
	@RequestMapping(value="/d2porderdetail/{oid}/{otype}")
	public ModelAndView d2porderdetail(HttpServletRequest request,@PathVariable Integer oid,@PathVariable Integer otype) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("oid", oid);
		map.put("otype", otype);
		D2pOrderBean order=d2pManager.queryOrderDetailInfo(oid,otype);
		map.put("order", order);
		Integer caseid=order.getCaseId();
		//病例信息
		CaseInfo ca=commonService.queryCaseInfoById(caseid);
		//病例图片
		map.put("caseimages", getAttahments(ca));
		//支付信息
		List<BusinessPayInfo> payinfos = wenzhenService
				.queryBusinesPayInfosByOId(oid, otype);
		map.putAll(payinfo(payinfos));
		//聊天消息
		List<BusinessMessageBean> msgs = wenzhenService
				.queryBusinessMessageBeansByCon(oid,order.getUuid(), otype);
		for (BusinessMessageBean _msg : msgs) {
			_msg.setMsgTime(_sdf.format(_msg.getSendTime()));
		}
		map.put("msgs", msgs);
		Integer docid=order.getDoctorId();
		if(docid!=null){
			MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(docid);
			System.out.println(doc);
			map.put("doc", doc);
		}
		//判断是否需要退款
		if((order.getStatus().equals(30)||order.getStatus().equals(50))&&order.getPayStatus()!=null&&order.getPayStatus().equals(1)){
			BigDecimal money=(BigDecimal)map.get("money");
			if(money!=null&&money.compareTo(BigDecimal.ZERO)>0){
				Object obj=map.get("refundStatus");
				if(obj!=null&&Integer.valueOf(obj.toString()).equals(1)){
					//退款成功，不需要显示退款
				}else{
					//待退款或退款失败
					map.put("needRefund", "true");
				}
			}
		}
		return new ModelAndView("admin/d2p_order_detail",map);
	}
	
	/**
	 * 病例图片
	 * @param ca
	 * @return
	 */
	public List<UserCaseAttachment> getAttahments(CaseInfo ca){
		List<UserCaseAttachment> images = wenzhenService.queryUserAttachmentByCaseUuid(ca.getUuid());
		for (UserCaseAttachment userCaseAttachment : images) {
				if(StringUtils.isNotBlank(userCaseAttachment.getAttachmentIds())){
					List<CustomFileStorage> files=wenzhenService.queryCustomFilesByCaseIds(userCaseAttachment.getAttachmentIds());
					userCaseAttachment.setFiles(files);
					Integer filescount=files!=null?files.size():0;
					userCaseAttachment.setFilescount(filescount);
				}
			}
		ca.setAttachments(images);
		return images;
	}
	
	/**
	 * 修改医生TO患者订单的状态 6-图文  7-电话 9-快速
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/changeOrderStatus")
	@ResponseBody
	public Map<String,Object> changeOrderStatus(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		Integer otype=Integer.parseInt(request.getParameter("otype"));
		String sval=request.getParameter("sval");
		String refuls=request.getParameter("refuls");
		systemAdminManager.changeOrderStatus(oid,otype,sval,refuls);
		return map;
	}
	/**
	 * 患者报道管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/d2preports")
	public ModelAndView d2preports(HttpServletRequest request){
		return new ModelAndView("admin/d2p_report_orders");
	}
	
	/**
	 * 获取患者-医生 患者报道数据 
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/d2preportdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object d2preportdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return d2pManager.queryd2preportdatas(paramMap);	
	}
	/**
	 * 患者报道详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/reportdetail/{id}")
	public ModelAndView reportdetail(@PathVariable Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		map.putAll(systemAdminManager.reportdetail(id));
		return new ModelAndView("/admin/d2p_report_detail",map);
	}

	/**
	 * 患者会诊需求申请单管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/d2pconreqs")
	public ModelAndView d2pconreqs(HttpServletRequest request){
		return new ModelAndView("admin/d2p_conreq_orders");
	}
	
	/**
	 * 获取患者-医生 患者会诊需求申请单数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/d2pconreqdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object d2pconreqdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return d2pManager.queryd2pconreqdatas(paramMap);	
	}
	/**
	 * 会诊申请订单详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/conreqdetail/{id}")
	public ModelAndView conreqdetail(@PathVariable Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		map.putAll(systemAdminManager.conreqdetail(id));
		return new ModelAndView("admin/d2p_conreq_detail",map);
	}
	
	
	/**
	 * 医生--医生   转诊订单
	 * @return
	 */
	@RequestMapping(value="/d2dreferrals")
	public ModelAndView d2dreferrals(){
		return new ModelAndView("admin/d2p_refer_orders");
	}

	/**
	 * 转诊订单数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/d2preferdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object d2preferdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return d2pManager.queryd2preferdatas(paramMap);	
	}
	/**
	 * 转诊订单详情
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/d2dreferdetail/{id}")
	public ModelAndView d2dreferdetail(HttpServletRequest request,@PathVariable Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		map.putAll(systemAdminManager.d2dreferdetail(id));
		return new ModelAndView("admin/d2p_refer_detail",map);
	}
	
	/**
	 * 医生或专家上下线 sval:1上线  sval:0下线
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/onoroffdoc")
	@ResponseBody
	public Map<String,Object> onoroffdoc(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer did=Integer.parseInt(request.getParameter("did"));
		Integer sval=Integer.parseInt(request.getParameter("sval"));
		DoctorRegisterInfo reg=commonService.queryDoctorRegisterInfoById(did);
		reg.setStatus(sval);
		commonService.updateDoctorRegisterInfo(reg);
		String contents="";
		String msgs="";
		if(sval.equals(0)){
			//清空token
			reg.setToken(null);
			reg.setWebToken(null);
			reg.setRongCloudToken(null);
			commonService.updateDoctorRegisterInfo(reg);
			//app推送下线
			 contents = "您的账户已下线。如有疑问，请拨打客服电话：400-6319377";
	         commonManager.generateSystemPushInfo(4, null, null, reg.getId(),
	            		reg.getUserType(),null, contents); 
	         //短信发送
	         msgs="您的账户已下线。如有疑问，请拨打客服电话：400-6319377   【佰医汇】";
	         HttpSendSmsUtil.sendSmsInteface(reg.getMobileNumber(),msgs);
		}else if(sval.equals(1)){
			//app推送上线
			msgs = "您的账户已上线，欢迎您继续使用。给您造成不便，我们深感歉意。【佰医汇】";
			HttpSendSmsUtil.sendSmsInteface(reg.getMobileNumber(),msgs);
		}
		return map;
	}
	
	
	/**
	 * 医生推优及取消推优设置
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/optimalchange")
	@ResponseBody
	public Map<String,Object> optimalchange(HttpServletRequest request){
		Integer did=Integer.parseInt(request.getParameter("did"));
		Integer sval=Integer.parseInt(request.getParameter("sval"));
		DoctorDetailInfo doc=commonService.queryDoctorDetailInfoById(did);
		doc.setAreaOptimal(sval);
		commonService.updateDoctorDetailInfo(doc);
		return null;
	}
	
	/**
	 * 进入医生团队审核
	 * @return
	 */
	@RequestMapping(value="/doctorteamaudit")
	public ModelAndView doctorteam(){
		return new ModelAndView("admin/doctor_team_audit");
	}
	/**
	 * 获取医生团队审核数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */ 
	@RequestMapping(value = "/auditdteams", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object auditdteams(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return d2pManager.queryauditdocteamdatas(paramMap);	
	}
	
	@RequestMapping(value="/docteamdetail/{id}")
	public ModelAndView docteamdetail(HttpServletRequest request,@PathVariable Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorTeam dt=commonService.queryDoctorTeamById_detail(id);
		map.put("dt", dt);
		return new ModelAndView("admin/doctor_team_detail",map);
	}
	
	
	@RequestMapping(value="/teamTestFlag")
	@ResponseBody
	public Map<String,Object> teamTestFlag(HttpServletRequest request) {
		Integer teamId = Integer.parseInt(request.getParameter("teamId"));
		DoctorTeam dt = commonService.queryDoctorTeamById(teamId);
		dt.setIsTest(1);
		commonService.updateDoctorTeam(dt);
		return null;
	}
	/**
	 * 更改医生团队的状态
	 * @param request
	 */
	@RequestMapping(value="/changedtstatus")
	@ResponseBody
	public void changedtstatus(HttpServletRequest request,HttpSession session) throws Exception{
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Integer tid=Integer.parseInt(request.getParameter("tid"));
		Integer sval=Integer.parseInt(request.getParameter("sval"));
		String refusalReason = request.getParameter("refusalReason");
		DoctorTeam dt=commonService.queryDoctorTeamById(tid);
		dt.setAuditorId(user.getId());
		dt.setAuditorType(user.getUserType());
		dt.setAuditTime(new Timestamp(System.currentTimeMillis()));
		dt.setStatus(sval);
		if(dt.getStatus().equals(-1)) {
			//审核未通过
			dt.setRefusalReason(refusalReason);
			commonService.updateDoctorTeam(dt);
			String content="您申请的团队未通过审核";
			commonManager.generateSystemPushInfo(402 , null, null, dt.getApplicantId(),
					dt.getApplicantType(),null, content);
		}else {
			systemAdminManager.processDoctorTeamAudit(dt);	
			String url=PropertiesUtil.getString("DOMAINURL")+"module/patient.html#/teaminfo/" + dt.getId();//进入主界面
			String erweimaUrl = OSSManageUtil.genErweima(
					ChineseToPinyinUtil.getPingYin(dt.getTeamName()),
					url);
			dt.setErweimaUrl(erweimaUrl);
			commonService.updateDoctorTeam(dt);
			String content="您申请的团队审核通过";
			commonManager.generateSystemPushInfo(402 , null, null, dt.getApplicantId(),
					dt.getApplicantType(),null, content);
		//团队通过审核给组长（申请人）发奖励金
			//1、动态控制发不发
			if("true".equals(ReaderConfigUtil.gainConfigVal(request, "basic.xml", "root/teambountyStatus"))){
				if(dt.getStatus().equals(1)){
					//2、查找负责人 发送奖励金（钱动态）
					DoctorRegisterInfo reg = commonService.queryDoctorRegisterInfoById(dt.getApplicantId());
					if(StringUtils.isNotBlank(ReaderConfigUtil.gainConfigVal(request, "basic.xml", "root/teambountyMoney"))){
	                    BigDecimal originalMoney = new BigDecimal(ReaderConfigUtil.gainConfigVal(request, "basic.xml", "root/teambountyMoney"));//账单金额
	                    BigDecimal actualMoney = originalMoney;//实际收入金额
	                    BigDecimal preAccount=(BigDecimal) (reg.getBalance() == null ? new BigDecimal(0.00):reg.getBalance());//收支前余额 
	                    BigDecimal curAccount;//当前账户余额 
	                    curAccount = preAccount.add(actualMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
	                    systemAdminManager.saveDoctorBillInfo(reg,actualMoney,originalMoney,curAccount,preAccount);
	                    reg.setBalance(preAccount.add(originalMoney));
	                    commonService.updateDoctorRegisterInfo(reg);
	                  //3、推送消息通知
	                    //app推送
	                    if(reg.getStatus().equals(1)){
	                        String contents = "恭喜，您申请组建的医生团队通过审核。佰医汇平台赠送您"+originalMoney+"元现金红包，请在[我的收入]中查看。";
	                        commonManager.generateSystemPushInfo(211, null, null, reg.getId(),
	                        		dt.getApplicantType(),null, contents);    
	                    }
	                    //短信发送
	                    if(StringUtils.isNotBlank(reg.getMobileNumber())){
	                        String msgs="恭喜，您申请组建的医生团队通过审核。佰医汇平台赠送您"+originalMoney+"元现金红包，赶快登录查收吧。【佰医汇】";
	                        HttpSendSmsUtil.sendSmsInteface(reg.getMobileNumber(),msgs);
	                    }
	                }
				}
			}
		}
	}
	
	@RequestMapping(value="/doctorteams")
	public ModelAndView doctorteams(){
		return new ModelAndView("admin/doctor_teams");
	}
	/**
	 * 获取医生团队数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/docteamdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object docteamdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return d2pManager.querydocteamdatas(paramMap);	
	}
	/**
	 * 团队推优及取消
	 * @param request
	 */
	@RequestMapping(value="/changedtopstatus")
	@ResponseBody
	public void changedtopstatus(HttpServletRequest request){
		Integer tid=Integer.parseInt(request.getParameter("tid"));
		Integer sval=Integer.parseInt(request.getParameter("sval"));
		DoctorTeam dt=commonService.queryDoctorTeamById(tid);
		dt.setAreaOptimal(sval);
		commonService.updateDoctorTeam(dt);
	}
	
	/**
	 * 团队问诊订单管理
	 * @return
	 */
	@RequestMapping(value="/t2ptuwens")
	public ModelAndView t2ptuwens(){
		return new ModelAndView("admin/t2p_tuwens");
	}
	
	/**
	 * 获取团队问诊数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/t2ptuwendatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object t2ptuwendatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return d2pManager.queryt2ptuwendatas(paramMap);	
	}
	/**
	 * 团队问诊 -详情
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/t2ptuwendetail/{id}")
	public ModelAndView t2ptuwendetail(HttpServletRequest request,@PathVariable Integer id) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		map.putAll(systemAdminManager.t2ptuwendetail(id));
		return new ModelAndView("admin/t2p_tuwen_detail",map);
	}
	/**
	 * 医联体审核界面
	 * @return
	 */
	@RequestMapping(value="/hoshealthaudit"	)
	public ModelAndView hoshealthaudit(){
		return new ModelAndView("admin/hos_health_audit");
	}
	/**
	 * 获取医联体审核数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hoshealthauditdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object hoshealthauditdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.queryhoshealthauditdatas(paramMap);	
	}
	@RequestMapping(value="/audithhadetail/{id}")
	public ModelAndView audithhadetail(@PathVariable Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		map.putAll(systemAdminManager.audithhadetail(id));
		return new ModelAndView("admin/hos_health_audit_detail",map);
	}
	/**
	 * 修改医联体状态
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/changeHosHealthStatus")
	@ResponseBody
	public Map<String,Object> changeHosHealthStatus(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Integer hospitalHealthId=Integer.parseInt(request.getParameter("hid"));
		Integer sval=Integer.parseInt(request.getParameter("sval"));
		systemAdminManager.changeHosHealthStatus(hospitalHealthId,sval,user.getId(),user.getUserType());
		return map;
	}
	/**
	 * 医联体管理
	 * @return
	 */
	@RequestMapping(value="/hoshealthmanage")
	public ModelAndView hoshealthmanage(){
		return new ModelAndView("admin/hos_health_manage");
	}
	/**
	 * 获取医联体数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hoshealthdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object hoshealthdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.queryhoshealthdatas(paramMap);	
	}
	/**
	 * 进入新增医联体界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addhoshealth")
	public ModelAndView addhoshealth(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String yltId=request.getParameter("yltId");
		System.out.println("===="+yltId);
		if(StringUtils.isNotBlank(yltId)){
			HospitalHealthAlliance ylt=commonService.queryHospitalHealthAllianceById_new(Integer.parseInt(yltId));
			System.out.println("=="+JSONObject.fromObject(ylt).toString());
			map.put("ylt", ylt);
		}
		return new ModelAndView("admin/add_hoshealth",map);
	}
	/**
	 * 保存医联体
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/savehoshealth")
	@ResponseBody
	public Map<String,Object> savehoshealth(HttpServletRequest request,HttpSession session){
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Map<String,Object> map=new HashMap<String,Object>();
		systemAdminManager.savehoshealth(request,user);
		return map;
	}
	/**
	 * 获取医联体结构 树形表格数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hoshealthstruts_new", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object hoshealthstruts_new(HttpServletRequest request) throws Exception {
		Integer yltId=Integer.parseInt(request.getParameter("yltId"));
		return systemAdminManager.hoshealthstruts(yltId);
	}
	/**
	 * 进入医联体详情
	 * @param hid
	 * @return
	 */
	@RequestMapping(value="/hoshealthdetail/{hid}")
	public ModelAndView hoshealthdetail(@PathVariable Integer hid){
		Map<String,Object> map=new HashMap<String,Object>();
		HospitalHealthAlliance hha=commonService.queryHospitalHealthAllianceById(hid);
		map.put("hha", hha);
		return new ModelAndView("admin/hos_health_detail",map);
	}
	/**
	 * 获取可选择的医院
	 * @param homeUrl
	 * @param queryString
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/gainhospitalsbycon")
	@ResponseBody
	public Map<String,Object> gainhospitalsbycon(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String hospitalId=request.getParameter("hosId");
		String status="";
		List<HospitalDetailInfo> hospitals=null;
		String aids="";
		if(StringUtils.isNotBlank(hospitalId)){
			//一级或二级医院
			HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(Integer.parseInt(hospitalId));
			Integer level=hos.getHospitalLevel();
			switch(level){
			case 10:
				status="11,12,13,14,15,16,17,18";
				break;
			case 11:
			case 12:
				status="13,14,15,16,17,18";
				break;
			case 13:
			case 14:
			case 15:
				status="16,17,18";
				break;
			case 16:
			case 17:
			case 18:
				break;
			}
		}else{
			//三级医院
			status="10";
			List<HospitalHealthAlliance> alliances=commonService.queryHospitalHealthAlliances_audited();
			aids=gainHealthAllianceIds(alliances);
		}
		if(StringUtils.isNotBlank(status)){
			hospitals=commonService.queryHospitalsByCon(status,aids);
		}
		map.put("hospitals", hospitals);
		return map;
	}
	private String gainHealthAllianceIds(List<HospitalHealthAlliance> alliances){
		StringBuilder sb=new StringBuilder();
		if(alliances!=null&&alliances.size()>0){
			for (HospitalHealthAlliance hha : alliances) {
				sb.append(hha.getHospitalId()+",");
			}
			sb=sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	/**
	 * 医联体新增医院保存
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveHosHealthMember")
	@ResponseBody
	public Map<String,Object> saveHosHealthMember(HttpServletRequest request){
		Integer allianceId=Integer.parseInt(request.getParameter("allianceId"));
		String parentHosId=request.getParameter("parentHosId");
		Integer hospitalId=Integer.parseInt(request.getParameter("hospitalId"));
		return systemAdminManager.saveHosHealthMember(allianceId,parentHosId,hospitalId);
	}
	/**
	 * 删除医联体医院
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/delHosHealthMember")
	@ResponseBody
	public Map<String,Object> delHosHealthMember(HttpServletRequest request){
		Integer hosId=Integer.parseInt(request.getParameter("hosId"));//医院id
		Integer allianceId=Integer.parseInt(request.getParameter("allianceId"));
		return systemAdminManager.delHosHealthMember(hosId,allianceId);
	}
	/**
	 * 运营人员管理
	 * @return
	 */
	@RequestMapping(value="/operatormanage")
	public ModelAndView operatormanage(){
		return new ModelAndView("admin/operator_manage");
	}
	/**
	 * 获取运营人员数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/operatordatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object operatordatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.queryoperatordatas(paramMap);	
	}
	/**
	 * 运营人员邀请医生的列表 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/invitdoclist")
	public ModelAndView invitdoclist(HttpServletRequest request){
		Map<String, String> map=new HashMap<String, String>();
		String invitCode = request.getParameter("invitCode");
		map.put("invitCode", invitCode);
		return	new ModelAndView("admin/invitdoc_detail",map);
	}
	/**
	 * 运营人员邀请医生的列表数据 
	 * @param userType
	 * @param request
	 * @param invitCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gaininvitDocdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaininvitDocdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.gaininvitDocdatas(paramMap);
	}
	/**
	 * 生成邀请码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/generateOperatorCode")
	@ResponseBody
	public Map<String,Object> generateOperatorCode(HttpServletRequest request){
		Integer docid = Integer.parseInt(request.getParameter("docid"));
		return systemAdminManager.generateOperatorCode(docid);
	}
	
	
	/**
	 *进入操作运营人员界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/operatorOp")
	public ModelAndView operatorOp(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String opId=request.getParameter("opId");
		if(StringUtils.isNotBlank(opId)){
			MobileSpecial mobileSpecial=commonService.queryMobileSpecialByUserIdAndUserType(Integer.parseInt(opId));
			map.put("operator", mobileSpecial);
		}
		return new ModelAndView("admin/operator_op",map);
	}

	/**
	 * 新增或编辑运营人员
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveorupdateOp")
	@ResponseBody
	public Map<String,Object> saveorupdateOp(HttpServletRequest request){
		systemAdminManager.saveorupdateOp(request);
		return null;
	}
	/**
	 * 运营人员医院设置
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/ophosset/{id}")
	public ModelAndView ophosset(HttpServletRequest request,@PathVariable Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("opId", id);
		return new ModelAndView("admin/operator_hosset",map);
	}
	/**
	 * 获取运营人员医院数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/operatorhosdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object operatorhosdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.operatorhosdatas(paramMap);	
	}
	/**
	 * 取消关联医院
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cancelRelativeHos")
	@ResponseBody
	public Map<String,Object> cancelRelativeHos(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer rid=Integer.parseInt(request.getParameter("rid"));//关联id
		commonService.delHospitalMaintainerRelation(rid);
		return map;
	}
	/**
	 * 新增运营人员医院
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/distHospitalToOp")
	@ResponseBody
	public Map<String,Object> distHospitalToOp(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer opId=Integer.parseInt(request.getParameter("opId"));
		Integer hospitalId=Integer.parseInt(request.getParameter("hospitalId"));
		systemAdminManager.distHospitalToOp(opId,hospitalId);
		return map;
	}
	/**
	 * 轮播界面
	 * @return
	 */
	@RequestMapping(value="/figureconfigs")
	public ModelAndView figureconfigs(){	
		return new ModelAndView("admin/figure_configs");
	}
	/**
	 * 根据类型获取轮播数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainfigconfigs")
	@ResponseBody
	public Map<String,Object> gainfigconfigs(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer type=Integer.parseInt(request.getParameter("type"));
		List<ShufflingFigureConfig> cons=commonService.queryShufflingFigureConfigsByType(type);
		map.put("cons", cons);
		return map;
	}
	/**
	 * 新增或编辑
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveorupdatefigcons")
	@ResponseBody
	public Map<String,Object> saveorupdatefigcons(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer figId=systemAdminManager.saveorupdatefigcons(request);
		map.put("figId", figId);
		map.put("status","success");
		return map;
	}
	/**
	 * 删除
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/delfigconfig")
	@ResponseBody
	public Map<String,Object> delfigconfig(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer figId=Integer.parseInt(request.getParameter("figId"));
		systemAdminManager.delfigconfig(figId);
		return map;
	}
	/**
	 * 轮播图排序
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sortfigconfig")
	@ResponseBody
	public Map<String,Object> sortfigconfig(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String ids=request.getParameter("ids");
		systemAdminManager.sortfigconfig(ids);
		map.put("status", "success");
		return map;
	}
	/**
	 * 视频会诊订单管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/vedioordermanage")
	public ModelAndView vedioordermanage(HttpServletRequest request){
		
		return new ModelAndView("admin/vedio_order_manage");
	}
	
	/**
	 * 查看已完成视频会诊备注项
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/vedioorderRemrk")
	public ModelAndView vedioorderRemrk(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer sta = Integer.valueOf(request.getParameter("sta"));
		Integer id = Integer.valueOf(request.getParameter("id"));
		BusinessVedioOrder vodio=weixinService.vedioorderRemrk(sta,id);
		map.put("vodio", vodio);
		return new ModelAndView("admin/vedio_order_remark",map);
	}
	/**
	 * 编辑修改已完成视频会诊备注项
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateVedioorderRemrk",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateVedioorderRemrk(HttpServletRequest request){
		Integer id = Integer.valueOf(request.getParameter("id"));
		String remark = request.getParameter("remark");
	    systemAdminManager.updateVedioorderRemrk(id,remark);
	    return null;
	}
	
	/**
	 * 获取视频会诊数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gainvedioorderdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainvedioorderdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.gainvedioorderdatas(paramMap);
	}
	/**
	 * 导出视频会诊数据
	 * @param params
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/reportvediooder", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public  void getfinshvedioList(HttpServletResponse response,HttpServletRequest request) throws Exception{
		Integer sta = ( request.getParameter("sta") != null && !"".equals(request.getParameter("sta")))?Integer.parseInt(request.getParameter("sta")):null;
		String startDate = request.getParameter("startDate");//订单创建时间
		String endDate = request.getParameter("endDate");
		String hosid = request.getParameter("hosid");
		String depid = request.getParameter("depid");
		String title = "视频会诊数据统计";
		List<BusinessVedioOrder> order =weixinService.getfinshvedioList(sta,startDate,endDate,hosid,depid);
		List<ExcelBean> list = new ArrayList<ExcelBean>();
		for (BusinessVedioOrder bvo : order) {
			ExcelBean excelBean = new ExcelBean();
			excelBean.setConsultationDate(bvo.getConsultationDate());
			excelBean.setPayStatus(bvo.getPayStatus());
			excelBean.setMoney(bvo.getMoney());
			excelBean.setStatus(bvo.getStatus());
			excelBean.setLocalHosName(bvo.getLocalHosName());
			excelBean.setLocalDepName(bvo.getLocalDepName());
			excelBean.setLocalDocName(bvo.getLocalDocName());
			excelBean.setHosName(bvo.getHosName());
			excelBean.setDepName(bvo.getDepName());
			excelBean.setExpertName(bvo.getExpertName());
			excelBean.setPatientName(bvo.getPatientName());
			excelBean.setSex(bvo.getSex());
			excelBean.setAge(bvo.getAge());
			list.add(excelBean);
		}
		ExcelUtil.buildExcel(list, response, title);
	}
    
	/**
	 * 医生-医生 图文咨询订单管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/dtdtuwenordermanage")
	public ModelAndView dtdtuwenordermanage(HttpServletRequest request){
		
		return new ModelAndView("admin/dtd_tuwen_manage");
	}
	/**
	 * 医生-医生  获取图文咨询订单数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gaindtdtuwenorderdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindtdtuwenorderdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.gaindtdtuwenorderdatas(paramMap);	
	}
	
	/**
	 * 导出图文会诊数据
	 * @param params
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/reportuwen", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public  void gettuwenList(HttpServletResponse response,HttpServletRequest request) throws Exception{
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Integer sta = ( request.getParameter("sta") != null && !"".equals(request.getParameter("sta")))?Integer.parseInt(request.getParameter("sta")):null;
		String hosid = request.getParameter("hosid");
		String depid = request.getParameter("depid");
		String title = "图文会诊数据统计";
		List<SpecialAdviceOrder> order =commonService.gettuwenList(sta,hosid,depid,startDate,endDate);
		List<ExcelBean> list = new ArrayList<ExcelBean>();
		for (SpecialAdviceOrder tuwen : order) {
			ExcelBean excelBean = new ExcelBean();
			excelBean.setConsultationDate(tuwen.getConsultationDate());
			excelBean.setPayStatus(tuwen.getPayStatus());
			excelBean.setMoney(tuwen.getMoney());
			excelBean.setStatus(tuwen.getStatus());
			excelBean.setLocalHosName(tuwen.getLocalHosName());
			excelBean.setLocalDepName(tuwen.getLocalDepName());
			excelBean.setLocalDocName(tuwen.getLocalDocName());
			excelBean.setHosName(tuwen.getHosName());
			excelBean.setDepName(tuwen.getDepName());
			excelBean.setExpertName(tuwen.getExpertName());
			excelBean.setPatientName(tuwen.getUserName());
			excelBean.setSex(tuwen.getSex());
			excelBean.setAge(tuwen.getAge());
			list.add(excelBean);
		}
		ExcelUtil.buildExcel(list, response, title);
	}
	/**
	 * 进入医生团队成员信息界面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/doctorTeamMembers/{id}")
	public ModelAndView doctorTeamMembers(HttpServletRequest request,@PathVariable Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("teamId", id);
		return new ModelAndView("admin/doctor_team_members",map);
	}
	/**
	 * 获取医生团队成员数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gainDoctorTeamMemberDatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainDoctorTeamMemberDatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.gainDoctorTeamMemberDatas(paramMap);	
	}
	/**
	 * 同市区同科室群组管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/citygroups")
	public ModelAndView sameAreaAndDepGroupManage(HttpServletRequest request){
		return new ModelAndView("/admin/groups/cityList");
	}
	/**
	 * 医院群组管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/hosgroups")
	public ModelAndView hospitalGroupManage(HttpServletRequest request){
		return new ModelAndView("/admin/groups/hosList");
	}
	/**
	 * 医生团队群组管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/docteams")
	public ModelAndView docteamsGroupManage(HttpServletRequest request){
		return new ModelAndView("/admin/groups/docteamList");
	}
	/**
	 * 踢出医生团队群组
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/removeDocTeamGroupMember")
	@ResponseBody
	public Map<String,Object> removeDocTeamGroupMember(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer memberId=Integer.parseInt(request.getParameter("memberId"));//成员id
		systemAdminManager.removeDocTeamGroupMember(memberId);
		return map;
	}
	/**
	 * 获取群组数据
	 * 访问：http://localhost:8080/system/gainGroupDatas
	 * 参数：sEcho，searchContent，iDisplayStart，iDisplayLength，groupType（群组类型 1:订单;2:Team;3:医院;4:区域科室;5自主创建）
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gainGroupDatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainGroupDatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.gainGroupDatas(paramMap);	
	}
	/**创建群组
	 * 参数：groupName  群组名称
		     distCode   区域
		     depId      标准科室id
		     hospitalId 医院id
		     groupType  群组类型
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/createGroup")
	@ResponseBody
	public Map<String,Object> createGroup(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		systemAdminManager.createGroup(user.getId(),user.getUserType(),request);
		return map;
	}
	/**
	 * 获取医院数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainHospitals")
	@ResponseBody
	public Map<String,Object> gainHospitals(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		List<HospitalDetailInfo> hospitals = weixinService
				.queryHospitalDetailsALL();
		map.put("hospitals", hospitals);
		return map;
	}
	/**
	 * 群组成员管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/groupmembers/{groupId}")
	public ModelAndView groupMembers(HttpServletRequest request,@PathVariable Integer groupId){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("groupId",groupId);
		return new ModelAndView("/admin/groups/members",map);
	}
	/**
	 * 医生团队群组成员管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/docteamgroupmembers/{groupId}")
	public ModelAndView docteamgroupmembers(HttpServletRequest request,@PathVariable Integer groupId){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("groupId",groupId);
		//map.put("docId",docId);
		return new ModelAndView("/admin/groups/docteammembers",map);
	}
	/**	 * 获取医生团队群组成员数据
	 * 访问：http://localhost:8080/system/gainDocTeamGroupMemberDatas
	 * 参数：sEcho，searchContent，iDisplayStart，iDisplayLength，groupId（群组id)
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/gainDocTeamGroupMemberDatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainDocTeamGroupMemberDatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.gainDocTeamGroupMemberDatas(paramMap);	
	}*/
	/**
	 * 获取群组成员数据
	 * 访问：http://localhost:8080/system/gainGroupMemberDatas
	 * 参数：sEcho，searchContent，iDisplayStart，iDisplayLength，groupId（群组id)
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gainGroupMemberDatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainGroupMemberDatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.gainGroupMemberDatas(paramMap);	
	}
	/**
	 * 解散或生效群组
	 * 访问 ：http://localhost:8080/system/disSolveGroup
	 * groupId:群组id
	 * otype:1解散  2：生效
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/disSolveGroup")
	@ResponseBody
	public Map<String,Object> disSolveGroup(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Integer groupId=Integer.parseInt(request.getParameter("groupId"));
		Integer otype=Integer.parseInt(request.getParameter("otype"));
		systemAdminManager.disSolveGroup(groupId,otype,user.getId(),user.getUserType());
		return map; 
	}
	/**
	 * 解散医生团队团队群组
	 * 访问 ：http://localhost:8080/system/disSolveDocTeamGroup
	 * groupId:群组id
	 * otype:1解散  2：生效
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/disSolveDocTeamGroup")
	@ResponseBody
	public Map<String,Object> disSolveDocTeamGroup(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Integer groupId=Integer.parseInt(request.getParameter("groupId"));
		Integer otype=Integer.parseInt(request.getParameter("otype"));
		systemAdminManager.disSolveDocTeamGroup(groupId, otype, user.getId(), user.getUserType());
		return map; 
	}
	/**
	 * 踢出群组
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/removeGroupMember")
	@ResponseBody
	public Map<String,Object> removeGroupMember(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer memberId=Integer.parseInt(request.getParameter("memberId"));//成员id
		systemAdminManager.removeGroupMember(memberId);
		return map;
	}
	
	@RequestMapping(value="/sysConCaseManage")
	public ModelAndView sysConCaseManage(HttpServletRequest request){
		
		return new ModelAndView("admin/sys_con_case_manage");
	}
	
	/**
	 * 获取会诊案例数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sysconcasedatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object sysconcasedatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.sysconcasedatas(paramMap);	
	}
	/**
	 * 进入会诊案例操作  新增或修改
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sysConCaseOp")
	public ModelAndView sysConCaseOp(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String sscId=request.getParameter("sscId");
		if(StringUtils.isNotBlank(sscId)){
			PlatformHealthConsultation scase=commonService.querySystemConsultationCaseById(Integer.parseInt(sscId));
			map.put("scase", scase);
		}
		List<PlatformHealthType> healthTypes = commonService.queryUseingPlatFormHealthTypes(1);
		map.put("healthTypes", healthTypes);
		return new ModelAndView("admin/sys_con_case_op",map);
	}
	/**
	 * 会诊案例 新增或修改
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveorupdateSysConCaseInfo")
	@ResponseBody
	public Map<String,Object> saveorupdateSysConCaseInfo(HttpServletRequest request){
		String sscId=request.getParameter("sscId");
		String title=request.getParameter("title");
		String summary=request.getParameter("summary");
		String backImage=request.getParameter("backImage");
		String textLink=request.getParameter("textLink");
		Integer tagType = Integer.valueOf(request.getParameter("tagType"));
		systemAdminManager.saveorupdateSysConCaseInfo(sscId, title, summary, backImage, textLink, tagType);
		return null;
	}
	
	@RequestMapping(value="/deleteSysConCaseInfo")
	@ResponseBody
	public Map<String,Object> deleteSysConCaseInfo(HttpServletRequest request){
		Integer sscId=Integer.parseInt(request.getParameter("sscId"));
		Integer sval=Integer.parseInt(request.getParameter("sval"));
		PlatformHealthConsultation scase=commonService.querySystemConsultationCaseById(sscId);
		scase.setStatus(sval);
		commonService.updateSystemConsultationCase(scase);
		return null;
	}
	
	/**
	 * 手动退款
	 * oid:订单id
	 * otype：订单类型  6-图文问诊，7-电话问诊 9-快速问诊 12-团队问诊 5-图文会诊 4-视频会诊
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/refund")
	@ResponseBody
	public Map<String,Object> refund(HttpServletRequest request) throws Exception{
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		Integer otype=Integer.parseInt(request.getParameter("otype"));
		return systemAdminManager.refund(oid,otype);
	}
	/**
	 * 进入医生线下入账界面
	 * @return
	 */
	@RequestMapping(value="/docImcome",method= RequestMethod.GET)
	public ModelAndView docImcome() {
		return new ModelAndView("admin/doctor_imcome");
	}
		
	/**
	 * 获取医生
	* 方法：http://localhost:8080/system/gainDoctors
	* @return
	*/
	@RequestMapping(value = "/gainDoctors", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainDoctors(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return d2pManager.queryDoctor(paramMap);	
	}
	/**
	 * 提交线下入账
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/subDocImcome",method = RequestMethod.POST) 
	@ResponseBody
	public Map<String,Object> subDocImcome(HttpServletRequest request) {
		Integer doctorId = Integer.parseInt(request.getParameter("doctorId"));
		String amount = request.getParameter("amount");
		String amountType = request.getParameter("amountType");
		String operator = request.getParameter("operator");
		return systemAdminManager.subDocImcome(doctorId, amount, amountType, operator);
	}
	
	private Map<String, Object> signMap(StringBuffer homeUrl, String queryString)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(queryString)) {
			homeUrl.append("?").append(queryString);
		}
		long timestamp = System.currentTimeMillis() / 1000;
		String nonceStr = UUID.randomUUID().toString();
		String signature = SignUtil.getSignature(TokenThread.jsapi_ticket,
				nonceStr, timestamp, homeUrl.toString());
		map.put("appid", PropertiesUtil.getString("APPID"));
		map.put("timestamp", timestamp);
		map.put("nonceStr", nonceStr);
		map.put("signature", signature);
		return map;
	}
	
	/**
	 * 对医生的签约
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/privateDoctor")
	public ModelAndView privateDoctor(HttpServletRequest request){
		return new ModelAndView("/admin/d2p_private_order");
	}
	/**
	 * 获取签约医生的数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/docprivatedatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object docprivatedatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.docprivatedatas(paramMap);	
	}
	/**
	 * 
	 * 私人医生服务订单详情
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/privateorders", method = RequestMethod.GET)
	public ModelAndView privateorders(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer id=Integer.parseInt(request.getParameter("id"));
		BusinessD2pPrivateOrderDto order=commonService.queryprivateOrdersByid(id);
		map.put("order", order);	
		//支付信息
		List<BusinessPayInfo> payinfos = wenzhenService
						.queryBusinesPayInfosByOId(id, 15);
		map.putAll(payinfo(payinfos));
		//是否解约
		if((order.getStatus().equals(30)||order.getStatus().equals(50))&&order.getPayStatus()!=null&&order.getPayStatus().equals(1)){
			BigDecimal money=(BigDecimal)map.get("money");
		if(money!=null&&money.compareTo(BigDecimal.ZERO)>0){
				Object obj=map.get("refundStatus");
				if(obj!=null&&Integer.valueOf(obj.toString()).equals(1)){
						//退款成功，不需要显示退款
				}else{
						//待退款或退款失败
						map.put("needRefund", "true");
					}
				}
			}
		return new ModelAndView("admin/privateorder_detail",map);
	}
	/**
	 * 对医生团队的签约
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/privateDoctorTeam")
	public ModelAndView privateDoctorTeam(HttpServletRequest request){
		return new ModelAndView("/admin/t2p_privatevip_order");
	}
	/**
	 * 获取团队vip服务的数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/docteamvipdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object docteamvipdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.docteamvipdatas(paramMap);	
	}
	
	/**
	 * 
	 * 团队vip服务订单详情
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/teamviporders", method = RequestMethod.GET)
	public ModelAndView teamviporders(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer id=Integer.parseInt(request.getParameter("id"));
		BusinessT2pVipOrderDto order=commonService.queryteamevipByid(id);
		map.put("order", order);	
		List<BusinessPayInfo> payinfos = wenzhenService
				.queryBusinesPayInfosByOId(id, 14);
		map.putAll(payinfo(payinfos));
		//判断是否需要退款
		if((order.getStatus().equals(30)||order.getStatus().equals(50))&&order.getPayStatus()!=null&&order.getPayStatus().equals(1)){
				BigDecimal money=(BigDecimal)map.get("money");
			if(money!=null&&money.compareTo(BigDecimal.ZERO)>0){
					Object obj=map.get("refundStatus");
					if(obj!=null&&Integer.valueOf(obj.toString()).equals(1)){
							//退款成功，不需要显示退款
					}else{
							//待退款或退款失败
							map.put("needRefund", "true");
						}
					}
				}
		return new ModelAndView("admin/teamviporder_detail",map);
	}
	
	/**
	 * vip退款
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/refundVip",method = RequestMethod.POST) 
	@ResponseBody
	public Map<String,Object> refundVip(HttpServletRequest request) throws Exception {
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		Integer otype = Integer.parseInt(request.getParameter("otype"));
		return systemAdminManager.refundVip(oid,otype);
	}
	
	/**
	 * 服务信息列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/serviceinfo")
	public ModelAndView serviceinfo(HttpServletRequest request){
		return new ModelAndView("/admin/serviceinfo");
	}
	/**
	 * 获取服务的数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/servicedatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object servicedatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.servicedatas(paramMap);	
	}
	
	@RequestMapping(value="/dayNewAdd")
	public ModelAndView dayNewAdd() {
		return new ModelAndView("admin/count/day_new_add");
	}
	
	@RequestMapping(value="/newAddDatas")
	@ResponseBody 
	public Map<String,Object> newAddDatas(HttpServletRequest request) {
		String queryType = request.getParameter("queryType");
		return systemAdminManager.newAddDatas(queryType);
	}
	
	/**
	 * 服务详情 
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/servicedatasInfo", method = RequestMethod.GET)
	public ModelAndView servicedatasInfo(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer id=Integer.parseInt(request.getParameter("id"));
		//String multiplePackage = request.getParameter("multiplePackage");
		SystemServiceInfo order=commonService.queryservicedatasInfo(id);
		map.put("order", order);	
		return new ModelAndView("admin/serviceinfo_detail",map);
	}
	/**
	 * 进入会服务操作  修改
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/serviceinfoupdate")
	public ModelAndView serviceinfoupdate(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer serviceid = Integer.valueOf(request.getParameter("serviceid"));
		Integer MultiplePackage = Integer.valueOf(request.getParameter("MultiplePackage"));
		if(null != serviceid && MultiplePackage==0){
			SystemServiceInfo systemServiceInfo=commonService.querySystemServiceById(serviceid);
			SystemServicePackage systemServicePackage=commonService.queryServicePackageByServiceId(serviceid);
			map.put("systemServiceInfo", systemServiceInfo);
			map.put("systemServicePackage", systemServicePackage);
		}else if(null != serviceid && MultiplePackage==1){
			SystemServiceInfo systemServiceInfo=commonService.querySystemServiceById(serviceid);
			List<SystemServicePackage> systemServicePackages=commonService.queryServicePackagesByServiceId(serviceid);
			map.put("systemServiceInfo", systemServiceInfo);
			map.put("systemServicePackages", systemServicePackages);
		}
		return new ModelAndView("admin/serviceinfo_update",map);
	}
	/**
	 * 进入会服务操作  新增
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/serviceinfoAdd")
	public ModelAndView serviceinfoAdd(HttpServletRequest request){
		return new ModelAndView("admin/serviceinfo_add");
	}
	/**
	 * 服务编辑与新增
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveorupdateServiceInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveorupdateServiceInfo(HttpServletRequest request) {
		Map<String, Object> map =new HashMap<>();
		String obj = request.getParameter("obj");
		systemAdminManager.saveorupdateServiceInfo(obj);
		return map;
	}
	
    /**
     * 进入统计导出页面
     * @param request
     * @return
     */
    @RequestMapping(value="/exportinfo")
    public ModelAndView exportinfo(HttpServletRequest request){
        return new ModelAndView("admin/export_info");
    }

	/**
	 * 获取医院数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainhosdatas")
	@ResponseBody
	public Map<String, Object> gainhosdatas(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		List<HospitalDetailInfo> hospitals = weixinService
				.queryhospitalInfos();
		map.put("hospitals", hospitals);
		return map;
	}
	/**
	 * 获取医院科室数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainhosdepdatas")
	@ResponseBody
	public Map<String, Object> gainhosdepdatas(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		Integer hosid=Integer.valueOf(request.getParameter("hosid"));
		List<HospitalDepartmentInfo> deps= weixinService.queryDeps(hosid);
		map.put("deps", deps);
		return map;
	}
	
	/**
	 * 进入订单详情
	 * 访问：http://localhost:8080/system/order/detail
	 * 返回：common/order_detail_new.jsp
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/detail")
	public ModelAndView orderInfoDetail(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		String orderId = request.getParameter("orderId");
		String orderType = request.getParameter("orderType");
		map.put("orderId", orderId);
		map.put("orderType", orderType);
		return new ModelAndView("common/order_detail_new",map);
	}
	
	/**
	 * 获取订单详情--通用
	 * orderId:订单id
	 * orderType：订单类型
	 * 访问：http://localhost:8080/system/order/info
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/info")
	@ResponseBody
	public Map<String,Object> orderInfoCommon(HttpServletRequest request) throws Exception{
		Integer orderId = Integer.parseInt(request.getParameter("orderId"));
		Integer orderType = Integer.parseInt(request.getParameter("orderType"));
		return systemAdminManager.orderInfoCommon(orderId,orderType);
	}
	
	/**
	 * 医生关注量/报道量统计导出
	 * 	@param request
	 * @return	
	 */
	@RequestMapping(value = "/docreportabout")
	@ResponseBody	
	public void docreportabout(HttpServletResponse response,HttpServletRequest request) throws Exception{
        String hosid = request.getParameter("hosid"); //按医院
        String depid = request.getParameter("depid");//按科室查询
	    String startDate = request.getParameter("startDate");//开始关注时间
	    String endDate = request.getParameter("endDate");//结束关注时间
	    Integer type = Integer.valueOf(request.getParameter("type"));// 1 关注量  2报道量
	    String titlel;
	    if(type.equals(1)){
	    	titlel="医生患者关注量";
	    	JSONArray ja = new JSONArray();
	    	List<DoctorAboutCount> docAttentCount=commonService.gainDocAttentCount(hosid,depid,startDate,endDate);
	    	for (DoctorAboutCount doctorAboutCount : docAttentCount) {
	    		DoctorAboutCount doca=new DoctorAboutCount();
	    		doca.setDocName(doctorAboutCount.getDocName());
	    		doca.setDepName(doctorAboutCount.getDepName());
	    		doca.setHosName(doctorAboutCount.getHosName());
	    		doca.setCounts(doctorAboutCount.getCounts());
	    		ja.add(doca);
			}
	    	HashMap<String, String> headMap = new LinkedHashMap<>();
			headMap.put("docName", "医生姓名");
			headMap.put("hosName", "医院名称");
			headMap.put("depName", "科室名称");
			headMap.put("counts", "关注人数");
			ExcelUtilToService.downloadExcelFile(titlel, headMap, ja, response);
	    }else if(type.equals(2)){
	    	titlel="医生患者报道量";
	    	JSONArray ja = new JSONArray();
	    	List<DoctorAboutCount> docreportCount=commonService.gainDocreportCount(hosid,depid,startDate,endDate);
	    	for (DoctorAboutCount docreportCounts : docreportCount) {
	    		DoctorAboutCount doca=new DoctorAboutCount();
	    		doca.setDocName(docreportCounts.getDocName());
	    		doca.setDepName(docreportCounts.getDepName());
	    		doca.setHosName(docreportCounts.getHosName());
	    		doca.setCounts(docreportCounts.getCounts());
	    		doca.setRefuseCounts(docreportCounts.getRefuseCounts());
	    		doca.setReceiveCounts(docreportCounts.getReceiveCounts());
	    		doca.setWaitCount(docreportCounts.getWaitCount());
	    		ja.add(doca);
			}
	    	HashMap<String, String> headMap = new LinkedHashMap<>();
			headMap.put("docName", "医生姓名");
			headMap.put("hosName", "医院名称");
			headMap.put("depName", "科室名称");
			headMap.put("waitCount", "待审核");
			headMap.put("receiveCounts", "已同意");
			headMap.put("refuseCounts", "已拒绝");
			headMap.put("counts", "报道总人数");
			ExcelUtilToService.downloadExcelFile(titlel, headMap, ja, response);
	    }
	    
	}
	
	@RequestMapping(value="/erweimaExport")
	public void erweimaExport(HttpServletResponse response,HttpServletRequest request) throws Exception {
		//String zipFileName = "C:\\Users\\kx\\Desktop\\上饶市人民医院3.zip";
		String hosid = request.getParameter("hosid"); //按医院
	    String depid = request.getParameter("depid");//按科室查询
	    String startDate = request.getParameter("startDate");
	    String endDate = request.getParameter("endDate");
		ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
		response.setContentType("application/octet-stream; charset=utf-8");
	    response.setHeader("Content-Disposition","attachment; filename="+"ewm.zip");
		List<DoctorSceneEwm> list = commonService.queryDoctorSceneEwms_doc(hosid, depid, startDate, endDate);
		for(int i=0;i<list.size();i++) {
			System.out.println("===执行开始==="+i+":"+list.size());
			DoctorSceneEwm dse = list.get(i);
			Integer doctorId = dse.getDoctorId();
			if(doctorId ==-1)continue;
			System.out.println("==docid:"+doctorId);
			MobileSpecial doc = commonService.queryMobileSpecialByUserIdAndUserType(doctorId);
			URL url = new URL(dse.getErweimaUrl());
			out.putNextEntry(new ZipEntry(doc.getHosName().trim()+"/"+doc.getDepName().trim()+"/"+doc.getSpecialName().trim()
					+doc.getSpecialId()+(StringUtils.isNotBlank(doc.getDuty())?
					"-"+doc.getDuty():"")+".jpg"));
			InputStream fis = url.openConnection().getInputStream();
			byte[] buffer = new byte[1024];
			int r = 0;
			while ((r = fis.read(buffer)) != -1) {
				out.write(buffer, 0, r);
			}
			fis.close();
		}
		System.out.println("===结束");
		out.flush();
		out.close();
	}
	
	/**
	 * 进入医生登陆列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/doctorlogincal")
	public ModelAndView doctorlogincal(HttpServletRequest request){
		return new ModelAndView("admin/doc_logincal");
	}
	
	/**
	 * 获取医生登陆数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaindoclogindata", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindoclogindata(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return commonManager.querydoclogindata(paramMap);	
	}
	
	/**
	 * 医生关注量/报道量统计
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/doctorAbout")
	public ModelAndView doctorAbout(HttpServletRequest request){
		return new ModelAndView("/admin/doctor_about");
	}
			
	/**
	 * 医生关注量/报道量统计查询
	 * 	@param request
	 * @return	
	 */
	@RequestMapping(value = "/docreportaboutdatass",method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody	
	public Object docreportaboutdatass(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception{
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.docreportaboutdatass(paramMap);
	}	
	
	/*
	 * 医生注册导出
	 * 
	 */
	@RequestMapping(value = "/docregisterexport")
	@ResponseBody	
	public void docregisterexport(HttpServletResponse response,HttpServletRequest request) throws Exception{
        String hosid = request.getParameter("hosid"); //按医院
        String depid = request.getParameter("depid");//按科室查询
	    String startDate = request.getParameter("startDate");//开始关注时间
	    String endDate = request.getParameter("endDate");//结束关注时间
	    String titlel="医生注册详情";
	    JSONArray ja = new JSONArray();
	    List<MobileSpecial> docAttentCount=commonService.gainDocregisterexport(hosid,depid,startDate,endDate);
	    	for (MobileSpecial mobileSpecial : docAttentCount) {
	    		DocRegisterDto docreg=new DocRegisterDto();
	    		docreg.setDocName(mobileSpecial.getSpecialName());
	    		docreg.setDepName(mobileSpecial.getDepName());
	    		docreg.setHosName(mobileSpecial.getHosName());
	    		docreg.setRegisterTime(mobileSpecial.getRegisterTimes());
	    		if(mobileSpecial.getStatus().equals(-1)){
	    			docreg.setStatuss("待认证");
	    		}else if(mobileSpecial.getStatus().equals(-3)){
	    			docreg.setStatuss("认证未通过");
	    		}else if(mobileSpecial.getStatus().equals(1)){
	    			docreg.setStatuss("启用");
	    		}
	    		ja.add(docreg);
			}
	    	HashMap<String, String> headMap = new LinkedHashMap<>();
			headMap.put("docName", "医生姓名");
			headMap.put("hosName", "医院名称");
			headMap.put("depName", "科室名称");
			headMap.put("registerTime", "注册时间");
			headMap.put("statuss", "审核状态");
			ExcelUtilToService.downloadExcelFile(titlel, headMap, ja, response);
	}
	
	/**
	 * 短信验证码
	 * @return
	 */
	@RequestMapping(value="/smsinfo")
	public ModelAndView smsinfo() {
		return new ModelAndView("/admin/sms_record");
	}
	@RequestMapping(value = "/smsDatas",method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody	
	public Object smsDatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception{
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.smsDatas(paramMap);
	}	
	
	/**
	 * 
	 * 医生收入导出
	 */
	@RequestMapping(value = "/docincomeexport")
	@ResponseBody	
	public void docincomeexport(HttpServletResponse response,HttpServletRequest request) throws Exception{
		String hid=request.getParameter("hosid"); //按医院
		String depid = request.getParameter("depid");//按医院科室
		String startDate = request.getParameter("startDate");//开始关注时间
	    String endDate = request.getParameter("endDate");//结束关注时间
	    String titlel="医生收入";
	    JSONArray ja = new JSONArray();
	    List<DoctorIncomeDto> doc=commonService.docincomeexport(hid,depid,startDate,endDate);
	    	for (DoctorIncomeDto doctorIncomeDto : doc) {
	    		DoctorIncomeDto docim=new DoctorIncomeDto();
	    		docim.setDocid(doctorIncomeDto.getDocid());
	    		docim.setDocName(doctorIncomeDto.getDocName());
	    		docim.setHosName(doctorIncomeDto.getHosName());
	    		docim.setDepName(doctorIncomeDto.getDepName());
	    		docim.setTw(doctorIncomeDto.getTw());
	    		docim.setTel(doctorIncomeDto.getTel());
	    		docim.setFasts(doctorIncomeDto.getFasts());
	    		docim.setWarm(doctorIncomeDto.getWarm());
	    		docim.setInvite(doctorIncomeDto.getInvite());
	    		docim.setQiandao(doctorIncomeDto.getQiandao());
	    		docim.setTodaymoney(doctorIncomeDto.getTodaymoney());
	    		docim.setSummoney(doctorIncomeDto.getSummoney());
	    		ja.add(docim);
			}
	    	HashMap<String, String> headMap = new LinkedHashMap<>();
			headMap.put("docid", "医生id");
			headMap.put("docName", "医生名称");
			headMap.put("hosName", "医院名称");
			headMap.put("depName", "科室名称");
			headMap.put("tw", "图文问诊");
			headMap.put("fasts", "快速问诊");
			headMap.put("tel", "电话问诊");
			headMap.put("warm", "送心意");
			headMap.put("invite", "邀请收入");
			headMap.put("qiandao", "签到收入");
			headMap.put("todaymoney", "今日收入");
			headMap.put("summoney", "累计收入");
			ExcelUtilToService.downloadExcelFile(titlel, headMap, ja, response);
	}
	
	@RequestMapping(value="/docFollows")
	public ModelAndView docFollows() {
		return new ModelAndView("admin/follow/doc_follows");
	}
	
	
	@RequestMapping(value = "/gainDocFollowDatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainDocFollowDatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.gainDocFollowDatas(paramMap);	
	}
	
	@RequestMapping(value="/gainDocFollowDetailData") 
	public ModelAndView gainDocFollowDetailData(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		Integer type = Integer.parseInt(request.getParameter("type"));
		String orderUuid = request.getParameter("orderUuid");
		String docId = request.getParameter("docId");
		String subUserId = request.getParameter("subUserId");
		List<DocFollowDto> datas = commonService.queryDocFollowDetailData(type, orderUuid, docId, subUserId);
		map.put("datas", datas);
		return new ModelAndView("admin/follow/doc_follow_detail",map);
	}
	
    /**
     * 送心意页面
     * @return
     */
    @RequestMapping(value="/warmsinfo")
    public ModelAndView warmsinfo() {
        return new ModelAndView("admin/user_doc_warm");
    }
    /**
     * 送心意详情
     * @param request
     * @return    
     */
    @RequestMapping(value = "/userWarmDocDatas",method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody    
    public Object userWarmDocDatas(@RequestBody JSONParam[] params,
            HttpServletRequest request) throws Exception{
        Map<String, String> paramMap = convertToMap(params);
        return systemAdminManager.userWarmDocDatas(paramMap);
    }
    
    @RequestMapping(value="/vediolist",method=RequestMethod.GET)
    public ModelAndView vediolist() {
    	return new ModelAndView("admin/tools/vedio_list");
    }

	/**
	 * 直播计划列表查询
	 * 	@param request
	 * @return
	 */
	@RequestMapping(value = "/liveplandatass",method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Object liveplandatass(@RequestBody JSONParam[] params,
									   HttpServletRequest request) throws Exception{
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.liveplandatass(paramMap);
	}

	// 直播详情信息

	@RequestMapping(value = "/planliveInfo", method = RequestMethod.GET)
	public ModelAndView planliveInfo(HttpServletRequest request,HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		//Integer userId=user.getId();
		DoctorDetailInfo doc=commonService.queryDoctorDetailInfoById(user.getId());
		//生成融云token
		String rongCloudUserId = RongCloudApi.getRongCloudUserId(13590, 6);
		String token = RongCloudApi.getUserToken(rongCloudUserId,doc.getDisplayName(),doc.getHeadImageUrl());
		Integer liveid=Integer.valueOf(request.getParameter("liveId"));
		LivePlanOrder order = weixinService.queryPlanLiveOrderById(liveid);
		String appkey = PropertiesUtil.getString("rongcloud_appkey").trim();
		map.put("order", order);
		map.put("token",token);
		map.put("key",appkey);
		return new ModelAndView("admin/tools/plan_live_info", map);
	}

	/**
	 * 新增或修改直播
	 * @param request
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/saveorupdateplanlive", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveorupdateplanlive(HttpServletRequest request) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		String liveId = request.getParameter("liveId");
		String title = request.getParameter("title");
		String beginTime = request.getParameter("beginTime");
		String chatRoomCloseTime = request.getParameter("chatRoomCloseTime");
		String chatRoomStartTime = request.getParameter("chatRoomStartTime");
		String userId = request.getParameter("userId");
		String vedioUrl = request.getParameter("vedioUrl");
		String duration = request.getParameter("duration");
		String consultationDetails = request.getParameter("consultationDetails");
		LivePlanOrder live = null;
		if (StringUtils.isNotBlank(liveId)) {
			// 编辑
			live = weixinService.queryPlanLiveOrderById(Integer
					.parseInt(liveId));
			if(StringUtils.isNotBlank(title)){
				live.setTitle(title);
			}
			if(StringUtils.isNotBlank(beginTime)){
				live.setBeginTime(Timestamp.valueOf(beginTime));
			}
			if(StringUtils.isNotBlank(chatRoomStartTime)){
				live.setChatRoomStartTime(Timestamp.valueOf(chatRoomStartTime));
			}
			if(StringUtils.isNotBlank(chatRoomCloseTime)){
				live.setChatRoomCloseTime(Timestamp.valueOf(chatRoomCloseTime));
			}
			if(StringUtils.isNotBlank(userId)){
				live.setUserId(Integer.valueOf(userId));
			}
			if(StringUtils.isNotBlank(vedioUrl)){
				live.setVedioUrl(vedioUrl);
			}
			if(StringUtils.isNotBlank(duration)){
				live.setDuration(Double.valueOf(duration));
				//计算视频结束时间
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			     Date date = (Date) sdf.parse(beginTime);
			     Date date2 = new Date(Long.valueOf(duration)+date.getTime());
			     String enddate=sdf.format(date2);
			     live.setEndTime(Timestamp.valueOf(enddate));
			}
			if(StringUtils.isNotBlank(consultationDetails)){
				live.setConsultationDetails(consultationDetails);
			}
			//live.setCreateTime(new Timestamp(System.currentTimeMillis()));
			weixinService.updatePlanLiveInfo(live);
		} else {
			// 新增
			live = new LivePlanOrder();
			live.setUuid(UUIDUtil.getUUID());
			if(StringUtils.isNotBlank(title)){
				live.setTitle(title);
			}
			if(StringUtils.isNotBlank(beginTime)){
				live.setBeginTime(Timestamp.valueOf(beginTime));
			}
			if(StringUtils.isNotBlank(chatRoomStartTime)){
				live.setChatRoomStartTime(Timestamp.valueOf(chatRoomStartTime));
			}
			if(StringUtils.isNotBlank(chatRoomCloseTime)){
				live.setChatRoomCloseTime(Timestamp.valueOf(chatRoomCloseTime));
			}
			if(StringUtils.isNotBlank(userId)){
				live.setUserId(Integer.valueOf(userId));
			}
			if(StringUtils.isNotBlank(vedioUrl)){
				live.setVedioUrl(vedioUrl);
			}
			if(StringUtils.isNotBlank(duration)){
				live.setDuration(Double.valueOf(duration));
				//计算视频结束时间
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			     Date date = (Date) sdf.parse(beginTime);
			     Date date2 = new Date(Long.valueOf(duration)+date.getTime());
			     String enddate=sdf.format(date2);
			     live.setEndTime(Timestamp.valueOf(enddate));
			}
			if(StringUtils.isNotBlank(consultationDetails)){
				live.setConsultationDetails(consultationDetails);
			}
			live.setCreateTime(new Timestamp(System.currentTimeMillis()));
			Integer id = weixinService.savePlanLiveInfo(live);
		}
		return map;
	}
	
 
	
	@RequestMapping(value="delplanliveinfo",method=RequestMethod.POST) 
	public @ResponseBody void delPlanLiveInfo(HttpServletRequest request) {
		Integer liveId = Integer.parseInt(request.getParameter("liveId"));
		weixinService.delPlanLiveInfo(liveId);
		
	}
	
	//===================往期视频回顾编辑=======================================
	/**
	 * 进入往期案例列表界面
	 * @return
	 */
	@RequestMapping(value="/hiscase/manage",method=RequestMethod.GET)
	public ModelAndView hisCaseManage() {
		return new ModelAndView("admin/tools/his_case_manage");
	}
	
	/**
	 * 往期病例列表数据
	 * 	@param request
	 * @return
	 */
	@RequestMapping(value = "/hiscase/list",method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Object hisCaseList(@RequestBody JSONParam[] params,
									   HttpServletRequest request) throws Exception{
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.hisCaseList(paramMap);
	}
	/**
	 * 进入新增或编辑界面
	 * @return
	 */
	@RequestMapping(value="/hiscase/operate",method=RequestMethod.GET)
	public ModelAndView hisCaseAdd(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("hisCaseUuid", request.getParameter("hisCaseUuid"));
		return new ModelAndView("admin/tools/his_case_add",map);
	}
	
	/**
	 * 新增编辑
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/hiscase/save",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> hisCaseSave(HttpServletRequest request) {
		String hisCaseUuid = request.getParameter("hisCaseUuid");//案例uuid 编辑时使用
		Integer type = Integer.parseInt(request.getParameter("type"));//4--视频  5--图文
		String title = request.getParameter("title");//案例标题
		String depIds = request.getParameter("depIds");//会诊科室ids
		String mainSuit = request.getParameter("mainSuit");//案例简介
		String caseDesc = request.getParameter("caseDesc");//图文案例描述信息
	    String backImageUrl = request.getParameter("backImageUrl");//背景图url
	    String attachmentIds = request.getParameter("attachmentIds");//附件id集合 视频和图文一样
	    String treatAdvice = request.getParameter("treatAdvice");//诊断结果
	    return systemAdminManager.hisCaseSave(type,hisCaseUuid,title,depIds,mainSuit,
	    		caseDesc,backImageUrl,attachmentIds,treatAdvice);
	    
	}
	/**
	 * 获取会诊案例详情
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/hiscase/get",method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> hisCaseGet(HttpServletRequest request) {
		String hisCaseUuid = request.getParameter("hisCaseUuid");
		return systemAdminManager.hisCaseGet(hisCaseUuid);
	}
	
	@RequestMapping(value="/hisCase/delete",method=RequestMethod.POST)
	public @ResponseBody void hisCaseDelete(HttpServletRequest request) {
		String hisCaseUuid = request.getParameter("hisCaseUuid");
		systemAdminManager.hisCaseDelete(hisCaseUuid);
	}
	@RequestMapping(value = "/standsdeparts", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> standsdeparts(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取标准科室
		List<StandardDepartmentInfo> stands = weixinService.queryStandardDepartments();
		map.put("stands", stands);
		return map;
	}
}
