package org.smartlink.business.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.smartlink.business.domain.DataDidiItinerary;
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
 * 滴滴行程单视图对象 data_didi_itinerary
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataDidiItinerary.class)
public class DataDidiItineraryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 图片表主键
     */
    @ExcelProperty(value = "图片表主键")
    private String fileId;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private Date invoiceDate;

    /**
     * 行程结束时间
     */
    @ExcelProperty(value = "行程结束时间")
    private String timeGetOff;

    /**
     * 行程开始时间
     */
    @ExcelProperty(value = "行程开始时间")
    private String timeGetOn;

    /**
     * 行程人手机号
     */
    @ExcelProperty(value = "行程人手机号")
    private String phone;

    /**
     * 总计
     */
    @ExcelProperty(value = "总计")
    private Long invoiceTotal;

    /**
     * 发票消费类型
     */
    @ExcelProperty(value = "发票消费类型")
    private String kind;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @ExcelProperty(value = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]")
    private String region;

    /**
     * token
     */
    @ExcelProperty(value = "token")
    private String saveToken;

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
