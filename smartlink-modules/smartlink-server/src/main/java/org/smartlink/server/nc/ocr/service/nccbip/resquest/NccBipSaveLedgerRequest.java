package org.smartlink.server.nc.ocr.service.nccbip.resquest;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author L
 * @title bip票据中心
 * @description bip票据中心
 * @date
 */
@NoArgsConstructor
@Data
public class NccBipSaveLedgerRequest {
    @JSONField(name = "uuid")
    private String uuId;
    @JSONField(name = "barcode")
    private String barCode;
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
        @JsonProperty("billType")
        private String billType;
        @JsonProperty("filePath")
        private String filePath;
        @JsonProperty("saveToken")
        private String saveToken;
        @JsonProperty("fpdm")
        private String fpdm;
        @JsonProperty("fphm")
        private String fphm;
        @JsonProperty("name")
        private String name;
        @JsonProperty("data")
        private SonDataDTO data;

        @NoArgsConstructor
        @Data
        public static class SonDataDTO {
            @JsonProperty("fpdm")
            private String fpdm;
            @JsonProperty("fphm")
            private String fphm;
            @JsonProperty("kprq")
            private String kprq;
            @JsonProperty("hjje")
            private String hjje;
            @JsonProperty("jshj")
            private String jshj;
            @JsonProperty("jym")
            private String jym;
            @JsonProperty("fplx")
            private String fplx;
            @JsonProperty("name")
            private String name;
            @JsonProperty("exit")
            private String exit;
            @JsonProperty("ticketNum")
            private String ticketNum;
        }
    }
}
