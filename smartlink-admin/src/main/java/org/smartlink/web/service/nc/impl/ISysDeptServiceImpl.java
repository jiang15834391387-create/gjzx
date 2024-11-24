package org.smartlink.web.service.nc.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.web.domain.SysDept;
import org.smartlink.web.mapper.SysDeptMapper1;
import org.smartlink.web.service.nc.ISysDeptService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ISysDeptServiceImpl implements ISysDeptService {
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
}
