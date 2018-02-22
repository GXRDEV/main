package com.tspeiz.modules.common.dao.release2;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.DoctorTeam;
import com.tspeiz.modules.common.entity.release2.DoctorTeamMember;

public interface IDoctorTeamMemberDao extends BaseDao<DoctorTeamMember>{

	public DoctorTeamMember queryDoctorTeamMembersByDocId(Integer userId);
	public List<DoctorTeamMember> queryDoctorTeamMembersByDocId(String groupUuid);

}
