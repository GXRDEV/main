package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IShortUrlRelateDao;
import com.tspeiz.modules.common.entity.newrelease.ShortUrlRelate;

@Repository
public class ShortUrlRelateDaoImpl extends BaseDaoImpl<ShortUrlRelate> implements IShortUrlRelateDao{
	@SuppressWarnings("unchecked")
	public ShortUrlRelate queryShortUrlRelate(String code) {
		// TODO Auto-generated method stub
		String hql="from ShortUrlRelate where shortCode='"+code+"' ";
		List<ShortUrlRelate> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public ShortUrlRelate queryShortUrlRelate(String ltype, String oid) {
		// TODO Auto-generated method stub
		String hql="from ShortUrlRelate where orderType="+ltype+" and orderId="+oid;
		List<ShortUrlRelate> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

}
