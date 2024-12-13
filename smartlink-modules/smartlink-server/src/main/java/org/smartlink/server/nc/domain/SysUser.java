package org.smartlink.server.nc.domain;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.smartlink.common.core.constant.UserConstants;
import org.smartlink.common.core.xss.Xss;
import org.smartlink.system.domain.SysDept;
import org.smartlink.system.domain.SysRole;
import org.smartlink.system.domain.modle.BaseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author L
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@Component
public class SysUser extends BaseEntity {

    /**
     * 用户ID
     */
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型（sys_user系统用户）
     */
    private String userType;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户头像
     */
    private Long avatar;

    /**
     * 密码
     */
    @TableField(
        insertStrategy = FieldStrategy.NOT_EMPTY,
        updateStrategy = FieldStrategy.NOT_EMPTY,
        whereStrategy = FieldStrategy.NOT_EMPTY
    )
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 部门对象
     */
    @ApiModelProperty(value = "部门对象")
    @TableField(exist = false)
    private SysDept dept;

    /**
     * 角色对象
     */
    @ApiModelProperty(value = "角色对象")
    @TableField(exist = false)
    private List<SysRole> roles;

    /**
     * 角色组
     */
    @ApiModelProperty(value = "角色组")
    @TableField(exist = false)
    private Long[] roleIds;

    /**
     * 岗位组
     */
    @ApiModelProperty(value = "岗位组")
    @TableField(exist = false)
    private Long[] postIds;


    public SysUser(Long userId) {
        this.userId = userId;
    }

    public boolean isSuperAdmin() {
        return UserConstants.SUPER_ADMIN_ID.equals(this.userId);
    }
    //---------------------------
    /**
     * nc用户id
     */
    private String ncUserId;
    /**
     * 所属集团id
     */
    private String groupId;
    /**
     * nc关联人员身份
     */
    private String ncUserRelevanceIdentity;
    /**
     * nc关联人员
     */
    private String ncUserRelevanceCode;

    /**
     * 租户id
     */
    private String tenantId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        };
        if (!(obj instanceof SysUser)) {
            return false;
        };
        SysUser tempUser= (SysUser) obj;
        if(StrUtil.equals(this.getNcUserId(),tempUser.getNcUserId())){
            return true;
        }else{
            return false;
        }
    }

}
