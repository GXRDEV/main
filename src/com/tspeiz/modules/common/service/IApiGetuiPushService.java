package com.tspeiz.modules.common.service;

import java.util.Map;

import com.tspeiz.modules.common.entity.SystemPushInfo;

public interface IApiGetuiPushService {

	public String PushMessage(SystemPushInfo systemPushInfo);
	
     // 设置推送医生信息扩展信息
	public  Map<String, String> setPushDoctorExtend(Map<String, String> map, Integer doctorId);

     //设置推送患者信息扩展信息
    public Map<String, String> setPushPatientExtend(Map<String, String> map, String subUserUuid);
}
