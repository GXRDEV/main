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

import com.tspeiz.modules.common.bean.D2pOrderBean;
import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.dto.ReferOrderDto;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IBusinessD2dReferralOrderDao;
import com.tspeiz.modules.common.entity.release2.BusinessD2dReferralOrder;
@Repository
public class BusinessD2dReferralOrderDaoImpl extends BaseDaoImpl<BusinessD2dReferralOrder> implements IBusinessD2dReferralOrderDao{
	private String gainstatusquery(Map<String,Object> querymap){
		Integer ostatus=Integer.parseInt(querymap.get("ostatus").toString());
		return gainstatusstr(ostatus);
	}
	private String gainstatusstr(Integer ostatus){
		String str="";
		switch(ostatus){
		case 1:
			str= " and (dro.ReferralDocId is null or dro.ReferralDocId is not null) and dro.Status in("+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey()+","+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey()+") ";
			break;
		case 2:
			str= " and dro.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();//待接诊
			break;
		case 3:
			str= " and dro.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey();//已接诊
			break;
		case 4:
			str= " and dro.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey();//已完成
			break;
		case 5:
			str= " and dro.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey();//已退诊
			break;
		case 6:
			str= " and dro.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();//已取消
			break;
		}
		return str;
	}
	private String gainstatusstrs(Integer ostatus, Integer dtype){
		String str="";
		switch(ostatus){
		case 1:
			if(dtype.equals(1)){
				str= " and dro.DoctorId is null and dro.Status in("+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey()+","+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey()+") ";
			}else{
				str= " and dro.ReferralDocId is null and dro.Status in("+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey()+","+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey()+") ";
			}
			break;
		case 2:
			if(dtype.equals(1)){
				str = " and dro.DoctorId is not null and dro.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();
			}else{
				str = " and dro.ReferralDocId is not null and dro.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();
			}	
			break;
		case 3:
			str= " and dro.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey();//已接诊
			break;
		case 4:
			str= " and dro.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey();//已完成
			break;
		case 5:
			str= " and dro.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey();//已退诊
			break;
		case 6:
			str= " and dro.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();//已取消
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
			sb.append(" uci.Telephone like '%"+search+"%' or ");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' or ");
			sb.append(" dep.DisplayName like '%"+search+"%' or ");
			sb.append(" tdoc.DisplayName like '%"+search+"%' or ");
			sb.append(" thos.DisplayName like '%"+search+"%' or ");
			sb.append(" tdep.DisplayName like '%"+search+"%'  ");
			sb.append(" ) ");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryd2preferdatas(
			Map<String, Object> querymap,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT dro.Id,dro.CreateTime,dro.ReceiveTime as receiveTime,dro.Status,dro.ReferralDate as orderDate,dro.ReferralType as consultationType "
				+ ",doc.DisplayName as docName,tdoc.DisplayName as tdocName,doc.Profession as profession,tdoc.Profession as tprofession,"
				+ "hos.DisplayName as hosName,thos.DisplayName as thosName,dep.DisplayName as depName,tdep.DisplayName as tdepName"
				+ ",ca.ContactName as userName,ca.Sex as sex,ca.Age as age,"
				+ " (CASE WHEN dro.SubUserUuid is not null THEN uci.Telephone ELSE ca.Telephone END) AS telphone"
				+ " from business_d2d_referral_order dro");
		sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=dro.DoctorId ");
		sqlBuilder.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" left join doctor_detail_info tdoc on tdoc.Id=dro.ReferralDocId ");
		sqlBuilder.append(" left join hospital_detail_info thos on thos.Id=tdoc.HospitalId ");
		sqlBuilder.append(" left join hospital_department_info tdep on tdep.Id=tdoc.DepId ");
		sqlBuilder.append(" left join user_case_info ca on ca.Id=dro.CaseId ");
		sqlBuilder.append(" left join user_contact_info uci on uci.UUID=dro.SubUserUuid ");
		sqlBuilder.append(" where 1=1 and (dro.DelFlag=0 or dro.DelFlag is null) ");
		sqlBuilder.append(gainstatusquery(querymap));
		sqlBuilder.append(gainsearchquery(querymap));
		sqlBuilder.append(" order by dro.CreateTime desc");
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
		countSql.append("SELECT count(dro.Id) from business_d2d_referral_order dro ");
		countSql.append(" left join doctor_detail_info doc on doc.Id=dro.DoctorId ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" left join doctor_detail_info tdoc on tdoc.Id=dro.ReferralDocId ");
		countSql.append(" left join hospital_detail_info thos on thos.Id=tdoc.HospitalId ");
		countSql.append(" left join hospital_department_info tdep on tdep.Id=tdoc.DepId ");
		countSql.append(" left join user_case_info ca on ca.Id=dro.CaseId ");
		countSql.append(" left join user_contact_info uci on uci.UUID=dro.SubUserUuid ");
		countSql.append(" where 1=1 and (dro.DelFlag=0 or dro.DelFlag is null) ");
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
	

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryReferordersByCondition(Integer docid,
			String searchContent, Integer ostatus,final Integer start,final Integer length,Integer dtype) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT dro.Id as referId,DATE_FORMAT(dro.CreateTime,'%Y-%m-%d %T') as createTime,DATE_FORMAT(dro.ReceiveTime,'%Y-%m-%d %T') as receiveTime,"
				+ "dro.Status,dro.ReferralDate as referDate,dro.ReferralType as referType "
				+ ",doc.DisplayName as docName,tdoc.DisplayName as referDocName,"
				+ "hos.DisplayName as hosName,thos.DisplayName as referHosName,"
				+ "dep.DisplayName as depName,tdep.DisplayName as referDepName"
				+ ",ca.ContactName as userName,ca.Sex as sex,ca.Age as age,"
				+ " (CASE WHEN dro.SubUserUuid is not null THEN uci.Telephone ELSE ca.Telephone END) AS telphone,"
				+ "ca.PresentHistory as caseDesc "
				+ " from business_d2d_referral_order dro");
		sqlBuilder.append(" left join hospital_detail_info thos on thos.Id=dro.ReferralHosId ");
		sqlBuilder.append(" left join hospital_department_info tdep on tdep.Id=dro.ReferralDepId ");
		sqlBuilder.append(" left join doctor_detail_info tdoc on tdoc.Id=dro.ReferralDocId ");
		sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=dro.DoctorId ");
		sqlBuilder.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" left join user_case_info ca on ca.Id=dro.CaseId ");
		sqlBuilder.append(" left join user_contact_info uci on uci.UUID=dro.SubUserUuid ");
		sqlBuilder.append(" where 1=1 and (dro.DelFlag=0 or dro.DelFlag is null) ");
		if(dtype.equals(1)){
			sqlBuilder.append(" and dro.DoctorId="+docid);
		}else if(dtype.equals(2)){
			sqlBuilder.append(" and dro.ReferralDocId="+docid);
		}
		sqlBuilder.append(gainstatusstr(ostatus));
		sqlBuilder.append(gainsearchquery(searchContent));
		sqlBuilder.append(" order by dro.CreateTime desc ");
		List<ReferOrderDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(ReferOrderDto.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("SELECT count(dro.Id) from business_d2d_referral_order dro ");
		countSql.append(" left join hospital_detail_info thos on thos.Id=dro.ReferralHosId ");
		countSql.append(" left join hospital_department_info tdep on tdep.Id=dro.ReferralDepId ");
		countSql.append(" left join doctor_detail_info tdoc on tdoc.Id=dro.ReferralDocId ");
		countSql.append(" left join doctor_detail_info doc on doc.Id=dro.DoctorId ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" left join user_case_info ca on ca.Id=dro.CaseId ");
		countSql.append(" left join user_contact_info uci on uci.UUID=dro.SubUserUuid ");
		countSql.append(" where 1=1 and (dro.DelFlag=0 or dro.DelFlag is null) ");
		if(dtype.equals(1)){
			countSql.append(" and dro.DoctorId="+docid);
		}else if(dtype.equals(2)){
			countSql.append(" and dro.ReferralDocId="+docid);
		}
		countSql.append(gainstatusstr(ostatus));
		countSql.append(gainsearchquery(searchContent));
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
	private String gainsearchquery(String search){
		StringBuilder sb=new StringBuilder();
		if(StringUtils.isNotBlank(search)){
			sb.append(" and ( ");
			sb.append(" ca.ContactName like '%"+search+"%' or ");
			sb.append(" ca.Telephone like '%"+search+"%' or ");
			sb.append(" uci.Telephone like '%"+search+"%' or ");
			sb.append(" thos.DisplayName like '%"+search+"%' or ");
			sb.append(" tdep.DisplayName like '%"+search+"%' or ");
			sb.append(" tdoc.DisplayName like '%"+search+"%' or ");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' or ");
			sb.append(" dep.DisplayName like '%"+search+"%' ");
			sb.append(" ) ");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryReferordersByCondition_hos(Integer hosId,
			String search, Integer ostatus,final Integer start,final Integer length,
			Integer dtype) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT dro.Id as referId,DATE_FORMAT(dro.CreateTime,'%Y-%m-%d %T') as createTime,DATE_FORMAT(dro.ReceiveTime,'%Y-%m-%d %T') as receiveTime,"
				+ "dro.Status,dro.ReferralDate as referDate,dro.ReferralType as referType "
				+ ",doc.DisplayName as docName,tdoc.DisplayName as referDocName,"
				+ "hos.DisplayName as hosName,thos.DisplayName as referHosName,"
				+ "dep.DisplayName as depName,tdep.DisplayName as referDepName"
				+ ",ca.ContactName as userName,ca.Sex as sex,ca.Age as age,"
				+ " (CASE WHEN dro.SubUserUuid is not null THEN uci.Telephone ELSE ca.Telephone END) AS telphone,"
				+ "ca.PresentHistory as caseDesc "
				+ " from business_d2d_referral_order dro");
		sqlBuilder.append(" left join hospital_detail_info thos on thos.Id=dro.ReferralHosId ");
		sqlBuilder.append(" left join hospital_department_info tdep on tdep.Id=dro.ReferralDepId ");
		sqlBuilder.append(" left join doctor_detail_info tdoc on tdoc.Id=dro.ReferralDocId ");
		sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=dro.DoctorId ");
		sqlBuilder.append(" LEFT JOIN hospital_detail_info hos ON dro.LocalHospitalId =hos.Id  ");
		sqlBuilder.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" left join user_case_info ca on ca.Id=dro.CaseId ");
		sqlBuilder.append(" left join user_contact_info uci on uci.UUID=dro.SubUserUuid ");
		sqlBuilder.append(" where 1=1 and (dro.DelFlag=0 or dro.DelFlag is null) ");
		if(dtype.equals(1)){
			//我发起的
			sqlBuilder.append(" and hos.Id="+hosId);
		}else if(dtype.equals(2)){
			//我接收的
			sqlBuilder.append(" and thos.Id="+hosId);
		}
		sqlBuilder.append(gainstatusstrs(ostatus,dtype));
		sqlBuilder.append(gainsearchquery(search));
		sqlBuilder.append(" order by dro.CreateTime desc ");
		List<ReferOrderDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(ReferOrderDto.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("SELECT count(dro.Id) from business_d2d_referral_order dro ");
		countSql.append(" left join hospital_detail_info thos on thos.Id=dro.ReferralHosId ");
		countSql.append(" left join hospital_department_info tdep on tdep.Id=dro.ReferralDepId ");
		countSql.append(" left join doctor_detail_info tdoc on tdoc.Id=dro.ReferralDocId ");
		countSql.append(" left join doctor_detail_info doc on doc.Id=dro.DoctorId ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" left join user_case_info ca on ca.Id=dro.CaseId ");
		countSql.append(" left join user_contact_info uci on uci.UUID=dro.SubUserUuid ");
		countSql.append(" where 1=1 and (dro.DelFlag=0 or dro.DelFlag is null) ");
		if(dtype.equals(1)){
			countSql.append(" and hos.Id="+hosId);
		}else if(dtype.equals(2)){
			countSql.append(" and thos.Id="+hosId);
		}
		countSql.append(gainstatusstr(ostatus));
		countSql.append(gainsearchquery(search));
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
	public BusinessD2dReferralOrder queryBusinessD2pReferralOrderByUuid(
			String orderUuid) {
		// TODO Auto-generated method stub
		String hql=" from BusinessD2dReferralOrder where 1=1 and uuid='"+orderUuid+"' ";
		List<BusinessD2dReferralOrder> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	@SuppressWarnings("unchecked")
	public MobileSpecial queryBusinessD2dReferralOrderByUserId(Integer docid) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder();
		sb.append(" SELECT doc.HospitalId AS hosId FROM doctor_detail_info doc ");
		sb.append(" LEFT JOIN hospital_detail_info hos ON doc.HospitalId=hos.Id  ");
		sb.append(" WHERE doc.Id="+docid+" ");
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}
}
