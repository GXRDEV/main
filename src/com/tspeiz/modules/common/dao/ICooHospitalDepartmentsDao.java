package com.tspeiz.modules.common.dao;

import java.util.List;

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.CooHospitalDepartments;

public interface ICooHospitalDepartmentsDao extends BaseDao<CooHospitalDepartments> {
	public List<CooHospitalDepartments> queryCooHospitalDepartmentsByHospitalAndExpert(Integer expertId,Integer cooHosId);
	public List<CooHospitalDepartments> queryCooHospitalDepartmentsByHospital(
			Integer hosid);
	public List<MobileSpecial> queryMobileSpecialsByLocalDepartId(Integer depid);
}
