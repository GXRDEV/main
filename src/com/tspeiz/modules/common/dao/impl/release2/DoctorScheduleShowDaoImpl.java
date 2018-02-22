package com.tspeiz.modules.common.dao.impl.release2;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IDoctorScheduleShowDao;
import com.tspeiz.modules.common.entity.release2.DoctorScheduleShow;
@Repository
public class DoctorScheduleShowDaoImpl extends BaseDaoImpl<DoctorScheduleShow> implements IDoctorScheduleShowDao{
	@SuppressWarnings("unchecked")
	public List<DoctorScheduleShow> queryDoctorScheduleShowsByDoctorId(
			Integer docid) {
		// TODO Auto-generated method stub
		String hql="from DoctorScheduleShow where doctorId="+docid+" order by weekDay,outTime ";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public DoctorScheduleShow isExistDoctorScheduleShowOpen(Integer docid,
			Integer weekDay, Integer outTime) {
		// TODO Auto-generated method stub
		String hql="from DoctorScheduleShow where doctorId="+docid+" and weekDay="+weekDay+" and outTime="+outTime+" and status=1 ";
		List<DoctorScheduleShow> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
}
