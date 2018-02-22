package com.tspeiz.modules.common.dao.impl.release2;

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
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IplatformHealthConsultationDao;
import com.tspeiz.modules.common.entity.release2.PlatformHealthConsultation;
@Repository
public class platformHealthConsultationDaoImpl extends BaseDaoImpl<PlatformHealthConsultation> implements IplatformHealthConsultationDao{

	@SuppressWarnings("unchecked")
	public Map<String, Object> querySystemConsultationCaseDatas(
			String search, Integer ostatus,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		String searchquery="";
		if(StringUtils.isNotBlank(search)){
			searchquery=" and phc.Title like '%"+search+"%' ";
		}
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sql=new StringBuilder();
		sql.append("SELECT phc.Id,phc.Title,phc.CreateTime ,pht.TypeName as typeName ");
		sql.append(" FROM platform_health_consultation phc ");
		sql.append(" LEFT JOIN  platform_health_type pht ON phc.TagType=pht.Id ");
		sql.append(" where 1=1 and phc.Status="+ostatus);
		sql.append(searchquery);
		sql.append(" order by phc.CreateTime desc ");
		List<PlatformHealthConsultation> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(PlatformHealthConsultation.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("select count(phc.Id) ");
		countSql.append(" from platform_health_consultation phc ");
		countSql.append(" where 1=1 and phc.Status="+ostatus);
		countSql.append(searchquery);
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
		return map;	}

}
