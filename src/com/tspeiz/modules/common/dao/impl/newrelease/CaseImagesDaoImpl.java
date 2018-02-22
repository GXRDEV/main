package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.ICaseImagesDao;
import com.tspeiz.modules.common.entity.newrelease.CaseImages;

@Repository
public class CaseImagesDaoImpl extends BaseDaoImpl<CaseImages> implements
		ICaseImagesDao {

	@SuppressWarnings("unchecked")
	public List<CaseImages> queryCaseImagesByCaseId(Integer caseId) {
		// TODO Auto-generated method stub
		String hql = "from CaseImages where 1=1 and caseId=" + caseId;
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public void deleteCaseImages(Integer caseid,String ids) {
		// TODO Auto-generated method stub
		final StringBuilder sql = new StringBuilder();
		if(StringUtils.isNotBlank(ids)){
			sql.append("delete FROM user_case_images where CaseId=" + caseid
					+ " and Id not in("+ids+") ");
		}else{
			sql.append("delete FROM user_case_images where CaseId=" + caseid
					+ " or CaseId is null ");
		}
		this.hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				return session.createSQLQuery(sql.toString()).executeUpdate();
			}
		});
	}

	
	@SuppressWarnings("unchecked")
	public List<CaseImages> queryCaseImagesByCaseIds(String ids) {
		// TODO Auto-generated method stub
		String hql="from CaseImages where 1=1 and id in("+ids+")";
		return this.hibernateTemplate.find(hql);
	}
	
	
}
