package org.smartlink.web.service.nc;

import org.smartlink.common.core.domain.R;

public interface NcService {
    /**
     * 测试与NCC接口联通性
     * @return 返回结果
     */
    String testNcc();

    /**
     * 同步用户
     */
    R<Void> synchronizeUser();

    /**
     * 同步组织机构
     * @return
     */
    R<Void> synchronizeDepart();

    /**
     * 同步单据类型
     * @return
     */
    R<Void> synchronizeBillType();

    /**
     * 添加影像
     * @return
     */
    String addScanTask(String xml);

    /**
     * 单点登陆
     * @param xml 参数
     * @return 返回结果
     */
    String singleLogin(String xml);

    /**
     * 获取影像查看链接
     * @param xml 参数
     * @return 返回结果
     */
    String getImageShowUrl(String xml);
}
