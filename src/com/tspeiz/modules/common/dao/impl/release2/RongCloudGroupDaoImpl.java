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

import com.tspeiz.modules.common.bean.dto.BusinessT2pVipOrderDto;
import com.tspeiz.modules.common.bean.dto.GroupInfoDto;
import com.tspeiz.modules.common.bean.dto.GroupMemberInfoDto;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IRongCloudGroupDao;
import com.tspeiz.modules.common.entity.release2.RongCloudGroup;
import com.tspeiz.modules.common.entity.release2.RongCloudGroupPostRelation;
@Repository
public class RongCloudGroupDaoImpl extends BaseDaoImpl<RongCloudGroup> implements IRongCloudGroupDao{
	private String gainsearchquery(String search){
		StringBuilder sb=new StringBuilder();
		if(StringUtils.isNotBlank(search)){
			sb.append(" and ( ");
			sb.append(" rcg.GroupUuid like '%"+search+"%' or ");
			sb.append(" rcg.GroupName like '%"+search+"%' or ");
			sb.append(" hos.DisplayName like '%"+search+"%' or ");
			sb.append(" dic.DistName like '%"+search+"%' or ");
			sb.append(" sdi.DisplayName like '%"+search+"%' ");
			sb.append(" ) ");
		}
		return sb.toString();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> queryGroups(Integer ostatus,Integer groupType,
			String search,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sql=new StringBuilder();
		if(ostatus.equals(1)){
			sql.append("select rcg.Id as groupId,rcg.GroupUuid as groupUuid,rcg.GroupName as groupName,rcg.Status as status,");
			sql.append("dic.DistName as distName,sdi.DisplayName as depName,hos.DisplayName as hosName,");
			sql.append("doc.DisplayName as creatorName,rcg.CreatorType as creatorType,DATE_FORMAT(rcg.CreateTime,'%Y-%m-%d %T') as createTime ");
			sql.append(" from rong_cloud_group rcg ");
			sql.append(" left join dict_district_info dic on dic.DistCode=rcg.DistCode ");
			sql.append(" left join standard_department_info sdi on sdi.Id=rcg.StandardDepId ");
			sql.append(" left join doctor_detail_info doc on doc.Id=rcg.CreatorId ");
			sql.append(" left join hospital_detail_info hos on hos.Id=rcg.HospitalId ");
			sql.append(" where 1=1 and rcg.Status = 1 and rcg.GroupType="+groupType);
			sql.append(gainsearchquery(search));
			sql.append(" order by rcg.CreateTime desc ");
			System.out.println(sql);
			List<GroupInfoDto> list = this.hibernateTemplate
					.executeFind(new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session.createSQLQuery(sql
									.toString());
							query.setFirstResult(start);
							query.setMaxResults(length);
							query.setResultTransformer(Transformers
									.aliasToBean(GroupInfoDto.class));
							return query.list();
						}
					});
			map.put("items", list);
			final StringBuilder countSql=new StringBuilder();
			countSql.append("select count(rcg.Id) ");
			countSql.append(" from rong_cloud_group rcg ");
			countSql.append(" left join dict_district_info dic on dic.DistCode=rcg.DistCode ");
			countSql.append(" left join standard_department_info sdi on sdi.Id=rcg.StandardDepId ");
			countSql.append(" left join doctor_detail_info doc on doc.Id=rcg.CreatorId ");
			countSql.append(" left join hospital_detail_info hos on hos.Id=rcg.HospitalId ");
			countSql.append(" where 1=1 and rcg.Status = 1 and rcg.GroupType="+groupType);
			countSql.append(gainsearchquery(search));
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
		}else if(ostatus.equals(2)){
			sql.append("select rcg.Id as groupId,rcg.GroupUuid as groupUuid,rcg.GroupName as groupName,rcg.Status as status,");
			sql.append("dic.DistName as distName,sdi.DisplayName as depName,hos.DisplayName as hosName,");
			sql.append("doc.DisplayName as creatorName,rcg.CreatorType as creatorType,DATE_FORMAT(rcg.CreateTime,'%Y-%m-%d %T') as createTime ");
			sql.append(" from rong_cloud_group rcg ");
			sql.append(" left join dict_district_info dic on dic.DistCode=rcg.DistCode ");
			sql.append(" left join standard_department_info sdi on sdi.Id=rcg.StandardDepId ");
			sql.append(" left join doctor_detail_info doc on doc.Id=rcg.CreatorId ");
			sql.append(" left join hospital_detail_info hos on hos.Id=rcg.HospitalId ");
			sql.append(" where 1=1 and rcg.Status = 0 and rcg.GroupType="+groupType);
			sql.append(gainsearchquery(search));
			sql.append(" order by rcg.CreateTime desc ");
			System.out.println(sql);
			List<GroupInfoDto> list = this.hibernateTemplate
					.executeFind(new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session.createSQLQuery(sql
									.toString());
							query.setFirstResult(start);
							query.setMaxResults(length);
							query.setResultTransformer(Transformers
									.aliasToBean(GroupInfoDto.class));
							return query.list();
						}
					});
			map.put("items", list);
			final StringBuilder countSql=new StringBuilder();
			countSql.append("select count(rcg.Id) ");
			countSql.append(" from rong_cloud_group rcg ");
			countSql.append(" left join dict_district_info dic on dic.DistCode=rcg.DistCode ");
			countSql.append(" left join standard_department_info sdi on sdi.Id=rcg.StandardDepId ");
			countSql.append(" left join doctor_detail_info doc on doc.Id=rcg.CreatorId ");
			countSql.append(" left join hospital_detail_info hos on hos.Id=rcg.HospitalId ");
			countSql.append(" where 1=1 and rcg.Status = 0 and rcg.GroupType="+groupType);
			countSql.append(gainsearchquery(search));
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
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> gainGroupMemberDatas(Integer groupId,
			String searchContent,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sql=new StringBuilder();
		sql.append("select rcm.Id as memberId,doc.DisplayName as memberName,doc.Duty as memberDuty,doc.Profession as memberProfession,");
		sql.append("rcm.UserType as memberType,hos.DisplayName as hosName,dep.DisplayName as depName,");
		sql.append(" rcm.Role as role,DATE_FORMAT(rcm.CreateTime,'%Y-%m-%d %T') as createTime,rcm.Status as status ");
		sql.append(" from rong_cloud_group_member rcm ");
		sql.append(" left join rong_cloud_group rcg on rcm.GroupUuid=rcg.GroupUuid ");
		sql.append(" left join doctor_detail_info doc on doc.Id=rcm.UserId ");
		sql.append(" left join hospital_detail_info hos on hos.Id=doc.HospitalId ");
		sql.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sql.append(" where 1=1 and rcg.Id="+groupId);
		sql.append(" order by rcm.CreateTime desc ");
		List<GroupMemberInfoDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(GroupMemberInfoDto.class));
						return query.list();
					}
				});
		
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("select count(rcm.Id) from rong_cloud_group_member rcm ");
		countSql.append(" left join rong_cloud_group rcg on rcm.GroupUuid=rcg.GroupUuid ");
		countSql.append(" where 1=1 and rcg.Id="+groupId);
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
	public RongCloudGroup queryRongCloudGroupByGroupUuid(String groupUuid) {
		// TODO Auto-generated method stub
		String hql ="from RongCloudGroup where 1=1 and groupUuid='"+groupUuid+"' ";
		List<RongCloudGroup> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> gainDocTeamGroupMemberDatas(Integer groupId, String searchContent, final Integer start,
			final Integer length) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sql=new StringBuilder();
		sql.append("select rcm.Id as memberId,doc.DisplayName as memberName,doc.Duty as memberDuty,doc.Profession as memberProfession,");
		sql.append("rcm.UserType as memberType,hos.DisplayName as hosName,dep.DisplayName as depName,");
		sql.append(" dtm.Role AS role,DATE_FORMAT(rcm.CreateTime,'%Y-%m-%d %T') as createTime,rcm.Status as status ");
		sql.append(" from rong_cloud_group_member rcm ");
		sql.append(" left join rong_cloud_group rcg on rcm.GroupUuid=rcg.GroupUuid ");
		sql.append(" left join doctor_detail_info doc on doc.Id=rcm.UserId ");
		sql.append(" LEFT JOIN doctor_team_member dtm ON dtm.DoctorId=rcm.userId ");
		sql.append(" left join hospital_detail_info hos on hos.Id=doc.HospitalId ");
		sql.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");
		sql.append(" where 1=1 and rcg.Id="+groupId);
		sql.append(" order by rcm.CreateTime desc ");
		List<GroupMemberInfoDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(GroupMemberInfoDto.class));
						return query.list();
					}
				});
		
		map.put("items", list);
		final StringBuilder countSql=new StringBuilder();
		countSql.append("select count(rcm.Id) from rong_cloud_group_member rcm ");
		countSql.append(" left join rong_cloud_group rcg on rcm.GroupUuid=rcg.GroupUuid ");
		countSql.append(" where 1=1 and rcg.Id="+groupId);
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
