package org.smartlink.server.nc.oss.service.impl;

import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.common.oss.exception.OssException;
import org.smartlink.server.nc.oss.abstractd.AbstractOssStrategy;
import org.springframework.stereotype.Component;

/**
 * 七牛云存储策略
 *
 * @author L
 */
@Component
public class QiniuOssStrategy extends AbstractOssStrategy {

    private UploadManager uploadManager;
    private Auth auth;

    @Override
    public UploadResult upload(byte[] data, String path, String contentType) {
        try {
            String token = auth.uploadToken(properties.getBucketName());
            Response res = uploadManager.put(data, path, token, null, contentType, false);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.error);
            }
        } catch (Exception e) {
            throw new OssException("上传文件失败，请核对七牛配置信息:[" + e.getMessage() + "]");
        }
        return UploadResult.builder().url(getEndpointLink() + "/" + path).filename(path).build();
    }

    @Override
    public String getEndpointLink() {
        return "";
    }
}
