package org.smartlink.workflow.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.workflow.domain.WfContract;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同视图对象
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WfContract.class)
public class WfContractVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "主键")
    private Long id;

    @ExcelProperty(value = "合同类型")
    private String contractType;

    @ExcelProperty(value = "合同编号")
    private String contractNo;

    @ExcelProperty(value = "合同名称")
    private String contractName;

    @ExcelProperty(value = "公司名称")
    private String companyName;

    @ExcelProperty(value = "合同金额")
    private String contractAmount;

    @ExcelProperty(value = "已付款金额")
    private String paidAmount;

    @ExcelProperty(value = "未付款金额")
    private String unpaidAmount;

    private String currency;

    @ExcelProperty(value = "合同状态")
    private String contractStatus;
    private String contractCounterparty;
    private String signUser;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date signDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    private String processInstanceId;

    @ExcelProperty(value = "付款进度")
    private Integer progress;

    private String attachmentUrl;

    private String remark;
    private Integer isDeleted;
    /**
     * 支付节点(JSON数组)
     */
    private String paymentNodes;
    private String email;
}
