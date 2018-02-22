package com.tspeiz.modules.common.dao;

import java.math.BigInteger;

import com.tspeiz.modules.common.bean.dcm.InstanceRelate;
import com.tspeiz.modules.common.dao.base.BaseDao;

public interface IInstanceRelateDao extends BaseDao<InstanceRelate>{
	public InstanceRelate queryInstanceRelateByInstPk(BigInteger ipk);
}
