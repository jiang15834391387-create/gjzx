package org.smartlink.server.nc.domain.scan;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.smartlink.server.nc.domain.modle.BaseEntity;


/**
 * 扫描仪驱动信息对象 data_scanner_driver
 *
 * @author ruoyi
 * @date 2022-03-31
 */
@Data
@TableName("data_scanner_driver")
public class DataScannerDriver extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 驱动ID
     */
    @TableId(value = "DRIVER_ID")
    private String driverId;
    /**
     * 驱动名称
     */
    private String driverName;

}
