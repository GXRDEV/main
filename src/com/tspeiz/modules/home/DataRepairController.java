package com.tspeiz.modules.home;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.DBObject;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.CaseInfoAll;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessOrderInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessPayInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessTelOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenZhenInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenzhenTel;
import com.tspeiz.modules.common.entity.newrelease.CaseImages;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorServiceInfo;
import com.tspeiz.modules.common.entity.release2.DoctorSceneEwm;
import com.tspeiz.modules.common.entity.release2.DoctorTeam;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.util.ChineseToPinyinUtil;
import com.tspeiz.modules.util.IdcardUtils;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.mongodb.MongoDBManager;
import com.tspeiz.modules.util.oss.OSSManageUtil;
import com.tspeiz.modules.util.weixin.CommonUtil;

@Controller
@RequestMapping("/data")
public class DataRepairController {
	@Autowired
	private IWeixinService weixinService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IWenzhenService wenzhenService;
	/**
	 * 远程数据恢复
	 * @param request
	 */
	@RequestMapping(value="/repair")
	public void repair(HttpServletRequest request){
		//查询所有的remote中数据
		List<RemoteConsultation> remotes=weixinService.queryRemoteConsulations();
		int i=0;
		int caseid=5000;
		for (RemoteConsultation _remote : remotes) {
			//if(StringUtils.isNotBlank(_remote.getSecondConsultationDate())||StringUtils.isNotBlank(_remote.getThirdConsultationDate()))continue;
			BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(_remote.getId());
			System.out.println("====开始第"+(i++)+"条数据的恢复");
			CaseInfo caseinfo=commonService.queryCaseInfoById(order.getCaseId());
			if(_remote.getAge()==null){
				if(StringUtils.isNotBlank(_remote.getIdCard())){
					try{
					caseinfo.setAge(IdcardUtils.getAgeByIdCard(_remote.getIdCard()));
					}catch(Exception e){}
				}else{
					caseinfo.setAge(0);
				}
			}else{
				caseinfo.setAge(_remote.getAge());
			}
			if(_remote.getSex()==null){
				if(StringUtils.isNotBlank(_remote.getIdCard())){
					try{
					String _sex=IdcardUtils.getGenderByIdCard(_remote.getIdCard());
					caseinfo.setSex(_sex.equalsIgnoreCase("M")?1:0);
					}catch(Exception e){}
				}
			}else{
				caseinfo.setSex(_remote.getSex());
			}
			
			commonService.updateCaseInfo(caseinfo);
			/*MedicalRecords mr=commonService.queryMedicalRecordsByOrderId(_remote.getId());
			//病例处理
			CaseInfo caseinfo=new CaseInfo();
			caseid++;
			caseinfo.setId(caseid);
			caseinfo.setUserId(_remote.getUserId());
			caseinfo.setCreateTime(_remote.getCreateTime());
			caseinfo.setContactName(_remote.getPatientName());
			caseinfo.setIdNumber(_remote.getIdCard());
			if(_remote.getAge()==null){
				if(StringUtils.isNotBlank(_remote.getIdCard())){
					try{
					caseinfo.setAge(IdcardUtils.getAgeByIdCard(_remote.getIdCard()));
					}catch(Exception e){}
				}
			}
			if(_remote.getSex()==null){
				if(StringUtils.isNotBlank(_remote.getIdCard())){
					try{
					String _sex=IdcardUtils.getGenderByIdCard(_remote.getIdCard());
					caseinfo.setSex(_sex.equalsIgnoreCase("M")?1:0);
					}catch(Exception e){}
				}
			}
			caseinfo.setTelephone(_remote.getTelephone());
			if(mr!=null){
				caseinfo.setMainSuit(mr.getMainSuit());
				caseinfo.setHistoryIll(mr.getHistoryIll());
				caseinfo.setExamined(mr.getExamined());
				caseinfo.setAssistantResult(mr.getAssistantResult());
				caseinfo.setInitialDiagnosis(mr.getInitialDiagnosis());
				caseinfo.setTreatAdvice(mr.getTreatAdvice());
			}
			caseinfo.setNormalImages(_remote.getNormalImages());
			wenzhenService.saveCaseInfo(caseinfo);*/
			
			
			//生成订单
			/*BusinessVedioOrder order=new BusinessVedioOrder();
			order.setId(_remote.getId());
			order.setCaseId(caseid);
			order.setUserId(_remote.getUserId());
			order.setExpertId(_remote.getExpertId());
			order.setCreateTime(new Timestamp(_remote.getCreateTime().getTime()));
			order.setLocalHospitalId(_remote.getLocalHospitalId());
			order.setLocalDepartId(_remote.getLocalDepartId());
			order.setLocalDoctorId(_remote.getLocalDoctorId());
			order.setConsultationDate(_remote.getConsultationDate());
			order.setConsultationTime(_remote.getConsultationTime());
			order.setConsultationDur(_remote.getConsultationDur());
			order.setVedioSubType(_remote.getRemoteSub()==null?null:_remote.getRemoteSub());
			order.setLocalPlusRet(_remote.getRetMessage());
			order.setVedioDur(_remote.getVedioTime());
			order.setSource(_remote.getSource());
			order.setConsultationResult(_remote.getConsultationResult());
			order.setOpenId(_remote.getOpenId());
			order.setStatus(_remote.getStatus());
			order.setProgressTag(_remote.getProgressTag());
			order.setPayStatus(_remote.getPayStatus());
			order.setExLevel(_remote.getExLevel());
			order.setHelpOrder(StringUtils.isNotBlank(_remote.getHelpOrder())?Integer.parseInt(_remote.getHelpOrder()):null);
			order.setHelpOrderSelExpert(StringUtils.isNotBlank(_remote.getHelpOrderSelExpert())?Integer.parseInt(_remote.getHelpOrderSelExpert()):null);
			order.setNurseId(_remote.getNurseId());
			order.setDelFlag(_remote.getDeleFlag());
			order.setSchedulerDateId(_remote.getSchedulerDateId());
			weixinService.saveBusinessVedioOrder(order);*/
		}
		
	}

