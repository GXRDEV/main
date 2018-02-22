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
import com.tspeiz.modules.common.dao.release2.IBusinessD2pConsultationRequestDao;
import com.tspeiz.modules.common.entity.release2.BusinessD2pConsultationRequest;
@Repository
public class BusinessD2pConsultationRequestDaoImpl extends BaseDaoImpl<BusinessD2pConsultationRequest> implements IBusinessD2pConsultationRequestDao{

	private String gainstatusquery(Map<String,Object> querymap){
		Integer ostatus=Integer.parseInt(querymap.get("ostatus").toString());
		String str="";
		switch(ostatus){
		case 2:
			str= " and dcr.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();
			break;
		case 3:
			str= " and dcr.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey();
			break;
		case 4:
			str= " and dcr.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey();
			break;
		case 5:
			str= " and dcr.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey();
			break;
		case 6:
			str= " and dcr.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();
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
			sb.append(" dep.DisplayName like '%"+search+"%' or ");
			sb.append(" tdoc.DisplayName like '%"+search+"%' or ");
			sb.append(" thos.DisplayName like '%"+search+"%' or ");
			sb.append(" tdep.DisplayName like '%"+search+"%' or ");
			sb.append(" dtm.TeamName like '%"+search+"%' ");
			sb.append(" ) ");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryd2pconreqdatas(
			Map<String, Object> querymap,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT dcr.Id,dcr.CreateTime,dcr.AuditTime as receiveTime,dcr.Status,dtm.TeamName,dcr.ConsultationType "
				+ ",doc.DisplayName as docName,tdoc.DisplayName as tdocName,doc.Profession as profession,tdoc.Profession as tprofession,"
				+ "hos.DisplayName as hosName,thos.DisplayName as thosName,dep.DisplayName as depName,tdep.DisplayName as tdepName"
				+ ",ca.ContactName as userName,ca.Sex as sex,ca.Age as age,ca.Telephone as telphone"
				+ " from business_d2p_consultation_request dcr");
		sqlBuilder.append(" left join doctor_detail_info doc on dcr.DoctorId=doc.Id ");
		sqlBuilder.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" left join user_case_info ca on dcr.CaseId=ca.Id ");
		sqlBuilder.append(" left join doctor_team dtm on dtm.UUID=dcr.TeamUuid ");
		sqlBuilder.append(" LEFT JOIN doctor_team_member doctm ON doctm.`TeamUuid`=dtm.`UUID` AND doctm.`Role`=1" );
		sqlBuilder.append(" left join doctor_detail_info tdoc on doctm.DoctorId=tdoc.Id ");
		sqlBuilder.append(" left join hospital_detail_info thos on tdoc.HospitalId=thos.Id ");
		sqlBuilder.append(" left join hospital_department_info tdep on tdoc.DepId=tdep.Id ");
		sqlBuilder.append(" where 1=1 and (dcr.DelFlag=0 or dcr.DelFlag is null) ");
		sqlBuilder.append(gainstatusquery(querymap));
		sqlBuilder.append(gainsearchquery(querymap));
		sqlBuilder.append(" order by dcr.CreateTime desc ");
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
		countSql.append("SELECT count(dcr.Id) from business_d2p_consultation_request dcr ");
		countSql.append(" left join doctor_detail_info doc on dcr.DoctorId=doc.Id ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" left join user_case_info ca on dcr.CaseId=ca.Id ");
		countSql.append(" left join doctor_team dtm on dtm.UUID=dcr.TeamUuid ");
		countSql.append(" LEFT JOIN doctor_team_member doctm ON doctm.`TeamUuid`=dtm.`UUID` AND doctm.`Role`=1" );
		countSql.append(" left join doctor_detail_info tdoc on doctm.DoctorId=tdoc.Id ");
		countSql.append(" left join hospital_detail_info thos on tdoc.HospitalId=thos.Id ");
		countSql.append(" left join hospital_department_info tdep on tdoc.DepId=tdep.Id ");
		countSql.append(" where 1=1 and (dcr.DelFlag=0 or dcr.DelFlag is null) ");
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
