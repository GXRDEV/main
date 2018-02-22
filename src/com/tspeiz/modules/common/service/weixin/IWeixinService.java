package com.tspeiz.modules.common.service.weixin;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.bean.weixin.DepartString;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.entity.BigDepartment;
import com.tspeiz.modules.common.entity.BusinessOperativeInfo;
import com.tspeiz.modules.common.entity.CooHospitalDepartments;
import com.tspeiz.modules.common.entity.CooHospitalDetails;
import com.tspeiz.modules.common.entity.CustomUpload;
import com.tspeiz.modules.common.entity.Departments;
import com.tspeiz.modules.common.entity.ExpertConsultationSchedule;
import com.tspeiz.modules.common.entity.ExpertConsultationTime;
import com.tspeiz.modules.common.entity.FaceDiagnoses;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.SpecialistAppoint;
import com.tspeiz.modules.common.entity.Users;
import com.tspeiz.modules.common.entity.VedioRelative;
import com.tspeiz.modules.common.entity.newrelease.*;
import com.tspeiz.modules.common.entity.weixin.Cases;
import com.tspeiz.modules.common.entity.weixin.CasesImage;
import com.tspeiz.modules.common.entity.weixin.ContactInfo;
import com.tspeiz.modules.common.entity.weixin.DoctorEstimateInfo;
import com.tspeiz.modules.common.entity.weixin.GraphicsInfo;
import com.tspeiz.modules.common.entity.weixin.Orders;
import com.tspeiz.modules.common.entity.weixin.Patients;
import com.tspeiz.modules.common.entity.weixin.UserExternals;
import com.tspeiz.modules.common.entity.weixin.UserFeedback;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;

public interface IWeixinService {
	public WeiAccessToken queryWeiAccessTokenById(String appId);

	public void updateWeiAccessToken(WeiAccessToken at);

	// 根据externalId查询关注用户
	public UserExternals queryUserExternalByExternalId(String externalId);

	// 更新关注用户---关注或取消关注
	public void updateUserExternal(UserExternals ue);

	// 新增users
	public Integer saveUsers(Users user);

	// 新增userexternals --关注用户
	public Integer saveUserExternal(UserExternals ue);

	// 查询佰医汇中的专家，可以根据科室过滤
	public List<MobileSpecial> queryMobileSpecialsByConditions(Integer depid,String scontent,String scity,String spro,
			Integer pageNo, Integer pageSize,String type);
	// 获取所有科室
	public List<Departments> queryDepartments();

	// 获取所有大科室
	public List<BigDepartment> queryBigDepartments();

	// 根据大科室获取相应科室
	public List<Departments> queryDepartmentByBigDep(Integer bigid);

	//获取合作意向的医院（含城市)
	public List<CooHospitalDetails> queryAllCooHospitalDetails();
	//根据城市获取合作医院
	public CooHospitalDetails queryCooHospitalDetailsByCity(String city);
	//查看专家详情
	public MobileSpecial queryMobileSpecialById(Integer sid);
	//新增远程会诊订单
	public Integer saveRemoteConsultation(RemoteConsultation rc);
	//更新远程会诊订单
	public void updateRemoteConsultation(RemoteConsultation rc);
	public List<DepartString> queryDepartStrings(Integer sid);
	
	//获取我的订单
	public List<RemoteConsultation> queryRemoteConsultationsByConditions(String openid,Integer pageNo,Integer pageSize,String stas);
	//查询远程会诊订单
	public RemoteConsultation queryRemoteConsultationById(Integer oid);
	public RemoteConsultation queryRemoteConsulationsById_detail(Integer id);
	/**
	 * 根据专家  
	 * @param xpertId
	 * @param cooHosId
	 * @return
	 */
	public List<MobileSpecial> queryMobileSpecialsByCurLocationAndExpert(Integer expertId,Integer cooHosId);
	
	public CooHospitalDetails queryCooHosPitalDetailsById(Integer coid);
	
	public RemoteConsultation queryRemoteConsultationByTradeNo(String tradeNo);
	
	//新增用户反馈
	public Integer saveUserFeedback(UserFeedback fb);
	public List<CooHospitalDepartments> queryCooHospitalDepartmentsByHospitalAndExpert(Integer expertId,Integer cosid);
	