	/**
	 * 图文数据恢复
	 */
	@RequestMapping(value="/repairTw")
	public void repairTw(){
		//19620
		List<BusinessWenZhenInfo> infos=wenzhenService.queryBusinessWenzhenInfos();
		Integer i=0;
		for (BusinessWenZhenInfo _info : infos) {
			System.out.println("===============完成第"+(i++)+"条数据修复");
			//处理病例
			CaseInfoAll ocase=wenzhenService.queryCaseInfoAllById(_info.getCases_Id());
			if(ocase==null)continue;
			CaseInfo _caseinfo=wenzhenService.queryCaseInfoById(_info.getCases_Id());
			if(_caseinfo!=null)continue;
			CaseInfo caseinfo=new CaseInfo();
			caseinfo.setId(_info.getCases_Id());
			caseinfo.setUserId(ocase.getUserId());
			caseinfo.setCreateTime(ocase.getCreateTime());
			caseinfo.setContactName(ocase.getContactName());
			caseinfo.setIdNumber(ocase.getIdNumber());
			if(ocase.getSex()==null){
				if(StringUtils.isNotBlank(ocase.getIdNumber())){
					try{
					String _sex=IdcardUtils.getGenderByIdCard(ocase.getIdNumber());
					caseinfo.setSex(_sex.equalsIgnoreCase("M")?1:0);
					}catch(Exception e){}
				}else{
					caseinfo.setSex(1);
				}
			}else{
				caseinfo.setSex(ocase.getSex());
			}
			if(ocase.getAge()==null){
				if(StringUtils.isNotBlank(ocase.getIdNumber())){
					try{
					Integer age=IdcardUtils.getAgeByIdCard(ocase.getIdNumber());
					caseinfo.setAge(age);
					}catch(Exception e){}
				}else{
					caseinfo.setAge(0);
				}
			}else{
				caseinfo.setAge(ocase.getAge());
			}
			caseinfo.setTelephone(ocase.getTelephone());
			caseinfo.setCaseName(ocase.getCaseName());
			caseinfo.setKeywords(ocase.getKeywords());
			caseinfo.setDescription(ocase.getDescription());
			caseinfo.setFamilyHistory(ocase.getFamilyHistory());
			caseinfo.setMainSuit(ocase.getAskProblem());
			List<CaseImages> cimages=wenzhenService.queryCaseImagesByCaseId(ocase.getId());
			StringBuilder sb=new StringBuilder();
			for (CaseImages _img : cimages) {
				CustomFileStorage cf=new CustomFileStorage();
				String url=_img.getUrl();
				cf.setCreateTime(ocase.getCreateTime());
				cf.setFileUrl(url);
				cf.setFileName(url.substring(url.lastIndexOf("/")+1, url.length()));
				cf.setStatus(0);
				Integer cid=commonService.saveCustomFileStorage(cf);
				sb.append(cid+",");
			}
			if(sb.length()>0)sb=sb.deleteCharAt(sb.length()-1);
			caseinfo.setNormalImages(sb.toString());
			wenzhenService.saveCaseInfo(caseinfo);
			//处理订单
			BusinessTuwenOrder order=new BusinessTuwenOrder();
			order.setId(_info.getId());
			order.setCaseId(ocase.getId());
			order.setDoctorId(_info.getDoctorId());
			order.setUserId(_info.getUserId());
			if(_info.getStartTime()!=null){
				order.setCreateTime(new Timestamp(_info.getStartTime().getTime()));
			}
			if(_info.getClosedTime()!=null){
				order.setClosedTime(new Timestamp(_info.getClosedTime().getTime()));
			}
			order.setCloserType(_info.getUserType());
			order.setCloserId(_info.getCloserId());
			if(_info.getSpecialistFirstAnswerTime()!=null){
				order.setDocFirstAnswerTime(new Timestamp(_info.getSpecialistFirstAnswerTime().getTime()));
			}
			if(_info.getSpecialistLastAnswerTime()!=null){
				order.setDocLastAnswerTime(new Timestamp(_info.getSpecialistLastAnswerTime().getTime()));
			}
			if(_info.getPatientLastAnswerTime()!=null){
				order.setDocLastAnswerTime(new Timestamp(_info.getPatientLastAnswerTime().getTime()));
			}
			order.setDocSendMsgCount(_info.getDocTalkingNumber());
			order.setPatRecvMsgCount(_info.getPatTalkingNumber());
			order.setDocUnreadMsgNum(0);
			order.setPatUnreadMsgNum(0);
			if(StringUtils.isNotBlank(_info.getOrigin())){
				if(_info.getOrigin().equalsIgnoreCase("Android")){
					order.setSource(2);
				}else if(_info.getOrigin().equalsIgnoreCase("佰医汇微信公众号")){
					order.setSource(3);
				}else if(_info.getOrigin().equalsIgnoreCase("iOS")){
					order.setSource(1);
				}else if(_info.getOrigin().equalsIgnoreCase("佰医汇官网")){
					order.setSource(4);
				}
			}
			order.setOpenId(_info.getOpenId());
			order.setStatus(_info.getAskStatus());
			order.setPayStatus(_info.getPayStatus());
			order.setDelFlag(0);
			wenzhenService.saveBusinessTuwenOrder(order);
			//处理支付订单
			List<BusinessOrderInfo> payinfos=wenzhenService.queryBusinessOrderInfosByOId(_info.getId(), 1);
			for (BusinessOrderInfo _pay : payinfos) {
				BusinessPayInfo payinfo=new BusinessPayInfo();
				payinfo.setId(_pay.getId());
				payinfo.setOrderType(1);
				payinfo.setOrderId(_pay.getWenZhen_Id());
				payinfo.setCreateTime(new Timestamp(_pay.getCreateTime().getTime()));
				payinfo.setTotalMoney(_pay.getAmount());
				payinfo.setPayMode(_pay.getPayMode());
				payinfo.setOnlinePay(_pay.getAmount());
				payinfo.setOutTradeNo(_pay.getFlowNumber());
				payinfo.setPayStatus(_pay.getPayStatus());
				if(_pay.getRefundTime()!=null){
					payinfo.setRefundTime(new Timestamp(_pay.getRefundTime().getTime()));
				}
				commonService.saveBusinessPayInfo(payinfo);
			}
		}
	}
	
