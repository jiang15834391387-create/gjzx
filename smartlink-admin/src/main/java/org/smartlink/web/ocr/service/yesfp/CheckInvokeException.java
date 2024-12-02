package org.smartlink.web.ocr.service.yesfp;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.smartlink.web.ocr.service.yesfp.request.YesfpOcrSaveResult;

import java.util.Objects;

/**
 * <p>Title: CheckInvokeException </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Slf4j
public class CheckInvokeException extends RuntimeException {

    /**
     * yesfp3.0报销台账接口成功状态码
     */
    private final static String YESFP_ERROR_RESULT_CODE = "0000";

    /**
     * bip报销台账接口成功状态码
     */
    private final static String BIP_YESFP_ERROR_RESULT_CODE = "200";

    public CheckInvokeException(String message) {
        super(message);
    }

//    @Contract("_ -> param1")
//    public static YesfpCheckResult submitYesfpResult(YesfpCheckResult yesfpCheckResult) {
//        log.info("税务云增值税发票保存报销台账返回报文："+ JSONObject.toJSONString(yesfpCheckResult));
//        if (!YESFP_ERROR_RESULT_CODE.equals(Objects.requireNonNull(yesfpCheckResult).getCode())) {
//            log.error(JsonUtils.toJsonString(yesfpCheckResult));
//            throw new CheckInvokeException("税务云增值税发票保存到税务云报销台账失败:" + yesfpCheckResult.getMsg());
//        }
//        List<YesfpCheckData> data = yesfpCheckResult.getDatas();
//        if (null != data && data.size() > 0) {
//            if (!YESFP_ERROR_RESULT_CODE.equals(data.get(0).getCode())) {
//                log.error(JsonUtils.toJsonString(yesfpCheckResult));
//                throw new CheckInvokeException("税务云增值税发票保存到税务云报销台账失败:" + data.get(0).getMsg());
//            }
//        }
//        return yesfpCheckResult;
//    }

    @Contract("_ -> param1")
    public static YesfpOcrSaveResult yesfpOcrSave(YesfpOcrSaveResult yesfpOcrSaveResult) {
        if (!YESFP_ERROR_RESULT_CODE.equals(Objects.requireNonNull(yesfpOcrSaveResult).getCode()) && !BIP_YESFP_ERROR_RESULT_CODE.equals(Objects.requireNonNull(yesfpOcrSaveResult).getCode())) {
            log.error(JSONObject.toJSONString(yesfpOcrSaveResult));
            String message;
            if(StrUtil.isNotEmpty(yesfpOcrSaveResult.getMsg())){
                message = yesfpOcrSaveResult.getMsg();
            }else{
                message = yesfpOcrSaveResult.getMessage();
            }
            throw new CheckInvokeException("识别结果保存税务云台账失败:" + message);
        }
        return yesfpOcrSaveResult;
    }

}
