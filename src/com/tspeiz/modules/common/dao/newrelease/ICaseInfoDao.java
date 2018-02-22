package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;

public interface ICaseInfoDao extends BaseDao<CaseInfo>{
	public List<CaseInfo> queryHisCaseInfosByUId(Integer uid);
	public CaseInfo queryCaseInfoById_new(Integer caseid);
	public CaseInfo queryCaseInfoByUuid(String caseUuid);
	public List<CaseInfo> queryCasesTransToAttachments();
}
