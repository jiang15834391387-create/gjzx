package org.smartlink.system.domain.bo;

import lombok.Data;
import org.smartlink.system.domain.vo.SysUserVo;

@Data
public class ParticipantQueryBo extends SysUserVo {
    /**
     * 任务ID（必填）
     */
    private String taskId;


}
