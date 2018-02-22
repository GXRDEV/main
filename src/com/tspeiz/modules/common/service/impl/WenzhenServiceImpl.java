package com.tspeiz.modules.common.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.dao.ICaseInfoAllDao;
import com.tspeiz.modules.common.dao.newrelease.IAppraisementDoctorInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IAppraisementImpressionDictionaryDao;
import com.tspeiz.modules.common.dao.newrelease.IAppraisementImpressionDoctorDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessMessageBeanDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessMessageInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessOrderInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessPayInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessTelOrderDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessTuwenOrderDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessVedioOrderDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessWenZhenInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessWenzhenTelDao;
import com.tspeiz.modules.common.dao.newrelease.ICaseImagesDao;
import com.tspeiz.modules.common.dao.newrelease.ICaseInfoDao;
import com.tspeiz.modules.common.dao.newrelease.ICustomFileStorageDao;
import com.tspeiz.modules.common.dao.newrelease.IUserAccountInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IUserBillRecordDao;
import com.tspeiz.modules.common.dao.newrelease.IUserCaseAttachmentDao;
import com.tspeiz.modules.common.dao.newrelease.IUserDevicesRecordDao;
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
import com.tspeiz.modules.common.entity.newrelease.UserAccountInfo;
import com.tspeiz.modules.common.entity.newrelease.UserBillRecord;
import com.tspeiz.modules.common.entity.newrelease.UserDevicesRecord;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;
import com.tspeiz.modules.common.service.IWenzhenService;

@Service
public class WenzhenServiceImpl implements IWenzhenService {
	@Resource
	private IBusinessWenZhenInfoDao businessWenZhenInfoDao;
	@Resource
	private IBusinessMessageInfoDao businessMessageInfoDao;
	@Resource
	private ICaseInfoDao caseInfoDao;
	@Resource
	private ICaseImagesDao caseImagesDao;
	@Resource
	private IBusinessOrderInfoDao businessOrderInfoDao;
	@Resource
	private IBusinessWenzhenTelDao businessWenzhenTelDao;
	@Resource
	private IUserAccountInfoDao userAccountInfoDao;
	@Resource
	private IUserDevicesRecordDao userDevicesRecordDao;
	@Resource
	private IAppraisementDoctorInfoDao appraisementDoctorInfoDao;
	@Resource
	private IAppraisementImpressionDictionaryDao appraisementImpressionDictionaryDao;
	@Resource
	private IAppraisementImpressionDoctorDao appraisementImpressionDoctorDao;
	@Autowired
	private IBusinessTuwenOrderDao businessTuwenOrderDao;
	@Autowired
	private IBusinessPayInfoDao businessPayInfoDao;
	
	@Autowired
	private IBusinessTelOrderDao businessTelOrderDao;
	
	private IUserBillRecordDao userBillRecordDao;
	
	@Autowired
	private IBusinessVedioOrderDao businessVedioOrderDao;
	
	@Autowired
	private IBusinessMessageBeanDao businessMessageBeanDao;
	@Autowired
	private ICustomFileStorageDao customFileStorageDao;
	
	@Autowired
	private ICaseInfoAllDao caseInfoAllDao;
	@Autowired
	private IUserCaseAttachmentDao userCaseAttachmentDao;
	

	public IBusinessWenZhenInfoDao getBusinessWenZhenInfoDao() {
		return businessWenZhenInfoDao;
	}

	public void setBusinessWenZhenInfoDao(
			IBusinessWenZhenInfoDao businessWenZhenInfoDao) {
		this.businessWenZhenInfoDao = businessWenZhenInfoDao;
	}
	
	public IUserBillRecordDao getUserBillRecordDao() {
		return userBillRecordDao;
	}

	public void setUserBillRecordDao(IUserBillRecordDao userBillRecordDao) {
		this.userBillRecordDao = userBillRecordDao;
	}

	public Map<String, Object> queryTuwenOrdersByConditions(String search,
			Integer start, Integer lenght, Integer type) {
		// TODO Auto-generated method stub
		return businessWenZhenInfoDao.queryTuwenOrdersByConditions(search, start, lenght, type);
	}

	public Map<String, Object> queryTuwenOrdersByExpertConditions(
			Integer docid, String search, Integer start, Integer lenght,
			Integer type) {
		// TODO Auto-generated method stub
		return businessWenZhenInfoDao.queryTuwenOrdersByExpertConditions(docid, search, start, lenght, type);
	}

