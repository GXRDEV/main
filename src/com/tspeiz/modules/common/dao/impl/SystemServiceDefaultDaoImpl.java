package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.ISystemServiceDefaultDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.SystemServiceDefault;

@Repository
public class SystemServiceDefaultDaoImpl extends BaseDaoImpl<SystemServiceDefault> implements ISystemServiceDefaultDao{
	@SuppressWarnings("unchecked")
	public SystemServiceDefault querySystemServiceDefaultByCon(
			Integer serviceId, Integer dutyId) {
		// TODO Auto-generated method stub
		String hql=" from SystemServiceDefault where serviceId="+serviceId+" and dutyId="+dutyId;
		List<SystemServiceDefault> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

}
