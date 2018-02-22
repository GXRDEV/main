package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IImTokenInfoDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.ImTokenInfo;
@Repository
public class ImTokenInfoDaoImpl extends BaseDaoImpl<ImTokenInfo> implements IImTokenInfoDao{
	
	@SuppressWarnings("unchecked")
	public ImTokenInfo queryImTokenInfoByCon(String userId, String mode) {
		// TODO Auto-generated method stub
		String hql="from ImTokenInfo where 1=1 and userId='"+userId+"' and appType='"+mode+"' ";
		List<ImTokenInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
}
