package com.tspeiz.modules.util.xinyi;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TestInterface {
	private static String userId = "20135";
	private static String password = "123456";
	private static String VISIT_URL = "http://112.33.17.181:8090/TMSServernew/api/v2/server.do";

	public static void main(String[] args) throws Exception {
		// System.out.println("===token:"+gainToken());
		// gainAssignHospital();
		// gainAssignDepartment("ef2a979843b149a4878cbaafcb6a0cf5");
		// gainAssignDocs("e1eae80202014342a45e489bbc9cc747");
		//saveConsultation();
		zipFiles();
		//saveConsultationFile();
	}
	/**
	 * 会诊申请
	 * @throws Exception
	 */
	private static void saveConsultation() throws Exception {
		JSONObject datas = gainConsultationDatas();
		String result = commonPost("saveConsultation", gainToken(), gainUuid(),
				datas);
		System.out.println("===result:" + result);
	}
	
	/*private static void saveConsultationFile() throws Exception {
		JSONObject datas = new JSONObject();
		datas.put("filename", "test.jpg");
		datas.put("url", "http://test20171106.oss-cn-beijing.aliyuncs.com/2017/01/03/8a6e0260-eb4a-48c0-8b47-4a6b08d29dc6.jpg");
		datas.put("srcsid", "623d1f8af97d43f781e7de835504c09c");
		String result = commonPost("saveConsultationFile", gainToken(), gainUuid(),
				datas);
		System.out.println("===result:"+result);
	}*/

	private static JSONObject gainConsultationDatas() {
		JSONObject datas = new JSONObject();
		// 患者信息
		datas.put("patientname", "何勇滨");// 患者姓名
		datas.put("patientage", "28");// 年龄
		datas.put("patientageunitid", "1");// 年龄单位
		datas.put("patientgender", "1");// 性别
		datas.put("idnumber", "362502198902051414");// 身份证
		datas.put("patientphone", "13681473419");// 联系电话

		// 会诊信息
		datas.put("requserid", userId);// 用户id
		datas.put("reqhospital", "第三方测试医院");// 申请医院
		datas.put("reqdepartment", "远程");// 申请科室
		datas.put("reqdoc", "第三方测试医生");// 申请医生
		datas.put("diagtype", "clinic");// 会诊类型
		datas.put("consultationtypeid", "1");// 会诊方式 1:点名会诊，2：非点名会诊、
		datas.put("assigndocid", "6b905c21d4f04552a0c42a05647e923b");
		datas.put("assigndepartmentid", "e1eae80202014342a45e489bbc9cc747");// 科室id
		datas.put("assignhospitalid", "ef2a979843b149a4878cbaafcb6a0cf5");// 医院id
		datas.put("emergency", "0");// 急诊情况
		JSONArray propertys = new JSONArray();
		JSONObject property = new JSONObject();
		property.put("key", "reqdoctel");
		property.put("val", "13681473419");
		propertys.add(property);
		datas.put("propertys", propertys);// 申请人联系电话

		// 临床信息
		datas.put("chiefcomplaint", "主诉");// 主诉 ---mainSuit
		datas.put("illnesshistory", "病史");// 病史---PresentHistory
		datas.put("pasthistory", "既往史");// 既往史--PastHistory
		datas.put("illness", "查体");// --Examined 体检
		datas.put("examination", "辅助检查");// 辅助检查--examination
		datas.put("prediagnose", "诊断");// 诊断--InitialDiagnosis
		datas.put("reqconsult", "咨询目的");// 会诊目的--askProblem

		return datas;
	}

	/**
	 * 获取所有医院
	 * 
	 * @throws Exception
	 */
	public static void gainAssignHospital() throws Exception {
		JSONObject datas = new JSONObject();
		datas.put("permission_id", "");
		String result = commonPost("getAssignHospital", gainToken(),
				gainUuid(), datas);
		System.out.println("===result:" + result);
	}

	/**
	 * 获取科室
	 * 
	 * @param hospitalId
	 * @throws Exception
	 */
	public static void gainAssignDepartment(String hospitalId) throws Exception {
		JSONObject datas = new JSONObject();
		datas.put("hospitalid", hospitalId);
		String result = commonPost("getAssignDepartment", gainToken(),
				gainUuid(), datas);
		System.out.println("===result:" + result);
	}
	/**
	 * 获取专家
	 * @param departmentId
	 * @throws Exception
	 */
	public static void gainAssignDocs(String departmentId) throws Exception {
		JSONObject datas = new JSONObject();
		datas.put("departmentid", departmentId);
		String result = commonPost("getAssignDocs", gainToken(), gainUuid(),
				datas);
		System.out.println("===result:" + result);
	}

	/**
	 * 通用post请求
	 * 
	 * @param logicName
	 * @param token
	 * @param messageId
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public static String commonPost(String logicName, String token,
			String messageId, JSONObject datas) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("LOGICNAME", logicName);
		obj.put("TOKEN", token);
		obj.put("MESSAGEID", messageId);
		obj.put("DATAS", datas.toString());
		System.out.println("===请求json:" + obj.toString());
		return httpPostWithJSON(VISIT_URL, obj.toString());
	}

	/**
	 * 获取token
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String gainToken() throws Exception {
		String token = "";
		JSONObject obj = new JSONObject();
		obj.put("userid", userId);
		obj.put("password", password);
		String result = httpPostWithJSON(
				"http://112.33.17.181:8090/TMSServer/auth/server.do",
				obj.toString());
		JSONObject resultObj = JSONObject.fromObject(result);
		if (resultObj.getString("code").equalsIgnoreCase("00")) {
			JSONObject datasObj = resultObj.getJSONObject("datas");
			token = datasObj.getString("TOKEN");
		}
		return token;
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
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpResponse resp = client.execute(httpPost);
		if (resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity httpEntity = resp.getEntity();
			respContent = EntityUtils.toString(httpEntity, "UTF-8");
		}
		return respContent;
	}

	private static String gainUuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static void test() throws Exception {
		JSONObject datas = new JSONObject();
		datas.put("id", "12345678901234567890");
		datas.put("status", "50");
		datas.put("statusName", "后质控完成");
		datas.put("remark", "备注信息");
		JSONObject obj = new JSONObject();
		obj.put("LOGICNAME", "sendStatus");
		obj.put("TOKEN", gainToken());
		obj.put("MESSAGEID", gainUuid());
		obj.put("DATAS", datas.toString());
		System.out.println("===" + obj.toString());
		String ret = httpPostWithJSON(
				"https://develop.ebaiyihui.com/modify/order/status",
				obj.toString());
		System.out.println(ret);
	}

	public static void zipFiles() throws Exception {
		String zipFileName = "C:\\Users\\kx\\Desktop\\test.zip";
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		BufferedOutputStream bos = new BufferedOutputStream(out);
		String[] files = new String[] {
				"https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQGp8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyemlfSmRVR2ZkSjIxMDAwMGcwN2UAAgRTuSRaAwQAAAAA"};
		for (int i = 0; i < files.length; i++) {
			URL url = new URL(files[i]);
			out.putNextEntry(new ZipEntry("医院/科室/"+i + ".jpg"));
			InputStream fis = url.openConnection().getInputStream();
			byte[] buffer = new byte[1024];
			int r = 0;
			while ((r = fis.read(buffer)) != -1) {
				out.write(buffer, 0, r);
			}
			fis.close();
		}
		out.flush();
		out.close();
	}
}
