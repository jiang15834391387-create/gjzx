package org.smartlink.common.entity.domain.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

/**
 * 其他附件 other_attachments
 *
 * @author Lion Li
 * @date 2025-10-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("other_attachments")
public class OtherAttachments extends TenantEntity {

    /**
     * 文件主键
     */
    @TableId(value = "file_id")
    private Long fileId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原名
     */
    private String originalName;

    /**
     * 文件后缀名
     */
    private String fileSuffix;

    /**
     * URL地址
     */
    private String url;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;
}
