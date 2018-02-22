package com.tspeiz.modules.common.dao.impl.release2;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IDoctorConsultationOpinionDao;
import com.tspeiz.modules.common.entity.release2.DoctorConsultationOpinion;

@Repository
public class DoctorConsultationOpinionDaoImpl extends BaseDaoImpl<DoctorConsultationOpinion> implements IDoctorConsultationOpinionDao{

	@Override
	public DoctorConsultationOpinion queryDoctorConsultationOpinionByUuid(
			String uuid) {
		// TODO Auto-generated method stub
		String hql="from DoctorConsultationOpinion where 1=1 and uuid='"+uuid+"' ";
		List<DoctorConsultationOpinion> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

}
