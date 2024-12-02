package org.smartlink.web.domain.ybz.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * @author shidunkai
 * @title 友报账上传图片
 * @description 上传图片
 * @date 2022-06
 */
@Data
public class YbzUploadImageRequest {
    @JsonProperty("filename")
    private String filename;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("barcode")
    @NotBlank(message = "barcode不能为空")
    private String barcode;
    @JsonProperty("content")
    private String content;
    @JsonProperty("imgkey")
    private String imgkey;
    @JsonProperty("info")
    private InfoDTO info;

    @Data
    public static class InfoDTO {
        @JsonProperty("se")
        private String se;
        @JsonProperty("fpdm")
        private String fpdm;
        /**
         * 01  专票，04  普票，10  电子普票，11 卷式普票
         */
        @JsonProperty("fplx")
        private String fplx;
        @JsonProperty("gfsh")
        private String gfsh;
        @JsonProperty("totalamount")
        private String totalamount;
        @JsonProperty("fpjym")
        private String fpjym;
        @JsonProperty("kprq")
        private String kprq;
        @JsonProperty("canAddWatermark")
        private String canAddWatermark;
        @JsonProperty("ischecked")
        private String ischecked;
        @JsonProperty("xfsh")
        private String xfsh;
        @JsonProperty("je")
        private String je;
        @JsonProperty("fphm")
        private String fphm;

        /**
         * 购买方名称
         */
        @JsonProperty("gfmc")
        private String gfmc;

        /**
         * 购买方地址、电话
         */
        @JsonProperty("gfdz")
        private String gfdz;

        /**
         * 购买方开户行及账号
         */
        @JsonProperty("gfyhzh")
        private String gfyhzh;

        @JsonProperty("invoiceKey")
        private String invoiceKey;

        /**
         * 销售方开户行及账号
         */
        @JsonProperty("xfyhzh")
        private String xfyhzh;

        /**
         * 销售方名称
         */
        @JsonProperty("xfmc")
        private String xfmc;

        /**
         * 销售方地址、电话
         */
        @JsonProperty("xfdz")
        private String xfdz;
    }
}
