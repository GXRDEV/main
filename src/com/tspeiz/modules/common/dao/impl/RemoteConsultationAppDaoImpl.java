package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IRemoteConsultationAppDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.RemoteConsultationApp;

@Repository
public class RemoteConsultationAppDaoImpl extends BaseDaoImpl<RemoteConsultationApp> implements IRemoteConsultationAppDao{

	@SuppressWarnings("unchecked")
	public RemoteConsultationApp getRemoteConsultationAppByUuid(String uuid) {
		// TODO Auto-generated method stub
		String hql="from RemoteConsultationApp where 1=1 and uuid='"+uuid+"' ";
		List<RemoteConsultationApp> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
}
