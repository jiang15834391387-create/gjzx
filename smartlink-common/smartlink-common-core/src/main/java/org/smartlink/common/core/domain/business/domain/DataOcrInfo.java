package org.smartlink.common.core.domain.business.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 增值税发票对象 data_ocr_info
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_ocr_info")
public class DataOcrInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 流水号
     */
    private String businessSerialNo;

    /**
     * 发票类型
     */
    private String pattern;

    /**
     * 购方银行账号
     */
    private String buyerAccount;

    /**
     * 购方地址
     */
    private String buyerAddress;

    /**
     * 购方名称
     */
    private String buyerName;

    /**
     * 购方纳税识别号
     */
    private String buyerNo;

    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    private String checkInvoice;

    /**
     * 作废标志 (N未作废,Y已作废)
     */
    private String cancellationMark;

    /**
     * 复合人
     */
    private String checker;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 图片表id
     */
    private String fileId;

    /**
     * 标题
     */
    private String title;

    /**
     * 发票代码
     */
    private String invoiceCode;

    /**
     * 发票日期
     */
    private String invoiceDate;

    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 开票人
     */
    private String issuer;

    /**
     * 品名，每个以逗号隔开
     */
    private String itemNames;

    /**
     * 校验码
     */
    private String checkCode;

    /**
     * 小写金额，（价税合计）
     */
    private String totalLowercase;

    /**
     * 税前金额
     */
    private String pretaxAmount;

    /**
     * 是否有公司印章(0: 没有; 1: 有)
     */
    private String companySeal;

    /**
     * 是否有销售方公司印章(0: 没有; 1: 有)
     */
    private String sellCompanySeal;

    /**
     * 发票联
     */
    private String pageNumber;

    /**
     * 发票联次
     */
    private String invoiceSheet;

    /**
     * 机械码
     */
    private String machineCode;

    /**
     * 种类
     */
    private String category;

    /**
     * 密码区
     */
    private String password1;

    /**
     * 全电发票号码（当存在该字段时移除 ciphertext）
     */
    private String electronicNumber;

    /**
     * 车船税
     */
    private String travelTax;

    /**
     * 收款人
     */
    private String payee;

    /**
     * 销货方纳税账户
     */
    private String sellerAccount;

    /**
     * 销货方纳税地址
     */
    private String sellerAddress;

    /**
     * 销货方纳税名称
     */
    private String sellerName;

    /**
     * 销货方纳税编号
     */
    private String sellerNo;

    /**
     * 合计金额（税前）
     */
    private String sumAmount;

    /**
     * 总税额
     */
    private String sumTax;

    /**
     * 大写金额，（加税合计）
     */
    private String totalUppercase;

    /**
     * 是否收购
     */
    private String purchaseMark;

    /**
     *  区块链标记
     */
    private String blockChain;

    /**
     * 是否为电子增票
     */
    private String electronicMark;

    /**
     * 通行费标志
     */
    private String transitMark;

    /**
     * 机动车标志
     */
    private String oilMark;

    /**
     * 机动车标志
     */
    private String vehicleMark;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String region;

    /**
     * 红冲/非红冲
     */
    private String redDashed;

    /**
     * qr码
     */
    private String qrCode;

    /**
     * 右侧打印开票日期
     */
    private String rightInvoiceDate;

    /**
     * 增加电子支付标识
     */
    private String elePayId;

    /**
     * 打印发票代码
     */
    private String printInvoiceCode;

    /**
     * 备注校验码
     */
    private String noteCheckCode;

    /**
     * 打印合计
     */
    private String printTotal;

    /**
     * 打印校验码
     */
    private String printCheckCode;

    /**
     * 右侧打印发票号码
     */
    private String rightInvoiceNumber;

    /**
     * 右侧打印发票代码
     */
    private String rightInvoiceCode;

    /**
     * 代开(非代开条目为空)
     */
    private String replaceOpen;

    /**
     * 差额征税
     */
    private String deduction;

    /**
     * 备注手写
     */
    private String handwrite;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本号
     */
    @Version
    private Long version;


}
