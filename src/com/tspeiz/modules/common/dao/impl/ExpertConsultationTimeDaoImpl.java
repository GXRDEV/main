package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IExpertConsultationTimeDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.ExpertConsultationTime;

@Repository
public class ExpertConsultationTimeDaoImpl extends BaseDaoImpl<ExpertConsultationTime> implements
		IExpertConsultationTimeDao {

	@SuppressWarnings("unchecked")
	public List<ExpertConsultationTime> queryExpertConsultationTimeByExpertId(
			Integer expertId,Integer week) {
		// TODO Auto-generated method stub
		String hql = "from ExpertConsultationTime where status = 1 and expertId = " + expertId+" and weekDay="+week;	
		return  this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<ExpertConsultationTime> queryExpertConsultationTimeByExpertId(
			Integer expertId) {
		// TODO Auto-generated method stub
		String hql = "from ExpertConsultationTime where expertId = " + expertId;	
		return  this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<ExpertConsultationTime> queryExperTimesByConditions(
			Integer expertId, Integer weekday, String timetype) {
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder();
		sb.append(" from ExpertConsultationTime where 1=1 and status=1 and expertId="+expertId+" and weekDay="+weekday);
		if(StringUtils.isNotBlank(timetype)&&!timetype.equalsIgnoreCase("0")){
			if(timetype.equalsIgnoreCase("1")){
				//上午
				sb.append(" and endTime<='12:00' ");
			}else if(timetype.equalsIgnoreCase("2")){
				//下午
				sb.append(" and startTime>='13:00' ");
			}
		}
		return this.hibernateTemplate.find(sb.toString());
	}
}
