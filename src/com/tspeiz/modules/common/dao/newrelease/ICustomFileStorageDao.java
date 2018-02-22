package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;

public interface ICustomFileStorageDao extends BaseDao<CustomFileStorage> {
	public List<CustomFileStorage> queryCustomFileStorageVedios(Integer oid);
	
	public List<CustomFileStorage> queryCustomFileStorageImages(String relatedPics);
}
