package org.smartlink.common.ocr.properties;

import lombok.Data;

@Data
public  class DetailInfo {
        private String url;
        private String appKey;
        private String appSecret;
        private String userName;
        private String pwdMd5;
        private String userSalt;
        private String taxNo;
    }
