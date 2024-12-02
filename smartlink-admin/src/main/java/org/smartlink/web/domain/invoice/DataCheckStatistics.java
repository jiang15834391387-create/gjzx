package org.smartlink.web.domain.invoice;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.smartlink.web.annotation.FieldName;
import org.smartlink.web.domain.modle.BaseEntity;

import java.util.Date;

/**
 * 查验记录统计对象 data_check_statistics
 *
 * @author L
 * @date
 */
@Data
@TableName("data_check_statistics")
public class DataCheckStatistics extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "ID")
    private String ID;
    /**
     * 查验厂商
     */
    private String checkSupplier;
    /**
     * 类型，0失败，1成功
     */
    @TableField(value = "TYPE")
    private String TYPE;
    /**
     * 查验备注
     */
    @TableField(value = "REMARK")
    private String REMARK;
    /**
     * 查验日期
     */
    private Date checkDate;

    /**
     * 租户id
     */
    @FieldName(value="租户id")
    private String tenantId;

    /**
     * 总使用次数
     */
    private Integer total;

    /**
     * 已使用次数
     */
    private Integer used;

    /**
     * 剩余使用次数
     */
    private Integer surplusUsed;

}
