package org.smartlink.server.nc.domain.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.smartlink.server.nc.annotation.FieldName;
import org.smartlink.server.nc.domain.modle.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * ocr信息对象 data_ocr_info
 *
 * @author L
 * @date
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("data_ocr_info")
public class DataOcrInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    @FieldName(value = "主键")
    private String id;

    /**
     * 购方银行账号
     */
    @FieldName(value = "购方银行账号")
    private String buyerAccount;
    /**
     * 购方地址
     */
    @FieldName(value = "购方地址")
    private String buyerAddress;
    /**
     * 购方名称
     */
    @FieldName(value = "购方名称")
    private String buyerName;
    /**
     * 购方纳税识别号
     */
    @FieldName(value = "购方纳税识别号")
    private String buyerNo;
    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    @FieldName(value = "是否查验标识")
    private String checkInvoice;
    /**
     * 作废标志 (N未作废,Y已作废)
     */
    @FieldName(value = "作废标志")
    private String cancellationMark;
    /**
     * 查验结果
     */
    @FieldName(value = "查验结果")
    private String checkResult;
    /**
     * 复合人
     */
    @FieldName(value = "复核人")
    private String checker;
    /**
     * 省
     */
    @FieldName(value="省")
    private String province;
    /**
     * 市
     */
    @FieldName(value="市")
    private String city;
    /**
     * 图片表id
     */
    @FieldName(value = "图片表id")
    private String fileId;
    /**
     * 发票代码
     */
    @FieldName(value = "发票代码")
    private String invoiceCode;
    /**
     * 发票日期
     */
    @FieldName(value = "发票日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    /**
     * 发票号码
     */
    @FieldName(value = "发票号码")
    private String invoiceNumber;

    /**
     * 数电票号码
     */
    @FieldName(value="数电票号码")
    private String electronicNumber;
    /**
     * 开票人
     */
    @FieldName(value = "开票人")
    private String issuer;
    /**
     * 品名，每个以逗号隔开
     */
    @FieldName(value = "品名")
    private String itemNames;
    /**
     * 校验码
     */
    @FieldName(value = "校验码")
    private String checkCode;
    /**
     * 小写金额，（价税合计）
     */
    @FieldName(value = "小写金额")
    private BigDecimal totalLowercase;
    /**
     * 税前金额
     */
    @FieldName(value="税前金额")
    private BigDecimal pretaxAmount;
    /**
     * 是否有公司印章（0: 没有; 1: 有）
     */
    @FieldName(value="是否有公司印章")
    private String companySeal;
    /**
     * 是否有公司印章（0: 没有; 1: 有）
     */
    @FieldName(value="是否有公司印章")
    private String sellCompanySeal;
    /**
     * 发票联
     */
    @FieldName(value="发票联")
    private String pageNumber;
    /**
     * 发票联次
     */
    @FieldName(value="发票联次")
    private String invoiceSheet;
    /**
     * 机械码
     */
    @FieldName(value = "机械码")
    private String machineCode;
    /**
     * 密码区
     */
    @FieldName(value = "密码区")
    private String password1;
    /**
     * 收款人
     */
    @FieldName(value = "收款人")
    private String payee;
    /**
     * 销货方纳税账户
     */
    @FieldName(value = "销货方纳税账户")
    private String sellerAccount;
    /**
     * 销货方纳税地址
     */
    @FieldName(value = "销货方纳税地址")
    private String sellerAddress;
    /**
     * 销货方纳税名称
     */
    @FieldName(value = "销货方纳税名称")
    private String sellerName;
    /**
     * 销货方纳税编号
     */
    @FieldName(value = "销货方纳税编号")
    private String sellerNo;
    /**
     * 合计金额（税前）
     */
    @FieldName(value = "合计金额")
    private BigDecimal sumAmount;
    /**
     * 总税额
     */
    @FieldName(value = "总税额")
    private BigDecimal sumTax;
    /**
     * 大写金额，（加税合计）
     */
    @FieldName(value = "大写金额")
    private String totalUppercase;
    /**
     * 是否收购
     */
    @FieldName(value="是否收购")
    private String purchaseMark;
    /**
     *  区块链标记
     */
    @FieldName(value=" 区块链标记")
    private String blockChain;
    /**
     * 是否为电子增票
     */
    @FieldName(value="是否为电子增票")
    private String electronicMark;
    /**
     * 通行费标志
     */
    @FieldName(value="通行费标志")
    private String transitMark;
    /**
     * 机动车标志
     */
    @FieldName(value="机动车标志")
    private String oilMark;
    /**
     * 机动车标志
     */
    @FieldName(value="机动车标志")
    private String vehicleMark;
    /**
     * 税务云token
     */
    @FieldName(value = "税务云token")
    private String saveToken;
    /**
     * 红冲/非红冲
     */
    @FieldName(value="红冲/非红冲")
    private String redDashed;
    /**
     * qr码
     */
    @FieldName(value="qr码")
    private String qrCode;
    /**
     * 右侧打印开票日期
     */
    @FieldName(value="右侧打印开票日期")
    private String rightInvoiceDate;
    /**
     * 打印发票代码
     */
    @FieldName(value="打印发票代码")
    private String elePayId;
    /**
     * 打印发票代码
     */
    @FieldName(value="打印发票代码")
    private String printInvoiceCode;
    /**
     * 备注校验码
     */
    @FieldName(value="备注校验码")
    private String noteCheckCode;
    /**
     * 打印合计
     */
    @FieldName(value="打印合计")
    private String printTotal;
    /**
     * 打印校验码
     */
    @FieldName(value="打印校验码")
    private String printCheckCode;
    /**
     * 右侧打印发票号码
     */
    @FieldName(value="右侧打印发票号码")
    private String rightInvoiceNumber;
    /**
     * 右侧打印发票代码
     */
    @FieldName(value="右侧打印发票代码")
    private String rightInvoiceCode;
    /**
     * 代开(非代开条目为空)
     */
    @FieldName(value="代开(非代开条目为空)")
    private String replaceOpen;
    /**
     * 备注手写
     */
    @FieldName(value="备注手写")
    private String handwrite;
    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @FieldName(value = "是否删除标识")
    private String deleteFlag;
    /**
     * 租户id
     */
    @FieldName(value="租户id")
    private String tenantId;
    /**
     * 备注
     */
    @FieldName(value = "备注")
    private String remark;
    /**
     * 置信度
     */
    @FieldName(value = "置信度")
    private String confidence;

    /**
     * OCR详情
     */
    @TableField(exist = false)
    private List<DataOcrDetails> details;

    /**
     *  发票类型: 0专票 1普票
     */
    @FieldName(value = "发票类型")
    private String pattern;

    /**
     * 台账推送业务系统成功标识
     */
    @FieldName(value="台账推送业务系统成功标识")
    private String pushBusinessInfoFlag;

    /**
     * 推送ncc台账图片id
     */
    @FieldName(value="推送ncc台账图片id")
    private String ncImageId;
    /**
     * 坐标
     */
    @FieldName(value = "坐标")
    private String coordinateStr;
    /**
     * 暂存状态
     */
    @FieldName(value = "暂存状态")
    private String isStaging;
}
