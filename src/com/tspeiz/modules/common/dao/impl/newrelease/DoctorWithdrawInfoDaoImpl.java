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

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IDoctorWithdrawInfoDao;
import com.tspeiz.modules.common.entity.coupon.MoneyExchange;
import com.tspeiz.modules.common.entity.newrelease.DoctorWithdrawInfo;

@Repository
public class DoctorWithdrawInfoDaoImpl extends BaseDaoImpl<DoctorWithdrawInfo> implements IDoctorWithdrawInfoDao{
	private String gainquery(String search){
		StringBuilder sb=new StringBuilder();
		if(StringUtils.isNotBlank(search)){
			sb.append(" and (");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' or");
			sb.append(" dep.DisplayName like '%"+search+"%' ");
			sb.append(" )");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryDoctorWithdrawdatas(String startDate,String endDate,String search,
			final Integer start,final Integer length,Integer docid,Integer ostatus,String busiType) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		String query=gainquery(search);
		final StringBuilder sqlBuilder = new StringBuilder("SELECT "
				+ "dwi.Id,dwi.CreateTime,dwi.Money,dwi.CardNo,dwi.IssuingBank,dwi.Cardholder,"
				+ "dwi.Telephone,dwi.AuditTime,dwi.Auditor,dwi.Status,doc.DisplayName as docName,hos.DisplayName as hosName,dep.DisplayName as depName,"
				+ "dwi.TaxationMoney,dwi.ActualMoney,dwi.IDCardNo as idCardNo  from doctor_withdraw_info dwi ");
		sqlBuilder.append(" left join doctor_detail_info doc on dwi.DoctorId=doc.Id ");
		sqlBuilder.append(" left join doctor_register_info reg on doc.Id = reg.Id ");
		sqlBuilder.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" where 1=1 ");
		if(StringUtils.isNotBlank(busiType)){
			sqlBuilder.append(" and reg.UserType="+busiType);
		}
		sqlBuilder.append(query);
		if(docid!=null){
			sqlBuilder.append(" and dwi.DoctorId="+docid);
		}
		if(StringUtils.isNotBlank(startDate)){
			sqlBuilder.append(" and (DATE_FORMAT(dwi.CreateTime,'%Y-%m-%d')>='" + startDate+"') ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlBuilder.append(" and (DATE_FORMAT(dwi.CreateTime,'%Y-%m-%d')<='" + endDate + "') ");
		}
		if(ostatus!=null){
			if(ostatus.equals(1)){
				sqlBuilder.append(" and (dwi.Status="+ostatus+" or dwi.Status is null )");
			}else{
				sqlBuilder.append(" and dwi.Status="+ostatus);
			}
		}
		sqlBuilder.append(" order by dwi.CreateTime desc ");
		List<DoctorWithdrawInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorWithdrawInfo.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) FROM doctor_withdraw_info dwi ");
		countSql.append(" left join doctor_detail_info doc on dwi.DoctorId=doc.Id ");
		countSql.append(" left join doctor_register_info reg on reg.Id = doc.Id ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(busiType)){
			countSql.append(" and reg.UserType="+busiType);
		}
		countSql.append(query);
		if(docid!=null){
			countSql.append(" and dwi.DoctorId="+docid);
		}
		if(StringUtils.isNotBlank(startDate)){
			countSql.append(" and (DATE_FORMAT(dwi.CreateTime,'%Y-%m-%d')>='" + startDate+"') ");
		}
		if(StringUtils.isNotBlank(endDate)){
			countSql.append(" and (DATE_FORMAT(dwi.CreateTime,'%Y-%m-%d')<='" + endDate + "') ");
		}
		if(ostatus!=null){
			if(ostatus.equals(1)){
				countSql.append(" and (dwi.Status="+ostatus+" or dwi.Status is null )");
			}else{
				countSql.append(" and dwi.Status="+ostatus);
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
	public DoctorWithdrawInfo queryDoctorWithdrawInfoById(Integer id) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder("SELECT "
				+ "dwi.Id,dwi.CreateTime,dwi.Money,dwi.CardNo,dwi.IssuingBank,dwi.Cardholder,dwi.OutTradeNo,"
				+ "dwi.Telephone,dwi.AuditTime,dwi.Auditor,dwi.Status,doc.DisplayName as docName,hos.DisplayName as hosName,dep.DisplayName as depName,"
				+ "dwi.ActualMoney,dwi.TaxationMoney,dwi.IDCardNo as idCardNo from doctor_withdraw_info dwi ");
		sqlBuilder.append(" left join doctor_detail_info doc on dwi.DoctorId=doc.Id ");
		sqlBuilder.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" where dwi.Id="+id);
		List<DoctorWithdrawInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorWithdrawInfo.class));
						return query.list();
					}
				});
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
	
}
