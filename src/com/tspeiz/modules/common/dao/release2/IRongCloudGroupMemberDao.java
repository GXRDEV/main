package com.tspeiz.modules.common.dao.release2;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.RongCloudGroupMember;

public interface IRongCloudGroupMemberDao extends BaseDao<RongCloudGroupMember>{
	public List<RongCloudGroupMember> queryRongCloudGroupMembersByGroupUuid(
			String groupUuid);
}
