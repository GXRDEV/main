package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IHosAppointOrderDao;
import com.tspeiz.modules.common.entity.newrelease.HosAppointOrder;

@Repository
public class HosAppointOrderDaoImpl extends BaseDaoImpl<HosAppointOrder>
		implements IHosAppointOrderDao {
	
	private String gainSearch(String search){
		StringBuilder sb=new StringBuilder();
		sb.append(" and (");
		sb.append(" hor.PatientName like '%"+search+"%' or ");
		sb.append(" hor.Telphone like '%"+search+"%' or ");
		sb.append(" dep.DisplayName like '%"+search+"%' or ");
		sb.append(" hor.AppointDate like '%"+search+"%' or ");
		sb.append(" hor.CreateTime like '%"+search+"%' ");
		sb.append(" )");
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryHosWxPlusOrders(Integer docid,
			String search,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		String _search=gainSearch(search);
		final StringBuilder sqlBuilder = new StringBuilder(
				"SELECT dep.DisplayName as depName,hor.id as id,hor.PatientName as patientName, hor.Telphone as telphone"
				+ ",hor.AppointDate as appointDate,hor.RegMessage as regMessage,hor.CreateTime as createTime,dt.AppointTime as timeType FROM hos_appoint_order hor ");
		sqlBuilder
				.append(" left join hospital_department_info dep on hor.DepId=dep.Id ");
		sqlBuilder.append(" left join local_depreg_time dt on hor.RegTimeId=dt.Id ");
		sqlBuilder.append(" where 1=1 and hor.DocId=" + docid);
		sqlBuilder.append(_search);
		sqlBuilder.append(" order by  hor.RefreshTime desc");
		
		List<HosAppointOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(HosAppointOrder.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) FROM hos_appoint_order hor ");
		countSql
				.append(" left join hospital_department_info dep on hor.DepId=dep.Id ");
		countSql.append(" left join local_depreg_time dt on hor.RegTimeId=dt.Id ");
		countSql.append(" where 1=1 and hor.DocId=" + docid);
		countSql.append(_search);	
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
