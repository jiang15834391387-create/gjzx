package org.smartlink.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serial;
import java.util.Date;

/**
 * 套版信息对象 test_template_info
 *
 * @author Lion Li
 * @date 2025-04-02
 */
@Data
@TableName("test_template_info")
public class TemplateInfo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 套版名称
     */
    private String templateName;

    /**
     * 套版编码
     */
    private String templateCode;

    /**
     * 套版存储地址路径
     */
    private String templateStoragePath;

    /**
     * 套版类型（http还是本地的这种）
     */
    private String templateType;

    /**
     * 套版表体行数(限制行数）
     */
    private String templateNumberRows;

    /**
     * 版本
     */
    @Version
    private Long version;

    /**
     * 删除标志
     */
    @TableLogic
    private Long delFlag;

    private Date createTime;

}
