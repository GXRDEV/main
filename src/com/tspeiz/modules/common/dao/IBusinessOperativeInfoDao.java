package com.tspeiz.modules.common.dao;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.BusinessOperativeInfo;

public interface IBusinessOperativeInfoDao extends BaseDao<BusinessOperativeInfo>{
	public BusinessOperativeInfo queryBusinessOperativeInfoByOid(Integer oid);
}
