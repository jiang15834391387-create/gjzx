package org.smartlink.server.nc.oss.service.impl;

import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.server.nc.oss.abstractd.AbstractOssStrategy;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * 腾讯云存储策略
 *
 * @author L
 */
@Component
public class QcloudOssStrategy extends AbstractOssStrategy {

    @Override
    public UploadResult upload(byte[] data, String path, String contentType) {
        return upload(new ByteArrayInputStream(data), path, contentType);
    }


    @Override
    public String getEndpointLink() {
        return "";
    }
}
