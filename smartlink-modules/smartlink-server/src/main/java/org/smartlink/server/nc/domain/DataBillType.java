package org.smartlink.server.nc.domain;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.smartlink.server.nc.annotation.FieldName;
import org.smartlink.server.nc.domain.modle.BaseEntity;


/**
 * 单据类型对象 data_bill_type
 *
 * @author L
 * @date
 */
@Data
@TableName("data_bill_type")
public class DataBillType extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private String id;
    /**
     * 单据是否普启用ocr识别，0-禁止  1-启用
     */
    private String ocrEnable;
    /**
     * 父级单据类型id
     */
    private String parentTypeId;
    /**
     * 父级系统
     */
    private String parentSystem;
    /**
     * 所属组织机构
     */
    private String groupId;
    /**
     * 系统编码
     */
    private String systemCode;
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 单据类型编码
     */
    private String typeCode;
    /**
     * 单据类型名称
     */
    private String typeName;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        };
        if (!(obj instanceof DataBillType)){
            return false;
        }
        DataBillType tempBillType= (DataBillType) obj;
        if(StrUtil.equals(this.getTypeCode(),tempBillType.getTypeCode())){
            return true;
        }else{
            return false;
        }
    }
}
