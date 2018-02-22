package com.tspeiz.modules.common.dao.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.tspeiz.modules.common.entity.Page;

/**
 * DAO���ӿ�
 * @author xiexq
 *
 * @param <T>
 */
public interface BaseDao<T> 
{
	
	/**
	 * ����ʵ��
	 * @param t
	 */
	public Integer save(T t);
	
	public String save_uid(T t);
	
	
	public Integer save_pacs(T t);
	
	/**
	 * ɾ��ʵ��
	 * @param id
	 */
	public void delete(Serializable id);
	
	/**
	 * ����ʵ��
	 * @param t
	 */
	public void update(T t);
	
	/**
	 * �õ����е�ʵ��
	 * @return
	 */
	public Collection<T> list();
	
	public List listBySql(final String sql);
	
	/**
	 * ���ID���ʵ��
	 * @param id
	 * @return
	 */
	public T find(Serializable id);
	
	
	/**
	 * ���������
	 * @return
	 */
	public int getCount();
	
	
	////////////////////////////////////////////////////////////////////////////////////////
	//���½ӿڴ�
	/**
	 * 
	 * @param hql
	 * @param firstResult
	 * @param pageCount
	 * @param params��ѯ����
	 * @return
	 */
	public List getListByPage(final String hql, final int nStart, final int nCount, final Object... params);
	/**
	 * ͨ���ҳȡ���
	 * @param hql
	 * @param firstResult
	 * @param pageCount
	 * @return
	 */
	public List getListByPage(final String hql, final int nStart, final int nCount);
	public List getListByPageBySql(final String sql, final int nStart, final int nCount);
	@SuppressWarnings("rawtypes")
	public List getListByPageBySql(final String sql, final Class target,
			int pageIndex, final int pageSize);

	/**
	 * �������
	 * @param hql
	 * @param params
	 */
	public void executeUpdate(final String hql, final Object... params);
	
	public Object executeSql(String sql);
}