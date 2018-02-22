
package com.tspeiz.modules.common.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tspeiz.modules.common.dao.release2.ILivePlanOrderInfoDao;
import com.tspeiz.modules.common.entity.newrelease.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.bean.weixin.DepartString;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.dao.IBigDepartmentDao;
import com.tspeiz.modules.common.dao.IBusinessOperativeInfoDao;
import com.tspeiz.modules.common.dao.ICooHospitalDepartmentsDao;
import com.tspeiz.modules.common.dao.ICooHospitalDetailsDao;
import com.tspeiz.modules.common.dao.ICustomUploadDao;
import com.tspeiz.modules.common.dao.IDepartmentsDao;
import com.tspeiz.modules.common.dao.IExpertConsultationScheduleDao;
import com.tspeiz.modules.common.dao.IExpertConsultationTimeDao;
import com.tspeiz.modules.common.dao.IFaceDiagnosesDao;
import com.tspeiz.modules.common.dao.IRemoteConsultationDao;
import com.tspeiz.modules.common.dao.ISpecialistAppointDao;
import com.tspeiz.modules.common.dao.IUsersDao;
import com.tspeiz.modules.common.dao.IVedioRelativeDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessVedioOrderDao;
import com.tspeiz.modules.common.dao.newrelease.IBusinessWenZhenInfoDao;
import com.tspeiz.modules.common.dao.newrelease.ICustomFileStorageDao;
import com.tspeiz.modules.common.dao.newrelease.IDepStandardRDao;
import com.tspeiz.modules.common.dao.newrelease.IDictionaryDao;
import com.tspeiz.modules.common.dao.newrelease.IDoctorRegisterInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IHosAppointOrderDao;
import com.tspeiz.modules.common.dao.newrelease.IHospitalDepartmentInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IHospitalDetailInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IStandardDepartmentInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IUserAccountInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IUserContactInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IUserDetailInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IUserFeedBackInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IUserWeixinRelativeDao;
import com.tspeiz.modules.common.dao.weixin.ICaseImageDao;
import com.tspeiz.modules.common.dao.weixin.ICasesDao;
import com.tspeiz.modules.common.dao.weixin.IContactInfoDao;
import com.tspeiz.modules.common.dao.weixin.IDoctorEstimateInfoDao;
import com.tspeiz.modules.common.dao.weixin.IGraphicsInfoDao;
import com.tspeiz.modules.common.dao.weixin.IOrdersDao;
import com.tspeiz.modules.common.dao.weixin.IPatientsDao;
import com.tspeiz.modules.common.dao.weixin.IUserExternalsDao;
import com.tspeiz.modules.common.dao.weixin.IUserFeedbackDao;
import com.tspeiz.modules.common.dao.weixin.IWeiAccessTokenDao;
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
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.util.DateUtil;
import com.tspeiz.modules.util.date.CalendarUtil;

@Service("weixinService")
public class WeixinServiceImpl implements IWeixinService {
	private IWeiAccessTokenDao weiAccessTokenDao;
	private IUsersDao usersDao;
	private IUserExternalsDao userExternalsDao;
	private IUserAccountInfoDao userAccountInfoDao;

	@Resource
	private IDepartmentsDao departmentsDao;
	@Resource
	private IBigDepartmentDao bigDepartmentDao;
	@Resource
	private ICooHospitalDetailsDao cooHospitalDetailsDao;
	@Resource
	private IRemoteConsultationDao remoteConsultationDao;
	@Resource
	private IUserFeedbackDao userFeedbackDao;
	@Resource
	private ICooHospitalDepartmentsDao cooHospitalDepartmentsDao;
	@Resource
	private IExpertConsultationTimeDao expertConsultationTimeDao;
	@Resource
	private IExpertConsultationScheduleDao expertConsultationScheduleDao;
	@Resource
	private IContactInfoDao contactInfoDao;
	@Resource
	private IGraphicsInfoDao graphicsInfoDao;
	@Resource
	private ISpecialistAppointDao specialistAppointDao;
	@Resource
	private IFaceDiagnosesDao faceDiagnosesDao;
	@Resource
	private ICasesDao casesDao;
	@Resource
	private ICaseImageDao caseIamgeDao;
	@Resource
	private IOrdersDao ordersDao;
	@Resource
	private IPatientsDao patientsDao;
	@Resource
	private ICustomUploadDao customUploadDao;
	@Resource
	private IVedioRelativeDao vedioRelativeDao;
	
	private IUserWeixinRelativeDao userWeixinRelativeDao;
	
	@Resource
	private IUserContactInfoDao userContactInfoDao;
	@Resource
	private IHospitalDetailInfoDao hospitalDetailInfoDao;
	@Resource
	private IHospitalDepartmentInfoDao hospitalDepartmentInfoDao;
	@Resource
	private IDoctorRegisterInfoDao doctorRegisterInfoDao;
	@Resource
	private ICustomFileStorageDao customFileStorageDao;
	@Resource
	private IDoctorEstimateInfoDao doctorEstimateInfoDao;

	@Resource
	private IStandardDepartmentInfoDao standardDepartmentInfoDao;
	@Resource
	private IDepStandardRDao depStandardRDao;
	@Resource
	private IDictionaryDao dictionaryDao;
	@Resource
	private IUserFeedBackInfoDao userFeedBackInfoDao;

