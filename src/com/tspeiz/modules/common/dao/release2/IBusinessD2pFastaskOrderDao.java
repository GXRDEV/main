package com.tspeiz.modules.common.dao.release2;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.BusinessD2pFastaskOrder;

public interface IBusinessD2pFastaskOrderDao extends BaseDao<BusinessD2pFastaskOrder>{
	public Map<String,Object> queryd2pfastaskdatas(Map<String,Object> querymap,Integer start,Integer length);
}