	/**
	 * 电话数据恢复
	 */
	@RequestMapping(value="/repairTel")
	public void repairTel(){
		List<BusinessWenzhenTel> infos=wenzhenService.queryBusinessWenzhenTels();
		Integer i=0;
		for (BusinessWenzhenTel _tel : infos) {
			System.out.println("===============完成第"+(i++)+"条数据修复");
			//处理病例
			CaseInfoAll ocase=wenzhenService.queryCaseInfoAllById(_tel.getCaseId());
			if(ocase==null)continue;
			CaseInfo _caseinfo=wenzhenService.queryCaseInfoById(_tel.getCaseId());
			if(_caseinfo!=null)continue;
			CaseInfo caseinfo=new CaseInfo();
			caseinfo.setId(_tel.getCaseId());
			caseinfo.setUserId(ocase.getUserId());
			caseinfo.setCreateTime(ocase.getCreateTime());
			caseinfo.setContactName(ocase.getContactName());
			caseinfo.setIdNumber(ocase.getIdNumber());
			if(ocase.getSex()==null){
				if(StringUtils.isNotBlank(ocase.getIdNumber())){
					try{
					String _sex=IdcardUtils.getGenderByIdCard(ocase.getIdNumber());
					caseinfo.setSex(_sex.equalsIgnoreCase("M")?1:0);
					}catch(Exception e){}
				}else{
					caseinfo.setSex(1);
				}
			}else{
				caseinfo.setSex(ocase.getSex());
			}
			if(ocase.getAge()==null){
				if(StringUtils.isNotBlank(ocase.getIdNumber())){
					try{
					Integer age=IdcardUtils.getAgeByIdCard(ocase.getIdNumber());
					caseinfo.setAge(age);
					}catch(Exception e){}
				}else{
					caseinfo.setAge(0);
				}
			}else{
				caseinfo.setAge(ocase.getAge());
			}
			caseinfo.setTelephone(ocase.getTelephone());
			caseinfo.setCaseName(ocase.getCaseName());
			caseinfo.setKeywords(ocase.getKeywords());
			caseinfo.setDescription(ocase.getDescription());
			caseinfo.setFamilyHistory(ocase.getFamilyHistory());
			caseinfo.setMainSuit(ocase.getAskProblem());
			List<CaseImages> cimages=wenzhenService.queryCaseImagesByCaseId(ocase.getId());
			StringBuilder sb=new StringBuilder();
			for (CaseImages _img : cimages) {
				CustomFileStorage cf=new CustomFileStorage();
				String url=_img.getUrl();
				cf.setCreateTime(ocase.getCreateTime());
				cf.setFileUrl(url);
				cf.setFileName(url.substring(url.lastIndexOf("/")+1, url.length()));
				cf.setStatus(0);
				Integer cid=commonService.saveCustomFileStorage(cf);
				sb.append(cid+",");
			}
			if(sb.length()>0)sb=sb.deleteCharAt(sb.length()-1);
			caseinfo.setNormalImages(sb.toString());
			wenzhenService.saveCaseInfo(caseinfo);
			
			//处理订单
			BusinessTelOrder order=new BusinessTelOrder();
			order.setId(_tel.getId());
			order.setCaseId(_tel.getCaseId());
			order.setDoctorId(_tel.getDoctorId());
			order.setUserId(_tel.getUserId());
			order.setCreateTime(new Timestamp(_tel.getCreateTime().getTime()));
			order.setOrderDur(_tel.getPayTelTime());
			order.setCloserId(_tel.getCloserId());
			order.setCloserType(_tel.getUtype());
			order.setOpenId(_tel.getOpenId());
			if(StringUtils.isNotBlank(_tel.getOrigin())){
				if(_tel.getOrigin().equalsIgnoreCase("Android")){
					order.setSource(2);
				}else if(_tel.getOrigin().equalsIgnoreCase("佰医汇微信公众号")){
					order.setSource(3);
				}else if(_tel.getOrigin().equalsIgnoreCase("iOS")){
					order.setSource(1);
				}else if(_tel.getOrigin().equalsIgnoreCase("佰医汇官网")){
					order.setSource(4);
				}
			}
			order.setStatus(_tel.getStatus());
			order.setPayStatus(_tel.getPayStatus());
			order.setDelFlag(0);
			wenzhenService.saveBusinessTelOrder(order);
			
			//处理支付订单
			List<BusinessOrderInfo> payinfos=wenzhenService.queryBusinessOrderInfosByOId(_tel.getId(), 2);
			for (BusinessOrderInfo _pay : payinfos) {
				BusinessPayInfo payinfo=new BusinessPayInfo();
				payinfo.setId(_pay.getId());
				payinfo.setOrderType(1);
				payinfo.setOrderId(_pay.getWenZhen_Id());
				payinfo.setCreateTime(new Timestamp(_pay.getCreateTime().getTime()));
				payinfo.setTotalMoney(_pay.getAmount());
				payinfo.setPayMode(_pay.getPayMode());
				payinfo.setOnlinePay(_pay.getAmount());
				payinfo.setOutTradeNo(_pay.getFlowNumber());
				payinfo.setPayStatus(_pay.getPayStatus());
				if(_pay.getRefundTime()!=null){
					payinfo.setRefundTime(new Timestamp(_pay.getRefundTime().getTime()));
				}
				commonService.saveBusinessPayInfo(payinfo);
			}
		}
	}
	
