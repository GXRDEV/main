package com.tspeiz.modules.common.dao;

import java.util.List;

import com.tspeiz.modules.common.bean.weixin.DepartString;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.Users;

public interface IUsersDao extends BaseDao<Users>{
	public List<MobileSpecial> queryMobileSpecialsByConditions(Integer depid,String scontent,String scity,String spro,
			Integer pageNo, Integer pageSize,String stype);
	public Users queryUsersByLogin(String username, String password,
			String stype);
	public MobileSpecial queryMobileSpecialById(Integer sid);
	public List<DepartString> queryDepartStrings(Integer sid);
	public List<MobileSpecial> queryMobileSpecialsByCurLocationAndExpert(Integer expertId,Integer cooHosId);
	public Users queryUsersByMobilePhone(String phone);
	public Users queryUsersByMobileDE(String tel);
	
	public MobileSpecial queryMobileSpecialByDoctorId(Integer doctorId);
	public MobileSpecial queryMobileSpecialByExpertId(Integer expertId);
	public MobileSpecial queryMobileSpecialByNurseId(Integer nurseId);
	public List<MobileSpecial> queryMobileSpecialsByConditions(Integer depid,String sdate,String timetype,Integer pageNo,Integer pageSize);
	
}
