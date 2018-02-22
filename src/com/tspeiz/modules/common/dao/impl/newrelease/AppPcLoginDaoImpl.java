package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IAppPcLoginDao;
import com.tspeiz.modules.common.entity.newrelease.AppPcLogin;

@Repository
public class AppPcLoginDaoImpl extends BaseDaoImpl<AppPcLogin> implements IAppPcLoginDao{

	@SuppressWarnings("unchecked")
	public AppPcLogin queryAppPcLoginByKeyId(String keyid) {
		// TODO Auto-generated method stub
		String hql="from AppPcLogin where keyId='"+keyid+"' ";
		List<AppPcLogin> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
}
