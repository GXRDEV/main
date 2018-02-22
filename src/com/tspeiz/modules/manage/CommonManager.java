package com.tspeiz.modules.manage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.ImTokenInfo;
import com.tspeiz.modules.common.entity.SystemPushInfo;
import com.tspeiz.modules.common.entity.SystemServiceDefault;
import com.tspeiz.modules.common.entity.SystemServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
import com.tspeiz.modules.common.entity.newrelease.BusinessTelOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.DoctorBillInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorWithdrawInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.newrelease.UserDevicesRecord;
import com.tspeiz.modules.common.entity.release2.BusinessD2dReferralOrder;
import com.tspeiz.modules.common.entity.release2.DoctorScheduleShow;
import com.tspeiz.modules.common.service.IApiGetuiPushService;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.ID2pService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.RongCloudApi;
import com.tspeiz.modules.util.UUIDUtil;
import com.tspeiz.modules.util.config.ReaderConfigUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.imchat.ImChatUtil;
import com.tspeiz.modules.util.imchat.RongCloudConfig;
import com.uwantsoft.goeasy.client.goeasyclient.GoEasy;

@Service
public class CommonManager {

	@Autowired
	private ICommonService commonService;
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private IApiGetuiPushService apiGetuiPushService;
	private SimpleDateFormat sm = new SimpleDateFormat("yyMMdd");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 生成13位UUID
	 * 
	 * @param nType
	 * @return
	 */

	public String generateUUID_old(Integer orderType) {
		String uuid = commonService.getOrderNumberByOrderType(orderType);
		System.out.println("===========uuid====" + uuid);
		return uuid;

	}

	/**
	 * 生成13位UUID
	 * 
	 * @param nType
	 * @return
	 */
	public String generateUUID(Integer orderType) {
		try{
			StringBuilder uuidBuilder = new StringBuilder();
			// 1位订单类型
			uuidBuilder.append(gainType(orderType));
			// 6位 日期
			uuidBuilder.append(sm.format(new Date()));
			// 4位订单流水
			int serialNumber = commonService
					.getOrderSerialNumberByOrderType(orderType);
			uuidBuilder.append(String.format("%04d", serialNumber));
			// 2位随机数
			Random random = new Random();
			int randomNum = random.nextInt(99);
			uuidBuilder.append(String.format("%02d", randomNum));
			return uuidBuilder.toString();
		}catch(Exception e){
			return UUID.randomUUID().toString().replace("-", "");
		}
	}

	private String gainType(Integer orderType) {
		if (orderType < 10) {
			return orderType.toString();
		}
		return String.valueOf((char) (orderType + 55));
	}

	// 各种订单价格计算
	public BigDecimal gainMoneyByOrder(Integer orderType, Integer docid) {
		DoctorServiceInfo dsi = commonService
				.queryDoctorServiceInfoByOrderType(orderType, docid);
		if (dsi != null){
			BigDecimal retAmount = dsi.getAmount();
			if(orderType.equals(4)) {
				//视频会诊
				SystemServiceInfo ssi = commonService.querySystemServiceById(dsi.getServiceId());
				retAmount = dsi.getShowPrice() != null?dsi.getShowPrice():processD2DVedioMoney(retAmount, BigDecimal.valueOf(Double.valueOf(ssi.getPriceParameter())));
			}else if(orderType.equals(5)) {
				//图文会诊
				retAmount = dsi.getShowPrice() != null?dsi.getShowPrice():processD2DAskMoney(retAmount);
			}
			return retAmount;
		}
		return null;
	}

	/**
	 * 新增系统消息
	 * 
	 * @param oid
	 *            订单id
	 * @param otype
	 *            订单类型
	 * @param msgType
	 *            消息类型
	 * @param msgContent
	 *            消息内容
	 */
	public void saveBusinessMessageInfo_sys(Integer oid, Integer otype,
			String msgType, String msgContent, Integer recvId, Integer recvType) {
		BusinessMessageBean message = new BusinessMessageBean();
		message.setOrderId(oid);
		message.setOrderType(otype);
		message.setMsgType(msgType);
		message.setMsgContent(msgContent);
		message.setSendTime(new Timestamp(System.currentTimeMillis()));
		message.setRecvId(recvId);
		message.setRecvType(recvType);
		wenzhenService.saveBusinessMessageBean(message);
	}
	public void saveBusinessMessageInfo_sys_new(String orderUuid, Integer otype,
			String msgType, String msgContent, Integer recvId, Integer recvType) {
		BusinessMessageBean message = new BusinessMessageBean();
		message.setOrderUuid(orderUuid);
		message.setOrderType(otype);
		message.setMsgType(msgType);
		message.setMsgContent(msgContent);
		message.setSendTime(new Timestamp(System.currentTimeMillis()));
		message.setRecvId(recvId);
		message.setRecvType(recvType);
		wenzhenService.saveBusinessMessageBean(message);
	}

	/**
	 * 新增消息--医生发送
	 */
	public void saveBusinessMessageInfo_doc(Integer oid, Integer otype,
			String msgType, String msgContent, Integer docid,String uuid) {
		BusinessMessageBean message = new BusinessMessageBean();
		message.setOrderId(oid);
		message.setOrderType(otype);
		message.setMsgType(msgType);
		message.setMsgContent(msgContent);
		message.setSendId(docid);
		message.setSendType(3);
		message.setSendTime(new Timestamp(System.currentTimeMillis()));
		message.setOrderUuid(uuid);
		wenzhenService.saveBusinessMessageBean(message);
	}

