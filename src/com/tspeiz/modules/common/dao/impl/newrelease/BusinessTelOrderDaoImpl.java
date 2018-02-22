package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IBusinessTelOrderDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessTelOrder;
@Repository
public class BusinessTelOrderDaoImpl extends BaseDaoImpl<BusinessTelOrder> implements IBusinessTelOrderDao{

	@SuppressWarnings("unchecked")
	public BusinessTelOrder queryBusinessTelOrderByUid(String uuid) {
		// TODO Auto-generated method stub
		String hql="from BusinessTelOrder where 1=1 and uuid='"+uuid+"' ";
		List<BusinessTelOrder> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0) return list.get(0);
		return null;
	}

}
