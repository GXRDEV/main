package com.tspeiz.modules.common.dao.impl;

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

import com.tspeiz.modules.common.bean.dto.BusinessD2pPrivateOrderDto;
import com.tspeiz.modules.common.bean.dto.BusinessT2pVipOrderDto;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.ISystemServiceInfoDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.SystemServiceInfo;
import com.tspeiz.modules.common.entity.release2.BusinessT2pTuwenOrder;

@Repository
public class SystemServiceInfoDaoImpl extends BaseDaoImpl<SystemServiceInfo> implements ISystemServiceInfoDao{

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryserverdatas(Map<String, Object> querymap,
			final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		Integer userType=Integer.parseInt(querymap.get("userType").toString()); 
		/*if(userType.equals(2)){
			sqlBuilder.append("SELECT ssi.Id,ssi.ServiceName,ssi.Description,ssi.ServiceNote FROM system_service_info ssi WHERE usertype=2 ");
			if(querymap.get("dtype").equals(1)){
				sqlBuilder.append(" and groupType=1 ");
			}else if(querymap.get("dtype").equals(2)){
				sqlBuilder.append(" and groupType=2 ");
			}
		}else if(userType.equals(3)){
			sqlBuilder.append("SELECT ssi.Id,ssi.ServiceName,ssi.Description,ssi.ServiceNote FROM system_service_info ssi  WHERE userType=3 ");
			if(querymap.get("dtype").equals(1)){
				sqlBuilder.append(" and groupType=1 ");
			}else if(querymap.get("dtype").equals(2)){
				sqlBuilder.append(" and groupType=2 ");
			}
		}*/
		if(userType.equals(2)){
			sqlBuilder.append("SELECT ssi.Id,ssi.ServiceName,ssi.Description,ssi.ServiceNote FROM system_service_info ssi  WHERE groupType=2 and userType not in(3) ");
		}else{
			sqlBuilder.append("SELECT ssi.Id,ssi.ServiceName,ssi.Description,ssi.ServiceNote FROM system_service_info ssi  WHERE groupType=2 and userType  in(2,3) ");
		}
		
		List<SystemServiceInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						if(start!=null){
							query.setFirstResult(start);
							query.setMaxResults(length);
						}
						query.setResultTransformer(Transformers
								.aliasToBean(SystemServiceInfo.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		if(userType.equals(2)){
			countSql.append("SELECT count(Id) FROM system_service_info WHERE usertype=2 ");
			if(querymap.get("dtype").equals(1)){
				countSql.append(" and groupType=1 ");
			}else if(querymap.get("dtype").equals(2)){
				countSql.append(" and groupType=2 ");
			}
		}else if(userType.equals(3)){
			countSql.append("SELECT count(Id) FROM system_service_info WHERE usertype=3 ");
			if(querymap.get("dtype").equals(1)){
				countSql.append(" and groupType=1 ");
			}else if(querymap.get("dtype").equals(2)){
				countSql.append(" and groupType=2 ");
			}
		}
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

	@SuppressWarnings("unchecked")
	public Map<String, Object> servicedatas(String searchContent, final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sql=new StringBuilder();
		sql.append(" SELECT ssi.Id,ssi.ServiceName,ssi.MultiplePackage as multiplePackage,ssi.Status ");
		sql.append(" FROM system_service_info ssi ");
		List<SystemServiceInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(SystemServiceInfo.class));
						return query.list();
					}
				});
		
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("SELECT count(Id) FROM system_service_info ");
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

	@SuppressWarnings("unchecked")
	public SystemServiceInfo queryservicedatasInfo(Integer id) {
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT ssi.id AS id,ssi.ServiceName AS serviceName,ssi.Status AS status,ssi.DoctorDivided  AS doctorDivided, "
				+ "  ssi.ExpertDivided  AS expertDivided,ssi.PlatformDivided  AS platformDivided,ssi.Remark  AS remark,ssi.ServiceNote AS serviceNote, "
				+ "  ssi.GroupType AS groupType, GROUP_CONCAT(ssp.PackageName) AS packageName ");
		sqlBuilder.append("  FROM system_service_info ssi");
		sqlBuilder.append(" LEFT JOIN system_service_package ssp ON ssi.id=ssp.serviceId");
		sqlBuilder.append("  WHERE ssi.id="+id+"");
		List<SystemServiceInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(SystemServiceInfo.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}
}
