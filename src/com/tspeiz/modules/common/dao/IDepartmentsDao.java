package com.tspeiz.modules.common.dao;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.Departments;

public interface IDepartmentsDao extends BaseDao<Departments>{
	public List<Departments> queryDepartments();
	public List<Departments> queryDepartmentByBigDep(Integer bigid);
}
