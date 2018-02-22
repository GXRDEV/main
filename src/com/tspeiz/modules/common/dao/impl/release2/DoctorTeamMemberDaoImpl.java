package com.tspeiz.modules.common.dao.impl.release2;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IDoctorTeamMemberDao;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.entity.release2.DoctorTeamMember;

@Repository
public class DoctorTeamMemberDaoImpl extends BaseDaoImpl<DoctorTeamMember> implements IDoctorTeamMemberDao{

	@SuppressWarnings("unchecked")
	public DoctorTeamMember queryDoctorTeamMembersByDocId(Integer userId) {
		final StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * from doctor_team_member dtm ");
		sb.append(" WHERE dtm.DoctorId="+userId+" ");
		List<DoctorTeamMember> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorTeamMember.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<DoctorTeamMember> queryDoctorTeamMembersByDocId(String groupUuid) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * from doctor_team_member dtm ");
		sb.append(" WHERE dtm.TeamUuid='"+groupUuid+"' ");
		List<DoctorTeamMember> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorTeamMember.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list;
		return null;
	}
}
