package com.tspeiz.modules.common.dao;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.SystemServicePackage;

public interface IServicePackageDao extends BaseDao<SystemServicePackage>{

	public SystemServicePackage queryServicePackageByServiceId(Integer id);
	public void deleteServicePackage(Integer id);
	public List<SystemServicePackage> queryServicePackagesByServiceId(Integer serviceid);


}
