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

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IStandardDepartmentInfoDao;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.release2.UserMedicalRecord;

@Repository
public class StandardDepartmentInfoDaoImpl extends BaseDaoImpl<StandardDepartmentInfo> implements IStandardDepartmentInfoDao{

	@SuppressWarnings("unchecked")
	public List<StandardDepartmentInfo> queryStandardDepartmentByBigDep(
			Integer bigdepid) {
		// TODO Auto-generated method stub
		String hql="from StandardDepartmentInfo where 1=1 and bigDepId="+bigdepid;
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<StandardDepartmentInfo> queryStandardDepartments() {
		// TODO Auto-generated method stub
		String hql="from StandardDepartmentInfo where 1=1 order by rank";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> queryStandsBydep(Integer depid, Integer type) {
		// TODO Auto-generated method stub
		final StringBuilder sql = new StringBuilder("select dsr.StandardDepId from dep_standarddep_r dsr where dsr.DepId="+depid+" and dsr.DepartmentType="+type);
		List<Integer> list=this.hibernateTemplate
				.execute(new HibernateCallback<List<Integer>>() {
					@Override
					public List<Integer> doInHibernate(Session session) throws HibernateException, SQLException {
						return session.createSQLQuery(sql.toString()).list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public void deleteStandConfig(Integer depid, Integer type) {
		// TODO Auto-generated method stub
		final StringBuilder sql = new StringBuilder("delete FROM dep_standarddep_r where DepId="+depid+" and DepartmentType="+type);
		this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(sql.toString()).executeUpdate();
					}
				});
	}

	@SuppressWarnings("unchecked")
	public void deleteStandConfigByCondition(String sds, Integer depid,
			Integer type) {
		// TODO Auto-generated method stub
		final StringBuilder sql = new StringBuilder("delete FROM dep_standarddep_r where DepId="+depid+" and DepartmentType="+type+" and StandardDepId in("+sds+") ");
		this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(sql.toString()).executeUpdate();
					}
				});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<StandardDepartmentInfo> querystanddeps(final String ispage,
			String ishot,final Integer pageNo,final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select sd.Id,sd.DisplayName,sd.Rank from standard_department_info sd ");
		sqlBuilder.append(" where 1=1 ");
		if(StringUtils.isNotBlank(ishot)&&ishot.equalsIgnoreCase("1")){
			sqlBuilder.append(" and sd.IsHot=1 ");
		}
		sqlBuilder.append(" order by sd.Rank desc ");
		List<StandardDepartmentInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						if(StringUtils.isNotBlank(ispage)&&ispage.equalsIgnoreCase("1")){
							query.setFirstResult((pageNo - 1) * pageSize);
							query.setMaxResults(pageSize);
						}
						query.setResultTransformer(Transformers
								.aliasToBean(StandardDepartmentInfo.class));
						return query.list();
					}
				});
		return list;
	}
}
