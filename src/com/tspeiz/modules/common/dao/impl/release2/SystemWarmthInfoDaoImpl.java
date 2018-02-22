package com.tspeiz.modules.common.dao.impl.release2;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.ISystemWarmthInfoDao;
import com.tspeiz.modules.common.entity.release2.SystemWarmthInfo;
@Repository
public class SystemWarmthInfoDaoImpl extends BaseDaoImpl<SystemWarmthInfo> implements ISystemWarmthInfoDao{

	@SuppressWarnings("unchecked")
	public List<SystemWarmthInfo> querysystemwarms() {
		// TODO Auto-generated method stub
		String hql=" from SystemWarmthInfo where status=1 ";
		return this.hibernateTemplate.find(hql);
	}
	
}
