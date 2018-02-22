package com.tspeiz.modules.common.service;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.entity.newrelease.DoctorForum;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;

public interface IKangxinService {
	public List<HospitalDetailInfo> queryHospitalsByPage(Integer pageNo,Integer pageSize);
	public Map<String,Integer> queryExpertAndDepNumberByHos(Integer hosid);
	
	public List<DoctorForum> queryDoctorForumsByConditions(Integer pageNo,Integer pageSie,String sort);
	
	public Pager searchspecialsByPager(Pager pager);
	
	public Pager queryDoctorForumsByConditions(Pager pager);
	public Pager searchspecialsByPager_advice(Pager pager);
	public Pager searchspecialsByPager_remote(Pager pager);
}
