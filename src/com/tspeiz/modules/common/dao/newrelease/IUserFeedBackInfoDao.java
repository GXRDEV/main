package com.tspeiz.modules.common.dao.newrelease;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.UserFeedBackInfo;

public interface IUserFeedBackInfoDao extends BaseDao<UserFeedBackInfo>{
	public Map<String, Object> queryHosWxFeedbacks(Integer docid,
			String search, Integer start, Integer length);
}
