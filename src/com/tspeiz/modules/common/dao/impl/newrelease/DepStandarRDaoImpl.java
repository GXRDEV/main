package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IDepStandardRDao;
import com.tspeiz.modules.common.entity.newrelease.DepStandardR;

@Repository
public class DepStandarRDaoImpl extends BaseDaoImpl<DepStandardR> implements IDepStandardRDao{

	@SuppressWarnings("unchecked")
	public List<DepStandardR> queryStandardDepartsByDepId(Integer depid,
			Integer type) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder(
				"SELECT stand.DisplayName as standName FROM dep_standarddep_r dr ");
		sqlBuilder.append(" left join standard_department_info stand on dr.StandardDepId=stand.Id ");
		sqlBuilder.append(" where 1=1 and dr.DepId="+depid+" and dr.DepartmentType="+type);
		List<DepStandardR> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DepStandardR.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public DepStandardR queryDepStandardRByConditions(Integer sid, Integer did,
			Integer type) {
		// TODO Auto-generated method stub
		String hql="from DepStandardR where 1=1 and standardDepId="+sid+" and depId="+did+" and departmentType="+type;
		List<DepStandardR> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
	
}
