package com.tspeiz.modules.common.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tspeiz.modules.common.bean.HelpBean;
import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.bean.dto.BusinessD2pPrivateOrderDto;
import com.tspeiz.modules.common.bean.dto.BusinessT2pVipOrderDto;
import com.tspeiz.modules.common.bean.dto.DocFollowDto;
import com.tspeiz.modules.common.bean.dto.DoctorAboutCount;
import com.tspeiz.modules.common.bean.dto.DoctorIncomeDto;
import com.tspeiz.modules.common.bean.dto.DoctorInfoDto;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.dao.ICompanyDao;
import com.tspeiz.modules.common.dao.ICustomUploadDao;
import com.tspeiz.modules.common.dao.IExpertConsultationTimeDao;
import com.tspeiz.modules.common.dao.IImTokenInfoDao;
import com.tspeiz.modules.common.dao.IMedicalRecordsDao;
import com.tspeiz.modules.common.dao.IOrderSerialNumberDao;
import com.tspeiz.modules.common.dao.IRemoteConsultationDao;
import com.tspeiz.modules.common.dao.IServicePackageDao;
import com.tspeiz.modules.common.dao.ISystemPushInfoDao;
import com.tspeiz.modules.common.dao.ISystemServiceDefaultDao;
import com.tspeiz.modules.common.dao.ISystemServiceInfoDao;
import com.tspeiz.modules.common.dao.IUsersDao;
import com.tspeiz.modules.common.dao.IVedioRelativeDao;
import com.tspeiz.modules.common.dao.coupon.IMoneyExchangeDao;
import com.tspeiz.modules.common.dao.newrelease.IAppPcLoginDao;
import com.tspeiz.modules.common.dao.newrelease.IBaiduAudioTokenDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessDtuwenOrderDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessMessageBeanDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessPayInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessVedioOrderDao;
import com.tspeiz.modules.common.dao.newrelease.ICaseInfoDao;
import com.tspeiz.modules.common.dao.newrelease.ICustomFileStorageDao;
import com.tspeiz.modules.common.dao.newrelease.IDictionaryDao;
import com.tspeiz.modules.common.dao.newrelease.IDistCodeDao;
import com.tspeiz.modules.common.dao.newrelease.IDoctorBillInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IDoctorDetailInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IDoctorForumDao;
import com.tspeiz.modules.common.dao.newrelease.IDoctorRegisterInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IDoctorServiceInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IDoctorWithdrawInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IHistoryCaseAttachmentDao;
import com.tspeiz.modules.common.dao.newrelease.IHistoryCaseDepRelativeDao;
import com.tspeiz.modules.common.dao.newrelease.IHistoryCaseInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IHospitalDepartmentInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IHospitalDetailInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IShortUrlRelateDao;
import com.tspeiz.modules.common.dao.newrelease.ISpecialAdviceOrderDao;
import com.tspeiz.modules.common.dao.newrelease.ISystemBusinessDictionaryDao;
import com.tspeiz.modules.common.dao.newrelease.IUserAccountInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IUserCaseAttachmentDao;
import com.tspeiz.modules.common.dao.newrelease.IUserDevicesRecordDao;
import com.tspeiz.modules.common.dao.newrelease.IUserWeixinRelativeDao;
import com.tspeiz.modules.common.dao.release2.IBusinessD2dReferralOrderDao;
import com.tspeiz.modules.common.dao.release2.IBusinessD2pPrivateOrder;
import com.tspeiz.modules.common.dao.release2.IBusinessT2pTuwenOrderDao;
import com.tspeiz.modules.common.dao.release2.IDoctorConsultationOpinionDao;
import com.tspeiz.modules.common.dao.release2.IDoctorSceneEwmDao;
import com.tspeiz.modules.common.dao.release2.IDoctorScheduleShowDao;
import com.tspeiz.modules.common.dao.release2.IDoctorTeamDao;
import com.tspeiz.modules.common.dao.release2.IDoctorTeamMemberDao;
import com.tspeiz.modules.common.dao.release2.IHospitalHealthAllianceDao;
import com.tspeiz.modules.common.dao.release2.IHospitalHealthAllianceMemberDao;
import com.tspeiz.modules.common.dao.release2.IHospitalMaintainerRelationDao;
import com.tspeiz.modules.common.dao.release2.ILivePlanOrderInfoDao;
import com.tspeiz.modules.common.dao.release2.IOperatorInvitCodeDao;
import com.tspeiz.modules.common.dao.release2.IOrderBindingCodeDao;
import com.tspeiz.modules.common.dao.release2.IPlatformHealthTypeDao;
import com.tspeiz.modules.common.dao.release2.IRongCloudGroupDao;
import com.tspeiz.modules.common.dao.release2.IRongCloudGroupMemberDao;
import com.tspeiz.modules.common.dao.release2.IRongCloudGroupPostRelation;
import com.tspeiz.modules.common.dao.release2.IShufflingFigureConfigDao;
import com.tspeiz.modules.common.dao.release2.ISystemSmsRecordDao;
import com.tspeiz.modules.common.dao.release2.IThirdOrderAttachmentDao;
import com.tspeiz.modules.common.dao.release2.IThirdOrderInfoDao;
import com.tspeiz.modules.common.dao.release2.IYltApplicationRequestDao;
import com.tspeiz.modules.common.dao.release2.IYltInvitationRequestDao;
import com.tspeiz.modules.common.dao.release2.IplatformHealthConsultationDao;
import com.tspeiz.modules.common.entity.CustomUpload;
import com.tspeiz.modules.common.entity.ExpertConsultationTime;
import com.tspeiz.modules.common.entity.ImTokenInfo;
import com.tspeiz.modules.common.entity.MedicalRecords;
import com.tspeiz.modules.common.entity.OrderBindingCode;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.SystemPushInfo;
import com.tspeiz.modules.common.entity.SystemServiceDefault;
import com.tspeiz.modules.common.entity.SystemServiceInfo;
import com.tspeiz.modules.common.entity.SystemServicePackage;
import com.tspeiz.modules.common.entity.Users;
import com.tspeiz.modules.common.entity.VedioRelative;
import com.tspeiz.modules.common.entity.coupon.MoneyExchange;
import com.tspeiz.modules.common.entity.newrelease.AppPcLogin;
import com.tspeiz.modules.common.entity.newrelease.BaiduAudioToken;
import com.tspeiz.modules.common.entity.newrelease.BusinessPayInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.Dictionary;
import com.tspeiz.modules.common.entity.newrelease.DistCode;
import com.tspeiz.modules.common.entity.newrelease.DoctorBillInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorForum;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorWithdrawInfo;
import com.tspeiz.modules.common.entity.newrelease.HistoryCaseAttachment;
import com.tspeiz.modules.common.entity.newrelease.HistoryCaseDepRelative;
import com.tspeiz.modules.common.entity.newrelease.HistoryCaseInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.ShortUrlRelate;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.SystemBusinessDictionary;
import com.tspeiz.modules.common.entity.newrelease.UserDevicesRecord;
import com.tspeiz.modules.common.entity.newrelease.UserWeixinRelative;
import com.tspeiz.modules.common.entity.release2.BusinessDtuwenOrder;
import com.tspeiz.modules.common.entity.release2.DoctorConsultationOpinion;
import com.tspeiz.modules.common.entity.release2.DoctorSceneEwm;
import com.tspeiz.modules.common.entity.release2.DoctorScheduleShow;
import com.tspeiz.modules.common.entity.release2.DoctorTeam;
import com.tspeiz.modules.common.entity.release2.DoctorTeamMember;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAlliance;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAllianceMember;
import com.tspeiz.modules.common.entity.release2.HospitalMaintainerRelation;
import com.tspeiz.modules.common.entity.release2.IBusinessT2pVipOrder;
import com.tspeiz.modules.common.entity.release2.OperatorInvitCode;
import com.tspeiz.modules.common.entity.release2.PlatformHealthConsultation;
import com.tspeiz.modules.common.entity.release2.PlatformHealthType;
import com.tspeiz.modules.common.entity.release2.RongCloudGroup;
import com.tspeiz.modules.common.entity.release2.RongCloudGroupMember;
import com.tspeiz.modules.common.entity.release2.RongCloudGroupPostRelation;
import com.tspeiz.modules.common.entity.release2.ShufflingFigureConfig;
import com.tspeiz.modules.common.entity.release2.ThirdOrderAttachment;
import com.tspeiz.modules.common.entity.release2.ThirdOrderInfo;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;
import com.tspeiz.modules.common.entity.release2.YltApplicationRequest;
import com.tspeiz.modules.common.entity.release2.YltInvitationRequest;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;

@Service
public class CommonServiceImpl implements ICommonService {
	@Resource
	private IUsersDao usersDao;
	@Resource
	private IRemoteConsultationDao remoteConsultationDao;
	
	@Resource
	private ICompanyDao  companyDao;
	@Resource
	private ICustomUploadDao customUploadDao;
	
	@Resource
	private IVedioRelativeDao vedioRelativeDao;
	
	@Resource
	private IMedicalRecordsDao medicalRecordsDao;
	
	@Resource
	private IOrderSerialNumberDao orderSerialNumberDao;
	
	private SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm");

	@Resource
	private IExpertConsultationTimeDao expertConsultationTimeDao;
	@Resource
	private IUserAccountInfoDao userAccountInfoDao;
	@Resource
	private IDoctorRegisterInfoDao doctorRegisterInfoDao;
	@Resource
	private ICustomFileStorageDao customFileStorageDao;
	@Resource
	private IDoctorDetailInfoDao doctorDetailInfoDao;

	@Resource
	private IHospitalDetailInfoDao hospitalDetailInfoDao;
	@Resource
	private IHospitalDepartmentInfoDao hospitalDepartmentInfoDao;
	@Resource
	private IDistCodeDao distCodeDao;
	@Resource
	private IMoneyExchangeDao moneyExchangeDao;
	@Resource
	private IDoctorForumDao doctorForumDao;
	@Resource
	private IShortUrlRelateDao shortUrlRelateDao;
	
