package com.tspeiz.modules.common.dao.impl.newrelease;

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

import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IBusinessWenZhenInfoDao;
import com.tspeiz.modules.common.entity.SystemServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenZhenInfo;

@Repository
public class BusinessWenZhenInfoDaoImpl extends
		BaseDaoImpl<BusinessWenZhenInfo> implements IBusinessWenZhenInfoDao {

	private String gainsearch(String search) {
		StringBuilder sb = new StringBuilder();
		sb.append(" and (");
		sb.append(" tw.CreateTime like BINARY '%" + search + "%' or ");
		sb.append(" ca.ContactName like '%" + search + "%' or ");
		if (search.equalsIgnoreCase("男")) {
			sb.append(" ca.Sex=1 or ");
		} else if (search.equalsIgnoreCase("女")) {
			sb.append(" ca.Sex=0 or ");
		}
		sb.append(" ca.Age like '%" + search + "%' or ");
		sb.append(" doc.DisplayName like '%" + search + "%' or ");
		sb.append(" hos.DisplayName like '%" + search + "%' or ");
		sb.append(" dep.DisplayName like '%" + search + "%' ");
		sb.append(" ) ");
		return sb.toString();
	}

	private String gainstatusquery(Integer ostatus) {
		String str = "";
		switch (ostatus) {
		case 1:
			str = " and tw.PayStatus=4 ";
			break;
		case 2:
			str = " and tw.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey()+" and tw.PayStatus=1 ";
			break;
		case 3:
			str = " and tw.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey()+" and tw.PayStatus=1 ";
			break;
		case 4:
			str = " and tw.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey()+" and tw.PayStatus=1 ";
			break;
		case 5:
			str = " and tw.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey()+" and tw.PayStatus=1 ";
			break;
		case 6:
			str = " and tw.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();
			break;
		}
		return str;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryTuwenOrdersByConditions(String search,
			final Integer start, final Integer length, Integer type) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		String querysearch = gainsearch(search);
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("SELECT tw.Id as id,ca.ContactName as contactName,ca.Sex as sex,ca.Age as age,ca.Telephone as telephone,doc.DisplayName as docName, tw.Status as askStatus "
						+ ",hos.DisplayName as hospital,dep.DisplayName as depart ,tw.CreateTime as createTime,tw.PayStatus as payStatus  "
						+ ",tw.DocUnreadMsgNum as docMessageCount,tw.PatUnreadMsgNum as patMessageCount,tw.Source as source "
						+ "FROM business_tuwen_order tw ");
		sqlBuilder
				.append(" left join doctor_detail_info doc on tw.DoctorId=doc.Id ");
		sqlBuilder
				.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder
				.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" left join user_case_info ca on tw.CaseId=ca.Id ");
		sqlBuilder.append(" where 1=1  ");
		sqlBuilder.append(gainstatusquery(type));
		sqlBuilder.append(querysearch);
		sqlBuilder.append(" order by tw.CreateTime desc");
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
		countSql.append("SELECT count(*) FROM business_tuwen_order tw ");
		countSql.append(" left join doctor_detail_info doc on tw.DoctorId=doc.Id ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" left join user_case_info ca on tw.CaseId=ca.Id ");
		countSql.append(" where 1=1 ");
		countSql.append(gainstatusquery(type));
		countSql.append(querysearch);
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
	public Map<String, Object> queryTuwenOrdersByExpertConditions(
			Integer docid, String search, final Integer start,
			final Integer length, Integer type) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		String querysearch = gainsearch(search);
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("SELECT tw.Id as id,ca.ContactName as contactName,ca.Sex as sex,ca.Age as age,ca.Telephone as telephone, tw.Status as askStatus"
						+ ",tw.CreateTime,tw.DocUnreadMsgNum as docMessageCount "
						+ "FROM business_tuwen_order tw ");
		sqlBuilder
				.append(" left join doctor_detail_info doc on tw.DoctorId=doc.Id ");
		sqlBuilder
				.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder
				.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" left join user_case_info ca on tw.CaseId=ca.Id ");
		sqlBuilder.append(" where 1=1 ");
		sqlBuilder.append(" and doc.Id=" + docid);
		sqlBuilder.append(gainstatusquery(type));
		sqlBuilder.append(querysearch);
		sqlBuilder.append(" order by tw.Id desc");
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
		countSql.append("SELECT count(*) FROM business_tuwen_order tw ");
		countSql.append(" left join doctor_detail_info doc on tw.DoctorId=doc.Id ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" left join user_case_info ca on tw.CaseId=ca.Id ");
		countSql.append(" where 1=1 ");
		countSql.append(" and doc.Id=" + docid);
		countSql.append(gainstatusquery(type));
		countSql.append(querysearch);
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
	public List<WenzhenBean> queryOverTimeTwOrders(Integer day) {
		// TODO Auto-generated method stub
		final String sql = "SELECT tw.Id as id  FROM business_wenzhen_tw tw WHERE NOW()>DATE_ADD(tw.StartTime,INTERVAL "
				+ day + " DAY) AND tw.AskStatus=4";
		List<WenzhenBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql);
						query.setResultTransformer(Transformers
								.aliasToBean(WenzhenBean.class));
						return query.list();
					}
				});
		return list;
	}

	public WenzhenBean queryBusinessTwById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public Boolean queryExistNoClosedTw(Integer uid) {
		// TODO Auto-generated method stub
		String hql = "from BusinessWenZhenInfo where 1=1 and userId=" + uid
				+ " and askStatus=4 ";
		List<BusinessWenZhenInfo> list = this.hibernateTemplate.find(hql);
		if (list != null && list.size() > 0)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public Boolean queryExistOrderBySevenDays(Integer docid, Integer uid) {
		// TODO Auto-generated method stub
		final String sql = "select count(*) from business_wenzhen_tw tw where DoctorId="
				+ docid
				+ " and UserId="
				+ uid
				+ " and tw.StartTime>=DATE_SUB(NOW(),INTERVAL 7 DAY) ";
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(sql).uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		if (num > 0)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<WenzhenBean> queryBusinessWenzhensByUId(Integer uid,
			final Integer pageNo, final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("SELECT tw.Id as id,ca.ContactName as contactName,ca.Sex as sex,ca.Age as age,ca.Telephone as telephone,doc.DisplayName as docName, tw.Status as askStatus "
						+ ",hos.DisplayName as hospital,dep.DisplayName as depart ,tw.CreateTime ,tw.PayStatus as payStatus,tw.PatUnreadMsgNum as patMessageCount "
						+ "FROM business_tuwen_order tw ");
		sqlBuilder
				.append(" left join doctor_detail_info doc on tw.DoctorId=doc.Id ");
		sqlBuilder
				.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder
				.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" left join user_case_info ca on tw.CaseId=ca.Id ");
		sqlBuilder
				.append(" where tw.Status in(10,20,30,40,50)");
		sqlBuilder.append(" and tw.UserId=" + uid);
		sqlBuilder.append(" order by tw.Id desc");
		// select 语句
		List<WenzhenBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult((pageNo - 1) * pageSize);
						query.setMaxResults(pageSize);
						query.setResultTransformer(Transformers
								.aliasToBean(WenzhenBean.class));
						return query.list();
					}
				});
		return list;
	}

	// 微信端订单整合一起
	@SuppressWarnings("unchecked")
	public List<WenzhenBean> queryWenzhenOrdersByConditions(Integer userId,
			final Integer pageNo, final Integer pageSize, String flag) {
		// TODO Auto-generated method stub
		final StringBuilder sb = gianCommonWenZhenInfoSql(flag, userId);
		List<WenzhenBean> list = this.getListByPageBySql(sb.toString(),
				WenzhenBean.class, pageNo, pageSize);
		return list;
	}
	
	private StringBuilder gianCommonWenZhenInfoSql(String flag,Integer userId){
		final StringBuilder sb = new StringBuilder();
		sb.append("SELECT main.Type, main.Id, main.UserId, main.DoctorId, doc.DisplayName as docName,");
		sb.append(" hos.DisplayName as hospital, dep.DisplayName as depart,");
		sb.append(" main.CaseId, main.Status as askStatus, main.CreateTime, main.PayStatus as paStatus ");
		sb.append(" FROM ( ");
		// 电话问诊
		sb.append("SELECT '2' as Type, tel.Id, tel.UserId, tel.DoctorId, tel.CaseId, tel.Status, tel.CreateTime, tel.PayStatus");
		sb.append(" FROM business_tel_order tel");
		sb.append(" LEFT JOIN business_pay_info o ON o.OrderId = tel.Id AND o.OrderType = 2");
		sb.append(" WHERE tel.Status in (")
				.append(gainTelStatus(flag)).append(") AND tel.UserId = ")
				.append(userId);

		sb.append(" UNION ");
		// 图文问诊
		sb.append("SELECT '1' as Type, tw.Id, tw.UserId, tw.DoctorId, tw.CaseId, tw.Status,tw.CreateTime, tw.PayStatus");
		sb.append(" FROM business_tuwen_order tw");
		sb.append(" WHERE tw.Status in (").append(gainTWStatus(flag))
				.append(") AND tw.UserId = ").append(userId);
		sb.append(" UNION ");
		// 远程问诊
		sb.append("SELECT '4' as Type, remote.Id,remote.UserId,remote.ExpertId as DoctorId,remote.CaseId, remote.Status, remote.CreateTime");
		sb.append(" ,remote.PayStatus");
		sb.append(" FROM business_vedio_order remote");
		sb.append(" LEFT JOIN user_weixin_relative r ON remote.OpenId = r.OpenId");
		sb.append(" WHERE remote.Status in (").append(gainRemoteStatus(flag))
				.append(") and (r.UserId = ").append(userId)
				.append(" or remote.UserId = ").append(userId).append(")");

		sb.append(" ) main ");
		sb.append(" LEFT JOIN doctor_detail_info doc ON doc.Id = main.DoctorId");
		sb.append(" LEFT JOIN hospital_detail_info hos ON hos.Id = doc.HospitalId");
		sb.append(" LEFT JOIN hospital_department_info dep ON dep.Id = doc.DepId");
		sb.append(" ORDER BY CreateTime DESC");
		return sb;
	}

	// 获取根据状态查询问诊列表Sql
	/*private StringBuilder gianCommonWenZhenInfoSql(String flag, Integer userId) {
		final StringBuilder sb = new StringBuilder();
		sb.append("SELECT main.Type, main.Id, main.UserId, main.DoctorId, doc.DisplayName as docName,");
		sb.append(" hos.DisplayName as hospital, dep.DisplayName as depart,");
		sb.append(" main.CaseId, main.Status as askStatus, main.CreateTime, main.Origin, main.PayStatus as paStatus ");
		sb.append(" FROM ( ");
		// 电话问诊
		sb.append("SELECT '2' as Type, tel.Id, tel.UserId, tel.DoctorId, tel.CaseId, tel.Status, tel.CreateTime, tel.Origin, tel.PayStatus");
		sb.append(" FROM business_wenzhen_tel tel");
		sb.append(" LEFT JOIN business_order_all o ON o.WenZhen_Id = tel.Id AND o.OrderType = 2");
		sb.append(" WHERE tel.Status in (")
				.append(gainTelStatus(flag)).append(") AND tel.UserId = ")
				.append(userId);

		sb.append(" UNION ");
		// 图文问诊
		sb.append("SELECT '1' as Type, Id, UserId, DoctorId, Cases_Id as CaseId, AskStatus as Status, StartTime as CreateTime, Origin, PayStatus");
		sb.append(" FROM business_wenzhen_tw tw");
		sb.append(" WHERE tw.AskStatus in (").append(gainTWStatus(flag))
				.append(") AND UserId = ").append(userId);

		sb.append(" UNION ");
		// 远程问诊
		sb.append("SELECT '4' as Type, remote.Id, NULL as UserId, remote.ExpertId as DoctorId, NULL as CaseId, remote.Status, CreateTime, '' as Origin");
		sb.append(" , IF(remote.Status = 0 , 4 , 1) as PayStatus");
		sb.append(" FROM remote_consultation_order remote");
		sb.append(" LEFT JOIN user_weixin_relative r ON remote.OpenId = r.OpenId");
		sb.append(" WHERE remote.Status in (").append(gainRemoteStatus(flag))
				.append(") and (r.UserId = ").append(userId)
				.append(" or remote.UserId = ").append(userId).append(")");

		sb.append(" ) main ");
		sb.append(" LEFT JOIN doctor_detail_info doc ON doc.Id = main.DoctorId");
		sb.append(" LEFT JOIN hospital_detail_info hos ON hos.Id = doc.HospitalId");
		sb.append(" LEFT JOIN hospital_department_info dep ON dep.Id = doc.DepId");
		sb.append(" ORDER BY CreateTime DESC");
		return sb;
	}*/
	

	// 获取电话问诊状态码
	private String gainTelStatus(String flag) {
		String state = "";
		if (flag.equalsIgnoreCase("processing")) {
			state = "0";
		} else if (flag.equalsIgnoreCase("complete")) {
			state = "1,3";
		} else if (flag.equalsIgnoreCase("cancel")) {
			state = "2";
		}
		return state;
	}

	// 获取图文问诊状态码
	private String gainTWStatus(String flag) {
		String state = "";
		if (flag.equalsIgnoreCase("processing")) {
			state = "1,2,3,4,8,9,10,11,12,20";
		} else if (flag.equalsIgnoreCase("complete")) {
			state = "0,7,40";
		} else if (flag.equalsIgnoreCase("cancel")) {
			state = "5,13,30,50";
		}
		return state;
	}

	// 获取远程问诊状态码
	private String gainRemoteStatus(String flag) {
		String state = "";
		if (flag.equalsIgnoreCase("processing")) {
			state = "0,1,2,3,4,5";
		} else if (flag.equalsIgnoreCase("complete")) {
			state = "10";
		} else if (flag.equalsIgnoreCase("cancel")) {
			state = "20,21";
		}
		return state;
	}


	@SuppressWarnings("unchecked")
	public List<BusinessWenZhenInfo> queryBusinessWenzhenInfos() {
		// TODO Auto-generated method stub
		String hql="from BusinessWenZhenInfo where 1=1 and id>=19620";
		return this.hibernateTemplate.find(hql);
	}


	
	
}
