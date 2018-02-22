package com.tspeiz.modules.common.dao.release2;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.ShufflingFigureConfig;

public interface IShufflingFigureConfigDao extends BaseDao<ShufflingFigureConfig>{
	public List<ShufflingFigureConfig> queryShufflingFigureConfigsByType(Integer type);
}
