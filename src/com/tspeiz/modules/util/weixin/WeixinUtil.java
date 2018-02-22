package com.tspeiz.modules.util.weixin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.tspeiz.modules.common.bean.weixin.resp.Article;
import com.tspeiz.modules.util.MD5Util;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.RequestHandler;
import com.tspeiz.modules.util.TenpayUtil;

public class WeixinUtil {
	private static SimpleDateFormat code_time = new SimpleDateFormat("yyyyMMddHHmmss");
	private static Logger log = Logger.getLogger(WeixinUtil.class);

	public static String toRefund(String out_trade_no,String out_refund_no,
			  String total_fee,String refund_fee,String tradeType)throws Exception{
			String certPath="";
			String appid="";
			String mch_id="";
			String parterKey="";
			if(tradeType!=null&&(tradeType.equalsIgnoreCase("JSAPI")||tradeType.equalsIgnoreCase("NATIVE"))){
				certPath=PropertiesUtil.getString("webCertPath");
				appid=PropertiesUtil.getString("APPID");
				mch_id=PropertiesUtil.getString("PARTNER");
				parterKey=PropertiesUtil.getString("PARTNERKEY");
			}else if(tradeType!=null&&tradeType.equalsIgnoreCase("APP")){
				certPath=PropertiesUtil.getString("appCertPath");
				appid=PropertiesUtil.getString("docAppId");
				mch_id=PropertiesUtil.getString("docPartner");
				parterKey=PropertiesUtil.getString("docPartnerKey");
			}else{
				return null;
			}
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("appid", appid);
			parameters.put("mch_id", mch_id);
			parameters.put("nonce_str", CreateNoncestr());
			parameters.put("out_trade_no", out_trade_no);
			parameters.put("out_refund_no", out_refund_no); // 我们自己设定的退款申请号，约束为UK
			parameters.put("total_fee", total_fee); // 单位为分
			parameters.put("refund_fee", refund_fee); // 单位为分
			parameters.put("op_user_id", mch_id);
			String sign = createSign("utf-8", parameters,parterKey);
			parameters.put("sign", sign);
			return refund(mch_id,getRequestXml(parameters),certPath);
	}
	
	public static String toRefund_child(String transactionId,String out_refund_no,
			  String total_fee,String refund_fee,String tradeType)throws Exception{
			String certPath="";
			String appid="";
			String mch_id="";
			String parterKey="";
			if(tradeType!=null&&(tradeType.equalsIgnoreCase("JSAPI")||tradeType.equalsIgnoreCase("NATIVE"))){
				certPath=PropertiesUtil.getString("webCertPath");
				appid=PropertiesUtil.getString("APPID");
				mch_id=PropertiesUtil.getString("PARTNER");
				parterKey=PropertiesUtil.getString("PARTNERKEY");
			}else if(tradeType!=null&&tradeType.equalsIgnoreCase("APP")){
				certPath=PropertiesUtil.getString("appCertPath");
				appid=PropertiesUtil.getString("docAppId");
				mch_id=PropertiesUtil.getString("docPartner");
				parterKey=PropertiesUtil.getString("docPartnerKey");
			}else{
				return null;
			}
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("appid", appid);
			parameters.put("mch_id", mch_id);
			parameters.put("nonce_str", CreateNoncestr());
			parameters.put("transaction_id", transactionId);
			parameters.put("out_refund_no", out_refund_no); // 我们自己设定的退款申请号，约束为UK
			parameters.put("total_fee", total_fee); // 单位为分
			parameters.put("refund_fee", refund_fee); // 单位为分
			parameters.put("op_user_id", mch_id);
			String sign = createSign("utf-8", parameters,parterKey);
			parameters.put("sign", sign);
			return refund(mch_id,getRequestXml(parameters),certPath);
	}
	
