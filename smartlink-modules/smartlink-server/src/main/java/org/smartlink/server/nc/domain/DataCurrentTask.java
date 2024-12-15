package org.smartlink.server.nc.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.smartlink.server.nc.annotation.FieldName;
import org.smartlink.server.nc.domain.modle.BaseEntity;

import java.util.Date;

/**
 * 任务对象 data_current_task
 *
 * @author L
 * @date
 */
@Data
@TableName("data_current_task")
public class DataCurrentTask extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 业务流水号
     */
    @TableId(value = "business_serial_no")
    private String businessSerialNo;
    /**
     * 单据条码 批扫使用
     */
    private String barCode;
    /**
     * 制单日期
     */
    private Date billDate;
    /**
     * 单据号
     */
    private String billNum;
    /**
     * 单据类型
     */
    private String billType;
    /**
     * 单据类型名称
     */
    private String billTypeName;
    /**
     * 交易类型
     */
    private String tradeType;
    /**
     * 交易类型名称
     */
    private String tradeTypeName;
    /**
     * 单据总金额
     */
    private String cash;
    /**
     * 所属组织机构id
     */
    private String groupId;
    /**
     * 识别类型 1:数影ocr  2.税务云ocr
     */
    private String ocrType;
    /**
     * 驳回原因
     */
    private String operateSuggest;
    /**
     * 机构号
     */
    private String orgCode;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 父单据类型编号
     */
    private String pkBillType;
    /**
     * 扫描方式  1：单扫  2：批扫
     */
    private String scanType;
    /**
     * 扫描方式名称
     */
    private String scanTypeName;
    /**
     * 渠道系统编码
     */
    private String systemCode;
    /**
     * 单据状态 1-扫描完成 0-待扫描
     */
    private String taskState;
    /**
     * 制单人id
     */
    private String userId;
    /**
     * 制单人code
     */
    private String userCode;
    /**
     * 制单人名称
     */
    private String userName;
    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;

    /**
     * 租户id
     */
    @FieldName(value="租户id")
    private String tenantId;

    /**
     * 待重扫文件List
     */
    @FieldName(value="待重扫文件数量")
    private String rescanFileSum;

    /**
     * 单据影像文件数量
     */
    @FieldName(value="单据影像文件数量")
    private String scanFileSum;

    /**
     * 业务类型
     */
    @FieldName(value="业务类型")
    private String businessType;

    /**
     * 批扫人名称
     */
    private String name;

    /**
     * 批扫人编号
     */
    private String code;

    /**
     * 批扫人编号
     */
    private String message;

    /**
     * 暂存状态
     */
    private String billSaved;
}
