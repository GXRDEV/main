package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IBusinessOperativeInfoDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.BusinessOperativeInfo;

@Repository
public class BusinessOperativeInfoDaoImpl extends BaseDaoImpl<BusinessOperativeInfo> implements IBusinessOperativeInfoDao{

	@SuppressWarnings("unchecked")
	public BusinessOperativeInfo queryBusinessOperativeInfoByOid(Integer oid) {
		// TODO Auto-generated method stub
		String hql="from BusinessOperativeInfo where 1=1 and orderId="+oid;
		List<BusinessOperativeInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
}
