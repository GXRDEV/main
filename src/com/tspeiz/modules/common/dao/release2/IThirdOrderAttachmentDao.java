package com.tspeiz.modules.common.dao.release2;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.ThirdOrderAttachment;

public interface IThirdOrderAttachmentDao extends BaseDao<ThirdOrderAttachment>{
	public List<ThirdOrderAttachment> queryThirdOrderAttachments(String orderUuid);
}
