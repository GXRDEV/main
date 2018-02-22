package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessPayInfo;

public interface IBusinessPayInfoDao extends BaseDao<BusinessPayInfo>{
	public BusinessPayInfo queryBusinessPayInfoByTradeNo(String tradeno);
	public List<BusinessPayInfo> queryBusinesPayInfosByOId(Integer oid,
			Integer type);
	public BusinessPayInfo queryBusinessPayInfoByCondition(Integer oid,Integer otype);
}