	/**
	 * 新增消息--专家发送
	 */
	public void saveBusinessMessageInfo_exp(Integer oid, Integer otype,
			String msgType, String msgContent, Integer expid,String uuid) {
		BusinessMessageBean message = new BusinessMessageBean();
		message.setOrderId(oid);
		message.setOrderType(otype);
		message.setMsgType(msgType);
		message.setMsgContent(msgContent);
		message.setSendId(expid);
		message.setSendType(2);
		message.setSendTime(new Timestamp(System.currentTimeMillis()));
		message.setOrderUuid(uuid);
		wenzhenService.saveBusinessMessageBean(message);
	}
	/**
	 * 加载专家
	 * @param pager
	 * @param uid
	 * @param hosid
	 * @param standDepId
	 * @param duty
	 * @param keywords
	 * @param docask
	 * @param distCode
	 * @return
	 */
	public Pager loadExperts(Pager pager, Integer uid, String hosid,
			String standDepId, String duty, String keywords, String docask,String distCode) {
		Map<String, String> querymap = new HashMap<String, String>();
		if (StringUtils.isNotBlank(keywords)) {
			querymap.put("keywords", keywords);
		}
		if (StringUtils.isNotBlank(docask)) {
			querymap.put("docask", docask);
		}
		if(StringUtils.isNotBlank(distCode)){
			querymap.put("distCode", distCode);
		}
		if (StringUtils.isNotBlank(hosid) || StringUtils.isNotBlank(standDepId)
				|| StringUtils.isNotBlank(duty)) {
			// 所有专家
			if (StringUtils.isNotBlank(hosid)) {
				querymap.put("hosid", hosid);
			}
			if (StringUtils.isNotBlank(standDepId)) {
				querymap.put("standDepId", standDepId);// 标准科室id
			}
			if (StringUtils.isNotBlank(duty)) {
				querymap.put("duty", duty);
			}
			pager.setQueryBuilder(querymap);
			pager = commonService.queryMobileSpecial_helporder(pager, 1);
		} else {
			// 相关专家
			MobileSpecial ms = commonService
					.queryMobileSpecialByUserIdAndUserType(uid);
			querymap.put("depid", ms.getDepId().toString());
			pager.setQueryBuilder(querymap);
			pager = commonService.queryMobileSpecial_helporder(pager, 2);
		}
		return pager;
	}
	/**
	 * 预约转诊-加载医生数据
	 * @param pager
	 * @param hosid
	 * @param depId
	 * @param keywords
	 * @param distCode
	 * @return
	 */
	public Pager loadDoctors(Pager pager,String hosid,String depId,String keywords,String distCode){
		Map<String, String> querymap = new HashMap<String, String>();
		if(StringUtils.isNotBlank(hosid)){
			querymap.put("hosid", hosid);//医院id
		}
		if(StringUtils.isNotBlank(depId)){
			querymap.put("depId",depId);//标准科室id
		}
		if (StringUtils.isNotBlank(keywords)) {
			querymap.put("keywords", keywords);
		}
		if(StringUtils.isNotBlank(distCode)){
			querymap.put("distCode", distCode);
		}
		pager.setQueryBuilder(querymap);
		pager=commonService.queryMobileSpecial_loaddoc(pager);//预约转诊加载医生
		return pager;
	}
	/**
	 * 视频会诊-加载医生或专家 数据
	 * @param pager
	 * @param distCode
	 * @param hosid
	 * @param duty
	 * @param keywords
	 * @return
	 */
	public Pager loadExpertOrDoctors(Pager pager,String distCode,String hosid,String duty,String keywords,String depId,String dtype){
		Map<String, String> querymap = new HashMap<String, String>();
		if(StringUtils.isNotBlank(distCode)){
			querymap.put("distCode", distCode);
		}
		if(StringUtils.isNotBlank(hosid)){
			querymap.put("hosid", hosid);//医院id
		}
		if(StringUtils.isNotBlank(duty)){
			querymap.put("duty", duty);
		}
		if (StringUtils.isNotBlank(keywords)) {
			querymap.put("keywords", keywords);
		}	
		if(StringUtils.isNotBlank(depId)){
			querymap.put("depId", depId);
		}
		if(StringUtils.isNotBlank(dtype)){
			querymap.put("dtype", dtype);
		}
		pager.setQueryBuilder(querymap);
		pager=commonService.queryMobileSpecial_loadExOrdoc(pager);//视频会诊加载医生或专家
		return pager;
	}
	
	public Map<String, Object> gainIMToken(HttpServletRequest request,
			String oid, String type, String otype) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		Integer expertId = null;
		Integer doctorId = null;

		if ("5".equalsIgnoreCase(otype)) {
			SpecialAdviceOrder order = commonService
					.querySpecialAdviceOrderById(Integer.parseInt(oid));
			expertId = order.getExpertId();
			doctorId = order.getDoctorId();

		} else {
			BusinessVedioOrder order = commonService
					.queryBusinessVedioOrderById(Integer.parseInt(oid));
			expertId = order.getExpertId();
			doctorId = order.getLocalDoctorId();
		}

