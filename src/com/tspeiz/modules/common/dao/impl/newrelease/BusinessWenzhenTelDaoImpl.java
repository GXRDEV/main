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

import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IBusinessWenzhenTelDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenzhenTel;

@Repository
public class BusinessWenzhenTelDaoImpl extends BaseDaoImpl<BusinessWenzhenTel> implements IBusinessWenzhenTelDao{

	private String gainstatusquery(Integer ostatus) {
		String str="";
		switch (ostatus) {
		case 1:
			str = " and tl.PayStatus=4 ";
			break;
		case 2:
			str = " and tl.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey()+" and tl.PayStatus=1 ";
			break;
		case 3:
			str = " and tl.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey()+" and tl.PayStatus=1 ";
			break;
		case 4:
			str = " and tl.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey()+" and tl.PayStatus=1 ";
			break;
		case 5:
			str = " and tl.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey()+" and tl.PayStatus=1 ";
			break;
		case 6:
			str = " and tl.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();
			break;
		}
		return str;
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> querytelorders(String search,final Integer start,
			final Integer length, String docid,Integer ostatus) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("SELECT tl.Id as id,ca.ContactName as contactName,ca.Sex as sex,ca.Age as age,ca.Telephone as telephone,doc.DisplayName as docName, tl.Status as askStatus "
						+ ",hos.DisplayName as hospital,dep.DisplayName as depart ,tl.CreateTime as createTime,tl.PayStatus as payStatus,reg.LoginName as docTel  "
						+ "FROM business_tel_order tl ");
		sqlBuilder
				.append(" left join doctor_detail_info doc on tl.DoctorId=doc.Id ");
		sqlBuilder.append(" left join doctor_register_info reg on reg.Id=doc.Id ");
		sqlBuilder
				.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder
				.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" left join user_case_info ca on tl.CaseId=ca.Id ");
		sqlBuilder.append(" where 1=1 ");
		if(StringUtils.isNotBlank(docid)){
			sqlBuilder.append(" and tl.DoctorId="+docid);
			sqlBuilder.append(" and tl.PayStatus=1 ");
		}
		sqlBuilder.append(gainstatusquery(ostatus));
		sqlBuilder.append(" order by tl.CreateTime desc");
		// select 语句
		List<WenzhenBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(WenzhenBean.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder();
		countSql.append("SELECT count(*) FROM business_tel_order tl ");
		countSql.append(" left join doctor_detail_info doc on tl.DoctorId=doc.Id ");
		countSql
		.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql
		.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" left join user_case_info ca on tl.CaseId=ca.Id ");
		countSql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(docid)){
			countSql.append(" and tl.DoctorId="+docid);
			countSql.append(" and tl.PayStatus=1  ");
		}
		countSql.append(gainstatusquery(ostatus));
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<WenzhenBean> queryBusinessWenZhenTelUid(Integer uid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("SELECT tl.Id as id,ca.ContactName as contactName,ca.Sex as sex,ca.Age as age,ca.Telephone as telephone,doc.DisplayName as docName, tl.Status as askStatus "
						+ ",hos.DisplayName as hospital,dep.DisplayName as depart ,tl.CreateTime as createTime,tl.PayStatus as payStatus  "
						+ "FROM business_tel_order tl ");
		sqlBuilder
				.append(" left join doctor_detail_info doc on tl.DoctorId=doc.Id ");
		sqlBuilder
				.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder
				.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" left join user_case_info ca on tl.CaseId=ca.Id ");
		sqlBuilder.append(" where 1=1 ");
		sqlBuilder.append(" and tl.UserId="+uid);
		sqlBuilder.append(" order by tl.CreateTime desc");
		List<WenzhenBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(WenzhenBean.class));
						return query.list();
					}
				});
		return list;
	}

	
	
	
	@SuppressWarnings("unchecked")
	public List<BusinessWenzhenTel> queryBusinessWenzhenTels() {
		// TODO Auto-generated method stub
		String hql="from BusinessWenzhenTel where 1=1";
		return this.hibernateTemplate.find(hql);
	}
}
