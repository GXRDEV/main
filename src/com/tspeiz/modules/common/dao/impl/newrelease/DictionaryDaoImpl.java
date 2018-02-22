package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.newrelease.Dictionary;
import com.tspeiz.modules.common.dao.newrelease.IDictionaryDao;

@Repository
public class DictionaryDaoImpl extends BaseDaoImpl<Dictionary> implements IDictionaryDao{

	@SuppressWarnings("unchecked")
	public List<Dictionary> queryDictionarysByParentId(Integer pid) {
		// TODO Auto-generated method stub
		String hql="from Dictionary where 1=1 and parentId="+pid;
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public Dictionary queryDictionaryByCon(String displayName, Integer parentId) {
		// TODO Auto-generated method stub
		String hql="from Dictionary where 1=1 and displayName='"+displayName+"' and parentId="+parentId;
		List<Dictionary> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public Integer queryCountBySql(final String sql) {
		// TODO Auto-generated method stub
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(sql)
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		return num;
	}
	
}