	public CooHospitalDepartments queryCooHospitalDepartmentsById(Integer coodepid);
	
	//根据id查找Users
	public Users queryUsersById(Integer uid);
	public Users queryUsersByMobilePhone(String phone);
	
	public List<ExpertConsultationTime> queryExpertConTimes(Integer uid);
	
	public ExpertConsultationTime queryExpertConTimeById(Integer tid);
	
	public Integer saveExpertConTime(ExpertConsultationTime et);
	
	public void updateExpertConTime(ExpertConsultationTime et);
	
	public void delExpertConTimeById(Integer tid);
	
	public Map<String, Object> queryRemoteConsulations(Integer expertId,String search,
			String sortdir, Integer sortcol,  Integer start,
			 Integer length);
	public Map<String, Object> queryRemoteConsulations_doc(Integer docid,String search,
			String sortdir, Integer sortcol,  Integer start,
			 Integer length);
	
	//查询是否有相同的用户记忆信息
	public boolean isExistContactInfo(String openid,String username,String idcard,String telphone);
	
	public Integer saveContactInfo(ContactInfo ci);
	
	public List<ContactInfo> queryContactInfosByOpenId(String openid);
	
	public Integer saveGraphicsInfo(GraphicsInfo ginfo);
	
	public void updateGraphicsInfo(GraphicsInfo gindo);
	
	public GraphicsInfo queryGraphicsInfoByTradeNo(String tradeNo);
	
	public List<SpecialistAppoint> querySpecialistAppointsBySid(Integer sid,String dtime);
	
	public Integer queryCountByPlused(Integer sid,String dtime);
	
	public Integer saveFaceDiagnoses(FaceDiagnoses fd);
	
	public SpecialistAppoint querySpecialistAppointById(Integer said);
	
	public Integer saveCases(Cases ca);
	
	public Integer saveCaseImage(CasesImage ci);

	public Integer saveOrders(Orders ord);
	
	public FaceDiagnoses queryFaceDiagnosesById(Integer id);
	
	public FaceDiagnoses queryFaceDiagnosesByTradeNo(String tradeNo);
	
	public Orders queryOrdersById(Integer id);
	
	public void updateFaceDiagnoses(FaceDiagnoses fd);
	
	public void updateOrders(Orders ord);
	
	public List<Cases> queryCasesListByUserId(Integer uid);
	
	public Cases queryCasesById(Integer id);
	
	public List<CasesImage> queryCaseImagesByCaseId(Integer caseid);
	
	public Integer savePatients(Patients pa);
	public CustomUpload queryCustomUploadByUrl(String url);
	
	//获取专家 或医生的订单，有问题，获取的是 全部医生的订单
	public Map<String,Object> queryRemoteConsulationsByConditions(Integer uid,String searchContent,String sortdesc,Integer start,Integer length,String status,String now);
	//获取专家 或医生所属医院科室 的订单
	public Map<String, Object> queryRemoteConsulationsByConditions(Integer userId,Integer userType,String searchContent, String sortdesc, Integer start,Integer length,String status, String now);
	public Map<String, Object> queryRemoteConsulationsByConditions_new(Integer userId,Integer userType,String searchContent, String sortdesc, Integer start,Integer length,String status, String now);
	
	public void updateRemoteConsulationUserStatus(Integer orderid);
	
	public Map<String,Object> generateConShedules(Integer expertId,String start,String end);
	
	public ExpertConsultationSchedule queryScheduleByConditions(Integer expertId,String scheduleDate,String startTime);
	
	public List<ExpertConsultationSchedule> queryExpertConTimeSchedulsByConditions(Integer expertId,String scheduleDate);
	
	public List<CooHospitalDepartments>  queryCooHospitalDepartmentsByHospital(Integer hosid);
	
	public List<MobileSpecial> queryMobileSpecialsByLocalDepartId(Integer depid);
	
	public ExpertConsultationSchedule queryExpertConScheduleById(Integer ecid);
	
	public void updatExpertConScheduleDate(ExpertConsultationSchedule sch);
	
	public List<MobileSpecial> queryMobileSpecialsByConditions(Integer depid,String sdate,String timetype,Integer pageNo,Integer pageSize);
	public List<MobileSpecial> queryMobileSpecialsByConditions_newnurse(Integer depid,String sdate,String timetype,Integer pageNo,Integer pageSize);
	
