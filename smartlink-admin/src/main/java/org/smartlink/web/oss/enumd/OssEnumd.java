package org.smartlink.web.oss.enumd;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smartlink.web.oss.service.impl.*;

/**
 * 对象存储服务商枚举
 *
 * @author L
 */
@Getter
@AllArgsConstructor
public enum OssEnumd {

    /**
     * 七牛云
     */
    QINIU("qiniu", QiniuOssStrategy.class),

    /**
     * 阿里云
     */
    ALIYUN("aliyun", AliyunOssStrategy.class),

    /**
     * 腾讯云
     */
    QCLOUD("qcloud", QcloudOssStrategy.class),

    /**
     * minio
     */
    MINIO("minio", MinioOssStrategy.class),

    /**
     * FastDFS
     */
    FastDFS("fastdfs", FastDFSStrategy.class),

    /**
     * loaclDisk
     */
    loaclDisk("loacldisk", LocalDiskStrategy.class);

    private final String value;

    private final Class<?> beanClass;

    public static OssEnumd find(String value) {
        for (OssEnumd enumd : values()) {
            if (enumd.getValue().equals(value)) {
                return enumd;
            }
        }
        return null;
    }
}
