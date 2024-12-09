package org.smartlink.server.nc.domain.initialization.vo;


import lombok.Data;
import org.smartlink.server.nc.domain.scan.DataScannerDriver;

import java.io.Serializable;
import java.util.List;

/**
 * 展示扫描仪驱动信息VO，用于勾选生成授权文件
 * 适用于接口：
 *
 * @author 马旭辉
 */
@Data
public class ShowScannerDriverInfoVO implements Serializable {
    private String scannerName;
    private String serialNumber;
    private List<DataScannerDriver> driverList;
}
