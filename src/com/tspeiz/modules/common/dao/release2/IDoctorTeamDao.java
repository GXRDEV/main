package com.tspeiz.modules.common.dao.release2;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.DoctorTeam;
import com.tspeiz.modules.common.entity.release2.DoctorTeamMember;

public interface IDoctorTeamDao extends BaseDao<DoctorTeam> {
	public Map<String,Object> queryauditdocteamdatas(Map<String,Object> querymap,Integer start,Integer length);
	public Map<String,Object> querydocteamdatas(Map<String,Object> querymap,Integer start,Integer length);
	public DoctorTeam queryDoctorTeamByUuid(String uuid);
	public DoctorTeam queryDoctorTeamById_detail(Integer tid);
	public Map<String,Object> queryDoctorTeamMemberDatas(Integer teamId,String searchContent,Integer start,Integer length);
	public List<DoctorTeamMember> queryDoctorTeamMembersByTeamId(Integer teamId);
	public DoctorTeam queryDoctorTeamByuuid(String groupUuid);
	public void delDocTeamRequest(String groupUuid);
	public List<DoctorTeam> queryDoctorTeamHasNoErweima();
}
