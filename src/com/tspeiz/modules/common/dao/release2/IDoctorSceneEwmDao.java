package com.tspeiz.modules.common.dao.release2;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.DoctorSceneEwm;

public interface IDoctorSceneEwmDao extends BaseDao<DoctorSceneEwm>{
	public DoctorSceneEwm queryDoctorSceneEwmByDoctorId(Integer doctorId);
	public List<DoctorSceneEwm> queryDoctorSceneEwms_doc(String hosid,String depid,String startDate,String endDate);
}