	@Resource
	private IHosAppointOrderDao hosAppointOrderDao;
	@Resource
	private IBusinessWenZhenInfoDao businessWenZhenInfoDao;
	@Autowired
	private IBusinessVedioOrderDao businessVedioOrderDao;
	
	@Autowired
	private IBusinessOperativeInfoDao businessOperativeInfoDao;
	@Autowired
	private IUserDetailInfoDao userDetailInfoDao;
	@Autowired
	private ILivePlanOrderInfoDao livePlanOrderInfoDao;
	
	public IUserWeixinRelativeDao getUserWeixinRelativeDao() {
		return userWeixinRelativeDao;
	}

	public void setUserWeixinRelativeDao(
			IUserWeixinRelativeDao userWeixinRelativeDao) {
		this.userWeixinRelativeDao = userWeixinRelativeDao;
	}

	public IUserDetailInfoDao getUserDetailInfoDao() {
		return userDetailInfoDao;
	}

	public void setUserDetailInfoDao(IUserDetailInfoDao userDetailInfoDao) {
		this.userDetailInfoDao = userDetailInfoDao;
	}

	public IUserAccountInfoDao getUserAccountInfoDao() {
		return userAccountInfoDao;
	}

	public void setUserAccountInfoDao(IUserAccountInfoDao userAccountInfoDao) {
		this.userAccountInfoDao = userAccountInfoDao;
	}

	public IWeiAccessTokenDao getWeiAccessTokenDao() {
		return weiAccessTokenDao;
	}

	public void setWeiAccessTokenDao(IWeiAccessTokenDao weiAccessTokenDao) {
		this.weiAccessTokenDao = weiAccessTokenDao;
	}

	public IUsersDao getUsersDao() {
		return usersDao;
	}

	public void setUsersDao(IUsersDao usersDao) {
		this.usersDao = usersDao;
	}

	public IUserExternalsDao getUserExternalsDao() {
		return userExternalsDao;
	}

	public void setUserExternalsDao(IUserExternalsDao userExternalsDao) {
		this.userExternalsDao = userExternalsDao;
	}

	public IRemoteConsultationDao getRemoteConsultationDao() {
		return remoteConsultationDao;
	}

	public void setRemoteConsultationDao(
			IRemoteConsultationDao remoteConsultationDao) {
		this.remoteConsultationDao = remoteConsultationDao;
	}

	public WeiAccessToken queryWeiAccessTokenById(String appId) {
		// TODO Auto-generated method stub
		return weiAccessTokenDao.queryWeiAccessTokenById(appId);
	}

	public void updateWeiAccessToken(WeiAccessToken at) {
		// TODO Auto-generated method stub
		weiAccessTokenDao.update(at);
	}

	public UserExternals queryUserExternalByExternalId(String externalId) {
		// TODO Auto-generated method stub
		return userExternalsDao.queryUserExternalByExternalId(externalId);
	}

	public Integer saveUserExternal(UserExternals ue) {
		// TODO Auto-generated method stub
		return userExternalsDao.save(ue);
	}

	public Integer saveUsers(Users user) {
		// TODO Auto-generated method stub
		return usersDao.save(user);
	}

	public void updateUserExternal(UserExternals ue) {
		// TODO Auto-generated method stub
		userExternalsDao.update(ue);
	}

	public List<MobileSpecial> queryMobileSpecialsByConditions(Integer depid,
			String scontent, String scity, String spro, Integer pageNo,
			Integer pageSize, String stype) {
		// TODO Auto-generated method stub
		return usersDao.queryMobileSpecialsByConditions(depid, scontent, scity,
				spro, pageNo, pageSize, stype);
	}

	public List<Departments> queryDepartments() {
		// TODO Auto-generated method stub
		return departmentsDao.queryDepartments();
	}

	public List<BigDepartment> queryBigDepartments() {
		// TODO Auto-generated method stub
		return bigDepartmentDao.queryBigDepartments();
	}

	public List<Departments> queryDepartmentByBigDep(Integer bigid) {
		// TODO Auto-generated method stub
		return departmentsDao.queryDepartmentByBigDep(bigid);
	}

	public List<CooHospitalDetails> queryAllCooHospitalDetails() {
		// TODO Auto-generated method stub
		return cooHospitalDetailsDao.queryAllCooHospitalDetails();
	}

	public CooHospitalDetails queryCooHospitalDetailsByCity(String city) {
		// TODO Auto-generated method stub
		return cooHospitalDetailsDao.queryCooHospitalDetailsByCity(city);
	}

	public MobileSpecial queryMobileSpecialById(Integer sid) {
		// TODO Auto-generated method stub
		return usersDao.queryMobileSpecialById(sid);
	}

