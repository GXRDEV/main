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
import com.tspeiz.modules.common.bean.dto.D2DTuwenDto;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.ISpecialAdviceOrderDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;

@Repository
public class SpecialAdviceOrderDaoImpl extends BaseDaoImpl<SpecialAdviceOrder> implements ISpecialAdviceOrderDao{

	@SuppressWarnings("unchecked")
	public Map<String, Object> querySpecialAdviceOrdersByCondition(
			Integer type, Integer userId, String search,final Integer start,
			final Integer length,Integer utype) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select sp.Id,ca.ContactName as userName,ca.IdCard as idCard,ca.PresentHistory as diseaseDes"
				+ ",ca.Sex as sex,ca.Age as age,doc.DisplayName as expertName,doc.HeadImageUrl as headImageUrl,"
				+ "hos.DisplayName as hosName,dep.DisplayName as depName,sp.CreateTime,sp.PayStatus,sp.Status,sp.DocUnreadMsgNum,sp.ExpertUnreadMsgNum  from business_d_tuwen_order sp");
		sqlBuilder.append(" left join user_case_info ca on sp.CaseId=ca.Id ");
		if(utype.equals(2)){
			sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=sp.DoctorId ");
		}else if(utype.equals(3)){
			sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=sp.ExpertId ");
		}
		//sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=sp.ExpertId ");
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=doc.HospitalId");
		sqlBuilder.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sqlBuilder.append(" where 1=1");
		if(type.equals(1)){
			//进行中
			sqlBuilder.append(" and sp.Status in(10,20) ");
		}else if(type.equals(2)){
			//历史的
			sqlBuilder.append(" and sp.Status in (30,40,50)");
		}
		//专家端的未付款的不显示
		if(utype.equals(2)){
			sqlBuilder.append(" and sp.ExpertId="+userId);
			sqlBuilder.append(" and sp.PayStatus=1");
		}else if(utype.equals(3)){
			sqlBuilder.append(" and sp.DoctorId="+userId);
		}
		/*and sp.PayStatus=1 */
		sqlBuilder.append(" and (sp.DelFlag=0 or sp.DelFlag IS NULL)");
		List<SpecialAdviceOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(SpecialAdviceOrder.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) FROM business_d_tuwen_order sp where 1=1 ");
		if(type.equals(1)){
			//进行中
			countSql.append(" and sp.Status in(10,20) ");
		}else if(type.equals(2)){
			//已结束
			countSql.append(" and sp.Status in (30,40,50)");
		}
		if(utype.equals(2)){
			countSql.append(" and sp.ExpertId="+userId);
			countSql.append(" and sp.PayStatus=1");
		}else if(utype.equals(3)){
			countSql.append(" and sp.DoctorId="+userId);
		}
		
		/*and sp.PayStatus=1 */
		countSql.append(" and (sp.DelFlag=0 or sp.DelFlag IS NULL) ");
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
	public SpecialAdviceOrder querySpecialAdviceOrderByUid(String uuid) {
		// TODO Auto-generated method stub
		String hql="from SpecialAdviceOrder where 1=1 and uuid='"+uuid+"' ";
		List<SpecialAdviceOrder> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
	private String gainstatusquery(Integer ostatus) {
		String str = "";
		switch (ostatus) {
		case 1:
			str = " and sp.PayStatus=4 ";
			break;
		case 2:
			str = " and sp.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();
			break;
		case 3:
			str = " and sp.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey();
			break;
		case 4:
			str = " and sp.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey();
			break;
		case 5:
			str = " and sp.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey();
			break;
		case 6:
			str = " and sp.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();
			break;
		}
		return str;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> querySpecialAdviceOrdersByCondition_sys(
			Integer type, String search,final Integer start,
			final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select sp.Id,ca.ContactName as userName,ca.IdCard as idCard,ca.AskProblem as diseaseDes"
				+ ",ca.Sex as sex,ca.Age as age,special.DisplayName as expertName,special.HeadImageUrl as headImageUrl,"
				+ "ehos.DisplayName as hosName,edep.DisplayName as depName,doc.DisplayName as localDocName,hos.DisplayName as localHosName,"
				+ "dep.DisplayName as localDepName,sp.CreateTime,sp.PayStatus,sp.Status from business_d_tuwen_order sp");
		sqlBuilder.append(" left join user_case_info ca on sp.CaseId=ca.Id ");
		sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=sp.DoctorId ");//当地医生
		sqlBuilder.append(" left join doctor_detail_info special on special.Id=sp.ExpertId ");//专家
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=doc.HospitalId");//当地医院
		sqlBuilder.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");//当地科室
		sqlBuilder.append(" left join hospital_detail_info ehos on ehos.Id=special.HospitalId ");//专家医院
		sqlBuilder.append(" left join hospital_department_info edep on edep.Id=special.DepId ");//专家科室
		sqlBuilder.append(" where 1=1");
		sqlBuilder.append(gainstatusquery(type));
		sqlBuilder.append(" and (sp.DelFlag=0 or sp.DelFlag IS NULL) ");
		List<SpecialAdviceOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(SpecialAdviceOrder.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) FROM business_d_tuwen_order sp where 1=1 ");
		countSql.append(gainstatusquery(type));
		countSql.append(" and (sp.DelFlag=0 or sp.DelFlag IS NULL) ");
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
	private String gainsearchquery(String search,boolean refundSearch){
		StringBuilder sb=new StringBuilder();
		if(StringUtils.isNotBlank(search)){
			sb.append(" and ( ");
			sb.append(" cinfo.ContactName like '%"+search+"%' or ");
			sb.append(" cinfo.Telephone like '%"+search+"%' or ");
			sb.append(" uinfo.Telephone like '%"+search+"%' or ");
			sb.append(" exdoc.DisplayName like '%"+search+"%' or ");
			sb.append(" exhos.DisplayName like '%"+search+"%' or ");
			sb.append(" exdep.DisplayName like '%"+search+"%' or ");
			sb.append(" localdoc.DisplayName like '%"+search+"%' or ");
			sb.append(" localhos.DisplayName like '%"+search+"%' or ");
			sb.append(" localdep.DisplayName like '%"+search+"%' ");
			if(refundSearch){
				sb.append(" or ");
				sb.append(" (bpay.PayStatus=1 and (CASE WHEN (bpay.RefundStatus IS NULL or bpay.RefundStatus=0) THEN \"待退款\" WHEN bpay.RefundStatus=1 THEN \"退款成功\" "
						+ " WHEN bpay.RefundStatus=-1 THEN \"退款失败\"  END ) LIKE '%"+search+"%' ) ");
			}
			sb.append(" )");
		}
		return sb.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> querySpecialAdviceOrdersByCondition_new(
			Integer type, String search,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sb=new StringBuilder();
		sb.append("select sp.Id,sp.ExpertId,");
		sb.append(" cinfo.ContactName as userName,cinfo.sex,cinfo.Age,");
		sb.append(" (CASE WHEN cinfo.Telephone is null or cinfo.Telephone='' THEN uinfo.Telephone WHEN cinfo.Telephone is not null and cinfo.Telephone!='' THEN cinfo.Telephone END ) as telephone");
		sb.append(",exdoc.DisplayName as expertName,exhos.DisplayName as hosName,exdep.DisplayName as depName,exdoc.Profession as exProfession,");
		sb.append(" localdoc.DisplayName as localDocName,localhos.DisplayName as localHosName,localdep.DisplayName as localDepName,localdoc.Profession as localProfession, ");
		sb.append(" sp.CreateTime,sp.ReceiveTime,sp.PayStatus ");
		sb.append(",bpay.RefundStatus as refundStatus,date_format(bpay.RefundTime, '%Y-%m-%d %H:%i:%s') as refundTime ");

		sb.append(" from business_d_tuwen_order sp ");
		sb.append(" left join user_case_info cinfo on cinfo.Id=sp.CaseId ");
		sb.append(" left join user_contact_info uinfo on uinfo.UUID=sp.SubUserUuid ");
		sb.append(" left join doctor_detail_info exdoc on exdoc.Id=sp.ExpertId ");
		sb.append(" left join hospital_detail_info exhos on exhos.Id=exdoc.HospitalId ");
		sb.append(" left join hospital_department_info exdep on exdep.Id=exdoc.DepId ");
		sb.append(" left join doctor_detail_info localdoc on localdoc.Id=sp.DoctorId ");
		sb.append(" left join hospital_detail_info localhos on localhos.Id=localdoc.HospitalId ");
		sb.append(" left join hospital_department_info localdep on localdep.Id=localdoc.DepId ");
		sb.append(" left join business_pay_info bpay on sp.Id =bpay.OrderId and bpay.OrderType=5 and bpay.PayStatus=1 ");
		sb.append(" where 1=1 ");
		sb.append(gainstatusquery(type));
		sb.append(gainsearchquery(search,true));
		sb.append(" and (sp.DelFlag=0 or sp.DelFlag IS NULL) ");
		sb.append(" order by sp.CreateTime desc ");
		List<SpecialAdviceOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(SpecialAdviceOrder.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(sp.Id) FROM business_d_tuwen_order sp ");
		countSql.append(" left join user_case_info cinfo on cinfo.Id=sp.CaseId ");
		countSql.append(" left join user_contact_info uinfo on uinfo.UUID=sp.SubUserUuid ");
		countSql.append(" left join doctor_detail_info exdoc on exdoc.Id=sp.ExpertId ");
		countSql.append(" left join hospital_detail_info exhos on exhos.Id=exdoc.HospitalId ");
		countSql.append(" left join hospital_department_info exdep on exdep.Id=exdoc.DepId ");
		countSql.append(" left join doctor_detail_info localdoc on localdoc.Id=sp.DoctorId ");
		countSql.append(" left join hospital_detail_info localhos on localhos.Id=localdoc.HospitalId ");
		countSql.append(" left join hospital_department_info localdep on localdep.Id=localdoc.DepId ");
		countSql.append(" left join business_pay_info bpay on sp.Id =bpay.OrderId and bpay.OrderType=5 and bpay.PayStatus=1 ");
		countSql.append(" where 1=1 ");
		countSql.append(gainstatusquery(type));
		countSql.append(gainsearchquery(search,true));
		
		countSql.append(" and (sp.DelFlag=0 or sp.DelFlag IS NULL) ");
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
	public Map<String, Object> queryD2DTuwenDatas_doc(Integer docid,
			String searchContent,Integer ostatus,final Integer start,
			final Integer length, Integer dtype) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sb=new StringBuilder();
		sb.append("select sp.Id as orderId,sp.ExpertId,sp.UUID as uuid,");
		sb.append(" cinfo.ContactName as userName,cinfo.sex,cinfo.Age,");
		sb.append(" (CASE WHEN cinfo.Telephone is null or cinfo.Telephone='' THEN uinfo.Telephone WHEN cinfo.Telephone is not null and cinfo.Telephone!='' THEN cinfo.Telephone END ) as telephone");
		sb.append(",exdoc.DisplayName as exDocName,exhos.DisplayName as exHosName,exdep.DisplayName as exDepName,");
		sb.append(" localdoc.DisplayName as localDocName,localhos.DisplayName as localHosName,localdep.DisplayName as localDepName, ");
		sb.append("DATE_FORMAT(sp.CreateTime,'%Y-%m-%d %T') as createTime,DATE_FORMAT(sp.ReceiveTime,'%Y-%m-%d %T') as receiveTime, ");
		sb.append(" sp.PayStatus ");
		sb.append(" from business_d_tuwen_order sp ");
		sb.append(" left join user_case_info cinfo on cinfo.Id=sp.CaseId ");
		sb.append(" left join user_contact_info uinfo on uinfo.UUID=cinfo.SubUserUuid ");
		sb.append(" left join doctor_detail_info exdoc on exdoc.Id=sp.ExpertId ");
		sb.append(" left join hospital_detail_info exhos on exhos.Id=exdoc.HospitalId ");
		sb.append(" left join hospital_department_info exdep on exdep.Id=exdoc.DepId ");
		sb.append(" left join doctor_detail_info localdoc on localdoc.Id=sp.DoctorId ");
		sb.append(" left join hospital_detail_info localhos on localhos.Id=localdoc.HospitalId ");
		sb.append(" left join hospital_department_info localdep on localdep.Id=localdoc.DepId ");
		sb.append(" where 1=1 ");
		sb.append(gainstatusquery(ostatus));
		sb.append(gainsearchquery(searchContent,false));
		if(dtype.equals(1)){
			//我发起的
			sb.append(" and sp.DoctorId="+docid);
		}else if(dtype.equals(2)){
			//我接收的
			sb.append(" and sp.ExpertId="+docid);
		}
		sb.append(" and (sp.DelFlag=0 or sp.DelFlag IS NULL) ");
		sb.append(" order by sp.CreateTime desc ");
		List<D2DTuwenDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(D2DTuwenDto.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(sp.Id) FROM business_d_tuwen_order sp  ");
		countSql.append(" left join user_case_info cinfo on cinfo.Id=sp.CaseId ");
		countSql.append(" left join user_contact_info uinfo on uinfo.UUID=sp.SubUserUuid ");
		countSql.append(" left join doctor_detail_info exdoc on exdoc.Id=sp.ExpertId ");
		countSql.append(" left join hospital_detail_info exhos on exhos.Id=exdoc.HospitalId ");
		countSql.append(" left join hospital_department_info exdep on exdep.Id=exdoc.DepId ");
		countSql.append(" left join doctor_detail_info localdoc on localdoc.Id=sp.DoctorId ");
		countSql.append(" left join hospital_detail_info localhos on localhos.Id=localdoc.HospitalId ");
		countSql.append(" left join hospital_department_info localdep on localdep.Id=localdoc.DepId ");
		countSql.append(" where 1=1 ");
		countSql.append(gainstatusquery(ostatus));
		countSql.append(gainsearchquery(searchContent,false));
		if(dtype.equals(1)){
			//我发起的
			countSql.append(" and sp.DoctorId="+docid);
		}else if(dtype.equals(2)){
			//我接收的
			countSql.append(" and sp.ExpertId="+docid);
		}
		countSql.append(" and (sp.DelFlag=0 or sp.DelFlag IS NULL) ");
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
	public Map<String, Object> queryD2DTuwenDatas_expert(Integer docid,
			String searchContent,Integer ostatus,final Integer start,
			final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sb=new StringBuilder();
		sb.append("select sp.Id as orderId,sp.ExpertId,sp.UUID as uuid,");
		sb.append(" cinfo.ContactName as userName,cinfo.sex,cinfo.Age,");
		sb.append(" (CASE WHEN cinfo.Telephone is null or cinfo.Telephone='' THEN uinfo.Telephone WHEN cinfo.Telephone is not null and cinfo.Telephone!='' THEN cinfo.Telephone END ) as telephone");
		sb.append(",exdoc.DisplayName as exDocName,exhos.DisplayName as exHosName,exdep.DisplayName as exDepName,");
		sb.append(" localdoc.DisplayName as localDocName,localhos.DisplayName as localHosName,localdep.DisplayName as localDepName, ");
		sb.append("DATE_FORMAT(sp.CreateTime,'%Y-%m-%d %T') as createTime,DATE_FORMAT(sp.ReceiveTime,'%Y-%m-%d %T') as receiveTime, ");
		sb.append(" sp.PayStatus ");
		sb.append(" from business_d_tuwen_order sp ");
		sb.append(" left join user_case_info cinfo on cinfo.Id=sp.CaseId ");
		sb.append(" left join user_contact_info uinfo on uinfo.UUID=sp.SubUserUuid ");
		sb.append(" left join doctor_detail_info exdoc on exdoc.Id=sp.ExpertId ");
		sb.append(" left join hospital_detail_info exhos on exhos.Id=exdoc.HospitalId ");
		sb.append(" left join hospital_department_info exdep on exdep.Id=exdoc.DepId ");
		sb.append(" left join doctor_detail_info localdoc on localdoc.Id=sp.DoctorId ");
		sb.append(" left join hospital_detail_info localhos on localhos.Id=localdoc.HospitalId ");
		sb.append(" left join hospital_department_info localdep on localdep.Id=localdoc.DepId ");
		sb.append(" where 1=1 ");
		sb.append(gainstatusquery(ostatus));
		sb.append(gainsearchquery(searchContent,false));
		sb.append(" and sp.ExpertId="+docid);
		sb.append(" and (sp.DelFlag=0 or sp.DelFlag IS NULL) ");
		sb.append(" order by sp.CreateTime desc ");
		List<D2DTuwenDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(D2DTuwenDto.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(sp.Id) FROM business_d_tuwen_order sp ");
		countSql.append(" left join user_case_info cinfo on cinfo.Id=sp.CaseId ");
		countSql.append(" left join user_contact_info uinfo on uinfo.UUID=sp.SubUserUuid ");
		countSql.append(" left join doctor_detail_info exdoc on exdoc.Id=sp.ExpertId ");
		countSql.append(" left join hospital_detail_info exhos on exhos.Id=exdoc.HospitalId ");
		countSql.append(" left join hospital_department_info exdep on exdep.Id=exdoc.DepId ");
		countSql.append(" left join doctor_detail_info localdoc on localdoc.Id=sp.DoctorId ");
		countSql.append(" left join hospital_detail_info localhos on localhos.Id=localdoc.HospitalId ");
		countSql.append(" left join hospital_department_info localdep on localdep.Id=localdoc.DepId ");
		countSql.append(" where 1=1 ");
		countSql.append(gainstatusquery(ostatus));
		countSql.append(gainsearchquery(searchContent,false));
		countSql.append(" and sp.ExpertId="+docid);
		countSql.append(" and (sp.DelFlag=0 or sp.DelFlag IS NULL) ");
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
	public List<SpecialAdviceOrder> gettuwenList(Integer sta, String hosid, String depid, String startDate, String endDate) {
		final StringBuilder sqlBuilder = new StringBuilder("");
		// TODO Auto-generated method stub
		sqlBuilder
		.append(" SELECT sp.Id,sp.Status AS status,cinfo.ContactName AS userName,cinfo.sex,cinfo.Age,"
				+ " exdoc.DisplayName AS expertName,exhos.DisplayName AS hosName,exdep.DisplayName AS depName,"
				+ " localdoc.DisplayName AS localDocName,localhos.DisplayName AS localHosName,localdep.DisplayName AS localDepName,"
				+ " sp.CreateTime,sp.PayStatus,bpay.RefundStatus AS refundStatus,DATE_FORMAT(sp.ReceiveTime, '%Y-%m-%d') AS consultationDate,"
				+ " (CASE WHEN bpay.TotalMoney IS NOT NULL THEN bpay.TotalMoney WHEN bpay.TotalMoney IS NULL THEN dsi.Amount END) AS money");
		sqlBuilder.append(" FROM business_d_tuwen_order sp ");
		sqlBuilder.append(" LEFT JOIN user_case_info cinfo ON cinfo.Id=sp.CaseId  ");
		sqlBuilder.append(" LEFT JOIN doctor_service_info dsi ON dsi.DoctorId=sp.ExpertId AND dsi.ServiceId=5 ");
		sqlBuilder.append(" LEFT JOIN user_contact_info uinfo ON uinfo.UUID=sp.SubUserUuid ");
		sqlBuilder.append(" LEFT JOIN doctor_detail_info exdoc ON exdoc.Id=sp.ExpertId ");
		sqlBuilder.append(" LEFT JOIN hospital_detail_info exhos ON exhos.Id=exdoc.HospitalId ");
		sqlBuilder.append(" LEFT JOIN hospital_department_info exdep ON exdep.Id=exdoc.DepId ");
		sqlBuilder.append(" LEFT JOIN doctor_detail_info localdoc ON localdoc.Id=sp.DoctorId ");
		sqlBuilder.append(" LEFT JOIN hospital_detail_info localhos ON localhos.Id=localdoc.HospitalId ");
		sqlBuilder.append(" LEFT JOIN hospital_department_info localdep ON localdep.Id=localdoc.DepId  ");
		sqlBuilder.append(" LEFT JOIN business_pay_info bpay ON sp.Id =bpay.OrderId AND bpay.OrderType=5 ");
		sqlBuilder.append(" WHERE 1=1 " );
		if(sta!=null){
			sqlBuilder.append(" and sp.Status = "+sta+" ");
		}
		if(StringUtils.isNotBlank(startDate)){
			sqlBuilder.append(" and (DATE_FORMAT(sp.CreateTime,'%Y-%m-%d')>='" + startDate+"') ");
        }
        if(StringUtils.isNotBlank(endDate)){
        	sqlBuilder.append(" and (DATE_FORMAT(sp.CreateTime,'%Y-%m-%d')<='" + endDate + "') ");
        }
		if(StringUtils.isNotBlank(hosid)){
			sqlBuilder.append(" and (exhos.Id ='"+hosid+"' or localhos.Id='"+hosid+"') ");
		}
		if(StringUtils.isNotBlank(depid)){
			sqlBuilder.append(" and exdep.Id ='"+depid+"' ");
		}
		sqlBuilder.append(" AND (sp.DelFlag=0 OR sp.DelFlag IS NULL) ORDER BY sp.CreateTime DESC");
		List<SpecialAdviceOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(SpecialAdviceOrder.class));
						return query.list();
					}
				});
		return list;
	}
	private String gainstatusquery_hos(Integer ostatus,Integer dtype) {
		String str = "";
		switch (ostatus) {
		case 1:
			if(dtype.equals(1)){
				str = " and sp.DoctorId is null and sp.Status in("+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey()+","
						+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey()+") ";
			}else{
				str = " and sp.ExpertId is null and sp.Status in("+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey()+","
						+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey()+") ";
			}
			break;
		case 2:
			if(dtype.equals(1)){
				str = " and sp.DoctorId is not null and sp.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();
			}else{
				str = " and sp.ExpertId is not null and sp.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();
			}	
			break;
		case 3:
			str = " and sp.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey();
			break;
		case 4:
			str = " and sp.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey();
			break;
		case 5:
			str = " and sp.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey();
			break;
		case 6:
			str = " and sp.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();
			break;
		}
		return str;
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> querySpecialAdviceOrdersByCondition(Integer hosId,
			String search, Integer ostatus,final Integer start,
			final Integer length,final Integer dtype) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sb=new StringBuilder();
		sb.append("select sp.Id,sp.ExpertId,sp.Status,");
		sb.append(" cinfo.ContactName as userName,cinfo.sex,cinfo.Age,");
		sb.append(" (CASE WHEN cinfo.Telephone is null or cinfo.Telephone='' THEN uinfo.Telephone WHEN cinfo.Telephone is not null and cinfo.Telephone!='' THEN cinfo.Telephone END ) as telephone");
		sb.append(",exdoc.DisplayName as expertName,exhos.DisplayName as hosName,exdep.DisplayName as depName,exdoc.Profession as exProfession,");
		sb.append(" localdoc.DisplayName as localDocName,localhos.DisplayName as localHosName,localdep.DisplayName as localDepName,localdoc.Profession as localProfession, ");
		sb.append(" DATE_FORMAT(sp.CreateTime,'%Y-%m-%d %T') as createTimes,sp.ReceiveTime,sp.PayStatus ");
		sb.append(" from business_d_tuwen_order sp ");
		sb.append(" left join user_case_info cinfo on cinfo.Id=sp.CaseId ");
		sb.append(" left join user_contact_info uinfo on uinfo.UUID=sp.SubUserUuid ");
		sb.append(" left join doctor_detail_info exdoc on exdoc.Id=sp.ExpertId ");
		sb.append(" left join hospital_detail_info exhos on exhos.Id=exdoc.HospitalId ");
		sb.append(" left join hospital_department_info exdep on exdep.Id=exdoc.DepId ");
		sb.append(" left join doctor_detail_info localdoc on localdoc.Id=sp.DoctorId ");
		sb.append(" LEFT JOIN hospital_detail_info localhos ON localhos.Id=sp.LocalHospitalId ");
		sb.append(" left join hospital_department_info localdep on localdep.Id=localdoc.DepId ");
		sb.append(" left join business_pay_info bpay on sp.Id =bpay.OrderId and bpay.OrderType=5 ");
		sb.append(" where 1=1 ");
		sb.append(gainstatusquery_hos(ostatus,dtype));
		sb.append(gainsearchquery(search,false));
		sb.append(" and (sp.DelFlag=0 or sp.DelFlag IS NULL) ");
		if(dtype.equals(1)){
			//发起的
			sb.append(" and localhos.Id="+hosId);
		}else{
			//接受到
			sb.append(" and exhos.Id="+hosId);
		}
		sb.append("  order by sp.CreateTime desc ");
		List<SpecialAdviceOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(SpecialAdviceOrder.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
                "SELECT count(sp.Id) FROM business_d_tuwen_order sp ");
		countSql.append(" left join user_case_info cinfo on cinfo.Id=sp.CaseId ");
		countSql.append(" left join user_contact_info uinfo on uinfo.UUID=sp.SubUserUuid ");
		countSql.append(" left join doctor_detail_info exdoc on exdoc.Id=sp.ExpertId ");
		countSql.append(" left join hospital_detail_info exhos on exhos.Id=exdoc.HospitalId ");
		countSql.append(" left join hospital_department_info exdep on exdep.Id=exdoc.DepId ");
		countSql.append(" left join doctor_detail_info localdoc on localdoc.Id=sp.DoctorId ");
		countSql.append(" LEFT JOIN hospital_detail_info localhos ON localhos.Id=sp.LocalHospitalId ");
		countSql.append(" left join hospital_department_info localdep on localdep.Id=localdoc.DepId ");
		countSql.append(" left join business_pay_info bpay on sp.Id =bpay.OrderId and bpay.OrderType=5 ");
		countSql.append(" where 1=1 ");
		countSql.append(gainstatusquery_hos(ostatus,dtype));
		countSql.append(gainsearchquery(search,false));
		countSql.append(" and (sp.DelFlag=0 or sp.DelFlag IS NULL) ");
		if(dtype.equals(1)){
			//我发起的
			countSql.append(" and localhos.Id="+hosId);
		}else{
			//我接受到
			countSql.append(" and exhos.Id="+hosId);
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
}
