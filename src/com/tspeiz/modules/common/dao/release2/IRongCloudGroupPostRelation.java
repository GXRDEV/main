package com.tspeiz.modules.common.dao.release2;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.RongCloudGroupPostRelation;

public interface IRongCloudGroupPostRelation extends BaseDao<RongCloudGroupPostRelation>{

	public RongCloudGroupPostRelation queryRongCloudGroupPostRelation(String groupUuid);

}
