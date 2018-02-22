package com.tspeiz.modules.home;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tspeiz.modules.common.bean.JSONParam;
import com.tspeiz.modules.common.bean.third.ThirdDepartment;
import com.tspeiz.modules.common.bean.third.ThirdDoctor;
import com.tspeiz.modules.common.bean.third.ThirdHospital;
import com.tspeiz.modules.common.bean.third.VisitBean;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.SystemBusinessDictionary;
import com.tspeiz.modules.common.entity.release2.ThirdOrderAttachment;
import com.tspeiz.modules.common.entity.release2.ThirdOrderInfo;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.manage.SystemAdminManager;
import com.tspeiz.modules.util.config.ReaderConfigUtil;
import com.tspeiz.modules.util.oss.OSSConfigure;
import com.tspeiz.modules.util.oss.OSSManageUtil;
import com.tspeiz.modules.util.xinyi.InterfaceUtil;

@Controller
@RequestMapping(value="xinyi")
public class XinyiController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
	@Autowired
	private SystemAdminManager systemAdminManager;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IWenzhenService wenzhenService;
	/**
	 * 中日订单list
	 * 
	 */
	@RequestMapping(value = "/receiveorderlist", method = RequestMethod.GET)
	public ModelAndView receiveorderlist(HttpServletRequest request) {
		return new ModelAndView("admin/xinyi_list");
	}
	
	/*
	 * 关于中日的会诊订单
	 * 
	 */
	@RequestMapping(value = "/gainreceiveorderdatas", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	Object gainreceiveorderdatas(@RequestBody JSONParam[] params,
			HttpServletRequest request) throws Exception {
		Map<String, String> paramMap = convertToMap(params);
		return systemAdminManager.gainreceiveorderdatas(paramMap);
	}
	
	/**
	 * 订单详情
	 * 
	 */
	@RequestMapping(value = "/receiveorderdatas", method = RequestMethod.GET)
	public ModelAndView receiveorderdatas(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orderId", request.getParameter("orderId"));
		return new ModelAndView("admin/xinyi_datas",map);
	}
	
	/**
	 * 
	 * 获取详情数据
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/order/info")
	@ResponseBody
	public Map<String,Object> vedioOrderInfo(HttpServletRequest request) throws Exception{
		Map<String,Object> map = new HashMap<>();
		Integer orderId = Integer.parseInt(request.getParameter("orderId"));
		//订单信息
		BusinessVedioOrder vedio =commonService.queryBusinessVedioById(orderId);
		//病例信息
		CaseInfo caseInfo = commonService.queryCaseInfoById(vedio.getCaseId());
		map.put("caseInfo", caseInfo);
		String doneApp = "false";
		ThirdOrderInfo orderInfo = commonService.queryThirdOrderInfoByOrderUuid(vedio.getUuid());
		map.put("info", orderInfo);
		if(orderInfo != null) {
			doneApp = "true";
		}
		if(doneApp.equalsIgnoreCase("true")) {
			List<ThirdOrderAttachment> attas = commonService.queryThirdOrderAttachments(vedio.getUuid());
			List<CustomFileStorage> files = new ArrayList<CustomFileStorage>();
			if(attas != null && attas.size()>0) {	
				for(ThirdOrderAttachment att:attas) {
					CustomFileStorage cus = commonService.queryCustomFileStorage(att.getAttachmentId());
					files.add(cus);
				}
			}
			map.put("files", files);
		}else {
			//还没有提交会诊申请
			List<CustomFileStorage> files = processAttachments_all(vedio.getUuid(),caseInfo,request);
			map.put("files", files);
		}
		map.put("doneApp", doneApp);
		return map;
	}
	private List<CustomFileStorage> processAttachments_all(String orderUuid,CaseInfo caseInfo,HttpServletRequest request) throws Exception{
		List<CustomFileStorage> files = null;
		List<ThirdOrderAttachment> attachments = commonService.queryThirdOrderAttachments(orderUuid);
		if(attachments != null && attachments.size()>0) {
			files = new ArrayList<CustomFileStorage>();
			List<UserCaseAttachment> old_attachments = commonService.queryUserCaseAttachmentsByCaseUuid(caseInfo.getUuid());
			boolean hasRecordZip = false;
			Integer recordAttachmentId = null;
			for(UserCaseAttachment oldAtt : old_attachments) {
				boolean has = false;
				for(ThirdOrderAttachment tatt : attachments) {
					if(tatt.getAttachmentType().equals(-1)){
						recordAttachmentId = tatt.getAttachmentId();
						hasRecordZip = true;
					}
					if(oldAtt.getType().equals(tatt.getAttachmentType())) {
						has = true;
						CustomFileStorage file = commonService.queryCustomFileStorage(tatt.getAttachmentId());
						files.add(file);
					}
				}
				if(!has) {
					if(StringUtils.isNotBlank(oldAtt.getAttachmentIds())) {
						SystemBusinessDictionary dic = commonService.querySysDicByGroupAndCode(3,oldAtt.getType());
						List<CustomFileStorage> attFiles = wenzhenService.queryCustomFilesByCaseIds(oldAtt.getAttachmentIds());
						CustomFileStorage attZip = processSingleZip(request,attFiles,dic.getDisplayName()+".zip",orderUuid);
						files.add(attZip);
						processSingleThirdAttachment(attZip.getId(),oldAtt.getType(),orderUuid);
					}
				}
			}
			if(!hasRecordZip) {
				String normals = caseInfo.getNormalImages();
				if(StringUtils.isNotBlank(normals)) {
					List<CustomFileStorage> normalFiles = wenzhenService.queryCustomFilesByCaseIds(normals);
					CustomFileStorage normalZip = processSingleZip(request,normalFiles,"入院记录.zip",orderUuid);
					files.add(normalZip);
					processSingleThirdAttachment(normalZip.getId(),-1,orderUuid);
				}
			}else {
				CustomFileStorage file = commonService.queryCustomFileStorage(recordAttachmentId);
				files.add(file);
			}
		}else {
			files = processAttachments(orderUuid, caseInfo,request);
		}
		return files;
	}
	
	private List<CustomFileStorage>  processAttachments(String orderUuid,CaseInfo caseInfo,
			HttpServletRequest request) throws Exception{
		List<CustomFileStorage> files = new ArrayList<CustomFileStorage>();
		StringBuilder sb = new StringBuilder();
		//入院记录
		String normals = caseInfo.getNormalImages();
		if(StringUtils.isNotBlank(normals)) {
			List<CustomFileStorage> normalFiles = wenzhenService.queryCustomFilesByCaseIds(normals);
			CustomFileStorage normalZip = processSingleZip(request,normalFiles,"入院记录.zip",orderUuid);
			files.add(normalZip);
			sb.append(normalZip.getId().toString());
			processSingleThirdAttachment(normalZip.getId(),-1,orderUuid);
		}
		List<UserCaseAttachment> attachments = commonService.queryUserCaseAttachmentsByCaseUuid(caseInfo.getUuid());
		if(attachments != null && attachments.size()>0) {
			for(UserCaseAttachment att : attachments) {
				if(StringUtils.isNotBlank(att.getAttachmentIds())) {
					SystemBusinessDictionary dic = commonService.querySysDicByGroupAndCode(3,att.getType());
					List<CustomFileStorage> attFiles = wenzhenService.queryCustomFilesByCaseIds(att.getAttachmentIds());
					CustomFileStorage attZip = processSingleZip(request,attFiles,dic.getDisplayName()+".zip",orderUuid);
					files.add(attZip);
					sb.append(attZip.getId().toString());
					processSingleThirdAttachment(attZip.getId(),att.getType(),orderUuid);
				}
			}
		}
		return files;
	}
	
	
	private void processSingleThirdAttachment(Integer attachmentId,Integer attachmentType,String orderUuid) {
		ThirdOrderAttachment att = new ThirdOrderAttachment();
		att.setAttachmentId(attachmentId);
		att.setAttachmentType(attachmentType);
		att.setOrderUuid(orderUuid);
		commonService.saveThirdOrderAttachment(att);
	}
	private CustomFileStorage processSingleZip(HttpServletRequest request,List<CustomFileStorage> files,String fileName,String orderUuid) throws Exception{
		String filePath = request.getSession().getServletContext()
				.getRealPath("/")
				+ "upload/"+fileName;
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(filePath));
		for(int i=0;i<files.size();i++) {
			CustomFileStorage file = files.get(i);
			URL url = new URL(file.getFileUrl());
			out.putNextEntry(new ZipEntry(file.getFileUrl().substring(file.getFileUrl().lastIndexOf("/")+1)));
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
		String retUrl = OSSManageUtil.uploadFile(filePath,sdf.format(new Date())+"/zip/"+orderUuid,new OSSConfigure(),request);
		System.out.println("====zipurl=="+retUrl);
		if (StringUtils.isNotBlank(filePath)) {
			File output = new File(filePath);
			if (output.exists()) {
				System.out.println("====删除成功么？===" + output.delete());
			}
		}
		CustomFileStorage zipFile = new CustomFileStorage();
		zipFile.setCreateTime(new Timestamp(System.currentTimeMillis()));
		zipFile.setFileName(fileName);
		zipFile.setFileType("zip");
		zipFile.setFileUrl(retUrl);
		zipFile.setStatus(1);
		commonService.saveCustomFileStorage(zipFile);
		return zipFile;
	}
	private Map<String, String> convertToMap(JSONParam[] params) {
		Map<String, String> map = new HashMap<String, String>();
		if (params != null && params.length > 0) {
			for (JSONParam jsonParam : params) {
				map.put(jsonParam.getName(), jsonParam.getValue());
			}
		}
		return map;
	}
	/**
	 * 获取医院接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/hospital/get",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> gainThirdHospitals(HttpServletRequest request) throws Exception{
		Map<String,Object> map =new HashMap<String,Object>();
		VisitBean visit = gainVisitBean(request);
		List<ThirdHospital> hospitals = InterfaceUtil.gainHospitals(visit);
		map.put("hospitals", hospitals);
		return map;
	}
	/**
	 * 根据医院获取科室
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/department/get",method=RequestMethod.GET)
	@ResponseBody 
	public Map<String,Object> gainThirdDepartments(HttpServletRequest request) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		VisitBean visit = gainVisitBean(request);
		String hospitalId = request.getParameter("hospitalId");
		List<ThirdDepartment> departments = InterfaceUtil.gainDepartments(visit,hospitalId);
		map.put("departments", departments);
		return map;
	}
	/**
	 * 获取专家数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/doctor/get",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> gainThirdDoctors(HttpServletRequest request) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String departmentId = request.getParameter("departmentId");
		VisitBean visit = gainVisitBean(request);
		List<ThirdDoctor> doctors = InterfaceUtil.gainDoctors(visit,departmentId);
		map.put("doctors",doctors);
		return map;
	}
	/**
	 * 申请会诊
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/order/submit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> submitThirdOrder(HttpServletRequest request) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		VisitBean visit = gainVisitBean(request);
		Integer orderId = Integer.parseInt(request.getParameter("orderId"));
		BusinessVedioOrder order = commonService.queryBusinessVedioById(orderId);
		MobileSpecial doc = commonService.queryMobileSpecialByUserIdAndUserType(order.getLocalDoctorId());
		CaseInfo caseInfo = commonService.queryCaseInfoById(order.getCaseId());
		String idCard = request.getParameter("idCard");
		String patientName = request.getParameter("patientName");
		String patientAge = request.getParameter("patientAge");
		String patientSex = request.getParameter("patientSex");
		caseInfo.setIdNumber(idCard);
		caseInfo.setContactName(patientName);
		caseInfo.setAge(Integer.parseInt(patientAge));
		caseInfo.setSex(Integer.parseInt(patientSex));
		String consultationTypeId = request.getParameter("consultationTypeId");//会诊方式 1：点名会诊，2非点名
		String doctorId = request.getParameter("thirdDoctorId");//第三方医生id
		String doctorName = request.getParameter("thirdDoctorName");//第三方医生姓名
		String departmentId = request.getParameter("thirdDepartmentId");//第三方医院科室id
		String departmentName = request.getParameter("thirdDepartmentName");//第三方医院科室名称
		String hospitalId = request.getParameter("thirdHospitalId");//第三方医院id
		String hospitalName = request.getParameter("thirdHospitalName");//第三方医院名称
		String emergency = request.getParameter("emergency");//是否紧急
		String attachments = request.getParameter("attachments");//附件
		String attachmentIds = request.getParameter("attachmentIds");//附件ids
		String consultationId = InterfaceUtil.submitOrder(visit, caseInfo, doc.getHosName(), doc.getDepName(), doc.getSpecialName(),
				consultationTypeId, doctorId, departmentId, hospitalId, emergency,doc.getMobileTelphone(),attachments);
		//ThirdOrderInfo 入库  状态10 申请完成
		processThirdOrderInfo(caseInfo,doc,consultationId,order.getUuid(),patientName,patientAge,patientSex,idCard,consultationTypeId,doctorId,departmentId,
				hospitalId,emergency,doctorName,departmentName,hospitalName);
		processAttachments(order.getUuid(),attachmentIds);
		return map;
	}
	
	
	private void processThirdOrderInfo(CaseInfo caseInfo,MobileSpecial doc,String consultationId,String orderUuid,String patientName,String patientAge,String patientSex,
			String idCard,String consultationTypeId,String doctorId,String departmentId,String hospitalId,String emergency,
			String doctorName,String departmentName,String hospitalName) {
		ThirdOrderInfo orderInfo = new ThirdOrderInfo();
		orderInfo.setPatientName(patientName);
		orderInfo.setPatientAge(Integer.parseInt(patientAge));
		orderInfo.setPatientSex(Integer.parseInt(patientSex));
		orderInfo.setIdCard(idCard);
		orderInfo.setPatientPhone(caseInfo.getTelephone());
		orderInfo.setConsultationTypeId(Integer.parseInt(consultationTypeId));
		orderInfo.setAppHosName(doc.getHosName());
		orderInfo.setAppDepName(doc.getDepName());
		orderInfo.setAppDocName(doc.getSpecialName());
		orderInfo.setAppDocTelphone(doc.getMobileTelphone());
		orderInfo.setDoctorId(doctorId);
		orderInfo.setDoctorName(doctorName);
		orderInfo.setDepartmentId(departmentId);
		orderInfo.setDepartmentName(departmentName);
		orderInfo.setHospitalId(hospitalId);
		orderInfo.setHospitalName(hospitalName);
		orderInfo.setEmergency(Integer.parseInt(emergency));
		orderInfo.setConsultationId(consultationId);
		orderInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		orderInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		orderInfo.setOrderUuid(orderUuid);
		orderInfo.setStatus("10");//申请完成
		orderInfo.setUuid(UUID.randomUUID().toString().replace("-", ""));
		commonService.saveThirdOrderInfo(orderInfo);
	}
	/**
	 * 处理新增的附件入库
	 * @param orderUuid
	 * @param attachmentIds
	 */
	private void processAttachments(String orderUuid,String attachmentIds) {
		List<ThirdOrderAttachment> oldAttas = commonService.queryThirdOrderAttachments(orderUuid);
		Set<Integer> remainIds = new HashSet<Integer>();
		if(StringUtils.isNotBlank(attachmentIds)) {
			String[] ids = attachmentIds.split(",");
			if(ids !=null && ids.length>0) {
				for(String id:ids) {
					if(StringUtils.isNotBlank(id)) {
						if(oldAttas ==null || oldAttas.size()<=0) {
							processSingleThirdAttachment(Integer.parseInt(id),-2, orderUuid);
						}else {
							boolean has = false;
							for(ThirdOrderAttachment att:oldAttas) {
								if(att.getAttachmentId().equals(Integer.parseInt(id))) {
									has = true;
									remainIds.add(att.getAttachmentId());
								}
							}
							if(!has) {
								processSingleThirdAttachment(Integer.parseInt(id),-2, orderUuid);
							}
						}
					}
				}
			}
		}
		 if(oldAttas != null && !oldAttas.isEmpty()){
	        for (ThirdOrderAttachment attachment : oldAttas) {
				if(!remainIds.contains(attachment.getAttachmentId())) {
					commonService.delThirdOrderAttachment(attachment.getId());
				}
			}
	     }
	}
	
	
	
	
	
	
	
	private VisitBean gainVisitBean(HttpServletRequest request) throws Exception{
		VisitBean bean = new VisitBean();
		bean.setUserId(ReaderConfigUtil.gainConfigVal(request, "third-config.xml","root/user_id"));
		bean.setPassword(ReaderConfigUtil.gainConfigVal(request, "third-config.xml","root/password"));
		bean.setVisitUrl(ReaderConfigUtil.gainConfigVal(request, "third-config.xml","root/visit_url"));
		bean.setTokenUrl(ReaderConfigUtil.gainConfigVal(request, "third-config.xml","root/token_url"));
		bean.setTokenValid(ReaderConfigUtil.gainConfigVal(request, "third-config.xml","root/token_valid"));
		return bean;
	}
}
