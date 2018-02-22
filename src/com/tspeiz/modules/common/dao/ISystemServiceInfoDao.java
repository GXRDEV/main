package com.tspeiz.modules.common.dao;

import java.util.Map;

import com.tspeiz.modules.common.bean.dto.BusinessD2pPrivateOrderDto;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.SystemServiceInfo;

public interface ISystemServiceInfoDao extends BaseDao<SystemServiceInfo>{
	public Map<String,Object> queryserverdatas(Map<String,Object> querymap,Integer start,Integer length);
	public Map<String, Object> servicedatas(String searchContent, Integer start, Integer length);
	public SystemServiceInfo queryservicedatasInfo(Integer id);
}
