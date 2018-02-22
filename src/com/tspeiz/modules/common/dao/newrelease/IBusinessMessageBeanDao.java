package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.bean.AdviceBean;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;

public interface IBusinessMessageBeanDao extends BaseDao<BusinessMessageBean>{
	public List<BusinessMessageBean> queryBusinessMessageBeansByCon(Integer oid,String orderUuid,Integer type);
	public List<BusinessMessageBean> queryBusinessMessageBeansByCon(
			Integer oid,String orderUuid, Integer type, Integer docid);
	public List<BusinessMessageBean> queryBusinessMessageBeansByCon(Integer oid,Integer otype,Integer pageNo,Integer pageSize);
	public List<AdviceBean> newlyadvices(Integer userId);
	public Integer queryUnReadMsg(String uuid,Integer otype,Integer docid);
	public void updateMsgToRead(String uuid,Integer otype,Integer docid);
}
