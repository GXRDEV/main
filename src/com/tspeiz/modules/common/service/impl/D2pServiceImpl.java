package com.tspeiz.modules.common.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.bean.AdviceBean;
import com.tspeiz.modules.common.bean.D2pOrderBean;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.newrelease.IBusinessMessageBeanDao;
import com.tspeiz.modules.common.dao.newrelease.IDistCodeDao;
import com.tspeiz.modules.common.dao.newrelease.IDoctorDetailInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IHospitalDetailInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IStandardDepartmentInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IUserBillRecordDao;
import com.tspeiz.modules.common.dao.release2.IBusinessD2dReferralOrderDao;
import com.tspeiz.modules.common.dao.release2.IBusinessD2pConsultationRequestDao;
import com.tspeiz.modules.common.dao.release2.IBusinessD2pFastaskOrderDao;
import com.tspeiz.modules.common.dao.release2.IBusinessD2pReportOrderDao;
import com.tspeiz.modules.common.dao.release2.IBusinessD2pTelOrderDao;
import com.tspeiz.modules.common.dao.release2.IBusinessD2pTuwenOrderDao;
import com.tspeiz.modules.common.dao.release2.IBusinessT2pTuwenOrderDao;
import com.tspeiz.modules.common.dao.release2.ISystemWarmthInfoDao;
import com.tspeiz.modules.common.dao.release2.IUserMedicalRecordDao;
import com.tspeiz.modules.common.dao.release2.IUserWarmthInfoDao;
import com.tspeiz.modules.common.entity.newrelease.DistCode;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.UserBillRecord;
import com.tspeiz.modules.common.entity.release2.BusinessD2dReferralOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pConsultationRequest;
import com.tspeiz.modules.common.entity.release2.BusinessD2pFastaskOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pReportOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pTelOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pTuwenOrder;
import com.tspeiz.modules.common.entity.release2.BusinessT2pTuwenOrder;
import com.tspeiz.modules.common.entity.release2.SystemWarmthInfo;
import com.tspeiz.modules.common.entity.release2.UserMedicalRecord;
import com.tspeiz.modules.common.entity.release2.UserWarmthInfo;
import com.tspeiz.modules.common.service.ID2pService;

@Service
public class D2pServiceImpl implements ID2pService{
	@Autowired
	private IBusinessD2pFastaskOrderDao businessD2pFastaskOrderDao;
	@Autowired
	private IBusinessD2pTelOrderDao businessD2pTelOrderDao;
	@Autowired
	private IBusinessD2pTuwenOrderDao businessD2pTuwenOrderDao;
	@Autowired
	private IBusinessD2pReportOrderDao businessD2pReportOrderDao;
	@Autowired
	private ISystemWarmthInfoDao systemWarmthInfoDao;
	@Autowired
	private IUserWarmthInfoDao userWarmthInfoDao;
	@Autowired
	private IUserMedicalRecordDao userMedicalRecordDao;
	@Autowired
	private IDoctorDetailInfoDao doctorDetailInfoDao;
	@Autowired
	private IBusinessMessageBeanDao businessMessageBeanDao;
	@Autowired
	private IStandardDepartmentInfoDao standardDepartmentInfoDao;
	@Autowired
	private IHospitalDetailInfoDao hospitalDetailInfoDao;
	@Autowired
	private IDistCodeDao distCodeDao;
	@Autowired
	private IBusinessD2pConsultationRequestDao businessD2pConsultationRequestDao;
	@Autowired
	private IBusinessD2dReferralOrderDao businessD2pReferralOrderDao;
	@Autowired
	private IBusinessT2pTuwenOrderDao businessT2pTuwenOrderDao;

	public Map<String, Object> queryd2pteldatas(Map<String, Object> querymap,
			Integer start, Integer length) {
		// TODO Auto-generated method stub
		return businessD2pTelOrderDao.queryd2pteldatas(querymap,start,length);
	}

	public Map<String, Object> queryd2ptuwendatas(Map<String, Object> querymap,
			Integer start, Integer length) {
		// TODO Auto-generated method stub
		return businessD2pTuwenOrderDao.queryd2ptuwendatas(querymap,start,length);
	}

