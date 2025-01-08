package org.smartlink.business.domain.vo;

import org.smartlink.business.domain.DataElectronicTransportationGoods;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 货物运输电子收款凭证视图对象 data_electronic_transportation_goods
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataElectronicTransportationGoods.class)
public class DataElectronicTransportationGoodsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 图片表id
     */
    @ExcelProperty(value = "图片表id")
    private String fileId;

    /**
     * 申请日期
     */
    @ExcelProperty(value = "申请日期")
    private String date;

    /**
     * 托运人证照号码
     */
    @ExcelProperty(value = "托运人证照号码")
    private String businessLicenseNumber;

    /**
     * 电子收款凭证号
     */
    @ExcelProperty(value = "电子收款凭证号")
    private String electronicReceiptNumber;

    /**
     * 服务商
     */
    @ExcelProperty(value = "服务商")
    private String producer;

    /**
     * 托运人名称
     */
    @ExcelProperty(value = "托运人名称")
    private String shipper;

    /**
     * 费用合计小写
     */
    @ExcelProperty(value = "费用合计小写")
    private String totalPrice;

    /**
     * 费用合计大写
     */
    @ExcelProperty(value = "费用合计大写")
    private String totalCn;

    /**
     * 承运人姓名
     */
    @ExcelProperty(value = "承运人姓名")
    private String transporter;

    /**
     * 承运人身份证号
     */
    @ExcelProperty(value = "承运人身份证号")
    private String transporterIdNumber;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @ExcelProperty(value = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]")
    private String region;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ExcelProperty(value = "是否删除标识 0-不删除  1-删除")
    private String deleteFlag;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
