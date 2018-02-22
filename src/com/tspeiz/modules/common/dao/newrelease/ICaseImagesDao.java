package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.CaseImages;

public interface ICaseImagesDao extends BaseDao<CaseImages>{
	public List<CaseImages> queryCaseImagesByCaseId(Integer caseId);
	public void deleteCaseImages(Integer caseid,String ids);
	public List<CaseImages> queryCaseImagesByCaseIds(String ids);
}
