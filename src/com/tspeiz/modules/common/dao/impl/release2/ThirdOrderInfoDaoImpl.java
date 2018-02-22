package com.tspeiz.modules.common.dao.impl.release2;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IThirdOrderInfoDao;
import com.tspeiz.modules.common.entity.release2.ThirdOrderInfo;

@Repository
public class ThirdOrderInfoDaoImpl extends BaseDaoImpl<ThirdOrderInfo> implements IThirdOrderInfoDao{

	@SuppressWarnings("unchecked")
	@Override
	public ThirdOrderInfo queryThirdOrderInfoByOrderUuid(String orderUuid) {
		// TODO Auto-generated method stub
		String hql = "from ThirdOrderInfo where 1=1 and orderUuid='"+orderUuid+"' ";
		List<ThirdOrderInfo> list = this.hibernateTemplate.find(hql);
		if(list != null && list.size()>0) return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ThirdOrderInfo queryThirdOrderInfoByConsultationId(String conId) {
		// TODO Auto-generated method stub
		String hql = "from ThirdOrderInfo where 1=1 and consultationId='"+conId+"' ";
		List<ThirdOrderInfo> list = this.hibernateTemplate.find(hql);
		if(list != null && list.size()>0) return list.get(0);
		return null;
	}
	
}
