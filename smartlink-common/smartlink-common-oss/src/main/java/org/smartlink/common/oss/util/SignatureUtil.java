package org.smartlink.common.oss.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignatureUtil {
    private static final char[] CHARS_TABLES = "0123456789abcdef".toCharArray();

    public static String signature(String appSecrete, String timestamp, String nonce,
                                   String uid) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(timestamp)
                .append(appSecrete)
                .append(nonce)
                .append(uid)
                .append(timestamp);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            return
                    toHexString(messageDigest.digest(stringBuilder.toString().getBytes(StandardCharsets.UTF_8
                    )));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHexString(byte[] aBytes) {
        char[] dst = new char[aBytes.length * 2];
        int si = 0;
        for (int di = 0; si < aBytes.length; ++si) {
            byte b = aBytes[si];
            dst[di++] = CHARS_TABLES[(b & 240) >>> 4];
            dst[di++] = CHARS_TABLES[b & 15];
        }
        return new String(dst);
    }
}