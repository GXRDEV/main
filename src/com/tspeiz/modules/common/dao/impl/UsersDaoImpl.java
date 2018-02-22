package com.tspeiz.modules.common.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.tspeiz.modules.common.bean.weixin.DepartString;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.IUsersDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.Users;
import com.tspeiz.modules.util.DateUtil;

@Repository
public class UsersDaoImpl extends BaseDaoImpl<Users> implements IUsersDao {

	@SuppressWarnings("unchecked")
	public Users queryUsersByLogin(String username, String password,
			String stype) {
		// TODO Auto-generated method stub
		//Integer utype = Integer.parseInt(stype);
		String hql = "from Users where loginName='" + username
				+ "' and passwordHashed='" + password + "'";
		List<Users> list = this.hibernateTemplate.find(hql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<MobileSpecial> queryMobileSpecialsByConditions(Integer depid,
			String scontent, String scity,String spro,final Integer pageNo, final Integer pageSize,String stype) {
		// TODO Auto-generated method stub
		final StringBuilder hql = new StringBuilder(
				"select distinct us.Id as specialId,us.DisplayName as specialName,sp.Title as specialTitle,sp.Specialty as specialty,sp.HospitalId as hosId,hos.DisplayName as hosName,dep.Id as depId ,"
						+ "dep.DisplayName as depName,sp.ListProfilePicture as listSpecialPicture,sp.OpenAsk as openAsk,sp.OpenAddNum as openAddNum,sp.OpenTel as openTel,sp.OpenEmergency as openEmergency,sp.Rank as rank from Users us");
		hql.append(" left join Specialists sp on us.Id=sp.Id");
		hql.append(" left join Hospitals hos on sp.HospitalId=hos.Id");
		hql.append(" left join HospitalDepartments dep on sp.HospitalDepartmentId = dep.Id");//医院科室
		hql.append(" left join DepartmentSpecialists sdr on sdr.Specialist_Id = sp.Id");//医生所在标准科室关系表
		hql.append(" left join Departments sdep on sdr.Department_Id = sdep.Id");//标准科室
		hql.append(" where us.UserType=2 ");
		if(StringUtils.isNotBlank(stype)&&stype.equalsIgnoreCase("2")){
			//开通了加号的专家
			hql.append(" and sp.OPenAddNum=1 ");
		}
		if (depid != null && !depid.equals(0)) {
			hql.append(" and sdep.id=" + depid);
		}
		hql.append(gainsearch(scontent,scity,spro));
		hql.append(" order by sp.Rank");
		System.out.println(hql.toString());
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hql.toString());
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
	public MobileSpecial queryMobileSpecialById(Integer sid) {
		// TODO Auto-generated method stub
		final StringBuilder hql = new StringBuilder(
				"select us.Id as specialId,us.DisplayName as specialName,sp.Title as specialTitle,sp.Specialty as specialty,sp.HospitalId as hosId,hos.DisplayName as hosName,dep.Id as depId ,"
						+ "dep.DisplayName as depName,sp.ListProfilePicture as listSpecialPicture,sp.OpenAsk as openAsk,"
						+ "sp.OpenAddNum as openAddNum,sp.OpenTel as openTel,sp.OpenEmergency as openEmergency,sp.DetailsProfilePicture as detailsProfilePicture,sp.AddNumCount  from Users us");
		hql.append(" left join Specialists sp on us.Id=sp.Id");
		hql.append(" left join Hospitals hos on sp.HospitalId=hos.Id");
		hql
				.append(" left join DepartmentSpecialists ds on us.Id=ds.Specialist_Id");
		hql.append(" left join Departments dep on ds.Department_Id=dep.Id");
		hql.append(" where us.UserType=2 and us.Id=" + sid);
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hql.toString());
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
	public List<DepartString> queryDepartStrings(Integer sid) {
		// TODO Auto-generated method stub
		final StringBuilder hql = new StringBuilder("select dep.DisplayName as depName from DepartmentSpecialists ds left join Departments dep on ds.Department_Id=dep.Id where Specialist_Id=" + sid);
		List<DepartString> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hql.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DepartString.class));
						return query.list();
					}
				});
		return list;
	}

	private static String gainsearch(String scontent,String scity,String spro) {
		StringBuilder sb = new StringBuilder("");
		if (StringUtils.isNotBlank(scontent)) {
			sb.append(" And ( us.DisplayName like '%" + scontent + "%' ");// 专家姓名
			sb.append(" or sp.Specialty like '%" + scontent + "%' ");// 擅长
			sb.append(" or hos.DisplayName like '%" + scontent + "%' ");// 医院
			sb.append(" or dep.DisplayName like '%" + scontent + "%' ");// 医院科室
			sb.append(" or sdep.DisplayName like '%" + scontent + "%' ");// 标准科室
			sb.append(" ) ");
		}
		if(StringUtils.isNotBlank(scity)&&!scity.equalsIgnoreCase("全部")){
			sb.append(" And ( us.City like '%" + scity + "%' or us.Province like '%"+scity+"%' ");// 城市
			sb.append(" ) ");
		}
		if(StringUtils.isNotBlank(spro)){
			sb.append(" And ( dbo.splitExpertTitleFun(sp.Title) ='" + spro + "' ");// 职称
			sb.append(" ) ");	
		}
		return sb.toString();
	}
	
	/**
	 * 获取医生信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MobileSpecial queryMobileSpecialByDoctorId(Integer doctorId){
		
		final StringBuilder hqlBuilder = new StringBuilder( "select users.Id as specialId,users.DisplayName as specialName,doctors.Title as specialTitle,doctors.Specialty as specialty,doctors.HospitalId as hosId,hospitals.DisplayName as hosName,dep.Id as depId," 
				+ "dep.DisplayName as depName,doctors.ListProfilePicture as listSpecialPicture,doctors.OpenAsk as openAsk," 
				+ "doctors.OpenAddNum as openAddNum,doctors.OpenTel as openTel,doctors.OpenEmergency as openEmergency,doctors.DetailsProfilePicture as detailsProfilePicture");
		hqlBuilder.append(" from Specialists  doctors");
		hqlBuilder.append("	Left join Users  users on users.Id = doctors.Id");
		hqlBuilder.append("	Left join CooHospitalDetails hospitals on  hospitals.Id = doctors.HospitalId");//合作医院
		hqlBuilder.append("	Left join CooHospitalDepartments dep on  dep.Id = doctors.HospitalDepartmentId"); //合作医院科室
		hqlBuilder.append(" where users.UserType = 3");
		hqlBuilder.append(" and users.Id =  " + doctorId);
		
		System.out.print("\r\n=================queryMobileSpecialByDoctorId:" + hqlBuilder.toString());
		
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hqlBuilder.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		
        
		return list != null && list.size() > 0 ? list.get(0) :null;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MobileSpecial queryMobileSpecialByNurseId(Integer nurseId){
		
		final StringBuilder hqlBuilder = new StringBuilder( "select users.Id as specialId,users.DisplayName as specialName,doctors.Title as specialTitle,doctors.Specialty as specialty,doctors.HospitalId as hosId,hospitals.DisplayName as hosName,dep.Id as depId," 
				+ "dep.DisplayName as depName,doctors.ListProfilePicture as listSpecialPicture,doctors.OpenAsk as openAsk," 
				+ "doctors.OpenAddNum as openAddNum,doctors.OpenTel as openTel,doctors.OpenEmergency as openEmergency,doctors.DetailsProfilePicture as detailsProfilePicture");
		hqlBuilder.append(" from Specialists  doctors");
		hqlBuilder.append("	Left join Users  users on users.Id = doctors.Id");
		hqlBuilder.append("	Left join CooHospitalDetails hospitals on  hospitals.Id = doctors.HospitalId");//合作医院
		hqlBuilder.append("	Left join CooHospitalDepartments dep on  dep.Id = doctors.HospitalDepartmentId"); //合作医院科室
		hqlBuilder.append(" where users.UserType = 4");
		hqlBuilder.append(" and users.Id =  " + nurseId);
		
		System.out.print("\r\n=================queryMobileSpecialByDoctorId:" + hqlBuilder.toString());
		
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hqlBuilder.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		
        
		return list != null && list.size() > 0 ? list.get(0) :null;
	}
	/**
	 * 获取专家信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MobileSpecial queryMobileSpecialByExpertId(Integer expertId){
		
		final StringBuilder hqlBuilder = new StringBuilder( "select users.Id as specialId,users.DisplayName as specialName,doctors.Title as specialTitle,doctors.Specialty as specialty,doctors.HospitalId as hosId,hospitals.DisplayName as hosName,dep.Id as depId," 
				+ "dep.DisplayName as depName,doctors.ListProfilePicture as listSpecialPicture,doctors.OpenAsk as openAsk," 
				+ "doctors.OpenAddNum as openAddNum,doctors.OpenTel as openTel,doctors.OpenEmergency as openEmergency,doctors.DetailsProfilePicture as detailsProfilePicture");
		hqlBuilder.append(" from Specialists  doctors");
		hqlBuilder.append("	Left join Users  users on users.Id = doctors.Id");
		hqlBuilder.append("	Left join Hospitals hospitals on  hospitals.Id = doctors.HospitalId");//合作医院
		hqlBuilder.append("	Left join HospitalDepartments dep on  dep.Id = doctors.HospitalDepartmentId"); //合作医院科室
		hqlBuilder.append(" where users.UserType = 2 ");
		hqlBuilder.append(" and users.Id =  " + expertId);
		
		System.out.print("\r\n=================queryMobileSpecialByExpertId:" + hqlBuilder.toString());
		
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hqlBuilder.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		
        
		return list != null && list.size() > 0 ? list.get(0) :null;
	}
	
	/**
	 * 根据当前位置及 选择的专家 获取 本地医生 （专家所在标准科室 与 医生所在标准科室相同）
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<MobileSpecial> queryMobileSpecialsByCurLocationAndExpert(Integer expertId,Integer cooHosId){
		
		final StringBuilder hqlBuilder = new StringBuilder( "select users.Id as specialId,users.DisplayName as specialName,doctors.Title as specialTitle,doctors.Specialty as specialty,doctors.HospitalId as hosId,hospitals.DisplayName as hosName,dep.Id as depId," 
				+ "dep.DisplayName as depName,doctors.ListProfilePicture as listSpecialPicture,doctors.OpenAsk as openAsk," 
				+ "doctors.OpenAddNum as openAddNum,doctors.OpenTel as openTel,doctors.OpenEmergency as openEmergency,doctors.DetailsProfilePicture as detailsProfilePicture");
		hqlBuilder.append(" from Specialists  doctors");
		hqlBuilder.append("	Left join Users  users on users.Id = doctors.Id");
		hqlBuilder.append("	Left join CooHospitalDetails hospitals on  hospitals.Id = doctors.HospitalId");//合作医院
		hqlBuilder.append("	Left join CooHospitalDepartments dep on  dep.Id = doctors.HospitalDepartmentId"); //合作医院科室
		hqlBuilder.append("	left join DepartmentSpecialists ddep  on ddep.Specialist_Id = doctors.Id");     //医生所在标准科室
		hqlBuilder.append("	left join DepartmentSpecialists eDep on eDep.Department_Id = ddep.Department_Id"); //专家所在标准科室
		
		hqlBuilder.append(" where users.UserType = 3 ");
		hqlBuilder.append(" and hospitals.Id="+cooHosId);
		hqlBuilder.append(" and eDep.Specialist_Id =" + expertId );
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hqlBuilder.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		
		System.out.print("queryMobileSpecialsByCurLocationAndExpert:" + JSONArray.toJSONString(list));
		return list;
	}
	@SuppressWarnings("unchecked")
	public Users queryUsersByMobilePhone(String phone) {
		// TODO Auto-generated method stub
		String hql="from Users where mobileNumber='"+phone+"' and userType=1";
		List<Users> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public Users queryUsersByMobileDE(String tel) {
		// TODO Auto-generated method stub
		String hql="from Users where (loginName is not null and loginName!='' and loginName='"+tel+"' ) or ((loginName is null or loginName ='') and mobileNumber='"+tel+"') and userType not in(1) ";
		List<Users> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<MobileSpecial> queryMobileSpecialsByConditions(Integer depid,
			String sdate,String timetype,final Integer pageNo,final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder hqlBuilder = new StringBuilder("select distinct us.Id as specialId,us.DisplayName as specialName,sp.Title as specialTitle,sp.Specialty as specialty,sp.HospitalId as hosId,hos.DisplayName as hosName,dep.Id as depId ,");
		hqlBuilder.append(" dep.DisplayName as depName,sp.ListProfilePicture as listSpecialPicture,sp.OpenAsk as openAsk,sp.OpenAddNum as openAddNum,sp.OpenTel as openTel,sp.OpenEmergency as openEmergency,sp.Rank as rank from Users us ");
		hqlBuilder.append(" left join Specialists sp on us.Id=sp.Id ");
		hqlBuilder.append(" left join Hospitals hos on sp.HospitalId=hos.Id ");
		hqlBuilder.append(" left join HospitalDepartments dep on sp.HospitalDepartmentId = dep.Id ");
		hqlBuilder.append(" left join DepartmentSpecialists sdr on sdr.Specialist_Id = sp.Id ");
		hqlBuilder.append(" left join Departments sdep on sdr.Department_Id = sdep.Id ");
		hqlBuilder.append(" inner join ExpertConsultationSchedule ecs on ecs.ExpertId=sp.Id ");
		hqlBuilder.append(" where us.Id in(select Specialist_Id from DepartmentSpecialists where Department_Id in(select StandardDepId from DepToStandardDepR where DepartmentId="+depid);
		hqlBuilder.append(" and DepartmengType=2)) and UserType=2 ");
		hqlBuilder.append(" and ecs.ScheduleDate='"+sdate+"'");
		hqlBuilder.append(" and ecs.HasAppoint='0' ");
		if(StringUtils.isNotBlank(timetype)&&!timetype.equalsIgnoreCase("0")){
			if(timetype.equalsIgnoreCase("1")){
				//上午
				hqlBuilder.append(" and (ecs.StartTime>='08:00' and ecs.StartTime<='12:00') ");
			}else if(timetype.equalsIgnoreCase("2")){
				//下午
				hqlBuilder.append(" and (ecs.StartTime>='13:00' and ecs.StartTime<='20:00') ");
			}
		}
		System.out.println("===sql=="+hqlBuilder.toString());
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hqlBuilder.toString());
						query.setFirstResult((pageNo - 1) * pageSize);
						query.setMaxResults(pageSize);
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});
		return list;
	}
}
