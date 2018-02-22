package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.HelpBean;
import com.tspeiz.modules.common.bean.dto.DocFollowDto;
import com.tspeiz.modules.common.bean.dto.DoctorAboutCount;
import com.tspeiz.modules.common.bean.dto.DoctorIncomeDto;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;

public interface IDoctorDetailInfoDao extends BaseDao<DoctorDetailInfo>{
	public List<DoctorDetailInfo> queryDoctorDetailsByExpert();
	public Map<String,Object> queryNewExpertsAdd(List<String> dates);
	public Integer queryNewExpertsAdd_t(String date);
	public MobileSpecial queryAuditDocDetailById(Integer id);
	public List<MobileSpecial> queryCooDocsByDep(Integer depid);
	public List<MobileSpecial> querydoctors(String search,String serviceid,String depid,String distcode,Integer _pageNo,Integer pageSize);
	public Map<String,Object> queryoperatordatas(String search,Integer ostatus,Integer start,Integer length);
	public List<HelpBean> queryOpenPros(String type);
	public List<HelpBean> queryOpenCitys(String pdist,String type,Integer stype);
	public List<MobileSpecial> queryDoctorDetailInfoByHosId(Integer hosId);
	public MobileSpecial getDoctorDetailList(Integer docId);
	public DoctorDetailInfo getLocalDocTel(Integer localDoctorId);
	public Map<String, Object> gaininvitDocdatas(String searchContent, Integer start, Integer length, String invitCode);
	public Map<String,Object> queryDoctor(Map<String,Object> queryMap,Integer start,Integer length);
	public Map<String, Object> aboutdocsdatas(Map<String, Object> querymap, Integer start, Integer length);
	public Map<String,Object> queryNewAddDoctors(List<String> dates,String queryType);
	public List<DoctorAboutCount> gainDocAttentCount(String hosid, String depid, String startDate, String endDate);
	public List<DoctorAboutCount> gainDocreportCount(String hosid, String depid, String startDate, String endDate);
	public Map<String, Object> docreportaboutdatass(Integer ostatus, String searchContent, Integer start,
			Integer length);
	public List<MobileSpecial> gainDocregisterexport(String hosid, String depid, String startDate, String endDate);
	public List<DoctorIncomeDto> docincomeexport(String hid, String depid, String startDate, String endDate);
	public Map<String, Object> queryDocFollowDatas(String search,
			Integer start, Integer length, Integer type);
	public List<DocFollowDto> queryDocFollowDetailData(Integer type,String orderUuid,String docId,String subUserId);
	public Map<String, Object> userWarmDocDatas(Integer ostatus,String searchContent, Integer start, Integer length);
}
