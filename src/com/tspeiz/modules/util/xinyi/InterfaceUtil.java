package com.tspeiz.modules.util.xinyi;

import java.util.ArrayList;
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
import org.apache.log4j.Logger;

import com.tspeiz.modules.common.bean.third.ThirdDepartment;
import com.tspeiz.modules.common.bean.third.ThirdDoctor;
import com.tspeiz.modules.common.bean.third.ThirdHospital;
import com.tspeiz.modules.common.bean.third.VisitBean;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;

public class InterfaceUtil {
	private static Logger log = Logger.getLogger(InterfaceUtil.class);
	/**
	 * 获取医院
	 * @param visit
	 * @return
	 * @throws Exception
	 */
	public static List<ThirdHospital> gainHospitals(VisitBean visit) throws Exception{
		JSONObject datas = new JSONObject();
		datas.put("permission_id", "");
		String result = commonPost("getAssignHospital", gainToken(visit),
				gainUuid(), datas,visit.getVisitUrl());
		JSONObject resultObj = JSONObject.fromObject(result);
		List<ThirdHospital> hospitals = new ArrayList<ThirdHospital>();
		if (resultObj.getString("code").equalsIgnoreCase("00")) {
			JSONObject datasObj = resultObj.getJSONObject("datas");
			JSONArray list = datasObj.getJSONArray("list");
			if(list != null && list.size()>0) {
				for(int i=0 ;i<list.size();i++) {
					JSONObject hosObj = list.getJSONObject(i);
					ThirdHospital hos = new ThirdHospital();
					hos.setHospitalId(hosObj.getString("HOSPITALID"));
					hos.setHospitalName(hosObj.getString("HOSPITALNAME"));
					hospitals.add(hos);
				}
			}
		}
		return hospitals;
	}
	
	/**
	 * 获取科室
	 * @param visit
	 * @param hospitalId
	 * @return
	 * @throws Exception
	 */
	public static List<ThirdDepartment> gainDepartments(VisitBean visit,String hospitalId) throws Exception {
		JSONObject datas = new JSONObject();
		datas.put("hospitalid", hospitalId);
		String result = commonPost("getAssignDepartment", gainToken(visit),
				gainUuid(), datas,visit.getVisitUrl());
		JSONObject resultObj = JSONObject.fromObject(result);
		List<ThirdDepartment> departments = new ArrayList<ThirdDepartment>();
		if (resultObj.getString("code").equalsIgnoreCase("00")) {
			JSONObject datasObj = resultObj.getJSONObject("datas");
			JSONArray list = datasObj.getJSONArray("list");
			if(list != null && list.size()>0) {
				for(int i=0 ;i<list.size();i++) {
					JSONObject depObj = list.getJSONObject(i);
					ThirdDepartment dep = new ThirdDepartment();
					dep.setDepartmentId(depObj.getString("DEPARTMENTID"));
					dep.setDepartmentName(depObj.getString("DEPARTMENTNAME"));
					departments.add(dep);
				}
			}
		}
		return departments;
	}
	/**
	 * 获取专家数据
	 * @param visit
	 * @param departmentId
	 * @return
	 * @throws Exception
	 */
	public static List<ThirdDoctor> gainDoctors(VisitBean visit,String departmentId) throws Exception {
		JSONObject datas = new JSONObject();
		datas.put("departmentid", departmentId);
		String result = commonPost("getAssignDocs", gainToken(visit), gainUuid(),
				datas,visit.getVisitUrl());
		JSONObject resultObj = JSONObject.fromObject(result);
		List<ThirdDoctor> doctors = new ArrayList<ThirdDoctor>();
		if (resultObj.getString("code").equalsIgnoreCase("00")) {
			JSONObject datasObj = resultObj.getJSONObject("datas");
			JSONArray list = datasObj.getJSONArray("list");
			if(list != null && list.size()>0) {
				for(int i=0 ;i<list.size();i++) {
					JSONObject docObj = list.getJSONObject(i);
					ThirdDoctor doc = new ThirdDoctor();
					doc.setDoctorId(docObj.getString("USERID"));
					doc.setDoctorName(docObj.getString("USERNAME"));
					doctors.add(doc);
				}
			}
		}
		return doctors;
	}
	/**
	 * 
	 * @param visit  访问实体
	 * @param caseInfo  病例信息
	 * @param appHosName  申请人医院
	 * @param appDepName  申请人科室
	 * @param appDoc      申请人
	 * @param consultationTypeId   会诊方式 1:点名会诊，2：非点名会诊、
	 * @param doctorId    中日专家id
	 * @param departmentId 中日科室id
	 * @param hospitalId   中日医院id
	 * @param emergency    是否紧急
	 * @param mobileTel    申请人电话
	 * @throws Exception
	 */
	public static String submitOrder(VisitBean visit,CaseInfo caseInfo,String appHosName,String appDepName,
			String appDoc,String consultationTypeId,String doctorId,String departmentId,
			String hospitalId,String emergency,String mobileTel,String attachments) throws Exception {
		JSONObject datas = gainConsultationDatas(caseInfo,visit,appHosName,appDepName,
				appDoc,consultationTypeId,doctorId,departmentId,hospitalId,emergency,mobileTel,attachments);
		log.info("===申请数据："+datas.toString());
		//String consultationId = UUID.randomUUID().toString().replace("-", "");
		String result = commonPost("saveConsultation", gainToken(visit), gainUuid(),
				datas,visit.getVisitUrl());
		String consultationId ="";
		JSONObject resultObj = JSONObject.fromObject(result);
		if (resultObj.getString("code").equalsIgnoreCase("00")) {
			JSONObject datasObj = resultObj.getJSONObject("datas");
			consultationId = datasObj.getString("id");
		}
		return consultationId;
	}
	
