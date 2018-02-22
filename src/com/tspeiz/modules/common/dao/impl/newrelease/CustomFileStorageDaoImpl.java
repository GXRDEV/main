package com.tspeiz.modules.common.dao.impl.newrelease;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.ICustomFileStorageDao;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;

@Repository
public class CustomFileStorageDaoImpl extends BaseDaoImpl<CustomFileStorage>
		implements ICustomFileStorageDao {

	@SuppressWarnings("unchecked")
	public List<CustomFileStorage> queryCustomFileStorageVedios(Integer oid) {
		// TODO Auto-generated method stub
		String hql="from CustomFileStorage where 1=1 and orderId="+oid;
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<CustomFileStorage> queryCustomFileStorageImages(
			String relatedPics) {
		// TODO Auto-generated method stub
		if(StringUtils.isNotBlank(relatedPics)){
			if(relatedPics.endsWith(",")){
				relatedPics = relatedPics.substring(0,relatedPics.length()-1);
				if(relatedPics.isEmpty()){
					return null;
				}
			}
		
			String hql="from CustomFileStorage where 1=1 and id in ("+relatedPics+") ";
			return this.hibernateTemplate.find(hql);
		}
		return null;
	}
}
