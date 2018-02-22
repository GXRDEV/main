package com.tspeiz.modules.common.dao.impl.release2;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IUserWarmthInfoDao;
import com.tspeiz.modules.common.entity.release2.UserMedicalRecord;
import com.tspeiz.modules.common.entity.release2.UserWarmthInfo;
@Repository
public class UserWarmthInfoDaoImpl extends BaseDaoImpl<UserWarmthInfo> implements IUserWarmthInfoDao{

	@SuppressWarnings("unchecked")
	public Integer querydoctorwarms(Integer docid) {
		// TODO Auto-generated method stub
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(uw.WarmthMoney) from user_warmth_info uw ");
		countSql.append(" where uw.status=1 and doctorId="+docid);
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(countSql.toString())
								.uniqueResult();
					}
				});
			Integer num=Integer.parseInt(effectNumber.toString());
			return num;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<UserWarmthInfo> querydoctorwarms_list(Integer docid,
			final Integer pageNo,final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select uw.Id,DATE_FORMAT(uw.CreateTime,'%Y-%m-%d') timeStr,uw.Content,uw.WarmthMoney,uw.WarmthName from user_warmth_info uw ");
		List<UserWarmthInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult((pageNo-1)*pageSize);
						query.setMaxResults(pageSize);
						query.setResultTransformer(Transformers
								.aliasToBean(UserWarmthInfo.class));
						return query.list();
					}
				});
		return list;
	}
}
