package com.tspeiz.modules.common.dao.weixin;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.weixin.UserExternals;

public interface IUserExternalsDao extends BaseDao<UserExternals> {
	public UserExternals queryUserExternalByExternalId(String externalId);
}
