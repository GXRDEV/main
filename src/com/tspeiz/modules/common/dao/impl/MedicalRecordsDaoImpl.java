package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IMedicalRecordsDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.MedicalRecords;

@Repository
public class MedicalRecordsDaoImpl extends BaseDaoImpl<MedicalRecords> implements IMedicalRecordsDao{
	@SuppressWarnings("unchecked")
	public MedicalRecords queryMedicalRecordsByOrderId(Integer oid) {
		// TODO Auto-generated method stub
		String hql="from MedicalRecords where 1=1 and orderId="+oid;
		List<MedicalRecords> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
}
