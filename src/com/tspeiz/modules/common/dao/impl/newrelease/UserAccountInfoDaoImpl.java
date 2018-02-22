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
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IUserAccountInfoDao;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.UserAccountInfo;

@Repository
public class UserAccountInfoDaoImpl extends BaseDaoImpl<UserAccountInfo>
		implements IUserAccountInfoDao {

	@SuppressWarnings("unchecked")
	public UserAccountInfo queryUserAccountInfoByMobilePhone(String tel) {
		// TODO Auto-generated method stub
		String hql = "from UserAccountInfo where 1=1 and loginName='" + tel
				+ "' ";
		List<UserAccountInfo> list = this.hibernateTemplate.find(hql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<MobileSpecial> queryMobileSpecialsByLocalDepartId(Integer depid,Integer type) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"SELECT distinct doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,"
						+ " doc.HospitalId as hosId,hos.DisplayName as hosName,doc.DepId as depId,dep.DisplayName as depName ,ds.Amount as vedioAmount, "
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty,ds.Amount as askAmount "
						+ " FROM doctor_detail_info doc ");
		sb.append(" left join doctor_register_info reg on reg.Id = doc.Id ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		sb.append(" WHERE DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId="
				+ depid + " AND DepartmentType=1) AND DepartmentType=2)");
		sb.append(" and reg.Status=1 ");
		if(type.equals(4)){
			sb.append(" and doc.Recommend=1 and doc.Status=1 and ds.ServiceId=4 and ds.PackageId=3 and ds.IsOpen=1  ");
		}else if(type.equals(1)){
			//图文
			sb.append(" and doc.Status=1 and ds.ServiceId=1 and ds.IsOpen=1  ");
		}
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
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MobileSpecial queryMobileSpecialByUserIdAndUserType(Integer user) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"SELECT doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,doc.Recommend as recommond,"
						+ " doc.HospitalId as hosId,doc.DepId as depId,doc.DistCode as distCode,doc.Position as position,doc.sex,"
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty,reg.LoginName as telphone,reg.MobileNumber as mobileTelphone,doc.Profile as profile,doc.RelatedPics as relatedPics,dic.DisplayName as hosLevel "
						+ ",doc.FamousDoctor as famousDoctor,");
		sb.append("(case when doc.HospitalId is null then doc.RegHospitalName else hos.DisplayName end) as hosName,");
		sb.append("(case when doc.DepId is null then doc.RegDepartmentName else dep.DisplayName end) as depName,doc.IDCardNo as idCardNo ");
		sb.append(",doc.BadgeUrl as badgeUrl,doc.DutyId as dutyId,reg.UserType as userType,GetDistrictNameSheng(ddi.DistCode) AS shengName,GetDistrictNameShi(ddi.DistCode) AS shiName");
		sb.append(",doc.BadgeUrl as badgeUrl,doc.DutyId as dutyId,reg.UserType as userType");
		sb.append(" FROM doctor_detail_info doc");
		sb.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" left join dictionary dic on hos.HospitalLevel=dic.Id ");
		sb.append("  LEFT JOIN dict_district_info ddi ON doc.DistCode=ddi.DistCode");
		sb.append(" where doc.Id=" + user);
		sb.append(" order by reg.RegisterTime desc ");
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

	@SuppressWarnings("unchecked")
	public List<MobileSpecial> queryMobileSpecialsByConditionsPro(
			Map<String, Object> querymap, final Integer pageNo,
			final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"SELECT DISTINCT doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,"
						+ " doc.HospitalId as hosId,hos.DisplayName as hosName,doc.DepId as depId,dep.DisplayName as depName ,"
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty,reg.LoginName as telphone,doc.Profile as profile,doc.RelatedPics as relatedPics "
						+ " FROM doctor_detail_info doc ");
		sb.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		if (querymap.containsKey("depid")
				&& StringUtils.isNotBlank(querymap.get("depid").toString())) {
			sb.append(" left join dep_standarddep_r dsr on dsr.DepId=dep.Id and dsr.DepartmentType=2 ");
		}
		sb.append(" where 1=1 and reg.UserType=2 and reg.Status=1 ");
		if (querymap.containsKey("hosid")
				&& StringUtils.isNotBlank(querymap.get("hosid").toString())) {
			sb.append(" and hos.Id=" + querymap.get("hosid").toString());
		}
		if (querymap.containsKey("depid")
				&& StringUtils.isNotBlank(querymap.get("depid").toString())) {
			sb.append(" and dsr.StandardDepId="
					+ querymap.get("depid").toString());
		}
		if(querymap.containsKey("ltype")&&StringUtils.isNotBlank(querymap.get("ltype").toString())){
			Integer ltype=Integer.parseInt(querymap.get("ltype").toString());
			if(ltype.equals(1)){
				sb.append(" and ds.ServiceId=1 and ds.IsOpen=1 ");
			}else if(ltype.equals(2)){
				sb.append(" and ds.ServiceId=2 and ds.IsOpen=1 ");
			}else if(ltype.equals(4)){
				sb.append(" and ds.ServiceId=4 and ds.PackageId=3 and ds.IsOpen=1 ");
			}
		}
		sb.append(searchsql(querymap));
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
	
	private String searchsql(Map<String,Object> querymap){
		StringBuilder sb=new StringBuilder();
		if(querymap.containsKey("searchContent")&&StringUtils.isNotBlank(querymap.get("searchContent").toString())){
			String search=querymap.get("searchContent").toString();
			sb.append(" and (");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" dep.DisplayName like '%"+search+"%' or ");
			sb.append(" doc.Duty like '%"+search+"%' or ");
			sb.append(" doc.Profession like '%"+search+"%' or ");
			sb.append(" doc.Speciality like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' ");
			sb.append(")");
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryHospitalNursesByHosId(Integer hosid,
			String search, final Integer start, final Integer length,
			Integer utype) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder(
				"SELECT doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,"
						+ " doc.HospitalId as hosId,doc.DepId as depId,dep.DisplayName as depName,"
						+ " reg.RegisterTime as registerTime,"
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty,reg.LoginName as telphone,doc.Profile as profile,doc.RelatedPics as relatedPics "
						+ " FROM doctor_detail_info doc ");
		sqlBuilder
				.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sqlBuilder
				.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" where 1=1 and doc.HospitalId=" + hosid);
		sqlBuilder.append(" and reg.UserType=" + utype+" and reg.Status=1 ");
		if (StringUtils.isNotBlank(search)) {
			sqlBuilder.append(" and (");
			sqlBuilder.append(" doc.DisplayName like '%" + search + "%' ");
			sqlBuilder.append(" or reg.LoginName like '%" + search + "%' ");
			sqlBuilder.append(" or dep.DisplayName like '%" + search + "%' ");
			sqlBuilder.append(" )");
		}
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
				"SELECT count(*) FROM doctor_detail_info doc");
		countSql.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" where 1=1 and doc.HospitalId=" + hosid);
		countSql.append(" and reg.UserType=" + utype+" and reg.Status=1 ");
		if (StringUtils.isNotBlank(search)) {
			countSql.append(" and (");
			countSql.append(" doc.DisplayName like '%" + search + "%' ");
			countSql.append(" or reg.LoginName like '%" + search + "%' ");
			countSql.append(" or dep.DisplayName like '%" + search + "%' ");
			countSql.append(" )");
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
	public List<MobileSpecial> queryDistributeDocs(Integer hosid, Integer depid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder(
				"SELECT doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,"
						+ " doc.HospitalId as hosId,doc.DepId as depId,dep.DisplayName as depName,"
						+ " reg.RegisterTime as registerTime,"
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty,reg.LoginName as telphone,doc.Profile as profile,doc.RelatedPics as relatedPics "
						+ " FROM doctor_detail_info doc ");
		sqlBuilder
				.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sqlBuilder
				.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder.append(" where 1=1 and doc.HospitalId=" + hosid);
		sqlBuilder.append(" and dep.Id=" + depid + " and reg.UserType=3 and reg.Status=1 ");
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryExpertsBySystem(String search,
			final Integer start, final Integer length,Integer status) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder(
				"SELECT doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,reg.LoginName as telphone,reg.Status,doc.AreaOptimal,"
						+ " doc.HospitalId as hosId,doc.DepId as depId,dep.DisplayName as depName,hos.DisplayName as hosName,"
						+ " reg.RegisterTime as registerTime,doc.Recommend as recommond,"
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty,reg.LoginName as telphone,doc.Profile as profile,doc.RelatedPics as relatedPics "
						+ " FROM doctor_detail_info doc ");
		sqlBuilder
				.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sqlBuilder
				.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		sqlBuilder
				.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sqlBuilder.append(" where 1=1 and reg.UserType=2 and reg.Status="+status);
		if (StringUtils.isNotBlank(search)) {
			sqlBuilder.append(" and (");
			sqlBuilder.append(" doc.DisplayName like '%" + search + "%' ");
			sqlBuilder.append(" or reg.LoginName like '%" + search + "%' ");
			sqlBuilder.append(" or dep.DisplayName like '%" + search + "%' ");
			sqlBuilder.append(" or doc.Duty like '%" + search + "%' ");
			sqlBuilder.append(" or doc.Profession like '%" + search + "%' ");
			sqlBuilder.append(" or hos.DisplayName like '%" + search + "%' ");
			sqlBuilder.append(" )");
		}
		sqlBuilder.append(" order by doc.id desc");
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
				"SELECT count(*) FROM doctor_detail_info doc");
		countSql.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		countSql.append(" left join hospital_department_info dep on doc.DepId=dep.Id ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" where 1=1  and reg.UserType=2 and reg.Status="+status);
		if (StringUtils.isNotBlank(search)) {
			countSql.append(" and (");
			countSql.append(" doc.DisplayName like '%" + search + "%' ");
			countSql.append(" or reg.LoginName like '%" + search + "%' ");
			countSql.append(" or dep.DisplayName like '%" + search + "%' ");
			countSql.append(" or doc.Duty like '%" + search + "%' ");
			countSql.append(" or doc.Profession like '%" + search + "%' ");
			countSql.append(" or hos.DisplayName like '%" + search + "%' ");
			countSql.append(" )");
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
	public UserAccountInfo queryUserAccountInfoByMobileNumber(String tel) {
		// TODO Auto-generated method stub
		String hql="from UserAccountInfo where 1=1 and mobileNumber='"+tel+"' ";
		List<UserAccountInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}


	@SuppressWarnings("unchecked")
	public List<HospitalDetailInfo> queryDocRelativeHospitals(Integer depid) {
		// TODO Auto-generated method stub
		final StringBuilder sb=new StringBuilder("SELECT hos.Id,hos.DisplayName");
		sb.append(" FROM doctor_detail_info doc");
		sb.append(" LEFT JOIN hospital_detail_info hos ON doc.HospitalId=hos.Id");
		sb.append(" LEFT JOIN hospital_department_info dep ON dep.Id=doc.DepId");
		sb.append(" WHERE DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId ");
		sb.append(" IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId="+depid+" AND DepartmentType=1) AND DepartmentType=2) ");
		sb.append(" AND doc.Status=1 and doc.OpenAsk=1 GROUP BY hos.Id");
		List<HospitalDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDetailInfo.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<StandardDepartmentInfo> queryDocRelativeDepparts(Integer depid) {
		// TODO Auto-generated method stub
		final StringBuilder sb=new StringBuilder("SELECT stand.Id,stand.DisplayName");
		sb.append(" FROM dep_standarddep_r dsr");
		sb.append(" LEFT JOIN standard_department_info stand ON dsr.`StandardDepId`=stand.`Id`");
		sb.append(" WHERE DepId="+depid);
		List<StandardDepartmentInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(StandardDepartmentInfo.class));
						return query.list();
					}
				});
		return list;
	}

	
	private String gianquery(Pager pager){
		StringBuilder sb=new StringBuilder();
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("keywords"))){
			String keywords=pager.getQueryBuilder().get("keywords");
			sb.append(" and (");
			sb.append(" doc.DisplayName like '%"+keywords+"%' ");
			sb.append(" or dep.DisplayName like '%"+keywords+"%' ");
			sb.append(" or doc.Speciality like '%"+keywords+"%' ");
			sb.append(" or hos.DisplayName like '%"+keywords+"%' ");
			sb.append(" ) ");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Pager queryMobileSpecial_helporder(Pager pager,Integer type) {
		// TODO Auto-generated method stub
		if (pager == null)
			pager = new Pager();
		final StringBuilder sb = new StringBuilder(
				"SELECT distinct doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,"
						+ " doc.HospitalId as hosId,hos.DisplayName as hosName,doc.DepId as depId,dep.DisplayName as depName ,ds.Amount as vedioAmount, "
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty,ds.Amount as askAmount "
						+ " FROM doctor_detail_info doc ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		sb.append(" where doc.Recommend=1 and doc.Status=1 ");
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("docask"))){
			String docask=pager.getQueryBuilder().get("docask");
			if(docask.equalsIgnoreCase("1")){
				sb.append(" and ds.ServiceId=5 and ds.IsOpen=1 ");
			}else if(docask.equalsIgnoreCase("2")){
				sb.append(" and ds.ServiceId=4 and ds.PackageId=3 and ds.IsOpen=1 ");
			}else if(docask.equalsIgnoreCase("3")){
				//针对医生
				sb.append(" and reg.UserType=3 ");
			}	
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("distCode"))){
			String distCode=pager.getQueryBuilder().get("distCode");
			if(distCode.endsWith("0000")){
				sb.append(" and LEFT(doc.DistCode,2) like '"+distCode.substring(0, 2)+"%' ");
			}else if(distCode.endsWith("00")){
				sb.append(" and LEFT(doc.DistCode,4) like '"+distCode.substring(0, 4)+"%' ");
			}else{
				sb.append(" and doc.DistCode ='"+distCode+"' ");
			}
		}
		String keyquery=gianquery(pager);
		sb.append(keyquery);
		if(type.equals(2)){
			sb.append(" and DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId="
					+ pager.getQueryBuilder().get("depid") + " AND DepartmentType=1) AND DepartmentType=2)");
		}else if(type.equals(1)){
			if(StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))&&!pager.getQueryBuilder().get("hosid").equalsIgnoreCase("-1")){
				sb.append(" and hos.Id="+pager.getQueryBuilder().get("hosid"));
			}
			if(StringUtils.isNotBlank(pager.getQueryBuilder().get("duty"))&&!pager.getQueryBuilder().get("duty").equalsIgnoreCase("-1")){
				sb.append(" and doc.Duty like '%"+pager.getQueryBuilder().get("duty")+"%' ");
			}
			if(StringUtils.isNotBlank(pager.getQueryBuilder().get("standDepId"))&&!pager.getQueryBuilder().get("standDepId").equalsIgnoreCase("-1")){
				sb.append("and doc.DepId in(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId="+pager.getQueryBuilder().get("standDepId")+") ");
			}
		}
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
		final StringBuilder countStr = new StringBuilder(
				"SELECT count(distinct doc.Id)  FROM doctor_detail_info doc ");
		countStr.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countStr.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		countStr.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		countStr.append(" where doc.Recommend=1 and doc.Status=1 ");
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("docask"))){
			countStr.append(" and ds.ServiceId=5 and ds.IsOpen=1 ");
		}else{
			countStr.append(" and ds.ServiceId=4 and ds.PackageId=3 and ds.IsOpen=1 ");
		}
		countStr.append(keyquery);
		if(type.equals(2)){
			countStr.append(" and DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId="
				+ pager.getQueryBuilder().get("depid") + " AND DepartmentType=1) AND DepartmentType=2)");
		}else if(type.equals(1)){
			if(StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))&&!pager.getQueryBuilder().get("hosid").equalsIgnoreCase("-1")){
				countStr.append(" and hos.Id="+pager.getQueryBuilder().get("hosid"));
			}
			if(StringUtils.isNotBlank(pager.getQueryBuilder().get("duty"))&&!pager.getQueryBuilder().get("duty").equalsIgnoreCase("-1")){
				countStr.append(" and doc.Duty like '%"+pager.getQueryBuilder().get("duty")+"%' ");
			}
			if(StringUtils.isNotBlank(pager.getQueryBuilder().get("standDepId"))&&!pager.getQueryBuilder().get("standDepId").equalsIgnoreCase("-1")){
				countStr.append("and doc.DepId in(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId="+pager.getQueryBuilder().get("standDepId")+") ");			}
		}
		String count = ObjectUtils.toString(this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createSQLQuery(countStr.toString()).uniqueResult());
		pager.setTotalCount(Integer.valueOf(count));
		pager.setList(list);
		return pager;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<MobileSpecial> queryMobileSpecialsByLocalDepartId_new(String keywords) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"SELECT distinct doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,"
						+ " doc.HospitalId as hosId,hos.DisplayName as hosName,doc.DepId as depId,dep.DisplayName as depName ,ds.Amount as vedioAmount, "
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty,ds.Amount as askAmount "
						+ " FROM doctor_detail_info doc ");
		sb.append(" left join doctor_register_info reg on reg.Id = doc.Id ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		sb.append(" where 1=1 and doc.Status=1 and ds.ServiceId=4 and ds.PackageId=3 and ds.IsOpen=1  and reg.Status=1 ");
		sb.append(" and (");
		sb.append(" doc.DisplayName like '%"+keywords+"%' ");
		sb.append(" or hos.DisplayName like '%"+keywords+"%' ");
		sb.append(" or dep.DisplayName like '%"+keywords+"%' ");
		sb.append(")");
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
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<MobileSpecial> queryExperts_wx(final Integer pageNo,
			final Integer pageSize, Integer depid, String hosid,String standdepid,String zc,String keywords) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"SELECT distinct doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,"
						+ " doc.HospitalId as hosId,hos.DisplayName as hosName,doc.DepId as depId,dep.DisplayName as depName ,ds.Amount as vedioAmount, "
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty,ds.Amount as askAmount "
						+ " FROM doctor_detail_info doc ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		sb.append(" where doc.Recommend=1 and doc.Status=1 ");
		sb.append(" and ds.ServiceId=4 and ds.PackageId=3 and ds.IsOpen=1 ");
		String keyquery=searchsql(keywords);
		sb.append(keyquery);
		sb.append(" and DepId IN(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId IN(SELECT StandardDepId FROM dep_standarddep_r WHERE DepId="
				+ depid + " AND DepartmentType=1) AND DepartmentType=2) ");
		if(StringUtils.isNotBlank(hosid)){
			sb.append(" and hos.Id="+hosid);
		}
		if(StringUtils.isNotBlank(standdepid)){
			sb.append(" and doc.DepId in(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId="+standdepid+") ");
		}
		if(StringUtils.isNotBlank(zc)){
			sb.append(" and doc.Duty like '%"+zc+"%' ");
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
	private String searchsql(String search){
		StringBuilder sb=new StringBuilder();
		if(StringUtils.isNotBlank(search)){
			sb.append(" and (");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" dep.DisplayName like '%"+search+"%' or ");
			sb.append(" doc.Duty like '%"+search+"%' or ");
			sb.append(" doc.Profession like '%"+search+"%' or ");
			sb.append(" doc.Speciality like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' ");
			sb.append(")");
		}
		return sb.toString();
	}
	/**
	 * 获取医生数据
	 */
	@SuppressWarnings("unchecked")
	public Pager queryMobileSpecial_loaddoc(Pager pager) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"SELECT distinct doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,"
						+ " doc.HospitalId as hosId,hos.DisplayName as hosName,doc.DepId as depId,dep.DisplayName as depName, "
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty "
						+ " FROM doctor_detail_info doc ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" left join doctor_service_info dsi on dsi.DoctorId=doc.Id ");
		sb.append(" where doc.Status=1 and reg.UserType=3 ");
		sb.append(" and hos.IsCooHospital=1 ");
		sb.append(" and (dsi.ServiceId=10 and dsi.IsOpen=1 )");
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("distCode"))){
			String distCode=pager.getQueryBuilder().get("distCode");
			if(distCode.length()==2){
				sb.append(" and substring(hos.DistCode,1,2)='"+distCode+"' ");
			}
			if(distCode.length()==4){
				sb.append(" and substring(hos.DistCode,1,4)='"+distCode+"' ");
			}
			if(distCode.length()==6){
				sb.append(" and hos.DistCode='"+distCode+"' ");
			}
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))&&!pager.getQueryBuilder().get("hosid").equalsIgnoreCase("-1")){
			sb.append(" and doc.HospitalId="+pager.getQueryBuilder().get("hosid"));
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("depId"))&&!pager.getQueryBuilder().get("depId").equalsIgnoreCase("-1")){
			sb.append("and doc.DepId in(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId="+pager.getQueryBuilder().get("depId")+") ");
		}
		String searchquery=querysearch(pager.getQueryBuilder().get("keywords"));
		sb.append(searchquery);
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
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(distinct doc.Id)  FROM doctor_detail_info doc ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		countSql.append(" left join doctor_service_info dsi on dsi.DoctorId=doc.Id ");
		countSql.append(" where doc.Status=1 and reg.UserType=3 ");
		countSql.append(" and (dsi.ServiceId=10 and dsi.IsOpen=1) ");
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("distCode"))){
			String distCode=pager.getQueryBuilder().get("distCode");
			if(distCode.length()==2){
				countSql.append(" and substring(hos.DistCode,1,2)='"+distCode+"' ");
			}
			if(distCode.length()==4){
				countSql.append(" and substring(hos.DistCode,1,4)='"+distCode+"' ");
			}
			if(distCode.length()==6){
				countSql.append(" and hos.DistCode='"+distCode+"' ");
			}
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))&&!pager.getQueryBuilder().get("hosid").equalsIgnoreCase("-1")){
			countSql.append(" and doc.HospitalId="+pager.getQueryBuilder().get("hosid"));
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("depId"))&&!pager.getQueryBuilder().get("depId").equalsIgnoreCase("-1")){
			countSql.append("and doc.DepId in(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId="+pager.getQueryBuilder().get("standDepId")+") ");
		}
		countSql.append(searchquery);
		String count = ObjectUtils.toString(this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createSQLQuery(countSql.toString()).uniqueResult());
		pager.setTotalCount(Integer.valueOf(count));
		pager.setList(list);
		return pager;
	}
	
	
	@SuppressWarnings("unchecked")
	public Pager queryMobileSpecial_loadExOrdoc(Pager pager) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"SELECT distinct doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,"
						+ " doc.HospitalId as hosId,hos.DisplayName as hosName,doc.DepId as depId,dep.DisplayName as depName, "
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty "
						+ " FROM doctor_detail_info doc ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		sb.append(" where doc.Status=1 ");
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("distCode"))){
			String distCode=pager.getQueryBuilder().get("distCode");
			if(distCode.length()==2){
				sb.append(" and substring(hos.DistCode,1,2)='"+distCode+"' ");
			}
			if(distCode.length()==4){
				sb.append(" and substring(hos.DistCode,1,4)='"+distCode+"' ");
			}
			if(distCode.length()==6){
				sb.append(" and hos.DistCode='"+distCode+"' ");
			}
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("dtype"))){
			if(pager.getQueryBuilder().get("dtype").equalsIgnoreCase("4")){
				sb.append(" and ds.ServiceId=4 and ds.IsOpen=1 ");
			}else if(pager.getQueryBuilder().get("dtype").equalsIgnoreCase("5")){
				sb.append(" and ds.ServiceId=5 and ds.IsOpen=1 ");
			}
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))&&!pager.getQueryBuilder().get("hosid").equalsIgnoreCase("-1")){
			sb.append(" and doc.HospitalId="+pager.getQueryBuilder().get("hosid"));
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("duty"))){
			sb.append(" and doc.Duty='"+pager.getQueryBuilder().get("duty")+"' ");
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("depId"))&&!pager.getQueryBuilder().get("depId").equalsIgnoreCase("-1")){
			sb.append(" and doc.DepId in(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId="+pager.getQueryBuilder().get("depId")+") ");
		}
		String searchquery=querysearch(pager.getQueryBuilder().get("keywords"));
		sb.append(searchquery);
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
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(distinct doc.Id)  FROM doctor_detail_info doc ");
		countSql.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		countSql.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		countSql.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		countSql.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		countSql.append(" where doc.Status=1 ");
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("distCode"))){
			String distCode=pager.getQueryBuilder().get("distCode");
			if(distCode.length()==2){
				countSql.append(" and substring(hos.DistCode,1,2)='"+distCode+"' ");
			}
			if(distCode.length()==4){
				countSql.append(" and substring(hos.DistCode,1,4)='"+distCode+"' ");
			}
			if(distCode.length()==6){
				countSql.append(" and hos.DistCode='"+distCode+"' ");
			}
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("dtype"))){
			if(pager.getQueryBuilder().get("dtype").equalsIgnoreCase("4")){
				countSql.append(" and ds.ServiceId=4 and ds.IsOpen=1 ");
			}else if(pager.getQueryBuilder().get("dtype").equalsIgnoreCase("5")){
				countSql.append(" and ds.ServiceId=5 and ds.IsOpen=1 ");
			}
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("hosid"))&&!pager.getQueryBuilder().get("hosid").equalsIgnoreCase("-1")){
			countSql.append(" and doc.HospitalId="+pager.getQueryBuilder().get("hosid"));
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("duty"))){
			countSql.append(" and doc.Duty='"+pager.getQueryBuilder().get("duty")+"' ");
		}
		if(StringUtils.isNotBlank(pager.getQueryBuilder().get("depId"))&&!pager.getQueryBuilder().get("depId").equalsIgnoreCase("-1")){
			countSql.append(" and doc.DepId in(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId="+pager.getQueryBuilder().get("depId")+") ");
		}
		countSql.append(searchquery);
		String count = ObjectUtils.toString(this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createSQLQuery(countSql.toString()).uniqueResult());
		pager.setTotalCount(Integer.valueOf(count));
		pager.setList(list);
		return pager;
	}

	private String querysearch(String search){
		StringBuilder sb=new StringBuilder();
		if(StringUtils.isNotBlank(search)){
			sb.append(" and (");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" doc.Duty like '%"+search+"%' or ");
			sb.append(" doc.Profession like '%"+search+"%' or ");
			sb.append(" doc.Speciality like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' ");
			sb.append(" )");
		}
		return  sb.toString();
	}

	@SuppressWarnings("unchecked")
	public MobileSpecial queryHosAdminMobileSpecialByHosId(Integer hosId) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"SELECT reg.Id as specialId,reg.MobileNumber as mobileTelphone ");
		sb.append(" FROM doctor_detail_info doc");
		sb.append(" left join doctor_register_info reg on doc.Id=reg.Id and reg.UserType=5 ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" where doc.HospitalId="+hosId);
		sb.append(" order by reg.RegisterTime desc ");
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

	@SuppressWarnings("unchecked")
	public List<MobileSpecial> getServiceList(String hosid,String startDate,String endDate,Integer isOpenvedio, Integer isOpentuwen,String depid) {
		// TODO Auto-generated method stub
		final StringBuilder sb=new StringBuilder(" SELECT doc.DisplayName AS specialName,doc.Duty AS duty,doc.Profession AS specialTitle, "
				+ " reg.LoginName AS telphone,dep.DisplayName AS depName,hos.DisplayName AS hosName,reg.RegisterTime AS registerTime,"
				+ " dsi4.IsOpen AS isOpenvedio,dsi5.IsOpen AS isOpentuwen,"
				+ " (CASE WHEN  dsi4.IsOpen=1 AND dsi4.Amount IS NOT NULL THEN dsi4.Amount WHEN dsi4.IsOpen=1 AND dsi4.Amount IS NULL THEN ssi4.ServicePrice WHEN dsi4.IsOpen=0 THEN NULL  END) AS vedioAmount, "
				+ " (CASE WHEN  dsi5.IsOpen=1 AND dsi5.Amount IS NOT NULL THEN dsi5.Amount WHEN dsi5.IsOpen=1 AND dsi5.Amount IS NULL THEN ssi5.ServicePrice WHEN dsi5.IsOpen=0 THEN NULL  END) AS adviceAmount");
		sb.append(" FROM doctor_detail_info doc ");
		sb.append(" LEFT JOIN doctor_register_info reg ON doc.Id=reg.Id ");
		sb.append(" LEFT JOIN hospital_department_info dep ON doc.DepId=dep.Id ");
		sb.append(" LEFT JOIN hospital_detail_info hos ON doc.HospitalId=hos.Id ");
		sb.append(" LEFT JOIN doctor_service_info dsi4 ON dsi4.DoctorId=doc.Id AND dsi4.ServiceId=4 ");
		sb.append(" LEFT JOIN system_service_info ssi4 ON ssi4.Id=dsi4.ServiceId");
		sb.append(" LEFT JOIN doctor_service_info dsi5 ON dsi5.DoctorId=doc.Id AND dsi5.ServiceId=5 ");
		sb.append(" LEFT JOIN system_service_info ssi5 ON ssi5.Id=dsi5.ServiceId ");
		sb.append(" WHERE  reg.UserType=2  " );
		if(StringUtils.isNotBlank(hosid)){
			sb.append(" and hos.Id ='"+hosid+"' ");
		}
		if(StringUtils.isNotBlank(depid)){
			sb.append(" and dep.Id = '"+depid+"' ");
		}
        if(StringUtils.isNotBlank(startDate)){
            sb.append(" and (DATE_FORMAT(reg.RegisterTime,'%Y-%m-%d')>='" + startDate+"')  ");
        }
        if(StringUtils.isNotBlank(endDate)){
            sb.append(" and (DATE_FORMAT(reg.RegisterTime,'%Y-%m-%d')<='" + endDate + "')  ");
        }
		if(isOpenvedio != 0 && isOpentuwen != 0){
			sb.append(" and (dsi4.IsOpen ="+isOpenvedio+" or dsi5.IsOpen ="+isOpentuwen+") or (dsi4.IsOpen ="+isOpenvedio+" and dsi5.IsOpen ="+isOpentuwen+") ");
		}
		if(isOpenvedio !=0 && isOpentuwen ==0){
			sb.append(" and dsi4.IsOpen ="+isOpenvedio );
		}
		if(isOpentuwen !=0 && isOpenvedio ==0){
			sb.append(" and dsi5.IsOpen ="+isOpentuwen );
		}
		sb.append(" GROUP BY doc.Id ORDER BY doc.DisplayName DESC ");
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
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> queryNewAddPatients(List<String> dates,String queryType) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder counthql = new StringBuilder();
		counthql.append(" select count(1) as count ");
		counthql.append(" from user_account_info us ");
		counthql.append(" where 1=1 ");
		String count_mdata=counthql.toString();
		StringBuilder finalcounthql=new StringBuilder();
		if(dates!=null&&dates.size()>0){
			for(String date:dates){
				finalcounthql.append(count_mdata).append("");
				if("1".equalsIgnoreCase(queryType)) {
					finalcounthql.append(" and (DATE_FORMAT(us.RegisterTime,'%Y-%m-%d')='"
							+ date + "' ) union all");
				}else if("2".equalsIgnoreCase(queryType)) {
					finalcounthql.append(" and (DATE_FORMAT(us.RegisterTime,'%Y-%m')='"
							+ date + "' ) union all");
				}
			}
			count_mdata=finalcounthql.substring(0, finalcounthql.length()-9);
		}
		final String cout_sql=count_mdata;
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
		return map;
	}
}
