package org.smartlink.server.nc.ocr.service.glority;


import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.smartlink.server.nc.exception.AppException;
import org.smartlink.server.nc.ocr.service.glority.response.GlorityResult;

import java.util.Objects;

/**
 * <p>Title: CacheException</p>
 * <p>
 * <p>Description:OCR识别信息转换异常类 </p>
 *
 * @author
 * @date L
 **/
@Slf4j
public class ConversionException extends AppException {

    private final static String ERROR_RESULT_CODE = "0";

    private final static String YESFP_ERROR_RESULT_CODE = "0000";

    /**
     * 合合错误处理
     */
    private final static String HEHEINT_SUCCESS_RESULT_CODE = "200";


    /**
     * 创建新的缓存异常类 <code>ConversionException</code>.
     */
    public ConversionException() {
        super();
    }

    /**
     * 创建新的缓存异常类 <code>ConversionException</code>.
     *
     * @param message 异常原因信息 the reason for the exception.
     */
    public ConversionException(String message) {
        super(message);
    }

    /**
     * 创建新的缓存异常类 <code>ConversionException</code>.
     *
     * @param cause 底层根本原因 the underlying cause of the exception.
     */
    public ConversionException(Throwable cause) {
        super(cause);
    }

    /**
     * 创建新的缓存异常类 <code>ConversionException</code>.
     *
     * @param message 异常原因信息  the reason for the exception.
     * @param cause   底层根本原因  the underlying cause of the exception.
     */
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    @Contract("_ -> param1")
    public static GlorityResult checkGlorityResult(GlorityResult glorityResult) {


        if (ERROR_RESULT_CODE.equals(Objects.requireNonNull(glorityResult).getResult())) {
            throw new ConversionException("图片识别失败:" + glorityResult.getMessage());
        }
        return glorityResult;
    }

}
