package com.tspeiz.modules.common.dao;

import java.util.List;

import com.tspeiz.modules.common.bean.dcm.InstanceInfo;
import com.tspeiz.modules.common.bean.dcm.SeriesInfo;
import com.tspeiz.modules.common.bean.dcm.StudyInfo;
import com.tspeiz.modules.common.bean.dcm.oviyam.StudyModel;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.BigDepartment;

public interface IBigDepartmentDao extends BaseDao<BigDepartment>{
	public List<BigDepartment> queryBigDepartments();
	public StudyInfo queryStudyinfoByCondition(String patientId,String studyId);
	
	public List<SeriesInfo> querySeriesInfosByCondition(String patientId,String studyId);
	public List<InstanceInfo> queryInstanceInfosByCondition(String patientId,String studyId,String seriesId);
	
	public List<StudyInfo> queryStudysByCondition(String patientId);
}
