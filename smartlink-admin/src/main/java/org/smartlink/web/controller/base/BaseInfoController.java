package org.smartlink.web.controller.base;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.R;
import org.smartlink.web.service.nc.NcService;
import org.springframework.web.bind.annotation.*;


/**
 * @description: 同步NC基础数据
 * @author: L
 * @create: 2024-11-20
 **/
@RestController
@RequestMapping("/system/base")
@RequiredArgsConstructor
public class BaseInfoController {

    private final NcService ncService;

    /**
     * 同步用户信息
     *
     * @return
     */
    @ApiOperation("同步用户信息")
    //@SaCheckPermission("system:base:synchronizeUser")
    @GetMapping("/synchronizeUser")
    public R<Void> synchronizeUser() {
        return ncService.synchronizeUser();
    }

    /**
     * 同步组织机构
     * @return
     */
    @ApiOperation("同步组织机构")
    @GetMapping("/synchronizeDepart")
    //@SaCheckPermission("system:base:synchronizeDepart")
    public R<Void> synchronizeDepart(){
        return ncService.synchronizeDepart();
    }

    /**
     * 同步单据类型
     * @return
     */
    @ApiOperation("同步单据类型信息")
    //@SaCheckPermission("system:base:synchronizeBillType")
    @GetMapping("/synchronizeBillType")
    public R<Void> synchronizeBillType(){
        return ncService.synchronizeBillType();
    }
}

