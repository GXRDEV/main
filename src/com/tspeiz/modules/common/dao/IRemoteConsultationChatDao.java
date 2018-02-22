package com.tspeiz.modules.common.dao;

import java.util.List;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.RemoteConsultationChat;

public interface IRemoteConsultationChatDao extends
		BaseDao<RemoteConsultationChat> {

	public List<RemoteConsultationChat> queryChats(Integer fromId,
			Integer toId, Integer start, Integer length);

}
