package com.tspeiz.modules.common.dao.release2;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.bean.dto.DoctorInfoDto;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAlliance;

public interface IHospitalHealthAllianceDao extends BaseDao<HospitalHealthAlliance>{
	public Map<String, Object> queryhoshealthdatas(String search,Integer start,Integer length,String status);
	public HospitalHealthAlliance queryHospitalHealthAllianceByUuid(String uuid);
	public HospitalHealthAlliance queryHospitalHealthAllianceByRegId(Integer regId);
	public List<HospitalHealthAlliance> queryHospitalHealthAlliances(Integer hosId);
	public HospitalHealthAlliance queryHospitalHealthAllianceById_new(Integer id);
	public List<HospitalHealthAlliance> querygainAllianceByArea(String distCode);
	public List<DoctorInfoDto> loadAllianceDoctors(String allianceId,Integer pageNo,Integer pageSize);
	public Pager queryAllianceDoctorsPager(Pager pager);
	public List<HospitalHealthAlliance> queryHospitalHealthAlliances_audited();
}
