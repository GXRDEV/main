package com.tspeiz.modules.common.dao.newrelease;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.DoctorWithdrawInfo;

public interface IDoctorWithdrawInfoDao extends BaseDao<DoctorWithdrawInfo>{
	public Map<String,Object> queryDoctorWithdrawdatas(String startDate,String endDate,String search,Integer start,Integer length,Integer docid,Integer ostatus,String busiType);
	public DoctorWithdrawInfo queryDoctorWithdrawInfoById(Integer id);
}
