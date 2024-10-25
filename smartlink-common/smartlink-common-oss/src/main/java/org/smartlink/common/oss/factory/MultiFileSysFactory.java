package org.smartlink.common.oss.factory;

import cn.hutool.core.util.ObjectUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.oss.constant.OssConstant;
import org.smartlink.common.oss.core.OssClient;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.common.oss.exception.OssException;
import org.smartlink.common.oss.service.StrategyService;
import org.smartlink.common.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: MultiFileSysFactory
 * Package: org.smartlink.common.oss.factory
 * Description:
 *
 * @Author 张志强
 * @Create 2024/9/25 1:42
 * @Version 1.0
 */
@Slf4j
@Component
public class MultiFileSysFactory {
    @Autowired
    private StrategyService strategyService;

    /**
     * 文件上传
     *
     * @param file
     * @param suffix
     * @return
     */
    public Map uploadSuffix(File file, String suffix,String uid) throws IOException {
        // 获取redis 默认类型
        String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
        if (StringUtils.isEmpty(configKey)) {
            throw new OssException("文件存储服务类型无法找到!");
        }
        // 判断是否润建公司文件服务器
        if (OssConstant.RUN_JIAN_CONFIG_KEY.equals(configKey)) {
            return getUploadResult(file,uid);
        } else {
            OssClient storage = OssFactory.instance();
            UploadResult uploadResult = storage.uploadSuffix(file, suffix);
            Map map = new HashMap();
            map.put("uploadResult", uploadResult);
            return map;
        }

    }

    /**
     * 文件上传
     *
     * @param file
     * @param suffix
     * @return
     */
    public Map uploadSuffix(MultipartFile file, String suffix,String uid) throws IOException {
        // 获取redis 默认类型
        String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
        if (StringUtils.isEmpty(configKey)) {
            throw new OssException("文件存储服务类型无法找到!");
        }
        // 判断是否润建公司文件服务器
        if (OssConstant.RUN_JIAN_CONFIG_KEY.equals(configKey)) {
            // 创建临时文件,用于传输
            File tempFile;
            try (InputStream in = file.getInputStream()) {
                // 创建一个临时文件
                tempFile = File.createTempFile("yxxt", suffix);
                Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            return getUploadResult(tempFile,uid);
        } else {
            OssClient storage = OssFactory.instance();
            UploadResult uploadResult;
            try {
                uploadResult = storage.uploadSuffix(file.getBytes(), suffix);
            } catch (IOException e) {
                throw new ServiceException(e.getMessage());
            }
            Map map = new HashMap();
            map.put("uploadResult", uploadResult);
            return map;
        }

    }

    /**
     * 文件上传
     *
     * @param fileBytes
     * @param suffix
     * @return
     */
    public Map uploadSuffix(byte[] fileBytes, String suffix,String uid) {
        // 获取redis 默认类型
        String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
        if (StringUtils.isEmpty(configKey)) {
            throw new OssException("文件存储服务类型无法找到!");
        }
        // 判断是否润建公司文件服务器
        if (OssConstant.RUN_JIAN_CONFIG_KEY.equals(configKey)) {
            try {
                //调用service去构建返回值
                File tempFile = File.createTempFile("temp", null);
                try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                    outputStream.write(fileBytes);
                }
                return getUploadResult(tempFile,uid);
            } catch (Exception e) {
                log.error("文件上传错误,流式转换异常:{}",e.toString());
                throw new OssException("文件上传错误,流式转换异常:{}" + e.getMessage());
            }
        } else {
            OssClient storage = OssFactory.instance();
            UploadResult uploadResult = storage.uploadSuffix(fileBytes, suffix);
            Map map = new HashMap();
            map.put("uploadResult", uploadResult);
            return map;
        }

    }

    //文件上传通用
    private Map getUploadResult(File file,String uid) {
        //调用service去构建返回值
        Map map = strategyService.upload(file,uid);
        //构建返回参数
        Map dataMap = (Map) map.get("data");
        //上传完文件后，根据类型编码关联交易类型
        String fileId = (String)dataMap.get("id");


        //通过文件id去查询文件预览路径
        String fileViewUrl = strategyService.fileViewUrl(uid);
        UploadResult uploadResult = UploadResult.builder().url("").filename("").build();
        uploadResult.setUrl(fileViewUrl);
        uploadResult.setFilename(dataMap.get("originalFilename").toString());
        dataMap.put("uploadResult", uploadResult);
        return dataMap;
    }


    public Long download(String ossId,String serviceName, String fileName, HttpServletResponse response, String uid) throws IOException {
        if (OssConstant.RUN_JIAN_CONFIG_KEY.equals(serviceName)) {
            //是润建服务器狭下载文件
            long contentLength = strategyService.download(ossId,response,uid);
            return contentLength;
        } else {
            OssClient storage = OssFactory.instance(serviceName);
            long contentLength = storage.download(fileName, response.getOutputStream());
            return contentLength;
        }
    }
    public byte[] downloadByte(String fileId,String serviceName, String fileName,String uid){
        if (OssConstant.RUN_JIAN_CONFIG_KEY.equals(serviceName)) {
            //是润建服务器狭下载文件
            strategyService.downloadByte(fileId,uid);
            //问题
            return new byte[0];
        } else {
            OssClient storage = OssFactory.instance(serviceName);
            if (ObjectUtil.isNotNull(storage)) {
                return storage.downloadByte(fileName);
            }
            return new byte[0];
        }
    }

    public void deleteWithValidById(String serviceName,String url) {
        if (OssConstant.RUN_JIAN_CONFIG_KEY.equals(serviceName)) {
        }else {
            OssClient storage = OssFactory.instance(serviceName);
            storage.delete(url);
        }
    }

    public void deleteWithValidByIds(String serviceName, String url) {
        if (OssConstant.RUN_JIAN_CONFIG_KEY.equals(serviceName)) {

        }else {
            OssClient storage = OssFactory.instance(serviceName);
            storage.delete(url);
        }
    }


}
