package org.smartlink.web.enums;

/**
 * NCC返回状态代码
 *
 * @author L
 * @date
 */
public enum NcCodeEnum {

    /**
     * 请求成功
     */
    NC_SUCCESS_STATE("0000","成功"),

    /**
     * 请求失败
     */
    NC_FAIL_STATE("1111","失败"),

    /**
     * 头部校验失败
     */
    NC_HEAD_CHECK_FAIL_STATE("1111","头部校验失败"),

    /**
     * nc单据不存在
     */
    NC_TASK_NO_EXISTS("1111","单据不存在"),

    /**
     *xml数据解析失败
     */
    NC_XML_ERROR("1111","xml数据解析失败"),

    /**
     * 未同步用户，机构，单据类型基础数据
     */
    NC_NOT_SYNC_DATA("1111","未同步业务系统基础数据"),

    /**
     * NCC接口图片未能识别ocr信息
     */
    NC_NOT_OCR_INFO("1001","识别为普通图片，请上传发票影像");



    private final String code;
    private final String codeName;

    NcCodeEnum(String code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }


    public String getCode() {
        return code;
    }

    public String getCodeName() {
        return codeName;
    }

}
