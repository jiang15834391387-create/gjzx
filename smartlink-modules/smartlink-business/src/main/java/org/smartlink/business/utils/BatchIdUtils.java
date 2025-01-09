package org.smartlink.business.utils;

import cn.hutool.core.util.StrUtil;
import org.smartlink.common.core.utils.DateUtils;

import java.io.File;
import java.util.UUID;

/**
 * @author shidunkai
 * @title batchId
 * @description batchId
 * @date 2022-04
 */
public class BatchIdUtils {
    public static String getNewBatchId() {
        String dateTimeNow = DateUtils.dateTime();
        return getUuID() + dateTimeNow.substring(0, 4) + getFullSequence(getDateString(), 12) + dateTimeNow.substring(4, 6) + getFullSequence("0", 4) + dateTimeNow.substring(6, 8);
    }


    private static String getUuID() {
        int len;
        len = 8;
        StringBuilder stringBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < len; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int strInteger = Integer.parseInt(str, 16);
            stringBuffer.append(CHARS[strInteger % 0x3E]);
        }
        return stringBuffer.toString();
    }
    /**
     * 字符串修改为指定位数
     *
     * @param str    需要补全字符串
     * @param length 需要修改的长度
     * @return String 修改后字符串
     */
    private static String getFullSequence(String str, int length) {
        int len = str.length();
        if (len > length) {
            return str.substring(len - length);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - len; i++) {
            sb.append("0");
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 返回文件路径
     * @param batchId 批次号
     * @return 文件地址
     */
    public static String getBatchIdPath(String batchId,String fileId){
        if(StrUtil.isNotEmpty(batchId) && batchId.length() > 30){
            return batchId.substring(8, 12) + File.separator + batchId.substring(24, 26) + File.separator + batchId.substring(30, 32) + File.separator + batchId + File.separator + fileId;
        }else{
            return batchId + File.separator + fileId;
        }

    }


    /**
     * 获取当前时间16进制字符串
     */
    private static String getDateString() {
        return Long.toHexString(System.currentTimeMillis());
    }

    private static final String[] CHARS = new String[]{
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
}
