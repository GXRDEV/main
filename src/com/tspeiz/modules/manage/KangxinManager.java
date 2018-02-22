package com.tspeiz.modules.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.bean.dto.DoctorInfoDto;
import com.tspeiz.modules.common.entity.newrelease.DistCode;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAlliance;
import com.tspeiz.modules.common.service.ICommonService;
@Service
public class KangxinManager {
	@Autowired
	private ICommonService commonService;
	/**
	 * 获取医联体区域
	 * @return
	 */
	public Map<String,Object> gainAllianceArea(){
		Map<String,Object> map=new HashMap<String,Object>();
		List<DistCode> areas=commonService.queryAllianceAreas();
		for (DistCode distCode : areas) {
			DistCode pcode=commonService.queryDistCodeByCode(distCode.getDistCode().substring(0,2)+"0000");
			distCode.setDistName(pcode.getDistName()+"-"+distCode.getDistName());
		}
		map.put("areas", areas);
		return map;
	}
	/**
	 * 根据区域获取医联体
	 * @param distCode
	 * @return
	 */
	public Map<String,Object> gainAllianceByArea(String distCode){
		Map<String,Object> map=new HashMap<String,Object>();
		List<HospitalHealthAlliance> alliances=commonService.querygainAllianceByArea(distCode);
		map.put("alliances", alliances);
		return map;
	}
	/**
	 *  获取医联体详情
	 * @param allianceId
	 * @return
	 */
	public HospitalHealthAlliance gainAllianceDetailInfo(Integer allianceId){
		return commonService.queryHospitalHealthAllianceById_new(allianceId);
	}
	/**
	 * 加载医联体中医生
	 * @param allianceId
	 * @return
	 */
	public Map<String,Object> loadAllianceDoctors(String allianceId,String pageNo,String pageSize){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer _pageNo=1,_pageSize=16;
		if(StringUtils.isNotBlank(pageNo))_pageNo=Integer.parseInt(pageNo);
		if(StringUtils.isNotBlank(pageSize))_pageSize=Integer.parseInt(pageSize);
		List<DoctorInfoDto> doctors=commonService.loadAllianceDoctors(allianceId,_pageNo,_pageSize);
		map.put("doctors", doctors);
		return map;
	}
}
