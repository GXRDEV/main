package com.tspeiz.modules.common.dao.impl.release2;

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

import com.tspeiz.modules.common.bean.D2pOrderBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IDoctorTeamDao;
import com.tspeiz.modules.common.entity.release2.DoctorTeam;
import com.tspeiz.modules.common.entity.release2.DoctorTeamMember;

@Repository
public class DoctorTeamDaoImpl extends BaseDaoImpl<DoctorTeam> implements IDoctorTeamDao{
	private String gainsearchquery(Map<String,Object> querymap){
		StringBuilder sb=new StringBuilder();
		if(querymap.containsKey("search")){
			String search=querymap.get("search").toString();
			sb.append(" and ( ");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" dt.TeamName like '%"+search+"%' ");
			sb.append(" ) ");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryauditdocteamdatas(
			Map<String, Object> querymap,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select dt.Id,dt.ApplicationTime,doc.DisplayName as applicant,dt.ApplicantType,dt.TeamName,dt.Keywords from doctor_team dt ");
		sqlBuilder.append(" left join doctor_register_info reg on dt.ApplicantId=reg.Id ");
		sqlBuilder.append(" left join doctor_detail_info doc on reg.Id=doc.Id ");
		sqlBuilder.append(" where 1=1 ");
		Integer ostatus=Integer.parseInt(querymap.get("ostatus").toString());
		if(ostatus.equals(0)){
			sqlBuilder.append(" and (dt.Status=0 or dt.Status is null) ");
		}else if(ostatus.equals(-1)){
			sqlBuilder.append(" and dt.Status=-1 ");
		}
		sqlBuilder.append(gainsearchquery(querymap));
		sqlBuilder.append(" order by dt.Id desc ");
		System.out.println("=="+sqlBuilder.toString());
		List<DoctorTeam> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorTeam.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("select count(dt.Id) from doctor_team dt ");
		countSql.append(" left join doctor_register_info reg on dt.ApplicantId=reg.Id ");
		countSql.append(" left join doctor_detail_info doc on reg.Id=doc.Id ");
		countSql.append(" where 1=1 ");
		if(ostatus.equals(0)){
			countSql.append(" and (dt.Status=0 or dt.Status is null) ");
		}else if(ostatus.equals(-1)){
			countSql.append(" and dt.Status=-1 ");
		}
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
	
	private String gainsearchquery_pass(Map<String, Object> querymap){
		StringBuilder sb=new StringBuilder();
		if(querymap.containsKey("search")){
			String search=querymap.get("search").toString();
			sb.append(" and ( ");
			sb.append(" dt.TeamName like '%"+search+"%' or ");
			sb.append(" doc.DisplayName like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' or ");
			sb.append(" dep.DisplayName like '%"+search+"%' ");
			sb.append(" ) ");
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> querydocteamdatas(Map<String, Object> querymap,
			final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select distinct dt.Id,dt.AuditTime,dt.TeamName,dt.Keywords,dt.AreaOptimal,doc.DisplayName as docName,hos.DisplayName as hosName,"
				+ "dep.DisplayName as depName,doc.Duty as docDuty,doc.Profession as docProfession,tnum.num as memNum,dt.IsTest as isTest from doctor_team dt ");
		//sqlBuilder.append(" left join doctor_team_member dtm on dt.UUID=dtm.TeamUuid ");
		//sqlBuilder.append(" left join dictionary dic on dtm.Role=dic.DisplayValue and dic.ParentId=49 ");
		sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=dt.ApplicantId ");
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=doc.HospitalId ");
		sqlBuilder.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sqlBuilder.append(" left join (SELECT COUNT(*) num,teamUuid FROM doctor_team_member GROUP BY teamUuid) tnum on tnum.teamUuid=dt.UUID ");
		sqlBuilder.append(" where 1=1 and dt.Status=1 and dt.DeFlag=0 ");
		//sqlBuilder.append(" and dtm.Role=1 ");
		sqlBuilder.append(gainsearchquery_pass(querymap));
		sqlBuilder.append(" order by dt.applicationTime desc");
		List<DoctorTeam> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorTeam.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("select count(distinct dt.Id) from doctor_team dt ");
		//countSql.append(" left join doctor_team_member dtm on dt.UUID=dtm.TeamUuid ");
		//countSql.append(" left join dictionary dic on dtm.Role=dic.DisplayValue and dic.ParentId=49 ");
		countSql.append(" left join doctor_detail_info doc on doc.Id=dt.ApplicantId ");
		countSql.append(" left join hospital_detail_info hos on hos.Id=doc.HospitalId ");
		countSql.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		countSql.append(" where dt.Status=1 ");
		//countSql.append(" and dtm.Role=1 ");
		countSql.append(gainsearchquery_pass(querymap));
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
	public DoctorTeam queryDoctorTeamByUuid(String uuid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select dt.Id,dt.TeamName,doc.DisplayName as docName,hos.DisplayName as hosName,"
				+ "dep.DisplayName as depName from doctor_team dt ");
		sqlBuilder.append(" left join doctor_team_member dtm on dt.UUID=dtm.TeamUuid ");
		sqlBuilder.append(" left join dictionary dic on dtm.Role=dic.DisplayValue and dic.ParentId=49 ");
		sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=dtm.DoctorId ");
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=doc.HospitalId ");
		sqlBuilder.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sqlBuilder.append(" where 1=1 and dt.Status=1 ");
		sqlBuilder.append(" and dtm.Role=1 ");
		List<DoctorTeam> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorTeam.class));
						return query.list();
					}
				});
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DoctorTeam queryDoctorTeamById_detail(Integer tid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select dt.Id,dt.TeamName,doc.DisplayName as docName,hos.DisplayName as hosName,doc.Duty as docDuty,doc.Profession as docProfession,"
				+ "dep.DisplayName as depName,dt.ApplicantType,dt.Profile,dt.Speciality,dt.LogoUrl,dic.DisplayName as hosLevel from doctor_team dt");
		sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=dt.ApplicantId ");
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=doc.HospitalId ");
		sqlBuilder.append(" left join dictionary dic on dic.Id=hos.HospitalLevel ");
		sqlBuilder.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sqlBuilder.append(" where 1=1 ");
		sqlBuilder.append(" and dt.Id="+tid);
		List<DoctorTeam> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorTeam.class));
						return query.list();
					}
				});
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryDoctorTeamMemberDatas(Integer teamId,
			String searchContent,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sb=new StringBuilder();
		sb.append("select dtm.Id,doc.DisplayName as docName,hos.DisplayName as hosName,dep.DisplayName as depName,"
				+ "doc.Duty as docDuty,doc.Profession as docProfession,dtm.Role,dtm.CreateTime from doctor_team_member dtm ");
		sb.append(" left join doctor_team dt on dtm.TeamUuid=dt.UUID ");
		sb.append(" left join doctor_detail_info doc on doc.Id=dtm.DoctorId ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId=hos.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sb.append(" where 1=1 and dt.Id="+teamId);
		sb.append(" order by dtm.CreateTime desc");
		List<DoctorTeamMember> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorTeamMember.class));
						return query.list();
					}
				});
		map.put("items",list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("select count(dtm.Id) from doctor_team_member dtm ");
		countSql.append(" left join doctor_team dt on dtm.TeamUuid=dt.UUID ");
		countSql.append(" where 1=1 and dt.Id="+teamId);
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
	public List<DoctorTeamMember> queryDoctorTeamMembersByTeamId(Integer teamId) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select dtm.DoctorId,dtm.DoctorType,dtm.Role  ");
		sqlBuilder.append(" from doctor_team_member dtm ");
		sqlBuilder.append(" left join doctor_team dt on dt.UUID=dtm.TeamUuid ");
		sqlBuilder.append(" where 1=1 and dt.Id="+teamId);
		List<DoctorTeamMember> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorTeamMember.class));
						return query.list();
					}
				});
		return list;
	}
	@SuppressWarnings("unchecked")
	public DoctorTeam queryDoctorTeamByuuid(String groupUuid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT dt.Id,dt.ApplicantId,dt.TeamName ");
		sqlBuilder.append(" FROM doctor_team dt ");
		sqlBuilder.append(" where dt.UUID='"+groupUuid+"' ");
		List<DoctorTeam> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorTeam.class));
						return query.list();
					}
				});
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	@SuppressWarnings("unchecked")
	public void delDocTeamRequest(String groupUuid) {
		// TODO Auto-generated method stub
		final String hql=" delete from DoctorTeamRequest where TeamUuid ='"+groupUuid+"' ";
		this.hibernateTemplate
		.execute(new HibernateCallback() {
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				return query.executeUpdate();
			}
		});
		
	}
	
	@SuppressWarnings("unchecked")
	public List<DoctorTeam> queryDoctorTeamHasNoErweima() {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT dt.Id,dt.TeamName ");
		sqlBuilder.append(" FROM doctor_team dt ");
		//sqlBuilder.append(" where (dt.ErweimaUrl is null or dt.ErweimaUrl ='') ");
		List<DoctorTeam> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorTeam.class));
						return query.list();
					}
				});
		return list;
	}
	
	
}
