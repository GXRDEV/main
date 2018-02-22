package com.tspeiz.modules.manage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.entity.BusinessOperativeInfo;
import com.tspeiz.modules.common.entity.ExpertConsultationSchedule;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.Dictionary;
import com.tspeiz.modules.common.entity.newrelease.UserAccountInfo;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;
import com.tspeiz.modules.common.entity.newrelease.UserDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.UserWeixinRelative;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;
import com.tspeiz.modules.common.service.ICaseService;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.util.DateUtil;
import com.tspeiz.modules.util.EmojiFilterUtil;
import com.tspeiz.modules.util.PasswordUtil;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.config.ReaderConfigUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.log.RecordLogUtil;
import com.tspeiz.modules.util.weixin.CommonUtil;
import com.tspeiz.modules.util.weixin.WeixinUtil;

/**
 * 微信公众号处理远程订单业务
 * @author heyongb
 *
 */
@Service
public class WxMainManager {
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private IWeixinService weixinService;
	@Autowired
	private ICaseService caseService;
	@Autowired
	private PayOrderManager payOrderManager;
	@Autowired
	private CommonManager commonManager;
	@Autowired
	private ICommonService commonService;
	/**
	 * 
	 * @param request
	 * @param response
	 * @param type  类型 远程门诊 1，远程会诊2
	 * @param userId
	 * @param money
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> processRemoteOrder(HttpServletRequest request,HttpServletResponse response,Integer type,Integer userId,Float money) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		//1.处理病例
		Integer caseid=caseService.saveOrUpdateCase(request, userId,1);
		//2.生成订单
		Integer orderid= generateRemoteOrder(request,caseid,userId,type);
		//3.生成支付单
		String body="";
		if(type==1){
			body=ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/remotebody");
		}else if(type==2){
			body=ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/remotesubbody");
		}
		Map<String, Object> retMap = WeixinUtil.weipay(request, response,
				money, PropertiesUtil.getString("APPID"), PropertiesUtil
						.getString("APPSECRET"), PropertiesUtil
						.getString("PARTNER"), PropertiesUtil
						.getString("PARTNERKEY"),body
				,
				request.getParameter("openid"),
				PropertiesUtil.getString("DOMAINURL") + "wzjh/remotenotify",
				null, null, null);
		Integer pid=payOrderManager.savePayInfo(4, orderid, retMap.get("out_trade_no").toString(), money, 2, money, 0.0f, 0.0f, 0.0f,null);
		//4.记录日志
		try {
			RecordLogUtil.insert("4", "远程门诊", orderid+"", pid+"",
					(String) retMap.get("paramxml"),
					(String) retMap.get("prepayxml"), "",retMap.get("out_trade_no").toString());
		} catch (Exception e) {

		}
		map.putAll(retMap);
		return map;
	}
	
	private Integer generateRemoteOrder(HttpServletRequest request,Integer caseId,Integer userId,Integer type) {
		BusinessVedioOrder rc=new BusinessVedioOrder();
		CaseInfo caseInfo = commonService.queryCaseInfoById(caseId);
		String openid = request.getParameter("openid");
		rc.setCreateTime(new Timestamp(System.currentTimeMillis()));
		rc.setOpenId(openid);
		rc.setConsultationDur(OrderStatusEnum.VEDIO_DURATION.getKey());
		rc.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
		rc.setLocalPlusNo("123");
		rc.setSource(1);
		rc.setPayStatus(4);
		rc.setUserId(userId);
		rc.setDelFlag(0);
		rc.setCaseId(caseId);
		rc.setCaseUuid(caseInfo.getUuid());
		rc.setOrderMode(2);
		boolean selfcon=false;
		if(type==1){
			String stimeid = request.getParameter("stimeid");
			if (StringUtils.isNotBlank(stimeid)) {
				ExpertConsultationSchedule ecs = weixinService
						.queryExpertConScheduleById(Integer.parseInt(stimeid));
				rc.setConsultationDate(ecs.getScheduleDate());
				rc.setConsultationTime(ecs.getStartTime());
			}
			String condate = request.getParameter("condate");
			String contime = request.getParameter("contime");
			if (StringUtils.isNotBlank(condate) && StringUtils.isNotBlank(contime)) {
				rc.setConsultationDate(condate);
				rc.setConsultationTime(contime);
			}
			rc.setExpertId(Integer.parseInt(request.getParameter("expertId")));
			rc.setLocalHospitalId(Integer.parseInt(request
					.getParameter("localHosId")));
			rc.setLocalDepartId(Integer.parseInt(request
					.getParameter("localDepartId")));
		}else if(type==2){
			String hospitalId = request.getParameter("hosid");
			String depid = request.getParameter("depid");
			String levelid = request.getParameter("levelid");
			String timeid = request.getParameter("timeid");
			if(!levelid.equalsIgnoreCase("-1")){
				Dictionary level = weixinService.queryDictionaryById(Integer
						.parseInt(levelid));
				rc.setExLevel(level.getDisplayName());
			}else{
				selfcon=true;
				rc.setExLevel("自定义级别");
				
			}
			Dictionary time = weixinService.queryDictionaryById(Integer
					.parseInt(timeid));
			rc.setLocalHospitalId(Integer.parseInt(hospitalId));
			rc.setLocalDepartId(Integer.parseInt(depid));
			rc.setConsultationDate(time.getDisplayName());
			rc.setConsultationDur(OrderStatusEnum.VEDIO_DURATION.getKey());
			rc.setVedioSubType(1);// 远程会诊;
			
		}
		rc.setUuid(commonManager.generateUUID(4));
		rc.setConsultationDur(20);
		Integer oid = weixinService.saveBusinessVedioOrder(rc);
		if(selfcon){
			//记录运营人员信息
			recordopinfo(request,oid);
		}
		processContact(type,openid,request,userId);
		//记录消息
		commonManager.saveBusinessMessageInfo_sys(oid, 4, "text", "创建订单",null,null);
		return oid;
	}
	
	private void recordopinfo(HttpServletRequest request,Integer oid){
		BusinessOperativeInfo op=new BusinessOperativeInfo();
		op.setCreateTime(sdf.format(new Date()));
		op.setOpMoney(request.getParameter("opmoney"));
		op.setOpName(request.getParameter("opname"));
		op.setOpTel(request.getParameter("optel"));
		op.setOrderId(oid);
		weixinService.saveBusinessOperativeInfo(op);
	}
	private void processContact(Integer type,String openid,HttpServletRequest request,Integer userId){
		if(type==1){
			boolean exist = weixinService.isExistUserContactInfo(openid, request.getParameter("username"),
					request.getParameter("idcard"), request.getParameter("telphone"));
			if (!exist) {
				UserWeixinRelative uw = weixinService
						.queryUserWeiRelativeByOpenId(openid);
				UserContactInfo ci = new UserContactInfo();
				ci.setIdCard(request.getParameter("idcard"));
				ci.setOpenId(openid);
				ci.setTelphone(request.getParameter("telphone"));
				ci.setContactName(request.getParameter("username"));
				ci.setCreateTime(new Timestamp(System.currentTimeMillis()));
				ci.setStatus(1);
				if (uw != null)
					ci.setUserId(uw.getUserId());
				weixinService.saveUserContactInfo(ci);
			}
		}else if(type==2){
			UserContactInfo contact = weixinService.queryUserContactorByCondition(
					userId, request.getParameter("contactName"), request.getParameter("telephone"));
			if (contact == null) {
				contact = new UserContactInfo();
				contact.setContactName(request.getParameter("contactName"));
				contact.setUserId(userId);
				contact.setTelphone(request.getParameter("telephone"));
				contact.setSex(StringUtils.isNotBlank(request.getParameter("sex")) ? Integer.parseInt(request.getParameter("sex"))
						: null);
				contact.setIdCard(request.getParameter("idcard"));
				contact.setCreateTime(new Timestamp(System.currentTimeMillis()));
				contact.setStatus(1);
				weixinService.saveUserContactInfo(contact);
			}
		}
	}
	
	public Map<String,Object> processWxRemoteOrder(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		String openid=request.getParameter("openid");
		UserWeixinRelative uwr=weixinService.queryUserWeiRelativeByOpenId(openid);
		if(uwr!=null){
			Integer caseid=caseService.saveOrUpdateCase(request, uwr.getUserId(), 1);
			Integer oid=processnewremoteorder(request,caseid,uwr.getUserId(),openid);
			String body="";
			body=ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/remotesubbody");
			Float money=null;
			if(StringUtils.isNotBlank(request.getParameter("expertid"))){
				//选择了专家
				money= commonManager.gainMoneyByOrder(4, Integer.parseInt(request.getParameter("expertid"))).floatValue();
			}
			if(StringUtils.isNotBlank(request.getParameter("levelid"))){
				//选择了级别
				Dictionary level = weixinService.queryDictionaryById(Integer
						.parseInt(request.getParameter("levelid")));
				money=Float.valueOf(level.getDisplayValue());
			}
			
			
			
			Map<String, Object> retMap = WeixinUtil.weipay(request, response,
					money, PropertiesUtil.getString("APPID"), PropertiesUtil
							.getString("APPSECRET"), PropertiesUtil
							.getString("PARTNER"), PropertiesUtil
							.getString("PARTNERKEY"),body
					,
					request.getParameter("openid"),
					PropertiesUtil.getString("DOMAINURL") + "wzjh/remotenotify",
					null, null, null);
			Integer pid=payOrderManager.savePayInfo(4, oid, retMap.get("out_trade_no").toString(), money, 2, money, 0.0f, 0.0f, 0.0f,null);
			//4.记录日志
			try {
				RecordLogUtil.insert("4", "远程门诊", oid+"", pid+"",
						(String) retMap.get("paramxml"),
						(String) retMap.get("prepayxml"), "",retMap.get("out_trade_no").toString());
			} catch (Exception e) {

			}
			map.putAll(retMap);
		}
		return map;
	}
	private Integer processnewremoteorder(HttpServletRequest request,Integer caseid,Integer userid,String openid){
		BusinessVedioOrder order=new BusinessVedioOrder();
		CaseInfo caseInfo = commonService.queryCaseInfoById(caseid);
		UUID uuid = UUID.randomUUID();
		order.setUuid(uuid.toString().replace("-", ""));
		order.setCreateTime(new Timestamp(System.currentTimeMillis()));
		String timeid=request.getParameter("timeid");
		if(StringUtils.isNotBlank(timeid)){
			Dictionary time = weixinService.queryDictionaryById(Integer
					.parseInt(timeid));
			order.setConsultationDate(time.getDisplayName());//期待会诊日期 几天内
		}
		order.setCaseId(caseid);
		order.setCaseUuid(caseInfo.getUuid());
		order.setUserId(userid);
		//设置专家
		if(StringUtils.isNotBlank(request.getParameter("expertid"))){
			//选择了专家
			order.setExpertId(Integer.parseInt(request.getParameter("expertid")));
		}
		order.setLocalHospitalId(Integer.parseInt(request.getParameter("hosid")));
		order.setLocalDepartId(Integer.parseInt(request.getParameter("departid")));
		order.setOrderMode(2);
		order.setSource(1);
		order.setOpenId(openid);
		order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
		order.setPayStatus(4);
		order.setDelFlag(0);
		order.setConsultationDur(OrderStatusEnum.VEDIO_DURATION.getKey());
		//设置级别
		if(StringUtils.isNotBlank(request.getParameter("levelid"))){
			
			//选择了级别
			Dictionary level = weixinService.queryDictionaryById(Integer
					.parseInt(request.getParameter("levelid")));
			order.setExLevel(level.getDisplayName());
			order.setVedioSubType(1);
		}
		order.setConsultationDur(20);
		Integer oid=weixinService.saveBusinessVedioOrder(order);
		return oid;
	}
	
	public void modifyinfo(String openid,HttpServletRequest request){
		UserWeixinRelative uwr=weixinService.queryUserWeiRelativeByOpenId(openid);
		//修改头像
		if(StringUtils.isNotBlank(request.getParameter("headimgurl"))){
			uwr.setHeadImageUrl(request.getParameter("headimgurl"));
		}
		//修改性别
		if(StringUtils.isNotBlank(request.getParameter("sex"))){
			uwr.setSex(Integer.parseInt(request.getParameter("sex")));
		}
		//修改年龄
		if(StringUtils.isNotBlank(request.getParameter("age"))){
			uwr.setAge(Integer.parseInt(request.getParameter("age")));
		}
		//修改密码
		if(StringUtils.isNotBlank(request.getParameter("newpassword"))){
			UserAccountInfo user=weixinService.queryUserAccountInfoById(uwr.getUserId());
			user.setSalt("cvYl8U");
			user.setPasswordHashed(PasswordUtil.MD5Salt(request.getParameter("newpassword")));
			weixinService.updateUserAccountInfo(user);
		}
		if(StringUtils.isNotBlank(request.getParameter("username"))){
			uwr.setDisplayName(request.getParameter("username"));
		}
		weixinService.updateUserWeixinRelative(uwr);
		//user_detail_info
		processUserDetailInfo(uwr,request);
	}
	private void processUserDetailInfo(UserWeixinRelative uw,HttpServletRequest request){
		UserDetailInfo ud=weixinService.queryUserDetailInfoById(uw.getUserId());
		UserAccountInfo ua=weixinService.queryUserAccountInfoById(uw.getUserId());
		if(ud==null){
			ud=new UserDetailInfo();
			ud.setId(uw.getUserId());
			ud.setDisplayName(uw.getDisplayName());
			ud.setSex(uw.getSex());
			ud.setBirthDay(uw.getAge()+"");
			ud.setTelephone(ua.getMobileNumber());
			ud.setHeadImageUrl(uw.getHeadImageUrl());
			weixinService.saveUserDetailInfo(ud);
		}else{
			ud.setHeadImageUrl(uw.getHeadImageUrl());
			ud.setSex(uw.getSex());
			ud.setBirthDay(uw.getAge()+"");
			ud.setDisplayName(uw.getDisplayName());
			weixinService.updateUserDetailInfo(ud);
		}
	}
	private SimpleDateFormat code_time = new SimpleDateFormat("yyyyMMddHHmmss");
	public Map<String,Object> gainVeryCode(String ltype,String openid,String code,String telphone,HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String content="";
		boolean send=false;
		if(StringUtils.isNotBlank(ltype)&&ltype.equalsIgnoreCase("pass")){
			//修改密码 获取验证码 需要验证输入的手机号是否正确
			content="验证码为"+code+"，佰医汇提醒您，您正在使用\"佰医汇\"微信公号，请勿泄露给第三人！【佰医汇】";
			UserWeixinRelative uwr=weixinService.queryUserWeiRelativeByOpenId(openid);
			UserAccountInfo user=weixinService.queryUserAccountInfoById(uwr.getUserId());
			if(telphone.equalsIgnoreCase(user.getLoginName())){
				//手机号输入正确
				send=true;
			}
		}else{
			 content= " 验证码为" + code + "，佰医汇提醒您，您正在绑定微信公众平台，请勿泄露给第三人！【佰医汇】";
			 send=true;
		}
		if(send){
			String ret = HttpSendSmsUtil.sendSmsInteface(telphone, content);
			if (ret.equalsIgnoreCase("100")) {
				map.put("status", "success");
				request.getSession().setAttribute("bindCode", telphone + "" + code);
				request.getSession().setAttribute("verifyCodeTime",
						code_time.format(new Date()));
			} else {
				map.put("status", "error");
			}
		}else{
			map.put("status", "error");
		}
		return map;
	}
	
	public Map<String,Object> tellCodeNew(HttpServletRequest request,String tel,String code){
		Map<String,Object> map=new HashMap<String,Object>();
		String bindCode = (String) request.getSession()
				.getAttribute("bindCode");
		String localVerifyCodeTime = (String) request.getSession()
				.getAttribute("verifyCodeTime");
		if (StringUtils.isNotBlank(bindCode)
				&& StringUtils.isNotBlank(localVerifyCodeTime)) {
			String now = code_time.format(new Date());
			long second = DateUtil.calculartorDateSecond(localVerifyCodeTime,
					now);
			if (second > 300) {
				// 超过五分钟,失效
				map.put("status", "error");
			} else {
				if ((tel + "" + code).equalsIgnoreCase(bindCode)) {
					//验证码输入正确  判断手机号是否注册过，如果没注册过需要设置密码
					map.put("status", "success");
					UserAccountInfo user=weixinService.queryUserAccountInfoByMobilePhone(tel);
					if(user!=null){
						map.put("reg", "true");//已注册过
					}else{
						map.put("reg", "false");//未注册过
					}
					map.put("tel", tel);
				} else {
					map.put("status", "error");
				}
			}
		} else {
			map.put("status", "error");
		}
		return map;
	}
	
	public void suretobind(HttpServletRequest request,String tel,String password,String openid) throws Exception{
		Integer uid=null;
		if(StringUtils.isNotBlank(password)){
			//设置密码--未注册过的绑定
			UserAccountInfo user=new UserAccountInfo();
			user.setPasswordHashed(PasswordUtil.MD5Salt(password));
			user.setSalt("cvYl8U");
			user.setLoginName(tel);
			user.setMobileNumber(tel);
			user.setRegisterTime(new Date());
			user.setOrigin("weixin");
			user.setStatus("1");
			user.setUuid(UUID.randomUUID().toString().replace("-", ""));
			uid = weixinService.saveUserAccountInfo(user);
		}else{
			//不需要设置密码--已注册过的绑定
			UserAccountInfo _user=weixinService.queryUserAccountInfoByMobilePhone(tel);
			uid=_user.getId();
		}
		UserWeixinRelative ue = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		if(ue==null)ue=new UserWeixinRelative();
		ue.setUserId(uid);
		ue.setOpenId(openid);
		dataSet(ue,openid,1);
		//user_detail_info数据
		userDetailSet(ue,uid,tel);
		processUserContactInfo(uid,tel);
		//发送短信
		String bindtel=ReaderConfigUtil.gainConfigVal(request, "basic.xml",
				"root/bindtel");
		if(StringUtils.isNotBlank(bindtel)){
			HttpSendSmsUtil.sendSmsInteface(bindtel, "账号"+tel+"的用户("+ue.getDisplayName()+")在佰医汇的微信公号端进行了用户注册绑定【佰医汇】");
		}
	}
	private void processUserContactInfo(Integer userid,String tel){
		List<UserContactInfo> cinfos=weixinService.queryUserContactInfosByUserId(userid);
		if(cinfos!=null&&cinfos.size()>0){
		}else{
			//新建usercontactinfo
			UserDetailInfo detail=weixinService.queryUserDetailInfoById(userid);
			UserContactInfo uc=new UserContactInfo();
			uc.setUuid(UUID.randomUUID().toString().replace("-", ""));
			uc.setUserId(userid);
			uc.setContactName(detail.getDisplayName());
			uc.setTelphone(detail.getTelephone());
			uc.setSex(detail.getSex());
			uc.setCreateTime(new Timestamp(System.currentTimeMillis()));
			uc.setStatus(1);
			uc.setIsMasterContact(1);
			uc.setIsDefault(1);
			weixinService.saveUserContactInfo(uc);
			
		}
	}
	private void userDetailSet(UserWeixinRelative uw,Integer uid,String tel){
		UserDetailInfo ud=weixinService.queryUserDetailInfoById(uid);
		if(ud==null){
			ud=new UserDetailInfo();
			ud.setId(uid);
			ud.setDisplayName(uw.getDisplayName());
			ud.setSex(uw.getSex());
			ud.setBirthDay(uw.getAge()+"");
			ud.setTelephone(tel);
			ud.setHeadImageUrl(uw.getHeadImageUrl());
			weixinService.saveUserDetailInfo(ud);
		}
	}
	public Map<String,Object> gainpersoninfo(String openid){
		Map<String,Object> map=new HashMap<String,Object>();
		UserWeixinRelative uwr=weixinService.queryUserWeiRelativeByOpenId(openid);
		if(uwr==null){
			uwr=new UserWeixinRelative();
			uwr.setOpenId(openid);
		}
		dataSet(uwr, openid, 2);
		map.put("uwr", uwr);
		if(uwr.getUserId()!=null){
			UserAccountInfo user=weixinService.queryUserAccountInfoById(uwr.getUserId());
			map.put("uinfo", user);
		}
		return map;
	}
	
	private void dataSet(UserWeixinRelative uwr,String openid,Integer type){
		if(!StringUtils.isNotBlank(uwr.getHeadImageUrl())){
			WeiAccessToken wat=weixinService.queryWeiAccessTokenById(PropertiesUtil.getString("APPID"));
			JSONObject obj=CommonUtil.GetPersonInfo(wat.getAccessToken(), openid);
			if(obj!=null&&obj.containsKey("headimgurl")){
				uwr.setHeadImageUrl(obj.getString("headimgurl"));
				uwr.setAppId(PropertiesUtil.getString("APPID"));
				uwr.setDisplayName(EmojiFilterUtil.filterEmoji(obj.getString("nickname")));
				if(obj.getString("sex").equalsIgnoreCase("2")){
					uwr.setSex(0);
				}else{
					uwr.setSex(1);
				}
				if(type.equals(1)){
					if(uwr.getId()==null){
						weixinService.saveUserWeixinRelative(uwr);
					}else{
						weixinService.updateUserWeixinRelative(uwr);
					}
				}
			}
		}
	}
}
