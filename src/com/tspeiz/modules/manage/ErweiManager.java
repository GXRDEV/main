package com.tspeiz.modules.manage;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.entity.newrelease.AppPcLogin;
import com.tspeiz.modules.common.service.ICommonService;

@Service
public class ErweiManager {
	@Autowired
	private ICommonService commonService;

	public String generateErInfo() {
		UUID uuid = UUID.randomUUID();
		String keyid = uuid.toString().replace("-", "");
		saveGlobleErFlag(keyid);
		return keyid;
	}

	/**
	 * 入库
	 * 
	 * @param keyid
	 */
	private void saveGlobleErFlag(String keyid) {
		AppPcLogin login = new AppPcLogin();
		login.setCreateTime(new Timestamp(System.currentTimeMillis()));
		login.setKeyId(keyid);
		commonService.saveAppPcLogin(login);
	}
}
