package com.tspeiz.modules.pacs.common.dao;

import java.math.BigInteger;
import java.util.List;

import com.tspeiz.modules.pacs.bean.InstanceInfo;
import com.tspeiz.modules.pacs.bean.InstanceRelate;
import com.tspeiz.modules.pacs.bean.SeriesInfo;
import com.tspeiz.modules.pacs.bean.StudyInfo;




public interface IInstanceRelateDao extends BaseDao<InstanceRelate>{
	public InstanceRelate queryInstanceRelateByInstPk(BigInteger ipk);
	public StudyInfo queryStudyinfoByCondition(String patientId,String studyId);
	
	public List<SeriesInfo> querySeriesInfosByCondition(String patientId,String studyId);
	public List<InstanceInfo> queryInstanceInfosByCondition(String patientId,String studyId,String seriesId);
	
	public List<StudyInfo> queryStudysByCondition(String patientId);
}
