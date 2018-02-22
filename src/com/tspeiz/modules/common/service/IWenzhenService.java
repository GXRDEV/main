package com.tspeiz.modules.common.service;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.entity.CaseInfoAll;
import com.tspeiz.modules.common.entity.SystemServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.AppraisementDoctorInfo;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDictionary;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDoctor;
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
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.newrelease.UserBillRecord;
import com.tspeiz.modules.common.entity.newrelease.UserDevicesRecord;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;

public interface IWenzhenService {
	public Map<String, Object> queryTuwenOrdersByConditions(String search,
			Integer start, Integer lenght, Integer type);
	public Map<String, Object> queryTuwenOrdersByExpertConditions(Integer docid,String search,
			Integer start, Integer lenght, Integer type);
	public List<WenzhenBean> queryOverTimeTwOrders(Integer day);
	public BusinessWenZhenInfo queryBusinessWenZhenInfoById(Integer id);
	public void updateBusinessWenzhenInfo(BusinessWenZhenInfo info);
	
	public List<BusinessMessageInfo> queryBusinessMessagesByTwId(Integer twid);
	
	public WenzhenBean queryBusinessTwById(Integer id);
	
	public CaseInfo queryCaseInfoById(Integer caseid);
	
	public List<CaseImages> queryCaseImagesByCaseId(Integer caseId);
	
	public Boolean queryExistNoClosedTw(Integer uid);
	public List<CaseInfo> queryHisCaseInfosByUId(Integer uid);
	public Integer saveCaseInfo(CaseInfo info);
	
	public Integer saveCaseImages(CaseImages ci);
	
	public CaseImages queryCaseImagesById(Integer id);
	
	public void updateCaseImages(CaseImages ci);
	
	public void deleteCaseImages(Integer caseid,String ids);
	
	public Boolean queryExistOrderBySevenDays(Integer docid,Integer uid);
	
	public Integer saveBusinessWenZhenTwInfo(BusinessWenZhenInfo info);
	
	public Integer saveBusinessOrderInfo(BusinessOrderInfo info);
	
	public BusinessOrderInfo queryBusinessOrderInfoByFlowNo(String tradeNo);
	
	public void updateBusinessOrderInfo(BusinessOrderInfo info);
	
	public Integer saveBusinessMessageInfo(BusinessMessageInfo info);
	
	public List<WenzhenBean> queryBusinessWenzhensByUId(Integer uid,Integer pageNo,Integer pageSize);
	
	public List<BusinessOrderInfo> queryBusinessOrderInfosByOId(Integer oid,Integer otype);
	
	public Integer saveBusinessWenZhenTel(BusinessWenzhenTel telinfo);
	
	public void updateBusinessWenZhenTel(BusinessWenzhenTel telinfo);
	public BusinessWenzhenTel queryBusinessWenZhenTelById(Integer id);
	
	public Map<String,Object> querytelorders(String search,Integer start,Integer length,String docid,Integer ostatus);
	
	public List<WenzhenBean> queryBusinessWenZhenTelUid(Integer uid);
	
	public Integer saveUserBillRecord(UserBillRecord bill);
	
	public UserDevicesRecord querySendMessageOrNot(Integer userId);
	
	public BusinessOrderInfo queryBusinessOrderInfoByOId(Integer oid,Integer type);
	
	public UserBillRecord queryUserBillByCondition(String content,Integer oid);
	
	public void updateUserBillRecord(UserBillRecord bill);
	
	public AppraisementDoctorInfo queryAppraisementDoctorInfoByConditions(String oid,Integer ltype);
	
	public List<AppraisementImpressionDictionary> queryAppraisementImpressions();
	
	public Integer saveAppraisementDoctorInfo(AppraisementDoctorInfo appraisementDoctorInfo);
	
	public Integer saveAppraisementImpressionDoctor(AppraisementImpressionDoctor appraisementImpressionDoctor);
	
	public List<AppraisementDoctorInfo> queryAppraisementDoctorInfosByDoc(Integer docid,Integer pageNo,Integer pageSize);
	
	public Integer queryAppraisementDoctorInfosCount(Integer docid);
	
	public List<AppraisementImpressionDoctor> queryAppraisementImpressionDoctorsByDoc(Integer docid,Integer appraiseId);
	
	public List<AppraisementImpressionDictionary> queryCalculateTagsNumByDoc(Integer docid);
	
	public Integer saveBusinessTuwenOrder(BusinessTuwenOrder order);
	
	public Integer saveBusinessTelOrder(BusinessTelOrder order);
	
	public List<BusinessPayInfo> queryBusinesPayInfosByOId(Integer oid,Integer type);
	
	public BusinessTuwenOrder queryBusinessTuwenInfoById(Integer id);
	
	public BusinessTelOrder queryBusinessTelOrderById(Integer id);
	
	public BusinessVedioOrder queryBusinessVedioOrderById(Integer id);
	
	public Integer saveBusinessMessageBean(BusinessMessageBean bean);
	
	public void updateBusinessTuwenOrder(BusinessTuwenOrder order);
	
	public List<BusinessMessageBean> queryBusinessMessageBeansByCon(Integer oid,String orderUuid,Integer type);
	
	public void updateBusinessTelOrder(BusinessTelOrder order);
	public List<CustomFileStorage> queryCustomFilesByCaseIds(String ids);
	
	public boolean queryExistNoClosedTwOrder(Integer uid);
	
	public List<BusinessWenZhenInfo> queryBusinessWenzhenInfos();
	
	public CaseInfoAll queryCaseInfoAllById(Integer id);
	
	public List<BusinessWenzhenTel> queryBusinessWenzhenTels();
	public List<BusinessMessageInfo> queryBusinessMsgs();
	public List<BusinessMessageBean> queryBusinessMessageBeansByCon(Integer oid,String orderUuid,Integer type,Integer docid);
	public BusinessVedioOrder queryBusinessVedioOrderByUuid(String buskey);
	public BusinessTuwenOrder queryBusinessTuwenInfoByUid(String uuid);
	public BusinessTelOrder queryBusinessTelOrderByUid(String uuid);
	
	public List<BusinessMessageBean> queryBusinessMessageBeansByCon(Integer oid,Integer otype,Integer pageNo,Integer pageSize);
	public List<UserBillRecord> gainuserbills(Integer userid,String ispage,Integer pageNo,Integer pageSize);
	public SpecialAdviceOrder querySpecialAdviceOrderById(Integer oid);
	public List<UserCaseAttachment> queryUserAttachmentByCaseUuid(String uuid);
}