		String mode = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
				"root/imchatmode");// 1为测试环境，2为生产环境
		Map<String, Object> exmap = processExpert(oid, expertId, mode, otype);
		Map<String, Object> docmap = processDoc(oid, doctorId, mode, otype);
		if (type.equalsIgnoreCase("2")) {
			// 专家端
			map.put("token", exmap.get("token"));
			map.put("appkey", exmap.get("appkey"));
			map.put("userId", docmap.get("userId"));
		} else if (type.equalsIgnoreCase("3")) {
			// 医生端
			map.put("token", docmap.get("token"));
			map.put("appkey", docmap.get("appkey"));
			map.put("userId", exmap.get("userId"));
		}
		return map;
	}

	private Map<String, Object> processExpert(String oid, Integer expertId,
			String mode, String otype) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 专家端
		if (expertId != null) {
			String userId = oid + expertId + otype;
			MobileSpecial doc = commonService
					.queryMobileSpecialByUserIdAndUserType(expertId);
			String name = doc.getSpecialName();
			String portraitUri = "";
			if (StringUtils.isNotBlank(doc.getListSpecialPicture())) {
				portraitUri = doc.getListSpecialPicture();
			} else {
				portraitUri = PropertiesUtil.getString("DOMAINURL") + "img/defdoc.jpg";
			}
			ImTokenInfo tokeninfo = commonService.queryImTokenInfoByCon(userId,
					mode);
			if (tokeninfo != null) {
				map.put("token", tokeninfo.getToken());
				map.put("appkey", tokeninfo.getAppKey());
				map.put("userId", tokeninfo.getUserId());
			} else {
				Map<String, String> retmap = ImChatUtil.getToken(userId, name,
						portraitUri, mode);
				map.put("token", retmap.get("token"));
				map.put("appkey", retmap.get("appkey"));
				map.put("userId", retmap.get("userId"));
				tokenindb(retmap, mode);
			}
		}
		return map;
	}

	private Map<String, Object> processDoc(String oid, Integer docid,
			String mode, String otype) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String userId = oid + docid + otype;
		MobileSpecial doc = commonService
				.queryMobileSpecialByUserIdAndUserType(docid);
		String name = doc.getSpecialName();
		String portraitUri = PropertiesUtil.getString("DOMAINURL") + "img/defdoc.jpg";
		ImTokenInfo tokeninfo = commonService.queryImTokenInfoByCon(userId,
				mode);
		if (tokeninfo != null) {
			map.put("token", tokeninfo.getToken());
			map.put("appkey", tokeninfo.getAppKey());
			map.put("userId", tokeninfo.getUserId());
		} else {
			Map<String, String> retmap = ImChatUtil.getToken(userId, name,
					portraitUri, mode);
			map.put("token", retmap.get("token"));
			map.put("appkey", retmap.get("appkey"));
			map.put("userId", retmap.get("userId"));
			tokenindb(retmap, mode);
		}
		return map;
	}

	private void tokenindb(Map<String, String> map, String mode) {
		ImTokenInfo tokeninfo = new ImTokenInfo();
		tokeninfo.setAppKey(map.get("appkey"));
		tokeninfo.setAppType(mode);
		tokeninfo.setToken(map.get("token"));
		tokeninfo.setUserId(map.get("userId"));
		commonService.saveImTokenInfo(tokeninfo);
	}

	/**
	 * 生成系统消息
	 */
	public void generateSystemPushInfo(Integer pushCode, String businessKey,
			Integer businessType,Integer userId,Integer userType,Map<String, String> map,String content) {
		SystemPushInfo pinfo = new SystemPushInfo();
		pinfo.setCreateTimeExtend(String.valueOf(System.currentTimeMillis()));
		UUID uuid = UUID.randomUUID();
		pinfo.setPushKey(uuid.toString().replace("-", ""));
		pinfo.setPushCode(pushCode);
		pinfo.setCreateTime(new Date());
		pinfo.setBusinessKey(businessKey);
		pinfo.setBusinessType(businessType);
		pinfo.setUserId(userId);
		pinfo.setUserType(userType);
		pinfo.setBusinessExtend(JSON.toJSONString(map));
		pinfo.setContent(content);
		commonService.saveSystemPushInfo(pinfo);
		
		//go easy推送,近推送专家和医生
	
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "messageNotify");
		jObj.put("content", JSONObject.fromObject(pinfo).toString());
		
		String channel = "未知cccccc";
		if(userType == 1){
			channel = "patient_" + userId;
		} else if(userType == 2){
			channel = "expert_" + userId;
		} else if(userType == 3){
			channel = "doctor_" + userId;
		}
		goEasy.publish(channel, jObj.toString());
		
		//app消息推送
		apiGetuiPushService.PushMessage(pinfo);
	}
	
	public List<SystemPushInfo> querySystemPushInfoByUser(Integer userId){
		return commonService.querySystemPushInfoByUser(userId);
	}
	
	public SystemPushInfo querySystemPushInfoByBusKey(String businessKey){
		return commonService.querySystemPushInfoByBusKey(businessKey);
	}
	public SystemPushInfo querySystemPushInfoByPushKey(String pushKey){
		return commonService.querySystemPushInfoByPushKey(pushKey);
	}
	
	//加入群组
	public String joinGroup(String wenzhenKey, Integer wenzhenType) {
		// TODO Auto-generated method stub
		String result = "没有对应的订单类型";
		switch (wenzhenType) {
			case 4:
				BusinessVedioOrder businessVedioOrder = wenzhenService.queryBusinessVedioOrderByUuid(wenzhenKey);
				if (null != businessVedioOrder) {
					ArrayList<String> list = new ArrayList<String>();
					if (null != businessVedioOrder.getLocalDoctorId()) {
						list.add(RongCloudApi.getRongCloudUserId(businessVedioOrder.getLocalDoctorId(), 3));
					}
					if (null != businessVedioOrder.getExpertId()) {
						list.add(RongCloudApi.getRongCloudUserId(businessVedioOrder.getExpertId(), 2));
					}
					String[] groupCreateUserId = list.toArray(new String[list.size()]);

					if (null != groupCreateUserId && groupCreateUserId.length > 0) {
						String groupId = businessVedioOrder.getUuid();
						if (StringUtils.isBlank(groupId)) {
							groupId = UUIDUtil.getUUID();
							businessVedioOrder.setUuid(groupId);
						}
						result = RongCloudApi.joinGroup(groupCreateUserId, groupId, "视频问诊");
					} else {
						result = "没有群组成员";
					}
				}
				break;
		}
		return result;
	}
	
	public void updateSystemPushInfo(SystemPushInfo pinfo){
		commonService.updateSystemPushInfo(pinfo);
	}
	
	public List<MobileSpecial> loadExperts_wx(Integer pageNo,Integer pageSize,Integer depid,String hosid,String standdepid,String zc,String keywords){
		return commonService.queryExperts_wx(pageNo,pageSize,depid,hosid,standdepid,zc,keywords);
	}
	
	/**
	 * 企业后台获取提现申请数据
	 * @param paramMap
	 * @return
	 */
	public String querydoctorwithdrawdatas(Map<String,String> paramMap){
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		String busitype = paramMap.get("busitype");
		StringBuilder stringJson = null;
		Map<String, Object> retmap =commonService.queryDoctorWithdrawdatas("","",searchContent, start, length,null,ostatus,busitype);
		Integer renum = (Integer) retmap.get("num");
		List<DoctorWithdrawInfo> rcs = (List<DoctorWithdrawInfo>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		DoctorWithdrawInfo dw = null;
		for (int i = 0; i < rcs.size(); i++) {
			dw = rcs.get(i);
			stringJson.append("[");
			stringJson.append("\"" +dw.getId()+ "\",");
			String docinfo="";
			docinfo+=(StringUtils.isNotBlank(dw.getDocName())?dw.getDocName():"");
			docinfo+=(StringUtils.isNotBlank(dw.getHosName())?"/"+dw.getHosName():"");
			docinfo+=(StringUtils.isNotBlank(dw.getDepName())?"/"+dw.getDepName():"");
			docinfo+=(StringUtils.isNotBlank(dw.getTelephone())?"/"+dw.getTelephone():"");
			stringJson.append("\"" +docinfo+ "\",");
			stringJson.append("\"" +gainBlank(dw)+ "\",");
			stringJson.append("\"" +dw.getMoney()+ "\",");
			stringJson.append("\"" +(dw.getTaxationMoney()==null?"":dw.getTaxationMoney())+ "\",");
			stringJson.append("\"" +(dw.getActualMoney()==null?"":dw.getActualMoney())+ "\",");
			stringJson.append("\"" +gainStatusDesc(dw.getStatus())+ "\",");
			stringJson.append("\"" +sdf.format(dw.getCreateTime())+ "\",");
			stringJson.append("\"" +(dw.getAuditTime()!=null?sdf.format(dw.getCreateTime()):"")+ "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	private String gainBlank(DoctorWithdrawInfo info){
		String cardNo=info.getCardNo();
		String desc="";
		if(StringUtils.isNotBlank(cardNo)){
			if(cardNo.length()>4){
				desc=info.getIssuingBank()+"(尾号"+cardNo.substring(cardNo.length()-4, cardNo.length())+")";
			}else{
				desc=info.getIssuingBank()+"(尾号"+cardNo+")";
			}
		}
		return desc;
	}
	private String gainStatusDesc(Integer status){
		String desc="待审核";
		if(status!=null){
			switch(status){
			case 1:
				desc="待审核";
				break;
			case 2:
				desc="审核通过";
				break;
			case 3:
				desc="审核未通过";
				break;
			}
			
		}
		return desc;
	}
	
	/**
	 * 企业后台账单数据
	 * @param paramMap
	 * @return
	 */
	public String querydocbilldatas(Map<String,String> paramMap){
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String search=paramMap.get("search");
		String docname=paramMap.get("docname");
		String busitype=paramMap.get("busitype");
		String actions=paramMap.get("actions");
		String startDate=paramMap.get("startDate");
		String endDate=paramMap.get("endDate");
		Map<String,Object> querymap=new HashMap<String,Object>();
		if(StringUtils.isNotBlank(search)){
			querymap.put("search", search);
		}
		if(StringUtils.isNotBlank(docname)){
			querymap.put("docname", docname);
		}
		if(StringUtils.isNotBlank(busitype)){
			querymap.put("busitype", busitype);
		}
		
		if(StringUtils.isNotBlank(actions)){
			querymap.put("actions", actions);
		}
		
		if(StringUtils.isNotBlank(startDate)){
			querymap.put("startDate", startDate);
		}
		if(StringUtils.isNotBlank(endDate)){
			querymap.put("endDate", endDate);
		}
		StringBuilder stringJson = null;
		Map<String, Object> retmap =commonService.querydoctorbilldatas(querymap, start, length,null);
		Integer renum = (Integer) retmap.get("num");
		List<DoctorBillInfo> rcs = (List<DoctorBillInfo>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		DoctorBillInfo bill = null;
		for (int i = 0; i < rcs.size(); i++) {
			bill = rcs.get(i);
			stringJson.append("[");
			stringJson.append("\"" +bill.getId()+ "\",");
			stringJson.append("\"" +bill.getDocName()+ "\",");
			stringJson.append("\"" +bill.getHosName()+ "\",");
			stringJson.append("\"" +bill.getDocTel()+ "\",");
			stringJson.append("\"" +bill.getSource()+ "\",");
			stringJson.append("\"" +(bill.getType().equals(1)?"收入":(bill.getType().equals(2)?"支出":"提成"))+ "\",");
			stringJson.append("\"" +bill.getActualMoney()+ "\",");
			stringJson.append("\"" +bill.getOriginalMoney()+ "\",");
			stringJson.append("\"" +bill.getTaxationMoney()+ "\",");
			stringJson.append("\"" +bill.getSubsidyMoney()+ "\",");
			stringJson.append("\"" +bill.getCurAccount()+ "\",");
			stringJson.append("\"" +bill.getPreAccount()+ "\",");
			stringJson.append("\"" +sdf.format(bill.getCreateTime())+ "\"");
			stringJson.append("],");
		}
		if (rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	
	/**
	 * 账单的业务类型
	 * @param bill
	 * @return
	 */
	private String gainDesc(DoctorBillInfo bill){
		String desc="未知";
		if(bill.getBusinessType()!=null){
			switch(bill.getBusinessType()){
			case -1:
				desc="提现";
				break;
			case 1:
				desc="图文咨询";
				break;
			case 2:
				desc="电话咨询";
				break;
			case 4:
				desc="远程门诊";
				break;
			case 5:
				desc="专家咨询";
				break;
			}
		}
		return desc;
	}
	
	/**
	 * 专家，医生端提现数据
	 * @param paramMap
	 * @param docid
	 * @return
	 */
	public String querydocwithdraws(Map<String,String> paramMap,Integer docid){
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String startDate=paramMap.get("startDate");
		String endDate=paramMap.get("endDate");
		StringBuilder stringJson = null;
		Map<String, Object> retmap =commonService.queryDoctorWithdrawdatas(startDate,endDate,searchContent, start, length,docid,null,null);
		Integer renum = (Integer) retmap.get("num");
		List<DoctorWithdrawInfo> rcs = (List<DoctorWithdrawInfo>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		DoctorWithdrawInfo dw = null;
		for (int i = 0; i < rcs.size(); i++) {
			dw = rcs.get(i);
			stringJson.append("[");
			stringJson.append("\"" +dw.getId()+ "\",");
			stringJson.append("\"" +gainBlank(dw)+ "\",");
			stringJson.append("\"" +dw.getMoney()+ "\",");
			stringJson.append("\"" +(dw.getTaxationMoney()==null?"-":(dw.getTaxationMoney().toString()))+ "\",");
			stringJson.append("\"" +dw.getActualMoney()+ "\",");
			stringJson.append("\"" +gainStatusDesc(dw.getStatus())+ "\",");
			stringJson.append("\"" +(dw.getAuditTime()==null?"":sdf.format(dw.getAuditTime()))+ "\",");
			stringJson.append("\"" +sdf.format(dw.getCreateTime())+ "\"");
			stringJson.append("],");
		}
		if (rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		System.out.println("==="+stringJson.toString());
		return stringJson.toString();
	}
	public String querydocwithdraws_old(Map<String,String> paramMap,Integer docid){
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String startDate=paramMap.get("startDate");
		String endDate=paramMap.get("endDate");
		StringBuilder stringJson = null;
		Map<String, Object> retmap =commonService.queryDoctorWithdrawdatas(startDate,endDate,searchContent, start, length,docid,null,null);
		Integer renum = (Integer) retmap.get("num");
		List<DoctorWithdrawInfo> rcs = (List<DoctorWithdrawInfo>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		DoctorWithdrawInfo dw = null;
		for (int i = 0; i < rcs.size(); i++) {
			dw = rcs.get(i);
			stringJson.append("[");
			stringJson.append("\"" +dw.getId()+ "\",");
			stringJson.append("\"" +gainBlank(dw)+ "\",");
			stringJson.append("\"" +dw.getMoney()+ "\",");
			stringJson.append("\"" +dw.getTaxationMoney()==null?"-":dw.getTaxationMoney()+ "\",");
			stringJson.append("\"" +dw.getActualMoney()+ "\",");
			stringJson.append("\"" +gainStatusDesc(dw.getStatus())+ "\",");
			stringJson.append("\"" +(dw.getAuditTime()==null?"":sdf.format(dw.getAuditTime()))+ "\",");
			stringJson.append("\"" +sdf.format(dw.getCreateTime())+ "\"");
			stringJson.append("],");
		}
		if (rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		System.out.println("==="+stringJson.toString());
		return stringJson.toString();
	}
	
	/**
	 * 专家，医生端账单数据
	 * @param paramMap
	 * @param docid
	 * @return
	 */
	public String querydocbilldatas_doc(Map<String,String> paramMap,Integer docid){
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String docname=paramMap.get("docname");
		String busitype=paramMap.get("busitype");//业务类型
		String actions=paramMap.get("actions");//操作类型
		String startDate=paramMap.get("startDate");
		String endDate=paramMap.get("endDate");
		System.out.println();
		Map<String,Object> querymap=new HashMap<String,Object>();
		if(StringUtils.isNotBlank(docname)){
			querymap.put("docname", docname);
		}
		if(StringUtils.isNotBlank(busitype)){
			querymap.put("busitype", busitype);
		}
		
		if(StringUtils.isNotBlank(actions)){
			querymap.put("actions", actions);
		}
		
		if(StringUtils.isNotBlank(startDate)){
			querymap.put("startDate", startDate);
		}
		if(StringUtils.isNotBlank(endDate)){
			querymap.put("endDate", endDate);
		}
		StringBuilder stringJson = null;
		Map<String, Object> retmap =commonService.querydoctorbilldatas(querymap, start, length,docid);
		Integer renum = (Integer) retmap.get("num");
		List<DoctorBillInfo> rcs = (List<DoctorBillInfo>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		DoctorBillInfo bill = null;
		for (int i = 0; i < rcs.size(); i++) {
			bill = rcs.get(i);
			stringJson.append("[");
			stringJson.append("\"" +bill.getId()+ "\",");
			stringJson.append("\"" +bill.getSource()+ "\",");
			stringJson.append("\"" +(bill.getType().equals(1)?"收入":"支出")+ "\",");
			stringJson.append("\"" +bill.getActualMoney()+ "\",");
			stringJson.append("\"" +bill.getOriginalMoney()+ "\",");
			stringJson.append("\"" +(bill.getTaxationMoney()==null?"-":bill.getTaxationMoney())+ "\",");
			stringJson.append("\"" +bill.getSubsidyMoney()+ "\",");
			stringJson.append("\"" +bill.getCurAccount()+ "\",");
			stringJson.append("\"" +bill.getPreAccount()+ "\",");
			stringJson.append("\"" +sdf.format(bill.getCreateTime())+ "\"");
			stringJson.append("],");
		}
		if (rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		System.out.println("==="+stringJson.toString());
		return stringJson.toString();
	}
	
	
	/**
	 * 医生专家收入策略
	 * @param oid
	 * @param otype
	 */
	public void revenueStrategy(Integer oid,Integer otype){
		SystemServiceInfo ssi=commonService.querySystemServiceInfoById(otype);
		BigDecimal docDivided=ssi.getDoctorDivided();
		BigDecimal expDivided=ssi.getExpertDivided();
		//给医生打款
		if(docDivided!=null&&docDivided.compareTo(BigDecimal.ZERO)>0){
			//生成账单,并更新医生账户金额
			generateDocBill(otype,oid,3,docDivided);
		}
		//给专家打款
		if(expDivided!=null&&expDivided.compareTo(BigDecimal.ZERO)>0){
			//生成账单，并更新医生账户金额
			generateDocBill(otype,oid,2,expDivided);
		}
	} 
	/**
	 * 订单完成  给专家或医生发送完成订单短信和推送消息
	 * @param oid
	 * @param otype
	 */
	@SuppressWarnings("unused")
	private void smsmessagenotify(Integer oid,Integer otype){
		Map<String, String> map = new HashMap<>();
		Integer docid=null;//医生
		Integer expid=null;//专家
		String uuid="";
		String content="";//待定
		switch(otype){
		case 1://图文订单
			BusinessTuwenOrder tuwen=wenzhenService.queryBusinessTuwenInfoById(oid);
			expid=tuwen.getDoctorId();
			map.put("closerId", String.valueOf(tuwen.getCloserId()));
			map.put("closerType", String.valueOf(tuwen.getCloserType()));
			uuid=tuwen.getUuid();
			break;
		case 2://电话订单
			BusinessTelOrder tel=wenzhenService.queryBusinessTelOrderById(oid);
			map.put("closerId", String.valueOf(tel.getCloserId()));
			map.put("closerType", String.valueOf(tel.getCloserType()));
			expid=tel.getDoctorId();
			uuid=tel.getUuid();
			break;
		case 4://远程订单
			BusinessVedioOrder vedio=wenzhenService.queryBusinessVedioOrderById(oid);
			map.put("closerId", String.valueOf(vedio.getCloserId()));
			map.put("closerType", String.valueOf(vedio.getCloserType()));
			expid=vedio.getExpertId();
			docid=vedio.getLocalDoctorId();
			uuid=vedio.getUuid();
			break;
		case 5://专家咨询订单
			SpecialAdviceOrder advice=commonService.querySpecialAdviceOrderById(oid);
			map.put("closerId", String.valueOf(advice.getCloserId()));
			map.put("closerType", String.valueOf(advice.getCloserType()));
			expid=advice.getExpertId();
			docid=advice.getDoctorId();
			uuid=advice.getUuid();
			break;
		}	
		if(docid!=null){
			generateSystemPushInfo(21, uuid, otype, docid,3,map, content);////给医生推送消息
			sendSms(docid,content);//给医生发送短信
		}
		if(expid!=null){
			//给专家推送消息
			generateSystemPushInfo(21, uuid, otype, expid,2,map, content);
			sendSms(expid,content);//给专家发送短信
		}
	}
	/**
	 * 给专家或医生发送短信
	 * @param docid
	 * @param content
	 */
	private void sendSms(Integer docid,String content){
		DoctorRegisterInfo reg=commonService.queryDoctorRegisterInfoById(docid);
		HttpSendSmsUtil.sendSmsInteface(reg.getMobileNumber(), content);
	}
	private  void generateDocBill(Integer otype,Integer oid,Integer ltype,BigDecimal divided){
		BigDecimal actualMoney=null;
		BigDecimal originalMoney=null;
		BigDecimal curAccount=null;
		Integer docid=null;
		BigDecimal money=null;//订单金额
		String uuid="";
		String source="";
		Integer payStatus=null;//是否已支付
		switch(otype){
		case 1:
			BusinessTuwenOrder tuwen=wenzhenService.queryBusinessTuwenInfoById(oid);
			docid=tuwen.getDoctorId();
			money=gainMoneyByOrder(otype, docid);
			uuid=tuwen.getUuid();
			source="图文咨询";
			payStatus=tuwen.getPayStatus();
			break;
		case 2:
			BusinessTelOrder tel=wenzhenService.queryBusinessTelOrderById(oid);
			docid=tel.getDoctorId();
			money=gainMoneyByOrder(otype,docid);
			uuid=tel.getUuid();
			source="电话咨询";
			payStatus=tel.getPayStatus();
			break;
		case 4:
			BusinessVedioOrder vedio=wenzhenService.queryBusinessVedioOrderById(oid);
			if(ltype.equals(2)){
				docid=vedio.getExpertId();
			}else if(ltype.equals(3)){
				docid=vedio.getLocalDoctorId();
			}
			if(StringUtils.isNotBlank(vedio.getExLevel())){
				//选择了级别，使用级别价格
				money=new BigDecimal(commonService.queryDictionaryByCon(vedio.getExLevel(),25).getDisplayValue());
			}else{
				money=gainMoneyByOrder(otype,docid);
			}
			uuid=vedio.getUuid();
			source="远程门诊";
			payStatus=vedio.getPayStatus();
			break;
		case 5:
			SpecialAdviceOrder advice=commonService.querySpecialAdviceOrderById(oid);
			if(ltype.equals(2)){
				docid=advice.getExpertId();
			}else if(ltype.equals(3)){
				docid=advice.getDoctorId();
			}
			money=gainMoneyByOrder(otype,docid);
			uuid=advice.getUuid();
			source="专家咨询";
			payStatus=advice.getPayStatus();
			break;
		}
		if(payStatus!=null&&payStatus.equals(1)){
			//已支付的订单进行分账
			actualMoney=money.multiply(divided);//实际收入金额/实际支出金额(收入时=账单金额+补贴-税费；支出时=到账金额+税费）
			originalMoney=money.multiply(divided);//账单金额/实际到账金额
			billorregprocess(docid,source,uuid,otype,actualMoney,originalMoney,1);
		}
	}
	/**
	 * 账单生成及账户余额更新
	 * @param docid
	 * @param source
	 * @param actualMoney
	 * @param originalMoney
	 * @param curAccount
	 */
	private void billorregprocess(Integer docid,String source,String uuid,Integer otype,
			BigDecimal actualMoney,BigDecimal originalMoney,Integer type){
		DoctorRegisterInfo reg=commonService.queryDoctorRegisterInfoById(docid);
		BigDecimal balance=reg.getBalance()==null?new BigDecimal(0):reg.getBalance();//原有账户金额
		DoctorBillInfo bill=new DoctorBillInfo();
		bill.setDoctorId(docid);
		bill.setCreateTime(new Timestamp(System.currentTimeMillis()));
		bill.setSource(source);
		bill.setType(type);
		bill.setActualMoney(actualMoney);
		bill.setOriginalMoney(originalMoney);
		bill.setCurAccount(balance.add(actualMoney));//当前账户余额（收入时=收支前余额 +实际收入金额）（提现时 = 收支前余额-实际支出账额）
		bill.setTaxationMoney(new BigDecimal(0));//扣除税费金额，收入时暂不扣税，提现扣税
		bill.setSubsidyMoney(new BigDecimal(0));//平台补贴金额，平台暂时没有补贴（收入时）
		bill.setPreAccount(balance);//收支前余额
		bill.setBusinessId(uuid);
		bill.setBusinessType(otype);
		commonService.saveDoctorBillInfo(bill);
		//更新账户金额
		reg.setBalance(bill.getActualMoney().add(balance));
		commonService.updateDoctorRegisterInfo(reg);
	}
	
	/**
	 * 获取专家登陆数据
	 * @param paramMap
	 * @return
	 */
	public String queryexlogindata(Map<String,String> paramMap){
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String search=paramMap.get("search");
		Map<String,Object> querymap=new HashMap<String,Object>();
		if(StringUtils.isNotBlank(search)){
			querymap.put("search", search);
		}
		StringBuilder stringJson = null;
		Map<String, Object> retmap =commonService.queryexlogindatas(querymap, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<UserDevicesRecord> rcs = (List<UserDevicesRecord>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		UserDevicesRecord udr = null;
		for (int i = 0; i < rcs.size(); i++) {
			udr = rcs.get(i);
			stringJson.append("[");
			//专家账号，专家姓名，登陆设备，登陆时间，登陆版本号
			stringJson.append("\"" +udr.getId()+ "\",");
			stringJson.append("\"" +udr.getLoginName()+ "\",");
			stringJson.append("\"" +udr.getDocName()+ "\",");
			stringJson.append("\"" +udr.getPlatform()+"("+udr.getModel()+")"+ "\",");
			stringJson.append("\"" +udr.getLastLoginTime()+ "\",");
			stringJson.append("\"" +udr.getLoginVersion()+ "\",");
			stringJson.append("\"" +udr.getLoginNum()+ "\"");
			stringJson.append("],");
		}
		if (rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	@Autowired
	private ID2pService d2pService;
	//获取专家咨询聊天需要的
	public Map<String,Object> gainadviceim(HttpServletRequest request){
		DoctorRegisterInfo user = (DoctorRegisterInfo) request.getSession().getAttribute("user");
		Map<String,Object> map=new HashMap<String,Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 订单id
		String _otype=request.getParameter("otype");
		Integer otype=null;//订单类型  4:远程  5:专家咨询,10：预约转诊
		if(StringUtils.isNotBlank(_otype)){
			otype=Integer.parseInt(_otype);
		}else{
			otype=4;
		}
		Integer type=Integer.parseInt(request.getParameter("type"));//接收方：2,发送方：3
		Integer docid=null;//医生id
		Integer exid=null;//专家id
		String uuid="";
		String groupname="";
		if(otype.equals(4)){
			BusinessVedioOrder border=commonService.queryBusinessVedioOrderById(oid);
			docid=border.getLocalDoctorId();
			exid=border.getExpertId();
			uuid=border.getUuid();
			groupname="视频问诊";
		}else if(otype.equals(5)){
			SpecialAdviceOrder order=commonService.querySpecialAdviceOrderById(oid);
			docid=order.getDoctorId();
			exid=order.getExpertId();
			uuid=order.getUuid();
			groupname="图文会诊";
		}else if(otype.equals(10)){
			BusinessD2dReferralOrder rorder=d2pService.queryd2dreferralOrderbyId(oid);
			docid=rorder.getDoctorId();
			exid=rorder.getReferralDocId();
			uuid=rorder.getUuid();
			groupname="预约转诊";
		}
		Map<String,Object> docmap=new HashMap<String,Object>();
		Map<String,Object> exmap=new HashMap<String,Object>();
		if(docid!=null){
			MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(docid);
			docmap.putAll(processgroup(docid,uuid,groupname,doc.getUserType()));
		}
		if(exid!=null){
			MobileSpecial exp=commonService.queryMobileSpecialByUserIdAndUserType(exid);
			exmap.putAll(processgroup(exid,uuid,groupname,exp.getUserType()));
		}
		if(docid!=null&&docid.equals(user.getId())){
			map.put("token", docmap.get("token"));
		}
		if(exid!=null&&exid.equals(user.getId())){
			map.put("token", exmap.get("token"));
		}
		map.put("userId", uuid);
		map.put("appkey",RongCloudConfig.APPKEY);
		return map;
	} 
	
	private Map<String,Object> processgroup(Integer docid,String uuid,String groupname,Integer utype){
		Map<String,Object> map=new HashMap<String,Object>();
		//加入群组
		DoctorRegisterInfo docreg=commonService.queryDoctorRegisterInfoById(docid);
		DoctorDetailInfo docdetail=commonService.queryDoctorDetailInfoById(docid);
		//token
		String doctoken=docreg.getRongCloudToken();
		String docuserid=RongCloudApi.getRongCloudUserId(docid, utype);
		String portraitUrl="";
		if(!StringUtils.isNotBlank(doctoken)){
			String defurl=PropertiesUtil.getString("DOMAINURL") + "img/defdoc.jpg";
			if(StringUtils.isNotBlank(docdetail.getHeadImageUrl())){
				portraitUrl=docdetail.getHeadImageUrl();
			}else{
				portraitUrl=defurl;
			}
			//生成token
			doctoken=RongCloudApi.getUserToken(docuserid, docdetail.getDisplayName(), portraitUrl);
			docreg.setRongCloudToken(doctoken);
			commonService.updateDoctorRegisterInfo(docreg);
		}
		RongCloudApi.joinGroup(new String[]{docuserid},uuid, groupname);
		map.put("token", doctoken);
		return map;
	}
	/**
	 * 获取医生服务数据
	 * @param paramMap
	 * @param docid
	 * @param userType
	 * @return
	 */
	public String queryserverdatas(Map<String, String> paramMap,Integer docid){
		MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(docid);
		String sEcho = paramMap.get("sEcho");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String search=paramMap.get("search");
		Integer dtype=Integer.parseInt(paramMap.get("dtype"));//1 患者服务，2协作服务
		Map<String,Object> querymap=new HashMap<String,Object>();
		querymap.put("dtype", dtype);
		if(StringUtils.isNotBlank(search)){
			querymap.put("search", search);
		}
		querymap.put("docid", docid);
		querymap.put("userType", doc.getUserType());
		StringBuilder stringJson = null;
		Map<String, Object> retmap =commonService.queryserverdatas(querymap, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<SystemServiceInfo> rcs = (List<SystemServiceInfo>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		SystemServiceInfo ssi = null;
		DoctorServiceInfo dsi=null;
		Integer doctorServiceId=null;
		String isopen="";
		BigDecimal money=null;
		BigDecimal defaultMoney=new BigDecimal(0);
		SystemServiceDefault ssd=null;
		for (int i = 0; i < rcs.size(); i++) {
			defaultMoney=new BigDecimal(0);
			ssi = rcs.get(i);
			dsi=commonService.queryDoctorServiceInfoByCon(docid, ssi.getId(), null);
			ssd=commonService.querySystemServiceDefaultByCon(ssi.getId(),doc.getDutyId());
			if(ssd!=null)defaultMoney=ssd.getDefaultMoney();
			if(dsi!=null){
				//已设置
				doctorServiceId=dsi.getId();
				if(dsi.getIsOpen().equals(1)){
					isopen="已开通";
				}else{
					isopen="未开通";
				}
				money=dsi.getAmount();
			}else{
				//未设置
				doctorServiceId=-1;
				money=new BigDecimal(0);
				isopen="未开通";
			}
			stringJson.append("[");
			stringJson.append("\"" +doctorServiceId+ "\",");//医生服务id
			stringJson.append("\"" +ssi.getId()+ "\",");//系统服务id
			stringJson.append("\"" +ssi.getServiceName()+ "\",");//服务名称
			stringJson.append("\"" +money+ "\",");//服务价格
			stringJson.append("\"" +defaultMoney+ "\",");//指导价格
			stringJson.append("\"" +ssi.getDescription()+ "\",");//服务说明
			stringJson.append("\"" +isopen+ "\",");
			stringJson.append("\"" +""+ "\"");
			stringJson.append("],");
		}
		if (rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<SystemServiceInfo> queryserverdatas_new(Integer dtype,Integer docid){
		MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(docid);
		Map<String,Object> querymap=new HashMap<String,Object>();
		querymap.put("dtype", dtype);
		querymap.put("docid", docid);
		querymap.put("userType", doc.getUserType());
		Map<String, Object> retmap =commonService.queryserverdatas(querymap, null, null);
		List<SystemServiceInfo> list = (List<SystemServiceInfo>) retmap.get("items");
		BigDecimal defaultMoney=null;
		BigDecimal money=null;
		DoctorServiceInfo dsi=null;
		SystemServiceDefault ssd=null;
		Integer doctorServiceId=null;
		Integer isOpen=null;
		for (SystemServiceInfo ssi : list) {
			defaultMoney=new BigDecimal(0);
			dsi=commonService.queryDoctorServiceInfoByCon(docid, ssi.getId(), null);
			ssd=commonService.querySystemServiceDefaultByCon(ssi.getId(),doc.getDutyId());
			if(ssd!=null)defaultMoney=ssd.getDefaultMoney();
			if(dsi!=null){
				//已设置
				doctorServiceId=dsi.getId();
				if(dsi.getIsOpen().equals(1)){
					isOpen=1;
				}else{
					isOpen=0;
				}
				money=dsi.getAmount();
			}else{
				//未设置
				doctorServiceId=-1;
				money=new BigDecimal(0);
				isOpen=0;
			}
			ssi.setIsOpen(isOpen);
			ssi.setMoney(money);
			ssi.setDefaultMoney(defaultMoney);
			ssi.setDoctorServiceId(doctorServiceId);
		}
		return list;
	}
	/**
	 * 更新
	 * @param docid
	 * @param doctorServiceId
	 * @param systemServiceId
	 * @param sval
	 * @param smoney
	 */
	public void changServerStatus(Integer docid,Integer doctorServiceId,Integer systemServiceId,Integer sval,BigDecimal smoney){
		BigDecimal showPrice = null;
		if(systemServiceId.equals(4)) {
			//视频会诊
			SystemServiceInfo ssi = commonService.querySystemServiceById(systemServiceId);
			showPrice = processD2DVedioMoney(smoney,BigDecimal.valueOf(Double.valueOf(ssi.getPriceParameter())));
		}else if(systemServiceId.equals(5)) {
			//图文会诊
			showPrice = processD2DAskMoney(smoney);
		}
		if(doctorServiceId.equals(-1)&&sval.equals(1)){
			//新开通
			DoctorServiceInfo ds=new DoctorServiceInfo();
			ds.setAmount(smoney);
			ds.setDoctorId(docid);
			ds.setIsOpen(sval);
			ds.setServiceId(systemServiceId);
			ds.setShowPrice(showPrice);
			commonService.saveDoctorServiceInfo(ds);
		}
		if(!doctorServiceId.equals(-1)){
			DoctorServiceInfo ds=commonService.queryDoctorServiceInfoByCon(docid, systemServiceId, null);
			ds.setIsOpen(sval);
			if(sval.equals(1)){
				ds.setAmount(smoney);
			}
			ds.setShowPrice(showPrice);
			commonService.updateDoctorServiceInfo(ds);
		}
	}
	/**
	 * 处理d2d 图文会诊价格问题
	 * @param openAsk
	 * @param askAmount
	 * @return
     */
	public BigDecimal processD2DAskMoney(BigDecimal askAmount) {
		//askAmount = askAmount.divide(BigDecimal.valueOf(6),1,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(10));
		return askAmount;
	}
	/**
	 * 处理d2d 视频会诊价格问题
	 * @param openVedio
	 * @param vedioAmount
	 * @param defaultMoney
	 * @return
	 */
	public BigDecimal processD2DVedioMoney(BigDecimal vedioAmount,BigDecimal defaultMoney) {
		if(defaultMoney.equals(BigDecimal.ZERO)) defaultMoney = defaultMoney.add(BigDecimal.valueOf(50));
		vedioAmount = vedioAmount.add(defaultMoney);
		return vedioAmount;
	}
	
	public Map<String,Object> sudocscheduleshow(HttpServletRequest request){
		DoctorRegisterInfo  user = (DoctorRegisterInfo) request.getSession().getAttribute(
				"user");
		Map<String,Object> map=new HashMap<String,Object>();
		String id=request.getParameter("id");
		boolean isexist=false;
		if(request.getParameter("status").equalsIgnoreCase("1")){
			DoctorScheduleShow show=commonService.isExistDoctorScheduleShowOpen(user.getId(),Integer.parseInt(request.getParameter("week")),Integer.parseInt(request.getParameter("outTime")));
			if(show==null){
				isexist=false;
			}else{
				if(StringUtils.isNotBlank(id)&&show.getId().equals(Integer.parseInt(id))){
					isexist=false;
				}else{
					isexist=true;
				}
			}
		}
		if(!isexist){
			DoctorScheduleShow dss=null;
			if(StringUtils.isNotBlank(id)){
				//编辑
				dss=commonService.queryDoctorScheduleShowById(Integer.parseInt(id));
			}else{
				//新增
				dss=new DoctorScheduleShow();
			}
			dss.setAddress(request.getParameter("address"));
			dss.setDoctorId(user.getId());
			dss.setOutTime(Integer.parseInt(request.getParameter("outTime")));
			dss.setOutType(Integer.parseInt(request.getParameter("outType")));
			dss.setStatus(Integer.parseInt(request.getParameter("status")));
			dss.setWeekDay(Integer.parseInt(request.getParameter("week")));
			BigDecimal cost=new BigDecimal(0);
			if(StringUtils.isNotBlank(request.getParameter("cost"))){
				cost=new BigDecimal(request.getParameter("cost"));
			}
			dss.setCost(cost);
			if(!StringUtils.isNotBlank(id)){
				id=commonService.saveDoctorScheduleShow(dss)+"";
			}else{
				commonService.updateDoctorScheduleShow(dss);
			}
			map.put("tid", id);
			map.put("status", "success");
		}else{
			map.put("status", "error");
		}
		return map;
	}
	
	public void modifyMsgToRead(Integer docid,Integer otype,Integer oid){
		if(otype.equals(5)){
			SpecialAdviceOrder order=commonService.querySpecialAdviceOrderById(oid);
			commonService.updateMsgToRead(order.getUuid(),5,docid);
		} 
	}

	/**
	 * 获取专家登陆数据
	 * @param paramMap
	 * @return
	 */
	public String querydoclogindata(Map<String,String> paramMap){
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String search=paramMap.get("search");
		Map<String,Object> querymap=new HashMap<String,Object>();
		if(StringUtils.isNotBlank(search)){
			querymap.put("search", search);
		}
		StringBuilder stringJson = null;
		Map<String, Object> retmap =commonService.querydoclogindatas(querymap, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<UserDevicesRecord> rcs = (List<UserDevicesRecord>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		UserDevicesRecord udr = null;
		for (int i = 0; i < rcs.size(); i++) {
			udr = rcs.get(i);
			stringJson.append("[");
			//医生账号，医生姓名，登录总次数
			stringJson.append("\"" +udr.getId()+ "\",");
			stringJson.append("\"" +udr.getLoginName()+ "\",");
			stringJson.append("\"" +udr.getDocName()+ "\",");
			stringJson.append("\"" +udr.getHosName()+ "\",");
			stringJson.append("\"" +udr.getDepName()+ "\",");
			stringJson.append("\"" +udr.getPlatform()+"("+udr.getModel()+")"+ "\",");
			stringJson.append("\"" +udr.getLastLoginTime()+ "\",");
			stringJson.append("\"" +udr.getLoginVersion()+ "\",");
			stringJson.append("\"" +udr.getTodaycounts()+ "\",");
			stringJson.append("\"" +udr.getLoginNum()+ "\"");
			stringJson.append("],");
		}
		if (rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
}
