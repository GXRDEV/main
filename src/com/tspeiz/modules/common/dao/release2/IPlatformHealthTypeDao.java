package com.tspeiz.modules.common.dao.release2;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.PlatformHealthType;

public interface IPlatformHealthTypeDao extends BaseDao<PlatformHealthType>{
	public List<PlatformHealthType> queryUseingPlatFormHealthTypes(Integer status);
}
