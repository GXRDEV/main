package com.tspeiz.modules.common.dao.release2;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.LivePlanOrder;

import java.util.Map;

/**
 * Created by kangxin on 2018/2/2.
 */
public interface ILivePlanOrderInfoDao extends BaseDao<LivePlanOrder> {
    public Map<String,Object> liveplandatass(String searchContent, Integer start, Integer length);
}
