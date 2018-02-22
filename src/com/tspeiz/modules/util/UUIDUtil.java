package com.tspeiz.modules.util;

import java.util.UUID;

public class UUIDUtil {

	/**
	 * 得到UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
