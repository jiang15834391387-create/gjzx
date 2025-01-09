package org.smartlink.common.entity.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;

import java.io.Serial;
import java.io.Serializable;


/**
 * 增值税发票视图对象 data_ocr_info
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataOcrInfo.class)
public class DataOcrInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 流水号
     */
    @ExcelProperty(value = "流水号")
    private String businessSerialNo;

    /**
     * 发票类型
     */
    @ExcelProperty(value = "发票类型")
    private String pattern;

    /**
     * 购方银行账号
     */
    @ExcelProperty(value = "购方银行账号")
    private String buyerAccount;

    /**
     * 购方地址
     */
    @ExcelProperty(value = "购方地址")
    private String buyerAddress;

    /**
     * 购方名称
     */
    @ExcelProperty(value = "购方名称")
    private String buyerName;

    /**
     * 购方纳税识别号
     */
    @ExcelProperty(value = "购方纳税识别号")
    private String buyerNo;

    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    @ExcelProperty(value = "是否查验标识，", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=查验失败，1查验成功")
    private String checkInvoice;

    /**
     * 作废标志 (N未作废,Y已作废)
     */
    @ExcelProperty(value = "作废标志 (N未作废,Y已作废)")
    private String cancellationMark;

    /**
     * 复合人
     */
    @ExcelProperty(value = "复合人")
    private String checker;

    /**
     * 省
     */
    @ExcelProperty(value = "省")
    private String province;

    /**
     * 市
     */
    @ExcelProperty(value = "市")
    private String city;

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
     * 发票代码
     */
    @ExcelProperty(value = "发票代码")
    private String invoiceCode;

    /**
     * 发票日期
     */
    @ExcelProperty(value = "发票日期")
    private String invoiceDate;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 开票人
     */
    @ExcelProperty(value = "开票人")
    private String issuer;

    /**
     * 品名，每个以逗号隔开
     */
    @ExcelProperty(value = "品名，每个以逗号隔开")
    private String itemNames;

    /**
     * 校验码
     */
    @ExcelProperty(value = "校验码")
    private String checkCode;

    /**
     * 小写金额，（价税合计）
     */
    @ExcelProperty(value = "小写金额，", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "价=税合计")
    private String totalLowercase;

    /**
     * 税前金额
     */
    @ExcelProperty(value = "税前金额")
    private String pretaxAmount;

    /**
     * 是否有公司印章(0: 没有; 1: 有)
     */
    @ExcelProperty(value = "是否有公司印章(0: 没有; 1: 有)")
    private String companySeal;

    /**
     * 是否有销售方公司印章(0: 没有; 1: 有)
     */
    @ExcelProperty(value = "是否有销售方公司印章(0: 没有; 1: 有)")
    private String sellCompanySeal;

    /**
     * 发票联
     */
    @ExcelProperty(value = "发票联")
    private String pageNumber;

    /**
     * 发票联次
     */
    @ExcelProperty(value = "发票联次")
    private String invoiceSheet;

    /**
     * 机械码
     */
    @ExcelProperty(value = "机械码")
    private String machineCode;

    /**
     * 种类
     */
    @ExcelProperty(value = "种类")
    private String category;

    /**
     * 密码区
     */
    @ExcelProperty(value = "密码区")
    private String password1;

    /**
     * 全电发票号码（当存在该字段时移除 ciphertext）
     */
    @ExcelProperty(value = "全电发票号码", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "当=存在该字段时移除,c=iphertext")
    private String electronicNumber;

    /**
     * 车船税
     */
    @ExcelProperty(value = "车船税")
    private String travelTax;

    /**
     * 收款人
     */
    @ExcelProperty(value = "收款人")
    private String payee;

    /**
     * 销货方纳税账户
     */
    @ExcelProperty(value = "销货方纳税账户")
    private String sellerAccount;

    /**
     * 销货方纳税地址
     */
    @ExcelProperty(value = "销货方纳税地址")
    private String sellerAddress;

    /**
     * 销货方纳税名称
     */
    @ExcelProperty(value = "销货方纳税名称")
    private String sellerName;

    /**
     * 销货方纳税编号
     */
    @ExcelProperty(value = "销货方纳税编号")
    private String sellerNo;

    /**
     * 合计金额（税前）
     */
    @ExcelProperty(value = "合计金额", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "税=前")
    private String sumAmount;

    /**
     * 总税额
     */
    @ExcelProperty(value = "总税额")
    private String sumTax;

    /**
     * 大写金额，（加税合计）
     */
    @ExcelProperty(value = "大写金额，", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "加=税合计")
    private String totalUppercase;

    /**
     * 是否收购
     */
    @ExcelProperty(value = "是否收购")
    private String purchaseMark;

    /**
     *  区块链标记
     */
    @ExcelProperty(value = " 区块链标记")
    private String blockChain;

    /**
     * 是否为电子增票
     */
    @ExcelProperty(value = "是否为电子增票")
    private String electronicMark;

    /**
     * 通行费标志
     */
    @ExcelProperty(value = "通行费标志")
    private String transitMark;

    /**
     * 机动车标志
     */
    @ExcelProperty(value = "机动车标志")
    private String oilMark;

    /**
     * 机动车标志
     */
    @ExcelProperty(value = "机动车标志")
    private String vehicleMark;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @ExcelProperty(value = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]")
    private String region;

    /**
     * 红冲/非红冲
     */
    @ExcelProperty(value = "红冲/非红冲")
    private String redDashed;

    /**
     * qr码
     */
    @ExcelProperty(value = "qr码")
    private String qrCode;

    /**
     * 右侧打印开票日期
     */
    @ExcelProperty(value = "右侧打印开票日期")
    private String rightInvoiceDate;

    /**
     * 增加电子支付标识
     */
    @ExcelProperty(value = "增加电子支付标识")
    private String elePayId;

    /**
     * 打印发票代码
     */
    @ExcelProperty(value = "打印发票代码")
    private String printInvoiceCode;

    /**
     * 备注校验码
     */
    @ExcelProperty(value = "备注校验码")
    private String noteCheckCode;

    /**
     * 打印合计
     */
    @ExcelProperty(value = "打印合计")
    private String printTotal;

    /**
     * 打印校验码
     */
    @ExcelProperty(value = "打印校验码")
    private String printCheckCode;

    /**
     * 右侧打印发票号码
     */
    @ExcelProperty(value = "右侧打印发票号码")
    private String rightInvoiceNumber;

    /**
     * 右侧打印发票代码
     */
    @ExcelProperty(value = "右侧打印发票代码")
    private String rightInvoiceCode;

    /**
     * 代开(非代开条目为空)
     */
    @ExcelProperty(value = "代开(非代开条目为空)")
    private String replaceOpen;

    /**
     * 差额征税
     */
    @ExcelProperty(value = "差额征税")
    private String deduction;

    /**
     * 备注手写
     */
    @ExcelProperty(value = "备注手写")
    private String handwrite;

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
