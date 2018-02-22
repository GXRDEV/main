package com.tspeiz.modules.common.dao;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.FaceDiagnoses;

public interface IFaceDiagnosesDao extends BaseDao<FaceDiagnoses>{
	public Integer queryCountByPlused(Integer sid, String dtime);
	
	public FaceDiagnoses queryFaceDiagnosesByTradeNo(String tradeNo);
}
