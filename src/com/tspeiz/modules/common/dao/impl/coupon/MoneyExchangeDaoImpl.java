package com.tspeiz.modules.common.dao.impl.coupon;

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
import com.tspeiz.modules.common.dao.coupon.IMoneyExchangeDao;
import com.tspeiz.modules.common.entity.coupon.MoneyExchange;
import com.tspeiz.modules.common.entity.newrelease.HosAppointOrder;

@Repository
public class MoneyExchangeDaoImpl extends BaseDaoImpl<MoneyExchange> implements
		IMoneyExchangeDao {


	@SuppressWarnings("unchecked")
	public Map<String, Object> queryMoneyExchangeCodes(String searchContent,
			final Integer start, final Integer length, Integer type) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder(
				"SELECT me.Id as id,me.CharCode as charCode,me.Money as money"
				+ ",me.UseOrNot as useOrNot,me.DeadLine as deadLine,me.CreateTime as createTime"
				+ ",me.Status as status from money_exchange me");
		sqlBuilder.append(" where 1=1 and me.MoneyType=1 ");
		
		List<MoneyExchange> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(MoneyExchange.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) FROM money_exchange me ");
		countSql.append(" where 1=1 and me.MoneyType=1");
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
	public MoneyExchange queryMoneyExchangeByConditions(String code) {
		// TODO Auto-generated method stub
		String hql="from MoneyExchange where 1=1 and charCode='"+code+"' and useOrNot=0 and status=1";
		List<MoneyExchange> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
}
