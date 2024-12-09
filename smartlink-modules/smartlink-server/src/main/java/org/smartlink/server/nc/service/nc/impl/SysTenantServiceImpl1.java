package org.smartlink.server.nc.service.nc.impl;


import lombok.RequiredArgsConstructor;
import org.smartlink.server.nc.domain.SysTenant;
import org.smartlink.server.nc.mapper.SysTenantMapper1;
import org.smartlink.server.nc.service.nc.ISysTenantService;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class SysTenantServiceImpl1 implements ISysTenantService {

    private final SysTenantMapper1 baseMapper;

    @Override
    public SysTenant queryById(String id) {
        return baseMapper.selectVoById(id);
    }
}
