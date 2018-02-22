package com.tspeiz.modules.common.dao.impl.weixin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.weixin.ICasesDao;
import com.tspeiz.modules.common.entity.weixin.Cases;
@Repository
public class CasesDaoImpl extends BaseDaoImpl<Cases> implements ICasesDao{

	
	@SuppressWarnings("unchecked")
	public List<Cases> queryCasesListByUserId(Integer uid) {
		// TODO Auto-generated method stub
		String hql=" from Cases where patientId="+uid;
		return this.hibernateTemplate.find(hql);
	}
	
}
