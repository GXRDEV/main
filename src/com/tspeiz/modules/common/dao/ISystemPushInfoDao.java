package com.tspeiz.modules.common.dao;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.SystemPushInfo;

public interface ISystemPushInfoDao extends BaseDao<SystemPushInfo>{
	public List<SystemPushInfo> querySystemPushInfoByUser(Integer userId);
	public SystemPushInfo querySystemPushInfoByBusKey(String businessKey);
	public SystemPushInfo querySystemPushInfoByPushKey(String pushKey);
	public Integer getUnReadPushInfo(Integer userId, Integer userType);
}
