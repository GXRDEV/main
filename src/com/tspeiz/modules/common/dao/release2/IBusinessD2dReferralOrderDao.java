package com.tspeiz.modules.common.dao.release2;

import java.util.Map;

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.BusinessD2dReferralOrder;

public interface IBusinessD2dReferralOrderDao extends BaseDao<BusinessD2dReferralOrder>{
	public BusinessD2dReferralOrder queryBusinessD2pReferralOrderByUuid(String orderUuid);
	public Map<String,Object> queryd2preferdatas(Map<String,Object> querymap,Integer start,Integer length);
	public Map<String,Object> queryReferordersByCondition(Integer docid,String searchContent,Integer ostatus,Integer start,Integer length,Integer dtype);
	public Map<String,Object> queryReferordersByCondition_hos(Integer hosId,String search,Integer ostatus,Integer start,Integer length,Integer dtype);
	public MobileSpecial queryBusinessD2dReferralOrderByUserId(Integer docid);
}
