package com.tspeiz.modules.common.dao.newrelease;
import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.Dictionary;

public interface IDictionaryDao extends BaseDao<Dictionary>{
	public List<Dictionary> queryDictionarysByParentId(Integer pid);
	public Dictionary queryDictionaryByCon(String displayName,Integer parentId);
	public Integer queryCountBySql(String sql);
}
