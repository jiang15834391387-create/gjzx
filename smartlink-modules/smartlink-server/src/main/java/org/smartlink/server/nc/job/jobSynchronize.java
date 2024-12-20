package org.smartlink.server.nc.job;

import io.swagger.annotations.ApiOperation;
import org.smartlink.common.core.domain.R;
import org.smartlink.server.nc.service.nc.NcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class jobSynchronize {

    @Autowired
    private NcService ncService;

    /**
     * 定时同步用户
     */
    @Scheduled(cron ="0 0 0 * * ?")
    @ApiOperation("定时同步用户")
    public R<Void> SynchronizeUser() throws Exception {
        try {
            return ncService.synchronizeUser();
        } catch (Exception e) {
           throw new Exception("同步用户失败！");
        }
    }

    /**
     * 定时同步组织机构
     */
    @Scheduled(cron ="0 0 0 * * ?")
    @ApiOperation("定时同步组织机构")
    public R<Void> SynchronizeDepart() throws Exception {
        try {
            return ncService.synchronizeDepart();
        } catch (Exception e) {
            throw new Exception("同步组织机构失败！");
        }
    }

    /**
     * 定时同步单据类型
     */
    @Scheduled(cron ="0 0 0 * * ?")
    @ApiOperation("定时同步单据类型")
    public R<Void> synchronizeBillType() throws Exception {
        try {
            return ncService.synchronizeBillType();
        } catch (Exception e) {
            throw new Exception("同步单据类型失败！");
        }
    }
//    @Scheduled(cron ="0/5 * * * * ?")
//    @ApiOperation("定时同步用户")
//    public R<Void> adc() throws Exception {
//        try {
//            System.out.println("定时同步用户11111111111111");
//        } catch (Exception e) {
//            throw new Exception("同步失败！");
//        }
//        return null;
//    }
}
