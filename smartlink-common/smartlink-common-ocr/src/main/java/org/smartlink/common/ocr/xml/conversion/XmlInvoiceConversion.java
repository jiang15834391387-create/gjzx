package org.smartlink.common.ocr.xml.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * XML增值税发票转换类
 */
public class XmlInvoiceConversion implements ChangeIdentifyInfo<JSONObject> {

    private static class LazyHolder {
        private static final XmlInvoiceConversion INSTANCE = new XmlInvoiceConversion();
    }

    private XmlInvoiceConversion() {
    }

    public static XmlInvoiceConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject xmlData) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();

        // 获取xbrl节点下的所有数据
        JSONObject xbrlData = xmlData.getJSONObject("xbrli:xbrl");
        if (xbrlData == null) {
            return null;
        }

        DataOcrInfo invoice = new DataOcrInfo();
        invoice.setFileId(dataImageFilesInfo.getFileId());

        // 基本发票信息映射
        invoice.setInvoiceCode(getContent(xbrlData, "inv:CodeOfInvoice"));
        invoice.setInvoiceNumber(getContent(xbrlData, "inv:NumberOfInvoice"));
        invoice.setInvoiceDate(getContent(xbrlData, "inv:DateOfIssue"));
        invoice.setSumTax(getContent(xbrlData, "inv:TotalTaxAmount"));
        invoice.setPretaxAmount(getContent(xbrlData, "inv:TotalAmountExcludingTax"));
        invoice.setInvoiceTotal(getContent(xbrlData, "inv:TaxIncludedAmountInFigures"));
        invoice.setTotalUppercase(getContent(xbrlData, "inv:TaxIncludedAmountInWords"));
        invoice.setCheckCode(getContent(xbrlData, "inv:IdentifyingCode"));

        // 销售方信息映射
        invoice.setSellerName(getContent(xbrlData, "inv:NameOfSeller"));
        invoice.setSellerNo(getContent(xbrlData, "inv:TaxpayerIdentificationNumberUnifiedSocialCreditCodeOfSeller"));
        invoice.setSellerAddress(getContent(xbrlData, "inv:AddressPhoneNumberOfSeller"));
        invoice.setSellerAccount(getContent(xbrlData, "inv:DepositBankAndAccountNumberOfSeller"));

        // 购买方信息映射
        invoice.setBuyerName(getContent(xbrlData, "inv:NameOfPurchaser"));
        invoice.setBuyerNo(getContent(xbrlData, "inv:TaxpayerIdentificationNumberUnifiedSocialCreditCodeOfPurchaser"));
        invoice.setBuyerAddress(getContent(xbrlData, "inv:AddressPhoneNumberOfPurchaser"));
        invoice.setBuyerAccount(getContent(xbrlData, "inv:DepositBankAndAccountNumberOfPurchaser"));

        // 密码区处理
        String securityCode = getContent(xbrlData, "inv:SecurityCode");
        if (StringUtils.isNotEmpty(securityCode)) {
            // 简单处理密码区，实际可能需要根据格式拆分
            invoice.setPassword1(securityCode);
        }

        // 其他信息映射
        invoice.setMachineCode(getContent(xbrlData, "inv:NumberOfInvoiceMachine"));
        invoice.setPayee(getContent(xbrlData, "inv:Payee"));
        invoice.setChecker(getContent(xbrlData, "inv:Reviewer"));
        invoice.setIssuer(getContent(xbrlData, "inv:Issuer"));
        invoice.setProvince(getContent(xbrlData, "inv:LocationOfInvoice"));

        // 发票类型判断
        String invoiceType = getContent(xbrlData, "inv:TypeOfInvoice");
        String invoiceCode = InvoiceGlorityEnumd.GLORITY_ELECTRONIC_CODE.getCode(); // 默认电子普通发票

        if (StringUtils.isNotEmpty(invoiceType)) {
            if (invoiceType.contains("增值税电子专用发票")) {
                invoiceCode = InvoiceGlorityEnumd.GLORITY_ELECTRON_TAX_SPECIAL_CODE.getCode();
            } else if (invoiceType.contains("增值税专用发票")) {
                invoiceCode = InvoiceGlorityEnumd.GLORITY_TAX_SPECIAL_CODE.getCode();
            } else if (invoiceType.contains("增值税电子普通发票")) {
                invoiceCode = InvoiceGlorityEnumd.GLORITY_ELECTRONIC_CODE.getCode();
            } else if (invoiceType.contains("增值税普通发票")) {
                invoiceCode = InvoiceGlorityEnumd.GLORITY_TAX_CODE.getCode();
            }
        }

        dataImageFilesInfo.setInvoice(invoiceCode);

        // 处理发票明细
        JSONObject detailTuple = xbrlData.getJSONObject("inv:InformationOfTaxableGoodsOrServicesDetailItemsTuple");
        List<DataOcrDetails> ocrDetailsList = new ArrayList<>();

        if (detailTuple != null) {
            DataOcrDetails detail = new DataOcrDetails();
            detail.setFileId(dataImageFilesInfo.getFileId());
            detail.setName(getContent(detailTuple, "inv:NameOfTaxableGoodsOrServices"));
            detail.setPrice(getContent(detailTuple, "inv:UnitPriceOfGoodsOrServices"));
            detail.setDetailAmount(getContent(detailTuple, "inv:AmountExcludingTax"));
            detail.setTaxRate(getContent(detailTuple, "inv:TaxRate"));
            detail.setTax(getContent(detailTuple, "inv:TaxAmount"));
            // 数量和单位信息在示例中没有，暂时设为null
            detail.setDetailsCount(null);
            detail.setUnit(null);
            ocrDetailsList.add(detail);
        }

        invoice.setDetails(ocrDetailsList);

        // 发票待查验
        dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());

        // 添加到结果列表
        resultsList.add(new IdentificationData<>(invoiceCode, invoice, null, "XML发票识别成功"));

        return resultsList;
    }

    /**
     * 从JSON对象中获取指定key的content值
     */
    private String getContent(JSONObject data, String key) {
        if (data == null || !data.containsKey(key)) {
            return null;
        }

        Object value = data.get(key);
        if (value instanceof Map) {
            return ((Map<?, ?>) value).get("content") != null ? ((Map<?, ?>) value).get("content").toString() : null;
        }

        return value != null ? value.toString() : null;
    }
}
