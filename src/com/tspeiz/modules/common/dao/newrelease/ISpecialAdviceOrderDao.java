package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;

public interface ISpecialAdviceOrderDao extends BaseDao<SpecialAdviceOrder>{
	public Map<String,Object> querySpecialAdviceOrdersByCondition(Integer type,Integer userId,String search,Integer start,Integer length,Integer utype);
	public Map<String,Object> querySpecialAdviceOrdersByCondition_sys(Integer type,String search,Integer start,Integer length);
	public SpecialAdviceOrder querySpecialAdviceOrderByUid(String uuid);
	public Map<String,Object> querySpecialAdviceOrdersByCondition_new(Integer type,String searchContent,Integer start,Integer length);
	public Map<String,Object> queryD2DTuwenDatas_doc(Integer docid,String searchContent,Integer ostatus,Integer start,Integer length,Integer dtype);
	public Map<String,Object> queryD2DTuwenDatas_expert(Integer docid,String searchContent,Integer ostatus,Integer start,Integer length);
	public List<SpecialAdviceOrder> gettuwenList(Integer sta,String hosid, String depid, String startDate, String endDate);
	public Map<String, Object> querySpecialAdviceOrdersByCondition(Integer hosId, String search, Integer ostatus,
			Integer start, Integer length, Integer dtype);
}