	public List<ExpertConsultationTime> queryExperTimesByConditions(Integer expertId,Integer weekday,String timetype);
	
	public List<ExpertConsultationSchedule> queryExpertConTimeSchedulsByConditions(Integer expertId,String sdate,String timetype);
	
	public Map<String,Object> queryRemoteConsultationsByConditions_nurse(Integer nurseid,Integer start,Integer length,String searchContent);
	
	public Map<String,Object> queryRemoteConsultationsByConditions_newnurse(Integer nurseid,Integer start,Integer length,String searchContent);
	
	public List<VedioRelative> queryVediosByOrderId(Integer oid);
	
	public UserAccountInfo queryUserAccountInfoByMobilePhone(String tel);
	
	public Integer saveUserAccountInfo(UserAccountInfo ua);
	
	public UserWeixinRelative queryUserWeiRelativeByOpenId(String openid);
	
	public Integer saveUserWeixinRelative(UserWeixinRelative uw);
	
	public Integer saveUserContactInfo(UserContactInfo uc);
	
	public List<UserContactInfo> queryUserContactInfosByOpenId(String openid);
	
	public boolean isExistUserContactInfo(String openid, String username,
			String idcard, String telphone);
	
	public List<HospitalDetailInfo> queryCoohospitalInfos();
	
	public HospitalDetailInfo queryHospitalDetailInfoById(Integer id);
	
	public List<HospitalDepartmentInfo> queryCoohospitalDepartmentsByCooHos(Integer hosid);
	
	public List<MobileSpecial> queryMobileSpecialsByLocalDepartId_new(Integer depid,Integer type);
	
	public HospitalDepartmentInfo queryHospitalDepartmentInfoById(Integer did);
	
	public List<RemoteConsultation> queryRemoteConsultationOrdersByConditions(String openid, Integer pageNo,Integer pageSize, String status);
	
	public List<CustomFileStorage> queryCustomFileStorageVedios(Integer oid);
	
	public Map<String, Object> queryRemoteConsulationsByConditions_docnew(Integer userId,Integer userType,String searchContent, String sortdesc, Integer start,Integer length,String status, String now);
	public UserAccountInfo queryUserAccountInfoById(Integer uid);
	public HospitalDetailInfo queryCooHospitalInfoByCity(String city);
	
	public Integer saveDoctorEstimateInfo(DoctorEstimateInfo estimate);
	
	public Map<String,Object> querySchedulesByTime(String begin,String end,Integer start,Integer length);
	
	public List<CustomFileStorage> queryCustomFileStorageImages(String relatedPics);
	
	public List<MobileSpecial> queryMobileSpecialsByConditionsPro(Map<String,Object> querymap,Integer pageNo,Integer pageSize);
	
	public List<StandardDepartmentInfo>queryStandardDepartmentByBigDep(Integer bigdepid);
	
	public List<HospitalDetailInfo> queryHospitalDetailsByDsitcode(String distcode,Integer type);
	
	public Map<String, Object> queryRemoteConsulationsByConditions_hos(Integer hosid,String searchContent, String sortdesc, Integer start,Integer length,String status, String now);
	
	public Map<String,Object> queryHospitalNursesByHosId(Integer hosid,String search,Integer start,Integer length,Integer utype);
	
	public Map<String,Object> queryHospitalDepartmentssByHosId(Integer hosid,String search,Integer statrt,Integer length,Integer ostatus);
	
	public String queryStandardDepartsByDepId(Integer depid,Integer type);
	
	public DepStandardR queryDepStandardRByConditions(Integer sid,Integer did,Integer type);
	public Integer saveDepStandardR(DepStandardR dr);
	public List<HospitalDepartmentInfo> queryHospitalDepartmentssByHosId(Integer hosid);
	
	public void updateHospitalDepartmentInfo(HospitalDepartmentInfo dep);
	public Integer saveHospitalDepartmentInfo(HospitalDepartmentInfo dep);
	
	public List<MobileSpecial> queryDistributeDocs(Integer hosid,Integer depid);
	
	public Map<String,Object> queryHospitalDepartmentssBySystem(String search,Integer start,Integer length,Integer hosid,Integer type);
	
	public List<HospitalDetailInfo> queryHospitalDetailsALL();
	
