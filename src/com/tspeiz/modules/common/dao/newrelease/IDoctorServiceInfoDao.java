package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.DoctorServiceInfo;

public interface IDoctorServiceInfoDao extends BaseDao<DoctorServiceInfo>{
	public DoctorServiceInfo queryDoctorServiceInfoByOrderType(Integer orderType,Integer docid);
	public DoctorServiceInfo queryDoctorServiceInfoByCon(Integer docid,Integer serviceId,Integer packageId);
	public List<DoctorServiceInfo>queryOpenDoctorServiceInfo(Integer docid,String serviceids);
	public List<DoctorServiceInfo>queryDoctorServiceInfoByDocId(Integer docid);
}
