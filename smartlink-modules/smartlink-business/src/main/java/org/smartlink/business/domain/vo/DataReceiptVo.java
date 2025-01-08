package org.smartlink.business.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.smartlink.business.domain.DataReceipt;
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
 * 小票视图对象 data_receipt
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataReceipt.class)
public class DataReceiptVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 币种
     */
    @ExcelProperty(value = "币种")
    private String currencyCode;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private Date invoiceDate;

    /**
     * 时间
     */
    @ExcelProperty(value = "时间")
    private String time;

    /**
     * 折扣
     */
    @ExcelProperty(value = "折扣")
    private String discount;

    /**
     * 图片表id
     */
    @ExcelProperty(value = "图片表id")
    private String fileId;

    /**
     * 店名
     */
    @ExcelProperty(value = "店名")
    private String storeName;

    /**
     * 小计
     */
    @ExcelProperty(value = "小计")
    private Long subTotal;

    /**
     * 税费
     */
    @ExcelProperty(value = "税费")
    private Long tax;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 小费
     */
    @ExcelProperty(value = "小费")
    private String tips;

    /**
     * 总计
     */
    @ExcelProperty(value = "总计")
    private Long invoiceTotal;

    /**
     * 消费类型
     */
    @ExcelProperty(value = "消费类型")
    private String type;

    /**
     * 1:国际票 0:国内票
     */
    @ExcelProperty(value = "1:国际票 0:国内票")
    private String internationalMark;

    /**
     * 发票专用章存在性判断
     */
    @ExcelProperty(value = "发票专用章存在性判断")
    private String invoiceStamp;

    /**
     * token
     */
    @ExcelProperty(value = "token")
    private String saveToken;

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

    /**
     * 查验结果
     */
    @ExcelProperty(value = "查验结果")
    private String checkResult;

    /**
     * 入台账标识
     */
    @ExcelProperty(value = "入台账标识")
    private String pushBusinessInfoFlag;


}
