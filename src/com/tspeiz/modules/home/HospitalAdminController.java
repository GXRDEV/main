package com.tspeiz.modules.home;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tspeiz.modules.common.bean.JSONParam;
import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.DepStandardR;
import com.tspeiz.modules.common.entity.newrelease.Dictionary;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.HosAppointOrder;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.UserFeedBackInfo;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAlliance;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAllianceMember;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;
import com.tspeiz.modules.common.service.IApiGetuiPushService;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.manage.CommonManager;
import com.tspeiz.modules.manage.HospitalAdminManager;
import com.tspeiz.modules.manage.SystemAdminManager;
import com.tspeiz.modules.util.ChineseToPinyinUtil;
import com.tspeiz.modules.util.PasswordUtil;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.common.StringRetUtil;
import com.tspeiz.modules.util.date.CalendarUtil;
import com.tspeiz.modules.util.date.RelativeDateFormat;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.oss.OSSManageUtil;

@Controller
@RequestMapping("hospital")
public class HospitalAdminController {
	private Logger log = Logger.getLogger(HospitalAdminController.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat _sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Resource
	private ICommonService commonService;
	@Resource
	private IWeixinService weixinService;
	@Autowired
	private CommonManager commonManager;
	@Autowired
	private HospitalAdminManager hospitalAdminManager;
	@Autowired
	private SystemAdminManager systemAdminManager;
	// 进入首页
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession().getAttribute("user");
		MobileSpecial hosinfo = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		//查询未完成的订单数
		Integer uncompnum=commonService.queryUnCompletedNum(hosinfo.getHosId());
		map.put("uncompnum", uncompnum);
		return new ModelAndView("hos/main",map);
	}

	// 院方订单管理
	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public ModelAndView experhz(HttpServletRequest request) {
		return new ModelAndView("hos/order_list");
	}

