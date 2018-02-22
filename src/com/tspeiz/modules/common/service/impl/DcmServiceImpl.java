package com.tspeiz.modules.common.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.bean.dcm.InstanceInfo;
import com.tspeiz.modules.common.bean.dcm.InstanceRelate;
import com.tspeiz.modules.common.bean.dcm.SeriesInfo;
import com.tspeiz.modules.common.bean.dcm.StudyInfo;
import com.tspeiz.modules.common.bean.dcm.oviyam.StudyModel;
import com.tspeiz.modules.common.dao.IBigDepartmentDao;
import com.tspeiz.modules.common.dao.IInstanceRelateDao;
import com.tspeiz.modules.common.service.IDcmService;


public class DcmServiceImpl implements IDcmService {
	private IBigDepartmentDao bigDepartmentDao;
	private IInstanceRelateDao instanceRelateDao;

	public IBigDepartmentDao getBigDepartmentDao() {
		return bigDepartmentDao;
	}

	public void setBigDepartmentDao(IBigDepartmentDao bigDepartmentDao) {
		this.bigDepartmentDao = bigDepartmentDao;
	}

	public IInstanceRelateDao getInstanceRelateDao() {
		return instanceRelateDao;
	}

	public void setInstanceRelateDao(IInstanceRelateDao instanceRelateDao) {
		this.instanceRelateDao = instanceRelateDao;
	}

	public StudyInfo queryStudyinfoByCondition(String patientId, String studyId) {
		// TODO Auto-generated method stub
		return bigDepartmentDao.queryStudyinfoByCondition(patientId, studyId);
	}

	public List<SeriesInfo> querySeriesInfosByCondition(String patientId,
			String studyId) {
		// TODO Auto-generated method stub
		return bigDepartmentDao.querySeriesInfosByCondition(patientId, studyId);
	}

	public List<InstanceInfo> queryInstanceInfosByCondition(String patientId,
			String studyId, String seriesId) {
		// TODO Auto-generated method stub
		return bigDepartmentDao.queryInstanceInfosByCondition(patientId,
				studyId, seriesId);
	}

	public InstanceRelate queryInstanceRelateByInstPk(BigInteger ipk) {
		// TODO Auto-generated method stub
		return instanceRelateDao.queryInstanceRelateByInstPk(ipk);
	}

	public Integer saveInstanceRelate(InstanceRelate re) {
		// TODO Auto-generated method stub
		return instanceRelateDao.save_pacs(re);
	}

	public List<StudyInfo> queryStudysByCondition(String patientId) {
		// TODO Auto-generated method stub
		return bigDepartmentDao.queryStudysByCondition(patientId);
	}
}
