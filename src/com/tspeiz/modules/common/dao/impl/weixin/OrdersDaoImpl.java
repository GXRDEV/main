package com.tspeiz.modules.common.dao.impl.weixin;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.weixin.IOrdersDao;
import com.tspeiz.modules.common.entity.weixin.Orders;
@Repository
public class OrdersDaoImpl extends BaseDaoImpl<Orders> implements IOrdersDao{

}
