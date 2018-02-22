package com.tspeiz.modules.common.dao.impl.release2;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IThirdOrderAttachmentDao;
import com.tspeiz.modules.common.entity.release2.ThirdOrderAttachment;

@Repository
public class ThirdOrderAttachmentDaoImpl extends BaseDaoImpl<ThirdOrderAttachment> implements IThirdOrderAttachmentDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<ThirdOrderAttachment> queryThirdOrderAttachments(
			String orderUuid) {
		// TODO Auto-generated method stub
		String hql = "from ThirdOrderAttachment where 1=1 and orderUuid='"+orderUuid+"' ";
		return this.hibernateTemplate.find(hql);
	}
	
}
