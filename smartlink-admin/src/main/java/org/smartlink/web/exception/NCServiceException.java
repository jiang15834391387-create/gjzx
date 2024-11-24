package org.smartlink.web.exception;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.web.ncc.deleteocr.response.DeleteInvoiceResponse;
import org.smartlink.web.ncc.nccor.response.allinvoice.UploadAllInvoiceResponse;
import org.smartlink.web.ncc.nccor.response.allinvoice.UploadElectronicInvoiceResponse;
import org.smartlink.web.ncc.nccor.response.taxinvoice.UploadElectronicInvoiceNonStandardResponse;
import org.smartlink.web.ncc.nccor.response.taxinvoice.UploadTaxInvoiceResponse;
import org.smartlink.web.ncc.nccstand.response.allinvoice.StandAllInvoiceResponse;
import org.smartlink.web.ncc.nccstand.response.allinvoice.StandAllInvoiceResponseData;
import org.smartlink.web.ncc.nccstand.response.taxinvoice.StandTaxInvoiceResponse;
import org.smartlink.web.ncc.nccstand.response.taxinvoice.StandTaxInvoiceResponseData;
import org.smartlink.web.ncc.nccverify.response.VerifyTaxInvoiceResponse;
import org.smartlink.web.ncc.nccverify.response.VerifyTaxInvoiceResponseData;
import org.smartlink.web.ncc.syncocr.response.allinvoice.SyncAllInvoiceResponse;
import org.smartlink.web.ncc.syncocr.response.taxinvoice.SyncTaxInvoiceResponse;
import org.smartlink.web.ncc.testapi.response.TestOpenApiResponse;
import org.smartlink.web.token.response.Token;

import java.util.List;
import java.util.Objects;

/**
 * NCC业务异常处理·类
 *
 * @author L
 */
