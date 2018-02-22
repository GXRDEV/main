package com.tspeiz.modules.util.imchat;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.tspeiz.modules.util.imchat.models.TokenReslut;

public class ImChatUtil {
	private static String APP_KEY = "kj7swf8o7wvd2";
	private static String APP_SECRET = "8RKzZU0OgyY43";
	/**
	 * 测试获取token
	 * @param args
	 *12912303  obk8zyduNcRkcb5l8x4A+vhkNClRoMZzL7e5vDuONFXVA8hMpyqAeF/MSDQ2HPZ94+/fo2tZgWlDCQf0yjq4EQ==
	 *12812087  pEOmRhAlFwHj9aRnenyO71KEZZnSaCuOMXp+TfQpOaK1bGC/OemXljiewkjwhkhZOoAp/T6BPr/n/p5oTY+FStuOv3OeIGdK
	 * 
	 */
	public static void main(String[] args) throws Exception{
		System.out.println(getToken("12812087","小黄","https://www.baidu.com/img/bd_logo1.png","1").toString());
	}
	/**
	 * 获取token
	 * 
	 * @param userId
	 * @param name
	 * @param portraitUri
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> getToken(String userId, String name, String portraitUri,String mode)
			throws Exception {
		Map<String,String> map=new HashMap<String,String>();
		if (userId == null) {
			throw new IllegalArgumentException("Paramer 'userId' is required");
		}
		if (name == null) {
			throw new IllegalArgumentException("Paramer 'name' is required");
		}
		if (portraitUri == null) {
			throw new IllegalArgumentException(
					"Paramer 'portraitUri' is required");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("&userId=").append(
				URLEncoder.encode(userId.toString(), "UTF8"));
		sb.append("&name=").append(URLEncoder.encode(name.toString(), "UTF8"));
		sb.append("&portraitUri=").append(
				URLEncoder.encode(portraitUri.toString(), "UTF8"));
		String body = sb.toString();
		if (body.indexOf("&") == 0) {
			body = body.substring(1, body.length());
		}
		if(mode.equalsIgnoreCase("1")){
			APP_KEY = "kj7swf8o7wvd2";
			APP_SECRET = "8RKzZU0OgyY43";	
		}else if(mode.equalsIgnoreCase("2")){
			APP_KEY = "qf3d5gbj35ylh";
			APP_SECRET = "RIBGbYILzgF";
		}
		map.put("appkey", APP_KEY);
		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(
				HostType.API, APP_KEY, APP_SECRET, "/user/getToken.json",
				"application/x-www-form-urlencoded");
		HttpUtil.setBodyParameter(body, conn);
		TokenReslut result=(TokenReslut) GsonUtil.fromJson(HttpUtil.returnResult(conn),
				TokenReslut.class);
		map.put("token", result.getToken());
		map.put("userId", result.getUserId());
		return map;
	}
}
