package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageInfo;

public interface IBusinessMessageInfoDao extends BaseDao<BusinessMessageInfo>{
	public List<BusinessMessageInfo> queryBusinessMessagesByTwId(Integer twid);
	public List<BusinessMessageInfo> queryBusinessMsgs();
}
