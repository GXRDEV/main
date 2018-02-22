package com.tspeiz.modules.common.dao.weixin;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.weixin.GraphicsInfo;

public interface IGraphicsInfoDao extends BaseDao<GraphicsInfo>{
	public GraphicsInfo queryGraphicsInfoByTradeNo(String tradeNo);
}
