package com.tspeiz.modules.common.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.dao.newrelease.IDoctorForumDao;
import com.tspeiz.modules.common.dao.newrelease.IDoctorRegisterInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IHospitalDetailInfoDao;
import com.tspeiz.modules.common.entity.newrelease.DoctorForum;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.service.IKangxinService;

@Service
public class KangxinServiceImpl implements IKangxinService {
	@Resource
	private IHospitalDetailInfoDao hospitalDetailInfoDao;
	@Resource
	private IDoctorForumDao doctorForumDao;
	
	public List<HospitalDetailInfo> queryHospitalsByPage(Integer pageNo,
			Integer pageSize) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryHospitalsByPage(pageNo, pageSize);
	}

	public Map<String, Integer> queryExpertAndDepNumberByHos(Integer hosid) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.queryExpertAndDepNumberByHos(hosid);
	}

	public List<DoctorForum> queryDoctorForumsByConditions(Integer pageNo,
			Integer pageSie, String sort) {
		// TODO Auto-generated method stub
		return doctorForumDao.queryDoctorForumsByConditions(pageNo, pageSie, sort);
	}

	public Pager searchspecialsByPager(Pager pager) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.searchspecialsByPager(pager);
	}

	public Pager queryDoctorForumsByConditions(Pager pager) {
		// TODO Auto-generated method stub
		return doctorForumDao.queryDoctorForumsByConditions(pager);
	}

	
	public Pager searchspecialsByPager_advice(Pager pager) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.searchspecialsByPager_advice(pager);
	}


	public Pager searchspecialsByPager_remote(Pager pager) {
		// TODO Auto-generated method stub
		return hospitalDetailInfoDao.searchspecialsByPager_remote(pager);
	}
	
	
}
