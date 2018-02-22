package com.tspeiz.modules.common.dao.impl.release2;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.D2pOrderBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.release2.UserMedicalRecord;
import com.tspeiz.modules.common.dao.release2.IUserMedicalRecordDao;

@Repository
public class UserMedicalRecordDaoImpl extends BaseDaoImpl<UserMedicalRecord>
		implements IUserMedicalRecordDao {
	@SuppressWarnings("unchecked")
	public List<UserMedicalRecord> queryusermedicalrecords(String subUserUuid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("SELECT um.Id,um.VisitDate,um.HosName,um.DepName,um.DoctorName,um.FirstVisit,ca.CaseName as diseaseName,"
						+ "ca.PresentHistory as diseaseDes,ca.NormalImages as images "
						+ " from user_medical_record um ");
		sqlBuilder.append(" left join user_case_info ca on um.CaseId=ca.Id ");
		sqlBuilder.append(" where 1=1 and um.SubUserUuid='" + subUserUuid
				+ "' and (um.delFlag=0 or um.delFlag is null) ");
		sqlBuilder.append(" order by um.VisitDate desc");

		List<UserMedicalRecord> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(UserMedicalRecord.class));
						return query.list();
					}
				});
		return list;
	}

}
