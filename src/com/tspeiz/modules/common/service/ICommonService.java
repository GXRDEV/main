package com.tspeiz.modules.common.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
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
import com.tspeiz.modules.common.entity.CustomUpload;
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

public interface ICommonService {
	public List<DoctorRegisterInfo> queryDoctorRegs();
	//检验登陆
	public Users queryUsersByLogin(String username,String password,String stype);
	//根据专家获取远程会诊订单
	public List<RemoteConsultation> queryRemoteConsulationByExpert(Integer expertId);
	
	public RemoteConsultation queryRemoteConsulationById(Integer orderId);
	public void UpdateRemoteConsulation(RemoteConsultation order);
	
	public Boolean isFreeExpertConsultationTime(Integer exportId,String scheduleDate,String startTime, Integer timeDur);
	
	public List<ReSourceBean> queryExpertConsultationTimesByExpertId(Integer expertId,String time,Integer week);
	
	public void testMongoDb();
	//
	public Integer saveCustomUpload(CustomUpload cu);
	
	public CustomUpload queryCustomUpload(Integer cuid);
	
	public Users queryUserByMobile(String tel);
	
	public Users queryUserById(Integer userId);
	
	public void updateUsers(Users user);
	
	public List<RemoteConsultation> queryRemoteConsultationsByExpertIdAndProgressTag(Integer expertId,Integer progressTag);
	/**
	 * 
	 * @param doctorId
	 * @param progressTag
	 * @param queryType   0:通过 医生所在医院及科室 查询（订单没有非陪医生）   1:通过医生ID 直接查询（ 订单已经分配医生的情况）
	 * @return
	 */
	public List<RemoteConsultation> queryRemoteConsultationsByDoctorIdAndProgressTag(Integer doctorId,Integer progressTag,Integer queryType);
	public boolean isExpertHasOrder(Integer expertId,Integer orderId,Integer progressTag);
	/**
	 * 
	 * @param doctorId
	 * @param progressTag
	 * @param queryType   0:通过 医生所在医院及科室 查询（订单没有非陪医生）   1:通过医生ID 直接查询（ 订单已经分配医生的情况）
	 * @return
	 */
	public boolean isDoctorHasOrder(Integer doctorId,Integer orderId,Integer progressTag,Integer queryType);
	
	public MobileSpecial  queryMobileSpecialByDoctorId(Integer doctorId);
	public MobileSpecial  queryMobileSpecialByExpertId(Integer expertId);
	public MobileSpecial  queryMobileSpecialByUserIdAndUserType(Integer userId,Integer userType);
	
	public Integer saveVedioRelative(VedioRelative vr);
	
	public Integer saveMedicalRecords(MedicalRecords mr);
	
	public void updateMedicalRecords(MedicalRecords mr);
	
	public MedicalRecords queryMedicalRecordsById(Integer mrid);
	
	public MedicalRecords queryMedicalRecordsByOrderId(Integer oid);
	
	public DoctorRegisterInfo queryDoctorRegisterInfo(String username,String password);
	
	public MobileSpecial queryMobileSpecialByUserIdAndUserType(Integer user);
	
	public MobileSpecial queryAuditDocDetailById(Integer id);
	
	public DoctorRegisterInfo queryDoctorRegisterInfoById(Integer id);
	
	public void updateDoctorRegisterInfo(DoctorRegisterInfo doc);
	
	public DoctorRegisterInfo queryDoctorRegisterInfoByTel(String tel);
	
	public CustomFileStorage queryCustomFileStorage(Integer id);
	
	public Integer saveCustomFileStorage(CustomFileStorage cu);
	
	public void updateCustomFileStorage(CustomFileStorage cu);
	
	public Integer saveDoctorRegisterInfo(DoctorRegisterInfo reg);
	
	public Integer saveDoctorDetailInfo(DoctorDetailInfo detail);
	
	public DoctorRegisterInfo queryDoctorRegisterInfoByTel(String tel,Integer utype);

	public HospitalDepartmentInfo  getHospitalDepartmentInfoById(Integer depId);
	
