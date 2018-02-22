package com.tspeiz.modules.home;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tspeiz.modules.common.bean.Calendar;
import com.tspeiz.modules.common.bean.JSONParam;
import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.ExpertConsultationSchedule;
import com.tspeiz.modules.common.entity.ExpertConsultationTime;
import com.tspeiz.modules.common.entity.SystemServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
import com.tspeiz.modules.common.entity.newrelease.BusinessOrderInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessTelOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.service.IApiGetuiPushService;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.manage.CommonManager;
import com.tspeiz.modules.manage.ExpertAdminManager;
import com.tspeiz.modules.util.ChineseToPinyinUtil;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.common.StringRetUtil;
import com.tspeiz.modules.util.date.RelativeDateFormat;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.oss.OSSManageUtil;

@Controller
@RequestMapping("expert")
public class ExpertAdminController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger log = Logger.getLogger(ExpertAdminController.class);
	@Resource
	private IWeixinService weixinService;
	@Resource
	private ICommonService commonService;
	@Resource
	private IWenzhenService wenzhenService;
	@Autowired
	private ExpertAdminManager expertAdminManager;
	@Autowired
	private CommonManager commonManager;
    @Resource
    private IApiGetuiPushService apiGetuiPushService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, ModelMap model) {
		return new ModelAndView("expert/index");
	}

	@RequestMapping(value = "/experhz", method = RequestMethod.GET)
	public ModelAndView experhz(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		return new ModelAndView("expert/zj_huizhen", map);
	}

	// 医生所属患者查询，前端分页
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
		if (StringUtils.isNotBlank(currorhis)
				&& currorhis.equalsIgnoreCase("history")) {
			Map<String, Object> retmap = weixinService
					.queryRemoteConsulationsByConditions_new(user.getId(),
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
					.queryRemoteConsulationsByConditions_new(user.getId(),
							user.getUserType(), searchContent, "", com_start,
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
					.queryRemoteConsulationsByConditions_new(user.getId(),
							user.getUserType(), searchContent, "", start,
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
		String time = "";
		String age = "未知";
		Integer stat = null;
		for (int i = 0; i < _rcs.size(); i++) {
			rc = _rcs.get(i);
			stringJson.append("[");
			stringJson.append("\"" + rc.getId() + "\",");
			stat = rc.getStatus();
			if(StringUtils.isNotBlank(rc.getConsultationDate())){
				time = rc.getConsultationDate();
			}
			if(StringUtils.isNotBlank(rc.getConsultationTime())){
				time =time+ " "+ rc.getConsultationTime();
			}
			
			// 专家信息
			stringJson.append("\"" + rc.getLocalDocImage() + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(rc.getLocalDocName()) ? rc
							.getLocalDocName() : "未知") + "\",");
			stringJson.append("\"" + rc.getHosName() + "\",");
			
			stringJson.append("\""
					+ StringRetUtil.gainStringByStats_new(rc.getStatus(),
							rc.getProgressTag(), 2) + "\",");
			stringJson.append("\"" + rc.getPatientName()+" "+(rc.getSex()==null?"未知":(rc.getSex().equals(1)?"男":"女")) +" "+(rc.getAge()==null?age:rc.getAge()) + "\",");
			String desc=clearCha((StringUtils.isNotBlank(rc.getDiseaseDes())?rc.getDiseaseDes():(StringUtils.isNotBlank(rc.getMainSuit())?rc.getMainSuit():"")));
			stringJson.append("\"" + desc + "\",");
			stringJson.append("\"" + rc.getTelephone() + "\",");
			stringJson.append("\"" + time + "\",");
			String _time = "";
			if (StringUtils.isNotBlank(time)
					&& !time.equalsIgnoreCase("null null")) {
				_time = RelativeDateFormat.calculateTimeLoc(time);
			} else {
				time = "待定";
			_time = "未知";
			}
			stringJson.append("\"" + _time
					+ "\",");
			stringJson.append("\"" + _sdf.format(rc.getCreateTime())
					+ "\",");
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

	// =================================================远程会诊排班设置
	@RequestMapping(value = "/remotetimeset", method = RequestMethod.GET)
	public ModelAndView remotetimeset(HttpServletRequest request) {
		DoctorRegisterInfo user = null;
		String expertid = request.getParameter("expertid");
		if (StringUtils.isNotBlank(expertid)) {
			user = commonService.queryDoctorRegisterInfoById(Integer
					.parseInt(expertid));
		} else {
			user = (DoctorRegisterInfo) request.getSession().getAttribute(
					"user");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<ExpertConsultationTime> times = weixinService
				.queryExpertConTimes(user.getId());
		map.put("times", times);
		map.put("expertid", expertid);
		return new ModelAndView("expert/remote_time", map);
	}

	@RequestMapping(value = "/saveorupdatert", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveorupdatert(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String tid = request.getParameter("id");
		ExpertConsultationTime etime = null;
		Integer _tid = null;
		if (StringUtils.isNotBlank(tid)) {
			// 编辑
			_tid = Integer.parseInt(tid);
			etime = weixinService.queryExpertConTimeById(_tid);
			setdtime(request, etime);
			weixinService.updateExpertConTime(etime);
		} else {
			// 保存
			etime = new ExpertConsultationTime();
			setdtime(request, etime);
			_tid = weixinService.saveExpertConTime(etime);
		}
		map.put("tid", _tid);
		return map;
	}

	@RequestMapping(value = "/delremotetime", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> delremotetime(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer tid = Integer.parseInt(request.getParameter("tid"));
		weixinService.delExpertConTimeById(tid);
		return map;
	}

	private void setdtime(HttpServletRequest request,
			ExpertConsultationTime dtime) {
		DoctorRegisterInfo user = null;
		String expertid = request.getParameter("expertid");
		if (StringUtils.isNotBlank(expertid)) {
			user = commonService.queryDoctorRegisterInfoById(Integer
					.parseInt(expertid));
		} else {
			user = (DoctorRegisterInfo) request.getSession().getAttribute(
					"user");
		}
		String week = request.getParameter("week");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String cost = request.getParameter("cost");
		String status = request.getParameter("state");
		String remark = request.getParameter("mark");
		dtime.setWeekDay(Integer.parseInt(week));
		dtime.setExpertId(user.getId());
		dtime.setStartTime(startTime);
		dtime.setEndTime(endTime);
		if (StringUtils.isNotBlank(cost))
			dtime.setCost(Float.parseFloat(cost));
		dtime.setRemark(remark);
		dtime.setStatus(Integer.parseInt(status));
	}

	// ===================================根据远程会诊排班模板生成就诊日期及时间段
	@RequestMapping(value = "/generateConShedules", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> generateConShedules(HttpServletRequest request) {
		DoctorRegisterInfo user = null;
		String expertid = request.getParameter("expertid");
		if (StringUtils.isNotBlank(expertid)) {
			user = commonService.queryDoctorRegisterInfoById(Integer
					.parseInt(expertid));
		} else {
			user = (DoctorRegisterInfo) request.getSession().getAttribute(
					"user");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		System.out.println(start + "===" + end);
		map = weixinService.generateConShedules(user.getId(), start, end);
		return map;
	}

	// 查看 当前专家一周内的时间安排
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/gainSchedules", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainSchedules(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		/*
		 * String now=sdf.format(new Date()); Calendar c =
		 * Calendar.getInstance(); c.setTime(new Date());
		 * c.add(Calendar.DAY_OF_YEAR, 6); String end=sdf.format(c.getTime());
		 * 
		 * Map<String, String> paramMap = convertToMap(params); String sEcho =
		 * paramMap.get("sEcho"); // 搜索内容 String searchContent =
		 * paramMap.get("searchContent"); Integer start =
		 * Integer.parseInt(paramMap.get("iDisplayStart")); Integer length =
		 * Integer.parseInt(paramMap.get("iDisplayLength")); Map<String,Object>
		 * map=weixinService.querySchedulesByTime(now,end,start,length);
		 * StringBuilder stringJson=null; Integer num=(Integer)map.get("num");
		 * stringJson= new StringBuilder("{\"sEcho\":" + sEcho +
		 * ",\"iTotalRecords\":" + num + ",\"iTotalDisplayRecords\":" + num +
		 * ",\"aaData\":["); List<ExpertConsultationSchedule>
		 * _rcs=(List<ExpertConsultationSchedule>)map.get("items");
		 * ExpertConsultationSchedule sche=null; for (int i = 0; i <
		 * _rcs.size(); i++) { sche=_rcs.get(i); stringJson.append("[");
		 * stringJson.append("\"" + sche.getId() + "\",");
		 * stringJson.append("\"" + rc.getHosName() + "\",");
		 * stringJson.append("\"" + rc.getDepName() + "\",");
		 * stringJson.append("\"" + rc.getPatientName()+ "\",");
		 * stringJson.append("\"" + age + "\","); stringJson.append("\"" + sex +
		 * "\","); stringJson.append("\"\""); stringJson.append("],"); }
		 * if(_rcs.size()>0) stringJson.deleteCharAt(stringJson.length() - 1);
		 * stringJson.append("]"); stringJson.append("}");
		 * log.info("====json=="+stringJson.toString()); return
		 * stringJson.toString();
		 */
		return "";
	}

	@RequestMapping(value = "/gainExpertTimeDatas", method = RequestMethod.GET)
	public void showJsonData(HttpServletRequest request,
			HttpServletResponse response) {
		DoctorRegisterInfo user = null;
		String expertid = request.getParameter("expertid");
		if (StringUtils.isNotBlank(expertid)) {
			user = commonService.queryDoctorRegisterInfoById(Integer
					.parseInt(expertid));
		} else {
			user = (DoctorRegisterInfo) request.getSession().getAttribute(
					"user");
		}
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ExpertConsultationSchedule> _list = weixinService
				.queryExpertConTimeSchedulsByExpertId(user.getId());
		List<Calendar> list = new ArrayList<Calendar>();
		String now = sdf.format(new Date());
		for (ExpertConsultationSchedule _time : _list) {
			Calendar cal = new Calendar();
			cal.setStart(_time.getScheduleDate());
			if (StringUtils.isNotBlank(_time.getHasAppoint())
					&& _time.getHasAppoint().equalsIgnoreCase("1")) {
				cal.setColor("#f30");
				cal.setTitle(_time.getStartTime() + "【已预约】");
			} else {
				if (now.compareTo(_time.getScheduleDate()) > 0) {
					cal.setTitle(_time.getStartTime() + "【已失效】");
					cal.setColor("#AAAAAA");
				} else {
					cal.setTitle(_time.getStartTime() + "【可预约】");
					cal.setColor("#360");
				}
			}
			list.add(cal);
		}
		String json = JSONSerializer.toJSON(list).toString();
		System.out.println("===json数据===" + json);
		out.print(json);
	}

	// ---------------图文问诊订单-------------------------------------
	@RequestMapping(value = "/graphics", method = RequestMethod.GET)
	public ModelAndView graphics(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		return new ModelAndView("/expert/graphics", map);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaingraphicorders", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaingraphicorders(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer _type=Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> retmap = wenzhenService
				.queryTuwenOrdersByExpertConditions(user.getId(),
						searchContent, start, length, _type);
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
					+ (order.getDocMessageCount() == null ? 0 : order
							.getDocMessageCount()) + "\",");
			stringJson
					.append("\"" + _sdf.format(order.getCreateTime()) + "\",");
			stringJson.append("\"" + order.getContactName() + "\",");
			stringJson
					.append("\""
							+ ((order.getSex() != null) ? (order.getSex()
									.equals(1) ? "男" : "女") : "未知") + "\",");
			stringJson.append("\"" + order.getAge() + "\",");
			stringJson.append("\"" + order.getTelephone() + "\",");
			String stat = StringRetUtil.gainstatusdesc(order.getAskStatus());
			stringJson.append("\"" + stat + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	/**
	 * 图文详情
	 */
	@RequestMapping(value = "/twdetail/{oid}", method = RequestMethod.GET)
	public ModelAndView twdetail(@PathVariable Integer oid,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessTuwenOrder tw=wenzhenService.queryBusinessTuwenInfoById(oid);
		// 聊天消息
		List<BusinessMessageBean> messages = wenzhenService.queryBusinessMessageBeansByCon(oid,tw.getUuid(), 1);
		for (BusinessMessageBean _bean : messages) {
			_bean.setMsgTime(_sdf.format(_bean.getSendTime()));
		}
		// 病例信息
		CaseInfo cinfo = wenzhenService.queryCaseInfoById(tw.getCaseId());
		// 病例图片
		List<CustomFileStorage> images = wenzhenService.queryCustomFilesByCaseIds(cinfo.getNormalImages());
		map.put("twinfo", tw);
		map.put("messages", messages);
		map.put("cinfo", cinfo);
		map.put("images", images);
		tw.setDocUnreadMsgNum(0);
		wenzhenService.updateBusinessTuwenOrder(tw);
		return new ModelAndView("expert/graphic_detail", map);
	}

	/**
	 * 专家回复
	 */
	@RequestMapping(value = "/replytouser", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> replytouser(HttpServletRequest request) {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 问诊id
		String msgContent = request.getParameter("msgContent");
		BusinessTuwenOrder order=wenzhenService.queryBusinessTuwenInfoById(oid);
		BusinessMessageBean message = new BusinessMessageBean();
		Timestamp date=new Timestamp(System.currentTimeMillis());
		message.setOrderId(oid);;
		message.setOrderType(1);
		message.setMsgType("text");
		message.setMsgContent(msgContent);
		message.setSendId(user.getId());
		message.setSendType(2);
		message.setSendTime(date);
		message.setRecvId(order.getUserId());
		message.setRecvType(1);
		message.setStatus(1);
		wenzhenService.saveBusinessMessageBean(message);
		message.setMsgTime(_sdf.format(message.getSendTime()));
		map.put("message",message);
		
		if (order.getDocFirstAnswerTime() == null) {
			order.setDocFirstAnswerTime(date);
		}
		order.setDocLastAnswerTime(date);
		//专家发送消息数
		order.setDocSendMsgCount(order.getDocSendMsgCount()==null?1:(order.getDocSendMsgCount()+1));
		//患者未读消息数
		order.setPatUnreadMsgNum(order.getPatUnreadMsgNum()==null?1:(order.getPatUnreadMsgNum()+1));
		wenzhenService.updateBusinessTuwenOrder(order);
		MobileSpecial special=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		
		//患者先不推App端
		/*UserDevicesRecord u=wenzhenService.querySendMessageOrNot(order.getUserId());
		if(u!=null){
			String _time=sdf.format(order.getCreateTime());
			String _ret="";
			try {
				_ret = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
						"root/getui");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			IPushResult ret = GetuiPushUtils.PushMessage(_ret,
					u.getPushId(),"1;"+order.getId().toString(),"您"+_time+"提交给"+special.getHosName()+""+special.getSpecialName()+special.getDuty()+"的图文问诊，专家已亲自回复，点击查看。", 1);
		}*/
		//短信发送
		sendpatientsms(order,special);
		return map;
	}
	private void sendpatientsms(BusinessTuwenOrder order,MobileSpecial special){
		CaseInfo caseinfo=wenzhenService.queryCaseInfoById(order.getCaseId());
		String ptelphone=caseinfo.getTelephone();
		String _time=sdf.format(order.getCreateTime());
		String desc="";
		if(order.getSource().equals(3)){
			desc="\"佰医汇\"微信公众号";
		}else if(order.getSource().equals(4)){
			desc="\"佰医汇\"官网";
		}else if(order.getSource().equals(1)){
			desc="\"佰医汇\"苹果APP";
		}else if(order.getSource().equals(2)){
			desc="\"佰医汇\"安卓APP";
		}
		String content="您"+_time+"提交给"+special.getHosName()+""+special.getSpecialName()+special.getDuty()+"的图文问诊，专家已亲自回复，请前往"+desc+"进行查看。【佰医汇】";
		HttpSendSmsUtil.sendSmsInteface(ptelphone, content);
		
		String _kefu_content=special.getHosName()+special.getSpecialName()+"给"+caseinfo.getContactName()+"的图文问诊回复消息啦。请注意查收。【佰医汇】";
		HttpSendSmsUtil.sendSmsInteface("13521231353,15001299884", _kefu_content);
	}

	// 电话问诊订单
	@RequestMapping(value = "/telorders", method = RequestMethod.GET)
	public ModelAndView telorders(HttpServletRequest request) {

		return new ModelAndView("expert/tel_orders");
	}

	// 获取电话订单数据
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gaintelorders", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gaintelorders(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, String> paramMap = convertToMap(params);
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> retmap = wenzhenService.querytelorders(
				searchContent, start, length, user.getId().toString(),ostatus);
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
			stringJson.append("\"" + (order.getAge()==null?"未知":order.getAge()) + "\",");
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
			List<BusinessOrderInfo> oinfos = wenzhenService
					.queryBusinessOrderInfosByOId(oid, otype);
			if (oinfos != null && oinfos.size() > 0) {
				boolean b = true;
				for (BusinessOrderInfo _oinfo : oinfos) {
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

	// 查看电话订单详情
	@RequestMapping(value = "/teldetail/{oid}", method = RequestMethod.GET)
	public ModelAndView teldetail(@PathVariable Integer oid,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessTelOrder telinfo=wenzhenService.queryBusinessTelOrderById(oid);
		// 病例信息
		CaseInfo cinfo = wenzhenService.queryCaseInfoById(telinfo.getCaseId());
		// 病例图片
		List<CustomFileStorage> images = wenzhenService
				.queryCustomFilesByCaseIds(cinfo.getNormalImages());
		map.put("telinfo", telinfo);
		map.put("cinfo", cinfo);
		map.put("images", images);
		return new ModelAndView("expert/tel_detail", map);
	}
	
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * 医生，专家个人信息编辑
	 * 访问：http://localhost:8080/expert/exinfo
	 * 
	 */
	@RequestMapping(value="/exinfo")
	@ResponseBody
	public Map<String,Object> exinfo(HttpServletRequest request) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		DoctorDetailInfo detail=commonService.queryDoctorDetailInfoById(user.getId());
		MobileSpecial special=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		if(user.getUserType().equals(2)){
			//专家需要二维码
			if(!StringUtils.isNotBlank(detail.getErweimaUrl())){
				//没有二维码，需要生成该专家的二维码图片
				String erweimaUrl=OSSManageUtil.genErweima(ChineseToPinyinUtil.getPingYin(detail.getDisplayName()), PropertiesUtil.getString("DOMAINURL")+"propagate/doctordetail/"+user.getId());
				detail.setErweimaUrl(erweimaUrl);
				commonService.updateDoctorDetailInfo(detail);
				special.setErweimaUrl(erweimaUrl);
			}else{
				special.setErweimaUrl(detail.getErweimaUrl());
			}
		}
		map.put("special", special);
		if(StringUtils.isNotBlank(special.getRelatedPics())){
			List<CustomFileStorage> images = wenzhenService
					.queryCustomFilesByCaseIds(special.getRelatedPics());
			map.put("images", images);
		}
		return map;
	}
	
	/**
	 * 编辑个人信息保存
	 * 访问：http://localhost:8080/expert/editexpert
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editexpert")
	@ResponseBody
	public Map<String,Object> editexpert(HttpServletRequest request) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		expertAdminManager.editExpertInfo(request);
		return map;
	}
	
	/**
	 * 进入专家咨询列表复
	 */
	@RequestMapping(value = "/showadviceorders")
	public ModelAndView showadviceorders(HttpServletRequest request) {

		return new ModelAndView("expert/advice_orders");
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
		String data = expertAdminManager.gainadviceorders(currstatus, user.getId(),
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
		order.setExpertUnreadMsgNum(0);
		commonService.updateSpecialAdviceOrder(order);
		map.put("order", order);
		map.put("caseinfo", caseinfo);
		List<CustomFileStorage> caseimages = wenzhenService
				.queryCustomFilesByCaseIds(caseinfo.getNormalImages());
		map.put("caseimages", caseimages);
		// 聊天消息
		List<BusinessMessageBean> msgs = wenzhenService
				.queryBusinessMessageBeansByCon(oid,order.getUuid(),5);
		for (BusinessMessageBean _msg : msgs) {
			_msg.setMsgTime(_sdf.format(_msg.getSendTime()));
		}
		map.put("msgs", msgs);
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(order.getExpertId());
		map.put("special", special);
		return new ModelAndView("expert/advice_detail", map);
	}
	/**
	 * 专家给医生发送消息
	 */
	@RequestMapping(value = "/replytodoc")
	@ResponseBody
	public Map<String, Object> replytodoc(HttpServletRequest request) 
	{
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 问诊id
		String msgContent = request.getParameter("msgContent");
		Map<String, Object> retmap = expertAdminManager.processReply(oid,
				msgContent, user.getId());
		map.putAll(retmap);
		//记录消息
		SpecialAdviceOrder order=commonService.querySpecialAdviceOrderById(oid);
		MobileSpecial sp=commonService.queryMobileSpecialByUserIdAndUserType(order.getExpertId());
		Map<String, String> maps = new HashMap<>();
		maps.put("doctorId", String.valueOf(order.getDoctorId()));
		maps = apiGetuiPushService.setPushDoctorExtend(maps, order.getExpertId());
		commonManager.generateSystemPushInfo(21, order.getUuid(), 5, order.getDoctorId(), 3,maps, sp.getSpecialName()+"专家回复了你的专家咨询订单。");
		return map;
	}
	
	/**
	 * 专家服务设置列表
	 */
	@RequestMapping(value="/serverlist")
	@ResponseBody
	public Map<String,Object> serverlist(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession()
				.getAttribute("user");
		List<DoctorServiceInfo> servers=commonService.queryDoctorServiceInfoByDocId(user.getId());
		tellservers(servers,user.getId());
		map.put("servers", servers);
		return map;
	}
	private static Map<Integer,String> sermap=new HashMap<Integer,String>();
	static{
		sermap.put(1,"图文问诊");
		sermap.put(2,"电话问诊");
		sermap.put(4,"远程问诊");
		sermap.put(5,"咨询专家");
	}
	private void tellservers(List<DoctorServiceInfo> servers,Integer docid){
		if(servers!=null&&servers.size()<4){
			List<DoctorServiceInfo> _servers=new ArrayList<DoctorServiceInfo>();
			for (Integer index : sermap.keySet()) {
				boolean b=false;
				for (DoctorServiceInfo serviceInfo : servers) {
					if(index.equals(serviceInfo.getServiceId())){
						b=true;
						break;
					}
				}
				if(!b){
					_servers.add(addserver(index,docid));
				}
			}
			servers.addAll(_servers);
		}else if(servers==null||servers.size()==0){
			servers=new ArrayList<DoctorServiceInfo>();
			servers.add(addserver(1,docid));
			servers.add(addserver(2,docid));
			servers.add(addserver(4,docid));
			servers.add(addserver(5,docid));
		}
	}
	private DoctorServiceInfo addserver(Integer type,Integer docid){
		SystemServiceInfo sinfo=commonService.querySystemServiceInfoById(type);
		DoctorServiceInfo dsinfo=new DoctorServiceInfo();
		dsinfo.setDoctorDivided(sinfo.getDoctorDivided());
		dsinfo.setExpertDivided(sinfo.getExpertDivided());
		dsinfo.setPlatformDivided(sinfo.getPlatformDivided());
		dsinfo.setDoctorId(docid);
		dsinfo.setAmount(null);
		dsinfo.setDescription(null);
		dsinfo.setIsOpen(0);
		if(type.equals(4)){
			dsinfo.setPackageId(3);
		}else{
			dsinfo.setPackageId(type);
		}
		dsinfo.setServiceId(type);
		Integer id=commonService.saveDoctorServiceInfo(dsinfo);
		dsinfo.setServiceName(sermap.get(type));
		dsinfo.setId(id);
		return dsinfo;
	}
	
	/**
	 * 进入详情设置
	 * @param request
	 * @param serid
	 * @return
	 */
	@RequestMapping(value="/serdetailset/{serid}",method=RequestMethod.GET)
	public ModelAndView serdetailset(HttpServletRequest request,@PathVariable Integer serid){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorServiceInfo server=commonService.queryDoctorServiceInfoById(serid);
		map.put("server", server);
		return new ModelAndView("expert/ser_detail",map);
	}
	
	/**
	 * 保存设置（图文，电话，咨询专家）简单保存
	 * 访问：http://localhost:8080/expert/saveset
	 */
	@RequestMapping(value="/saveset",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveset(HttpServletRequest request){
		Integer serid=Integer.parseInt(request.getParameter("serid"));//服务id
		DoctorServiceInfo ser=commonService.queryDoctorServiceInfoById(serid);
		String isopen=request.getParameter("isopen");//是否开通
		if(StringUtils.isNotBlank(isopen)){
			ser.setIsOpen(Integer.parseInt(isopen));
		}
		ser.setAmount(new BigDecimal(request.getParameter("amount")));//金额
		ser.setDescription(request.getParameter("description"));//备注
		commonService.updateDoctorServiceInfo(ser);
		return new HashMap<String,Object>();
	}
	
	/**
	 * 获取该专家视频会诊数据
	 * 访问：http://localhost:8080/expert/gainVedioOrderDatas
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
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));//2,3,4,5,6
		String data = expertAdminManager.gainVedioOrderDatas(ostatus, user.getId(),
				sEcho, start, length, searchContent);
		return data;
	}
	
	/**
	 * 获取该专家--咨询专家数据
	 * 访问：http://localhost:8080/expert/gainD2DTuwenDatas
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
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
		String data = expertAdminManager.gainD2DTuwenDatas(ostatus, user.getId(),
				sEcho, start, length, searchContent);
		return data;
	}
}
