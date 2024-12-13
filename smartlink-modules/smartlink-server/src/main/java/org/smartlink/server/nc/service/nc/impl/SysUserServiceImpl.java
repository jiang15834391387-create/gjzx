package org.smartlink.server.nc.service.nc.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.server.nc.domain.SysUser;
import org.smartlink.server.nc.mapper.SysUserMapperWeb;
import org.smartlink.server.nc.service.nc.ISysUserService;
import org.smartlink.system.domain.SysUserRole;
import org.smartlink.system.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author L
 */
@Slf4j
@RequiredArgsConstructor
@Service("sysUserServiceImplSystem")
public class SysUserServiceImpl implements ISysUserService {

    private final SysUserMapperWeb baseMapper;
    private final SysUserRoleMapper userRoleMapper;

    @Override
    public Boolean deleteAllExternalUser() {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getCreateBy,"NC");
        boolean result = this.baseMapper.delete(queryWrapper) >= 0;
        LambdaQueryWrapper<SysUserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 删除普通用户的权限表记录
        userRoleLambdaQueryWrapper.eq(SysUserRole::getRoleId,2L);
        userRoleMapper.delete(userRoleLambdaQueryWrapper);
        return result;
    }

    @Override
    public Boolean insertAllExternalUser(List<SysUser> userList) {
        //return this.baseMapper.insertOrUpdateBatch(userList);
        boolean result = this.baseMapper.insertBatch(userList);
        List<Long> list = userList.stream().map(SysUser::getUserId).collect(Collectors.toList());
        List<SysUserRole> userRoleList = new ArrayList<>();
        for (Long aLong : list) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(aLong);
            userRole.setRoleId(2L);
            userRoleList.add(userRole);
        }
        userRoleMapper.insertBatch(userRoleList);
        return result;
    }

    @Override
    public List<SysUser> selectListByNcUserId(String ncUserId) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getNcUserId,ncUserId);
        queryWrapper.eq(SysUser::getCreateBy,"NC");
        return this.baseMapper.selectList(queryWrapper);
    }
    @Override
    public String selectUserNameByUserId(String userId) {
        return this.baseMapper.selectUserNameByUserId(userId);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return baseMapper.selectUserById(userId);
    }


    @Override
    public SysUser selectSysUserByUserNo(String userNo) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserName,userNo);
        return this.baseMapper.selectOne(queryWrapper,true);
    }
}
