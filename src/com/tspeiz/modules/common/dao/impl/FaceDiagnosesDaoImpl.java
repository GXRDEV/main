package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IFaceDiagnosesDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.FaceDiagnoses;
@Repository
public class FaceDiagnosesDaoImpl extends BaseDaoImpl<FaceDiagnoses> implements IFaceDiagnosesDao{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer queryCountByPlused(Integer sid, String dtime) {
		// TODO Auto-generated method stub
		final StringBuilder countSql = new StringBuilder(
				"SELECT count(*) from FaceDiagnoses face where Status not in(0) and SpecialistId="+sid+" and CONVERT(varchar(100),DATEADD(S,face.Date + 8 * 3600,'1970-01-01 00:00:00'), 23)='"+dtime+"' ");
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
	public FaceDiagnoses queryFaceDiagnosesByTradeNo(String tradeNo) {
		// TODO Auto-generated method stub
		String hql="from FaceDiagnoses where code='"+tradeNo+"' ";
		List<FaceDiagnoses> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
	
}
