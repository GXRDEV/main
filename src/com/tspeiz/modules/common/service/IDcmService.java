package com.tspeiz.modules.common.service;

import java.math.BigInteger;
import java.util.List;

import com.tspeiz.modules.common.bean.dcm.InstanceInfo;
import com.tspeiz.modules.common.bean.dcm.InstanceRelate;
import com.tspeiz.modules.common.bean.dcm.SeriesInfo;
import com.tspeiz.modules.common.bean.dcm.StudyInfo;
import com.tspeiz.modules.common.bean.dcm.oviyam.StudyModel;

public interface IDcmService {
	public StudyInfo queryStudyinfoByCondition(String patientId,String studyId);
	public List<SeriesInfo> querySeriesInfosByCondition(String patientId,String studyId);
	public List<InstanceInfo> queryInstanceInfosByCondition(String patientId,String studyId,String seriesId);
	public InstanceRelate queryInstanceRelateByInstPk(BigInteger ipk);
	public Integer saveInstanceRelate(InstanceRelate re);
	
	public List<StudyInfo> queryStudysByCondition(String patientId);
}
