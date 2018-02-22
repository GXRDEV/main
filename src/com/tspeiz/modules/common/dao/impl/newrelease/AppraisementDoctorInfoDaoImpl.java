package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IAppraisementDoctorInfoDao;
import com.tspeiz.modules.common.entity.newrelease.AppraisementDoctorInfo;

@Repository
public class AppraisementDoctorInfoDaoImpl extends
		BaseDaoImpl<AppraisementDoctorInfo> implements
		IAppraisementDoctorInfoDao {
	@SuppressWarnings("unchecked")
	public AppraisementDoctorInfo queryAppraisementDoctorInfoByConditions(
			String oid, Integer ltype) {
		// TODO Auto-generated method stub
		String hql="from AppraisementDoctorInfo where 1=1 and orderUuid='"+oid+"' and orderType="+ltype;
		List<AppraisementDoctorInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AppraisementDoctorInfo> queryAppraisementDoctorInfosByDoc(
			Integer docid,final Integer pageNo,final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select appraise.Id as id,uinfo.LoginName as userAccount,appraise.OrderType,appraise.Grade as grade,appraise.CreateTime as createTime,appraise.Content as content ");
		sqlBuilder.append(" from appraisement_doctor_info appraise");
		sqlBuilder.append(" left join user_account_info uinfo on uinfo.Id=appraise.UserId ");
		sqlBuilder.append(" where appraise.DoctorId="+docid+" and appraise.IsPassed=1");
		// select 语句
		List<AppraisementDoctorInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult((pageNo - 1) * pageSize);
						query.setMaxResults(pageSize);
						query.setResultTransformer(Transformers
								.aliasToBean(AppraisementDoctorInfo.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer queryAppraisementDoctorInfosCount(Integer docid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select count(appraise.Id) ");
		sqlBuilder.append(" from appraisement_doctor_info appraise");
		sqlBuilder.append(" where appraise.DoctorId="+docid+" and appraise.IsPassed=1");
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(sqlBuilder.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		return num ;
	}

}