	public List<WenzhenBean> queryOverTimeTwOrders(Integer day) {
		// TODO Auto-generated method stub
		return businessWenZhenInfoDao.queryOverTimeTwOrders(day);
	}

	public BusinessWenZhenInfo queryBusinessWenZhenInfoById(Integer id) {
		// TODO Auto-generated method stub
		return businessWenZhenInfoDao.find(id);
	}

	public void updateBusinessWenzhenInfo(BusinessWenZhenInfo info) {
		// TODO Auto-generated method stub
		businessWenZhenInfoDao.update(info);
	}

	public List<BusinessMessageInfo> queryBusinessMessagesByTwId(Integer twid) {
		// TODO Auto-generated method stub
		return businessMessageInfoDao.queryBusinessMessagesByTwId(twid);
	}


	public WenzhenBean queryBusinessTwById(Integer id) {
		// TODO Auto-generated method stub
		return businessWenZhenInfoDao.queryBusinessTwById(id);
	}

	public CaseInfo queryCaseInfoById(Integer caseid) {
		// TODO Auto-generated method stub
		return caseInfoDao.find(caseid);
	}

	public List<CaseImages> queryCaseImagesByCaseId(Integer caseId) {
		// TODO Auto-generated method stub
		return caseImagesDao.queryCaseImagesByCaseId(caseId);
	}

	public Boolean queryExistNoClosedTw(Integer uid) {
		// TODO Auto-generated method stub
		return businessWenZhenInfoDao.queryExistNoClosedTw(uid);
	}

	public List<CaseInfo> queryHisCaseInfosByUId(Integer uid) {
		// TODO Auto-generated method stub
		return caseInfoDao.queryHisCaseInfosByUId(uid);
	}

	public Integer saveCaseInfo(CaseInfo info) {
		// TODO Auto-generated method stub
		return caseInfoDao.save(info);
	}

	
	public Integer saveCaseImages(CaseImages ci) {
		// TODO Auto-generated method stub
		return caseImagesDao.save(ci);
	}

	public CaseImages queryCaseImagesById(Integer id) {
		// TODO Auto-generated method stub
		return caseImagesDao.find(id);
	}

	public void updateCaseImages(CaseImages ci) {
		// TODO Auto-generated method stub
		caseImagesDao.update(ci);
	}

	public void deleteCaseImages(Integer caseid,String ids) {
		// TODO Auto-generated method stub
		caseImagesDao.deleteCaseImages(caseid,ids);
	}

	public Boolean queryExistOrderBySevenDays(Integer docid, Integer uid) {
		// TODO Auto-generated method stub
		return businessWenZhenInfoDao.queryExistOrderBySevenDays(docid, uid);
	}

	public Integer saveBusinessWenZhenTwInfo(BusinessWenZhenInfo info) {
		// TODO Auto-generated method stub
		return businessWenZhenInfoDao.save(info);
	}

	public Integer saveBusinessOrderInfo(BusinessOrderInfo info) {
		// TODO Auto-generated method stub
		return businessOrderInfoDao.save(info);
	}

	public BusinessOrderInfo queryBusinessOrderInfoByFlowNo(String tradeNo) {
		// TODO Auto-generated method stub
		return businessOrderInfoDao.queryBusinessOrderInfoByFlowNo(tradeNo);
	}

	public void updateBusinessOrderInfo(BusinessOrderInfo info) {
		// TODO Auto-generated method stub
		businessOrderInfoDao.update(info);
	}

	public Integer saveBusinessMessageInfo(BusinessMessageInfo info) {
		// TODO Auto-generated method stub
		return businessMessageInfoDao.save(info);
	}

	public List<WenzhenBean> queryBusinessWenzhensByUId(Integer uid,
			Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return businessWenZhenInfoDao.queryBusinessWenzhensByUId(uid, pageNo, pageSize);
	}

	public List<BusinessOrderInfo> queryBusinessOrderInfosByOId(Integer oid,Integer otype) {
		// TODO Auto-generated method stub
		return businessOrderInfoDao.queryBusinessOrderInfosByOId(oid,otype);
	}


	public Integer saveBusinessWenZhenTel(BusinessWenzhenTel telinfo) {
		// TODO Auto-generated method stub
		return businessWenzhenTelDao.save(telinfo);
	}


