package com.tspeiz.modules.common.dao.newrelease;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.UserWeixinRelative;

public interface IUserWeixinRelativeDao extends BaseDao<UserWeixinRelative>{
	public UserWeixinRelative queryUserWeiRelativeByOpenId(String openid);

	public UserWeixinRelative queryUserWeiRelativeByUserId(Integer userid);
	public UserWeixinRelative queryUserWeixinRelativeByCondition(String appId,Integer userId);
}
