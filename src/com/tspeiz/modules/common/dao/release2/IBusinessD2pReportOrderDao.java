package com.tspeiz.modules.common.dao.release2;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.BusinessD2pReportOrder;

public interface IBusinessD2pReportOrderDao extends BaseDao<BusinessD2pReportOrder>{
	public List<BusinessD2pReportOrder>queryd2preportordersbyuserid(Integer userid);
	public BusinessD2pReportOrder queryd2preportorderbyconditions(String subUserUuid,Integer docid);
	public Map<String, Object> queryd2preportdatas(
			Map<String, Object> querymap, Integer start, Integer length);
}
