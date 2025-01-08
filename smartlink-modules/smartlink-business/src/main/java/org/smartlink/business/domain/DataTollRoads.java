package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 过路费对象 data_toll_roads
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_toll_roads")
public class DataTollRoads extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 发票代码
     */
    private String invoiceCode;

    /**
     * 日期
     */
    private Date invoiceDate;

    /**
     * 入口
     */
    private String entrance;

    /**
     * 出口
     */
    private String exit;

    /**
     * 图片表id
     */
    private String fileId;

    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 时间
     */
    private String invoiceTime;

    /**
     * 总计
     */
    private Long invoiceTotal;

    /**
     * 发票专用章存在性判断
     */
    private String invoiceStamp;

    /**
     * 高速标志(0:没有; 1: 有)
     */
    private String highwayFlag;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String region;

    /**
     * token
     */
    private String saveToken;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 置信度
     */
    private String confidence;

    /**
     * 版本号
     */
    @Version
    private Long version;

    /**
     * 查验结果
     */
    private String checkResult;

    /**
     * 入台账标识
     */
    private String pushBusinessInfoFlag;


}
