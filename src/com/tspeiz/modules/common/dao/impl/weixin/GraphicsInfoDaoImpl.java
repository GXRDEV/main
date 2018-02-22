package com.tspeiz.modules.common.dao.impl.weixin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.weixin.IGraphicsInfoDao;
import com.tspeiz.modules.common.entity.weixin.GraphicsInfo;
@Repository
public class GraphicsInfoDaoImpl extends BaseDaoImpl<GraphicsInfo> implements IGraphicsInfoDao{

	@SuppressWarnings("unchecked")
	public GraphicsInfo queryGraphicsInfoByTradeNo(String tradeNo) {
		// TODO Auto-generated method stub
		String hql="from GraphicsInfo where outTradeNo='"+tradeNo+"' ";
		List<GraphicsInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
}
