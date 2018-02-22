package com.tspeiz.modules.common.dao.newrelease;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.HistoryCaseInfo;

public interface IHistoryCaseInfoDao extends BaseDao<HistoryCaseInfo>{
	public HistoryCaseInfo queryHistoryCaseInfo(String historyCaseUuid);
	public Map<String, Object> queryHisCaseList(String searchContent,
			Integer start, Integer length, Integer type);
}
