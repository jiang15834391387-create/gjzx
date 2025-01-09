package org.smartlink.common.core.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.business.domain.DataTollRoads;

import java.io.Serial;
import java.io.Serializable;


/**
 * 过路费视图对象 data_toll_roads
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataTollRoads.class)
public class DataTollRoadsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

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
     * 入口
     */
    @ExcelProperty(value = "入口")
    private String entrance;

    /**
     * 出口
     */
    @ExcelProperty(value = "出口")
    private String exit;

    /**
     * 图片表id
     */
    @ExcelProperty(value = "图片表id")
    private String fileId;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

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
     * 发票专用章存在性判断
     */
    @ExcelProperty(value = "发票专用章存在性判断")
    private String invoiceStamp;

    /**
     * 高速标志(0:没有; 1: 有)
     */
    @ExcelProperty(value = "高速标志(0:没有; 1: 有)")
    private String highwayFlag;

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
