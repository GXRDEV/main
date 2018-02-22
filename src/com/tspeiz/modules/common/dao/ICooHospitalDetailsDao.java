package com.tspeiz.modules.common.dao;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.CooHospitalDetails;

public interface ICooHospitalDetailsDao extends BaseDao<CooHospitalDetails>{
	//获取合作意向的医院（含城市)
	public List<CooHospitalDetails> queryAllCooHospitalDetails();
	//根据城市获取合作医院
	public CooHospitalDetails queryCooHospitalDetailsByCity(String city);
}
