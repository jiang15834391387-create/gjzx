package org.smartlink.server.nc.oss.service.impl;

import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.server.nc.oss.abstractd.AbstractOssStrategy;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * 阿里云存储策略
 *
 * @author L
 */
@Component
public class AliyunOssStrategy extends AbstractOssStrategy {

    @Override
    public UploadResult upload(byte[] data, String path, String contentType) {
        return upload(new ByteArrayInputStream(data), path, contentType);
    }

//    @Override
//    public UploadResult upload(InputStream inputStream, String path, String contentType) {
//        String tok = "";
//        try {
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentType(contentType);
//            client.putObject(new PutObjectRequest(properties.getBucketName(), path, inputStream, metadata));
//        } catch (Exception e) {
//            throw new OssException("上传文件失败，请检查阿里云配置信息:[" + e.getMessage() + "]");
//        }
//        path = URLUtil.encode(path);
//        return UploadResult.builder().url(getEndpointLink() + "/" + path).filename(path).build();
//    }

    @Override
    public String getEndpointLink() {
        return "";
    }
}
