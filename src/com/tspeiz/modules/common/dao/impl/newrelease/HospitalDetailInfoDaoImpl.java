package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.bean.dto.GroupInfoDto;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IHospitalDetailInfoDao;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;

@Repository
public class HospitalDetailInfoDaoImpl extends BaseDaoImpl<HospitalDetailInfo>
		implements IHospitalDetailInfoDao {

	@SuppressWarnings("unchecked")
	public List<HospitalDetailInfo> queryCoohospitalInfos() {
		// TODO Auto-generated method stub
		final StringBuilder hqlBuilder = new StringBuilder(
				"select hos.Id,hos.DisplayName as displayName,hos.ShortName as shortName,hos.HospitalLevel as hospitalLevel"
						+ ",hos.DistCode as distCode, hos.Location as location,hos.BigPicture as bigPicture,hos.Remark as remark,hos.IsCooHospital as isCooHospital"
						+ ",hos.Keywords as keywords,hos.Status as status,hos.Lat as lat,hos.Lng as lng,hos.AuditStatus as auditStatus,"
						+ "hos.HosProperty as hosProperty,hos.HosType as hosType,hos.HosWebSite as hosWebSite ,hos.DailyOutpatientNumber as dailyOutpatientNumber,"
						+ "hos.DailyAppointNumber as dailyAppointNumber,hos.ExternalAppointmentApproach as externalAppointmentApproach,"
						+ "hos.HosProfile as hosProfile,hos.CharacteristicClinic as characteristicClinic,hos.ContactorName as contactorName,hos.ContactorTelphone as contactorTelphone,"
						+ "hos.ContactorDuty as contactorDuty,hos.ContactorEmail as contactorEmail,hos.AuthorizeFile as authorizeFile,"
						+ "hos.DockingMode as dockingMode ,hos.AccountCreate as accountCreate,dict.DistName as city");
		hqlBuilder.append(" from hospital_detail_info hos ");
		hqlBuilder
				.append(" left join dict_district_info dict on hos.DistCode=dict.DistCode ");
		hqlBuilder
				.append(" where hos.IsCooHospital=1 and hos.AuditStatus=1 and hos.Status=1 ");
		hqlBuilder.append(" order by hos.Rank desc");
		List<HospitalDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDetailInfo.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HospitalDetailInfo queryHospitalDetailInfoById(Integer id) {
		// TODO Auto-generated method stub
		final StringBuilder hqlBuilder = new StringBuilder(
				"select hos.Id,hos.DisplayName as displayName,hos.ShortName as shortName,hos.HospitalLevel as hospitalLevel,dic.DisplayName as hosLevel"
						+ ",hos.DistCode as distCode, hos.Location as location,hos.BigPicture as bigPicture,hos.Remark as remark,hos.IsCooHospital as isCooHospital"
						+ ",hos.Keywords as keywords,hos.Status as status,hos.Lat as lat,hos.Lng as lng,hos.AuditStatus as auditStatus,"
						+ "hos.HosProperty as hosProperty,hos.HosType as hosType,hos.HosWebSite as hosWebSite ,hos.DailyOutpatientNumber as dailyOutpatientNumber,"
						+ "hos.DailyAppointNumber as dailyAppointNumber,hos.ExternalAppointmentApproach as externalAppointmentApproach,"
						+ "hos.HosProfile as hosProfile,hos.CharacteristicClinic as characteristicClinic,hos.ContactorName as contactorName,hos.ContactorTelphone as contactorTelphone,"
						+ "hos.ContactorDuty as contactorDuty,hos.ContactorEmail as contactorEmail,hos.AuthorizeFile as authorizeFile,"
						+ "hos.DockingMode as dockingMode ,hos.AccountCreate as accountCreate,dict.DistName as city,hos.HospitalLogo as hospitalLogo,hos.HospitalIntroduction as hospitalIntroduction ");
		hqlBuilder.append(" from hospital_detail_info hos ");
		hqlBuilder
		.append(" left join dictionary dic on hos.HospitalLevel=dic.Id");
		hqlBuilder
				.append(" left join dict_district_info dict on hos.DistCode=dict.DistCode ");
		hqlBuilder.append(" where 1=1 and hos.Id=" + id);
		List<HospitalDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDetailInfo.class));
						return query.list();
					}
				});
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public HospitalDetailInfo queryCooHospitalInfoByCity(String city) {
		// TODO Auto-generated method stub
		final StringBuilder hqlBuilder = new StringBuilder(
				"select hos.Id,hos.DisplayName as displayName,hos.ShortName as shortName,hos.HospitalLevel as hospitalLevel"
						+ ",hos.DistCode as distCode, hos.Location as location,hos.BigPicture as bigPicture,hos.Remark as remark,hos.IsCooHospital as isCooHospital"
						+ ",hos.Keywords as keywords,hos.Status as status,hos.Lat as lat,hos.Lng as lng,hos.AuditStatus as auditStatus,"
						+ "hos.HosProperty as hosProperty,hos.HosType as hosType,hos.HosWebSite as hosWebSite ,hos.DailyOutpatientNumber as dailyOutpatientNumber,"
						+ "hos.DailyAppointNumber as dailyAppointNumber,hos.ExternalAppointmentApproach as externalAppointmentApproach,"
						+ "hos.HosProfile as hosProfile,hos.CharacteristicClinic as characteristicClinic,hos.ContactorName as contactorName,hos.ContactorTelphone as contactorTelphone,"
						+ "hos.ContactorDuty as contactorDuty,hos.ContactorEmail as contactorEmail,hos.AuthorizeFile as authorizeFile,"
						+ "hos.DockingMode as dockingMode ,hos.AccountCreate as accountCreate,dict.DistName as city");
		hqlBuilder.append(" from hospital_detail_info hos ");
		hqlBuilder
				.append(" left join dict_district_info dict on hos.DistCode=dict.DistCode ");
		hqlBuilder
				.append(" where 1=1 and hos.IsCooHospital=1 and hos.AuditStatus=1 and dict.DistName like '%"
						+ city + "%' ");
		hqlBuilder.append(" and hos.Status=1 ");
		List<HospitalDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDetailInfo.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<HospitalDetailInfo> queryHospitalDetailsByDsitcode(
			String distcode,Integer type) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder(
				"from HospitalDetailInfo where 1=1 ");
		if(type.equals(1)){
			sb.append(" and isCooHospital is null");
		}else if(type.equals(2)){
			sb.append(" and isCooHospital=1 ");
		}
		if (StringUtils.isNotBlank(distcode) && distcode.length() == 2) {
			sb.append(" and substring(distCode,1,2)='" + distcode + "' ");
		}
		if(StringUtils.isNotBlank(distcode)&&distcode.length()==4){
			sb.append(" and substring(distCode,1,4)='"+distcode+"' ");
		}
		if (StringUtils.isNotBlank(distcode) && distcode.length() == 6) {
			if(distcode.endsWith("00")) {
				sb.append(" and distCode like '" + distcode.substring(0, 4) + "%' ");
			}else {
				sb.append(" and distCode='" + distcode + "' ");
			}
			
		}
		sb.append(" and status=1 ");
		System.out.println("==="+sb.toString());
		return this.hibernateTemplate.find(sb.toString());
	}

	@SuppressWarnings("unchecked")
	public List<HospitalDetailInfo> queryHospitalDetailsALL() {
		// TODO Auto-generated method stub
		String hql = "from HospitalDetailInfo where 1=1";
		return this.hibernateTemplate.find(hql);
	}

	private String generateSearchSql(String search) {
		StringBuilder sb = new StringBuilder();
		sb.append(" and (");
		sb.append(" hos.DisplayName like '%" + search + "%' ");
		sb.append(" or hos.ShortName like '%" + search + "%' ");
		sb.append(" or pro.DistName like '%" + search + "%' ");
		sb.append(" or dict.DistName like '%" + search + "%' ");
		sb.append(" )");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryHospitalsBySystem(String search,
			final Integer start, final Integer length, Integer type) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder(
				"select hos.Id,hos.DisplayName as displayName,hos.ShortName as shortName,hos.HospitalLevel as hospitalLevel"
						+ ",hos.DistCode as distCode, hos.Location as location,hos.BigPicture as bigPicture,hos.Remark as remark,hos.IsCooHospital as isCooHospital"
						+ ",hos.Keywords as keywords,hos.Status as status,hos.Lat as lat,hos.Lng as lng,hos.AuditStatus as auditStatus,"
						+ "hos.HosProperty as hosProperty,hos.HosType as hosType,hos.HosWebSite as hosWebSite ,hos.DailyOutpatientNumber as dailyOutpatientNumber,"
						+ "hos.DailyAppointNumber as dailyAppointNumber,hos.ExternalAppointmentApproach as externalAppointmentApproach,"
						+ "hos.HosProfile as hosProfile,hos.CharacteristicClinic as characteristicClinic,hos.ContactorName as contactorName,hos.ContactorTelphone as contactorTelphone,"
						+ "hos.ContactorDuty as contactorDuty,hos.ContactorEmail as contactorEmail,hos.AuthorizeFile as authorizeFile,"
						+ "hos.DockingMode as dockingMode ,hos.AccountCreate as accountCreate,dic.DisplayName as hosLevel,concat_ws(' ',pro.DistName,dict.DistName) as distName from hospital_detail_info hos");
		sqlBuilder
				.append(" left join dictionary dic on hos.HospitalLevel=dic.Id");
		sqlBuilder
				.append(" left join dict_district_info dict on hos.DistCode=dict.DistCode ");
		sqlBuilder
				.append("LEFT JOIN dict_district_info pro ON CONCAT(SUBSTRING(dict.DistCode,1,2),'0000')=pro.DistCode");
		sqlBuilder.append(" where 1=1 ");
		sqlBuilder.append(generateSearchSql(search));
		if (type != null && type.equals(1)) {
			sqlBuilder.append(" and hos.IsCooHospital =1");
		} else {
			sqlBuilder.append(" and hos.IsCooHospital is null ");
		}
		sqlBuilder.append(" order by hos.CreateTime desc");
		List<HospitalDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDetailInfo.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"select count(*) from hospital_detail_info hos");
		countSql.append(" left join dictionary dic on hos.HospitalLevel=dic.Id");
		countSql.append(" left join dict_district_info dict on hos.DistCode=dict.DistCode ");
		countSql.append("LEFT JOIN dict_district_info pro ON CONCAT(SUBSTRING(dict.DistCode,1,2),'0000')=pro.DistCode");
		countSql.append(" where 1=1 ");
		countSql.append(generateSearchSql(search));
		if (type != null && type.equals(1)) {
			countSql.append(" and hos.IsCooHospital =1");
		} else {
			countSql.append(" and hos.IsCooHospital is null ");
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

	@SuppressWarnings("unchecked")
	public List<HospitalDetailInfo> queryHospitalsByPage(final Integer pageNo,
			final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder(
				"select hos.Id as id,hos.DisplayName as displayName,hos.BigPicture as bigPicture,dic.DisplayName as hosLevel from hospital_detail_info hos");
		sqlBuilder
				.append(" left join dictionary dic on hos.HospitalLevel=dic.Id ");
		sqlBuilder.append(" where 1=1 and hos.IsCooHospital is null ");
		List<HospitalDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult((pageNo - 1) * pageSize);
						query.setMaxResults(pageSize);
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDetailInfo.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Integer> queryExpertAndDepNumberByHos(Integer hosid) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		// TODO Auto-generated method stub
		final StringBuilder countSql = new StringBuilder(
				"select count(*) from doctor_detail_info where HospitalId="
						+ hosid);
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(countSql.toString())
								.uniqueResult();
					}
				});
		Integer expertnum = StringUtils.isNotBlank(effectNumber.toString()) ? Integer
				.parseInt(effectNumber.toString()) : 0;

		final StringBuilder _countSql = new StringBuilder(
				"SELECT  COUNT(DISTINCT DepId) FROM  doctor_detail_info WHERE HospitalId="
						+ hosid);
		Object _effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(_countSql.toString())
								.uniqueResult();
					}
				});
		Integer depnum = StringUtils.isNotBlank(_effectNumber.toString()) ? Integer
				.parseInt(_effectNumber.toString()) : 0;

		map.put("expertnum", expertnum);
		map.put("depnum", depnum);
		return map;
	}

	@SuppressWarnings("unchecked")
	public Pager searchspecialsByPager(Pager pager) {
		// TODO Auto-generated method stub
		if (pager == null)
			pager = new Pager();
		final StringBuilder queryStr = new StringBuilder(
				"select distinct doc.Id as specialId,doc.DisplayName as specialName,hos.DisplayName as hosName,dep.DisplayName as depName,ds.IsOpen as openAsk,ts.IsOpen as openTel,"
						+ "doc.HeadImageUrl as listSpecialPicture,doc.Duty as duty,doc.Profession as profession,doc.Speciality as specialty,doc.Recommend as recommond from doctor_detail_info doc ");
		queryStr.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		queryStr.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		queryStr.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		queryStr.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id and ds.ServiceId=1 ");
		queryStr.append(" left join doctor_service_info ts on ts.DoctorId=doc.Id and ts.ServiceId=2 ");
		queryStr.append(" where 1=1 and reg.UserType=2 and reg.Status=1 ");
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("distcode"))
				&& pager.getQueryBuilder().get("distcode").length() == 2) {
			queryStr.append(" and substring(hos.DistCode,1,2)='"
					+ pager.getQueryBuilder().get("distcode") + "' ");
		}
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("distcode"))
				&& pager.getQueryBuilder().get("distcode").length() == 6) {
			queryStr.append(" and hos.DistCode='"
					+ pager.getQueryBuilder().get("distcode") + "' ");
		}

		if (pager.getQueryBuilder().get("stype").equalsIgnoreCase("remote")) {
			System.out.println("============远程门诊过滤========================");
			if (StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))
					&& !StringUtils.isNotBlank(pager.getQueryBuilder().get(
							"depid"))) {
				queryStr.append(" and  DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId IN(SELECT Id  FROM hospital_department_info WHERE HospitalId="
						+ pager.getQueryBuilder().get("hosid")
						+ ")"
						+ " AND DepartmentType=1) AND DepartmentType=2)");
			}
			if (StringUtils.isNotBlank(pager.getQueryBuilder().get("depid"))) {
				queryStr.append(" and  DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId="
						+ pager.getQueryBuilder().get("depid")
						+ " AND DepartmentType=1) AND DepartmentType=2)");
			}
		} else {
			if (StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))) {
				queryStr.append(" and hos.Id="
						+ pager.getQueryBuilder().get("hosid"));
			}
			if (StringUtils.isNotBlank(pager.getQueryBuilder().get("sdepid"))) {
				queryStr.append(" and doc.DepId in(select DepId from dep_standarddep_r where DepartmentType=2 AND StandardDepId="
						+ pager.getQueryBuilder().get("sdepid") + ") ");
			}
		}
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("stype"))
				&& pager.getQueryBuilder().get("stype")
						.equalsIgnoreCase("online")) {
			// 过滤在线问诊的专家数据
			queryStr.append(" and (ds.IsOpen=1 or ts.IsOpen=1) ");
		}
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("stype"))
				&& pager.getQueryBuilder().get("stype")
						.equalsIgnoreCase("ask")) {
			// 过滤在线问诊的专家数据
			queryStr.append(" and doc.OpenAsk=1 ");
		}
		StringBuilder querysb = gainSearchQuey(pager);
		queryStr.append(querysb.toString());
		queryStr.append(" and doc.Status=1 ");
		if (!StringUtils.isNotBlank(pager.getQueryBuilder().get("openVedio"))
				|| !pager.getQueryBuilder().get("openVedio")
						.equalsIgnoreCase("false")) {
			queryStr.append(" and doc.OpenVedio=1");
		}
		if (!StringUtils.isNotBlank(pager.getQueryBuilder().get("recommond"))
				|| !pager.getQueryBuilder().get("recommond")
						.equalsIgnoreCase("false")) {
			queryStr.append(" and doc.Recommend=1");
		}
		System.out.println("===sql==" + queryStr.toString());
		final Pager _pager = pager;
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery query = session.createSQLQuery(queryStr
								.toString());
						query.setFirstResult((_pager.getPageNumber() - 1)
								* _pager.getPageSize());
						query.setMaxResults(_pager.getPageSize());
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		StringBuilder countStr = new StringBuilder(
				"select count(*) from doctor_detail_info doc");
		countStr.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		countStr.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countStr.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countStr.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id and ds.ServiceId=1 ");
		countStr.append(" left join doctor_service_info ts on ts.DoctorId=doc.Id and ts.ServiceId=2 ");
		countStr.append(" where 1=1 and reg.UserType=2 and reg.Status=1 ");
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("distcode"))
				&& pager.getQueryBuilder().get("distcode").length() == 2) {
			countStr.append(" and substring(hos.DistCode,1,2)='"
					+ pager.getQueryBuilder().get("distcode") + "' ");
		}
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("distcode"))
				&& pager.getQueryBuilder().get("distcode").length() == 6) {
			countStr.append(" and hos.DistCode='"
					+ pager.getQueryBuilder().get("distcode") + "' ");
		}
		if (pager.getQueryBuilder().get("stype").equalsIgnoreCase("remote")) {
			if (StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))
					&& !StringUtils.isNotBlank(pager.getQueryBuilder().get(
							"depid"))) {
				countStr.append(" and  DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId IN(SELECT Id  FROM hospital_department_info WHERE HospitalId="
						+ pager.getQueryBuilder().get("hosid")
						+ ")"
						+ " AND DepartmentType=1) AND DepartmentType=2)");
			}
			if (StringUtils.isNotBlank(pager.getQueryBuilder().get("depid"))) {
				countStr.append(" and  DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId="
						+ pager.getQueryBuilder().get("depid")
						+ " AND DepartmentType=1) AND DepartmentType=2)");
			}
		} else {
			if (StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))) {
				countStr.append(" and hos.Id="
						+ pager.getQueryBuilder().get("hosid"));
			}
			if (StringUtils.isNotBlank(pager.getQueryBuilder().get("sdepid"))) {
				countStr.append(" and doc.DepId in(select DepId from dep_standarddep_r where DepartmentType=2 AND StandardDepId="
						+ pager.getQueryBuilder().get("sdepid") + ") ");
			}
		}
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("stype"))
				&& pager.getQueryBuilder().get("stype")
						.equalsIgnoreCase("online")) {
			// 过滤在线问诊的专家数据
			countStr.append(" and (ds.IsOpen=1 or ts.IsOpen=1) ");
		}
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("stype"))
				&& pager.getQueryBuilder().get("stype")
						.equalsIgnoreCase("ask")) {
			// 过滤在线问诊的专家数据
			countStr.append(" and doc.OpenAsk=1 ");
		}
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("duty"))) {
			countStr.append(" and doc.duty='"
					+ pager.getQueryBuilder().get("duty") + "' ");
		}
		countStr.append(querysb.toString());
		countStr.append(" and doc.Status=1 ");
		if (!StringUtils.isNotBlank(pager.getQueryBuilder().get("openVedio"))
				|| !pager.getQueryBuilder().get("openVedio")
						.equalsIgnoreCase("false")) {
			countStr.append(" and doc.OpenVedio=1");
		}
		if (!StringUtils.isNotBlank(pager.getQueryBuilder().get("recommond"))
				|| !pager.getQueryBuilder().get("recommond")
						.equalsIgnoreCase("false")) {
			countStr.append(" and doc.Recommend=1");
		}
		String count = ObjectUtils.toString(this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createSQLQuery(countStr.toString()).uniqueResult());
		pager.setTotalCount(Integer.valueOf(count));
		pager.setList(list);
		return pager;
	}

	

	@SuppressWarnings("unchecked")
	public Pager queryMobileSpecialsByLocalDepartId_newpager(Integer depid,
			Integer type, Pager pager) {
		// TODO Auto-generated method stub
		if (pager == null)
			pager = new Pager();
		final StringBuilder sb = new StringBuilder(
				"SELECT doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,"
						+ " doc.HospitalId as hosId,hos.DisplayName as hosName,doc.DepId as depId,dep.DisplayName as depName ,doc.VedioAmount as vedioAmount, "
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty,ds.Amount as askAmount "
						+ " FROM doctor_detail_info doc ");
		sb.append(" left join doctor_register_info reg on reg.Id=doc.Id ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id");
		sb.append(" WHERE DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId="
				+ depid + " AND DepartmentType=1) AND DepartmentType=2)");
		sb.append(" and doc.Status=1 ");
		sb.append(" and ds.ServiceId=5 and ds.IsOpen=1 and reg.Status=1 ");
		
		final Pager _pager = pager;
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult((_pager.getPageNumber() - 1)
								* _pager.getPageSize());
						query.setMaxResults(_pager.getPageSize());
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		StringBuilder countStr = new StringBuilder(
				"select count(*) FROM doctor_detail_info doc");
		countStr.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		countStr.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countStr.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		countStr.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		countStr.append(" WHERE DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId="
				+ depid + " AND DepartmentType=1) AND DepartmentType=2)");
		countStr.append(" and doc.Status=1 ");
		countStr.append(" and ds.ServiceId=5 and ds.IsOpen=1 and reg.Status=1 ");
		String count = ObjectUtils.toString(this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createSQLQuery(countStr.toString()).uniqueResult());
		pager.setTotalCount(Integer.valueOf(count));
		pager.setList(list);
		return pager;
	}

	private StringBuilder gainSearchQuey(Pager pager) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("queryseach"))) {
			String search = pager.getQueryBuilder().get("queryseach");
			sb.append(" and (");
			sb.append(" doc.DisplayName like '%" + search + "%' ");
			sb.append(" or doc.Duty like '%" + search + "%' ");
			sb.append(" or doc.Speciality like '%" + search + "%' ");
			sb.append(" or doc.Profile like '%" + search + "%' ");
			sb.append(" or doc.Profession like '%" + search + "%' ");
			sb.append(" or hos.DisplayName like '%" + search + "%' ");
			sb.append(" or hos.ShortName like '%" + search + "%' ");
			sb.append(" or dep.DisplayName like '%" + search + "%' ");
			sb.append(" ) ");
		}
		return sb;
	}

	@SuppressWarnings("unchecked")
	public List<ReSourceBean> queryOpenCitys() {
		// TODO Auto-generated method stub
		final StringBuilder hqlBuilder = new StringBuilder(
				"select dict.DistCode as remark,dict.DistName as name");
		hqlBuilder.append(" from hospital_detail_info hos ");
		hqlBuilder
				.append(" left join dict_district_info dict on hos.DistCode=dict.DistCode ");
		hqlBuilder
				.append(" where hos.IsCooHospital=1 and hos.AuditStatus=1 and hos.Status=1 ");
		hqlBuilder.append("GROUP BY dict.DistName ");
		hqlBuilder.append(" order by hos.Rank desc");
		List<ReSourceBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(ReSourceBean.class));
						return query.list();
					}
				});
		return list;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public Pager searchspecialsByPager_advice(Pager pager) {
		// TODO Auto-generated method stub
		if (pager == null)
			pager = new Pager();
		final StringBuilder queryStr = new StringBuilder(
				"select doc.Id as specialId,doc.DisplayName as specialName,hos.DisplayName as hosName,dep.DisplayName as depName,"
				+"ds.Amount as askAmount,"
						+ "doc.HeadImageUrl as listSpecialPicture,doc.Duty as duty,doc.Profession as profession,doc.Speciality as specialty,doc.Recommend as recommond from doctor_detail_info doc ");
		queryStr.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		queryStr.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		queryStr.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		queryStr.append(" left join doctor_service_info ds on doc.Id=ds.DoctorId ");
		queryStr.append(" where 1=1 and reg.UserType=2 and reg.Status=1 ");
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))) {
			queryStr.append(" and hos.Id="
					+ pager.getQueryBuilder().get("hosid"));
		}
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("sdepid"))) {
			queryStr.append(" and doc.DepId in(select DepId from dep_standarddep_r where DepartmentType=2 AND StandardDepId="
					+ pager.getQueryBuilder().get("sdepid") + ") ");
		}
		queryStr.append(" and ds.ServiceId=5 and ds.IsOpen=1 ");
		StringBuilder querysb = gainSearchQuey(pager);
		queryStr.append(querysb.toString());
		queryStr.append(" and doc.Status=1 ");
		
		final Pager _pager = pager;
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery query = session.createSQLQuery(queryStr
								.toString());
						query.setFirstResult((_pager.getPageNumber() - 1)
								* _pager.getPageSize());
						query.setMaxResults(_pager.getPageSize());
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		StringBuilder countStr = new StringBuilder(
				"select count(*) from doctor_detail_info doc");
		countStr.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		countStr.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countStr.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countStr.append(" left join doctor_service_info ds on doc.Id=ds.DoctorId ");
		countStr.append(" where 1=1 and reg.UserType=2 and reg.Status=1 ");
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))) {
			countStr.append(" and hos.Id="
					+ pager.getQueryBuilder().get("hosid"));
		}
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("sdepid"))) {
			countStr.append(" and doc.DepId in(select DepId from dep_standarddep_r where DepartmentType=2 AND StandardDepId="
					+ pager.getQueryBuilder().get("sdepid") + ") ");
		}
		countStr.append(" and ds.ServiceId=5 and ds.IsOpen=1 ");
		countStr.append(querysb.toString());
		countStr.append(" and doc.Status=1 ");
		
		String count = ObjectUtils.toString(this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createSQLQuery(countStr.toString()).uniqueResult());
		pager.setTotalCount(Integer.valueOf(count));
		pager.setList(list);
		return pager;
	}
	
	
	@SuppressWarnings("unchecked")
	public Pager searchspecialsByPager_remote(Pager pager) {
		// TODO Auto-generated method stub
		if (pager == null)
			pager = new Pager();
		final StringBuilder queryStr = new StringBuilder(
				"select doc.Id as specialId,doc.DisplayName as specialName,hos.DisplayName as hosName,dep.DisplayName as depName,"
						+ "doc.HeadImageUrl as listSpecialPicture,doc.Duty as duty,doc.Profession as profession,doc.Speciality as specialty,doc.Recommend as recommond from doctor_detail_info doc ");
		queryStr.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		queryStr.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		queryStr.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		queryStr.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		queryStr.append(" where 1=1 and reg.UserType=2 and reg.Status=1 ");
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))
				&& !StringUtils.isNotBlank(pager.getQueryBuilder().get(
						"depid"))) {
			queryStr.append(" and  DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId IN(SELECT Id  FROM hospital_department_info WHERE HospitalId="
					+ pager.getQueryBuilder().get("hosid")
					+ ")"
					+ " AND DepartmentType=1) AND DepartmentType=2)");
		}
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("depid"))) {
			queryStr.append(" and  DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId="
					+ pager.getQueryBuilder().get("depid")
					+ " AND DepartmentType=1) AND DepartmentType=2)");
		}
		StringBuilder querysb = gainSearchQuey(pager);
		queryStr.append(querysb.toString());
		queryStr.append(" and doc.Status=1 ");
		queryStr.append(" and ds.ServiceId=4 and ds.PackageId=3 and ds.IsOpen=1 ");
		if (!StringUtils.isNotBlank(pager.getQueryBuilder().get("recommond"))
				|| !pager.getQueryBuilder().get("recommond")
						.equalsIgnoreCase("false")) {
			queryStr.append(" and doc.Recommend=1");
		}
		final Pager _pager = pager;
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery query = session.createSQLQuery(queryStr
								.toString());
						query.setFirstResult((_pager.getPageNumber() - 1)
								* _pager.getPageSize());
						query.setMaxResults(_pager.getPageSize());
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		StringBuilder countStr = new StringBuilder(
				"select count(*) from doctor_detail_info doc");
		countStr.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		countStr.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countStr.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countStr.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		countStr.append(" where 1=1 and reg.UserType=2 and reg.Status=1 ");
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))
				&& !StringUtils.isNotBlank(pager.getQueryBuilder().get(
						"depid"))) {
			countStr.append(" and  DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId IN(SELECT Id  FROM hospital_department_info WHERE HospitalId="
					+ pager.getQueryBuilder().get("hosid")
					+ ")"
					+ " AND DepartmentType=1) AND DepartmentType=2)");
		}
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("depid"))) {
			countStr.append(" and  DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId="
					+ pager.getQueryBuilder().get("depid")
					+ " AND DepartmentType=1) AND DepartmentType=2)");
		}
		countStr.append(querysb.toString());
		countStr.append(" and doc.Status=1 ");
		countStr.append(" and ds.ServiceId=4 and ds.PackageId=3 and ds.IsOpen=1 ");
		if (!StringUtils.isNotBlank(pager.getQueryBuilder().get("recommond"))
				|| !pager.getQueryBuilder().get("recommond")
						.equalsIgnoreCase("false")) {
			countStr.append(" and doc.Recommend=1");
		}
		String count = ObjectUtils.toString(this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createSQLQuery(countStr.toString()).uniqueResult());
		pager.setTotalCount(Integer.valueOf(count));
		pager.setList(list);
		return pager;
	}


	@SuppressWarnings("unchecked")
	public Map<String,Object> queryNewAddHospital(List<String> dates) {
		Map<String,Object> map=new HashMap<String,Object>();
		// TODO Auto-generated method stub
		final StringBuilder counthql = new StringBuilder();
		counthql.append(" select count(1) as count ");
		counthql.append(" from hospital_detail_info rc ");
		counthql.append(" where 1=1 ");
		counthql.append(" and IsCooHospital=1 AND STATUS=1 AND AuditStatus=1 ");
		String mcountsql=counthql.toString();
		StringBuilder finalcounthql=new StringBuilder();
		
		if(dates!=null&&dates.size()>0){
			for(String date:dates){
				finalcounthql.append(mcountsql).append("");
				finalcounthql.append(" and (DATE_FORMAT(CreateTime,'%Y-%m')='"
						+ date + "' ) union all");
			}
			mcountsql=finalcounthql.substring(0, finalcounthql.length()-9);
		}
		final String cout_sql=mcountsql;
		List<ReSourceBean> modatas = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(cout_sql
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(ReSourceBean.class));
						return query.list();
					}
				});
		map.put("modata", modatas);
		/*final StringBuilder _counthql=new StringBuilder();
		for(String date:dates){
			_counthql.append(mcountsql).append("");
			_counthql.append("and (DATE_FORMAT(CreateTime,'%Y-%m')<='"
				+ date + "' ) union all");
		}
		final String _cout_sql=_counthql.substring(0, _counthql.length()-9);
		List<ReSourceBean> todata = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(_cout_sql
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(ReSourceBean.class));
						return query.list();
					}
				});
			map.put("todata", todata);*/
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<HospitalDetailInfo> queryHospitals_expert() {
		// TODO Auto-generated method stub
		String hql="select id,displayName from  HospitalDetailInfo where 1=1 and isCooHospital is null";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<HospitalDetailInfo> querynearhoses(final String ispage, String htype,
			String distcode,final Integer pageNo,final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select hos.Id,hos.DisplayName,hos.ShortName,hos.BigPicture from hospital_detail_info hos ");
		sqlBuilder.append(" where 1=1 ");
		if(!StringUtils.isNotBlank(htype))htype="3";
		//1:省级，2：市级，3：区县
		if(htype.equalsIgnoreCase("1")){
			String pdist=distcode.substring(0,2);
			sqlBuilder.append(" and hos.DistCode like '"+pdist+"%' ");
		}else if(htype.equalsIgnoreCase("2")){
			String cdist=distcode.substring(0, 4);
			sqlBuilder.append(" and hos.DistCode like '"+cdist+"%' ");
		}else if(htype.equalsIgnoreCase("3")){
			sqlBuilder.append(" and hos.DistCode='"+distcode+"' ");
		}
		sqlBuilder.append(" and hos.IsCooHospital=1 and hos.Status=1 order by hos.Rank desc ");
		List<HospitalDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						if(StringUtils.isNotBlank(ispage)&&ispage.equalsIgnoreCase("1")){
							query.setFirstResult((pageNo - 1) * pageSize);
							query.setMaxResults(pageSize);
						}
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDetailInfo.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<HospitalDetailInfo> queryHospitalsByCon(String status,String aids) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select hos.Id,hos.DisplayName,dic.DisplayName as hosLevel from hospital_detail_info hos ");
		sqlBuilder.append(" left join dictionary dic on hos.HospitalLevel=dic.Id ");
		sqlBuilder.append(" where 1=1 and hos.HospitalLevel in("+status+") and hos.Status=1  ");
		if(StringUtils.isNotBlank(aids)){
			sqlBuilder.append(" and hos.Id not in("+aids+")");
		}
		List<HospitalDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDetailInfo.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<HospitalDetailInfo> queryhighlevelhos(Integer allianceId,
			String levels) {
		// TODO Auto-generated method stub
		final StringBuilder sb=new StringBuilder();
		sb.append("select hos.Id,hos.DisplayName,dic.DisplayName as hosLevel from hospital_health_alliance hha ");
		sb.append(" left join hospital_health_alliance_member hm on hha.UUID=hm.AllianceUuid ");
		sb.append(" left join hospital_detail_info hos on hm.HospitalId=hos.Id ");
		sb.append(" left join dictionary dic on dic.Id=hos.HospitalLevel ");
		sb.append(" where 1=1 and hha.Id="+allianceId);
		sb.append(" and hos.HospitalLevel in("+levels+") ");
		sb.append(" and hm.Status=1 ");
		List<HospitalDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDetailInfo.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> operatorhosdatas(String searchContent,
			Integer opId,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sql=new StringBuilder();
		sql.append("select hmr.Id as relativeId,hos.Id,hos.DisplayName as displayName,dic.DistName as distName,hmr.CreateTime as createTime  ");
		sql.append(" from hospital_maintainer_relation hmr ");
		sql.append(" left join hospital_detail_info hos on hmr.HospitalId=hos.Id ");
		sql.append(" left join dict_district_info dic on dic.DistCode=hos.DistCode ");
		sql.append(" where 1=1 and hmr.UserId="+opId);
		List<HospitalDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDetailInfo.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("select count(hmr.Id) ");
		countSql.append(" from hospital_maintainer_relation hmr ");
		countSql.append(" where 1=1 and hmr.UserId="+opId);
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
	public List<HospitalDetailInfo> queryhospitalInfos() {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder();
		sb.append(" select DISTINCT hos.DisplayName,hos.id from hospital_detail_info hos where hos.Status=1 order by hos.id");		
		List<HospitalDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDetailInfo.class));
						return query.list();
					}
				});
		return list;
	}
}
