package com.tspeiz.modules.common.dao.impl;

import java.sql.SQLException;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IUserCaseAttachmentDao;
import com.tspeiz.modules.common.entity.newrelease.UserBillRecord;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;
@Repository
public class UserCaseAttachmentDaoImpl extends BaseDaoImpl<UserCaseAttachment> implements IUserCaseAttachmentDao{

	@SuppressWarnings("unchecked")
	public List<UserCaseAttachment> queryUserAttachmentByCaseUuid(String uuid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
        sqlBuilder.append("SELECT uca.Id,uca.CreateTime AS createTime,uca.CaseUuid AS caseUuid,uca.Uuid AS uuid,uca.Type AS type,uca.Remark AS remark,DATE_FORMAT(uca.ReportTime,'%Y-%m-%d') AS reportTimes,uca.ReportTime as reportTime,uca.AttachmentIds AS attachmentIds from user_case_attachment uca where CaseUuid = '"+uuid+"' ");
		List<UserCaseAttachment> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(UserCaseAttachment.class));
						return query.list();
					}
				});
		return list;
	}

}
