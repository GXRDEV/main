package com.tspeiz.modules.common.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.dcm.InstanceRelate;
import com.tspeiz.modules.common.dao.IInstanceRelateDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;

public class InstanceRelateDaoImpl extends BaseDaoImpl<InstanceRelate> implements IInstanceRelateDao{

	@SuppressWarnings("unchecked")
	public InstanceRelate queryInstanceRelateByInstPk(BigInteger ipk) {
		// TODO Auto-generated method stub
		String hql="from InstanceRelate where instFk="+ipk;
		List<InstanceRelate> list=this.pacsHibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

}