	public Map<String, Object> queryd2pfastaskdatas(
			Map<String, Object> querymap, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return businessD2pFastaskOrderDao.queryd2pfastaskdatas(querymap,start,length);
	}

	public Map<String, Object> queryd2preportdatas(
			Map<String, Object> querymap, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return businessD2pReportOrderDao.queryd2preportdatas(querymap,start,length);
	}

	public Map<String, Object> queryd2pconreqdatas(
			Map<String, Object> querymap, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return businessD2pConsultationRequestDao.queryd2pconreqdatas(querymap, start, length);
	}

	public D2pOrderBean queryOrderDetailInfo(Integer oid, Integer otype) {
		// TODO Auto-generated method stub
		return businessD2pTelOrderDao.queryOrderDetailInfo(oid,otype);
	}

	public Integer saveBusinessD2pFastAskOrder(BusinessD2pFastaskOrder forder) {
		// TODO Auto-generated method stub
		return businessD2pFastaskOrderDao.save(forder);
	}

	public BusinessD2pFastaskOrder queryd2pfastaskorderbyid(Integer oid) {
		// TODO Auto-generated method stub
		return businessD2pFastaskOrderDao.find(oid);
	}

	public void updated2pfastaskorder(BusinessD2pFastaskOrder order) {
		// TODO Auto-generated method stub
		businessD2pFastaskOrderDao.update(order);
	}

	public Integer saveBusinessPatientReportOrder(BusinessD2pReportOrder order) {
		// TODO Auto-generated method stub
		return businessD2pReportOrderDao.save(order);
	}

	public List<BusinessD2pReportOrder> queryd2preportordersbyuserid(
			Integer userid) {
		// TODO Auto-generated method stub
		return businessD2pReportOrderDao.queryd2preportordersbyuserid(userid);
	}

	public BusinessD2pReportOrder queryd2preportorderbyconditions(
			String subUserUuid, Integer docid) {
		// TODO Auto-generated method stub
		return businessD2pReportOrderDao.queryd2preportorderbyconditions(subUserUuid, docid);
	}

	public List<SystemWarmthInfo> querysystemwarms() {
		// TODO Auto-generated method stub
		return systemWarmthInfoDao.querysystemwarms();
	}

	public Integer saveUserWarmthInfo(UserWarmthInfo uw) {
		// TODO Auto-generated method stub
		return userWarmthInfoDao.save(uw);
	}

	public UserWarmthInfo queryuserwarminfo(Integer id) {
		// TODO Auto-generated method stub
		return userWarmthInfoDao.find(id);
	}

	public void updateuserwarminfo(UserWarmthInfo uw) {
		// TODO Auto-generated method stub
		userWarmthInfoDao.update(uw);
	}

	public List<D2pOrderBean> querymyorders(Integer userid, String ltype,
			Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return businessD2pTelOrderDao.querymyorders(userid, ltype, pageNo, pageSize);
	}

	public List<UserMedicalRecord> queryusermedicalrecords(String subUserUuid) {
		// TODO Auto-generated method stub
		return userMedicalRecordDao.queryusermedicalrecords(subUserUuid);
	}

	public Integer saveUserMedicalRecord(UserMedicalRecord record) {
		// TODO Auto-generated method stub
		return userMedicalRecordDao.save(record);
	}

	public Integer querydoctorwarms(Integer docid) {
		// TODO Auto-generated method stub
		return userWarmthInfoDao.querydoctorwarms(docid);
	}

	public BusinessD2pReportOrder queryd2preportorderbyid(Integer id) {
		// TODO Auto-generated method stub
		return businessD2pReportOrderDao.find(id);
	}

	public BusinessD2pTuwenOrder queryd2ptuwenorderbyid(Integer id) {
		// TODO Auto-generated method stub
		return businessD2pTuwenOrderDao.find(id);
	}

	public BusinessD2pTelOrder queryd2ptelorderbyid(Integer id) {
		// TODO Auto-generated method stub
		return businessD2pTelOrderDao.find(id);
	}

