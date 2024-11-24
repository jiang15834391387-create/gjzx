package org.smartlink.web.utils;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.StrUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.MGF1ParameterSpec;

/**
 * @description: 加密工具类
 * @author: L
 * @create:
 **/
public class EncryptionUtil {

    private static final int MAX_ENCRYPT_BLOCK = 117;

    private static final String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/LX+Z4f+hPKhru0GaUeNOZVr5KYGRWgJMYyUcdf12M4/kIMnbnvsO3qvxqZNo6mUwI3tu1NMm8vGk+mJWyYVh0fPxlxvks4BXypzTIweuq7QLdpfj6JOKCwIh44S8ArgKRUZRdA1WBeb/impxXAEieBrq89hKU8aYZqUQTc4KIwIDAQAB";

    public static String pubEncrypt(String src) throws Exception {
//        JSONObject defaultPropertiesInfo = NcProperties.getDefaultPropertiesInfo();
//        if(ObjectUtil.isEmpty(defaultPropertiesInfo)){
//            throw new Exception("获取NCC参数配置缓存对象失败，缓存实例为空");
//        }
        String encryptType = "AES";
//        if(ObjectUtil.isEmpty(encryptType)){
//            throw new Exception("NCC版本加密类型字段缓存为空");
//        }
        String target = null;
        ByteArrayOutputStream out = null;
        try {
            Key key = KeysFactory.getPublicKey(pubKey);
            Cipher cipher;
            if(StrUtil.equals(encryptType,"AES")){
                cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, key,new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT));
            }else{
                cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            byte[] data = src.getBytes();
            int inputLen = data.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }

            target = Base64Encoder.encode(out.toByteArray());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new Exception("加密失败：" + e.getMessage());
        }finally{
            if(out != null){
                out.close();
            }
        }
        return target;
    }


    public static String symEncrypt(String strkey, String src) throws Exception {
        String target = null;
        try {
            Key key = KeysFactory.getSymKey(strkey);
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            IvParameterSpec iv = new IvParameterSpec(strkey.substring(0,16).getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, key,iv);
            byte[] encodeResult = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
            target = Base64Encoder.encode(encodeResult);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
            throw new Exception("加密失败：" + e.getMessage());
        }
        return target;
    }

}
