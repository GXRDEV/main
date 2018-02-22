package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IBusinessTuwenOrderDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenZhenInfo;
@Repository
public class BusinessTuwenOrderDaoImpl extends BaseDaoImpl<BusinessTuwenOrder> implements IBusinessTuwenOrderDao{

	@SuppressWarnings("unchecked")
	public boolean queryExistNoClosedTwOrder(Integer uid) {
		// TODO Auto-generated method stub
		String hql = "from BusinessTuwenOrder where 1=1 and userId=" + uid
				+ " and status in(10,20) ";
		List<BusinessWenZhenInfo> list = this.hibernateTemplate.find(hql);
		if (list != null && list.size() > 0)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public BusinessTuwenOrder queryBusinessTuwenInfoByUid(String uuid) {
		// TODO Auto-generated method stub
		String hql="from BusinessTuwenOrder where 1=1 and uuid='"+uuid+"' ";
		List<BusinessTuwenOrder> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0) return list.get(0);
		return null;
	}
	
}
