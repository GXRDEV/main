package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IUserWeixinRelativeDao;
import com.tspeiz.modules.common.entity.newrelease.UserWeixinRelative;

@Repository
public class UserWeixinRelativeDaoImpl extends BaseDaoImpl<UserWeixinRelative>
		implements IUserWeixinRelativeDao {

	@SuppressWarnings("unchecked")
	public UserWeixinRelative queryUserWeiRelativeByOpenId(String openid) {
		// TODO Auto-generated method stub
		String hql="from UserWeixinRelative where 1=1 and openId='"+openid+"' ";
		List<UserWeixinRelative> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	public UserWeixinRelative queryUserWeiRelativeByUserId(Integer userid) {
		// TODO Auto-generated method stub
		String hql="from UserWeixinRelative where 1=1 and userId="+userid;
		List<UserWeixinRelative> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0) return list.get(0);
		return null;
	}

	public UserWeixinRelative queryUserWeixinRelativeByCondition(String appId,
			Integer userId) {
		// TODO Auto-generated method stub
		String hql="from UserWeixinRelative where 1=1 and userId="+userId+" and appId='"+appId+"' ";
		List<UserWeixinRelative> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0) return list.get(0);
		return null;
	}

}
