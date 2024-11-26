package org.smartlink.web.service.nc;

import java.util.List;

public interface IDataImageTreeService {

    /**
     * 影像扫描页面提交单据前判断自定义节点下是否有发票
     * @param productName 自定义节点名称集合
     * @return
     */
    boolean verifyImageTree(List<String> productName, String batchId);
}
