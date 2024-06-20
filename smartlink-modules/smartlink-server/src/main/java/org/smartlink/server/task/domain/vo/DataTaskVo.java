package org.smartlink.server.task.domain.vo;

import lombok.Data;

@Data
public class DataTaskVo {

    /**
     * 流水号
     */
    private String businessSerialNo;

    /**
     * 单据号
     */
    private String billNum;

    /**
     * 单据类型名称
     */
    private String billTypeName;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 制单人名称
     */
    private String userName;
    /**
     * 制单日期
     */
    private String billDate;

    /**
     * 单据状态0待登记、1待扫描、2扫描完成、3驳回修改、4驳回重扫、5修改完成、6补扫完成、7未装册、8已装册
     */
    private String taskState;
}
