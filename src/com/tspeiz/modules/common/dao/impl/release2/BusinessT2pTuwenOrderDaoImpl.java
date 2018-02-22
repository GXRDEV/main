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
import com.tspeiz.modules.common.dao.release2.IBusinessT2pTuwenOrderDao;
import com.tspeiz.modules.common.entity.release2.BusinessT2pTuwenOrder;

@Repository
public class BusinessT2pTuwenOrderDaoImpl extends BaseDaoImpl<BusinessT2pTuwenOrder> implements IBusinessT2pTuwenOrderDao{
	private String gainstatusquery(Map<String,Object> querymap){
		Integer ostatus=Integer.parseInt(querymap.get("ostatus").toString());
		String str="";
		switch(ostatus){
		case 2://待接诊
			str= " and btt.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();
			break;
		case 3://已接诊
			str= " and btt.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey();
			break;
		case 4://待复诊
			str= " and btt.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RERUNNING.getKey();
			break;
		case 5://已完成
			str= " and btt.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey();
			break;
		case 6://已退诊
			str= " and btt.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey();
			break;
		case 7://已取消
			str= " and btt.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();
			break;
		}
		return str;
	}
	private String gainsearchquery(Map<String,Object> querymap){
		StringBuilder sb=new StringBuilder();
		if(querymap.containsKey("search")){
			String search=querymap.get("search").toString();
			sb.append(" and ( ");
			sb.append(" us.ContactName like '%"+search+"%' or ");
			sb.append(" us.Telephone like '%"+search+"%' or ");
			sb.append(" tdoc.DisplayName like '%"+search+"%' or ");
			sb.append(" thos.DisplayName like '%"+search+"%' or ");
			sb.append(" tdep.DisplayName like '%"+search+"%' or ");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' or ");
			sb.append(" dep.DisplayName like '%"+search+"%' or ");
			sb.append(" dt.TeamName like '%"+search+"%' or ");
			sb.append(" (bpay.PayStatus=1 and (CASE WHEN (bpay.RefundStatus IS NULL or bpay.RefundStatus=0) THEN \"待退款\" WHEN bpay.RefundStatus=1 THEN \"退款成功\" "
					+ " WHEN bpay.RefundStatus=-1 THEN \"退款失败\"  END ) LIKE '%"+search+"%' ) ");
			sb.append(" ) ");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryt2ptuwendatas(Map<String, Object> querymap,
			final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		String query=gainstatusquery(querymap);
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT btt.Id,date_format(btt.CreateTime,'%Y-%m-%d %H:%i:%s') as timeStr,btt.NeedDispatch,btt.DispatchTime,btt.DispatchNum"
				+ ",btt.PayStatus,btt.ReceiveTime,btt.Telephone,"
				+ "us.ContactName as userName,us.Sex,us.Age,dt.TeamName,us.Telephone as telephone, tdoc.DisplayName as tdocName,thos.DisplayName as tdocHos,"
				+"bpay.RefundStatus as refundStatus,date_format(bpay.RefundTime, '%Y-%m-%d %H:%i:%s') as refundTime, "
				+ "tdep.DisplayName as tdocDep,doc.DisplayName as docName,hos.DisplayName as docHos,dep.DisplayName as docDep from business_t2p_tuwen_order btt ");
		sqlBuilder.append(" left join user_contact_info us on us.UUID=btt.SubUserUuid ");
		sqlBuilder.append(" left join doctor_team dt on dt.UUID=btt.TeamUuid ");
		sqlBuilder.append(" left join doctor_team_member dtm on dtm.TeamUuid=dt.UUID and dtm.Role=1 ");
		sqlBuilder.append(" left join doctor_detail_info tdoc on tdoc.Id=dtm.DoctorId ");
		sqlBuilder.append(" left join hospital_detail_info thos on thos.Id=tdoc.HospitalId ");
		sqlBuilder.append(" left join hospital_department_info tdep on tdep.Id=tdoc.DepId ");
		sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=btt.DoctorId ");
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=doc.HospitalId ");
		sqlBuilder.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sqlBuilder.append(" left join business_pay_info bpay on btt.Id =bpay.OrderId and bpay.OrderType=12 and bpay.PayStatus=1 ");
		sqlBuilder.append(" where 1=1 and (btt.DelFlag is null or btt.DelFlag=0) ");
		sqlBuilder.append(query);
		sqlBuilder.append(gainsearchquery(querymap));
		sqlBuilder.append(" order by btt.CreateTime desc ");
		List<BusinessT2pTuwenOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessT2pTuwenOrder.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("SELECT count(btt.Id) from business_t2p_tuwen_order btt ");
		countSql.append(" left join user_contact_info us on us.UUID=btt.SubUserUuid ");
		countSql.append(" left join doctor_team dt on dt.UUID=btt.TeamUuid ");
		countSql.append(" left join doctor_team_member dtm on dtm.TeamUuid=dt.UUID and dtm.Role=1 ");
		countSql.append(" left join doctor_detail_info tdoc on tdoc.Id=dtm.DoctorId ");
		countSql.append(" left join hospital_detail_info thos on thos.Id=tdoc.HospitalId ");
		countSql.append(" left join hospital_department_info tdep on tdep.Id=tdoc.DepId ");
		countSql.append(" left join doctor_detail_info doc on doc.Id=btt.DoctorId ");
		countSql.append(" left join hospital_detail_info hos on hos.Id=doc.HospitalId ");
		countSql.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		countSql.append(" left join business_pay_info bpay on btt.Id =bpay.OrderId and bpay.OrderType=12 and bpay.PayStatus=1 ");
		countSql.append(" where 1=1 and (btt.DelFlag=0 or btt.DelFlag is null) ");
		countSql.append(query);
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
