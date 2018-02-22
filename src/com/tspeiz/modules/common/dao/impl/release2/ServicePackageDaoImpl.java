package com.tspeiz.modules.common.dao.impl.release2;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IServicePackageDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.SystemServicePackage;

@Repository
public class ServicePackageDaoImpl extends BaseDaoImpl<SystemServicePackage> implements IServicePackageDao {
	
	@SuppressWarnings("unchecked")
	public SystemServicePackage queryServicePackageByServiceId(Integer id) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append(" select * from system_service_package ssp ");
		sqlBuilder.append("  WHERE ssp.serviceId="+id+" ");
		List<SystemServicePackage> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(SystemServicePackage.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	
	}

	@SuppressWarnings("unchecked")
	public void deleteServicePackage(Integer id) {
		// TODO Auto-generated method stub
		///final StringBuilder sqlBuilder=new StringBuilder();
		final String hql=" delete from SystemServicePackage where ServiceId = "+id;
		this.hibernateTemplate
		.execute(new HibernateCallback() {
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				return query.executeUpdate();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<SystemServicePackage> queryServicePackagesByServiceId(Integer serviceid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append(" select * from system_service_package ssp ");
		sqlBuilder.append("  WHERE ssp.serviceId="+serviceid+" ");
		List<SystemServicePackage> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(SystemServicePackage.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list;
		return null;
	}


}