	public HospitalDetailInfo  getHospitalDetailInfoById(Integer hospId);
	public DoctorDetailInfo queryDoctorDetailInfoById(Integer eid);
	
	public void updateDoctorDetailInfo(DoctorDetailInfo dinfo);
	
	public List<DistCode> queryDistCodesByConditions(String stype,String procost);
	
	public List<HospitalDetailInfo> queryHospitalsByPro(String procost,String type);
	
	public Integer queryOrdersNumByConditions(Integer hosid,Integer depid,String begin,String end);
	
	public Integer queryUnCompletedNum(Integer hosid);
	
	public Integer queryTotalOrders(Integer hosid);
	
	public Integer queryOrderNumbyType(Integer hosid,Integer type);
	
	public DoctorRegisterInfo queryDoctorRegisterInfoByLoginName(String loginName);
	
	public DoctorRegisterInfo queryDoctorRegisterInfoByHosAndType(Integer hosid,Integer type);
	
	public Map<String,Object> queryMoneyExchangeCodes(String searchContent,Integer start,Integer length,Integer type);
	
	public MoneyExchange queryMoneyExchangeByConditions(String code);
	
	public Integer saveMoneyExchange(MoneyExchange me);
	
	public DoctorForum querynewforum();
	
	public ShortUrlRelate queryShortUrlRelate(String code);
	
	public ShortUrlRelate queryShortUrlRelate(String ltype,String oid);
	
	public Integer saveShortUrlRelate(ShortUrlRelate surl);
	
	public BaiduAudioToken queryBaiduAudioTokenById(Integer id);
	
	public void updateBaiduAudioToken(BaiduAudioToken token);
	
	public Integer saveBusinessPayInfo(BusinessPayInfo info);
	
	public BusinessPayInfo queryBusinessPayInfoByTradeNo(String tradeno);
	
	public BusinessVedioOrder queryBusinessVedioOrderById(Integer id);
	
	public void updateBusinessVedioOrder(BusinessVedioOrder order);
	
	public CaseInfo queryCaseInfoById(Integer id);
	
	public void updateBusinessPayInfo(BusinessPayInfo info);
	
	public void updateCaseInfo(CaseInfo caseinfo);
	
	public Integer saveSpecialAdviceOrder(SpecialAdviceOrder order);
	
	public List<HospitalDetailInfo> queryDocRelativeHospitals(Integer docid);
	
	public List<StandardDepartmentInfo> queryDocRelativeDepparts(Integer docid);
	
	public SpecialAdviceOrder querySpecialAdviceOrderById(Integer id);
	
	public void updateSpecialAdviceOrder(SpecialAdviceOrder order);
	
	public Map<String,Object> querySpecialAdviceOrdersByCondition(Integer type,Integer userId,String search,Integer start,Integer length,Integer utype);
	
	public Integer getOrderSerialNumberByOrderType(Integer orderType);
	public String getOrderNumberByOrderType(Integer orderType);
	
	public DoctorServiceInfo queryDoctorServiceInfoByOrderType(Integer orderType,Integer docid);
	
	public List<DoctorDetailInfo> queryDoctorDetailsByExpert();
	
	public Integer saveDoctorServiceInfo(DoctorServiceInfo info);
	
	public DoctorServiceInfo queryDoctorServiceInfoByCon(Integer docid,Integer serviceId,Integer packageId);
	
	public void updateDoctorServiceInfo(DoctorServiceInfo info);
	
	public Integer saveAppPcLogin(AppPcLogin login);
	
	public AppPcLogin queryAppPcLoginByKeyId(String keyid);
	
	public BusinessVedioOrder queryBusinessVedioOrderByUid(String uid);
	
	public Map<String,Object> queryNewAddHospital(List<String> dates);
	
	public Map<String,Object> queryNewExpertsAdd(List<String> dates);
	
