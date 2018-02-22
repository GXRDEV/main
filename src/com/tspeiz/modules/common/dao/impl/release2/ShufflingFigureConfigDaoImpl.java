package com.tspeiz.modules.common.dao.impl.release2;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IShufflingFigureConfigDao;
import com.tspeiz.modules.common.entity.release2.ShufflingFigureConfig;

@Repository
public class ShufflingFigureConfigDaoImpl extends BaseDaoImpl<ShufflingFigureConfig> implements IShufflingFigureConfigDao{

	@SuppressWarnings("unchecked")
	public List<ShufflingFigureConfig> queryShufflingFigureConfigsByType(
			Integer type) {
		// TODO Auto-generated method stub
		String hql="from ShufflingFigureConfig where 1=1 and appType="+type+" and status=1 order by rank ";
		return this.hibernateTemplate.find(hql);
	}
	
}
