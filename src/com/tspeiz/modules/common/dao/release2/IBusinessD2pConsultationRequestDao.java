package com.tspeiz.modules.common.dao.release2;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.BusinessD2pConsultationRequest;

public interface IBusinessD2pConsultationRequestDao extends BaseDao<BusinessD2pConsultationRequest>{
	public Map<String, Object> queryd2pconreqdatas(
			Map<String, Object> querymap, Integer start, Integer length);
}
