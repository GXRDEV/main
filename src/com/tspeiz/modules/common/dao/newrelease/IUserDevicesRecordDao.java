package com.tspeiz.modules.common.dao.newrelease;

import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.dao.base.BaseDao;
import com.tspeiz.modules.common.entity.newrelease.UserDevicesRecord;

/**
 * 用户登陆设备信息相关
 * 
 * @author liqi
 * 
 */
public interface IUserDevicesRecordDao extends BaseDao<UserDevicesRecord> {
	/**
	 * 获取最近一次登录设备信息
	 * 
	 * @param userId
	 * @param userType
	 * @return
	 */
	public UserDevicesRecord getLastLoginDevice(Integer userId, Integer userType);

	/**
	 * 获取用户登录设备信息
	 * 
	 * @param userId
	 * @param userType
	 * @param clientId
	 * @return
	 */
	public UserDevicesRecord queryUserDevice(Integer userId, Integer userType,
			String clientId);
	
	public Map<String,Object> queryexlogindatas(Map<String,Object> querymap,Integer start,Integer length);

	public List<UserDevicesRecord> getLoginList(String hosName, String hosid, String depid, String startDate, String endDate, String docName);

	public Map<String, Object> querydoclogindatas(Map<String, Object> querymap, Integer start, Integer length);
}
