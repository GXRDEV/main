package com.tspeiz.modules.manage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.tspeiz.modules.common.bean.HelpBean;
import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.dto.D2DTuwenDto;
import com.tspeiz.modules.common.bean.dto.ReferOrderDto;
import com.tspeiz.modules.common.bean.dto.VedioOrderDto;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.OrderBindingCode;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.newrelease.SystemBusinessDictionary;
import com.tspeiz.modules.common.entity.release2.BusinessD2dReferralOrder;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAlliance;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;
import com.tspeiz.modules.common.service.IApiGetuiPushService;
import com.tspeiz.modules.common.service.ICaseService;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.ID2pService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.util.IdcardUtils;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.RandomUtil;
import com.tspeiz.modules.util.date.RelativeDateFormat;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.log.RecordLogUtil;
import com.tspeiz.modules.util.weixin.WeixinUtil;

@Service
public class DoctorAdminManager {
	@Autowired
	private ICommonService commonService;
	@Autowired
	private CommonManager commonManager;
	@Autowired
	private ICaseService caseService;
	@Autowired
	private PayOrderManager payOrderManager;
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private ID2pService d2pService;
	@Autowired
    private IApiGetuiPushService apiGetuiPushService;
	@Autowired
	private IWeixinService weixinService;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat ssdf=new SimpleDateFormat("yyyy-MM-dd");
	private Logger log=Logger.getLogger(DoctorAdminManager.class);

