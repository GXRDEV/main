package com.tspeiz.modules.util;

import java.util.Date;
import java.util.Random;

public class CheckNumUtil {
	public static void main(String[] args) {
		System.out.println(randomChars_new(6));
	}
	public static String randomChars_new(int randomLength) {
		char[] randoms = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f',
				'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E','F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		Random random = new Random();
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < randomLength; i++) {
			ret.append(randoms[random.nextInt(randoms.length)]);
		}
		random = null;
		return ret.toString();
	}
	/**
	 * 生成随机字符串. <br>
	 * 随机字符串的内容包含[0-9]的字符. <br>
	 * 
	 * @param randomLength
	 *            随机字符串的长度
	 * @return 随机字符串.
	 */
	public static String randomChars(int randomLength) {
		char[] randoms = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		Random random = new Random();
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < randomLength; i++) {
			ret.append(randoms[random.nextInt(randoms.length)]);
		}
		random = null;
		return ret.toString();
	}

	public static String randowChars1(int randomLength) {

		Date d = new Date();
		long lseed = d.getTime();
		Random random = new Random(lseed);

		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < randomLength; i++) {
			ret.append(random.nextInt(9));
		}

		return ret.toString();
	}

	public static String getErrString(int errCode) {

		String errString = "";

		switch (errCode) {
		case 101:
			errString = "验证失败";
			break;
		case 102:
			errString = "短信不足";
			break;
		case 103:
			errString = "操作失败";
			break;
		case 104:
			errString = "非法字符";
			break;
		case 105:
			errString = "内容过多";
			break;
		case 106:
			errString = "号码过多";
			break;
		case 107:
			errString = "频率过快";
			break;
		case 108:
			errString = "号码内容空";
			break;
		case 109:
			errString = "账号冻结";
			break;
		case 110:
			errString = "禁止频繁单条发送";
			break;
		case 111:
			errString = "系统暂定发送";
			break;
		case 112:
			errString = "号码错误";
			break;
		case 113:
			errString = "定时时间格式不对";
			break;
		case 114:
			errString = "账号被锁，10分钟后登录";
			break;
		case 115:
			errString = "连接失败";
			break;
		case 116:
			errString = "禁止接口发送";
			break;
		case 117:
			errString = "绑定IP不正确";
			break;

		case 120:
			errString = "系统升级";
			break;
		default:
			break;
		}

		return errString;
	}

}
