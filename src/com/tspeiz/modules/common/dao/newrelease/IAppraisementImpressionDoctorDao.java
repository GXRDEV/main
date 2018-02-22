package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDoctor;

public interface IAppraisementImpressionDoctorDao extends BaseDao<AppraisementImpressionDoctor>{
	public List<AppraisementImpressionDoctor> queryAppraisementImpressionDoctorsByDoc(Integer docid,Integer appraiseId);
}