	public Map<String,Object> queryOrdersAddCon(List<String> dates,String hosid);
	public Map<String,Integer> queryOrdersAddCon(String startDate,String endDate,Integer type,String hosid);
	public List<ReSourceBean> queryOrderHosCal(String startDate,String endDate);
	public List<ReSourceBean> orderexcal(String hosid,String startDate,String endDate);
	public List<ReSourceBean> orderdepcal(String hosid,String startDate,String endDate);
	public List<DoctorServiceInfo>queryOpenDoctorServiceInfo(Integer docid,String serviceIds);
	public List<DoctorServiceInfo>queryDoctorServiceInfoByDocId(Integer docid);
	public DoctorServiceInfo queryDoctorServiceInfoById(Integer id);
	
	public List<HospitalDetailInfo> queryHospitals_expert();
	
	public Pager queryMobileSpecial_helporder(Pager pager,Integer type);
	public Integer saveImTokenInfo(ImTokenInfo info);
	public ImTokenInfo queryImTokenInfoByCon(String userId,String mode);
	
	public Integer saveSystemPushInfo(SystemPushInfo pinfo);
	public List<SystemPushInfo> querySystemPushInfoByUser(Integer userId);
	public SystemPushInfo querySystemPushInfoByBusKey(String businessKey);
	public SystemPushInfo querySystemPushInfoByPushKey(String pushKey);
	public Map<String,Object> queryNeedAuditDocs(Integer status,String search,Integer start,Integer length);
	public SpecialAdviceOrder querySpecialAdviceOrderByUid(String uuid);
	public void updateSystemPushInfo(SystemPushInfo pinfo);
	public List<MobileSpecial> queryExperts_wx(Integer pageNo,Integer pageSize,Integer depid,String hosid,String standdepid,String zc,String keywords);
	public Map<String,Object> querySpecialAdviceOrdersByCondition_sys(Integer type,String search,Integer start,Integer length);
	public Dictionary queryDictionaryByCon(String displayName,Integer parentId);
	public Map<String,Object> queryDoctorWithdrawdatas(String startDate,String endDate,String search,Integer start,Integer length,Integer docid,Integer ostatus,String busiType);
	public DoctorWithdrawInfo queryDoctorWithdrawInfoById(Integer id);
	public DoctorWithdrawInfo queryDoctorWithdrawInfoByIdAll(Integer id);
	public void updateDoctorWithdrawInfo(DoctorWithdrawInfo info);
	public Map<String,Object> querydoctorbilldatas(Map<String,Object> querymap,Integer start,Integer length,Integer docid);
	public SystemServiceInfo querySystemServiceInfoById(Integer id);
	public List<MobileSpecial> queryCooDocsByDep(Integer depid);
	public Integer saveDoctorBillInfo(DoctorBillInfo bill);
	public Map<String,Object> queryexlogindatas(Map<String,Object> querymap,Integer start,Integer length);
	
	public DistCode queryDistCodeByCode(String code);
	
	public CaseInfo queryCaseInfoById_new(Integer caseid);
	
