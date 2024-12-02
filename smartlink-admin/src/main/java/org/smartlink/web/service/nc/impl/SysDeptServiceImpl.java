package org.smartlink.web.service.nc.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.web.domain.SysDept;
import org.smartlink.web.mapper.SysDeptMapper1;
import org.smartlink.web.service.nc.ISysDeptService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service("sysDeptServiceWeb")
public class SysDeptServiceImpl implements ISysDeptService {
    private final SysDeptMapper1 baseMapper;

    @Override
    public Boolean insertAllExternalDept(List<SysDept> deptList) {
        return this.baseMapper.insertBatch(deptList);
    }

    @Override
    public Boolean deleteAllExternalDept() {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        return this.baseMapper.delete(queryWrapper)>=0;
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(String deptId) {
        return baseMapper.selectById(deptId);
    }
}
