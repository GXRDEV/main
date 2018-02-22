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

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IDoctorRegisterInfoDao;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;

@Repository
public class DoctorRegisterInfoDaoImpl extends BaseDaoImpl<DoctorRegisterInfo>
		implements IDoctorRegisterInfoDao {


	@SuppressWarnings("unchecked")
	public List<DoctorRegisterInfo> queryDoctorRegs() {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"select re.Id as id  from doctor_register_info re left join doctor_detail_info det on re.Id=det.Id");
		sb.append(" where (re.UserType=2 or re.UserType=3) and det.Id >=17750 ");
		List<DoctorRegisterInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorRegisterInfo.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public DoctorRegisterInfo queryDoctorRegisterInfo(final String username,
			final String password) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"select re.Id as id,re.LoginName as loginName,re.Salt as salt,"
						+ "re.MobileNumber as mobileNumber,re.RegisterTime as registerTime,re.UserType as userType,re.Token as token,re.WebToken as webToken,det.DisplayName as displayName from doctor_register_info re left join doctor_detail_info det on re.Id=det.Id");
		sb.append(" where loginName=? and password=? and re.Status=1 ");
		List<DoctorRegisterInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setString(0, username);
						query.setString(1, password);
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorRegisterInfo.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public DoctorRegisterInfo queryDoctorRegisterInfoByTel(String tel) {
		// TODO Auto-generated method stub
		String hql = " from DoctorRegisterInfo where 1=1 and loginName=? or mobileNumber=? ";
		List<DoctorRegisterInfo> list = this.hibernateTemplate.find(hql,tel,tel);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<MobileSpecial> queryMobileSpecialsByConditions_newnurse(
			Integer depid, String sdate, String timetype, final Integer pageNo,
			final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"SELECT distinct doc.Id as specialId,doc.DisplayName as specialName,"
						+ " doc.HospitalId as hosId,hos.DisplayName as hosName,doc.DepId as depId,dep.DisplayName as depName ,"
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Duty as duty,doc.Profession as profession,doc.Speciality as specialty"
						+ " FROM doctor_detail_info doc ");
		sb.append(" left join doctor_register_info reg on reg.Id=doc.Id ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" inner join expert_consultation_schedule ecs on ecs.ExpertId=doc.Id ");
		sb.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		sb.append(" WHERE DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId="
				+ depid + " AND DepartmentType=1) AND DepartmentType=2)");
		sb.append(" and doc.Recommend=1 and doc.Status=1 and ds.ServiceId=4 and ds.PackageId=3 and ds.IsOpen=1 ");
		sb.append(" and ecs.ScheduleDate='" + sdate + "'");
		sb.append(" and ecs.HasAppoint='0' ");
		sb.append(" and reg.Status=1 ");
		if (StringUtils.isNotBlank(timetype) && !timetype.equalsIgnoreCase("0")) {
			if (timetype.equalsIgnoreCase("1")) {
				// 上午
				sb.append(" and (ecs.StartTime>='08:00' and ecs.StartTime<='12:00') ");
			} else if (timetype.equalsIgnoreCase("2")) {
				// 下午
				sb.append(" and (ecs.StartTime>='13:00' and ecs.StartTime<='20:00') ");
			}
		}
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setFirstResult((pageNo - 1) * pageSize);
						query.setMaxResults(pageSize);
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryRemoteConsultationsByConditions_newnurse(
			Integer nurseid, final Integer start, final Integer length,
			String searchContent) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		// select 语句
		sqlBuilder
				.append("SELECT  remote.Id,remote.ExpertId ,remote.LocalHospitalId,remote.LocalDepartId,"
						+ "remote.LocalDoctorId,remote.CreateTime ,remote.ConsultationTime ,"
						+ "ca.ContactName as patientName,ca.Telephone,ca.IdCard,ca.Sex ,"
						+ "remote.Status,specal.DisplayName as expertName,"
						+ "remote.ConsultationDate");
		sqlBuilder
				.append(",hos.DisplayName as hosName ,department.DisplayName as depName,remote.ProgressTag ");
		sqlBuilder
				.append(",(case  when remote.ConsultationDate is not null and remote.ConsultationDate!='' then CONCAT_WS(' ',remote.ConsultationDate,remote.ConsultationTime) end) sortTime ");
		sqlBuilder.append(" FROM business_vedio_order remote");
		sqlBuilder
				.append(" left join doctor_detail_info specal on specal.Id=remote.ExpertId ");
		sqlBuilder
				.append(" LEFT JOIN hospital_detail_info hos on hos.Id = remote.LocalHospitalId");
		sqlBuilder
				.append(" LEFT JOIN hospital_department_info department on department.Id = remote.LocalDepartId");
		sqlBuilder.append(" left join user_case_info ca on ca.Id=remote.CaseId ");
		sqlBuilder.append(" WHERE 1=1 ");
		sqlBuilder.append(" and remote.NurseId=" + nurseid);
		// String _whereSql = generatesqlstr(searchContent);
		sqlBuilder.append(" and remote.Status not in(0) ");
		// sqlBuilder.append(_whereSql);
		sqlBuilder.append(" order by sortTime desc");
		List<BusinessVedioOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessVedioOrder.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) from business_vedio_order remote");
		countSql.append(" LEFT JOIN  doctor_detail_info specal on specal.Id = remote.ExpertId");
		countSql.append(" LEFT JOIN hospital_detail_info hos on hos.Id = remote.LocalHospitalId");
		countSql.append(" LEFT JOIN hospital_department_info department on department.Id = remote.LocalDepartId");
		countSql.append(" WHERE 1=1");
		// countSql.append(_whereSql);
		countSql.append(" and remote.Status not in(0) ");
		countSql.append(" and remote.NurseId=" + nurseid);
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
	public DoctorRegisterInfo queryDoctorRegisterInfoByTel(String tel,
			Integer utype) {
		// TODO Auto-generated method stub
		/*String hql = " from DoctorRegisterInfo where 1=1 and (loginName='"
				+ tel + "' or mobileNumber='" + tel + "') and userType="
				+ utype;*/
		String hql="from DoctorRegisterInfo where 1=1 and (loginName =? or mobileNumber=?) and userType=?";
		List<DoctorRegisterInfo> list = this.hibernateTemplate.find(hql,tel,tel,utype);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public DoctorRegisterInfo queryDoctorRegisterInfoByLoginName(
			String loginName) {
		// TODO Auto-generated method stub
		String hql = " from DoctorRegisterInfo where 1=1 and loginName=? ";
		List<DoctorRegisterInfo> list = this.hibernateTemplate.find(hql,loginName);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DoctorRegisterInfo queryDoctorRegisterInfoByHosAndType(
			Integer hosid, Integer type) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"select re.Id as id,re.LoginName as loginName,re.Salt as salt,"
						+ "re.MobileNumber as mobileNumber,re.RegisterTime as registerTime,re.UserType as userType,re.Token as token,re.WebToken as webToken from doctor_register_info re left join doctor_detail_info det on re.Id=det.Id");
		sb.append(" where 1=1 and det.HospitalId=" + hosid);
		sb.append(" and re.UserType=" + type+" and re.Status=1 ");
		List<DoctorRegisterInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorRegisterInfo.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryNeedAuditDocs(Integer status,String search,final Integer start,
			final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		// select 语句
		sqlBuilder.append("select reg.Id as id,doc.DisplayName as displayName,reg.MobileNumber as mobileNumber,reg.RegisterTime,");
		sqlBuilder.append("(case when doc.HospitalId is null then doc.RegHospitalName else hos.DisplayName end) as hosName,");
		sqlBuilder.append("(case when doc.DepId is null then doc.RegDepartmentName else dep.DisplayName end) as depName ");
		sqlBuilder.append(",reg.Status as status ");
		sqlBuilder.append(" from doctor_register_info reg ");
		sqlBuilder.append(" left join doctor_detail_info doc on reg.Id=doc.Id");
		sqlBuilder.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id");
		sqlBuilder.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" where reg.Status ="+status);
		sqlBuilder.append(gainsearch(search));
		sqlBuilder.append(" order by reg.RegisterTime desc ");
		List<DoctorRegisterInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorRegisterInfo.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder();
		countSql.append("select count(*) from doctor_register_info reg ");
		countSql.append(" left join doctor_detail_info doc on reg.Id=doc.Id");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" where reg.Status ="+status);
		countSql.append(gainsearch(search));
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
	
	private String gainsearch(String search){
		StringBuilder sb=new StringBuilder();
		sb.append(" and ( ");
		sb.append(" doc.DisplayName like '%"+search+"%' or ");
		sb.append(" reg.MobileNumber like '%"+search+"%' or ");
		sb.append(" hos.DisplayName like '%"+search+"%' or ");
		sb.append(" dep.DisplayName like '%"+search+"%' ");
		sb.append(" )");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> querylocaldocs(String search,final Integer start,
			final Integer length,Integer status) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		String query=gainsearch(search);
		// select 语句
		sqlBuilder.append("select doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty ,reg.RegisterTime as registerTime,reg.Status,doc.AreaOptimal,"
						+ " doc.HospitalId as hosId,hos.DisplayName as hosName,doc.DepId as depId,dep.DisplayName as depName ,reg.MobileNumber as telphone,reg.IsTest as isTest from doctor_register_info reg ");
		sqlBuilder.append(" left join doctor_detail_info doc on reg.Id=doc.Id ");
		sqlBuilder.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sqlBuilder.append(" where 1=1 and hos.IsCooHospital=1 and reg.Status="+status+" and reg.UserType=3 ");//合作医院
		sqlBuilder.append(query);
		sqlBuilder.append(" order by reg.RegisterTime desc ");	
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) from doctor_register_info reg ");
		countSql.append(" left join doctor_detail_info doc on reg.Id=doc.Id ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		countSql.append(" where 1=1 and hos.IsCooHospital=1  and reg.Status="+status+" and reg.UserType=3");//合作医院
		countSql.append(query);
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
