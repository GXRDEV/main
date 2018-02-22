package com.tspeiz.modules.util.getui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.tspeiz.modules.common.bean.PushCodeEnum;
import com.tspeiz.modules.common.bean.PushTemplateBean;
import com.tspeiz.modules.common.entity.SystemPushInfo;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.UUIDUtil;




/**
 * 个推消息服务类
 * 
 * @author liqi
 * 
 */
public class GetuiPushUtils {
	
	private static Logger log = Logger.getLogger(GetuiPushUtils.class);

	private static String Host = "http://sdk.open.api.igexin.com/apiex.htm";
	private static Map<Integer,GetuiConfigure> getuiMap = null;
	
	static {
		try {
			String type = PropertiesUtil.getString("getui");
			init(type);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage().toString());
	
		}
	}

	/**
	 * 发送个推消息
	 * 
	 * @param clientId
	 *            个推pushId
	 * 
	 * @param appType
	 *            推送App类型
	 * @param pushInfo
	 *            推送内容
	 * @return
	 */
	public static IPushResult PushMessage(String clientId, Integer appType, SystemPushInfo pushInfo) {
		if (null == pushInfo) {
			return null;
		}
		
		GetuiConfigure configure  = getuiMap.get(appType);
		if(null == configure ){
			return null;
		}

		// 设置推送内容
		PushTemplateBean bean = new PushTemplateBean();
		bean.setPushKey(pushInfo.getPushKey());
		bean.setPushCode(pushInfo.getPushCode());
		bean.setBusinessKey(pushInfo.getBusinessKey());
		bean.setBusinessType(pushInfo.getBusinessType());
		bean.setContent(pushInfo.getContent());
		bean.setBusinessExtend(pushInfo.getBusinessExtend());
		bean.setCreateTimeExtend(pushInfo.getCreateTimeExtend());
		
		IGtPush push = new IGtPush(Host, configure.getAppKey(), configure.getMasterSecret());
		
		SingleMessage message = new SingleMessage();
		message.setOffline(true); // 用户当前不在线时，是否离线存储,可选
		message.setOfflineExpireTime(12 * 3600 * 1000); // 离线有效时间，单位为毫秒，可选
		message.setData(getTemplate(configure.getAppId(), configure.getAppKey(), JSONObject.fromObject(bean).toString(), pushInfo.getContent(),pushInfo.getBadge()));// 设置消息模版
		message.setPushNetWorkType(0); // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
		Target target = new Target();
		target.setAppId(configure.getAppId());// 设置客户端所属应用唯一ID
		target.setClientId(clientId);// 设置客户端身份ID，pushId
		target.setAlias("WenZhenId");// 设置客户端所属用户别名，可选

		IPushResult ret = null;
		try {
			ret = push.pushMessageToSingle(message, target);
		} catch (RequestException e) {
			e.printStackTrace();
			log.debug(e.getMessage().toString());
			ret = push.pushMessageToSingle(message, target, e.getRequestId());
		} finally{
			if (null != ret) {
				System.out.println(ret.getResponse().toString());
				log.info("========PushMessage:" + ret.getResponse().toString());
			} else {
				System.out.println("服务器响应异常");
			}
		}
		
		return ret;
	}

	// 初始化推送参数
	private static void init(String  type) {
		getuiMap = new HashMap<Integer,GetuiConfigure>();
		if(type.equalsIgnoreCase("offline")){
			
			GetuiConfigure doctorGetui = new GetuiConfigure("nIfL8OksObAvSByD9gBxx8","ATVuyI3weMATka13kgwzW6","HqmL63s9Qr9iu7S7jKy4o7");
			getuiMap.put(3, doctorGetui);
			
			GetuiConfigure expertGetui = new GetuiConfigure("umdLrPfcCo7iExQh3jaCV2","e4dP4S8tKR8aqWSF0BNOI4","8Y1ZYKrjTU66KhOxUI0nCA");
			getuiMap.put(2, expertGetui);
			
			GetuiConfigure patientGetui = new GetuiConfigure("TnnU7GAkm79fr0tTcZ1djA","MPfkkRfksp8OUF00cin0F5","EATSB9MIvIAG400l6CItK7");
			getuiMap.put(1, patientGetui);
			
		} else {
			
			GetuiConfigure doctorGetui = new GetuiConfigure("xFFVMJBuYW9jUxJQajwNm8","9fOBksJw6RA7yB9m9IXc21","nq4oib9yMy64usVQtnHgv5");
			getuiMap.put(3, doctorGetui);
			
			GetuiConfigure expertGetui = new GetuiConfigure("Le7uw5mib56VduRNFQnwT4","YmZeHuRbZpANApAzHV9eB","bjkQ59PYdL7AJlEZKTcBS5");
			getuiMap.put(2, expertGetui);
			
			GetuiConfigure patientGetui = new GetuiConfigure("Mx3S6xQ6vc9y0JZoTADIF6","N462txNsyQ6FDgnRqt40l1","x7bwNcCQ4S6VcSefpYTgf1");
			getuiMap.put(1, patientGetui);
		}
	}

	// 设置透传消息模版
	@SuppressWarnings("deprecation")
	private static TransmissionTemplate getTemplate(String appId, String appKey, String obj, String alertMsg,Integer badge) {

		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);// 设置APPID与APPKEY
		template.setAppkey(appKey);// 用于鉴定身份是否合法
		template.setTransmissionContent(obj);// 透传内容，不支持转义字符
		template.setTransmissionType(2);// 应用启动类型，1：强制应用启动 2：等待应用启动

		APNPayload payload = new APNPayload();
		payload.setBadge(badge); // 应用icon上显示的数字
		payload.setContentAvailable(1); // 推送直接带有透传数据
		payload.setSound("default");// 通知铃声文件名
		payload.setCategory("$由客户端定义");// 在客户端通知栏触发特定的action和button显示
		payload.setAlertMsg(new APNPayload.SimpleAlertMsg(alertMsg));// 通知消息体
		payload.addCustomMsg("info", obj);// 增加自定义的数据，透传内容

		template.setAPNInfo(payload);// iOS推送使用该字段

		return template;
	}

	public static void main(String[] args) throws Exception {
		SystemPushInfo pushInfo = new SystemPushInfo();
		pushInfo.setPushKey(UUIDUtil.getUUID());
		pushInfo.setPushCode(PushCodeEnum.EditAccountDetail.getCode());
		pushInfo.setContent("您明天（2016-12-02 14:30） 有一个来自【一个名字非常长非常长非常长非常长非常长非常长的名字非常长非常长非常长非常长非常长非常长的】医院会诊订单，请提前查看订单详情及患者病历。");

		PushMessage("9bb6ee57210aebabeeadbe4a6d9e9a0c", 3, pushInfo);
	}

	@SuppressWarnings("unused")
	private static void testLinkTemplatePush() {
		GetuiConfigure configure  = getuiMap.get(2);
		
		IGtPush push = new IGtPush(Host, configure.getAppKey(), configure.getMasterSecret());

		// 定义"点击链接打开通知模板"，并设置标题、内容、链接
		LinkTemplate template = new LinkTemplate();
		template.setAppId(configure.getAppId());
		template.setAppkey(configure.getAppKey());
		template.setTitle("欢迎使用个推!");// 通知栏标题
		template.setText("这是一条推送消息~");// 通知栏内容
		template.setUrl("http://getui.com");

		List<String> appIds = new ArrayList<String>();
		appIds.add(configure.getAppId());

		// 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
		AppMessage message = new AppMessage();
		message.setData(template);
		message.setAppIdList(appIds);
		message.setOffline(true);
		message.setOfflineExpireTime(1000 * 600);

		IPushResult ret = push.pushMessageToApp(message);
		System.out.println(ret.getResponse().toString());
	}

}
