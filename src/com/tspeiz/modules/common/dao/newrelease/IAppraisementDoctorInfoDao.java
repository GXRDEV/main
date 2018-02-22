package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.AppraisementDoctorInfo;

public interface IAppraisementDoctorInfoDao extends BaseDao<AppraisementDoctorInfo>{
	public AppraisementDoctorInfo queryAppraisementDoctorInfoByConditions(String oid,Integer ltype);
	public List<AppraisementDoctorInfo> queryAppraisementDoctorInfosByDoc(Integer docid,Integer pageNo,Integer pageSize);
	public Integer queryAppraisementDoctorInfosCount(Integer docid);
}
