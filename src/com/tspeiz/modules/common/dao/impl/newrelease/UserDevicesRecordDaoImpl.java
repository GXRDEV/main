package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IUserDevicesRecordDao;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.newrelease.UserDevicesRecord;

@Repository
public class UserDevicesRecordDaoImpl extends BaseDaoImpl<UserDevicesRecord>
		implements IUserDevicesRecordDao {

	@SuppressWarnings("unchecked")
	public UserDevicesRecord getLastLoginDevice(Integer userId, Integer userType) {
		// TODO Auto-generated method stub
		String hql = "from UserDevicesRecord where UserId = " + userId
				+ " and UserType = " + userType
				+ " order by LastLoginTime desc";
		List<UserDevicesRecord> list = this.hibernateTemplate.find(hql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public UserDevicesRecord queryUserDevice(Integer userId, Integer userType,
			String clientId) {
		// TODO Auto-generated method stub
		String hql = "from UserDevicesRecord where UserId = " + userId
				+ " and UserType = " + userType + " and ClientId = '"
				+ clientId + "' order by LastLoginTime desc";
		List<UserDevicesRecord> list = this.hibernateTemplate.find(hql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}
	
	private String gainquery(Map<String,Object> querymap){
		StringBuilder sb=new StringBuilder();
		if(querymap.containsKey("search")){
			sb.append(" and (");
			sb.append(" reg.LoginName like '%"+querymap.get("search")+"%' or ");
			sb.append(" detail.DisplayName like '%"+querymap.get("search")+"%' or ");
			sb.append(" ud.LoginVersion like '%"+querymap.get("search")+"%' " );
			sb.append(" )");
		}
		return sb.toString();
	}
	private String gainquerys(Map<String,Object> querymap){
		StringBuilder sb=new StringBuilder();
		if(querymap.containsKey("search")){
			sb.append(" and (");
			sb.append(" reg.LoginName like '%"+querymap.get("search")+"%' or ");
			sb.append(" detail.DisplayName like '%"+querymap.get("search")+"%' or ");
			sb.append(" hos.DisplayName like '%"+querymap.get("search")+"%' or ");
			sb.append(" dep.DisplayName like '%"+querymap.get("search")+"%' or ");
			sb.append(" ud.LoginVersion like '%"+querymap.get("search")+"%' " );
			sb.append(" )");
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryexlogindatas(Map<String, Object> querymap,
			final Integer start,final  Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		String query=gainquery(querymap);
		/**
		 * 专家账号，专家姓名，登陆设备，登陆时间，登陆版本号
		 */
		sqlBuilder.append("SELECT ud.Id,ud.Platform,ud.Model,ud.LastLoginTime,ud.LoginVersion,reg.LoginName as loginName,detail.DisplayName as docName,_ud.loginNum as loginNum FROM user_devices_record ud ");
		sqlBuilder.append(" INNER JOIN ( ");
		sqlBuilder.append("SELECT MAX(Id) AS Id,count(Id) as loginNum FROM `user_devices_record` WHERE usertype =2  GROUP BY UserId )");
		sqlBuilder.append(" _ud on ud.Id=_ud.Id ");
		sqlBuilder.append(" left join doctor_register_info reg on ud.UserId=reg.Id ");
		sqlBuilder.append(" left join doctor_detail_info detail on ud.UserId=detail.Id ");
		sqlBuilder.append(" where ud.UserType=2  ");
		sqlBuilder.append(query);
		sqlBuilder.append(" order by ud.LastLoginTime desc");
		List<UserDevicesRecord> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(UserDevicesRecord.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("SELECT count(ud.Id) FROM user_devices_record ud ");
		countSql.append(" INNER JOIN ( ");
		countSql.append("SELECT MAX(Id) AS Id FROM `user_devices_record` WHERE usertype =2  GROUP BY UserId )");
		countSql.append(" _ud on ud.Id=_ud.Id ");
		countSql.append(" left join doctor_register_info reg on ud.UserId=reg.Id ");
		countSql.append(" left join doctor_detail_info detail on ud.UserId=detail.Id ");
		countSql.append(" where ud.UserType=2 ");
		countSql.append(query);
		countSql.append(" order by ud.LastLoginTime desc ");
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
	public List<UserDevicesRecord> getLoginList(String docName,String hosid, String depid, String lastTimes, String startDate, String endDate) {
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append(" SELECT ud.Id,ud.Platform,ud.Model,ud.LoginVersion,reg.LoginName AS loginName,detail.DisplayName AS docName,"
				+ " hos.DisplayName AS hosName,dep.DisplayName AS depName,ud.IMEI,date_format(ud.LastLoginTime, '%Y-%m-%d %H:%i:%s') AS lastTimes");
		sqlBuilder.append(" FROM user_devices_record ud ");
		sqlBuilder.append(" LEFT JOIN doctor_register_info reg ON ud.UserId=reg.Id");
		sqlBuilder.append(" LEFT JOIN doctor_detail_info detail ON ud.UserId=detail.Id");
		sqlBuilder.append(" LEFT JOIN hospital_department_info dep ON dep.Id=detail.DepId");
		sqlBuilder.append(" LEFT JOIN hospital_detail_info hos ON hos.Id=detail.HospitalId");
		sqlBuilder.append(" WHERE ud.UserType=2");
		if(StringUtils.isNotBlank(hosid)){
			sqlBuilder.append(" and hos.Id ='"+hosid+"' ");
		}
		if(StringUtils.isNotBlank(depid)){
			sqlBuilder.append(" and dep.Id = '"+depid+"' ");
		}
		if(StringUtils.isNotBlank(startDate)){
			sqlBuilder.append(" and (DATE_FORMAT(ud.LastLoginTime,'%Y-%m-%d') >='"
					+ startDate + "' ) ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlBuilder.append(" and (DATE_FORMAT(ud.LastLoginTime,'%Y-%m-%d') <='"
					+ endDate + "' ) ");
		}
		if(StringUtils.isNotBlank(docName)){
			sqlBuilder.append(" and detail.DisplayName like '%"+docName+"%' ");
		}
		
		if(StringUtils.isNotBlank(lastTimes)){
			sqlBuilder.append(" and ud.LastLoginTime LIKE '"+lastTimes+"%' ");
		}
		sqlBuilder.append(" order by ud.LastLoginTime desc");
		List<UserDevicesRecord> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(UserDevicesRecord.class));
						return query.list();
					}
				});
		return list;
	}
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> querydoclogindatas(Map<String, Object> querymap, final Integer start, final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		String now = sdf.format(new Date());
		final StringBuilder sqlBuilder=new StringBuilder();
		String query=gainquerys(querymap);
		/**
		 * 医生账号，姓名，登录总次数
		 */
		sqlBuilder.append("SELECT ud.Id,ud.Platform,ud.Model,ud.LastLoginTime,ud.LoginVersion,reg.LoginName as loginName,"
				+ "detail.DisplayName as docName,hos.DisplayName as hosName,dep.DisplayName as depName,_ud.loginNum as loginNum,_ud.todaycounts FROM user_devices_record ud ");
		sqlBuilder.append(" INNER JOIN ( ");
		sqlBuilder.append("SELECT MAX(Id) AS Id,count(Id) as loginNum,COUNT(Id AND IF(DATE_FORMAT(LastLoginTime,'%Y-%m-%d')='"+now+"',TRUE,NULL)) AS todaycounts FROM `user_devices_record` WHERE usertype =3  GROUP BY UserId )");
		sqlBuilder.append(" _ud on ud.Id=_ud.Id ");
		sqlBuilder.append(" left join doctor_register_info reg on ud.UserId=reg.Id ");
		sqlBuilder.append(" left join doctor_detail_info detail on ud.UserId=detail.Id ");
		sqlBuilder.append(" LEFT JOIN hospital_detail_info hos ON hos.id=detail.HospitalId ");
		sqlBuilder.append(" LEFT JOIN hospital_department_info dep ON dep.Id=detail.DepId ");
		sqlBuilder.append(" where ud.UserType=3");
		sqlBuilder.append(query);
		sqlBuilder.append(" order by todaycounts desc,loginNum desc");
		List<UserDevicesRecord> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(UserDevicesRecord.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("SELECT count(ud.Id) FROM user_devices_record ud ");
		countSql.append(" INNER JOIN ( ");
		countSql.append("SELECT MAX(Id) AS Id FROM `user_devices_record` WHERE usertype =3  GROUP BY UserId )");
		countSql.append(" _ud on ud.Id=_ud.Id ");
		countSql.append(" left join doctor_register_info reg on ud.UserId=reg.Id ");
		countSql.append(" left join doctor_detail_info detail on ud.UserId=detail.Id ");
		countSql.append(" LEFT JOIN hospital_detail_info hos ON hos.id=detail.HospitalId ");
		countSql.append(" LEFT JOIN hospital_department_info dep ON dep.Id=detail.DepId ");
		countSql.append(" where ud.UserType=3 ");
		countSql.append(query);
		countSql.append(" order by ud.LastLoginTime desc ");
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
