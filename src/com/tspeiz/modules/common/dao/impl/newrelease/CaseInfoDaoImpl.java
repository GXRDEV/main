package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.ICaseInfoDao;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;

@Repository
public class CaseInfoDaoImpl extends BaseDaoImpl<CaseInfo> implements ICaseInfoDao{

	@SuppressWarnings("unchecked")
	public List<CaseInfo> queryHisCaseInfosByUId(Integer uid) {
		// TODO Auto-generated method stub
		String hql="from CaseInfo where 1=1 and userId="+uid+" order by createTime desc";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CaseInfo queryCaseInfoById_new(Integer caseid) {
		// TODO Auto-generated method stub
		final StringBuilder sb = new StringBuilder("select uci.ContactName,uci.Telephone as telphone,uci.Sex,uci.Age"
				+ ",uca.AskProblem,uca.Uuid from "
				+ "user_case_info uca ");
		sb.append(" left join user_contact_info uci on uca.SubUserUuid=uci.UUID ");
		sb.append(" where uca.Id="+caseid);
		List<CaseInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(CaseInfo.class));
						return query.list();
					}
				});
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CaseInfo queryCaseInfoByUuid(String caseUuid) {
		// TODO Auto-generated method stub
		String hql="from CaseInfo where 1=1 and Uuid='"+caseUuid+"' ";
		List<CaseInfo> list = this.hibernateTemplate.find(hql);
		if(list !=null && !list.isEmpty()) return list.get(0);
		return null;
	}

	@Override
	public List<CaseInfo> queryCasesTransToAttachments() {
		// TODO Auto-generated method stub
		String hql="from CaseInfo where 1=1 and ((normalImages is not null and normalImages !='') or (checkReportImages is not null and checkReportImages !='') "
				+ "or (radiographFilmImags is not null and radiographFilmImags !='') )";
		return this.hibernateTemplate.find(hql);
	}
}
