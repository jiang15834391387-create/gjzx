package org.smartlink.server.nc.oss.service.impl;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.Constants;
import org.smartlink.server.nc.constant.ParamConstants;
import org.smartlink.server.nc.oss.abstractd.AbstractOssStrategy;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author L
 */
@Component
@Slf4j
public class LocalDiskStrategy extends AbstractOssStrategy {

    String getPath= RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_LOCAL_LOCATION);

    @Override
    public UploadResult upload(byte[] data, String path, String contentType) {
        String fullPath = getPath+ File.separator+ path;
        FileUtil.writeBytes(data,fullPath);
        return UploadResult.builder().url(Constants.RESOURCE_PREFIX+File.separator+path).filename(path).build();
    }


    @Override
    public String getEndpointLink() {
        return "";
    }
}
