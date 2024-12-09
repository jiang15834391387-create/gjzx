package org.smartlink.server.nc.service.brecheck;


import org.smartlink.server.nc.domain.DataImageFilesInfo;

/**
 * @author shidunkai
 * @title 总校验
 * @description 总校验
 * @date 2022-11
 */
public interface BreCheckInvoiceService {


    /**
     * 图片预校验
     * @param dataImageFilesInfo 文件信息
     * @return 文件状态更新
     */
    public DataImageFilesInfo breCheckInvoice(DataImageFilesInfo dataImageFilesInfo, String orgCode) throws Exception;

}