	public void updateBusinessWenZhenTel(BusinessWenzhenTel telinfo) {
		// TODO Auto-generated method stub
		businessWenzhenTelDao.update(telinfo);
	}

	
	public BusinessWenzhenTel queryBusinessWenZhenTelById(Integer id) {
		// TODO Auto-generated method stub
		return businessWenzhenTelDao.find(id);
	}

	public Map<String, Object> querytelorders(String search, Integer start,
			Integer length,String docid,Integer ostatus) {
		// TODO Auto-generated method stub
		return businessWenzhenTelDao.querytelorders(search, start, length,docid,ostatus);
	}

	
	public List<WenzhenBean> queryBusinessWenZhenTelUid(Integer uid) {
		// TODO Auto-generated method stub
		return businessWenzhenTelDao.queryBusinessWenZhenTelUid(uid);
	}

	public Integer saveUserBillRecord(UserBillRecord bill) {
		// TODO Auto-generated method stub
		return userBillRecordDao.save(bill);
	}

	public UserDevicesRecord querySendMessageOrNot(Integer userId) {
		// TODO Auto-generated method stub
		UserAccountInfo user = userAccountInfoDao.find(userId);
		if (StringUtils.isBlank(user.getToken())) {
			return null;
		}
		UserDevicesRecord userDevicesRecord = userDevicesRecordDao
				.getLastLoginDevice(userId, 1);
		if (userDevicesRecord == null) {
			return null;
		}
		return userDevicesRecord;
	}

	public BusinessOrderInfo queryBusinessOrderInfoByOId(Integer oid,
			Integer type) {
		// TODO Auto-generated method stub
		return businessOrderInfoDao.queryBusinessOrderInfoByOId(oid, type);
	}

	public UserBillRecord queryUserBillByCondition(String content, Integer oid) {
		// TODO Auto-generated method stub
		return userBillRecordDao.queryUserBillByCondition(content,oid);
	}

	public void updateUserBillRecord(UserBillRecord bill) {
		// TODO Auto-generated method stub
		userBillRecordDao.update(bill);
	}

	public AppraisementDoctorInfo queryAppraisementDoctorInfoByConditions(
			String oid, Integer ltype) {
		// TODO Auto-generated method stub
		return appraisementDoctorInfoDao.queryAppraisementDoctorInfoByConditions(oid, ltype);
	}

	@SuppressWarnings("unchecked")
	public List<AppraisementImpressionDictionary> queryAppraisementImpressions() {
		// TODO Auto-generated method stub
		return appraisementImpressionDictionaryDao.queryAppraisementImpressions();
	}

	public Integer saveAppraisementDoctorInfo(
			AppraisementDoctorInfo appraisementDoctorInfo) {
		// TODO Auto-generated method stub
		return appraisementDoctorInfoDao.save(appraisementDoctorInfo);
	}

	public Integer saveAppraisementImpressionDoctor(
			AppraisementImpressionDoctor appraisementImpressionDoctor) {
		// TODO Auto-generated method stub
		return appraisementImpressionDoctorDao.save(appraisementImpressionDoctor);
	}

	public List<AppraisementDoctorInfo> queryAppraisementDoctorInfosByDoc(
			Integer docid, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return appraisementDoctorInfoDao.queryAppraisementDoctorInfosByDoc(docid, pageNo, pageSize);
	}

	public List<AppraisementImpressionDoctor> queryAppraisementImpressionDoctorsByDoc(
			Integer docid, Integer appraiseId) {
		// TODO Auto-generated method stub
		return appraisementImpressionDoctorDao.queryAppraisementImpressionDoctorsByDoc(docid, appraiseId);
	}

	public List<AppraisementImpressionDictionary> queryCalculateTagsNumByDoc(
			Integer docid) {
		// TODO Auto-generated method stub
		return appraisementImpressionDictionaryDao.queryCalculateTagsNumByDoc(docid);
	}

	public Integer saveBusinessTuwenOrder(BusinessTuwenOrder order) {
		// TODO Auto-generated method stub
		return businessTuwenOrderDao.save(order);
	}

	public Integer saveBusinessTelOrder(BusinessTelOrder order) {
		// TODO Auto-generated method stub
		return businessTelOrderDao.save(order);
	}


	public List<BusinessPayInfo> queryBusinesPayInfosByOId(Integer oid,
			Integer type) {
		// TODO Auto-generated method stub
		return businessPayInfoDao.queryBusinesPayInfosByOId(oid,type);
	}

