package org.smartlink.server.nc.ocr.service;

/**
 * 查验记录统计Service接口
 *
 * @author
 * @date L
 */
public interface IDataCheckStatisticsService {

    /**
     * 插入查验记录
     *
     * @param checkSupplierEnum 厂商枚举
     * @param checkStatus       状态，成功或者失败
     * @return boolean
     */
    boolean insertCheckInfo(Integer checkSupplierEnum, String checkStatus);
}
