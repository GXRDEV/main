package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.UserBillRecord;

public interface IUserBillRecordDao extends BaseDao<UserBillRecord>{
	public UserBillRecord queryUserBillByCondition(String content, Integer oid);
	public List<UserBillRecord> gainuserbills(Integer userid,String ispage,Integer pageNo,Integer pageSize);
}
