package org.smartlink.workflow.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 任务请求对象
 *
 * @author may
 */
@Data
public class TaskBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 流程定义名称
     */
    private String processDefinitionName;

    /**
     * 流程定义key
     */
    private String processDefinitionKey;


    /**
     * 任务类型(1:代办  2: 已办  3:已发起   )
     */
    private int taskType;

    /**
     * 流程key
     */
    private String key;

    /**
     * 任务发起人
     */
    private String startUserId;

    /**
     * 业务id
     */
    private String businessKey;

    /**
     * 模型分类
     */
    private String categoryCode;
}
