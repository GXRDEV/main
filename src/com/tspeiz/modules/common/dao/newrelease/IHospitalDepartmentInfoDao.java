package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;

public interface IHospitalDepartmentInfoDao extends BaseDao<HospitalDepartmentInfo>{
	public List<HospitalDepartmentInfo> queryCoohospitalDepartmentsByCooHos(Integer hosid);
	
	public Map<String,Object> queryHospitalDepartmentssByHosId(Integer hosid,String search,Integer start,Integer length,Integer ostatus);
	public List<HospitalDepartmentInfo> queryHospitalDepartmentssByHosId(Integer hosid);
	public Map<String,Object> queryHospitalDepartmentssBySystem(String search,Integer start,Integer length,Integer hosid,Integer type);
	public List<HospitalDepartmentInfo> queryHospitalDeparts();
	public List<HospitalDepartmentInfo> queryDeps(Integer hosid);
}
