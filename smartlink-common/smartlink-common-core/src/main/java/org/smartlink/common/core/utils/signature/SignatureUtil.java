package org.smartlink.common.core.utils.signature;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignatureUtil {

    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    public static String signature(String appSecret, String timestamp, String nonce, String uid) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(timestamp)
                     .append(appSecret)
                     .append(nonce)
                     .append(uid)
                     .append(timestamp);

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(stringBuilder.toString().getBytes("UTF-8"));
            return toHexString(digest);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to generate MD5 signature", e);
        }
    }

    private static String toHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_DIGITS[v >>> 4];
            hexChars[j * 2 + 1] = HEX_DIGITS[v & 0x0F];
        }
        return new String(hexChars);
    }
}