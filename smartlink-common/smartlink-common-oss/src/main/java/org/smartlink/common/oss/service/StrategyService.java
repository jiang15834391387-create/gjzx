package org.smartlink.common.oss.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * ClassName: StrategyService
 * Package: com.example.strategydemo.service
 * Description:
 *
 * @Author zzq
 * @Create 2024/2/2 20:16
 * @Version 1.0
 */
@Service
public interface StrategyService {
    /**
     * 上传文件到对象存储服务，并保存文件信息到数据库
     *
     * @param file 要上传的文件对象
     * @return 上传成功后的 SysOssVo 对象，包含文件信息
     */
    Map upload(File file) throws IOException;

    /**
     * 文件下载方法，支持一次性下载完整文件
     *
     * @param ossId    OSS对象ID
     * @param response HttpServletResponse对象，用于设置响应头和向客户端发送文件内容
     */
    void download(Long ossId, HttpServletResponse response) throws IOException;

    // TODO 文件关联业务主键
    void relObjectId() throws IOException;

    void fileInfo() throws IOException;

    String fileViewUrl() throws IOException;
}
