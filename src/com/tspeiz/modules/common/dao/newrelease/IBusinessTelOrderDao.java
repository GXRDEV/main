package com.tspeiz.modules.common.dao.newrelease;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessTelOrder;

public interface IBusinessTelOrderDao extends BaseDao<BusinessTelOrder>{

	public BusinessTelOrder queryBusinessTelOrderByUid(String uuid);
}
