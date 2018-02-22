package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IDoctorServiceInfoDao;
import com.tspeiz.modules.common.entity.newrelease.DoctorServiceInfo;

@Repository
public class DoctorServiceInfoDaoImpl extends BaseDaoImpl<DoctorServiceInfo> implements IDoctorServiceInfoDao{

	@SuppressWarnings("unchecked")
	public DoctorServiceInfo queryDoctorServiceInfoByOrderType(Integer orderType,Integer docid) {
		// TODO Auto-generated method stub
		String hql="from DoctorServiceInfo where 1=1 and serviceId="+orderType+" and doctorId="+docid;
		List<DoctorServiceInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}


	@SuppressWarnings("unchecked")
	public DoctorServiceInfo queryDoctorServiceInfoByCon(Integer docid,
			Integer serviceId, Integer packageId) {
		// TODO Auto-generated method stub
		String hql="from DoctorServiceInfo where 1=1 and doctorId="+docid+" and serviceId="+serviceId;
		List<DoctorServiceInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<DoctorServiceInfo> queryOpenDoctorServiceInfo(Integer docid,String serviceids) {
		// TODO Auto-generated method stub
		String hql=" from DoctorServiceInfo where 1=1 and doctorId="+docid+" and isOpen=1 and serviceId in("+serviceids+")";
		List<DoctorServiceInfo> list=this.hibernateTemplate.find(hql);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<DoctorServiceInfo> queryDoctorServiceInfoByDocId(Integer docid) {
		// TODO Auto-generated method stub
		final StringBuilder sb=new StringBuilder("select ds.Id as id,ds.ServiceId as serviceId,ds.IsOpen as isOpen,"
				+ "ss.ServiceName as serviceName,ds.Amount as amount,ds.Description as description,"
				+ "ss.ExpertDivided as expertDivided,ss.DoctorDivided as doctorDivided,ss.PlatformDivided as platformDivided from  doctor_service_info ds ");
		sb.append(" left join system_service_info ss on ds.ServiceId=ss.Id ");
		sb.append(" where 1=1 and ds.DoctorId="+docid);
		List<DoctorServiceInfo> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(DoctorServiceInfo.class));
						return query.list();
					}
				});
		return list;
	}

	
}
