package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;

public interface IHospitalDetailInfoDao extends BaseDao<HospitalDetailInfo> {
	public List<HospitalDetailInfo> queryCoohospitalInfos();

	public HospitalDetailInfo queryHospitalDetailInfoById(Integer id);

	public HospitalDetailInfo queryCooHospitalInfoByCity(String city);

	public List<HospitalDetailInfo> queryHospitalDetailsByDsitcode(
			String distcode,Integer type);

	public List<HospitalDetailInfo> queryHospitalDetailsALL();

	public Map<String, Object> queryHospitalsBySystem(String search,
			Integer start, Integer length,Integer type);

	public List<HospitalDetailInfo> queryHospitalsByPage(Integer pageNo,
			Integer pageSize);
	public Map<String,Integer> queryExpertAndDepNumberByHos(Integer hosid);
	
	public Pager searchspecialsByPager(Pager pager);
	public Pager searchspecialsByPager_advice(Pager pager);
	
	public List<ReSourceBean> queryOpenCitys();
	
	public Pager queryMobileSpecialsByLocalDepartId_newpager(Integer depid,Integer type,Pager pager);
	public Pager searchspecialsByPager_remote(Pager pager);
	
	public Map<String,Object> queryNewAddHospital(List<String> dates);
	
	public List<HospitalDetailInfo> queryHospitals_expert();
	public List<HospitalDetailInfo> querynearhoses(String ispage,String htype,String distcode,Integer _pageNo,Integer _pageSize);
	public List<HospitalDetailInfo> queryHospitalsByCon(String status,String aids);
	public List<HospitalDetailInfo> queryhighlevelhos(Integer allianceId,String levels);
	public Map<String,Object> operatorhosdatas(String searchContent,Integer opId,Integer start,Integer length);
	public List<HospitalDetailInfo> queryhospitalInfos();
}
