package org.smartlink.common.mybatis.modle;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 *
 * @author L
 */

@Data
public class BaseEntity extends Model<BaseEntity> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected static final ToStringStyle TOSTRING_STYLE = ToStringStyle.DEFAULT_STYLE;
    /**
     * 搜索值
     */
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
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 请求参数
     */
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();

    /**
     * 备注
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,exist = false)
    private String remark;

    /**
     * 图片base64
     */
    @TableField(exist = false)
    private String base64;


    /**
     * 图片坐标
     */
    @TableField(exist = false)
    private String[] coordinate;

    /**
     * 切图类型
     */
    @TableField(exist = false)
    private String option;

    /**
     * 旋转 角度
     */
    @TableField(exist = false)
    private int orientation = 0;

    /**
     * 图片表id
     */
    @TableField(exist = false)
    private String ocrFileId;

    /**
     * ocr识别类型
     */
    @TableField(exist = false)
    private String ocrFileType;

    /**
     * 乐观锁版本控制
     */
    @Version
    @TableField(exist = false)
    private Integer version;

    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    @TableField(exist = false)
    private String checkInvoice;

    /**
     * 查验结果
     */
    @TableField(exist = false)
    private String checkResult;

    /**
     * 图片fileId
     */
    @TableField(exist = false)
    private String fileId;

    /**
     * 暂存状态
     */
    @TableField(exist = false)
    private String billSaved;

}
