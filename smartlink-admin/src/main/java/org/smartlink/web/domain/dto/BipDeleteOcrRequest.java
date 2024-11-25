package org.smartlink.web.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author L
 * @title
 * @description
 * @date
 */
@NoArgsConstructor
@Data
public class BipDeleteOcrRequest {

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
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JSONField(name = "billType")
        private String billType;
        @JSONField(name = "imgOcrToken")
        private String imgOcrToken;
        @JSONField(name = "saveToken")
        private String saveToken;
        @JSONField(name = "data")
        private DataDTO.Invoice data;

        @NoArgsConstructor
        @Data
        public static class Invoice {
            @JSONField(name = "fpdm")
            private String fpdm;
            @JSONField(name = "fphm")
            private String fphm;
        }
    }
}
