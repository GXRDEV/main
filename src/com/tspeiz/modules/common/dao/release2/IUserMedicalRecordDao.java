package com.tspeiz.modules.common.dao.release2;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.UserMedicalRecord;

public interface IUserMedicalRecordDao extends BaseDao<UserMedicalRecord>{
	public List<UserMedicalRecord> queryusermedicalrecords(String subUserUuid);
}
