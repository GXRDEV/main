package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IRemoteConsultationAppRecommendDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.RemoteConsultationAppRecommend;

@Repository
public class RemoteConsultationAppRecommendDaoImpl extends BaseDaoImpl<RemoteConsultationAppRecommend> implements IRemoteConsultationAppRecommendDao{

	@SuppressWarnings("unchecked")
	public List<RemoteConsultationAppRecommend> getRemoteConsultationAppRecommendsByUuid(
			String uuid) {
		// TODO Auto-generated method stub
		String hql="from RemoteConsultationAppRecommend where 1=1 and uuid='"+uuid+"' ";
		return this.hibernateTemplate.find(hql);
	}
	
}
