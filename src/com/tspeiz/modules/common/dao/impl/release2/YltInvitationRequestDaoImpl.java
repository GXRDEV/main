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

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IYltInvitationRequestDao;
import com.tspeiz.modules.common.entity.release2.BusinessT2pTuwenOrder;
import com.tspeiz.modules.common.entity.release2.YltInvitationRequest;

@Repository
public class YltInvitationRequestDaoImpl extends BaseDaoImpl<YltInvitationRequest> implements IYltInvitationRequestDao{

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryinvitjoin_hos(Integer hosId, String search,
			final Integer start,final Integer length,Integer otype) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select yr.Id,yr.InvitationTime,hha.YltName as yltName,dic.DistName as position,"
				+ "hos_hm.DisplayName as coreHosName,hos.DisplayName as hosName,hm_dt.DisplayName as coreHosLevel,dt.DisplayName as hosLevel,"
				+ "ohos.DisplayName as otherHosName,odt.DisplayName as otherHosLevel,yr.AuditTime,yr.Status from ylt_invitation_request yr ");
		sqlBuilder.append(" left join hospital_health_alliance hha on yr.AllianceUuid=hha.UUID ");
		sqlBuilder.append(" left join hospital_health_alliance_member hm on hha.UUID=hm.AllianceUuid and hm.Role=1 ");
		sqlBuilder.append(" left join hospital_detail_info hos_hm on hm.HospitalId=hos_hm.Id ");
		sqlBuilder.append(" left join dictionary hm_dt on hm_dt.Id=hos_hm.HospitalLevel ");
		sqlBuilder.append(" left join dict_district_info dic on hos_hm.DistCode=dic.DistCode ");
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=yr.HospitalId ");
		sqlBuilder.append(" left join dictionary dt on dt.Id=hos.HospitalLevel ");
		sqlBuilder.append(" left join hospital_detail_info ohos on ohos.Id=yr.ParentHosId ");
		sqlBuilder.append(" left join dictionary odt on odt.Id=ohos.HospitalLevel ");
		sqlBuilder.append(" where 1=1 ");
		if(otype.equals(1)){
			//我发起的邀请
			sqlBuilder.append(" and yr.ParentHosId="+hosId);
		}else if(otype.equals(2)){
			//我收到的邀请
			sqlBuilder.append(" and yr.HospitalId="+hosId);
		}
		List<YltInvitationRequest> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(YltInvitationRequest.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("select count(yr.Id) from ylt_invitation_request yr where 1=1 ");
		if(otype.equals(1)){
			//我发起的邀请
			countSql.append(" and yr.ParentHosId="+hosId);
		}else if(otype.equals(2)){
			//我收到的邀请
			countSql.append(" and yr.HospitalId="+hosId);
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
}