	private static String createSign(String charSet,
			SortedMap<Object, Object> parameters,String parterKey) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + parterKey);
		String sign = MD5Util.MD5Encode(sb.toString(), charSet).toUpperCase();
		return sign;
	}
	
	
	public static String refund(String mch_id,String reuqestXml, String certPath)
			throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream instream = new FileInputStream(new File(certPath));// 放退款证书的路径
		try {
			keyStore.load(instream, mch_id.toCharArray());
		} finally {
			instream.close();
		}
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,
				mch_id.toCharArray()).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		StringBuffer buffer = new StringBuffer();
		try {
			HttpPost httpPost = new HttpPost(
					"https://api.mch.weixin.qq.com/secapi/pay/refund");// 退款接口

			System.out.println("executing request" + httpPost.getRequestLine());
			StringEntity reqEntity = new StringEntity(reuqestXml);
			// 设置类型
			reqEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();

				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println("Response content length: "
							+ entity.getContentLength());
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"));
					String text;
					while ((text = bufferedReader.readLine()) != null) {
						buffer.append(text);
						System.out.println(text);
					}
				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return buffer.toString();
	}
	
	
	// 微信退款方法
	public static String refund(String appid, String mch_id,
			String out_trade_no, String out_refund_no, String total_fee,
			String refund_fee, String op_user_id, String certPath)
			throws Exception {
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("appid", appid);
		parameters.put("mch_id", mch_id);
		parameters.put("nonce_str", CreateNoncestr());
		parameters.put("out_trade_no", out_trade_no);
		parameters.put("out_refund_no", out_refund_no); // 我们自己设定的退款申请号，约束为UK
		parameters.put("total_fee", total_fee); // 单位为分
		parameters.put("refund_fee", refund_fee); // 单位为分
		System.out.println(refund_fee);
		parameters.put("op_user_id", op_user_id);
		String sign = createSign("utf-8", parameters);
		parameters.put("sign", sign);
		String reuqestXml = getRequestXml(parameters);
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream instream = new FileInputStream(new File(certPath));// 放退款证书的路径
		try {
			keyStore.load(instream, mch_id.toCharArray());
		} finally {
			instream.close();
		}

		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,
				mch_id.toCharArray()).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		try {

			HttpPost httpPost = new HttpPost(
					"https://api.mch.weixin.qq.com/secapi/pay/refund");// 退款接口

			System.out.println("executing request" + httpPost.getRequestLine());
			StringEntity reqEntity = new StringEntity(reuqestXml);
			// 设置类型
			reqEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();

				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println("Response content length: "
							+ entity.getContentLength());
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"));
					String text;
					while ((text = bufferedReader.readLine()) != null) {
						System.out.println(text);
					}

				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return "";
	}

	private static String CreateNoncestr() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < 16; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	private static String createSign(String charSet,
			SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + "Dzj1Ijv0EsnYteR1ZUt4gW0d8RFtj6Ry");
		String sign = MD5Util.MD5Encode(sb.toString(), charSet).toUpperCase();
		return sign;
	}

	private static String getRequestXml(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)
					|| "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}
	
	
	
	public static Map<String, Object> weipay(HttpServletRequest request,
			HttpServletResponse response, Float money, String appid,
			String appsecret, String partner, String partnerkey, String body,
			String openid, String notify_url,String out_trade_no,Integer oid,String orderprev){
		Map<String, Object> map = new HashMap<String, Object>();
		String finalmoney = String.format("%.2f", money);
		finalmoney = finalmoney.replace(".", "");
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 12位序列号,可以自行调整。
		String strReq = strTime + strRandom;
		// //商户号
		String mch_id = partner;
		// 随机数
		String nonce_str = strReq;
		// 商户订单号
		
		if(!StringUtils.isNotBlank(out_trade_no)){
			UUID uuid = UUID.randomUUID();
			out_trade_no = uuid.toString().replace("-", "");
		}
		log.info("====订单唯一识别====" + out_trade_no);
		int intMoney = Integer.parseInt(finalmoney);
		int total_fee = intMoney;
		String spbill_create_ip = request.getRemoteAddr();
		System.out.println("===ip==" + spbill_create_ip);
		String trade_type = "JSAPI";
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("out_trade_no", out_trade_no);

		packageParams.put("total_fee", total_fee + "");
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openid);

		RequestHandler reqHandler = new RequestHandler(request, response);
		reqHandler.init(appid, appsecret, partnerkey);
		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" + "<attach>" + ""
				+ "</attach>" + "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<total_fee>" + total_fee
				+ "</total_fee>" + "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<openid>" + openid + "</openid>"
				+ "</xml>";
		map.put("paramxml", xml);
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

		String prepay_id = "";
		try {
			prepay_id = new GetWxOrderno().getPayNo(createOrderURL, xml);
			map.put("prepayxml", prepay_id);
			Document doc = DocumentHelper.parseText(prepay_id);
			Element rootElt = doc.getRootElement(); // 获取根节点
			prepay_id = rootElt.elementText("prepay_id");
			if (!StringUtils.isNotBlank(prepay_id)) {
				log.info("=====统一支付接口获取预支付订单出错===");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String appid2 = appid;
		String timestamp = Sha1Util.getTimeStamp();
		String nonceStr2 = nonce_str;
		String prepay_id2 = "prepay_id=" + prepay_id;
		String packages = prepay_id2;
		finalpackage.put("appId", appid2);
		finalpackage.put("timeStamp", timestamp);
		finalpackage.put("nonceStr", nonceStr2);
		finalpackage.put("package", packages);
		finalpackage.put("signType", "MD5");
		String finalsign = reqHandler.createSign(finalpackage);
		map.put("out_trade_no", out_trade_no);
		map.put("appid", appid2);
		map.put("timeStamp", timestamp);
		map.put("nonceStr", nonceStr2);
		map.put("package", packages);
		map.put("sign", finalsign);
		return map;
	}
	
	
	public static Map<String, Object> weipay_pc(HttpServletRequest request,
			HttpServletResponse response, Float money, String appid,
			String appsecret, String partner, String partnerkey, String body,
			String product_id, String notify_url,String out_trade_no,Integer oid,String orderprev) {
		Map<String, Object> map = new HashMap<String, Object>();
		String finalmoney = String.format("%.2f", money);
		finalmoney = finalmoney.replace(".", "");
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 12位序列号,可以自行调整。
		String strReq = strTime + strRandom;
		// //商户号
		String mch_id = partner;
		// 随机数
		String nonce_str = strReq;
		// 商户订单号
		
		if(!StringUtils.isNotBlank(out_trade_no)){
			UUID uuid = UUID.randomUUID();
			out_trade_no = uuid.toString().replace("-", "");
		}
		log.info("====订单唯一识别====" + out_trade_no);
		int intMoney = Integer.parseInt(finalmoney);
		int total_fee = intMoney;
		String strEffectiveTime = code_time.format(new Date(new Date()
		.getTime() + (long) 2 * 60 * 60 * 1000));
		String time_start=code_time.format(new Date());
		String spbill_create_ip = request.getRemoteAddr();
		System.out.println("===ip==" + spbill_create_ip);
		String trade_type = "NATIVE";
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("out_trade_no", out_trade_no);

		packageParams.put("total_fee", total_fee + "");
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		packageParams.put("product_id", product_id);
		packageParams.put("time_start",time_start);
		packageParams.put("time_expire", strEffectiveTime);
		RequestHandler reqHandler = new RequestHandler(request, response);
		reqHandler.init(appid, appsecret, partnerkey);
		String sign = reqHandler.createSign(packageParams);
		
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" + "<attach>" + ""
				+ "</attach>" + "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<total_fee>" + total_fee
				+ "</total_fee>" + "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<product_id>" + product_id + "</product_id>"
				+ "<time_start>" + time_start
				+ "</time_start>" + "<time_expire>" + strEffectiveTime + "</time_expire>"
				+ "</xml>";
		map.put("paramxml", xml);
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

		String prepay_id = "";
		String code_url="";
		try {
			prepay_id = new GetWxOrderno().getPayNo(createOrderURL, xml);
			map.put("prepayxml", prepay_id);
			Document doc = DocumentHelper.parseText(prepay_id);
			Element rootElt = doc.getRootElement(); // 获取根节点
			log.info("===预支付返回==="+doc.asXML());
			prepay_id = rootElt.elementText("prepay_id");
			code_url=rootElt.elementText("code_url");
			if (!StringUtils.isNotBlank(prepay_id)) {
				log.info("=====统一支付接口获取预支付订单出错===");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		map.put("out_trade_no", out_trade_no);
		map.put("code_url", code_url);
		return map;
	}
	
	
	public static void makeNewsCustomMessage(String token,String openId,String orderUuid,Integer otype,String title,String username,String docname,String expname,String time) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+token;
		List<Article> all=new ArrayList<Article>();
		Article article=new Article();
		article.setDescription("专家提交了会诊报告。\n" +
				"患者姓名："+username+"\n" +
				"医生姓名："+docname+"\n" +
				"专家姓名："+expname+"\n" +
				"申请时间："+time+"\n" +
				"请查看会诊报告详情。");
		article.setTitle(title);
		article.setUrl(PropertiesUtil.getString("DOMAINURL") + "module/patient.html?openid="+openId+"#/details?oid="+orderUuid+"&otype="+otype);
		all.add(article);
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"news\",\"news\":{\"articles\":%s}}";
		jsonMsg = String.format(jsonMsg, openId, JSONArray.fromObject(all).toString().replaceAll("\"", "\\\""));
		jsonMsg = jsonMsg.replace("picUrl", "picurl");
		CommonUtil.httpRequest(url, "POST",jsonMsg);
	}
	public static String changeY2F(String amount) {
		String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥//
		// 或者$的金额
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(
					".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(
					".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(
					".", "") + "00");
		}
		return amLong.toString();
	}
}
