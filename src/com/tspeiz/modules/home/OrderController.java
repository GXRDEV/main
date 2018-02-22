package com.tspeiz.modules.home;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.entity.newrelease.AppPcLogin;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;
import com.tspeiz.modules.common.entity.release2.BusinessD2dReferralOrder;
import com.tspeiz.modules.common.entity.release2.DoctorConsultationOpinion;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.ID2pService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.impl.WeixinServiceImpl;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.manage.OrderManager;
import com.tspeiz.modules.util.DataCatchUtil;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.common.LoadLisAndPacsDataUtil;
import com.tspeiz.modules.util.oss.OSSConfigure;
import com.tspeiz.modules.util.oss.OSSManageUtil;
import com.uwantsoft.goeasy.client.goeasyclient.GoEasy;


/**
 * 订单控制器--新增控制器
 * @author heyongb
 *
 */
@Controller
@RequestMapping("order")
public class OrderController {
	@Autowired  
	private ICommonService commonService;
	@Autowired
	private ID2pService d2pService;
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private IWeixinService weixinService;
	@Autowired
	private OrderManager orderManager;
	/**
	 * 加载订单信息
	 * 访问：http://localhost:8080/order/loadOrderInfo
	 * 参数：orderId:订单id
	 *       orderType:订单类型  4:视频会诊  5:咨询专家  10:预约转诊
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/loadOrderInfo")
	@ResponseBody
	public Map<String,Object> loadOrderInfo(HttpServletRequest request,HttpSession session){
		Integer orderType=Integer.parseInt(request.getParameter("orderType"));
		Integer orderId=Integer.parseInt(request.getParameter("orderId"));
		return loadOrderInfo(orderType,orderId,session);
	}
	/**
	 * 加载病例信息
	 * 访问：http://localhost:8080/order/loadCaseInfo
	 * 参数：orderId：订单id
	 *       orderType:订单类型  4:视频会诊  5:咨询专家  10:预约转诊
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loadCaseInfo")
	@ResponseBody
	public Map<String, Object> loadcaseinfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String orderId = request.getParameter("orderId");
		String orderType= request.getParameter("orderType");
		String caseUuid = request.getParameter("caseUuid");
		CaseInfo caseinfo =  null;
		if(StringUtils.isNotBlank(orderType) && StringUtils.isNotBlank(orderId)) {
			//订单中病例加载
			Integer caseid = gainOrderCaseId(Integer.parseInt(orderType),Integer.parseInt(orderId));
			caseinfo = commonService.queryCaseInfoById(caseid);
		}
		if(StringUtils.isNotBlank(caseUuid)) {
			caseinfo = commonService.queryCaseInfoByUuid(caseUuid);
		}
		if(!StringUtils.isNotEmpty(caseinfo.getTelephone())){
			String subUserUuid=caseinfo.getSubUserUuid();
			if(StringUtils.isNotBlank(subUserUuid)){
				UserContactInfo uc=weixinService.queryUserContactInfoByUuid(subUserUuid);
				caseinfo.setTelephone(uc.getTelphone());
			}
		}	
		// 病例信息
		map.put("caseinfo", caseinfo);
		// 入院记录
		if (StringUtils.isNotBlank(caseinfo.getNormalImages())) {
			List<CustomFileStorage> normals = wenzhenService
					.queryCustomFilesByCaseIds(caseinfo.getNormalImages());
			map.put("normals", normals);
		}
		//病例附件
		List<UserCaseAttachment> attachments = commonService.queryUserCaseAttachmentsByCaseUuid(caseinfo.getUuid());
		if(attachments != null && !attachments.isEmpty()) {
			for (UserCaseAttachment attachment : attachments) {
				if(StringUtils.isNotBlank(attachment.getAttachmentIds())){
					List<CustomFileStorage> files = wenzhenService.queryCustomFilesByCaseIds(attachment.getAttachmentIds());
					attachment.setFiles(files);
				}
			}
		}
		map.put("attachments", attachments);
		return map;
	}
	/**
	 * 获取pacs影像数据
	 * 访问：http://localhost:8080/order/gainPacsData
	 * 参数：orderId：订单id
	 *       orderType:订单类型  4:视频会诊  5:咨询专家  10:预约转诊
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gainPacsData")
	public @ResponseBody
	Map<String, Object> gainPacsData(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer orderId=Integer.parseInt(request.getParameter("orderId"));
		Integer orderType=Integer.parseInt(request.getParameter("orderType"));
		String uuid=gainOrderUuid(orderType,orderId);
		if(orderType.equals(4)){
			List<ReSourceBean> pac_records = DataCatchUtil.gainPacsData(orderId.toString());
			map.put("pac_records",
					LoadLisAndPacsDataUtil.gainPacsGroup(pac_records));
		}else{
			List<ReSourceBean> pac_records = DataCatchUtil.gainPacsData(uuid);
			map.put("pac_records",
					LoadLisAndPacsDataUtil.gainPacsGroup(pac_records));
		}
		return map;
	}
	/**
	 * 删除订单
	 * 访问：http://localhost:8080/order/delOrder
	 * 参数：orderId：订单id
	 *       orderType:订单类型  4:视频会诊  5:咨询专家  10:预约转诊
	 * @param request
	 * @param session
	 */
	@RequestMapping(value="/delOrder")
	@ResponseBody
	public void delOrder(HttpServletRequest request,HttpSession session){
		Integer orderId=Integer.parseInt(request.getParameter("orderId"));
		Integer orderType=Integer.parseInt(request.getParameter("orderType"));
		delOrder(orderType,orderId,session);
	}
	
