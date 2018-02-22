package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;

public interface IBusinessVedioOrderDao extends BaseDao<BusinessVedioOrder>{
	public BusinessVedioOrder queryBusinessVedioOrderById(Integer id);
	public Object queryFlowNumberCallPROCEDURE(Integer type);
	public BusinessVedioOrder queryBusinessVedioOrderByUid(String uid);
	public Map<String,Object> queryOrdersAddCon(List<String> dates,String hosid);
	public Map<String,Integer> queryOrdersAddCon(String startDate,String endDate,Integer type,String hosid);
	public List<ReSourceBean> queryOrderHosCal(String startDate,String endDate);
	public List<ReSourceBean> orderexcal(String hosid,String startDate,String endDate);
	public List<ReSourceBean> orderdepcal(String hosid,String startDate,String endDate);
	public Map<String,Object> gainvedioorderdatas(String search,Integer start,Integer length,Integer type);
	public Map<String,Object> queryVedioOrderDatas_doc(Integer docid,String searchContent,Integer ostatus,Integer start,Integer length,Integer dtype);
	public Map<String, Object> queryVedioordersByCondition_hos(Integer hosId,
			String search, Integer ostatus, Integer start, Integer length,
			Integer dtype);
	public Map<String,Object> queryVedioOrderDatas_expert(Integer docid,String searchContent,Integer ostatus,Integer start,Integer length);
	public SpecialAdviceOrder querySpecialAdviceOrderById(Integer oid);
	public List<BusinessVedioOrder> getfinshvedioList(Integer sta,String startDate,String endDate, String hosid, String depid);
	public BusinessVedioOrder vedioorderRemrk(Integer sta, Integer id);
	public Map<String, Object> gainrecriveorderdatas(String search, Integer start, Integer length, Integer type);
}
