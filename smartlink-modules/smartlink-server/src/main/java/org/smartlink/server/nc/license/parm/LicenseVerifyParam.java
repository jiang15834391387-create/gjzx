/**
 * Copyright (c) 2016-Now http://www.j2eefast.com All rights reserved.
 * No deletion without permission
 */
package org.smartlink.server.nc.license.parm;

import lombok.Data;

import java.io.File;

@Data
public class LicenseVerifyParam {
    /**
     * 证书subject
     */
    private String subject;

    /**
     * 公钥别称
     */
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    private String storePass;

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    private String publicKeysStorePath;

    public LicenseVerifyParam() {
        this.subject = "CN=dataflyProduct, OU=北京数影互联科技有限公司, O=产品研发部, L=beijing, ST=beijing, C=CN";
        this.publicAlias = "dataflyPrivate";
        this.storePass = "dataflyAdmin2018";
        File directory = new File("");
        String absolutePath = directory.getAbsolutePath();
        this.licensePath = absolutePath+File.separator+("license.lic");
        this.publicKeysStorePath = absolutePath+File.separator+("dataflyPublicKeys.keystore");
        System.out.println("正在读取证书位置："+licensePath);
    }
}
