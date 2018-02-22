package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IAppraisementImpressionDictionaryDao;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDictionary;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDoctor;

@Repository
public class AppraisementImpressionDictionaryDaoImpl extends
		BaseDaoImpl<AppraisementImpressionDictionary> implements
		IAppraisementImpressionDictionaryDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<AppraisementImpressionDictionary> queryAppraisementImpressions() {
		// TODO Auto-generated method stub
		String hql = "from AppraisementImpressionDictionary where type=1";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<AppraisementImpressionDictionary> queryCalculateTagsNumByDoc(
			Integer docid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("SELECT dict.`Content`,COUNT(*) as totalNumber FROM `appraisement_impression_doctor` appraise"
						+ " LEFT JOIN `appraisement_impression_dictionary` dict "
						+ "ON appraise.`ImpressionCode`=dict.`Id` WHERE appraise.`DoctorId`="+docid+" GROUP BY dict.`Code`");
		// select 语句
		List<AppraisementImpressionDictionary> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(AppraisementImpressionDictionary.class));
						return query.list();
					}
				});
		return list;
	}
}
