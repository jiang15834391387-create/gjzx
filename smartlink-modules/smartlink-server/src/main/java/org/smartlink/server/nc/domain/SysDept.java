package org.smartlink.server.nc.domain;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.server.nc.domain.modle.TreeEntity;

import java.io.Serial;

/**
 * 部门表 sys_dept
 *
 * @author L
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class SysDept extends TreeEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @TableId(value = "dept_id")
    private String deptId;

    /**
     * 部门pk
     */
    @ApiModelProperty(value = "部门pk")
    //@NotBlank(message = "部门pk不能为空")
    @Size(min = 0, max = 50, message = "部门pk长度不能超过50个字符")
    private String pkOrg;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门类别编码
     */
    private String deptCategory;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门状态:0正常,1停用
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门code
     */

    private String deptCode;


    /**
     * 部门税号
     */

    private String deptTax;

    /**
     * 集团id
     */

    private String deptGroupid;
    /**
     * nc修改版本主键
     */

    private String deptVid;

    /**
     * 租户id
     */
    private String tenantId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (!(obj instanceof SysDept)) {
            return false;
        };
        SysDept tempDept= (SysDept) obj;
        if(StrUtil.equals(this.getPkOrg(),tempDept.getPkOrg())){
            return true;
        }else{
            return false;
        }
    }
}
