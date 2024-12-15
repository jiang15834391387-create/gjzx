package org.smartlink.server.nc.service.nc;

import org.smartlink.common.core.domain.R;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.task.momain.DataTask;


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
     * 获取代办任务数量
     * @param xml 参数
     * @return 返回结果
     */
    String getCurrentTaskCount(String xml);

    /**
     * 添加影像
     * @return
     */
    String addScanTask(String xml);

    /**
     * 删除影像任务
     * @param xml 参数
     * @return 返回结果
     */
    String deleteScanTask(String xml);
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

    /**
     * 更新单据号
     * @param xml 参数
     * @return 返回结果
     */
    String updateBillNo(String xml);

    /**
     * 提供给电子档案下载接口
     * @param xml 参数
     * @return 返回结果
     */
    String downloadImages(String xml);

    /**
     * 驳回影像状态
     * @param xml 参数
     * @return 返回结果
     */
    String rejectImageOnBillReject(String xml);

    /**
     * 收票文件上传节点获取影像扫描链接
     * @param xml 参数
     * @return 返回结果
     */
    String fileScan(String xml);

    /**
     * 凭证节点获取影像查看链接
     * @param xml 参数
     * @return 返回结果
     */
    String getCombineImageShowUrl(String xml);

    /**
     * 收票发票上传节点获取影像扫描链接
     * @param xml 参数
     * @return 返回结果
     */
    String invoiceScan(String xml);

    /**
     * 更改影像状态
     * @param xml 参数
     * @return 返回结果
     */
    String updateImageState(String xml);

    /**
     * 移动审批查看影像
     * @param xml 参数
     * @return 返回结果
     */
    String mobileImageQuery(String xml);

    DataCurrentTask submitTaskStateForNc(DataCurrentTask dataCurrentTask, String userId, String supplementaryScan) throws Exception;

    DataCurrentTask rejectTaskStateForNc(DataCurrentTask dataCurrentTask,String userId,String taskState) throws Exception;

    R<DataTask> imageSubmission(String businessSerialNo) throws Exception;
}
