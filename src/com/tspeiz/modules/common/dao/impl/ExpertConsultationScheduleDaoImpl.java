package com.tspeiz.modules.common.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IExpertConsultationScheduleDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.ExpertConsultationSchedule;
import com.tspeiz.modules.common.entity.RemoteConsultation;

@Repository
public class ExpertConsultationScheduleDaoImpl extends
		BaseDaoImpl<ExpertConsultationSchedule> implements
		IExpertConsultationScheduleDao {

	@SuppressWarnings("unchecked")
	public ExpertConsultationSchedule queryScheduleByConditions(
			Integer expertId, String scheduleDate, String startTime) {
		// TODO Auto-generated method stub
		String hql = "from ExpertConsultationSchedule where expertId="
				+ expertId + " and scheduleDate='" + scheduleDate
				+ "' and startTime='" + startTime + "' ";
		List<ExpertConsultationSchedule> list = this.hibernateTemplate
				.find(hql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public void deletedirtydatas(Integer expertId, String start, String end,
			List<Integer> ids) {
		// TODO Auto-generated method stub
		// 删除某时间段中无用的垃圾数据
		StringBuilder _ids=null;
		if (ids != null && ids.size() > 0) {
			_ids = new StringBuilder("");
			for (Integer id : ids) {
				_ids.append(id + ",");
			}
			_ids = _ids.deleteCharAt(_ids.length() - 1);
		}
		final StringBuilder sql = new StringBuilder("delete from expert_consultation_schedule where ExpertId="
				+ expertId
				+ " and ScheduleDate>='"
				+ start
				+ "' and ScheduleDate<='"
				+ end
				+ "' ");
		if(_ids!=null&&_ids.length()>0){
			sql.append("and Id not in("
					+ _ids.toString() + ")");
		}
		this.hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query query = session.createSQLQuery(sql.toString());
				return query.executeUpdate();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<ExpertConsultationSchedule> queryExpertConTimeSchedulsByConditions(
			Integer expertId, String scheduleDate) {
		// TODO Auto-generated method stub
		String hql = "from ExpertConsultationSchedule where expertId="
				+ expertId + " and scheduleDate='" + scheduleDate
				+ "' order by StartTime";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<ExpertConsultationSchedule> queryExpertConTimeSchedulsByConditions(
			Integer expertId, String sdate, String timetype) {
		// TODO Auto-generated method stub
		StringBuilder hql = new StringBuilder(
				"from ExpertConsultationSchedule where expertId=" + expertId
						+ " and scheduleDate='" + sdate + "'");
		if (StringUtils.isNotBlank(timetype) && !timetype.equalsIgnoreCase("0")) {
			if (timetype.equalsIgnoreCase("1")) {
				// 上午
				hql.append(" and startTime<='12:00' ");
			} else if (timetype.equalsIgnoreCase("2")) {
				// 下午
				hql.append(" and startTime>='13:00' ");
			}
		}
		hql.append(" order by StartTime");
		return this.hibernateTemplate.find(hql.toString());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> querySchedulesByTime(String begin, String end,
			final Integer start, final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sb = new StringBuilder(
				"SELECT * from expert_consultation_schedule where scheduleDate>='"
						+ begin + "' and scheduleDate<='" + end
						+ "' order by scheduleDate,startTime");
		List<ExpertConsultationSchedule> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(ExpertConsultationSchedule.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) from expert_consultation_schedule where scheduleDate>='"
						+ begin + "' and scheduleDate<='" + end
						+ "' order by scheduleDate,startTime");
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(countSql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		map.put("num", num);
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<ExpertConsultationSchedule> queryExpertConTimeSchedulsByExpertId(
			Integer expertId) {
		// TODO Auto-generated method stub
		String hql="from ExpertConsultationSchedule where 1=1 and expertId="+expertId;
		return this.hibernateTemplate.find(hql);
	}
	
	
}
