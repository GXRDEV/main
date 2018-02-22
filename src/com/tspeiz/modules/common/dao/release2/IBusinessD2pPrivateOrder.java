package com.tspeiz.modules.common.dao.release2;

import java.util.Map;

import com.tspeiz.modules.common.bean.dto.BusinessD2pPrivateOrderDto;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.BusinessD2pPrivateOrder;

public interface IBusinessD2pPrivateOrder extends BaseDao<BusinessD2pPrivateOrder>{

	public Map<String, Object> docprivatedatas(Integer ostatus,String searchContent, Integer start, Integer length);

	public BusinessD2pPrivateOrderDto queryprivateOrdersByid(Integer id);

}
