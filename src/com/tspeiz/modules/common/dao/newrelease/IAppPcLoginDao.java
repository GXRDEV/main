package com.tspeiz.modules.common.dao.newrelease;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.AppPcLogin;

public interface IAppPcLoginDao extends BaseDao<AppPcLogin>{
	public AppPcLogin queryAppPcLoginByKeyId(String keyid);
}
