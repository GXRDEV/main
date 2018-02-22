package com.tspeiz.modules.common.dao.impl.weixin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.weixin.IContactInfoDao;
import com.tspeiz.modules.common.entity.weixin.ContactInfo;

@Repository
public class ContactInfoDaoImpl extends BaseDaoImpl<ContactInfo> implements
		IContactInfoDao {

	
	@SuppressWarnings("unchecked")
	public ContactInfo queryByConditions(String openid, String username,
			String idcard, String telphone) {
		// TODO Auto-generated method stub
		String hql=" from ContactInfo where openId='"+openid+"' and userName='"+username+"' and idCard='"+idcard+"' and telphone='"+telphone+"' ";
		List<ContactInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ContactInfo> queryContactInfosByOpenId(String openid) {
		// TODO Auto-generated method stub
		String hql=" from ContactInfo where openId='"+openid+"' ";
		return this.hibernateTemplate.find(hql);
	}
	
}
