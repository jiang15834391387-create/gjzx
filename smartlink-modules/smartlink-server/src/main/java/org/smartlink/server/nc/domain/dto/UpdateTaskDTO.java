package org.smartlink.server.nc.domain.dto;


import lombok.Data;
import org.smartlink.server.nc.domain.DataCurrentTask;

import java.util.List;

/**
 * @description: 驳回影像状态接口DTO
 * @author: L
 * @create:
 **/
@Data
public class UpdateTaskDTO {

    /**
     * 流水号
     */
    private String businessSerialNo;

    /**
     * 驳回原因
     */
    private String message;

    /**
     * 驳回类型：4.驳回补扫
     */
    private String updateState;

    /**
     * 当前用户id
     */
    private String userId;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 任务对象
     */
    private DataCurrentTask dataCurrentTask;

    //BIP
    /**
     * 用户名
     */
    private String userName;

    /**
     * 驳回文件list
     */
    List<String> fileIds;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 版本号
     */
    private Integer version;
}