	@Resource
	private IBaiduAudioTokenDao baiduAudioTokenDao;
	
	@Autowired
	private IWeixinService weixinService;
	
	@Autowired
	private IBusinessPayInfoDao businessPayInfoDao;
	@Autowired
	private ICaseInfoDao caseInfoDao;
	@Autowired
	private IBusinessVedioOrderDao businessVedioOrderDao;
	
	@Autowired
	private ISpecialAdviceOrderDao specialAdviceOrderDao;
	
	@Autowired
	private IDoctorServiceInfoDao doctorServiceInfoDao;
	@Autowired
	private IAppPcLoginDao appPcLoginDao;
	@Autowired
	private IImTokenInfoDao imTokenInfoDao;
	@Autowired
	private ISystemPushInfoDao systemPushInfoDao;
	@Autowired
	private IDictionaryDao dictionaryDao;
	@Autowired
	private IDoctorWithdrawInfoDao doctorWithdrawInfoDao;
	@Autowired
	private IDoctorBillInfoDao doctorBillInfoDao;
	@Autowired
	private ISystemServiceInfoDao systemServiceInfoDao;
	@Autowired
	private IUserDevicesRecordDao userDevicesRecordDao;
	@Autowired
	private IDoctorTeamDao doctorTeamDao;
	@Autowired
	private IBusinessT2pTuwenOrderDao businessT2pTuwenOrderDao;
	@Autowired
	private ISystemServiceDefaultDao systemServiceDefaultDao;
	@Autowired
	private IDoctorScheduleShowDao doctorScheduleShowDao;
	@Autowired
	private IHospitalHealthAllianceDao hospitalHealthAllianceDao;
	@Autowired
	private IHospitalHealthAllianceMemberDao hospitalHealthAllianceMemberDao;
	@Autowired
	private IShufflingFigureConfigDao shufflingFigureConfigDao;
	@Autowired
	private IYltApplicationRequestDao yltApplicationRequestDao;
	@Autowired
	private IYltInvitationRequestDao yltInvitationRequestDao;
	@Autowired
	private IBusinessD2dReferralOrderDao businessD2dReferralOrderDao;
	@Autowired
	private IRongCloudGroupDao rongCloudGroupDao;
	@Autowired
	private IHospitalMaintainerRelationDao hospitalMaintainerRelationDao;
	@Autowired
	private IRongCloudGroupMemberDao rongCloudGroupMemberDao;
	@Autowired
	private IplatformHealthConsultationDao platformHealthConsultationDao;
	@Autowired
	private IDoctorConsultationOpinionDao doctorConsultationOpinionDao;
	@Autowired
	private IOrderBindingCodeDao orderBindingCodeDao;
	@Autowired
	private IBusinessMessageBeanDao businessMessageBeanDao;
	@Autowired
	private IUserWeixinRelativeDao userWeixinRelativeDao;
	@Autowired
	private IUserCaseAttachmentDao userCaseAttachmentDao;
	@Autowired
	private IDoctorTeamMemberDao doctorTeamMemberDao;
	@Autowired 
	private IBusinessD2pPrivateOrder businessD2pPrivateOrder;
	@Autowired
	private IBusinessT2pVipOrder businessT2pVipOrder;
	@Autowired
	private IOperatorInvitCodeDao operatorInvitCodeDao;
	@Autowired
	private IServicePackageDao servicePackageDao;
	@Autowired
	private IRongCloudGroupPostRelation rongCloudGroupPostRelation;
	@Autowired
	private ISystemBusinessDictionaryDao systemBusinessDictionaryDao;
    @Autowired
    private IBusinessDtuwenOrderDao businessDtuwenOrderDao;
    @Autowired
    private IPlatformHealthTypeDao platformHealthTypeDao;
    @Autowired
    private IDoctorSceneEwmDao doctorSceneEwmDao;
    @Autowired
    private ISystemSmsRecordDao systemSmsRecordDao;
    @Autowired
    private IThirdOrderInfoDao thirdOrderInfoDao;
    @Autowired
    private IThirdOrderAttachmentDao thirdOrderAttachmentDao;
	@Autowired
	private ILivePlanOrderInfoDao livePlanOrderInfoDao;
	@Autowired
	private IHistoryCaseInfoDao historyCaseInfoDao;
	@Autowired
	private IHistoryCaseAttachmentDao historyCaseAttachmentDao;
	@Autowired
	private IHistoryCaseDepRelativeDao historyCaseDepRelativeDao;
	
	public List<DoctorRegisterInfo> queryDoctorRegs() {
		// TODO Auto-generated method stub
		return doctorRegisterInfoDao.queryDoctorRegs();
	}

	public Users queryUsersByLogin(String username, String password,
			String stype) {
		// TODO Auto-generated method stub
		return usersDao.queryUsersByLogin(username, password, stype);
	}

