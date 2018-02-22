package com.tspeiz.modules.common.dao;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.OrderSerialNumber;

public interface IOrderSerialNumberDao extends BaseDao<OrderSerialNumber> {
	
	public Integer getOrderSerialNumberByOrderType(Integer orderType);
	public String getOrderNumberByOrderType(Integer orderType);
}
