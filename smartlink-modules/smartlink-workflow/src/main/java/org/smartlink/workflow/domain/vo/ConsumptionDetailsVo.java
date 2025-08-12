package org.smartlink.workflow.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 86158
 */
@Data
public class ConsumptionDetailsVo {

    private List<DetailsVo> details;

    private Long fromId;
}
