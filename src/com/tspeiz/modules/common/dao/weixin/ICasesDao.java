package com.tspeiz.modules.common.dao.weixin;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.weixin.Cases;

public interface ICasesDao extends BaseDao<Cases> {
	public List<Cases> queryCasesListByUserId(Integer uid) ;
}
