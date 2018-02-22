package com.tspeiz.modules.common.entity.release2;

import java.util.Map;

import com.tspeiz.modules.common.bean.dto.BusinessD2pPrivateOrderDto;
import com.tspeiz.modules.common.bean.dto.BusinessT2pVipOrderDto;
import com.tspeiz.modules.common.dao.base.BaseDao;

public interface IBusinessT2pVipOrder extends BaseDao<BusinessT2pVipOrder>{

	public Map<String, Object> docteamvipdatas(Integer ostatus,String searchContent, Integer start, Integer length);

	public BusinessT2pVipOrderDto queryteamevipByid(Integer id);

}
