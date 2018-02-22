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

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.ISpecialistAppointDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.SpecialistAppoint;

@Repository
public class SpecialistAppointDaoImpl extends BaseDaoImpl<SpecialistAppoint> implements ISpecialistAppointDao{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SpecialistAppoint> querySpecialistAppointsBySid(Integer sid,
			String dtime) {
		// TODO Auto-generated method stub
		final StringBuilder sql = new StringBuilder("select sas.Id,sas.CanMorning,sas.CanAfternoon,sas.CanEvening,sas.Amount as amount,sp.AddNumCount as addCount ,sas.NumberType,sas.WorkLocation,sas.NumberType from SpecialistAppoints sas ");
		sql.append(" join Specialists sp on sas.SpecialistId=sp.Id ");
		sql.append(" where sas.SpecialistId="+sid+" and CONVERT(varchar(100),DATEADD(S,sas.AppointDate + 8 * 3600,'1970-01-01 00:00:00'), 23)='"+dtime+"' ");
		System.out.println(sql.toString());
		List<SpecialistAppoint> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(SpecialistAppoint.class));
						return query.list();
					}
				});
		return list;
	}
}
