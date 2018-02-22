package com.tspeiz.modules.common.dao;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.VedioRelative;

public interface IVedioRelativeDao extends BaseDao<VedioRelative>{
	public List<VedioRelative> queryVediosByOrderId(Integer oid);
}
