package com.tspeiz.modules.common.dao.release2;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.SystemWarmthInfo;

public interface ISystemWarmthInfoDao extends BaseDao<SystemWarmthInfo>{
	public List<SystemWarmthInfo> querysystemwarms();
}
