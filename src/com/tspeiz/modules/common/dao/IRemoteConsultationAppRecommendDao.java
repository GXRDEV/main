package com.tspeiz.modules.common.dao;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.RemoteConsultationAppRecommend;

public interface IRemoteConsultationAppRecommendDao extends BaseDao<RemoteConsultationAppRecommend>{
	public List<RemoteConsultationAppRecommend> getRemoteConsultationAppRecommendsByUuid(String uuid);
}
