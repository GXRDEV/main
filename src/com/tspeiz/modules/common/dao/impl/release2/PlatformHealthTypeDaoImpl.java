package com.tspeiz.modules.common.dao.impl.release2;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IPlatformHealthTypeDao;
import com.tspeiz.modules.common.entity.release2.PlatformHealthType;
@Repository
public class PlatformHealthTypeDaoImpl extends BaseDaoImpl<PlatformHealthType> implements IPlatformHealthTypeDao{

	@SuppressWarnings("unchecked")
	public List<PlatformHealthType> queryUseingPlatFormHealthTypes(
			Integer status) {
		// TODO Auto-generated method stub
		String hql = "from PlatformHealthType where 1=1 and status="+status;
		return this.hibernateTemplate.find(hql);
	}

}
