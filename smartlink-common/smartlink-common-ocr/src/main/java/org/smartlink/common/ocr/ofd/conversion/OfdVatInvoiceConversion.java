package org.smartlink.common.ocr.ofd.conversion;

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
 * OFD格式增值税电子专票转换类
 */
public class OfdVatInvoiceConversion implements ChangeIdentifyInfo<JSONObject> {

    private static class LazyHolder {
        private static final OfdVatInvoiceConversion INSTANCE = new OfdVatInvoiceConversion();
    }

    private OfdVatInvoiceConversion() {
    }

    public static OfdVatInvoiceConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject ofdData) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();

        // OFD发票的根节点是eInvoice
        JSONObject eInvoiceData = ofdData.getJSONObject("eInvoice");
        if (eInvoiceData == null) {
            return null;
        }

        DataOcrInfo invoice = new DataOcrInfo();
        invoice.setFileId(dataImageFilesInfo.getFileId());

        // 基本发票信息映射 - OFD格式
        invoice.setInvoiceCode(getContent(eInvoiceData, "fp:InvoiceCode"));
        invoice.setInvoiceNumber(getContent(eInvoiceData, "fp:InvoiceNo"));
        invoice.setInvoiceDate(getContent(eInvoiceData, "fp:IssueDate"));
        invoice.setSumTax(getContent(eInvoiceData, "fp:TaxTotalAmount"));
        invoice.setPretaxAmount(getContent(eInvoiceData, "fp:TaxExclusiveTotalAmount"));
        invoice.setInvoiceTotal(getContent(eInvoiceData, "fp:TaxInclusiveTotalAmount"));
        invoice.setCheckCode(getContent(eInvoiceData, "fp:InvoiceCheckCode"));

        // 销售方信息映射
        JSONObject sellerData = eInvoiceData.getJSONObject("fp:Seller");
        if (sellerData != null) {
            invoice.setSellerName(getContent(sellerData, "fp:SellerName"));
            invoice.setSellerNo(getContent(sellerData, "fp:SellerTaxID"));
            invoice.setSellerAddress(getContent(sellerData, "fp:SellerAddrTel"));
            invoice.setSellerAccount(getContent(sellerData, "fp:SellerFinancialAccount"));
        }

        // 购买方信息映射
        JSONObject buyerData = eInvoiceData.getJSONObject("fp:Buyer");
        if (buyerData != null) {
            invoice.setBuyerName(getContent(buyerData, "fp:BuyerName"));
            invoice.setBuyerNo(getContent(buyerData, "fp:BuyerTaxID"));
            invoice.setBuyerAddress(getContent(buyerData, "fp:BuyerAddrTel"));
            invoice.setBuyerAccount(getContent(buyerData, "fp:BuyerFinancialAccount"));
        }

        // 密码区处理
        String taxControlCode = getContent(eInvoiceData, "fp:TaxControlCode");
        if (StringUtils.isNotEmpty(taxControlCode)) {
            invoice.setPassword1(taxControlCode);
        }

        // 其他信息映射
        invoice.setMachineCode(getContent(eInvoiceData, "fp:MachineNo"));
        invoice.setPayee(getContent(eInvoiceData, "fp:Payee"));
        invoice.setChecker(getContent(eInvoiceData, "fp:Checker"));
        invoice.setIssuer(getContent(eInvoiceData, "fp:InvoiceClerk"));

        // 商品明细处理
        List<DataOcrDetails> detailsList = new ArrayList<>();
        JSONObject goodsInfos = eInvoiceData.getJSONObject("fp:GoodsInfos");
        if (goodsInfos != null) {
            // 处理单个或多个商品明细
            Object goodsInfoObj = goodsInfos.get("fp:GoodsInfo");
            if (goodsInfoObj instanceof JSONObject) {
                // 单个商品明细
                DataOcrDetails detail = processGoodsInfo((JSONObject) goodsInfoObj, invoice.getFileId());
                if (detail != null) {
                    detailsList.add(detail);
                }
            } else if (goodsInfoObj instanceof List) {
                // 多个商品明细
                List<JSONObject> goodsInfoList = (List<JSONObject>) goodsInfoObj;
                for (JSONObject goodsInfo : goodsInfoList) {
                    DataOcrDetails detail = processGoodsInfo(goodsInfo, invoice.getFileId());
                    if (detail != null) {
                        detailsList.add(detail);
                    }
                }
            }
        }

        // 设置查验状态
        dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
        dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_ELECTRON_TAX_SPECIAL_CODE.getCode());
        
        // 添加到结果列表
        resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE, invoice, detailsList, "OFD增值税电子专票识别成功"));

        return resultsList;
    }

    /**
     * 处理商品明细
     */
    private DataOcrDetails processGoodsInfo(JSONObject goodsInfo, String fileId) {
        if (goodsInfo == null) {
            return null;
        }

        DataOcrDetails detail = new DataOcrDetails();
        detail.setFileId(fileId);
        detail.setStandard(getContent(goodsInfo, "fp:Item"));
        detail.setStandard(getContent(goodsInfo, "fp:Specification"));
        detail.setUnit(getContent(goodsInfo, "fp:MeasurementDimension"));
        detail.setPrice(getContent(goodsInfo, "fp:Price"));
        detail.setDetailsCount(getContent(goodsInfo, "fp:Quantity"));
        detail.setDetailAmount(getContent(goodsInfo, "fp:Amount"));
        detail.setTaxRate(getContent(goodsInfo, "fp:TaxScheme"));
        detail.setTax(getContent(goodsInfo, "fp:TaxAmount"));

        return detail;
    }

    /**
     * 获取JSON对象中指定键的值，如果为null则返回空字符串
     */
    private String getContent(JSONObject jsonObject, String key) {
        if (jsonObject == null) {
            return "";
        }
        Object value = jsonObject.get(key);
        return value == null ? "" : value.toString();
    }
}