package com.tspeiz.modules.home;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tspeiz.modules.common.bean.JSONParam;
import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.bean.PushCodeEnum;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.BigDepartment;
import com.tspeiz.modules.common.entity.SystemPushInfo;
import com.tspeiz.modules.common.entity.SystemServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.SystemBusinessDictionary;
import com.tspeiz.modules.common.entity.release2.BusinessD2dReferralOrder;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAlliance;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;
import com.tspeiz.modules.common.service.ICaseService;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.ID2pService;
import com.tspeiz.modules.common.service.IKangxinService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.manage.CommonManager;
import com.tspeiz.modules.manage.DoctorAdminManager;
import com.tspeiz.modules.manage.PayOrderManager;
import com.tspeiz.modules.util.DataCatchUtil;
import com.tspeiz.modules.util.IdcardUtils;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.common.StringRetUtil;
import com.tspeiz.modules.util.config.ReaderConfigUtil;
import com.tspeiz.modules.util.date.RelativeDateFormat;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.log.RecordLogUtil;
import com.tspeiz.modules.util.weixin.WeixinUtil;

@Controller
@RequestMapping("docadmin")
public class DoctorAdminController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger log = Logger.getLogger(DoctorAdminController.class);
	@Resource
	private IWeixinService weixinService;
	@Resource
	private ICommonService commonService;
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private ICaseService caseService;
	@Autowired
	private DoctorAdminManager doctorAdminManager;
	@Autowired  
	private IKangxinService kangxinService;
	@Autowired
	private CommonManager commonManager;
	@Autowired
	private PayOrderManager payOrderManager;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) {
		return new ModelAndView("doc/index");
	}

	// 远程会诊订单
	@RequestMapping(value = "/experhz", method = RequestMethod.GET)
	public ModelAndView experhz(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		return new ModelAndView("doc/zj_huizhen", map);
	}

	// 该医生远程订单
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gainorders", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainorders(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));

		// 判断是当前订单还是历史订单
		String currorhis = paramMap.get("currorhis");// current,history
		StringBuilder stringJson = null;
		List<BusinessVedioOrder> _rcs = null;
		if (StringUtils.isNotBlank(currorhis)
				&& currorhis.equalsIgnoreCase("history")) {
			Map<String, Object> retmap = weixinService
					.queryRemoteConsulationsByConditions_docnew(user.getId(),
							user.getUserType(), searchContent, "desc", start,
							length, "30,40,50", null);
			Integer renum = (Integer) retmap.get("num");
			List<BusinessVedioOrder> rcs = (List<BusinessVedioOrder>) retmap
					.get("items");
			if (_rcs == null)
				_rcs = new ArrayList<BusinessVedioOrder>();
			_rcs.addAll(rcs);
			stringJson = new StringBuilder("{\"sEcho\":" + sEcho
					+ ",\"iTotalRecords\":" + (renum)
					+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		} else {
			// 第一页，获取当天最近的两条已完成的数据
			Integer com_start = 0;
			Integer com_length = 2;
			Map<String, Object> _retmap = weixinService
					.queryRemoteConsulationsByConditions_docnew(user.getId(),
							user.getUserType(), searchContent, "", com_start,
							com_length, "40", sdf.format(new Date()));
			Integer _renum = (Integer) _retmap.get("num");
			_rcs = (List<BusinessVedioOrder>) _retmap.get("items");
			if (start.equals(0)) {
				if (_renum > 0) {
					length = length - _renum;
				}
			} else {
				if (_renum > 0) {
					start = start - 2;
				}
			}
			Map<String, Object> retmap = weixinService
					.queryRemoteConsulationsByConditions_docnew(user.getId(),
							user.getUserType(), searchContent, "", start,
							length, "10,20", null);
			Integer renum = (Integer) retmap.get("num");
			List<BusinessVedioOrder> rcs = (List<BusinessVedioOrder>) retmap
					.get("items");
			if (_rcs == null)
				_rcs = new ArrayList<BusinessVedioOrder>();
			_rcs.addAll(rcs);
			stringJson = new StringBuilder("{\"sEcho\":" + sEcho
					+ ",\"iTotalRecords\":" + (_renum + renum)
					+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		}

		BusinessVedioOrder rc = null;

		String age = "未知";
		Integer stat = null;
		for (int i = 0; i < _rcs.size(); i++) {
			String time = "";
			rc = _rcs.get(i);
			stringJson.append("[");
			stringJson.append("\"" + rc.getId() + "\",");
			stat = rc.getStatus();
			if (StringUtils.isNotBlank(rc.getConsultationDate())) {
				time += rc.getConsultationDate();
			}
			if (StringUtils.isNotBlank(rc.getConsultationTime())) {
				time += " " + rc.getConsultationTime();
			}
			// 专家信息
			stringJson.append("\"" + rc.getExpertHeadImage() + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(rc.getExpertName()) ? rc
							.getExpertName() : "未知") + "\",");
			stringJson.append("\"" + rc.getHosName() + "\",");
			stringJson.append("\""
					+ StringRetUtil.gainStringByStats_new(rc.getStatus(),
							rc.getProgressTag(), 3) + "\",");
			stringJson.append("\"" + rc.getPatientName() + "  "
					+ (rc.getSex().equals(1) ? "男" : "女") + " " + rc.getAge()
					+ "岁" + "\",");
			String desc=(StringUtils.isNotBlank(rc.getDiseaseDes()) ? rc
					.getDiseaseDes() : (StringUtils.isNotBlank(rc
					.getMainSuit()) ? rc.getMainSuit() : ""));
			desc=clearCha(desc);
			desc=desc.replaceAll("\"","\\\\\"");  
			stringJson.append("\""
					+ (StringUtils.isNotBlank(rc.getCaseName()) ? rc
							.getCaseName() : "")
					+ " "
					+ desc + "\",");
			stringJson.append("\"" + rc.getTelephone() + "\",");
			String _time = "";
			if (StringUtils.isNotBlank(time)
					&& !time.equalsIgnoreCase("null null")) {
				_time = RelativeDateFormat.calculateTimeLoc(time);
			} else {
				time = "待定";
			_time = "未知";
			}
			stringJson.append("\"" + time + "\",");
			stringJson.append("\"" + _time + "\",");
			stringJson.append("\"" + _sdf.format(rc.getCreateTime()) + "\",");
			stringJson.append("\"" + rc.getPayStatus() + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (_rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		log.info("====json==" + stringJson.toString());
		return stringJson.toString();
	}

	private String clearCha(String s){
		char[] chars = s.toCharArray();  
		StringBuilder sb=new StringBuilder();
        for(int i = 0; i < chars.length; i ++) {  
            if((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 97 && chars[i] <= 122) || (chars[i] >= 65 && chars[i] <= 90)) {  
                sb.append(chars[i]);
            }  
        } 
        return sb.toString();
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

	// 进入辅助下单
	@RequestMapping(value = "/helporder", method = RequestMethod.GET)
	public ModelAndView helporder(HttpServletRequest request,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		MobileSpecial ms = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		List<MobileSpecial> specials = weixinService
				.queryMobileSpecialsByLocalDepartId_new(ms.getDepId(), 4);
		map.put("specials", specials);
		map.put("localdocid", user.getId());
		HospitalDetailInfo detail = weixinService
				.queryHospitalDetailInfoById(ms.getHosId());
		map.put("dockingMode", detail.getDockingMode());
		return new ModelAndView("doc/help_order", map);
	}
	/**
	 * 加载医院
	 * 访问：http://localhost:8080/docadmin/loadhos
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/loadhos")
	@ResponseBody
	public Map<String,Object> gainhos(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		List<HospitalDetailInfo> hospitals= commonService.queryHospitals_expert();
		map.put("hospitals", hospitals);
		return map;
	}
	/**
	 * 加载大科室
	 * * 访问：http://localhost:8080/docadmin/loadbigdeps
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/loadbigdeps")
	@ResponseBody
	public Map<String,Object> loadbigdeps(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		List<BigDepartment> bigDeps = weixinService.queryBigDepartments();
		map.put("bigDeps", bigDeps);
		return map;
	}
	/**
	 * 加载专家 (分页）
	 * 访问：http://localhost:8080/docadmin/loadExperts
	 * 
	 * @param request
	 * @param session
	 * @param pager
	 * @return
	 */
	@RequestMapping(value="/loadExperts")	
	@ResponseBody
	public Map<String,Object> loadExperts(HttpServletRequest request,HttpSession session,@ModelAttribute Pager pager){
		if (pager == null)
			pager = new Pager();
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		String docask=request.getParameter("docask");//专家咨询："1",远程门诊：“2”，预约转诊：“3”
		String hosid=request.getParameter("hosid");//医院id       全部医院：-1
		String depId=request.getParameter("depId");//标准科室id   全部科室：-1
		String duty=request.getParameter("duty");//职务   全部：-1
		String keywords=request.getParameter("keywords");//关键字
		String distCode=request.getParameter("distCode");//区域
		pager=commonManager.loadExperts(pager,user.getId(),hosid,depId,duty,keywords,docask,distCode);
		map.put("pager", pager);// 返回分页数据
		return map;
	}
	/**
	 * 视频会诊-加载专家或医生(分页)
	 * @param request
	 * @param session
	 * @param pager
	 * @return
	 */
	@RequestMapping(value="/loadExpertOrDoctors")	
	@ResponseBody
	public Map<String,Object> loadExpertOrDoctors(HttpServletRequest request,HttpSession session,@ModelAttribute Pager pager){
		if (pager == null)
			pager = new Pager();
		Map<String,Object> map=new HashMap<String,Object>();	
		String distCode=request.getParameter("distCode");//区域
		String hosid=request.getParameter("hosid");//医院id       全部医院：-1
		String keywords=request.getParameter("keywords");//关键字
		String depId=request.getParameter("depId");//标准科室id
		String duty=request.getParameter("duty");//职务
		String dtype=request.getParameter("dtype");//视频会诊传值 dtype:"4",专家咨询传值：dtype:"5"
		pager=commonManager.loadExpertOrDoctors(pager,distCode,hosid,duty,keywords,depId,dtype);
		map.put("pager", pager);// 返回分页数据
		return map;
	}
	/**
	 * 视频会诊 填写基本信息后提交
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/nextSubBasicVedioInfo")
	@ResponseBody
	public Map<String,Object> nextSubBasicVedioInfo(HttpServletRequest request,HttpSession session) throws Exception{
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
        return doctorAdminManager.nextSubBasicVedioInfo(request,user.getId(),user.getUserType());
	}
	/**
	 * 视频会诊  填写完咨询目的后提交  如果没有选择医生 使用此方法
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/subnodocorder", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> subnodocorder(HttpServletRequest request,
			HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));//订单id
		// 更新病例
		updatecaseinfo(oid,4, request);
		pacsInfoStorage(oid+"", request);
		lisInfoStorage(oid, request);
		BusinessVedioOrder order = commonService
				.queryBusinessVedioOrderById(oid);
		order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
		commonService.updateBusinessVedioOrder(order);
		doctorAdminManager.processBindCode(oid,4);
		processHosAdminMsg(4, oid);
		return map;
	}
	
	/**
	 * 二维码支付
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/surepay_pc")
	@ResponseBody
	public Map<String, Object> surepayPC(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UUID uuid = UUID.randomUUID();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		BusinessVedioOrder order = commonService.queryBusinessVedioOrderById(oid);
		order.setConsultationDur(OrderStatusEnum.VEDIO_DURATION.getKey());
		order.setHelpOrderSelExpert(1);
		commonService.updateBusinessVedioOrder(order);
		updatecaseinfo(oid,4, request);
		pacsInfoStorage(oid+"", request);
		lisInfoStorage(oid, request);
		String product_id = uuid.toString().replace("-", "");
		Integer docid=order.getExpertId();	
		Float money=0f;
		if(docid!=null){
			BigDecimal vedioMoney=commonManager.gainMoneyByOrder(4, docid);
			if(vedioMoney!=null&&vedioMoney.compareTo(BigDecimal.ZERO)>0){
				money=vedioMoney.floatValue();
				Map<String, Object> retMap = WeixinUtil.weipay_pc(request, response,
						money, PropertiesUtil.getString("APPID"), PropertiesUtil
								.getString("APPSECRET"), PropertiesUtil
								.getString("PARTNER"), PropertiesUtil
								.getString("PARTNERKEY"),
						ReaderConfigUtil.gainConfigVal(request, "basic.xml",
								"root/remotebody"), product_id,
						PropertiesUtil.getString("PayCallBackUrl") + "d2p/paynotify",
						null, oid, "");
				Integer pid=payOrderManager.savePayInfo(4, oid, retMap.get("out_trade_no").toString(), money, 2, money, 0.0f, 0.0f, 0.0f,null);
				log.info("======二维码url===" + retMap.get("code_url"));
				map.put("status", "success");
				map.put("code_url", retMap.get("code_url"));
				map.put("out_trade_no", retMap.get("out_trade_no"));
				map.put("money",money);
				try {
					RecordLogUtil.insert("4", "远程门诊", oid+"",pid+"",
							(String) retMap.get("paramxml"),
							(String) retMap.get("prepayxml"), "",(String)retMap.get("out_trade_no"));
				} catch (Exception e) {
					log.error(e);
				}
			}else{
				order.setPayStatus(1);
				commonService.updateBusinessVedioOrder(order);
				map.put("status", "error");//视频价格为0的跳过支付
				doctorAdminManager.processBindCode(oid,4);
			}
			
		}else{
			map.put("status", "error");//没选择医生-跳过支付
		}
		return map;
	}
	/**
	 * 通知患者去支付
	 * 访问：http://localhost:8080/docadmin/notifyPatientToPay
	 * oid:订单id
	 * otype:订单类型（4-视频会诊，5-图文会诊)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/notifyPatientToPay")
	@ResponseBody
	public Map<String,Object> notifyPatientToPay(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		Integer otype=Integer.parseInt(request.getParameter("otype"));
		doctorAdminManager.notifyPatientToPay(oid,otype);
		map.put("status", "success");
		return map;
	}
	/**
	 * 预约转诊---加载医生 (分页）
	 * 访问：http://localhost:8080/docadmin/loadDoctors
	 * 
	 * @param request
	 * @param session
	 * @param pager
	 * @return
	 */
	@RequestMapping(value="/loadDoctors")	
	@ResponseBody
	public Map<String,Object> loadDoctors(HttpServletRequest request,HttpSession session,@ModelAttribute Pager pager){
		if (pager == null)
			pager = new Pager();
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		String hosid=request.getParameter("hosid");//医院id       全部医院：-1
		String depId=request.getParameter("depId");//标准科室id   全部科室：-1
		String keywords=request.getParameter("keywords");//关键字
		String distCode=request.getParameter("distCode");//区域
		pager=commonManager.loadDoctors(pager,hosid,depId,keywords,distCode);
		map.put("pager", pager);// 返回分页数据
		return map;
	}
	
	// 基本信息下单
	@RequestMapping(value = "/createorder", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> createorder(HttpServletRequest request,
			HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		MobileSpecial ms = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		String oid = request.getParameter("oid");
		BusinessVedioOrder order = null;
		String username = request.getParameter("username");
		String telphone = request.getParameter("telphone");
		String idcard = request.getParameter("idcard");
		String sex = request.getParameter("sex");
		String age = request.getParameter("age");
		if (!StringUtils.isNotBlank(sex))
			sex = "0";
		if (StringUtils.isNotBlank(oid)) {
			// 已有订单，更新病例
			order = commonService.queryBusinessVedioOrderById(Integer
					.parseInt(oid));

		} else {
			// 新订单，新病例
			order = new BusinessVedioOrder();

		}
		order.setLocalHospitalId(ms.getHosId());
		order.setLocalDepartId(ms.getDepId());
		order.setLocalDoctorId(ms.getSpecialId());
		order.setCreateTime(new Timestamp(System.currentTimeMillis()));
		order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
		order.setHelpOrder(1);
		order.setSource(4);
		order.setPayStatus(4);// 待支付
		order.setDelFlag(0);
		order.setConsultationDur(20);
		order.setUuid(commonManager.generateUUID(4));
		if (StringUtils.isNotBlank(oid)) {
			commonService.updateBusinessVedioOrder(order);
			CaseInfo caseinfo = commonService.queryCaseInfoById(order
					.getCaseId());
			caseinfo.setContactName(username);
			caseinfo.setTelephone(telphone);
			caseinfo.setIdNumber(idcard);
			caseinfo.setSex(Integer.parseInt(sex));
			if (StringUtils.isNotBlank(age)) {
				caseinfo.setAge(Integer.parseInt(age));
			} else {
				if (StringUtils.isNotBlank(caseinfo.getIdNumber())) {
					caseinfo.setAge(IdcardUtils.getAgeByIdCard(caseinfo
							.getIdNumber()));
				}
			}
			commonService.updateCaseInfo(caseinfo);
		} else {
			CaseInfo caseinfo = new CaseInfo();
			caseinfo.setCreateTime(new Date());
			caseinfo.setUuid(UUID.randomUUID().toString().replace("-", ""));
			caseinfo.setContactName(username);
			caseinfo.setTelephone(telphone);
			caseinfo.setIdNumber(idcard);
			caseinfo.setSex(Integer.parseInt(sex));
			if (StringUtils.isNotBlank(age)) {
				caseinfo.setAge(Integer.parseInt(age));
			} else {
				if (StringUtils.isNotBlank(caseinfo.getIdNumber())) {
					caseinfo.setAge(IdcardUtils.getAgeByIdCard(caseinfo
							.getIdNumber()));
				}
			}
			Integer caseid = wenzhenService.saveCaseInfo(caseinfo);
			order.setCaseId(caseid);
			order.setCaseUuid(caseinfo.getUuid());
			Integer _oid = weixinService.saveBusinessVedioOrder(order);
			oid = _oid.toString();
			// 记录消息
			commonManager.saveBusinessMessageInfo_sys(_oid, 4, "text", "创建订单",
					null, null);
			
			// 推送消息
			if(order.getExpertId() != null){
				commonManager.generateSystemPushInfo(21, order.getUuid(), 4,order.getExpertId(),2,null, "您有一个新的远程会诊订单，请提前查看订单详情及患者病历");
			}
		}
		map.put("oid", oid);
		return map;
	}
	
	
	
	
	@Autowired
	private ID2pService d2pService;
	
	private void updatecaseinfo(Integer oid, Integer otype,HttpServletRequest request) {
		Integer caseid=null;
		if(otype.equals(4)){
			BusinessVedioOrder order = commonService
					.queryBusinessVedioOrderById(oid);
			caseid=order.getCaseId();
		}else if(otype.equals(10)){
			BusinessD2dReferralOrder order=d2pService.queryd2dreferralOrderbyId(oid);
			caseid=order.getCaseId();
		}else if(otype.equals(5)){
			SpecialAdviceOrder adviceOrder=commonService.querySpecialAdviceOrderById(oid);
			caseid=adviceOrder.getCaseId();
		}
		CaseInfo caseinfo = commonService.queryCaseInfoById(caseid);
		caseinfo.setMainSuit(request.getParameter("mainSuit"));
		caseinfo.setPresentIll(request.getParameter("presentIll"));
		caseinfo.setAssistantResult(request.getParameter("assistantResult"));
		caseinfo.setExamined(request.getParameter("examined"));
		caseinfo.setHistoryIll(request.getParameter("historyIll"));
		caseinfo.setInitialDiagnosis(request.getParameter("initialDiagnosis"));
		caseinfo.setTreatAdvice(request.getParameter("treatAdvice"));
		caseinfo.setNormalImages(request.getParameter("normalImages"));//入院记录
		caseinfo.setFamilyHistory(request.getParameter("familyHistory"));
		caseinfo.setCheckReportImages(request.getParameter("checkReportImages"));
		caseinfo.setRadiographFilmImags(request.getParameter("radiographFilmImags"));
		caseinfo.setAskProblem(request.getParameter("askProblem"));
		commonService.updateCaseInfo(caseinfo);
		doctorAdminManager.processCaseAttachments(caseinfo.getUuid(),request.getParameter("attachments"));
	}
	

	// pacs信息入库
	private void pacsInfoStorage(String oid, HttpServletRequest request)
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
	 * 进入专家咨询
	 */
	@RequestMapping(value = "/intospadvice")
	public ModelAndView intospadvice(HttpServletRequest request,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		MobileSpecial ms = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());

		List<HospitalDetailInfo> hospitals = commonService
				.queryDocRelativeHospitals(ms.getDepId());
		List<StandardDepartmentInfo> deps = commonService
				.queryDocRelativeDepparts(ms.getDepId());
		map.put("hospitals", hospitals);
		map.put("deps", deps);
		return new ModelAndView("doc/special_advice", map);
	}

	/**
	 * 填写了基本信息后下一步
	 */
	@RequestMapping(value = "/nextadvice")
	@ResponseBody
	public Map<String, Object> nextadvice(HttpServletRequest request,
			HttpSession session) throws Exception {
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Map<String, Object> map = new HashMap<String, Object>();
		Integer caseid = caseService.saveOrUpdateCase(request, user.getId(), 3);
		Map<String, Object> retmap = doctorAdminManager
				.saveOrUpdateSpecialAdviceOrder(request, caseid, user.getId());
		map.put("caseid", caseid);
		map.put("oid", retmap.get("oid"));
		map.put("uuid", retmap.get("uuid"));
		return map;
	}

	/**
	 * 获取专家数据信息
	 */
	@RequestMapping(value = "/gainspecials")
	@ResponseBody
	public Map<String, Object> gainspecials(HttpServletRequest request,
			HttpSession session, @ModelAttribute Pager pager) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (pager == null)
			pager = new Pager();
		String hosid = request.getParameter("hosid");
		String sdepid = request.getParameter("sdepid");
		if (StringUtils.isNotBlank(hosid) || StringUtils.isNotBlank(sdepid)) {
			Map<String, String> querymap = new HashMap<String, String>();
			if (StringUtils.isNotBlank(hosid)) {
				querymap.put("hosid", hosid);// 医院id
			}
			if (StringUtils.isNotBlank(sdepid)) {
				querymap.put("sdepid", sdepid);// 标准科室id
			}
			pager.setQueryBuilder(querymap);
			pager = kangxinService.searchspecialsByPager_advice(pager);
		} else {
			DoctorRegisterInfo user = (DoctorRegisterInfo) session
					.getAttribute("user");
			MobileSpecial ms = commonService

			.queryMobileSpecialByUserIdAndUserType(user.getId());
			pager.setQueryBuilder(new HashMap<String, String>());
			pager = weixinService.queryMobileSpecialsByLocalDepartId_newpager(
					ms.getDepId(), 1, pager);
		}

		map.put("pager", pager);// 返回分页数据
		return map;
	}

	/**
	 * 选择专家后进入支付
	 */
	@RequestMapping(value = "/continuetopay")
	@ResponseBody
	public Map<String, Object> continuetopay(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Map<String, Object> retmap = doctorAdminManager.processcpay(request,
				response, user.getId(), 3);
		map.putAll(retmap);
		map.put("pay", "true");
		return map;
	}

	/**
	 * 进入专家咨询列表复
	 */
	@RequestMapping(value = "/showadviceorders")
	public ModelAndView showadviceorders(HttpServletRequest request) {

		return new ModelAndView("doc/advice_orders");
	}

	/**
	 * 获取专家咨询列表数据
	 * 
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gainadviceorders", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainadviceorders(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		// 进行中或者历史订单
		String currstatus = paramMap.get("currstatus");
		String data = doctorAdminManager.gainadviceorders(currstatus, user.getId(),
				sEcho, start, length, searchContent);
		return data;
	}

	/**
	 * 查看详情
	 */
	@RequestMapping(value = "/showadvicedetail/{oid}")
	public ModelAndView showadvicedetail(HttpServletRequest request,
			@PathVariable Integer oid) {
		Map<String, Object> map = new HashMap<String, Object>();
		SpecialAdviceOrder order = commonService
				.querySpecialAdviceOrderById(oid);
		CaseInfo caseinfo = commonService.queryCaseInfoById(order.getCaseId());
		order.setDocUnreadMsgNum(0);
		commonService.updateSpecialAdviceOrder(order);
		map.put("order", order);
		map.put("caseinfo", caseinfo);
		List<CustomFileStorage> caseimages = wenzhenService
				.queryCustomFilesByCaseIds(caseinfo.getNormalImages());
		map.put("caseimages", caseimages);
		// 聊天消息
		List<BusinessMessageBean> msgs = wenzhenService
				.queryBusinessMessageBeansByCon(oid,order.getUuid(), 5);
		for (BusinessMessageBean _msg : msgs) {
			_msg.setMsgTime(_sdf.format(_msg.getSendTime()));
		}
		map.put("msgs", msgs);
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(order.getExpertId());
		map.put("special", special);
		return new ModelAndView("doc/advice_detail", map);
	}

	/**
	 * 医生给专家发送消息
	 */
	@RequestMapping(value = "/replytoexpert")
	@ResponseBody
	public Map<String, Object> replytouser(HttpServletRequest request) 
	{
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 问诊id
		String msgContent = request.getParameter("msgContent");
		Map<String, Object> retmap = doctorAdminManager.processReply(oid,
				msgContent, user.getId());
		map.putAll(retmap);
		return map;
	}
	
	/**
	 * 辅助下单 去支付
	 * @param request
	 * @param oid 订单id
	 * @return
	 */
	@RequestMapping(value="/topaycontinue/{oid}",method=RequestMethod.GET)
	public ModelAndView topaycontinue(HttpServletRequest request,@PathVariable Integer oid){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("oid", oid);
		BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(oid);
		if(order.getExpertId()==null){
			map.put("exsel", "false");//未选专家
		}else{
			map.put("exsel", "true");//已选专家
		}
		return new ModelAndView("doc/help_topay",map);
	}
	
	
	/**
	 * 获取支付二维码
	 * 访问：http://localhost:8080/docadmin/gainerweima
	 * 参数：oid 订单id ,如果是专家咨询继续支付需传askdoc：“true”
	 * @param request
	 * @param response
	 * @return 微信支付二维码  code_url
	 * @throws Exception
	 */
	@RequestMapping(value="/gainerweima",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> gainerweima(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		Integer oid=Integer.parseInt(request.getParameter("oid"));//订单id
		String askdoc=request.getParameter("askdoc");//专家咨询 ：“true"  
		Integer exid=null;
		DoctorServiceInfo ds=null;
		if(StringUtils.isNotBlank(askdoc)&&askdoc.equalsIgnoreCase("true")){
			//专家咨询订单
			SpecialAdviceOrder order=commonService.querySpecialAdviceOrderById(oid);
			exid=order.getExpertId();
			if(exid!=null)ds=commonService.queryDoctorServiceInfoByOrderType(5,exid);
		}else{
			//远程订单
			BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(oid);
			exid=order.getExpertId();
			if(exid!=null)ds=commonService.queryDoctorServiceInfoByOrderType(4, exid);
		}
		if(exid==null){
			map.put("pay", "false");
		}else{
			if(StringUtils.isNotBlank(askdoc)&&askdoc.equalsIgnoreCase("true")){
				//专家咨询订单
				if(ds!=null&&ds.getIsOpen().equals(1)){
					//Float  money=ds.getAmount().floatValue();
					Float money = ds.getShowPrice() != null?ds.getShowPrice().floatValue():
						commonManager.processD2DAskMoney(ds.getAmount()).floatValue();
					UUID uuid = UUID.randomUUID();
					String product_id = uuid.toString().replace("-", "");
					Map<String, Object> retMap = WeixinUtil.weipay_pc(request, response,
							money, PropertiesUtil.getString("APPID"), PropertiesUtil
									.getString("APPSECRET"), PropertiesUtil
									.getString("PARTNER"), PropertiesUtil
									.getString("PARTNERKEY"),"专家咨询", product_id,
									PropertiesUtil.getString("PayCallBackUrl") + "d2p/paynotify",
							null, oid, null);
					Integer pid=payOrderManager.savePayInfo(5, oid, retMap.get("out_trade_no").toString(), money, 2, money, 0.0f, 0.0f, 0.0f,null);
					map.put("code_url", retMap.get("code_url"));
					map.put("out_trade_no", retMap.get("out_trade_no"));
					map.put("money", money);
					try {
						RecordLogUtil.insert("5", "专家咨询", oid+"",pid+"",
								(String) retMap.get("paramxml"),
								(String) retMap.get("prepayxml"), "",(String)retMap.get("out_trade_no"));
					} catch (Exception e) {

					}
				}else{
					map.put("pay", "false");
				}
			}else{
				//远程订单
				if(ds!=null&&ds.getIsOpen().equals(1)){
					//Float  money=ds.getAmount().floatValue();
					SystemServiceInfo ssi = commonService.querySystemServiceById(ds.getServiceId());
					Float money = ds.getShowPrice() != null?ds.getShowPrice().floatValue():
						commonManager.processD2DVedioMoney(ds.getAmount(), BigDecimal.valueOf(Double.valueOf(ssi.getPriceParameter()))).floatValue();
					UUID uuid = UUID.randomUUID();
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
									PropertiesUtil.getString("PayCallBackUrl") + "d2p/paynotify",
							null, oid, ret);
					Integer pid=payOrderManager.savePayInfo(4, oid, retMap.get("out_trade_no").toString(), money, 2, money, 0.0f, 0.0f, 0.0f,null);
					map.put("code_url", retMap.get("code_url"));
					map.put("out_trade_no", retMap.get("out_trade_no"));
					map.put("money", money);
					try {
						RecordLogUtil.insert("4", "远程门诊", oid+"",pid+"",
								(String) retMap.get("paramxml"),
								(String) retMap.get("prepayxml"), "",(String)retMap.get("out_trade_no"));
					} catch (Exception e) {

					}
				}else{
					map.put("pay", "false");
				}	
			}
		}		
		return map;
	}
	//==============================================================预约转诊下单开始====================================================
	/**
	 * 判断该医生所在医院是否在某个医联体中
	 * 访问：http://localhost:8080/docadmin/tellexistylt
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/tellexistylt")
	@ResponseBody
	public Map<String,Object> tellexistylt(HttpServletRequest request,HttpSession session){
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		return doctorAdminManager.tellexistylt(user.getId());
	}
	/**
	 * 获取医生所在医院加入的医联体
	 *  访问：http://localhost:8080/docadmin/gainhoshealthbydoc
	 */
	@RequestMapping(value="/gainhoshealthbydoc")
	@ResponseBody
	public Map<String,Object> gainhoshealthbydoc(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		List<HospitalHealthAlliance> hhas=doctorAdminManager.gainHospitalHealthAllianceByHosId(user.getId());
		map.put("hhas", hhas);
		return map;
	}
	
	/**
	 * 获取医联体中医院成员
	 * 访问：http://localhost:8080/docadmin/gainhoshealthmember
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainhoshealthmember")
	@ResponseBody
	public Map<String,Object> gainhoshealthmember(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer hhaId=Integer.parseInt(request.getParameter("hhaId"));//医联体id
		List<HospitalDetailInfo> hospitals=doctorAdminManager.gainhoshealthmember(hhaId);
		map.put("members", hospitals);
		return map;
	}
	/**
	 * 根据医院id获取科室
	 *  访问：http://localhost:8080/docadmin/gaindepsbyhos
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gaindepsbyhos")
	@ResponseBody
	public Map<String,Object> gaindepsbyhos(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer hosId=Integer.parseInt(request.getParameter("hosId"));
		List<HospitalDepartmentInfo> departs=commonService.queryHospitalDepartmentInfosByHosId(hosId);
		map.put("departs", departs);
		return map;
	}
	/**
	 * 基本信息填写完点击下一步 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/basicnext")
	@ResponseBody
	public Map<String,Object> basicnext(HttpServletRequest request,HttpSession session) throws Exception{	
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		return doctorAdminManager.basicnext(user.getId(),user.getUserType(),request);
	}
	/**
	 * 最终提交预约转诊订单， 更新病例，更新pacs数据
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/subreferralorder", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> subreferralorder(HttpServletRequest request,
			HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		String uuid= request.getParameter("uuid");
		// 更新病例
		updatecaseinfo(oid,10,request);
		pacsInfoStorage(uuid, request);
		processHosAdminMsg(10,oid);
		return map;
	}
	@RequestMapping(value="/testSend")
	@ResponseBody
	public void testSend(){
		processHosAdminMsg(10,114);
	}
	//==============================================================预约转诊下单结束====================================================
	/**
	 * 接诊方医院管理员接收短信及消息
	 * @param otype
	 * @param oid
	 */
	private void processHosAdminMsg(Integer otype,Integer oid) {
		Integer hosId = null;
		String  content = "";
		String uuid = "";
		if(otype.equals(10)){
			//预约转诊
			BusinessD2dReferralOrder order = d2pService.queryd2dreferralOrderbyId(oid);
			uuid = order.getUuid();
			if(order.getReferralDocId() == null ){
				Integer docId = order.getDoctorId();
				MobileSpecial doc = commonService.queryMobileSpecialByUserIdAndUserType(docId);
				//需要分诊
				hosId = order.getReferralHosId();
				Integer refType = order.getReferralType();//0：门诊，1：住院
				String  refContent = refType.equals(1)?"住院":"门诊";
				//您收到一个@@的@转诊申请，请尽快登陆医院管理员账号安排本院医生接收。【佰医汇】
				content = "您收到一个"+doc.getHosName()+" "+doc.getDepName()+" "+doc.getSpecialName()+" 的"+refContent
						+"转诊申请，请尽快登陆医院管理员账号安排本院医生接收。【佰医汇】";
			}
		}else if(otype.equals(4)){
			//视频会诊
			BusinessVedioOrder vorder = commonService.queryBusinessVedioOrderById(oid);
			uuid = vorder.getUuid();
			if(vorder.getExpertId() == null ){
				//没有选择视频会诊对象
				Integer docId = vorder.getLocalDoctorId();
				MobileSpecial doc = commonService.queryMobileSpecialByUserIdAndUserType(docId);
				hosId = vorder.getExpertHospitalId();	
				//您收到一个@@的@转诊申请，请尽快登陆医院管理员账号安排本院医生接收。【佰医汇】
				content = "您收到一个"+doc.getHosName()+" "+doc.getDepName()+" "+doc.getSpecialName()+" 的视频会诊申请，请尽快登陆医院管理员账号安排本院医生接收。【佰医汇】";
			}	
		}
		if(hosId!=null){
			MobileSpecial h_doc = commonService.queryHosAdminMobileSpecialByHosId(hosId);
			if(h_doc!=null&&StringUtils.isNotBlank(h_doc.getMobileTelphone())){
				HttpSendSmsUtil.sendSmsInteface(h_doc.getMobileTelphone(), content);
				processSystemPushInfo(PushCodeEnum.NewOrder.getCode(),uuid,otype,h_doc.getSpecialId(),5,content);
			}
		}
	}
	private void processSystemPushInfo(Integer pushCode,String businessKey,Integer businessType,Integer userId,Integer userType,String content){
		SystemPushInfo pinfo = new SystemPushInfo();
		UUID uuid = UUID.randomUUID();
		pinfo.setPushKey(uuid.toString().replace("-", ""));
		pinfo.setPushCode(pushCode);
		pinfo.setCreateTime(new Date());
		pinfo.setBusinessKey(businessKey);
		pinfo.setBusinessType(businessType);
		pinfo.setUserId(userId);
		pinfo.setUserType(userType);
		pinfo.setContent(content);
		commonService.saveSystemPushInfo(pinfo);
	}
	/**
	 * 获取预约转诊数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gainreferorders", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainreferorders(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		//状态
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));//2：待接诊，3：已接诊， 4：已完成，5：已退诊，6：已取消
		Integer dtype=Integer.parseInt(paramMap.get("dtype"));//类型，发起1，接收2；
		String data = doctorAdminManager.gainreferorders(ostatus, user.getId(),
				sEcho, start, length, searchContent,dtype);
		return data;
	}
	/**
	 * 更改转诊订单的状态 删除或取消
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/changeReferOrderStat")
	@ResponseBody
	public Map<String,Object> changeReferOrderStat(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Integer orderId=Integer.parseInt(request.getParameter("orderId"));
		Integer opType=Integer.parseInt(request.getParameter("opType"));//1--删除，2--取消
		doctorAdminManager.changeReferOrderStat(orderId,opType,user);
		return map;
	}
	
	
	/**
	 * 获取该医生视频会诊数据
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gainVedioOrderDatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainVedioOrderDatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		//状态
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));//
		Integer dtype=Integer.parseInt(paramMap.get("dtype"));//类型，发起1，接收2；
		String data = doctorAdminManager.gainVedioOrderDatas(ostatus, user.getId(),
				sEcho, start, length, searchContent,dtype);
		return data;
	}
	
	/**
	 * 获取该医生--咨询专家数据
	 * 访问：http://localhost:8080/docadmin/gainD2DTuwenDatas
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gainD2DTuwenDatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainD2DTuwenDatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		//状态
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));//
		Integer dtype=Integer.parseInt(paramMap.get("dtype"));//类型，发起1，接收2；
		String data = doctorAdminManager.gainD2DTuwenDatas(ostatus, user.getId(),
				sEcho, start, length, searchContent,dtype);
		return data;
	}
	//======================================================医生to医生 咨询专家下单==================================================
	/**
	 * 专家咨询 填写基本信息下一步提交
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/nextSubAdviceOrder")
	@ResponseBody 
	public Map<String,Object> nextSubAdviceOrder(HttpServletRequest request,HttpSession session) throws Exception{
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Map<String,Object> map=new HashMap<String,Object>();
		map.putAll(doctorAdminManager.nextSubAdviceOrder(request,user));
		return map;
	}
	/**
	 * 最终提交咨询专家订单 去支付，如果需要的话
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/finishSubAdviceOrder")
	@ResponseBody
	public Map<String,Object> finishSubAdviceOrder(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception{
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		SpecialAdviceOrder order = commonService
				.querySpecialAdviceOrderById(oid);
		updatecaseinfo(oid, 5, request);
		pacsInfoStorage(order.getUuid(), request);
		return doctorAdminManager.finishSubAdviceOrder(request,response,user);
	}
	/**
	 * 获取 开通的城市
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainopencitys")
	@ResponseBody
	public Map<String,Object> gainopencitys(HttpServletRequest request){
		String type=request.getParameter("type");//2专家  3：医生  全部为空
		return doctorAdminManager.gainopencitys(type);
	}
	
	/**
	 * 获取系统业务字典
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value="/gainSysDicList/{groupId}")
	@ResponseBody
	public Map<String,Object> gainSysDicList(@PathVariable Integer groupId) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<SystemBusinessDictionary> dics = doctorAdminManager.gainSysDicList(groupId);
		map.put("dictionaries", dics);
		return map;
	}
	
}
