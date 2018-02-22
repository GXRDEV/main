package com.tspeiz.modules.common.dao.release2;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.release2.OperatorInvitCode;

public interface IOperatorInvitCodeDao extends BaseDao<OperatorInvitCode>{
	public OperatorInvitCode queryOperatorInvitCode(Integer docid,String code);

	public OperatorInvitCode queryDocIdByinvitCode(String invitationCode);
}
