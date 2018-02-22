package com.tspeiz.modules.util.common;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

/**
 * 个推消息服务类
 * 
 * @author liqi
 * 
 */
public class GetuiPushUtils {

	private static String Host = "http://sdk.open.api.igexin.com/apiex.htm";
	private static String AppId; // 设定接收的应用
	private static String AppKey; // 用于鉴定身份是否合法
	private static String MasterSecret; // 第三方客户端个推集成鉴权码，用于验证第三方合法性。在客户端集成SDK时需要提供

	public static IPushResult PushMessage(String _type,String clientId, String obj,String alertMessage,
			Integer type) {
		// 初始化参数
		init(_type,type);

		IGtPush push = new IGtPush(Host, AppKey, MasterSecret);

		SingleMessage message = new SingleMessage();
		message.setOffline(true); // 用户当前不在线时，是否离线存储,可选
		message.setOfflineExpireTime(12 * 3600 * 1000); // 离线有效时间，单位为毫秒，可选
		message.setData(getTemplate(AppId, AppKey, obj,alertMessage));// 设置消息模版
		message.setPushNetWorkType(0); // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发

		Target target = new Target();
		target.setAppId(AppId);
		target.setClientId(clientId);
		target.setAlias("WenZhenId");

		IPushResult ret = null;
		try {
			ret = push.pushMessageToSingle(message, target);
		} catch (RequestException e) {
			e.printStackTrace();
			ret = push.pushMessageToSingle(message, target, e.getRequestId());
		}
		if (ret != null) {
			System.out.println(ret.getResponse().toString());
		} else {
			System.out.println("服务器响应异常");
		}

		return ret;
	}

	private static void init(String _type,Integer type) {
		switch (type) {
		case 1: // 患者App
			if(_type.equalsIgnoreCase("offline")){
				AppId = "TnnU7GAkm79fr0tTcZ1djA";
				AppKey = "MPfkkRfksp8OUF00cin0F5";
				MasterSecret = "EATSB9MIvIAG400l6CItK7";
			}else if(_type.equalsIgnoreCase("")){
				AppId = "Mx3S6xQ6vc9y0JZoTADIF6";
				AppKey = "N462txNsyQ6FDgnRqt40l1";
				MasterSecret = "x7bwNcCQ4S6VcSefpYTgf1";
			}
			break;
		case 2: // 专家App
			AppId = "Le7uw5mib56VduRNFQnwT4";
			AppKey = "YmZeHuRbZpANApAzHV9eB";
			MasterSecret = "bjkQ59PYdL7AJlEZKTcBS5";
			break;
		case 3: // 合作医生App
			AppId = "xFFVMJBuYW9jUxJQajwNm8";
			AppKey = "9fOBksJw6RA7yB9m9IXc21";
			MasterSecret = "nq4oib9yMy64usVQtnHgv5";
			break;
		}
	}


	@SuppressWarnings("deprecation")
	private static TransmissionTemplate getTemplate(String appId,
			String appKey, String obj,String alertMessage) {

		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);// 设置APPID与APPKEY
		template.setAppkey(appKey);
		template.setTransmissionContent(obj);// 透传内容
		template.setTransmissionType(2);// 应用启动类型，1：强制应用启动 2：等待应用启动

		APNPayload payload = new APNPayload();
		payload.setBadge(1); // 应用icon上显示的数字
		payload.setContentAvailable(1); // 推送直接带有透传数据
		payload.setSound("default");// 通知铃声文件名
		payload.setCategory("$由客户端定义");// 在客户端通知栏触发特定的action和button显示
		payload.setAlertMsg(new APNPayload.SimpleAlertMsg(alertMessage));// 通知消息体
		payload.addCustomMsg("info", obj);// 增加自定义的数据

		template.setAPNInfo(payload);

		return template;
	}

	@SuppressWarnings("unused")
	private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg() {
		APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
		alertMsg.setBody("body");
		alertMsg.setActionLocKey("ActionLockey");
		alertMsg.setLocKey("LocKey");
		alertMsg.addLocArg("loc-args");
		alertMsg.setLaunchImage("launch-image");
		// IOS8.2以上版本支持
		alertMsg.setTitle("Title");
		alertMsg.setTitleLocKey("TitleLocKey");
		alertMsg.addTitleLocArg("TitleLocArg");
		return alertMsg;
	}

	@SuppressWarnings("unused")
	private static void apnpush() throws Exception {
		IGtPush push = new IGtPush(Host, AppKey, MasterSecret);
		APNTemplate t = new APNTemplate();
		APNPayload apnpayload = new APNPayload();
		apnpayload.setSound("");
		// apn高级推送
		APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
		// //通知文本消息标题
		alertMsg.setTitle("aaaaaa");
		// 通知文本消息字符串
		alertMsg.setBody("bbbb");
		// 对于标题指定执行按钮所使用的Localizable.strings,仅支持IOS8.2以上版本
		alertMsg.setTitleLocKey("ccccc");
		// 指定执行按钮所使用的Localizable.strings
		alertMsg.setActionLocKey("ddddd");
		apnpayload.setAlertMsg(alertMsg);

		t.setAPNInfo(apnpayload);
		SingleMessage sm = new SingleMessage();
		sm.setData(t);

		String devicetoken = "3c88e484d04f5b41d27aa5b7803d2722e922adb7a8f8a9ea5462ac5f9ee258c4";// iOS设备唯一标识，由苹果那边生成
		IPushResult ret0 = push.pushAPNMessageToSingle(AppId, devicetoken, sm);
		System.out.println(ret0.getResponse());

	}
}
