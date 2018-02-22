package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.dto.HistoryCaseDto;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IHistoryCaseInfoDao;
import com.tspeiz.modules.common.entity.newrelease.HistoryCaseInfo;

@Repository
public class HistoryCaseInfoDaoImpl extends BaseDaoImpl<HistoryCaseInfo> implements IHistoryCaseInfoDao{

	@SuppressWarnings("unchecked")
	@Override
	public HistoryCaseInfo queryHistoryCaseInfo(String historyCaseUuid) {
		// TODO Auto-generated method stub
		String hql="from HistoryCaseInfo where 1=1 and uuid='"+historyCaseUuid+"' ";
		List<HistoryCaseInfo> list = this.hibernateTemplate.find(hql);
		if(list != null && list.size()>0) return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryHisCaseList(String searchContent,
			final Integer start,final Integer length, Integer type) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select loi.UUID as hisCaseUuid,loi.Title,DATE_FORMAT(loi.CreateTime,'%Y-%m-%d') as createTime, ");
		sqlBuilder.append("GROUP_CONCAT(sdi.`DisplayName` SEPARATOR ',') AS depName");
		sqlBuilder.append(" from live_order_info loi");
		sqlBuilder.append(" LEFT JOIN `live_departments_relative` ldr ON loi.`UUID`=ldr.`LiveUuid` ");
		sqlBuilder.append(" LEFT JOIN standard_department_info sdi ON sdi.`Id` = ldr.`StandardDepId`");
		sqlBuilder.append(" WHERE 1=1 AND loi.Status=1 and type="+type);
		sqlBuilder.append(" GROUP BY loi.`UUID`");
		sqlBuilder.append(" ORDER BY loi.`CreateTime` DESC");
		List<HistoryCaseDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(HistoryCaseDto.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"select count(loi.Id) from live_order_info loi where 1=1 and status = 1 and type="+type);
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(countSql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		map.put("num", num);
		return map;
			
	}
	
	
}
