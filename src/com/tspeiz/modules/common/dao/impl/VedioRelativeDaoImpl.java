package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IVedioRelativeDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.VedioRelative;

@Repository
public class VedioRelativeDaoImpl extends BaseDaoImpl<VedioRelative> implements IVedioRelativeDao{
	@SuppressWarnings("unchecked")
	public List<VedioRelative> queryVediosByOrderId(Integer oid) {
		// TODO Auto-generated method stub
		String hql="from VedioRelative where orderId="+oid+" order by createTime desc";
		return this.hibernateTemplate.find(hql);
	}
	
}
