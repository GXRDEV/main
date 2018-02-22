package com.tspeiz.modules.home;

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

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tspeiz.modules.common.bean.JSONParam;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.ExpertConsultationSchedule;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.manage.CommonManager;
import com.tspeiz.modules.manage.PayOrderManager;
import com.tspeiz.modules.util.IdcardUtils;
import com.tspeiz.modules.util.PythonVisitUtil;
import com.tspeiz.modules.util.common.StringRetUtil;
import com.tspeiz.modules.util.date.RelativeDateFormat;

@Controller
@RequestMapping("nuradmin")
public class NurseAdminController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat _format=new SimpleDateFormat("yyyy-MM-dd");
	private Logger log = Logger.getLogger(NurseAdminController.class);
	@Resource
	private IWeixinService weixinService;
	@Resource
	private ICommonService commonService;
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private PayOrderManager payOrderManager;
	@Autowired
	private CommonManager commonManager;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) {
		return new ModelAndView("nurse/index");
	}

	// 进入预约挂号主界面
	@RequestMapping(value = "/registeration", method = RequestMethod.GET)
	public ModelAndView registeration(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 查找本地开通的科室
		List<HospitalDepartmentInfo> departs=weixinService.queryCoohospitalDepartmentsByCooHos(1);
		System.out.println(JSONArray.fromObject(departs).toString());
		map.put("departs", departs);
		return new ModelAndView("nurse/regist", map);
	}

	// 根据科室以及时间查询能出诊的专家
	@RequestMapping(value = "/showexperts", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> showexperts(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer depid = Integer.parseInt(request.getParameter("depid"));
		String sdate = request.getParameter("sdate");// 日期
		String timetype = request.getParameter("timetype");// 0或者空为"全天"
															// ,1为"上午",2为"下午"
		String pageNo = request.getParameter("pageNo");
		Integer pageSize = 10;
		Integer _pageNo = 1;
		if (StringUtils.isNotBlank(pageNo)) {
			_pageNo = Integer.parseInt(pageNo);
		}
		List<MobileSpecial> specials=weixinService.queryMobileSpecialsByConditions_newnurse(depid, sdate, timetype, _pageNo, pageSize);
		map.put("specials", specials);
		return map;
	}

	// 填写信息
	@RequestMapping(value = "/writeinfo", method = RequestMethod.GET)
	public ModelAndView writeinfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sdate = request.getParameter("sdate");
		String timetype = request.getParameter("timetype");
		Integer sid = Integer.parseInt(request.getParameter("sid"));
		String depid = request.getParameter("depid");
		//MobileSpecial special = weixinService.queryMobileSpecialById(sid);
		MobileSpecial special=commonService.queryMobileSpecialByUserIdAndUserType(sid);
		map.put("sdate", sdate);// 日期
		map.put("timetype", timetype);// 类型
		map.put("sid", sid);// 专家id
		map.put("special", special);// 专家信息
		map.put("depid", depid);
		return new ModelAndView("nurse/writeinfo", map);
	}

	// 获取预约时间段
	@RequestMapping(value = "/gainSpecialTimes", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> gainSpecialTimes(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sdate = request.getParameter("sdate");
		String timetype = request.getParameter("timetype");
		Integer sid = Integer.parseInt(request.getParameter("sid"));
		List<ExpertConsultationSchedule> times = weixinService
				.queryExpertConTimeSchedulsByConditions(sid, sdate, timetype);
		map.put("times", times);
		return map;
	}

	// 人工收取费用下单挂号
	@RequestMapping(value = "/registwinfo", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> registwinfo(HttpServletRequest request)throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession().getAttribute("user");
		Integer sid = Integer.parseInt(request.getParameter("sid"));
		MobileSpecial special=commonService.queryMobileSpecialByUserIdAndUserType(sid);
		Integer depid = Integer.parseInt(request.getParameter("depid"));// 本地科室
		Integer timeid = Integer.parseInt(request.getParameter("timeid"));// 时间id
		//MobileSpecial nurse = commonService
				//.queryMobileSpecialByUserIdAndUserType(user.getId(), 4);
		MobileSpecial nurse=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		Integer hosId = nurse.getHosId();// 本地医院
		String patientName = request.getParameter("patientName");// 患者姓名
		String telphone = request.getParameter("telphone");// 联系电话
		String idcard = request.getParameter("idcard");// 身份证号
		Integer sex=1;
		Integer age=null;
		if(StringUtils.isNotBlank(idcard)){
			String _sex=IdcardUtils.getGenderByIdCard(idcard);
			sex = _sex.equalsIgnoreCase("M") ? 1
					: (_sex.equalsIgnoreCase("F") ? 0 : 1);
			age=IdcardUtils.getAgeByIdCard(idcard);
		}else{
			sex=1;
		}
		//处理病例
		CaseInfo caseinfo=new CaseInfo();
		caseinfo.setCreateTime(new Date());
		caseinfo.setContactName(patientName);
		caseinfo.setIdNumber(idcard);
		caseinfo.setSex(sex);
		caseinfo.setAge(age);
		caseinfo.setTelephone(telphone);
		caseinfo.setUuid(UUID.randomUUID().toString().replace("-", ""));
		Integer caseid=wenzhenService.saveCaseInfo(caseinfo);
		BusinessVedioOrder order=new BusinessVedioOrder();
		order.setCaseId(caseid);
		order.setExpertId(sid);
		order.setCreateTime(new Timestamp(System.currentTimeMillis()));
		order.setLocalHospitalId(hosId);
		order.setLocalDepartId(depid);
		ExpertConsultationSchedule time = weixinService
				.queryExpertConScheduleById(timeid);
		order.setConsultationDate(time.getScheduleDate());
		order.setConsultationTime(time.getStartTime());
		order.setConsultationDur(15);
		order.setStatus(1);// 待就诊
		order.setLocalPlusNo("123");
		order.setSchedulerDateId(timeid);
		order.setNurseId(user.getId());
		order.setSource(3);
		order.setPayStatus(1);//默认已收钱
		order.setDelFlag(0);
		order.setUuid(commonManager.generateUUID(4));
		Integer orderid=weixinService.saveBusinessVedioOrder(order);
		payOrderManager.savePayInfo(4, orderid, null, special.getVedioAmount().floatValue(), 4, special.getVedioAmount().floatValue(), 0.0f,  0.0f,  0.0f, null);
		time.setHasAppoint("1");
		weixinService.updatExpertConScheduleDate(time);
		// 医院挂号
		toplusInHis(order);
		return map;
	}

	// 支付完成时进行六安挂号
	private void toplusInHis(BusinessVedioOrder rc) throws Exception {
		Integer local_depid = rc.getLocalDepartId();
		HospitalDepartmentInfo depart=weixinService.queryHospitalDepartmentInfoById(local_depid);
		if(depart.getLocalDepId()!=null){
			log.info("=====六安挂号开始====");
			CaseInfo caseinfo=commonService.queryCaseInfoById(rc.getCaseId());
			Map<String, Object> pinfo = new HashMap<String, Object>();
			pinfo.put("name", caseinfo.getContactName());
			pinfo.put("id_card", caseinfo.getIdNumber());
			pinfo.put("telephone", caseinfo.getTelephone());
			String pstr = PythonVisitUtil.generateJSONObject(pinfo).toString();
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
			Map<String, Object> map = PythonVisitUtil.register_info(nameValuePairs);
			rc.setLocalPlusRet(map.get("message").toString());
			commonService.updateBusinessVedioOrder(rc);
			log.info("=====六安挂号结束====");
		}
	}
	
	private Integer gainType(BusinessVedioOrder rc){
		Integer type=3;
		String time = rc.getConsultationDate().trim();
		if(time.equalsIgnoreCase(_format.format(new Date()))){
			type=0;
		}
		return type;
	}
	private String gaintime(BusinessVedioOrder rc){
		String time= rc.getConsultationDate().trim() + " "
				+ rc.getConsultationTime().trim()+":00";
		return time;
	}


	// 挂号历史订单

	@RequestMapping(value = "/registhistory", method = RequestMethod.GET)
	public ModelAndView registhistory(HttpServletRequest reuqest) {
		Map<String, Object> map = new HashMap<String, Object>();
		return new ModelAndView("nurse/history", map);
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

	// 护士 挂号历史订单
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gainhistorys", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainhistorys(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession().getAttribute("user");
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = weixinService
				.queryRemoteConsultationsByConditions_newnurse(user.getId(),
						start, length, searchContent);
		
		Integer renum = (Integer) retmap.get("num");
		List<BusinessVedioOrder> _rcs = (List<BusinessVedioOrder>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		BusinessVedioOrder rc = null;
		String time = "";
		String age = "未知";
		Integer stat = null;
		for (int i = 0; i < _rcs.size(); i++) {
			rc = _rcs.get(i);
			stringJson.append("[");
			stringJson.append("\"" + rc.getId() + "\",");
			stat = rc.getStatus();
			switch (stat) {
			case 1:

			case 2:
				time = rc.getConsultationDate() + " "
						+ rc.getConsultationTime();
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
				time = rc.getConsultationDate() + " "
						+ rc.getConsultationTime();
				break;
			}
			stringJson.append("\""
					+ StringRetUtil.gainStringByStats(rc.getStatus(),
							rc.getProgressTag()) + "\",");
			stringJson.append("\"" + time + "\",");
			stringJson.append("\"" + RelativeDateFormat.calculateTimeLoc(time)
					+ "\",");
			stringJson.append("\"" + rc.getHosName() + "\",");
			stringJson.append("\"" + rc.getDepName() + "\",");
			stringJson.append("\"" + rc.getPatientName() + "\",");
			if(rc.getAge()==null){
				if (StringUtils.isNotBlank(rc.getIdCard())) {
					age = IdcardUtils.getAgeByIdCard(rc.getIdCard()) + "岁";
				}
			}
			String sex = rc.getSex().equals(1) ? "男"
					: (rc.getSex().equals(2) ? "女" : "");
			stringJson.append("\"" + age + "\",");
			stringJson.append("\"" + sex + "\",");
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

}