	public Integer saveRemoteConsultation(RemoteConsultation rc) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.save(rc);
	}

	public void updateRemoteConsultation(RemoteConsultation rc) {
		// TODO Auto-generated method stub
		remoteConsultationDao.update(rc);
	}

	public List<DepartString> queryDepartStrings(Integer sid) {
		// TODO Auto-generated method stub
		return usersDao.queryDepartStrings(sid);
	}

	public List<RemoteConsultation> queryRemoteConsultationsByConditions(
			String openid, Integer pageNo, Integer pageSize, String stas) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsultationsByConditions(
				openid, pageNo, pageSize, stas);
	}

	public RemoteConsultation queryRemoteConsultationById(Integer oid) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.find(oid);
	}

	public List<MobileSpecial> queryMobileSpecialsByCurLocationAndExpert(
			Integer expertId, Integer cooHosId) {
		// TODO Auto-generated method stub
		return usersDao.queryMobileSpecialsByCurLocationAndExpert(expertId,
				cooHosId);
	}

	public CooHospitalDetails queryCooHosPitalDetailsById(Integer coid) {
		// TODO Auto-generated method stub
		return cooHospitalDetailsDao.find(coid);
	}

	public RemoteConsultation queryRemoteConsultationByTradeNo(String tradeNo) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsultationByTradeNo(tradeNo);
	}

	public Integer saveUserFeedback(UserFeedback fb) {
		// TODO Auto-generated method stub
		return userFeedbackDao.save(fb);
	}

	public List<CooHospitalDepartments> queryCooHospitalDepartmentsByHospitalAndExpert(
			Integer expertId, Integer cosid) {
		// TODO Auto-generated method stub
		return cooHospitalDepartmentsDao
				.queryCooHospitalDepartmentsByHospitalAndExpert(expertId, cosid);
	}

	public CooHospitalDepartments queryCooHospitalDepartmentsById(
			Integer coodepid) {
		// TODO Auto-generated method stub
		return cooHospitalDepartmentsDao.find(coodepid);
	}

	public Users queryUsersById(Integer uid) {
		// TODO Auto-generated method stub
		return usersDao.find(uid);
	}

	public Users queryUsersByMobilePhone(String phone) {
		// TODO Auto-generated method stub
		return usersDao.queryUsersByMobilePhone(phone);
	}

	public List<ExpertConsultationTime> queryExpertConTimes(Integer uid) {
		// TODO Auto-generated method stub
		return expertConsultationTimeDao
				.queryExpertConsultationTimeByExpertId(uid);
	}

	public ExpertConsultationTime queryExpertConTimeById(Integer tid) {
		// TODO Auto-generated method stub
		return expertConsultationTimeDao.find(tid);
	}

	public Integer saveExpertConTime(ExpertConsultationTime et) {
		// TODO Auto-generated method stub
		return expertConsultationTimeDao.save(et);
	}

	public void updateExpertConTime(ExpertConsultationTime et) {
		// TODO Auto-generated method stub
		expertConsultationTimeDao.update(et);
	}

	public void delExpertConTimeById(Integer tid) {
		// TODO Auto-generated method stub
		expertConsultationTimeDao.delete(tid);
	}

	public Map<String, Object> queryRemoteConsulations(Integer expertId,
			String search, String sortdir, Integer sortcol, Integer start,
			Integer length) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsulations(expertId, search,
				sortdir, sortcol, start, length);
	}

	public Map<String, Object> queryRemoteConsulations_doc(Integer docid,
			String search, String sortdir, Integer sortcol, Integer start,
			Integer length) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsulations_doc(docid, search,
				sortdir, sortcol, start, length);
	}

	public boolean isExistContactInfo(String openid, String username,
			String idcard, String telphone) {
		// TODO Auto-generated method stub
		ContactInfo ci = contactInfoDao.queryByConditions(openid, username,
				idcard, telphone);
		if (ci != null)
			return true;
		return false;
	}

	public Integer saveContactInfo(ContactInfo ci) {
		// TODO Auto-generated method stub
		return contactInfoDao.save(ci);
	}

	public List<ContactInfo> queryContactInfosByOpenId(String openid) {
		// TODO Auto-generated method stub
		return contactInfoDao.queryContactInfosByOpenId(openid);
	}

	public Integer saveGraphicsInfo(GraphicsInfo ginfo) {
		// TODO Auto-generated method stub
		return graphicsInfoDao.save(ginfo);
	}

	public void updateGraphicsInfo(GraphicsInfo ginfo) {
		// TODO Auto-generated method stub
		graphicsInfoDao.update(ginfo);
	}

	public GraphicsInfo queryGraphicsInfoByTradeNo(String tradeNo) {
		// TODO Auto-generated method stub
		return graphicsInfoDao.queryGraphicsInfoByTradeNo(tradeNo);
	}

	public List<SpecialistAppoint> querySpecialistAppointsBySid(Integer sid,
			String dtime) {
		// TODO Auto-generated method stub
		// 判断该专家当天是否已加满
		return specialistAppointDao.querySpecialistAppointsBySid(sid, dtime);
	}

	public Integer queryCountByPlused(Integer sid, String dtime) {
		// TODO Auto-generated method stub
		return faceDiagnosesDao.queryCountByPlused(sid, dtime);
	}

	public Integer saveFaceDiagnoses(FaceDiagnoses fd) {
		// TODO Auto-generated method stub
		return faceDiagnosesDao.save(fd);
	}

	public Integer saveCaseImage(CasesImage ci) {
		// TODO Auto-generated method stub
		return caseIamgeDao.save(ci);
	}

	public SpecialistAppoint querySpecialistAppointById(Integer said) {
		// TODO Auto-generated method stub
		return specialistAppointDao.find(said);
	}

	public Integer saveCases(Cases ca) {
		// TODO Auto-generated method stub
		return casesDao.save(ca);
	}

	public Integer saveOrders(Orders ord) {
		// TODO Auto-generated method stub
		return ordersDao.save(ord);
	}

	public FaceDiagnoses queryFaceDiagnosesById(Integer id) {
		// TODO Auto-generated method stub
		return faceDiagnosesDao.find(id);
	}

	public FaceDiagnoses queryFaceDiagnosesByTradeNo(String tradeNo) {
		// TODO Auto-generated method stub
		return faceDiagnosesDao.queryFaceDiagnosesByTradeNo(tradeNo);
	}

	public Orders queryOrdersById(Integer id) {
		// TODO Auto-generated method stub
		return ordersDao.find(id);
	}

	public void updateFaceDiagnoses(FaceDiagnoses fd) {
		// TODO Auto-generated method stub
		faceDiagnosesDao.update(fd);
	}

	public void updateOrders(Orders ord) {
		// TODO Auto-generated method stub
		ordersDao.update(ord);
	}

	public List<Cases> queryCasesListByUserId(Integer uid) {
		// TODO Auto-generated method stub
		return casesDao.queryCasesListByUserId(uid);
	}

	public Cases queryCasesById(Integer id) {
		// TODO Auto-generated method stub
		return casesDao.find(id);
	}

	public List<CasesImage> queryCaseImagesByCaseId(Integer caseid) {
		// TODO Auto-generated method stub
		return caseIamgeDao.queryCaseImagesByCaseId(caseid);
	}

	public Integer savePatients(Patients pa) {
		// TODO Auto-generated method stub
		return patientsDao.save(pa);
	}

	public CustomUpload queryCustomUploadByUrl(String url) {
		// TODO Auto-generated method stub
		return customUploadDao.queryCustomUploadByUrl(url);
	}

	public Map<String, Object> queryRemoteConsulationsByConditions(Integer uid,
			String searchContent, String sortdesc, Integer start,
			Integer length, String status, String now) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsulationsByConditions(uid,
				searchContent, sortdesc, start, length, status, now);
	}

	public Map<String, Object> queryRemoteConsulationsByConditions(
			Integer userId, Integer userType, String searchContent,
			String sortdesc, Integer start, Integer length, String status,
			String now) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsulationsByConditions(
				userId, userType, searchContent, sortdesc, start, length,
				status, now);
	}

	public void updateRemoteConsulationUserStatus(Integer orderid) {
		// TODO Auto-generated method stub
		remoteConsultationDao.updateRemoteConsulationUserStatus(orderid);
	}

	public Map<String, Object> generateConShedules(Integer expertId,
			String start, String end) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		List<ExpertConsultationTime> conTimes = expertConsultationTimeDao
				.queryExpertConsultationTimeByExpertId(expertId);
		if (conTimes != null && conTimes.size() > 0) {
			Integer weekday = null;
			List<String> times = null;
			List<String> timeslices = null;
			// 获取起始日期，及结束日期
			Map<String, String> startaend = new HashMap<String, String>();
			startaend.put("start", start);
			startaend.put("end", end);
			List<Integer> allids = new ArrayList<Integer>();
			for (ExpertConsultationTime _conTime : conTimes) {
				if (_conTime.getStatus().equals(1)) {
					// 开启
					weekday = _conTime.getWeekDay();
					try {
						times = CalendarUtil.gaintimes(start, end, weekday);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//
					if (times != null && times.size() > 0) {
						// 有模板日期返回
						// 拆分时间段
						timeslices = splitTimeToTimeSlices(
								_conTime.getStartTime(), _conTime.getEndTime(),
								15);
						allids.addAll(saveslicetimes(times, timeslices,
								_conTime));
					}
				}
			}
			// 删除没用的数据
			deletedirtydatas(expertId, startaend, allids);
		}
		return map;
	}

	private void deletedirtydatas(Integer expertId, Map<String, String> ste,
			List<Integer> allids) {
		expertConsultationScheduleDao.deletedirtydatas(expertId,
				ste.get("start"), ste.get("end"), allids);
	}

	private List<Integer> saveslicetimes(List<String> times,
			List<String> timeslices, ExpertConsultationTime conTime) {
		List<Integer> ids = new ArrayList<Integer>();
		if (timeslices != null && timeslices.size() > 0) {
			ExpertConsultationSchedule ecs = null;
			for (String _time : times) {
				for (String _lices : timeslices) {
					ecs = expertConsultationScheduleDao
							.queryScheduleByConditions(conTime.getExpertId(),
									_time, _lices);
					if (ecs == null) {
						ecs = new ExpertConsultationSchedule();
						ecs.setCost(conTime.getCost());
						ecs.setDuration(15);
						ecs.setExpertId(conTime.getExpertId());
						ecs.setStatus(1);
						ecs.setScheduleDate(_time);
						ecs.setStartTime(_lices);
						Integer id = expertConsultationScheduleDao.save(ecs);
						ids.add(id);
					} else {
						ids.add(ecs.getId());
					}
				}
			}
		}
		return ids;
	}

	private SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm");

	private List<String> splitTimeToTimeSlices(String startTime,
			String endTime, Integer intervalM) {
		List<String> list = new ArrayList<String>();
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

	public ExpertConsultationSchedule queryScheduleByConditions(
			Integer expertId, String scheduleDate, String startTime) {
		// TODO Auto-generated method stub
		return expertConsultationScheduleDao.queryScheduleByConditions(
				expertId, scheduleDate, startTime);
	}

	public List<ExpertConsultationSchedule> queryExpertConTimeSchedulsByConditions(
			Integer expertId, String scheduleDate) {
		// TODO Auto-generated method stub
		return expertConsultationScheduleDao
				.queryExpertConTimeSchedulsByConditions(expertId, scheduleDate);
	}

	public List<CooHospitalDepartments> queryCooHospitalDepartmentsByHospital(
			Integer hosid) {
		// TODO Auto-generated method stub
		return cooHospitalDepartmentsDao
				.queryCooHospitalDepartmentsByHospital(hosid);
	}

	public List<MobileSpecial> queryMobileSpecialsByLocalDepartId(Integer depid) {
		// TODO Auto-generated method stub
		return cooHospitalDepartmentsDao
				.queryMobileSpecialsByLocalDepartId(depid);
	}

	public ExpertConsultationSchedule queryExpertConScheduleById(Integer ecid) {
		// TODO Auto-generated method stub
		return expertConsultationScheduleDao.find(ecid);
	}

	public void updatExpertConScheduleDate(ExpertConsultationSchedule sch) {
		// TODO Auto-generated method stub
		expertConsultationScheduleDao.update(sch);
	}

	public List<ExpertConsultationTime> queryExperTimesByConditions(
			Integer expertId, Integer weekday, String timetype) {
		// TODO Auto-generated method stub
		return expertConsultationTimeDao.queryExperTimesByConditions(expertId,
				weekday, timetype);
	}

	public List<MobileSpecial> queryMobileSpecialsByConditions(Integer depid,
			String sdate, String timetype, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		Integer weekday = null;
		try {
			weekday = DateUtil.dayForWeek(sdate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<MobileSpecial> specials = usersDao
				.queryMobileSpecialsByConditions(depid, sdate, timetype,
						pageNo, pageSize);
		for (MobileSpecial _special : specials) {
			List<ExpertConsultationTime> times = queryExperTimesByConditions(
					_special.getSpecialId(), weekday, timetype);
			_special.setRegisStr(gaintimestr(times));
		}
		return specials;
	}

	public List<MobileSpecial> queryMobileSpecialsByConditions_newnurse(
			Integer depid, String sdate, String timetype, Integer pageNo,
			Integer pageSize) {
		// TODO Auto-generated method stub
		Integer weekday = null;
		try {
			weekday = DateUtil.dayForWeek(sdate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// List<MobileSpecial> specials=
		// usersDao.queryMobileSpecialsByConditions(depid, sdate,timetype,
		// pageNo, pageSize);
		List<MobileSpecial> specials = doctorRegisterInfoDao
				.queryMobileSpecialsByConditions_newnurse(depid, sdate,
						timetype, pageNo, pageSize);
		for (MobileSpecial _special : specials) {
			List<ExpertConsultationTime> times = queryExperTimesByConditions(
					_special.getSpecialId(), weekday, timetype);
			_special.setRegisStr(gaintimestr(times));
		}
		return specials;
	}

	private String gaintimestr(List<ExpertConsultationTime> times) {
		if (times == null || times.size() <= 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (ExpertConsultationTime _time : times) {
			sb.append(_time.getStartTime() + "-" + _time.getEndTime() + " ");
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		System.out.println("====timestirng===" + sb.toString());
		return sb.toString();
	}

	public List<ExpertConsultationSchedule> queryExpertConTimeSchedulsByConditions(
			Integer expertId, String sdate, String timetype) {
		// TODO Auto-generated method stub
		return expertConsultationScheduleDao
				.queryExpertConTimeSchedulsByConditions(expertId, sdate,
						timetype);
	}

	public Map<String, Object> queryRemoteConsultationsByConditions_nurse(
			Integer nurseid, Integer start, Integer length, String searchContent) {
		// TODO Auto-generated method stub
		return remoteConsultationDao
				.queryRemoteConsultationsByConditions_nurse(nurseid, start,
						length, searchContent);
	}

	public List<VedioRelative> queryVediosByOrderId(Integer oid) {
		// TODO Auto-generated method stub
		return vedioRelativeDao.queryVediosByOrderId(oid);
	}

	public UserAccountInfo queryUserAccountInfoByMobilePhone(String tel) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryUserAccountInfoByMobilePhone(tel);
	}

	public Integer saveUserAccountInfo(UserAccountInfo ua) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.save(ua);
	}

	public UserWeixinRelative queryUserWeiRelativeByOpenId(String openid) {
		// TODO Auto-generated method stub
		return userWeixinRelativeDao.queryUserWeiRelativeByOpenId(openid);
	}

	public Integer saveUserWeixinRelative(UserWeixinRelative uw) {
		// TODO Auto-generated method stub
		return userWeixinRelativeDao.save(uw);
	}

	public Integer saveUserContactInfo(UserContactInfo uc) {
		// TODO Auto-generated method stub
		return userContactInfoDao.save(uc);
	}

	public List<UserContactInfo> queryUserContactInfosByOpenId(String openid) {
		// TODO Auto-generated method stub
		return userContactInfoDao.queryUserContactInfosByOpenId(openid);
	}

	public boolean isExistUserContactInfo(String openid, String username,
			String idcard, String telphone) {
		// TODO Auto-generated method stub
		UserContactInfo uc = userContactInfoDao
				.queryUserContactInfoByConditions(openid, username, idcard,
						telphone);
		if (uc != null)
			return true;
		return false;
	}

	public List<HospitalDetailInfo> queryCoohospitalInfos() {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryCoohospitalInfos();
	}

	public HospitalDetailInfo queryHospitalDetailInfoById(Integer id) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryHospitalDetailInfoById(id);
	}

	public List<HospitalDepartmentInfo> queryCoohospitalDepartmentsByCooHos(
			Integer hosid) {
		// TODO Auto-generated method stub
		return hospitalDepartmentInfoDao
				.queryCoohospitalDepartmentsByCooHos(hosid);
	}

	public List<MobileSpecial> queryMobileSpecialsByLocalDepartId_new(
			Integer depid,Integer type) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryMobileSpecialsByLocalDepartId(depid,type);
	}

	public DoctorRegisterInfo queryDoctorRegisterInfo(String username,
			String password) {
		// TODO Auto-generated method stub
		return doctorRegisterInfoDao
				.queryDoctorRegisterInfo(username, password);
	}

	public HospitalDepartmentInfo queryHospitalDepartmentInfoById(Integer did) {
		// TODO Auto-generated method stub
		return hospitalDepartmentInfoDao.find(did);
	}

	public List<RemoteConsultation> queryRemoteConsultationOrdersByConditions(
			String openid, Integer pageNo, Integer pageSize, String status) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsultationOrdersByConditions(
				openid, pageNo, pageSize, status);
	}

	public List<CustomFileStorage> queryCustomFileStorageVedios(Integer oid) {
		// TODO Auto-generated method stub
		return customFileStorageDao.queryCustomFileStorageVedios(oid);
	}

	public Map<String, Object> queryRemoteConsulationsByConditions_new(
			Integer userId, Integer userType, String searchContent,
			String sortdesc, Integer start, Integer length, String status,
			String now) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsulationsByConditions_new(
				userId, userType, searchContent, sortdesc, start, length,
				status, now);
	}

	public Map<String, Object> queryRemoteConsulationsByConditions_docnew(
			Integer userId, Integer userType, String searchContent,
			String sortdesc, Integer start, Integer length, String status,
			String now) {
		// TODO Auto-generated method stub
		return remoteConsultationDao
				.queryRemoteConsulationsByConditions_docnew(userId, userType,
						searchContent, sortdesc, start, length, status, now);
	}

	public Map<String, Object> queryRemoteConsultationsByConditions_newnurse(

	Integer nurseid, Integer start, Integer length, String searchContent) {
		// TODO Auto-generated method stub
		return doctorRegisterInfoDao
				.queryRemoteConsultationsByConditions_newnurse(nurseid, start,
						length, searchContent);
	}

	public UserAccountInfo queryUserAccountInfoById(Integer uid) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.find(uid);
	}

	public HospitalDetailInfo queryCooHospitalInfoByCity(String city) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryCooHospitalInfoByCity(city);
	}

	public Integer saveDoctorEstimateInfo(DoctorEstimateInfo estimate) {
		// TODO Auto-generated method stub
		return doctorEstimateInfoDao.save(estimate);
	}

	public Map<String, Object> querySchedulesByTime(String begin, String end,
			Integer start, Integer length) {
		// TODO Auto-generated method stub
		return expertConsultationScheduleDao.querySchedulesByTime(begin, end,
				start, length);
	}

	public List<CustomFileStorage> queryCustomFileStorageImages(
			String relatedPics) {
		// TODO Auto-generated method stub
		return customFileStorageDao.queryCustomFileStorageImages(relatedPics);
	}

	public List<MobileSpecial> queryMobileSpecialsByConditionsPro(
			Map<String, Object> querymap, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryMobileSpecialsByConditionsPro(querymap,
				pageNo, pageSize);
	}

	public List<StandardDepartmentInfo> queryStandardDepartmentByBigDep(
			Integer bigdepid) {
		// TODO Auto-generated method stub
		return standardDepartmentInfoDao
				.queryStandardDepartmentByBigDep(bigdepid);
	}

	public List<HospitalDetailInfo> queryHospitalDetailsByDsitcode(
			String distcode,Integer type) {
		// TODO Auto-generated method stub
		List<HospitalDetailInfo> list= hospitalDetailInfoDao.queryHospitalDetailsByDsitcode(distcode,type);
		return list;
	}

	public Map<String, Object> queryRemoteConsulationsByConditions_hos(
			Integer hosid, String searchContent, String sortdesc,
			Integer start, Integer length, String status, String now) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsulationsByConditions_hos(
				hosid, searchContent, sortdesc, start, length, status, now);
	}

	public Map<String, Object> queryHospitalNursesByHosId(Integer hosid,
			String search, Integer start, Integer length, Integer utype) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryHospitalNursesByHosId(hosid, search,
				start, length, utype);
	}

	public Map<String, Object> queryHospitalDepartmentssByHosId(Integer hosid,
			String search, Integer start, Integer length,Integer ostatus) {
		// TODO Auto-generated method stub
		return hospitalDepartmentInfoDao.queryHospitalDepartmentssByHosId(
				hosid, search, start, length,ostatus);
	}

	public String queryStandardDepartsByDepId(Integer depid, Integer type) {
		// TODO Auto-generated method stub
		List<DepStandardR> dsrs = depStandardRDao.queryStandardDepartsByDepId(
				depid, type);
		StringBuilder sb = new StringBuilder();
		if (dsrs != null && dsrs.size() > 0) {
			for (DepStandardR _d : dsrs) {
				sb.append(_d.getStandName() + ",");
			}
			sb = sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	
	public DepStandardR queryDepStandardRByConditions(Integer sid, Integer did,
			Integer type) {
		// TODO Auto-generated method stub
		return depStandardRDao.queryDepStandardRByConditions(sid,did,type);
	}


	public Integer saveDepStandardR(DepStandardR dr) {
		// TODO Auto-generated method stub
		return depStandardRDao.save(dr);
	}


	public List<HospitalDepartmentInfo> queryHospitalDepartmentssByHosId(
			Integer hosid) {
		// TODO Auto-generated method stub
		return hospitalDepartmentInfoDao.queryHospitalDepartmentssByHosId(hosid);
	}

	
	public void updateHospitalDepartmentInfo(HospitalDepartmentInfo dep) {
		// TODO Auto-generated method stub
		hospitalDepartmentInfoDao.update(dep);
	}


	public Integer saveHospitalDepartmentInfo(HospitalDepartmentInfo dep) {
		// TODO Auto-generated method stub
		return hospitalDepartmentInfoDao.save(dep);
	}

	public List<MobileSpecial> queryDistributeDocs(Integer hosid, Integer depid) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryDistributeDocs(hosid,depid);
	}

	public Map<String, Object> queryHospitalDepartmentssBySystem(String search,
			Integer start, Integer length,Integer hosid,Integer type) {
		// TODO Auto-generated method stub
		return hospitalDepartmentInfoDao.queryHospitalDepartmentssBySystem(search, start, length,hosid,type);
	}

	public List<HospitalDetailInfo> queryHospitalDetailsALL() {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryHospitalDetailsALL();
	}

	public List<HospitalDepartmentInfo> queryHospitalDeparts() {
		// TODO Auto-generated method stub
		return hospitalDepartmentInfoDao.queryHospitalDeparts();
	}

	public List<StandardDepartmentInfo> queryStandardDepartments() {
		// TODO Auto-generated method stub
		return standardDepartmentInfoDao.queryStandardDepartments();
	}

	public RemoteConsultation queryRemoteConsulationsById_detail(Integer id) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsulationsById_detail(id);
	}

	public Map<String, Object> queryExpertsBySystem(String search,
			Integer start, Integer length,Integer status) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryExpertsBySystem(search, start, length,status);
	}

	public Map<String, Object> queryHospitalsBySystem(String search,
			Integer start, Integer length,Integer type) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryHospitalsBySystem(search, start, length,type);
	}

	public List<Integer> queryStandsBydep(Integer depid, Integer type) {
		// TODO Auto-generated method stub
		return standardDepartmentInfoDao.queryStandsBydep(depid, type);
	}

	public void updateHospitalDetailInfo(HospitalDetailInfo info) {
		// TODO Auto-generated method stub
		hospitalDetailInfoDao.update(info);
	}
	
	public Integer saveHospitalDetailInfo(HospitalDetailInfo info) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.save(info);
	}

	public List<Dictionary> queryDictionarysByParentId(Integer pid) {
		// TODO Auto-generated method stub
		return dictionaryDao.queryDictionarysByParentId(pid);
	}

	public void deleteStandConfig(Integer depid, Integer type) {
		// TODO Auto-generated method stub
		standardDepartmentInfoDao.deleteStandConfig(depid,type);
	}

	public void deleteStandConfigByCondition(String sds, Integer depid,
			Integer type) {
		// TODO Auto-generated method stub
		standardDepartmentInfoDao.deleteStandConfigByCondition(sds,depid,type);
	}

	public List<UserContactInfo> queryUserContactInfosByUserId(Integer uid) {
		// TODO Auto-generated method stub
		return userContactInfoDao.queryUserContactInfosByUserId(uid);
	}

	public UserContactInfo queryUserContactorByCondition(Integer uid,
			String username, String tel) {
		// TODO Auto-generated method stub
		return userContactInfoDao.queryUserContactorByCondition(uid, username, tel);
	}

	public UserContactInfo queryUserContactorInfoById(Integer cid) {
		// TODO Auto-generated method stub
		return userContactInfoDao.find(cid);
	}

	public Map<String, Object> queryhelporders(String search, Integer start,
			Integer length,Integer type) {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryhelporders(search,start,length,type);
	}

	public void updateUserAccountInfo(UserAccountInfo info) {
		// TODO Auto-generated method stub
		userAccountInfoDao.update(info);
	}

	public Integer saveUserFeedbackInfo(UserFeedBackInfo info) {
		// TODO Auto-generated method stub
		return userFeedBackInfoDao.save(info);
	}


	public List<ExpertConsultationSchedule> queryExpertConTimeSchedulsByExpertId(
			Integer expertId) {
		// TODO Auto-generated method stub
		return expertConsultationScheduleDao.queryExpertConTimeSchedulsByExpertId(expertId);
	}

	public Map<String, Object> queryHosWxPlusOrders(Integer docid,
			String search, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return hosAppointOrderDao.queryHosWxPlusOrders(docid, search, start, length);
	}

	public WeiAccessToken queryWeiAccessTokenByDocId(Integer docid) {
		// TODO Auto-generated method stub
		return weiAccessTokenDao.queryWeiAccessTokenByDocId(docid);
	}

	public Integer saveWeiAccessToken(WeiAccessToken wat) {
		// TODO Auto-generated method stub
		return weiAccessTokenDao.save(wat);
	}

	public UserAccountInfo queryUserAccountInfoByMobileNumber(String tel) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryUserAccountInfoByMobileNumber(tel);
	}

	public List<WenzhenBean> queryWenzhenOrdersByConditions(Integer userId,
			Integer pageNo, Integer pageSize, String flag) {
		// TODO Auto-generated method stub
		return businessWenZhenInfoDao.queryWenzhenOrdersByConditions(userId, pageNo, pageSize, flag);
	}

	public List<ReSourceBean> queryOpenCitys() {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryOpenCitys();
	}

	public Dictionary queryDictionaryById(Integer id) {
		// TODO Auto-generated method stub
		return dictionaryDao.find(id);
	}

	public Integer saveBusinessVedioOrder(BusinessVedioOrder order) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.save(order);
	}

	
	public List<RemoteConsultation> queryRemoteConsulations() {
		// TODO Auto-generated method stub
		return remoteConsultationDao.queryRemoteConsulations();
	}

	
	public Pager queryMobileSpecialsByLocalDepartId_newpager(Integer depid,
			Integer type, Pager pager) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryMobileSpecialsByLocalDepartId_newpager(depid, type, pager);
	}

	public Map<String, Object> queryHosWxFeedbacks(Integer docid,
			String search, Integer start, Integer length) {
		// TODO Auto-generated method stub
		return userFeedBackInfoDao.queryHosWxFeedbacks(docid,search,start,length);
	}

	public Integer saveBusinessOperativeInfo(BusinessOperativeInfo info) {
		// TODO Auto-generated method stub
		return businessOperativeInfoDao.save(info);
	}

	public BusinessOperativeInfo queryBusinessOperativeInfoByOid(Integer oid) {
		// TODO Auto-generated method stub
		return businessOperativeInfoDao.queryBusinessOperativeInfoByOid(oid);
	}

	public List<MobileSpecial> queryMobileSpecialsByLocalDepartId_new(
			String keywords) {
		// TODO Auto-generated method stub
		return userAccountInfoDao.queryMobileSpecialsByLocalDepartId_new(keywords);
	}

	public UserWeixinRelative queryUserWeiRelativeByUserId(Integer userid) {
		// TODO Auto-generated method stub
		return userWeixinRelativeDao.queryUserWeiRelativeByUserId(userid);
	}

	public Map<String, Object> querylocaldocs(String search, Integer start,
			Integer length,Integer status) {
		// TODO Auto-generated method stub
		return doctorRegisterInfoDao.querylocaldocs(search, start, length,status);
	}

	
	public void updateUserWeixinRelative(UserWeixinRelative rel) {
		// TODO Auto-generated method stub
		userWeixinRelativeDao.update(rel);
	}

	public UserDetailInfo queryUserDetailInfoById(Integer id) {
		// TODO Auto-generated method stub
		return userDetailInfoDao.find(id);
	}

	public Integer saveUserDetailInfo(UserDetailInfo ud) {
		// TODO Auto-generated method stub
		return userDetailInfoDao.save(ud);
	}

	public void updateUserDetailInfo(UserDetailInfo ud) {
		// TODO Auto-generated method stub
		userDetailInfoDao.update(ud);
	}

	public void updateUserContactInfo(UserContactInfo user) {
		// TODO Auto-generated method stub
		userContactInfoDao.update(user);
	}

	public UserContactInfo queryUserContactInfoByUuid(String uuid) {
		// TODO Auto-generated method stub
		return userContactInfoDao.queryUserContactInfoByUuid(uuid);
	}

	public Map<String, Object> gainvedioorderdatas(String search,
			Integer start, Integer length, Integer type) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.gainvedioorderdatas(search, start, length, type);
	}

	public MobileSpecial queryMobileSpecialByUserIdAndUserType(Integer uid) {
		// TODO Auto-generated method stub
		System.out.println("==="+userAccountInfoDao);
		return userAccountInfoDao.queryMobileSpecialByUserIdAndUserType(uid);
	}

	@Override
	public List<BusinessVedioOrder> getfinshvedioList(Integer sta,String startDate,String endDate, String hosid, String depid) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.getfinshvedioList(sta,startDate,endDate,hosid,depid);
	}

	@Override
	public BusinessVedioOrder vedioorderRemrk(Integer sta, Integer id) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.vedioorderRemrk(sta,id);
	}

	@Override
	public List<HospitalDetailInfo> queryhospitalInfos() {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryhospitalInfos();
	}

	@Override
	public List<HospitalDepartmentInfo> queryDeps(Integer hosid) {
		// TODO Auto-generated method stub
		return hospitalDepartmentInfoDao.queryDeps(hosid);
	}

	@Override
	public Map<String, Object> gainrecriveorderdatas(String search, Integer start, Integer length, Integer type) {
		// TODO Auto-generated method stub
		return businessVedioOrderDao.gainrecriveorderdatas(search,start,length,type);
	}

	@Override
	public LivePlanOrder queryPlanLiveOrderById(Integer liveId) {
		return livePlanOrderInfoDao.find(liveId);
	}

	@Override
	public void updatePlanLiveInfo(LivePlanOrder live) {
		livePlanOrderInfoDao.update(live);
	}

	@Override
	public Integer savePlanLiveInfo(LivePlanOrder live) {
		return livePlanOrderInfoDao.save(live);
	}

	@Override
	public void delPlanLiveInfo(Integer liveId) {
		// TODO Auto-generated method stub
		livePlanOrderInfoDao.delete(liveId);
	}
}
