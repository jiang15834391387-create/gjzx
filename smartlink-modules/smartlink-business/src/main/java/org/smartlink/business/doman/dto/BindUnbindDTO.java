package org.smartlink.business.doman.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BindUnbindDTO {

    // 工作流ID
    private String workflowId;

    // 附件ID列表
    @NotEmpty(message = "附件ID列表不能为空")
    private List<String> fileIds;
    // 是否解绑(true绑定 false解除绑定)
    private Boolean isBinding;
}
