package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;

public interface IStandardDepartmentInfoDao extends BaseDao<StandardDepartmentInfo>{
	public List<StandardDepartmentInfo> queryStandardDepartmentByBigDep(
			Integer bigdepid);
	public List<StandardDepartmentInfo> queryStandardDepartments();
	
	public List<Integer> queryStandsBydep(Integer depid,Integer type);
	
	public void deleteStandConfig(Integer depid,Integer type);
	
	public void deleteStandConfigByCondition(String sds, Integer depid,
			Integer type);
	
	public List<StandardDepartmentInfo> querystanddeps(String ispage,String ishot,Integer pageNo,Integer pageSize);
}
