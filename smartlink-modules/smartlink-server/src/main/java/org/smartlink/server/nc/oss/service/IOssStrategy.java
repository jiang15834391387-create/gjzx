package org.smartlink.server.nc.oss.service;

import org.smartlink.common.oss.entity.UploadResult;

import java.io.InputStream;

/**
 * 对象存储策略
 *
 * @author L
 */
public interface IOssStrategy {

    /**
     * 文件上传
     *
     * @param data 文件字节数组
     * @param path 文件路径，包含文件名
     * @param contentType 文件类型
     * @return 返回http地址
     */
    UploadResult upload(byte[] data, String path, String contentType);

    /**
     * 文件上传
     *
     * @param inputStream 字节流
     * @param path        文件路径，包含文件名
     * @param contentType 文件类型
     * @return 返回http地址
     */
    UploadResult upload(InputStream inputStream, String path, String contentType);
}
