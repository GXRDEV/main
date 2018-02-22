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
import com.tspeiz.modules.common.dao.release2.IHospitalHealthAllianceMemberDao;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAlliance;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAllianceMember;
@Repository
public class HospitalHealthAllianceMemberDaoImpl extends BaseDaoImpl<HospitalHealthAllianceMember> implements IHospitalHealthAllianceMemberDao{

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<HospitalHealthAllianceMember> queryHospitalHealAllianceMembersByCon(
		String hhaUuid,	Integer level, Integer parentHosId) {
		// TODO Auto-generated method stub
		final StringBuilder sb=new StringBuilder("select hhm.Id,hhm.CreateTime,hhm.HospitalId,hhm.HospitalLevel,"
				+ "hhm.Role,hos.DisplayName as hosName,ddi.DistName as area,dic.DisplayName as levelDesc from hospital_health_alliance_member hhm ");
		sb.append(" left join hospital_detail_info hos on hos.Id=hhm.HospitalId ");
		sb.append(" left join dict_district_info ddi on ddi.DistCode=hos.DistCode ");
		sb.append(" left join dictionary dic on dic.Id=hhm.HospitalLevel ");
		sb.append(" where 1=1 and hhm.Status=1 ");
		sb.append(" and hhm.AllianceUuid='"+hhaUuid+"' ");
		if(level!=null){
			sb.append(" and hhm.hospitalLevel="+level);
		}
		if(parentHosId!=null){
			sb.append(" and hhm.ParentHosId="+parentHosId);
		}
		List<HospitalHealthAllianceMember> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalHealthAllianceMember.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public HospitalHealthAllianceMember queryHospitalHealthAllianceMemberByCon(
			Integer hosId, String allianceUuid) {
		// TODO Auto-generated method stub
		String hql="from HospitalHealthAllianceMember where 1=1 and allianceUuid='"+allianceUuid+"' and hospitalId="+hosId;
		List<HospitalHealthAllianceMember> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	public List<HospitalHealthAllianceMember> queryHospitalHealthAllianceMemberByCon_main(
			String allianceUuid) {
		// TODO Auto-generated method stub
		String hql="from HospitalHealthAllianceMember where 1=1 and allianceUuid='"+allianceUuid+"'";	
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> querymyyltdatas_hos(Integer hosId,
			String search,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sql=new StringBuilder();
		sql.append(" select hha.Id,hha.UUID as uuid,hha.YltName,hm.CreateTime as applicationTime,hha.Status from hospital_health_alliance_member hm ");
		sql.append(" left join hospital_health_alliance hha on hm.AllianceUuid=hha.UUID ");
		sql.append(" where hm.HospitalId="+hosId);
		sql.append(" and (hha.Status=1 or (hha.Status=0 and hha.HospitalId="+hosId+") or (hha.Status=-1 and hha.HospitalId="+hosId+") )  group by hm.AllianceUuid ");
		sql.append(" order by hm.CreateTime desc ");
		List<HospitalHealthAlliance> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalHealthAlliance.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("SELECT count(hm.AllianceUuid) from hospital_health_alliance_member hm ");
		countSql.append(" left join hospital_health_alliance hha on hm.AllianceUuid=hha.UUID ");
		countSql.append(" where hm.HospitalId="+hosId);
		countSql.append(" and (hha.Status=1 or (hha.Status=0 and hha.HospitalId="+hosId+") or (hha.Status=-1 and hha.HospitalId="+hosId+") )  group by hm.AllianceUuid ");
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(countSql.toString())
								.uniqueResult();
					}
				});
		if(effectNumber!=null){
			Integer num = Integer.parseInt(effectNumber.toString());
			map.put("num", num);
		}else{
			map.put("num", 0);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<HospitalDetailInfo> queryHospitalHealthAllianceMemberByHhaId(
			String alianceUuid) {
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder();
		final StringBuilder sql=new StringBuilder();
		sql.append("select hos.Id,hos.DisplayName,dic.DisplayName as hosLevel from hospital_health_alliance_member hm ");
		sql.append(" left join hospital_detail_info hos on hm.HospitalId=hos.Id ");
		sql.append(" left join dictionary dic on dic.Id=hos.HospitalLevel ");
		sql.append(" where hm.AllianceUuid='"+alianceUuid+"' ");
		sql.append(" and hm.Status=1 ");
		List<HospitalDetailInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(HospitalDetailInfo.class));
						return query.list();
					}
				});
		return list;
	}
}
