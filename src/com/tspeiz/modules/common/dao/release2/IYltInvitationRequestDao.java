package com.tspeiz.modules.common.dao.release2;

import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.YltInvitationRequest;

public interface IYltInvitationRequestDao extends BaseDao<YltInvitationRequest>{
	public Map<String,Object> queryinvitjoin_hos(Integer hosId,String search,Integer start,Integer length,Integer otype);
}
