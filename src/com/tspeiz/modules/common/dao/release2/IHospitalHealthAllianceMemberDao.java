package com.tspeiz.modules.common.dao.release2;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAllianceMember;

public interface IHospitalHealthAllianceMemberDao extends BaseDao<HospitalHealthAllianceMember>{
	public List<HospitalHealthAllianceMember> queryHospitalHealAllianceMembersByCon(String hhaUuid,Integer level,Integer parentHosId);
	public HospitalHealthAllianceMember queryHospitalHealthAllianceMemberByCon(Integer hosId,String allianceUuid);
	public Map<String,Object> querymyyltdatas_hos(Integer hosId,String search,Integer start,Integer length);
	public List<HospitalDetailInfo> queryHospitalHealthAllianceMemberByHhaId(String  alianceUuid);
	public List<HospitalHealthAllianceMember> queryHospitalHealthAllianceMemberByCon_main(String allianceUuid);
}
