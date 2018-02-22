package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IDoctorForumDao;
import com.tspeiz.modules.common.entity.newrelease.DoctorForum;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
@Repository
public class DoctorForumDaoImpl extends BaseDaoImpl<DoctorForum> implements IDoctorForumDao{

	@SuppressWarnings("unchecked")
	public List<DoctorForum> queryDoctorForumsByConditions( final Integer pageNo,
			final Integer pageSize, String sort) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder("select Title as title,DepName as depName,Duty as duty "
				+",DocName as docName,AccessUrl as accessUrl,BackImag as backImag "
				+ "from  doctor_forum ");
		sqlBuilder.append(" order by  PeriodNum "+sort);
		List<DoctorForum> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult((pageNo-1)*pageSize);
						query.setMaxResults(pageSize);
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorForum.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public Pager queryDoctorForumsByConditions(Pager pager) {
		// TODO Auto-generated method stub
		if (pager == null)
			pager = new Pager();
		final StringBuilder sqlBuilder = new StringBuilder("select Title as title,DepName as depName,Duty as duty "
				+",DocName as docName,AccessUrl as accessUrl,BackImag as backImag "
				+ "from  doctor_forum ");
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("sort"))){
			sqlBuilder.append(" order by  PeriodNum "+pager.getQueryBuilder().get("sort"));
		}else{
			sqlBuilder.append(" order by  PeriodNum asc");
		}
		final Pager _pager = pager;
		List<DoctorForum> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult((_pager.getPageNumber() - 1)
								* _pager.getPageSize());
						query.setMaxResults(_pager.getPageSize());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorForum.class));
						return query.list();
					}
				});
		StringBuilder countStr = new StringBuilder(
				"select count(*) from doctor_forum doc");
		String count = ObjectUtils.toString(this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createSQLQuery(countStr.toString()).uniqueResult());
		pager.setTotalCount(Integer.valueOf(count));
		pager.setList(list);
		return pager;
	}

	@SuppressWarnings("unchecked")
	public DoctorForum querynewforum() {
		// TODO Auto-generated method stub
		String hql="from DoctorForum where 1=1 order by createTime desc";
		List<DoctorForum> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
}
