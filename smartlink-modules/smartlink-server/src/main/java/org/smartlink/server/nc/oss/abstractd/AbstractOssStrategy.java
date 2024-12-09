package org.smartlink.server.nc.oss.abstractd;

import cn.hutool.core.io.IoUtil;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.common.oss.properties.OssProperties;
import org.smartlink.server.nc.oss.service.IOssStrategy;

import java.io.InputStream;

/**
 * 对象存储策略(支持七牛、阿里云、腾讯云、minio)
 *
 * @author L
 */
public abstract class AbstractOssStrategy implements IOssStrategy {

    protected OssProperties properties;
    public boolean isInit = false;

    public void init(OssProperties properties) {
        this.properties = properties;
    }

    @Override
    public abstract UploadResult upload(byte[] data, String path, String contentType);

    @Override
    public UploadResult upload(InputStream inputStream, String path, String contentType) {
        byte[] data = IoUtil.readBytes(inputStream);
        return this.upload(data, path, contentType);
    }

//    @Override
//    public abstract UploadResult uploadSuffix(byte[] data, String suffix, String contentType);

//    @Override
//    public abstract UploadResult uploadSuffix(InputStream inputStream, String suffix, String contentType);

    /**
     * 获取域名访问链接
     *
     * @return 域名访问链接
     */
    public abstract String getEndpointLink();
}
