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

import com.tspeiz.modules.common.bean.D2pOrderBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IUserContactInfoDao;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;

@Repository
public class UserContactInfoDaoImpl extends BaseDaoImpl<UserContactInfo>
		implements IUserContactInfoDao {

	@SuppressWarnings("unchecked")
	public List<UserContactInfo> queryUserContactInfosByOpenId(String openid) {
		// TODO Auto-generated method stub
		String hql="from UserContactInfo where 1=1 ";
		if(StringUtils.isNotBlank(openid)){
			hql+="and openId='"+openid+"' ";
		}
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public UserContactInfo queryUserContactInfoByConditions(String openid,
			String username, String idcard, String telphone) {
		// TODO Auto-generated method stub
		String hql="from UserContactInfo where 1=1 and openid='"+openid+"' and contactName='"+username+"' and idCard='"+idcard+"' and telphone='"+telphone+"' ";
		List<UserContactInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	/*@SuppressWarnings("unchecked")
	public List<UserContactInfo> queryUserContactInfosByUserId(Integer uid) {
		// TODO Auto-generated method stub
		String hql="from UserContactInfo where 1=1 and userId="+uid+" order by isMasterContact desc,createTime asc";
		return this.hibernateTemplate.find(hql);
	}*/

	@SuppressWarnings("unchecked")
	public List<UserContactInfo> queryUserContactInfosByUserId(Integer uid){
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT uc.Id,uc.UUID as uuid,uc.UserId,uc.OpenId,uc.ContactName,uc.Telephone as telphone,uc.Sex,uc.Age,uc.IsMasterContact"
				+ ",uc.DistCode,uc.IdCard,uc.IsDefault from user_contact_info uc ");
		sqlBuilder.append(" where 1=1 and uc.UserId="+uid+" and uc.Status=1 ");
		sqlBuilder.append(" order by uc.IsDefault desc,uc.IsMasterContact desc,uc.CreateTime asc");
		
		List<UserContactInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(UserContactInfo.class));
						return query.list();
					}
				});
		return list;
	}
	@SuppressWarnings("unchecked")
	public UserContactInfo queryUserContactorByCondition(Integer uid,
			String username, String tel) {
		// TODO Auto-generated method stub
		String hql="from UserContactInfo where 1=1 and userId="+uid+" and contactName ='"+username+"' and telphone='"+tel+"' ";
		List<UserContactInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public UserContactInfo queryUserContactInfoByUuid(String uuid) {
		// TODO Auto-generated method stub
		String hql=" from UserContactInfo where 1=1 and uuid='"+uuid+"' ";
		List<UserContactInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0) return list.get(0);
		return null;
	}
	
	
}