	public List<MobileSpecial> querydoctors(String search, String serviceid,
			String depid, String distcode, Integer _pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.querydoctors(search, serviceid, depid, distcode, _pageNo, pageSize);
	}

	public List<AdviceBean> newlyadvices(Integer userId) {
		// TODO Auto-generated method stub
		return businessMessageBeanDao.newlyadvices(userId);
	}

	public Integer saveBusinessD2pTelOrder(BusinessD2pTelOrder order) {
		// TODO Auto-generated method stub
		return businessD2pTelOrderDao.save(order);
	}

	public Integer saveBusinessD2pTuwenOrder(BusinessD2pTuwenOrder order) {
		// TODO Auto-generated method stub
		return businessD2pTuwenOrderDao.save(order);
	}

	public List<StandardDepartmentInfo> querystanddeps(String ispage,
			String ishot, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return standardDepartmentInfoDao.querystanddeps(ispage, ishot, pageNo, pageSize);
	}

	public List<HospitalDetailInfo> querynearhoses(String ispage, String htype,
			String distcode, Integer _pageNo, Integer _pageSize) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.querynearhoses(ispage, htype, distcode, _pageNo, _pageSize);
	}

	public void updated2ptuwenorder(BusinessD2pTuwenOrder order) {
		// TODO Auto-generated method stub
		businessD2pTuwenOrderDao.update(order);
	}

	public void updated2ptelorder(BusinessD2pTelOrder order) {
		// TODO Auto-generated method stub
		businessD2pTelOrderDao.update(order);
	}

	public List<UserWarmthInfo> querydoctorwarms_list(Integer docid,
			Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return userWarmthInfoDao.querydoctorwarms_list(docid, pageNo, pageSize);
	}

	public List<DistCode> gainArea(String type, String parentCode) {
		// TODO Auto-generated method stub
		return distCodeDao.gainArea(type, parentCode);
	}

	public BusinessD2pConsultationRequest queryd2pconreqorderbyid(Integer oid) {
		// TODO Auto-generated method stub
		return businessD2pConsultationRequestDao.find(oid);
	}

	public void updated2pconreqorder(BusinessD2pConsultationRequest order) {
		// TODO Auto-generated method stub
		businessD2pConsultationRequestDao.update(order);
	}

	public Map<String, Object> queryd2preferdatas(Map<String, Object> querymap,
			Integer start, Integer length) {
		// TODO Auto-generated method stub
		return businessD2pReferralOrderDao.queryd2preferdatas(querymap, start, length);
	}

	public BusinessT2pTuwenOrder querybusinesst2ptuwenById(Integer id) {
		// TODO Auto-generated method stub
		return businessT2pTuwenOrderDao.find(id);
	}

	public void updatet2ptuwen(BusinessT2pTuwenOrder order) {
		// TODO Auto-generated method stub
		businessT2pTuwenOrderDao.update(order);
	}

	public BusinessD2dReferralOrder queryd2dreferralOrderbyId(Integer id) {
		// TODO Auto-generated method stub
		return businessD2pReferralOrderDao.find(id);
	}

	public void updated2dreferralOrder(BusinessD2dReferralOrder order) {
		// TODO Auto-generated method stub
		businessD2pReferralOrderDao.update(order);
	}

	public Integer saveBusinessD2dReferralOrder(BusinessD2dReferralOrder order) {
		// TODO Auto-generated method stub
		return businessD2pReferralOrderDao.save(order);
	}

	public void updateBusinessD2dReferralOrder(BusinessD2dReferralOrder order) {
		// TODO Auto-generated method stub
		businessD2pReferralOrderDao.update(order);
	}

	public BusinessD2dReferralOrder queryBusinessD2pReferralOrderByUuid(
			String orderUuid) {
		// TODO Auto-generated method stub
		return businessD2pReferralOrderDao.queryBusinessD2pReferralOrderByUuid(orderUuid);
	}

	@Override
	public MobileSpecial queryBusinessD2dReferralOrderByUserId(Integer docid) {
		// TODO Auto-generated method stub
		return businessD2pReferralOrderDao.queryBusinessD2dReferralOrderByUserId(docid);
	}
	
	
}
