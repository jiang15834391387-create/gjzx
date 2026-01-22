package org.smartlink.common.check.doman;

import lombok.Data;

/**
 * 发票夹列表分页对象
 */
@Data
public class InvoicePageQuery {
    //页码
    private int pageNum;
    //每页条数
    private int pageSize;
    //用户id
    private Long userId;
    //报销状态
    private String status;
    //查验状态
    private String checkStatus;
}
