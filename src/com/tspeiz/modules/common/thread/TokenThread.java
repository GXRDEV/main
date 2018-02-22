package com.tspeiz.modules.common.thread;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.tspeiz.modules.common.bean.weixin.AccessToken;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.util.SpringUtil;
import com.tspeiz.modules.util.weixin.CommonUtil;

public class TokenThread implements Runnable {
	Logger log = Logger.getLogger(TokenThread.class);
	// 第三方用户唯一凭证
	public static String appid = "";
	// 第三方用户唯一凭证密钥
	public static String appsecret = "";
	public static AccessToken accessToken = null;
	public static String jsapi_ticket = null;
	private static IWeixinService weixinService;
	static {
		ApplicationContext ac = SpringUtil.getApplicationContext();
		weixinService = (IWeixinService) ac.getBean("weixinService");
	}
	public void run() {
		while (true) {
			try {
				accessToken = CommonUtil.getAccessToken(appid, appsecret);
				jsapi_ticket = CommonUtil
						.getJsApiTicket(accessToken.getToken());
				if (null != accessToken) {
					WeiAccessToken wat=weixinService.queryWeiAccessTokenById(appid);
                	wat.setAccessToken(accessToken.getToken());
                	weixinService.updateWeiAccessToken(wat);
					Thread.sleep((accessToken.getExpiresIn() - 200) * 1000);
				} else {
					// 如果access_token为null，60秒后再获取
					Thread.sleep(30 * 1000);
				}
			} catch (InterruptedException e) {
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e1) {

				}
			}
		}
	}
}