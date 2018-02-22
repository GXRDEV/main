package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;

public interface IDoctorRegisterInfoDao extends BaseDao<DoctorRegisterInfo>{
	public List<DoctorRegisterInfo> queryDoctorRegs();
	public DoctorRegisterInfo queryDoctorRegisterInfo(String username,
			String password) ;
	public DoctorRegisterInfo queryDoctorRegisterInfoByTel(String tel);
	public List<MobileSpecial> queryMobileSpecialsByConditions_newnurse(Integer depid,
			String sdate,String timetype, Integer pageNo, Integer pageSize);
	public Map<String,Object> queryRemoteConsultationsByConditions_newnurse(Integer nurseid,Integer start,Integer length,String searchContent);
	
	public DoctorRegisterInfo queryDoctorRegisterInfoByTel(String tel,Integer utype);
	
	public DoctorRegisterInfo queryDoctorRegisterInfoByLoginName(String loginName);
	public DoctorRegisterInfo queryDoctorRegisterInfoByHosAndType(Integer hosid,Integer type);
	public Map<String,Object> queryNeedAuditDocs(Integer status,String search,Integer start,Integer length);
	public Map<String,Object> querylocaldocs(String search,Integer start,Integer length,Integer status);
}
