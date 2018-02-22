package com.tspeiz.modules.pacs.common.serviceImpl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.dao.IBigDepartmentDao;
import com.tspeiz.modules.pacs.bean.InstanceInfo;
import com.tspeiz.modules.pacs.bean.InstanceRelate;
import com.tspeiz.modules.pacs.bean.SeriesInfo;
import com.tspeiz.modules.pacs.bean.StudyInfo;
import com.tspeiz.modules.pacs.common.dao.IInstanceRelateDao;
import com.tspeiz.modules.pacs.common.service.IDcmService;
@Service
public class DcmServiceImpl implements IDcmService {
	private IInstanceRelateDao instanceRelateDao;

	public IInstanceRelateDao getInstanceRelateDao() {
		return instanceRelateDao;
	}

	public void setInstanceRelateDao(IInstanceRelateDao instanceRelateDao) {
		this.instanceRelateDao = instanceRelateDao;
	}

	public StudyInfo queryStudyinfoByCondition(String patientId, String studyId) {
		// TODO Auto-generated method stub
		return instanceRelateDao.queryStudyinfoByCondition(patientId, studyId);
	}

	public List<SeriesInfo> querySeriesInfosByCondition(String patientId,
			String studyId) {
		// TODO Auto-generated method stub
		return instanceRelateDao.querySeriesInfosByCondition(patientId, studyId);
	}

	public List<InstanceInfo> queryInstanceInfosByCondition(String patientId,
			String studyId, String seriesId) {
		// TODO Auto-generated method stub
		return instanceRelateDao.queryInstanceInfosByCondition(patientId,
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
		return instanceRelateDao.queryStudysByCondition(patientId);
	}
}
