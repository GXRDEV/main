package com.tspeiz.modules.common.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.dcm.InstanceInfo;
import com.tspeiz.modules.common.bean.dcm.SeriesInfo;
import com.tspeiz.modules.common.bean.dcm.StudyInfo;
import com.tspeiz.modules.common.dao.IBigDepartmentDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.BigDepartment;

@Repository
public class BigDepartmentDaoImpl extends BaseDaoImpl<BigDepartment> implements
		IBigDepartmentDao {
	@SuppressWarnings("unchecked")
	public List<BigDepartment> queryBigDepartments() {
		// TODO Auto-generated method stub
			String hql="from BigDepartment where 1=1 order by rank";
			return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public StudyInfo queryStudyinfoByCondition(String patientId,String studyId) {
		// TODO Auto-generated method stub
		final StringBuilder hql=new StringBuilder("SELECT patient.pat_id as accNumber,study.mods_in_study as modality,patient.pat_sex as pat_gender,patient.pat_birthdate as pat_Birthdate"
				+",patient.pat_name as pat_Name,study.study_desc as studyDesc,study.study_datetime as studyDate,patient.pat_id as pat_ID,study.num_series as totalSeries, "
				+"study.num_instances as totalIns,study.study_iuid as studyUID "
				+ "  from  study");
		hql.append(" LEFT JOIN patient ON study.patient_fk=patient.pk ");
		hql.append(" where 1=1 and patient.pat_id='"+patientId+"' and study.study_iuid='"+studyId+"' ");
		List<StudyInfo> list = this.pacsHibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hql.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(StudyInfo.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<SeriesInfo> querySeriesInfosByCondition(String patientId,
			String studyId) {
		final StringBuilder hql=new StringBuilder("SELECT series.modality as modality,series.series_iuid as seriesUID,"
				+ "series.num_instances as totalInstances,patient.pat_id as patientId,series.series_desc as seriesDesc,study.study_iuid as studyUID"
				+ ",series.series_no as seriesNumber,series.body_part as bodyPart FROM series "
				+ "LEFT JOIN study ON series.study_fk =study.pk "
				+ "LEFT JOIN patient ON patient.pk=study.patient_fk");
		hql.append(" where 1=1 and patient.pat_id='"+patientId+"' and study.study_iuid='"+studyId+"' order by series.series_no  ");
		List<SeriesInfo> list = this.pacsHibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hql.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(SeriesInfo.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<InstanceInfo> queryInstanceInfosByCondition(String patientId,
			String studyId, String seriesId) {
		// TODO Auto-generated method stub
		final StringBuilder hql=new StringBuilder("SELECT instance.pk as pk,instance.sop_iuid as SopUID,instance.sop_cuid as SopClassUID,instance.inst_no as InstanceNo FROM instance "
				+" left join series on instance.series_fk=series.pk "
				+ " LEFT JOIN study ON series.study_fk =study.pk "
				+ " LEFT JOIN patient ON patient.pk=study.patient_fk");
		hql.append(" where 1=1 and patient.pat_id='"+patientId+"' and study.study_iuid='"+studyId+"' and series.series_iuid='"+seriesId+"' ");
		List<InstanceInfo> list = this.pacsHibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hql.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(InstanceInfo.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<StudyInfo> queryStudysByCondition(String patientId) {
		// TODO Auto-generated method stub
		final StringBuilder hql=new StringBuilder("SELECT patient.pat_id as accNumber,study.mods_in_study as modality,patient.pat_sex as pat_gender,patient.pat_birthdate as pat_Birthdate"
				+",patient.pat_name as pat_Name,study.study_desc as studyDesc,study.study_datetime as studyDate,patient.pat_id as pat_ID,study.num_series as totalSeries, "
				+"study.num_instances as totalIns,study.study_iuid as studyUID "
				+ "  from  study");
		hql.append(" LEFT JOIN patient ON study.patient_fk=patient.pk ");
		hql.append(" where 1=1 and patient.pat_id='"+patientId+"'");
		
		
		if(this.pacsHibernateTemplate==null){
			ApplicationContext ac = new FileSystemXmlApplicationContext("/WEB-INF/pacsconfig.xml"); 
			this.pacsHibernateTemplate=(HibernateTemplate)ac.getBean("pacsHibernateTemplate");
		}
		List<StudyInfo> list = this.pacsHibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hql.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(StudyInfo.class));
						return query.list();
					}
				});
		return list;
	}
}