@Slf4j
public class NCServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    /**
     * 返回结果成功
     */
    private final static String NCC_SUCCESS_RESULT_CODE = "0000";

    /**
     * 返回结果失败
     */
    private final static String NCC_ERROR_TOKEN_CODE = "false";

    /**
     * 返回结果成功
     */
    private final static String NCC_SUCCESS_TOKEN_CODE = "true";


    public NCServiceException(String msg) {
        super(msg);
    }

    /**
     * NCC获取token结果异常处理
     * @param token
     * @return
     */
    @Contract("_ -> param1")
    public static Token getNCCTokenResult(Token token) {
        if (StrUtil.equals(NCC_ERROR_TOKEN_CODE,token.getSuccess())) {
            log.error("获取业务系统Token失败,失败原因："+ JsonUtils.toJsonString(token));
            throw new NCServiceException("获取业务系统Token失败,失败原因：" + token.getMessage());
        }
        return token;
    }

    /**
     * NCC增值税OCR识别接口结果异常处理
     * @param uploadTaxResponse
     * @return
     */
    @Contract("_ -> param1")
    public static UploadTaxInvoiceResponse getNccOcrResult(UploadTaxInvoiceResponse uploadTaxResponse) {
        if (!StrUtil.equals(NCC_SUCCESS_RESULT_CODE,uploadTaxResponse.getCode())) {
            log.error("请求业务系统OCR识别接口失败，失败原因："+uploadTaxResponse.getMessage());
            throw new NCServiceException("请求业务系统OCR识别接口失败，失败原因：" + uploadTaxResponse.getMessage());
        }
        return uploadTaxResponse;
    }

    /**
     * NCC全票种OCR识别接口结果异常处理
     * @param uploadAllInvoiceResponse
     * @return
     */
    @Contract("_ -> param1")
    public static UploadAllInvoiceResponse getNccOcrResult(UploadAllInvoiceResponse uploadAllInvoiceResponse) {
        if (!StrUtil.equals(NCC_SUCCESS_RESULT_CODE,uploadAllInvoiceResponse.getCode())) {
            log.error("请求业务系统全票种OCR识别接口失败，失败原因："+uploadAllInvoiceResponse.getMessage());
            throw new NCServiceException("请求业务系统全票种OCR识别接口失败，失败原因：" + uploadAllInvoiceResponse.getMessage());
        }
        return uploadAllInvoiceResponse;
    }

    /**
     * NCC测试OPEN API接口结果异常处理
     * @param testOpenApiResponse
     * @return
     */
    @Contract("_ -> param1")
    public static TestOpenApiResponse getNccOpenApiResult(TestOpenApiResponse testOpenApiResponse) {
        if (!StrUtil.equals(NCC_SUCCESS_TOKEN_CODE,testOpenApiResponse.getSuccess())) {
            log.error("请求业务系统测试OPEN API接口失败，失败原因："+testOpenApiResponse.getMessage());
            throw new NCServiceException("请求业务系统测试OPEN API接口失败，失败原因：" + testOpenApiResponse.getMessage());
        }
        return testOpenApiResponse;
    }

    /**
     * NCC增值税OCR识别接口结果异常处理
     * @param verifyTaxInvoiceResponse
     * @return
     */
    @Contract("_ -> param1")
    public static VerifyTaxInvoiceResponse checkVerifyResult(VerifyTaxInvoiceResponse verifyTaxInvoiceResponse) {

        if (!NCC_SUCCESS_RESULT_CODE.equals(Objects.requireNonNull(verifyTaxInvoiceResponse).getCode())) {
            throw new NCServiceException("业务系统返回查验结果信息:" + verifyTaxInvoiceResponse.getMessage());
        }
        List<VerifyTaxInvoiceResponseData> verifyTaxInvoiceResponseDataList = Objects.requireNonNull(verifyTaxInvoiceResponse).getData();
        verifyTaxInvoiceResponseDataList.forEach(e -> {
            if (!NCC_SUCCESS_RESULT_CODE.equals(e.getCode())) {
                throw new NCServiceException("业务系统返回查验结果信息:" + e.getMsg());
            }
        });
        return verifyTaxInvoiceResponse;
    }

    /**
     * NCC增值税OCR台账保存接口结果异常处理
     * @param taxInvoiceResponse
     * @return
     */
    @Contract("_ -> param1")
    public static StandTaxInvoiceResponse checkSaveTaxInvoiceResult(StandTaxInvoiceResponse taxInvoiceResponse) {

        if (!NCC_SUCCESS_RESULT_CODE.equals(Objects.requireNonNull(taxInvoiceResponse).getCode())) {
            throw new NCServiceException("业务系统保存OCR信息失败:" + taxInvoiceResponse.getMessage());
        }
        List<StandTaxInvoiceResponseData> data = Objects.requireNonNull(taxInvoiceResponse).getData();
        data.forEach(e -> {
            if (!NCC_SUCCESS_RESULT_CODE.equals(e.getCode())) {
                throw new NCServiceException("业务系统保存OCR信息失败:" + e.getMsg());
            }
        });
        return taxInvoiceResponse;
    }


    /**
     * NCC全票种OCR台账保存接口结果异常处理
     * @param standAllInvoiceResponse
     * @return
     */
    @Contract("_ -> param1")
    public static StandAllInvoiceResponse checkSaveAllInvoiceResult(StandAllInvoiceResponse standAllInvoiceResponse) {

        if (!NCC_SUCCESS_RESULT_CODE.equals(Objects.requireNonNull(standAllInvoiceResponse).getCode())) {
            throw new NCServiceException("业务系统保存OCR信息失败:" + standAllInvoiceResponse.getMessage());
        }
        List<StandAllInvoiceResponseData> data = Objects.requireNonNull(standAllInvoiceResponse).getData();
        data.forEach(e -> {
            if (!NCC_SUCCESS_RESULT_CODE.equals(e.getCode())) {
                throw new NCServiceException("业务系统保存OCR信息失败:" + e.getMsg());
            }
        });
        return standAllInvoiceResponse;
    }

    /**
     * NCC发票台账删除接口结果异常处理
     * @param taxInvoiceResponse
     * @return
     */
    @Contract("_ -> param1")
    public static DeleteInvoiceResponse deleteInvoiceResult(DeleteInvoiceResponse taxInvoiceResponse) {
        if (!NCC_SUCCESS_RESULT_CODE.equals(Objects.requireNonNull(taxInvoiceResponse).getCode())) {
            throw new NCServiceException("业务系统删除发票台账失败:" + taxInvoiceResponse.getMessage());
        }
        return taxInvoiceResponse;
    }

    /**
     * NCC增值税OCR同步接口结果异常处理
     * @param syncTaxInvoiceResponse
     * @return
     */
    @Contract("_ -> param1")
    public static SyncTaxInvoiceResponse checkSyncTaxInvoiceResult(SyncTaxInvoiceResponse syncTaxInvoiceResponse) {
        if (!NCC_SUCCESS_RESULT_CODE.equals(Objects.requireNonNull(syncTaxInvoiceResponse).getCode())) {
            throw new NCServiceException("NCC同步增值税OCR失败:" + syncTaxInvoiceResponse.getMessage());
        }
        return syncTaxInvoiceResponse;
    }

    /**
     * NCC全票种OCR同步接口结果异常处理
     * @param syncAllInvoiceResponse
     * @return
     */
    @Contract("_ -> param1")
    public static SyncAllInvoiceResponse checkSyncAllInvoiceResult(SyncAllInvoiceResponse syncAllInvoiceResponse) {
        if (!NCC_SUCCESS_RESULT_CODE.equals(Objects.requireNonNull(syncAllInvoiceResponse).getCode())) {
            throw new NCServiceException("NCC同步增值税OCR失败:" + syncAllInvoiceResponse.getMessage());
        }
        return syncAllInvoiceResponse;
    }

    /**
     * NCC2207电子发票上传结果异常处理
     * @param uploadElectronicInvoiceResponse
     * @return
     */
    @Contract("_ -> param1")
    public static UploadElectronicInvoiceResponse checkElectronicSaveResult(UploadElectronicInvoiceResponse uploadElectronicInvoiceResponse) {
        if (!NCC_SUCCESS_RESULT_CODE.equals(Objects.requireNonNull(uploadElectronicInvoiceResponse).getCode())) {
            log.error("业务系统返回电子发票保存台账信息:" + uploadElectronicInvoiceResponse.getMessage());
            throw new NCServiceException("业务系统返回电子发票保存台账信息:" + uploadElectronicInvoiceResponse.getMessage());
        }
        return uploadElectronicInvoiceResponse;
    }

    /**
     * NCC2111电子发票上传结果异常处理
     * @param uploadElectronicInvoiceNonStandardResponse
     * @return
     */
    @Contract("_ -> param1")
    public static UploadElectronicInvoiceNonStandardResponse checkElectronicSaveResult(UploadElectronicInvoiceNonStandardResponse uploadElectronicInvoiceNonStandardResponse) {
        if (!NCC_SUCCESS_RESULT_CODE.equals(Objects.requireNonNull(uploadElectronicInvoiceNonStandardResponse).getCode())) {
            log.error("业务系统返回电子发票保存台账信息:" + uploadElectronicInvoiceNonStandardResponse.getMessage());
            throw new NCServiceException("业务系统返回电子发票保存台账信息:" + uploadElectronicInvoiceNonStandardResponse.getMessage());
        }
        return uploadElectronicInvoiceNonStandardResponse;
    }

}
