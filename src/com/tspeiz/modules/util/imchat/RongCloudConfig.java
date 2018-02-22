package com.tspeiz.modules.util.imchat;

import com.tspeiz.modules.util.PropertiesUtil;


public class RongCloudConfig {

	/**
	 * App Key(MyApplication)
	 */
	public static String APPKEY = PropertiesUtil.getString("rongcloud_appkey").trim();

	/**
	 * App Secret(MyApplication)
	 */
	public static String APPSECRET = PropertiesUtil.getString("rongcloud_appsecret").trim();

}
