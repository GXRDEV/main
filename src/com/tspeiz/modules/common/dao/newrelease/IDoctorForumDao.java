package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.bean.Pager;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.DoctorForum;

public interface IDoctorForumDao extends BaseDao<DoctorForum>{
	public List<DoctorForum> queryDoctorForumsByConditions(Integer pageNo,Integer pageSie,String sort);
	public Pager queryDoctorForumsByConditions(Pager pager);
	
	public DoctorForum querynewforum();
}
