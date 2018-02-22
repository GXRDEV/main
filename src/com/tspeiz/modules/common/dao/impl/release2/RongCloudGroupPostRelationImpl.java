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
import com.tspeiz.modules.common.dao.release2.IRongCloudGroupPostRelation;
import com.tspeiz.modules.common.entity.release2.RongCloudGroupPostRelation;

@Repository
public class RongCloudGroupPostRelationImpl extends BaseDaoImpl<RongCloudGroupPostRelation> implements IRongCloudGroupPostRelation{
	@SuppressWarnings("unchecked")
	public RongCloudGroupPostRelation queryRongCloudGroupPostRelation(String groupUuid) {
		// TODO Auto-generated method stub
		final StringBuilder sql=new StringBuilder();
		sql.append(" SELECT * FROM rong_cloud_group_post_relation pr ");
		sql.append(" WHERE pr.GroupUuid='"+groupUuid+"' ");
		List<RongCloudGroupPostRelation> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(RongCloudGroupPostRelation.class));
						return query.list();
					}
				});
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
}
