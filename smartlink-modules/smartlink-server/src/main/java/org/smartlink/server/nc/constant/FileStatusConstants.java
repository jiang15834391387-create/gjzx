package org.smartlink.server.nc.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 文件状态常量
 *
 * @author L
 */

public final class FileStatusConstants {
    public static List<String> getFileStatusConstants(){
        return Arrays.asList(IMAGE_SAVE_FAILED,INVOICE_ALREADY_EXISTS,OCR_SAVE_FAILED,MD5_CHECK_FAILED,INVOICE_SERIAL_NUMBERS,INVOICE_INFO_ERROR,INVOICE_HOLIDAY,INVOICE_SENSITIVE,INVOICE_SPECIAL_COMMODITY,INVOICE_CHECK_FAIL,INVOICE_SUPPLER,INVOICE_INCONSISTENT);
    }

    /**
     * 图片保存失败
     */
    public static final String IMAGE_SAVE_FAILED = "0";

    /**
     * 文件保存成功
     */
    public static final String SAVED_SUCCESSFULLY = "1";

    /**
     * 发票重复
     */
    public static final String INVOICE_ALREADY_EXISTS = "2";

    /**
     * 发票信息保存失败
     */
    public static final String OCR_SAVE_FAILED = "3";

    /**
     * 文件校验失败
     */
    public static final String MD5_CHECK_FAILED = "4";

    /**
     * 图片长度为零
     */
    public static final String IMAGE_SIZE_ZERO = "5";

    /**
     * 发票连号
     */
    public static final String INVOICE_SERIAL_NUMBERS = "6";

    /**
     * 查验成功
     */
    public static final String INVOICE_CHECK_SUCCESS = "7";

    /**
     * 发票抬头错误
     */
    public static final String INVOICE_INFO_ERROR = "8";

    /**
     * 节假日发票
     */
    public static final String INVOICE_HOLIDAY = "9";

    /**
     * 敏感词发票
     */
    public static final String INVOICE_SENSITIVE = "10";

    /**
     * 特殊商品发票
     */
    public static final String INVOICE_SPECIAL_COMMODITY = "11";

    /**
     * 发票验真失败
     */
    public static final String INVOICE_CHECK_FAIL = "12";

    /**
     * 抬头校验失败
     */
    public static final String INVOICE_SUPPLER = "13";

    /**
     * 发票修改成功
     */
    public static final String INVOICE_UPDATE = "14";

    /**
     * 发票打印号码不一致
     */
    public static final String INVOICE_INCONSISTENT="15";

    /**
     * 发票非发票联
     */
    public static final String INVOICE_FROMNUMBERERROR="16";

    /**
     * 匹配成功
     */
    public static final String MATCH_SUCCESSFUL = "28";
    /**
     * 匹配失败
     */
    public static final String MATCH_FAILED = "29";

    public static final HashMap<String,String> INVOICE_STATUS = new HashMap<>();
    static {
        INVOICE_STATUS.put(IMAGE_SAVE_FAILED,"图片保存失败");
        INVOICE_STATUS.put(INVOICE_ALREADY_EXISTS,"发票重复");
        INVOICE_STATUS.put(INVOICE_CHECK_FAIL,"发票验真失败");
        INVOICE_STATUS.put(OCR_SAVE_FAILED,"发票信息保存失败");
    }
}
