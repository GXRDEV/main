package com.tspeiz.modules.common.dao.impl.release2;

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

import com.tspeiz.modules.common.bean.dto.BusinessD2pPrivateOrderDto;
import com.tspeiz.modules.common.bean.dto.GroupMemberInfoDto;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IBusinessD2pPrivateOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pPrivateOrder;
import com.tspeiz.modules.common.entity.release2.DoctorTeam;

@Repository
public class BusinessD2pPrivateOrderImpl extends BaseDaoImpl<BusinessD2pPrivateOrder> implements IBusinessD2pPrivateOrder{
	
	private String gainSearch(String search) {
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotBlank(search)) {
			sb.append(" and ( ");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" uc.ContactName like '%"+search+"%' ");
			sb.append(" )");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> docprivatedatas(Integer ostatus,String searchContent, final Integer start, final Integer length) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sql=new StringBuilder();
		sql.append(" SELECT  pri.Id AS id,pri.Uuid AS uuid,pri.DocServicePackageId AS docServicePackageId,DATE_FORMAT(pri.CreateTime,'%Y-%m-%d %T') as createTimes,pri.DoctorId AS doctorId,pri.UserId as userId,pri.Source as source, "
				+ " pri.PayStatus as payStatus,pri.Status as status,DATE_FORMAT(pri.StartDate,'%Y-%m-%d') as startDate,DATE_FORMAT(pri.EndDate,'%Y-%m-%d') as endDate, "
				+ " doc.DisplayName AS docName,uc.ContactName AS userName,dsp.Amount AS money,ssp.PackageName as packageName");
		sql.append(" FROM business_d2p_private_order pri ");
		sql.append(" LEFT JOIN doctor_detail_info doc ON doc.id = pri.DoctorId ");
		sql.append(" LEFT JOIN user_contact_info uc ON uc.UUID = pri.SubUserUuid ");
		sql.append(" LEFT JOIN doctor_service_package dsp ON dsp.Id = pri.DocServicePackageId ");
		sql.append(" LEFT JOIN system_service_package ssp ON ssp.id = dsp.PackageId ");
		sql.append("  where 1=1   ");
		sql.append(" and pri.Status = "+ostatus);
		sql.append(gainSearch(searchContent));
		sql.append(" ORDER BY pri.CreateTime DESC");
		List<BusinessD2pPrivateOrderDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessD2pPrivateOrderDto.class));
						return query.list();
					}
				});
		
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("select count(pri.Id) from business_d2p_private_order pri ");
		countSql.append(" LEFT JOIN doctor_detail_info doc ON doc.id = pri.DoctorId ");
		countSql.append(" LEFT JOIN user_contact_info uc ON uc.UUID = pri.SubUserUuid ");
		countSql.append(" where 1=1 ");
		countSql.append(" and pri.Status = "+ostatus);
		countSql.append(gainSearch(searchContent));
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
	public BusinessD2pPrivateOrderDto queryprivateOrdersByid(Integer id) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append(" SELECT  pri.Id,pri.uuid,DATE_FORMAT(pri.CreateTime,'%Y-%m-%d %T') AS createTimes, "
				+ "  pri.DocServicePackageId,pri.DoctorId,pri.UserId,pri.Source, "
				+ "  pri.PayStatus as PayStatus,pri.Status as status,DATE_FORMAT(pri.StartDate,'%Y-%m-%d') as startDate,DATE_FORMAT(pri.EndDate,'%Y-%m-%d') as endDate,"
				+ "  DATE_FORMAT(pri.relieveTime,'%Y-%m-%d') as relieveTime,pri.totalServerTime,pri.ServerMoney as serverMoney,pri.returnMoney,"
				+ "  doc.DisplayName AS docName,doc.Duty AS duty,hos.DisPlayName AS hosName,dep.DisPlayName AS depName, "
				+ "  uc.ContactName AS userName,uc.sex AS sex,dsp.Amount AS money,ssp.PackageName as packageName ");
		sqlBuilder.append(" FROM business_d2p_private_order pri ");
		sqlBuilder.append(" LEFT JOIN doctor_detail_info doc ON doc.id = pri.DoctorId  ");
		sqlBuilder.append(" LEFT JOIN user_contact_info uc ON uc.UUID = pri.SubUserUuid  ");
		sqlBuilder.append(" LEFT JOIN doctor_service_package dsp ON dsp.Id = pri.DocServicePackageId ");
		sqlBuilder.append(" LEFT JOIN system_service_package ssp ON ssp.id = dsp.PackageId ");
		sqlBuilder.append(" LEFT JOIN hospital_detail_info hos ON doc.HospitalId=hos.Id ");
		sqlBuilder.append(" LEFT JOIN hospital_department_info dep ON dep.Id=doc.DepId ");
		sqlBuilder.append(" WHERE pri.Id ="+id+" ORDER BY pri.CreateTime DESC ");
		List<BusinessD2pPrivateOrderDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessD2pPrivateOrderDto.class));
						return query.list();
					}
				});
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

}
