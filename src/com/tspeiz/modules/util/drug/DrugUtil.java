package com.tspeiz.modules.util.drug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.tspeiz.modules.util.MD5Util;

public class DrugUtil {
	private static String SK_KEY = "SK2006TOERL17TENYEA";
	private static String VISIT_URL = "http://218.65.18.146:11089/";
	public static void main(String[] args)throws Exception {
		//gainShops("GetStoreList","");
		//gainGoods("GetGoodsList","");
		//gainGoodsPic("GetGoodsPic","SPH00000438");
	}
	/**
	 * 获取请求json
	 * @param skSign  sk KEY
	 * @param action  ：GetGoodsList
	 * @param mtime  2017-10-01 00:00:01
	 */
	@SuppressWarnings("rawtypes")
	public static void gainPostJson(String skSign,JSONObject jsonObject) {
		Iterator iterator = jsonObject.keys();
		List<String> keys = new ArrayList<String>();
		while(iterator.hasNext()){
			String key = iterator.next().toString();
			keys.add(key);
		}
		Collections.sort(keys);
		StringBuilder sb = new StringBuilder();
		sb.append(skSign);
		for(String key:keys) {
			sb.append(key+"="+jsonObject.getString(key));
		}
		sb.append(skSign);
		System.out.println("==md5参数："+sb.toString());
		String sign = MD5Util.string2MD5(MD5Util.string2MD5(sb.toString()).toUpperCase()).toUpperCase();
		jsonObject.put("Sign", sign);
	}
	
	/**
	 * 获取药店数据
	 * @param lastModifyTime
	 * @throws Exception
	 */
	public static void gainShops(String actionName,String lastModifyTime) throws Exception{
		JSONObject obj = new JSONObject();
		obj.put("Action", actionName);
		obj.put("LastModifyTime", lastModifyTime);
		gainPostJson(SK_KEY,obj);
		System.out.println("===请求json=="+obj.toString());
		String ret = httpPostWithJSON(VISIT_URL+actionName,obj.toString());
		System.out.println("==药店返回:"+ret);
		if(StringUtils.isNotBlank(ret)) {
			JSONObject retObj = JSONObject.fromObject(ret);
			if(retObj.containsKey("Success") && retObj.getString("Success").equalsIgnoreCase("TRUE")){
				JSONArray datas = retObj.getJSONArray("Data");
				System.out.println("===药店："+datas.toString());
			}
		}
	}
	/**
	 * 获取药品
	 * @param lastModifyTime
	 * @throws Exception
	 */
	public static void gainGoods(String actionName,String lastModifyTime) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("Action", actionName);
		obj.put("LastModifyTime", lastModifyTime);
		gainPostJson(SK_KEY,obj);
		System.out.println("===请求json=="+obj.toString());
		/*String ret = httpPostWithJSON(VISIT_URL+actionName,obj.toString());
		System.out.println("==药品返回:"+ret);
		if(StringUtils.isNotBlank(ret)) {
			JSONObject retObj = JSONObject.fromObject(ret);
			if(retObj.containsKey("Success") && retObj.getString("Success").equalsIgnoreCase("TRUE")){
				JSONArray datas = retObj.getJSONArray("Data");
				System.out.println("===药品："+datas.toString());
			}
		}*/
	}
	
	/**
	 * 获取药品图片
	 * @param spid
	 * @throws Exception
	 */
	public static void gainGoodsPic(String actionName,String spid) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("Action", actionName);
		obj.put("Spid", spid);
		gainPostJson(SK_KEY,obj);
		System.out.println("===请求json=="+obj.toString());
		/*String ret = httpPostWithJSON(VISIT_URL+actionName,obj.toString());
		System.out.println("==药品图片返回:"+ret);
		if(StringUtils.isNotBlank(ret)) {
			JSONObject retObj = JSONObject.fromObject(ret);
			if(retObj.containsKey("Success") && retObj.getString("Success").equalsIgnoreCase("TRUE")){
				JSONArray datas = retObj.getJSONArray("Data");
				System.out.println("===药品图片："+datas.toString());
			}
		}*/
	}
	
	/**
	 * post请求
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static String httpPostWithJSON(String url, String param)
			throws Exception {
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpClient client = HttpClients.createDefault();
		String respContent = null;
		StringEntity entity = new StringEntity(param, "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/x-www-form-urlencoded");
		httpPost.setEntity(entity);
		HttpResponse resp = client.execute(httpPost);
		if (resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity httpEntity = resp.getEntity();
			respContent = EntityUtils.toString(httpEntity, "UTF-8");
		}
		return respContent;
	}
	
}
