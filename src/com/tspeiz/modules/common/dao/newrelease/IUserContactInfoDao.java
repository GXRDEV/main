package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;

public interface IUserContactInfoDao extends BaseDao<UserContactInfo>{
	public List<UserContactInfo> queryUserContactInfosByOpenId(String openid);
	public UserContactInfo queryUserContactInfoByConditions(String openid, String username,String idcard, String telphone);
	public List<UserContactInfo> queryUserContactInfosByUserId(Integer uid);
	public UserContactInfo queryUserContactorByCondition(Integer uid,String username,String tel);
	public UserContactInfo queryUserContactInfoByUuid(String uuid);
	
}
