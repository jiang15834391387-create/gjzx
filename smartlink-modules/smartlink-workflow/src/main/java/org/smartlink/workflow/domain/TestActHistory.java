package org.smartlink.workflow.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.engine.task.Attachment;
import org.smartlink.common.translation.annotation.Translation;
import org.smartlink.common.translation.constant.TransConstant;

import java.util.Date;
import java.util.List;

/**
 * @author 86158
 */
@Data
@TableName("test_act_history")
public class TestActHistory {
    /**
     * id
     */
    private String id;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 运行时长
     */
    private String runDuration;
    /**
     * 状态
     */
    private String status;
    private String statusName;
    /**
     * 办理人id
     */
    private String assignee;

    /**
     * 办理人名称
     */
    @Translation(type = TransConstant.USER_ID_TO_NICKNAME, mapper = "assignee")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String nickName;
}