	public BusinessTuwenOrder queryBusinessTuwenInfoById(Integer id) {
		// TODO Auto-generated method stub
		return businessTuwenOrderDao.find(id);
	}

	
	public BusinessTelOrder queryBusinessTelOrderById(Integer id) {
		// TODO Auto-generated method stub
		return businessTelOrderDao.find(id);
	}

	
	public BusinessVedioOrder queryBusinessVedioOrderById(Integer id) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.queryBusinessVedioOrderById(id);
	}
	public Integer saveBusinessMessageBean(BusinessMessageBean bean) {
		// TODO Auto-generated method stub
		return businessMessageBeanDao.save(bean);
	}

	
	public void updateBusinessTuwenOrder(BusinessTuwenOrder order) {
		// TODO Auto-generated method stub
		businessTuwenOrderDao.update(order);
	}

	public List<BusinessMessageBean> queryBusinessMessageBeansByCon(
			Integer oid,String orderUuid, Integer type) {
		// TODO Auto-generated method stub
		return businessMessageBeanDao.queryBusinessMessageBeansByCon(oid,orderUuid, type);
	}

	public void updateBusinessTelOrder(BusinessTelOrder order) {
		// TODO Auto-generated method stub
		businessTelOrderDao.update(order);
	}

	
	public List<CaseImages> queryCaseImagesByCaseIds(String ids) {
		// TODO Auto-generated method stub
		return caseImagesDao.queryCaseImagesByCaseIds(ids);
	}


	public boolean queryExistNoClosedTwOrder(Integer uid) {
		// TODO Auto-generated method stub
		return businessTuwenOrderDao.queryExistNoClosedTwOrder(uid);
	}

	
	public List<CustomFileStorage> queryCustomFilesByCaseIds(String ids) {
		// TODO Auto-generated method stub
		return customFileStorageDao.queryCustomFileStorageImages(ids);
	}

	public List<BusinessWenZhenInfo> queryBusinessWenzhenInfos() {
		// TODO Auto-generated method stub
		return businessWenZhenInfoDao.queryBusinessWenzhenInfos();
	}


	public CaseInfoAll queryCaseInfoAllById(Integer id) {
		// TODO Auto-generated method stub
		return caseInfoAllDao.find(id);
	}

	public List<BusinessWenzhenTel> queryBusinessWenzhenTels() {
		// TODO Auto-generated method stub
		return businessWenzhenTelDao.queryBusinessWenzhenTels();
	}

	public List<BusinessMessageInfo> queryBusinessMsgs() {
		// TODO Auto-generated method stub
		return businessMessageInfoDao.queryBusinessMsgs();
	}

	public List<BusinessMessageBean> queryBusinessMessageBeansByCon(
			Integer oid,String orderUuid, Integer type, Integer docid) {
		// TODO Auto-generated method stub
		return businessMessageBeanDao.queryBusinessMessageBeansByCon(oid,orderUuid, type, docid);
	}

	public BusinessVedioOrder queryBusinessVedioOrderByUuid(String buskey) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.queryBusinessVedioOrderByUid(buskey);
	}

	public BusinessTuwenOrder queryBusinessTuwenInfoByUid(String uuid) {
		// TODO Auto-generated method stub
		return businessTuwenOrderDao.queryBusinessTuwenInfoByUid(uuid);
	}

	public BusinessTelOrder queryBusinessTelOrderByUid(String uuid) {
		// TODO Auto-generated method stub
		return businessTelOrderDao.queryBusinessTelOrderByUid(uuid);
	}

	public List<BusinessMessageBean> queryBusinessMessageBeansByCon(
			Integer oid, Integer otype, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return businessMessageBeanDao.queryBusinessMessageBeansByCon(oid, otype, pageNo, pageSize);
	}

	public Integer queryAppraisementDoctorInfosCount(Integer docid) {
		// TODO Auto-generated method stub
		return appraisementDoctorInfoDao.queryAppraisementDoctorInfosCount(docid);
	}

	public List<UserBillRecord> gainuserbills(Integer userid, String ispage,
			Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return userBillRecordDao.gainuserbills(userid, ispage, pageNo, pageSize);
	}

	@Override
	public SpecialAdviceOrder querySpecialAdviceOrderById(Integer oid) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.querySpecialAdviceOrderById(oid);
	}

	@Override
	public List<UserCaseAttachment> queryUserAttachmentByCaseUuid(String uuid) {
		// TODO Auto-generated method stub
		return userCaseAttachmentDao.queryUserAttachmentByCaseUuid(uuid);
	}


}
