package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IHistoryCaseDepRelativeDao;
import com.tspeiz.modules.common.entity.newrelease.HistoryCaseDepRelative;

@Repository
public class HistoryCaseDepRelativeDaoImpl extends BaseDaoImpl<HistoryCaseDepRelative> implements IHistoryCaseDepRelativeDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<HistoryCaseDepRelative> queryHistoryCaseDepRelatives(
			String historyCaseUuid) {
		// TODO Auto-generated method stub
		String hql = "from HistoryCaseDepRelative where 1=1 and historyCaseUuid='"+historyCaseUuid+"' ";
		return this.hibernateTemplate.find(hql);
	}
	
}
