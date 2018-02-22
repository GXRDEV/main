package com.tspeiz.modules.common.dao.impl.weixin;

import java.util.List;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.weixin.IWeiAccessTokenDao;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;

public class WeiAccessTokenDaoImpl extends BaseDaoImpl<WeiAccessToken> implements IWeiAccessTokenDao{

	@SuppressWarnings("unchecked")
	public WeiAccessToken queryWeiAccessTokenByDocId(Integer docid) {
		// TODO Auto-generated method stub
		String hql="from WeiAccessToken where 1=1 and docId="+docid;
		List<WeiAccessToken> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@Override
	public WeiAccessToken queryWeiAccessTokenById(String appId) {
		// TODO Auto-generated method stub
		String hql="from WeiAccessToken where 1=1 and appId='"+appId+"'";
		List<WeiAccessToken> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
}
