package com.tspeiz.modules.common.dao.impl.release2;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.IOperatorInvitCodeDao;
import com.tspeiz.modules.common.entity.release2.OperatorInvitCode;
@Repository
public class OperatorInvitCodeDaoImpl extends BaseDaoImpl<OperatorInvitCode> implements IOperatorInvitCodeDao{

	@SuppressWarnings("unchecked")
	public OperatorInvitCode queryOperatorInvitCode(Integer docid, String code) {
		// TODO Auto-generated method stub
		String hql = "from OperatorInvitCode where doctorId="+docid+" and code='"+code+"'";
		List<OperatorInvitCode> list = this.hibernateTemplate.find(hql);
		if(list != null && !list.isEmpty())return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public OperatorInvitCode queryDocIdByinvitCode(String invitationCode) {
		// TODO Auto-generated method stub
		String hql = "from OperatorInvitCode where code='"+invitationCode+"'";
		List<OperatorInvitCode> list = this.hibernateTemplate.find(hql);
		if(list != null && !list.isEmpty())return list.get(0);
		return null;
	}
	
}
