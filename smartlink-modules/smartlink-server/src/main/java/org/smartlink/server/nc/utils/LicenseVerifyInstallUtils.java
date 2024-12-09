package org.smartlink.server.nc.utils;


import de.schlichtherle.license.LicenseContent;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.server.nc.license.install.LicenseVerify;
import org.smartlink.server.nc.license.parm.LicenseVerifyParam;

/**
 * @author shidunkai
 * @title 证书安装工具
 * @description 证书安装工具
 * @date 2023-01
 */
@Slf4j
public class LicenseVerifyInstallUtils {

    public LicenseContent getMessage(LicenseVerifyParam param) throws Exception {
        //安装
        log.info("++++++++ 开始安装证书 ++++++++");
        LicenseVerify licenseVerify = new LicenseVerify();
        //安装证书
        LicenseContent install = licenseVerify.install(param);
        //验证证书唯一码是否有效
        log.info("++++++++ 证书安装结束 ++++++++");
        return install;
    }
}
