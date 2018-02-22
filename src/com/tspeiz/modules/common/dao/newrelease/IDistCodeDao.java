package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.DistCode;

public interface IDistCodeDao extends BaseDao<DistCode>{
	public List<DistCode> queryDistCodesByConditions(String stype,String procost);
	public DistCode queryDistCodeByCode(String code);
	public List<DistCode> gainArea(String type,String parentCode);
	public List<DistCode> queryAllianceAreas();
}
