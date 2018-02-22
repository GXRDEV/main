package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IBusinessPayInfoDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessPayInfo;

@Repository
public class BusinessPayInfoDaoImpl extends BaseDaoImpl<BusinessPayInfo> implements IBusinessPayInfoDao{
	@SuppressWarnings("unchecked")
	public BusinessPayInfo queryBusinessPayInfoByTradeNo(String tradeno) {
		// TODO Auto-generated method stub
		String hql="from BusinessPayInfo where 1=1 and outTradeNo='"+tradeno+"' ";	
		List<BusinessPayInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	
	@SuppressWarnings("unchecked")
	public List<BusinessPayInfo> queryBusinesPayInfosByOId(Integer oid,
			Integer type) {
		// TODO Auto-generated method stub
		String hql="from BusinessPayInfo where 1=1 and orderId="+oid+" and orderType="+type;
		return this.hibernateTemplate.find(hql);
	}


	@SuppressWarnings("unchecked")
	public BusinessPayInfo queryBusinessPayInfoByCondition(Integer oid,
			Integer otype) {
		// TODO Auto-generated method stub
		String hql="from BusinessPayInfo where 1=1 and orderId="+oid+" and orderType="+otype+" and payStatus=1 ";
		List<BusinessPayInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
}
