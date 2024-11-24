package org.smartlink.web.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.smartlink.web.annotation.FieldName;
import org.smartlink.web.domain.modle.BaseEntity;


/**
 * 任务、图片中间关联对象 data_cm_info
 *
 * @author L
 * @date
 */
@Data
@TableName("data_cm_info")
public class DataCmInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private String id;
    /**
     * 批次号
     */
    private String batchId;
    /**
     * 业务流水号
     */
    private String businessSerialNo;
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
     * 备注
     */
    private String remark;

}