	/**
	 * 新增专家咨询订单
	 */
	public Map<String, Object> saveOrUpdateSpecialAdviceOrder(
			HttpServletRequest request, Integer caseid, Integer docid) {
		Map<String, Object> map = new HashMap<String, Object>();
		String orderid = request.getParameter("oid");
		if (StringUtils.isNotBlank(orderid)) {
			map.put("oid", orderid);
			map.put("uuid", request.getParameter("uuid"));
		} else {
			// 新增
			SpecialAdviceOrder sao = new SpecialAdviceOrder();
			sao.setUuid(commonManager.generateUUID(5));
			sao.setCaseId(caseid);
			sao.setDoctorId(docid);
			sao.setCreateTime(new Timestamp(System.currentTimeMillis()));
			sao.setSource(5);// 医生后台
			sao.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());// 进行中
			sao.setPayStatus(4);// 待付款
			sao.setDelFlag(0);
			Integer oid = commonService.saveSpecialAdviceOrder(sao);
			map.put("oid", oid);
			map.put("uuid", sao.getUuid());
		}
		return map;
	}
	

	/**
	 * 处理支付及更新订单及病例
	 */
	public Map<String, Object> processcpay(HttpServletRequest request,
			HttpServletResponse response, Integer uid, Integer utype)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 更新病例
		caseService.saveOrUpdateCase(request, uid, utype);
		CaseInfo caseinfo = wenzhenService.queryCaseInfoById(Integer.valueOf(request.getParameter("caseid")));
		// 更新订单
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		SpecialAdviceOrder order = commonService
				.querySpecialAdviceOrderById(oid);
		order.setExpertId(Integer.parseInt(request.getParameter("expertId")));
		commonService.updateSpecialAdviceOrder(order);
		Map<String, String> maps = new HashMap<>();
        // 添加消息附属信息
        maps = apiGetuiPushService.setPushPatientExtend(maps, caseinfo.getSubUserUuid());
		//推送消息
		commonManager.generateSystemPushInfo(21, order.getUuid(), 5,order.getExpertId(), 2,maps, "您有一个新的专家咨询订单，请及时处理。");
		
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(order.getExpertId());
		special.setAskAmount(commonManager.gainMoneyByOrder(5, special.getSpecialId()));
		// 预支付二维码
		UUID uuid = UUID.randomUUID();
		String product_id = uuid.toString().replace("-", "");
		Map<String, Object> wmap = WeixinUtil
				.weipay_pc(request, response, special.getAskAmount()
						.floatValue(), PropertiesUtil.getString("APPID"),
						PropertiesUtil.getString("APPSECRET"), PropertiesUtil
								.getString("PARTNER"), PropertiesUtil
								.getString("PARTNERKEY"), "专家咨询", product_id,
						PropertiesUtil.getString("DOMAINURL")
								+ "kangxin/wenzhennotify", null, null, null);
		Integer pid = payOrderManager.savePayInfo(5, oid,
				wmap.get("out_trade_no").toString(), special.getAskAmount()
						.floatValue(), 2, special.getAskAmount().floatValue(),
				0.0f, 0.0f, 0.0f, null);
		// 4.记录日志
		try {
			RecordLogUtil.insert("5", "专家咨询", oid + "", pid + "", (String) wmap
					.get("paramxml"), (String) wmap.get("prepayxml"), "", wmap
					.get("out_trade_no").toString());
		} catch (Exception e) {

		}
		map.putAll(wmap);
		return map;
	}

	/**
	 * 获取专家咨询订单
	 * @param unread
	 * @param userId
	 * @param sEcho
	 * @param start
	 * @param length
	 * @param searchContent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String gainadviceorders(String currstatus,Integer userId,String sEcho,Integer start,Integer length,String searchContent){
		StringBuilder stringJson = null;
		List<SpecialAdviceOrder> _rcs = null;
		if (StringUtils.isNotBlank(currstatus)
				&& currstatus.equalsIgnoreCase("processing")) {
			Map<String, Object> retmap = commonService.querySpecialAdviceOrdersByCondition(1,userId,searchContent,start,length,3);
			Integer renum = (Integer) retmap.get("num");
			List<SpecialAdviceOrder> rcs = (List<SpecialAdviceOrder>) retmap
					.get("items");
			if (_rcs == null)
				_rcs = new ArrayList<SpecialAdviceOrder>();
			_rcs.addAll(rcs);
			stringJson = new StringBuilder("{\"sEcho\":" + sEcho
					+ ",\"iTotalRecords\":" + (renum)
					+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		} else {
			Map<String, Object> retmap = commonService.querySpecialAdviceOrdersByCondition(2,userId,searchContent,start,length,3);
			Integer renum = (Integer) retmap.get("num");
			List<SpecialAdviceOrder> rcs = (List<SpecialAdviceOrder>) retmap
					.get("items");
			if (_rcs == null)
				_rcs = new ArrayList<SpecialAdviceOrder>();
			_rcs.addAll(rcs);
			stringJson = new StringBuilder("{\"sEcho\":" + sEcho
					+ ",\"iTotalRecords\":" + renum
					+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		}
		SpecialAdviceOrder order = null;
		for (int i = 0; i < _rcs.size(); i++) {
			String time = "";
			order = _rcs.get(i);
			stringJson.append("[");
			stringJson.append("\"" + order.getId() + "\",");
			stringJson.append("\"" + order.getHeadImageUrl()+ "\",");
			stringJson.append("\"" + order.getExpertName() + "\",");
			stringJson.append("\"" + order.getHosName() + "\",");
			Integer unreadNum = order.getDocUnreadMsgNum()!=null?order.getDocUnreadMsgNum():0;
			stringJson.append("\"" + "您有"+unreadNum+"条新消息" + "\",");
			String age="";
			String sex="";
			if(order.getAge()==null){
				if(StringUtils.isNotBlank(order.getIdCard())){
					age=String.valueOf(IdcardUtils.getAgeByIdCard(order.getIdCard()));
				}else{
					age="未知";
				}
			}else{
				age=order.getAge().toString();
			}
			if(order.getSex()==null){
				if(StringUtils.isNotBlank(order.getIdCard())){
					String _sex=IdcardUtils.getGenderByIdCard(order.getIdCard());
					sex=_sex.equalsIgnoreCase("M")?"男":"女";
				}else{
					sex="未知";
				}
			}else{
				sex=order.getSex().equals(1)?"男":"女";
			}
			stringJson.append("\"" + order.getUserName()+" "+sex+" "+age + "\",");
			stringJson.append("\"" +(StringUtils.isNotBlank(order.getDiseaseDes())?order.getDiseaseDes():"")+ "\",");
			stringJson.append("\"" + sdf.format(order.getCreateTime()) + "\",");
			stringJson.append("\"" + order.getPayStatus() + "\",");
			stringJson.append("\"" + OrderStatusEnum.getStatusValue(21, order.getStatus()) + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (_rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	
	public Map<String,Object> processReply(Integer oid,String msgContent,Integer userId){
		Map<String,Object> map=new HashMap<String,Object>();
		SpecialAdviceOrder order=commonService.querySpecialAdviceOrderById(oid);
		BusinessMessageBean message = new BusinessMessageBean();
		Timestamp date=new Timestamp(System.currentTimeMillis());
		message.setOrderId(oid);
		message.setOrderType(5);
		message.setMsgType("text");
		message.setMsgContent(msgContent);
		message.setSendId(userId);
		message.setSendType(3);
		message.setSendTime(date);
		message.setRecvId(order.getExpertId());
		message.setRecvType(2);
		message.setStatus(1);
		wenzhenService.saveBusinessMessageBean(message);
		message.setMsgTime(sdf.format(message.getSendTime()));
		map.put("message",message);
		order.setDocLastAnswerTime(new Timestamp(System.currentTimeMillis()));
		order.setDocSendMsgCount(order.getDocSendMsgCount()==null?1:(order.getDocSendMsgCount()+1));
		//专家未读数
		order.setExpertUnreadMsgNum(order.getExpertUnreadMsgNum()==null?1:(order.getExpertUnreadMsgNum()+1));
		commonService.updateSpecialAdviceOrder(order);
		/*UserDevicesRecord u=wenzhenService.querySendMessageOrNot(order.getUserId());
		MobileSpecial special=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
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
		}
		//短信发送
		sendpatientsms(order,special);*/
		return map;
	}
	/**
	 * 判断该医生所在医院是否在某个医联体中
	 * @param docid
	 * @return
	 */
	public Map<String,Object> tellexistylt(Integer docid){
		Map<String,Object> map=new HashMap<String,Object>();
		List<HospitalHealthAlliance> hhas=gainHospitalHealthAllianceByHosId(docid);
		if(hhas!=null&&hhas.size()>0){
			map.put("exist", "true");
		}else{
			map.put("exist", "false");
		}
		return map;
	}
	/**
	 * 获取医生所在医院加入的医联体
	 * @param docid
	 * @return
	 */
	public List<HospitalHealthAlliance> gainHospitalHealthAllianceByHosId(Integer docid){
		DoctorDetailInfo doc=commonService.queryDoctorDetailInfoById(docid);
		List<HospitalHealthAlliance> hhas=commonService.queryHospitalHealthAlliances(doc.getHospitalId());
		return hhas;
	}
	/**
	 * 获取医联体成员
	 * @param hhaId
	 * @return
	 */
	public List<HospitalDetailInfo> gainhoshealthmember(Integer hhaId){
		HospitalHealthAlliance hha=commonService.queryHospitalHealthAllianceById(hhaId);
		List<HospitalDetailInfo> members=commonService.queryHospitalHealthAllianceMemberByHhaId(hha.getUuid());
		return members;
	}
	/**
	 * 基本信息填写完点击下一步 
	 * @param docid
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> basicnext(Integer docid,Integer userType,HttpServletRequest request) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		if(userType.equals(5)){
			Integer caseid = caseService.saveOrUpdateCase(request, docid, 5);
			BusinessD2dReferralOrder reforder=processRerralOrder(request,caseid,docid,userType);
			map.put("caseid", caseid);
			map.put("oid", reforder.getId());
			map.put("uuid",reforder.getUuid());
		}else{
			Integer caseid = caseService.saveOrUpdateCase(request, docid, 3);
			BusinessD2dReferralOrder reforder=processRerralOrder(request,caseid,docid,userType);
			map.put("caseid", caseid);
			map.put("oid", reforder.getId());
			map.put("uuid",reforder.getUuid());
		}
		return map;
	}
	/**
	 * 处理转诊订单的新增或更新
	 * @return
	 */
	private BusinessD2dReferralOrder processRerralOrder(HttpServletRequest request,Integer caseId,Integer docid,Integer userType){
		CaseInfo caseInfo = commonService.queryCaseInfoById(caseId);
		MobileSpecial localdoc=d2pService.queryBusinessD2dReferralOrderByUserId(docid);
		String orderid = request.getParameter("oid");
		Integer referralType=Integer.parseInt(request.getParameter("referralType"));//转诊类型   0：门诊  1：住院
		String referralDate=request.getParameter("referralDate");//转诊日期
		String referralDocId=request.getParameter("referralDocId");//选择的转诊医生
		String referralHosId=request.getParameter("referralHosId");//转诊医院
		String referralDepId=request.getParameter("referralDepId");//转诊科室
		boolean issave=false;
		BusinessD2dReferralOrder reforder=null;
		if (StringUtils.isNotBlank(orderid)) {
			reforder=d2pService.queryd2dreferralOrderbyId(Integer.parseInt(orderid));
		} else {
			issave=true;
			reforder=new BusinessD2dReferralOrder();
			reforder.setUuid(UUID.randomUUID().toString().replace("-", ""));
			reforder.setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		reforder.setCaseId(caseId);
		reforder.setCaseUuid(caseInfo.getUuid());
		reforder.setReferralType(referralType);
		//设置发起者信息
		if(userType.equals(5)){
			reforder.setDoctorId(null);
		}else{
			reforder.setDoctorId(docid);
		}
		reforder.setLocalHospitalId(localdoc.getHosId());
		reforder.setReferralDate(referralDate);
		if(StringUtils.isNotBlank(referralDocId)){
			DoctorDetailInfo doc=commonService.queryDoctorDetailInfoById(Integer.parseInt(referralDocId));
			reforder.setReferralHosId(doc.getHospitalId());
			reforder.setReferralDepId(doc.getDepId());
			reforder.setReferralDocId(Integer.parseInt(referralDocId));
		}
		if(StringUtils.isNotBlank(referralHosId)){
			reforder.setReferralHosId(Integer.parseInt(referralHosId));
		}
		if(StringUtils.isNotBlank(referralDepId)){
			reforder.setReferralDepId(Integer.parseInt(referralDepId));
		}
		reforder.setSource(4);
		reforder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
		reforder.setDelFlag(0);
		if(issave){
			d2pService.saveBusinessD2dReferralOrder(reforder);
		}else{
			d2pService.updateBusinessD2dReferralOrder(reforder);
		}
		return reforder;
	}
	
	@SuppressWarnings("unchecked")
	public String gainreferorders(Integer ostatus,Integer docid,
			String sEcho,Integer start,Integer length,String searchContent,Integer dtype){
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.queryReferordersByCondition(docid,searchContent,ostatus,start,length,dtype);
		Integer renum = (Integer) retmap.get("num");
		List<ReferOrderDto> rcs = (List<ReferOrderDto>) retmap.get("items");
		if(rcs!=null&&rcs.size()>0){
			for (ReferOrderDto dto : rcs) {
				String createTime=dto.getCreateTime();
				dto.setDisCreateTime(RelativeDateFormat.calculateTimeLoc(createTime));
				String referDate=dto.getReferDate();
				if(referDate.contains(":")){
					dto.setDisReferDate(RelativeDateFormat.calculateTimeLoc(referDate));
				}
			}
		}
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(rcs).toString());
		stringJson.append("}");
		return stringJson.toString();
	}
	/**
	 * 获取该医生视频会诊数据
	 * @param ostatus
	 * @param docid
	 * @param sEcho
	 * @param start
	 * @param length
	 * @param searchContent
	 * @param dtype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String gainVedioOrderDatas(Integer ostatus,Integer docid,
				String sEcho,Integer start,Integer length,String searchContent,Integer dtype){
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.queryVedioOrderDatas_doc(docid,searchContent,ostatus,start,length,dtype);
		Integer renum = (Integer) retmap.get("num");
		List<VedioOrderDto> vedios = (List<VedioOrderDto>) retmap.get("items");
		for (VedioOrderDto dto : vedios) {
			dto.setDisCreateTime(RelativeDateFormat.calculateTimeLoc(dto.getCreateTime()));
			String ctime="";
			ctime+=StringUtils.isNotBlank(dto.getConsultationDate())?dto.getConsultationDate():"";
			ctime+=StringUtils.isNotBlank(dto.getConsultationTime())?" "+dto.getConsultationTime():"";
			if(StringUtils.isNotBlank(ctime)){
				dto.setDisBeginTime(RelativeDateFormat.calculateTimeLoc(ctime));
			}else{
				dto.setDisBeginTime(ctime);
			}
			
		}
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(vedios).toString());
		stringJson.append("}");
		return stringJson.toString();
	}
	
	/**
	 * 获取该医生专家咨询数据
	 * @param ostatus
	 * @param docid
	 * @param sEcho
	 * @param start
	 * @param length
	 * @param searchContent
	 * @param dtype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String gainD2DTuwenDatas(Integer ostatus,Integer docid,
				String sEcho,Integer start,Integer  length,String searchContent,Integer dtype){
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.queryD2DTuwenDatas_doc(docid, searchContent, ostatus, start, length, dtype);
		Integer renum = (Integer) retmap.get("num");
		List<D2DTuwenDto> tuwens = (List<D2DTuwenDto>) retmap.get("items");
		for (D2DTuwenDto tuwen : tuwens) {
			if(ostatus.equals(3)){
				//已接诊
				Integer count=commonService.queryUnReadMsg(tuwen.getUuid(),5,docid);
				tuwen.setUnReadMsgCount(count);
			}
			tuwen.setDisCreateTime(RelativeDateFormat.calculateTimeLoc(tuwen.getCreateTime()));
			if(StringUtils.isNotBlank(tuwen.getReceiveTime())){
				tuwen.setDisReceiveTime(RelativeDateFormat.calculateTimeLoc(tuwen.getReceiveTime()));
			}
		}
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(tuwens).toString());
		stringJson.append("}");
		return stringJson.toString();
	}
	/**
	 * 专家咨询 填写基本信息下一步提交
	 * @param request
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> nextSubAdviceOrder(HttpServletRequest request,DoctorRegisterInfo user) throws Exception{
		Integer caseid = null;
		if(user.getUserType().equals(5)){
			 caseid = caseService.saveOrUpdateCase(request, user.getId(), 5);
		}else{
			 caseid = caseService.saveOrUpdateCase(request, user.getId(), 3);
		}
		//Integer caseid = caseService.saveOrUpdateCase(request, user.getId(), 3);
		Map<String, Object> map = new HashMap<String, Object>();
		String orderid = request.getParameter("oid");//订单id 如果有的话
		String uuid=request.getParameter("uuid");//订单uuid
		Integer docid=Integer.parseInt(request.getParameter("doctorId"));//选择的医生id
		DoctorRegisterInfo exp=commonService.queryDoctorRegisterInfoById(docid);
		//DoctorRegisterInfo localdoc=commonService.queryDoctorRegisterInfoById(user.getId());
		MobileSpecial localdoc=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		SpecialAdviceOrder adviceOrder =null;
		if (StringUtils.isNotBlank(orderid)) {
			adviceOrder=commonService.querySpecialAdviceOrderById(Integer.parseInt(orderid));
			adviceOrder.setExpertId(exp.getId());
			adviceOrder.setExpertType(exp.getUserType());
			//adviceOrder.setExpertHospitalId);
			commonService.updateSpecialAdviceOrder(adviceOrder);
		} else {
			// 新增
			CaseInfo caseInfo = commonService.queryCaseInfoById(caseid);
			adviceOrder= new SpecialAdviceOrder();
			adviceOrder.setUuid(commonManager.generateUUID(5));
			adviceOrder.setExpertId(exp.getId());
			adviceOrder.setExpertType(exp.getUserType());
			adviceOrder.setCaseId(caseid);
			adviceOrder.setCaseUuid(caseInfo.getUuid());
			//设置发起者信息
			if(user.getUserType().equals(5)){
				adviceOrder.setDoctorId(null);
			}else{
				adviceOrder.setDoctorId(user.getId());
			}
			//adviceOrder.setDoctorId(user.getId());
			adviceOrder.setLocalHospitalId(localdoc.getHosId());
			adviceOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
			adviceOrder.setSource(5);// 医生后台
			adviceOrder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());// 待接诊
			adviceOrder.setPayStatus(4);// 待付款
			adviceOrder.setDelFlag(0);
			Integer oid = commonService.saveSpecialAdviceOrder(adviceOrder);
			orderid=oid.toString();
			uuid=adviceOrder.getUuid();
		}
		map.put("caseid", caseid);
		map.put("oid",orderid);
		map.put("uuid",uuid);
		return map;
	}
	/**
	 * 最终提交咨询专家订单 去支付，如果需要的话
	 * @param request
	 * @return
	 */
	public Map<String,Object> finishSubAdviceOrder(HttpServletRequest request,HttpServletResponse response,DoctorRegisterInfo user) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		SpecialAdviceOrder order = commonService
				.querySpecialAdviceOrderById(oid);
		CaseInfo caseinfo = wenzhenService.queryCaseInfoById(order.getCaseId());
		Map<String, String> maps = new HashMap<>();
		maps.put("doctorId", String.valueOf(order.getDoctorId()));
        // 添加消息附属信息
        maps = apiGetuiPushService.setPushPatientExtend(maps, caseinfo.getSubUserUuid());
		//推送消息
		commonManager.generateSystemPushInfo(21, order.getUuid(), 5,order.getExpertId(), 2,maps, "您有一个新的专家咨询订单，请及时处理。");
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(order.getExpertId());
		BigDecimal money=commonManager.gainMoneyByOrder(5, special.getSpecialId());
		if(money!=null&&money.compareTo(BigDecimal.ZERO)>0){
			//需要支付
			UUID uuid = UUID.randomUUID();
			String product_id = uuid.toString().replace("-", "");
			Map<String, Object> wmap = WeixinUtil
					.weipay_pc(request, response, money
							.floatValue(), PropertiesUtil.getString("APPID"),
							PropertiesUtil.getString("APPSECRET"), PropertiesUtil
									.getString("PARTNER"), PropertiesUtil
									.getString("PARTNERKEY"), "专家咨询", product_id,
							PropertiesUtil.getString("PayCallBackUrl")
									+ "d2p/paynotify", null, null, null);
			payOrderManager.savePayInfo(5, oid,
					wmap.get("out_trade_no").toString(), money
							.floatValue(), 2, money.floatValue(),
					0.0f, 0.0f, 0.0f, null); 
			map.put("money", money);
			map.putAll(wmap);
			map.put("needpay", "true");
			map.put("status", "success");
		}else{
			//不需要支付
			order.setPayStatus(1);
			commonService.updateSpecialAdviceOrder(order);
			map.put("needpay", "false");
			map.put("status", "error");
			processBindCode(oid,5);
		}
		return map;
	}
	public Map<String,Object> gainopencitys(String type){
		Map<String,Object> map=new HashMap<String,Object>();
		List<HelpBean> pros=commonService.queryOpenPros(type);
		if(pros!=null&&pros.size()>0){
			List<HelpBean> citys=null;
			for (HelpBean pro : pros) {
				citys=commonService.queryOpenCitys(pro.getDistCode(),type,1);
				if(citys!=null&&citys.size()>0){
					List<HelpBean> dists=null;
					for(HelpBean city:citys){
						dists=commonService.queryOpenCitys(city.getDistCode(),type,2);
						city.setCitys(dists);
					}
				}
				pro.setCitys(citys);
			}
		}
		map.put("pros", pros);
		return map;
	}
	/**
	 * 视频会诊 填写基本信息后提交
	 * @param request
	 * @param integer 
	 * @return
	 */
	public Map<String,Object> nextSubBasicVedioInfo(HttpServletRequest request,Integer userId, Integer userType) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		String oid=request.getParameter("oid");//订单id  如果有的话
		String remark=request.getParameter("remark");//填写的期望医生或专家要求
		String doctorId=request.getParameter("doctorId");//选择平台内的医生或专家id
		String hospitalId=request.getParameter("hospitalId");//选择医联体中的医院id
		String depId=request.getParameter("depId");//选择医联体中的医院科室id
		String consultationDate=request.getParameter("consultationDate");//期望会诊时间
        if(userType.equals(5)){
            Integer caseid = caseService.saveOrUpdateCase(request, userId, 5);
            //创建订单
            BusinessVedioOrder order=processCreateVedioOrder(oid,userId,userType,caseid,doctorId,hospitalId,depId,consultationDate,remark);
            map.put("oid", order.getId());
            map.put("uuid", order.getUuid());
            map.put("caseid", caseid);
            //return map;
        }else{
            Integer caseid = caseService.saveOrUpdateCase(request, userId, 3);
            //创建订单
            BusinessVedioOrder order=processCreateVedioOrder(oid,userId,userType,caseid,doctorId,hospitalId,depId,consultationDate,remark);
            map.put("oid", order.getId());
            map.put("uuid", order.getUuid());
            map.put("caseid", caseid);
            //return map;
        }
        return map;
	}
	/**
	 * 创建视频会诊订单
	 * @param oid
	 * @param userId
	 * @param caseid
	 * @param caseid 
	 * @param doctorId
	 * @param hospitalId
	 * @param depId
	 * @param consultationDate
	 * @param remark
	 * @return
	 */
	private BusinessVedioOrder processCreateVedioOrder(String oid,Integer userId,Integer userType,Integer caseid, String doctorId,
			String hospitalId,String depId,String consultationDate,String remark){
		BusinessVedioOrder order=null;
		MobileSpecial localdoc=commonService.queryMobileSpecialByUserIdAndUserType(userId);
		boolean issave=false;
		if(StringUtils.isNotBlank(oid)){
			order=commonService.queryBusinessVedioOrderById(Integer.parseInt(oid));	
		}else{
			order=new BusinessVedioOrder();
			order.setCreateTime(new Timestamp(System.currentTimeMillis()));
			order.setUuid(commonManager.generateUUID(4));
			issave=true;
		}
		CaseInfo caseinfo = commonService.queryCaseInfoById(caseid);
		order.setCaseId(caseid);
		order.setCaseUuid(caseinfo.getUuid());
		//设置发起者信息
        if(userType.equals(5)){
            order.setLocalDoctorId(null);
        }else{
            order.setLocalDoctorId(userId);
        }
		order.setLocalHospitalId(localdoc.getHosId());
		order.setLocalDepartId(localdoc.getDepId());
		order.setConsultationDur(20);
		//设置接受者信息
		if(StringUtils.isNotBlank(doctorId)){
			MobileSpecial ex=commonService.queryMobileSpecialByUserIdAndUserType(Integer.parseInt(doctorId));
			order.setExpertId(Integer.parseInt(doctorId));
			order.setExpertType(ex.getUserType());
			order.setExpertHospitalId(ex.getHosId());
			order.setExpertDepId(ex.getDepId());
		}else{
			if(StringUtils.isNotBlank(hospitalId)){
				order.setExpertHospitalId(Integer.parseInt(hospitalId));
			}
			if(StringUtils.isNotBlank(depId)){
				order.setExpertDepId(Integer.parseInt(depId));
			}
		}
		order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
		order.setHelpOrder(1);
		order.setSource(4);
		order.setPayStatus(4);
		order.setDelFlag(0);
		if(issave){
			oid=weixinService.saveBusinessVedioOrder(order)+"";
		}else{
			commonService.updateBusinessVedioOrder(order);
		}
		return order;
	}
	/**
	 * 更新转诊订单状态
	 * @param orderId
	 * @param opType
	 * @param user
	 */
	public void changeReferOrderStat(Integer orderId,Integer opType,DoctorRegisterInfo user){
		BusinessD2dReferralOrder order=d2pService.queryd2dreferralOrderbyId(orderId);
		if(opType.equals(1)){
			//删除
			order.setDelFlag(1);
		}else if(opType.equals(2)){
			order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey());
			order.setClosedTime(new Timestamp(System.currentTimeMillis()));
			order.setCloserId(user.getId());
			order.setCloserType(user.getUserType());
		}
		d2pService.updateBusinessD2dReferralOrder(order);
	}
	private SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
    public static String OrderInvite_ToCustomer = "%s医生帮您下了一个%s订单，为方便您查看订单详情及会诊结果，请关注微信公号[佰医汇]，到[个人中心]->[设置]中点击[绑定订单]，输入订单绑定码即可查看详情。订单绑定码：%s。【佰医汇】";
	public static String ORDER_INVITE_PATIENT_TOPAY="%s医生的%s订单已生成，请按如下方法完成支付。关注微信公号[佰医汇]，到[个人中心]->[设置]中点击[绑定订单]，输入订单绑定码即可查看详情及进行支付。订单绑定码：%s。【佰医汇】";
	public void processBindCode(Integer oid,Integer otype){
		String uuid="";
        Integer caseId=null;
        Integer docid=null;
        String desc="";
        if(otype.equals(4)){
            BusinessVedioOrder order=wenzhenService.queryBusinessVedioOrderById(oid);
            uuid=order.getUuid();
            caseId=order.getCaseId();
            docid=order.getLocalDoctorId();
            desc="视频会诊";
        }else if(otype.equals(5)){
            SpecialAdviceOrder order=wenzhenService.querySpecialAdviceOrderById(oid);
            uuid=order.getUuid();
            caseId=order.getCaseId();
            docid=order.getDoctorId();
            desc="图文会诊";
        }
        MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(docid);
        CaseInfo caseInfo=wenzhenService.queryCaseInfoById(caseId);
        OrderBindingCode code = new OrderBindingCode();
        code.setOrderUuid(uuid);
        code.setOrderType(otype);
        code.setBindingCode(format.format(new Date()) + RandomUtil.GetNumericRandomString(Integer.valueOf(4)));
        code.setCreateTime(new Timestamp(System.currentTimeMillis()));
        code.setTelephone(caseInfo.getTelephone());
        commonService.saveOrderBindingCode(code);
        if (StringUtils.isNotBlank(caseInfo.getTelephone())) {
        	if(docid !=null){
        		String message = String.format(OrderInvite_ToCustomer, new Object[] { doc.getSpecialName(), desc, code.getBindingCode() });
                String ret = HttpSendSmsUtil.sendSmsInteface(caseInfo.getTelephone(), message);
                /*if (!ret.equalsIgnoreCase("100")) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    log.error("短信发送验证码失败。订单号：" + uuid+" 订单类型："+otype);
                }*/
        	}
        }
	}
	
	
	public void  notifyPatientToPay(Integer oid,Integer otype){
		String uuid="";
        Integer caseId=null;
        Integer docid=null;
        String desc="";
		if(otype.equals(4)){
            BusinessVedioOrder order=wenzhenService.queryBusinessVedioOrderById(oid);
            uuid=order.getUuid();
            caseId=order.getCaseId();
            docid=order.getLocalDoctorId();
            desc="视频会诊";
        }else if(otype.equals(5)){
            SpecialAdviceOrder order=wenzhenService.querySpecialAdviceOrderById(oid);
            uuid=order.getUuid();
            caseId=order.getCaseId();
            docid=order.getDoctorId();
            desc="图文会诊";
        }
		MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(docid);
        CaseInfo caseInfo=wenzhenService.queryCaseInfoById(caseId);
        OrderBindingCode code = new OrderBindingCode();
        code.setOrderUuid(uuid);
        code.setOrderType(otype);
        code.setBindingCode(format.format(new Date()) + RandomUtil.GetNumericRandomString(Integer.valueOf(4)));
        code.setCreateTime(new Timestamp(System.currentTimeMillis()));
        code.setTelephone(caseInfo.getTelephone());
        commonService.saveOrderBindingCode(code);
        if (StringUtils.isNotBlank(caseInfo.getTelephone())) {
        	if(docid!=null){
        		String message = String.format(ORDER_INVITE_PATIENT_TOPAY, new Object[] { doc.getSpecialName(), desc, code.getBindingCode() });
                String ret = HttpSendSmsUtil.sendSmsInteface(caseInfo.getTelephone(), message);
                if (!ret.equalsIgnoreCase("100")) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    log.error("短信发送验证码失败。订单号：" + uuid+" 订单类型："+otype);
                }	
        	}
        }
	}
	
	public List<SystemBusinessDictionary> gainSysDicList(Integer groupId) {
		return commonService.querySysDicList(groupId);
	}
	
	
	/**
	 * 处理病例附件信息
	 * @param caseUuid
	 * @param attachments
	 */
	public void processCaseAttachments(String caseUuid,String attachments) {
		List<UserCaseAttachment> list = commonService.queryUserCaseAttachmentsByCaseUuid(caseUuid);
		log.info("===attachments:"+attachments);
        Set<Integer> remainIds = new HashSet<>();
        if(StringUtils.isNotBlank(attachments)){
        	JSONObject obj = JSONObject.fromObject(attachments);
            JSONArray array = obj.getJSONArray("attachments");
            if(array !=null && !array.isEmpty()){
                for(int i =0 ;i<array.size() ; i++){
                    JSONObject jsonObj = array.getJSONObject(i);
                    String id = jsonObj.getString("id");
                    UserCaseAttachment attachment = null;
                    if(StringUtils.isNotBlank(id)) {
                        remainIds.add(Integer.parseInt(id));
                        attachment = commonService.queryUserCaseAttachmentById(Integer.parseInt(id));
                        attachment.setRemark(jsonObj.getString("remark"));
                        attachment.setType(jsonObj.getInt("type"));
                        try {
        					attachment.setReportTime(new Timestamp(ssdf.parse(jsonObj.getString("reportTime")).getTime()));
                          	} catch (Exception e) {
                          		e.printStackTrace();
                          	}
                        attachment.setAttachmentIds(jsonObj.getString("attachmentIds"));
                        attachment.setCaseUuid(caseUuid);
                        commonService.updateUserCaseAttachment(attachment);
                    }else {
                        attachment = new UserCaseAttachment();
                        attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        attachment.setUuid(UUID.randomUUID().toString().replace("-", ""));
                        attachment.setRemark(jsonObj.getString("remark"));
                        attachment.setType(jsonObj.getInt("type"));
                      try {
    					attachment.setReportTime(new Timestamp(ssdf.parse(jsonObj.getString("reportTime")).getTime()));
                      	} catch (Exception e) {
                      		e.printStackTrace();
                      	}
                      attachment.setAttachmentIds(jsonObj.getString("attachmentIds"));
                      attachment.setCaseUuid(caseUuid);
                      commonService.saveOrUpdateUserCaseAttachment(attachment);
                    }
                }
            }
        }
        if(list != null && !list.isEmpty()){
        	for (UserCaseAttachment attachment : list) {
				if(!remainIds.contains(attachment.getId())) {
					 commonService.delUserCaseAttachment(attachment.getId());
				}
			}
        }
	}
	private String gaindesc(Integer status){
		String str="";
		switch(status){
		case 10:
			str="待接诊";
			break;
		case 20:
			str="已接诊";
			break;
		case 30:
			str="已退诊";
			break;
		case 40:
			str="已完成";
			break;
		case 50:
			str="已取消";
			break;
		}
		return str;
	}
}
