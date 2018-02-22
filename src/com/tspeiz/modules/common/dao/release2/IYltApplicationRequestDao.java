package com.tspeiz.modules.common.dao.release2;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.YltApplicationRequest;

public interface IYltApplicationRequestDao extends BaseDao<YltApplicationRequest>{
	public Map<String,Object> queryappjoin_hos(Integer hosId,String search,Integer start,Integer length,Integer otype);
	public YltApplicationRequest queryYltApplicationRequestByCondition(String allianceUuid,Integer applicantId,Integer applicantType);
}
