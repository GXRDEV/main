package com.tspeiz.modules.common.dao.coupon;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.coupon.MoneyExchange;

public interface IMoneyExchangeDao extends BaseDao<MoneyExchange>{
	public Map<String,Object> queryMoneyExchangeCodes(String searchContent,Integer start,Integer length,Integer type);
	public MoneyExchange queryMoneyExchangeByConditions(String code);
}
