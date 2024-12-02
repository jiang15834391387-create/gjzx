package org.smartlink.web.ocr.service.nccbip.resquest;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author L
 * @title bip查验
 * @description bip查验
 * @date
 */
@NoArgsConstructor
@Data
public class NccBipCheckRequest {

//    @JSONField(name = "ytenantid")
//    private String ytenantId;
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
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("fpDm")
        private String fpDm;
        @JsonProperty("fpHm")
        private String fpHm;
        @JsonProperty("kprq")
        private String kprq;
        @JsonProperty("hjje")
        private String hjje;
        @JsonProperty("jshj")
        private String jshj;
        @JsonProperty("jym")
        private String jym;
        @JsonProperty("electronic_number")
        private String electronic_number;
        @JsonProperty("billid")
        private String billid;
    }
}
