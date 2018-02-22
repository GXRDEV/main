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
import com.tspeiz.modules.common.dao.release2.IBusinessD2pTuwenOrderDao;
import com.tspeiz.modules.common.entity.release2.BusinessD2pTuwenOrder;
@Repository
public class BusinessD2pTuwenOrderDaoImpl extends BaseDaoImpl<BusinessD2pTuwenOrder> implements IBusinessD2pTuwenOrderDao{

	private String gainstatusquery(Map<String,Object> querymap){
		Integer ostatus=Integer.parseInt(querymap.get("ostatus").toString());
		String str="";
		switch(ostatus){
		case 1:
			str= " and tuwen.PayStatus=4 ";
			break;
		case 2:
			str= " and tuwen.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey()+" and tuwen.PayStatus=1 ";
			break;
		case 3:
			str= " and tuwen.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey()+" and tuwen.PayStatus=1 ";
			break;
		case 4:
			str= " and tuwen.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey()+" and tuwen.PayStatus=1 ";
			break;
		case 5:
			str= " and tuwen.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey()+" and tuwen.PayStatus=1 ";
			break;
		case 6:
			str= " and tuwen.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();
			break;
		}
		return str;
	}
	private String gainsearchquery(Map<String,Object> querymap){
		StringBuilder sb=new StringBuilder();
		if(querymap.containsKey("search")){
			String search=querymap.get("search").toString();
			sb.append(" and (");
			sb.append(" ca.ContactName like '%"+search+"%' or ");
			sb.append(" ca.Telephone like '%"+search+"%' or ");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' or ");
			sb.append(" dep.DisplayName like '%"+search+"%' or ");
			sb.append(" (bpay.PayStatus=1 and (CASE WHEN (bpay.RefundStatus IS NULL or bpay.RefundStatus=0) THEN \"待退款\" WHEN bpay.RefundStatus=1 THEN \"退款成功\" "
					+ " WHEN bpay.RefundStatus=-1 THEN \"退款失败\"  END ) LIKE '%"+search+"%' ) ");
			sb.append(" ) ");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryd2ptuwendatas(Map<String, Object> querymap,
			final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT tuwen.Id,tuwen.CreateTime,tuwen.ReceiveTime,tuwen.PayStatus,tuwen.Status"
				+ ",doc.DisplayName as docName,doc.Profession as profession,hos.DisplayName as hosName,dep.DisplayName as depName"
				+ ",ca.ContactName as userName,ca.Sex as sex,ca.Age as age,ca.Telephone as telphone"
				+",bpay.RefundStatus as refundStatus,date_format(bpay.RefundTime, '%Y-%m-%d %H:%i:%s') as refundTime "
				+ " from business_d2p_tuwen_order tuwen");
		sqlBuilder.append(" left join doctor_detail_info doc on tuwen.DoctorId=doc.Id ");
		sqlBuilder.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" left join user_case_info ca on tuwen.CaseId=ca.Id ");
		sqlBuilder.append(" left join business_pay_info bpay on tuwen.Id =bpay.OrderId and bpay.OrderType=6 and bpay.PayStatus=1 ");
		sqlBuilder.append(" where 1=1 and (tuwen.DelFlag=0 or tuwen.DelFlag is null) ");
		sqlBuilder.append(gainstatusquery(querymap));
		sqlBuilder.append(gainsearchquery(querymap));
		sqlBuilder.append(" order by tuwen.CreateTime desc");
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
		countSql.append(" select count(tuwen.Id) from business_d2p_tuwen_order tuwen ");
		countSql.append(" left join doctor_detail_info doc on tuwen.DoctorId=doc.Id ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" left join user_case_info ca on tuwen.CaseId=ca.Id ");
		countSql.append(" left join business_pay_info bpay on tuwen.Id =bpay.OrderId and bpay.OrderType=6 and bpay.PayStatus=1 ");
		countSql.append(" where 1=1 and (tuwen.DelFlag=0 or tuwen.DelFlag is null) ");
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
		return map;	}
	
}
