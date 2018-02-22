package com.tspeiz.modules.common.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.ISystemPushInfoDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.SystemPushInfo;

@Repository
public class SystemPushInfoDaoImpl extends BaseDaoImpl<SystemPushInfo> implements ISystemPushInfoDao{

	@SuppressWarnings("unchecked")
	public List<SystemPushInfo> querySystemPushInfoByUser(Integer userId) {
		// TODO Auto-generated method stub
		String filterPushCodes="301,302,303,311,312";
		String hql="from SystemPushInfo where userId="+userId+" and (isRead=0 or isRead IS NULL) "+" and pushCode not in("+filterPushCodes+") order by createTime desc";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public SystemPushInfo querySystemPushInfoByBusKey(String businessKey) {
		// TODO Auto-generated method stub
		String hql="from SystemPushInfo where businessKey='"+businessKey+"' ";
		List<SystemPushInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public SystemPushInfo querySystemPushInfoByPushKey(String pushKey) {
		// TODO Auto-generated method stub
		String hql="from SystemPushInfo where pushKey='"+pushKey+"' ";
		List<SystemPushInfo> list=this.hibernateTemplate.find(hql);
		if(list!=null&&list.size()>0)return list.get(0);
		return null;
	}
	
    @Override
    public Integer getUnReadPushInfo(Integer userId, Integer userType) {
        String sql = "SELECT count(Id) FROM system_push_info p" +
                " WHERE (p.IsRead IS NULL OR p.IsRead = 0) AND p.UserId = " + userId +
                " AND p.UserType = " + userType;
        Object num = this.executeSql(sql);
        return Integer.parseInt(num.toString());
    }


}
