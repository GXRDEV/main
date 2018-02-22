package com.tspeiz.modules.common.dao.impl.release2;

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
import com.tspeiz.modules.common.bean.dto.DoctorInfoDto;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IHospitalHealthAllianceDao;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAlliance;
@Repository
public class HospitalHealthAllianceDaoImpl extends BaseDaoImpl<HospitalHealthAlliance> implements IHospitalHealthAllianceDao{
	private String gainsearchquery(String search){
		StringBuilder sb=new StringBuilder();
		if(StringUtils.isNotBlank(search)){
			sb.append(" and ( ");
			sb.append(" hha.YltName like '%"+search+"%' or ");
			sb.append(" adoc.DisplayName like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' or ");
			sb.append(" dic.DisplayName like '%"+search+"%' or ");
			sb.append(" ddi.DistName like '%"+search+"%' or ");
			sb.append(" doc.DisplayName like '%"+search+"%' ");
			sb.append(" ) ");
		}
		return sb.toString();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> queryhoshealthdatas(String search,
			final Integer start,final Integer length, String status) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT hha.Id,hha.UUID as uuid,hha.ApplicationTime,hha.ApplicantType,hha.YltName,hha.Status,hha.IconUrl,doc.DisplayName as applicant,"
				+ "hha.AuditTime,hha.AuditorType,adoc.DisplayName as auditor,hos.DisplayName as hosName,dic.DisplayName as hosLevel,"
				+ "ddi.DistName  from hospital_health_alliance hha ");
		sqlBuilder.append(" left join doctor_register_info reg on hha.ApplicantId=reg.Id ");
		sqlBuilder.append(" left join doctor_register_info areg on hha.AuditorId=areg.Id ");
		sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=reg.Id ");
		sqlBuilder.append(" left join doctor_detail_info adoc on adoc.Id=areg.Id ");
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=hha.HospitalId ");
		sqlBuilder.append(" left join dictionary dic on dic.Id=hos.HospitalLevel ");
		sqlBuilder.append(" left join dict_district_info ddi on ddi.DistCode=hos.DistCode ");
		sqlBuilder.append(" where 1=1 and hha.Status in("+status+") ");
		sqlBuilder.append(gainsearchquery(search));
		sqlBuilder.append(" order by hha.applicationTime desc ");
		List<HospitalHealthAlliance> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalHealthAlliance.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("SELECT count(hha.Id) from hospital_health_alliance hha ");
		countSql.append(" left join doctor_register_info reg on hha.ApplicantId=reg.Id ");
		countSql.append(" left join doctor_register_info areg on hha.AuditorId=areg.Id ");
		countSql.append(" left join doctor_detail_info doc on doc.Id=reg.Id ");
		countSql.append(" left join doctor_detail_info adoc on adoc.Id=areg.Id ");
		countSql.append(" left join hospital_detail_info hos on hos.Id=hha.HospitalId ");
		countSql.append(" left join dictionary dic on dic.Id=hos.HospitalLevel ");
		countSql.append(" left join dict_district_info ddi on ddi.DistCode=hos.DistCode ");
		countSql.append(" where 1=1 and hha.Status in("+status+") ");
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
	public HospitalHealthAlliance queryHospitalHealthAllianceByUuid(String uuid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select hos.DisplayName as hosName,dic.DistName as position from hospital_health_alliance hha ");
		sqlBuilder.append(" LEFT JOIN hospital_health_alliance_member hm ON hha.`UUID`=hm.`AllianceUuid` ");
		sqlBuilder.append(" left join hospital_detail_info hos on hm.HospitalId=hos.Id ");
		sqlBuilder.append(" left join dict_district_info dic on dic.DistCode=hos.DistCode ");
		sqlBuilder.append(" where hha.UUID='"+uuid+"' and hm.Role=1 ");
		List<HospitalHealthAlliance> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalHealthAlliance.class));
						return query.list();
					}
				});
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}


	@SuppressWarnings("unchecked")
	public HospitalHealthAlliance queryHospitalHealthAllianceByRegId(
			Integer regId) {
		// TODO Auto-generated method stub
		String hql=" from HospitalHealthAlliance where applicantId="+regId+" and Status not in(-1) ";
		List<HospitalHealthAlliance> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<HospitalHealthAlliance> queryHospitalHealthAlliances(
			Integer hosId) {
		// TODO Auto-generated method stub
		final StringBuilder sb=new StringBuilder();
		sb.append("SELECT hha.Id,hha.YltName FROM hospital_health_alliance hha LEFT JOIN hospital_health_alliance_member hm ON hha.`UUID`=hm.`AllianceUuid` ");
		sb.append(" WHERE hm.`HospitalId`="+hosId);
		sb.append(" and hha.Status=1 ");
		sb.append(" and hm.Status=1 ");
		List<HospitalHealthAlliance> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalHealthAlliance.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public HospitalHealthAlliance queryHospitalHealthAllianceById_new(Integer id) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT hha.Id,hha.ApplicationTime,hha.ApplicantType,hha.YltName,hha.Status,hha.IconUrl,doc.DisplayName as applicant,"
				+ "hha.AuditTime,hha.AuditorType,adoc.DisplayName as auditor,hos.DisplayName as hosName,dic.DisplayName as hosLevel,"
				+ "ddi.DistName,hha.Profile,hha.Speciality,hha.IconUrl,hha.HospitalId  from hospital_health_alliance hha ");
		sqlBuilder.append(" left join doctor_register_info reg on hha.ApplicantId=reg.Id ");
		sqlBuilder.append(" left join doctor_register_info areg on hha.AuditorId=areg.Id ");
		sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=reg.Id ");
		sqlBuilder.append(" left join doctor_detail_info adoc on adoc.Id=areg.Id ");
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=hha.HospitalId ");
		sqlBuilder.append(" left join dictionary dic on dic.Id=hos.HospitalLevel ");
		sqlBuilder.append(" left join dict_district_info ddi on ddi.DistCode=hos.DistCode ");
		sqlBuilder.append(" where 1=1 and hha.Id= "+id);
		List<HospitalHealthAlliance> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalHealthAlliance.class));
						return query.list();
					}
				});
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<HospitalHealthAlliance> querygainAllianceByArea(String distCode) {
		// TODO Auto-generated method stub
		final StringBuilder sb=new StringBuilder();
		sb.append("select  alliance.Id,alliance.YltName ");
		sb.append(" from hospital_health_alliance alliance ");
		sb.append(" left join hospital_detail_info hos on hos.Id=alliance.HospitalId ");
		sb.append(" where 1=1 and alliance.Status=1 ");
		sb.append(" and hos.DistCode='"+distCode+"' ");
		List<HospitalHealthAlliance> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalHealthAlliance.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DoctorInfoDto> loadAllianceDoctors(String allianceId,
			final Integer pageNo,final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select doc.Id as docId,doc.DisplayName as docName,doc.HeadImageUrl as headImage, ");
		sqlBuilder.append(" hos.DisplayName as hosName,dep.DisplayName as depName,doc.Duty,doc.Profession ");
		sqlBuilder.append(" from hospital_health_alliance alliance ");
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=alliance.HospitalId ");
		sqlBuilder.append(" left join hospital_detail_info thos on thos.Id=");	
		sqlBuilder.append(" left join doctor_register_info reg on reg.Id=doc.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sqlBuilder.append(" where 1=1 and alliance.Status=1 ");
		if(StringUtils.isNotBlank(allianceId)){
			sqlBuilder.append(" and alliance.Id="+allianceId);
		}
		sqlBuilder.append(" and reg.UserType not in(5,6,7,8)");
		List<DoctorInfoDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult((pageNo-1)*pageSize);
						query.setMaxResults(pageSize);
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorInfoDto.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Pager queryAllianceDoctorsPager(Pager pager) {
		// TODO Auto-generated method stub
		if (pager == null)
			pager = new Pager();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select doc.Id as docId,doc.DisplayName as docName,doc.HeadImageUrl as headImage, ");
		sqlBuilder.append(" hos.DisplayName as hosName,dep.DisplayName as depName,doc.Duty,doc.Profession,doc.Profile,doc.Speciality ");
		sqlBuilder.append(" from hospital_health_alliance alliance ");
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=alliance.HospitalId ");
		sqlBuilder.append(" left join doctor_detail_info  doc on doc.HospitalId=hos.Id ");	
		sqlBuilder.append(" left join doctor_register_info reg on reg.Id=doc.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sqlBuilder.append(" where 1=1 and alliance.Status=1 ");
		sqlBuilder.append(" and doc.DutyId in(4,5) ");
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("allianceId"))) {
			sqlBuilder.append(" and alliance.Id="+pager.getQueryBuilder().get("allianceId"));
		}
		sqlBuilder.append(" and reg.UserType not in(5,6,7,8)");
		final Pager _pager = pager;
		List<DoctorInfoDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult((_pager.getPageNumber()-1)*_pager.getPageSize());
						query.setMaxResults(_pager.getPageSize());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorInfoDto.class));
						return query.list();
					}
				});
		final StringBuilder countSql=new StringBuilder();
		countSql.append("select count(doc.Id) ");
		countSql.append(" from hospital_health_alliance alliance ");
		countSql.append(" left join hospital_detail_info hos on hos.Id=alliance.HospitalId ");
		countSql.append(" left join doctor_detail_info  doc on doc.HospitalId=hos.Id ");	
		countSql.append(" left join doctor_register_info reg on reg.Id=doc.Id ");
		countSql.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		countSql.append(" where 1=1 and alliance.Status=1 ");
		countSql.append(" and doc.DutyId in(4,5) ");
		if (StringUtils.isNotBlank(pager.getQueryBuilder().get("allianceId"))) {
			countSql.append(" and alliance.Id="+pager.getQueryBuilder().get("allianceId"));
		}
		countSql.append(" and reg.UserType not in(5,6,7,8)");
		String count = ObjectUtils.toString(this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createSQLQuery(countSql.toString()).uniqueResult());
		pager.setTotalCount(Integer.valueOf(count));
		pager.setList(list);
		return pager;
	}
	public List<HospitalHealthAlliance> queryHospitalHealthAlliances_audited() {
		// TODO Auto-generated method stub
		String hql="from HospitalHealthAlliance where status=1 ";
		return this.hibernateTemplate.find(hql);
	}
	
	
}
