package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IBusinessOrderInfoDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessOrderInfo;
@Repository
public class BusinessOrderInfoDaoImpl extends BaseDaoImpl<BusinessOrderInfo> implements IBusinessOrderInfoDao{

	@SuppressWarnings("unchecked")
	public BusinessOrderInfo queryBusinessOrderInfoByFlowNo(String tradeNo) {
		// TODO Auto-generated method stub
		String hql="from BusinessOrderInfo where 1=1 and flowNumber='"+tradeNo+"' ";
		List<BusinessOrderInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	
	@SuppressWarnings("unchecked")
	public List<BusinessOrderInfo> queryBusinessOrderInfosByOId(Integer oid,Integer otype) {
		// TODO Auto-generated method stub
		String hql="from BusinessOrderInfo where 1=1 and wenZhen_Id="+oid+" and orderType="+otype;
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public BusinessOrderInfo queryBusinessOrderInfoByOId(Integer oid,
			Integer type) {
		// TODO Auto-generated method stub
		String hql="from BusinessOrderInfo where 1=1 and wenZhen_Id="+oid+" and orderType="+type+" order by createTime desc";
		List<BusinessOrderInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
	
}
