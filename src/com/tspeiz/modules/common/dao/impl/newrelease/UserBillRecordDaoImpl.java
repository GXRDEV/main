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
import com.tspeiz.modules.common.dao.newrelease.IUserBillRecordDao;
import com.tspeiz.modules.common.entity.newrelease.UserBillRecord;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;

@Repository
public class UserBillRecordDaoImpl extends BaseDaoImpl<UserBillRecord>
		implements IUserBillRecordDao {
	@SuppressWarnings("unchecked")
	public UserBillRecord queryUserBillByCondition(String content, Integer oid) {
		// TODO Auto-generated method stub
		String hql="from UserBillRecord where 1=1 and content like '%"+content+"%' and orderId="+oid;
		List<UserBillRecord> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<UserBillRecord> gainuserbills(Integer userid,final String ispage,
			final Integer pageNo,final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select OpTime,Content,OpAction,Money from user_bill_record where userId="+userid);
		List<UserBillRecord> list = this.hibernateTemplate
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
								.aliasToBean(UserBillRecord.class));
						return query.list();
					}
				});
		return list;
	}
}
