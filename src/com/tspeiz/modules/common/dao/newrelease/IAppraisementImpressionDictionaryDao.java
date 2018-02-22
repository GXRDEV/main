package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDictionary;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDoctor;

public interface IAppraisementImpressionDictionaryDao extends BaseDao<AppraisementImpressionDictionary>{
	public List<AppraisementImpressionDictionary> queryAppraisementImpressions();
	public List<AppraisementImpressionDictionary> queryCalculateTagsNumByDoc(Integer docid);
}
