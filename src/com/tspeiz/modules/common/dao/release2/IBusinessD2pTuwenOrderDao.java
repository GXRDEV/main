package com.tspeiz.modules.common.dao.release2;
import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.BusinessD2pTuwenOrder;

public interface IBusinessD2pTuwenOrderDao extends BaseDao<BusinessD2pTuwenOrder>{
	public Map<String,Object> queryd2ptuwendatas(Map<String,Object> querymap,Integer start,Integer length);

}
