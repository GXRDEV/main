package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenzhenTel;

public interface IBusinessWenzhenTelDao extends BaseDao<BusinessWenzhenTel>{
	public Map<String,Object> querytelorders(String search,Integer start,Integer length,String docid,Integer ostatus);
	public List<WenzhenBean> queryBusinessWenZhenTelUid(Integer uid);
	public List<BusinessWenzhenTel> queryBusinessWenzhenTels();
}
