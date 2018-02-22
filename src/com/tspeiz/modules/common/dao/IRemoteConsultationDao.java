package com.tspeiz.modules.common.dao;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.RemoteConsultation;

public interface IRemoteConsultationDao extends BaseDao<RemoteConsultation> {
	public List<RemoteConsultation> queryRemoteConsulationByExpert(
			Integer expertId);

	public Map<String, Object> queryRemoteConsulations(Integer expertId,
			String search, String sortby, Integer sortdir, final Integer start,
			final Integer length);

	public Map<String, Object> queryRemoteConsulations_doc(Integer docid,
			String search, String sortby, Integer sortdir, final Integer start,
			final Integer length);

	public RemoteConsultation queryRemoteConsulationByConditions(
			Integer expertId, String date, String startTime, Integer dur);

	public List<RemoteConsultation> queryRemoteConsultationsByConditions(
			String openid, Integer pageNo, Integer pageSize, String stas);

	public RemoteConsultation queryRemoteConsultationByTradeNo(String tradeNo);

	public Map<String, Object> queryRemoteConsulationsByConditions(Integer uid,
			String searchContent, String sortdesc, Integer start,
			Integer length, String status, String now);

	public Map<String, Object> queryRemoteConsulationsByConditions_new(
			Integer uid, Integer userType, String searchContent,
			String sortdesc, Integer start, Integer length, String status,
			String now);

	public Map<String, Object> queryRemoteConsulationsByConditions(
			Integer userId, Integer userType, String searchContent,
			String sortdesc, final Integer start, final Integer length,
			String status, String now);

	public void updateRemoteConsulationUserStatus(Integer orderid);

	public List<RemoteConsultation> queryRemoteConsultationsByProgressTag(
			Integer userId, Integer userType, Integer progressTag);

	public List<RemoteConsultation> queryRemoteConsultationsByConditions(
			Integer localHosId, Integer localDepId, Integer progressTag);

	public Map<String, Object> queryRemoteConsultationsByConditions_nurse(
			Integer nurseid, Integer start, Integer length, String searchContent);

	public List<RemoteConsultation> queryRemoteConsultationOrdersByConditions(
			String openid, Integer pageNo, Integer pageSize, String status);

	public Map<String, Object> queryRemoteConsulationsByConditions_docnew(
			Integer userId, Integer userType, String searchContent,
			String sortdesc, Integer start, Integer length, String status,
			String now);

	public Map<String, Object> queryRemoteConsulationsByConditions_hos(
			Integer hosid, String searchContent,
			String sortdesc, Integer start, Integer length, String status,
			String now);
	
	public RemoteConsultation queryRemoteConsulationsById_detail(
			Integer id); 
	
	public Map<String,Object> queryhelporders(String search,Integer start,Integer length,Integer type);
	
	public Integer queryOrdersNumByConditions(Integer hosid,Integer depid, String begin,
			String end);
	
	public Integer queryUnCompletedNum(Integer hosid);
	public Integer queryTotalOrders(Integer hosid);
	public Integer queryOrderNumbyType(Integer hosid, Integer type);
	
	public List<RemoteConsultation> queryRemoteConsulations();
}
