package com.tspeiz.modules.common.dao.release2;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.RongCloudGroup;
import com.tspeiz.modules.common.entity.release2.RongCloudGroupPostRelation;

public interface IRongCloudGroupDao extends BaseDao<RongCloudGroup>{
	public Map<String, Object> queryGroups(Integer ostatus,Integer groupType,
			String searchContent, Integer start, Integer length);
	public Map<String,Object> gainGroupMemberDatas(Integer groupId,String searchContent,Integer start,Integer length);
	public RongCloudGroup queryRongCloudGroupByGroupUuid(String groupUuid);
	public Map<String, Object> gainDocTeamGroupMemberDatas(Integer groupId, String searchContent, Integer start,
			Integer length);
}
