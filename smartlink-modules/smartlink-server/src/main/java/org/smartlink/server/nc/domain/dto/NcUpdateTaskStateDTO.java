package org.smartlink.server.nc.domain.dto;


import lombok.Data;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.SysUser;
import org.smartlink.server.task.momain.DataTask;


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

    private DataTask dataTask;

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

    private String scanType;

    /**
     * dataSource
     */
    private String dataSource;

    /**
     * factoryCode
     */
    private String factoryCode;


    String businessSerialNo;
    String billCode;
    String billType;
    String pk_billtype;
    String imagenum;
    String OrgNo;
    String groupid;
    String opuserdatetime;
    String opusername;
    String opuserpk;
    String opuseraccount;


}
