package org.smartlink.server.nc.domain.xietong;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.smartlink.server.nc.annotation.FieldName;
import org.smartlink.server.nc.domain.modle.BaseEntity;


/**
 * 附件推送外部系统中间表 data_synergy_attachment
 *
 * @author Lion Li
 */

@Data
@TableName("data_synergy_attachment")
public class DataSynergyAttachment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    @FieldName(value = "主键")
    private String id;

    /**
     * 任务表对象
     */
    @FieldName(value = "任务表对象")
    private String taskDetailInfo;

    /**
     * 图片表ID
     */
    @FieldName(value = "图片表ID")
    private String fileId;

    /**
     * 文件URL地址
     */
    @FieldName(value = "文件URL地址")
    private String fileUrl;

    /**
     * 文件名称
     */
    @FieldName(value = "文件名称")
    private String fileName;

    /**
     * 业务系统返回文件ID
     */
    @FieldName(value = "业务系统返回文件ID")
    private String businessImageId;

    /**
     * 推送状态
     */
    @FieldName(value = "推送状态")
    private String pushStatus;

    /**
     * 租户表主键
     */
    @FieldName(value = "租户表主键")
    private String tenementNo;

    /**
     * 推送次数
     */
    @FieldName(value = "推送次数")
    private Long pushCount;

    /**
     * 删除次数
     */
    @FieldName(value = "删除次数")
    private Long delCount;

    /**
     * 推送失败信息
     */
    @FieldName(value = "推送失败信息")
    private String message;



}
