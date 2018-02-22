package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IDepartmentsDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.Departments;

@Repository
public class DepartmentsDaoImpl extends BaseDaoImpl<Departments> implements IDepartmentsDao{

	@SuppressWarnings("unchecked")
	public List<Departments> queryDepartments() {
		// TODO Auto-generated method stub
		String hql="from Departments where 1=1 order by rank";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<Departments> queryDepartmentByBigDep(Integer bigid) {
		// TODO Auto-generated method stub
		String hql="from Departments where 1=1 and bigDepartmentId="+bigid+"order by rank";
		return this.hibernateTemplate.find(hql);
	}

}
