package org.smartlink.server.nc.service.nc.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.constant.CacheNames;
import org.smartlink.common.core.constant.TenantConstants;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.domain.dto.RoleDTO;
import org.smartlink.common.core.enums.DeviceType;
import org.smartlink.common.tenant.helper.TenantHelper;
import org.smartlink.server.nc.domain.modle.LoginUser;
import org.smartlink.server.nc.domain.token.LoginVo;
import org.smartlink.server.nc.helper.LoginHelper;
import org.smartlink.server.nc.service.nc.ExternalTokenService;
import org.smartlink.server.nc.service.nc.ISysUserService;
import org.smartlink.system.domain.SysDept;
import org.smartlink.system.domain.vo.SysClientVo;
import org.smartlink.system.domain.vo.SysDeptVo;
import org.smartlink.system.domain.vo.SysRoleVo;
import org.smartlink.system.domain.vo.SysUserVo;
import org.smartlink.system.mapper.SysDeptMapper;
import org.smartlink.system.mapper.SysRoleMapper;
import org.smartlink.system.mapper.SysUserMapper;
import org.smartlink.system.service.ISysClientService;
import org.smartlink.system.service.ISysMenuService;
import org.smartlink.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExternalTokenServiceImpl implements ExternalTokenService {


    private final ISysRoleService roleService;
    private final ISysMenuService menuService;
    private final SysUserMapper baseMapper;
    private final SysDeptMapper deptMapper;
    private final SysRoleMapper roleMapper;
    private final ISysClientService clientService;
    private final ISysUserService userService;
    @Autowired
    private ISysUserService iSysUserService;

    @Override
    public R<LoginVo> getToken() {
        SysClientVo client = clientService.queryByClientId("e5cd7e4891bf95d1d19206ce24a7b32e");
        SysUserVo user = selectUserById("1");
        LoginUser loginUser = buildLoginUser(user);
        loginUser.setClientKey(client.getClientKey());
        loginUser.setDeviceType(client.getDeviceType());
        SaLoginModel model = new SaLoginModel();
        model.setDevice(client.getDeviceType());
        // 自定义分配 不同用户体系 不同 token 授权时间 不设置默认走全局 yml 配置
        // 例如: 后台用户30分钟过期 app用户1天过期
        model.setTimeout(client.getTimeout());
        model.setActiveTimeout(client.getActiveTimeout());
        model.setExtra(LoginHelper.CLIENT_KEY, client.getClientId());
        // 生成token
        LoginHelper.login(loginUser, model);
        LoginVo loginVo = new LoginVo();
        loginVo.setAccessToken(StpUtil.getTokenValue());
        loginVo.setExpireIn(StpUtil.getTokenTimeout());
        loginVo.setClientId(client.getClientId());
        return R.ok(loginVo);
//        return StpUtil.getTokenValueByLoginId(loginUser.getLoginId());
    }

    public SysUserVo selectUserById(String userId) {
        SysUserVo user = baseMapper.selectVoById(userId);
        if (ObjectUtil.isNull(user)) {
            return user;
        }
        user.setRoles(roleMapper.selectRolesByUserId(user.getUserId()));
        return user;
    }
    /**
     * 构建登录用户
     */
    public LoginUser buildLoginUser(SysUserVo user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setTenantId(user.getTenantId());
        loginUser.setUserId(user.getUserId());
        loginUser.setDepId(Long.parseLong(user.getDeptId()));
        loginUser.setUsername(user.getUserName());
        loginUser.setNickname(user.getNickName());
        loginUser.setUserType(user.getUserType());
        loginUser.setMenuPermission(getMenuPermission(user.getUserId()));
        loginUser.setRolePermission(getRolePermission(user.getUserId()));
        TenantHelper.dynamic(user.getTenantId(), () -> {
            SysDeptVo dept = null;
            if (ObjectUtil.isNotNull(user.getDeptId())) {
                dept = selectDeptById(Long.parseLong(user.getDeptId()));
            }
            loginUser.setDeptName(ObjectUtil.isNull(dept) ? "" : dept.getDeptName());
            loginUser.setDeptCategory(ObjectUtil.isNull(dept) ? "" : dept.getDeptCategory());
            List<SysRoleVo> roles = selectRolesByUserId(user.getUserId());
            loginUser.setRoles(BeanUtil.copyToList(roles, RoleDTO.class));
        });
        return loginUser;
    }

    public List<SysRoleVo> selectRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }

    @Cacheable(cacheNames = CacheNames.SYS_DEPT, key = "#deptId")
    public SysDeptVo selectDeptById(Long deptId) {
        SysDeptVo dept = deptMapper.selectVoById(deptId);
        if (ObjectUtil.isNull(dept)) {
            return null;
        }
        SysDeptVo parentDept = deptMapper.selectVoOne(new LambdaQueryWrapper<SysDept>()
            .select(SysDept::getDeptName).eq(SysDept::getDeptId, dept.getParentId()));
        dept.setParentName(ObjectUtil.isNotNull(parentDept) ? parentDept.getDeptName() : null);
        return dept;
    }
    /**
     * 获取角色数据权限
     *
     * @param userId  用户id
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(Long userId) {
        Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        if (org.smartlink.common.satoken.utils.LoginHelper.isSuperAdmin(userId)) {
            roles.add(TenantConstants.SUPER_ADMIN_ROLE_KEY);
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(userId));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param userId  用户id
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(Long userId) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (org.smartlink.common.satoken.utils.LoginHelper.isSuperAdmin(userId)) {
            perms.add("*:*:*");
        } else {
            perms.addAll(menuService.selectMenuPermsByUserId(userId));
        }
        return perms;
    }
    /**
     * 单点登录
     *
     * @return TokenValue
     */
    @Override
    public String getNccToken(String userId,String userNo) {
        Set<String> perms = new HashSet<String>();
        perms.add("*:*:*");
        LoginUser loginUser = new LoginUser();
        loginUser.setUserType("sys_user");
        if(StrUtil.isNotEmpty(userId)){
            loginUser.setUserId(Long.valueOf(userId));
        }else{
            loginUser.setUserId(1L);
        }
        loginUser.setMenuPermission(perms);
        String userName = iSysUserService.selectUserNameByUserId(userId);
        if(StrUtil.isEmpty(userName)){
            loginUser.setUsername("未知用户");
        }else{
            loginUser.setUsername(userName);
            loginUser.setUserNo(userNo);
        }
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);
        return StpUtil.getTokenValueByLoginId(loginUser.getLoginId());
    }


    @Override
    public String getBIPToken(String tenantId,String userName,String userCode,String userId) {
        Set<String> perms = new HashSet<String>();
        perms.add("*:*:*");
        LoginUser loginUser = new LoginUser();
        loginUser.setUserType("sys_user");
        loginUser.setUserId(1L);
        loginUser.setMenuPermission(perms);
        if (StrUtil.isBlank(userName)){
            loginUser.setUsername("admin");
        }else{
            loginUser.setUsername(userName);
            loginUser.setUserNo(userCode);
            loginUser.setNccUserId(userId);
        }
        loginUser.setTenantId(tenantId);
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);
        return StpUtil.getTokenValueByLoginId(loginUser.getLoginId());
    }
}
