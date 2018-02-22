package com.tspeiz.modules.common.dao.release2;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.DoctorConsultationOpinion;

public interface IDoctorConsultationOpinionDao extends BaseDao<DoctorConsultationOpinion>{
	public DoctorConsultationOpinion queryDoctorConsultationOpinionByUuid(String uuid);
}
