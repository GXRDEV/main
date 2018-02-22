package com.tspeiz.modules.home;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;

import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse.JobResult;
import com.mongodb.DBObject;
import com.tspeiz.modules.common.bean.JSONParam;
import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.PacsHelper;
import com.tspeiz.modules.common.bean.PushCodeEnum;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.SystemPushInfo;
import com.tspeiz.modules.common.entity.SystemServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.AppraisementDoctorInfo;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDictionary;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDoctor;
import com.tspeiz.modules.common.entity.newrelease.BaiduAudioToken;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
import com.tspeiz.modules.common.entity.newrelease.BusinessTelOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2dReferralOrder;
import com.tspeiz.modules.common.entity.release2.DoctorScheduleShow;
import com.tspeiz.modules.common.service.IApiGetuiPushService;
import com.tspeiz.modules.common.service.ICaseService;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.ID2pService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.home.rtc.UserClient;
import com.tspeiz.modules.home.rtc.room.WebRTCRoomManager;
import com.tspeiz.modules.home.rtc.room.WebRTCRoomManager_new;
import com.tspeiz.modules.manage.CommonManager;
import com.tspeiz.modules.manage.DoctorAdminManager;
import com.tspeiz.modules.manage.ErweiManager;
import com.tspeiz.modules.util.CheckNumUtil;
import com.tspeiz.modules.util.DataCatchUtil;
import com.tspeiz.modules.util.DateUtil;
import com.tspeiz.modules.util.DicomLoadUtil;
import com.tspeiz.modules.util.PasswordUtil;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.PythonVisitUtil;
import com.tspeiz.modules.util.common.BusinessTypeGenerate;
import com.tspeiz.modules.util.common.LoadLisAndPacsDataUtil;
import com.tspeiz.modules.util.config.ReaderConfigUtil;
import com.tspeiz.modules.util.date.RelativeDateFormat;
import com.tspeiz.modules.util.dcm.UploadDcmUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.mongodb.MongoDBManager;
import com.tspeiz.modules.util.oss.OSSConfigure;
import com.tspeiz.modules.util.oss.OSSManageUtil;
import com.tspeiz.modules.util.oss.VedioTransUtil;
import com.tspeiz.modules.util.redis.RedisUtil;
import com.tspeiz.modules.util.weixin.CommonUtil;
import com.uwantsoft.goeasy.client.goeasyclient.GoEasy;

@Controller
@RequestMapping("doctor")
public class CommonVisitController {
	private Logger log = Logger.getLogger(CommonVisitController.class);
	private SimpleDateFormat dir_time = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat code_time = new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat year_sdf=new SimpleDateFormat("yyyy");
	private static SimpleDateFormat month_sdf=new SimpleDateFormat("MM");
	private static SimpleDateFormat day_sdf=new SimpleDateFormat("dd");
	private SimpleDateFormat seriestime = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");
	private static List<String> videos = null;
	
	static {
		videos=new ArrayList<String>();
		videos.add("mp4");
		videos.add("mov");
		videos.add("avi");
		videos.add("wmv");
	}
	private SimpleDateFormat _format = new SimpleDateFormat("yyyy-MM-dd");
	@Resource
	private ICommonService commonService;
	@Resource
	private IWeixinService weixinService;
	@Resource
	private IWenzhenService wenzhenService;
	@Autowired
	private ErweiManager erweiManager;
	@Autowired
	private CommonManager commonManager;
	@Autowired
	private DoctorAdminManager doctorAdminManager;
	@Autowired
	private ICaseService caseService;
    @Resource
    private IApiGetuiPushService apiGetuiPushService;


	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String ltype = request.getParameter("ltype");
		map.put("ltype", ltype);
		// 二维码keyid生成及入库
		/*
		 * String keyid=erweiManager.generateErInfo(); map.put("keyid", keyid);
		 */
		return new ModelAndView("common/login", map);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> logout(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		request.getSession().setAttribute("user", null);
		request.getSession().setAttribute("userDetail", null);
		return map;
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, ModelMap model) {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		if (user == null)
			return new ModelAndView("common/login");
		String url = "";
		if (user.getUserType().equals(2)) {
			// 专家版
			url = "expert/index";
		} else if (user.getUserType().equals(3)) {
			// 医生版
			url = "doc/index";
		} else if (user.getUserType().equals(4)) {
			// 护士版
			url = "nurse/index";
		} else if (user.getUserType().equals(5)) {
			// 医院管理版本
			url = "hos/index";
		} else if (user.getUserType().equals(6)) {
			// 管理员版本
			url = "admin/index";
		}
		return new ModelAndView(url);
	}

	
	private Map<String,Object> keyMap=new HashMap<String,Object>();
	
	/**
	 * 登陆方法 stype{2-专家，3-医生 空值为管理员}
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> checkLogin(HttpServletRequest request,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String stype = request.getParameter("stype");
		DoctorRegisterInfo user = null;
		boolean b = true;
		if (StringUtils.isNotBlank(stype)) {
			user = commonService.queryDoctorRegisterInfoByTel(username,
					Integer.parseInt(stype));
			if (user != null) {
				if(StringUtils.isBlank(user.getSalt())){
					if (!PasswordUtil.MD5Salt(password).equalsIgnoreCase(
							user.getPassword())) {
						b = false;
					}
				}else {
					if (!PasswordUtil.MD5Salt(password,user.getSalt()).equalsIgnoreCase(
							user.getPassword())) {
						b = false;
					}
				}
			}
		} else {
			user = commonService.queryDoctorRegisterInfo(username,
					PasswordUtil.MD5Salt(password));
		}
		if (b && user != null) {
			String loginUuid=UUID.randomUUID().toString().replace("-", "");
			MobileSpecial userDetail = commonService
					.queryMobileSpecialByUserIdAndUserType(user.getId());
			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("userDetail", userDetail);
			map.put("status", "success");
			map.put("stype", user.getUserType() + "");
			map.put("uid", session.getId());
			map.put("id", user.getId());
			map.put("loginUuid",loginUuid);
			if(user.getUserType().equals(2)||user.getUserType().equals(3)){
				GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
				JSONObject jObj = new JSONObject();
				jObj.put("type", "onlyOneWeb");
				jObj.put("loginUuid", loginUuid);
				goEasy.publish((user.getUserType().equals(3)?"doctor_":"expert_")+user.getId(),jObj.toString());
			}
		} else {
			map.put("status", "error");
		}
		return map;
	}

	@RequestMapping(value = "/gainVeryCode")
	public @ResponseBody
	Map<String, Object> gainVeryCode(HttpServletRequest request,
			HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String telphone = request.getParameter("telphone");
		DoctorRegisterInfo reg = commonService
				.queryDoctorRegisterInfoByTel(telphone);
		
		  if(reg!=null){ 
			  String code = CheckNumUtil.randomChars(4);
			  String content = " 验证码为" + code +
					  "，佰医汇提醒您，您正在使用\"佰医汇\"云SaaS平台，请勿泄露给第三人！【佰医汇】"; 
			  log.info("code===" +code + " content===" + content);
			  String ret = HttpSendSmsUtil.sendSmsInteface(telphone, content);
			  request.getSession().setAttribute("bindCode", telphone + "" + code);
			  request.getSession().setAttribute("verifyCodeTime",code_time.format(new Date())); 
			  if (ret.equalsIgnoreCase("100")) {
				  	map.put("status", "success");
				  	request.getSession().setAttribute("bindCode", telphone + "" + code);
				  	request.getSession().setAttribute("verifyCodeTime",
				  	code_time.format(new Date())); 
			   } else { 
				   map.put("status", "error"); 
			   }
		  }else{ 
			  map.put("status", "error"); 
		  }
		 
		log.info("===还在不停的刷么===");
		return map;
	}
	

	/**
	 * 获取短信验证码
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getsmscode")
	public @ResponseBody
	Map<String, Object> getsmscode(HttpServletRequest request,
			HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String telphone = request.getParameter("telphone");
		DoctorRegisterInfo reg = commonService
				.queryDoctorRegisterInfoByTel(telphone);
		if (reg != null) {
			String code = CheckNumUtil.randomChars(4);
			String content = " 验证码为" + code
					+ "，佰医汇提醒您，您正在使用\"佰医汇\"云SaaS平台，请勿泄露给第三人！【佰医汇】";
			log.info("code===" + code + " content===" + content);
			String ret = HttpSendSmsUtil.sendSmsInteface(telphone, content);
			request.getSession().setAttribute("bindCode", telphone + "" + code);
			request.getSession().setAttribute("verifyCodeTime",
					code_time.format(new Date()));
			if (ret.equalsIgnoreCase("100")) {
				map.put("status", "success");
				request.getSession().setAttribute("bindCode",
						telphone + "" + code);
				request.getSession().setAttribute("verifyCodeTime",
						code_time.format(new Date()));
			} else {
				map.put("status", "error");
			}
		} else {
			map.put("status", "error");
		}
		return map;
	}

	// 下一步验证
	@RequestMapping(value = "/nextvalidate", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> nextvalidate(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String tel = request.getParameter("telphone");
		String code = request.getParameter("code");
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
					map.put("status", "success");
				} else {
					map.put("status", "error");
				}
			}
		} else {
			map.put("status", "error");
		}
		return map;
	}

	// 忘记密码=====设置新密码
	@RequestMapping(value = "/newpassset", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> newpassset(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String tel = request.getParameter("telphone");
		String newpass = request.getParameter("newpass");
		// 修改密码
		DoctorRegisterInfo user = commonService
				.queryDoctorRegisterInfoByTel(tel);
		user.setSalt("cvYl8U");
		user.setPassword(PasswordUtil.MD5Salt(newpass));
		commonService.updateDoctorRegisterInfo(user);
		map.put("status", "success");
		return map;
	}

	// 进入详情
	/*
	 * oid===订单id vtype===1(医生) 2==专家 医生和专家公用一个详情方法
	 */
	@RequestMapping(value = "/orderdetail/{oid}/{vtype}", method = RequestMethod.GET)
	public ModelAndView orderdetail(HttpServletRequest request,
			@PathVariable Integer oid, @PathVariable Integer vtype)
			throws Exception {
		HttpSession session = request.getSession();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessVedioOrder order = wenzhenService
				.queryBusinessVedioOrderById(oid);
		if (vtype.equals(2)) {
			// 专家进入需要记录系统消息
			commonManager.saveBusinessMessageInfo_sys(oid, 4, "text", "专家查看病例",
					order.getLocalDoctorId(), 3);
		}
		map.put("order", order);
		map.put("oid", oid);
		map.put("vtype", vtype);
		map.put("uid", session.getId());
		/*
		 * String ret = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
		 * "root/openorclose");
		 */
		/*
		 * if (vtype.equals(2) && order.getExpertReaded() == null &&
		 * ret.equalsIgnoreCase("1")) { sendsms(order, 2);
		 * order.setExpertReaded(1);
		 * weixinService.updateRemoteConsultation(order); }
		 */
		// 本地图片加载
		/*
		 * List<CustomFileStorage> normals = new ArrayList<CustomFileStorage>();
		 * CaseInfo caseinfo=commonService.queryCaseInfoById(order.getCaseId());
		 * map.put("caseinfo", caseinfo); String normalImages =
		 * caseinfo.getNormalImages(); CustomFileStorage cu = null; if
		 * (StringUtils.isNotBlank(normalImages)) { String[] _images =
		 * normalImages.split(","); if (_images != null && _images.length > 0) {
		 * for (String _image : _images) { cu =
		 * commonService.queryCustomFileStorage(Integer .parseInt(_image)); if
		 * (cu != null) { String filename = cu.getFileUrl();
		 * cu.setFileType(filename.substring(filename .lastIndexOf(".") + 1));
		 * normals.add(cu); } } } } map.put("normals", normals);
		 */
		Integer _hasuser = WebRTCRoomManager.getUserNum(order.getId()
				.toString());
		map.put("_hasuser", _hasuser);

		// 推给自己,用于取消消息
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "clearNotify");
		jObj.put("from", vtype);
		jObj.put("orderId", oid);

