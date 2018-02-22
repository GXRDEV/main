package com.tspeiz.modules.common.dao.weixin;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;

public interface IWeiAccessTokenDao extends BaseDao<WeiAccessToken>{
	public WeiAccessToken queryWeiAccessTokenByDocId(Integer docid);
	public WeiAccessToken queryWeiAccessTokenById(String appId);
}
