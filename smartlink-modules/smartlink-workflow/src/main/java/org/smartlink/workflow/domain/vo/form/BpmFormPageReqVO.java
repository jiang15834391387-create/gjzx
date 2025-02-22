package org.smartlink.workflow.domain.vo.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 动态表单分页 Request VO")
@Data
public class BpmFormPageReqVO  {

    @Schema(description = "表单名称", example = "芋道")
    private String name;

    private Integer pageNo=1;
    private Integer pageSize=10;

}