	public List<RemoteConsultation> queryRemoteConsulationByExpert(
			Integer expertId) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsulationByExpert(expertId);
	}

	// 判断此时间段是否已经有订单
	public Boolean isFreeExpertConsultationTime(Integer exportId,
			String scheduleDate, String startTime, Integer timeDur) {
		// TODO Auto-generated method stub
		// 查找是否有次时间段 的订单 ，如果有的话 则此时间段不可用
		RemoteConsultation consult = remoteConsultationDao
				.queryRemoteConsulationByConditions(exportId, scheduleDate,
						startTime, timeDur);
		return consult == null ? true : false;
	}
	
	

	// 获取医生会诊时间 并拆分 时间段为 15分钟的时间片段 例如:[startTime:6:00;endTime:6:30]06:00,06:15,
	public List<ReSourceBean> queryExpertConsultationTimesByExpertId(
			Integer expertId, String time, Integer week) {
		// TODO Auto-generated method stub
		List<ExpertConsultationTime> times = expertConsultationTimeDao
				.queryExpertConsultationTimeByExpertId(expertId, week);
		Boolean b = false;
		List<ReSourceBean> rs=new ArrayList<ReSourceBean>();
		ReSourceBean rb=null;
		if (times != null && times.size() > 0) {
			for (ExpertConsultationTime _time : times) {		
				List<String> ts = splitTimeToTimeSlices(_time.getStartTime(), _time
						.getEndTime(), 15);
				for (String _st : ts) {
					rb=new ReSourceBean();
					rb.setName(_st);
					rb.setCost(_time.getCost());
				    b = isFreeExpertConsultationTime(expertId, time,_st, 15);    
				    if(b){
				    	rb.setRemark("1");
				    }else{
				    	rb.setRemark("0");
				    }
				    rs.add(rb);
				}  
			}
		}
		return rs;
	}

	private List<String> splitTimeToTimeSlices(String startTime, String endTime,
			Integer intervalM) {
		List<String> list=new ArrayList<String>();
		// 获取空闲时间段
		Date dStartTime = null;
		Date dEndTime = new Date();
		Date dLastEndTime = null;
		try {
			dStartTime = _sdf.parse(startTime);
			dLastEndTime = _sdf.parse(endTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch lock
			e.printStackTrace();
		}

		do {
			long _Time = (dStartTime.getTime() / 1000) + 60 * (intervalM - 1); // 结束时间
			dEndTime.setTime(_Time * 1000);
			// 如果长度足够的话
			if (dEndTime.before(dLastEndTime)) {
				list.add(_sdf.format(dStartTime));
				_Time = (dStartTime.getTime() / 1000) + 60 * intervalM; // 新的开始时间
				dStartTime.setTime(_Time * 1000);
			} else {
				break;
			}
		} while (true);
		return list;
	}
	
	
	public void testMongoDb(){
		
		/*Map<String,Object> pinfo=new HashMap<String,Object>();
		pinfo.put("name", "zhangsan");
		pinfo.put("id_card", "352203198407261017");
		pinfo.put("telephone", "13683270579");
		pinfo.put("register_fee", 7);
		pinfo.put("dept_id",1407);
		pinfo.put("type_name","普通挂号");
		DBObject dbobject=(DBObject)JSON.parse(PythonVisitUtil.generateJSONObject(pinfo).toString());
		MongoClient mongoClient = new MongoClient();
		DB psdoc = mongoClient.getDB("test");
	    DBCollection tr=psdoc.getCollection("test_register");
	    tr.save(dbobject);*/
	    
		//DBObject dbobject=
		/*Company company = new Company();
		company.setCompanyName("ceshi");
		company.setCreateTime(new Date());
		company.setUrl("1111111");
		company =  companyDao.mg_save(company);
		System.out.print("=====company:" + JSONObject.toJSONString(company));
		
		List<Company> companys= companyDao.mg_find(new Query());
		System.out.print("=====mg_find:" + JSONArray.toJSONString(companys));
		
		Company company1= companyDao.mg_findOne(new Query(Criteria.where("id").is(company.getId())));
		System.out.print("=====mg_findOne:" + JSONObject.toJSONString(company1));
		
		
		//companyDao.mg_deleteById(company.getId());
		List<Company> companys1= companyDao.mg_find(new Query());
		System.out.print("=====mg_find1:" + JSONArray.toJSONString(companys1));
		
		Page<Company> page = new Page<Company>();
		page.setPageNumber(1);
		page.setPageSize(2);
		
		page = companyDao.mg_findPage(page, new Query());
		System.out.print("=====mg_findPage:" + JSONObject.toJSONString(page));*/
	}

	public Integer saveCustomUpload(CustomUpload cu) {
		// TODO Auto-generated method stub
		return customUploadDao.save(cu);
	}


	public CustomUpload queryCustomUpload(Integer cuid) {
		// TODO Auto-generated method stub
		return customUploadDao.find(cuid);
	}

	public Users queryUserByMobile(String tel) {
		// TODO Auto-generated method stub
		return usersDao.queryUsersByMobileDE(tel);
	}

	public void updateUsers(Users user) {
		// TODO Auto-generated method stub
		usersDao.update(user);
	}

	public Users queryUserById(Integer userId) {
		// TODO Auto-generated method stub
		return usersDao.find(userId);
	}

	public List<RemoteConsultation> queryRemoteConsultationsByExpertIdAndProgressTag(
			Integer expertId, Integer progressTag) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsultationsByProgressTag(expertId, 2, progressTag);
	}

	public List<RemoteConsultation> queryRemoteConsultationsByDoctorIdAndProgressTag(
			Integer doctorId, Integer progressTag, Integer queryType) {
		// TODO Auto-generated method stub
		if(queryType == 0){
			MobileSpecial  mobileSpecial =queryMobileSpecialByUserIdAndUserType(doctorId) ;
			return remoteConsultationDao.queryRemoteConsultationsByConditions(mobileSpecial.getHosId(),mobileSpecial.getDepId(),progressTag);
		} else {
			return remoteConsultationDao.queryRemoteConsultationsByProgressTag(doctorId, 3, progressTag);
		}
	}


	public MobileSpecial queryMobileSpecialByDoctorId(Integer doctorId) {
		// TODO Auto-generated method stub
		return usersDao.queryMobileSpecialByDoctorId(doctorId);
	}

	public MobileSpecial queryMobileSpecialByExpertId(Integer expertId) {
		// TODO Auto-generated method stub
		return usersDao.queryMobileSpecialByExpertId(expertId);
	}

	public MobileSpecial queryMobileSpecialByUserIdAndUserType(Integer userId,
			Integer userType) {
		// TODO Auto-generated method stub
		if(userType == 2){
			return usersDao.queryMobileSpecialByExpertId(userId);
		} else if(userType == 3){
			return usersDao.queryMobileSpecialByDoctorId(userId);
		}else if(userType==4){
			return usersDao.queryMobileSpecialByNurseId(userId);
		}
		return null;
	}

	public boolean isExpertHasOrder(Integer expertId,
			Integer orderId, Integer progressTag) {
		// TODO Auto-generated method stub
		BusinessVedioOrder order=businessVedioOrderDao.find(orderId);
		System.out.print("\r\n=========isExpertHasOrder:" + JSONObject.toJSONString(order) );
		if(order != null){
			if(expertId.equals(order.getExpertId())
					&& progressTag.equals(order.getProgressTag()));{
				return true;
			}
		}
		return false;
		
	}

	public boolean isDoctorHasOrder(Integer doctorId,
			Integer orderId, Integer progressTag, Integer queryType) {
		// TODO Auto-generated method stub
		BusinessVedioOrder order=businessVedioOrderDao.find(orderId);
		System.out.print("\r\n=========isDoctorHasOrder:" + JSONObject.toJSONString(order) );
		if(queryType == 0){
			MobileSpecial  special = queryMobileSpecialByUserIdAndUserType(doctorId);
			if(special != null && order!= null){
				if(order.getLocalHospitalId().equals(special.getHosId()) 
						&& order.getLocalDepartId().equals(special.getDepId())
						&& progressTag.equals(order.getProgressTag())){
					return true;
				}
			}
			
		} else {
			if(order != null){
				if(doctorId.equals(order.getLocalDoctorId())
						&& progressTag.equals(order.getProgressTag()));{
					return true;
				}
			}		
		}
		return false;
	}

	public Integer saveVedioRelative(VedioRelative vr) {
		// TODO Auto-generated method stub
		return vedioRelativeDao.save(vr);
	}

	public Integer saveMedicalRecords(MedicalRecords mr) {
		// TODO Auto-generated method stub
		return medicalRecordsDao.save(mr);
	}

	public void updateMedicalRecords(MedicalRecords mr) {
		// TODO Auto-generated method stub
		medicalRecordsDao.update(mr);
	}

	public MedicalRecords queryMedicalRecordsById(Integer mrid) {
		// TODO Auto-generated method stub
		return medicalRecordsDao.find(mrid);
	}


	public MedicalRecords queryMedicalRecordsByOrderId(Integer oid) {
		// TODO Auto-generated method stub
		return medicalRecordsDao.queryMedicalRecordsByOrderId(oid);
	}

	public DoctorRegisterInfo queryDoctorRegisterInfo(String username,
			String password) {
		// TODO Auto-generated method stub
		return doctorRegisterInfoDao.queryDoctorRegisterInfo(username, password);
	}

	public MobileSpecial queryMobileSpecialByUserIdAndUserType(Integer user) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryMobileSpecialByUserIdAndUserType(user);
	}
	
	public MobileSpecial queryAuditDocDetailById(Integer id) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.queryAuditDocDetailById(id);
	}

	public DoctorRegisterInfo queryDoctorRegisterInfoById(Integer id) {
		// TODO Auto-generated method stub
		return doctorRegisterInfoDao.find(id);
	}

	public void updateDoctorRegisterInfo(DoctorRegisterInfo doc) {
		// TODO Auto-generated method stub
		doctorRegisterInfoDao.update(doc);
	}

	public DoctorRegisterInfo queryDoctorRegisterInfoByTel(String tel) {
		// TODO Auto-generated method stub
		return doctorRegisterInfoDao.queryDoctorRegisterInfoByTel(tel);
	}

	public CustomFileStorage queryCustomFileStorage(Integer id) {
		// TODO Auto-generated method stub
		return customFileStorageDao.find(id);
	}

	public Integer saveCustomFileStorage(CustomFileStorage cu) {
		// TODO Auto-generated method stub
		return customFileStorageDao.save(cu);
	}

	public void updateCustomFileStorage(CustomFileStorage cu) {
		// TODO Auto-generated method stub
		customFileStorageDao.update(cu);
	}

	public Integer saveDoctorRegisterInfo(DoctorRegisterInfo reg) {
		// TODO Auto-generated method stub
		return doctorRegisterInfoDao.save(reg);
	}

	public Integer saveDoctorDetailInfo(DoctorDetailInfo detail) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.save(detail);
	}

	public DoctorRegisterInfo queryDoctorRegisterInfoByTel(String tel,
			Integer utype) {
		// TODO Auto-generated method stub
		return doctorRegisterInfoDao.queryDoctorRegisterInfoByTel(tel, utype);
	}

	public HospitalDepartmentInfo getHospitalDepartmentInfoById(Integer depId) {
		// TODO Auto-generated method stub
		return hospitalDepartmentInfoDao.find(depId);
	}

	public HospitalDetailInfo getHospitalDetailInfoById(Integer hospId) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.find(hospId);
	}

	public DoctorDetailInfo queryDoctorDetailInfoById(Integer eid) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.find(eid);
	}

	public void updateDoctorDetailInfo(DoctorDetailInfo dinfo) {
		// TODO Auto-generated method stub
		doctorDetailInfoDao.update(dinfo);
	}

	public List<DistCode> queryDistCodesByConditions(String stype,
			String procost) {
		// TODO Auto-generated method stub
		return distCodeDao.queryDistCodesByConditions(stype, procost);
	}


	public List<HospitalDetailInfo> queryHospitalsByPro(String procost,String type) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryHospitalDetailsByDsitcode(procost,StringUtils.isNotBlank(type)?Integer.parseInt(type):1);
	}

	public Integer queryOrdersNumByConditions(Integer hosid,Integer depid, String begin,
			String end) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryOrdersNumByConditions(hosid,depid,begin,end);
	}

	public Integer queryUnCompletedNum(Integer hosid) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryUnCompletedNum(hosid);
	}

	public Integer queryTotalOrders(Integer hosid) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryTotalOrders(hosid);
	}

	public Integer queryOrderNumbyType(Integer hosid, Integer type) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryOrderNumbyType(hosid, type);
	}

	public DoctorRegisterInfo queryDoctorRegisterInfoByLoginName(
			String loginName) {
		// TODO Auto-generated method stub
		return doctorRegisterInfoDao.queryDoctorRegisterInfoByLoginName(loginName);
	}

	public DoctorRegisterInfo queryDoctorRegisterInfoByHosAndType(
			Integer hosid, Integer type) {
		// TODO Auto-generated method stub
		return doctorRegisterInfoDao.queryDoctorRegisterInfoByHosAndType(hosid, type);
	}

	
	public Map<String, Object> queryMoneyExchangeCodes(String searchContent,
			Integer start, Integer length, Integer type) {
		// TODO Auto-generated method stub
		return moneyExchangeDao.queryMoneyExchangeCodes(searchContent, start, length, type);
	}


	public MoneyExchange queryMoneyExchangeByConditions(String code) {
		// TODO Auto-generated method stub
		return moneyExchangeDao.queryMoneyExchangeByConditions(code);
	}


	public Integer saveMoneyExchange(MoneyExchange me) {
		// TODO Auto-generated method stub
		return moneyExchangeDao.save(me);
	}

	public DoctorForum querynewforum() {
		// TODO Auto-generated method stub
		return doctorForumDao.querynewforum();
	}

	public RemoteConsultation queryRemoteConsulationById(Integer orderId) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.find(orderId);
	}

	public void UpdateRemoteConsulation(RemoteConsultation order) {
		// TODO Auto-generated method stub
		remoteConsultationDao.update(order);
		
	}

	public ShortUrlRelate queryShortUrlRelate(String code) {
		// TODO Auto-generated method stub
		return shortUrlRelateDao.queryShortUrlRelate(code);
	}

	public ShortUrlRelate queryShortUrlRelate(String ltype, String oid) {
		// TODO Auto-generated method stub
		return shortUrlRelateDao.queryShortUrlRelate(ltype, oid);
	}


	public Integer saveShortUrlRelate(ShortUrlRelate surl) {
		// TODO Auto-generated method stub
		return shortUrlRelateDao.save(surl);
	}

	public BaiduAudioToken queryBaiduAudioTokenById(Integer id) {
		// TODO Auto-generated method stub
		return baiduAudioTokenDao.find(id);
	}

	public void updateBaiduAudioToken(BaiduAudioToken token) {
		// TODO Auto-generated method stub
		baiduAudioTokenDao.update(token);
	}

	public Integer saveBusinessPayInfo(BusinessPayInfo info) {
		// TODO Auto-generated method stub
		return businessPayInfoDao.save(info);
	}


	public BusinessPayInfo queryBusinessPayInfoByTradeNo(String tradeno) {
		// TODO Auto-generated method stub
		return businessPayInfoDao.queryBusinessPayInfoByTradeNo(tradeno);
	}


	public BusinessVedioOrder queryBusinessVedioOrderById(Integer id) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.find(id);
	}


	public void updateBusinessVedioOrder(BusinessVedioOrder order) {
		// TODO Auto-generated method stub
		businessVedioOrderDao.update(order);
	}


	public CaseInfo queryCaseInfoById(Integer id) {
		// TODO Auto-generated method stub
		return caseInfoDao.find(id);
	}

	public void updateBusinessPayInfo(BusinessPayInfo info) {
		// TODO Auto-generated method stub
		businessPayInfoDao.update(info);
	}

	
	public void updateCaseInfo(CaseInfo caseinfo) {
		// TODO Auto-generated method stub
		caseInfoDao.update(caseinfo);
	}

	public Integer saveSpecialAdviceOrder(SpecialAdviceOrder order) {
		// TODO Auto-generated method stub
		return specialAdviceOrderDao.save(order);
	}


	public List<HospitalDetailInfo> queryDocRelativeHospitals(Integer docid) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryDocRelativeHospitals(docid);
	}


	public List<StandardDepartmentInfo> queryDocRelativeDepparts(Integer docid) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryDocRelativeDepparts(docid);
	}


	public SpecialAdviceOrder querySpecialAdviceOrderById(Integer id) {
		// TODO Auto-generated method stub
		return specialAdviceOrderDao.find(id);
	}


	public void updateSpecialAdviceOrder(SpecialAdviceOrder order) {
		// TODO Auto-generated method stub
		specialAdviceOrderDao.update(order);
	}

	public Map<String, Object> querySpecialAdviceOrdersByCondition(
			Integer type, Integer userId, String search, Integer start,
			Integer length,Integer utype) {
		// TODO Auto-generated method stub
		return specialAdviceOrderDao.querySpecialAdviceOrdersByCondition(type,userId,search,start,length,utype);
	}

	public Integer getOrderSerialNumberByOrderType(Integer orderType) {
		// TODO Auto-generated method stub
		return Integer.valueOf(businessVedioOrderDao.queryFlowNumberCallPROCEDURE(orderType).toString());
	}

	public String getOrderNumberByOrderType(Integer orderType) {
		// TODO Auto-generated method stub
		return orderSerialNumberDao.getOrderNumberByOrderType(orderType);
	}

	public DoctorServiceInfo queryDoctorServiceInfoByOrderType(Integer orderType,Integer docid) {
		// TODO Auto-generated method stub
		return doctorServiceInfoDao.queryDoctorServiceInfoByOrderType(orderType,docid);
	}

	public List<DoctorDetailInfo> queryDoctorDetailsByExpert() {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.queryDoctorDetailsByExpert();
	}

	public Integer saveDoctorServiceInfo(DoctorServiceInfo info) {
		// TODO Auto-generated method stub
		return doctorServiceInfoDao.save(info);
	}


	public DoctorServiceInfo queryDoctorServiceInfoByCon(Integer docid,
			Integer serviceId, Integer packageId) {
		// TODO Auto-generated method stub
		return doctorServiceInfoDao.queryDoctorServiceInfoByCon(docid, serviceId, packageId);
	}

	public void updateDoctorServiceInfo(DoctorServiceInfo info) {
		// TODO Auto-generated method stub
		doctorServiceInfoDao.update(info);
	}

	public Integer saveAppPcLogin(AppPcLogin login) {
		// TODO Auto-generated method stub
		return appPcLoginDao.save(login);
	}

	public AppPcLogin queryAppPcLoginByKeyId(String keyid) {
		// TODO Auto-generated method stub
		return appPcLoginDao.queryAppPcLoginByKeyId(keyid);
	}


	public BusinessVedioOrder queryBusinessVedioOrderByUid(String uid) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.queryBusinessVedioOrderByUid(uid);
	}


	public Map<String,Object> queryNewAddHospital(List<String> date) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryNewAddHospital(date);
	}

	public Map<String,Object> queryNewExpertsAdd(List<String> dates) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.queryNewExpertsAdd(dates);
	}

	public Map<String, Object> queryOrdersAddCon(List<String> dates,String hosid) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.queryOrdersAddCon(dates,hosid);
	}
	public Map<String, Integer> queryOrdersAddCon(String startDate,String endDate,Integer type,String hosid) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.queryOrdersAddCon(startDate,endDate,type,hosid);
	}

	public List<ReSourceBean> queryOrderHosCal(String startDate,String endDate) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.queryOrderHosCal(startDate,endDate);
	}

	public List<ReSourceBean> orderexcal(String hosid,String startDate,String endDate) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.orderexcal(hosid,startDate,endDate);
	}


	public List<ReSourceBean> orderdepcal(String hosid,String startDate,String endDate) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.orderdepcal(hosid,startDate,endDate);
	}

	public List<DoctorServiceInfo> queryOpenDoctorServiceInfo(Integer docid,String serviceids) {
		// TODO Auto-generated method stub
		return doctorServiceInfoDao.queryOpenDoctorServiceInfo(docid,serviceids);
	}

	public List<DoctorServiceInfo> queryDoctorServiceInfoByDocId(Integer docid) {
		// TODO Auto-generated method stub
		return doctorServiceInfoDao.queryDoctorServiceInfoByDocId(docid);
	}

	public DoctorServiceInfo queryDoctorServiceInfoById(Integer id) {
		// TODO Auto-generated method stub
		return doctorServiceInfoDao.find(id);
	}

	public List<HospitalDetailInfo> queryHospitals_expert() {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryHospitals_expert();
	}

	
	public Pager queryMobileSpecial_helporder(Pager pager,Integer type) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryMobileSpecial_helporder(pager,type);
	}

	public Integer saveImTokenInfo(ImTokenInfo info) {
		// TODO Auto-generated method stub
		return imTokenInfoDao.save(info);
	}

	public ImTokenInfo queryImTokenInfoByCon(String userId, String mode) {
		// TODO Auto-generated method stub
		return imTokenInfoDao.queryImTokenInfoByCon(userId, mode);
	}

	public Integer saveSystemPushInfo(SystemPushInfo pinfo) {
		// TODO Auto-generated method stub
		return systemPushInfoDao.save(pinfo);
	}

	public List<SystemPushInfo> querySystemPushInfoByUser(Integer userId) {
		// TODO Auto-generated method stub
		return systemPushInfoDao.querySystemPushInfoByUser(userId);
	}

	
	public SystemPushInfo querySystemPushInfoByBusKey(String businessKey) {
		// TODO Auto-generated method stub
		return systemPushInfoDao.querySystemPushInfoByBusKey(businessKey);
	}
	
	public SystemPushInfo querySystemPushInfoByPushKey(String pushKey) {
		// TODO Auto-generated method stub
		return systemPushInfoDao.querySystemPushInfoByPushKey(pushKey);
	}
	
	public Map<String, Object> queryNeedAuditDocs(Integer status,String search, Integer start,
			Integer length) {
		// TODO Auto-generated method stub
		return doctorRegisterInfoDao.queryNeedAuditDocs(status,search, start, length);
	}

	public SpecialAdviceOrder querySpecialAdviceOrderByUid(String uuid) {
		// TODO Auto-generated method stub
		return specialAdviceOrderDao.querySpecialAdviceOrderByUid(uuid);
	}

	public void updateSystemPushInfo(SystemPushInfo pinfo) {
		// TODO Auto-generated method stub
		systemPushInfoDao.update(pinfo);
	}

	public List<MobileSpecial> queryExperts_wx(Integer pageNo,
			Integer pageSize, Integer depid,String hosid,String standdepid,String zc, String keywords) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryExperts_wx(pageNo, pageSize, depid,hosid,standdepid,zc, keywords);
	}

	public Map<String, Object> querySpecialAdviceOrdersByCondition_sys(
			Integer type, String search, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return specialAdviceOrderDao.querySpecialAdviceOrdersByCondition_sys(type, search, start, length);
	}

	public Dictionary queryDictionaryByCon(String displayName, Integer parentId) {
		// TODO Auto-generated method stub
		return dictionaryDao.queryDictionaryByCon(displayName, parentId);
	}

	public Map<String, Object> queryDoctorWithdrawdatas(String startDate,String endDate,String search,
			Integer start, Integer length,Integer docid,Integer ostatus,String busiType) {
		// TODO Auto-generated method stub
		return doctorWithdrawInfoDao.queryDoctorWithdrawdatas(startDate,endDate,search, start, length,docid,ostatus,busiType);
	}

	public DoctorWithdrawInfo queryDoctorWithdrawInfoById(Integer id) {
		// TODO Auto-generated method stub
		return doctorWithdrawInfoDao.queryDoctorWithdrawInfoById(id);
	}

	public DoctorWithdrawInfo queryDoctorWithdrawInfoByIdAll(Integer id) {
		// TODO Auto-generated method stub
		return doctorWithdrawInfoDao.find(id);
	}

	public void updateDoctorWithdrawInfo(DoctorWithdrawInfo info) {
		// TODO Auto-generated method stub
		doctorWithdrawInfoDao.update(info);
	}

	public Map<String, Object> querydoctorbilldatas(
			Map<String, Object> querymap, Integer start, Integer length,Integer docid) {
		// TODO Auto-generated method stub
		return doctorBillInfoDao.querydoctorbilldatas(querymap, start, length,docid);
	}

	public SystemServiceInfo querySystemServiceInfoById(Integer id) {
		// TODO Auto-generated method stub
		return systemServiceInfoDao.find(id);
	}

	public List<MobileSpecial> queryCooDocsByDep(Integer depid) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.queryCooDocsByDep(depid);
	}

	public Integer saveDoctorBillInfo(DoctorBillInfo bill) {
		// TODO Auto-generated method stub
		return doctorBillInfoDao.save(bill);
	}

	public Map<String, Object> queryexlogindatas(Map<String, Object> querymap,
			Integer start, Integer length) {
		// TODO Auto-generated method stub
		return userDevicesRecordDao.queryexlogindatas(querymap, start, length);
	}

	public DistCode queryDistCodeByCode(String code) {
		// TODO Auto-generated method stub
		return distCodeDao.queryDistCodeByCode(code);
	}

	public CaseInfo queryCaseInfoById_new(Integer caseid) {
		// TODO Auto-generated method stub
		return caseInfoDao.queryCaseInfoById_new(caseid);
	}

	public Map<String, Object> queryauditdocteamdatas(
			Map<String, Object> querymap, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return doctorTeamDao.queryauditdocteamdatas(querymap, start, length);
	}

	public DoctorTeam queryDoctorTeamById(Integer tid) {
		// TODO Auto-generated method stub
		return doctorTeamDao.find(tid);
	}

	public void updateDoctorTeam(DoctorTeam dt) {
		// TODO Auto-generated method stub
		doctorTeamDao.update(dt);
	}

	public Map<String, Object> querydocteamdatas(Map<String, Object> querymap,
			Integer start, Integer length) {
		// TODO Auto-generated method stub
		return doctorTeamDao.querydocteamdatas(querymap, start, length);
	}

	public Map<String, Object> queryt2ptuwendatas(Map<String, Object> querymap,
			Integer start, Integer length) {
		// TODO Auto-generated method stub
		return businessT2pTuwenOrderDao.queryt2ptuwendatas(querymap, start, length);
	}

	public Map<String, Object> queryserverdatas(Map<String, Object> querymap,
			Integer start, Integer length) {
		// TODO Auto-generated method stub
		return systemServiceInfoDao.queryserverdatas(querymap, start, length);
	}

	public SystemServiceDefault querySystemServiceDefaultByCon(
			Integer serviceId, Integer dutyId) {
		// TODO Auto-generated method stub
		return systemServiceDefaultDao.querySystemServiceDefaultByCon(serviceId, dutyId);
	}

	public List<DoctorScheduleShow> queryDoctorScheduleShowsByDoctorId(
			Integer docid) {
		// TODO Auto-generated method stub
		return doctorScheduleShowDao.queryDoctorScheduleShowsByDoctorId(docid);
	}

	public Integer saveDoctorScheduleShow(DoctorScheduleShow dss) {
		// TODO Auto-generated method stub
		return doctorScheduleShowDao.save(dss);
	}

	public void updateDoctorScheduleShow(DoctorScheduleShow dss) {
		// TODO Auto-generated method stub
		doctorScheduleShowDao.update(dss);
	}

	public DoctorScheduleShow isExistDoctorScheduleShowOpen(Integer docid,
			Integer weekDay, Integer outTime) {
		// TODO Auto-generated method stub
		return doctorScheduleShowDao.isExistDoctorScheduleShowOpen(docid, weekDay, outTime);
	}

	public DoctorScheduleShow queryDoctorScheduleShowById(Integer id) {
		// TODO Auto-generated method stub
		return doctorScheduleShowDao.find(id);
	}

	public void deldocscheduleshowId(Integer id) {
		// TODO Auto-generated method stub
		doctorScheduleShowDao.delete(id);
	}

	public Map<String, Object> queryhoshealthdatas(String search,
			Integer start, Integer length, String status) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceDao.queryhoshealthdatas(search, start, length, status);
	}

	public HospitalHealthAlliance queryHospitalHealthAllianceById(Integer hid) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceDao.find(hid);
	}

	public void updateHospitalHealthAlliance(HospitalHealthAlliance hha) {
		// TODO Auto-generated method stub
		hospitalHealthAllianceDao.update(hha);
	}

	public Integer saveHospitalHealthAlliance(HospitalHealthAlliance hha) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceDao.save(hha);
	}

	public List<HospitalHealthAllianceMember> queryHospitalHealAllianceMembersByCon(
			String hhaUuid,Integer level, Integer parentHosId) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceMemberDao.queryHospitalHealAllianceMembersByCon(hhaUuid,level, parentHosId);
	}

	public List<HospitalDetailInfo> queryHospitalsByCon(String status,String aids) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryHospitalsByCon(status,aids);
	}

	public Integer saveHospitalHealthAllianceMember(
			HospitalHealthAllianceMember member) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceMemberDao.save(member);
	}

	public HospitalHealthAllianceMember queryHospitalHealthAllianceMemberByCon(
			Integer hosId, String allianceUuid) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceMemberDao.queryHospitalHealthAllianceMemberByCon(hosId, allianceUuid);
	}

	public void delHospitalHealthAllianceMember(Integer mid) {
		// TODO Auto-generated method stub
		hospitalHealthAllianceMemberDao.delete(mid);
	}

	public Map<String, Object> queryoperatordatas(String search,
			Integer ostatus, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.queryoperatordatas(search, ostatus, start, length);
	}

	public List<ShufflingFigureConfig> queryShufflingFigureConfigsByType(
			Integer type) {
		// TODO Auto-generated method stub
		return shufflingFigureConfigDao.queryShufflingFigureConfigsByType(type);
	}

	public ShufflingFigureConfig queryShufflingFigureConfigById(Integer id) {
		// TODO Auto-generated method stub
		return shufflingFigureConfigDao.find(id);
	}

	public Integer saveShufflingFigureConfig(ShufflingFigureConfig fig) {
		// TODO Auto-generated method stub
		return shufflingFigureConfigDao.save(fig);
	}

	public void updateShufflingFigureConfig(ShufflingFigureConfig fig) {
		// TODO Auto-generated method stub
		shufflingFigureConfigDao.update(fig);
	}


	public void delShufflingFigureConfig(Integer id) {
		// TODO Auto-generated method stub
		shufflingFigureConfigDao.delete(id);
	}

	public Integer saveYltApplicationRequest(YltApplicationRequest yltreq) {
		// TODO Auto-generated method stub
		return yltApplicationRequestDao.save(yltreq);
	}

	public Integer saveYltInvitationRequest(YltInvitationRequest yirreq) {
		// TODO Auto-generated method stub
		return yltInvitationRequestDao.save(yirreq);
	}

	public Map<String, Object> querymyyltdatas_hos(Integer hosId,
			String search, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceMemberDao.querymyyltdatas_hos(hosId, search, start, length);
	}

	public HospitalHealthAlliance queryHospitalHealthAllianceByUuid(String uuid) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceDao.queryHospitalHealthAllianceByUuid(uuid);
	}

	public HospitalHealthAlliance queryHospitalHealthAllianceByRegId(
			Integer regId) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceDao.queryHospitalHealthAllianceByRegId(regId);
	}

	public Integer queryCountBySql(String sql) {
		// TODO Auto-generated method stub
		return dictionaryDao.queryCountBySql(sql);
	}

	public List<HospitalHealthAlliance> queryHospitalHealthAlliances() {
		// TODO Auto-generated method stub
		return (List<HospitalHealthAlliance>)hospitalHealthAllianceDao.list();
	}

	public List<HospitalDetailInfo> queryhighlevelhos(Integer allianceId,
			String levels) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryhighlevelhos(allianceId, levels);
	}

	public Map<String, Object> queryinvitjoin_hos(Integer hosId, String search,
			Integer start, Integer length,Integer otype) {
		// TODO Auto-generated method stub
		return yltInvitationRequestDao.queryinvitjoin_hos(hosId, search, start, length,otype);
	}

	public Map<String, Object> queryappjoin_hos(Integer hosId, String search,
			Integer start, Integer length,Integer otype) {
		// TODO Auto-generated method stub
		return yltApplicationRequestDao.queryappjoin_hos(hosId, search, start, length,otype);
	}

	public YltApplicationRequest queryYltApplicationRequestById(Integer id) {
		// TODO Auto-generated method stub
		return yltApplicationRequestDao.find(id);
	}

	public void updateYltApplicationRequest(YltApplicationRequest rq) {
		// TODO Auto-generated method stub
		yltApplicationRequestDao.update(rq);
	}

	public List<HospitalHealthAlliance> queryHospitalHealthAlliances(
			Integer hosId) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceDao.queryHospitalHealthAlliances(hosId);
	}

	public List<HospitalDetailInfo> queryHospitalHealthAllianceMemberByHhaId(
			String alianceUuid) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceMemberDao.queryHospitalHealthAllianceMemberByHhaId(alianceUuid);
	}

	public List<HospitalDepartmentInfo> queryHospitalDepartmentInfosByHosId(
			Integer hosId) {
		// TODO Auto-generated method stub
		return hospitalDepartmentInfoDao.queryHospitalDepartmentssByHosId(hosId);
	}

	public Map<String, Object> queryReferordersByCondition(Integer docid,
			String searchContent, Integer ostatus, Integer start, Integer length,Integer dtype) {
		// TODO Auto-generated method stub
		return businessD2dReferralOrderDao.queryReferordersByCondition(docid, searchContent, ostatus, start, length,dtype);
	}

	public DoctorTeam queryDoctorTeamByUuid(String uuid) {
		// TODO Auto-generated method stub
		return doctorTeamDao.queryDoctorTeamByUuid(uuid);
	}

	public HospitalHealthAlliance queryHospitalHealthAllianceById_new(Integer id) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceDao.queryHospitalHealthAllianceById_new(id);
	}

	public void delHospitalHealthAlliance(Integer id) {
		// TODO Auto-generated method stub
		hospitalHealthAllianceDao.delete(id);
	}

	public void updateHospitalHealthAllianceMember(
			HospitalHealthAllianceMember member) {
		// TODO Auto-generated method stub
		hospitalHealthAllianceMemberDao.update(member);
	}

	public HospitalHealthAllianceMember queryHospitalHealthAllianceMemberById(
			Integer id) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceMemberDao.find(id);
	}


	public YltInvitationRequest queryYltInvitationRequestById(Integer id) {
		// TODO Auto-generated method stub
		return yltInvitationRequestDao.find(id);
	}

	public void updateYltInvitationRequest(YltInvitationRequest yr) {
		// TODO Auto-generated method stub
		yltInvitationRequestDao.update(yr);
	}

	public Map<String, Object> querySpecialAdviceOrdersByCondition_new(
			Integer type, String searchContent, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return specialAdviceOrderDao.querySpecialAdviceOrdersByCondition_new(type, searchContent, start, length);
	}

	public DoctorTeam queryDoctorTeamById_detail(Integer tid) {
		// TODO Auto-generated method stub
		return doctorTeamDao.queryDoctorTeamById_detail(tid);
	}

	public Map<String, Object> queryDoctorTeamMemberDatas(Integer teamId,
			String searchContent, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return doctorTeamDao.queryDoctorTeamMemberDatas(teamId, searchContent,start, length);
	}

	public Map<String, Object> queryGroups(Integer ostatus,Integer groupType,
			String searchContent, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return rongCloudGroupDao.queryGroups(ostatus,groupType, searchContent, start, length);
	}

	public Map<String, Object> gainGroupMemberDatas(Integer groupId,
			String searchContent, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return rongCloudGroupDao.gainGroupMemberDatas(groupId, searchContent, start, length);
	}

	public Map<String, Object> operatorhosdatas(String searchContent,
			Integer opId, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.operatorhosdatas(searchContent, opId, start, length);
	}

	public void delHospitalMaintainerRelation(Integer id) {
		// TODO Auto-generated method stub
		hospitalMaintainerRelationDao.delete(id);
	}

	public void saveRongCloudGroup(RongCloudGroup group) {
		// TODO Auto-generated method stub
		rongCloudGroupDao.save(group);
	}

	public void saveRongCloudGroupMember(RongCloudGroupMember member) {
		// TODO Auto-generated method stub
		rongCloudGroupMemberDao.save(member);
	}

	public List<HelpBean> queryOpenPros(String type) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.queryOpenPros(type);
	}

	public List<HelpBean> queryOpenCitys(String pdist, String type,
			Integer stype) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.queryOpenCitys(pdist, type, stype);
	}

	public RongCloudGroup queryRongCloudGroupById(Integer groupId) {
		// TODO Auto-generated method stub
		return rongCloudGroupDao.find(groupId);
	}

	public void updateRongCloudGroup(RongCloudGroup group) {
		// TODO Auto-generated method stub
		rongCloudGroupDao.update(group);
	}

	public List<RongCloudGroupMember> queryRongCloudGroupMembersByGroupUuid(
			String groupUuid) {
		// TODO Auto-generated method stub
		return rongCloudGroupMemberDao.queryRongCloudGroupMembersByGroupUuid(groupUuid);
	}

	public void updateRongCloudGroupMember(RongCloudGroupMember member) {
		// TODO Auto-generated method stub
		rongCloudGroupMemberDao.update(member);
	}

	public RongCloudGroupMember queryRongCloudGroupMembersById(Integer memberId) {
		// TODO Auto-generated method stub
		return rongCloudGroupMemberDao.find(memberId);
	}

	public RongCloudGroup queryRongCloudGroupByGroupUuid(String groupUuid) {
		// TODO Auto-generated method stub
		return rongCloudGroupDao.queryRongCloudGroupByGroupUuid(groupUuid);
	}

	public void delRongCloudGroupMember(Integer memberId) {
		// TODO Auto-generated method stub
		rongCloudGroupMemberDao.delete(memberId);
	}


	public void saveHospitalMaintainerRelation(HospitalMaintainerRelation hmr) {
		// TODO Auto-generated method stub
		hospitalMaintainerRelationDao.save(hmr);
	}

	public Pager queryMobileSpecial_loaddoc(Pager pager) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryMobileSpecial_loaddoc(pager);
	}

	
	public Pager queryMobileSpecial_loadExOrdoc(Pager pager) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryMobileSpecial_loadExOrdoc(pager);
	}

	public Map<String, Object> queryVedioOrderDatas_doc(Integer docid,
			String searchContent, Integer ostatus, Integer start,
			Integer length, Integer dtype) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.queryVedioOrderDatas_doc(docid, searchContent, ostatus, start, length, dtype);
	}
	
	
	public Map<String, Object> queryVedioOrderDatas_expert(Integer docid,
			String searchContent, Integer ostatus, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.queryVedioOrderDatas_expert(docid, searchContent, ostatus, start, length);
	}

	public Map<String, Object> queryD2DTuwenDatas_doc(Integer docid,
			String searchContent, Integer ostatus, Integer start,
			Integer length, Integer dtype) {
		// TODO Auto-generated method stub
		return specialAdviceOrderDao.queryD2DTuwenDatas_doc(docid, searchContent, ostatus, start, length, dtype);
	}
	

	public Map<String, Object> queryD2DTuwenDatas_expert(Integer docid,
			String searchContent, Integer ostatus, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return specialAdviceOrderDao.queryD2DTuwenDatas_expert(docid, searchContent, ostatus, start, length);
	}

	public List<DistCode> queryAllianceAreas() {
		// TODO Auto-generated method stub
		return distCodeDao.queryAllianceAreas();
	}

	public List<HospitalHealthAlliance> querygainAllianceByArea(String distCode) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceDao.querygainAllianceByArea(distCode);
	}

	public Map<String, Object> querySystemConsultationCaseDatas(
			String searchContent, Integer ostatus, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return platformHealthConsultationDao.querySystemConsultationCaseDatas(searchContent, ostatus, start, length);
	}

	public void saveSystemConsultationCase(PlatformHealthConsultation ssc) {
		// TODO Auto-generated method stub
		platformHealthConsultationDao.save(ssc);
	}

	public PlatformHealthConsultation querySystemConsultationCaseById(Integer sscId) {
		// TODO Auto-generated method stub
		return platformHealthConsultationDao.find(sscId);
	}

	public void updateSystemConsultationCase(PlatformHealthConsultation ssc) {
		// TODO Auto-generated method stub
		platformHealthConsultationDao.update(ssc);
	}

	public List<DoctorInfoDto> loadAllianceDoctors(String allianceId,
			Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceDao.loadAllianceDoctors(allianceId, pageNo, pageSize);
	}

	public Pager queryAllianceDoctorsPager(Pager pager) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceDao.queryAllianceDoctorsPager(pager);
	}

	public Map<String, Object> queryReferordersByCondition_hos(Integer hosId,
			String search, Integer ostatus, Integer start, Integer length,
			Integer dtype) {
		// TODO Auto-generated method stub
		return businessD2dReferralOrderDao.queryReferordersByCondition_hos(hosId, search, ostatus, start, length, dtype);
	}

	public List<MobileSpecial> queryDoctorDetailInfoByHosId(Integer hosId) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.queryDoctorDetailInfoByHosId(hosId);
	}

	public Map<String, Object> queryVedioordersByCondition_hos(Integer hosId,
			String search, Integer ostatus, Integer start, Integer length,
			Integer dtype) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.queryVedioordersByCondition_hos(hosId, search, ostatus, start, length, dtype);
	}

	public List<DoctorTeamMember> queryDoctorTeamMembersByTeamId(Integer teamId) {
		// TODO Auto-generated method stub
		return doctorTeamDao.queryDoctorTeamMembersByTeamId(teamId);
	}

	public DoctorConsultationOpinion queryDoctorConsultationOpinion(Integer opId) {
		// TODO Auto-generated method stub
		return doctorConsultationOpinionDao.find(opId);
	}

	public Integer saveDoctorConsultationOpinion(DoctorConsultationOpinion op) {
		// TODO Auto-generated method stub
		return doctorConsultationOpinionDao.save(op);
	}

	public void updateDoctorConsultationOpinion(DoctorConsultationOpinion op) {
		// TODO Auto-generated method stub
		doctorConsultationOpinionDao.update(op);
	}

	public DoctorConsultationOpinion queryDoctorConsultationOpinionByUuid(
			String uuid) {
		// TODO Auto-generated method stub
		return doctorConsultationOpinionDao.queryDoctorConsultationOpinionByUuid(uuid);
	}

	public void saveOrderBindingCode(OrderBindingCode obc) {
		// TODO Auto-generated method stub
		orderBindingCodeDao.save(obc);
	}

	public List<HospitalHealthAllianceMember> queryHospitalHealthAllianceMemberByCon_main(
			String allianceUuid) {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceMemberDao.queryHospitalHealthAllianceMemberByCon_main(allianceUuid);
	}

	public List<HospitalHealthAlliance> queryHospitalHealthAlliances_audited() {
		// TODO Auto-generated method stub
		return hospitalHealthAllianceDao.queryHospitalHealthAlliances_audited();
	}

	public Integer queryUnReadMsg(String uuid, Integer otype, Integer docid) {
		// TODO Auto-generated method stub
		return businessMessageBeanDao.queryUnReadMsg(uuid, otype, docid);
	}

	public void updateMsgToRead(String uuid, Integer otype, Integer docid) {
		// TODO Auto-generated method stub
		businessMessageBeanDao.updateMsgToRead(uuid, otype, docid);
	}

	public UserWeixinRelative queryUserWeixinRelativeByCondition(String appId,
			Integer userId) {
		// TODO Auto-generated method stub
		return userWeixinRelativeDao.queryUserWeixinRelativeByCondition(appId, userId);
	}

	@Override
	public MobileSpecial getDoctorDetailList(Integer docId) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.getDoctorDetailList(docId);
	}

	public BusinessPayInfo queryBusinessPayInfoByCondition(Integer oid,
			Integer otype) {
		// TODO Auto-generated method stub
		return businessPayInfoDao.queryBusinessPayInfoByCondition(oid, otype);
	}

	public YltApplicationRequest queryYltApplicationRequestByCondition(
			String allianceUuid, Integer applicantId, Integer applicantType) {
		// TODO Auto-generated method stub
		return yltApplicationRequestDao.queryYltApplicationRequestByCondition(allianceUuid, applicantId, applicantType);
	}

	public MobileSpecial queryHosAdminMobileSpecialByHosId(Integer hosId) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryHosAdminMobileSpecialByHosId(hosId);
	}

	@Override
	public DoctorDetailInfo getLocalDocTel(Integer localDoctorId) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.getLocalDocTel(localDoctorId);
	}

	@Override
	public List<MobileSpecial> getServiceList(String hosid, String startDate, String endDate, Integer isOpenvedio, Integer isOpentuwen, String depid) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.getServiceList(hosid,startDate,endDate,isOpenvedio,isOpentuwen,depid);
	}

	@Override
	public List<SpecialAdviceOrder> gettuwenList(Integer sta,String hosid, String depid, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return specialAdviceOrderDao.gettuwenList(sta,hosid,depid,startDate,endDate);
	}

	@Override
	public List<UserDevicesRecord> getLoginList(String docName,String hosid, String depid, String lastTimes, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return userDevicesRecordDao.getLoginList(docName,hosid,depid,lastTimes,startDate,endDate);
	}

	@Override
	public void updateVedioorderRemrk(BusinessVedioOrder vodio) {
		// TODO Auto-generated method stub
		businessVedioOrderDao.update(vodio);
	}

	@Override
	public BusinessVedioOrder queryBusinessVedioById(Integer id) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.find(id);
	}

	@Override
	public UserCaseAttachment queryUserCaseAttachmentById(Integer id) {
		// TODO Auto-generated method stub
		return userCaseAttachmentDao.find(id);
	}

	@Override
	public Integer saveOrUpdateUserCaseAttachment(UserCaseAttachment attachment) {
		// TODO Auto-generated method stub
		return userCaseAttachmentDao.save(attachment);
	}

	@Override
	public void delUserCaseAttachment(Integer id) {
		// TODO Auto-generated method stub
		userCaseAttachmentDao.delete(id);
	}

	@Override
	public void delDoctorTeamMembers(Integer userId) {
		// TODO Auto-generated method stub
		doctorTeamMemberDao.delete(userId);
	}

	@Override
	public DoctorTeamMember queryDoctorTeamMembersByDocId(Integer userId) {
		// TODO Auto-generated method stub
		return doctorTeamMemberDao.queryDoctorTeamMembersByDocId(userId);
	}

	@Override
	public Map<String, Object> gainDocTeamGroupMemberDatas(Integer groupId, String searchContent, Integer start,
			Integer length) {
		return rongCloudGroupDao.gainDocTeamGroupMemberDatas(groupId, searchContent, start, length);
	}

	@Override
	public List<DoctorTeamMember> queryDoctorTeamMembers(String groupUuid) {
		// TODO Auto-generated method stub
		return doctorTeamMemberDao.queryDoctorTeamMembersByDocId(groupUuid);
	}

	@Override
	public DoctorTeam queryDoctorTeamByuuid(String groupUuid) {
		// TODO Auto-generated method stub
		return doctorTeamDao.queryDoctorTeamByuuid(groupUuid);
	}

	public Map<String,Object> queryDoctor(Map<String,Object> queryMap,Integer start,Integer length) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.queryDoctor(queryMap,start,length);
	}
	@Override
	public Map<String, Object> docprivatedatas(Integer ostatus,String searchContent, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return businessD2pPrivateOrder.docprivatedatas(ostatus,searchContent,start,length);
	}

	@Override
	public BusinessD2pPrivateOrderDto queryprivateOrdersByid(Integer id) {
		// TODO Auto-generated method stub
		return businessD2pPrivateOrder.queryprivateOrdersByid(id);
	}

	@Override
	public Map<String, Object> docteamvipdatas(Integer ostatus,String searchContent, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return businessT2pVipOrder.docteamvipdatas(ostatus,searchContent,start,length);
	}

	@Override
	public BusinessT2pVipOrderDto queryteamevipByid(Integer id) {
		// TODO Auto-generated method stub
		return businessT2pVipOrder.queryteamevipByid(id);
	}

	public OperatorInvitCode queryOperatorInvitCode(Integer docid, String code) {
		// TODO Auto-generated method stub
		return operatorInvitCodeDao.queryOperatorInvitCode(docid, code);
	}

	public void saveOperatorInvitCode(OperatorInvitCode invitCode) {
		// TODO Auto-generated method stub
		operatorInvitCodeDao.save(invitCode);
	}
	@Override
	public Map<String, Object> servicedatas(String searchContent, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return systemServiceInfoDao.servicedatas(searchContent,start,length);
	}

	@Override
	public SystemServiceInfo queryservicedatasInfo(Integer id) {
		return systemServiceInfoDao.queryservicedatasInfo(id);
	}

	@Override
	public Integer saveServiceInfo(SystemServiceInfo systemServiceInfo) {
		// TODO Auto-generated method stub
		return systemServiceInfoDao.save(systemServiceInfo);
	}

	@Override
	public void updateServiceInfo(SystemServiceInfo systemServiceInfo) {
		// TODO Auto-generated method stub
		systemServiceInfoDao.update(systemServiceInfo);
	}

	@Override
	public void updateServicePackage(Integer id, JSONArray packageArray,List<SystemServicePackage> systemServicePackages) {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < packageArray.size() ; i ++){
			SystemServicePackage systemServicePackage = new SystemServicePackage();
			JSONObject packageObj = (JSONObject) packageArray.get(i);
			for (SystemServicePackage servicePackage : systemServicePackages) {
				if(servicePackage.getId().equals(Integer.parseInt(packageObj.getString("id")))){
					systemServicePackage.setPackageName(StringUtils.isNotBlank(packageObj
							.getString("packageName"))?packageObj
							.getString("packageName"):null);
					systemServicePackage.setPackagePrice(StringUtils.isNotBlank(packageObj
							.getString("packagePrice"))?new BigDecimal(packageObj
							.getString("packagePrice")):null);
					systemServicePackage.setDescription(StringUtils.isNotBlank(packageObj
							.getString("description"))?packageObj
							.getString("description"):null);
					systemServicePackage.setStatus(StringUtils.isNotBlank(packageObj
							.getString("status"))?Integer.parseInt(packageObj
							.getString("status")):null);
					systemServicePackage.setRank(StringUtils.isNotBlank(packageObj
							.getString("rank"))?Integer.parseInt(packageObj
							.getString("rank")):null);
					systemServicePackage.setServiceId(id);
					systemServicePackage.setId(Integer.parseInt(packageObj.getString("id")));
					servicePackageDao.update(systemServicePackage);
				}
			}
		}
	}

	@Override
	public void deleteServicePackage(Integer id) {
		// TODO Auto-generated method stub
		 servicePackageDao.deleteServicePackage(id);
	}

	@Override
	public SystemServicePackage queryServicePackageByServiceId(Integer id) {
		// TODO Auto-generated method stub
		return servicePackageDao.queryServicePackageByServiceId(id);
	}

	@Override
	public RongCloudGroupPostRelation queryRongCloudGroupPostRelation(String groupUuid) {
		// TODO Auto-generated method stub
		return rongCloudGroupPostRelation.queryRongCloudGroupPostRelation(groupUuid);
	}

	@Override
	public void updateRongCloudGroupPostRelation(RongCloudGroupPostRelation grouppost) {
		// TODO Auto-generated method stub
		rongCloudGroupPostRelation.update(grouppost);
	}

	@Override
	public void delDocTeamRequest(String groupUuid) {
		// TODO Auto-generated method stub
		doctorTeamDao.delDocTeamRequest(groupUuid);
	}

	@Override
	public Map<String, Object> gaininvitDocdatas(String searchContent, Integer start, Integer length,
			String invitCode) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.gaininvitDocdatas(searchContent,start,length,invitCode);
	}

	@Override
	public SystemServiceInfo querySystemServiceById(Integer id) {
		// TODO Auto-generated method stub
		return systemServiceInfoDao.find(id);
	}

	@Override
	public List<SystemServicePackage> queryServicePackagesByServiceId(Integer serviceid) {
		// TODO Auto-generated method stub
		return servicePackageDao.queryServicePackagesByServiceId(serviceid);
	}

	@Override
	public void saveServicePackage(Integer id, JSONArray packageArray) {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < packageArray.size() ; i ++){
			SystemServicePackage systemServicePackage = new SystemServicePackage();
			JSONObject packageObj = (JSONObject) packageArray.get(i);
			systemServicePackage.setPackageName(StringUtils.isNotBlank(packageObj
					.getString("packageName"))?packageObj
					.getString("packageName"):null);
			systemServicePackage.setPackagePrice(StringUtils.isNotBlank(packageObj
					.getString("packagePrice"))?new BigDecimal(packageObj
					.getString("packagePrice")):null);
			systemServicePackage.setDescription(StringUtils.isNotBlank(packageObj
					.getString("description"))?packageObj
					.getString("description"):null);
			systemServicePackage.setStatus(StringUtils.isNotBlank(packageObj
					.getString("status"))?Integer.parseInt(packageObj
					.getString("status")):null);
			systemServicePackage.setRank(StringUtils.isNotBlank(packageObj
					.getString("rank"))?Integer.parseInt(packageObj
					.getString("rank")):null);
			systemServicePackage.setServiceId(id);
			servicePackageDao.save(systemServicePackage);
		}
	}

	@Override
	public Map<String, Object> aboutdocsdatas(Map<String, Object> querymap, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.aboutdocsdatas(querymap,start,length);
	}

	public Map<String, Object> queryNewAddDoctors(List<String> dates,String queryType) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.queryNewAddDoctors(dates,queryType);
	}

	public Map<String, Object> queryNewAddPatients(List<String> dates,String queryType) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryNewAddPatients(dates,queryType);
	}

	public List<DoctorTeam> queryDoctorTeamHasNoErweima() {
		// TODO Auto-generated method stub
		return doctorTeamDao.queryDoctorTeamHasNoErweima();
	}

	public List<SystemBusinessDictionary> querySysDicList(Integer groupId) {
		// TODO Auto-generated method stub
		return systemBusinessDictionaryDao.querySystemBusinessDictionarysByGroup(groupId);
	}

	public List<UserCaseAttachment> queryUserCaseAttachmentsByCaseUuid(String caseUuid) {
		// TODO Auto-generated method stub
		return userCaseAttachmentDao.queryUserAttachmentByCaseUuid(caseUuid);
	}
	
	@Override
	public Map<String, Object> querySpecialAdviceOrdersByCondition(Integer hosId,String search,Integer ostatus,Integer start,Integer length,Integer dtype) {
		// TODO Auto-generated method stub
		return specialAdviceOrderDao.querySpecialAdviceOrdersByCondition(hosId, search, ostatus, start, length, dtype);
	}


    @Override
    public BusinessDtuwenOrder queryBussinessTuwenOrderById(Integer tuwenId) {
        // TODO Auto-generated method stub
        return businessDtuwenOrderDao.find(tuwenId);
    }

    @Override
    public void updateBusinesstuwenOrder(BusinessDtuwenOrder order) {
        // TODO Auto-generated method stub
        businessDtuwenOrderDao.update(order);
    }

	@Override
	public void updateUserCaseAttachment(UserCaseAttachment attachment) {
		// TODO Auto-generated method stub
		userCaseAttachmentDao.update(attachment);
	}

	@Override
	public CaseInfo queryCaseInfoByUuid(String caseUuid) {
		// TODO Auto-generated method stub
		return caseInfoDao.queryCaseInfoByUuid(caseUuid);
	}

	public List<PlatformHealthType> queryUseingPlatFormHealthTypes(
			Integer status) {
		// TODO Auto-generated method stub
		return platformHealthTypeDao.queryUseingPlatFormHealthTypes(status);
	}

	@Override
	public DoctorBillInfo querydoctorbill(Integer docid) {
		// TODO Auto-generated method stub
		return doctorBillInfoDao.querydoctorbill(docid);
	}

	@Override
	public List<CaseInfo> queryCasesTransToAttachments() {
		// TODO Auto-generated method stub
		return caseInfoDao.queryCasesTransToAttachments();
	}

	@Override
	public OperatorInvitCode queryDocIdByinvitCode(String invitationCode) {
		// TODO Auto-generated method stub
		return operatorInvitCodeDao.queryDocIdByinvitCode(invitationCode);
	}

	@Override
	public List<DoctorAboutCount> gainDocAttentCount(String hosid, String depid, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.gainDocAttentCount(hosid,depid,startDate,endDate);
	}

	@Override
	public List<DoctorAboutCount> gainDocreportCount(String hosid, String depid, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.gainDocreportCount(hosid,depid,startDate,endDate);
	}

	@Override
	public Map<String, Object> querydoclogindatas(Map<String, Object> querymap, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return userDevicesRecordDao.querydoclogindatas(querymap,start,length);
	}

	@Override
	public Map<String, Object> docreportaboutdatass(Integer ostatus, String searchContent, Integer start,
			Integer length) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.docreportaboutdatass(ostatus,searchContent,start,length);
	}

	@Override
	public List<MobileSpecial> gainDocregisterexport(String hosid, String depid, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.gainDocregisterexport(hosid,depid,startDate,endDate);
	}

	@Override
	public DoctorSceneEwm queryDoctorSceneEwmByDoctorId(Integer doctorId) {
		// TODO Auto-generated method stub
		return doctorSceneEwmDao.queryDoctorSceneEwmByDoctorId(doctorId);
	}

	@Override
	public void saveDoctorSceneErweima(DoctorSceneEwm dse) {
		// TODO Auto-generated method stub
		doctorSceneEwmDao.save(dse);
	}

	@Override
	public void updateDoctorSceneErweima(DoctorSceneEwm dse) {
		// TODO Auto-generated method stub
		doctorSceneEwmDao.update(dse);
	}

	@Override
	public List<DoctorSceneEwm> queryDoctorSceneEwms_doc(String hosid,String depid,String startDate,String endDate) {
		// TODO Auto-generated method stub
		return doctorSceneEwmDao.queryDoctorSceneEwms_doc(hosid,depid,startDate,endDate);
	}

	@Override
	public Map<String, Object> querySystemSmsRecordDatas(String search,
			Integer status, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return systemSmsRecordDao.querySystemSmsRecordDatas(search, status, start, length);
	}

	@Override
	public List<DoctorIncomeDto> docincomeexport(String hid,String depid,String startDate, String endDate) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.docincomeexport(hid,depid,startDate,endDate);
	}

	@Override
	public void saveThirdOrderInfo(ThirdOrderInfo orderInfo) {
		// TODO Auto-generated method stub
		thirdOrderInfoDao.save(orderInfo);
	}

	@Override
	public ThirdOrderInfo queryThirdOrderInfoByOrderUuid(String orderUuid) {
		// TODO Auto-generated method stub
		return thirdOrderInfoDao.queryThirdOrderInfoByOrderUuid(orderUuid);
	}

	@Override
	public List<ThirdOrderAttachment> queryThirdOrderAttachments(
			String orderUuid) {
		// TODO Auto-generated method stub
		return thirdOrderAttachmentDao.queryThirdOrderAttachments(orderUuid);
	}

	@Override
	public SystemBusinessDictionary querySysDicByGroupAndCode(Integer group,
			Integer code) {
		// TODO Auto-generated method stub
		return systemBusinessDictionaryDao.querySysDicByGroupAndCode(group, code);
	}

	@Override
	public void saveThirdOrderAttachment(ThirdOrderAttachment att) {
		// TODO Auto-generated method stub
		thirdOrderAttachmentDao.save(att);
	}

	@Override
	public void delThirdOrderAttachment(Integer id) {
		// TODO Auto-generated method stub
		thirdOrderAttachmentDao.delete(id);
	}

	@Override
	public ThirdOrderInfo queryThirdOrderInfoByConsultationId(String conId) {
		// TODO Auto-generated method stub
		return thirdOrderInfoDao.queryThirdOrderInfoByConsultationId(conId);
	}

	@Override
	public void updateThirdOrderInfo(ThirdOrderInfo orderInfo) {
		// TODO Auto-generated method stub
		thirdOrderInfoDao.update(orderInfo);
	}

	@Override
	public Integer addDoctorBillInfo(DoctorBillInfo doctorBillInfo) {
		// TODO Auto-generated method stub
		return doctorBillInfoDao.save(doctorBillInfo);
	}

	@Override
	public Map<String, Object> queryDocFollowDatas(String search,
			Integer start, Integer length, Integer type) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.queryDocFollowDatas(search,start,length,type);
	}

	@Override
	public List<DocFollowDto> queryDocFollowDetailData(Integer type,
			String orderUuid, String docId, String subUserId) {
		// TODO Auto-generated method stub
		return doctorDetailInfoDao.queryDocFollowDetailData(type, orderUuid, docId, subUserId);
	}
	
    @Override
    public Map<String, Object> userWarmDocDatas(Integer ostatus,String searchContent, Integer start, Integer length) {
        // TODO Auto-generated method stub
        return doctorDetailInfoDao.userWarmDocDatas(ostatus,searchContent,start,length);
    }

	@Override
	public Map<String, Object> liveplandatass(String searchContent, Integer start, Integer length) {
		return livePlanOrderInfoDao.liveplandatass(searchContent,start,length);
	}

	@Override
	public Integer saveHistoryCaseInfo(HistoryCaseInfo historyCaseInfo) {
		// TODO Auto-generated method stub
		return historyCaseInfoDao.save(historyCaseInfo);
	}
	
	@Override
	public Integer saveHistoryCaseAttachment(
			HistoryCaseAttachment historyCaseAttachment) {
		// TODO Auto-generated method stub
		return historyCaseAttachmentDao.save(historyCaseAttachment);
	}

	@Override
	public Integer saveHistoryCaseDepRelative(
			HistoryCaseDepRelative historyCaseDepRelative) {
		// TODO Auto-generated method stub
		return historyCaseDepRelativeDao.save(historyCaseDepRelative);
	}
	

	@Override
	public HistoryCaseInfo queryHistoryCaseInfo(String historyCaseUuid) {
		// TODO Auto-generated method stub
		return historyCaseInfoDao.queryHistoryCaseInfo(historyCaseUuid);
	}

	
	@Override
	public void updateHistoryCaseInfo(HistoryCaseInfo historyCaseInfo) {
		// TODO Auto-generated method stub
		historyCaseInfoDao.update(historyCaseInfo);
	}

	@Override
	public HistoryCaseAttachment queryHistoryCaseAttachment(
			String historyCaseUuid) {
		// TODO Auto-generated method stub
		return historyCaseAttachmentDao.queryHistoryCaseAttachment(historyCaseUuid);
	}
	
	
	@Override
	public void updateHistoryCaseAttachment(
			HistoryCaseAttachment historyCaseAttachment) {
		// TODO Auto-generated method stub
		historyCaseAttachmentDao.update(historyCaseAttachment);
	}

	@Override
	public List<HistoryCaseDepRelative> queryHistoryCaseDepRelatives(
			String historyCaseUuid) {
		// TODO Auto-generated method stub
		return historyCaseDepRelativeDao.queryHistoryCaseDepRelatives(historyCaseUuid);
	}
	
	@Override
	public void delHistoryCaseDepRelative(Integer id) {
		// TODO Auto-generated method stub
		historyCaseDepRelativeDao.delete(id);	
	}

	@Override
	public Map<String, Object> queryHisCaseList(String searchContent,
			Integer start, Integer length, Integer type) {
		// TODO Auto-generated method stub
		return historyCaseInfoDao.queryHisCaseList(searchContent,start,length,type);
	}
}
