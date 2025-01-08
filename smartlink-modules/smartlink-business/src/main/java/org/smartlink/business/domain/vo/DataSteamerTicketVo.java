package org.smartlink.business.domain.vo;

import org.smartlink.business.domain.DataSteamerTicket;
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
 * 船票视图对象 data_steamer_ticket
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataSteamerTicket.class)
public class DataSteamerTicketVo implements Serializable {

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
     * 币种
     */
    @ExcelProperty(value = "币种")
    private String currencyCode;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private String invoiceDate;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名")
    private String name;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 省
     */
    @ExcelProperty(value = "省")
    private String province;

    /**
     * 到达站
     */
    @ExcelProperty(value = "到达站")
    private String stationGetOff;

    /**
     * 出发站
     */
    @ExcelProperty(value = "出发站")
    private String stationGetOn;

    /**
     * 时间
     */
    @ExcelProperty(value = "时间")
    private String invoiceTime;

    /**
     * 总计
     */
    @ExcelProperty(value = "总计")
    private String invoiceTotal;

    /**
     * 发票消费类型
     */
    @ExcelProperty(value = "发票消费类型")
    private String kind;

    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号")
    private String userId;

    /**
     * 发票专用章存在性判断
     */
    @ExcelProperty(value = "发票专用章存在性判断")
    private String invoiceStamp;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @ExcelProperty(value = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]")
    private String region;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
