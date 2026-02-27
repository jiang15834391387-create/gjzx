package org.smartlink.workflow.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.smartlink.workflow.domain.WfContract;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同业务对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WfContract.class, reverseConvertGenerate = false)
public class WfContractBo extends BaseEntity {

    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 合同类型(1付款合同 2收款合同)
     */
    @NotBlank(message = "合同类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String contractType;

    @NotBlank(message = "合同编号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String contractNo;

    @NotBlank(message = "合同名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String contractName;

    @NotBlank(message = "公司名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String companyName;

    private String contractAmount;

    private String paidAmount;

    private String unpaidAmount;

    private String currency;

    private String contractStatus;

    private String signUser;
    private Long signUserId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date signDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    private String processInstanceId;

    private Integer progress;

    private String attachmentUrl;

    private String keyword;

    private String remark;

    private Integer isDeleted;
    /**
     * 支付节点(JSON数组)
     */
    private String paymentNodes;
    private String email;
    private String contractCounterparty;

    private String treeId;
}
