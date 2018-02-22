package com.tspeiz.modules.common.dao.newrelease;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.ShortUrlRelate;

public interface IShortUrlRelateDao extends BaseDao<ShortUrlRelate>{
	public ShortUrlRelate queryShortUrlRelate(String code);
	public ShortUrlRelate queryShortUrlRelate(String ltype,String oid);
}
