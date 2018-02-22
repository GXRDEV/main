package com.tspeiz.modules.common.dao.newrelease;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.HosAppointOrder;

public interface IHosAppointOrderDao extends BaseDao<HosAppointOrder>{
	public Map<String,Object> queryHosWxPlusOrders(Integer docid,String search,Integer start,Integer length);
}