	/**
	 *消息数据恢复
	 */
	@RequestMapping(value="/repairMsg")
	public void repairMsg(){ 
		List<BusinessMessageInfo> msgs=wenzhenService.queryBusinessMsgs();
		Integer i=0;
		for (BusinessMessageInfo _info : msgs) {
			System.out.println("===完成第"+(i++)+"条数据");
			BusinessMessageBean bean=new BusinessMessageBean();
			bean.setId(_info.getId());
			bean.setOrderId(_info.getWenZhen_Id());
			bean.setOrderType(1);
			bean.setMsgType(_info.getMsgType());
			bean.setMsgContent(_info.getMsgContent());
			bean.setFileUrl(_info.getPath());
			bean.setRecvId(_info.getToId());
			bean.setRecvType(_info.getToUserType());
			bean.setSendId(_info.getFromId());
			bean.setSendType(_info.getFromUserType());
			bean.setSendTime(new Timestamp(_info.getSendTime().getTime()));
			bean.setStatus(_info.getSendStatus());
			wenzhenService.saveBusinessMessageBean(bean);
		}
	}

	/**
	 * 医生服务信息
	 */
	@RequestMapping(value="/repairDoctorServiceInfo")
	public void repairDoctorServiceInfo(){
		List<DoctorDetailInfo> docs=commonService.queryDoctorDetailsByExpert();
		Integer i=0;
		for (DoctorDetailInfo _doc : docs) {
			System.out.println("==============处理第"+(i++)+"条数据");
			Integer docid=_doc.getId();
			/*BigDecimal vedioAmout=_doc.getVedioAmount();
			BigDecimal askAmout=_doc.getAskAmount();
			BigDecimal telAmout=_doc.getTelAmount();
			Integer vopen=(_doc.getOpenVedio()==null?0:(_doc.getOpenVedio().equals(1)?1:0));
			Integer aopen=(_doc.getOpenAsk()==null?0:(_doc.getOpenAsk().equals(1)?1:0));
			Integer topen=(_doc.getOpenTel()==null?0:(_doc.getOpenTel().equals(1)?1:0));
			//处理远程
			saveDoctorServiceInfo(docid,4,3,vopen,vedioAmout);
			//处理图文及专家咨询，暂放一致
			saveDoctorServiceInfo(docid,1,1,aopen,askAmout);
			saveDoctorServiceInfo(docid,5,5,aopen,askAmout);
			//处理电话
			saveDoctorServiceInfo(docid,2,2,topen,telAmout);*/
		}
	}
	
