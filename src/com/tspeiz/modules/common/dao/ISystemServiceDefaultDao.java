package com.tspeiz.modules.common.dao;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.SystemServiceDefault;

public interface ISystemServiceDefaultDao extends BaseDao<SystemServiceDefault>{
	public SystemServiceDefault querySystemServiceDefaultByCon(Integer serviceId,Integer dutyId);
}
