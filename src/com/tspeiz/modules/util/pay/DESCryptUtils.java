package com.tspeiz.modules.util.pay;

import java.security.Key;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class DESCryptUtils {

	private static String strDefaultKey = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";

	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;

	/**
	 * 默认构造方法，使用默认密钥
	 * 
	 * @throws Exception
	 */
	public DESCryptUtils() {
		this(strDefaultKey);
	}

	/**
	 * 指定密钥构造方法
	 * 
	 * @param strKey
	 *            指定的密钥
	 * @throws Exception
	 */
	public DESCryptUtils(String strKey) {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());

		try {
			Key key = getKey(strKey.getBytes());

			encryptCipher = Cipher.getInstance("DES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, key);

			decryptCipher = Cipher.getInstance("DES");
			decryptCipher.init(Cipher.DECRYPT_MODE, key);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 暗号化 明文入力String 密文出力
	 * 
	 * @param strMing
	 *            明文
	 * @return 密文
	 * @throws Exception
	 */
	public String getEncString(String strMing) {
		String result = "";
		try {
			result = byteArr2HexStr(encrypt(strMing.getBytes("UTF8")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return result;
	}

	/**
	 * 解密 String密文入力,String明文出力
	 * 
	 * @param strMi
	 *            密文
	 * @return 明文
	 * @throws Exception
	 */
	public String getDesString(String strMi) {
		String result = "";
		try {
			result = new String(decrypt(hexStr2ByteArr(strMi)), "UTF8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return result;
	}

	public String getDesStr(String strMi) {
		String ming = null;
		try {
			ming = new String(decrypt(hexStr2ByteArr(strMi)), "UTF8");
		} catch (Exception e) {
			ming = strMi;
		}
		return ming;
	}

	/**
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
	 * hexStr2ByteArr(String strIn) 互为可逆的转换过程
	 * 
	 * @param arrB
	 *            需要转换的byte数组
	 * @return 转换后的字符串
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 */
	private static String byteArr2HexStr(byte[] arrB) {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程
	 * 
	 * @param strIn
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 */
	private static byte[] hexStr2ByteArr(String strIn) {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	/**
	 * 加密字节数组
	 * 
	 * @param arrB
	 *            需加密的字节数组
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	private byte[] encrypt(byte[] arrB) {
		try {
			return encryptCipher.doFinal(arrB);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}

	/**
	 * 加密字符串
	 * 
	 * @param strIn
	 *            需加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private String encrypt(String strIn) {
		return byteArr2HexStr(encrypt(strIn.getBytes()));
	}

	/**
	 * 解密字节数组
	 * 
	 * @param arrB
	 *            需解密的字节数组
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	private byte[] decrypt(byte[] arrB) {
		try {
			return decryptCipher.doFinal(arrB);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}

	/**
	 * 解密字符串
	 * 
	 * @param strIn
	 *            需解密的字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private String decrypt(String strIn) {
		return new String(decrypt(hexStr2ByteArr(strIn)));
	}

	/**
	 * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
	 * 
	 * @param arrBTmp
	 *            构成该字符串的字节数组
	 * @return 生成的密钥
	 * @throws java.lang.Exception
	 */
	private Key getKey(byte[] arrBTmp) {
		// 创建一个空的8位字节数组（默认值为0）
		byte[] arrB = new byte[8];

		// 将原始字节数组转换为8位
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}

		// 生成密钥
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

		return key;
	}

	public static void main(String[] args) {
		DESCryptUtils des = new DESCryptUtils();
		String str1 = Integer.MAX_VALUE + "-" + Integer.MAX_VALUE;

		// DES加密
		String str2 = des.getEncString(str1);
		System.out.println("密文:" + str2 + "  length:" + str2.length());

		// DES解密
		String deStr = des.getDesString(str2);
		System.out.println("明文:" + deStr + "  length:" + deStr.length());
	}
}