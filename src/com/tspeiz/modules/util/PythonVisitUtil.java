package com.tspeiz.modules.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import redis.clients.jedis.Jedis;

import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.util.dcm.UploadDcmUtil;
import com.tspeiz.modules.util.ftp.FTPUtil;
import com.tspeiz.modules.util.mongodb.MongoDBManager;
import com.tspeiz.modules.util.redis.RedisUtil;
import com.uwantsoft.goeasy.client.goeasyclient.GoEasy;

public class PythonVisitUtil {
	private static Logger log = Logger.getLogger(PythonVisitUtil.class);
	// private static String VISIT_IP = "http://101.200.216.220:80";
	private static String VISIT_IP = "http://218.22.197.122:59409";
	private static String FTP_IP = "";
	private static String FTP_PORT = "";
	private static String FTP_USER = "";
	private static String FTP_PASS = "";
	private static Map<String, String> osmap = new HashMap<String, String>();
	static {
		FTP_IP = PropertiesUtil.getString("ftp_ip");
		FTP_PORT = PropertiesUtil.getString("ftp_port");
		FTP_USER = PropertiesUtil.getString("ftp_user");
		FTP_PASS = PropertiesUtil.getString("ftp_pass");
	}

	// 发送post请求
	@SuppressWarnings({ "deprecation", "resource" })
	public static String doPostMethod(String url,
			List<NameValuePair> nameValuePairs) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		HttpResponse response;
		response = httpclient.execute(httppost);
		String strResult = EntityUtils.toString(response.getEntity());
		return strResult;
	}

	// map转换为json
	public static JSONObject generateJSONObject(Map<String, Object> params) {
		JSONObject obj = new JSONObject();
		for (String param : params.keySet()) {
			obj.put(param, params.get(param));
		}
		return obj;
	}

	// 获取科室列表
	public static String departments() throws Exception {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "1.0.0"));
		nameValuePairs.add(new BasicNameValuePair("data", "{}"));
		String ret = doPostMethod(VISIT_IP + "/departments/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println(eobj.get("message"));
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONArray departs = retObj.getJSONArray("response");
			for (int i = 0; i < departs.size(); i++) {
				JSONObject obj = departs.getJSONObject(i);
				System.out.println("===" + obj.getString("ID") + "===="
						+ obj.getString("name"));
			}
		}
		return ret;
	}

	public static void main(String[] args)throws Exception {
		patients_list("","511425201407016129","","","","","");
	}
	// 获取病人列表********************************************************************************
	public static List<ReSourceBean> patients_list(String orderid,
			String idcard, String searchName, String searchPatientId,
			String admisionNum, String outpatientNum, String department)
			throws Exception {
		List<ReSourceBean> beans = new ArrayList<ReSourceBean>();
		JSONObject _obj = new JSONObject();
		_obj.put("id_card", idcard);
		_obj.put("patient_id", searchPatientId);
		_obj.put("outpatient_id", outpatientNum);
		_obj.put("hospitalize_id", admisionNum);
		_obj.put("patient_name", searchName);
		_obj.put("department", department);
		System.out.println("===json==" + _obj.toString());
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", _obj.toString()));
		String ret = doPostMethod(VISIT_IP + "/patient/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		System.out.println("===返回消息=="+retObj.toString());
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println("===返回消息=="+eobj.getString("message"));
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONArray response = retObj.getJSONArray("response");
			if (response != null && response.size() > 0) {
				for (int i = 0; i < response.size(); i++) {
					ReSourceBean bean = new ReSourceBean();
					Map<Object, Object> kvs = new HashMap<Object, Object>();
					JSONObject obj = response.getJSONObject(i);
					for (Object key : obj.keySet()) {
						System.out.println("==key==" + obj.get(key));
						if (key.equals("病人ID"))
							bean.setKey(obj.get(key));
						kvs.put(key, obj.get(key));
					}
					bean.setKvs(kvs);
					beans.add(bean);
				}
			}
		}
		return beans;
	}

	// ***************************************************病人病例信息获取开始******************************************
	// 获取病人病例信息
	public static List<ReSourceBean> cases_list(String orderid, String patientid)
			throws Exception {
		JSONObject _obj = new JSONObject();
		if (StringUtils.isNotBlank(patientid)) {
			_obj.put("patient_id", Integer.parseInt(patientid));
		} else {
			_obj.put("patient_id", 895350);
		}
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", _obj.toString()));
		String ret = doPostMethod(VISIT_IP + "/cases/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		List<ReSourceBean> beans = new ArrayList<ReSourceBean>();
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			// 有病例信息
			JSONArray response = retObj.getJSONArray("response");
			if (response != null && response.size() > 0) {
				for (int i = 0; i < response.size(); i++) {
					JSONObject obj = response.getJSONObject(i);
					ReSourceBean bean = new ReSourceBean();
					Map<Object, Object> kvs = new HashMap<Object, Object>();
					for (Object key : obj.keySet()) {
						if (key.equals("病人ID"))
							bean.setKey(obj.get(key));
						kvs.put(key, obj.get(key));
						System.out.println("===key:" + key + "===value:"
								+ obj.get(key));
					}
					bean.setKvs(kvs);
					beans.add(bean);
				}
			}
		}
		return beans;
	}

	// ***************************************************病人病例信息获取结束******************************************

	// ****************************************************获取病人检查报告列表开始**************************************
	public static List<ReSourceBean> records_list(String oid, String patientid,
			String collection, String sub_collection,
			HttpServletRequest request, String syncSeries,String caseid) throws Exception {
		List<ReSourceBean> beans = new ArrayList<ReSourceBean>();
		// patientid="1618330";
		String rsyncSeris = (String) request.getSession().getAttribute(
				"synclisseris");
		if ((oid + "_" + syncSeries).equalsIgnoreCase(rsyncSeris)) {
			JSONObject obj = new JSONObject();
			if (StringUtils.isNotBlank(patientid)) {
				obj.put("patient_id", patientid);
			} else {
				obj.put("patient_id", 895350);
			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
			nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
			System.out.println(obj.toString());
			nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
			String ret = doPostMethod(VISIT_IP + "/records/", nameValuePairs);
			System.out.println("====返回===" + ret);
			JSONObject retObj = JSONObject.fromObject(ret);
			JSONObject eobj = retObj.getJSONObject("error");
			log.info("====获取病人检查报告列表返回：" + eobj.get("message"));
			if (eobj.getString("id").equalsIgnoreCase("0")) {
				JSONArray response = retObj.getJSONArray("response");
				// 清空相关的mongodb数据
				log.info("===清空mongodb数据===");
				MongoDBManager.clear_mongodb_rela(oid, collection, "");
				MongoDBManager.clear_mongodb_rela(oid, sub_collection, "");
				// 缓存列表
				// 界面显示
				String _rsyncSeris = "";
				boolean b = false;
				for (int i = 0; i < response.size(); i++) {
					_rsyncSeris = (String) request.getSession().getAttribute(
							"synclisseris");
					if (_rsyncSeris.equalsIgnoreCase(oid + "_" + syncSeries)) {
						b = true;
						sendLisMessage(oid, response.size(), i);
						ReSourceBean bean = new ReSourceBean();
						Map<Object, Object> kvs = new HashMap<Object, Object>();
						JSONObject jo = response.getJSONObject(i);
						jo.put("syncSeries_ad", syncSeries);
						jo.put("admin_case_id", caseid);//绑定病例
						// 入mongodb
						String _id = MongoDBManager.common_mongodb_insert(jo,
								collection, oid);
						log.info("=========ObjectId:" + _id);
						bean.setKey(_id);
						// 单条入mongodb
						log.info("==========报告单详情开始====");
						// MongoDBManager.common_mongodb_lis_single_insert(jo,
						// sub_collection, oid, _id);
						List<ReSourceBean> _beans = gainRecordDetail(jo,
								sub_collection, oid, _id, syncSeries,caseid);
						log.info("==========报告单详情结束====");
						for (Object key : jo.keySet()) {
							kvs.put(key, jo.get(key));
							log.info("===key:" + key + "===value:"
									+ jo.get(key));
						}
						bean.setKvs(kvs);
						bean.setBeans(_beans);
						beans.add(bean);
					} else {
						b = false;
						MongoDBManager.clear_mongodb_rela(oid, collection,
								syncSeries);
						MongoDBManager.clear_mongodb_rela(oid, sub_collection,
								syncSeries);
						beans = new ArrayList<ReSourceBean>();
					}
				}
				if (b) {
					// 设置缓存
					Jedis edis = RedisUtil.getJedis();
					String cacheName = oid + "_lis";
					edis.set(cacheName, JSONArray.fromObject(beans).toString());
					RedisUtil.returnResource(edis);
				}
			} else {
				sendLisMessage(oid, 0, 0);
			}
		}
		return beans;
	}

	private static void sendLisMessage(String orderid, Integer total,
			Integer curr) {
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "syncLis");
		jObj.put("from", 1);
		jObj.put("orderId", orderid);
		jObj.put("total", total);
		jObj.put("curr", curr);
		goEasy.publish(orderid + "", jObj.toString() + "");
	}

	private static List<ReSourceBean> gainRecordDetail(JSONObject jo,
			String collection, String oid, String _id, String syncSeries,String caseid)
			throws Exception {
		List<ReSourceBean> beans = new ArrayList<ReSourceBean>();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", "{\"id\":"
				+ jo.getInt("ID") + "}"));
		String ret = doPostMethod(VISIT_IP + "/record/detail/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println(eobj.get("message"));
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONArray response = retObj.getJSONArray("response");
			if (response != null && response.size() > 0) {
				for (int i = 0; i < response.size(); i++) {
					ReSourceBean bean = new ReSourceBean();
					Map<Object, Object> kvs = new HashMap<Object, Object>();
					JSONObject obj = response.getJSONObject(i);
					obj.put("parent_lis", _id);
					obj.put("syncSeries_ad", syncSeries);
					obj.put("admin_case_id", caseid);
					// 详情入库
					MongoDBManager
							.common_mongodb_insert(obj, "lis_sub_tb", oid);
					for (Object key : obj.keySet()) {
						kvs.put(key, obj.get(key));
					}
					bean.setKvs(kvs);
					beans.add(bean);
				}
			}
		}
		return beans;
	}

	// ****************************************************获取病人检查报告列表结束**************************************

	// ****************************************************获取影像信息开始======改版**********************************************
	public static List<ReSourceBean> imagesex(String oid, String patientid,
			String collection, HttpServletRequest request, String syncSeries,
			String localdir,String caseid) throws Exception {
		if (osmap.containsKey(oid)) {
			osmap.remove(oid);
		}
		List<ReSourceBean> beans = new ArrayList<ReSourceBean>();
		JSONObject obj = new JSONObject();
		if (StringUtils.isNotBlank(patientid)) {
			obj.put("patient_ids", patientid);
		}
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
		String ret = doPostMethod(VISIT_IP + "/imagesex/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println(eobj.get("message"));
		String cacheName = oid + "_pacs";
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONArray response = retObj.getJSONArray("response");
			// 清空相关库里记录
			MongoDBManager.clear_mongodb_pacs_pics(oid, collection, "");
			Map<String, List<JSONObject>> jmaps = sendpacstotalmessage_new(oid,
					response,syncSeries);// 初始发送
			String _rsyncSeris = "";
			boolean b = false;
			if (jmaps.size() > 0) {
				for (String modality : jmaps.keySet()) {
					List<JSONObject> jsons = jmaps.get(modality);
					for (int i = 0; i < jsons.size(); i++) {
						if (!osmap.containsKey(oid)) {
							osmap.put(oid, syncSeries);
						}
						if (osmap.get(oid).equalsIgnoreCase(syncSeries)) {
							JSONObject jo = jsons.get(i);
							b = true;
							log.info("=====================开始跑一序列=============");
							ReSourceBean bean = new ReSourceBean();
							Map<Object, Object> kvs = new HashMap<Object, Object>();
							jo.put("syncSeries_ad", syncSeries);
							jo.put("admin_case_id", caseid);//pacs 绑定病例
							String _id = MongoDBManager.common_mongodb_insert(
									jo, collection, oid);
							log.info("==============绑定病例===========");
							Map<String, Object> _ret = subPicsInsert(jo, _id,
									oid, jsons.size(), i, modality, localdir,
									request, syncSeries);
							if (_ret.containsKey("notexist")) {
								sendpacsmessage(oid, jsons.size(), i,
										Integer.parseInt(_ret.get("size")
												.toString()), 0, "none", jo,
										modality,syncSeries);
								MongoDBManager manage = MongoDBManager
										.getInstance();
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("_id", new ObjectId(_id));
								manage.delete("pacs_tb", map);
							} else if (!_ret.containsKey("status")) {
								jo.put("patient_id", _ret.get("patient_id"));
								jo.put("study_id", _ret.get("study_id"));
								jo.put("series_id", _ret.get("series_id"));
								jo.put("instance_id", _ret.get("instance_id"));
								MongoDBManager.common_mongodb_insert(jo,
										collection, oid, _id);
								bean.setKey(_id);
								bean.setStudyId(_ret.get("study_id").toString());
								bean.setSeriesId(_ret.get("series_id")
										.toString());
								bean.setInstanceId(_ret.get("instance_id")
										.toString());
								for (Object key : jo.keySet()) {
									kvs.put(key, jo.get(key));
									log.info("===key:" + key + "===value:"
											+ jo.get(key));
								}
								bean.setKvs(kvs);
								beans.add(bean);
								jo.put("_id", _id);
								sendpacsmessage(oid, jsons.size(), i,
										Integer.parseInt(_ret.get("size")
												.toString()), 0,
										_ret.get("patient_id").toString()
												+ "|"
												+ _ret.get("study_id")
														.toString()
												+ "|"
												+ _ret.get("series_id")
														.toString()
												+ "|"
												+ _ret.get("instance_id")
														.toString(), jo,
										modality,syncSeries);
								log.info("=====================一序列跑完=============");
							}

						} else {
							MongoDBManager.clear_mongodb_pacs_pics(oid,
									collection, syncSeries);
						}
					}
				}
				if (b) {
					// 设置缓存
					Jedis edis = RedisUtil.getJedis();
					edis.set(cacheName, JSONArray.fromObject(beans).toString());
					RedisUtil.returnResource(edis);
				}
			}

		}
		return beans;
	}

	private static Map<String, List<JSONObject>> sendpacstotalmessage_new(
			String oid, JSONArray response,String syncSeries) {
		Map<String, List<JSONObject>> jmaps = new TreeMap<String, List<JSONObject>>();
		for (int i = 0; i < response.size(); i++) {
			JSONObject jo = response.getJSONObject(i);
			String modality = jo.getString("Modality");
			List<JSONObject> jobs = jmaps.get(modality);
			if (jobs == null)
				jobs = new ArrayList<JSONObject>();
			jobs.add(jo);
			jmaps.put(modality, jobs);
		}
		JSONObject sendobj = new JSONObject();
		for (String key : jmaps.keySet()) {
			sendobj.put(key, jmaps.get(key).size());
		}
		sendpacstotalmessage(oid, sendobj,syncSeries);
		return jmaps;
	}

	private static void sendpacstotalmessage(String oid, JSONObject sendobj,String syncSeries) {
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "syncPacsOut");
		jObj.put("from", 1);
		jObj.put("orderId", oid);
		jObj.put("outTotal", sendobj.toString());
		jObj.put("syncSeries", syncSeries);
		goEasy.publish(oid, jObj.toString() + "");
	}

	private static Map<String, Object> subPicsInsert(JSONObject pobj,
			String pid, String oid, Integer outTotal, Integer outCurr,
			String modality, String localdir, HttpServletRequest request,
			String syncSeries) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("=====开始获取影像图片目录信息并上传至pacs服务器====");
		// String http_url = "";
		String dir = pobj.getString("Image_Directory");
		List<String> filenames = FTPUtil.listFTPFiles(dir);
		String ftp_url = "ftp://" + FTP_USER + ":" + FTP_PASS + "@" + FTP_IP
				+ ":" + FTP_PORT + "/" + dir;
		String afilename = "";
		String fname = "";
		// String seriesNumber = "";
		String patient_id = "";
		String study_id = "";
		String series_id = "";
		String instance_id = "";
		Map<String, Object> d_map = null;
		// MongoDBManager manager = MongoDBManager.getInstance();
		Integer num = 0;
		boolean first = true;
		if (filenames != null && filenames.size() > 0) {
			for (int i = 0; i < filenames.size(); i++) {
				if (osmap.containsKey(oid)
						&& osmap.get(oid).equalsIgnoreCase(syncSeries)) {
					sendpacsmessage(oid, outTotal, outCurr, filenames.size(),
							i, "", null, modality,syncSeries);
					num = num + 1;
					fname = filenames.get(i);
					afilename = ftp_url + fname;
					System.out.println("===afilename:" + afilename);
					log.info("====开始上传图片到pacs系统===");
					String retstr = FileUpload.copyFileToLocal(afilename,
							localdir);
					log.info("===" + retstr);
					if (!StringUtils.isNotBlank(retstr)) {
						map.put("notexist", "true");
						continue;
					}
					String status=UploadDcmUtil.uploadSingleDcm(retstr);
					if(!status.equalsIgnoreCase("true"))continue;
					if (StringUtils.isNotBlank(retstr)) {
						File output = new File(retstr);
						if (output.exists()) {
							System.out.println("====删除成功么？==="
									+ output.delete());
						}
					}
					log.info("====上传图片到pacs系统结束====");
					if (first) {
						d_map = DicomLoadUtil.gainDicomSeries2(afilename);
						if (d_map != null && d_map.size() > 0) {
							patient_id = d_map.get("patient_id").toString();
							study_id = d_map.get("study_id").toString();
							series_id = d_map.get("series_id").toString();
							instance_id = d_map.get("instance_id").toString();
							map.put("patient_id", patient_id);
							map.put("study_id", study_id);
							map.put("series_id", series_id);
							map.put("instance_id", instance_id);
							first = false;
						}
					}
				} else {
					map.put("status", "error");
					map.put("patient_id", "");
					map.put("study_id", "");
					map.put("series_id", "");
					map.put("instance_id", "");
				}
			}
			log.info("====开始结束====");
		}
		log.info("=====获取影像图片目录信息并上传至oss服务器结束====");
		map.put("size", num);
		return map;
	}

	private static void sendpacsmessage(String oid, Integer outTotal,
			Integer outCurr, Integer inTotal, Integer inCurr, String httpurl,
			JSONObject jo, String modality,String syncSeries) {
		String checkItem = "";
		String imageCount = "";
		String _id = "";
		String reportdate="";
		if (jo != null) {
			checkItem = jo.getString("Check_Item_E");
			imageCount = jo.getString("Image_Count");
			if(jo.containsKey("REPORT_DATE")&&StringUtils.isNotBlank(jo.get("REPORT_DATE")
					.toString())
			&& !jo.get("REPORT_DATE").toString()
					.equalsIgnoreCase("None")){
				reportdate=jo.get("REPORT_DATE").toString();
			}
			_id = jo.containsKey("_id") ? jo.getString("_id") : "";
		}
		GoEasy goEasy = new GoEasy(PropertiesUtil.getString("goeasykey"));
		JSONObject jObj = new JSONObject();
		jObj.put("type", "syncPacs");
		jObj.put("from", 1);
		jObj.put("orderId", oid);
		jObj.put("outTotal", outTotal);
		jObj.put("outCurr", outCurr);
		jObj.put("inTotal", inTotal);
		jObj.put("inCurr", inCurr);
		jObj.put("httpurl", httpurl);
		jObj.put("checkItem", checkItem);
		jObj.put("imageCount", imageCount);
		jObj.put("pid", _id);
		jObj.put("modality", modality);
		jObj.put("syncSeries", syncSeries);
		jObj.put("reportdate", reportdate);
		goEasy.publish(oid, jObj.toString() + "");
	}

	// ****************************************************获取影像信息结束**********************************************

	// 病人病历
	public static String cases() throws Exception {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data",
				"{\"id_card\":\"130283198902181929\"}"));
		String ret = doPostMethod(VISIT_IP + "/cases/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");

		System.out.println(eobj.get("message"));
		return ret;
	}

	public static String record_detail() throws Exception {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", "{\"id\":1853464}"));
		String ret = doPostMethod(VISIT_IP + "/record/detail/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println(eobj.get("message"));
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONArray response = retObj.getJSONArray("response");
			if (response != null && response.size() > 0) {
				for (int i = 0; i < response.size(); i++) {
					System.out.println("***************************");
					JSONObject obj = response.getJSONObject(i);
					for (Object key : obj.keySet()) {
						System.out.println("==key:" + key + "==value:"
								+ obj.get(key));
					}
					System.out.println("***************************");
					System.out.println();
				}
			}
		}
		return ret;
	}

	public static String record_detail(Integer rid) throws Exception {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", "{\"id\":" + rid
				+ "}"));
		String ret = doPostMethod(VISIT_IP + "/record/detail/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println(eobj.get("message"));
		return ret;
	}

	// 获取病人电子病历
	public static String cases2() throws Exception {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data",
				"{\"patient_id\":895350}"));
		String ret = doPostMethod(VISIT_IP + "/cases/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println(eobj.get("message"));
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONArray response = retObj.getJSONArray("response");
			if (response != null && response.size() > 0) {
				for (int i = 0; i < response.size(); i++) {
					System.out.println("***************************");
					JSONObject obj = response.getJSONObject(i);
					for (Object key : obj.keySet()) {
						System.out.println("==key:" + key + "==value:"
								+ obj.get(key));
					}
					System.out.println("***************************");
					System.out.println();
				}
			}
		}
		return ret;
	}

	// 获取报告单详情
	public static String record_detail2() throws Exception {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", "{\"id\":617556}"));
		String ret = doPostMethod(VISIT_IP + "/record/detail/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println(eobj.get("message"));
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONArray response = retObj.getJSONArray("response");
			if (response != null && response.size() > 0) {
				for (int i = 0; i < response.size(); i++) {
					System.out.println("***************************");
					JSONObject obj = response.getJSONObject(i);
					for (Object key : obj.keySet()) {
						System.out.println("==key:" + key + "==value:"
								+ obj.get(key));
					}
					System.out.println("***************************");
					System.out.println();
				}
			}
		}
		return ret;
	}

	public static List<ReSourceBean> imagesex(String pid) throws Exception {
		List<ReSourceBean> beans = new ArrayList<ReSourceBean>();
		JSONObject obj = new JSONObject();
		obj.put("patient_ids", pid);// 需要修改为病例id///1618330///1625477
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
		String ret = doPostMethod(VISIT_IP + "/imagesex/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		System.out.println(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println(eobj.get("message"));
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONArray response = retObj.getJSONArray("response");
			// 清空相关库里记录
			Map<String, List<JSONObject>> jmaps = sendpacstotalmessage_new(
					"123", response,"");// 初始发送
			boolean b = false;
			if (jmaps.size() > 0) {
				for (String modality : jmaps.keySet()) {
					List<JSONObject> jsons = jmaps.get(modality);
					for (int i = 0; i < jsons.size(); i++) {
						JSONObject jo = jsons.get(i);
						b = true;
						log.info("=====================开始跑一序列=============");
						ReSourceBean bean = new ReSourceBean();
						Map<Object, Object> kvs = new HashMap<Object, Object>();
						Map<String, Object> _ret = subPicsInsert2(jo, "12344",
								"123", jsons.size(), i, modality);
						// 旗下所有图片入库--返回第一张
						String furl = _ret.get("http_url").toString();
						jo.put("http_url", "http://www.baidu.com");
						bean.setKey("12344");
						bean.setValue(furl);
						for (Object key : jo.keySet()) {
							kvs.put(key, jo.get(key));
						}
						bean.setKvs(kvs);
						beans.add(bean);
						jo.put("_id", "12344");
						/*sendpacsmessage("123", jsons.size(), i,
								Integer.parseInt(_ret.get("size").toString()),
								0, furl, jo, modality,"");*/
						log.info("=====================一序列跑完=============");
					}
				}
			}

		}
		return beans;
	}

	private static Map<String, Object> subPicsInsert2(JSONObject pobj,
			String pid, String oid, Integer outTotal, Integer outCurr,
			String modality) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("=====开始获取影像图片目录信息并上传至oss服务器====");
		String http_url = "";
		String dir = pobj.getString("Image_Directory");
		List<String> filenames = FTPUtil.listFTPFiles(dir);
		String ftp_url = "ftp://" + FTP_USER + ":" + FTP_PASS + "@" + FTP_IP
				+ ":" + FTP_PORT + "/" + dir;
		String afilename = "";
		String fname = "";
		String seriesNumber = "";
		MongoDBManager manager = MongoDBManager.getInstance();
		Set<Integer> nums = new HashSet<Integer>();
		Map<Integer, String> urls = new HashMap<Integer, String>();
		Integer num = 0;
		
		if (filenames != null && filenames.size() > 0) {
			log.info("====图片数量*************"+filenames.size());
			for (int i = 0; i < filenames.size(); i++) {
				if (i > 9)
					break;
				sendpacsmessage(oid, outTotal, outCurr, filenames.size(), i,
						"", null, modality,"");
			}
			log.info("====开始结束====");
		}
		log.info("=====获取影像图片目录信息并上传至oss服务器结束====");
		map.put("http_url", http_url);
		map.put("size", num);
		return map;
	}

	public static void patients_list() throws Exception {
		JSONObject _obj = new JSONObject();
		_obj.put("id_card", "");
		_obj.put("patient_id", "");
		_obj.put("outpatient_id", "");
		_obj.put("hospitalize_id", "");
		_obj.put("department", "");
		_obj.put("patient_name", "刘文情");
		System.out.println(_obj.toString());
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", _obj.toString()));
		String ret = doPostMethod(VISIT_IP + "/patient/", nameValuePairs);
		System.out.println(ret);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println(eobj.getString("message"));
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONArray response = retObj.getJSONArray("response");
			if (response != null && response.size() > 0) {
				for (int i = 0; i < response.size(); i++) {
					System.out.println("***************************");
					JSONObject obj = response.getJSONObject(i);
					for (Object key : obj.keySet()) {
						System.out.println("==key:" + key + "==value:"
								+ obj.get(key));
					}
					System.out.println("***************************");
					System.out.println();
				}
			}
		}
	}

	// 获取报告单列表
	public static String records() throws Exception {
		System.out.println("====sele");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data",
				"{\"patient_id\":\"1545808,1545957\"}"));
		System.out.println("====sele");
		String ret = doPostMethod(VISIT_IP + "/records/", nameValuePairs);
		System.out.println(ret);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println(eobj.get("message"));
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONArray response = retObj.getJSONArray("response");
			if (response != null && response.size() > 0) {
				for (int i = 0; i < response.size(); i++) {
					System.out.println("***************************");
					JSONObject obj = response.getJSONObject(i);
					for (Object key : obj.keySet()) {
						System.out.println("==key:" + key + "==value:"
								+ obj.get(key));
					}
					System.out.println("***************************");
					System.out.println();
				}
			}
		}
		return ret;
	}

	// 挂号
	public static void register_info() throws Exception {
		log.info("=====六安挂号开始====");
		Map<String, Object> pinfo = new HashMap<String, Object>();
		pinfo.put("name", "何勇滨");
		pinfo.put("id_card", "362502198902051414");
		pinfo.put("telephone", "13681473419");
		String pstr = PythonVisitUtil.generateJSONObject(pinfo).toString();
		System.out.println(pstr);
		pinfo.clear();
		pinfo.put("dept_id", 1375);
		pinfo.put("type_name", "普通挂号");
		pinfo.put("register_fee", 2);
		pinfo.put("register_type", 3);
		pinfo.put("register_time", "2016-07-22 15:30:00");
		String rinfo = PythonVisitUtil.generateJSONObject(pinfo).toString();
		pinfo.clear();
		pinfo.put("patient_info", pstr);
		pinfo.put("register_info", rinfo);
		String data = PythonVisitUtil.generateJSONObject(pinfo).toString();
		log.info("=====六安挂号data:" + data);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", data));
		System.out.println(JSONArray.fromObject(nameValuePairs).toString());
		/*String ret = doPostMethod(VISIT_IP + "/register/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println(eobj.get("message"));
		System.out.println(ret);*/
		log.info("=====六安挂号结束====");
	}

	public static Map<String, Object> register_info(
			List<NameValuePair> nameValuePairs) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("=====六安挂号开始====");
		String ret = doPostMethod(VISIT_IP + "/register/", nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		map.put("eid", eobj.get("id"));
		map.put("message", eobj.get("message"));
		System.out.println(eobj.get("message"));
		log.info("=====六安挂号结束====");
		return map;
	}

	public static List<ReSourceBean> imageadvance(String orderid,
			String patientName, String checkNo, String checkType,
			String patientId, String mzNumber, String regtime) throws Exception {
		String cacheName = orderid + "_pacs_advance";
		JSONObject paramobj = new JSONObject();
		paramobj.put("patient_name", patientName);
		paramobj.put("check_no", checkNo);
		paramobj.put("check_type", checkType);
		paramobj.put("patient_id", patientId);
		paramobj.put("mz_number", mzNumber);
		paramobj.put("register_date", regtime);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", paramobj.toString()));
		String ret = doPostMethod(VISIT_IP + "/images/advanced/",
				nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println("==================="+eobj.get("message"));
		List<ReSourceBean> beans = new ArrayList<ReSourceBean>();
		Map<String, String> dirjsons = new HashMap<String, String>();
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONArray response = retObj.getJSONArray("response");
			if (response != null && response.size() > 0) {
				for (int i = 0; i < response.size(); i++) {
					ReSourceBean bean = new ReSourceBean();
					Map<Object, Object> kvs = new HashMap<Object, Object>();
					JSONObject obj = response.getJSONObject(i);
					for (Object key : obj.keySet()) {
						kvs.put(key, obj.get(key));
						if (key.equals("Image_Directory")) {
							dirjsons.put(obj.get(key).toString(),
									obj.toString());
						}
					}
					bean.setKvs(kvs);
					beans.add(bean);
				}
			}
		}
		if(dirjsons.size()>0){
			Jedis edis = RedisUtil.getJedis();
			edis.hmset(cacheName, dirjsons);
			RedisUtil.returnResource(edis);
		}
		System.out.println(JSONArray.fromObject(beans).toString());
		return beans;
	}

	private static void sendpacstotalmessage_advance(String oid,
			Map<String, List<String>> groupdirs,String syncSeries) {
		JSONObject sendobj = new JSONObject();
		for (String key : groupdirs.keySet()) {
			sendobj.put(key, groupdirs.get(key).size());
		}
		sendpacstotalmessage(oid, sendobj,syncSeries);
	}

	public static List<ReSourceBean> imagesex_advance(String orderid,
			Map<String, List<String>> groupdirs, String syncSeries,
			HttpServletRequest request, String localdir, String synctype,String caseid)
			throws Exception {
		if (osmap.containsKey(orderid)) {
			osmap.remove(orderid);
		}
		Jedis edis = RedisUtil.getJedis();
		List<ReSourceBean> beans = new ArrayList<ReSourceBean>();
		boolean b = false;
		sendpacstotalmessage_advance(orderid, groupdirs,syncSeries);
		if (synctype.equalsIgnoreCase("1")) {
			MongoDBManager.clear_mongodb_pacs_pics(orderid, "pacs_tb", "");
			edis.set(orderid + "_pacs", "");
		}
		if (groupdirs.size() > 0) {
			// 按影像类型进行分组解析
			for (String modality : groupdirs.keySet()) {
				List<String> dirs = groupdirs.get(modality);
				for (int i = 0; i < dirs.size(); i++) {
					String dir = dirs.get(i);
					if (!osmap.containsKey(orderid)) {
						osmap.put(orderid, syncSeries);
					}
					if (osmap.get(orderid).equalsIgnoreCase(syncSeries)) {
						List<String> list = edis.hmget(orderid
								+ "_pacs_advance", dir);
						if (list != null && list.size() > 0) {
							JSONObject jo = JSONObject.fromObject(list.get(0));
							b = true;
							ReSourceBean bean = new ReSourceBean();
							Map<Object, Object> kvs = new HashMap<Object, Object>();
							jo.put("syncSeries_ad", syncSeries);
							jo.put("admin_case_id", caseid);
							String _id = MongoDBManager.common_mongodb_insert(
									jo, "pacs_tb", orderid);
							Map<String, Object> _ret = subPicsInsert(jo, _id,
									orderid, dirs.size(), i, modality,
									localdir, request, syncSeries);
							if (_ret.containsKey("notexist")) {
								sendpacsmessage(orderid, dirs.size(), i,
										Integer.parseInt(_ret.get("size")
												.toString()), 0, "none", jo,
										modality,syncSeries);
								MongoDBManager manage = MongoDBManager
										.getInstance();
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("_id", new ObjectId(_id));
								manage.delete("pacs_tb", map);
							} else if (!_ret.containsKey("status")) {
								jo.put("patient_id", _ret.get("patient_id"));
								jo.put("study_id", _ret.get("study_id"));
								jo.put("series_id", _ret.get("series_id"));
								jo.put("instance_id", _ret.get("instance_id"));
								MongoDBManager.common_mongodb_insert(jo,
										"pacs_tb", orderid, _id);
								bean.setKey(_id);
								bean.setStudyId(_ret.get("study_id").toString());
								bean.setSeriesId(_ret.get("series_id")
										.toString());
								bean.setInstanceId(_ret.get("instance_id")
										.toString());
								for (Object key : jo.keySet()) {
									if(!jo.get(key).toString().equalsIgnoreCase("null"))
									kvs.put(key, jo.get(key));
									log.info("===key:" + key + "===value:"
											+ jo.get(key));
								}
								bean.setKvs(kvs);
								beans.add(bean);
								jo.put("_id", _id);
								sendpacsmessage(orderid, dirs.size(), i,
										Integer.parseInt(_ret.get("size")
												.toString()), 0,
										_ret.get("patient_id").toString()
												+ "|"
												+ _ret.get("study_id")
														.toString()
												+ "|"
												+ _ret.get("series_id")
														.toString()
												+ "|"
												+ _ret.get("instance_id")
														.toString(), jo,
										modality,syncSeries);
								log.info("=====================一序列跑完=============");
							}else if(_ret.containsKey("status")&&_ret.get("status").equals("error")){
								sendpacsmessage(orderid, dirs.size(), i,
										Integer.parseInt(_ret.get("size")
												.toString()), 0, "stop", jo,
										modality,syncSeries);
							}
						}
					} else {
						MongoDBManager.clear_mongodb_pacs_pics(orderid,
								"pacs_tb", syncSeries);
					}
				}
			}
		}
		// 更新缓存
		if (synctype.equalsIgnoreCase("1")) {
			// 覆盖
			edis.set(orderid + "_pacs", JSONArray.fromObject(beans).toString());
		} else if (synctype.equalsIgnoreCase("0")) {
			// 追加
			beans = appendcache(beans, orderid);
			edis.set(orderid + "_pacs", JSONArray.fromObject(beans).toString());
		}
		RedisUtil.returnResource(edis);
		return beans;
	}

	private static List<ReSourceBean> appendcache(List<ReSourceBean> beans,
			String orderid) {
		Jedis edis = RedisUtil.getJedis();
		String pacs_cache = edis.get(orderid + "_pacs");
		List<ReSourceBean> allbeans = new ArrayList<ReSourceBean>();
		if (StringUtils.isNotBlank(pacs_cache)
				&& !pacs_cache.equalsIgnoreCase("[]")) {
			RedisUtil.returnResource(edis);
			log.info("=====从缓存中获取pacs数据=====" + pacs_cache);
			JSONArray response = JSONArray.fromObject(pacs_cache);
			if (response != null && response.size() > 0) {
				Map<String, Class> classMap = new HashMap<String, Class>();
				classMap.put("beans", ReSourceBean.class);
				List<ReSourceBean> _beans = JSONArray.toList(response,
						ReSourceBean.class, classMap);
				allbeans.addAll(_beans);
			}
		}
		allbeans.addAll(beans);
		return allbeans;
	}
	
	public static void imageadvance_test() throws Exception {
		JSONObject paramobj = new JSONObject();
		paramobj.put("patient_name", "");
		paramobj.put("check_no", "");
		paramobj.put("check_type", "");
		paramobj.put("patient_id", "16051791");
		paramobj.put("mz_number", "");
		paramobj.put("register_date", "");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("appid", "dev.test.dev"));
		nameValuePairs.add(new BasicNameValuePair("version", "3.0"));
		nameValuePairs.add(new BasicNameValuePair("data", paramobj.toString()));
		String ret = doPostMethod(VISIT_IP + "/images/advanced/",
				nameValuePairs);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		System.out.println("==================="+eobj.get("message"));
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONArray response = retObj.getJSONArray("response");
			if (response != null && response.size() > 0) {
				for (int i = 0; i < response.size(); i++) {
					JSONObject obj = response.getJSONObject(i);
					for (Object key : obj.keySet()) {
						System.out.println("===key:"+key+" ===value:"+obj.get(key));
					}
				}
			}
		}
	}

	/*public static void main(String[] args) throws Exception {
		
		 * long start = System.currentTimeMillis();
		 * System.out.println(cases2()); long end = System.currentTimeMillis();
		 * System.out.println(end - start);
		 * System.out.println("==================");
		 
		// PythonVisitUtil.records_list("21","","lis_tb","lis_sub_tb");
		// }

		long start = System.currentTimeMillis();
		patients_list();
		//records();
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		System.out.println("==================");
		

	}*/
}
