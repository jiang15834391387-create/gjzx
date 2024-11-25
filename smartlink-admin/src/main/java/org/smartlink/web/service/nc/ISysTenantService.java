package org.smartlink.web.service.nc;


import org.smartlink.web.domain.SysTenant;

public interface ISysTenantService {
    /**
     * 查询使用者信息
     *
     * @param id 使用者信息主键
     * @return 使用者信息
     */
    SysTenant queryById(String id);
}
