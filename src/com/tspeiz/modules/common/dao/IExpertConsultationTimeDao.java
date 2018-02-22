package com.tspeiz.modules.common.dao;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.ExpertConsultationTime;

public interface IExpertConsultationTimeDao extends BaseDao<ExpertConsultationTime> {
	
	public List<ExpertConsultationTime> queryExpertConsultationTimeByExpertId(Integer expertId,Integer week);
	public List<ExpertConsultationTime> queryExpertConsultationTimeByExpertId(Integer expertId);
	public List<ExpertConsultationTime> queryExperTimesByConditions(
			Integer expertId, Integer weekday, String timetype);

}
