package com.tspeiz.modules.common.dao;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.CustomUpload;

public interface ICustomUploadDao extends BaseDao<CustomUpload>{
	public CustomUpload queryCustomUploadByUrl(String url);
}
