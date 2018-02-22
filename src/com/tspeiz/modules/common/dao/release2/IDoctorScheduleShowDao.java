package com.tspeiz.modules.common.dao.release2;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.DoctorScheduleShow;

public interface IDoctorScheduleShowDao extends BaseDao<DoctorScheduleShow>{
	public List<DoctorScheduleShow> queryDoctorScheduleShowsByDoctorId(Integer docid);
	public DoctorScheduleShow isExistDoctorScheduleShowOpen(Integer docid,Integer weekDay,Integer outTime);
}
