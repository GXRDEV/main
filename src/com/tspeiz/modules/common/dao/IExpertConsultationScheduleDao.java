package com.tspeiz.modules.common.dao;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.ExpertConsultationSchedule;

public interface IExpertConsultationScheduleDao extends BaseDao<ExpertConsultationSchedule> {
	public ExpertConsultationSchedule queryScheduleByConditions(Integer expertId,String scheduleDate,String startTime);
	public void deletedirtydatas(Integer expertId,String start,String end,List<Integer> ids);
	public List<ExpertConsultationSchedule> queryExpertConTimeSchedulsByConditions(
			Integer expertId, String scheduleDate);
	public List<ExpertConsultationSchedule> queryExpertConTimeSchedulsByConditions(
			Integer expertId, String sdate, String timetype);
	
	public Map<String,Object> querySchedulesByTime(String begin,String end,Integer start,Integer length);
	
	public List<ExpertConsultationSchedule> queryExpertConTimeSchedulsByExpertId(
			Integer expertId);
}
