package org.smartlink.server.nc.ocr.service.yesfp.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * <p>Title: YesfpPDFOcrRequest </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@AllArgsConstructor
@Data
@Builder
public class YesfpPDFOcrRequest {
    /**
     * 用户账号 是否必填 是
     */
    private String usercode;
    /**
     * 用户邮箱 是否必填 否
     */
    private String useremail;
    /**
     * 用户手机号 是否必填 否
     */
    private String usermobile;
    /**
     * 纳税人识别号
     */
    private String nsrsbh;
    /**
     * 组织编码
     */
    private String orgcode;
    /**
     * 默认组织模式
     */
    private String defaultOrgMode;

    private List<PdfFiles> pdfFiles;



}