	public List<HospitalDepartmentInfo> queryHospitalDeparts();
	public List<StandardDepartmentInfo> queryStandardDepartments();
	
	public Map<String,Object> queryExpertsBySystem(String search,Integer start,Integer length,Integer status);
	
	public Map<String,Object> queryHospitalsBySystem(String search,Integer start,Integer length,Integer type);
	
	public List<Integer> queryStandsBydep(Integer depid,Integer type);
	
	public void updateHospitalDetailInfo(HospitalDetailInfo info);
	
	public Integer saveHospitalDetailInfo(HospitalDetailInfo info);
	
	public List<Dictionary> queryDictionarysByParentId(Integer pid);
	
	public void deleteStandConfig(Integer depid,Integer type);
	
	public void deleteStandConfigByCondition(String sds,Integer depid,Integer type);
	
	public List<UserContactInfo>queryUserContactInfosByUserId(Integer uid);
	
	public UserContactInfo queryUserContactorByCondition(Integer uid,String username,String tel);
	
	public UserContactInfo queryUserContactorInfoById(Integer cid);
	
	public Map<String,Object> queryhelporders(String search,Integer start,Integer length,Integer type);
	
	public void updateUserAccountInfo(UserAccountInfo info);
	
	public Integer saveUserFeedbackInfo(UserFeedBackInfo info);
	
	public List<ExpertConsultationSchedule>queryExpertConTimeSchedulsByExpertId(Integer expertId);
	
	public Map<String,Object> queryHosWxPlusOrders(Integer docid,String search,Integer start,Integer length);
	public WeiAccessToken queryWeiAccessTokenByDocId(Integer docid);
	
	public Integer saveWeiAccessToken(WeiAccessToken wat);
	
	public UserAccountInfo queryUserAccountInfoByMobileNumber(String tel);
	
	public List<WenzhenBean> queryWenzhenOrdersByConditions(Integer userId,Integer pageNo,Integer pageSize,String flag);
	
	public List<ReSourceBean> queryOpenCitys();
	
	public Dictionary queryDictionaryById(Integer id);
	
	public Integer saveBusinessVedioOrder(BusinessVedioOrder order);
	
	public List<RemoteConsultation> queryRemoteConsulations();
	
	public Pager queryMobileSpecialsByLocalDepartId_newpager(Integer depid,Integer type,Pager pager);
	
	public Map<String,Object> queryHosWxFeedbacks(Integer docid,String search,Integer start,Integer length);
	
	public Integer saveBusinessOperativeInfo(BusinessOperativeInfo info);
	public BusinessOperativeInfo queryBusinessOperativeInfoByOid(Integer oid);
	public List<MobileSpecial> queryMobileSpecialsByLocalDepartId_new(String keywords);
	public UserWeixinRelative queryUserWeiRelativeByUserId(Integer userid);
	public Map<String,Object> querylocaldocs(String search,Integer start,Integer length,Integer staus);
	public void updateUserWeixinRelative(UserWeixinRelative rel);
	public UserDetailInfo queryUserDetailInfoById(Integer id);
	public Integer saveUserDetailInfo(UserDetailInfo ud);
	public void updateUserDetailInfo(UserDetailInfo ud);
	public void updateUserContactInfo(UserContactInfo user);
	public UserContactInfo queryUserContactInfoByUuid(String uuid);
	public Map<String,Object> gainvedioorderdatas(String search,Integer start,Integer length,Integer type);
	public MobileSpecial queryMobileSpecialByUserIdAndUserType(Integer uid);
	public List<BusinessVedioOrder> getfinshvedioList(Integer sta,String startDate,String endDate, String hosid, String depid);
	public BusinessVedioOrder vedioorderRemrk(Integer sta, Integer id);
	public List<HospitalDetailInfo> queryhospitalInfos();
	public List<HospitalDepartmentInfo> queryDeps(Integer hosid);
	public Map<String, Object> gainrecriveorderdatas(String search, Integer start, Integer length, Integer type);
	public LivePlanOrder queryPlanLiveOrderById(Integer liveId);
	public void updatePlanLiveInfo(LivePlanOrder live);
	public Integer savePlanLiveInfo(LivePlanOrder live);
	public void delPlanLiveInfo(Integer liveId);
}