		String outsideGroup = "";
		if (user.getUserType() == 2) {
			outsideGroup = "expert_" + order.getExpertId();
		} else {
			outsideGroup = "doctor_" + order.getLocalDoctorId();
		}
		System.out.print("\r\n=======outsideGroup:" + outsideGroup + ","
				+ jObj.toString());
		goEasy.publish(outsideGroup + "", jObj.toString() + "");
		String _ret = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
				"root/openremote");
		System.out.println("===" + _ret);

		if (_ret.equalsIgnoreCase("old")) {
			return new ModelAndView("common/order_detail", map);
		} else {
			if (user.getUserType() == 2) {
				return new ModelAndView("expert/order_detail_2", map);
			} else {
				return new ModelAndView("doc/order_detail_1", map);
			}
		}
	}

	@RequestMapping(value = "/gainLisData")
	public @ResponseBody
	Map<String, Object> gainLisData(HttpServletRequest request)
			throws Exception {
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		String asktype=request.getParameter("asktype");
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(asktype)){
			//专家咨询
		}else{
			BusinessVedioOrder order = commonService
					.queryBusinessVedioOrderById(oid);
			List<ReSourceBean> records = DataCatchUtil.gainLisData(order);
			map.put("records", LoadLisAndPacsDataUtil.gainLisGroups(records));
		}
		return map;
	}

	@RequestMapping(value = "/gainPacsData")
	public @ResponseBody
	Map<String, Object> gainPacsData(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String asktype = request.getParameter("asktype");
		if (StringUtils.isNotBlank(asktype) && asktype.equalsIgnoreCase("true")) {
			String oid=request.getParameter("oid");
			String uuid = request.getParameter("uuid");
			if(StringUtils.isNotBlank(oid)){
				SpecialAdviceOrder adviceOrder=commonService.querySpecialAdviceOrderById(Integer.parseInt(oid));
				uuid=adviceOrder.getUuid();
			}
			List<ReSourceBean> pac_records = DataCatchUtil.gainPacsData(uuid);
			map.put("pac_records",
					LoadLisAndPacsDataUtil.gainPacsGroup(pac_records));
		}else if(StringUtils.isNotBlank(asktype)&&asktype.equalsIgnoreCase("referral")){
			String oid=request.getParameter("oid");
			String uuid = request.getParameter("uuid");
			if(StringUtils.isNotBlank(oid)){
				BusinessD2dReferralOrder referOrder=d2pService.queryd2dreferralOrderbyId(Integer.parseInt(oid));
				uuid=referOrder.getUuid();
			}
			List<ReSourceBean> pac_records = DataCatchUtil.gainPacsData(uuid);
			map.put("pac_records",
					LoadLisAndPacsDataUtil.gainPacsGroup(pac_records));
		}else {
			Integer oid = Integer.parseInt(request.getParameter("oid"));
			BusinessVedioOrder order = commonService
					.queryBusinessVedioOrderById(oid);

			List<ReSourceBean> pac_records = DataCatchUtil.gainPacsData(order
					.getId().toString());
			map.put("pac_records",
					LoadLisAndPacsDataUtil.gainPacsGroup(pac_records));
		}
		return map;
	}
	/**
	 * 获取影像--病例管理
	 * http://localhost:8080/doctor/gainPacsData_case
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gainPacsData_case")
	public @ResponseBody
	Map<String, Object> gainPacsDataCase(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String caseUuid = request.getParameter("caseUuid");
		List<ReSourceBean> pac_records = DataCatchUtil.gainPacsDataCase(caseUuid);
		map.put("pac_records",
				LoadLisAndPacsDataUtil.gainPacsGroup(pac_records));
		return map;
	}
	

	// ******************************************************同步模块改版开始*************************************************
	// 1.点击同步获取病人列表
	@RequestMapping(value = "/gainUserPatients")
	public @ResponseBody
	Map<String, Object> gainUserCases(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String orderid = request.getParameter("oid");// 获取订单id
		String idsearch = request.getParameter("idsearch");
		String idcard = "";
		List<ReSourceBean> beans = null;
		String searchName = request.getParameter("searchName");
		String searchPatientId = request.getParameter("searchPatientId");
		String admisionNum = request.getParameter("adminsionNum");
		String outpatientNum = request.getParameter("outpatientNum");
		String department = request.getParameter("department");
		if (StringUtils.isNotBlank(orderid)) {
			BusinessVedioOrder order = commonService
					.queryBusinessVedioOrderById(Integer.parseInt(orderid));
			CaseInfo caseinfo = commonService.queryCaseInfoById(order
					.getCaseId());
			idcard = caseinfo.getIdNumber();
		}
		beans = PythonVisitUtil.patients_list(orderid, idcard, searchName,
				searchPatientId, admisionNum, outpatientNum, department);// 暂时设置id为默认
		map.put("beans", beans);
		return map;
	}

	// 2,选择一条开始同步所有数据
	@RequestMapping(value = "syncLisInfoByCase", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> syncLisInfoByCase(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String orderid = request.getParameter("oid");
		String patientid = request.getParameter("patientid");
		String syncSeries = seriestime.format(new Date());
		request.getSession().setAttribute("synclisseris",
				orderid + "_" + syncSeries);
		log.info("====patientid===" + patientid);
		// 根据病人id开始同步数据
		// 同步lis数据
		log.info("***********开始同步lis数据*****************");
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(Integer.parseInt(orderid));
		commonManager.saveBusinessMessageInfo_sys(Integer.parseInt(orderid), 4,
				"text", "医生同步患者检查报告数据", order.getExpertId(), 2);
		List<ReSourceBean> lisbeans = syncLisInfo(orderid, patientid, request,
				syncSeries);
		log.info("***********同步lis数据结束*****************");
		map.put("lisbeans", LoadLisAndPacsDataUtil.gainLisGroups(lisbeans));
		return map;
	}

	// 2,选择一条开始同步所有数据
	@RequestMapping(value = "syncPacInfoByCase", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> syncPacInfoByCase(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String orderid = request.getParameter("oid");
		String patientid = request.getParameter("patientid");
		String syncSeries = request.getParameter("syncSeries");
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(Integer.parseInt(orderid));
		commonManager.saveBusinessMessageInfo_sys(Integer.parseInt(orderid), 4,
				"text", "医生同步患者影像报告数据", order.getExpertId(), 2);
		log.info("====patientid===" + patientid);
		// 根据病人id开始同步数据
		// 同步pacs数据
		log.info("***********开始同步pacs数据****************");
		List<ReSourceBean> pacsbeans = syncPacsInfo(orderid, patientid,
				request, syncSeries);
		// List<ReSourceBean> pacsbeans =new ArrayList<ReSourceBean>();
		log.info("***********同步pacs数据结束****************");
		map.put("pacsbeans", LoadLisAndPacsDataUtil.gainPacsGroup(pacsbeans));
		return map;
	}

	// 同步lis数据
	private List<ReSourceBean> syncLisInfo(String orderid, String patientid,
			HttpServletRequest request, String syncSeries) throws Exception {
		log.info("====开始获取lis数据=====");
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(Integer.parseInt(orderid));
		List<ReSourceBean> beans = PythonVisitUtil.records_list(orderid,
				patientid, "lis_tb", "lis_sub_tb", request, syncSeries,
				String.valueOf(order.getCaseId()));
		log.info("====获取lis数据完成=====");
		return beans;
	}

	// 同步pacs数据
	private List<ReSourceBean> syncPacsInfo(String orderid, String patientid,
			HttpServletRequest request, String syncSeries) throws Exception {
		log.info("====开始获取pacs数据====");
		String dir = request.getServletContext().getRealPath("dcmtemp");
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(Integer.parseInt(orderid));
		List<ReSourceBean> beans = PythonVisitUtil.imagesex(orderid, patientid,
				"pacs_tb", request, syncSeries, dir,
				String.valueOf(order.getCaseId()));
		log.info("====获取pacs数据结束====");
		return beans;
	}

	@ResponseBody
	@RequestMapping(value = "/gainImagePics", method = RequestMethod.GET)
	public String gainImagePics(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String oid = request.getParameter("oid");// 订单id
		String rid = request.getParameter("rid");// 父id
		// 先从缓存获取数据
		Jedis edis = RedisUtil.getJedis();
		String pacs_details = edis.get(oid + "_" + rid);
		PacsHelper ph = null;
		if (StringUtils.isNotBlank(pacs_details)) {
			// 存在缓存，
			ph = (PacsHelper) JSONObject.toBean(
					JSONObject.fromObject(pacs_details), PacsHelper.class);
		} else {
			// 从mogodb数据库中查询
			MongoDBManager manager = MongoDBManager.getInstance();
			Map<String, Object> querymap = new HashMap<String, Object>();
			querymap.put("parent_pacs", rid);
			List<DBObject> dbos = manager.find("pacs_sub_tb", querymap);
			if (dbos != null && dbos.size() > 0) {
				Map<String, List<JSONObject>> picgroups = new TreeMap<String, List<JSONObject>>();
				for (DBObject dbo : dbos) {
					String seriesNum = dbo.get("series_number").toString();
					List<JSONObject> groupjsons = picgroups.get(seriesNum);
					if (groupjsons == null)
						groupjsons = new ArrayList<JSONObject>();
					groupjsons.add(JSONObject.fromObject(dbo));
					picgroups.put(seriesNum, groupjsons);
				}
				ph = new PacsHelper();
				ph.setParentPacs(rid);
				List<PacsHelper> sub_pics = new ArrayList<PacsHelper>();
				for (String snum : picgroups.keySet()) {
					List<JSONObject> _pjsons = picgroups.get(snum);
					List<PacsHelper> datas = new ArrayList<PacsHelper>();
					PacsHelper datas_pac = new PacsHelper();
					datas_pac.setSeriesNumber(snum);
					for (JSONObject _job : _pjsons) {
						PacsHelper _ph = new PacsHelper();
						// _ph.setHttpUrl(_job.getString("http_url"));
						_ph.setStudyId(_job.getString("study_id"));
						_ph.setSeriesId(_job.getString("series_id"));
						_ph.setInstanceId(_job.getString("instance_id"));
						_ph.setSeriesNumber(_job.getString("series_number"));
						datas.add(_ph);
					}
					datas_pac.setSubPics(datas);
					sub_pics.add(datas_pac);
				}
				ph.setSubPics(sub_pics);
				edis.set(oid + "_" + rid, JSONObject.fromObject(ph).toString());
			}
		}
		RedisUtil.returnResource(edis);
		map.put("ph", ph);
		return JSONObject.fromObject(map).toString();
	}

	// 3，保存信息

	// ******************************************************同步模块改版结束*************************************************

	@RequestMapping(value = "/showdetail_record", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> showdetail_record(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> querymap = new HashMap<String, Object>();
		String rid = request.getParameter("rid");
		querymap.put("parent_lis", rid);
		MongoDBManager manager = MongoDBManager.getInstance();
		DBObject fields = manager.findOne("lis_sub_tb", querymap);
		ReSourceBean bean = new ReSourceBean();
		JSONObject jo = JSONObject.fromObject(fields);
		Map<Object, Object> kvs = new HashMap<Object, Object>();
		System.out.println(jo.toString());
		for (Object key : jo.keySet()) {
			kvs.put(key, jo.get(key));
			log.info("===key:" + key + "===value:" + jo.get(key));
		}
		bean.setKvs(kvs);
		map.put("bean", bean);
		return map;
	}

	// 患者基本信息保存
	@RequestMapping(value = "/saveInfos", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveBasicInfo(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 订单id
		// 2==lis信息入库
		log.info("================lis信息入库begin===");
		// lisInfoStorage(oid, request);
		log.info("================lis信息入库end===");
		// 3==pacs信息入库
		log.info("================pacs信息入库begin===");
		// pacsInfoStorage(oid, request);
		log.info("================pacs信息入库end===");
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(oid);
		/**
		 * order.setProgressTag(1);
		 * commonService.updateBusinessVedioOrder(order);
		 */

		// 保存病例记录系统消息，专家查看
		commonManager.saveBusinessMessageInfo_sys(oid, 4, "text", "医生保存病例",
				order.getExpertId(), 2);
		return map;
	}

	// 会诊结果入库
	@RequestMapping(value = "/insertConResult", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> insertConResult(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer orderid = Integer.parseInt(request.getParameter("oid"));
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(orderid);
		String conResult = request.getParameter("conResult");// 诊断意见
		String treatPlan = request.getParameter("treatPlan");// 治疗方案
		String attentions = request.getParameter("attentions");// 注意事项
		order.setConsultationResult(conResult);
		order.setTreatPlan(treatPlan);
		order.setAttentions(attentions);
		order.setReplyTime(new Timestamp(System.currentTimeMillis()));
		commonService.updateBusinessVedioOrder(order);
		// 记录系统消息 医生查看
		commonManager.saveBusinessMessageInfo_sys(orderid, 4, "text",
				"专家填写就诊结果报告", order.getLocalDoctorId(), 3);

		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		String sponsor = user.getDisplayName();

		MobileSpecial userDetail = (MobileSpecial) request.getSession()
				.getAttribute("userDetail");
		// 目标用户订阅组号
		String outsideGroup = getNotifyGroup(order, user.getUserType());
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "reportNotify");
		jObj.put("from", 2);
		jObj.put("orderId", orderid);
		jObj.put("sponsor", sponsor);
		jObj.put("result", conResult);// 诊断意见
		jObj.put("treatPlan", treatPlan);// 治疗方案
		jObj.put("attentions", attentions);// 注意事项
		jObj.put("hosName", userDetail.getHosName());
		jObj.put("depName", userDetail.getDepName());

		goEasy.publish(orderid + "", jObj.toString() + "");
		goEasy.publish(outsideGroup + "", jObj.toString() + "");
		// 记录消息
		MobileSpecial sp = commonService
				.queryMobileSpecialByUserIdAndUserType(order.getExpertId());
		CaseInfo ca = wenzhenService.queryCaseInfoById(order.getCaseId());
		//添加消息附属信息
		Map<String, String> maps = new HashMap<>();
		maps = apiGetuiPushService.setPushPatientExtend(maps, order.getSubUserUuid());
		commonManager.generateSystemPushInfo(21, order.getUuid(), 4,
				order.getLocalDoctorId(), 3,maps,
				sp.getSpecialName() + "专家填写了" + ca.getContactName()
						+ "患者远程会诊订单的就诊报告。");
		return map;
	}

	// 视频通话时间入库
	@RequestMapping(value = "/insertVedioTime", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> insertVedioTime(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer orderid = Integer.parseInt(request.getParameter("oid"));
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(orderid);
		BigInteger times = BigInteger.valueOf(Long.parseLong(request
				.getParameter("times")));
		BigInteger o_time = order.getVedioDur();
		BigInteger newtime = null;
		if (o_time == null) {
			newtime = times;
		} else {
			newtime = BigInteger
					.valueOf(o_time.longValue() + times.longValue());
		}
		order.setVedioDur(newtime);
		commonService.updateBusinessVedioOrder(order);
		return map;
	}

	// 清空已通话时长
	@RequestMapping(value = "/clearVedioTime", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> clearVedioTime(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer orderid = Integer.parseInt(request.getParameter("oid"));
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(orderid);
		order.setVedioDur(null);
		if(order.getStatus().equals(40)){
			order.setStatus(20);
		}
		commonService.updateBusinessVedioOrder(order);
		map.put("status", "success");

		/*
		 * GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		 * JSONObject jObj = new JSONObject(); jObj.put("type",
		 * "clearVedioTime"); jObj.put("orderId", orderid);
		 * goEasy.publish(orderid + "", jObj.toString() + "");
		 */

		return map;
	}

	// 本地图片入库
	/*
	 * private void localPicsStorage(Integer oid, HttpServletRequest request) {
	 * BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(oid);
	 * CaseInfo caseinfo=commonService.queryCaseInfoById(order.getCaseId());
	 * caseinfo.setNormalImages(request.getParameter("picsIds"));
	 * commonService.updateCaseInfo(caseinfo); }
	 */

	// 本地图片上传

	@RequestMapping(value = "/uploadLocalFile", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> uploadLocalFile(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String rootDir=year_sdf.format(new Date())+"/"+month_sdf.format(new Date())+"/"+day_sdf.format(new Date());
		String dirpath = "";
		if (StringUtils.isNotBlank(request.getParameter("orderid"))) {
			Integer orderid = Integer.parseInt(request.getParameter("orderid"));
			dirpath = rootDir + "/" + orderid;
		} else {
			dirpath = rootDir;
		}
		Map<String, Object> ret_map = OSSManageUtil.uploadFile_in(dirpath,
				new OSSConfigure(), request);
		CustomFileStorage cu = new CustomFileStorage();
		cu.setCreateTime(new Timestamp(new Date().getTime()));
		cu.setFileName((String) ret_map.get("filename"));
		cu.setFileUrl((String) ret_map.get("urlpath"));
		cu.setStatus(0);
		Integer cuid = commonService.saveCustomFileStorage(cu);
		map.put("upid", cuid);
		map.put("urlpath", ret_map.get("urlpath"));
		return map;
	}

	/**
	 * 新的上传方法--针对微信端新控件
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadFileNew", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> uploadFileNew(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		File uploadPath = new File(request.getServletContext().getRealPath("upload"));
		OSSConfigure ossConfig = new OSSConfigure();
		String dirpath =year_sdf.format(new Date())+"/"+month_sdf.format(new Date());
		Map<String,Object> ret_map=OSSManageUtil.upload_new(dirpath, new OSSConfigure(), request);
		String str=(String) ret_map.get("filename");
		Integer cuid=null;
		if(videos.contains(str.substring(str.length()-3,str.length()))){
			//视频转码
			String url=(String) ret_map.get("urlpath");
			String[] split = url.split("/");
			String inputObject = split[split.length - 3] + "/" +split[split.length - 2] + "/" + split[split.length - 1];
			String aa =split[split.length-1];
			String s2=aa.split("\\.")[1]; 
			String re=aa.replace("."+s2,""); 
			String outputObject = year_sdf.format(new Date())+"/"+month_sdf.format(new Date())+re;
			JobResult result = VedioTransUtil.transferMedia(inputObject,ossConfig.getVedioBucketName() , outputObject, ossConfig.getOutPutvedioBucketName());
			String urlpath=ossConfig.getAccessUrloutput()+"/"+outputObject+".m3u8";
		    
		    CustomFileStorage cu = new CustomFileStorage();
			cu.setCreateTime(new Timestamp(new Date().getTime()));
			cu.setFileName((String) ret_map.get("filename"));
			cu.setFileUrl((String) ret_map.get("urlpath"));
			cu.setStatus(0);
			cuid = commonService.saveCustomFileStorage(cu);
			map.put("urlpath",urlpath);
		}else{
			CustomFileStorage cu = new CustomFileStorage();
			cu.setCreateTime(new Timestamp(new Date().getTime()));
			cu.setFileName((String) ret_map.get("filename"));
			cu.setFileUrl((String) ret_map.get("urlpath"));
			cu.setStatus(0);
			cuid = commonService.saveCustomFileStorage(cu);
			map.put("urlpath", ret_map.get("urlpath"));
		}
		map.put("upid", cuid);
		map.put("duration", ret_map.get("duration"));
		return map;
	}

	private String gaintime(RemoteConsultation order) {
		String time = "";
		if (StringUtils.isNotBlank(order.getThirdConsultationDate())) {
			time = order.getThirdConsultationDate() + " "
					+ order.getThirdConsultationTime();
		} else if (StringUtils.isNotBlank(order.getSecondConsultationDate())) {
			time = order.getSecondConsultationDate() + " "
					+ order.getSecondConsultationTime();
		} else {
			time = order.getConsultationDate() + " "
					+ order.getConsultationTime();
		}
		return time;
	}

	private void sendsms(RemoteConsultation order, Integer type) {
		if (order.getExpertId() != null) {
			MobileSpecial sepcial = commonService
					.queryMobileSpecialByUserIdAndUserType(order.getExpertId());
			if (type.equals(1)) {
				HospitalDetailInfo hos = weixinService
						.queryHospitalDetailInfoById(order.getLocalHospitalId());
				HospitalDepartmentInfo dep = weixinService
						.queryHospitalDepartmentInfoById(order
								.getLocalDepartId());
				// 六安市立医院的骨科患者何勇斌的病例已准备好
				String content = hos.getDisplayName() + "的"
						+ dep.getDisplayName() + "患者" + order.getPatientName()
						+ "的病例已准备好，并通过\"佰医汇\"平台发送到您的账号，该订单预约时间为"
						+ gaintime(order) + "，为节省时间，请您提前查看病例。【佰医汇】";
				HttpSendSmsUtil.sendSmsInteface(sepcial.getTelphone(), content);
			} else if (type.equals(2)) {
				// 北京协和医院肝胆外科李丽主任医师正在精心查看您的病例，如有病例补充，请提前跟当地医院医生联系
				String content = sepcial.getHosName() + sepcial.getDepName()
						+ sepcial.getSpecialName() + sepcial.getDuty()
						+ "正在精心查看您的病例，如有病例补充，请提前跟当地医院医生联系。【佰医汇】";
				HttpSendSmsUtil.sendSmsInteface(order.getTelephone(), content);
			}
		}
	}

	// 修改进度状态
	@RequestMapping(value = "/postInfoToExpert", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> postInfoToExpert(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		Integer sval = Integer.parseInt(request.getParameter("sval"));
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(oid);
		order.setProgressTag(sval);// 2=发送病例到专家 ,3--有事离开，5--已完成，6--二次就诊，7--三次就诊
		/*
		 * if (sval.equals(2)) { String ret =
		 * ReaderConfigUtil.gainConfigVal(request, "basic.xml",
		 * "root/openorclose"); if (ret.equalsIgnoreCase("1")) { sendsms(order,
		 * 1); } }
		 */
		if (sval.equals(5)) {
			order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey());
			order.setProgressTag(null);
		} else if (sval.equals(6)) {
			order.setProgressTag(null);
			order.setStatus(2);// 待二次下单
		} else if (sval.equals(7)) {
			order.setProgressTag(null);
			order.setStatus(3);// 待三次下单
		}
		commonService.updateBusinessVedioOrder(order);
		map.put("protag", order.getProgressTag());
		if (sval.equals(4) || sval.equals(3) || sval.equals(6)
				|| sval.equals(7)) {
			// 通知对方
			HttpSession session = request.getSession();
			DoctorRegisterInfo user = (DoctorRegisterInfo) session
					.getAttribute("user");
			MobileSpecial userDetail = (MobileSpecial) session
					.getAttribute("userDetail");

			Integer utype = (user.getUserType() == 2) ? 2 : 1;
			String sponsor = user.getDisplayName();
			GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
			JSONObject jObj = new JSONObject();
			jObj.put("type", "progressNotify");
			jObj.put("from", utype);
			jObj.put("orderId", oid);
			jObj.put("progress", sval);
			jObj.put("sponsor", sponsor);
			jObj.put("hosName", userDetail.getHosName());
			jObj.put("depName", userDetail.getDepName());
			String outsideGroup = getNotifyGroup(order, user.getUserType());
			goEasy.publish(oid + "", jObj.toString() + "");
			goEasy.publish(outsideGroup + "", jObj.toString() + "");
		}

		return map;
	}

	// 修改进度状态
	@RequestMapping(value = "/finishBusinessVedioOrder", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> finishBusinessVedioOrder(HttpServletRequest request,HttpSession session)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> maps = new HashMap<>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		Integer orderType=Integer.parseInt(request.getParameter("orderType"));//4--视频 5--图文
		String orderUuid="";
		Integer utype =null;
		Integer recvType=null;
		Integer revcId=null;
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		MobileSpecial userDetail = (MobileSpecial) session
				.getAttribute("userDetail");
		String outsideGroup = "";
		String content = "";
		if(orderType.equals(4)){
			BusinessVedioOrder order = commonService
					.queryBusinessVedioOrderById(oid);
			orderUuid=order.getUuid();
			order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey());
			order.setProgressTag(null);
			maps.put("closerId", String.valueOf(order.getCloserId()));
			maps.put("closerType", String.valueOf(order.getCloserType()));
			commonService.updateBusinessVedioOrder(order);
			utype = (user.getId().equals(order.getExpertId())) ? 2 : 1;
			if (utype == 2) {
				outsideGroup = "doctor_" + order.getLocalDoctorId();
			} else {
				outsideGroup = "expert_" + order.getExpertId();
			}
			recvType = (user.getUserType() == 2) ? 3 : 2;
			revcId = utype == 2 ? order.getLocalDoctorId(): order.getExpertId();
			content = (utype == 2) ? "专家结束视频订单" : "医生结束视频订单";
		}else if(orderType.equals(5)){
			SpecialAdviceOrder adviceOrder=commonService.querySpecialAdviceOrderById(oid);
			orderUuid=adviceOrder.getUuid();
			adviceOrder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey());
			maps.put("closerId", String.valueOf(adviceOrder.getCloserId()));
			maps.put("closerType", String.valueOf(adviceOrder.getCloserType()));
			//添加消息附属信息
			maps = apiGetuiPushService.setPushDoctorExtend(maps, adviceOrder.getExpertId());
			commonService.updateSpecialAdviceOrder(adviceOrder);
			utype = (user.getId().equals(adviceOrder.getExpertId())) ? 2 : 1;
			if (utype == 2) {
				outsideGroup = "doctor_" + adviceOrder.getDoctorId();
			} else {
				outsideGroup = "expert_" + adviceOrder.getExpertId();
			}
			recvType = (user.getUserType() == 2) ? 3 : 2;
			revcId = utype == 2 ?adviceOrder.getDoctorId(): adviceOrder.getExpertId();
			content = (utype == 2) ? "专家结束图文订单" : "医生结束图文订单";
		}
		String sponsor = user.getDisplayName();
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "finishVedioNotify");
		jObj.put("from", utype);
		jObj.put("orderUuid", orderUuid);
		jObj.put("sponsor", sponsor);
		jObj.put("hosName", userDetail.getHosName());
		jObj.put("depName", userDetail.getDepName());
		log.info("===点击完成goeasy推送内容==="+jObj.toString());
		goEasy.publish(orderUuid, jObj.toString() + "");
		goEasy.publish(outsideGroup + "", jObj.toString() + "");	
		//推送聊条消息
		commonManager.saveBusinessMessageInfo_sys_new(orderUuid, orderType, "RC:TxtMsg", content,
						revcId, recvType);
		
		//推送系统消息
		commonManager.generateSystemPushInfo(21,orderUuid, orderType,
						revcId, recvType,maps, content);
		return map;
	}

	@RequestMapping(value = "/gainConfig", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainToken(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String ret = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
				"root/openorclose");
		map.put("ret", ret);
		return map;
	}

	// 图文问诊，加急电话
	@RequestMapping(value = "/graphics", method = RequestMethod.GET)
	public ModelAndView graphics(HttpServletRequest request) {
		return new ModelAndView("common/graphics");
	}

	@RequestMapping(value = "/removeRoomUser", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> removeRoomUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Integer roomKey = Integer.parseInt(request.getParameter("orderid"));
		String uid = request.getParameter("uid");
		Map<String, Object> map = new HashMap<String, Object>();

		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(roomKey);
		System.out.print("======removeRoomUser:[roomKey:" + roomKey + ";user:"
				+ uid + "]");

		if (WebRTCRoomManager.isUserExist(roomKey.toString(), uid)) {
			WebRTCRoomManager.removeUser(roomKey.toString(), uid);
			map.put("status", "success");

		} else {
			map.put("status", "error");
			map.put("message", "用户【room:" + roomKey + ",user:" + uid
					+ "]没有找到此用户");
			System.out.print("\r\n=====removeRoomUser:" + "【room:" + roomKey
					+ ";user:" + uid + "]没有找到此用户");
		}

		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Integer utype = (user.getUserType() == 2) ? 2 : 1;
		if (utype.equals(1)) {
			commonManager.saveBusinessMessageInfo_sys(roomKey, 4, "text",
					"医生退出房间", order.getExpertId(), 2);
		} else if (utype.equals(2)) {
			commonManager.saveBusinessMessageInfo_sys(roomKey, 4, "text",
					"专家退出房间", order.getLocalDoctorId(), 3);
		}
		Integer _hasuser = WebRTCRoomManager.getUserNum(roomKey.toString());
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "cancelNotify");
		jObj.put("from", utype);
		jObj.put("orderId", roomKey);
		jObj.put("hasuser", _hasuser);
		String outsideGroup = getNotifyGroup(order, user.getUserType());
		goEasy.publish(roomKey + "", jObj.toString() + "");
		goEasy.publish(outsideGroup + "", jObj.toString() + "");

		return map;
	}

	/**
	 * App 测试使用
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/removeRoomUser_test", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> removeRoomUser_test(HttpServletRequest request) {

		Integer roomKey = Integer.parseInt(request.getParameter("orderid"));
		String uid = request.getParameter("uid").toString();

		Map<String, Object> map = new HashMap<String, Object>();

		System.out.print("======removeRoomUser_test:[roomKey:" + roomKey
				+ ";user:" + uid + "]");

		if (WebRTCRoomManager.isUserExist(roomKey.toString(), uid)) {
			WebRTCRoomManager.removeUser(roomKey.toString(), uid);
			map.put("status", "success");
		} else {
			map.put("status", "error");
			map.put("message", "【room:" + roomKey + ";user:" + uid + "]没有找到此用户");
			System.out.print("\r\n=====removeRoomUser_test:" + "【room:"
					+ roomKey + ";user:" + uid + "]没有找到此用户");
		}

		return map;
	}

	/**
	 * App 测试使用
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/removeAllRoom_test", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> removeAllRoom_test(HttpServletRequest request) {

		WebRTCRoomManager.clear();
		Map<String, Object> map = new HashMap<String, Object>();

		return map;
	}

	@RequestMapping(value = "/addRoomUser", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> addRoomUser(HttpServletRequest request) {
		HttpSession session = request.getSession();

		Integer roomKey = Integer.parseInt(request.getParameter("orderid"));
		String uid = request.getParameter("uid");
		Integer utype = Integer.parseInt(request.getParameter("utype"));
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(roomKey);
		Map<String, Object> map = new HashMap<String, Object>();
		// String uid = session.getId();
		// Integer initiator = 0;
		if (utype.equals(1)) {
			commonManager.saveBusinessMessageInfo_sys(roomKey, 4, "text",
					"医生进入房间", order.getExpertId(), 2);
		} else if (utype.equals(2)) {
			commonManager.saveBusinessMessageInfo_sys(roomKey, 4, "text",
					"专家进入房间", order.getLocalDoctorId(), 3);
		}

		if (WebRTCRoomManager.getRoom(roomKey.toString()) == null) {
			if (!WebRTCRoomManager.canCreateRoom()) {
				map.put("status", "error");
				map.put("message", "不能创建通话房间，超过最大创建数量！");
				System.out.print("\r\n=====addRoomUser:不能创建通话房间，超过最大创建数量！");
				return map;
			}
		}

		// 如果 房间用户已满，并且此用户 不在此房间中
		if (WebRTCRoomManager.getUserNum(roomKey.toString()) >= 2
				&& !WebRTCRoomManager.isUserExist(roomKey.toString(), uid)) {
			map.put("status", "error");
			map.put("message", "对不起房间已经人满，你不能加入！");
			System.out.print("\r\n=====addRoomUser:对不起房间已经人满，你不能加入！");
			return map;
		}
		// 不允许医生首先进入
		if (WebRTCRoomManager.getUserNum(roomKey.toString()) == 0 && utype == 1) {
			map.put("status", "error");
			map.put("message", "对不起你不能加入,专家还没有进入房间，需要专家首先进入！");
			System.out
					.print("\r\n=====addRoomUser:对不起你不能加入,专家还没有进入房间，需要专家首先进入");
			return map;
		}
		boolean bExist = WebRTCRoomManager.isTypeUserExist(roomKey.toString(),
				utype);

		if (bExist) {

			if (utype == 2) {
				// 如果是专家重新发起，则踢掉之前的连接
				WebRTCRoomManager.removeRoom(roomKey.toString());
				System.out.print("\r\n=====addRoomUser:"
						+ "此房间已经有专家进入，需要强制踢掉其他用户");

			} else {
				String msage = (utype == 1 ? "已经有医生" : "已经有专家" + "进入此房间！");
				map.put("status", "error");
				map.put("message", msage);
				System.out.print("\r\n=====addRoomUser:" + msage);
				return map;
			}
		}
		map.put("vedioDur", order.getVedioDur());

		WebRTCRoomManager.addUser(roomKey.toString(), uid, utype);
		MobileSpecial userDetail = (MobileSpecial) session
				.getAttribute("userDetail");

		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");

		String sponsor = user.getDisplayName();
		// 目标用户订阅组号
		String outsideGroup = getNotifyGroup(order, user.getUserType());

		Integer _hasuser = WebRTCRoomManager.getUserNum(roomKey.toString());
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));

		JSONObject jObj = new JSONObject();
		jObj.put("type", "launchNotify");
		jObj.put("from", utype);
		jObj.put("orderId", roomKey);
		jObj.put("sponsor", sponsor);
		jObj.put("hasuser", _hasuser);

		jObj.put("hosName", userDetail.getHosName());
		jObj.put("depName", userDetail.getDepName());

		goEasy.publish(roomKey + "", jObj.toString() + "");
		goEasy.publish(outsideGroup + "", jObj.toString() + "");

		// map.put("_uid", session.getId());

		return map;
	}

	/**
	 * App测试使用
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addRoomUser_test", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> addRoomUser_test(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Integer roomKey = Integer.parseInt(request.getParameter("orderid"));
		Integer utype = Integer.parseInt(request.getParameter("utype"));

		/*
		 * RemoteConsultation order = weixinService
		 * .queryRemoteConsultationById(roomKey);
		 */

		Map<String, Object> map = new HashMap<String, Object>();
		String uid = session.getId();
		Integer initiator = 0;
		if (WebRTCRoomManager.getRoom(roomKey.toString()) == null) {
			if (!WebRTCRoomManager.canCreateRoom()) {
				map.put("status", "error");
				map.put("message", "不能创建通话房间，超过最大创建数量！");
				System.out.print("\r\n=====addRoomUser:不能创建通话房间，超过最大创建数量！");
				return map;
			}
		}

		// 如果 房间用户已满，并且此用户 不在此房间中
		if (WebRTCRoomManager.getUserNum(roomKey.toString()) >= 2
				&& !WebRTCRoomManager.isUserExist(roomKey.toString(), uid)) {
			map.put("status", "error");
			map.put("message", "对不起房间已经人满，你不能加入！");
			System.out.print("\r\n=====addRoomUser:对不起房间已经人满，你不能加入！");
			return map;
		}

		if (WebRTCRoomManager.isUserExist(roomKey.toString(), uid)) {
			// map.put("status", "error");
			// map.put("message", "您已经进入房间，不能重复进入");
			System.out.print("\r\n=====addRoomUser:【room:" + roomKey.toString()
					+ ";user:" + uid + "】您已经进入房间，不要重复进入！");
			// return map;
		} else {
			WebRTCRoomManager.addUser(roomKey.toString(), uid, utype);
		}
		initiator = WebRTCRoomManager.getUserNum(roomKey.toString()) > 1 ? 1
				: 0;
		System.out.print("\r\n======initiator:" + initiator);

		map.put("_initiator", initiator);
		map.put("_uid", session.getId());

		return map;
	}

	@RequestMapping(value = "/isPeerPresence", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> isPeerPresence(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		Integer userId = Integer.parseInt(request.getParameter("userId"));
		Integer userType = Integer.parseInt(request.getParameter("userType"));
		Integer orderId = Integer.parseInt(request.getParameter("orderId"));

		List<UserClient> otherUsers = WebRTCRoomManager_new.getOtherUsers(
				orderId.toString(), userId.toString(), userType);
		// 在线
		if (otherUsers != null && otherUsers.size() > 0) {
			UserClient client = otherUsers.get(0);

			MobileSpecial mobileSpecial = commonService
					.queryMobileSpecialByUserIdAndUserType(Integer
							.parseInt(client.getUserId()));

			map.put("result", "true");
			map.put("peer", mobileSpecial);
			// 不在先
		} else {
			// 获取订单信息
			BusinessVedioOrder order = commonService
					.queryBusinessVedioOrderById(orderId);
			if (order != null) {
				if (userType == 2 && order.getLocalDoctorId() != null) {
					MobileSpecial mobileSpecial = commonService
							.queryMobileSpecialByUserIdAndUserType(order
									.getLocalDoctorId());
					map.put("peer", mobileSpecial);

				} else if (userType != 2 && order.getExpertId() != null) {
					MobileSpecial mobileSpecial = commonService
							.queryMobileSpecialByUserIdAndUserType(order
									.getExpertId());
					map.put("peer", mobileSpecial);
				}
			}

			map.put("result", "false");
		}

		return map;
	}

	@RequestMapping(value = "/isUserhasOrder", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> isUserHavsOrder(HttpServletRequest request)
			throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		Integer userId = Integer.parseInt(request.getParameter("userId"));
		Integer utype = Integer.parseInt(request.getParameter("utype"));
		Integer orderId = Integer.parseInt(request.getParameter("orderId"));
		Integer progress = Integer.parseInt(request.getParameter("progress"));

		System.out.print("\r\n========isUserHavsOrder:userId" + userId
				+ ",utype" + utype + ",orderId" + orderId + ",progress"
				+ progress);
		Boolean result = false;
		if (utype == 1) {
			result = commonService.isDoctorHasOrder(userId, orderId, progress,
					1);
		} else {

			result = commonService.isExpertHasOrder(userId, orderId, progress);
		}

		map.put("result", result);
		return map;
	}

	// 此处的 userType 与 uType 不同
	private String getNotifyGroup_old(RemoteConsultation order, Integer userType) {

		// 目标用户订阅组号
		String outsideGroup = "outside"; // 目的用户组 的用户类型 + 医院 + 科室 (登陆用户后就顶下来了)

		if (userType == 2) {
			// 当前为专家用户 ,发送给 医生放
			outsideGroup = outsideGroup + "1" + order.getLocalHospitalId()
					+ order.getLocalDepartId();
		} else {
			// 当前为医生用户 获取目标专家信息
			/*
			 * MobileSpecial mobileSpecial = commonService
			 * .queryMobileSpecialByExpertId(order.getExpertId());
			 */
			MobileSpecial mobileSpecial = commonService
					.queryMobileSpecialByUserIdAndUserType(order.getExpertId());
			outsideGroup = outsideGroup + "2" + mobileSpecial.getHosId()
					+ mobileSpecial.getDepId();
		}

		System.out.print("\r\n=======outsideGroup:" + outsideGroup);

		return outsideGroup;
	}

	// 推送给 订单 用户
	private String getNotifyGroup(BusinessVedioOrder order, Integer userType) {

		String outsideGroup = "";
		if (userType == 2) {
			outsideGroup = "doctor_" + order.getLocalDoctorId();
		} else {
			outsideGroup = "expert_" + order.getExpertId();
		}
		System.out.print("\r\n=======outsideGroup:" + outsideGroup);

		return outsideGroup;
	}

	// 弹出的教学视频-屏幕分享
	@RequestMapping(value = "/popscreenshare", method = RequestMethod.GET)
	public ModelAndView popscreenshare(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		map.put("_roomKey", oid);
		map.put("_user", request.getSession().getId());

		System.out.print("\r\n=======popscreenshare:" + oid);
		return new ModelAndView("common/screenshare", map);
	}

	@RequestMapping(value = "/closeshare", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> closeshare(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String oid = request.getParameter("orderid");
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		goEasy.publish(oid + "_close", "close");
		return map;
	}

	// 邀请专家再次分享
	@RequestMapping(value = "/callforshare", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> callforshare(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer orderId = Integer.parseInt(request.getParameter("orderid"));
		BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(orderId);
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		String sponsor = user.getDisplayName();
		MobileSpecial userDetail = (MobileSpecial) request.getSession()
				.getAttribute("userDetail");

		JSONObject jObj = new JSONObject();
		jObj.put("type", "share");
		jObj.put("orderId", orderId);
		jObj.put("sponsor", sponsor);
		jObj.put("hosName", userDetail.getHosName());
		jObj.put("depName", userDetail.getDepName());

		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		goEasy.publish(order.getUuid(), jObj.toString() + "");
		return map;
	}

	@RequestMapping(value = "/testgo", method = RequestMethod.GET)
	public void testgo(HttpServletRequest request) throws Exception {
		for (int i = 0; i < 100; i++) {
			Thread.sleep(1500);
			GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
			JSONObject jObj = new JSONObject();
			jObj.put("type", "sync");
			jObj.put("from", 1);
			jObj.put("orderId", 1);
			jObj.put("pro", i);
			goEasy.publish("1", jObj.toString() + "");
		}
	}

	@RequestMapping(value = "/testgo2", method = RequestMethod.GET)
	public ModelAndView testgo2(HttpServletRequest request) throws Exception {
		return new ModelAndView("common/test");
	}

	@RequestMapping(value = "/upblob", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> upblob(HttpServletRequest request,
			@PathVariable MultipartFile file) throws Exception {
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		RemoteConsultation order = weixinService
				.queryRemoteConsultationById(oid);
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(order.getExpertId());
		// 患者姓名-专家名称-时间格式
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("===开始上传==");
		Map<String, String> retmap = OSSManageUtil.uploadInputStream(
				file.getInputStream(), new OSSConfigure(), order, special);
		log.info("===上传结束==");
		String returl = retmap.get("returl");
		String filename = retmap.get("filename");
		CustomFileStorage cu = new CustomFileStorage();
		cu.setCreateTime(new Timestamp(new Date().getTime()));
		cu.setFileName(filename);
		cu.setFileUrl(returl);
		cu.setOrderId(oid);
		cu.setStatus(1);
		commonService.saveCustomFileStorage(cu);

		// /转码为MP4并上传
		// WebmToMp4Util.setCommonService(commonService);
		// String localDir =
		// request.getServletContext().getRealPath("webmtemp");
		// WebmToMp4Util.convert(returl, localDir,
		// oid,order.getPatientName(),special.getSpecialName());
		// /转码为MP4并上传

		return map;
	}

	@RequestMapping(value = "/upblobslice", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> upblobslice(HttpServletRequest request,
			@PathVariable MultipartFile file) throws Exception {
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		String filename = request.getParameter("filename");
		Long position = Long.parseLong(request.getParameter("position"));
		Integer end = Integer.parseInt(request.getParameter("end"));
		System.out.print("\r\n=================upblobslice:[oid:" + oid
				+ ",filename:" + filename + ",position:" + position + ",end:"
				+ end + "]");

		RemoteConsultation order = weixinService
				.queryRemoteConsultationById(oid);
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(order.getExpertId());
		// 患者姓名-专家名称-时间格式
		Map<String, Object> map = new HashMap<String, Object>();
		// 没有结束
		if (end.equals(0)) {
			map = OSSManageUtil.uploadInputStreamAppend(order, special,
					file.getInputStream(), new OSSConfigure(), filename,
					position);
		} else if (end.equals(1) || StringUtils.isNotBlank(filename)) {
			// 结束了 并且有数据
			if (end.equals(1)) {
				Map<String, Object> tempmap = OSSManageUtil
						.uploadInputStreamAppend(order, special,
								file.getInputStream(), new OSSConfigure(),
								filename, position);
			}
			// 结束了
			map.put("nextPosition", 0L);
			map.put("filename", "");

			OSSConfigure ossConfig = new OSSConfigure();
			CustomFileStorage cu = new CustomFileStorage();
			cu.setCreateTime(new Timestamp(new Date().getTime()));
			cu.setFileName(filename.substring(filename.lastIndexOf("/") + 1));
			cu.setFileUrl(ossConfig.getAccessUrl() + "/" + filename);
			cu.setOrderId(oid);
			cu.setStatus(1);
			commonService.saveCustomFileStorage(cu);
			// /转码为MP4并上传
			// WebmToMp4Util.setCommonService(commonService);
			// String localDir =
			// request.getServletContext().getRealPath("webmtemp");
			// WebmToMp4Util.convert(ossConfig.getAccessUrl() + "/" + filename,
			// localDir, oid,order.getPatientName(),special.getSpecialName());
			// /转码为MP4并上传
		} else {
			map.put("nextPosition", 0L);
			map.put("filename", "");
		}

		System.out.print("\r\n==================upblobslice:"
				+ JSONObject.fromObject(map).toString());
		return map;
	}

	// 个人信息显示
	@RequestMapping(value = "/showbasicinfo", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> showbasicinfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		if(user !=null ){
			MobileSpecial special = commonService
					.queryMobileSpecialByUserIdAndUserType(user.getId());
			map.put("special", special);
		}
		return map;
	}

	@RequestMapping(value = "/updatepass")
	public @ResponseBody
	Map<String, Object> updatepass(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String oldpass = request.getParameter("oldpass");
		String newass = request.getParameter("newpass");
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		DoctorRegisterInfo _user = commonService
				.queryDoctorRegisterInfoById(user.getId());
		if (PasswordUtil.MD5Salt(oldpass,_user.getSalt()).equalsIgnoreCase(_user.getPassword())) {
			map.put("status", "success");
			_user.setPassword(PasswordUtil.MD5Salt(newass,_user.getSalt()));
			commonService.updateDoctorRegisterInfo(_user);
		} else {
			map.put("status", "error");
		}
		return map;
	}

	// pacs影像高级检索
	@RequestMapping(value = "/pacsadvance")
	public @ResponseBody
	Map<String, Object> pacsadvance(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String orderid = request.getParameter("orderid");
		String patientName = request.getParameter("patientName");
		String checkNo = request.getParameter("checkNo");
		String checkType = request.getParameter("checkType");
		String patientId = request.getParameter("patientId");
		String mzNumber = request.getParameter("mzNumber");
		String time = request.getParameter("regtime");
		List<ReSourceBean> beans = PythonVisitUtil.imageadvance(orderid,
				patientName, checkNo, checkType, patientId, mzNumber, time);
		map.put("pacbeans", beans);
		return map;
	}

	// 选择同步pacs影像信息
	@RequestMapping(value = "/syncpacsadvance", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> syncpacsadvance(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String orderid = request.getParameter("oid");// 订单id
		String imagedirs = request.getParameter("imagedirs");// 目录串
		String synctype = request.getParameter("syncType");// 同步类型(追加0,覆盖1)
		Map<String, List<String>> groupdirs = gainimagedirs(imagedirs, orderid,
				synctype);
		String syncSeries = request.getParameter("syncSeries");
		String dir = request.getServletContext().getRealPath("dcmtemp");
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(Integer.parseInt(orderid));
		// 消息记录
		commonManager.saveBusinessMessageInfo_sys(Integer.parseInt(orderid), 4,
				"text", "医生同步患者影像报告数据", order.getExpertId(), 2);
		List<ReSourceBean> pacsbeans = PythonVisitUtil.imagesex_advance(
				orderid, groupdirs, syncSeries, request, dir, synctype,
				String.valueOf((order.getCaseId())));
		log.info("***********同步pacs数据结束****************");
		map.put("pacsbeans", LoadLisAndPacsDataUtil.gainPacsGroup(pacsbeans));
		return map;
	}

	private Map<String, List<String>> gainimagedirs(String imagedirs,
			String orderid, String synctype) throws Exception {
		Map<String, List<String>> groupdirs = new HashMap<String, List<String>>();

		if (StringUtils.isNotBlank(imagedirs)) {
			String[] _ims = imagedirs.split(";");
			if (_ims != null && _ims.length > 0) {
				MongoDBManager mongo = MongoDBManager.getInstance();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("admin_order_id", orderid);
				if (synctype.equalsIgnoreCase("1")) {
					// 覆盖
					// 删除mongodb数据库中所有的相关pacs影像
					for (String dir : _ims) {
						String[] _ds = dir.split(":");
						map.put("Image_Directory", _ds[1]);
						mongo.delete("pacs_tb", map);
						List<String> imags = groupdirs.get(_ds[0]);
						if (imags == null)
							imags = new ArrayList<String>();
						imags.add(_ds[1]);
						groupdirs.put(_ds[0], imags);
					}
				} else if (synctype.equalsIgnoreCase("0")) {
					// 追加
					for (String dir : _ims) {
						String[] _ds = dir.split(":");
						if (_ds != null && _ds.length > 1) {
							map.put("Image_Directory", _ds[1]);
							DBObject dbo = mongo.findOne("pacs_tb", map);
							if (dbo == null) {
								List<String> imags = groupdirs.get(_ds[0]);
								if (imags == null)
									imags = new ArrayList<String>();
								imags.add(_ds[1]);
								groupdirs.put(_ds[0], imags);
							}
						}
					}
				}
			}
		}
		return groupdirs;
	}

	// 修改图片或视频的文件名
	@RequestMapping(value = "/updatefname", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> updatefname(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer fid = Integer.parseInt(request.getParameter("fid"));
		String filename = request.getParameter("filename");
		CustomFileStorage stor = commonService.queryCustomFileStorage(fid);
		stor.setFileName(filename);
		commonService.updateCustomFileStorage(stor);
		return map;
	}

	/**
	 * 手动上传DCM影像图片
	 * 
	 * @param request
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updcmfile", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> updcmfile(HttpServletRequest request,
			@PathVariable MultipartFile file) throws Exception {
		Integer orderType=Integer.parseInt(request.getParameter("otype"));//4--视频 5--专家咨询  10--预约转诊  -1---病例影像
		System.out.println("==="+request.getQueryString());
		String order_id = "";
		String order_uuid="";
		String case_id = "";
		if (orderType.equals(5)) {
			// 专家咨询
			Integer oid = Integer.parseInt(request.getParameter("oid"));
			SpecialAdviceOrder order = commonService
					.querySpecialAdviceOrderById(oid);
			order_id = order.getUuid();
			order_uuid=order.getUuid();
			CaseInfo caseInfo = commonService.queryCaseInfoById(order.getCaseId());
			case_id = caseInfo.getUuid();
		}else if (orderType.equals(10)) {
			Integer oid=Integer.parseInt(request.getParameter("oid"));
			BusinessD2dReferralOrder referralOrder=d2pService.queryd2dreferralOrderbyId(oid);
			order_id=referralOrder.getUuid();
			order_uuid=referralOrder.getUuid();
			CaseInfo caseInfo = commonService.queryCaseInfoById(referralOrder.getCaseId());
			case_id = caseInfo.getUuid();
		}else if (orderType.equals(-1)){
			case_id = request.getParameter("caseUuid");//病例uuid
			order_uuid = case_id;//goeasy 使用case uuid
		}else {
			// 远程订单
			Integer oid = Integer.parseInt(request.getParameter("oid"));
			order_id = oid.toString();
			BusinessVedioOrder order = commonService
					.queryBusinessVedioOrderById(oid);
			order_uuid=order.getUuid();
			CaseInfo caseInfo = commonService.queryCaseInfoById(order.getCaseId());
			case_id = caseInfo.getUuid();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("=====本地保存开始====");
		String filePath = request.getSession().getServletContext()
				.getRealPath("/")
				+ "upload/" + file.getOriginalFilename();
		file.transferTo(new File(filePath));
		log.info("=====本地保存结束====");
		Map<String, Object> retmap = parseDicom(filePath);
		if (retmap != null && retmap.size() > 0) {
			MongoDBManager manage = MongoDBManager.getInstance();
			Map<String, Object> query = new HashMap<String, Object>();
			if(orderType.equals(-1)) {
				query.put("admin_case_id", case_id);
			}else {
				query.put("admin_order_id", order_id);
			}
			query.put("patient_id", retmap.get("patient_id"));
			query.put("study_id", retmap.get("study_id"));
			DBObject dbo = manage.findOne("pacs_tb", query);
			boolean b = true;
			String _id = "";
			String type = "";
			if (dbo == null) {
				if(!orderType.equals(-1))
				retmap.put("admin_order_id", order_id);
				retmap.put("admin_case_id", case_id);
				// 入库一组影像数据--创建新的模块
				_id = manage.insertmap("pacs_tb", retmap);
				log.info("==="+order_id+":"+_id);
				type = "1";
				//sendmessage(order_uuid, retmap, "1", _id);
				// 更新缓存
			} else {
				// 已有检查数据存在，追加序列数据
				//sendmessage(order_uuid, retmap, "0", "");
				type = "0";
				b = false;
			}
			uploadtopacs(filePath);
			if (b && !orderType.equals(-1)) {
				updatepacscache(orderType.equals(4)?order_id:order_uuid, retmap, _id);
			}
			sendmessage(order_uuid, retmap, type, _id);
		}
		return map;
	}

	// 上传完毕时需要填写项目名称
	@RequestMapping(value = "/compleupcheckitem", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> compleupcheckitem(HttpServletRequest request)
			throws Exception {
		String oid = request.getParameter("oid");
		Map<String, Object> map = new HashMap<String, Object>();
		String pid = request.getParameter("pid");// mongodb数据库中pacs_tb _id
		String itemname = request.getParameter("itemname");// 检查项目
		MongoDBManager manage = MongoDBManager.getInstance();
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("_id", new ObjectId(pid));
		DBObject dbo = manage.findOne("pacs_tb", query);
		dbo.put("Check_Item_E", itemname);
		manage.insertupdate("pacs_tb", dbo);
		// 跟新缓存中的数据
		if(StringUtils.isNotBlank(oid)) {
			String cacheName = oid + "_pacs";
			Jedis edis = RedisUtil.getJedis();
			String pacs_cache = edis.get(cacheName);
			JSONArray response = JSONArray.fromObject(pacs_cache);
			Map<String, Class> classMap = new HashMap<String, Class>();
			classMap.put("beans", ReSourceBean.class);
			List<ReSourceBean> _beans = JSONArray.toList(response,
					ReSourceBean.class, classMap);
			for (ReSourceBean _bean : _beans) {
				if (_bean.getKey().equals(pid)) {
					Map<Object, Object> kvmap = _bean.getKvs();
					kvmap.put("Check_Item_E", itemname);
					_bean.setKvs(kvmap);
					break;
				}
			}
			log.info("===缓存："+cacheName);
			edis.set(cacheName, JSONArray.fromObject(_beans).toString());
			RedisUtil.returnResource(edis);
		}
		return map;
	}

	// 创建新模块时更新缓存
	private void updatepacscache(String oid, Map<String, Object> map, String _id) {
		String cacheName = oid + "_pacs";
		Jedis edis = RedisUtil.getJedis();
		String pacs_cache = edis.get(cacheName);
		List<ReSourceBean> beans = new ArrayList<ReSourceBean>();
		ReSourceBean bean = new ReSourceBean();
		bean.setKey(_id);
		bean.setStudyId(map.get("study_id") == null ? "" : map.get("study_id")
				.toString());
		bean.setSeriesId(map.get("series_id") == null ? "" : map.get(
				"series_id").toString());
		bean.setInstanceId(map.get("instance_id") == null ? "" : map.get(
				"instance_id").toString());
		Map<Object, Object> kvsmap = new HashMap<Object, Object>();
		for (String key : map.keySet()) {
			if (!map.get(key).toString().equalsIgnoreCase("null"))
				kvsmap.put(key, map.get(key));
		}
		bean.setKvs(kvsmap);
		if (StringUtils.isNotBlank(pacs_cache)
				&& !pacs_cache.equalsIgnoreCase("[]")) {
			JSONArray response = JSONArray.fromObject(pacs_cache);
			if (response != null && response.size() > 0) {
				Map<String, Class> classMap = new HashMap<String, Class>();
				classMap.put("beans", ReSourceBean.class);
				List<ReSourceBean> _beans = JSONArray.toList(response,
						ReSourceBean.class, classMap);
				beans.addAll(_beans);
			}
		}
		beans.add(bean);
		edis.set(cacheName, JSONArray.fromObject(beans).toString());
		RedisUtil.returnResource(edis);
	}

	private void sendmessage(String orderid, Map<String, Object> map,
			String type, String _id) {
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "updcm");
		jObj.put("orderId", orderid);
		if (type.equalsIgnoreCase("1")) {
			jObj.put("status", "new");
			jObj.put(
					"dcmurl",
					map.get("patient_id").toString() + "|"
							+ map.get("study_id").toString() + "|"
							+ map.get("series_id").toString() + "|"
							+ map.get("instance_id").toString());
			jObj.put("pid", _id);
			jObj.put("Modality", map.get("Modality").toString());
			jObj.put("REPORT_DATE", map.get("REPORT_DATE").toString());
		} else {
			jObj.put("status", "old");
		}
		log.info("===objjson+++:"+jObj.toString());
		goEasy.publish(orderid + "", jObj.toString());
	}

	private Map<String, Object> parseDicom(String localpath) {
		log.info("=====影像解析开始====");
		Map<String, Object> retmap = DicomLoadUtil.LoadDicomObject(new File(
				localpath));
		log.info("=====影像解析结束====");
		return retmap;
	}

	private void uploadtopacs(String filePath) {
		log.info("===上传影像到pacs系统开始====");
		UploadDcmUtil.uploadSingleDcm(filePath);
		if (StringUtils.isNotBlank(filePath)) {
			File output = new File(filePath);
			if (output.exists()) {
				System.out.println("====删除成功么？===" + output.delete());
			}
		}
		log.info("===上传影像到pacs系统结束====");
	}

	@RequestMapping(value = "/redit", method = RequestMethod.GET)
	public String redit(HttpServletRequest request) {
		return "redirect:http://192.168.1.104:8080/doctor/testredit";
	}

	@RequestMapping(value = "/testredit", method = RequestMethod.GET)
	public ModelAndView testredit(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie cookie = new Cookie("JSESSIONID", request.getSession().getId());
		response.addCookie(cookie);
		return new ModelAndView("admin/test");
	}

	// 图文结束
	@RequestMapping(value = "/closetw", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> closetw(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 图文问诊订单id
		Integer utype = Integer.parseInt(request.getParameter("utype"));// 医生-2、患者-1、客服-0
		BusinessTuwenOrder order = wenzhenService
				.queryBusinessTuwenInfoById(oid);
		order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey());
		order.setClosedTime(new Timestamp(System.currentTimeMillis()));
		if (utype.equals(1)) {
			order.setCloserId(order.getUserId());
		} else if (utype.equals(2)) {
			order.setCloserId(order.getDoctorId());
		}
		order.setCloserType(utype);
		wenzhenService.updateBusinessTuwenOrder(order);
		return map;
	}

	@RequestMapping(value = "/closetel", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> closetel(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 电话问诊订单id
		Integer stype = Integer.parseInt(request.getParameter("ctype"));
		BusinessTelOrder order = wenzhenService.queryBusinessTelOrderById(oid);
		if (stype.equals(3)) {
			Integer utype = Integer.parseInt(request.getParameter("utype"));// 用户类型
			Integer uid = Integer.parseInt(request.getParameter("uid"));
			order.setCloserId(uid);
			order.setCloserType(utype);
		}
		order.setStatus(stype);
		wenzhenService.updateBusinessTelOrder(order);
		return map;
	}

	/**
	 * 获取医生的评价
	 */
	@RequestMapping(value = "/doceval", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> doceval(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer docid = Integer.parseInt(request.getParameter("docid"));
		Integer _pageNo = 1;
		Integer _pageSize = 10;
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSie");
		if (StringUtils.isNotBlank(pageSize)) {
			_pageSize = Integer.parseInt(pageSize);
		}
		if (StringUtils.isNotBlank(pageNo)) {
			_pageNo = Integer.parseInt(pageNo);
		}
		List<AppraisementDoctorInfo> appraises = wenzhenService
				.queryAppraisementDoctorInfosByDoc(docid, _pageNo, _pageSize);
		for (AppraisementDoctorInfo _appraise : appraises) {
			_appraise.setTimeStr(sdf.format(_appraise.getCreateTime()));
			List<AppraisementImpressionDoctor> tags = wenzhenService
					.queryAppraisementImpressionDoctorsByDoc(docid,
							_appraise.getId());
			_appraise.setTags(tags);
		}
		map.put("appriases", appraises);
		return map;
	}

	/**
	 * 标签统计
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/caltags", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> caltags(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer docid = Integer.parseInt(request.getParameter("docid"));
		List<AppraisementImpressionDictionary> tags = wenzhenService
				.queryCalculateTagsNumByDoc(docid);
		map.put("tags", tags);
		return map;
	}

	/**
	 * 语音合成
	 */
	@RequestMapping(value = "/generateAudio", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> genereateAudio(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String content = request.getParameter("content");
		BaiduAudioToken token = commonService.queryBaiduAudioTokenById(1);
		String access_token = "";
		// 判断token是否失效 判断当前日期是否在最新生成时间的一个月内
		boolean isable = telltoken(token.getNewGenerateDate());
		if (!isable) {
			// 重新生成token入库
			access_token = generateTokenNew();
			token.setAccessToken(access_token);
			token.setNewGenerateDate(_format.format(new Date()));
			commonService.updateBaiduAudioToken(token);
		} else {
			access_token = token.getAccessToken();
		}
		StringBuilder url = new StringBuilder(
				"http://tsn.baidu.com/text2audio?tex=" + content);
		url.append("&lan=zh&");
		url.append("cuid=" + gainLocalIp() + "&ctp=1&");
		url.append("tok=" + access_token);
		map.put("url", url);
		return map;
	}

	private String generateTokenNew() {
		StringBuilder url = new StringBuilder(
				"https://openapi.baidu.com/oauth/2.0/token?");
		url.append("grant_type=client_credentials&");
		url.append("client_id=G2arqvNrzxNZSjG8n1Cv4r2a&");
		url.append("client_secret=5a8f5be62001aa7e9ec541f2f3dc25e5");
		JSONObject obj = CommonUtil.httpRequest(url.toString(), "GET", null);
		return obj.getString("access_token");
	}

	@SuppressWarnings("static-access")
	private String gainLocalIp() {
		InetAddress ia = null;
		String localip = "";
		try {
			ia = ia.getLocalHost();
			localip = ia.getHostAddress();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localip;
	}

	private boolean telltoken(String generatDate) throws Exception {
		String now = _format.format(new Date());
		Calendar c = Calendar.getInstance();
		c.setTime(_format.parse(generatDate));
		c.add(Calendar.MONTH, 1);
		String newdate = _format.format(c.getTime());
		if (now.compareTo(newdate) > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 保存聊天消息 访问：http://localhost:8080/doctor/saveMessage
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveMessage")
	@ResponseBody
	public Map<String, Object> saveMessage(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 订单id
		Integer otype = Integer.parseInt(request.getParameter("otype"));// 远程：4 专家咨询：5
		String uuid="";
		if(otype.equals(4)){
			BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(oid);
			uuid=order.getUuid();
		}else if(otype.equals(5)){
			SpecialAdviceOrder border=commonService.querySpecialAdviceOrderById(oid);
			uuid=border.getUuid();
		}
		String msgContent = request.getParameter("msgContent");// 消息内容
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		if (user != null) {
			if (user.getUserType().equals(2)) {
				commonManager.saveBusinessMessageInfo_exp(oid, otype, "text",
						msgContent, user.getId(),uuid);
			} else if (user.getUserType().equals(3)) {
				commonManager.saveBusinessMessageInfo_doc(oid, otype, "text",
						msgContent, user.getId(),uuid);
			}
		}
		return map;
	}

	/**
	 * 结束专家咨询订单(医生端和专家端都需要该功能，通用此方法)
	 * http://localhost:8080/doctor/endadviceorder?oid=xxx
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/endadviceorder")
	@ResponseBody
	public Map<String, Object> endadviceorder(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		SpecialAdviceOrder order = commonService
				.querySpecialAdviceOrderById(oid);
		order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey());
		commonService.updateSpecialAdviceOrder(order);
		return map;
	}

	private void processCase(Integer caseId,HttpServletRequest request) {
		CaseInfo caseInfo = commonService.queryCaseInfoById(caseId);
		Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();  
			String value = request.getParameter(paraName);
			if(paraName.equalsIgnoreCase("idcard")) {
				caseInfo.setIdNumber(value);
			}
			if(paraName.equalsIgnoreCase("telphone")) {
				caseInfo.setTelephone(value);
			}
			if(paraName.equalsIgnoreCase("normalImages")) {
				caseInfo.setNormalImages(value);
			}
			if(paraName.equalsIgnoreCase("askProblem")) {
				caseInfo.setAskProblem(value);
			}
			if(paraName.equalsIgnoreCase("description")) {
				caseInfo.setDescription(value);
			}
			if(paraName.equalsIgnoreCase("mainSuit")) {
				caseInfo.setMainSuit(value);
			}
			if(paraName.equalsIgnoreCase("historyIll")) {
				caseInfo.setHistoryIll(value);
			}
			if(paraName.equalsIgnoreCase("presentIll")) {
				caseInfo.setPresentIll(value);
			}
			if(paraName.equalsIgnoreCase("examined")) {
				caseInfo.setExamined(value);
			}
			if(paraName.equalsIgnoreCase("assistantResult")) {
				caseInfo.setAssistantResult(value);
			}
			if(paraName.equalsIgnoreCase("initialDiagnosis")) {
				caseInfo.setInitialDiagnosis(value);
			}
			if(paraName.equalsIgnoreCase("familyHistory")) {
				caseInfo.setFamilyHistory(value);
			}
			if(paraName.equalsIgnoreCase("treatAdvice")) {
				caseInfo.setTreatAdvice(value);
			}
			System.out.println("key:"+paraName+" value:"+value);
		}
		commonService.updateCaseInfo(caseInfo);
	}
	/**
	 * 保存编辑病例通用方法 远程，专家咨询中基本信息，诊疗信息，其他（本地资源）
	 * http://localhost:8080/doctor/editCaseInfo
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editCaseInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> editCaseInfo(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer caseId = caseService.saveOrUpdateCase(request, null, null);
		processCase(caseId,request);
		//更新病历附件
		CaseInfo caseInfo = commonService.queryCaseInfoById(caseId);
		doctorAdminManager.processCaseAttachments(caseInfo.getUuid(),request.getParameter("attachments"));
		// 远程订单 传入订单id
		String oid = request.getParameter("oid");
		String otype = request.getParameter("otype");
		if (StringUtils.isNotBlank(otype) && "4".equalsIgnoreCase(otype) && StringUtils.isNotBlank(oid)) {
			BusinessVedioOrder order = wenzhenService
					.queryBusinessVedioOrderById(Integer.parseInt(oid));
			// 记录消息
			if (order.getExpertId() != null) {
				MobileSpecial doc = commonService
						.queryMobileSpecialByUserIdAndUserType(order
								.getLocalDoctorId());
				Map<String, String> maps = new HashMap<>();
				maps = apiGetuiPushService.setPushDoctorExtend(maps, order.getExpertId());
				commonManager.generateSystemPushInfo(21, order.getUuid(), 4,
						order.getExpertId(), 2,maps, "来自" + doc.getHosName()
								+ "的会诊订单，已被" + doc.getSpecialName()
								+ "医生完善了患者病例信息，请提前查看订单详情及患者病例。");
			}
		} else if (StringUtils.isNotBlank(otype) && "5".equalsIgnoreCase(otype)
				&& StringUtils.isNotBlank(oid)) {
			SpecialAdviceOrder order = commonService
					.querySpecialAdviceOrderById(Integer.parseInt(oid));
			// 记录消息
			if (order.getExpertId() != null) {
				MobileSpecial doc = commonService
						.queryMobileSpecialByUserIdAndUserType(order
								.getDoctorId());
				Map<String, String> maps = new HashMap<>();
				maps = apiGetuiPushService.setPushDoctorExtend(maps, order.getExpertId());
				commonManager.generateSystemPushInfo(21, order.getUuid(), 5,
						order.getExpertId(), 2,maps, "来自" + doc.getHosName()
								+ "的专家咨询订单，已被" + doc.getSpecialName()
								+ "医生完善了患者病例信息，请查看订单详情及患者病例。");
			}
		}
		return map;
	}

	/**
	 * 远程订单,专家咨询 pacs影像信息保存编辑 http://localhost:8080/doctor/editPacsInfo
	 * 
	 * @param oid
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/editPacsInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> editPacsInfo(HttpServletRequest request)
			throws Exception {
		String oid = request.getParameter("oid");// 如果是专家咨询，oid实际上是订单的uuid
		Integer otype = Integer.parseInt(request.getParameter("otype"));
		Map<String, Object> map = new HashMap<String, Object>();
		String pacs_ids = request.getParameter("pacs_ids");
		// 保存时数据更新
		DataCatchUtil.clearedPacsData(pacs_ids, oid);
		if (otype != null && otype.equals(4) && StringUtils.isNotBlank(oid)) {
			BusinessVedioOrder order = wenzhenService
					.queryBusinessVedioOrderById(Integer.parseInt(oid));
			// 记录消息
			if (order.getExpertId() != null) {
				MobileSpecial doc = commonService
						.queryMobileSpecialByUserIdAndUserType(order
								.getLocalDoctorId());
				Map<String, String> maps = new HashMap<>();
				maps = apiGetuiPushService.setPushDoctorExtend(maps, order.getExpertId());
				commonManager.generateSystemPushInfo(21, order.getUuid(), 4,
						order.getExpertId(), 2,maps, "来自" + doc.getHosName()
								+ "的会诊订单，已被" + doc.getSpecialName()
								+ "医生完善了患者影像信息，请提前查看订单详情及患者病例。");
			}
		} else if (otype != null && otype.equals(5)
				&& StringUtils.isNotBlank(oid)) {
			SpecialAdviceOrder order = commonService
					.querySpecialAdviceOrderById(Integer.parseInt(oid));
			// 记录消息
			if (order.getExpertId() != null) {
				MobileSpecial doc = commonService
						.queryMobileSpecialByUserIdAndUserType(order
								.getDoctorId());
				Map<String, String> maps = new HashMap<>();
				maps = apiGetuiPushService.setPushDoctorExtend(maps, order.getExpertId());
				commonManager.generateSystemPushInfo(21, order.getUuid(), 5,
						order.getExpertId(), 2,maps, "来自" + doc.getHosName()
								+ "的专家咨询订单，已被" + doc.getSpecialName()
								+ "医生完善了患者影像信息，请查看订单详情及患者病例。");
			}
		}
		return map;
	}
	
	/**
	 * 病例管理中编辑影像
	 * http://localhost:8080/doctor/editPacsInfo_case
	 * @param oid
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/editPacsInfo_case", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> editPacsInfoCase(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String caseUuid = request.getParameter("caseUuid");
		String pacs_ids = request.getParameter("pacs_ids");
		// 保存时数据更新
		DataCatchUtil.clearedPacsData_case(pacs_ids, caseUuid);
		return map;
	}

	/**
	 * 远程订单 lis检查检验保存编辑 http://localhost:8080/doctor/editLisInfo
	 * 
	 * @param oid
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/editLisInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> editLisInfo(HttpServletRequest request)
			throws Exception {
		String oid = request.getParameter("oid");
		Integer otype = Integer.parseInt(request.getParameter("otype"));
		Map<String, Object> map = new HashMap<String, Object>();
		String lis_ids = request.getParameter("lis_ids");
		// 保存时数据更新
		DataCatchUtil.clearedLisData(lis_ids, oid);
		// 记录消息
		if (otype != null && otype.equals(4) && StringUtils.isNotBlank(oid)) {
			// 记录消息
			BusinessVedioOrder order = wenzhenService
					.queryBusinessVedioOrderById(Integer.parseInt(oid));
			if (order.getExpertId() != null) {
				MobileSpecial doc = commonService
						.queryMobileSpecialByUserIdAndUserType(order
								.getLocalDoctorId());
				Map<String, String> maps = new HashMap<>();
				maps = apiGetuiPushService.setPushDoctorExtend(maps, order.getExpertId());
				commonManager.generateSystemPushInfo(21, order.getUuid(), 4,
						order.getExpertId(), 2,maps, "来自" + doc.getHosName()
								+ "的会诊订单，已被" + doc.getSpecialName()
								+ "医生完善了患者检查检验信息，请提前查看订单详情及患者病例。");
			}
		} else if (otype != null && otype.equals(5)
				&& StringUtils.isNotBlank(oid)) {
			SpecialAdviceOrder order = commonService
					.querySpecialAdviceOrderById(Integer.parseInt(oid));
			// 记录消息
			if (order.getExpertId() != null) {
				MobileSpecial doc = commonService
						.queryMobileSpecialByUserIdAndUserType(order
								.getDoctorId());
				Map<String, String> maps = new HashMap<>();
				maps = apiGetuiPushService.setPushDoctorExtend(maps, order.getExpertId());
				commonManager.generateSystemPushInfo(21, order.getUuid(), 5,
						order.getExpertId(), 2,maps, "来自" + doc.getHosName()
								+ "的专家咨询订单，已被" + doc.getSpecialName()
								+ "医生完善了患者检查检验信息，请查看订单详情及患者病例。");
			}
		}
		return map;
	}

	/**
	 * 检查登陆状态 访问：http://localhost:8080/doctor/checklogin
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/checklogin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checklogin(HttpServletRequest request,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		if (user == null) {
			// 登陆失效
			map.put("login_status", "faillogin");
		} else {
			// 登陆未失效
			map.put("login_status", "logining");
		}
		return map;
	}

	/**
	 * 远程订单,专家咨询加载病例信息（基本，临床，本地资源） 访问：httpj://localhost:8080/doctor/loadcaseinfo
	 * 参数：oid:订单id
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loadcaseinfo")
	@ResponseBody
	public Map<String, Object> loadcaseinfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		String type = request.getParameter("docask");// 远程订单默认为空，专家咨询：true,预约转诊：referral
		Integer caseid = null;
		if (StringUtils.isNotBlank(type) && type.equalsIgnoreCase("true")) {
			// 专家咨询
			SpecialAdviceOrder order = commonService
					.querySpecialAdviceOrderById(oid);
			caseid = order.getCaseId();
		}else if(StringUtils.isNotBlank(type)&&type.equalsIgnoreCase("referral")){
			BusinessD2dReferralOrder order=d2pService.queryd2dreferralOrderbyId(oid);
			caseid=order.getCaseId();
		} else {
			BusinessVedioOrder order = wenzhenService
					.queryBusinessVedioOrderById(oid);
			caseid = order.getCaseId();
		}
		CaseInfo caseinfo = commonService.queryCaseInfoById(caseid);
		// 病例信息
		map.put("caseinfo", caseinfo);
		if (StringUtils.isNotBlank(caseinfo.getNormalImages())) {
			List<CustomFileStorage> normals = wenzhenService
					.queryCustomFilesByCaseIds(caseinfo.getNormalImages());
			// 本地资源
			map.put("normals", normals);
		}
		if(StringUtils.isNotBlank(caseinfo.getCheckReportImages())){
			List<CustomFileStorage> checkReportImages = wenzhenService
					.queryCustomFilesByCaseIds(caseinfo.getCheckReportImages());
			//检查报告图片
			map.put("checkReportImages", checkReportImages);
		}
		if(StringUtils.isNotBlank(caseinfo.getRadiographFilmImags())){
			List<CustomFileStorage> radiographFilmImags = wenzhenService
					.queryCustomFilesByCaseIds(caseinfo.getRadiographFilmImags());
			map.put("radiographFilmImags", radiographFilmImags);//影像报告图片
		}
		return map;
	}

	private SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 远程订单 加载对话消息 访问：http://localhost:8080/doctor/loadmessages 参数：oid--订单id
	 * vtype--类型
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loadmessages")
	@ResponseBody
	public Map<String, Object> loadmessages(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 订单id
		String type = request.getParameter("docask");
		if (StringUtils.isNotBlank(type) && type.equalsIgnoreCase("true")) {
			// 专家咨询
			SpecialAdviceOrder order=commonService.querySpecialAdviceOrderById(oid);
			List<BusinessMessageBean> msgs = wenzhenService
					.queryBusinessMessageBeansByCon(oid,order.getUuid(), 5);
			for (BusinessMessageBean _msg : msgs) {
				_msg.setMsgTime(_sdf.format(_msg.getSendTime()));
			}
			map.put("messages", msgs);
		} else {
			BusinessVedioOrder order = commonService
					.queryBusinessVedioOrderById(oid);
			Integer vtype = Integer.parseInt(request.getParameter("vtype"));// vtype:2--专家
																			// 3--医生
			List<BusinessMessageBean> messages = wenzhenService
					.queryBusinessMessageBeansByCon(
							oid,order.getUuid(),
							4,
							vtype.equals(2) ? order.getLocalDoctorId() : order
									.getExpertId());
			map.put("messages", messages);
		}
		return map;
	}
	@Autowired
	private ID2pService d2pService;
	/**
	 * 加载订单信息 访问：http://localhost:8080/doctor/loadorder 参数：oid订单id
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loadorder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loadorder(HttpServletRequest request,HttpSession session)
			throws Exception {
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		String type = request.getParameter("docask");//预约转诊 ：referral
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(type) && type.equalsIgnoreCase("true")) {
			// 专家咨询
			SpecialAdviceOrder order = commonService
					.querySpecialAdviceOrderById(oid);
			map.put("order", order);
			System.out.println("==="+JSONObject.fromObject(order).toString());
		}if(StringUtils.isNotBlank(type)&&type.equalsIgnoreCase("referral")){
			BusinessD2dReferralOrder order=d2pService.queryd2dreferralOrderbyId(oid);
			map.put("order", order);
			
		} else {
			BusinessVedioOrder order = wenzhenService
					.queryBusinessVedioOrderById(oid);
			map.put("order", order);
			
			Integer _hasuser = WebRTCRoomManager.getUserNum(oid.toString());
			map.put("_hasuser", _hasuser);
			DoctorRegisterInfo user = (DoctorRegisterInfo) session
					.getAttribute("user");
			Integer docid=order.getLocalDoctorId();
			Integer expid=order.getExpertId();
			if(docid!=null&&docid.equals(user.getId())){
				map.put("sendOrReceive", "send");
			}
			if(expid!=null&&expid.equals(user.getId())){
				map.put("sendOrReceive", "receive");
			}
		}
		return map;
	}

	/**
	 * 获取token 访问：http://localhost:8080/doctor/gainIMToken
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gainIMToken")
	@ResponseBody
	public Map<String, Object> gainIMToken(HttpServletRequest request)
			throws Exception {
		return commonManager.gainadviceim(request);
	}

	private SimpleDateFormat tformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * 加载系统消息 访问：http://localhost:8080/doctor/loadsysmsgs 如果pushcode为21
	 * 则点击后要跳转到订单详情
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/loadsysmsgs")
	@ResponseBody
	public Map<String, Object> loadsysmsgs(HttpServletRequest request,
			HttpSession session) {
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Map<String, Object> map = new HashMap<String, Object>();
		if(user !=null ){
			List<SystemPushInfo> msgs = commonManager
					.querySystemPushInfoByUser(user.getId());
			for (SystemPushInfo systemPushInfo : msgs) {
				systemPushInfo.setDesc(PushCodeEnum.getStatusValue(systemPushInfo.getPushCode()));
				String desc=gainBusinessDesc(systemPushInfo.getBusinessType());
				if(StringUtils.isNotBlank(desc)){
					systemPushInfo.setDesc(desc);
				}
				systemPushInfo.setTimeStr(RelativeDateFormat
						.calculateTimeLoc(tformat.format(systemPushInfo
								.getCreateTime())));
			}
			map.put("msgs", msgs);
		}
		return map;
	}

	/**
	 * 跳转 访问：http://localhost:8080/doctor/jumptodetail
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/jumptodetail")
	@ResponseBody
	public Map<String, Object> jumptodetail(HttpServletRequest request) {

		Map<String, Object> map = new HashMap<String, Object>();
		String key = request.getParameter("businessKey");
		String pushkey = request.getParameter("pushKey");
		SystemPushInfo pinfo = commonManager
				.querySystemPushInfoByPushKey(pushkey);
		
		if(pinfo.getBusinessType()!= null){
			if (pinfo.getBusinessType().equals(4)) {
				// 远程订单
				BusinessVedioOrder order = commonService
						.queryBusinessVedioOrderByUid(key);
				map.put("oid", order.getId());
				map.put("otype", 4);// 远程订单
			} else if (pinfo.getBusinessType().equals(5)) {
				// 专家咨询
				SpecialAdviceOrder order = commonService
						.querySpecialAdviceOrderByUid(key);
				map.put("oid", order.getId());
				map.put("otype", 5);// 专家咨询
			} else if (pinfo.getBusinessType().equals(1)) {
				// 图文订单
				BusinessTuwenOrder order = wenzhenService
						.queryBusinessTuwenInfoByUid(key);
				map.put("oid", order.getId());
				map.put("otype", 1);// 图文订单
			} else if (pinfo.getBusinessType().equals(2)) {
				// 电话订单
				BusinessTelOrder order = wenzhenService
						.queryBusinessTelOrderByUid(key);
				map.put("oid", order.getId());
				map.put("otype", 2);// 电话订单
			}else if (pinfo.getBusinessType().equals(10)) {
				// 预约转诊
				BusinessD2dReferralOrder rorder = d2pService.queryBusinessD2pReferralOrderByUuid(key);
				map.put("oid", rorder.getId());
				map.put("otype", 10);// 电话订单
			}
		}
		// 更新为已读
		pinfo.setIsRead(1);// 已读
		commonManager.updateSystemPushInfo(pinfo);
		return map;
	}

	/**
	 * 首页二维码keyid 访问：http://localhost:8080/doctor/indexsaoma
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/indexsaoma")
	@ResponseBody
	public Map<String, Object> indexsaoma(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String keyid = erweiManager.generateErInfo();
		map.put("keyid", keyid);
		return map;
	}
	/**
	 * 结束专家咨询订单（医生端，专家端）
	 * 访问：http://localhost:8080/doctor/endzjzx
	 * 参数：oid 专家咨询订单id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/endzjzx")
	@ResponseBody
	public Map<String,Object> endzjzx(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer oid=Integer.parseInt(request.getParameter("oid"));//专家咨询订单id
		SpecialAdviceOrder order=commonService.querySpecialAdviceOrderById(oid);
		if(order!=null){
			order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey());//已结束
			commonService.updateSpecialAdviceOrder(order);
		}
		return map;
	}
	/**
	 * 针对拆开的检查报告图片和影像图片的编辑保存
	 * 访问：http://localhost:8080/doctor/editjcyxpic
	 * 参数：caseid：病例id  ltype：类型  两个图片数据参数
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/editjcyxpic")
	@ResponseBody
	public Map<String,Object> editjcyxpic(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer caseid=Integer.parseInt(request.getParameter("caseid"));
		String ltype=request.getParameter("ltype");//jc:检查报告图片  yx：影像图片
		CaseInfo cinfo=commonService.queryCaseInfoById(caseid);
		if(ltype.equalsIgnoreCase("jc")){
			cinfo.setCheckReportImages(request.getParameter("checkReportImages"));
		}else if(ltype.equalsIgnoreCase("yx")){
			cinfo.setRadiographFilmImags(request.getParameter("radiographFilmImags"));
		}
		commonService.updateCaseInfo(cinfo);
		return map;
	}
	/**
	 * 进入医生专家的提现申请
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/docwithdraws")
	public ModelAndView docwithdraws(HttpServletRequest request){
		return new ModelAndView("common/doctor_withdraws");
	}
	
	/**
	 * 获取医生专家提现申请数据
	 * 访问：http://localhost:8080/doctor/gaindocwithdraws
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	/*@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaindocwithdraws", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindocwithdraws(@RequestBody JSONParam[] params,
			HttpServletRequest request,HttpSession session) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		return commonManager.querydocwithdraws(paramMap,user.getId());	
	}*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaindocwithdraws", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindocwithdraws(@RequestBody JSONParam[] params,
			HttpServletRequest request,HttpSession session) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		return commonManager.querydocwithdraws(paramMap,user.getId());	
	}
	/**
	 * 访问：http://localhost:8080/doctor/gainBusinessTypes
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/gainBusinessTypes")
	@ResponseBody
	public Map<String,Object> gainBusinessTypes(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		map.putAll(BusinessTypeGenerate.gainBusinessTypeByUserType(user.getUserType()));
		return map;
	}
	/**
	 * 进入医生专家的账单列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/docbills")
	public ModelAndView docbills(HttpServletRequest reuqest){
		return new ModelAndView("common/doctor_bills");
	}
	/**
	 * 获取医生专家账单数据
	 * 访问：http://localhost:8080/doctor/gaindocbilldatas
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaindocbilldatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindocbilldatas(@RequestBody JSONParam[] params,
			HttpServletRequest request,HttpSession session) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		return commonManager.querydocbilldatas_doc(paramMap,user.getId());	
	}
	
	/**
	 * 获取专家咨询,远程订单群聊所需参数数据
	 * @param request
	 * @return
	 */
	/*@RequestMapping(value="/gainadviceim")
	@ResponseBody
	public Map<String,Object> gainadviceim(HttpServletRequest request){
		
	}*/
	
	/**
	 * 我的服务设置
	 * @param reuqest
	 * @return
	 */
	@RequestMapping(value="/myserver")
	public ModelAndView myserver(HttpServletRequest reuqest){
		return new ModelAndView("common/doc_servers");
	}
	
	/**
	 * 获取医生服务数据
	 * 访问：http://localhost:8080/doctor/gainserverdatas
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gainserverdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainserverdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request,HttpSession session) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		Integer uid=null;
		String docid=request.getParameter("docid");
		if(StringUtils.isNotBlank(docid)){
			uid=Integer.parseInt(docid);
		}else{
			DoctorRegisterInfo user = (DoctorRegisterInfo) session
					.getAttribute("user");
			uid=user.getId();
		}
		return commonManager.queryserverdatas(paramMap,uid);	
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gainserverdatas_new")
	public @ResponseBody
	Map<String,Object> gainserverdatas_new(HttpServletRequest request,HttpSession session) throws Exception {
		Integer dtype=Integer.parseInt(request.getParameter("dtype"));
		Map<String,Object> map=new HashMap<String,Object>();
		Integer uid=null;
		String docid=request.getParameter("docid");
		if(StringUtils.isNotBlank(docid)){
			uid=Integer.parseInt(docid);
		}else{
			DoctorRegisterInfo user = (DoctorRegisterInfo) session
					.getAttribute("user");
			uid=user.getId();
		}
		List<SystemServiceInfo> servers= commonManager.queryserverdatas_new(dtype,uid);	
		map.put("servers", servers);
		return map;
	}
	/**
	 * 更新医生服务设置
	 * @param request
	 * @return
	 */
	@RequestMapping(value="changeServerStatus")
	@ResponseBody
	public Map<String,Object> changeServerStatus(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer uid=null;
		String docid=request.getParameter("docid");
		if(StringUtils.isNotBlank(docid)){
			uid=Integer.parseInt(docid);
		}else{
			DoctorRegisterInfo user = (DoctorRegisterInfo) session
					.getAttribute("user");
			uid=user.getId();
		}
		Integer doctorServiceId=Integer.parseInt(request.getParameter("dsid"));
		Integer systemServiceId=Integer.parseInt(request.getParameter("ssid"));
		Integer sval=Integer.parseInt(request.getParameter("sval"));
		BigDecimal smoney=new BigDecimal(0);
		if(StringUtils.isNotBlank(request.getParameter("smoney"))){
			smoney=new BigDecimal(request.getParameter("smoney"));
		}
		commonManager.changServerStatus(uid,doctorServiceId,systemServiceId,sval,smoney);
		return map;
	}
	/**
	 * 进入医生出诊时间设置
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mytimeset", method = RequestMethod.GET)
	public ModelAndView mytimeset(HttpServletRequest request) {
		DoctorRegisterInfo  user = (DoctorRegisterInfo) request.getSession().getAttribute(
				"user");
		Map<String, Object> map = new HashMap<String, Object>();
		List<DoctorScheduleShow> times =commonService.queryDoctorScheduleShowsByDoctorId(user.getId());
		map.put("times", times);
		return new ModelAndView("common/schedule_time_show", map);
	}
	/**
	 * 保存或编辑出诊时间
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sudocscheduleshow")
	@ResponseBody
	public Map<String,Object> sudocscheduleshow(HttpServletRequest request){
		return commonManager.sudocscheduleshow(request);
	}
	/**
	 * 删除出诊时间
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deldocscheduleshow", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> deldocscheduleshow(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer tid = Integer.parseInt(request.getParameter("tid"));
		commonService.deldocscheduleshowId(tid);
		return map;
	}
	/**
	 * 访问：http://localhost:8080/doctor/modifyMsgToRead
	 * 参数：otype,oid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/modifyMsgToRead")
	@ResponseBody
	public Map<String,Object> modifyMsgToRead(HttpServletRequest request){
		DoctorRegisterInfo  user = (DoctorRegisterInfo) request.getSession().getAttribute(
				"user");
		Integer otype=Integer.parseInt(request.getParameter("otype"));//5图文会诊
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		commonManager.modifyMsgToRead(user.getId(),otype,oid);
		return null;
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
	private String gainBusinessDesc(Integer businessType){
		String desc="";
		if(businessType!=null){
			switch(businessType){
			case 13:
				desc="送心意";
				break;
			case 12:
				desc="团队问诊";
				break;
			case 11:
				desc="团队会诊意向";
				break;
			case 10:
				desc="转诊订单";
				break;
			case 9:
				desc="快速问诊";
				break;
			case 8:
				desc="患者报道";
				break;
			case 7:
				desc="电话问诊";
				break;
			case 6:
				desc="图文问诊";
				break;
			case 5:
				desc="图文会诊";
				break;
			case 4:
				desc="视频问诊";
				break;
			}
		}
		return desc;
	}
}
