package org.smartlink.server.nc.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.smartlink.common.excel.convert.ExcelDictConvert;
import org.smartlink.server.nc.annotation.ExcelDictFormat;

import java.math.BigDecimal;
import java.util.Date;


/**
 * ocr信息视图对象 data_ocr_info
 *
 * @author ruoyi
 * @date 2022-04-01
 */
@Data
@ApiModel("ocr信息视图对象")
@ExcelIgnoreUnannotated
public class DataOcrInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private String id;

    /**
     * 流水号
     */
    @ExcelProperty(value = "流水号")
    @ApiModelProperty("流水号")
    private String businessSerialNo;

    /**
     * 购方银行账号
     */
    @ExcelProperty(value = "购方银行账号")
    @ApiModelProperty("购方银行账号")
    private String buyerAccount;

    /**
     * 购方地址
     */
    @ExcelProperty(value = "购方地址")
    @ApiModelProperty("购方地址")
    private String buyerAddress;

    /**
     * 购方名称
     */
    @ExcelProperty(value = "购方名称")
    @ApiModelProperty("购方名称")
    private String buyerName;

    /**
     * 购方纳税识别号
     */
    @ExcelProperty(value = "购方纳税识别号")
    @ApiModelProperty("购方纳税识别号")
    private String buyerNo;

    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    @ExcelProperty(value = "是否查验标识，", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=查验失败，1查验成功")
    @ApiModelProperty("是否查验标识，（0查验失败，1查验成功）")
    private String checkInvoice;

    /**
     * 作废标志 (N未作废,Y已作废)
     */
    @ExcelProperty(value = "作废标志 (N未作废,Y已作废)")
    @ApiModelProperty("作废标志 (N未作废,Y已作废)")
    private String cancellationMark;

    /**
     * 查验结果
     */
    @ExcelProperty(value = "查验结果")
    @ApiModelProperty("查验结果")
    private String checkResult;

    /**
     * 复合人
     */
    @ExcelProperty(value = "复合人")
    @ApiModelProperty("复合人")
    private String checker;

    /**
     * 省
     */
    @ExcelProperty(value = "省")
    @ApiModelProperty("省")
    private String province;

    /**
     * 市
     */
    @ExcelProperty(value = "市")
    @ApiModelProperty("市")
    private String city;

    /**
     * 图片表id
     */
    @ExcelProperty(value = "图片表id")
    @ApiModelProperty("图片表id")
    private String fileId;

    /**
     * 发票代码
     */
    @ExcelProperty(value = "发票代码")
    @ApiModelProperty("发票代码")
    private String invoiceCode;

    /**
     * 发票日期
     */
    @ExcelProperty(value = "发票日期")
    @ApiModelProperty("发票日期")
    private Date invoiceDate;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    @ApiModelProperty("发票号码")
    private String invoiceNumber;

    /**
     * 开票人
     */
    @ExcelProperty(value = "开票人")
    @ApiModelProperty("开票人")
    private String issuer;

    /**
     * 品名，每个以逗号隔开
     */
    @ExcelProperty(value = "品名，每个以逗号隔开")
    @ApiModelProperty("品名，每个以逗号隔开")
    private String itemNames;

    /**
     * 校验码
     */
    @ExcelProperty(value = "校验码")
    @ApiModelProperty("校验码")
    private String checkCode;

    /**
     * 小写金额，（价税合计）
     */
    @ExcelProperty(value = "小写金额，", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "价=税合计")
    @ApiModelProperty("小写金额，（价税合计）")
    private BigDecimal totalLowercase;

    /**
     * 税前金额
     */
    @ExcelProperty(value = "税前金额")
    @ApiModelProperty("税前金额")
    private BigDecimal pretaxAmount;

    /**
     * 是否有公司印章（0: 没有; 1: 有）
     */
    @ExcelProperty(value = "是否有公司印章", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=:,没=有;,1=:,有=")
    @ApiModelProperty("是否有公司印章（0: 没有; 1: 有）")
    private String companySeal;

    /**
     * 是否有 '销售方（章）' 标记（0: 没有; 1: 有）
     */
    @ExcelProperty(value = "是否有 '销售方（章）' 标记（0: 没有; 1: 有）", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=:,没=有;,1=:,有=")
    @ApiModelProperty("是否有 '销售方（章）' 标记（0: 没有; 1: 有）")
    private String sellCompanySeal;

    /**
     * 发票联
     */
    @ExcelProperty(value = "发票联")
    @ApiModelProperty("发票联")
    private String pageNumber;

    /**
     * 发票联次
     */
    @ExcelProperty(value = "发票联次")
    @ApiModelProperty("发票联次")
    private String invoiceSheet;

    /**
     * 机械码
     */
    @ExcelProperty(value = "机械码")
    @ApiModelProperty("机械码")
    private String machineCode;

    /**
     * 密码区
     */
    @ExcelProperty(value = "密码区")
    @ApiModelProperty("密码区")
    private String password1;

    /**
     * 收款人
     */
    @ExcelProperty(value = "收款人")
    @ApiModelProperty("收款人")
    private String payee;

    /**
     * 销货方纳税账户
     */
    @ExcelProperty(value = "销货方纳税账户")
    @ApiModelProperty("销货方纳税账户")
    private String sellerAccount;

    /**
     * 销货方纳税地址
     */
    @ExcelProperty(value = "销货方纳税地址")
    @ApiModelProperty("销货方纳税地址")
    private String sellerAddress;

    /**
     * 销货方纳税名称
     */
    @ExcelProperty(value = "销货方纳税名称")
    @ApiModelProperty("销货方纳税名称")
    private String sellerName;

    /**
     * 销货方纳税编号
     */
    @ExcelProperty(value = "销货方纳税编号")
    @ApiModelProperty("销货方纳税编号")
    private String sellerNo;

    /**
     * 合计金额（税前）
     */
    @ExcelProperty(value = "合计金额", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "税=前")
    @ApiModelProperty("合计金额（税前）")
    private BigDecimal sumAmount;

    /**
     * 总税额
     */
    @ExcelProperty(value = "总税额")
    @ApiModelProperty("总税额")
    private BigDecimal sumTax;

    /**
     * 大写金额，（加税合计）
     */
    @ExcelProperty(value = "大写金额，", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "加=税合计")
    @ApiModelProperty("大写金额，（加税合计）")
    private String totalUppercase;

    /**
     * 是否收购
     */
    @ExcelProperty(value = "是否收购")
    @ApiModelProperty("是否收购")
    private String purchaseMark;

    /**
     *  区块链标记
     */
    @ExcelProperty(value = " 区块链标记")
    @ApiModelProperty(" 区块链标记")
    private String blockChain;

    /**
     * 是否为电子增票
     */
    @ExcelProperty(value = "是否为电子增票")
    @ApiModelProperty("是否为电子增票")
    private String electronicMark;

    /**
     * 通行费标志
     */
    @ExcelProperty(value = "通行费标志")
    @ApiModelProperty("通行费标志")
    private String transitMark;

    /**
     * 机动车标志
     */
    @ExcelProperty(value = "机动车标志")
    @ApiModelProperty("机动车标志")
    private String oilMark;

    /**
     * 机动车标志
     */
    @ExcelProperty(value = "机动车标志")
    @ApiModelProperty("机动车标志")
    private String vehicleMark;

    /**
     * 税务云token
     */
    @ExcelProperty(value = "税务云token")
    @ApiModelProperty("税务云token")
    private String saveToken;

    /**
     * 红冲/非红冲
     */
    @ExcelProperty(value = "红冲/非红冲")
    @ApiModelProperty("红冲/非红冲")
    private String redDashed;

    /**
     * qr码
     */
    @ExcelProperty(value = "qr码")
    @ApiModelProperty("qr码")
    private String qrCode;

    /**
     * 右侧打印开票日期
     */
    @ExcelProperty(value = "右侧打印开票日期")
    @ApiModelProperty("右侧打印开票日期")
    private String rightInvoiceDate;

    /**
     * 打印发票代码
     */
    @ExcelProperty(value = "打印发票代码")
    @ApiModelProperty("打印发票代码")
    private String elePayId;

    /**
     * 打印发票代码
     */
    @ExcelProperty(value = "打印发票代码")
    @ApiModelProperty("打印发票代码")
    private String printInvoiceCode;

    /**
     * 备注校验码
     */
    @ExcelProperty(value = "备注校验码")
    @ApiModelProperty("备注校验码")
    private String noteCheckCode;

    /**
     * 打印合计
     */
    @ExcelProperty(value = "打印合计")
    @ApiModelProperty("打印合计")
    private String printTotal;

    /**
     * 打印校验码
     */
    @ExcelProperty(value = "打印校验码")
    @ApiModelProperty("打印校验码")
    private String printCheckCode;

    /**
     * 右侧打印发票号码
     */
    @ExcelProperty(value = "右侧打印发票号码")
    @ApiModelProperty("右侧打印发票号码")
    private String rightInvoiceNumber;

    /**
     * 右侧打印发票代码
     */
    @ExcelProperty(value = "右侧打印发票代码")
    @ApiModelProperty("右侧打印发票代码")
    private String rightInvoiceCode;

    /**
     * 代开(非代开条目为空)
     */
    @ExcelProperty(value = "代开(非代开条目为空)")
    @ApiModelProperty("代开(非代开条目为空)")
    private String replaceOpen;

    /**
     * 备注手写
     */
    @ExcelProperty(value = "备注手写")
    @ApiModelProperty("备注手写")
    private String handwrite;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ExcelProperty(value = "是否删除标识 0-不删除  1-删除")
    @ApiModelProperty("是否删除标识 0-不删除  1-删除")
    private String deleteFlag;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 置信度
     */
    @ExcelProperty(value = "置信度")
    @ApiModelProperty("置信度")
    private String confidence;

    /**
     *  发票类型: 0专票 1普票
     */
    @ExcelProperty(value = "发票类型")
    @ApiModelProperty("发票类型")
    private String pattern;

    /**
     * 台账推送业务系统成功标识
     */
    @ExcelProperty(value = "台账推送业务系统成功标识")
    @ApiModelProperty("台账推送业务系统成功标识")
    private String pushBusinessInfoFlag;

}
