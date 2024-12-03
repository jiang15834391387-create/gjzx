package org.smartlink.web.ocr.service.yesfp;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.smartlink.web.ocr.service.yesfp.reponse.YesfpDeleteResult;
import org.smartlink.web.ocr.service.yesfp.request.YesfpOcrSaveResult;

import java.util.Objects;

/**
 * <p>Title: ConversionException </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Slf4j
public class ConversionException extends RuntimeException {

    /**
     * yesfp3.0报销台账接口成功状态码
     */
    private final static String YESFP_ERROR_RESULT_CODE = "0000";
    /**
     * bip基础接口成功状态码
     */
    private final static String BIP_YESFP_BASE_ERROR_RESULT_CODE = "00000";

    /**
     * bip报销台账接口成功状态码
     */
    private final static String BIP_YESFP_ERROR_RESULT_CODE = "200";

    public ConversionException(String message) {
        super(message);
    }

    @Contract("_ -> param1")
    public static YesfpOcrSaveResult yesfpOcrSave(YesfpOcrSaveResult yesfpOcrSaveResult) {
        log.info("税务云非增值税发票保存报销台账返回报文："+ JSONObject.toJSONString(yesfpOcrSaveResult));
        if (!YESFP_ERROR_RESULT_CODE.equals(Objects.requireNonNull(yesfpOcrSaveResult).getCode()) && !BIP_YESFP_ERROR_RESULT_CODE.equals(Objects.requireNonNull(yesfpOcrSaveResult).getCode())) {
            log.error(JSONObject.toJSONString(yesfpOcrSaveResult));
            String message;
            if(StrUtil.isNotEmpty(yesfpOcrSaveResult.getMsg())){
                message = yesfpOcrSaveResult.getMsg();
            }else{
                message = yesfpOcrSaveResult.getMessage();
            }
            throw new ConversionException("税务云非增值税发票保存税务云台账失败:" + message);
        }
        return yesfpOcrSaveResult;
    }
    @Contract("_ -> param1")
    public static YesfpDeleteResult yesfpOcrDelete(YesfpDeleteResult yesfpDeleteResult) {
        if (!YESFP_ERROR_RESULT_CODE.equals(Objects.requireNonNull(yesfpDeleteResult).getCode()) && !BIP_YESFP_ERROR_RESULT_CODE.equals(Objects.requireNonNull(yesfpDeleteResult).getCode())) {
            log.error(JSONObject.toJSONString(yesfpDeleteResult));
            String message;
            if(StrUtil.isNotEmpty(yesfpDeleteResult.getMsg())){
                message = yesfpDeleteResult.getMsg();
            }else{
                message = yesfpDeleteResult.getMessage();
            }
            throw new ConversionException("报销台账删除失败:" + message);
        }
        return yesfpDeleteResult;
    }


    /**
     * 税务云异常处理方法
     * @param yesfpResult
     * @return
     */
//    @Contract("_ -> param1")
//    public static YesfpResult checkOcrYesfpResult(YesfpResult yesfpResult) {
//        String interFaceType = YesfpProperties.getDefaultYesfpProperties().getInterFaceType();
//        if(StrUtil.equals(interFaceType, Constants.BIP_YESFP_INTERFACE_TYPE)){
//            if (!BIP_YESFP_ERROR_RESULT_CODE.equals(Objects.requireNonNull(yesfpResult).getCode())) {
//                throw new ConversionException("bip税务云识别发票失败:" + yesfpResult.getMessage());
//            }
//        }else{
//            if (!YESFP_ERROR_RESULT_CODE.equals(Objects.requireNonNull(yesfpResult).getCode())) {
//                throw new ConversionException("税务云3.0识别发票失败:" + yesfpResult.getMsg());
//            }
//        }
//
//        return yesfpResult;
//    }

    /**
     * bip税务云获取接口地址异常处理方法
     * @param bipAddressResult
     * @return
     */
//    @Contract("_ -> param1")
//    public static BipAddressResult checkBipAddressResult(BipAddressResult bipAddressResult) {
//        if (!BIP_YESFP_BASE_ERROR_RESULT_CODE.equals(Objects.requireNonNull(bipAddressResult).getCode())) {
//            throw new com.datafly.ocr.respage.service.glority.ConversionException("BIP税务云获取接口失败:" + bipAddressResult.getMessage());
//        }
//        return bipAddressResult;
//    }

    /**
     * bip税务云获取获取token接口异常处理方法
     * @param openApiAccessTokenResult
     * @return
     */
//    @Contract("_ -> param1")
//    public static OpenApiAccessTokenResult checkOpenApiAccessTokenResult(OpenApiAccessTokenResult openApiAccessTokenResult) {
//        if (!BIP_YESFP_BASE_ERROR_RESULT_CODE.equals(Objects.requireNonNull(openApiAccessTokenResult).getCode())) {
//            throw new com.datafly.ocr.respage.service.glority.ConversionException("BIP税务云获取token接口失败:" + openApiAccessTokenResult.getMessage());
//        }
//        return openApiAccessTokenResult;
//    }
}
