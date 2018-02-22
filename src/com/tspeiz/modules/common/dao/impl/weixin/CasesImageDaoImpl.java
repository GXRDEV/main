package com.tspeiz.modules.common.dao.impl.weixin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.weixin.ICaseImageDao;
import com.tspeiz.modules.common.entity.weixin.CasesImage;
@Repository
public class CasesImageDaoImpl extends BaseDaoImpl<CasesImage> implements ICaseImageDao{

	
	@SuppressWarnings("unchecked")
	public List<CasesImage> queryCaseImagesByCaseId(Integer caseid) {
		// TODO Auto-generated method stub
		String hql=" from CasesImage where caseId="+caseid;
		return this.hibernateTemplate.find(hql);
	}
	
}
