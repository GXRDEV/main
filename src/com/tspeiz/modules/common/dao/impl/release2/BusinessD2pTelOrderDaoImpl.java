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
import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IBusinessD2pTelOrderDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pTelOrder;

@Repository
public class BusinessD2pTelOrderDaoImpl extends
		BaseDaoImpl<BusinessD2pTelOrder> implements IBusinessD2pTelOrderDao {
	private String gainstatusquery(Map<String, Object> querymap) {
		Integer ostatus = Integer.parseInt(querymap.get("ostatus").toString());
		String str = "";
		switch (ostatus) {
		case 1:
			str = " and dtel.PayStatus=4 ";
			break;
		case 2:
			str = " and dtel.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey()+" and dtel.PayStatus=1 ";
			break;
		case 3:
			str = " and dtel.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey()+" and dtel.PayStatus=1 ";
			break;
		case 4:
			str = " and dtel.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey()+" and dtel.PayStatus=1 ";
			break;
		case 5:
			str = " and dtel.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey()+" and dtel.PayStatus=1 ";
			break;
		case 6:
			str = " and dtel.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();
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
			sb.append(" (bpay.PayStatus=1 and (CASE WHEN (bpay.RefundStatus IS NULL or bpay.RefundStatus=0) THEN \"待退款\" WHEN bpay.RefundStatus=1 THEN \"退款成功\" "
					+ " WHEN bpay.RefundStatus=-1 THEN \"退款失败\"  END ) LIKE '%"+search+"%' ) ");
			sb.append(" ) ");
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryd2pteldatas(Map<String, Object> querymap,
			final Integer start, final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("SELECT dtel.Id,dtel.CreateTime,dtel.ReceiveTime,dtel.PayStatus,dtel.Status,dtel.OrderTime"
						+ ",doc.DisplayName as docName,doc.Profession as profession,hos.DisplayName as hosName,dep.DisplayName as depName"
						+ ",ca.ContactName as userName,ca.Sex as sex,ca.Age as age,ca.Telephone as telphone"
						+",bpay.RefundStatus as refundStatus,date_format(bpay.RefundTime, '%Y-%m-%d %H:%i:%s') as refundTime "
						+ " from business_d2p_tel_order dtel");
		sqlBuilder
				.append(" left join doctor_detail_info doc on dtel.DoctorId=doc.Id ");
		sqlBuilder
				.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder
				.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" left join user_case_info ca on dtel.CaseId=ca.Id ");
		sqlBuilder.append(" left join business_pay_info bpay on dtel.Id =bpay.OrderId and bpay.OrderType=7 and bpay.PayStatus=1 ");
		sqlBuilder
				.append(" where 1=1 and (dtel.DelFlag=0 or dtel.DelFlag is null) ");
		sqlBuilder.append(gainstatusquery(querymap));
		sqlBuilder.append(gainsearchquery(querymap));
		sqlBuilder.append(" order by dtel.CreateTime desc");
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
		final StringBuilder countSql = new StringBuilder();
		countSql.append("SELECT count(dtel.Id) from business_d2p_tel_order dtel ");
		countSql.append(" left join doctor_detail_info doc on dtel.DoctorId=doc.Id ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" left join user_case_info ca on dtel.CaseId=ca.Id ");
		countSql.append(" left join business_pay_info bpay on dtel.Id =bpay.OrderId and bpay.OrderType=7 and bpay.PayStatus=1 ");
		countSql.append(" where 1=1 and (dtel.DelFlag=0 or dtel.DelFlag is null) ");
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
	public D2pOrderBean queryOrderDetailInfo(Integer oid, Integer otype) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder(
				"select obean.Id,obean.UUID as uuid,obean.CaseId,obean.CreateTime,obean.ReceiveTime,obean.PayStatus,obean.Status,obean.DoctorId,");
		sqlBuilder
				.append("doc.DisplayName as docName,doc.Profession as profession,hos.DisplayName as hosName,dep.DisplayName as depName");
		sqlBuilder
				.append(",ca.ContactName as userName,ca.CaseName,ca.Sex as sex,ca.Age as age,ca.Telephone as telphone,obean.ClosedTime,obean.CloserType,obean.CloserId,");
		sqlBuilder.append("obean.Source,ca.IdCard as idCard,ca.AskProblem,ca.PresentHistory as presentIll");
		if (otype.equals(6)) {
			// 图文
			sqlBuilder
					.append(",obean.DocFirstAnswerTime,obean.DocLastAnswerTime,obean.PatLastAnswerTime,obean.DocSendMsgCount,obean.PatSendMsgCount,obean.DocUnreadMsgNum,obean.PatUnreadMsgNum");
			sqlBuilder.append(" from business_d2p_tuwen_order obean ");
		} else if (otype.equals(7)) {
			// 电话
			sqlBuilder
					.append(",obean.OrderTime,obean.OrderDur,obean.TalkTime,obean.TalkDur,obean.Remark,obean.AnswerTelephone ");
			sqlBuilder.append(" from business_d2p_tel_order obean ");
		} else if (otype.equals(9)) {
			// 快速
			sqlBuilder
					.append(",obean.DocFirstAnswerTime,obean.DocLastAnswerTime,obean.PatLastAnswerTime,obean.DocSendMsgCount,obean.PatSendMsgCount,obean.DocUnreadMsgNum,obean.PatUnreadMsgNum");
			sqlBuilder.append(" from business_d2p_fastask_order obean ");
		}
		sqlBuilder
				.append(" left join doctor_detail_info doc on obean.DoctorId=doc.Id ");
		sqlBuilder
				.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder
				.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder
				.append(" left join user_case_info ca on obean.CaseId=ca.Id ");
		sqlBuilder
				.append(" where 1=1 and (obean.DelFlag=0 or obean.DelFlag is null) ");
		sqlBuilder.append(" and obean.Id="+oid);
		List<D2pOrderBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());

						query.setResultTransformer(Transformers
								.aliasToBean(D2pOrderBean.class));
						return query.list();
					}
				});
		return list.size() > 0 ? list.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	public List<D2pOrderBean> querymyorders(Integer userid, String ltype,
			Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sb = gianCommonD2pOrderInfoSql(ltype, userid);
		List<D2pOrderBean> list = this.getListByPageBySql(sb.toString(),
				D2pOrderBean.class, pageNo, pageSize);
		return list;
	}

	private StringBuilder gianCommonD2pOrderInfoSql(String ltype, Integer userid) {
		final StringBuilder sb = new StringBuilder();
		String status = "";
		String paystatus = "";
		String isappraise = "";
		// 1：待付款，2：待就诊，3：待评价，4：历史订单
		if (!StringUtils.isNotBlank(ltype))
			ltype = "1";
		if (ltype.equalsIgnoreCase("1")) {
			// 待付款
			status = "10";
			paystatus = "4";
		} else if (ltype.equalsIgnoreCase("2")) {
			// 待就诊
			status = "10,20";
			paystatus = "1";
		} else if (ltype.equalsIgnoreCase("3")) {
			// 待评价
			status = "40";
			paystatus = "1,4";
			isappraise = "0";
		} else if (ltype.equalsIgnoreCase("4")) {
			// 历史订单
			status = "30,40,50";
		}
		sb.append("SELECT main.Type, main.Id, main.DoctorId,main.expertId as expertId,doc.DisplayName as docName,");
		sb.append(" hos.DisplayName as hosName, dep.DisplayName as depName,exp.DisplayName as expertName,exhos.DisplayName as expertHos,exdep.DisplayName as expertDep,");
		sb.append(" main.Status as status, DATE_FORMAT(main.otime,'%Y-%m-%d %h:%i') timeStr,main.otime as createTime, main.PayStatus as payStatus,ca.AskProblem  ");
		sb.append(" FROM ( ");
		// 远程会诊
		sb.append("SELECT '4' as Type, rm.Id, rm.LocalDoctorId as DoctorId,rm.ExpertId as expertId,rm.CaseId, rm.Status,rm.CreateTime as otime, rm.PayStatus");
		sb.append(" FROM business_vedio_order rm ");
		sb.append(" WHERE rm.Status in (").append(status)
				.append(") AND rm.UserId = ").append(userid)
				.append(" and rm.PayStatus in(" + paystatus + ") ");
		if (StringUtils.isNotBlank(isappraise)) {
			sb.append(" and rm.IsAppraisal= " + isappraise);
		}
		sb.append(" UNION ");
		//专家咨询
		sb.append("SELECT '5' as Type, rm.Id, rm.LocalDoctorId as DoctorId,rm.ExpertId as expertId,rm.CaseId, rm.Status,rm.CreateTime as otime, rm.PayStatus");
		sb.append(" FROM business_vedio_order rm ");
		sb.append(" WHERE rm.Status in (").append(status)
				.append(") AND rm.UserId = ").append(userid)
				.append(" and rm.PayStatus in(" + paystatus + ") ");
		if (StringUtils.isNotBlank(isappraise)) {
			sb.append(" and rm.IsAppraisal= " + isappraise);
		}
		sb.append(" UNION ");
		// 电话问诊(患者-医生）
		sb.append("SELECT '7' as Type, tel.Id, tel.DoctorId,null as expertId,tel.CaseId,tel.Status, tel.CreateTime as otime, tel.PayStatus");
		sb.append(" FROM business_d2p_tel_order tel");
		sb.append(" WHERE tel.Status in (").append(status)
				.append(") AND tel.UserId = ").append(userid)
				.append(" and tel.PayStatus in(" + paystatus + ") ");
		if (StringUtils.isNotBlank(isappraise)) {
			sb.append(" and tel.IsAppraisal= " + isappraise);
		}
		sb.append(" UNION ");
		// 图文问诊(患者-医生）
		sb.append("SELECT '6' as Type, tw.Id, tw.DoctorId,null as expertId,tw.CaseId, tw.Status,tw.CreateTime as otime, tw.PayStatus");
		sb.append(" FROM business_d2p_tuwen_order tw");
		sb.append(" WHERE tw.Status in (").append(status)
				.append(") AND tw.UserId = ").append(userid)
				.append(" and tw.PayStatus in(" + paystatus + ") ");
		if (StringUtils.isNotBlank(isappraise)) {
			sb.append(" and tw.IsAppraisal= " + isappraise);
		}
		sb.append(" UNION ");
		// 快速问诊
		sb.append("SELECT '9' as Type, fast.Id,fast.DoctorId as DoctorId,null as expertId,fast.CaseId,fast.Status, fast.CreateTime as otime");
		sb.append(" ,fast.PayStatus");
		sb.append(" FROM business_d2p_fastask_order fast");
		sb.append(" WHERE fast.Status in (").append(status)
				.append(") AND fast.UserId = ").append(userid)
				.append(" and fast.PayStatus in(" + paystatus + ") ");
		if (StringUtils.isNotBlank(isappraise)) {
			sb.append(" and fast.IsAppraisal= " + isappraise);
		}
		sb.append(" ) main ");
		sb.append(" LEFT JOIN doctor_detail_info doc ON doc.Id = main.DoctorId");
		sb.append(" LEFT JOIN doctor_detail_info exp on exp.Id=main.expertId ");
		sb.append(" LEFT JOIN hospital_detail_info hos ON hos.Id = doc.HospitalId");
		sb.append(" LEFT JOIN hospital_detail_info exhos on exhos.Id=exp.HospitalId");
		sb.append(" LEFT JOIN hospital_department_info dep ON dep.Id = doc.DepId");
		sb.append(" LEFT JOIN hospital_department_info exdep on exdep.Id=exp.DepId");
		sb.append(" left join user_case_info ca on ca.Id=main.CaseId ");
		sb.append(" ORDER BY main.otime DESC");
		return sb;
	}
}
