package org.smartlink.business.scan.request;

import lombok.Data;

/**
 * @author: 史敦凯
 * @date: 2021/11/9 13:43
 * @description: 初始化参数
 */
@Data
public class InitializationRequest {
    /**
     * 流水
     */
    String businessSerialNo;
    /**
     * 扫描类型 1单扫  2批扫
     */
    String scanType;
    /**
     * 客户端ip
     */
    String ip;
    /**
     * 客户端mac地址
     */
    String mac;
    /**
     * 客户端富士通序列号
     */
    String serialNumber;
}
