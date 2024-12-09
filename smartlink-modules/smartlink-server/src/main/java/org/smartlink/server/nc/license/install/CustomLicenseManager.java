/**
 * Copyright (c) 2016-Now http://www.j2eefast.com All rights reserved.
 * No deletion without permission
 */
package org.smartlink.server.nc.license.install;

import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import lombok.extern.slf4j.Slf4j;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>自定义LicenseManager，用于增加额外的服务器硬件信息校验</p>

 */
@Slf4j
public class CustomLicenseManager extends LicenseManager {

    private static final int DEFAULT_BUFSIZE = 8 * 1024;


    public CustomLicenseManager() {

    }

    public CustomLicenseManager(LicenseParam param) {
        super(param);
    }



    /**
     * <p>
     *     校验当前服务器的IP地址是否在可被允许的IP范围内<br/>
     *     如果存在IP在可被允许的IP地址范围内，则返回true
     * </p>
     * @param expectedList 证书允许范围
     * @param serverList 服务器自身IP
     * @return
     */
    private boolean checkIpAddress(List<String> expectedList, List<String> serverList){

        if(expectedList != null && expectedList.size() > 0){
            if(serverList != null && serverList.size() > 0){
                for(String expected : expectedList){
                    if(serverList.contains(expected.trim())){
                        return true;
                    }
                }
            }
            return false;
        }else {
            return true;
        }
    }

    /**
     * <p>重写install方法</p>
     */
    @Override
    protected synchronized LicenseContent install(final byte[] key, final LicenseNotary notary) throws Exception {
        final GenericCertificate certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent)this.load(certificate.getEncoded());
        // 增加额外的自己的license校验方法，校验 机器码
        this.validate(content);
        setLicenseKey(key);
        setCertificate(certificate);
        return content;
    }


//    /**
//     * <p>重写validate方法，增加机器码与IP校验</p>
//     * */
//    @Override
//    protected synchronized void validate(final LicenseContent content)
//            throws LicenseContentException {
//
//        //1、 首先调用父类的validate方法
//        super.validate(content);
//
//        //2、 然后校验自定义的License参数
//        //License中可被允许的参数信息
//        HardwareMessageBody expectedCheck = (HardwareMessageBody) content.getExtra();
//
//        if(ObjectUtil.isNotEmpty(expectedCheck)){
//            if(StrUtil.isNotBlank(expectedCheck.getMainBoardSerial())){
//                OsInfo osInfo = SystemUtil.getOsInfo();
//                GetHardwareMessageInfo getHardwareMessageInfo;
//                String minNo="";
//                if (osInfo.isMac() || osInfo.isMacOsX()) {
//                    //mac
//                    getHardwareMessageInfo = new MacHardwareMessageInfo();
//                    minNo = getHardwareMessageInfo.getServerInfos().getMainBoardSerial();
//                } else if (osInfo.isWindows()) {
//                    //windows
//                    getHardwareMessageInfo = new WinHardwareMessageInfo();
//                    minNo = getHardwareMessageInfo.getServerInfos().getMainBoardSerial();
//                } else {
//                    //linux
//                    getHardwareMessageInfo = new LinuxHardwareMessageInfo();
//                    minNo = getHardwareMessageInfo.getServerInfos().getMainBoardSerial();
//                }
//                if(!expectedCheck.getMainBoardSerial().equalsIgnoreCase(minNo)){
//                    log.error("证书无效，当前服务器的注册码未激活!+"+expectedCheck.getMainBoardSerial()+"+"+minNo);
//                    throw new LicenseContentException("证书无效，当前服务器的注册码未激活!"+expectedCheck.getMainBoardSerial()+"+"+minNo);
//                }
//            }
////            else{
////                log.error("证书无效，当前服务器的注册码未激活!");
////                throw new LicenseContentException("证书无效，当前服务器的注册码未激活!");
////            }
//        }else{
//            log.error("证书无效或不能获取服务器硬件信息");
//            throw new LicenseContentException("证书无效或不能获取服务器硬件信息");
//        }
//    }

//    /**
//     * <p>重写verify方法</p>
//     */
//    @Override
//    protected synchronized LicenseContent verify(final LicenseNotary notary)
//            throws Exception {
//
//        // Load license key from preferences,
//        final byte[] key = getLicenseKey();
//        if (null == key){
//            throw new NoLicenseInstalledException(getLicenseParam().getSubject());
//        }
//
//        GenericCertificate certificate = getPrivacyGuard().key2cert(key);
//        notary.verify(certificate);
//        final LicenseContent content = (LicenseContent)this.load(certificate.getEncoded());
//        // 增加额外的自己的license校验方法，校验 机器码等
//        this.validate(content);
//        setCertificate(certificate);
//        return content;
//    }

    /**
     * <p>重写XMLDecoder解析XML</p>
     */
    private Object load(String encoded){

        BufferedInputStream inputStream = null;
        XMLDecoder decoder = null;
        try {
            inputStream = new BufferedInputStream(new ByteArrayInputStream(encoded.getBytes(StandardCharsets.UTF_8)));
            decoder = new XMLDecoder(new BufferedInputStream(inputStream),null,null);
            Object o = decoder.readObject();
            return o;
        } finally {
            try {
                if(decoder != null){
                    decoder.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (Exception e) {
                log.error("XMLDecoder解析XML失败",e);
            }
        }
    }

//    /**
//     * 最大在线人数校验
//     *
//     * @return
//     * @throws Exception
//     */
//    public synchronized String onlieVerify() throws Exception {
//        final byte[] key = getLicenseKey();
//        if (null == key){
//            throw new NoLicenseInstalledException(getLicenseParam().getSubject());
//        }
//        GenericCertificate certificate = getPrivacyGuard().key2cert(key);
//        LicenseContent content = (LicenseContent)this.load(certificate.getEncoded());
//        HardwareMessageBody expectedCheck = (HardwareMessageBody) content.getExtra();
//        if(StrUtil.isNotBlank(expectedCheck.getMiniSum())){
//            return expectedCheck.getMiniSum();
//        }
//        return "0";
//    }
}
