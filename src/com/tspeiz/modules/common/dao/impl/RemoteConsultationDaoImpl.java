package com.tspeiz.modules.common.dao.impl;

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
import com.tspeiz.modules.common.dao.IRemoteConsultationDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;

@Repository
public class RemoteConsultationDaoImpl extends BaseDaoImpl<RemoteConsultation>
		implements IRemoteConsultationDao {

	@SuppressWarnings("unchecked")
	public List<RemoteConsultation> queryRemoteConsulationByExpert(
			Integer expertId) {
		// TODO Auto-generated method stub
		String hql = "from RemoteConsultation where 1=1";
		return this.hibernateTemplate.find(hql);
	}

	// 需要添加状态过滤条件
	@SuppressWarnings("unchecked")
	public RemoteConsultation queryRemoteConsulationByConditions(
			Integer expertId, String date, String startTime, Integer dur) {
		// TODO Auto-generated method stub
		final StringBuilder hqlBuilder = new StringBuilder(
				"from RemoteConsultation ");
		hqlBuilder.append(" where expertId = " + expertId);
		hqlBuilder.append(" and consultationDate = '" + date + "'");
		hqlBuilder
				.append(" and ('"
						+ startTime
						+ "' BETWEEN consultationTime AND CONVERT(varchar(5), DATEADD(n,consultationDur-1,consultationTime), 8) or ");
		hqlBuilder
				.append(" CONVERT(varchar(5), DATEADD(n,"
						+ dur
						+ "-1,'"
						+ startTime
						+ "'), 8)"
						+ " BETWEEN consultationTime AND CONVERT(varchar(5), DATEADD(n,consultationDur,consultationTime), 8) )");

		System.out.print("==========queryRemoteConsulationByConditions:"
				+ hqlBuilder.toString());
		List<RemoteConsultation> consults = this.hibernateTemplate
				.find(hqlBuilder.toString());

		if (consults != null && consults.size() > 0) {
			return consults.get(0);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> queryRemoteConsulations_doc(Integer docid,
			String search, String sortdir, Integer sortcol,
			final Integer start, final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		// select 语句
		sqlBuilder
				.append("SELECT  remote.Id,remote.ExpertId ,remote.LocalHospitalId,remote.LocalDepartId,"
						+ "remote.LocalDoctorId,remote.CreateTime,remote.RefreshTime ,remote.ConsultationTime ,"
						+ "remote.PatientName,remote.Telephone,remote.IdCard,remote.Sex ,"
						+ "remote.Amount,remote.PayMode,remote.Status,specal.DisplayName as expertName,"
						+ "remote.ConsultationDate,remote.SecondConsultationDate,remote.ThirdConsultationDate,"
						+ "remote.SecondConsultationTime,remote.ThirdConsultationTime");
		sqlBuilder
				.append(",hos.DisplayName as hosName ,department.DisplayName as depName,remote.ProgressTag ");
		sqlBuilder
				.append(",(case when remote.ThirdConsultationDate is not null and remote.ThirdConsultationDate!='' then (remote.ThirdConsultationDate+remote.ThirdConsultationTime) when remote.SecondConsultationDate is not null and remote.SecondConsultationDate!='' then (remote.SecondConsultationDate+remote.SecondConsultationTime) when remote.ConsultationDate is not null and remote.ConsultationDate!='' then (remote.ConsultationDate+remote.ConsultationTime) end) sortTime ");
		sqlBuilder.append(" FROM RemoteConsultation remote");
		sqlBuilder
				.append(" LEFT JOIN CooHospitalDetails hos on hos.Id = remote.LocalHospitalId");
		sqlBuilder
				.append(" LEFT JOIN CooHospitalDepartments department on department.Id = remote.LocalDepartId");
		sqlBuilder
				.append(" left join Users specal on specal.Id=remote.ExpertId");
		sqlBuilder.append(" WHERE 1=1");
		// sqlBuilder.append(" and (remote.LocalDepartId) in(select cooDep.Id depid from Users us left join Specialists sp on us.Id=sp.Id left join CooHospitalDetails cooHos on sp.HospitalId=cooHos.Id left join CooHospitalDepartments cooDep on cooDep.Id=sp.HospitalDepartmentId where us.UserType=3 and cooHos.Id=1 and sp.Id="+docid+")");
		// where 语句
		String _whereSql = generatesqlstr(search);
		sqlBuilder.append(" and remote.Status not in(0) ");
		sqlBuilder.append(_whereSql);
		sqlBuilder.append(" order by sortTime desc ");
		sqlBuilder.append(" and (remote.DelFlag=0 or remote.DelFlag is null) ");
		System.out.println(sqlBuilder.toString());
		List<RemoteConsultation> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(RemoteConsultation.class));
						return query.list();
					}
				});
		map.put("items", list);

		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) from RemoteConsultation remote");
		countSql.append(" LEFT JOIN  Users specal on specal.Id = remote.ExpertId");
		countSql.append(" LEFT JOIN CooHospitalDetails hos on hos.Id = remote.LocalHospitalId");
		countSql.append(" LEFT JOIN CooHospitalDepartments department on department.Id = remote.LocalDepartId");
		countSql.append(" WHERE 1=1");
		// countSql.append(" and (remote.LocalDepartId) in(select cooDep.Id depid from Users us left join Specialists sp on us.Id=sp.Id left join CooHospitalDetails cooHos on sp.HospitalId=cooHos.Id left join CooHospitalDepartments cooDep on cooDep.Id=sp.HospitalDepartmentId where us.UserType=3 and cooHos.Id=1 and sp.Id="+docid+")");
		countSql.append(_whereSql);
		countSql.append(" and remote.Status not in(0) ");
		countSql.append(" and (remote.DelFlag=0 or remote.DelFlag is null) ");

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> queryRemoteConsulations(Integer expertId,
			String search, String sortdir, Integer sortcol,
			final Integer start, final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		// select 语句
		sqlBuilder
				.append("SELECT  remote.Id,remote.ExpertId ,remote.LocalHospitalId,remote.LocalDepartId,"
						+ "remote.LocalDoctorId,remote.CreateTime,remote.RefreshTime ,remote.ConsultationTime ,"
						+ "remote.PatientName,remote.Telephone,remote.IdCard,remote.Sex ,"
						+ "remote.Amount,remote.PayMode,remote.Status,specal.DisplayName as expertName,"
						+ "remote.ConsultationDate,remote.SecondConsultationDate,remote.ThirdConsultationDate,"
						+ "remote.SecondConsultationTime,remote.ThirdConsultationTime");
		sqlBuilder
				.append(",hos.DisplayName as hosName ,department.DisplayName as depName,remote.ProgressTag");
		sqlBuilder
				.append(",(case when remote.ThirdConsultationDate is not null and remote.ThirdConsultationDate!='' then (remote.ThirdConsultationDate+remote.ThirdConsultationTime) when remote.SecondConsultationDate is not null and remote.SecondConsultationDate!='' then (remote.SecondConsultationDate+remote.SecondConsultationTime) when remote.ConsultationDate is not null and remote.ConsultationDate!='' then (remote.ConsultationDate+remote.ConsultationTime) end) sortTime ");
		sqlBuilder.append(" FROM RemoteConsultation remote");
		sqlBuilder
				.append(" LEFT JOIN  Users specal on specal.Id = remote.ExpertId");
		sqlBuilder
				.append(" LEFT JOIN CooHospitalDetails hos on hos.Id = remote.LocalHospitalId");
		sqlBuilder
				.append(" LEFT JOIN CooHospitalDepartments department on department.Id = remote.LocalDepartId");

		sqlBuilder.append(" WHERE 1=1");
		// where 语句
		String _whereSql = generatesqlstr(search);
		sqlBuilder.append(" and remote.Status not in(0) ");
		sqlBuilder.append(" and remote.ExpertId=" + expertId + " ");
		sqlBuilder.append(_whereSql);
		sqlBuilder.append(" and (remote.DelFlag=0 or remote.DelFlag is null) ");
		sqlBuilder.append(" order by sortTime desc");
		
		System.out.println(sqlBuilder.toString());
		List<RemoteConsultation> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(RemoteConsultation.class));
						return query.list();
					}
				});
		map.put("items", list);

		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) from RemoteConsultation remote");
		countSql.append(" LEFT JOIN  Users specal on specal.Id = remote.ExpertId");
		countSql.append(" LEFT JOIN CooHospitalDetails hos on hos.Id = remote.LocalHospitalId");
		countSql.append(" LEFT JOIN CooHospitalDepartments department on department.Id = remote.LocalDepartId");
		countSql.append(" WHERE 1=1");
		countSql.append(_whereSql);
		countSql.append(" and remote.Status not in(0) ");
		countSql.append(" and remote.ExpertId=" + expertId);
		sqlBuilder.append(" and (remote.DelFlag=0 or remote.DelFlag is null) ");

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

	// 需要补充
	private String generatesqlstr(String search) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(search)) {
			// 医院
			sb.append(" and ( ");
			sb.append(" hos.DisplayName like '%" + search + "%' ");
			// 科室
			sb.append(" or department.DisplayName like '%" + search + "%' ");
			// 姓名
			sb.append(" or ca.ContactName like '%" + search + "%' ");
			if (search.equalsIgnoreCase("男")) {
				sb.append("  or ca.Sex=1");
			} else if (search.equalsIgnoreCase("女")) {
				sb.append("  or ca.Sex=2");
			}
			sb.append(" or (CONCAT_WS(' ',remote.ConsultationDate,remote.ConsultationTime) ) like '%"
					+ search + "%'");
			sb.append(" )");
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public List<RemoteConsultation> queryRemoteConsultationsByConditions(
			String openid, final Integer pageNo, final Integer pageSize,
			String stas) {
		final StringBuilder hql = new StringBuilder();
		hql.append("select rc.Id,rc.ExpertId,rc.LocalHospitalId,rc.CreateTime,rc.SecondRefreshTime,rc.ThirdRefreshTime,rc.Status,hos.DisplayName as hosName,hd.DisplayName as depName,"
				+ "rc.ConsultationDate,rc.SecondConsultationDate,rc.ThirdConsultationDate,rc.ConsultationTime,rc.SecondConsultationTime,rc.ThirdConsultationTime,us.DisplayName as expertName");
		hql.append(",coohos.DisplayName as localHosName,coohos.Location as hosAddress,coohos.Lat as lat,coohos.Lng as lng,coodep.DisplayName as localDepName ");
		hql.append(",rc.PatientName as patientName ,sp.Title as expertTitle,");
		hql.append("(case when rc.ThirdConsultationDate is not null and rc.ThirdConsultationDate!='' then (rc.ThirdConsultationDate+' '+rc.ThirdConsultationTime) when rc.SecondConsultationDate is not null and rc.SecondConsultationDate!='' then (rc.SecondConsultationDate+' '+rc.SecondConsultationTime) when rc.ConsultationDate is not null and rc.ConsultationDate!='' then (rc.ConsultationDate+' '+rc.ConsultationTime) end) as conDate ");
		hql.append(",(case when rc.ThirdConsultationDate is not null and rc.ThirdConsultationDate!='' then (rc.ThirdRefreshTime) when rc.SecondConsultationDate is not null and rc.SecondConsultationDate!='' then (rc.SecondRefreshTime) when rc.ConsultationDate is not null and rc.ConsultationDate!='' then (rc.RefreshTime) end) as refreshTime ");
		hql.append(", rc.ConsultationResult as consultationResult ");
		hql.append(" from RemoteConsultation rc ");
		hql.append(" left join Specialists sp on rc.ExpertId=sp.id");
		hql.append(" left join Users us on sp.id=us.id");
		hql.append(" left join Hospitals hos on sp.HospitalId=hos.id");
		hql.append(" left join HospitalDepartments hd on sp.HospitalDepartmentId=hd.id ");
		hql.append(" left join CooHospitalDetails coohos on rc.LocalHospitalId=coohos.Id ");
		hql.append(" left join CooHospitalDepartments coodep on rc.LocalDepartId=coodep.Id ");
		hql.append(" where 1=1");
		hql.append(" and rc.OpenId='" + openid + "' ");
		if (StringUtils.isNotBlank(stas)) {
			hql.append(" and rc.Status in(" + stas + ")");
		}
		hql.append(" and (rc.DelFlag=0 or rc.DelFlag is null) ");

		hql.append(" ORDER BY rc.RefreshTime DESC");
		System.out.println("====sql====" + hql.toString());
		List<RemoteConsultation> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hql.toString());
						query.setFirstResult((pageNo - 1) * pageSize);
						query.setMaxResults(pageSize);
						query.setResultTransformer(Transformers
								.aliasToBean(RemoteConsultation.class));

						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public RemoteConsultation queryRemoteConsultationByTradeNo(String tradeNo) {
		// TODO Auto-generated method stub
		String hql = "from RemoteConsultation where 1=1 and outTradeNo='"
				+ tradeNo + "' ";
		List<RemoteConsultation> list = this.hibernateTemplate.find(hql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	// 此函数获取的医生订单信息 有 问题，获取的是所有医生的 而不是 医生所属科室订单
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryRemoteConsulationsByConditions(Integer uid,
			String searchContent, String sortdesc, final Integer start,
			final Integer length, String status, String now) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		// select 语句
		sqlBuilder
				.append("SELECT  remote.Id,remote.ExpertId ,remote.LocalHospitalId,remote.LocalDepartId,"
						+ "remote.LocalDoctorId,remote.CreateTime,remote.RefreshTime ,remote.ConsultationTime ,"
						+ "remote.PatientName,remote.Telephone,remote.IdCard,remote.Sex ,"
						+ "remote.Amount,remote.PayMode,remote.Status,specal.DisplayName as expertName,"
						+ "remote.ConsultationDate,remote.SecondConsultationDate,remote.ThirdConsultationDate,"
						+ "remote.SecondConsultationTime,remote.ThirdConsultationTime");
		sqlBuilder
				.append(",hos.DisplayName as hosName ,department.DisplayName as depName,remote.ProgressTag ");
		sqlBuilder
				.append(",(case when remote.ThirdConsultationDate is not null and remote.ThirdConsultationDate!='' then (remote.ThirdConsultationDate+remote.ThirdConsultationTime) when remote.SecondConsultationDate is not null and remote.SecondConsultationDate!='' then (remote.SecondConsultationDate+remote.SecondConsultationTime) when remote.ConsultationDate is not null and remote.ConsultationDate!='' then (remote.ConsultationDate+remote.ConsultationTime) end) sortTime ");
		sqlBuilder.append(" FROM RemoteConsultation remote");
		sqlBuilder
				.append(" LEFT JOIN CooHospitalDetails hos on hos.Id = remote.LocalHospitalId");
		sqlBuilder
				.append(" LEFT JOIN CooHospitalDepartments department on department.Id = remote.LocalDepartId");
		sqlBuilder
				.append(" left join Users specal on specal.Id=remote.ExpertId");
		sqlBuilder.append(" WHERE 1=1 ");
		if (uid != null) {
			sqlBuilder.append(" and remote.ExpertId=" + uid + " ");
		}
		if (StringUtils.isNotBlank(now)) {
			sqlBuilder
					.append(" and (case when remote.ThirdConsultationDate is not null and remote.ThirdConsultationDate!='' then (remote.ThirdConsultationDate) when remote.SecondConsultationDate is not null and remote.SecondConsultationDate!='' then (remote.SecondConsultationDate) when remote.ConsultationDate is not null and remote.ConsultationDate!='' then (remote.ConsultationDate) end)='"
							+ now + "' ");
		}// where 语句
		String _whereSql = generatesqlstr(searchContent);
		sqlBuilder.append(" and remote.Status in(" + status + ") ");
		sqlBuilder.append(_whereSql);
		sqlBuilder.append(" and (remote.DelFlag=0 or remote.DelFlag is null) ");
		sqlBuilder.append(" order by sortTime " + sortdesc);
		System.out.println(sqlBuilder.toString());
		List<RemoteConsultation> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(RemoteConsultation.class));
						return query.list();
					}
				});
		map.put("items", list);
		if (StringUtils.isNotBlank(now)) {
			Integer num = (list != null && list.size() > 0) ? list.size() : 0;
			map.put("num", num);
		} else {
			final StringBuilder countSql = new StringBuilder(
					"SELECT count(*) from RemoteConsultation remote");
			countSql.append(" LEFT JOIN  Users specal on specal.Id = remote.ExpertId");
			countSql.append(" LEFT JOIN CooHospitalDetails hos on hos.Id = remote.LocalHospitalId");
			countSql.append(" LEFT JOIN CooHospitalDepartments department on department.Id = remote.LocalDepartId");
			countSql.append(" WHERE 1=1");
			countSql.append(_whereSql);
			countSql.append(" and remote.Status in(" + status + ") ");
			if (uid != null) {
				countSql.append(" and remote.ExpertId=" + uid + " ");
			}
			countSql.append(" and (remote.DelFlag=0 or remote.DelFlag is null) ");
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
		}
		return map;
	}

	// 获取的专家订单 或 医生所属科室 订单信息 ，
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryRemoteConsulationsByConditions(
			Integer userId, Integer userType, String searchContent,
			String sortdesc, final Integer start, final Integer length,
			String status, String now) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		// select 语句
		sqlBuilder
				.append("SELECT  remote.Id,remote.ExpertId ,remote.LocalHospitalId,remote.LocalDepartId,"
						+ "remote.LocalDoctorId,remote.CreateTime,remote.RefreshTime ,remote.ConsultationTime ,"
						+ "remote.PatientName,remote.Telephone,remote.IdCard,remote.Sex ,"
						+ "remote.Amount,remote.PayMode,remote.Status,specal.DisplayName as expertName,"
						+ "remote.ConsultationDate,remote.SecondConsultationDate,remote.ThirdConsultationDate,"
						+ "remote.SecondConsultationTime,remote.ThirdConsultationTime");
		sqlBuilder
				.append(",hos.DisplayName as hosName ,department.DisplayName as depName,remote.ProgressTag ");
		sqlBuilder
				.append(",(case when remote.ThirdConsultationDate is not null and remote.ThirdConsultationDate!='' then (remote.ThirdConsultationDate+remote.ThirdConsultationTime) when remote.SecondConsultationDate is not null and remote.SecondConsultationDate!='' then (remote.SecondConsultationDate+remote.SecondConsultationTime) when remote.ConsultationDate is not null and remote.ConsultationDate!='' then (remote.ConsultationDate+remote.ConsultationTime) end) sortTime ");
		sqlBuilder.append(" FROM RemoteConsultation remote");

		// 医生条件，先 获取 医生 所在医院 及 科室信息，再按医院及可是信息差尊
		if (userType == 3) {
			sqlBuilder
					.append(" RIGHT JOIN  Specialists doctors on doctors.hospitalId = remote.localHospitalId and doctors.hospitalDepartmentId  = remote.LocalDepartId  and doctors.id =  "
							+ userId);// 涮选 用户 放在此处是为了效率
		}

		sqlBuilder
				.append(" LEFT JOIN CooHospitalDetails hos on hos.Id = remote.LocalHospitalId");
		sqlBuilder
				.append(" LEFT JOIN CooHospitalDepartments department on department.Id = remote.LocalDepartId");
		sqlBuilder
				.append(" left join Users specal on specal.Id=remote.ExpertId");

		sqlBuilder.append(" WHERE 1=1 ");

		// 专家条件
		if (userType == 2) {
			sqlBuilder.append(" and remote.ExpertId=" + userId + " ");
			sqlBuilder.append(" and specal.userType = 2");
		}

		if (StringUtils.isNotBlank(now)) {
			sqlBuilder
					.append(" and (case when remote.ThirdConsultationDate is not null and remote.ThirdConsultationDate!='' then (remote.ThirdConsultationDate) when remote.SecondConsultationDate is not null and remote.SecondConsultationDate!='' then (remote.SecondConsultationDate) when remote.ConsultationDate is not null and remote.ConsultationDate!='' then (remote.ConsultationDate) end)='"
							+ now + "' ");
		}// where 语句
		String _whereSql = generatesqlstr(searchContent);
		sqlBuilder.append(" and remote.Status in(" + status + ") ");
		sqlBuilder.append(_whereSql);
		sqlBuilder.append(" and (remote.DelFlag=0 or remote.DelFlag is null) ");
		sqlBuilder.append(" order by sortTime " + sortdesc);
		System.out
				.println("\r\n==========queryRemoteConsulationsByConditions1:"
						+ sqlBuilder.toString());
		List<RemoteConsultation> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(RemoteConsultation.class));
						return query.list();
					}
				});
		map.put("items", list);
		if (StringUtils.isNotBlank(now)) {
			Integer num = (list != null && list.size() > 0) ? list.size() : 0;
			map.put("num", num);
		} else {
			final StringBuilder countSql = new StringBuilder(
					"SELECT count(*) from RemoteConsultation remote");
			// 医生条件，先 获取 医生 所在医院 及 科室信息，再按医院及可是信息差尊
			if (userType == 3) {
				countSql.append(" RIGHT JOIN  Specialists doctors on doctors.hospitalId = remote.localHospitalId and doctors.hospitalDepartmentId  = remote.LocalDepartId  and doctors.id =  "
						+ userId);// 涮选 用户 放在此处是为了效率
			}
			countSql.append(" LEFT JOIN  Users specal on specal.Id = remote.ExpertId");
			countSql.append(" LEFT JOIN CooHospitalDetails hos on hos.Id = remote.LocalHospitalId");
			countSql.append(" LEFT JOIN CooHospitalDepartments department on department.Id = remote.LocalDepartId");
			countSql.append(" WHERE 1=1");
			countSql.append(_whereSql);
			countSql.append(" and remote.Status in(" + status + ") ");

			// 专家条件
			if (userType == 2) {
				countSql.append(" and remote.ExpertId=" + userId + " ");
				countSql.append(" and specal.userType = 2");
			}
			sqlBuilder.append(" and (remote.DelFlag=0 or remote.DelFlag is null) ");

			System.out
					.println("\r\n==========queryRemoteConsulationsByConditions2:"
							+ countSql.toString());
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
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public void updateRemoteConsulationUserStatus(Integer orderid) {
		// TODO Auto-generated method stub
		final String sql = "update RemoteConsultation set HasUser='0' where Id="
				+ orderid;
		this.hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				return session.createSQLQuery(sql).uniqueResult();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<RemoteConsultation> queryRemoteConsultationsByProgressTag(
			Integer userId, Integer userType, Integer progressTag) {
		// TODO Auto-generated method stub
		StringBuilder hqlBuilder = new StringBuilder(
				"from RemoteConsultation where 1= 1");
		if (userType == 1) {
			hqlBuilder.append(" and localDoctorId=" + userId);
		} else {
			hqlBuilder.append(" and expertId=" + userId);
		}
		hqlBuilder.append(" and progressTag =" + progressTag);

		System.out.print("=======queryRemoteConsultationsByProgressTag:"
				+ hqlBuilder.toString());
		return this.hibernateTemplate.find(hqlBuilder.toString());
	}

	@SuppressWarnings("unchecked")
	public List<RemoteConsultation> queryRemoteConsultationsByConditions(
			Integer localHosId, Integer localDepId, Integer progressTag) {
		// TODO Auto-generated method stub
		StringBuilder hqlBuilder = new StringBuilder(
				"from RemoteConsultation where 1= 1");
		hqlBuilder.append(" and localHospitalId=" + localHosId);
		hqlBuilder.append(" and LocalDepartId=" + localDepId);
		hqlBuilder.append(" and progressTag=" + progressTag);

		System.out.print("=======queryRemoteConsultationsByConditons:"
				+ hqlBuilder.toString());
		return this.hibernateTemplate.find(hqlBuilder.toString());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryRemoteConsultationsByConditions_nurse(
			Integer nurseid, final Integer start, final Integer length,
			String searchContent) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		// select 语句
		sqlBuilder
				.append("SELECT  remote.Id,remote.ExpertId ,remote.LocalHospitalId,remote.LocalDepartId,"
						+ "remote.LocalDoctorId,remote.CreateTime,remote.RefreshTime ,remote.ConsultationTime ,"
						+ "remote.PatientName,remote.Telephone,remote.IdCard,remote.Sex ,"
						+ "remote.Amount,remote.PayMode,remote.Status,specal.DisplayName as expertName,"
						+ "remote.ConsultationDate,remote.SecondConsultationDate,remote.ThirdConsultationDate,"
						+ "remote.SecondConsultationTime,remote.ThirdConsultationTime");
		sqlBuilder
				.append(",hos.DisplayName as hosName ,department.DisplayName as depName,remote.ProgressTag ");
		sqlBuilder
				.append(",(case when remote.ThirdConsultationDate is not null and remote.ThirdConsultationDate!='' then (remote.ThirdConsultationDate+remote.ThirdConsultationTime) when remote.SecondConsultationDate is not null and remote.SecondConsultationDate!='' then (remote.SecondConsultationDate+remote.SecondConsultationTime) when remote.ConsultationDate is not null and remote.ConsultationDate!='' then (remote.ConsultationDate+remote.ConsultationTime) end) sortTime ");
		sqlBuilder.append(" FROM RemoteConsultation remote");
		sqlBuilder
				.append(" LEFT JOIN CooHospitalDetails hos on hos.Id = remote.LocalHospitalId");
		sqlBuilder
				.append(" LEFT JOIN CooHospitalDepartments department on department.Id = remote.LocalDepartId");
		sqlBuilder
				.append(" left join Users specal on specal.Id=remote.ExpertId");

		sqlBuilder.append(" WHERE 1=1 ");
		sqlBuilder.append(" and remote.NurseId=" + nurseid);
		String _whereSql = generatesqlstr(searchContent);
		sqlBuilder.append(" and remote.Status not in(0) ");
		// sqlBuilder.append(_whereSql);
		sqlBuilder.append(" and (remote.DelFlag=0 or remote.DelFlag is null) ");

		sqlBuilder.append(" order by sortTime desc");
		System.out
				.println("\r\n==========queryRemoteConsulationsByConditions1:"
						+ sqlBuilder.toString());
		List<RemoteConsultation> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(RemoteConsultation.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) from RemoteConsultation remote");
		countSql.append(" LEFT JOIN  Users specal on specal.Id = remote.ExpertId");
		countSql.append(" LEFT JOIN CooHospitalDetails hos on hos.Id = remote.LocalHospitalId");
		countSql.append(" LEFT JOIN CooHospitalDepartments department on department.Id = remote.LocalDepartId");
		countSql.append(" WHERE 1=1");
		countSql.append(_whereSql);
		countSql.append(" and remote.Status not in(0) ");
		countSql.append(" and remote.NurseId=" + nurseid);
		sqlBuilder.append(" and (remote.DelFlag=0 or remote.DelFlag is null) ");

		System.out
				.println("\r\n==========queryRemoteConsulationsByConditions2:"
						+ countSql.toString());
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
	public List<RemoteConsultation> queryRemoteConsultationOrdersByConditions(
			String openid, final Integer pageNo, final Integer pageSize,
			String stas) {
		final StringBuilder hql = new StringBuilder();
		hql.append("select rc.Id,rc.ExpertId,rc.LocalHospitalId,rc.CreateTime,rc.SecondRefreshTime,rc.ThirdRefreshTime,rc.Status,hos.DisplayName as hosName,hd.DisplayName as depName,"
				+ "rc.ConsultationDate,rc.SecondConsultationDate,rc.ThirdConsultationDate,rc.ConsultationTime,rc.SecondConsultationTime,rc.ThirdConsultationTime,doc.DisplayName as expertName");
		hql.append(",coohos.DisplayName as localHosName,coohos.Location as hosAddress,coohos.Lat as lat,coohos.Lng as lng,coodep.DisplayName as localDepName ");
		hql.append(",rc.PatientName as patientName ,doc.Duty as duty,doc.Profession as profession,");
		hql.append("(case when rc.ThirdConsultationDate is not null and rc.ThirdConsultationDate!='' then CONCAT_WS(' ',rc.ThirdConsultationDate,rc.ThirdConsultationTime) when rc.SecondConsultationDate is not null and rc.SecondConsultationDate!='' then CONCAT_WS(' ',rc.SecondConsultationDate,rc.SecondConsultationTime) when rc.ConsultationDate is not null and rc.ConsultationDate!='' then CONCAT_WS(' ',rc.ConsultationDate,rc.ConsultationTime) end) as conDate ");
		hql.append(",(case when rc.ThirdConsultationDate is not null and rc.ThirdConsultationDate!='' then (rc.ThirdRefreshTime) when rc.SecondConsultationDate is not null and rc.SecondConsultationDate!='' then (rc.SecondRefreshTime) when rc.ConsultationDate is not null and rc.ConsultationDate!='' then (rc.RefreshTime) end) as refreshTime ");
		hql.append(", rc.ConsultationResult as consultationResult ");
		hql.append(" from remote_consultation_order rc ");
		hql.append(" left join doctor_detail_info doc on rc.ExpertId=doc.Id ");
		hql.append(" LEFT JOIN hospital_detail_info hos ON hos.Id=doc.HospitalId ");
		hql.append(" LEFT JOIN hospital_department_info hd ON hd.Id=doc.DepId ");
		hql.append(" left join hospital_detail_info coohos on rc.LocalHospitalId=coohos.Id ");
		hql.append(" left join hospital_department_info coodep on rc.LocalDepartId=coodep.Id ");
		hql.append(" where 1=1");
		hql.append(" and rc.OpenId='" + openid + "' ");
		if (StringUtils.isNotBlank(stas)) {
			hql.append(" and rc.Status in(" + stas + ")");
		}
		hql.append(" and (rc.DelFlag=0 or rc.DelFlag is null) ");

		hql.append(" ORDER BY rc.RefreshTime DESC");
		System.out.println("====sql====" + hql.toString());
		List<RemoteConsultation> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hql.toString());
						query.setFirstResult((pageNo - 1) * pageSize);
						query.setMaxResults(pageSize);
						query.setResultTransformer(Transformers
								.aliasToBean(RemoteConsultation.class));

						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryRemoteConsulationsByConditions_new(
			Integer userId, Integer userType, String searchContent,
			String sortdesc, final Integer start, final Integer length,
			String status, String now) {
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder("");
		// TODO Auto-generated method stub
		sqlBuilder
				.append("SELECT  remote.Id,remote.ExpertId ,remote.LocalHospitalId,remote.LocalDepartId,"
						+ "remote.LocalDoctorId,remote.CreateTime,remote.ConsultationTime ,"
						+ "ca.ContactName as patientName,ca.Age as age,ca.Telephone as telephone,ca.Sex as sex,"
						+ "remote.Status,specal.DisplayName as expertName,ca.PresentHistory as diseaseDes,ca.MainSuit as mainSuit,"
						+ "remote.ConsultationDate,"
						+ "local.DisplayName as localDocName,"
						+ "local.HeadImageUrl as localDocImage");
		sqlBuilder
				.append(",hos.DisplayName as hosName ,department.DisplayName as depName,remote.ProgressTag ");
		sqlBuilder
				.append(",(case  when remote.ConsultationDate is not null and remote.ConsultationDate!='' then CONCAT(remote.ConsultationDate,remote.ConsultationTime) end) sortTime ");
		sqlBuilder.append(" FROM business_vedio_order remote");
		sqlBuilder
				.append(" left join doctor_detail_info specal on remote.ExpertId=specal.Id ");
		sqlBuilder.append(" left join doctor_detail_info local on remote.LocalDoctorId=local.Id ");
		sqlBuilder.append(" left join doctor_register_info doc on specal.Id=doc.Id ");
		sqlBuilder
				.append(" left join hospital_detail_info hos on remote.LocalHospitalId=hos.Id ");
		sqlBuilder
				.append(" left join hospital_department_info department on remote.LocalDepartId=department.Id ");
		sqlBuilder.append(" left join user_case_info ca on ca.Id=remote.CaseId ");
		sqlBuilder.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(now)) {
			sqlBuilder
					.append(" and (case  when remote.ConsultationDate is not null and remote.ConsultationDate!='' then (remote.ConsultationDate) end)='"
							+ now + "' ");
		}
		if (userType == 2) {
			sqlBuilder.append(" and remote.ExpertId=" + userId + " ");
			sqlBuilder.append(" and doc.userType = 2");
		}
		String _whereSql = generatesqlstr(searchContent);
		sqlBuilder.append(" and remote.Status in(" + status + ") ");
		sqlBuilder.append(_whereSql);
		sqlBuilder.append(" and (remote.DelFlag=0 or remote.DelFlag IS NULL) ");
		sqlBuilder.append(" order by sortTime " + sortdesc);
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
		if (StringUtils.isNotBlank(now)) {
			Integer num = (list != null && list.size() > 0) ? list.size() : 0;
			map.put("num", num);
		} else {
			final StringBuilder countSql = new StringBuilder(
					"SELECT count(*) from business_vedio_order remote");
			countSql.append(" left join doctor_detail_info doc on remote.ExpertId=doc.Id ");
			countSql.append(" LEFT JOIN  doctor_register_info specal on specal.Id = doc.Id ");
			countSql.append(" left join hospital_detail_info hos on remote.LocalHospitalId=hos.Id ");
			countSql.append(" left join hospital_department_info department on remote.LocalDepartId=department.Id ");
			countSql.append(" left join user_case_info ca on ca.Id=remote.CaseId ");
			countSql.append(" WHERE 1=1");
			if (userType == 2) {
				countSql.append(" and remote.ExpertId=" + userId + " ");
				countSql.append(" and specal.userType = 2");
			}
			countSql.append(_whereSql);
			countSql.append(" and remote.Status in(" + status + ") ");
			countSql.append(" and (remote.DelFlag=0 or remote.DelFlag IS NULL) ");
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
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryRemoteConsulationsByConditions_docnew(
			Integer userId, Integer userType, String searchContent,
			String sortdesc, final Integer start, final Integer length,
			String status, String now) {
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sb = new StringBuilder(
				"SELECT  DISTINCT remote.Id,remote.ExpertId ,specal.HeadImageUrl as expertHeadImage,remote.LocalHospitalId,remote.LocalDepartId,remote.LocalDoctorId,remote.CreateTime,remote.ConsultationTime ,");
		sb.append(" ca.ContactName as patientName,ca.Telephone as telephone,ca.Age as age,ca.Sex as sex,remote.Status,specal.DisplayName as expertName,");
		sb.append("remote.ConsultationDate,ca.CaseName as caseName,ca.PresentHistory as diseaseDes,ca.MainSuit as mainSuit");
		sb.append(",hos.DisplayName as hosName ,department.DisplayName as depName,remote.ProgressTag,remote.PayStatus as payStatus ");
		sb.append(",(case when remote.ConsultationDate is not null and remote.ConsultationDate!='' then CONCAT(remote.ConsultationDate,remote.ConsultationTime) end) sortTime ");
		sb.append(" FROM business_vedio_order remote");
		sb.append(" left join doctor_detail_info doc ON doc.DepId=remote.LocalDepartId ");
		sb.append(" left join doctor_detail_info specal on specal.Id=remote.ExpertId ");
		sb.append(" left join hospital_detail_info hos on specal.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info department on specal.DepId=department.Id ");
		sb.append(" left join user_case_info ca on ca.Id=remote.CaseId ");
		sb.append(" where 1=1 and remote.LocalDoctorId=" + userId);
		if (StringUtils.isNotBlank(now)) {
			sb.append(" and (case when remote.ConsultationDate is not null and remote.ConsultationDate!='' then (remote.ConsultationDate)  end)='"
					+ now + "' ");
		}
		String _whereSql = generatesqlstr(searchContent);
		if(status.equalsIgnoreCase("10")){
			sb.append(" and (remote.Status in(" + status + ") )");
		}else if (status.equalsIgnoreCase("10,12,20,21")){
			sb.append(" and (remote.Status in(" + status + ") )");
		}else{
			sb.append(" and ((remote.Status in(" + status + ") and remote.PayStatus=1) or (remote.Status in("+status+") and remote.HelpOrder=1 and remote.PayStatus=4)) ");
		}
		sb.append(_whereSql);
		sb.append(" and (remote.DelFlag=0  or remote.DelFlag IS NULL)");
		sb.append(" order by sortTime " + sortdesc);
		System.out.println("=="+sb.toString());
		List<BusinessVedioOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessVedioOrder.class));
						return query.list();
					}
				});
		map.put("items", list);
		if (StringUtils.isNotBlank(now)) {
			Integer num = (list != null && list.size() > 0) ? list.size() : 0;
			map.put("num", num);
		} else {
			final StringBuilder countSql = new StringBuilder(
					"SELECT count(DISTINCT remote.Id) from business_vedio_order remote");
			countSql.append(" left join doctor_detail_info doc ON doc.DepId=remote.LocalDepartId ");
			countSql.append(" left join doctor_detail_info specal on specal.Id=remote.ExpertId ");
			countSql.append(" left join hospital_detail_info hos on specal.HospitalId=hos.Id ");
			countSql.append(" left join hospital_department_info department on specal.DepId=department.Id ");
			countSql.append(" left join user_case_info ca on ca.Id=remote.CaseId ");
			countSql.append(" where 1=1 and remote.LocalDoctorId=" + userId);
			countSql.append(_whereSql);
			if(status.equalsIgnoreCase("10")){
				countSql.append(" and ( remote.Status in(" + status + ") and remote.PayStatus=1)");
			}else if (status.equalsIgnoreCase("10,12,20,21")){
				countSql.append(" and (remote.Status in(" + status + ") )");
			}else{
				countSql.append(" and (( remote.Status in(" + status + ") and remote.PayStatus=1) or (remote.Status in("+status+") and remote.HelpOrder=1 and remote.PayStatus=4))");
			}
			countSql.append(" and (remote.DelFlag=0 or remote.DelFlag IS NULL)");
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
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryRemoteConsulationsByConditions_hos(
			Integer hosid, String searchContent, String sortdesc,
			final Integer start, final Integer length, String status, String now) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sb = new StringBuilder(
				"SELECT  remote.Id,remote.ExpertId ,remote.LocalHospitalId,remote.LocalDepartId,locdep.DisplayName as localDepName, remote.LocalDoctorId,remote.CreateTime,remote.ConsultationTime ,");
		sb.append(" ca.ContactName as patientName,ca.Age as age,ca.Telephone as telephone,ca.Sex as sex ,remote.Status,specal.DisplayName as expertName,");
		sb.append("remote.ConsultationDate");
		sb.append(",remote.PayStatus as payStatus");
		sb.append(",hos.DisplayName as hosName ,department.DisplayName as depName,remote.ProgressTag ");
		sb.append(",(case when remote.ConsultationDate is not null and remote.ConsultationDate!='' then CONCAT(remote.ConsultationDate,remote.ConsultationTime)  end) sortTime ");
		sb.append(" FROM business_vedio_order remote");
		sb.append(" left join doctor_detail_info specal on specal.Id=remote.ExpertId ");
		sb.append(" left join hospital_detail_info hos on specal.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info department on specal.DepId=department.Id ");
		sb.append(" left join hospital_department_info locdep on remote.LocalDepartId=locdep.Id ");
		sb.append(" left join user_case_info ca on remote.CaseId=ca.Id ");
		sb.append(" where 1=1 and remote.LocalHospitalId=" + hosid);
		
		if (StringUtils.isNotBlank(now)) {
			sb.append(" and (case when remote.ConsultationDate is not null and remote.ConsultationDate!='' then (remote.ConsultationDate) end)='"
					+ now + "' ");
		}
		String _whereSql = generatesqlstr_new(searchContent);
		sb.append(" and remote.Status in(" + status + ") ");
		sb.append(_whereSql);
		sb.append(" and (remote.DelFlag=0 or remote.DelFlag is null)");
		sb.append(" order by sortTime " + sortdesc);
		List<BusinessVedioOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessVedioOrder.class));
						return query.list();
					}
				});
		map.put("items", list);
		if (StringUtils.isNotBlank(now)) {
			Integer num = (list != null && list.size() > 0) ? list.size() : 0;
			map.put("num", num);
		} else {
			final StringBuilder countSql = new StringBuilder(
					"SELECT count(*) from business_vedio_order remote");
			countSql.append(" left join doctor_detail_info specal on specal.Id=remote.ExpertId ");
			countSql.append(" left join hospital_detail_info hos on specal.HospitalId=hos.Id ");
			countSql.append(" left join hospital_department_info department on specal.DepId=department.Id ");
			countSql.append(" left join hospital_department_info locdep on remote.LocalDepartId=locdep.Id ");
			countSql.append(" left join user_case_info ca on ca.Id=remote.CaseId ");
			countSql.append(" where 1=1 and remote.LocalHospitalId=" + hosid);
			
			countSql.append(_whereSql);
			countSql.append(" and remote.Status in(" + status + ") ");
			countSql.append(" and (remote.DelFlag=0 or remote.DelFlag is null)");

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
		}
		return map;
	}

	private String generatesqlstr_new(String search) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(search)) {
			// 医院
			sb.append(" and ( ");
			sb.append(" hos.DisplayName like '%" + search + "%' ");
			// 科室
			sb.append(" or department.DisplayName like '%" + search + "%' ");
			// 姓名
			sb.append(" or ca.ContactName like '%" + search + "%' ");
			if (search.equalsIgnoreCase("男")) {
				sb.append("  or ca.Sex=1");
			} else if (search.equalsIgnoreCase("女")) {
				sb.append("  or ca.Sex=2");
			}
			sb.append(" or locdep.DisplayName like '%" + search + "%' ");
			sb.append(" or (case  when remote.Status=3 or remote.Status=4 then CONCAT_WS(' ',remote.SecondConsultationDate,remote.SecondConsultationTime) "
					+ "when remote.Status=5 then CONCAT_WS(' ',remote.ThirdConsultationDate,remote.ThirdConsultationTime) "
					+ "when remote.Status=10 then ((case when remote.ThirdConsultationDate is not null then CONCAT_WS(' ',remote.ThirdConsultationDate,remote.ThirdConsultationTime) "
					+ "when remote.SecondConsultationDate is not null then CONCAT_WS(' ',remote.SecondConsultationDate,remote.SecondConsultationTime) else CONCAT_WS(' ',remote.ConsultationDate,remote.ConsultationTime) end  ))"
					+ "else CONCAT_WS(' ',remote.ConsultationDate,remote.ConsultationTime) end) like '%"
					+ search + "%'");
			sb.append(" )");
		}
		return sb.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RemoteConsultation queryRemoteConsulationsById_detail(Integer id) {
		final StringBuilder sqlBuilder = new StringBuilder("");
		// TODO Auto-generated method stub
		sqlBuilder
				.append("SELECT  remote.Id,remote.ExpertId ,remote.LocalHospitalId,remote.LocalDepartId,"
						+ "remote.LocalDoctorId,remote.CreateTime,remote.RefreshTime ,remote.ConsultationTime,"
						+ "remote.PatientName,remote.Telephone,remote.IdCard,remote.Sex ,"
						+ "remote.Amount,remote.PayMode,remote.Status,remote.ProgressTag,remote.NormalImages as normalImages,"
						+ "remote.ConsultationDate,remote.SecondConsultationDate,remote.ThirdConsultationDate,remote.OutTradeNo as outTradeNo,"
						+ "remote.SecondConsultationTime,remote.ThirdConsultationTime,remote.PayStatus as payStatus,remote.Amount as amount");
		sqlBuilder
				.append(",hos.DisplayName as hosName ,department.DisplayName as depName,specal.DisplayName as expertName,remote.Source as source,remote.ExLevel as exLevel,remote.RemoteSub as remoteSub");
		sqlBuilder
				.append(",localHos.DisplayName as localHosName ,localDep.DisplayName as localDepName,doc.DisplayName as localDocName,localHos.DockingMode as dockingMode ");

		sqlBuilder.append(" FROM remote_consultation_order remote");
		sqlBuilder
				.append(" left join doctor_detail_info specal on remote.ExpertId=specal.Id ");
		sqlBuilder
				.append(" left join hospital_detail_info hos on specal.HospitalId=hos.Id ");
		sqlBuilder
				.append(" left join hospital_department_info department on specal.DepId=department.Id");

		sqlBuilder
				.append(" left join hospital_detail_info localHos on remote.LocalHospitalId=localHos.Id ");
		sqlBuilder
				.append(" left join hospital_department_info localDep on remote.LocalDepartId=localDep.Id");
		sqlBuilder
				.append(" left join doctor_detail_info doc on remote.LocalDoctorId=doc.Id ");

		sqlBuilder.append(" WHERE remote.Id = " + id);
		System.out.println("\r\n==========queryRemoteConsulationsById_detail:"
				+ sqlBuilder.toString());
		List<RemoteConsultation> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());

						query.setResultTransformer(Transformers
								.aliasToBean(RemoteConsultation.class));
						return query.list();
					}
				});

		return list.size() > 0 ? list.get(0) : null;
	}
	
	private String gainstatusquery(Integer ostatus) {
		String str = "";
		switch (ostatus) {
		case 1:
			str = " and rc.PayStatus=4 ";
			break;
		case 2:
			str = " and rc.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();
			break;
		case 3:
			str = " and rc.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey();
			break;
		case 4:
			str = " and rc.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey();
			break;
		case 5:
			str = " and rc.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey();
			break;
		case 6:
			str = " and rc.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();
			break;
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryhelporders(String search,
			final Integer start, final Integer length, Integer type) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder hql = new StringBuilder();
		hql.append("select DISTINCT rc.Id,rc.ExpertId,rc.LocalHospitalId,rc.CreateTime,rc.Status,hos.DisplayName as hosName,hd.DisplayName as depName,"
				+ "rc.ConsultationDate,rc.ConsultationTime,doc.DisplayName as expertName");
		hql.append(",coohos.DisplayName as localHosName,coodep.DisplayName as localDepName ");
		hql.append(",ca.ContactName as patientName ,ca.Sex,ca.Telephone as telephone,ldoc.DisplayName as localDocName,rc.PayStatus as payStatus,rc.ExLevel as exLevel,");
		hql.append("rc.ConsultationResult as consultationResult ,rc.ProgressTag as progressTag ");
		hql.append(" from business_vedio_order rc ");
		hql.append(" left join doctor_detail_info doc on rc.ExpertId=doc.Id ");
		hql.append(" LEFT JOIN hospital_detail_info hos ON hos.Id=doc.HospitalId ");
		hql.append(" left join doctor_detail_info ldoc on rc.LocalDoctorId=ldoc.Id ");
		hql.append(" LEFT JOIN hospital_department_info hd ON hd.Id=doc.DepId ");
		hql.append(" left join hospital_detail_info coohos on rc.LocalHospitalId=coohos.Id ");
		hql.append(" left join hospital_department_info coodep on rc.LocalDepartId=coodep.Id ");
		hql.append(" left join user_case_info ca on rc.CaseId=ca.Id ");
		hql.append(" where 1=1 ");
		hql.append(gainstatusquery(type));
		hql.append(" and (rc.DelFlag=0 or rc.DelFlag IS NULL) ");
		hql.append("  order by rc.CreateTime desc ");
		System.out.println("===sql=="+hql.toString());
		List<BusinessVedioOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hql.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessVedioOrder.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder counthql = new StringBuilder();
		counthql.append("select count(DISTINCT rc.Id) ");
		counthql.append(" from business_vedio_order rc ");
		counthql.append(" left join doctor_detail_info doc on rc.ExpertId=doc.Id ");
		counthql.append(" left join doctor_detail_info ldoc on rc.LocalDoctorId=ldoc.Id ");
		counthql.append(" LEFT JOIN hospital_detail_info hos ON hos.Id=doc.HospitalId ");
		counthql.append(" LEFT JOIN hospital_department_info hd ON hd.Id=doc.DepId ");
		counthql.append(" left join hospital_detail_info coohos on rc.LocalHospitalId=coohos.Id ");
		counthql.append(" left join hospital_department_info coodep on rc.LocalDepartId=coodep.Id ");
		counthql.append(" left join user_case_info ca on rc.CaseId=ca.Id ");
		counthql.append(" where 1=1 ");
		counthql.append(gainstatusquery(type));
		counthql.append(" and (rc.DelFlag=0 or rc.DelFlag IS NULL) ");
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(counthql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		map.put("num", num);
		return map;
	}

	@SuppressWarnings("unchecked")
	public Integer queryOrdersNumByConditions(Integer hosid, Integer depid,
			String begin, String end) {
		// TODO Auto-generated method stub
		final StringBuilder counthql = new StringBuilder();
		counthql.append("select count(*) ");
		counthql.append(" from business_vedio_order rc ");
		counthql.append(" where 1=1 and rc.LocalHospitalId=" + hosid);
		if (StringUtils.isNotBlank(begin) && StringUtils.isNotBlank(end))
			counthql.append(" and (DATE_FORMAT(rc.CreateTime,'%Y-%m-%d')>='" + begin
					+ "' and DATE_FORMAT(rc.CreateTime,'%Y-%m-%d')<='" + end + "' ) ");

		if (depid != null) {
			counthql.append(" and rc.LocalDepartId=" + depid);
		}
		counthql.append(" and rc.Status not in(0) ");
		counthql.append(" and (rc.DelFlag=0 or rc.DelFlag is null) ");
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(counthql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		return num;
	}

	@SuppressWarnings("unchecked")
	public Integer queryUnCompletedNum(Integer hosid) {
		// TODO Auto-generated method stub
		final StringBuilder counthql = new StringBuilder();
		counthql.append("select count(*) ");
		counthql.append(" from business_vedio_order rc ");
		counthql.append(" where 1=1 and rc.LocalHospitalId=" + hosid);
		counthql.append(" and rc.Status not in(30,40,50) ");
		counthql.append(" and (rc.DelFlag=0 or rc.DelFlag is null) ");
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(counthql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());

		return num;
	}

	@SuppressWarnings("unchecked")
	public Integer queryTotalOrders(Integer hosid) {
		// TODO Auto-generated method stub
		final StringBuilder counthql = new StringBuilder();
		counthql.append("select count(*) ");
		counthql.append(" from business_vedio_order rc ");
		counthql.append(" where 1=1 and rc.LocalHospitalId=" + hosid);
		counthql.append(" and rc.Status not in(0) ");
		counthql.append(" and (rc.DelFlag=0 or rc.DelFlag is null) ");
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(counthql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());

		return num;
	}

	@SuppressWarnings("unchecked")
	public Integer queryOrderNumbyType(Integer hosid, Integer type) {
		// TODO Auto-generated method stub
		final StringBuilder counthql = new StringBuilder();
		counthql.append("select count(*) ");
		counthql.append(" from business_vedio_order rc ");
		counthql.append(" where 1=1 and rc.LocalHospitalId=" + hosid);
		counthql.append(" and rc.Status not in(0) and rc.Source=" + type);
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(counthql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());

		return num;
	}

	public List<RemoteConsultation> queryRemoteConsulations() {
		// TODO Auto-generated method stub
		String hql="from RemoteConsultation where 1=1 and (deleFlag=0 or deleFlag is null) ";
		return this.hibernateTemplate.find(hql);
	}
	
	
}
