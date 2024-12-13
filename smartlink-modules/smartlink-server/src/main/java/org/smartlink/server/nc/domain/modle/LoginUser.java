package org.smartlink.server.nc.domain.modle;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.smartlink.common.core.domain.dto.RoleDTO;
import org.smartlink.server.nc.helper.LoginHelper;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 登录用户身份权限
 *
 * @author L
 */

@Data
@NoArgsConstructor
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private String deptId;


    private Long depId;

    /**
     * 部门类别编码
     */
    private String deptCategory;
    /**
     * 部门名
     */
    private String deptName;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 菜单权限
     */
    private Set<String> menuPermission;

    /**
     * 角色权限
     */
    private Set<String> rolePermission;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户编号
     */
    private String userNo;

    /**
     * NCC用户id
     */
    private String nccUserId;

    /**
     * 角色对象
     */
    private List<RoleDTO> roles;

    /**
     * 数据权限 当前角色ID
     */
    private Long roleId;

    /**
     * 租户 当前所属租户ID
     */
    private String tenantId;

    /**
     * 获取登录id
     */
    public String getLoginId() {
        return userType + LoginHelper.JOIN_CODE + userId;
    }

    /**
     * 客户端
     */
    private String clientKey;

    /**
     * 设备类型
     */
    private String deviceType;
}
