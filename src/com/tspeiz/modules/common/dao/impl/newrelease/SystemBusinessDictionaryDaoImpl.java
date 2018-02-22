package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.ISystemBusinessDictionaryDao;
import com.tspeiz.modules.common.entity.newrelease.SystemBusinessDictionary;
@Repository
public class SystemBusinessDictionaryDaoImpl extends BaseDaoImpl<SystemBusinessDictionary> implements ISystemBusinessDictionaryDao{
	@SuppressWarnings("unchecked")
	public List<SystemBusinessDictionary> querySystemBusinessDictionarysByGroup(
			Integer groupId) {
		// TODO Auto-generated method stub
		String hql = "from SystemBusinessDictionary where 1=1 and groupId = "+groupId;
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SystemBusinessDictionary querySysDicByGroupAndCode(Integer group,
			Integer code) {
		// TODO Auto-generated method stub
		String hql = "from SystemBusinessDictionary where 1=1 and groupId = "+group+" and code="+code;
		List<SystemBusinessDictionary> list = this.hibernateTemplate.find(hql);
		if(list != null && list.size()>0) return list.get(0);
		return null;
	}
	
}
