package org.smartlink.workflow.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同支付节点
 */
@Data
public class WfContractPaymentNodeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 节点名称
     */
    @NotBlank(message = "节点名称不能为空")
    private String nodeName;

    /**
     * 节点金额
     */
    private String nodeAmount;

    /**
     * 支付比例
     */
    private String paymentRatio;

    /**
     * 节点状态
     */
    private String nodeStatus;

    /**
     * 计划日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String planDate;

    /**
     * 备注
     */
    private String remark;
}
