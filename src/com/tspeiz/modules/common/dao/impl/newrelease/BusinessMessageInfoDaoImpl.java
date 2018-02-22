package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IBusinessMessageInfoDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageInfo;

@Repository
public class BusinessMessageInfoDaoImpl extends BaseDaoImpl<BusinessMessageInfo> implements IBusinessMessageInfoDao{

	@SuppressWarnings("unchecked")
	public List<BusinessMessageInfo> queryBusinessMessagesByTwId(Integer twid) {
		// TODO Auto-generated method stub
		String hql="from BusinessMessageInfo where 1=1 and wenZhen_Id="+twid+" AND FromId IS NOT NULL";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<BusinessMessageInfo> queryBusinessMsgs() {
		// TODO Auto-generated method stub
		String hql="from BusinessMessageInfo where 1=1 ";
		return this.hibernateTemplate.find(hql);
	}
	
	
}
