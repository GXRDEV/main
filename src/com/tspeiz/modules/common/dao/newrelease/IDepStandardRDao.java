package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.DepStandardR;

public interface IDepStandardRDao extends BaseDao<DepStandardR>{
	public List<DepStandardR> queryStandardDepartsByDepId(Integer depid,Integer type);
	public DepStandardR queryDepStandardRByConditions(Integer sid, Integer did,
			Integer type);
}
