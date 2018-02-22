package com.tspeiz.modules.common.dao.release2;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.D2pOrderBean;
import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.BusinessD2pTelOrder;

public interface IBusinessD2pTelOrderDao extends BaseDao<BusinessD2pTelOrder>{
	public Map<String,Object> queryd2pteldatas(Map<String,Object> querymap,Integer start,Integer length);
	public D2pOrderBean queryOrderDetailInfo(Integer oid,Integer otype);
	public List<D2pOrderBean> querymyorders(Integer userid,String ltype,Integer pageNo,Integer pageSize);
}
