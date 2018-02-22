package com.tspeiz.modules.common.dao.weixin;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.weixin.CasesImage;

public interface ICaseImageDao extends BaseDao<CasesImage>{
	public List<CasesImage> queryCaseImagesByCaseId(Integer caseid);
}
