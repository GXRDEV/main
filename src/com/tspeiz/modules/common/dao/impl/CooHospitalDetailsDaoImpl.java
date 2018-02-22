package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.ICooHospitalDetailsDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.CooHospitalDetails;

@Repository
public class CooHospitalDetailsDaoImpl extends BaseDaoImpl<CooHospitalDetails>
		implements ICooHospitalDetailsDao {

	@SuppressWarnings("unchecked")
	public List<CooHospitalDetails> queryAllCooHospitalDetails() {
		// TODO Auto-generated method stub
		String hql="from CooHospitalDetails where 1=1 order by city";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public CooHospitalDetails queryCooHospitalDetailsByCity(String city) {
		// TODO Auto-generated method stub
		String hql="from CooHospitalDetails where city like '%"+city+"%'";
		List<CooHospitalDetails> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
}