	// 查询该院远程订单
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
		String currorhis = paramMap.get("currorhis");// current,history
		StringBuilder stringJson = null;
		List<BusinessVedioOrder> _rcs = null;
		MobileSpecial hosinfo = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());

		if (StringUtils.isNotBlank(currorhis)
				&& currorhis.equalsIgnoreCase("history")) {

			Map<String, Object> retmap = weixinService
					.queryRemoteConsulationsByConditions_hos(
							hosinfo.getHosId(), searchContent, "desc", start,
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
					.queryRemoteConsulationsByConditions_hos(
							hosinfo.getHosId(), searchContent, "", com_start,
							com_length, "40", sdf.format(new Date()));
			Integer _renum = (Integer) _retmap.get("num");
			_rcs = (List<BusinessVedioOrder>) _retmap.get("items");
			if (start.equals(0)) {
				if (_renum > 0)
					length = length - _renum;
			} else {
				if (_renum > 0)
					start = start - 2;
			}
			Map<String, Object> retmap = weixinService
					.queryRemoteConsulationsByConditions_hos(
							hosinfo.getHosId(), searchContent, "", start,
							length, "10,20", null);
			Integer renum = (Integer) retmap.get("num");
			List<BusinessVedioOrder> rcs = (List<BusinessVedioOrder>) retmap
					.get("items");
			if (_rcs == null)
				_rcs = new ArrayList<BusinessVedioOrder>();
			_rcs.addAll(rcs);
			stringJson = new StringBuilder("{\"sEcho\":" + sEcho
					+ ",\"iTotalRecords\":" + renum
					+ ",\"iTotalDisplayRecords\":" + (_renum + renum)
					+ ",\"aaData\":[");
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
			if(StringUtils.isNotBlank(rc.getConsultationDate())){
				time+=rc.getConsultationDate();
			}
			if(StringUtils.isNotBlank(rc.getConsultationTime())){
				time+=" "+rc.getConsultationTime();
			}
			String docname = "";
			if (!(rc.getLocalDoctorId() == null)) {
				MobileSpecial doc = commonService
						.queryMobileSpecialByUserIdAndUserType(rc
								.getLocalDoctorId());
				docname = doc.getSpecialName();
			}
			stringJson.append("\"" + rc.getLocalDepName() + "\",");
			stringJson.append("\"" + docname + "\",");
			stringJson.append("\""
					+ StringRetUtil.gainStringByStats_new(rc.getStatus(),
							rc.getProgressTag()) + "\",");
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
			stringJson.append("\"" + rc.getPatientName() + "\",");
			
			stringJson.append("\"" + rc.getAge() + "\",");
			stringJson.append("\"" +(rc.getSex().equals(1)?"男":"女")+ "\",");
			stringJson.append("\"" + rc.getTelephone() + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(rc.getExpertName()) ? rc
							.getExpertName() : "未配置") + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(rc.getHosName()) ? rc
							.getHosName() : "未配置") + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(rc.getDepName()) ? rc
							.getDepName() : "未配置") + "\",");
			stringJson.append("\"" + (rc.getPayStatus()!=null?(rc.getPayStatus().equals(1)?"已支付":"未支付"):"未支付") + "\",");
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

	private Map<String, String> convertToMap(JSONParam[] params) {
		Map<String, String> map = new HashMap<String, String>();
		if (params != null && params.length > 0) {
			for (JSONParam jsonParam : params) {
				map.put(jsonParam.getName(), jsonParam.getValue());
			}
		}
		return map;
	}

	// 订单分配
	@RequestMapping(value = "/gainrelatevedocs", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> gainrelatevedocs(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(oid);
		List<MobileSpecial> docs = weixinService.queryDistributeDocs(
				order.getLocalHospitalId(), order.getLocalDepartId());
		map.put("docs", docs);
		return map;
	}

	// 分配医生
	@RequestMapping(value = "/distribute", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> distribute(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		Integer docid = Integer.parseInt(request.getParameter("docid"));
		BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(oid);
		map.put("status", "success");
		order.setLocalDoctorId(docid);
		commonService.updateBusinessVedioOrder(order);
		HospitalDetailInfo detail=commonService.getHospitalDetailInfoById(order.getLocalHospitalId());
		commonManager.generateSystemPushInfo(21, order.getUuid(), 4, order.getExpertId(), 2,null, "来自"+detail.getDisplayName()+"的会诊订单，已被分派医生。");
		//维护群组
		commonManager.joinGroup(order.getUuid(), 4);
		return map;
	}

	// 院方医生管理
	@RequestMapping(value = "/doctors", method = RequestMethod.GET)
	public ModelAndView doctors(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		MobileSpecial hosinfo = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		List<HospitalDepartmentInfo> departs = weixinService
				.queryHospitalDepartmentssByHosId(hosinfo.getHosId());
		map.put("departs", departs);
		List<Dictionary> dutys=weixinService.queryDictionarysByParentId(2);//{教授。。}
		List<Dictionary> profs=weixinService.queryDictionarysByParentId(1);//{主任。。。}
		map.put("dutys", dutys);
		map.put("profs", profs);
		return new ModelAndView("hos/doctor_list", map);
	}

	// 获取医院医生数据
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaindoctors", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindoctors(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		StringBuilder stringJson = null;
		MobileSpecial hosinfo = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		Map<String, Object> retmap = weixinService.queryHospitalNursesByHosId(
				hosinfo.getHosId(), searchContent, start, length, 3);
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
			stringJson.append("\"" + rc.getDepName() + "\",");
			stringJson.append("\"" + rc.getSpecialName() + "\",");
			stringJson.append("\"" + rc.getTelphone() + "\",");
			stringJson.append("\"" + _sdf.format(rc.getRegisterTime()) + "\",");
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

	// 新增医生
	@RequestMapping(value = "/savedoctor", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> savedoctor(HttpServletRequest request)throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		MobileSpecial hosinfo = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		String tel = request.getParameter("telphone");
		String uname = request.getParameter("username");
		String depid = request.getParameter("depid");
		if(!StringUtils.isNotBlank(tel)){
			map.put("status", "error");
			map.put("msg", "请输入联系电话");
			return map;
		}
		// 判断是否该号码已注册
		DoctorRegisterInfo regist = commonService.queryDoctorRegisterInfoByTel(
				tel, 3);
		if (regist == null) {
			// register
			regist = new DoctorRegisterInfo();
			regist.setLoginName(tel);
			regist.setPassword(PasswordUtil.MD5Salt("123456"));
			regist.setSalt("cvYl8U");
			regist.setMobileNumber(tel);
			regist.setRegisterTime(new Timestamp(new Date().getTime()));
			regist.setLastLoginTime(new Timestamp(new Date().getTime()));
			regist.setUserType(3);
			regist.setStatus(1);
			Integer _id = commonService.saveDoctorRegisterInfo(regist);
			System.out.println("====regist:" + _id);
			DoctorDetailInfo detail = new DoctorDetailInfo();
			detail.setId(_id);
			detail.setDisplayName(uname);
			detail.setRefreshTime(new Timestamp(new Date().getTime()));
			detail.setStatus(1);
			detail.setHospitalId(hosinfo.getHosId());
			if (StringUtils.isNotBlank(depid))
				detail.setDepId(Integer.parseInt(depid));
			detail.setIdCardNo(request.getParameter("idCardNo"));//身份证
			detail.setHeadImageUrl(request.getParameter("headImageUrl"));//头像地址
			detail.setBadgeUrl(request.getParameter("badgeUrl"));
			detail.setProfile(request.getParameter("profile"));//个人简介
			detail.setDuty(request.getParameter("dutyName"));//职务
			String dutyId=request.getParameter("dutyId");
			if(StringUtils.isNotBlank(dutyId)){
				detail.setDutyId(Integer.parseInt(dutyId));
			}
			detail.setProfession(request.getParameter("profession"));//职称
			detail.setSpeciality(request.getParameter("speciality"));
			/*String famousDoctor=request.getParameter("famousDoctor");
			if(StringUtils.isNotBlank(famousDoctor)){
				detail.setFamousDoctor(Integer.parseInt(famousDoctor));//是否名医显示
			}*/
			//detail.setOutTime(request.getParameter("outTime"));//出诊时间  (三:上午,四:下午,五:全天)
			String url=PropertiesUtil.getString("DOMAINURL") + "module/patient.html#/docinfo/"+ _id;//进入主界面
			String erweimaUrl = OSSManageUtil.genErweima(
					ChineseToPinyinUtil.getPingYin(detail.getDisplayName()),
					url);
			detail.setErweimaUrl(erweimaUrl);
			commonService.saveDoctorDetailInfo(detail);
			sendExpertSms(regist.getLoginName(),detail.getDisplayName());
			map.put("status", "success");
		} else {
			map.put("status", "error");// 已注册
			map.put("msg", "该联系电话已注册");
		}
		return map;
	}
	
	private void sendExpertSms(String telphone,String uname){
		String content=""+uname+"医生，您在佰医汇的医师认证已通过审核，现在可以正常使用全部服务功能，请登录佰医汇APP体验。如有疑问请拨打客服热线400-890-5111 【佰医汇】";
		HttpSendSmsUtil.sendSmsInteface(telphone, content);
	}
	
	/**
	 * 获取医生信息
	 * @param docid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/queryDoctorInfo/{docid}")
	@ResponseBody 
	public Map<String,Object> queryDoctorInfo(@PathVariable Integer docid,HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(docid);
		map.put("doc", doc);
		return map;
	}
	
	/**
	 * 更新医生信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateDoctorInfo")
	@ResponseBody 
	public Map<String,Object> updateDoctorInfo(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer docid=Integer.parseInt(request.getParameter("docid"));//医生id
		DoctorDetailInfo detail=commonService.queryDoctorDetailInfoById(docid);
		detail.setDepId(Integer.parseInt(request.getParameter("depid")));//科室id
		detail.setHeadImageUrl(request.getParameter("headImageUrl"));//头像地址
		detail.setBadgeUrl(request.getParameter("badgeUrl"));
		detail.setProfile(request.getParameter("profile"));//个人简介
		detail.setDuty(request.getParameter("dutyName"));//职务
		detail.setDutyId(StringUtils.isNotBlank(request.getParameter("dutyId"))?Integer.parseInt(request.getParameter("dutyId")):null);
		detail.setSpeciality(request.getParameter("speciality"));
		detail.setProfession(request.getParameter("profession"));//职称
		detail.setIdCardNo(request.getParameter("idCardNo"));//身份证
		commonService.updateDoctorDetailInfo(detail);
		String telphone=request.getParameter("telphone");
		if(StringUtils.isNotBlank(telphone)){
			DoctorRegisterInfo reg=commonService.queryDoctorRegisterInfoById(docid);
			reg.setLoginName(telphone);
			reg.setMobileNumber(telphone);
			commonService.updateDoctorRegisterInfo(reg);
		}
		commonManager.generateSystemPushInfo(51, null, null, detail.getId(), 3,null, "您的信息已被医院管理员更改");
		return map;
	}
	
	

	// 院方护士管理
	@RequestMapping(value = "/nurses", method = RequestMethod.GET)
	public ModelAndView nurseadmin(HttpServletRequest request) {
		return new ModelAndView("hos/nurse_list");
	}

	// 获取护士数据
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gainnurses", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainnurses(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		StringBuilder stringJson = null;
		MobileSpecial hosinfo = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		Map<String, Object> retmap = weixinService.queryHospitalNursesByHosId(
				hosinfo.getHosId(), searchContent, start, length, 4);
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
			stringJson.append("\"" + rc.getSpecialName() + "\",");
			stringJson.append("\"" + rc.getTelphone() + "\",");
			stringJson.append("\"" + rc.getRegisterTime() + "\",");
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

	// 新增护士
	@RequestMapping(value = "/addnurse", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> addnurse(HttpServletRequest request) {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		MobileSpecial hosinfo = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		Map<String, Object> map = new HashMap<String, Object>();
		String tel = request.getParameter("telphone");
		String uname = request.getParameter("username");
		// 判断是否该号码已注册
		DoctorRegisterInfo regist = commonService.queryDoctorRegisterInfoByTel(
				tel, 4);
		if (regist == null) {
			// register
			regist = new DoctorRegisterInfo();
			regist.setLoginName(tel);
			regist.setPassword(PasswordUtil.MD5Salt("123456"));
			regist.setMobileNumber(tel);
			regist.setRegisterTime(new Timestamp(new Date().getTime()));
			regist.setLastLoginTime(new Timestamp(new Date().getTime()));
			regist.setUserType(4);
			regist.setStatus(1);
			Integer _id = commonService.saveDoctorRegisterInfo(regist);
			DoctorDetailInfo detail = new DoctorDetailInfo();
			detail.setId(_id);
			detail.setDisplayName(uname);
			detail.setRefreshTime(new Timestamp(new Date().getTime()));
			detail.setStatus(1);
			detail.setHospitalId(hosinfo.getHosId());
			commonService.saveDoctorDetailInfo(detail);
			map.put("status", "success");
		} else {
			map.put("status", "error");// 已注册
		}
		return map;
	}

	// 科室管理
	@RequestMapping(value = "/departments", method = RequestMethod.GET)
	public ModelAndView departments(HttpServletRequest requets) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<StandardDepartmentInfo> stands = weixinService
				.queryStandardDepartments();
		map.put("stands", stands);
		return new ModelAndView("hos/depart_list", map);
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gaindeparts", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindeparts(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		// 查询科室
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		MobileSpecial hosinfo = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = weixinService
				.queryHospitalDepartmentssByHosId(hosinfo.getHosId(),
						search, start, length,ostatus);
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
			stringJson.append("\"" + dep.getId() + "\",");
			stringJson.append("\"" + dep.getDisplayName() + "\",");
			stringJson.append("\""
					+ (dep.getLocalDepId() == null ? "" : dep.getLocalDepId())
					+ "\",");
			// 标准科室
			String standardstr = weixinService.queryStandardDepartsByDepId(
					dep.getId(), 1);
			stringJson.append("\"" + standardstr + "\",");
			String status = dep.getStatus().equals(1) ? "已上线" : "已下线";
			stringJson.append("\"" + status + "\",");
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

	@RequestMapping(value="/changeDepStatus")
	@ResponseBody
	public Map<String,Object> changeDepStatus(HttpServletRequest request){
		Integer depId=Integer.parseInt(request.getParameter("depId"));
		HospitalDepartmentInfo dep=weixinService.queryHospitalDepartmentInfoById(depId);
		dep.setStatus(Integer.parseInt(request.getParameter("sval")));
		weixinService.updateHospitalDepartmentInfo(dep);
		return null;
	}
	
	@RequestMapping(value="/gaindepartdetail",method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> gaindepartdetail(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer depid=Integer.parseInt(request.getParameter("depid"));
		HospitalDepartmentInfo dep=weixinService.queryHospitalDepartmentInfoById(depid);
		map.put("dep", dep);
		return map;
	}
	
	// 新增科室
	@RequestMapping(value = "/saveorupdatedepart", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveorupdatedepart(HttpServletRequest request) {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		MobileSpecial hosinfo = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		Map<String, Object> map = new HashMap<String, Object>();
		String depid = request.getParameter("depid");
		String displayName = request.getParameter("displayName");
		String introduction=request.getParameter("introduction");
		String introductionTxt=request.getParameter("introductionTxt");
		String depIcon=request.getParameter("depIcon");
		String localDepId = request.getParameter("localDepId");
		if (StringUtils.isNotBlank(depid)) {
			// 编辑
			HospitalDepartmentInfo dep = weixinService
					.queryHospitalDepartmentInfoById(Integer.parseInt(depid));
			dep.setDisplayName(displayName);
			if (StringUtils.isNotBlank(localDepId)) {
				dep.setLocalDepId(Integer.parseInt(localDepId));
			}
			dep.setIntroduction(introduction);
			dep.setIntroductionTxt(introductionTxt);
			dep.setDepIcon(depIcon);
			weixinService.updateHospitalDepartmentInfo(dep);
		} else {
			// 新增
			HospitalDepartmentInfo dep = new HospitalDepartmentInfo();
			dep.setDisplayName(displayName);
			dep.setHospitalId(hosinfo.getHosId());
			if (StringUtils.isNotBlank(localDepId)) {
				dep.setLocalDepId(Integer.parseInt(localDepId));
			}
			dep.setStatus(1);
			dep.setIntroduction(introduction);
			dep.setIntroductionTxt(introductionTxt);
			dep.setDepIcon(depIcon);
			weixinService.saveHospitalDepartmentInfo(dep);
		}
		return map;
	}

	// 设置医院科室与标准科室的对应关系
	@RequestMapping(value = "/depstandset", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> depstandset(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sdepids = request.getParameter("depids");
		String depid = request.getParameter("depid");
		if (StringUtils.isNotBlank(sdepids)) {
			String[] _sds = sdepids.split(",");
			if (_sds != null && _sds.length > 0) {
				for (String _sd : _sds) {
					DepStandardR dr = weixinService
							.queryDepStandardRByConditions(
									Integer.parseInt(_sd),
									Integer.parseInt(depid), 1);
					if (dr == null) {
						dr = new DepStandardR();
						dr.setDepId(Integer.parseInt(depid));
						dr.setStandardDepId(Integer.parseInt(_sd));
						dr.setDepartmentType(1);
						weixinService.saveDepStandardR(dr);
					}
				}
			}
		}
		return map;
	}

	// 重置密码
	@RequestMapping(value = "/passreset", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> passreset(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		DoctorRegisterInfo reg = commonService.queryDoctorRegisterInfoById(uid);
		reg.setPassword(PasswordUtil.MD5Salt("123456"));
		commonService.updateDoctorRegisterInfo(reg);
		return map;
	}

	// 图表数据
	// 每周订单总数
	@RequestMapping(value = "/ordersbyweek", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> ordersbyweek(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		MobileSpecial hosinfo = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		Integer hosid = hosinfo.getHosId();
		List<Integer> ordernums = new ArrayList<Integer>();
		// 查询每个月的患者人数
		Map<String, Object> weekdatas = CalendarUtil.gainWeeksData(12);
		List<String> categorys = (List<String>) weekdatas.get("categorys");
		List<String> dates = (List<String>) weekdatas.get("dates");
		for (String string : dates) {
			String[] _dates = string.split(";");
			Integer count = commonService.queryOrdersNumByConditions(hosid,
					null, _dates[0], _dates[1]);
			ordernums.add(count);
		}
		map.put("categorys", categorys);
		map.put("ordernums", ordernums);
		return map;
	}

	@RequestMapping(value = "/ordersbydep", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> orersbydep(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询科室
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Integer stype = Integer.parseInt(request.getParameter("stype"));// 1--周，2--月，3--总订单数
		MobileSpecial hosinfo = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		Integer hosid = hosinfo.getHosId();
		List<HospitalDepartmentInfo> departs = weixinService
				.queryCoohospitalDepartmentsByCooHos(hosid);
		List<String> categorys = new ArrayList<String>();
		List<Integer> ordernums = new ArrayList<Integer>();
		Integer number = 0;
		Map<String, Object> _datemap = gainDateByType(stype);
		for (HospitalDepartmentInfo dep : departs) {
			// 查询各科室订单数Integer
			number = commonService.queryOrdersNumByConditions(hosid, dep
					.getId(), _datemap.get("begin").toString(),
					_datemap.get("end").toString());
			ordernums.add(number);
			categorys.add(dep.getDisplayName());
		}
		map.put("ordernums", ordernums);
		map.put("categorys", categorys);
		return map;
	}

	private Map<String, Object> gainDateByType(Integer stype) {
		Map<String, Object> map = new HashMap<String, Object>();
		String begin = "";
		String end = "";
		Calendar cal = Calendar.getInstance();
		String datestr = "";
		switch (stype) {
		// //1--周，2--月，3--总订单数
		case 1:
			datestr = CalendarUtil.gainCurrentWeekDate(cal);
			break;
		case 2:
			datestr = CalendarUtil.gainCurrentMonthDate();
			break;
		case 3:
			break;
		}
		if (StringUtils.isNotBlank(datestr)) {
			String[] _dstr = datestr.split(";");
			begin = _dstr[0];
			end = _dstr[1];
		}
		map.put("begin", begin);
		map.put("end", end);
		return map;
	}
	
	//远程门诊订单来源饼图
	@RequestMapping(value="/gainOrderSources",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> gainOrderSources(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		MobileSpecial hosinfo = commonService
				.queryMobileSpecialByUserIdAndUserType(user.getId());
		Integer totalCount = commonService.queryTotalOrders(hosinfo.getHosId());
		Map<Integer,String> _groups=new HashMap<Integer,String>();
		_groups.put(1, "微信公众号");
		_groups.put(2, "佰医汇官网");
		_groups.put(3, "窗口护士");
		_groups.put(4, "医生辅助");
		Map<String, Double> retMap = new HashMap<String, Double>();
		if (totalCount != null && !totalCount.equals(0)) {
			for(Integer type:_groups.keySet()){
				Integer num=commonService.queryOrderNumbyType(hosinfo.getHosId(),type);
				retMap.put(_groups.get(type), Double.valueOf(getPercent(num, totalCount)));
			}
			map.put("status", "success");
			map.put("areaData", retMap);
			System.out.println(retMap.toString());
		} else {
			retMap.put("暂无数据", 1.0);
			map.put("areaData", retMap);
		}
		return map;
	}
	
	private String getPercent(int x, int total) {
		Format format = new DecimalFormat("0.0");
		return format.format(((double) x / total) * 100);
	}
	
	@RequestMapping(value="/updateorderstatus",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> updateorderstatus(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(Integer.parseInt(request.getParameter("oid")));
		String val=request.getParameter("val");
		if(val.equalsIgnoreCase("cancel")){
			order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey());
		}else if(val.equalsIgnoreCase("complete")){
			order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey());
			order.setProgressTag(null);
			//记录消息
			commonManager.saveBusinessMessageInfo_sys(order.getId(), 4, "text", "医院管理人员结束订单",null,null);
		}
		commonService.updateBusinessVedioOrder(order);
		return map;
	}
	
	
	//--------------------------------------微信公号方面的开发=============
	//查看订单
	@RequestMapping(value="/wxplusorders",method=RequestMethod.GET)
	public ModelAndView wxplusorders(HttpServletRequest request){
		
		return new ModelAndView("hos/wx/plus_orders");
	}
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gaindwxplus", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindwxplus(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		// 查询科室
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = weixinService
				.queryHosWxPlusOrders(user.getId(),
						searchContent, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<HosAppointOrder> orders = (List<HosAppointOrder>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		HosAppointOrder order = null;
		for (int i = 0; i < orders.size(); i++) {
			order = orders.get(i);
			stringJson.append("[");
			stringJson.append("\"" + order.getId() + "\",");
			stringJson.append("\"" + order.getPatientName()+ "\",");
			stringJson.append("\"" + order.getTelphone()+ "\",");
			stringJson.append("\"" + order.getDepName() + "\",");
			stringJson.append("\"" + order.getAppointDate() + "\",");
			stringJson.append("\"" + (order.getTimeType().equalsIgnoreCase("am")?"上午":"下午") + "\",");
			stringJson.append("\"" + order.getRegMessage() + "\",");
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
	
	//-----基本设置---------------------
	@RequestMapping(value="/basicconfig",method=RequestMethod.GET)
	public ModelAndView basicconfig(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		MobileSpecial special=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(special.getHosId());
		//医院的院徽设置 hospitalLogo
		map.put("hos", hos);
		//公号的设置 appid 密钥
		WeiAccessToken wt=weixinService.queryWeiAccessTokenByDocId(user.getId());
		map.put("wt", wt);
		System.out.println(JSONObject.fromObject(wt).toString());
		return new ModelAndView("hos/wx/basic_config",map);
	}
	
	/**
	 * 院徽设置
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/hosbasic",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> hosbasic(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		MobileSpecial special=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(special.getHosId());
		String logo=request.getParameter("logourl");
		if(StringUtils.isNotBlank(logo)){
			hos.setHospitalLogo(logo);
		}
		String introduction=request.getParameter("introduction");
		if(StringUtils.isNotBlank(introduction)){
			hos.setHospitalIntroduction(introduction);
		}
		weixinService.updateHospitalDetailInfo(hos);
		return map;
	}
	/**
	 *  公号及商户基本配置
	 * @param request
	 * @return
	 */
	 
	@RequestMapping(value="/wxbasic",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> wxbasic(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		String appid=request.getParameter("appid");
		String appsecret=request.getParameter("appsecret");//公号密钥
		String partner=request.getParameter("partner");//商户
		String partnerKey=request.getParameter("partnerKey");//商户密钥
		WeiAccessToken wat=weixinService.queryWeiAccessTokenByDocId(user.getId());
		if(wat==null){
			wat=new WeiAccessToken();
			wat.setDocId(user.getId());
			wat.setAppId(appid);
			wat.setAppsecret(appsecret);
			wat.setPartner(partner);
			wat.setPartnerKey(partnerKey);
			weixinService.saveWeiAccessToken(wat);
		}else{
			wat.setAppId(appid);
			wat.setAppsecret(appsecret);
			wat.setPartner(partner);
			wat.setPartnerKey(partnerKey);
			weixinService.updateWeiAccessToken(wat);
		}
		return map;
	}
	
	@RequestMapping(value="/userfeedbacks")
	public ModelAndView userfeedbacks(HttpServletRequest request){
		return new ModelAndView("hos/feedbacks");
	}
	
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gaindfeedbacks", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaindfeedbacks(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = weixinService
				.queryHosWxFeedbacks(user.getId(),
						searchContent, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<UserFeedBackInfo> orders = (List<UserFeedBackInfo>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		UserFeedBackInfo order = null;
		for (int i = 0; i < orders.size(); i++) {
			order = orders.get(i);
			stringJson.append("[");
			stringJson.append("\"" + order.getId() + "\",");
			stringJson.append("\"" + _sdf.format(order.getCreateTime())+ "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(order.getTelphone())?order.getTelphone():"")+ "\",");
			stringJson.append("\"" + order.getContent()+ "\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		log.info("====json==" + stringJson.toString());
		return stringJson.toString();
	}
	
	@RequestMapping(value="/delorder")
	@ResponseBody
	public Map<String,Object> delorder(HttpServletRequest request){
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(oid);
		order.setDelFlag(1);
		commonService.updateBusinessVedioOrder(order);
		return null;
	}
	/**
	 * 医联体管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/hoshealthmanage")
	public ModelAndView hoshealthmanage(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		MobileSpecial ms=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		Integer hosId=ms.getHosId();
		HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(hosId);
		map.put("hosId",hos.getId());
		map.put("hosLevel", hos.getHosLevel());
		map.put("hosName", hos.getDisplayName());
		if(hos.getHospitalLevel()!=null&&hos.getHospitalLevel().equals(10)){
			//三甲可以组建医联体
			HospitalHealthAlliance hha=commonService.queryHospitalHealthAllianceByRegId(user.getId());
			if(hha!=null){
				map.put("cancreatylt", "false");//已创建过医联体
			}else{
				map.put("cancreatylt", "true");//可以创建医联体
			}
			map.put("appjoin", "false");//三甲不可以申请加入医联体
		}else{
			map.put("cancreatylt", "false");//不是三甲不可以创建医联体
			map.put("appjoin", "true");//非三甲可以申请加入医联体
		}
		return new ModelAndView("hos/hos_health_manage",map);
	}
	/**
	 * 医院管理---我的医联体数据
	 * @param params
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gainmyyltdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainmyyltdatas(@RequestBody JSONParam[] params,HttpServletRequest request,HttpSession session) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		MobileSpecial ms=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		Integer hosId=ms.getHosId();
		return  hospitalAdminManager.gainmyyltdatas(hosId,paramMap);
	}
	
	
	/**
	 * 三甲医院组建医联体
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/orghoshealthalliance")
	@ResponseBody
	public Map<String,Object> orghoshealthalliance(HttpServletRequest request,HttpSession session){
		DoctorRegisterInfo user = (DoctorRegisterInfo) session.getAttribute("user");
		Map<String,Object> map=new HashMap<String,Object>();
		hospitalAdminManager.savehoshealth(request,user);
		return map;
	}
	/**
	 * 获取所有的医联体
	 * @return
	 */
	@RequestMapping(value="/gainallylts")
	@ResponseBody
	public Map<String,Object> gainallylts(){
		Map<String,Object> map=new HashMap<String,Object>();
		List<HospitalHealthAlliance> ylts=commonService.queryHospitalHealthAlliances_audited();
		map.put("ylts", ylts);
		return map;
	}
	/**
	 * 根据选择的医联体 获取上级医院
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainhighlevelhos")
	@ResponseBody
	public Map<String,Object> gainhighlevelhos(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer hosId=Integer.parseInt(request.getParameter("hosId"));//申请医院id
		Integer allianceId=Integer.parseInt(request.getParameter("allianceId"));//选择的医联体id
		HospitalHealthAlliance hha=commonService.queryHospitalHealthAllianceById(allianceId);
		HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(hha.getHospitalId());
		HospitalDetailInfo local_hos=weixinService.queryHospitalDetailInfoById(hosId);
		map.put("hos", hos);//hos.displayName,hos.hosLevel   核心医院
		map.put("local_hos", local_hos);//hos.displayName,hos.hosLevel   当前医院
		map.put("hospitals", hospitalAdminManager.gainhighlevelhos(hosId,allianceId));
		return map;
	}
	
	/**
	 * 申请加入医联体
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/appjoinylt")
	@ResponseBody
	public Map<String,Object> appjoinylt(HttpServletRequest request,HttpSession session){
		DoctorRegisterInfo user = (DoctorRegisterInfo) session.getAttribute("user");
		MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(doc.getHosId());
		Map<String,Object> map=new HashMap<String,Object>();
		Integer yltId=Integer.parseInt(request.getParameter("yltId"));
		Integer parentHosId=Integer.parseInt(request.getParameter("parentHosId"));
		hospitalAdminManager.appjoinylt(yltId,user.getId(),user.getUserType(),hos,parentHosId);
		return map;
	}
	
	/**
	 * 某医联体详情--弹出
	 * @param yltId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/yltdetailNew/{yltId}")
	@ResponseBody
	public Map<String,Object> yltdetailNew(@PathVariable Integer yltId,HttpServletRequest request,HttpSession session){
		DoctorRegisterInfo user = (DoctorRegisterInfo) session.getAttribute("user");
		MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(doc.getHosId());//本医院 hos.Id,hos.DisplayName,hos.hosLevel
		Map<String,Object> map=new HashMap<String,Object>();
		HospitalHealthAlliance alliance=commonService.queryHospitalHealthAllianceById(yltId);
		HospitalDetailInfo mainHos=weixinService.queryHospitalDetailInfoById(alliance.getHospitalId());
		alliance.setMainHosName(mainHos.getDisplayName());
		map.put("alliance", alliance);//医联体数据
		map.put("hos", hos);//本院数据
		map.put("mainHos", mainHos);//核心医院
		map.put("docName",doc.getSpecialName());//邀请人
		return map;
	}
	
	/**
	 * 某医联体详情
	 * @param yltId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/yltdetail/{yltId}")
	public ModelAndView yltdetail(@PathVariable Integer yltId,HttpServletRequest request,HttpSession session){
		DoctorRegisterInfo user = (DoctorRegisterInfo) session.getAttribute("user");
		MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(doc.getHosId());//本医院 hos.Id,hos.DisplayName,hos.hosLevel
		Map<String,Object> map=new HashMap<String,Object>();
		HospitalHealthAlliance alliance=commonService.queryHospitalHealthAllianceById(yltId);
		map.put("alliance", alliance);//医联体数据
		map.put("hos", hos);//本院数据
		map.put("docName",doc.getSpecialName());//邀请人
		return new ModelAndView("hos/hos_health_detail",map);
	}
	/**
	 * 获取下级成员
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/gainlowermembers")
	@ResponseBody
	public Map<String,Object> gainlowermembers(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session.getAttribute("user");
		MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(doc.getHosId());
		Integer yltId=Integer.parseInt(request.getParameter("yltId"));//医联体id
		HospitalHealthAlliance alliance=commonService.queryHospitalHealthAllianceById(yltId);
		List<HospitalHealthAllianceMember> members=commonService.queryHospitalHealAllianceMembersByCon(alliance.getUuid(), null, hos.getId());
		map.put("members", members);
		return map;
	}
	/**
	 * 删除医联体成员
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/dellowermembers")
	@ResponseBody
	public Map<String,Object> dellowermembers(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String memberIds=request.getParameter("memberIds");//成员ids 以逗号隔开
		hospitalAdminManager.dellowermembers(memberIds);
		map.put("status", "success");
		return map;
	}
	/**
	 * 获取医联体结构 树形表格数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainyltdetaildatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Object gainyltdetaildatas(HttpServletRequest request,HttpSession session){
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		MobileSpecial ms=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		Integer hosId=ms.getHosId();
		Integer yltId=Integer.parseInt(request.getParameter("yltId"));
		return hospitalAdminManager.hoshealthstruts(yltId,hosId);
	}
	/**
	 * 退出医联体
	 * http://localhost:8080/hospital/quitylt
	 * 参数:yltId 医联体id
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/quitylt")
	@ResponseBody
	public Map<String,Object> quitylt(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Integer yltId=Integer.parseInt(request.getParameter("yltId"));//医联体id
		hospitalAdminManager.quitylt(user.getId(),yltId);
		map.put("status", "success");
		return map;
	}
	
	/**
	 * 邀请加入医联体
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/invitjoinylt")
	@ResponseBody
	public Map<String,Object> invitjoinylt(HttpServletRequest request,HttpSession session){
		DoctorRegisterInfo user = (DoctorRegisterInfo) session.getAttribute("user");
		MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		Map<String,Object> map=new HashMap<String,Object>();
		Integer yltId=Integer.parseInt(request.getParameter("yltId"));
		Integer hospitalId=Integer.parseInt(request.getParameter("hospitalId"));//被邀请的医院
		HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(hospitalId);
		hospitalAdminManager.invitjoinylt(yltId,user.getId(),user.getUserType(),hos,doc.getHosId());
		return map;
	}
	/**
	 * 邀请的医院管理界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/invitjoinmanage")
	public ModelAndView invitjoinmanage(HttpServletRequest request){
		return new ModelAndView("hos/invit_join_manage");
	}
	/**
	 * 获取邀请的医院列表数据
	 * @param params
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gaininvitjoindatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaininvitjoindatas(@RequestBody JSONParam[] params,HttpServletRequest request,HttpSession session) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		MobileSpecial ms=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		Integer hosId=ms.getHosId();
		return  hospitalAdminManager.gaininvitjoindatas(hosId,paramMap);
	}
	/**
	 * 申请的医院管理界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/appjoinmanage")
	public ModelAndView appjoinmanage(HttpServletRequest request){
		return new ModelAndView("hos/app_join_manage");
	}
	/**
	 * 获取申请的医院列表数据
	 * @param params
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gainappjoindatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainappjoindatas(@RequestBody JSONParam[] params,HttpServletRequest request,HttpSession session) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		MobileSpecial ms=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		Integer hosId=ms.getHosId();
		return  hospitalAdminManager.gainappjoindatas(hosId,paramMap);
	}
	/**
	 * 修改邀请或申请的状态
	 * aid :邀请或申请 id
	 * sval: -1:拒绝  1:同意
	 * stype:1--申请加入  2--邀请
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/changeappstatus")
	@ResponseBody
	public Map<String,Object> changeappstatus(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Integer aid=Integer.parseInt(request.getParameter("aid"));
		Integer sval=Integer.parseInt(request.getParameter("sval"));
		Integer stype=Integer.parseInt(request.getParameter("stype"));
		hospitalAdminManager.changeappstatus(user.getId(),user.getUserType(),aid,sval,stype);
		return map;
	}
	
	//=========================================订单模块===================================================
	//=======================================1.转诊订单===================================================
	@RequestMapping(value="/referralOrderManage")
	public ModelAndView referralOrderManage(HttpServletRequest request){
		return new ModelAndView("hos/referral_order_manage");
	}
	/**
	 * 获取转诊数据
	 * @param params
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gainReferralOrderDatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainReferralOrderDatas(@RequestBody JSONParam[] params,HttpServletRequest request,HttpSession session) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		return  hospitalAdminManager.gainReferralOrderDatas(user,paramMap);
	}
	/**
	 * 删除转诊订单
	 * @param request
	 * @param session
	 */
	@RequestMapping(value="/delReferral")
	@ResponseBody
	public void delReferral(HttpServletRequest request,HttpSession session){
		Integer referId=Integer.parseInt(request.getParameter("referId"));
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		hospitalAdminManager.delReferral(user,referId);
	}
	/**
	 * 更新转诊订单状态 取消或退诊
	 * @param request
	 * @param session
	 */
	@RequestMapping(value="/changReferralStat")
	@ResponseBody
	public void changReferralStat(HttpServletRequest request,HttpSession session){
		Integer referId=Integer.parseInt(request.getParameter("referId"));
		Integer sval=Integer.parseInt(request.getParameter("sval"));
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		hospitalAdminManager.changReferralStat(user,referId,sval);
	}
	//=======================================1.视频订单===================================================
	@RequestMapping(value="/vedioOrderManage")
	public ModelAndView vedioOrderManage(HttpServletRequest resquest){
		return new ModelAndView("hos/vedio_order_manage");
	}
	/**
	 * 获取视频会诊数据
	 * @param params
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gainVedioOrderDatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainVedioOrderDatas(@RequestBody JSONParam[] params,HttpServletRequest request,HttpSession session) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		return  hospitalAdminManager.gainVedioOrderDatas(user,paramMap);
	}
	/**
	 * 删除视频会诊订单
	 * @param request
	 * @param session
	 */
	@RequestMapping(value="/delVedio")
	@ResponseBody
	public void delVedio(HttpServletRequest request,HttpSession session){
		Integer vedioId=Integer.parseInt(request.getParameter("vedioId"));
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		hospitalAdminManager.delVedio(user,vedioId);
	}
	/**
	 * 更新视频会诊订单状态 取消或退诊
	 * @param request
	 * @param session
	 */
	@RequestMapping(value="/changVedioStat")
	@ResponseBody
	public void changVedioStat(HttpServletRequest request,HttpSession session){
		Integer referId=Integer.parseInt(request.getParameter("vedioId"));
		Integer sval=Integer.parseInt(request.getParameter("sval"));
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		hospitalAdminManager.changVedioStat(user,referId,sval);
	}
	
	/**
	 * 获取分配医生
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/gainDistDoctors")
	@ResponseBody
	public Map<String,Object> gainDistDoctors(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Integer otype=Integer.parseInt(request.getParameter("otype"));
		MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		List<MobileSpecial> doctors=commonService.queryDoctorDetailInfoByHosId(doc.getHosId());
		for (MobileSpecial _doc : doctors) {
			DoctorServiceInfo ds=commonService.queryDoctorServiceInfoByCon(_doc.getSpecialId(), otype, null);
			if(ds!=null&&ds.getIsOpen().equals(1)){
				_doc.setOpenAsk(1);
			}else{
				_doc.setOpenAsk(0);
			}
		}
		map.put("doctors", doctors);
		return map;
	}
	/**视频或者转诊订单
	 * 
	 * 分配医生  
	 * @param request
	 */
	@RequestMapping(value="/distAndSelDoc")
	@ResponseBody
	public Map<String,Object> distAndSelDoc(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer dtype = Integer.parseInt(request.getParameter("dtype"));//发起，接收
		Integer type=Integer.parseInt(request.getParameter("type"));//类型 4：视频订单  10：转诊订单 5 图文会诊
		Integer doctorId=Integer.parseInt(request.getParameter("doctorId"));//选择的医生id
		Integer orderId=Integer.parseInt(request.getParameter("orderId"));//分配医生的订单id
		hospitalAdminManager.distAndSelDoc(type,doctorId,orderId,dtype);
		return map;
	}
	//=======================================2.图文订单===================================================、
	@RequestMapping(value="/tuwenOrderManage")
	public ModelAndView tuwenOrderManage(HttpServletRequest resquest){
		return new ModelAndView("hos/tuwen_order_manage");
	}
	
	/**
	 * 获取图文会诊数据
	 * @param params
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gaintuwenorderdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaintuwenorderdatas(@RequestBody JSONParam[] params,HttpServletRequest request,HttpSession session) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		return  hospitalAdminManager.gaintuwenOrderDatas(user,paramMap);
	}
	
    /**
     * 删除图文会诊订单
     * @param request
     * @param session
     */
    @RequestMapping(value="/delTuwen")
    @ResponseBody
    public void delTuwen(HttpServletRequest request,HttpSession session){
        Integer tuwenId=Integer.parseInt(request.getParameter("tuwenId"));
        DoctorRegisterInfo user = (DoctorRegisterInfo) session
                .getAttribute("user");
        hospitalAdminManager.delTuwen(user,tuwenId);
        
    }
    
    /**
     * 更新图文会诊订单状态 取消或退诊
     * @param request
     * @param session
     */
    @RequestMapping(value="/changTuwenStat")
    @ResponseBody
    public void changTuwenStat(HttpServletRequest request,HttpSession session){
        Integer referId=Integer.parseInt(request.getParameter("tuwenId"));
        Integer sval=Integer.parseInt(request.getParameter("sval"));
        DoctorRegisterInfo user = (DoctorRegisterInfo) session
                .getAttribute("user");
        hospitalAdminManager.changTuwenStat(user,referId,sval);
    }

	
	
}
