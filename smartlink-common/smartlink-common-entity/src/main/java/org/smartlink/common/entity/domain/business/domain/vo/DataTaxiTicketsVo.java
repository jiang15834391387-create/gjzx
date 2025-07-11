package org.smartlink.common.entity.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.common.entity.domain.business.domain.DataTaxiTickets;

import java.io.Serial;
import java.io.Serializable;


/**
 * 出租车发票视图对象 data_taxi_tickets
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataTaxiTickets.class)
public class DataTaxiTicketsVo implements Serializable {

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
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 城市
     */
    @ExcelProperty(value = "城市")
    private String city;

    /**
     * 发票代码
     */
    @ExcelProperty(value = "发票代码")
    private String invoiceCode;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private String invoiceDate;

    /**
     * 车牌号
     */
    @ExcelProperty(value = "车牌号")
    private String licensePlate;

    /**
     * 发票消费类型
     */
    @ExcelProperty(value = "发票消费类型")
    private String kind;


    /**
     * 图片旋转角度
     */
    @ExcelProperty(value = "发票消费类型")
    private String orientation;

    /**
     * 里程
     */
    @ExcelProperty(value = "里程")
    private String mileage;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 发票所属地区
     */
    @ExcelProperty(value = "发票所属地区")
    private String place;

    /**
     * 省
     */
    @ExcelProperty(value = "省")
    private String province;

    /**
     * 下车时间
     */
    @ExcelProperty(value = "下车时间")
    private String timeGetOff;

    /**
     * 金额
     */
    private String fare;

    /**
     * 上车时间
     */
    @ExcelProperty(value = "上车时间")
    private String timeGetOn;

    /**
     * 总计
     */
    @ExcelProperty(value = "总计")
    private String invoiceTotal;

    /**
     * 发票专用章存在性判断
     */
    @ExcelProperty(value = "发票专用章存在性判断")
    private String invoiceStamp;

    /**
     * 燃油附加费
     */
    @ExcelProperty(value = "燃油附加费")
    private String fuelSurcharge;

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
