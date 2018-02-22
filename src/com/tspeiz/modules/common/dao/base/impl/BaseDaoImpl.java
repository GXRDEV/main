package com.tspeiz.modules.common.dao.base.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.Page;

@SuppressWarnings("unchecked")
public class BaseDaoImpl<T> implements BaseDao<T> {

	private Class<T> classt;

	@Autowired
	protected HibernateTemplate hibernateTemplate;
	
	protected HibernateTemplate pacsHibernateTemplate;

	public BaseDaoImpl() {
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		this.classt = (Class) type.getActualTypeArguments()[0];
	}

	public void delete(Serializable id) {
		T obj = this.find(id);

		if (obj != null) {
			this.hibernateTemplate.delete(obj);
		}

	}

	public T find(Serializable id) {
		T obj = (T) this.hibernateTemplate.get(this.classt, id);
		return obj;
	}

	public int getCount() {
		Long nCount;

		String hql = "select count(*) from " + this.classt.getName();

		List list = this.hibernateTemplate.find(hql);
		nCount = (Long) list.get(0);
		return nCount.intValue();
	}

	public Collection<T> list() {
		return this.hibernateTemplate.find("from " + this.classt.getName());
	}

	@SuppressWarnings("unchecked")
	public List listBySql(final String sql) {
		List list = this.hibernateTemplate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				return query.list();
			}
		});
		return list;
	}

	public Integer save(T t) {
		return (Integer) this.hibernateTemplate.save(t);
	}
	
	public String save_uid(T t) {
		// TODO Auto-generated method stub
		return (String) this.hibernateTemplate.save(t);
	}

	public Integer save_pacs(T t){
		return (Integer) this.pacsHibernateTemplate.save(t);
	}

	public void update(T t) {
		this.hibernateTemplate.update(t);

	}

	/**
	 * ͨ���ҳȡ���
	 * 
	 * @param hql
	 * @param firstResult
	 * @param pageCount
	 * @return
	 */
	public List getListByPage(final String hql, final int firstResult,
			final int pageCount) {
		List list = this.hibernateTemplate.executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setFirstResult(firstResult);
				query.setMaxResults(pageCount);
				return query.list();
			}

		});
		return list;
	}

	public List getListByPageBySql(final String sql, final int firstResult,
			final int pageCount) {
		List list = this.hibernateTemplate.executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				query.setFirstResult(firstResult);
				query.setMaxResults(pageCount);
				return query.list();
			}

		});
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List getListByPageBySql(final String sql, final Class target,
			int pageIndex, final int pageSize) {

		if (pageIndex < 1) {
			pageIndex = 0;
		} else {
			pageIndex = (pageIndex - 1) * pageSize;
		}

		final int firstResult = pageIndex;

		List list = this.hibernateTemplate.executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.aliasToBean(target));
				query.setFirstResult(firstResult);
				query.setMaxResults(pageSize);
				return query.list();
			}

		});
		return list;
	}

	/**
	 * ͨ���ҳȡ���
	 * 
	 * @param hql
	 * @param firstResult
	 * @param pageCount
	 * @param params
	 * @return
	 */
	public List getListByPage(final String hql, final int firstResult,
			final int pageCount, final Object... params) {
		List list = this.hibernateTemplate.executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setFirstResult(firstResult);
				query.setMaxResults(pageCount);
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i, params[i]);
				}
				return query.list();
			}

		});
		return list;
	}

	/**
	 * ����
	 * 
	 * @param hql
	 * @param params
	 */
	public void executeUpdate(final String hql, final Object... params) {
		this.hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i, params[i]);
				}
				query.executeUpdate();
				return null;
			}
		});
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	// ///////////////////////////////////////////////////////////////////////

	public void setPacsHibernateTemplate(HibernateTemplate pacsHibernateTemplate) {
		this.pacsHibernateTemplate = pacsHibernateTemplate;
	}

	
	public HibernateTemplate getPacsHibernateTemplate() {
		return pacsHibernateTemplate;
	}
	
	@Override
    public Object executeSql(final String sql) {
        Object num = this.hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                return session.createSQLQuery(sql).uniqueResult();
            }
        });

        return num;
    }

}