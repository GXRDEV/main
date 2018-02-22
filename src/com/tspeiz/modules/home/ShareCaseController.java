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

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gexin.rp.sdk.base.IPushResult;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.MedicalRecords;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessTelOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenZhenInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenzhenTel;
import com.tspeiz.modules.common.entity.newrelease.CaseImages;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.ShortUrlRelate;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.newrelease.UserDevicesRecord;
import com.tspeiz.modules.common.entity.newrelease.UserWeixinRelative;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.common.thread.TokenThread;
import com.tspeiz.modules.util.PasswordUtil;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.common.GetuiPushUtils;
import com.tspeiz.modules.util.config.ReaderConfigUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.weixin.CommonUtil;
import com.tspeiz.modules.util.weixin.SignUtil;

@Controller
@RequestMapping("share")
public class ShareCaseController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Resource
	private ICommonService commonService;
	@Resource
	private IWenzhenService wenzhenService;
	@Resource
	private IWeixinService weixinService;
	
	@RequestMapping(value="/case/{param}",method=RequestMethod.GET)
	public ModelAndView sharecase2(HttpServletRequest request,@PathVariable String param) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		ShortUrlRelate surl=commonService.queryShortUrlRelate(param);
		Integer type=surl.getOrderType();
		Integer oid=surl.getOrderId();
		Integer caseid = null;
		Integer docid = null;
		Integer localdocid=null;
		switch (type) {
		case 1:
			// 图文
			BusinessTuwenOrder twinfo=wenzhenService.queryBusinessTuwenInfoById(oid);
			twinfo.setTimeStr(sdf.format(twinfo.getCreateTime()));
			map.put("twinfo", twinfo);
			caseid = twinfo.getCaseId();
			docid = twinfo.getDoctorId();
			break;
		case 2:
			BusinessTelOrder telinfo=wenzhenService.queryBusinessTelOrderById(oid);
			telinfo.setTimeStr(sdf.format(telinfo.getCreateTime()));
			map.put("telinfo", telinfo);
			caseid = telinfo.getCaseId();
			docid = telinfo.getDoctorId();
			// 电话
			break;
		case 4:
			// 远程门诊
			// 订单信息
			BusinessVedioOrder reminfo=commonService.queryBusinessVedioOrderById(oid);
			map.put("reminfo", reminfo);
			docid=reminfo.getExpertId();
			caseid=reminfo.getCaseId();
			break;
		case 5:
			//专家咨询订单
			SpecialAdviceOrder order=commonService.querySpecialAdviceOrderById(oid);
			map.put("adviceinfo", order);
			caseid=order.getCaseId();
			docid=order.getExpertId();//专家
			localdocid=order.getDoctorId();//医生
			break;
		}
		if (docid != null) {
			//专家信息
			MobileSpecial special = commonService
					.queryMobileSpecialByUserIdAndUserType(docid);
			map.put("special", special);
		}
		if(localdocid!=null){
			//医生信息
			MobileSpecial localdoc = commonService
					.queryMobileSpecialByUserIdAndUserType(docid);
			map.put("localdoc", localdoc);
		}
		if (caseid != null) {
			CaseInfo caseinfo = wenzhenService.queryCaseInfoById(caseid);
			// 病例图片
			List<UserCaseAttachment> images = wenzhenService.queryUserAttachmentByCaseUuid(caseinfo.getUuid());
			for (UserCaseAttachment userCaseAttachment : images) {
				if(StringUtils.isNotBlank(userCaseAttachment.getAttachmentIds())){
					List<CustomFileStorage> files=wenzhenService.queryCustomFilesByCaseIds(userCaseAttachment.getAttachmentIds());
					userCaseAttachment.setFiles(files);
				}
			}
			caseinfo.setAttachments(images);
			map.put("caseinfo", caseinfo);
		}
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		map.put("paramval", param);
		map.put("oid", oid);
		map.put("otype", type);//只有type为1：图文咨询才做回复
		return new ModelAndView("case/case_detail", map);
	}
	/**
	 * 进入病例分享
	 * 
	 * @param request
	 * @param type
	 *            类型 1图文，2电话，4远程
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "/case/{type}/{oid}", method = RequestMethod.GET)
	public ModelAndView sharecase(HttpServletRequest request,
			@PathVariable Integer type, @PathVariable Integer oid) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		Integer caseid = null;
		Integer docid = null;
		switch (type) {
		case 1:
			// 图文
			BusinessWenZhenInfo twinfo = wenzhenService
					.queryBusinessWenZhenInfoById(oid);
			twinfo.setTimeStr(sdf.format(twinfo.getStartTime()));
			map.put("twinfo", twinfo);
			caseid = twinfo.getCases_Id();
			docid = twinfo.getDoctorId();
			break;
		case 2:
			BusinessWenzhenTel telinfo = wenzhenService
					.queryBusinessWenZhenTelById(oid);
			telinfo.setTimeStr(sdf.format(telinfo.getCreateTime()));
			map.put("telinfo", telinfo);
			caseid = telinfo.getCaseId();
			docid = telinfo.getDoctorId();
			// 电话
			break;
		case 4:
			// 远程门诊
			// 门诊病历
			MedicalRecords record = commonService
					.queryMedicalRecordsByOrderId(oid);
			map.put("record", record);
			// 订单信息
			RemoteConsultation reminfo = weixinService
					.queryRemoteConsulationsById_detail(oid);
			map.put("reminfo", reminfo);
			docid=reminfo.getExpertId();
			break;
		}
		if (docid != null) {
			MobileSpecial special = commonService
					.queryMobileSpecialByUserIdAndUserType(docid);
			map.put("special", special);
		}
		if (caseid != null) {
			CaseInfo caseinfo = wenzhenService.queryCaseInfoById(caseid);
			// 病例图片
			List<UserCaseAttachment> images = wenzhenService.queryUserAttachmentByCaseUuid(caseinfo.getUuid());
			for (UserCaseAttachment userCaseAttachment : images) {
				if(StringUtils.isNotBlank(userCaseAttachment.getAttachmentIds())){
					List<CustomFileStorage> files=wenzhenService.queryCustomFilesByCaseIds(userCaseAttachment.getAttachmentIds());
					userCaseAttachment.setFiles(files);
				}
			}
			caseinfo.setAttachments(images);
			map.put("caseinfo", caseinfo);
		}
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		return new ModelAndView("case/case_detail", map);
	}

	/**
	 * 加载本地资源
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/remotenormals", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> remotenormals(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		RemoteConsultation order = weixinService
				.queryRemoteConsultationById(oid);
		List<CustomFileStorage> normals = new ArrayList<CustomFileStorage>();
		String normalImages = order.getNormalImages();
		CustomFileStorage cu = null;
		if (StringUtils.isNotBlank(normalImages)) {
			String[] _images = normalImages.split(",");
			if (_images != null && _images.length > 0) {
				for (String _image : _images) {
					cu = commonService.queryCustomFileStorage(Integer
							.parseInt(_image));
					if (cu != null) {
						String filename = cu.getFileUrl();
						cu.setFileType(filename.substring(filename
								.lastIndexOf(".") + 1));
						normals.add(cu);
					}
				}
			}
		}
		map.put("normals", normals);
		return map;
	}
	/**
	 * 登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> login(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String tel=request.getParameter("tel");
		String password=request.getParameter("password");//cvYl8U
		DoctorRegisterInfo reg=commonService.queryDoctorRegisterInfoByLoginName(tel);
		if(reg==null){
			map.put("status", "error");
			map.put("message", "手机号错误");
		}else{
			String salt="cvYl8U";
			if(StringUtils.isNotBlank(reg.getSalt())){
				salt=reg.getSalt();
			}
			if(PasswordUtil.MD5Salt(password, salt).equalsIgnoreCase(reg.getPassword())){
				map.put("status", "success");
				map.put("docid", reg.getId());
			}else{
				map.put("status", "error");
				map.put("message", "密码输入错误");
			}
		}
		return map;
	}
	
	/**
	 * 获取图文消息对话
	 * 访问：http://localhost:8080/share/gainmessages
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainmessages")
	public @ResponseBody Map<String,Object> gainmessages(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		BusinessTuwenOrder order=wenzhenService.queryBusinessTuwenInfoById(oid);
		List<BusinessMessageBean> messages = wenzhenService.queryBusinessMessageBeansByCon(oid, order.getUuid(),1);
		for (BusinessMessageBean _bean : messages) {
			_bean.setMsgTime(_sdf.format(_bean.getSendTime()));
		}
		map.put("messages", messages);
		return map;
	}
	
	private SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 回复图文消息
	 * /share/case/R2rirU37ea(param)---图文详情
	 * 访问：http://localhost:8080/share/replytoUser
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/replytoUser")
	@ResponseBody
	public Map<String,Object> replytoUser(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String param=request.getParameter("paramval");
		String content=request.getParameter("content");
		ShortUrlRelate surl=commonService.queryShortUrlRelate(param);
		Integer oid=surl.getOrderId();
		// 图文
		BusinessTuwenOrder twinfo=wenzhenService.queryBusinessTuwenInfoById(oid);
		BusinessMessageBean message = new BusinessMessageBean();
		Timestamp date=new Timestamp(System.currentTimeMillis());
		message.setOrderId(oid);;
		message.setOrderType(1);
		message.setMsgType("text");
		message.setMsgContent(content);
		message.setSendId(twinfo.getDoctorId());
		message.setSendType(2);
		message.setSendTime(date);
		message.setRecvId(twinfo.getUserId());
		message.setRecvType(1);
		message.setStatus(1);
		wenzhenService.saveBusinessMessageBean(message);
		message.setMsgTime(_sdf.format(message.getSendTime()));
		
		
		if (twinfo.getDocFirstAnswerTime() == null) {
			twinfo.setDocFirstAnswerTime(date);
		}
		twinfo.setDocLastAnswerTime(date);
		//专家发送消息数
		twinfo.setDocSendMsgCount(twinfo.getDocSendMsgCount()==null?1:(twinfo.getDocSendMsgCount()+1));
		//患者未读消息数
		twinfo.setPatUnreadMsgNum(twinfo.getPatUnreadMsgNum()==null?1:(twinfo.getPatUnreadMsgNum()+1));
		//wenzhenService.updateBusinessTuwenOrder(twinfo);
		/*MobileSpecial special=commonService.queryMobileSpecialByUserIdAndUserType(twinfo.getDoctorId());
		String _time=sdf.format(twinfo.getCreateTime());
		String sendcontent="您"+_time+"提交给"+special.getHosName()+""+special.getSpecialName()+special.getDuty()+"的图文问诊，专家已亲自回复，点击查看。";
		*/
		//患者先不推app端
		/*UserDevicesRecord u=wenzhenService.querySendMessageOrNot(twinfo.getUserId());
		if(u!=null){
			String _ret="";
			try {
				_ret = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
						"root/getui");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			IPushResult ret = GetuiPushUtils.PushMessage(_ret,
					u.getPushId(),"1;"+twinfo.getId().toString(),sendcontent, 1);
		}*/
		//回复微信
		/*UserWeixinRelative uwr=weixinService.queryUserWeiRelativeByUserId(twinfo.getUserId());
		if(uwr!=null){
			String openid=uwr.getOpenId();
			if(StringUtils.isNotBlank(openid)){
				String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
						+ weixinService.queryWeiAccessTokenById(1).getAccessToken();
				JSONObject jsonObject = CommonUtil.httpRequest(url, "POST",
						textJson(sendcontent,openid));
			}
		}
		//给客服发送消息
		HttpSendSmsUtil.sendSmsInteface("15001299884", sendcontent);*/
		
		map.put("message",message);
		return map;
	}
	
	private static String textJson(String content, String openid) {
		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put("content", content);
		JSONObject json = new JSONObject();
		json.put("touser", openid);
		json.put("msgtype", "text");
		json.put("text", jsonMsg);
		return json.toString();
	}
	private Map<String, Object> signMap(StringBuffer homeUrl, String queryString)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(queryString)) {
			homeUrl.append("?").append(queryString);
		}
		System.out.println(homeUrl.toString());
		long timestamp = System.currentTimeMillis() / 1000;
		System.out.println("===timestamp==" + timestamp);
		String nonceStr = UUID.randomUUID().toString();
		System.out.println("===nonceStr==" + nonceStr);
		WeiAccessToken wat=weixinService.queryWeiAccessTokenById(PropertiesUtil.getString("APPID"));
		String signature = SignUtil.getSignature(wat.getJsapiTicket(),
				nonceStr, timestamp, homeUrl.toString());
		map.put("appid", PropertiesUtil.getString("APPID"));
		map.put("timestamp", timestamp);
		map.put("nonceStr", nonceStr);
		map.put("signature", signature);
		return map;
	}
}
