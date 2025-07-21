package org.smartlink.common.entity.domain.business.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

/**
 * 增值税发票业务对象 data_ocr_info
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataOcrInfo.class, reverseConvertGenerate = false)
public class DataOcrInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 流水号
     */
    @NotBlank(message = "流水号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String businessSerialNo;

    /**
     * 发票消费类型
     */
    @NotBlank(message = "发票消费类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String kind;

    /**
     * 购方银行账号
     */
    @NotBlank(message = "购方银行账号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerAccount;

    /**
     * 购方地址
     */
    @NotBlank(message = "购方地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerAddress;

    /**
     * 购方名称
     */
    @NotBlank(message = "购方名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerName;

    /**
     * 购方纳税识别号
     */
    @NotBlank(message = "购方纳税识别号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerNo;

    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    @NotBlank(message = "是否查验标识，（0查验失败，1查验成功）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkInvoice;

    /**
     * 作废标志 (N未作废,Y已作废)
     */
    @NotBlank(message = "作废标志 (N未作废,Y已作废)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cancellationMark;

    /**
     * 复合人
     */
    @NotBlank(message = "复合人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checker;

    /**
     * 省
     */
    @NotBlank(message = "省不能为空", groups = { AddGroup.class, EditGroup.class })
    private String province;

    /**
     * 地区
     */
    @NotBlank(message = "地区不能为空", groups = { AddGroup.class, EditGroup.class })
    private String area;

    /**
     * 市
     */
    @NotBlank(message = "市不能为空", groups = { AddGroup.class, EditGroup.class })
    private String city;

    /**
     * 图片表id
     */
    @NotBlank(message = "图片表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 发票代码
     */
    @NotBlank(message = "发票代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceCode;

    /**
     * 发票日期
     */
    @NotBlank(message = "发票日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceDate;

    /**
     * 发票号码
     */
    @NotBlank(message = "发票号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceNumber;

    /**
     * 开票人
     */
    @NotBlank(message = "开票人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String issuer;

    /**
     * 品名，每个以逗号隔开
     */
    @NotBlank(message = "品名，每个以逗号隔开不能为空", groups = { AddGroup.class, EditGroup.class })
    private String itemNames;

    /**
     * 校验码
     */
    @NotBlank(message = "校验码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkCode;

    /**
     * 小写金额，（价税合计）
     */
    @NotBlank(message = "小写金额，（价税合计）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String totalLowercase;

    /**
     * 税前金额
     */
    @NotBlank(message = "税前金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String pretaxAmount;

    /**
     * 是否有公司印章(0: 没有; 1: 有)
     */
    @NotBlank(message = "是否有公司印章(0: 没有; 1: 有)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String companySeal;

    /**
     * 是否有销售方公司印章(0: 没有; 1: 有)
     */
    @NotBlank(message = "是否有销售方公司印章(0: 没有; 1: 有)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellCompanySeal;

    /**
     * 发票联
     */
    @NotBlank(message = "发票联不能为空", groups = { AddGroup.class, EditGroup.class })
    private String pageNumber;

    /**
     * 总计
     */
    private String invoiceTotal;

    /**
     * 服务类型
     */
    private String serviceName;

    /**
     * 发票联次
     */
    @NotBlank(message = "发票联次不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceSheet;

    /**
     * 机械码
     */
    @NotBlank(message = "机械码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String machineCode;

    /**
     * 种类
     */
    @NotBlank(message = "种类不能为空", groups = { AddGroup.class, EditGroup.class })
    private String category;

    /**
     * 密码区
     */
    @NotBlank(message = "密码区不能为空", groups = { AddGroup.class, EditGroup.class })
    private String password1;

    /**
     * 全电发票号码（当存在该字段时移除 ciphertext）
     */
    @NotBlank(message = "全电发票号码（当存在该字段时移除 ciphertext）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String electronicNumber;

    /**
     * 车船税
     */
    @NotBlank(message = "车船税不能为空", groups = { AddGroup.class, EditGroup.class })
    private String travelTax;

    /**
     * 收款人
     */
    @NotBlank(message = "收款人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payee;

    /**
     * 销货方纳税账户
     */
    @NotBlank(message = "销货方纳税账户不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerAccount;

    /**
     * 销货方纳税地址
     */
    @NotBlank(message = "销货方纳税地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerAddress;

    /**
     * 销货方纳税名称
     */
    @NotBlank(message = "销货方纳税名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerName;

    /**
     * 销货方纳税编号
     */
    @NotBlank(message = "销货方纳税编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerNo;

    /**
     * 合计金额（税前）
     */
    @NotBlank(message = "合计金额（税前）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sumAmount;

    /**
     * 总税额
     */
    @NotBlank(message = "总税额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sumTax;

    /**
     * 大写金额，（加税合计）
     */
    @NotBlank(message = "大写金额，（加税合计）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String totalUppercase;

    /**
     * 是否收购
     */
    @NotBlank(message = "是否收购不能为空", groups = { AddGroup.class, EditGroup.class })
    private String purchaseMark;

    /**
     *  区块链标记
     */
    @NotBlank(message = " 区块链标记不能为空", groups = { AddGroup.class, EditGroup.class })
    private String blockChain;

    /**
     * 是否为电子增票
     */
    @NotBlank(message = "是否为电子增票不能为空", groups = { AddGroup.class, EditGroup.class })
    private String electronicMark;

    /**
     * 通行费标志
     */
    @NotBlank(message = "通行费标志不能为空", groups = { AddGroup.class, EditGroup.class })
    private String transitMark;

    /**
     * 机动车标志
     */
    @NotBlank(message = "机动车标志不能为空", groups = { AddGroup.class, EditGroup.class })
    private String oilMark;

    /**
     * 机动车标志
     */
    @NotBlank(message = "机动车标志不能为空", groups = { AddGroup.class, EditGroup.class })
    private String vehicleMark;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @NotBlank(message = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]不能为空", groups = { AddGroup.class, EditGroup.class })
    private String region;

    /**
     * 图片旋转角度
     */
    @NotBlank(message = "图片旋转角度不能为空", groups = { AddGroup.class, EditGroup.class })
    private String orientation;

    /**
     * 红冲/非红冲
     */
    @NotBlank(message = "红冲/非红冲不能为空", groups = { AddGroup.class, EditGroup.class })
    private String redDashed;

    /**
     * qr码
     */
    @NotBlank(message = "qr码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String qrCode;

    /**
     * 右侧打印开票日期
     */
    @NotBlank(message = "右侧打印开票日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String rightInvoiceDate;

    /**
     * 增加电子支付标识
     */
    @NotBlank(message = "增加电子支付标识不能为空", groups = { AddGroup.class, EditGroup.class })
    private String elePayId;

    /**
     * 打印发票代码
     */
    @NotBlank(message = "打印发票代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String printInvoiceCode;

    /**
     * 备注校验码
     */
    @NotBlank(message = "备注校验码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String noteCheckCode;

    /**
     * 打印合计
     */
    @NotBlank(message = "打印合计不能为空", groups = { AddGroup.class, EditGroup.class })
    private String printTotal;

    /**
     * 打印校验码
     */
    @NotBlank(message = "打印校验码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String printCheckCode;

    /**
     * 右侧打印发票号码
     */
    @NotBlank(message = "右侧打印发票号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String rightInvoiceNumber;

    /**
     * 右侧打印发票代码
     */
    @NotBlank(message = "右侧打印发票代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String rightInvoiceCode;

    /**
     * 代开(非代开条目为空)
     */
    @NotBlank(message = "代开(非代开条目为空)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String replaceOpen;

    /**
     * 差额征税
     */
    @NotBlank(message = "差额征税不能为空", groups = { AddGroup.class, EditGroup.class })
    private String deduction;

    /**
     * 备注手写
     */
    @NotBlank(message = "备注手写不能为空", groups = { AddGroup.class, EditGroup.class })
    private String handwrite;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @NotBlank(message = "是否删除标识 0-不删除  1-删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private String deleteFlag;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
