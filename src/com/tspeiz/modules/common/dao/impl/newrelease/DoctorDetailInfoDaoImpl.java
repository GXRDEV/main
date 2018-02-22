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

import com.tspeiz.modules.common.bean.HelpBean;
import com.tspeiz.modules.common.bean.dto.DocAboutDatas;
import com.tspeiz.modules.common.bean.dto.DocFollowDto;
import com.tspeiz.modules.common.bean.dto.DoctorAboutCount;
import com.tspeiz.modules.common.bean.dto.DoctorIncomeDto;
import com.tspeiz.modules.common.bean.dto.DoctorsAboutsDto;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IDoctorDetailInfoDao;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;

@Repository
public class DoctorDetailInfoDaoImpl extends BaseDaoImpl<DoctorDetailInfo>
		implements IDoctorDetailInfoDao {

	@SuppressWarnings("unchecked")
	public List<DoctorDetailInfo> queryDoctorDetailsByExpert() {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder();
		sb.append("select det.Id,det.OpenAsk,det.OpenVedio,det.OpenTel,det.VedioAmount,det.AskAmount,TelAmount ");
		sb.append(" from doctor_detail_info det");
		sb.append(" left join doctor_register_info reg on det.Id=reg.Id ");
		sb.append(" where 1=1 and reg.UserType=2 and reg.Status=1 ");
		List<DoctorDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorDetailInfo.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String,Object> queryNewExpertsAdd(List<String> dates) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder counthql = new StringBuilder();
		counthql.append(" select count(1) as count ");
		counthql.append(" from doctor_register_info doc ");
		counthql.append(" where 1=1 and doc.UserType=2");
		counthql.append(" and doc.Status=1 ");
		String count_mdata=counthql.toString();
		StringBuilder finalcounthql=new StringBuilder();
		if(dates!=null&&dates.size()>0){
			for(String date:dates){
				finalcounthql.append(count_mdata).append("");
				finalcounthql.append(" and (DATE_FORMAT(doc.RegisterTime,'%Y-%m')='"
						+ date + "' ) union all");
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
		/*final StringBuilder _counthql=new StringBuilder();
		for(String date:dates){
			_counthql.append(count_mdata).append("");
			_counthql.append(" and (DATE_FORMAT(doc.RegisterTime,'%Y-%m')<='"
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
	public Integer queryNewExpertsAdd_t(String date) {
		// TODO Auto-generated method stub
		final StringBuilder counthql = new StringBuilder();
		counthql.append("select count(1) ");
		counthql.append(" from doctor_register_info doc ");
		counthql.append(" where 1=1 and doc.UserType=2");
		counthql.append(" and doc.Status=1 ");
		counthql.append(" and (DATE_FORMAT(doc.RegisterTime,'%Y-%m')<='"
				+ date + "' ) ");
			Object _effectNumber = this.hibernateTemplate
					.execute(new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException {
							return session.createSQLQuery(counthql.toString())
									.uniqueResult();
						}
					});
		return Integer.parseInt(_effectNumber.toString());
	}
	

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MobileSpecial queryAuditDocDetailById(Integer user) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"SELECT doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,doc.Recommend as recommond,"
						+ " doc.HospitalId as hosId,doc.DepId as depId,doc.DistCode as distCode,doc.Position as position,"
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Sex,doc.Speciality as specialty,reg.LoginName as telphone,reg.MobileNumber as mobileTelphone,doc.Profile as profile,doc.RelatedPics as relatedPics,dic.DisplayName as hosLevel "
						+ ",doc.FamousDoctor as famousDoctor,");
		sb.append("(case when doc.HospitalId is null then doc.RegHospitalName else hos.DisplayName end) as hosName,");
		sb.append("(case when doc.DepId is null then doc.RegDepartmentName else dep.DisplayName end) as depName ");
		sb.append(",doc.BadgeUrl as badgeUrl,doc.IDCardNo as idCardNo ");
		sb.append(" FROM doctor_detail_info doc");
		sb.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" left join dictionary dic on hos.HospitalLevel=dic.Id ");
		sb.append(" where doc.Id=" + user+" and reg.Status in (-1,-2,-3)");
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
	public List<MobileSpecial> queryCooDocsByDep(Integer depid) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"SELECT doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,doc.Recommend as recommond,"
						+ " doc.HospitalId as hosId,doc.DepId as depId,doc.DistCode as distCode,doc.Position as position,"
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty,reg.LoginName as telphone,reg.MobileNumber as mobileTelphone,doc.Profile as profile,doc.RelatedPics as relatedPics,dic.DisplayName as hosLevel "
						+ ",doc.AskAmount as askAmount,doc.FamousDoctor as famousDoctor,");
		sb.append("(case when doc.HospitalId is null then doc.RegHospitalName else hos.DisplayName end) as hosName,");
		sb.append("(case when doc.DepId is null then doc.RegDepartmentName else dep.DisplayName end) as depName ");
		sb.append(",doc.BadgeUrl as badgeUrl ");
		sb.append(" FROM doctor_detail_info doc");
		sb.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" left join dictionary dic on hos.HospitalLevel=dic.Id ");
		sb.append(" where 1=1 and doc.DepId="+depid);
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
	public List<MobileSpecial> querydoctors(String search, String serviceid,
			String depid, String distcode,final Integer pageNo,final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"SELECT DISTINCT doc.Id as specialId,doc.DisplayName as specialName,doc.Duty as duty,doc.Profession as profession,doc.Recommend as recommond,"
						+ " doc.HospitalId as hosId,doc.DepId as depId,doc.DistCode as distCode,doc.Position as position,"
						+ " doc.HeadImageUrl as listSpecialPicture,doc.Speciality as specialty,doc.Profile as profile"
						+ ",doc.FamousDoctor as famousDoctor,");
		sb.append("hos.DisplayName as hosName,");
		sb.append("dep.DisplayName as depName ");
		sb.append(" FROM doctor_detail_info doc");
		sb.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		if(StringUtils.isNotBlank(serviceid)){
			sb.append(" left join doctor_service_info ds on ds.DoctorId=doc.Id ");
		}
		sb.append(" where 1=1 and reg.UserType=3 ");
		if(StringUtils.isNotBlank(serviceid)){
			sb.append(" and ds.serviceId in("+serviceid+") and ds.IsOpen=1 ");
		}
		if(StringUtils.isNotBlank(depid)){
			sb.append(" and doc.DepId in(SELECT DepId FROM dep_standarddep_r WHERE StandardDepId="+depid+")");
		}
		if(StringUtils.isNotBlank(distcode)){
			sb.append(" and doc.DistCode='"+distcode+"' ");
		}
		sb.append(querysearch(search));
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
	private String querysearch(String search){
		StringBuilder sb=new StringBuilder();
		if(StringUtils.isNotBlank(search)){
			sb.append(" and (");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' or ");
			sb.append(" dep.DisplayName like '%"+search+"%' or ");
			sb.append(" doc.Speciality  like '%"+search+"%' ");
			sb.append(" )");
		}
		return sb.toString();
	}
	
	private String gainsearchquery(String search){
		StringBuilder sb=new StringBuilder();
		if(StringUtils.isNotBlank(search)){
			sb.append(" and ( ");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" reg.MobileNumber like '%"+search+"%' or ");
			sb.append(" dic.DistName like '%"+search+"%' ");
			sb.append(" ) ");
		}
		return sb.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> queryoperatordatas(String search,
			Integer ostatus,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder(
				"select DISTINCT doc.Id as specialId,doc.DisplayName as specialName,reg.MobileNumber as mobileTelphone"
				+ ",reg.RegisterTime,dic.DistName as position,doc.InvitCode as invitCode from doctor_detail_info doc ");
		sqlBuilder.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sqlBuilder.append(" left join dict_district_info dic on dic.DistCode=doc.DistCode ");
		sqlBuilder.append(" where 1=1 and reg.UserType="+ostatus);
		sqlBuilder.append(" and reg.Status=1 ");
		sqlBuilder.append(gainsearchquery(search));
		sqlBuilder.append(" order by reg.RegisterTime desc");
		System.out.println(sqlBuilder);
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
				"SELECT count(*) from doctor_detail_info doc  ");
		countSql.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		countSql.append(" left join dict_district_info dic on dic.DistCode=doc.DistCode ");
		countSql.append(" where 1=1 and reg.UserType="+ostatus+" and reg.Status=1 ");
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
	public List<HelpBean> queryOpenPros(String type) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotBlank(type)){
			if(type.equalsIgnoreCase("2")){
				sb.append("SELECT ddi.DistCode,ddi.DistName FROM `dict_district_info` ddi "
						+ "LEFT JOIN (SELECT CONCAT(LEFT(DistCode,2),'0000')dist FROM hospital_detail_info "
						+ "WHERE (IsCooHospital=0 or IsCooHospital is null)) dis ON ddi.`DistCode`=dis.dist "
						+ "WHERE dis.dist IS NOT NULL GROUP BY ddi.`DistCode`");
			}else if(type.equalsIgnoreCase("3")){
				sb.append("SELECT ddi.DistCode,ddi.DistName FROM `dict_district_info` ddi "
						+ "LEFT JOIN (SELECT CONCAT(LEFT(DistCode,2),'0000')dist FROM hospital_detail_info "
						+ "WHERE IsCooHospital=1 AND AuditStatus=1) dis ON ddi.`DistCode`=dis.dist "
						+ "WHERE dis.dist IS NOT NULL GROUP BY ddi.`DistCode`");
			}
		}else{
			sb.append("SELECT ddi.DistCode,ddi.DistName FROM `dict_district_info` ddi ");
			sb.append(" LEFT JOIN (SELECT CONCAT(LEFT(DistCode,2),'0000')dist FROM hospital_detail_info");
			sb.append(" ) dis ON ddi.`DistCode`=dis.dist ");
			sb.append(" WHERE dis.dist IS NOT NULL GROUP BY ddi.`DistCode` ");
		}
		List<HelpBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HelpBean.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<HelpBean> queryOpenCitys(String pdist, String type,
			Integer stype) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder();
		String pcode=pdist.substring(0, 2);
		String _pcode=pdist.substring(0,4);
		if(StringUtils.isNotBlank(type)){
			if(type.equalsIgnoreCase("2")){
				if(stype.equals(1)){
					sb.append("SELECT ddi.DistCode,ddi.DistName  FROM `dict_district_info` ddi "
							+ "LEFT JOIN (SELECT  CONCAT(LEFT(DistCode,4),'00') dist FROM hospital_detail_info "
							+ "WHERE (IsCooHospital=0 or IsCooHospital is null) AND DistCode LIKE '"+pcode+"%') dis ON ddi.DistCode=dis.dist "
							+ "WHERE ddi.DistCode LIKE '%00' AND dis.dist IS NOT NULL GROUP BY ddi.DistCode ");
				}else{
					sb.append("SELECT ddi.DistCode,ddi.DistName  FROM `dict_district_info` ddi "
							+ "LEFT JOIN (SELECT  DistCode dist FROM hospital_detail_info "
							+ "WHERE (IsCooHospital=0 or IsCooHospital is null) AND DistCode LIKE '"+_pcode+"%') dis ON ddi.DistCode=dis.dist "
							+ "WHERE ddi.DistCode LIKE '%00' AND dis.dist IS NOT NULL GROUP BY ddi.DistCode ");
				}
			}else if(type.equalsIgnoreCase("3")){
				if(stype.equals(1)) {
					sb.append("SELECT ddi.DistCode,ddi.DistName  FROM `dict_district_info` ddi "
							+ "LEFT JOIN (SELECT  CONCAT(LEFT(DistCode,4),'00') dist FROM hospital_detail_info "
							+ "WHERE IsCooHospital=1 AND AuditStatus=1 AND DistCode LIKE '" + pcode + "%') dis ON ddi.DistCode=dis.dist "
							+ "WHERE ddi.DistCode LIKE '%00' AND dis.dist IS NOT NULL and ddi.DistCode not in('"+_pcode+"00')GROUP BY ddi.DistCode ");
				}else{
					sb.append("SELECT ddi.DistCode,ddi.DistName  FROM `dict_district_info` ddi " +
							"LEFT JOIN ( " +
							"SELECT  DistCode dist FROM hospital_detail_info " +
							"WHERE (IsCooHospital=1 OR IsCooHospital IS NOT NULL) AND DistCode LIKE '"+_pcode+"%') dis ON ddi.`DistCode`=dis.dist " +
							"WHERE ddi.DistCode LIKE '"+_pcode+"%' AND dis.dist IS NOT NULL and ddi.DistCode not in('"+_pcode+"00') GROUP BY ddi.`DistCode`");
				}
			}
		}else{
			if(stype.equals(1)){
				sb.append("SELECT ddi.DistCode,ddi.DistName  FROM `dict_district_info` ddi "
						+ "LEFT JOIN (SELECT  CONCAT(LEFT(DistCode,4),'00') dist FROM hospital_detail_info "
						+ "WHERE  DistCode LIKE '"+pcode+"%') dis ON ddi.DistCode=dis.dist "
						+ "WHERE ddi.DistCode LIKE '%00' AND dis.dist IS NOT NULL GROUP BY ddi.DistCode ");
			}else{
				sb.append("SELECT ddi.DistCode,ddi.DistName  FROM `dict_district_info` ddi "
						+ "LEFT JOIN (SELECT  DistCode dist FROM hospital_detail_info "
						+ "WHERE AND DistCode LIKE '"+_pcode+"%') dis ON ddi.DistCode=dis.dist "
						+ "WHERE ddi.DistCode LIKE '%00' AND dis.dist IS NOT NULL GROUP BY ddi.DistCode ");
			}
		}
		List<HelpBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HelpBean.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<MobileSpecial> queryDoctorDetailInfoByHosId(Integer hosId) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder(
				"select distinct doc.DisplayName as specialName,doc.Id as specialId,dep.DisplayName as depName ");
		sqlBuilder.append(" from doctor_detail_info doc ");
		sqlBuilder.append(" left join doctor_register_info reg on doc.Id=reg.Id ");
		sqlBuilder.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sqlBuilder.append(" left join doctor_service_info dsi on dsi.DoctorId=doc.Id ");
		sqlBuilder.append(" where 1=1 and doc.HospitalId="+hosId);
		sqlBuilder.append(" and reg.Status=1 and reg.UserType=3 ");
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
	public MobileSpecial getDoctorDetailList(Integer docId) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder();
		sb.append(" SELECT doc.Id AS specialId,doc.DisplayName AS specialName,reg.MobileNumber AS mobileTelphone,doc.Duty AS duty,doc.Profession AS profession,"
				+ " doc.Recommend AS recommond,doc.FamousDoctor AS famousDoctor,doc.Position AS position,doc.AreaOptimal AS areaOptimal,doc.BadgeUrl AS badgeUrl,"
				+ " doc.HeadImageUrl AS listSpecialPicture,doc.Speciality AS specialty,doc.Profile AS profile,doc.IDCardNo AS idCardNo,hos.DisplayName AS hosName,dep.DisplayName AS depName,GROUP_CONCAT(ssi.`ServiceName` SEPARATOR ';')  AS serviceName,"
				+"  GetDistrictNameSheng(ddi.DistCode) AS shengName,GetDistrictNameShi(ddi.DistCode) AS shiName");
		sb.append(" ,doc.IDCardNo as idCardNo from doctor_detail_info doc");
		sb.append(" LEFT JOIN doctor_register_info reg ON doc.Id=reg.Id");
		sb.append("  LEFT JOIN hospital_detail_info hos ON doc.HospitalId=hos.Id ");
		sb.append("  LEFT JOIN hospital_department_info dep ON dep.Id=doc.DepId");
		sb.append("  LEFT JOIN dict_district_info ddi ON doc.DistCode=ddi.DistCode");
		sb.append("  LEFT JOIN doctor_service_info dsi ON dsi.DoctorId= doc.Id");
		sb.append("  LEFT JOIN system_service_info ssi ON dsi.ServiceId=ssi.Id");
		sb.append("  WHERE doc.Id = "+docId+" ");
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
	public DoctorDetailInfo getLocalDocTel(Integer localDoctorId) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder();
		sb.append(" SELECT reg.MobileNumber AS telNumber");
		sb.append(" FROM business_vedio_order vedio");
		sb.append(" LEFT JOIN doctor_detail_info localdoc ON localdoc.Id=vedio.LocalDoctorId ");
		sb.append(" LEFT JOIN  doctor_register_info reg ON reg.id=localdoc.Id");
		sb.append(" WHERE vedio.LocalDoctorId="+localDoctorId+" ");
		List<DoctorDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorDetailInfo.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	private String gainDocSearch(Map<String,Object> queryMap) {
		StringBuilder sb = new StringBuilder();
		if(queryMap.containsKey("search")){
			String search=queryMap.get("search").toString();
			sb.append(" and ( ");
			sb.append(" reg.MobileNumber like '%"+search+"%' or ");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' or ");
			sb.append(" dep.DisplayName like '%"+search+"%' ) ");
		}
		return sb.toString();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Object> queryDoctor(Map<String,Object> queryMap,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sb = new StringBuilder();
		sb.append(" SELECT reg.Id as specialId,doc.DisplayName as specialName,reg.MobileNumber as telphone,hos.DisplayName as hosName,dep.DisplayName as depName,reg.UserType as userType ");
		sb.append(" FROM doctor_register_info reg ");
		sb.append(" LEFT JOIN doctor_detail_info doc on reg.Id = doc.Id ");
		sb.append(" LEFT JOIN hospital_detail_info hos on hos.Id = doc.HospitalId ");
		sb.append(" LEFT JOIN hospital_department_info dep on dep.Id = doc.DepId ");
		sb.append(" WHERE 1=1 AND (reg.UserType =3 or reg.UserType=2) AND reg.Status =1 and (doc.DisplayName is not null and doc.DisplayName !='' )");
		sb.append(gainDocSearch(queryMap));
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append(" SELECT count(reg.Id) ");
		countSql.append(" FROM doctor_register_info reg ");
		countSql.append(" LEFT JOIN doctor_detail_info doc on reg.Id = doc.Id ");
		countSql.append(" LEFT JOIN hospital_detail_info hos on hos.Id = doc.HospitalId ");
		countSql.append(" LEFT JOIN hospital_department_info dep on dep.Id = doc.DepId ");
		countSql.append(" WHERE 1=1 AND (reg.UserType =3 or reg.UserType=2) AND reg.Status =1 and (doc.DisplayName is not null and doc.DisplayName !='' )");
		countSql.append(gainDocSearch(queryMap));
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
	public List<MobileSpecial> gaininvitDocdatas(String invitCode) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder();
		sb.append(" SELECT doc.DisplayName AS specialName,hos.DisplayName AS hosName,dep.DisplayName AS depName ");
		sb.append(" FROM doctor_detail_info doc ");
		sb.append(" LEFT JOIN doctor_register_info reg ON doc.Id=reg.Id AND reg.UserType IN(2,3)");
		sb.append(" LEFT JOIN hospital_detail_info hos ON hos.Id=doc.HospitalId ");
		sb.append(" LEFT JOIN hospital_department_info dep  ON dep.Id=doc.DepId ");
		sb.append(" WHERE doc.InvitCode='"+invitCode+"' AND reg.Status =1 ORDER BY reg.RegisterTime DESC ");
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

	@SuppressWarnings("unchecked")
	public Integer gaininvitcount(String invitCode) {
		// TODO Auto-generated method stub
		final StringBuilder countSql = new StringBuilder();
		countSql.append(" SELECT COUNT(*) FROM doctor_detail_info doc ");
		countSql.append(" LEFT JOIN doctor_register_info reg ON doc.Id=reg.Id AND reg.UserType IN(2,3) ");
		countSql.append(" WHERE doc.InvitCode='"+invitCode+"' AND reg.Status =1 ");
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(countSql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());		
		return num;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> gaininvitDocdatas(String search, final Integer start, final Integer length,
			String invitCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sb = new StringBuilder();
		sb.append(" SELECT doc.Id as specialId,doc.DisplayName AS specialName,hos.DisplayName AS hosName,dep.DisplayName AS depName ");
		sb.append(" FROM doctor_detail_info doc ");
		sb.append(" LEFT JOIN doctor_register_info reg ON doc.Id=reg.Id AND reg.UserType IN(2,3)");
		sb.append(" LEFT JOIN hospital_detail_info hos ON hos.Id=doc.HospitalId ");
		sb.append(" LEFT JOIN hospital_department_info dep  ON dep.Id=doc.DepId ");
		sb.append(" WHERE doc.InvitCode='"+invitCode+"' AND reg.Status =1");
		sb.append(" order by reg.RegisterTime desc");
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
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
				"SELECT COUNT(*) FROM doctor_detail_info doc ");
		countSql.append(" LEFT JOIN doctor_register_info reg ON doc.Id=reg.Id AND reg.UserType IN(2,3) ");
		countSql.append(" WHERE doc.InvitCode='"+invitCode+"' AND reg.Status =1 ");
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
	
    private String gainquery(Map<String,Object> querymap){
        StringBuilder sb=new StringBuilder();
        if(querymap.containsKey("search")){
            sb.append(" and (");
            sb.append(" docreg.LoginName like '%"+querymap.get("search")+"%' or ");
            sb.append(" doc.DisplayName like '%"+querymap.get("search")+"%' or ");
            sb.append(" hos.DisplayName like '%"+querymap.get("search")+"%' or " );
            sb.append(" dep.DisplayName like '%"+querymap.get("search")+"%' " );
            sb.append(" )");
        }
        return sb.toString();
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> aboutdocsdatas(Map<String, Object> querymap, final Integer start, final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sb = new StringBuilder();
        String query=gainquery(querymap);
		sb.append(" SELECT doc.Id as doctorId,doc.DisPlayName AS docName,dep.DisplayName AS depName,"
				+ "(CASE WHEN docreg.LoginName IS NULL THEN docreg.MobileNumber WHEN docreg.LoginName IS NOT NULL THEN docreg.LoginName END) AS tel,hos.DisPlayName AS hosName, ");
		sb.append(" (SELECT COUNT(id) FROM user_attent_doctor attdoc WHERE attdoc.DoctorId=doc.id) AS attentCount, ");
		sb.append(" (SELECT COUNT(id) FROM business_d2p_report_order rep WHERE rep.DoctorId=doc.id) AS reportCount, ");
		sb.append(" (SELECT COUNT(id) FROM business_d2p_tel_order tel WHERE tel.DoctorId=doc.id and tel.PayStatus =1 ) AS telCount, ");
		sb.append(" (SELECT COUNT(id) FROM business_d2p_tuwen_order tuwen WHERE tuwen.DoctorId=doc.id and tuwen.PayStatus =1 ) AS tuwenCount, ");
		sb.append(" (SELECT COUNT(id) FROM business_d_tuwen_order dtuwen WHERE dtuwen.DoctorId=doc.id) AS dtuwenCount, ");
		sb.append(" (SELECT COUNT(id) FROM business_vedio_order vedio WHERE vedio.LocalDoctorId=doc.id) AS vedioCount ");
		sb.append(" from doctor_detail_info doc  ");
		sb.append(" LEFT JOIN doctor_register_info docreg ON doc.id=docreg.id ");
		sb.append(" LEFT JOIN hospital_detail_info hos ON hos.id=doc.HospitalId ");
		sb.append(" LEFT JOIN hospital_department_info dep ON dep.Id=doc.DepId ");
        sb.append(" WHERE  docreg.UserType=3 and docreg.Status=1 ");
        sb.append(query);
        sb.append(" ORDER BY doc.id DESC ");
		List<DocAboutDatas> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(DocAboutDatas.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder(
				"SELECT COUNT(*) FROM doctor_detail_info doc ");
		countSql.append(" LEFT JOIN doctor_register_info docreg ON doc.id=docreg.id ");
		countSql.append(" LEFT JOIN hospital_detail_info hos ON hos.id=doc.HospitalId ");
		countSql.append(" LEFT JOIN hospital_department_info dep ON dep.Id=doc.DepId ");
		countSql.append(" WHERE  docreg.UserType=3 and docreg.Status=1 ");
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

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryNewAddDoctors(List<String> dates,String queryType) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder counthql = new StringBuilder();
		counthql.append(" select count(1) as count ");
		counthql.append(" from doctor_register_info doc ");
		counthql.append(" where 1=1 and doc.UserType=3");
		counthql.append(" and doc.Status=1 ");
		String count_mdata=counthql.toString();
		StringBuilder finalcounthql=new StringBuilder();
		if(dates!=null&&dates.size()>0){
			for(String date:dates){
				finalcounthql.append(count_mdata).append("");
				if("1".equalsIgnoreCase(queryType)) {
					finalcounthql.append(" and (DATE_FORMAT(doc.RegisterTime,'%Y-%m-%d')='"
							+ date + "' ) union all");
				} else if("2".equalsIgnoreCase(queryType)) {
					finalcounthql.append(" and (DATE_FORMAT(doc.RegisterTime,'%Y-%m')='"
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

	@SuppressWarnings("unchecked")
	public List<DoctorAboutCount> gainDocAttentCount(String hosid, String depid, String startDate, String endDate) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append(" SELECT COUNT(attdoc.DoctorId) AS counts,doc.DisPlayName AS docName,hos.DisPlayName AS hosName,dep.DisplayName AS depName ");
		sqlBuilder.append(" FROM user_attent_doctor attdoc ");
		sqlBuilder.append(" LEFT JOIN doctor_detail_info doc  ON doc.id=attdoc.DoctorId");
		sqlBuilder.append(" LEFT JOIN doctor_register_info docreg ON doc.id=docreg.id");
		sqlBuilder.append(" LEFT JOIN hospital_detail_info hos ON hos.id=doc.HospitalId");
		sqlBuilder.append(" LEFT JOIN hospital_department_info dep ON dep.Id=doc.DepId");
		sqlBuilder.append(" WHERE  docreg.UserType=3 AND docreg.Status=1 ");
		if(StringUtils.isNotBlank(startDate)){
			sqlBuilder.append(" and (DATE_FORMAT(attdoc.CreateTime,'%Y-%m-%d') >='"
					+ startDate + "' ) ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlBuilder.append(" and (DATE_FORMAT(attdoc.CreateTime,'%Y-%m-%d') <='"
					+ endDate + "' ) ");
		}
		if(StringUtils.isNotBlank(hosid)){
			sqlBuilder.append(" and hos.Id = '"+hosid+"' ");
		}
		if(StringUtils.isNotBlank(depid)){
			sqlBuilder.append(" and dep.Id = '"+depid+"' ");
		}
		sqlBuilder.append(" GROUP BY attdoc.DoctorId ");
		List<DoctorAboutCount> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorAboutCount.class));
						return query.list();
					}
				});
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DoctorAboutCount> gainDocreportCount(String hosid, String depid, String startDate, String endDate) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append(" SELECT COUNT(rep.DoctorId) AS counts,"
				+ " COUNT(IF(rep.Status=10,TRUE,NULL)) AS waitCount, "
				+ " COUNT(IF(rep.Status=20,TRUE,NULL)) AS receiveCounts, "
				+ " COUNT(IF(rep.Status=30,TRUE,NULL)) AS refuseCounts, "
				+ "doc.DisPlayName AS docName,hos.DisPlayName AS hosName,dep.DisplayName AS depName");
		sqlBuilder.append(" FROM business_d2p_report_order rep  ");
		sqlBuilder.append(" LEFT JOIN doctor_detail_info doc  ON doc.id=rep.DoctorId");
		sqlBuilder.append(" LEFT JOIN doctor_register_info docreg ON doc.id=docreg.id ");
		sqlBuilder.append(" LEFT JOIN hospital_detail_info hos ON hos.id=doc.HospitalId");
		sqlBuilder.append(" LEFT JOIN hospital_department_info dep ON dep.Id=doc.DepId");
		sqlBuilder.append(" WHERE  docreg.UserType=3 AND docreg.Status=1 ");
		if(StringUtils.isNotBlank(startDate)){
			sqlBuilder.append(" and (DATE_FORMAT(rep.CreateTime,'%Y-%m-%d') >='"
					+ startDate + "' ) ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlBuilder.append(" and (DATE_FORMAT(rep.CreateTime,'%Y-%m-%d') <='"
					+ endDate + "' ) ");
		}
		if(StringUtils.isNotBlank(hosid)){
			sqlBuilder.append(" and hos.Id = '"+hosid+"' ");
		}
		if(StringUtils.isNotBlank(depid)){
			sqlBuilder.append(" and dep.Id = '"+depid+"' ");
		}
		sqlBuilder.append(" GROUP BY rep.DoctorId ");
		List<DoctorAboutCount> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorAboutCount.class));
						return query.list();
					}
				});
		return list;
	}
	private String gainSearch(String search) {
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotBlank(search)) {
			sb.append(" and ( ");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" dep.DisplayName like '%"+search+"%' or ");
			sb.append(" hos.DisPlayName like '%"+search+"%' ");
			sb.append(" )");
		}
		return sb.toString();
	}
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> docreportaboutdatass(Integer ostatus, String searchContent, final Integer start,
			final Integer length) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		String now = sdf.format(new Date());
		if(ostatus.equals(1)){
			sqlBuilder.append(" SELECT COUNT(rep.DoctorId) AS counts,COUNT(rep.DoctorId AND IF(DATE_FORMAT(rep.`CreateTime`,'%Y-%m-%d')='"+now+"',TRUE,NULL)) todayCounts,doc.Id as id,doc.DisPlayName AS docName,"
					+ "hos.DisPlayName AS hosName,dep.DisplayName AS depName");
			sqlBuilder.append(" FROM business_d2p_report_order rep  ");
			sqlBuilder.append(" LEFT JOIN doctor_detail_info doc  ON doc.id=rep.DoctorId");
			sqlBuilder.append(" LEFT JOIN doctor_register_info docreg ON doc.id=docreg.id ");
			sqlBuilder.append(" LEFT JOIN hospital_detail_info hos ON hos.id=doc.HospitalId");
			sqlBuilder.append(" LEFT JOIN hospital_department_info dep ON dep.Id=doc.DepId");
			sqlBuilder.append(" WHERE  docreg.UserType=3 AND docreg.Status=1 ");
			sqlBuilder.append(gainSearch(searchContent));
			sqlBuilder.append(" GROUP BY rep.DoctorId ORDER BY todayCounts desc  ");
			List<DoctorsAboutsDto> list = this.hibernateTemplate
					.executeFind(new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session.createSQLQuery(sqlBuilder
									.toString());
							query.setFirstResult(start);
							query.setMaxResults(length);
							query.setResultTransformer(Transformers
									.aliasToBean(DoctorsAboutsDto.class));
							return query.list();
						}
					});
			
			map.put("items", list);
			final StringBuilder countSql=new StringBuilder();
			countSql.append("SELECT COUNT(Distinct rep.DoctorId) AS counts FROM business_d2p_report_order rep ");
			countSql.append(" LEFT JOIN doctor_detail_info doc  ON doc.id=rep.DoctorId");
			countSql.append(" LEFT JOIN doctor_register_info docreg ON doc.id=docreg.id ");
			countSql.append(" LEFT JOIN hospital_detail_info hos ON hos.id=doc.HospitalId");
			countSql.append(" LEFT JOIN hospital_department_info dep ON dep.Id=doc.DepId");
			countSql.append(" WHERE  docreg.UserType=3 AND docreg.Status=1 ");
			countSql.append(gainSearch(searchContent));
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
		}else if(ostatus.equals(2)){
			sqlBuilder.append(" SELECT COUNT(attdoc.DoctorId) AS counts,COUNT(attdoc.DoctorId AND IF(DATE_FORMAT(attdoc.`CreateTime`,'%Y-%m-%d')='"+now+"',TRUE,NULL)) todayCounts,doc.Id as id,doc.DisPlayName AS docName,hos.DisPlayName AS hosName,"
					+ "dep.DisplayName AS depName ");
			sqlBuilder.append(" FROM user_attent_doctor attdoc ");
			sqlBuilder.append(" LEFT JOIN doctor_detail_info doc  ON doc.id=attdoc.DoctorId");
			sqlBuilder.append(" LEFT JOIN doctor_register_info docreg ON doc.id=docreg.id");
			sqlBuilder.append(" LEFT JOIN hospital_detail_info hos ON hos.id=doc.HospitalId");
			sqlBuilder.append(" LEFT JOIN hospital_department_info dep ON dep.Id=doc.DepId");
			sqlBuilder.append(" WHERE  docreg.UserType=3 AND docreg.Status=1 ");
			sqlBuilder.append(gainSearch(searchContent));
			sqlBuilder.append(" GROUP BY attdoc.DoctorId ORDER BY todayCounts desc ");
			List<DoctorsAboutsDto> list = this.hibernateTemplate
					.executeFind(new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session.createSQLQuery(sqlBuilder
									.toString());
							query.setFirstResult(start);
							query.setMaxResults(length);
							query.setResultTransformer(Transformers
									.aliasToBean(DoctorsAboutsDto.class));
							return query.list();
						}
					});
			
			map.put("items", list);
			final StringBuilder countSql=new StringBuilder();
			countSql.append("SELECT COUNT(Distinct attdoc.DoctorId) AS counts FROM user_attent_doctor attdoc ");
			countSql.append(" LEFT JOIN doctor_detail_info doc  ON doc.id=attdoc.DoctorId");
			countSql.append(" LEFT JOIN doctor_register_info docreg ON doc.id=docreg.id ");
			countSql.append(" LEFT JOIN hospital_detail_info hos ON hos.id=doc.HospitalId");
			countSql.append(" LEFT JOIN hospital_department_info dep ON dep.Id=doc.DepId");
			countSql.append(" WHERE  docreg.UserType=3 AND docreg.Status=1 ");
			countSql.append(gainSearch(searchContent));
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
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<MobileSpecial> gainDocregisterexport(String hosid, String depid, String startDate, String endDate) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append(" SELECT doc.DisplayName AS specialName,hos.DisplayName AS hosName,dep.DisplayName AS depName,"
				+ "DATE_FORMAT(docreg.RegisterTime,'%Y-%m-%d') AS registerTimes,docreg.Status AS status");
		sqlBuilder.append(" FROM doctor_register_info docreg  ");
		sqlBuilder.append(" LEFT JOIN doctor_detail_info doc ON doc.Id=docreg.Id");
		sqlBuilder.append(" LEFT JOIN hospital_detail_info hos ON hos.id = doc.HospitalId ");
		sqlBuilder.append(" LEFT JOIN hospital_department_info dep ON dep.Id = doc.DepId");
		sqlBuilder.append(" WHERE docreg.UserType=3");
		if(StringUtils.isNotBlank(startDate)){
			sqlBuilder.append(" and (DATE_FORMAT(docreg.RegisterTime,'%Y-%m-%d') >='"
					+ startDate + "' ) ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlBuilder.append(" and (DATE_FORMAT(docreg.RegisterTime,'%Y-%m-%d') <='"
					+ endDate + "' ) ");
		}
		if(StringUtils.isNotBlank(hosid)){
			sqlBuilder.append(" and hos.Id = '"+hosid+"' ");
		}
		if(StringUtils.isNotBlank(depid)){
			sqlBuilder.append(" and dep.Id = '"+depid+"' ");
		}
		sqlBuilder.append(" order BY docreg.RegisterTime ");
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
	public List<DoctorIncomeDto> docincomeexport(String hid,String depid,String startDate, String endDate) {
		// TODO Auto-generated method stub
        String now = sdf.format(new Date());
		final StringBuilder sb=new StringBuilder();
		sb.append(" SELECT ");
		sb.append(" SUM(CASE WHEN (b.type = 1 AND b.BusinessType = 6 OR b.source LIKE '%图文问诊%') THEN b.ActualMoney ELSE 0 END) AS tw, ");
		sb.append(" SUM(CASE WHEN (b.type = 1 AND b.BusinessType = 9 OR b.source LIKE '%快速问诊%' ) THEN b.ActualMoney ELSE 0 END) AS fasts, ");
		sb.append(" SUM(CASE WHEN (b.type = 1 AND b.BusinessType = 7 OR b.source LIKE '%电话问诊%' ) THEN b.ActualMoney ELSE 0 END) AS tel, ");
		sb.append(" SUM(CASE WHEN (b.type = 1 AND b.BusinessType = 13 OR b.source LIKE '%送心意%' ) THEN b.ActualMoney ELSE 0 END) AS warm, ");
		sb.append(" SUM(CASE WHEN (b.type = 1 AND b.ActualMoney = 10 AND b.source LIKE '%奖励金%' ) THEN b.ActualMoney ELSE 0 END) AS invite, ");
		sb.append(" SUM(CASE WHEN (b.type = 1 AND b.source LIKE '%签到%' ) THEN b.ActualMoney ELSE 0 END) AS qiandao, ");
		sb.append(" SUM(CASE WHEN (b.type = 1  AND IF (DATE_FORMAT(b.CreateTime, '%Y-%m-%d') = '"+now+"',TRUE,NULL)) THEN b.ActualMoney ELSE 0 END) AS todaymoney, ");
		sb.append(" reg.Balance AS summoney,doc.DisplayName AS docName, hos.DisplayName AS hosName,dep.DisplayName as depName,b.DoctorId AS docid ");
		sb.append(" FROM doctor_bill_info b ");
		sb.append(" LEFT JOIN doctor_detail_info doc ON doc.id = b.DoctorId ");
		sb.append(" LEFT JOIN doctor_register_info reg ON reg.id = doc.id ");
		sb.append(" LEFT JOIN hospital_detail_info hos ON hos.id = doc.HospitalId ");
		sb.append(" LEFT JOIN hospital_department_info dep ON dep.id=doc.DepId ");
		sb.append(" WHERE reg.UserType = 3 ");
		if(StringUtils.isNotBlank(hid)){
			sb.append(" AND hos.id='"+hid+"' ");
		}
		if(StringUtils.isNotBlank(depid)){
			sb.append(" AND dep.id='"+depid+"' ");
		}
		if(StringUtils.isNotBlank(startDate)){
			sb.append(" and (DATE_FORMAT(b.CreateTime,'%Y-%m-%d') >='"
					+ startDate + "' ) ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sb.append(" and (DATE_FORMAT(b.CreateTime,'%Y-%m-%d') <='"
					+ endDate + "' ) ");
		}
		sb.append(" GROUP BY b.DoctorId ");
		System.out.println(sb);
		List<DoctorIncomeDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorIncomeDto.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> queryDocFollowDatas(String search,
			final Integer start,final Integer length, Integer type) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		if(type.equals(99)) {
			processCommonMsg(search,start,length,map);
		}else {
			processOrderMsg(search,type,start,length,map);
		}
		return map;
	}
	
	private String gainSearchQuery(String search) {
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotBlank(search)) {
			sb.append(" and ( ");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' or ");
			sb.append(" dep.DisplayName like '%"+search+"%' or ");
			sb.append(" us.ContactName like '%"+search+"%' ");
			sb.append(" )");
		}
		return sb.toString();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processCommonMsg(String search,final Integer start,final Integer length,Map<String,Object> map) {
		final StringBuilder sb = new StringBuilder();
		String query =gainSearchQuery(search);
		sb.append(" SELECT *,count(*) as msgCount FROM ( ");
		sb.append(" SELECT '99' as orderType,doc.`Id` AS docId,doc.`DisplayName` AS docName,hos.`DisplayName` AS docHos,dep.`DisplayName` AS docDep,us.`ContactName` AS userName, ");
		sb.append(" DATE_FORMAT(msg.`SendTime`,'%Y-%m-%d %H:%i:%S') AS msgTime,us.Id as subUserId,");
		sb.append(" (CASE WHEN (msg.MsgType='RC:TxtMsg' or msg.MsgType='RC:WarmMsg') THEN REPLACE(REPLACE(msg.`MsgContent`, CHAR(10), ''), CHAR(13),'')  WHEN msg.MsgType='RC:ImgMsg' THEN '[图片]' WHEN msg.MsgType='RC:VcMsg' THEN '[语音消息]' END ) AS msgContent ");
		sb.append(" FROM business_message_info msg ");
		sb.append(" LEFT JOIN doctor_detail_info doc ON doc.`Id` = (CASE WHEN msg.`SendType`=3 THEN msg.`SendId` WHEN msg.`RecvType`=3 THEN msg.`RecvId` END ) ");
		sb.append(" LEFT JOIN hospital_detail_info hos ON doc.`HospitalId` = hos.`Id` ");
		sb.append(" LEFT JOIN hospital_department_info dep ON doc.`DepId` = dep.`Id` ");
		sb.append(" LEFT JOIN user_contact_info us ON us.`Id` = (CASE WHEN msg.`SendType`=1 THEN msg.`SendId` WHEN msg.`RecvType`=1 THEN msg.`RecvId` END ) ");
		sb.append(" WHERE msg.ChannelType='PERSON' ");
		sb.append(" AND (msg.`SendType`=1 OR msg.`RecvType`=1) ");
		sb.append(query);
		sb.append(" ORDER BY msg.`SendTime` DESC ");
		sb.append(" ) main ");
		sb.append(" WHERE main.docId IS NOT NULL ");
		sb.append(" GROUP BY main.docId,main.subUserId ");
		sb.append(" ORDER BY main.msgTime DESC ");
		List<DocFollowDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(DocFollowDto.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder();
		countSql.append(" SELECT count(*) FROM ( ");
		countSql.append(" SELECT doc.`Id` AS docId,doc.`DisplayName` AS docName,hos.`DisplayName` AS docHos,dep.`DisplayName` AS docDep,us.`ContactName` AS userName, ");
		countSql.append(" DATE_FORMAT(msg.`SendTime`,'%Y-%m-%d %H:%i:%S') AS msgTime,us.Id as subUserId ");
		countSql.append(" FROM business_message_info msg ");
		countSql.append(" LEFT JOIN doctor_detail_info doc ON doc.`Id` = (CASE WHEN msg.`SendType`=3 THEN msg.`SendId` WHEN msg.`RecvType`=3 THEN msg.`RecvId` END ) ");
		countSql.append(" LEFT JOIN hospital_detail_info hos ON doc.`HospitalId` = hos.`Id` ");
		countSql.append(" LEFT JOIN hospital_department_info dep ON doc.`DepId` = dep.`Id` ");
		countSql.append(" LEFT JOIN user_contact_info us ON us.`Id` = (CASE WHEN msg.`SendType`=1 THEN msg.`SendId` WHEN msg.`RecvType`=1 THEN msg.`RecvId` END ) ");
		countSql.append(" WHERE msg.ChannelType='PERSON' ");
		countSql.append(" AND (msg.`SendType`=1 OR msg.`RecvType`=1) and doc.Id is not null");
		countSql.append(query);
		countSql.append(" GROUP BY doc.Id,us.Id ");
		countSql.append(" ) main ");
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
	@SuppressWarnings("unchecked")
	private void processOrderMsg(String search,Integer type,final Integer start,final Integer length,Map<String,Object> map) {
		final StringBuilder sb = new StringBuilder();
		sb.append(" SELECT *,count(*) as msgCount FROM ( ");
		sb.append(" SELECT ");
		String leftTable = "";
		if(type.equals(6)) {
			sb.append(" '6' ");
			leftTable ="business_d2p_tuwen_order";
		}else if(type.equals(7)) {
			sb.append(" '7' ");
			leftTable ="business_d2p_tel_order";
		}else if(type.equals(9)) {
			sb.append(" '9' ");
			leftTable ="business_d2p_fastask_order";
		}else if(type.equals(12)) {
			sb.append(" '12' ");
			leftTable ="business_t2p_tuwen_order";
		}
		sb.append(" as orderType,doc.`DisplayName` AS docName,hos.`DisplayName` AS docHos,dep.`DisplayName` AS docDep, ");
		sb.append(" us.`ContactName` AS userName,DATE_FORMAT(msg.`SendTime`,'%Y-%m-%d %H:%i:%S') AS msgTime, ");
		sb.append(" (CASE WHEN (msg.MsgType='RC:TxtMsg' or msg.MsgType='RC:WarmMsg') THEN REPLACE(REPLACE(msg.`MsgContent`, CHAR(10), ''), CHAR(13),'')  WHEN msg.MsgType='RC:ImgMsg' THEN '[图片]' WHEN msg.MsgType='RC:VcMsg' THEN '[语音消息]' END ) AS msgContent,");
		sb.append(" obean.`UUID` AS orderUuid FROM business_message_info msg ");
		sb.append(" LEFT JOIN "+leftTable+" obean ON msg.`OrderUuid` = obean.`UUID` ");
		sb.append(" LEFT JOIN doctor_detail_info doc ON doc.Id = obean.`DoctorId` ");
		sb.append(" LEFT JOIN hospital_detail_info hos ON doc.`HospitalId` = hos.`Id` ");
		sb.append(" LEFT JOIN hospital_department_info dep ON doc.`DepId` = dep.`Id` ");
		sb.append(" LEFT JOIN user_contact_info us ON obean.`SubUserUuid` = us.`UUID` ");
		sb.append(" WHERE msg.OrderType="+type);
		sb.append(gainSearchQuery(search));
		sb.append(" ORDER BY msg.`SendTime` DESC ");
		sb.append(") main ");
		sb.append(" GROUP BY main.orderUuid ");
		sb.append(" ORDER BY main.msgTime DESC ");
		List<DocFollowDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(DocFollowDto.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder();
		countSql.append(" SELECT count(*) FROM ( ");
		countSql.append(" SELECT doc.`DisplayName` AS docName,hos.`DisplayName` AS hosName,dep.`DisplayName` AS depName, ");
		countSql.append(" us.`ContactName` AS userName,msg.`MsgContent` AS msgContent, ");
		countSql.append(" obean.`UUID` AS orderUuid FROM business_message_info msg ");
		countSql.append(" LEFT JOIN "+leftTable+" obean ON msg.`OrderUuid` = obean.`UUID` ");
		countSql.append(" LEFT JOIN doctor_detail_info doc ON doc.Id = obean.`DoctorId` ");
		countSql.append(" LEFT JOIN hospital_detail_info hos ON doc.`HospitalId` = hos.`Id` ");
		countSql.append(" LEFT JOIN hospital_department_info dep ON doc.`DepId` = dep.`Id` ");
		countSql.append(" LEFT JOIN user_contact_info us ON obean.`SubUserUuid` = us.`UUID` ");
		countSql.append(" WHERE msg.OrderType="+type);
		countSql.append(gainSearchQuery(search));
		countSql.append(" GROUP BY obean.`UUID` ");
		countSql.append(") main ");
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

	@SuppressWarnings("unchecked")
	@Override
	public List<DocFollowDto> queryDocFollowDetailData(Integer type,
			String orderUuid, String docId, String subUserId) {
		// TODO Auto-generated method stub
		if(type.equals(99)) {
			return processCommonMsgDetail(docId,subUserId);
		}else {
			return processOrderMsgDetail(type,orderUuid);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<DocFollowDto> processCommonMsgDetail(String docId,String subUserId) {
		final StringBuilder sb = new StringBuilder();
		sb.append(" SELECT doc.`Id` AS docId,doc.`DisplayName` AS docName,hos.`DisplayName` AS docHos,dep.`DisplayName` AS docDep,us.`ContactName` AS userName, ");
		sb.append(" DATE_FORMAT(msg.`SendTime`,'%Y-%m-%d %H:%i:%S') AS msgTime,us.Id as subUserId,");
		sb.append(" msg.MsgContent AS msgContent,msg.MsgType as msgType,msg.FileUrl as fileUrl,msg.SendType as sendType ");
		sb.append(" FROM business_message_info msg ");
		sb.append(" LEFT JOIN doctor_detail_info doc ON doc.`Id` = (CASE WHEN msg.`SendType`=3 THEN msg.`SendId` WHEN msg.`RecvType`=3 THEN msg.`RecvId` END ) ");
		sb.append(" LEFT JOIN hospital_detail_info hos ON doc.`HospitalId` = hos.`Id` ");
		sb.append(" LEFT JOIN hospital_department_info dep ON doc.`DepId` = dep.`Id` ");
		sb.append(" LEFT JOIN user_contact_info us ON us.`Id` = (CASE WHEN msg.`SendType`=1 THEN msg.`SendId` WHEN msg.`RecvType`=1 THEN msg.`RecvId` END ) ");
		sb.append(" WHERE msg.ChannelType='PERSON' ");
		sb.append(" AND (msg.`SendType`=1 OR msg.`RecvType`=1) ");
		sb.append(" and doc.Id="+docId);
		sb.append(" and us.Id ="+subUserId);
		sb.append(" ORDER BY msg.`SendTime` DESC ");
		List<DocFollowDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DocFollowDto.class));
						return query.list();
					}
				});
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private List<DocFollowDto> processOrderMsgDetail(Integer type,String orderUuid) {
		final StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		String leftTable = "";
		if(type.equals(6)) {
			sb.append(" '6' ");
			leftTable ="business_d2p_tuwen_order";
		}else if(type.equals(7)) {
			sb.append(" '7' ");
			leftTable ="business_d2p_tel_order";
		}else if(type.equals(9)) {
			sb.append(" '9' ");
			leftTable ="business_d2p_fastask_order";
		}else if(type.equals(12)) {
			sb.append(" '12' ");
			leftTable ="business_t2p_tuwen_order";
		}
		sb.append(" as orderType,doc.`DisplayName` AS docName,hos.`DisplayName` AS docHos,dep.`DisplayName` AS docDep, ");
		sb.append(" us.`ContactName` AS userName,DATE_FORMAT(msg.`SendTime`,'%Y-%m-%d %H:%i:%S') AS msgTime,msg.SendType as sendType, ");
		sb.append(" msg.MsgContent AS msgContent,msg.MsgType as msgType,msg.FileUrl as fileUrl,");
		sb.append(" obean.`UUID` AS orderUuid FROM business_message_info msg ");
		sb.append(" LEFT JOIN "+leftTable+" obean ON msg.`OrderUuid` = obean.`UUID` ");
		sb.append(" LEFT JOIN doctor_detail_info doc ON doc.Id = obean.`DoctorId` ");
		sb.append(" LEFT JOIN hospital_detail_info hos ON doc.`HospitalId` = hos.`Id` ");
		sb.append(" LEFT JOIN hospital_department_info dep ON doc.`DepId` = dep.`Id` ");
		sb.append(" LEFT JOIN user_contact_info us ON obean.`SubUserUuid` = us.`UUID` ");
		sb.append(" WHERE msg.OrderType="+type);
		sb.append(" and obean.UUID ='"+orderUuid+"' ");
		sb.append(" ORDER BY msg.`SendTime` DESC ");
		List<DocFollowDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DocFollowDto.class));
						return query.list();
					}
				});
		return list;
	}
    @SuppressWarnings("unchecked")
    public Map<String, Object> userWarmDocDatas(Integer ostatus,String searchContent, final Integer start, final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder = new StringBuilder();
		if (ostatus.equals(1)) {
			sqlBuilder.append(" SELECT doc.DisplayName AS specialName,hos.DisplayName AS hosName,dep.DisplayName AS depName," +
					"  db.ActualMoney AS warmMoney,users.DisplayName AS distName,uac.MobileNumber AS telphone,DATE_FORMAT(uw.CreateTime,'%Y-%m-%d %H:%i:%s') AS registerTimes ");
			sqlBuilder.append(" from doctor_bill_info db ");
			sqlBuilder.append(" LEFT JOIN user_warmth_info uw ON db.BusinessId=uw.UUID AND db.DoctorId=uw.`DoctorId` ");
			sqlBuilder.append(" LEFT JOIN doctor_detail_info doc ON doc.Id = uw.DoctorId    ");
			sqlBuilder.append(" LEFT JOIN hospital_detail_info hos ON hos.Id = doc.HospitalId   ");
			sqlBuilder.append(" LEFT JOIN hospital_department_info dep ON dep.Id = doc.DepId  ");
			sqlBuilder.append(" LEFT JOIN user_weixin_relative users ON users.UserId = uw.UserId  ");
			sqlBuilder.append(" LEFT JOIN user_account_info uac ON uac.Id = users.UserId  ");
			sqlBuilder.append(" WHERE uw.Status=1   ");
			sqlBuilder.append(gainSearch(searchContent));
			sqlBuilder.append(" ORDER BY db.CreateTime DESC ");
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
			final StringBuilder countSql = new StringBuilder();
			countSql.append(" SELECT COUNT(db.Id) as specialId from doctor_bill_info db  ");
			countSql.append(" LEFT JOIN user_warmth_info uw ON db.BusinessId=uw.UUID AND db.DoctorId=uw.`DoctorId` ");
			countSql.append(" LEFT JOIN doctor_detail_info doc ON doc.Id = uw.DoctorId    ");
			countSql.append(" LEFT JOIN hospital_detail_info hos ON hos.Id = doc.HospitalId   ");
			countSql.append(" LEFT JOIN hospital_department_info dep ON dep.Id = doc.DepId  ");
			countSql.append(" LEFT JOIN user_weixin_relative users ON users.UserId = uw.UserId  ");
			countSql.append(" LEFT JOIN user_account_info uac ON uac.Id = users.UserId  ");
			countSql.append(" WHERE uw.Status=1   ");
			countSql.append(gainSearch(searchContent));
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
		} else if (ostatus.equals(2)) {
			sqlBuilder.append(" SELECT doc.DisplayName AS specialName,hos.DisplayName AS hosName,dep.DisplayName AS depName,db.ActualMoney AS warmMoney,DATE_FORMAT(uw.CreateTime,'%Y-%m-%d %H:%i:%s') AS registerTimes ");
			sqlBuilder.append(" FROM doctor_bill_info db  ");
			sqlBuilder.append(" LEFT JOIN user_warmth_info uw ON db.BusinessId=uw.UUID ");
			sqlBuilder.append(" LEFT JOIN doctor_detail_info doc ON doc.Id = db.DoctorId  ");
			sqlBuilder.append(" LEFT JOIN doctor_register_info drg ON drg.Id = doc.Id ");
			sqlBuilder.append(" LEFT JOIN hospital_detail_info hos ON hos.Id = doc.HospitalId  ");
			sqlBuilder.append(" LEFT JOIN hospital_department_info dep ON dep.Id = doc.DepId   ");
			sqlBuilder.append(" WHERE db.type=3 AND uw.Status=1    ");
			sqlBuilder.append(gainSearch(searchContent));
			sqlBuilder.append(" ORDER BY db.CreateTime DESC ");
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
			final StringBuilder countSql = new StringBuilder();
			countSql.append(" SELECT COUNT(db.Id) as specialId from doctor_bill_info db  ");
			countSql.append(" LEFT JOIN user_warmth_info uw ON db.BusinessId=uw.UUID ");
			countSql.append(" LEFT JOIN doctor_detail_info doc ON doc.Id = db.DoctorId  ");
			countSql.append(" LEFT JOIN doctor_register_info drg ON drg.Id = doc.Id ");
			countSql.append(" LEFT JOIN hospital_detail_info hos ON hos.Id = doc.HospitalId  ");
			countSql.append(" LEFT JOIN hospital_department_info dep ON dep.Id = doc.DepId   ");
			countSql.append(" WHERE db.type=3 AND uw.Status=1    ");
			countSql.append(gainSearch(searchContent));
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
		return null;
    }

}
