package com.tspeiz.modules.manage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.PushCodeEnum;
import com.tspeiz.modules.common.bean.PushWordBean;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;
import com.tspeiz.modules.common.entity.newrelease.UserDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.UserWeixinRelative;
import com.tspeiz.modules.common.entity.release2.BusinessD2dReferralOrder;
import com.tspeiz.modules.common.entity.release2.DoctorConsultationOpinion;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;
import com.tspeiz.modules.common.service.IApiGetuiPushService;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.ID2pService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.oss.OSSManageUtil;
import com.tspeiz.modules.util.weixin.WeixinUtil;
import com.uwantsoft.goeasy.client.goeasyclient.GoEasy;

@Service
public class OrderManager {
	@Autowired
	private ICommonService commonService;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private SimpleDateFormat _sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private CommonManager commonManager;
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private ID2pService d2pService;
	@Autowired
	private IWeixinService weixinService;
	@Autowired
    private IApiGetuiPushService apiGetuiPushService;

	/**
	 * 获取订单中签名二维码
	 * @param orderUuid
	 * @param orderType
	 * @return
	 */
	public String gainOrderSigErweima(String orderUuid,Integer orderType) throws Exception{
		String url="";
		BusinessVedioOrder order=null;
		SpecialAdviceOrder adviceOrder=null;
		if(orderType.equals(4)){
			order=commonService.queryBusinessVedioOrderByUid(orderUuid);
			url=order.getSignatureErWeiMa();
		}else if(orderType.equals(5)){
			adviceOrder=commonService.querySpecialAdviceOrderByUid(orderUuid);
			url=adviceOrder.getSignatureErWeiMa();
		}
		if(StringUtils.isNotBlank(url)){
			return url;
		}
		//生成签名二维码，并更新到订单中
		String visitUrl=PropertiesUtil.getString("signature_erweima")+"?orderUuid="+orderUuid+"&orderType="+orderType;
		url = OSSManageUtil.genErweima(sdf.format(new Date()),visitUrl);
		if(orderType.equals(4)){
			commonService.updateBusinessVedioOrder(order);
		}else if(orderType.equals(5)){
			commonService.updateSpecialAdviceOrder(adviceOrder);
		}
		return url;
	}
	/**
	 * 上传数字签名后推送消息到web
	 * @param url
	 * @param orderUuid
	 * @param orderType
	 */
	public void sendMessageAfterUpload(String url,String orderUuid,Integer orderType){
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "jSignature");
		jObj.put("from", 2);
		jObj.put("orderUuid", orderUuid);
		jObj.put("orderType", orderType);
		jObj.put("url",url);
		System.out.println("==="+jObj.toString());
		goEasy.publish(orderUuid + "", jObj.toString() + "");
	}
	public Integer saveOrUpdateReport(String reportId,String orderUuid,Integer orderType,String diagnosis,
			String treatPlan,String attentions,String signature,String photoReport,HttpSession session){
		Integer _reportId=null;
		DoctorConsultationOpinion opinion=null;
		boolean issave=false;
		if(StringUtils.isNotBlank(reportId)){
			//编辑
			opinion=commonService.queryDoctorConsultationOpinion(Integer.parseInt(reportId));
		}else{
			//新增
			opinion=new DoctorConsultationOpinion();
			opinion.setCreateTime(new Timestamp(System.currentTimeMillis()));
			opinion.setUuid(UUID.randomUUID().toString().replace("-", ""));
			issave=true;
		}
		opinion.setOrderType(orderType);
		opinion.setOrderUuid(orderUuid);
		opinion.setDiagnosis(diagnosis);
		opinion.setTreatPlan(treatPlan);
		opinion.setAttentions(attentions);
		opinion.setSignature(signature);
		opinion.setPhotoReport(photoReport);
		if(issave){	
			_reportId=commonService.saveDoctorConsultationOpinion(opinion);
		}else{
			commonService.updateDoctorConsultationOpinion(opinion);
			_reportId=opinion.getId();
		}
		Integer docId=null;
		String content="";
		Integer userId=null;
		String title="";
		String username="";
		String time="";
		Integer expId=null;
		String subUserUuid="";
		if(orderType.equals(4)){
			BusinessVedioOrder order=commonService.queryBusinessVedioOrderByUid(orderUuid);
			order.setConsultationOpinionUuid(opinion.getUuid());
			order.setReplyTime(new Timestamp(System.currentTimeMillis()));
			commonService.updateBusinessVedioOrder(order);
			docId=order.getLocalDoctorId();
			content=PushWordBean.VEDIO_ORDER_SAVE_EDIT_REPORT;
			userId=order.getUserId();
			title="视频会诊通知";
			time=_sdf.format(order.getCreateTime());
			expId=order.getExpertId();
			subUserUuid=order.getSubUserUuid();
		}else if(orderType.equals(5)){
			SpecialAdviceOrder order=commonService.querySpecialAdviceOrderByUid(orderUuid);
			order.setConsultationOpinionUuid(opinion.getUuid());
			commonService.updateSpecialAdviceOrder(order);
			docId=order.getDoctorId();
			content=PushWordBean.TUWEN_ORDER_SAVE_EDIT_REPORT;
			userId=order.getUserId();
			title="图文会诊通知";
			time=_sdf.format(order.getCreateTime());
			expId=order.getExpertId();
			subUserUuid=order.getSubUserUuid();
		}
		sendMessageToWeb(orderUuid,orderType,session,opinion);
		//给医生推送消息
		MobileSpecial doc=null;
		if(docId!=null){
			doc=commonService.queryMobileSpecialByUserIdAndUserType(docId);
			Map<String, String> map = new HashMap<>();
	        // 添加消息附属信息
	        map = apiGetuiPushService.setPushDoctorExtend(map,docId);
			commonManager.generateSystemPushInfo(PushCodeEnum.SAVE_EDIT_REPORT.getCode(), orderUuid, orderType, docId, doc.getUserType(),null, content);
		}
		//给患者推送微信消息，如果该订单绑定了患者
		if(userId!=null){
			UserWeixinRelative uwr=commonService.queryUserWeixinRelativeByCondition(PropertiesUtil.getString("APPID"),userId);
			String openid=uwr.getOpenId();
			if(StringUtils.isNotBlank(openid)){
				if(StringUtils.isNotBlank(subUserUuid)){
					UserContactInfo uc=weixinService.queryUserContactInfoByUuid(subUserUuid);
					username=uc.getContactName();
				}
				if(!StringUtils.isNotBlank(username)){
					UserDetailInfo user=weixinService.queryUserDetailInfoById(userId);
					username=user.getDisplayName();
				}
				MobileSpecial exp=commonService.queryMobileSpecialByUserIdAndUserType(expId);
				WeiAccessToken wat=weixinService.queryWeiAccessTokenById(PropertiesUtil.getString("APPID"));
				WeixinUtil.makeNewsCustomMessage(wat.getAccessToken(), openid, orderUuid,orderType,title, username, doc.getSpecialName(), exp.getSpecialName(), time);
			}
		}
		return _reportId;
	}
	private void sendMessageToWeb(String orderUuid,Integer orderType,HttpSession session,DoctorConsultationOpinion opinion){
		Integer orderid=null;
		Integer doctorId=null;
		if(orderType.equals(4)){
			BusinessVedioOrder order=commonService.queryBusinessVedioOrderByUid(orderUuid);
			doctorId=order.getLocalDoctorId();
			// 记录系统消息 医生查看
			commonManager.saveBusinessMessageInfo_sys_new(orderUuid, 5, "RC:TxtMsg",
					"专家填写就诊结果报告", order.getLocalDoctorId(), 3);	
		}else if(orderType.equals(5)){
			SpecialAdviceOrder adviceOrder=commonService.querySpecialAdviceOrderByUid(orderUuid);
			doctorId=adviceOrder.getDoctorId();
			// 记录系统消息 医生查看
			commonManager.saveBusinessMessageInfo_sys_new(orderUuid, 5, "RC:TxtMsg",
					"专家填写就诊结果报告",adviceOrder.getDoctorId(), 3);
		}
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		String sponsor = user.getDisplayName();

		MobileSpecial userDetail = (MobileSpecial) session.getAttribute("userDetail");
		// 目标用户订阅组号
		String outsideGroup = "doctor_"+doctorId;
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "reportNotify");
		jObj.put("from", 2);
		jObj.put("orderUuid", orderid);
		jObj.put("sponsor", sponsor);
		jObj.put("diagnosis", opinion.getDiagnosis());// 诊断意见
		jObj.put("treatPlan", opinion.getTreatPlan());// 治疗方案
		jObj.put("attentions", opinion.getAttentions());// 注意事项
		jObj.put("signature", opinion.getSignature());// 数字签名
		jObj.put("photoReport", opinion.getPhotoReport());// 图片报告
		jObj.put("hosName", userDetail.getHosName());
		jObj.put("depName", userDetail.getDepName());
		goEasy.publish(orderUuid + "", jObj.toString() + "");
		goEasy.publish(outsideGroup + "", jObj.toString() + "");
	}
	
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
	
	public DoctorConsultationOpinion loadReportInfo(String orderUuid,Integer orderType){
		String opUuid="";
		if(orderType.equals(4)){
			BusinessVedioOrder order=commonService.queryBusinessVedioOrderByUid(orderUuid);
			opUuid=order.getConsultationOpinionUuid();
		}else if(orderType.equals(5)){
			SpecialAdviceOrder order=commonService.querySpecialAdviceOrderByUid(orderUuid);
			opUuid=order.getConsultationOpinionUuid();
		}
		return commonService.queryDoctorConsultationOpinionByUuid(opUuid);
	}
	
	public void modifyConTime(String orderUuid,Integer orderType,String newTime){
		if(orderType.equals(4)){
			BusinessVedioOrder order=commonService.queryBusinessVedioOrderByUid(orderUuid);
			String[] _times=newTime.split(" ");
			order.setConsultationDate(_times[0]);
			if(_times.length>0)order.setConsultationTime(_times[1]);
			commonService.updateBusinessVedioOrder(order);
			DoctorDetailInfo doc = commonService.getLocalDocTel(order.getLocalDoctorId());
			String content="佰医汇提醒：您有一个视频会诊订单已被上级医生接诊，会诊时间为："+_times+"，请及时与患者沟通，在约定时间会诊【佰医汇】";
			HttpSendSmsUtil.sendSmsInteface(doc.getTelNumber(), content);
		}else if(orderType.equals(10)){
			BusinessD2dReferralOrder order=d2pService.queryBusinessD2pReferralOrderByUuid(orderUuid);
			order.setReferralDate(newTime);
			d2pService.updateBusinessD2dReferralOrder(order);
		}
        
	}

	/**
	 * 处理订单状态
	 * @param orderType
	 * @param orderId
	 * @param sval
	 * @param refusalReason
	 * @param session
	 */
	public void processOrderStatus(Integer orderType,Integer orderId,Integer sval,String refusalReason,HttpSession session){
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		if(orderType.equals(4)){
			processVedioOrder(orderId,sval,refusalReason,user.getId());
		}else if(orderType.equals(5)){
			processD2dTuwenOrder(orderId,sval,refusalReason,user.getId());
		}else if(orderType.equals(10)){
			processReferralOrder(orderId,sval,refusalReason,user.getId());
		}
	}
	
	/**
	 * 处理视频订单状态
	 * @param orderId
	 * @param sval
	 * @param refusalReason
	 * @param userId
	 * @param userType
	 */
	private void processVedioOrder(Integer orderId,Integer sval,String refusalReason,Integer userId){
		BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(orderId);
		order.setStatus(sval);
		order.setRefusalReason(refusalReason);
		commonService.updateBusinessVedioOrder(order);
        Map<String, String> map = new HashMap<>();
		if(sval.equals(OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey())){
			//接诊
			//推送接诊消息
			if(order.getLocalDoctorId()!=null){
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(order.getLocalDoctorId());
		        map = apiGetuiPushService.setPushDoctorExtend(map, order.getLocalDoctorId());
				commonManager.generateSystemPushInfo(PushCodeEnum.AgreeOrder.getCode(), order.getUuid(), 4, order.getLocalDoctorId(), doc.getUserType(),map, PushWordBean.VEDIO_ORDER_AGREE_EXP);
			}
		}else if(sval.equals(OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey())){
			//拒诊
			//推送拒诊消息
			if(order.getLocalDoctorId()!=null){
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(order.getLocalDoctorId());
				 map = apiGetuiPushService.setPushDoctorExtend(map, order.getLocalDoctorId());
				commonManager.generateSystemPushInfo(PushCodeEnum.RefuseOrder.getCode(), order.getUuid(), 4, order.getLocalDoctorId(),doc.getUserType(),map, PushWordBean.VEDIO_ORDER_REFULSE_EXP);
			}
		}else if(sval.equals(OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey())){
			//取消
			if(order.getExpertId()!=null){
				MobileSpecial exp=commonService.queryMobileSpecialByUserIdAndUserType(order.getExpertId());
				 map = apiGetuiPushService.setPushDoctorExtend(map, order.getExpertId());
				commonManager.generateSystemPushInfo(PushCodeEnum.CancelOrder.getCode(), order.getUuid(), 4, order.getExpertId(), exp.getUserType(),map, PushWordBean.VEDIO_ORDER_CANCEL_EXP);
			}
		}else if(sval.equals(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey())){
			//完成
		}
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "launchNotify");
		goEasy.publish(order.getUuid(), jObj.toString() + "");
	}
	
	/**
	 * 处理图文订单状态
	 * @param orderId
	 * @param sval
	 * @param refusalReason
	 * @param userId
	 * @param userType
	 */
	private void processD2dTuwenOrder(Integer orderId,Integer sval,String refusalReason,Integer userId){
		SpecialAdviceOrder adviceOrder=commonService.querySpecialAdviceOrderById(orderId);
		adviceOrder.setStatus(sval);
		adviceOrder.setRefusalReason(refusalReason);
		commonService.updateSpecialAdviceOrder(adviceOrder);
        Map<String, String> map = new HashMap<>();
		if(sval.equals(OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey())){
			adviceOrder.setReceiveTime(new Timestamp(System.currentTimeMillis()));
			commonService.updateSpecialAdviceOrder(adviceOrder);
			//接诊
			//推送接诊消息
			if(adviceOrder.getDoctorId()!=null){
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(adviceOrder.getDoctorId());
		        map = apiGetuiPushService.setPushDoctorExtend(map, adviceOrder.getDoctorId());
				commonManager.generateSystemPushInfo(PushCodeEnum.AgreeOrder.getCode(), adviceOrder.getUuid(), 5, adviceOrder.getDoctorId(), doc.getUserType(),map, PushWordBean.TUWEN_ORDER_AGREE_EXP);
			}
		}else if(sval.equals(OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey())){
			//拒诊
			//推送拒诊消息
			if(adviceOrder.getDoctorId()!=null){
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(adviceOrder.getDoctorId());
				map = apiGetuiPushService.setPushDoctorExtend(map, adviceOrder.getDoctorId());
				commonManager.generateSystemPushInfo(PushCodeEnum.RefuseOrder.getCode(), adviceOrder.getUuid(), 5, adviceOrder.getDoctorId(), doc.getUserType(),map, PushWordBean.TUWEN_ORDER_REFULSE_EXP);
			}
		}else if(sval.equals(OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey())){
			//取消
			if(adviceOrder.getExpertId()!=null){
				MobileSpecial exp=commonService.queryMobileSpecialByUserIdAndUserType(adviceOrder.getExpertId());
				map = apiGetuiPushService.setPushDoctorExtend(map, adviceOrder.getExpertId());
				commonManager.generateSystemPushInfo(PushCodeEnum.CancelOrder.getCode(), adviceOrder.getUuid(), 5, adviceOrder.getExpertId(), exp.getUserType(),map, PushWordBean.TUWEN_ORDER_CANCEL_EXP);
			}
		}else if(sval.equals(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey())){
			//完成
		}
	}
	
	/**
	 * 处理转诊订单状态
	 * @param orderId
	 * @param sval
	 * @param refusalReason
	 * @param userId
	 * @param userType
	 */
	private void processReferralOrder(Integer orderId,Integer sval,String refusalReason,Integer userId){
		BusinessD2dReferralOrder referralOrder=d2pService.queryd2dreferralOrderbyId(orderId);
		referralOrder.setStatus(sval);
		referralOrder.setRefusalReason(refusalReason);
		d2pService.updateBusinessD2dReferralOrder(referralOrder);
		Map<String, String> map = new HashMap<>();
		if(sval.equals(OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey())){
			referralOrder.setReceiveTime(new Timestamp(System.currentTimeMillis()));
			d2pService.updateBusinessD2dReferralOrder(referralOrder);
			//接诊
			//推送接诊消息
			if(referralOrder.getDoctorId()!=null){
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(referralOrder.getDoctorId());
				map.put("doctorId", String.valueOf(referralOrder.getDoctorId()));
				map.put("referralType", String.valueOf(referralOrder.getReferralType()));
		        map = apiGetuiPushService.setPushDoctorExtend(map, referralOrder.getDoctorId());
				commonManager.generateSystemPushInfo(PushCodeEnum.AgreeOrder.getCode(), referralOrder.getUuid(), 10, referralOrder.getDoctorId(), doc.getUserType(),map, PushWordBean.REFER_ORDER_AGREE_EXP);
			}
		}else if(sval.equals(OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey())){
			//拒诊
			//推送拒诊消息
			if(referralOrder.getDoctorId()!=null){
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(referralOrder.getDoctorId());
				map.put("doctorId", String.valueOf(referralOrder.getDoctorId()));
				map.put("referralType", String.valueOf(referralOrder.getReferralType()));
				map = apiGetuiPushService.setPushDoctorExtend(map, referralOrder.getDoctorId());
				commonManager.generateSystemPushInfo(PushCodeEnum.RefuseOrder.getCode(), referralOrder.getUuid(), 10, referralOrder.getDoctorId(), doc.getUserType(),map, PushWordBean.REFER_ORDER_REFULSE_EXP);
			}
		}else if(sval.equals(OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey())){
			//取消
			if(referralOrder.getReferralDocId()!=null){
				MobileSpecial exp=commonService.queryMobileSpecialByUserIdAndUserType(referralOrder.getReferralDocId());
				map.put("doctorId", String.valueOf(referralOrder.getDoctorId()));
				map.put("referralType", String.valueOf(referralOrder.getReferralType()));
				map = apiGetuiPushService.setPushDoctorExtend(map, referralOrder.getDoctorId());
				commonManager.generateSystemPushInfo(PushCodeEnum.CancelOrder.getCode(), referralOrder.getUuid(), 10, referralOrder.getReferralDocId(), exp.getUserType(),map, PushWordBean.REFER_ORDER_CANCEL_EXP);
			}	
		}else if(sval.equals(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey())){
			//完成
			if(userId.equals(referralOrder.getDoctorId())){
				map.put("doctorId", String.valueOf(referralOrder.getDoctorId()));
				map.put("referralType", String.valueOf(referralOrder.getReferralType()));
				map.put("closerId", String.valueOf(referralOrder.getCloserId()));
				map.put("closerType", String.valueOf(referralOrder.getCloserType()));
				MobileSpecial exp=commonService.queryMobileSpecialByUserIdAndUserType(referralOrder.getReferralDocId());
				map = apiGetuiPushService.setPushDoctorExtend(map, referralOrder.getDoctorId());
				commonManager.generateSystemPushInfo(PushCodeEnum.CloseOrder.getCode(), referralOrder.getUuid(), 10, referralOrder.getReferralDocId(), exp.getUserType(),map, PushWordBean.REFER_ORDER_COMPLETED_EXP);
			}
			if(userId.equals(referralOrder.getReferralDocId())){
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(referralOrder.getDoctorId());
				map.put("doctorId", String.valueOf(referralOrder.getDoctorId()));
				map.put("referralType", String.valueOf(referralOrder.getReferralType()));
				map.put("closerId", String.valueOf(referralOrder.getCloserId()));
				map.put("closerType", String.valueOf(referralOrder.getCloserType()));
				map = apiGetuiPushService.setPushDoctorExtend(map, referralOrder.getDoctorId());
				commonManager.generateSystemPushInfo(PushCodeEnum.CloseOrder.getCode(), referralOrder.getUuid(), 10, referralOrder.getDoctorId(), doc.getUserType(),map, PushWordBean.REFER_ORDER_COMPLETED_EXP);
			}
		}
	}
	
}
