package org.smartlink.common.entity.domain.business.domain.bo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.entity.domain.business.domain.DataUsedCarSales;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataUsedCarSales.class, reverseConvertGenerate = false)
public class OtherAttachmentsBo  extends BaseEntity {

    /**
     * 文件主键
     */
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
