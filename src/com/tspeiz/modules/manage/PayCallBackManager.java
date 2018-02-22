package com.tspeiz.modules.manage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.ExpertConsultationSchedule;
import com.tspeiz.modules.common.entity.newrelease.BusinessPayInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessTelOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pFastaskOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pTelOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pTuwenOrder;
import com.tspeiz.modules.common.entity.release2.BusinessT2pTuwenOrder;
import com.tspeiz.modules.common.entity.release2.UserWarmthInfo;
import com.tspeiz.modules.common.service.IApiGetuiPushService;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.ID2pService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.util.PythonVisitUtil;
import com.tspeiz.modules.util.common.GeneUserBillRecordUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.log.RecordLogUtil;

@Service
public class PayCallBackManager {
	private Logger log=Logger.getLogger(PayCallBackManager.class);
	private SimpleDateFormat _format = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private IWeixinService weixinService;
	@Autowired
	private CommonManager commonManager;
	@Autowired
	private ID2pService d2pService;
	@Autowired
    private IApiGetuiPushService apiGetuiPushService;
	/**
	 * 在线问诊订单支付回调处理
	 * 
	 * @param result
	 */
	public void updateWenzhenNotifyOrder(String result) {
		try {
			Document doc = DocumentHelper.parseText(result);
			Element rootElt = doc.getRootElement(); // 获取根节点
			String tradeNo = rootElt.elementText("out_trade_no");
			BusinessPayInfo rc = commonService
					.queryBusinessPayInfoByTradeNo(tradeNo);
			Map<String, String> map = new HashMap<>();
			// 更新服务订单状态
			if (rc.getPayStatus() != null && rc.getPayStatus().equals(4)) {
				rc.setPayStatus(1);// 已付款
				rc.setPayTime(new Timestamp(System.currentTimeMillis()));
				commonService.updateBusinessPayInfo(rc);
				String content = "";
				String type = "";
				String oid = "";
				boolean send=true;
				if (rc.getOrderType().equals(1)) {
					type = "1";
					// 图文问诊
					BusinessTuwenOrder order = wenzhenService
							.queryBusinessTuwenInfoById(rc.getOrderId());
					order.setPayStatus(1);
					wenzhenService.updateBusinessTuwenOrder(order);
					CaseInfo caseinfo = wenzhenService.queryCaseInfoById(order.getCaseId());
					GeneUserBillRecordUtil.geneBill(rc.getId(),
							order.getUserId(), order.getDoctorId(),
							rc.getOnlinePay(), "消费", "图文问诊", "");
					oid = order.getId() + "";
					content = "有用户提交了图文问诊订单，请尽快安排处理。【佰医汇】";
					//记录消息
					CaseInfo ca=wenzhenService.queryCaseInfoById(order.getCaseId());
			        map = apiGetuiPushService.setPushPatientExtend(map, caseinfo.getSubUserUuid());
					commonManager.generateSystemPushInfo(21, order.getUuid(), 1, order.getDoctorId(), 2,map, "您有一个来自患者"+ca.getContactName()+"图文咨询订单，请及时处理。");
				} else if (rc.getOrderType().equals(2)) {
					type = "2";
					// 电话问诊
					BusinessTelOrder telinfo = wenzhenService
							.queryBusinessTelOrderById(rc.getOrderId());
					telinfo.setTalkDur(600);
					telinfo.setPayStatus(1);
					wenzhenService.updateBusinessTelOrder(telinfo);
					CaseInfo caseinfo = wenzhenService.queryCaseInfoById(telinfo.getCaseId());
					GeneUserBillRecordUtil.geneBill(rc.getId(),
							telinfo.getUserId(), telinfo.getDoctorId(),
							rc.getOnlinePay(), "消费", "电话问诊", "");
					content = "有用户提交了电话问诊订单，请尽快安排处理。【佰医汇】";
					oid = telinfo.getId() + "";
					//记录消息
					CaseInfo ca=wenzhenService.queryCaseInfoById(telinfo.getCaseId());
					 map = apiGetuiPushService.setPushPatientExtend(map, caseinfo.getSubUserUuid());
					commonManager.generateSystemPushInfo(21, telinfo.getUuid(), 2, telinfo.getDoctorId(), 2,map, "您有一个来自患者"+ca.getContactName()+"的电话咨询订单，请等待客服和您核实时间。");
				}else if(rc.getOrderType().equals(5)){
					//专家咨询
					type = "5";
					SpecialAdviceOrder sao=commonService.querySpecialAdviceOrderById(rc.getOrderId());
					sao.setPayStatus(1);
					commonService.updateSpecialAdviceOrder(sao);
					/*GeneUserBillRecordUtil.geneBill(rc.getId(),
							sao.getUserId(), telinfo.getDoctorId(),
							rc.getOnlinePay(), "消费", "电话问诊", "");*/
					MobileSpecial special=commonService.queryMobileSpecialByUserIdAndUserType(sao.getExpertId());
					content = special.getHosName()+special.getDepName()+"的"+special.getSpecialName()+"医生提交了专家咨询订单，请尽快安排处理。【佰医汇】";
					oid = sao.getId() + "";
					//记录消息
					MobileSpecial _doc=commonService.queryMobileSpecialByUserIdAndUserType(sao.getDoctorId());
					map = apiGetuiPushService.setPushDoctorExtend(map, sao.getExpertId());
					commonManager.generateSystemPushInfo(21, sao.getUuid(), 5, sao.getExpertId(), 2,map, "您有一个"+_doc.getHosName()+_doc.getSpecialName()+"的专家咨询订单，请及时处理。");
				}else if(rc.getOrderType().equals(4)){
					//远程会诊
					type="4";
					BusinessVedioOrder order=wenzhenService.queryBusinessVedioOrderById(rc.getOrderId());
					order.setPayStatus(1);
					commonService.updateBusinessVedioOrder(order);
					GeneUserBillRecordUtil.geneBill(rc.getId(),
							order.getUserId(), order.getExpertId(),
							rc.getOnlinePay(), "消费", "远程会诊", "");
					send=false;
				}else if(rc.getOrderType().equals(9)){
					//快速问诊
					type="9";
					BusinessD2pFastaskOrder forder=d2pService.queryd2pfastaskorderbyid(rc.getOrderId());
					forder.setPayStatus(1);
					forder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey());
					d2pService.updated2pfastaskorder(forder);
					GeneUserBillRecordUtil.geneBill(rc.getId(),
							forder.getUserId(), forder.getDoctorId(),
							rc.getOnlinePay(), "消费", "快速问诊", "");
					send=false;
				}else if(rc.getOrderType().equals(6)){
					send=false;
					type="6";
					BusinessD2pTuwenOrder tuwen=d2pService.queryd2ptuwenorderbyid(rc.getOrderId());
					tuwen.setPayStatus(1);
					tuwen.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
					d2pService.updated2ptuwenorder(tuwen);
					GeneUserBillRecordUtil.geneBill(rc.getId(),
							tuwen.getUserId(), tuwen.getDoctorId(),
							rc.getOnlinePay(), "消费", "图文问诊", "");
				}else if(rc.getOrderType().equals(7)){
					send=false;
					type="7";
					BusinessD2pTelOrder telorder=d2pService.queryd2ptelorderbyid(rc.getOrderId());
					telorder.setPayStatus(1);
					telorder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
					d2pService.updated2ptelorder(telorder);
					GeneUserBillRecordUtil.geneBill(rc.getId(),
							telorder.getUserId(), telorder.getDoctorId(),
							rc.getOnlinePay(), "消费", "电话问诊", "");
				}else if(rc.getOrderType().equals(10)){
					send=false;
					type="10";
					UserWarmthInfo uw=d2pService.queryuserwarminfo(rc.getOrderId());
					uw.setStatus(1);
					d2pService.updateuserwarminfo(uw);
					GeneUserBillRecordUtil.geneBill(rc.getId(),
							uw.getUserId(), uw.getDoctorId(),
							rc.getOnlinePay(), "消费", "送心意", "");
				}else if(rc.getOrderType().equals(12)){
					send=false;
					type="12";
					BusinessT2pTuwenOrder tto=d2pService.querybusinesst2ptuwenById(rc.getOrderId());
					tto.setPayStatus(1);
					tto.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
					d2pService.updatet2ptuwen(tto);
					GeneUserBillRecordUtil.geneBill(rc.getId(),
							tto.getUserId(), tto.getDoctorId(),
							rc.getOnlinePay(), "消费", "团队问诊", "");
				}
				if(send){
					HttpSendSmsUtil.sendSmsInteface("13521231353,15001299884",
							content);
				}
				RecordLogUtil.update(type, oid, rc.getId() + "", result,
						tradeNo);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 远程订单支付回调处理
	 * 
	 * @param result
	 */
	public void updateRemoteNotifyOrder(String result) {
		try {
			Document doc = DocumentHelper.parseText(result);
			Element rootElt = doc.getRootElement(); // 获取根节点
			String tradeNo = rootElt.elementText("out_trade_no");
			BusinessPayInfo pinfo = commonService
					.queryBusinessPayInfoByTradeNo(tradeNo);
			// 更新服务订单状态
			if (pinfo.getPayStatus() != null && pinfo.getPayStatus().equals(4)) {
				pinfo.setPayTime(new Timestamp(System.currentTimeMillis()));
				pinfo.setPayStatus(1);// 已支付
				pinfo.setOutTradeResult(result);
				commonService.updateBusinessPayInfo(pinfo);
				BusinessVedioOrder rc = commonService
						.queryBusinessVedioOrderById(pinfo.getOrderId());
				rc.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
				rc.setPayStatus(1);
				commonService.updateBusinessVedioOrder(rc);
				//记录消息
				recordmess(rc);
				GeneUserBillRecordUtil.geneBill(pinfo.getId(), rc.getUserId(),
						rc.getExpertId() != null ? rc.getExpertId() : null,
						pinfo.getOnlinePay(), "消费", "特需远程门诊", "");
				// 更新时间为已选
				if (rc.getSchedulerDateId() != null) {
					ExpertConsultationSchedule sch = weixinService
							.queryExpertConScheduleById(rc.getSchedulerDateId());
					sch.setHasAppoint("1");
					weixinService.updatExpertConScheduleDate(sch);
					HttpSendSmsUtil.sendSmsInteface("13521231353,15001299884",
							"有用户通提交了远程门诊订单，请尽快安排处理。【佰医汇】");
					// 对接his系统进行挂号
					toplusInHis(rc);
					// 更新日志
					RecordLogUtil.update("4", rc.getId() + "", pinfo.getId()
							+ "", result, tradeNo);
				}
				/*if(rc.getStatus()!=null&&rc.getStatus().equals(10)){
					//已完成的，继续支付成功的需要给医生或专家打款分账
					commonManager.revenueStrategy(rc.getId(), 4);//打款
					//发送消息
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void recordmess(BusinessVedioOrder order){
        Map<String, String> map = new HashMap<>();
		if(order.getExpertId()!=null){
			//专家端
			HospitalDetailInfo hos=commonService.getHospitalDetailInfoById(order.getLocalHospitalId());
	        map = apiGetuiPushService.setPushDoctorExtend(map, order.getExpertId());
			commonManager.generateSystemPushInfo(21, order.getUuid(), 4, order.getExpertId(), 2,map, "您有一个来自"+hos.getDisplayName()+"的会诊订单，请提前查看订单详情及患者病历。");
		}
		if(order.getLocalDoctorId()!=null){
			//医生端
			CaseInfo ca=wenzhenService.queryCaseInfoById(order.getCaseId());
			map = apiGetuiPushService.setPushDoctorExtend(map, order.getLocalDoctorId());
			commonManager.generateSystemPushInfo(21, order.getUuid(), 4, order.getLocalDoctorId(), 3,map, "您有一个来自患者"+ca.getContactName()+"的会诊订单，请提前查看订单详情及完善患者病历。");
		}
	}
	// 支付完成时进行六安挂号
	private void toplusInHis(BusinessVedioOrder rc) throws Exception {
		Integer local_depid = rc.getLocalDepartId();
		HospitalDepartmentInfo depart = weixinService
				.queryHospitalDepartmentInfoById(local_depid);
		if (depart.getLocalDepId() != null) {
			log.info("=====六安挂号开始====");
			CaseInfo caseinfo = commonService.queryCaseInfoById(rc.getCaseId());
			Map<String, Object> pinfo = new HashMap<String, Object>();
			pinfo.put("name", caseinfo.getContactName());
			pinfo.put("id_card", caseinfo.getIdNumber());
			pinfo.put("telephone", caseinfo.getTelephone());
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
			rc.setLocalPlusNo(map.get("eid").toString());
			rc.setLocalPlusRet(map.get("message").toString());
			commonService.updateBusinessVedioOrder(rc);
			// 发送短信通知
			if (map.get("eid").toString().equalsIgnoreCase("0")) {
				sendsms(rc, caseinfo);
			}
			log.info("=====六安挂号结束====");
		}
	}
	
	private Integer gainType(BusinessVedioOrder rc) {
		Integer type = 3;
		String time = rc.getConsultationDate().trim();
		if (time.equalsIgnoreCase(_format.format(new Date()))) {
			type = 0;
		}
		return type;
	}

	private String gaintime(BusinessVedioOrder rc) {
		String time = rc.getConsultationDate().trim() + " "
				+ rc.getConsultationTime().trim() + ":00";
		return time;
	}
	
	private void sendsms(BusinessVedioOrder rc,CaseInfo caseinfo) {
		// 挂号成功
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(rc.getExpertId());
		String time = rc.getConsultationDate() + " " + rc.getConsultationTime();
		// =================患者短信============================
		String patient_content = "尊敬的用户，恭喜您预约成功，会诊时间：" + time
				+ "。请您准备好病历资料，提前一小时到您预约医院等候。详情请登录“佰医汇”微信公众号查看我的订单。【佰医汇】";
		HttpSendSmsUtil.sendSmsInteface(caseinfo.getTelephone(), patient_content);
		// ================客服短信================================
		StringBuilder kefu_content = new StringBuilder();
		kefu_content.append("已有患者预约成功。患者详细信息如下：\n");
		kefu_content.append("姓名：" + caseinfo.getContactName() + "\n");
		if (caseinfo.getSex() != null) {
			if (caseinfo.getSex().equals(1))
				kefu_content.append("性别：男\n");
			if (caseinfo.getSex().equals(2))
				kefu_content.append("性别：女\n");
		}
		kefu_content.append("手机号码：" + caseinfo.getTelephone() + "\n");
		if (StringUtils.isNotBlank(caseinfo.getIdNumber())) {
			kefu_content.append("身份证：" + caseinfo.getIdNumber() + "\n");
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
}
