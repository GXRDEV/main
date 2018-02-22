package com.tspeiz.modules.util;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

public class PasswordUtil {
	/**
	 * 获取十六进制字符串形式的MD5摘要
	 */
	private static String md5Hex(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(src.getBytes());
			return new String(new Hex().encode(bs));
		} catch (Exception e) {
			return null;
		}
	}

	public static String MD5Salt(String password) {
		String psalt = md5Hex(password) + "cvYl8U";
		return md5Hex(psalt);
	}
	public static String MD5Salt(String password,String salt) {
		String psalt = md5Hex(password) + salt;
		return md5Hex(psalt);
	}
	
	public static void main(String[] args) {
		System.out.println(MD5Salt("123456","PdjwDr"));
	}
}
