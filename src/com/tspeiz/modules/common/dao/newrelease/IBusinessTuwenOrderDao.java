package com.tspeiz.modules.common.dao.newrelease;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;

public interface IBusinessTuwenOrderDao extends BaseDao<BusinessTuwenOrder>{
	public boolean queryExistNoClosedTwOrder(Integer uid);
	public BusinessTuwenOrder queryBusinessTuwenInfoByUid(String uuid);
}