	public void saveDoctorServiceInfo(Integer doctorId,Integer serviceId,Integer packageId,Integer isOpen,BigDecimal amount){
		//判断是否存在数据
		DoctorServiceInfo ds=commonService.queryDoctorServiceInfoByCon(doctorId,serviceId,packageId);
		if(ds==null){
			ds=new DoctorServiceInfo();
			ds.setDoctorId(doctorId);
			ds.setServiceId(serviceId);
			ds.setPackageId(packageId);
			ds.setIsOpen(isOpen);
			ds.setAmount(amount);
			commonService.saveDoctorServiceInfo(ds);
		}
	}
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
	@RequestMapping(value="/erweima")
	public void generateErweima() throws Exception{
		List<DoctorRegisterInfo> docs=commonService.queryDoctorRegs();
		Integer i=0;
		if(docs!=null&&docs.size()>0){
			for (DoctorRegisterInfo reg : docs) {
				System.out.println("===执行开始==="+(i++)+":"+docs.size());
				System.out.println("==="+reg.getId());
				DoctorDetailInfo detail=commonService.queryDoctorDetailInfoById(reg.getId());
				if(detail!=null){
					DoctorSceneEwm dse = commonService.queryDoctorSceneEwmByDoctorId(detail.getId());
					if(dse == null) {
						//新增 doctorSceneEwm
						dse =new DoctorSceneEwm();
						WeiAccessToken wat = weixinService.queryWeiAccessTokenById("wx4ee3ae2857ad1e18");
						String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + wat.getAccessToken();
						String json = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" + "docinfo_"+detail.getId() + "\"}}}";
						JSONObject obj =  CommonUtil.httpRequest(url, "POST", json);
						dse.setDoctorId(detail.getId());
						dse.setRealUrl(obj.getString("url"));
						dse.setErweimaUrl("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+obj.getString("ticket"));
						commonService.saveDoctorSceneErweima(dse);
					}else {
						if(!StringUtils.isNotBlank(dse.getRealUrl())) {
							//修改二维码
							WeiAccessToken wat = weixinService.queryWeiAccessTokenById("wx4ee3ae2857ad1e18");
							String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + wat.getAccessToken();
							String json = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" + "docinfo_"+detail.getId() + "\"}}}";
							JSONObject obj =  CommonUtil.httpRequest(url, "POST", json);
							dse.setRealUrl(obj.getString("url"));
							dse.setErweimaUrl("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+obj.getString("ticket"));
							commonService.updateDoctorSceneErweima(dse);
						}
					}
				}	
				System.out.println("===执行结束===");
			}
		}
		
	}
	/**
	 * 二维码打包
	 * @throws Exception
	 */
	@RequestMapping(value="/zipFile")
	public void zipFile() throws Exception{
		/*String zipFileName = "C:\\Users\\kx\\Desktop\\上饶市人民医院2.zip";
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		BufferedOutputStream bos = new BufferedOutputStream(out);
		List<DoctorSceneEwm> list = commonService.queryDoctorSceneEwms_doc();
		for(int i=0;i<list.size();i++) {
			System.out.println("===执行开始==="+i+":"+list.size());
			DoctorSceneEwm dse = list.get(i);
			Integer doctorId = dse.getDoctorId();
			if(doctorId ==-1)continue;
			System.out.println("==docid:"+doctorId);
			MobileSpecial doc = commonService.queryMobileSpecialByUserIdAndUserType(doctorId);
			if(doc.getHosId()!=null && doc.getHosId().equals(175)){
				URL url = new URL(dse.getErweimaUrl());
				out.putNextEntry(new ZipEntry(doc.getHosName().trim()+"/"+doc.getDepName().trim()+"/"+doc.getSpecialName().trim() +(StringUtils.isNotBlank(doc.getDuty())?
						"-"+doc.getDuty():"")+".jpg"));
				InputStream fis = url.openConnection().getInputStream();
				byte[] buffer = new byte[1024];
				int r = 0;
				while ((r = fis.read(buffer)) != -1) {
					out.write(buffer, 0, r);
				}
				fis.close();
			}
		}
		System.out.println("===结束");
		out.flush();
		out.close();*/
	}
	/*@RequestMapping(value="/erweimaupdate")
	public void zipFile() throws Exception{
		List<DoctorSceneEwm> list = commonService.queryDoctorSceneEwms_doc();
		for(int i=0;i<list.size();i++) {
			System.out.println("===执行开始==="+i+":"+list.size());
			DoctorSceneEwm dse = list.get(i);
			Integer doctorId = dse.getDoctorId();
			if(doctorId ==-1)continue;
			System.out.println("==docid:"+doctorId);
			DoctorDetailInfo detail = commonService.queryDoctorDetailInfoById(doctorId);
			detail.setErweimaUrl(dse.getErweimaUrl());
			commonService.updateDoctorDetailInfo(detail);
		}
	}*/
	@RequestMapping(value="/t2pErweima")
	public void generateT2pErweima() throws Exception{
		List<DoctorTeam> teams = commonService.queryDoctorTeamHasNoErweima();
		for (DoctorTeam doctorTeam : teams) {
			String url=PropertiesUtil.getString("DOMAINURL")+"module/patient.html#/teaminfo/" + doctorTeam.getId();//进入主界面
			String erweimaUrl = OSSManageUtil.genErweima(
					ChineseToPinyinUtil.getPingYin(doctorTeam.getTeamName()),
					url);
			DoctorTeam _team = commonService.queryDoctorTeamById(doctorTeam.getId());
			_team.setErweimaUrl(erweimaUrl);
			commonService.updateDoctorTeam(_team);
		}
	}
	/**
	 * dcm中的caseId 替换为caseUUid
	 * @throws Exception
	 */
	@RequestMapping(value="/dcm")
	@ResponseBody
	public void dcm() throws Exception {
		MongoDBManager ma = MongoDBManager.getInstance();
		Map<String,Object> map=new HashMap<String,Object>();
		List<DBObject> objs=ma.findAll("pacs_tb");
		for(DBObject obj:objs) {
			JSONObject json = JSONObject.fromObject(obj);
			Object caseid = json.get("admin_case_id");
			if(caseid != null) {
				if(StringUtils.isNumeric(caseid.toString())) {
					Integer caseId = Integer.parseInt(caseid.toString());
					CaseInfo caseInfo = commonService.queryCaseInfoById(caseId);
					obj.put("admin_case_id", caseInfo.getUuid());
					ma.insertupdate("pacs_tb", obj);
				}
			}
		}
	}
	
	
	/**
	 * 批量修改 原user_case_info中 的 病历附件
	 */
	@RequestMapping(value="/caseAttachmentProcess")
	@ResponseBody
	public void caseAttachmentProcess() throws Exception{
		//查询nomals不为空的或者检查报告或者影像图片不为空，转换为病例附件
		List<CaseInfo> cases = commonService.queryCasesTransToAttachments();
		if(!cases.isEmpty()) {
			for(CaseInfo info :cases) {
				System.out.println("====执行中====");
				if(StringUtils.isNotBlank(info.getUuid())){
					info.setUuid(UUID.randomUUID().toString().replace("-", ""));
					
				}
				//处理nomals
				if(StringUtils.isNotBlank(info.getNormalImages())) {
					generateCaseAttachment(info,info.getNormalImages(),99);
					info.setNormalImages(null);
				}
				
				//处理检查报告
				if(StringUtils.isNotBlank(info.getCheckReportImages())) {
					generateCaseAttachment(info,info.getCheckReportImages(),6);
					info.setCheckReportImages(null);
				}
				//处理影像图片
				if(StringUtils.isNotBlank(info.getRadiographFilmImags())) {
					generateCaseAttachment(info,info.getRadiographFilmImags(),7);
					info.setRadiographFilmImags(null);
				}
				commonService.updateCaseInfo(info);
			}
			System.out.println("====执行完成====");
		}
	}
	private void generateCaseAttachment(CaseInfo info,String attachmentIds,Integer type) throws Exception {
		UserCaseAttachment userCaseAttachment = new UserCaseAttachment();
		userCaseAttachment.setAttachmentIds(attachmentIds);
		userCaseAttachment.setCaseUuid(info.getUuid());
		if(info.getCreateTime()!=null) {
			userCaseAttachment.setCreateTime(new Timestamp(info.getCreateTime().getTime()));
			userCaseAttachment.setReportTime(new Timestamp(sdf.parse(sdf.format(info.getCreateTime())).getTime()));
		}
		userCaseAttachment.setType(type);
		userCaseAttachment.setUuid(UUID.randomUUID().toString().replace("-", ""));
		commonService.saveOrUpdateUserCaseAttachment(userCaseAttachment);
	}
}

