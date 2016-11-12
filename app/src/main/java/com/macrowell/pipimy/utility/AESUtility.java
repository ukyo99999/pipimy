package com.macrowell.pipimy.utility;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

public class AESUtility {
	private static final String ALGORITHM = "AES"; // 加密演算法使用AES
	private static final String TRANSFORMATION = "AES/CBC/PKCS7Padding"; // AES使用CBC模式與PKCS7Padding

	/**
	 * AES解密 帶入byte[]型態的16位英數組合文字、32位英數組合Key、需解密文字
	 * 
	 * @param iv
	 *            AES CBC模式使用的Initialization Vector
	 * @param key
	 *            AES加解密的密鑰
	 * @param encrypted
	 *            已加密的資料
	 * @return 傳回解密後的資料
	 * @throws Exception
	 */
	public static String decrypt(String iv, String key, String encrypted)
			throws Exception {
		AlgorithmParameterSpec algorithmParameterSpec = new IvParameterSpec(
				iv.getBytes());
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(),
				ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, algorithmParameterSpec);
		byte[] result = cipher.doFinal(hexToBytes(encrypted));

		return new String(result);

	}

	/**
	 * AES加密 帶入byte[]型態的16位英數組合文字、32位英數組合Key、需加密文字
	 * 
	 * @param iv
	 *            AES CBC模式使用的Initialization Vector
	 * @param key
	 *            AES加解密的密鑰
	 * @param uncrypted
	 *            傳入要加密的資料
	 * @return 傳回加密後的資料
	 * @throws Exception
	 */
	public static String encrypt(String iv, String key, String uncrypted)
			throws Exception {

		AlgorithmParameterSpec algorithmParameterSpec = new IvParameterSpec(
				iv.getBytes());
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(),
				ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, algorithmParameterSpec);
		byte[] rerult = cipher.doFinal(uncrypted.getBytes());

		return toHexString(rerult);

	}

	/**
	 * 將16進位字串轉為Byte
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] hexToBytes(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	/**
	 * Byte轉為16進位字串
	 * 
	 * @param buf
	 * @return
	 */
	public static String toHexString(byte[] buf) {
		String HEX = "0123456789ABCDEF";
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			result.append(HEX.charAt((buf[i] >> 4) & 0x0f)).append(
					HEX.charAt(buf[i] & 0x0f));
		}
		return result.toString();
	}

	/**
	 * AES加密(使用Base64編碼) 帶入byte[]型態的16位英數組合文字、32位英數組合Key、需加密文字
	 * 
	 * @param key1
	 * @param key2
	 * @param text
	 * @return
	 */
	public static synchronized String encryptAES(String key1, String key2,
			String text) {
		try {
			AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(
					key1.getBytes("UTF-8"));
			SecretKeySpec mSecretKeySpec = new SecretKeySpec(
					key2.getBytes("UTF-8"), "AES");
			Cipher mCipher = null;
			mCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			mCipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec,
					mAlgorithmParameterSpec);
			// byte[] ssdString = mCipher.doFinal(text.getBytes("UTF-8"));
			// String dsd = ssdString.toString();
			// return
			// Base64.encodeToString(mCipher.doFinal(text.getBytes("UTF-8")),Base64.DEFAULT).replace("\n",
			// "");
			return Base64.encodeToString(
					mCipher.doFinal(text.getBytes("UTF-8")), Base64.DEFAULT);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * AES解密 帶入byte[]型態的16位英數組合文字、32位英數組合Key、需解密文字
	 * 
	 * @param key1
	 * @param key2
	 * @param text
	 * @return
	 */
	public static synchronized String decryptAES(String key1, String key2,
			String text) {
		try {
			AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(
					key1.getBytes("UTF-8"));
			SecretKeySpec mSecretKeySpec = new SecretKeySpec(
					key2.getBytes("UTF-8"), "AES");
			Cipher mCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			mCipher.init(Cipher.DECRYPT_MODE, mSecretKeySpec,
					mAlgorithmParameterSpec);

			return new String(mCipher.doFinal(Base64.decode(
					text.getBytes("UTF-8"), Base64.DEFAULT)), "UTF-8");
		} catch (Exception ex) {
			return "";
		}
	}

}
