package com.tspeiz.modules.common.dao;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.SpecialistAppoint;

public interface ISpecialistAppointDao extends BaseDao<SpecialistAppoint>{
	public List<SpecialistAppoint> querySpecialistAppointsBySid(Integer sid,
			String dtime);
}
