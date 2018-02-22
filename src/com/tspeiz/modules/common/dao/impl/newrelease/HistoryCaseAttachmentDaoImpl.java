package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IHistoryCaseAttachmentDao;
import com.tspeiz.modules.common.entity.newrelease.HistoryCaseAttachment;
@Repository
public class HistoryCaseAttachmentDaoImpl extends BaseDaoImpl<HistoryCaseAttachment> implements IHistoryCaseAttachmentDao{

	@SuppressWarnings("unchecked")
	@Override
	public HistoryCaseAttachment queryHistoryCaseAttachment(
			String historyCaseUuid) {
		// TODO Auto-generated method stub
		String hql = "from HistoryCaseAttachment where 1=1 and historyCaseUuid='"+historyCaseUuid+"' ";
		List<HistoryCaseAttachment> list = this.hibernateTemplate.find(hql);
		if(list != null && list.size()>0) return list.get(0);
		return null;
	}
	
}