	/**
	 * 更新订单状态（取消，退诊）
	 * 访问：http://localhost:8080/order/changeOrderStatus
	 * 参数：orderId：订单id
	 *       orderType:订单类型  4:视频会诊  5:咨询专家  10:预约转诊
	 *       sval:取消---50    退诊---30
	 * @param request
	 * @param session
	 */
	@RequestMapping(value="/changeOrderStatus")
	@ResponseBody
	public void changeOrderStatus(HttpServletRequest request,HttpSession session){
		Integer orderId=Integer.parseInt(request.getParameter("orderId"));
		Integer orderType=Integer.parseInt(request.getParameter("orderType"));
		Integer sval=Integer.parseInt(request.getParameter("sval"));
		changeOrderStatus(orderType,orderId,sval,session);
	}
	
	
	/**
	 * 订单状态处理
	 * 访问：http://localhost:8080/order/processOrderStatus
	 * 参数：orderId：订单id
	 *       orderType:订单类型  4:视频会诊  5:咨询专家  10:预约转诊
	 *       sval:接诊---20  退诊---30  取消---50  完成--40  
	 *       RefusalReason：退诊理由
	 * @param request
	 * @param session
	 */
	@RequestMapping(value="/processOrderStatus")
	@ResponseBody
	public void processOrderStatus(HttpServletRequest request,HttpSession session){
		Integer orderId=Integer.parseInt(request.getParameter("orderId"));
		Integer orderType=Integer.parseInt(request.getParameter("orderType"));
		Integer sval=Integer.parseInt(request.getParameter("sval"));
		String refusalReason=request.getParameter("refusalReason");
		orderManager.processOrderStatus(orderType,orderId,sval,refusalReason,session);
	}
	
	
	/**
	 * 获取订单中签名二维码
	 * 访问：http://localhost:8080/order/gainOrderSigErweima
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainOrderSigErweima")
	@ResponseBody
	public Map<String,Object> gainOrderSigErweima(HttpServletRequest request) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		String orderUuid=request.getParameter("orderUuid");
		Integer orderType=Integer.parseInt(request.getParameter("orderType"));
		String url=orderManager.gainOrderSigErweima(orderUuid,orderType);
		map.put("url", url);
		return map;
	}
	
	
	/**
	 * 上传数字签名  
	 * 访问：http://localhost:8080/order/uploadJSignature
	 * @param request
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/uploadJSignature")
	@ResponseBody
	public Map<String,String> uploadJSignature(HttpServletRequest request,@PathVariable MultipartFile file) throws Exception{
		String orderUuid=request.getParameter("orderUuid");
		Integer orderType=Integer.parseInt(request.getParameter("orderType"));
		String turnRate=request.getParameter("turnRate");//旋转角度 90,,180..
		Map<String,String> map=OSSManageUtil.uploadInputStream(
				file.getInputStream(), new OSSConfigure(),turnRate);
		orderManager.sendMessageAfterUpload(map.get("url"),orderUuid,orderType);
		return map;
	}
	
	
	/**
	 * 保存或编辑专家报告信息
	 * 访问：http://localhost:8080/order/saveOrUpdateReport
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveOrUpdateReport")
	@ResponseBody
	public Map<String,Object> saveOrUpdateReport(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		String reportId=request.getParameter("id");//报告id
		String orderUuid=request.getParameter("orderUuid");//订单uuid
		Integer orderType=Integer.parseInt(request.getParameter("orderType"));//订单类型  4：视频会诊，5：咨询专家
		String diagnosis=request.getParameter("diagnosis");//主要诊断
		String treatPlan=request.getParameter("treatPlan");//治疗建议
		String attentions=request.getParameter("attentions");//注意事项
		String signature=request.getParameter("signature");//数字签名
		String photoReport=request.getParameter("photoReport");//图片报告
		Integer _reportId=orderManager.saveOrUpdateReport(reportId,orderUuid,orderType,diagnosis,treatPlan,attentions,signature,photoReport,session);
		map.put("reportId", _reportId);
		return map;
	}
	/**
	 * 加载专家报告信息
	 * 访问：http://localhost:8080/order/loadReportInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/loadReportInfo")
	@ResponseBody
	public Map<String,Object> loadReportInfo(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String orderUuid=request.getParameter("orderUuid");//订单uuid
		Integer orderType=Integer.parseInt(request.getParameter("orderType"));//订单类型  4：视频会诊，5：咨询专家
		DoctorConsultationOpinion opinion=orderManager.loadReportInfo(orderUuid,orderType);
		map.put("opinion", opinion);
		return map;
	}
	/**
	 * 修改会诊时间  (视频--4，转诊--10）
	 * 访问：http://localhost:8080/order/modifyConTime
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/modifyConTime")
	@ResponseBody
	public Map<String,Object> modifyConTime(HttpServletRequest request){
		String orderUuid=request.getParameter("orderUuid");
		Integer orderType=Integer.parseInt(request.getParameter("orderType"));
		String newtime=request.getParameter("newTime");
		orderManager.modifyConTime(orderUuid,orderType,newtime);
		return null;
	}
	
	/**
	 * 更新订单状态（取消，退诊）
	 * @param otype
	 * @param oid
	 * @param sval
	 * @param session
	 */
	private void changeOrderStatus(Integer otype,Integer oid,Integer sval,HttpSession session){
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		switch(otype){
		case 4:
			BusinessVedioOrder vedioOrder=wenzhenService
					.queryBusinessVedioOrderById(oid);
			vedioOrder.setStatus(sval);
			vedioOrder.setClosedTime(new Timestamp(System.currentTimeMillis()));
			vedioOrder.setCloserId(user.getId());
			vedioOrder.setCloserType(user.getUserType());
			commonService.updateBusinessVedioOrder(vedioOrder);
			break;
		case 5:
			SpecialAdviceOrder adviceOrder=commonService
					.querySpecialAdviceOrderById(oid);
			adviceOrder.setStatus(sval);
			adviceOrder.setClosedTime(new Timestamp(System.currentTimeMillis()));
			adviceOrder.setCloserId(user.getId());
			adviceOrder.setCloserType(user.getUserType());
			commonService.updateSpecialAdviceOrder(adviceOrder);
			break;
		case 10:
			BusinessD2dReferralOrder referOrder=d2pService.queryd2dreferralOrderbyId(oid);
			referOrder.setStatus(sval);
			referOrder.setClosedTime(new Timestamp(System.currentTimeMillis()));
			referOrder.setCloserId(user.getId());
			referOrder.setCloserType(user.getUserType());
			d2pService.updateBusinessD2dReferralOrder(referOrder);
			break;
		}
	}
	
