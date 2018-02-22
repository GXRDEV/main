package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.SystemBusinessDictionary;

public interface ISystemBusinessDictionaryDao extends BaseDao<SystemBusinessDictionary>{
	public List<SystemBusinessDictionary> querySystemBusinessDictionarysByGroup(Integer groupId);
	public SystemBusinessDictionary querySysDicByGroupAndCode(Integer group,Integer code);
}
