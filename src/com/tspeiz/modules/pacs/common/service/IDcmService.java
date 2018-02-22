package com.tspeiz.modules.pacs.common.service;

import java.math.BigInteger;
import java.util.List;

import com.tspeiz.modules.pacs.bean.InstanceInfo;
import com.tspeiz.modules.pacs.bean.InstanceRelate;
import com.tspeiz.modules.pacs.bean.SeriesInfo;
import com.tspeiz.modules.pacs.bean.StudyInfo;



public interface IDcmService {
	public StudyInfo queryStudyinfoByCondition(String patientId,String studyId);
	public List<SeriesInfo> querySeriesInfosByCondition(String patientId,String studyId);
	public List<InstanceInfo> queryInstanceInfosByCondition(String patientId,String studyId,String seriesId);
	public InstanceRelate queryInstanceRelateByInstPk(BigInteger ipk);
	public Integer saveInstanceRelate(InstanceRelate re);
	
	public List<StudyInfo> queryStudysByCondition(String patientId);
}
