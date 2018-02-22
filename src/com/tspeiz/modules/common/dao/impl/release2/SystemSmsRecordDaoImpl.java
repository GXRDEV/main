package com.tspeiz.modules.common.dao.impl.release2;

import java.sql.SQLException;
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

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.ISystemSmsRecordDao;
import com.tspeiz.modules.common.entity.release2.SystemSmsRecord;

@Repository
public class SystemSmsRecordDaoImpl extends BaseDaoImpl<SystemSmsRecord> implements ISystemSmsRecordDao{

	private String gainquery(String search) {
		if(StringUtils.isNotBlank(search)) {
			return " and sms.Telphone like '%"+search+"%' ";
		}
		return "";
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> querySystemSmsRecordDatas(String search,
			Integer status,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sb = new StringBuilder();
        String query=gainquery(search);
		sb.append(" select sms.Id,sms.Telphone,sms.Code,sms.SendTime,sms.UpdateTime,sms.Status,sms.Remark  from system_sms_record sms");
		sb.append(" where 1=1 and sms.Source="+status);
        sb.append(query);
        sb.append(" order by sms.SendTime desc");
		List<SystemSmsRecord> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(SystemSmsRecord.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql = new StringBuilder("");
		countSql.append(" select count(sms.Id) from system_sms_record sms");
		countSql.append(" where 1=1 and sms.Source="+status);
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

}
