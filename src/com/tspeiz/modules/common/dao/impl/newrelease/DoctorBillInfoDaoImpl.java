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
import com.tspeiz.modules.common.dao.newrelease.IDoctorBillInfoDao;
import com.tspeiz.modules.common.entity.newrelease.DoctorBillInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorWithdrawInfo;
@Repository
public class DoctorBillInfoDaoImpl extends BaseDaoImpl<DoctorBillInfo> implements IDoctorBillInfoDao{
	private String gainquery(Map<String,Object> querymap){
		StringBuilder sb=new StringBuilder();
		if(querymap.containsKey("docname")){
			sb.append(" and doc.DisplayName like '%"+querymap.get("docname")+"%' ");
		}
		if(querymap.containsKey("busitype")){
			sb.append(" and dbi.BusinessType="+querymap.get("busitype"));		
		}
		if(querymap.containsKey("actions")){
			sb.append(" and dbi.Type="+querymap.get("actions"));
		}
		if(querymap.containsKey("startDate")){
			sb.append(" and (DATE_FORMAT(dbi.CreateTime,'%Y-%m-%d')>='" + querymap.get("startDate")+"') ");
		}
		if(querymap.containsKey("endDate")){
			sb.append(" and (DATE_FORMAT(dbi.CreateTime,'%Y-%m-%d')<='" + querymap.get("endDate") + "') ");
		}
		return sb.toString();
	}
	private String gainsearchquery(Map<String,Object> querymap){
		StringBuilder sb=new StringBuilder();
		if(querymap.containsKey("search")){
			String search=querymap.get("search").toString();
			sb.append(" and ( ");
			sb.append(" hos.DisplayName like '%"+search+"%' or ");
			sb.append(" doc.DisplayName like '%"+search+"%' ");	
			sb.append(" ) ");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> querydoctorbilldatas(
			Map<String, Object> querymap,final Integer start,final Integer length,Integer docid) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder("SELECT dbi.Id,doc.DisplayName as docName,hos.DisplayName as hosName,dep.DisplayName as depName,reg.MobileNumber as docTel,"
				+ "dbi.CreateTime,dbi.Source,dbi.Type,IFNULL(dbi.ActualMoney,0.0) as actualMoney,IFNULL(dbi.OriginalMoney,0.0) as originalMoney,"
				+ "IFNULL(dbi.TaxationMoney,0.0) as taxationMoney,IFNULL(dbi.SubsidyMoney,0.0) as subsidyMoney,IFNULL(dbi.CurAccount,0.0) as curAccount,"
				+ "IFNULL(dbi.PreAccount,0.0) as preAccount,dbi.BusinessType,dbi.CreateTime from doctor_bill_info dbi ");
		sqlBuilder.append(" left join doctor_detail_info doc on dbi.DoctorId=doc.Id ");
		sqlBuilder.append(" left join doctor_register_info reg on doc.Id = reg.Id ");
		sqlBuilder.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" where 1=1 ");
		sqlBuilder.append(gainsearchquery(querymap));
		String query=gainquery(querymap);
		sqlBuilder.append(query);
		if(docid!=null){
			sqlBuilder.append(" and dbi.DoctorId="+docid);
		}
		sqlBuilder.append(" order by dbi.CreateTime desc ");
		List<DoctorBillInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorBillInfo.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) FROM doctor_bill_info dbi ");
		countSql.append(" left join doctor_detail_info doc on dbi.DoctorId=doc.Id ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" where 1=1 ");
		countSql.append(gainsearchquery(querymap));
		countSql.append(query);
		if(docid!=null){
			countSql.append(" and dbi.DoctorId="+docid);
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
	public DoctorBillInfo querydoctorbill(Integer docid) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * FROM doctor_bill_info docbill ");
		sb.append(" WHERE docbill.DoctorId="+docid+" AND docbill.Source LIKE '奖励金' ");
		List<DoctorBillInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorBillInfo.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}
	
}
