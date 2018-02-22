package com.tspeiz.modules.common.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gexin.rp.sdk.base.IPushResult;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.ISystemPushInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IDoctorDetailInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IUserContactInfoDao;
import com.tspeiz.modules.common.dao.newrelease.IUserDevicesRecordDao;
import com.tspeiz.modules.common.entity.SystemPushInfo;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;
import com.tspeiz.modules.common.entity.newrelease.UserDevicesRecord;
import com.tspeiz.modules.common.service.IApiGetuiPushService;
import com.tspeiz.modules.util.getui.GetuiPushUtils;


@Service
public class ApiGetuiPushServiceImpl implements IApiGetuiPushService {

	@Resource
	private IUserDevicesRecordDao userDevicesRecordDao;
	@Resource
	private ISystemPushInfoDao systemPushInfoDao;
	@Resource
    private IDoctorDetailInfoDao doctorDetailInfoDao;
    @Resource
    private IUserContactInfoDao userContactInfoDao;

	public String PushMessage(SystemPushInfo systemPushInfo) {
		// TODO Auto-generated method stub
		// 验证参数
		if (null == systemPushInfo.getUserId()) {
			return "用户Id不能为空";
		}
		if (null == systemPushInfo.getUserType()) {
			return "用户类型不能为空";
		}
		if (null == systemPushInfo.getPushCode()) {
			return "推送用途不能为空";
		}
		String result="";
		// 验证设备信息
		UserDevicesRecord userDevicesRecord = userDevicesRecordDao.getLastLoginDevice(systemPushInfo.getUserId(), systemPushInfo.getUserType());
		if (null == userDevicesRecord) {
			result ="没有找到用户登录设备信息";
		}else{
			String pushId = userDevicesRecord.getPushId();
			if (StringUtils.isBlank(pushId)) {
				result= "没有找到用户设备的pushId";
			}else{
				// 推送
				 Integer appType = 0;
			        switch (systemPushInfo.getUserType()) {
			            case 1: // 患者
			                appType = 1;
			                break;
			            case 2: // 专家
			            case 3: // 医生
			                appType = 3;
			                break;
			        }
			    // 获取该用户设备未读推送条数
			    Integer badge = systemPushInfoDao.getUnReadPushInfo(systemPushInfo.getUserId(), systemPushInfo.getUserType());
			    systemPushInfo.setBadge(badge);
				IPushResult ret = GetuiPushUtils.PushMessage(pushId, appType, systemPushInfo);
				result=ret.getResponse().toString();
				
				result=ret.getResponse().get("result").toString();
			}
		}
		// 获得推送结果
		systemPushInfo.setPushResponse(result);
		// 保存推送结果记录
		systemPushInfoDao.update(systemPushInfo);
		return result.toUpperCase().equals("OK") ? "" : result;
	}
	
	 @Override
	    public Map<String, String> setPushDoctorExtend(Map<String, String> map, Integer doctorId) {
	        if (null == map) {
	            map = new HashMap<>();
	        }
	        if (null == doctorId) {
	            return map;
	        }
	        // 添加医生附属信息
	        MobileSpecial mobileSpecial = doctorDetailInfoDao.getDoctorDetailList(doctorId);
	        if (null != mobileSpecial) {
	            map.put("headImageUrl", mobileSpecial.getListSpecialPicture());
	            map.put("name", mobileSpecial.getSpecialName());
	            map.put("hosName", mobileSpecial.getHosName());
	            map.put("depName", mobileSpecial.getDepName());
	        }
	        return map;
	    }

	    @Override
	    public Map<String, String> setPushPatientExtend(Map<String, String> map, String subUserUuid) {
	        if (null == map) {
	            map = new HashMap<>();
	        }
	        if (StringUtils.isBlank(subUserUuid)) {
	            return map;
	        }
	        // 添加患者附属信息
	        UserContactInfo userContactInfo = userContactInfoDao.queryUserContactInfoByUuid(subUserUuid);
	        if (null != userContactInfo) {
	            map.put("headImageUrl", userContactInfo.getHeadImageUrl());
	            map.put("name", userContactInfo.getContactName());
	            if (null != userContactInfo.getSex()) {
	                map.put("sexString", userContactInfo.getSex() == 1 ? "男" : "女");
	            }
	            map.put("age", String.valueOf(userContactInfo.getAge()));
	        }
	        return map;
	    }


}
