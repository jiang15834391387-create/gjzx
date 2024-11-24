package org.smartlink.web.utils;


import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class SHA256Util {

	private static final String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/LX+Z4f+hPKhru0GaUeNOZVr5KYGRWgJMYyUcdf12M4/kIMnbnvsO3qvxqZNo6mUwI3tu1NMm8vGk+mJWyYVh0fPxlxvks4BXypzTIweuq7QLdpfj6JOKCwIh44S8ArgKRUZRdA1WBeb/impxXAEieBrq89hKU8aYZqUQTc4KIwIDAQAB";

	/**
	 * @param str 加密字符串
	 * @return
	 */
	public static String getSignatureData(String str) throws Exception {
//		JSONObject defaultPropertiesInfo = NcProperties.getDefaultPropertiesInfo();
//		if(ObjectUtil.isEmpty(defaultPropertiesInfo)){
//			throw new Exception("获取NCC参数配置缓存对象失败，缓存实例为空");
//		}
		String encryptType = "AES";
//		if(ObjectUtil.isEmpty(encryptType)){
//			throw new Exception("NCC版本加密类型字段缓存为空");
//		}
		if(StrUtil.equalsAnyIgnoreCase(encryptType,"AES")){
			return getSHA256ForRandom(str);
		}else{
			return getSHA256(str+pubKey);
		}
	}

	private static String getSHA256ForRandom(String str) {
		str = str + pubKey;
		byte[] salt = new byte[16];
		try{
            SecureRandom random =SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(pubKey.getBytes());
            random.nextBytes(salt);
        }catch (NoSuchAlgorithmException e){
			ExceptionUtils.getMessage(e);
        }
        String salt_value= Base64Encoder.encode(salt);
		return getSHA256(str+salt_value.replaceAll("\r|\n",""));
	}

	private static String getSHA256(String str) {
		MessageDigest messageDigest;
		String encodestr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
			encodestr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encodestr;
	}

	private static String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				// 1寰楀埌涓?綅鐨勮繘琛岃ˉ0鎿嶄綔
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}

}
