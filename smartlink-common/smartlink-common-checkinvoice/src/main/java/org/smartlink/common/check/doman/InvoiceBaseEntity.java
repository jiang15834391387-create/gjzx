package org.smartlink.common.check.doman;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 */

@Data
public class InvoiceBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    @JsonIgnore
    @TableField(exist = false)
    private String searchValue;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();

    /**
     * 备注
     */
    //@ApiModelProperty(value = "备注")
    @TableField(fill = FieldFill.INSERT_UPDATE,exist = false)
    private String remark;

    /**
     * 图片base64
     */
    //@ApiModelProperty(value = "图片base64")
    @TableField(exist = false)
    private String base64;


    /**
     * 图片坐标
     */
    //@ApiModelProperty(value = "图片坐标")
    @TableField(exist = false)
    private String[] coordinate;

    /**
     * 切图类型
     */
    //@ApiModelProperty(value = "切图类型")
    @TableField(exist = false)
    private String option;

    /**
     * 旋转 角度
     */
    //@ApiModelProperty(value = "旋转 角度")
    @TableField(exist = false)
    private int orientation = 0;

    /**
     * 图片表id
     */
    //@ApiModelProperty(value = "图片表id")
    @TableField(exist = false)
    private String ocrFileId;

    /**
     * ocr识别类型ocr识别类型
     */
    //@ApiModelProperty(value = "ocr识别类型")
    @TableField(exist = false)
    private String ocrFileType;

    /**
     * 乐观锁版本控制
     */
    @Version
    private Integer version;

    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    //@ApiModelProperty(value = "是否查验标识，（0查验失败，1查验成功）")
    @TableField(exist = false)
    private String checkInvoice;

    /**
     * 查验结果
     */
    //@ApiModelProperty(value = "查验结果")
    @TableField(exist = false)
    private String checkResult;

}
