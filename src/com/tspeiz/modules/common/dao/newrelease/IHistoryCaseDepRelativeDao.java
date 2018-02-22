package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.HistoryCaseDepRelative;

public interface IHistoryCaseDepRelativeDao extends BaseDao<HistoryCaseDepRelative>{
	public List<HistoryCaseDepRelative> queryHistoryCaseDepRelatives(String historyCaseUuid);
}	
