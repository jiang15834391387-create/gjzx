package org.smartlink.common.check.doman;

import lombok.Data;

/**
 * 发票夹列表分页对象
 */
@Data
public class InvoicePageQuery {
    //页码
    private Long pageNum;
    //每页条数
    private Long pageSize;
    //用户id
    private Long userId;
    //报销状态
    private String status;
}
