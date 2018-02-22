package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;

public interface IUserCaseAttachmentDao extends BaseDao<UserCaseAttachment>{

	public List<UserCaseAttachment> queryUserAttachmentByCaseUuid(String uuid);


}
