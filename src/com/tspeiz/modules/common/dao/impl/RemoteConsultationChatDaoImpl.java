package com.tspeiz.modules.common.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IRemoteConsultationChatDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.RemoteConsultationChat;

@Repository
public class RemoteConsultationChatDaoImpl extends
		BaseDaoImpl<RemoteConsultationChat> implements
		IRemoteConsultationChatDao {

	public List<RemoteConsultationChat> queryChats(Integer fromId,
			Integer toId, Integer start, final Integer length) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		final StringBuilder hqlBuilder = new StringBuilder();
		// select 语句
		hqlBuilder
				.append("from RemoteConsultationChat where 1= 1");
		hqlBuilder.append(" and (fromId = " + fromId + " and toId = " + toId + ")"); //自己发送的
		hqlBuilder.append(" and (fromId = " + toId + " and toId = " + fromId + ")"); //他人发送的
		hqlBuilder.append(" order by sendTime desc");
		
		List<RemoteConsultationChat> list = this.hibernateTemplate.find(hqlBuilder.toString());
	
		return list;
	}

	

}