	public Map<String,Object> queryauditdocteamdatas(Map<String,Object> querymap,Integer start,Integer length);
	public DoctorTeam queryDoctorTeamById(Integer tid);
	public void updateDoctorTeam(DoctorTeam dt);
	public Map<String,Object> querydocteamdatas(Map<String,Object> querymap,Integer start,Integer length);
	public Map<String,Object> queryt2ptuwendatas(Map<String,Object> querymap,Integer start,Integer length);
	public Map<String,Object> queryserverdatas(Map<String,Object> querymap,Integer start,Integer length);
	public SystemServiceDefault querySystemServiceDefaultByCon(Integer serviceId,Integer dutyId);
	public List<DoctorScheduleShow> queryDoctorScheduleShowsByDoctorId(Integer docid);
	public Integer saveDoctorScheduleShow(DoctorScheduleShow dss);
	public void updateDoctorScheduleShow(DoctorScheduleShow dss);
	public DoctorScheduleShow isExistDoctorScheduleShowOpen(Integer docid,Integer weekDay,Integer outTime);
	public DoctorScheduleShow queryDoctorScheduleShowById(Integer id);
	public void deldocscheduleshowId(Integer id);
	public Map<String, Object> queryhoshealthdatas(String search,Integer start,Integer length,String status);
	public HospitalHealthAlliance queryHospitalHealthAllianceById(Integer hid);
	public void updateHospitalHealthAlliance(HospitalHealthAlliance hha);
	public Integer saveHospitalHealthAlliance(HospitalHealthAlliance hha);
	public List<HospitalHealthAllianceMember> queryHospitalHealAllianceMembersByCon(String hhaUuid,Integer level,Integer parentHosId);
	public List<HospitalDetailInfo> queryHospitalsByCon(String status,String aids);
	public Integer saveHospitalHealthAllianceMember(HospitalHealthAllianceMember member);
	public HospitalHealthAllianceMember queryHospitalHealthAllianceMemberByCon(Integer hosId,String allianceUuid);
	public void delHospitalHealthAllianceMember(Integer mid);
	public Map<String,Object> queryoperatordatas(String search,Integer ostatus,Integer start,Integer length);
	public List<ShufflingFigureConfig> queryShufflingFigureConfigsByType(Integer type);
	public ShufflingFigureConfig queryShufflingFigureConfigById(Integer id);
	public Integer saveShufflingFigureConfig(ShufflingFigureConfig fig);
	public void updateShufflingFigureConfig(ShufflingFigureConfig fig);
	public void delShufflingFigureConfig(Integer id);
	public Integer saveYltApplicationRequest(YltApplicationRequest yltreq);
	public Integer saveYltInvitationRequest(YltInvitationRequest yirreq);
	public Map<String,Object> querymyyltdatas_hos(Integer hosId,String search,Integer start,Integer length);
	public HospitalHealthAlliance queryHospitalHealthAllianceByUuid(String uuid);
	public HospitalHealthAlliance queryHospitalHealthAllianceByRegId(Integer regId);
	public Integer queryCountBySql(String sql);
	public List<HospitalHealthAlliance> queryHospitalHealthAlliances();
	public List<HospitalDetailInfo> queryhighlevelhos(Integer allianceId,String levels);
	public Map<String,Object> queryinvitjoin_hos(Integer hosId,String search,Integer start,Integer length,Integer otype);
	public Map<String,Object> queryappjoin_hos(Integer hosId,String search,Integer start,Integer length,Integer otype);
	public YltApplicationRequest queryYltApplicationRequestById(Integer id);
	public void updateYltApplicationRequest(YltApplicationRequest rq);
	public List<HospitalHealthAlliance> queryHospitalHealthAlliances(Integer hosId);
	public List<HospitalDetailInfo> queryHospitalHealthAllianceMemberByHhaId(String  alianceUuid);
	public List<HospitalDepartmentInfo> queryHospitalDepartmentInfosByHosId(Integer hosId);
	public Map<String,Object> queryReferordersByCondition(Integer docid,String searchContent,Integer ostatus,Integer start,Integer length,Integer dtype);
	public DoctorTeam queryDoctorTeamByUuid(String uuid);
	public HospitalHealthAlliance queryHospitalHealthAllianceById_new(Integer id);
	public void delHospitalHealthAlliance(Integer id);
	public void updateHospitalHealthAllianceMember(HospitalHealthAllianceMember member);
	public HospitalHealthAllianceMember queryHospitalHealthAllianceMemberById(Integer id);
	public YltInvitationRequest queryYltInvitationRequestById(Integer id);
	public void updateYltInvitationRequest(YltInvitationRequest yr);
	public Map<String,Object> querySpecialAdviceOrdersByCondition_new(Integer type,String searchContent,Integer start,Integer length);
	public DoctorTeam queryDoctorTeamById_detail(Integer tid);
	public Map<String,Object> queryDoctorTeamMemberDatas(Integer teamId,String searchContent,Integer start,Integer length);
	public Map<String,Object> queryGroups(Integer ostatus,Integer groupType, String searchContent,Integer start,Integer length);
	public Map<String,Object> gainGroupMemberDatas(Integer groupId,String searchContent,Integer start,Integer length);
	public Map<String,Object> operatorhosdatas(String searchContent,Integer opId,Integer start,Integer length);
	public void delHospitalMaintainerRelation(Integer id);
	public void saveRongCloudGroup(RongCloudGroup group);
	public void saveRongCloudGroupMember(RongCloudGroupMember member);
	public List<HelpBean> queryOpenPros(String type);
	public List<HelpBean> queryOpenCitys(String pdist,String type,Integer stype);
	public RongCloudGroup queryRongCloudGroupById(Integer groupId);
	public void updateRongCloudGroup(RongCloudGroup group);
	public List<RongCloudGroupMember> queryRongCloudGroupMembersByGroupUuid(String groupUuid);
	public void updateRongCloudGroupMember(RongCloudGroupMember member);
	public RongCloudGroupMember queryRongCloudGroupMembersById(Integer memberId);
	public RongCloudGroup queryRongCloudGroupByGroupUuid(String groupUuid);
	public void delRongCloudGroupMember(Integer memberId);
	public void saveHospitalMaintainerRelation(HospitalMaintainerRelation hmr);
	public Pager queryMobileSpecial_loaddoc(Pager pager);
	public Pager queryMobileSpecial_loadExOrdoc(Pager pager);
	public Map<String,Object> queryVedioOrderDatas_doc(Integer docid,String searchContent,Integer ostatus,Integer start,Integer length,Integer dtype);
	public Map<String,Object> queryD2DTuwenDatas_doc(Integer docid,String searchContent,Integer ostatus,Integer start,Integer length,Integer dtype);
	public Map<String,Object> queryD2DTuwenDatas_expert(Integer docid,String searchContent,Integer ostatus,Integer start,Integer length);
	public List<DistCode> queryAllianceAreas();
	public List<HospitalHealthAlliance> querygainAllianceByArea(String distCode);
	public Map<String,Object> querySystemConsultationCaseDatas(String searchContent,Integer ostatus,Integer start,Integer length);
	public void saveSystemConsultationCase(PlatformHealthConsultation ssc);
	public PlatformHealthConsultation querySystemConsultationCaseById(Integer sscId);
	public void updateSystemConsultationCase(PlatformHealthConsultation ssc);
	public List<DoctorInfoDto> loadAllianceDoctors(String allianceId,Integer pageNo,Integer pageSize);
	public Pager queryAllianceDoctorsPager(Pager pager);
	public Map<String,Object> queryReferordersByCondition_hos(Integer hosId,String search,Integer ostatus,Integer start,Integer length,Integer dtype);
	public List<MobileSpecial> queryDoctorDetailInfoByHosId(Integer hosId);
	public Map<String,Object> queryVedioordersByCondition_hos(Integer hosId,String search,Integer ostatus,Integer start,Integer length,Integer dtype);
	public List<DoctorTeamMember> queryDoctorTeamMembersByTeamId(Integer teamId);
	public DoctorConsultationOpinion queryDoctorConsultationOpinion(Integer opId);
	public Integer saveDoctorConsultationOpinion(DoctorConsultationOpinion op);
	public void updateDoctorConsultationOpinion(DoctorConsultationOpinion op);
	public DoctorConsultationOpinion queryDoctorConsultationOpinionByUuid(String uuid);
	public Map<String,Object> queryVedioOrderDatas_expert(Integer docid,String searchContent,Integer ostatus,Integer start,Integer length);
	public void saveOrderBindingCode(OrderBindingCode obc);
	public List<HospitalHealthAllianceMember> queryHospitalHealthAllianceMemberByCon_main(String allianceUuid);
	public List<HospitalHealthAlliance> queryHospitalHealthAlliances_audited();
	public Integer queryUnReadMsg(String uuid,Integer otype,Integer docid);
	public void updateMsgToRead(String uuid,Integer otype,Integer docid);
	public UserWeixinRelative queryUserWeixinRelativeByCondition(String appId,Integer userId);
	public MobileSpecial getDoctorDetailList(Integer docId);
	public BusinessPayInfo queryBusinessPayInfoByCondition(Integer oid,Integer otype);
	public YltApplicationRequest queryYltApplicationRequestByCondition(String allianceUuid,Integer applicantId,Integer applicantType);
	public MobileSpecial queryHosAdminMobileSpecialByHosId(Integer hosId);
	public DoctorDetailInfo getLocalDocTel(Integer localDoctorId);
	public List<MobileSpecial> getServiceList(String hosid, String startDate, String endDate, Integer isOpenvedio, Integer isOpentuwen, String depid);
	public List<SpecialAdviceOrder> gettuwenList(Integer sta,String hosid, String depid, String startDate, String endDate);
	public void updateVedioorderRemrk(BusinessVedioOrder vodio);
	public BusinessVedioOrder queryBusinessVedioById(Integer id);
	public UserCaseAttachment queryUserCaseAttachmentById(Integer id);
	public Integer saveOrUpdateUserCaseAttachment(UserCaseAttachment attachment);
	public void delUserCaseAttachment(Integer id);
	public void delDoctorTeamMembers(Integer userId);
	public List<UserCaseAttachment> queryUserCaseAttachmentsByCaseUuid(String caseUuid);
	public DoctorTeamMember queryDoctorTeamMembersByDocId(Integer userId);
	public Map<String, Object> gainDocTeamGroupMemberDatas(Integer groupId, String searchContent, Integer start,
			Integer length);
	public List<DoctorTeamMember> queryDoctorTeamMembers(String groupUuid);
	public DoctorTeam queryDoctorTeamByuuid(String groupUuid);
	public Map<String, Object> docprivatedatas(Integer ostatus,String searchContent, Integer start, Integer length);
	public BusinessD2pPrivateOrderDto queryprivateOrdersByid(Integer id);
	public Map<String, Object> docteamvipdatas(Integer ostatus,String searchContent, Integer start, Integer length);
	public BusinessT2pVipOrderDto queryteamevipByid(Integer id);
	public OperatorInvitCode queryOperatorInvitCode(Integer docid,String code);
	public void saveOperatorInvitCode(OperatorInvitCode invitCode);
	public Map<String, Object> servicedatas(String searchContent, Integer start, Integer length);
	public SystemServiceInfo queryservicedatasInfo(Integer id);
	public Integer saveServiceInfo(SystemServiceInfo systemServiceInfo);
	public void updateServiceInfo(SystemServiceInfo systemServiceInfo);
	public void updateServicePackage(Integer id, JSONArray packageArray, List<SystemServicePackage> systemServicePackages);
	public void deleteServicePackage(Integer id);
	public SystemServicePackage queryServicePackageByServiceId(Integer id);
	public RongCloudGroupPostRelation queryRongCloudGroupPostRelation(String groupUuid);
	public void updateRongCloudGroupPostRelation(RongCloudGroupPostRelation grouppost);
	public void delDocTeamRequest(String groupUuid);
	public Map<String, Object> gaininvitDocdatas(String searchContent, Integer start, Integer length, String invitCode);
	public SystemServiceInfo querySystemServiceById(Integer id);
	public List<SystemServicePackage> queryServicePackagesByServiceId(Integer serviceid);
	public Map<String,Object> queryDoctor(Map<String, Object> queryMap,Integer start,Integer length);
	public void saveServicePackage(Integer serviceId, JSONArray packageArray);
	public Map<String, Object> aboutdocsdatas(Map<String, Object> querymap, Integer start, Integer length);
	public List<DoctorTeam> queryDoctorTeamHasNoErweima();
	public List<UserDevicesRecord> getLoginList(String docName, String hosid, String depid, String lastTimes,
			String startDate, String endDate);
	public Map<String,Object> queryNewAddDoctors(List<String> dates,String queryType);
	public Map<String,Object> queryNewAddPatients(List<String> dates,String queryType);
	public List<SystemBusinessDictionary> querySysDicList(Integer groupId);
	public Map<String, Object> querySpecialAdviceOrdersByCondition(Integer hosId,String search,Integer ostatus,Integer start,Integer length,Integer dtype);
	public void updateBusinesstuwenOrder(BusinessDtuwenOrder order);
	public BusinessDtuwenOrder queryBussinessTuwenOrderById(Integer tuwenId);
	public void updateUserCaseAttachment(UserCaseAttachment attachment);
	public CaseInfo queryCaseInfoByUuid(String caseUuid);
	public List<PlatformHealthType> queryUseingPlatFormHealthTypes(Integer status);
	public DoctorBillInfo querydoctorbill(Integer docid);
	public List<CaseInfo> queryCasesTransToAttachments();
	public OperatorInvitCode queryDocIdByinvitCode(String invitationCode);
	public List<DoctorAboutCount> gainDocAttentCount(String hosid, String depid, String startDate, String endDate);
	public List<DoctorAboutCount> gainDocreportCount(String hosid, String depid, String startDate, String endDate);
	public Map<String, Object> querydoclogindatas(Map<String, Object> querymap, Integer start, Integer length);
	public Map<String, Object> docreportaboutdatass(Integer ostatus, String searchContent, Integer start,
			Integer length);
	public List<MobileSpecial> gainDocregisterexport(String hosid, String depid, String startDate, String endDate);
	public DoctorSceneEwm queryDoctorSceneEwmByDoctorId(Integer doctorId);
	public void saveDoctorSceneErweima(DoctorSceneEwm dse);
	public void updateDoctorSceneErweima(DoctorSceneEwm dse);
	public List<DoctorSceneEwm> queryDoctorSceneEwms_doc(String hosid,String depid,String startDate,String endDate);
	public Map<String,Object> querySystemSmsRecordDatas(String search,Integer status,Integer start,Integer length);
	public List<DoctorIncomeDto> docincomeexport(String hid, String depid, String startDate, String endDate);
	
