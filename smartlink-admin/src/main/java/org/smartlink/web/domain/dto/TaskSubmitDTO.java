package org.smartlink.web.domain.dto;


import lombok.Data;
import org.smartlink.web.domain.DataCurrentTask;

import java.util.List;

/**
 * @description: 单据提交DTO
 * @author: L
 * @create:
 **/
@Data
public class TaskSubmitDTO {

    /**
     * 流水号
     */
    private String businessSerialNo;

    /**
     * 当前用户ID
     */
    private String userId;

    /**
     * 流水号集合（提供给批扫使用）
     */
    private List<String> businessSerialNoList;

    /**
     * 任务对象
     */
    private DataCurrentTask dataCurrentTask;

    //BIP

    /**
     * 租户
     */
    private String tenantId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 事后补扫标识
     */
    private String supplementaryScan;
    /**
     * 自定义树节点集合
     */
    private String batchId;
    /**
     * 自定义树节点集合
     */
    private List<String> productNames;
}
