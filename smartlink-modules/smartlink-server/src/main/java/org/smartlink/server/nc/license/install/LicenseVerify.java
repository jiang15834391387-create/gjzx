/**
 * Copyright (c) 2016-Now http://www.j2eefast.com All rights reserved.
 * No deletion without permission
 */
package org.smartlink.server.nc.license.install;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileUtil;
import de.schlichtherle.license.*;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.server.nc.license.parm.CustomKeyStoreParam;
import org.smartlink.server.nc.license.parm.LicenseVerifyParam;

import java.text.MessageFormat;
import java.util.prefs.Preferences;


@Slf4j
public class LicenseVerify {


    /**
     * <p>安装License证书</p>
     */
    public synchronized LicenseContent install(LicenseVerifyParam param) throws Exception {
        LicenseContent result = null;
        //1. 安装证书
        LicenseManager licenseManager =new CustomLicenseManager(initLicenseParam(param));
        // 先卸载证书 == 给null
        licenseManager.uninstall();
        // 安装
        result = licenseManager.install(FileUtil.file(param.getLicensePath()));
        log.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}",DatePattern.NORM_DATETIME_FORMAT.format(result.getNotBefore()),
                DatePattern.NORM_DATETIME_FORMAT.format(result.getNotAfter())));
        return result;
    }

    /**
     * <p>初始化证书生成参数</p>
     * @param param License校验类需要的参数
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param){

        Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);
        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());
        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class
                ,param.getPublicKeysStorePath()
                ,param.getPublicAlias()
                ,param.getStorePass()
                ,null);

        return new DefaultLicenseParam(param.getSubject()
                ,preferences
                ,publicStoreParam
                ,cipherParam);
    }
//
//
//    /**
//     * <p>校验License证书</p>
//     */
//    public boolean verify(LicenseVerifyParam param){
//
//        LicenseManager licenseManager = new CustomLicenseManager(initLicenseParam(param));
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        //2. 校验证书
//        try {
//            LicenseContent licenseContent = licenseManager.verify();
//            log.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}",format.format(licenseContent.getNotBefore()),format.format(licenseContent.getNotAfter())));
//            return true;
//        }catch (Exception e){
//            return false;
//        }
//    }
//
//    /**
//     * 最大在线人数校验
//     * @param param
//     * @return
//     */
//    public String onlineNumVerify(LicenseVerifyParam param){
//        try{
//            return  new CustomLicenseManager(initLicenseParam(param)).onlieVerify();
//        }catch (Exception e){
//            return "1";
//        }
//    }
}
