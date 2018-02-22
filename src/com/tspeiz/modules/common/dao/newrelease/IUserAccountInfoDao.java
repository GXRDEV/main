package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.UserAccountInfo;

public interface IUserAccountInfoDao extends BaseDao<UserAccountInfo>{
	public UserAccountInfo queryUserAccountInfoByMobilePhone(String tel);
	
	public List<MobileSpecial> queryMobileSpecialsByLocalDepartId(Integer depid,Integer type);
	
	public MobileSpecial queryMobileSpecialByUserIdAndUserType(Integer user);
	
	public List<MobileSpecial> queryMobileSpecialsByConditionsPro(Map<String,Object> querymap,Integer pageNo,Integer pageSize);
	
	public Map<String,Object> queryHospitalNursesByHosId(Integer hosid,String search,Integer start,Integer length,Integer utype);
	
	public List<MobileSpecial> queryDistributeDocs(Integer hosid, Integer depid);
	
	public Map<String,Object> queryExpertsBySystem(String search,Integer start,Integer lenght,Integer status);
	
	public UserAccountInfo queryUserAccountInfoByMobileNumber(String tel);
	
	public List<HospitalDetailInfo> queryDocRelativeHospitals(Integer docid);
	public List<StandardDepartmentInfo> queryDocRelativeDepparts(Integer docid);
	public Pager queryMobileSpecial_helporder(Pager pager,Integer type);
	public List<MobileSpecial> queryMobileSpecialsByLocalDepartId_new(String keywords);
	public List<MobileSpecial> queryExperts_wx(Integer pageNo,Integer pageSize,Integer depid,String hosid,String standdepid,String zc,String keywords);
	public Pager queryMobileSpecial_loaddoc(Pager pager);
	public Pager queryMobileSpecial_loadExOrdoc(Pager pager);
	public MobileSpecial queryHosAdminMobileSpecialByHosId(Integer hosId);
	public List<MobileSpecial> getServiceList(String hosid, String startDate, String endDate, Integer isOpenvedio, Integer isOpentuwen, String depid);
	public Map<String,Object> queryNewAddPatients(List<String> dates,String queryType);	
}
