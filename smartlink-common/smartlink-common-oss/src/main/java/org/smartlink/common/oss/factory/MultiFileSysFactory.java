package org.smartlink.common.oss.factory;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    public UploadResult uploadSuffix(File file, String suffix) throws IOException {
        // 获取redis 默认类型
        String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
        if (StringUtils.isEmpty(configKey)) {
            throw new OssException("文件存储服务类型无法找到!");
        }
        // 判断是否润建公司文件服务器
        if (OssConstant.RUN_JIAN_CONFIG_KEY.equals(configKey)) {
            return getUploadResult(file);
        } else {
            OssClient storage = OssFactory.instance();
            UploadResult uploadResult = storage.uploadSuffix(file, suffix);
            return uploadResult;
        }

    }

    /**
     * 文件上传
     *
     * @param file
     * @param suffix
     * @return
     */
    public UploadResult uploadSuffix(MultipartFile file, String suffix) throws IOException {
        // 获取redis 默认类型
        String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
        if (StringUtils.isEmpty(configKey)) {
            throw new OssException("文件存储服务类型无法找到!");
        }
        // 判断是否润建公司文件服务器
        if (OssConstant.RUN_JIAN_CONFIG_KEY.equals(configKey)) {
            // 创建临时文件,用于传输
            File tempFile = File.createTempFile("temp", null);
            tempFile.deleteOnExit(); // 程序退出时删除临时文件
            // 使用Files.copy方法复制文件内容
            Files.copy(file.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return getUploadResult(tempFile);
        } else {
            OssClient storage = OssFactory.instance();
            UploadResult uploadResult;
            try {
                uploadResult = storage.uploadSuffix(file.getBytes(), suffix);
            } catch (IOException e) {
                throw new ServiceException(e.getMessage());
            }
            return uploadResult;
        }

    }
    /**
     * 文件上传
     *
     * @param fileBytes
     * @param suffix
     * @return
     */
    public UploadResult uploadSuffix(byte[] fileBytes, String suffix) throws IOException {
        // 获取redis 默认类型
        String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
        if (StringUtils.isEmpty(configKey)) {
            throw new OssException("文件存储服务类型无法找到!");
        }
        // 判断是否润建公司文件服务器
        if (OssConstant.RUN_JIAN_CONFIG_KEY.equals(configKey)) {
            //调用service去构建返回值
            File tempFile = File.createTempFile("temp", null);
            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                outputStream.write(fileBytes);
            }

            return getUploadResult(tempFile);
        } else {
            OssClient storage = OssFactory.instance();
            UploadResult uploadResult = storage.uploadSuffix(fileBytes, suffix);
            return uploadResult;
        }

    }

    private UploadResult getUploadResult(File file) throws IOException {
        //调用service去构建返回值
        Map map = strategyService.upload(file);
        Map dataMap = (Map) map.get("data");
        //通过文件id去查询文件预览路径
        String fileViewUrl = strategyService.fileViewUrl();
        UploadResult uploadResult = UploadResult.builder().url("").filename("").build();
        uploadResult.setUrl(fileViewUrl);
        uploadResult.setFilename(dataMap.get("originalFilename").toString());
        return uploadResult;
    }
}
