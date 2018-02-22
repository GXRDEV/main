package com.tspeiz.modules.util.weixin;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.tspeiz.modules.common.bean.weixin.AccessToken;
import com.tspeiz.modules.common.bean.weixin.pojo.Menu;

public class CommonUtil {
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	public static String get_personal_info_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	public static String get_personal_info_url2 = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	
	public static String get_jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	public static String send_money_url="https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			System.out.println("=====json返回==="+buffer.toString());
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;
		String requestUrl = access_token_url.replace("APPID", appid).replace(
				"APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (Exception e) {
				accessToken = null;
				// 获取token失败
				System.out.println("===获取失败===");
			}
		}
		return accessToken;
	}

	/**
	 * 创建菜单
	 * 
	 * @param menu
	 *            菜单实例
	 * @param accessToken
	 *            有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;
		// 拼装创建菜单的url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		System.out.println(jsonMenu);
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				System.out.println("创建菜单失败 " + jsonObject.getInt("errcode")
						+ "===" + jsonObject.getString("errmsg"));
			}
		}
		return result;
	}

	// 获取用户的基本信息
	public static JSONObject GetPersonInfo2(String accessToken, String openid) {
		String url = get_personal_info_url.replace("ACCESS_TOKEN", accessToken);
		String realUrl = url.replace("OPENID", openid);
		JSONObject jsonObject = httpRequest(realUrl, "GET", null);
		if (null != jsonObject) {
			// 返回用户基本信息的json对象数据
			return jsonObject;
		}
		return null;
	}
	
	// 获取用户的基本信息
	public static JSONObject GetPersonInfo(String accessToken, String openid) {
		String url = get_personal_info_url2.replace("ACCESS_TOKEN", accessToken);
		String realUrl = url.replace("OPENID", openid);
		JSONObject jsonObject=null;
		for(int i=0;i<=50;i++){
			jsonObject = httpRequest(realUrl, "GET", null);
			if(jsonObject.containsKey("nickname"))break;
		}
		return jsonObject;	
	}
	private static String download_media_url="http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	// 下载媒体文件
	public static String downloadMediaFromWx(String accessToken,
			String mediaId, String fileSavePath,String newfilename) throws IOException {
		if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(mediaId))
			return null;
		String requestUrl = download_media_url.replace("ACCESS_TOKEN",
				accessToken).replace("MEDIA_ID", mediaId);
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		InputStream in = conn.getInputStream();
		File dir = new File(fileSavePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (!fileSavePath.endsWith("/")) {
			fileSavePath += "/";
		}
		//String ContentDisposition = conn.getHeaderField("Content-disposition");
		/*String weixinServerFileName = ContentDisposition.substring(
				ContentDisposition.indexOf("filename") + 10, ContentDisposition
						.length() - 1);*/
		String weixinServerFileName=newfilename+".amr";
		fileSavePath += weixinServerFileName;
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(fileSavePath));
		byte[] data = new byte[1024];
		int len = -1;
		while ((len = in.read(data)) != -1) {
			bos.write(data, 0, len);
		}
		bos.close();
		in.close();
		conn.disconnect();
		return fileSavePath;
	}

	// 获取 jsapi_ticket
	public static String getJsApiTicket(String accessToken) {
		String url = get_jsapi_ticket_url.replace("ACCESS_TOKEN", accessToken);
		JSONObject jsonObject = httpRequest(url, "GET", null);
		if (null != jsonObject) {
			return jsonObject.getString("ticket");
		}
		return "";
	}

	// 生成随机字符串
	private static String[] strs = new String[] { "a", "b", "c", "d", "e", "f",
			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };

	public static String GenerateNonceStr() {
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		int length = strs.length;
		for (int i = 0; i < 16; i++) {
			sb.append(strs[r.nextInt(length - 1)]);
		}
		return sb.toString();
	}

	public static String GenerateSignature(String jsapi_ticket,
			String noncestr, long timestamp, String url) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("jsapi_ticket=").append(jsapi_ticket).append("&")
				.append("noncestr=").append(noncestr).append("&").append(
						"timestamp=").append(timestamp).append("&").append(
						"url=").append(
						url.indexOf("#") >= 0 ? url.substring(0, url
								.indexOf("#")) : url);
		String content = stringBuilder.toString();
		System.out.println("====content==="+content);
		String signature = null;
		try {
			/*MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(content.toString().getBytes());
			signature = SignUtil.byteToStr(digest);*/
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(content.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}
	
	private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
	//调用发送红包的接口
	public static void main(String[] args) {
		GetPersonInfo("EIhKA7Cu8q3YT0rbZwzHAFoxmldUzjqar7dLyx63Mg33swfgpbZg6zzFajucmakfKCke-9V0cxShjtVNSVSJguyscmBbexUQoAogSE4wRp6vfrxyNm4g2oBhuGbFhb4XGGDbAIARRO", "oqDWct5MKGNbnRyAqIRqJih2C-pk");
	}
	
}
