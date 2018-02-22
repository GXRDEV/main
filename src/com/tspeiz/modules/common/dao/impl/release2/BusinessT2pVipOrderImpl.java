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
import com.tspeiz.modules.common.bean.dto.BusinessT2pVipOrderDto;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.release2.BusinessT2pVipOrder;
import com.tspeiz.modules.common.entity.release2.IBusinessT2pVipOrder;
@Repository
public class BusinessT2pVipOrderImpl extends BaseDaoImpl<BusinessT2pVipOrder> implements IBusinessT2pVipOrder{

	private String gainSearch(String search) {
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotBlank(search)) {
			sb.append(" and ( ");
			sb.append(" dt.TeamName like '%"+search+"%' or ");
			sb.append(" users.DisplayName like '%"+search+"%' ");
			sb.append(" )");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> docteamvipdatas(Integer ostatus,String searchContent, final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sql=new StringBuilder();
		sql.append(" SELECT  pro.Id AS id,pro.uuid AS uuid,DATE_FORMAT(pro.CreateTime,'%Y-%m-%d %T') AS createTimes,"
				+ " pro.DocServicePackageId AS docServicePackageId ,pro.TeamUuid AS teamUuid,pro.UserId AS userId,pro.Source AS source,  "
				+ " pro.PayStatus AS payStatus,pro.Status AS status,pro.Remark AS remark,DATE_FORMAT(pro.StartDate,'%Y-%m-%d') AS startDate,DATE_FORMAT(pro.EndDate,'%Y-%m-%d') AS endDate, "
				+ " dt.TeamName AS teamName,users.DisplayName AS userName,dsp.Amount AS money,ssp.PackageName AS packageName ");
		sql.append(" FROM business_t2p_vip_order pro ");
		sql.append(" LEFT JOIN doctor_team dt ON dt.uuid=pro.TeamUuid ");
		sql.append(" LEFT JOIN user_detail_info users ON users.id = pro.UserId ");
		sql.append(" LEFT JOIN doctor_service_package dsp ON dsp.Id = pro.DocServicePackageId ");
		sql.append(" LEFT JOIN system_service_package ssp ON ssp.id = dsp.PackageId  ");
		sql.append(" WHERE 1=1  ");
		sql.append(" and pro.Status = "+ostatus);
		sql.append(gainSearch(searchContent));
		sql.append(" ORDER BY pro.CreateTime DESC ");
		List<BusinessT2pVipOrderDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessT2pVipOrderDto.class));
						return query.list();
					}
				});
		
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("select count(pro.Id) from business_t2p_vip_order pro  ");
		countSql.append(" LEFT JOIN doctor_team dt ON dt.uuid=pro.TeamUuid ");
		countSql.append(" LEFT JOIN user_detail_info users ON users.id = pro.UserId ");
		countSql.append(" where 1=1  ");
		countSql.append(" and pro.Status = "+ostatus);
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
	public BusinessT2pVipOrderDto queryteamevipByid(Integer id) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append(" SELECT  pro.Id AS id,pro.uuid AS uuid,DATE_FORMAT(pro.CreateTime,'%Y-%m-%d %T') AS createTimes, ");
		sqlBuilder.append(" pro.DocServicePackageId AS docServicePackageId ,pro.TeamUuid AS teamUuid,pro.UserId AS userId,pro.Source AS source, ");
		sqlBuilder.append(" pro.PayStatus,pro.Status AS status,pro.Remark AS remark,DATE_FORMAT(pro.StartDate,'%Y-%m-%d') AS startDate,DATE_FORMAT(pro.EndDate,'%Y-%m-%d') AS endDate, ");
        sqlBuilder.append(" DATE_FORMAT(pro.relieveTime,'%Y-%m-%d') as relieveTime,pro.totalServerTime,pro.ServerMoney as serverMoney,pro.returnMoney, ");
		sqlBuilder.append(" dt.TeamName AS teamName,dt.Profile AS profile,dt.LogoUrl AS logoUrl, ");
		sqlBuilder.append(" users.DisplayName AS userName,users.sex AS sex,dsp.Amount AS money,ssp.PackageName AS packageName ");
		sqlBuilder.append(" FROM business_t2p_vip_order pro ");
		sqlBuilder.append(" LEFT JOIN doctor_team dt ON dt.uuid=pro.TeamUuid ");
		sqlBuilder.append(" LEFT JOIN user_detail_info users ON users.id = pro.UserId  ");
		sqlBuilder.append(" LEFT JOIN doctor_service_package dsp ON dsp.Id = pro.DocServicePackageId  ");
		sqlBuilder.append(" LEFT JOIN system_service_package ssp ON ssp.id = dsp.PackageId  ");
		sqlBuilder.append(" WHERE pro.Id ="+id+" ORDER BY pro.CreateTime DESC ");
		List<BusinessT2pVipOrderDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessT2pVipOrderDto.class));
						return query.list();
					}
				});
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

}
