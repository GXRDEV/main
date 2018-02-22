package com.tspeiz.modules.util.pay;

import com.tspeiz.modules.util.PropertiesUtil;

/**
 * 支付相关辅助类
 * 
 * @author liqi
 * 
 */
public class PayUtils {

	public static String ConvertOrderId(Integer orderId, String orderprev)
			throws Exception {
		DESCryptUtils des = new DESCryptUtils();
		return des.getEncString(orderprev + orderId);
	}

	public static Integer ConvertBackOrderId(String out_trade_no,String orderprev)
			throws Exception {
		DESCryptUtils des = new DESCryptUtils();
		return Integer.valueOf(
				(des.getDesString(out_trade_no).replace(orderprev, "")))
				.intValue();
	}

	/**
	 * 根据订单类型获取收费项目名称
	 * 
	 * @param typeId
	 * @return
	 */
	public static String GetOrderTypeStrById(int typeId) {
		switch (typeId) {
		case 1:
			return "图文问诊";
		case 2:
			return "电话问诊";
		case 3:
			return "视频问诊";
		case 4:
			return "远程门诊";
		default:
			return "佰医汇";
		}
	}

	public static void main(String[] args) throws Exception {
		/*
		 * DESCryptUtils des = new DESCryptUtils();
		 * 
		 * // DES加密
		 * 
		 * String str2 = ConvertOrderId(5249); String deStr =
		 * des.getDesString(str2);
		 * 
		 * String temp = des.getDesString(str2).replace(_OrderPrefix, "");
		 * Integer i = Integer.valueOf(
		 * (des.getDesString(str2).replace(_OrderPrefix, ""))).intValue();
		 * System.out.println("密文:" + str2); // DES解密
		 * 
		 * System.out.println("明文:" + deStr); System.out.println("明文2:" + i);
		 * System.out.println("明文3:" + temp);
		 */

		
		System.out
				.println(ConvertOrderId(Integer.MAX_VALUE,"o_w_"));
	}
}
