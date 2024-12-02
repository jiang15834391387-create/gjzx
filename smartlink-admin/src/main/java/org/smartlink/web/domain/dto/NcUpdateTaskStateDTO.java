package org.smartlink.web.domain.dto;


import lombok.Data;
import org.smartlink.web.domain.DataCurrentTask;
import org.smartlink.web.domain.SysUser;

/**
 * @description: 更改影像状态参数类
 * @author: L
 * @create:
 **/
@Data
public class NcUpdateTaskStateDTO {

    /**
     * 任务对象
     */
    private DataCurrentTask dataCurrentTask;

    /**
     * 影像数量
     */
    private int imageCount;

    /**
     * 发票数量
     */
    private int invoiceCount;

    /**
     * 任务对象
     */
    private String webUrl;

    /**
     * 任务对象
     */
    private SysUser sysUser;

    /**
     * 影像状态
     */
    private String state;

    /**
     * dataSource
     */
    private String dataSource;

    /**
     * factoryCode
     */
    private String factoryCode;


}
