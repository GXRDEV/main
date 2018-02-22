package com.tspeiz.modules.common.dao;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.RemoteConsultationApp;

public interface IRemoteConsultationAppDao extends BaseDao<RemoteConsultationApp>{
	public RemoteConsultationApp getRemoteConsultationAppByUuid(String uuid);
}
