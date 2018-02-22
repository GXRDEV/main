package com.tspeiz.modules.common.dao.impl.weixin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.weixin.IUserExternalsDao;
import com.tspeiz.modules.common.entity.weixin.UserExternals;

@Repository
public class UserExternalsDaoImpl extends BaseDaoImpl<UserExternals> implements
		IUserExternalsDao {

	@SuppressWarnings("unchecked")
	public UserExternals queryUserExternalByExternalId(String externalId) {
		// TODO Auto-generated method stub
		String hql="from UserExternals where 1=1 and externalId='"+externalId+"'";
		List<UserExternals> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
}
