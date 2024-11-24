package org.smartlink.web.utils;

import cn.hutool.core.codec.Base64Decoder;
import org.smartlink.web.exception.NCServiceException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Program：imagesystem
 * @Description：Key工厂类
 * @Author： L
 * @Create:
 */

public class KeysFactory {




    public static Key getPublicKey(String pubKey) throws NCServiceException {
        Key key = null;
        try {
            byte[] keyBytes = Base64Decoder.decode(pubKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            key = keyFactory.generatePublic(x509KeySpec);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NCServiceException("无效的密钥  " + e.getMessage());
        }
        return key;
    }


    public static Key getSymKey(String symKey) throws Exception {
        Key key = null;
        try {
            byte[] keyBytes = Base64Decoder.decode(symKey);
            // Key转换
            key = new SecretKeySpec(keyBytes,"AES");
        } catch (Exception e) {
            throw new Exception("无效密钥 " + e.getMessage());
        }
        return key;
    }
}