	/**
	 * 删除订单
	 * @param otype
	 * @param oid
	 * @param session
	 */
	private void delOrder(Integer otype,Integer oid,HttpSession session){
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		switch(otype){
		case 4:
			BusinessVedioOrder vedioOrder=wenzhenService
					.queryBusinessVedioOrderById(oid);
			vedioOrder.setDelFlag(1);
			vedioOrder.setClosedTime(new Timestamp(System.currentTimeMillis()));
			vedioOrder.setCloserId(user.getId());
			vedioOrder.setCloserType(user.getUserType());
			commonService.updateBusinessVedioOrder(vedioOrder);
			break;
		case 5:
			SpecialAdviceOrder adviceOrder=commonService
					.querySpecialAdviceOrderById(oid);
			adviceOrder.setDelFlag(1);
			adviceOrder.setClosedTime(new Timestamp(System.currentTimeMillis()));
			adviceOrder.setCloserId(user.getId());
			adviceOrder.setCloserType(user.getUserType());
			commonService.updateSpecialAdviceOrder(adviceOrder);
			break;
		case 10:
			BusinessD2dReferralOrder referOrder=d2pService.queryd2dreferralOrderbyId(oid);
			referOrder.setDelFlag(1);
			referOrder.setClosedTime(new Timestamp(System.currentTimeMillis()));
			referOrder.setCloserId(user.getId());
			referOrder.setCloserType(user.getUserType());
			d2pService.updateBusinessD2dReferralOrder(referOrder);
			break;
		}
	}
	
	
	/**
	 * 获取订单病例id
	 * @param otype
	 * @param oid
	 * @return
	 */
	private Integer gainOrderCaseId(Integer otype,Integer oid){
		Integer caseid=null;
		switch(otype){
		case 4:
			caseid = wenzhenService
					.queryBusinessVedioOrderById(oid).getCaseId();
			break;
		case 5:
			caseid =commonService
					.querySpecialAdviceOrderById(oid).getCaseId();
			break;
		case 10:
			caseid=d2pService.queryd2dreferralOrderbyId(oid).getCaseId();
			break;
		}
		return caseid;
	}
	/**
	 * 获取订单uuid
	 * @param otype
	 * @param oid
	 * @return
	 */
	private String gainOrderUuid(Integer otype,Integer oid){
		String uuid="";
		switch(otype){
		case 4:
			uuid = wenzhenService
					.queryBusinessVedioOrderById(oid).getUuid();
			break;
		case 5:
			uuid =commonService
					.querySpecialAdviceOrderById(oid).getUuid();
			break;
		case 10:
			uuid=d2pService.queryd2dreferralOrderbyId(oid).getUuid();
			break;
		}
		return uuid;
	}
	/**
	 * 根据类型获取订单记录
	 * @param otype
	 * @param oid
	 * @return
	 */
	private Map<String,Object> loadOrderInfo(Integer otype,Integer oid,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		DoctorRegisterInfo user = (DoctorRegisterInfo) session
				.getAttribute("user");
		Integer docid=null;
		Integer expid=null;
		switch(otype){
		case 4:
			BusinessVedioOrder order= commonService.queryBusinessVedioOrderById(oid);
			map.put("order", order);
			docid=order.getLocalDoctorId();
			expid=order.getExpertId();
			if(docid!=null&&docid.equals(user.getId())){
				map.put("sendOrReceive", "send");
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(docid);
				map.put("docName", doc.getSpecialName());
			}
			if(expid!=null&&expid.equals(user.getId())){
				map.put("sendOrReceive", "receive");
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(expid);
				map.put("docName", doc.getSpecialName());
			}
			break;
		case 5:
			SpecialAdviceOrder adviceOrder=commonService.querySpecialAdviceOrderById(oid);
			map.put("order",adviceOrder);
			docid=adviceOrder.getDoctorId();
			expid=adviceOrder.getExpertId();
			if(docid!=null&&docid.equals(user.getId())){
				map.put("sendOrReceive", "send");
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(docid);
				map.put("docName", doc.getSpecialName());
			}
			if(expid!=null&&expid.equals(user.getId())){
				map.put("sendOrReceive", "receive");
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(expid);
				map.put("docName", doc.getSpecialName());
			}	
			break;
		case 10:
			BusinessD2dReferralOrder referralOrder=d2pService.queryd2dreferralOrderbyId(oid);
			map.put("order", referralOrder);
			docid=referralOrder.getDoctorId();
			expid=referralOrder.getReferralDocId();
			if(docid!=null&&docid.equals(user.getId())){
				map.put("sendOrReceive", "send");
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(docid);
				map.put("docName", doc.getSpecialName());
			}
			if(expid!=null&&expid.equals(user.getId())){
				map.put("sendOrReceive", "receive");
				MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(expid);
				map.put("docName", doc.getSpecialName());
			}	
			break;
		}
		return map;
	}
	
	@RequestMapping(value="/testGo")
	@ResponseBody 
	public void testGo(){
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "jSignature"+System.currentTimeMillis());
		jObj.put("from", 2);
		jObj.put("orderUuid", "test");
		jObj.put("orderType", 9);
		jObj.put("url","");
		goEasy.publish("patient_16003", jObj.toString() + "");
	}
}