	private static JSONObject gainConsultationDatas(CaseInfo caseInfo,VisitBean visit,String appHosName,
			String appDepName,String appDoc,
			String consultationTypeId,String doctorId,String departmentId,String hospitalId,
			String emergency,String mobileTel,String attachments) {
		JSONObject datas = new JSONObject();
		//附件json
		if(StringUtils.isNotBlank(attachments)) {
			JSONObject attachmentsObj = JSONObject.fromObject(attachments);
			JSONArray listArray = attachmentsObj.getJSONArray("attechmentlist");
			datas.put("attechmentlist", listArray);
		}
		// 患者信息
		datas.put("patientname", caseInfo.getContactName());// 患者姓名
		datas.put("patientage", caseInfo.getAge()!=null?caseInfo.getAge().toString():"");// 年龄
		datas.put("patientageunitid", "1");// 年龄单位
		datas.put("patientgender", caseInfo.getSex()!=null?(caseInfo.getSex().equals(1)?"1":"0"):"2");// 性别
		datas.put("idnumber", caseInfo.getIdNumber());// 身份证
		datas.put("patientphone", caseInfo.getTelephone());// 联系电话

		// 会诊信息
		datas.put("requserid", visit.getUserId());// 用户id
		
		datas.put("reqhospital", appHosName);// 申请医院
		datas.put("reqdepartment", appDepName);// 申请科室
		datas.put("reqdoc", appDoc);// 申请医生
		
		
		datas.put("diagtype", "clinic");// 会诊类型
		
		datas.put("consultationtypeid", consultationTypeId);// 会诊方式 1:点名会诊，2：非点名会诊、
		
		datas.put("assigndocid", doctorId);
		datas.put("assigndepartmentid", departmentId);// 科室id
		datas.put("assignhospitalid", hospitalId);// 医院id
		datas.put("emergency",emergency);// 急诊情况
		if(StringUtils.isNotBlank(mobileTel)) {
			JSONArray propertys = new JSONArray();
			JSONObject property = new JSONObject();
			property.put("key", "reqdoctel");
			property.put("val", mobileTel);
			propertys.add(property);
			datas.put("propertys", propertys);// 申请人联系电话
		}
		// 临床信息
		datas.put("chiefcomplaint", caseInfo.getMainSuit());// 主诉 ---mainSuit
		datas.put("illnesshistory", caseInfo.getPresentIll());// 病史---PresentHistory
		datas.put("pasthistory", caseInfo.getHistoryIll());// 既往史--PastHistory
		datas.put("illness", caseInfo.getExamined());// --Examined 体检
		datas.put("examination", caseInfo.getAssistantResult());// 辅助检查--examination
		datas.put("prediagnose", caseInfo.getInitialDiagnosis());// 诊断--InitialDiagnosis
		datas.put("reqconsult", caseInfo.getAskProblem());// 会诊目的--askProblem
		
		return datas;
	}
	/**
	 * 获取会诊信息
	 * @param consultationId
	 * @param visit
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getConsultationDetail(String consultationId,VisitBean visit) throws Exception {
		JSONObject datas = new JSONObject();
		datas.put("id", consultationId);
		String result = commonPost("getConsultationDetail", gainToken(visit), gainUuid(),
				datas,visit.getVisitUrl());
		JSONObject resultObj = JSONObject.fromObject(result);
		return resultObj;
	}

	private static String gainToken(VisitBean visit) throws Exception{
		return gainToken(visit.getUserId(),visit.getPassword(),visit.getTokenUrl(),visit.getTokenValid());
	}
	public static String gainToken(String userId,String password,String tokenUrl,String tokenValid) throws Exception {
		String token = "";
		if(StringUtils.isNotBlank(tokenValid) && tokenValid.equalsIgnoreCase("true")) {
			JSONObject obj = new JSONObject();
			obj.put("userid", userId);
			obj.put("password", password);
			String result = httpPostWithJSON(
					tokenUrl,
					obj.toString());
			JSONObject resultObj = JSONObject.fromObject(result);
			if (resultObj.getString("code").equalsIgnoreCase("00")) {
				JSONObject datasObj = resultObj.getJSONObject("datas");
				token = datasObj.getString("TOKEN");
			}
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
			String messageId, JSONObject datas,String visitUrl) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("LOGICNAME", logicName);
		obj.put("TOKEN", token);
		obj.put("MESSAGEID", messageId);
		obj.put("DATAS", datas.toString());
		System.out.println("===请求json:" + obj.toString());
		return httpPostWithJSON(visitUrl, obj.toString());
	}

	private static String gainUuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
