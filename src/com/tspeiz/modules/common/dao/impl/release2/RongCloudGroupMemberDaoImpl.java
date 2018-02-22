package com.tspeiz.modules.common.dao.impl.release2;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IRongCloudGroupMemberDao;
import com.tspeiz.modules.common.entity.release2.RongCloudGroupMember;

@Repository
public class RongCloudGroupMemberDaoImpl extends BaseDaoImpl<RongCloudGroupMember> implements IRongCloudGroupMemberDao{

	@SuppressWarnings("unchecked")
	public List<RongCloudGroupMember> queryRongCloudGroupMembersByGroupUuid(
			String groupUuid) {
		// TODO Auto-generated method stub
		String hql="from RongCloudGroupMember where 1=1 and groupUuid='"+groupUuid+"' ";
		return this.hibernateTemplate.find(hql);
	}

}
