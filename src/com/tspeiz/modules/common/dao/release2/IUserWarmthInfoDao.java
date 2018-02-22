package com.tspeiz.modules.common.dao.release2;

import java.math.BigDecimal;
import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.UserWarmthInfo;

public interface IUserWarmthInfoDao extends BaseDao<UserWarmthInfo>{
	public Integer querydoctorwarms(Integer docid);
	public List<UserWarmthInfo> querydoctorwarms_list(Integer docid,Integer pageNo,Integer pageSize);
}
