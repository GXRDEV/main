package com.tspeiz.modules.common.dao.weixin;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.weixin.ContactInfo;

public interface IContactInfoDao extends BaseDao<ContactInfo>{
	public ContactInfo queryByConditions(String openid, String username,
			String idcard, String telphone);
	public List<ContactInfo> queryContactInfosByOpenId(String openid);
}
