package com.tspeiz.modules.common.dao.release2;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.BusinessT2pTuwenOrder;

public interface IBusinessT2pTuwenOrderDao extends BaseDao<BusinessT2pTuwenOrder>{
	public Map<String,Object> queryt2ptuwendatas(Map<String,Object> querymap,Integer start,Integer length);
}
