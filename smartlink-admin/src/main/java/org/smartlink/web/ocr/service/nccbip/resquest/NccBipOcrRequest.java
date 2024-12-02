package org.smartlink.web.ocr.service.nccbip.resquest;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author L
 * @title 租户识别请求
 * @description 租户识别请求
 * @date
 */
@NoArgsConstructor
@Data
public class NccBipOcrRequest {

    /**
     * 租户id
     */
//    @JSONField(name = "ytenantid")
//    private String ytenantId;

    @JSONField(name = "uuid")
    private String uuId;
    @JSONField(name = "barcode")
    private String barcode;
    @JSONField(name = "opTime")
    private String opTime;
    @JSONField(name = "factorycode")
    private String factoryCode;
    @JSONField(name = "billid")
    private String billId;
    @JSONField(name = "data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JSONField(name = "file")
        private String file;
    }
}
