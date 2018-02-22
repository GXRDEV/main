package com.tspeiz.modules.common.dao.release2;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.ThirdOrderInfo;

public interface IThirdOrderInfoDao extends BaseDao<ThirdOrderInfo>{
	public ThirdOrderInfo queryThirdOrderInfoByOrderUuid(String orderUuid);
	public ThirdOrderInfo queryThirdOrderInfoByConsultationId(String conId);
}
