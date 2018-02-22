package com.tspeiz.modules.util;

import it.sauronsoftware.base64.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class LocationUtil {
	public static String gainLocationString(double y, double x) {
		Map<String, String> map = gainLocationXY(y, x);
		return getCityFromLngAndlat(map.get("x"), map.get("y"));
	}

	public static Map<String, String> gainLocationXY(double y, double x) {
		String path = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x="
				+ x + "+&y=" + y + "&callback=BMap.Convertor.cbk_7594";
		Map<String, String> map = new HashMap<String, String>();
		try {
			// 使用http请求获取转换结果
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();

			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			// 得到返回的结果
			String res = outStream.toString();
			System.out.println(res);
			// 处理结果
			if (res.indexOf("(") > 0 && res.indexOf(")") > 0) {
				String str = res.substring(res.indexOf("(") + 1, res
						.indexOf(")"));
				String err = res.substring(res.indexOf("error") + 7, res
						.indexOf("error") + 8);
				if ("0".equals(err)) {
					JSONObject js = JSONObject.fromObject(str);
					// 编码转换
					String x1 = new String(Base64.decode(js.getString("x")));
					String y1 = new String(Base64.decode(js.getString("y")));
					map.put("x", x1);
					map.put("y", y1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static String getCityFromLngAndlat(String y, String x) {
		String url2 = "http://api.map.baidu.com/geocoder/v2/?ak=9GKXFcV6sK9YrE07ekaWxyhu&location="
				+ x + "," + y + "&output=json";
		System.out.println(url2);
		URL myURL2 = null;
		URLConnection httpsConn2 = null;
		try {
			myURL2 = new URL(url2);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		InputStreamReader insr2 = null;
		BufferedReader br2 = null;
		try {
			httpsConn2 = (URLConnection) myURL2.openConnection();// 不使用代理
			if (httpsConn2 != null) {
				insr2 = new InputStreamReader(httpsConn2.getInputStream(),
						"UTF-8");
				br2 = new BufferedReader(insr2);
				String data2 = br2.readLine();
				JSONObject jsonObject = JSONObject.fromObject(data2);
				if (jsonObject.getString("status").equalsIgnoreCase("0")) {
					// 成功
					System.out.println(jsonObject.getJSONObject("result"));
					return jsonObject.getJSONObject("result").getJSONObject(
							"addressComponent").getString("city");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static Map<String,Object> gainDataFromLngAndlat(double y, double x){
		Map<String, String> map = gainLocationXY(y, x);
		return getDataFromLngAndlat(map.get("x"), map.get("y"));
		
	}
	private static Map<String,Object> getDataFromLngAndlat(String y, String x) {
		Map<String,Object> map=new HashMap<String,Object>();
		String url2 = "http://api.map.baidu.com/geocoder/v2/?ak=9GKXFcV6sK9YrE07ekaWxyhu&location="
				+ x + "," + y + "&output=json";
		System.out.println(url2);
		URL myURL2 = null;
		URLConnection httpsConn2 = null;
		try {
			myURL2 = new URL(url2);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		InputStreamReader insr2 = null;
		BufferedReader br2 = null;
		try {
			httpsConn2 = (URLConnection) myURL2.openConnection();// 不使用代理
			if (httpsConn2 != null) {
				insr2 = new InputStreamReader(httpsConn2.getInputStream(),
						"UTF-8");
				br2 = new BufferedReader(insr2);
				String data2 = br2.readLine();
				JSONObject jsonObject = JSONObject.fromObject(data2);
				if (jsonObject.getString("status").equalsIgnoreCase("0")) {
					// 成功
					JSONObject obj=jsonObject.getJSONObject("result").getJSONObject(
							"addressComponent");
					String province=obj.getString("province");
					String city= obj.getString("city");
					String district=obj.getString("district");
					String adcode=obj.getString("adcode");
					map.put("province", province);
					map.put("city", city);
					map.put("district", district);
					map.put("distcode", adcode);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String[] args) {
		//27.9535030000,116.9010920000
		//31.7370680000,116.5137120000
		//27.9003930000,116.8872940000
		//27.9371640000,116.3905670000
		System.out.println(gainLocationString(27.9371640000,116.3905670000));
	}
}
