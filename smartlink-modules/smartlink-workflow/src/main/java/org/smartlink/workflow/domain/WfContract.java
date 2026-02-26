package org.smartlink.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同对象 wf_contract
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_contract")
public class WfContract extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 合同类型(1付款合同 2收款合同)
     */
    private String contractType;

    private String contractNo;

    private String contractName;

    private String companyName;

    private String contractAmount;

    private String paidAmount;

    private String unpaidAmount;

    private String currency;

    private String contractStatus;

    private String signUser;

    private Date signDate;

    private Date effectiveDate;

    private Date expireDate;

    private String processInstanceId;

    private Integer progress;

    private String attachmentUrl;

    private String remark;
    private Integer isDeleted;
}
