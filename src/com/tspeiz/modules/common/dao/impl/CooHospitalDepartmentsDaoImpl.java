package com.tspeiz.modules.common.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.ICooHospitalDepartmentsDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.CooHospitalDepartments;

@Repository
public class CooHospitalDepartmentsDaoImpl extends
		BaseDaoImpl<CooHospitalDepartments> implements
		ICooHospitalDepartmentsDao {

	/**
	 * 根据专家级 城市 获取 和专家一致的城市医院科室
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CooHospitalDepartments> queryCooHospitalDepartmentsByHospitalAndExpert(
			Integer expertId, Integer cooHosId) {

		final StringBuilder hqlBuilder = new StringBuilder(
				"select distinct cdep.id,cdep.CooHospitalId,cdep.DisplayName,cdep.status,cdep.IsGood,cdep.describe,cdep.DepartmentMoney,cdep.LiuAnDepartmentId ");
		hqlBuilder
				.append(" ,chos.DisplayName as hosName,chos.Location as location,chos.ContactTelephone as contactTelephone");
		hqlBuilder.append(" from DepToStandardDepR ddr");
		hqlBuilder
				.append("	left join CooHospitalDepartments cdep on cdep.Id = ddr.DepartmentId");
		hqlBuilder
				.append("	left join DepartmentSpecialists sdep on sdep.Department_Id = ddr.StandardDepId");
		hqlBuilder
				.append(" left join CooHospitalDetails chos on chos.Id=cdep.CooHospitalId");
		hqlBuilder.append(" where ddr.DepartmengType = 2 ");// 合作意愿科室 与标准科室对应关系
		hqlBuilder.append(" and cdep.CooHospitalId =" + cooHosId);
		hqlBuilder.append(" and sdep.Specialist_Id =" + expertId);

		System.out.print("=====queryCooHospitalDepartmentsByHospitalAndExpert:"
				+ hqlBuilder.toString());
		List<CooHospitalDepartments> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(CooHospitalDepartments.class));
						return query.list();
					}
				});

		System.out.print("queryCooHospitalDepartmentsByHospitalAndExpert:"
				+ JSONArray.toJSONString(list));
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CooHospitalDepartments> queryCooHospitalDepartmentsByHospital(
			Integer hosid) {
		// TODO Auto-generated method stub
		String hql = " from CooHospitalDepartments where cooHospitalId="
				+ hosid + " and status=1 and  LiuAnDepartmentId is not null";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<MobileSpecial> queryMobileSpecialsByLocalDepartId(Integer depid) {
		// TODO Auto-generated method stub
		final StringBuilder hqlBuilder = new StringBuilder(
				"select distinct us.Id as specialId,us.DisplayName as specialName,sp.Title as specialTitle,sp.Specialty as specialty,sp.HospitalId as hosId,hos.DisplayName as hosName,dep.Id as depId ,");
		hqlBuilder
				.append("dep.DisplayName as depName,sp.ListProfilePicture as listSpecialPicture,sp.OpenAsk as openAsk,sp.OpenAddNum as openAddNum,sp.OpenTel as openTel,sp.OpenEmergency as openEmergency,sp.Rank as rank from Users us");
		hqlBuilder.append(" left join Specialists sp on us.Id=sp.Id ");
		hqlBuilder.append(" left join Hospitals hos on sp.HospitalId=hos.Id ");
		hqlBuilder
				.append(" left join HospitalDepartments dep on sp.HospitalDepartmentId = dep.Id ");
		hqlBuilder
				.append(" left join DepartmentSpecialists sdr on sdr.Specialist_Id = sp.Id ");
		hqlBuilder
				.append(" left join Departments sdep on sdr.Department_Id = sdep.Id ");
		hqlBuilder
				.append(" where us.Id in(select Specialist_Id from DepartmentSpecialists where Department_Id in(select StandardDepId from DepToStandardDepR where DepartmentId="
						+ depid + " and DepartmengType=2)) and UserType=2 and us.Recommend='1' ");
		System.out.println(hqlBuilder.toString());
		List<MobileSpecial> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(hqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(MobileSpecial.class));
						return query.list();
					}
				});

		return list;
	}
}
