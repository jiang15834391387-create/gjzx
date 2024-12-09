package org.smartlink.server.nc.ocr.service.ccint.conversion;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.DataOcrDetails;
import org.smartlink.server.nc.domain.invoice.DataOcrInfo;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.ccint.ccintenum.CcintKeyValueEnum;
import org.smartlink.server.nc.ocr.service.ccint.response.ItemListBean;
import org.smartlink.server.nc.ocr.service.ccint.response.ObjectListBean;
import org.smartlink.server.nc.ocr.service.nccbip.BIPCoordinateUtil;
import org.springframework.core.convert.ConversionException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Description:禹水增值税卷票转换为实体类 </p>
 *
 * @date
 **/

public class RollTicketConversion implements ChangeIdentifyInfo<ObjectListBean> {

    //懒加载线程安全
    private static class LazyHolder {
        private static final RollTicketConversion INSTANCE = new RollTicketConversion();
    }

    public static RollTicketConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, ObjectListBean objectListBean) throws ConversionException {
        List<ItemListBean> item_list = objectListBean.getItem_list();
        //置信度
        List<String> confidence = new ArrayList<String>();
        //明细置信度
        List<String> detailsConfidence = new ArrayList<String>();
        List<Integer> position = objectListBean.getPosition();
        DataOcrInfo ocrInfo = new DataOcrInfo();
        ocrInfo.setFileId(dataImageFilesInfo.getFileId());
        ocrInfo.setCoordinate(Convert.toStrArray(position));
        ocrInfo.setOrientation(objectListBean.getImage_angle());

        ocrInfo.setId(IdUtil.fastSimpleUUID());
        Class<?> clazz = DataOcrInfo.class;
        Field[] declaredFields = clazz.getDeclaredFields();
        String fieldName = "";
        Class<?> clazzDetails = DataOcrDetails.class;
        Field[] detailsFields = clazzDetails.getDeclaredFields();
        String detailsFieldName = "";

        for (Field declaredField : declaredFields) {
            fieldName = declaredField.getName();//反射获取私有变量
            for (ItemListBean i : item_list) {
                 if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("invoiceCodeJuan"))) {
                    ocrInfo.setInvoiceCode(i.getValue());
                    if (fieldName.equals("invoiceCode")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("invoiceNumberJuan"))) {
                    ocrInfo.setInvoiceNumber(i.getValue());
                    if (fieldName.equals("invoiceNumber")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("soldNameJuan"))) {
                    ocrInfo.setSellerName(i.getValue());
                    if (fieldName.equals("sellerName")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("soldTaxNumberJuan"))) {
                    ocrInfo.setSellerNo(i.getValue());
                    if (fieldName.equals("sellerNo")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("dateOfInvoiceJuan"))) {
                    ocrInfo.setInvoiceDate(DateUtil.parse(i.getValue()));
                    if (fieldName.equals("invoiceDate")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("cashierNameJuan"))) {
                    ocrInfo.setPayee(i.getValue());
                    if (fieldName.equals("payee")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("buyNameJuan"))) {
                    ocrInfo.setBuyerName(i.getValue());
                    if (fieldName.equals("buyerName")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("buyTaxNumberJuan"))) {
                    ocrInfo.setBuyerNo(i.getValue());
                    if (fieldName.equals("buyerNo")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("totalMoneyJuan"))) {
                    String value = i.getValue();
                    if (value.contains("¥")) {
                        value = value.replace("¥", "");
                    }
                    ocrInfo.setSumAmount(Convert.toBigDecimal(value));
                    if (fieldName.equals("sumAmount")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("totalMoneyBigJuan"))) {
                    ocrInfo.setTotalUppercase(i.getValue());
                    if (fieldName.equals("totalUppercase")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("checkNumberJuan"))) {
                    ocrInfo.setCheckCode(i.getValue());
                    if (fieldName.equals("checkCode")) {
                        confidence.add("\"" + fieldName + "\"" + ":" + i.getConfidence());
                    }
                }
            }
        }
        List<List<ItemListBean>> product_list = objectListBean.getProduct_list();
        List<DataOcrDetails> ocrDetails = new ArrayList<>(16);
        if (CollUtil.isNotEmpty(product_list)) {
            ocrDetails = new ArrayList<>();
            for (int j = 0; j < product_list.size(); j++) {
                List<ItemListBean> itemListBeans = product_list.get(j);
                DataOcrDetails detail = new DataOcrDetails();
                detail.setId(IdUtil.fastSimpleUUID());
                detail.setFileId(ocrInfo.getFileId());
                detail.setDetailNo(String.valueOf(j));

                ocrDetails.add(detail);
                for (ItemListBean i : itemListBeans) {
                    if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("goodsNameListJuan"))) {//货物或服务名称
                        detail.setCommodityName(i.getValue());
                        detail.setName(i.getValue());
                    } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("item_standard"))) {//规格型号
                        detail.setStandard(i.getValue() == null ? "型号" : i.getValue());
                    } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("item_unit"))) {//单位明细
                        detail.setUnit(i.getValue() == null ? "单位" : i.getValue());
                    } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("goodsNumberListJuan"))) {//数量明细  //
                        detail.setDetailsCount(Convert.toBigDecimal(i.getValue(), BigDecimal.ZERO));
                    } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("goodsUnitPriceList"))) {//单价明细
                        detail.setPrice(Convert.toBigDecimal(i.getValue(), BigDecimal.ZERO));
                    } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("goodsMoneyListJuan"))) {//金额明细
                        detail.setDetailAmount(Convert.toBigDecimal(i.getValue(), BigDecimal.ZERO));
                    } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("item_taxRate"))) {//税率明细
                        detail.setTaxRate(i.getValue() == null ? "0%" : i.getValue());
                    } else if (StrUtil.equals(i.getKey(), CcintKeyValueEnum.getCode("item_tax"))) {//税额明细
                        detail.setTax(Convert.toBigDecimal(i.getValue(), BigDecimal.ZERO));
                    }
                }
            }
        }
        ocrInfo.setDetails(ocrDetails);
        String confidenceJson = "{" + String.join(",", confidence) + "}";
        ocrInfo.setConfidence(confidenceJson);
        ocrInfo.setCheckInvoice("0");
        //base64
        ocrInfo.setOption("base64");
        ocrInfo.setBase64(objectListBean.getImage());
        dataImageFilesInfo.setFileType(InvoiceConstants.ROLL_TICKET);
        ocrInfo.setCoordinateStr(BIPCoordinateUtil.getCciCoordinateStr(Convert.toIntArray(objectListBean.getPosition())));
        return new IdentificationData<>(dataImageFilesInfo.getFileType(), ocrInfo);
    }
}
