package com.tspeiz.modules.common.dao.release2;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.SystemSmsRecord;

public interface ISystemSmsRecordDao extends BaseDao<SystemSmsRecord>{
	public Map<String, Object> querySystemSmsRecordDatas(String search,Integer status, Integer start, Integer length);
}