	public void saveThirdOrderInfo(ThirdOrderInfo orderInfo);
	public ThirdOrderInfo queryThirdOrderInfoByOrderUuid(String orderUuid);
	public List<ThirdOrderAttachment> queryThirdOrderAttachments(String orderUuid);
	public SystemBusinessDictionary querySysDicByGroupAndCode(Integer group,Integer code);
	public void saveThirdOrderAttachment(ThirdOrderAttachment att);
	public void delThirdOrderAttachment(Integer id);
	public ThirdOrderInfo queryThirdOrderInfoByConsultationId(String conId);
	public void updateThirdOrderInfo(ThirdOrderInfo orderInfo);
	public Integer addDoctorBillInfo(DoctorBillInfo doctorBillInfo);
	public Map<String,Object> queryDocFollowDatas(String search,Integer start,Integer length,Integer type);
	public List<DocFollowDto> queryDocFollowDetailData(Integer type,String orderUuid,String docId,String subUserId);
	public Map<String, Object> userWarmDocDatas(Integer ostatus,String searchContent, Integer start, Integer length);
	public Map<String,Object> liveplandatass(String searchContent, Integer start, Integer length);
	public Map<String,Object> queryHisCaseList(String searchContent,Integer start,Integer length,Integer type);
	public Integer saveHistoryCaseInfo(HistoryCaseInfo historyCaseInfo);
	public Integer saveHistoryCaseAttachment(HistoryCaseAttachment historyCaseAttachment);
	public Integer saveHistoryCaseDepRelative(HistoryCaseDepRelative historyCaseDepRelative);
	public HistoryCaseInfo queryHistoryCaseInfo(String historyCaseUuid);
	public void updateHistoryCaseInfo(HistoryCaseInfo historyCaseInfo);
	public HistoryCaseAttachment queryHistoryCaseAttachment(String historyCaseUuid);
	public void updateHistoryCaseAttachment(HistoryCaseAttachment historyCaseAttachment);
	public List<HistoryCaseDepRelative> queryHistoryCaseDepRelatives(String historyCaseUuid);
	public void delHistoryCaseDepRelative(Integer id);
}
