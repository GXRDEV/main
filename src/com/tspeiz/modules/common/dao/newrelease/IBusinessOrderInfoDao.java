package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessOrderInfo;

public interface IBusinessOrderInfoDao extends BaseDao<BusinessOrderInfo>{
	public BusinessOrderInfo queryBusinessOrderInfoByFlowNo(String tradeNo);
	public List<BusinessOrderInfo> queryBusinessOrderInfosByOId(Integer oid,Integer otype);
	public BusinessOrderInfo queryBusinessOrderInfoByOId(Integer oid,Integer type);
}
