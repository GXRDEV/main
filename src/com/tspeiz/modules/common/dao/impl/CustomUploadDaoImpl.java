package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.ICustomUploadDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.CustomUpload;
@Repository
public class CustomUploadDaoImpl extends BaseDaoImpl<CustomUpload> implements
		ICustomUploadDao {

	
	@SuppressWarnings("unchecked")
	public CustomUpload queryCustomUploadByUrl(String url) {
		// TODO Auto-generated method stub
		String hql="from CustomUpload  where urlPath='"+url+"' ";
		List<CustomUpload> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

}
