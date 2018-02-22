package com.tspeiz.modules.common.dao.newrelease;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.DoctorBillInfo;

public interface IDoctorBillInfoDao extends BaseDao<DoctorBillInfo>{
	public Map<String,Object> querydoctorbilldatas(Map<String,Object> querymap,Integer start,Integer length,Integer docid);

	public DoctorBillInfo querydoctorbill(Integer docid);
}
