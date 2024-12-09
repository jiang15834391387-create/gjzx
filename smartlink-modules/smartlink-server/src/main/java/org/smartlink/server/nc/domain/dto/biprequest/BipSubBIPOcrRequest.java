package org.smartlink.server.nc.domain.dto.biprequest;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author L
 * @title
 * @description
 * @date
 */
@NoArgsConstructor
@Data
public class BipSubBIPOcrRequest {

//    @JSONField(name = "ytenantid")
//    private String ytenantid;
    @JSONField(name = "uuid")
    private String uuId;
    @JSONField(name = "barcode")
    private String barcode;
    @JSONField(name = "opTime")
    private String opTime;
    @JSONField(name = "factorycode")
    private String factorycode;
    @JSONField(name = "billid")
    private String billid;
    @JSONField(name = "data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JSONField(name = "imagestate")
        private String imagestate;
        @JSONField(name = "imagenum")
        private String imagenum;
        @JSONField(name = "invoicenum")
        private String invoicenum;
        @JSONField(name = "opuserid")
        private String opuserid;
        @JSONField(name = "opusercode")
        private String opusercode;
        @JSONField(name = "opusername")
        private String opusername;
        @JSONField(name = "optime")
        private String optime;
    }
}
