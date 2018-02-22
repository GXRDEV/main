package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IUserFeedBackInfoDao;
import com.tspeiz.modules.common.entity.newrelease.HosAppointOrder;
import com.tspeiz.modules.common.entity.newrelease.UserFeedBackInfo;
@Repository
public class UserFeedBackInfoDaoImpl extends BaseDaoImpl<UserFeedBackInfo> implements IUserFeedBackInfoDao{

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryHosWxFeedbacks(Integer docid,
			String search,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select Id,UContent as content,CreateTime,Telphone from user_feedback_info fb ");
		sqlBuilder.append(" where 1=1 and DocId="+docid+" order by CreateTime desc");
		List<UserFeedBackInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(UserFeedBackInfo.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) FROM user_feedback_info fb ");
		countSql.append(" where 1=1 and DocId="+docid);
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

}
