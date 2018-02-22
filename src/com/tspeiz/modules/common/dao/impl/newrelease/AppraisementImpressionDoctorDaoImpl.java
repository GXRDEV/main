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
import com.tspeiz.modules.common.dao.newrelease.IAppraisementImpressionDoctorDao;
import com.tspeiz.modules.common.entity.newrelease.AppraisementDoctorInfo;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDoctor;

@Repository
public class AppraisementImpressionDoctorDaoImpl extends
		BaseDaoImpl<AppraisementImpressionDoctor> implements
		IAppraisementImpressionDoctorDao {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AppraisementImpressionDoctor> queryAppraisementImpressionDoctorsByDoc(
			Integer docid, Integer appraiseId) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select dic.Content as tagName");
		sqlBuilder.append(" from appraisement_impression_doctor appraise");
		sqlBuilder.append(" left join appraisement_impression_dictionary dic on appraise.ImpressionCode=dic.Id ");
		sqlBuilder.append(" where appraise.DoctorId="+docid+" and appraise.AppraisementId="+appraiseId);
		// select 语句
		List<AppraisementImpressionDoctor> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(AppraisementImpressionDoctor.class));
						return query.list();
					}
				});
		return list;
	}
	
}
