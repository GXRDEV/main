package com.tspeiz.modules.common.dao.impl.weixin;

import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.weixin.IUserFeedbackDao;
import com.tspeiz.modules.common.entity.weixin.UserFeedback;

@Repository
public class UserFeedbackDaoImpl extends BaseDaoImpl<UserFeedback> implements
		IUserFeedbackDao {

}
