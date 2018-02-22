package com.tspeiz.modules.common.dao.release2;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.PlatformHealthConsultation;

public interface IplatformHealthConsultationDao extends BaseDao<PlatformHealthConsultation>{
	public Map<String,Object> querySystemConsultationCaseDatas(String searchContent,Integer ostatus,Integer start,Integer length);

}
