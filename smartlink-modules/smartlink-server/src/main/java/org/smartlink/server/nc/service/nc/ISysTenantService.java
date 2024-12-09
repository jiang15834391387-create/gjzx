package org.smartlink.server.nc.service.nc;


import org.smartlink.server.nc.domain.SysTenant;

/**
 * 使用者信息Service接口
 */
public interface ISysTenantService {
    /**
     * 查询使用者信息
     *
     * @param id 使用者信息主键
     * @return 使用者信息
     */
    SysTenant queryById(String id);
}
