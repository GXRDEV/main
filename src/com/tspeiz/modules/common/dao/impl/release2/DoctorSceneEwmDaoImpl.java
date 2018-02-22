package com.tspeiz.modules.common.dao.impl.release2;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IDoctorSceneEwmDao;
import com.tspeiz.modules.common.entity.release2.DoctorSceneEwm;

@Repository
public class DoctorSceneEwmDaoImpl extends BaseDaoImpl<DoctorSceneEwm> implements IDoctorSceneEwmDao{

	@Override
	public DoctorSceneEwm queryDoctorSceneEwmByDoctorId(Integer doctorId) {
		// TODO Auto-generated method stub
		String hql= "from DoctorSceneEwm where 1=1 and doctorId="+doctorId;
		List<DoctorSceneEwm> list = this.hibernateTemplate.find(hql);
		if(list != null && list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DoctorSceneEwm> queryDoctorSceneEwms_doc(String hosid,String depid,String startDate,String endDate) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder(
				"select ewm.DoctorId,ewm.RealUrl,ewm.ErweimaUrl from doctor_scene_ewm ewm ");
		sb.append(" left join doctor_detail_info doc on doc.Id = ewm.DoctorId ");
		sb.append(" left join doctor_register_info reg on doc.Id = reg.Id ");
		sb.append(" left join hospital_detail_info hos on doc.HospitalId = hos.Id ");
		sb.append(" left join hospital_department_info dep on dep.Id = doc.DepId ");
		sb.append(" where ewm.DoctorId is not null and ewm.DoctorId !=-1 ");
		if(StringUtils.isNotBlank(hosid) && StringUtils.isNumeric(hosid)) {
			sb.append(" and doc.HospitalId="+hosid);
		}
		if(StringUtils.isNotBlank(depid) && StringUtils.isNumeric(depid)) {
			sb.append(" and doc.DepId ="+depid);
		}
		if(StringUtils.isNotBlank(startDate)){
			sb.append(" and (DATE_FORMAT(reg.RegisterTime,'%Y-%m-%d') >='"
					+ startDate + "' ) ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sb.append(" and (DATE_FORMAT(reg.RegisterTime,'%Y-%m-%d') <='"
					+ endDate + "' ) ");
		}
		
		List<DoctorSceneEwm> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorSceneEwm.class));
						return query.list();
					}
				});
		return list;
	}
	
}
