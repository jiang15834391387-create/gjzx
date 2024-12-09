package org.smartlink.server.nc.oss.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.server.nc.oss.abstractd.AbstractOssStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author L
 */
@Component
public class FastDFSStrategy extends AbstractOssStrategy {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Override
    public UploadResult upload(byte[] data, String path, String contentType) {
        return upload(new ByteArrayInputStream(data),path,contentType);
    }

    @Override
    public UploadResult upload(InputStream inputStream, String path, String contentType){
        long available =0;
        try {
            available = inputStream.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String substring = path.substring(path.lastIndexOf(".") + 1);
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, available, substring, null);
        String fullPath = storePath.getFullPath();
        return UploadResult.builder().url(getEndpointLink() + "/" + fullPath).filename(fullPath.substring(fullPath.lastIndexOf("/")+1)).build();
    }
    @Override
    public String getEndpointLink() {
        return "";
    }
}
