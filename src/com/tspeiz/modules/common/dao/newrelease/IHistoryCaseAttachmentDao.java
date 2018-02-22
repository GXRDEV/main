package com.tspeiz.modules.common.dao.newrelease;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.HistoryCaseAttachment;

public interface IHistoryCaseAttachmentDao extends BaseDao<HistoryCaseAttachment>{
	public HistoryCaseAttachment queryHistoryCaseAttachment(String historyCaseUuid);
}
