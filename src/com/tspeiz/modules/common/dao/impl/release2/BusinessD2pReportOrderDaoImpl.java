package com.tspeiz.modules.common.dao.impl.release2;

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

import com.tspeiz.modules.common.bean.D2pOrderBean;
import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IBusinessD2pReportOrderDao;
import com.tspeiz.modules.common.entity.release2.BusinessD2pReportOrder;
@Repository
public class BusinessD2pReportOrderDaoImpl extends BaseDaoImpl<BusinessD2pReportOrder> implements IBusinessD2pReportOrderDao{
	@SuppressWarnings("unchecked")
	public List<BusinessD2pReportOrder> queryd2preportordersbyuserid(
			Integer userid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT dre.Id,dre.Status,doc.HeadImageUrl as headImage,doc.DisplayName as docName,"
				+ "doc.Duty as duty,hos.DisplayName as hosName,dep.DisplayName as depName  "
				+ " from business_d2p_report_order dre");
		sqlBuilder.append(" left join doctor_detail_info doc on dre.DoctorId=doc.Id ");
		sqlBuilder.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" where 1=1 and (dre.DelFlag=0 or dre.DelFlag is null) ");
		List<BusinessD2pReportOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessD2pReportOrder.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public BusinessD2pReportOrder queryd2preportorderbyconditions(
			String subUserUuid, Integer docid) {
		// TODO Auto-generated method stub
		String hql="from BusinessD2pReportOrder where 1=1 and doctorId="+docid+" and subUserUuid='"+subUserUuid+"' ";
		List<BusinessD2pReportOrder> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0) return list.get(0);
		return null;
	}

	private String gainstatusquery(Map<String,Object> querymap){
		Integer ostatus=Integer.parseInt(querymap.get("ostatus").toString());
		String str="";
		switch(ostatus){
		case 2:
			str= " and dfast.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();
			break;
		case 3:
			str= " and dfast.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey();
			break;
		case 5:
			str= " and dfast.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey();
			break;
		}
		return str;
	}
	private String gainsearchquery(Map<String,Object> querymap){
		StringBuilder sb=new StringBuilder();
		if(querymap.containsKey("search")){
			String search=querymap.get("search").toString();
			sb.append(" and ( ");
			sb.append(" ca.ContactName like '%"+search+"%' or ");
			sb.append(" ca.Telephone like '%"+search+"%' or ");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' or ");
			sb.append(" dep.DisplayName like '%"+search+"%' ");
			sb.append(" ) ");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryd2preportdatas(
			Map<String, Object> querymap,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT dfast.Id,dfast.CreateTime,dfast.ReceiveTime,dfast.Status,dfast.ChatStatus"
				+ ",doc.DisplayName as docName,doc.Profession as profession,hos.DisplayName as hosName,dep.DisplayName as depName"
				+ ",ca.ContactName as userName,ca.Sex as sex,ca.Age as age,ca.Telephone as telphone"
				+ " from business_d2p_report_order dfast");
		sqlBuilder.append(" left join doctor_detail_info doc on dfast.DoctorId=doc.Id ");
		sqlBuilder.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" left join user_case_info ca on dfast.CaseId=ca.Id ");
		sqlBuilder.append(" where 1=1 and (dfast.DelFlag=0 or dfast.DelFlag is null) ");
		sqlBuilder.append(gainstatusquery(querymap));
		sqlBuilder.append(gainsearchquery(querymap));
		sqlBuilder.append(" order by dfast.CreateTime desc ");
		List<D2pOrderBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(D2pOrderBean.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("SELECT count(dfast.Id) from business_d2p_report_order dfast ");
		countSql.append(" left join doctor_detail_info doc on dfast.DoctorId=doc.Id ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" left join user_case_info ca on dfast.CaseId=ca.Id ");
		countSql.append(" where 1=1 and (dfast.DelFlag=0 or dfast.DelFlag is null) ");
		countSql.append(gainstatusquery(querymap));
		countSql.append(gainsearchquery(querymap));
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
