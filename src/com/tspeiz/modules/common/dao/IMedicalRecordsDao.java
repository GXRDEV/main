package com.tspeiz.modules.common.dao;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.MedicalRecords;

public interface IMedicalRecordsDao extends BaseDao<MedicalRecords>{
	public MedicalRecords queryMedicalRecordsByOrderId(Integer oid);
}
