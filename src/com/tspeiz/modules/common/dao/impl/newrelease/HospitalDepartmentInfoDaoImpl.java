package com.tspeiz.modules.common.dao.impl.newrelease;

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

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IHospitalDepartmentInfoDao;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;

@Repository
public class HospitalDepartmentInfoDaoImpl extends
		BaseDaoImpl<HospitalDepartmentInfo> implements
		IHospitalDepartmentInfoDao {

	@SuppressWarnings("unchecked")
	public List<HospitalDepartmentInfo> queryCoohospitalDepartmentsByCooHos(
			Integer hosid) {
		// TODO Auto-generated method stub
		String hql = "from HospitalDepartmentInfo where 1=1 and hospitalId="
				+ hosid + " and status=1";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryHospitalDepartmentssByHosId(Integer hosid,
			String search, final Integer start, final Integer length,Integer ostatus) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		String searchquery="";
		if(StringUtils.isNotBlank(search)){
			searchquery=" and dep.DisplayName like '%"+search+"%' ";
		}
		final StringBuilder sqlBuilder = new StringBuilder(
				" select dep.Id as id,dep.DisplayName as displayName,dep.LocalDepId as localDepId,dep.Keywords as keywords,dep.Status as status,dep.AppointStatus as appointStatus,"
				+ "dep.HospitalId as hospitalId,dep.Introduction as introduction from hospital_department_info dep where dep.HospitalId="
						+ hosid);
		sqlBuilder.append(" and dep.Status="+ostatus);
		sqlBuilder.append(searchquery);
		sqlBuilder.append(" order by dep.Id desc ");
		List<HospitalDepartmentInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDepartmentInfo.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				" select count(*) from hospital_department_info dep where dep.HospitalId="
						+ hosid);
		countSql.append(" and dep.Status="+ostatus);
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
		return map;
	}

	public List<HospitalDepartmentInfo> queryHospitalDepartmentssByHosId(
			Integer hosid) {
		// TODO Auto-generated method stub
		String hql = "from HospitalDepartmentInfo where 1=1 and hospitalId="
				+ hosid;
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryHospitalDepartmentssBySystem(String search,
			final Integer start, final Integer length, Integer hosid,
			Integer type) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder(
				" select dep.Id as id,dep.DisplayName as displayName,dep.LocalDepId as localDepId"
						+ ",dep.Keywords as keywords,dep.Status as status,dep.HospitalId as hospitalId,hos.DisplayName as hosName from hospital_department_info dep ");
		sqlBuilder
				.append(" left join hospital_detail_info hos on dep.HospitalId=hos.Id where 1=1");
		sqlBuilder.append(" and hos.Id=" + hosid);
		if (StringUtils.isNotBlank(search)) {
			sqlBuilder.append(" and (");
			sqlBuilder.append(" dep.DisplayName like '%" + search + "%' or");
			sqlBuilder.append(" hos.DisplayName like '%" + search + "%'");
			sqlBuilder.append(")");
		}
		sqlBuilder.append(" and dep.Status=" + type);
		sqlBuilder.append(" order by dep.Id desc");
		List<HospitalDepartmentInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDepartmentInfo.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				" select count(*) from hospital_department_info dep ");
		countSql.append(" left join hospital_detail_info hos on dep.HospitalId=hos.Id where 1=1");
		countSql.append(" and hos.Id=" + hosid);
		if (StringUtils.isNotBlank(search)) {
			countSql.append(" and (");
			countSql.append(" dep.DisplayName like '%" + search + "%' or");
			countSql.append(" hos.DisplayName like '%" + search + "%' ");
			countSql.append(")");
		}
		countSql.append(" and dep.Status=" + type);
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
	public List<HospitalDepartmentInfo> queryHospitalDeparts() {
		// TODO Auto-generated method stub
		String hql = "from HospitalDepartmentInfo where 1=1 order by HospitalId ";
		return this.hibernateTemplate.find(hql);
	}
	
	@SuppressWarnings("unchecked")
	public List<HospitalDepartmentInfo> queryDeps(Integer hosid) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder();
		sb.append(" select DISTINCT dep.DisplayName,dep.Id from hospital_department_info dep");
        sb.append("  WHERE dep.HospitalId="+hosid+"  order by dep.id");
		List<HospitalDepartmentInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDepartmentInfo.class));
						return query.list();
					}
				});
		return list;
	}

}
