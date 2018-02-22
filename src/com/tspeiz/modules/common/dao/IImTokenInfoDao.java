package com.tspeiz.modules.common.dao;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.ImTokenInfo;

public interface IImTokenInfoDao extends BaseDao<ImTokenInfo>{
	public ImTokenInfo queryImTokenInfoByCon(String userId,String mode);
}
