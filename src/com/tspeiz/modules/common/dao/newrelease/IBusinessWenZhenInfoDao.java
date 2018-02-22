package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.SystemServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenZhenInfo;

public interface IBusinessWenZhenInfoDao extends BaseDao<BusinessWenZhenInfo> {
	public Map<String, Object> queryTuwenOrdersByConditions(String search,
			Integer start, Integer lenght, Integer type);
	public Map<String, Object> queryTuwenOrdersByExpertConditions(Integer docid,String search,
			Integer start, Integer lenght, Integer type);
	public List<WenzhenBean> queryOverTimeTwOrders(Integer day);
	
	public WenzhenBean queryBusinessTwById(Integer id);
	
	public Boolean queryExistNoClosedTw(Integer uid);
	
	public Boolean queryExistOrderBySevenDays(Integer docid,Integer uid);
	
	public List<WenzhenBean> queryBusinessWenzhensByUId(Integer uid,Integer pageNo,Integer pageSize);
	
	public List<WenzhenBean> queryWenzhenOrdersByConditions(Integer userId,Integer pageNo,Integer pageSize,String flag);
	
	public List<BusinessWenZhenInfo> queryBusinessWenzhenInfos();
	
}
