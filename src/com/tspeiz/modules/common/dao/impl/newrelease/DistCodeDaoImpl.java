package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IDistCodeDao;
import com.tspeiz.modules.common.entity.newrelease.DistCode;
import com.tspeiz.modules.common.entity.newrelease.HospitalDepartmentInfo;

@Repository
public class DistCodeDaoImpl extends BaseDaoImpl<DistCode> implements
		IDistCodeDao {

	@SuppressWarnings("unchecked")
	public List<DistCode> queryDistCodesByConditions(String stype,
			String procost) {
		// TODO Auto-generated method stub
		String hql = "";
		if (stype.equalsIgnoreCase("1")) {
			// 获取省份
			hql = "from DistCode where 1=1 and distCode like '__0000'";
		}else if(stype.equalsIgnoreCase("2")){
			//获取市区
			String str="11,12,31,50";
			if(str.contains(procost)){
				hql="from DistCode where 1=1 and distCode like '"+procost+"__00' and distCode not in ('"+procost+"0000')";
			}else{
				hql="from DistCode where 1=1 and distCode like '"+procost+"__00' and distCode not in ('"+procost+"0000')";
			}
		}else if(stype.equalsIgnoreCase("3")){
			hql="from DistCode where 1=1 and distCode like '"+procost+"%' and distCode not in ('"+procost+"00')";
		}
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public DistCode queryDistCodeByCode(String code) {
		// TODO Auto-generated method stub
		String hql="from DistCode where 1=1 and distCode ='"+code+"' ";
		List<DistCode> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<DistCode> gainArea(String type, String parentCode) {
		// TODO Auto-generated method stub
		String hql="";
		if(type.equalsIgnoreCase("0")){
			hql = "from DistCode ";
		}else if(type.equalsIgnoreCase("1")){
			//省
			hql = "from DistCode where 1=1 and distCode like '__0000'";
		}else if(type.equalsIgnoreCase("2")){
			//市
			String pcode=parentCode.substring(0, 2);
			hql=" from DistCode where 1=1 and distCode like '"+pcode+"__00"+"' and distCode not like '"+pcode+"0000' ";
		}else if(type.equalsIgnoreCase("3")){
			//区
			String ccode=parentCode.substring(0, 4);
			hql="from DistCode where 1=1 and distCode like '"+ccode+"__"+"' and distCode not like '"+ccode+"00' ";
		}
		return this.hibernateTemplate.find(hql);
	}
	/**
	 * 获取医联体区域
	 */
	@SuppressWarnings("unchecked")
	public List<DistCode> queryAllianceAreas() {
		// TODO Auto-generated method stub
		final StringBuilder sb=new StringBuilder();
		sb.append("select DISTINCT dic.DistCode,dic.DistName ");
		sb.append(" from hospital_health_alliance alliance ");
		sb.append(" left join hospital_detail_info hos on hos.Id=alliance.HospitalId ");
		sb.append(" left join dict_district_info dic on dic.DistCode=hos.DistCode ");
		sb.append(" where 1=1 and alliance.Status=1 ");
		List<DistCode> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DistCode.class));
						return query.list();
					}
				});
		return list;
	}
}
